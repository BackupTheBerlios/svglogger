/***************************************************************************/
/* COACH: Component Based Open Source Architecture for                     */
/*        Distributed Telecom Applications                                 */
/* See:   http://www.objectweb.org/                                        */
/*                                                                         */
/* Copyright (C) 2003 Lucent Technologies Nederland BV                     */
/*                    Bell Labs Advanced Technologies - EMEA               */
/*                                                                         */
/* Initial developer(s): Harold Batteram                                   */
/*                                                                         */
/* This library is free software; you can redistribute it and/or           */
/* modify it under the terms of the GNU Lesser General Public              */
/* License as published by the Free Software Foundation; either            */
/* version 2.1 of the License, or (at your option) any later version.      */
/*                                                                         */
/* This library is distributed in the hope that it will be useful,         */
/* but WITHOUT ANY WARRANTY; without even the implied warranty of          */
/* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU        */
/* Lesser General Public License for more details.                         */
/*                                                                         */
/* You should have received a copy of the GNU Lesser General Public        */
/* License along with this library; if not, write to the Free Software     */
/* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA */
/***************************************************************************/
package org.coach.tracing.service;

import java.net.*;
import java.io.*;
import java.util.*;
import org.coach.tracing.service.ntp.*;
import org.coach.tracing.api.*;
import org.omg.CORBA.*;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;

public class Sender
{
    private static String[] excludeTable;
    private i_Trace traceServer = null;
    public String name = "";
    private ORB orb = null;
    private boolean initialized = false;
    private static boolean initializing = false;
    private static String hostName;
    private static String hostAddress;
    private Queue sendQueue = new Queue();
    private NamingContextExt root_nc = null;
    private NameComponent[] traceServerPath;

    private Parameter[] exceptionParameter = new Parameter[] {new Parameter("exception", "exception", "exception")};

    private static Sender sender; // singleton

    public static Sender createSender(ORB orb)
    {
        if (sender == null)
        {
            initializing = true;
            System.err.println("Create Sender");
            sender = new Sender(orb);
        }
        return sender;
    }

    protected Sender(ORB orb)
    {
        try
        {
            String traceServerName = "COACH_TracingServer";
            traceServerPath = new NameComponent[] {new NameComponent(traceServerName, "")};
            hostName = InetAddress.getLocalHost().getHostName();
            hostAddress = InetAddress.getLocalHost().getHostAddress();
            root_nc = null;
            try
            {
                // first try to resolve the naming context using resolve_initial_references
                root_nc = NamingContextExtHelper.narrow(orb.resolve_initial_references("NameService"));
            }
            catch (Exception e1)
            {
                System.err.println("No nameserver found");
            }

            try
            {
                org.omg.CORBA.Object obj = root_nc.resolve(traceServerPath);
                traceServer = i_TraceHelper.narrow(obj);
            }
            catch (Exception e)
            {
                System.err.println("No trace server found: " + traceServerName + " error: " + e.toString());
            }
            this.orb = orb;
            if (traceServer != null)
            {
                SendThread st = new SendThread();
                st.setPriority(Thread.currentThread().getPriority() - 1);
                st.start();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static boolean isInitializing()
    {
        return initializing && sender == null;
    }

    public static boolean exclude(String id)
    {
        if (excludeTable == null)
        {
            // initialize the exclude table
            Vector v = new Vector();
            String excludes = System.getProperty("coach.tracing.exclude");
            if (excludes != null && !excludes.equals(""))
            {
                StringTokenizer st = new StringTokenizer(excludes, ", ");
                while (st.hasMoreTokens())
                {
                    String t = st.nextToken();
                    if (t.endsWith("*"))
                    {
                        t = t.substring(0, t.lastIndexOf("*"));
                    }
                    v.add(t);
                }
            }
            excludeTable = new String[v.size()];
            v.toArray(excludeTable);
        }
        
        for (int i = 0; i < excludeTable.length; i++)
        {
            if (id.startsWith(excludeTable[i]))
            {
                return true;
            }
        }
        return false;
    }

    protected TraceEvent createEvent(ThreadContext tc)
    {
        TraceEvent traceEvent = new TraceEvent();
        try
        {
            traceEvent.identity = new IdentityDescriptor();
            traceEvent.identity.object_instance_id = tc.getObjectInstanceId();
            traceEvent.identity.object_repository_id = tc.getObjectRepositoryId();
            traceEvent.identity.process_id = tc.getVmId();
            traceEvent.identity.node_name = hostName;
            traceEvent.identity.node_ip = hostAddress;
            traceEvent.identity.cmp_name = tc.getComponentName();
            traceEvent.identity.cmp_type = tc.getComponentType();
            traceEvent.identity.cnt_name = tc.getContainerName();
            traceEvent.identity.cnt_type = tc.getContainerType();
            traceEvent.trail_label = tc.getTrailLabel();
            traceEvent.trail_id = tc.getTrailId();
            traceEvent.event_counter = tc.incrementEventCounter();
            traceEvent.time_stamp = PhysicalTime.getInstance().getTime();
            traceEvent.thread_id = tc.getThreadId();
            traceEvent.op_name = "";
            traceEvent.parameters = new Parameter[0];
            traceEvent.parameter_values = new Any[0];
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return traceEvent;
    }

    public void stubPreInvoke(ThreadContext tc, String op_name, Parameter[] parameters, Any[] parameter_values, boolean isOneway)
    {
        try
        {
            //generate the event generic part
            TraceEvent event = createEvent(tc);
            // Message Id is set only on stub pre invoke.
            tc.setMessageId(tc.getVmId() + "_" + tc.getEventCounter());
            event.message_id = tc.getMessageId() + "F";

            //Add the interaction specific fields
            if (isOneway)
            {
                event.interaction_point = InteractionPoint.ONEWAY_STUB_OUT;
            }
            else
            {
                event.interaction_point = InteractionPoint.STUB_OUT;
            }
            event.op_name = op_name;
            if (parameter_values != null)
            {
                event.parameters = parameters;
                event.parameter_values = parameter_values;
            }              
            send(event);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
       
    public void poaPreInvoke(ThreadContext tc, String op_name, Parameter[] parameters, Any[] parameter_values, boolean isOneway)
    {
        try
        {      
            //generate the event generic part
            TraceEvent event = createEvent(tc);
            event.message_id = tc.getMessageId() + "F";
            //Add the interaction specific fields
            if (isOneway)
            {
                event.interaction_point = InteractionPoint.ONEWAY_POA_IN;
            }
            else
            {
                event.interaction_point = InteractionPoint.POA_IN;
            }
            event.op_name = op_name;
            if (parameter_values != null)
            {
                event.parameters = parameters;
                event.parameter_values = parameter_values;
            }
            send(event);
        }
        catch (Exception e)
        {
            //problems...
            e.printStackTrace();
        }
    }

    public void poaPostInvoke(ThreadContext tc, String op_name, Parameter[] parameters, Any[] parameter_values)
    {
        try
        {
            TraceEvent event = createEvent(tc);
            event.op_name = op_name;
            event.message_id = tc.getMessageId() + "B";
            if (parameter_values != null)
            {
                event.parameters = parameters;
                event.parameter_values = parameter_values;
            }
            
            //Add the interaction specific fields
            event.interaction_point = InteractionPoint.POA_OUT;

            send(event);
        }
        catch (Exception e)
        {
            //problems...
            e.printStackTrace();
        }
    }

    public void poaPostException(ThreadContext tc, String op_name, Any exception_value)
    {
        try 
        {
            //generate the event generic part
            TraceEvent event = createEvent(tc);
            event.op_name = op_name;
            event.parameters = exceptionParameter;
            event.parameter_values = new Any[] {exception_value};
            event.message_id = tc.getMessageId() + "B";
            
            //Add the interaction specific fields
            event.interaction_point = InteractionPoint.POA_OUT_EXCEPTION;

            send(event);
        }
        catch (Exception e)
        {
            //problems...
            e.printStackTrace();
        }
    }

    public void stubPostInvoke(ThreadContext tc, String op_name, Parameter[] parameters, Any[] parameter_values, boolean isExceptional, boolean isOneway)
    {
        if (!isOneway)
        {
            //the previous call to the notification handler in the stub has done the filtering, if any
            try
            {                               
                //generate the event generic part
                TraceEvent event = createEvent(tc);
                event.message_id = tc.getMessageId() + "B";
                event.op_name = op_name;
                if (parameter_values != null)
                {
                    event.parameters = parameters;
                    event.parameter_values = parameter_values;
                }
                
                //Add the interaction specific fields
                event.interaction_point = InteractionPoint.STUB_IN;
            
                send(event);
            }
            catch (Exception e)
            {
                //problems...
                e.printStackTrace();
            }
        }
    }
    
    public void stubPostException(ThreadContext tc, String op_name, Any exception_value)
    {
        //the previous call to the notification handler in the stub has done the filtering, if any
        try
        {            
            //generate the event generic part
            TraceEvent event = createEvent(tc);
            event.message_id = tc.getMessageId() + "B";
            event.op_name = op_name;
            event.parameters = exceptionParameter;
            event.parameter_values = new Any[] {exception_value};
            
            //Add the interaction specific fields
            event.interaction_point = InteractionPoint.STUB_IN_EXCEPTION;

            send(event);
        }
        catch (Exception e)
        {
            //problems...
            e.printStackTrace();
        }
    }

    private void send(TraceEvent event)
    {
        if (traceServer == null)
        {
            // Check for the monitor, it may have been started after this container.
            try
            {
                traceServer = i_TraceHelper.narrow(root_nc.resolve(traceServerPath));
            }
            catch (Exception e)
            {
            }
        }
        if (traceServer != null)
        {
            sendQueue.add(event);
//            TraceEvent[] events = new TraceEvent[1];
//            events[0] = event;
//               
//            traceServer.receiveEvent(events);
        }
    }

   private void printEvent(TraceEvent event)
   {
        System.err.println("*******************");
        System.err.println("object_instance_id:         " + event.identity.object_instance_id);
        System.err.println("object_repository_id:       " + event.identity.object_repository_id);
        System.err.println("interaction_point:          " + event.interaction_point.value());
        System.err.println("process_id:                 " + event.identity.process_id);
        System.err.println("node_name:                  " + event.identity.node_name);
        System.err.println("node_ip:                    " + event.identity.node_ip);
        System.err.println("trail_id:                   " + event.trail_id);
        System.err.println("event_counter:              " + event.event_counter);
        System.err.println("op_name:                    " + event.op_name);
        System.err.println("cmp_name:                   " + event.identity.cmp_name);
        System.err.println("cmp_type:                   " + event.identity.cmp_type);
        System.err.println("cnt_name:                   " + event.identity.cnt_name);
        System.err.println("cnt_type:                   " + event.identity.cnt_type);
        System.err.println("time_stamp:                 " + event.time_stamp);
        System.err.println("thread_id:                  " + event.thread_id);
        System.err.println("message_id:                 " + event.message_id);
        System.err.println("trail_label:                " + event.trail_label);
        System.err.println("*******************");
   }
   
    private String getNamingIor(String namingserver)
    {
        String ior = null;
        try
        {
            java.net.URL u = null;
            try
            {
                // check for a normal URL
                u = new java.net.URL(namingserver);
            }
            catch (java.net.MalformedURLException e1)
            {
    
                try
                {
                    // our last chance is that it's a plain old style hostname
                    u = new java.net.URL("http://" + namingserver + "/etc/naming.ior");
                }
                catch (java.net.MalformedURLException e2)
                {
                }
            }
            BufferedReader in = null;
            try
            {
                in = new BufferedReader(new InputStreamReader(u.openStream()));
                ior = in.readLine();
            }
            catch (Exception e1)
            {
                System.out.println("Failed to find name server!");
                return null;
            }
    
            while (ior != null && !ior.startsWith("IOR:"))
            {
                ior = in.readLine();
            }
            in.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return ior;
    }

    public class Queue
    {
        private LinkedList list = new LinkedList();
        
        public synchronized void add(TraceEvent event)
        {
//printEvent(event);
            list.add(event);
            notifyAll();
        }
        
        public synchronized TraceEvent[] take()
        {
            if (list.size() == 0)
            {
                try
                {
                    wait();
                }
                catch (Exception e)
                {
                }
            }
            TraceEvent[] all = new TraceEvent[list.size()];
            list.toArray(all);
            list.clear();
            return all;
        }
    }
  
    public class SendThread extends Thread
    {
        Random random = new Random();
        
        public void run()
        {
            while(true)
            {
                try
                {                   
                    TraceEvent[] events = sendQueue.take();
                    traceServer.receiveEvent(events);
                }
                catch (Exception e)
                {
                }
            }
        }
    }
}