/***************************************************************************/
/* Copyright (C) 2003 Lucent Technologies Nederland BV                     */
/*                    Bell Labs Advanced Technologies - EMEA               */
/*                                                                         */
/* Initial developer(s): Wim Hellenthal                                    */
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

package org.coach.tracing.server;

import org.coach.tracing.api.*;
import org.omg.CORBA.*;
import java.io.*;
import java.util.*;
import java.net.*;

public class ServerTest
{
	private i_Trace connection;
	private org.omg.CORBA.ORB orb;
	// process specific event counter
	private	int event_counter 		= 0;
	private String appl_id;

	public ServerTest ()
	{
	}

    /**
     * This routine initializes the ORB and resolves the i_Trace interface using the Name Service
     *
     * @param args The command line arguments.
     */
	void init (String args[])
	{
		org.omg.CORBA.Object obj = null;

		try
		{
			Properties properties = new Properties();

			properties.setProperty("org.omg.CORBA.ORBClass", "org.openorb.CORBA.ORB");
			properties.setProperty("org.omg.CORBA.ORBSingletonClass", "org.openorb.CORBA.ORBSingleton");

     		if ( args[0].startsWith ("corbaloc") )
     		{
	            properties.setProperty("ORBInitRef.NameService", args[0]);

	        	Vector vector = new Vector();

	        	vector.addElement("-ORBInitRef");
	        	vector.addElement("NameService=" + args[0]);

	        	String[] s = new String[vector.size()];

	        	vector.copyInto(s);

	   		 	orb = org.omg.CORBA.ORB.init(s, properties);

				obj = orb.resolve_initial_references("NameService");

				org.omg.CosNaming.NamingContext nc = org.omg.CosNaming.NamingContextHelper.narrow(obj);

				org.omg.CosNaming.NameComponent[] ncomp = new org.omg.CosNaming.NameComponent[1];

				ncomp[0] = new org.omg.CosNaming.NameComponent("TracingServer", "");

				obj = nc.resolve(ncomp);
	   		}
	   		else
	   		{
	   			orb = org.omg.CORBA.ORB.init(args, properties);

	   			// read ior from file

		        try
		        {
		            java.io.FileInputStream file = new java.io.FileInputStream( "ior" );
		            java.io.BufferedReader myInput = new java.io.BufferedReader(new java.io.InputStreamReader( file ));

		            String stringTarget = myInput.readLine();

		            obj = orb.string_to_object( stringTarget );

		        }
		        catch ( java.io.IOException ex )
		        {
		            System.out.println( "File error" );
		            System.exit( 0 );
		        }
	   		}

			connection = i_TraceHelper.narrow (obj);

			java.io.File tmp = java.io.File.createTempFile("tmp", "");

            tmp.deleteOnExit();

            appl_id = tmp.getName().substring(3);
		}
		catch (Exception _ex)
		{
			_ex.printStackTrace();

			System.exit(-1);
		}
	}

    /**
     * The main method. This method initializes the server and creates some test events.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args)
    {
        ServerTest application = new ServerTest();

        application.init(args);

		// start sending events to server

		// start sending events to tracing server

		if ( args.length <= 1 )
			application.send_invocations(1);
		else
			application.send_invocations(Integer.parseInt(args[1]));
	}

	public void send_invocations(int cnt)
	{
		IdentityDescriptor cmp1 = createObject (appl_id, "Container_A", "Component_1", "Hello_A");
		IdentityDescriptor cmp2 = createObject (appl_id, "Container_A", "Component_2", "Hello_B");
		IdentityDescriptor cmp3 = createObject (appl_id, "Container_B", "Component_1", "Hello_C");
		IdentityDescriptor cmp4 = createObject (appl_id, "Container_C", "Component_1", "Hello_D");

		// generating some invocations

		Parameter[] parameter = new Parameter[] {
			new Parameter("in", "string", "v1")
		};

        Any[] value = new Any[1];

        value[0] = orb.create_any();
        value[0].insert_string("Hello World");

		System.out.println ("Coach Tracing Server Test, n = " + cnt);

		for (int i = 0; i < cnt; i++)
		{
			invocation ("hello_world",  cmp1, cmp2, parameter, value, true); // oneway invocation
			invocation ("hi_there",     cmp2, cmp3, parameter, value, false);
			invocation ("how_are_you",  cmp3, cmp4, parameter, value, false);
			invocation ("I'm_fine",     cmp3, cmp2, parameter, value, false);
			invocation ("thank_you",    cmp2, cmp1, parameter, value, false);
		}
	}

	public void invocation (String operation_name,
							IdentityDescriptor from,
							IdentityDescriptor to,
							Parameter[] parameters,
							Any[] values,
							boolean oneway)
	{
		/* thread of execution for a given interaction point. This must be a unique id meaning that you must be able
		 * to discriminate between threads with the same id running on two different machines for example */

		String exec_thread = "";

		try
		{
			exec_thread = Thread.currentThread().getName() + "_" + InetAddress.getLocalHost().getHostName();
		}
		catch (Exception _ex)
		{
			_ex.printStackTrace();
		}


		/* trail id - every trail (sequence of causaly related events) has a starting point identified by the trail id. */
		String trail_id = new Integer (event_counter).toString();
        /* message id - each invocation has a unique message id to associate the invoker with the receiver. */
        String message_id = trail_id + "_" + event_counter + "_";
		// create event for STUB_OUT

        /*
         * The traceserver orders events per process using the process event counter.
         * Events with the same message id are associated with each other and form a message between invoker and receiver.
         * The trace server calculates the relative ordering of events using the message id, event counter and trail id.
         * All events with the same trail id are causaly related to each other.
         * The traceserver maintains a total ordering of events using the exact relative ordering of causaly related events and
         * physical timestamp comparison for causaly unrelated event.
         */
		TraceEvent stub_out_event = new TraceEvent (
			System.currentTimeMillis(),					/* physical time */
			oneway ? InteractionPoint.ONEWAY_STUB_OUT :
				     InteractionPoint.STUB_OUT,  	    /* interaction point */
			"",											/* trail label - user defined label to mark a trail segment,
			                                             * not used by the trace viewer yet (giving trail segments a
			                                             * different color for example */
            message_id + "F",                           /* The message id to associate the originator with the receiver of the invocation */
			exec_thread,								/* thread of execution */
			trail_id,									/* trail id */
			event_counter++,							/* global event counter per process, incremented on every send and receive event */
			operation_name,								/* operation name */
			from,										/* object that initiated the invocation	*/
			parameters,									/* parameters (dir, type, name)	*/
			values);									/* parameter values	*/

		// create event for POA_IN

		TraceEvent poa_in_event = new TraceEvent (
			System.currentTimeMillis(),					/* physical time */
			oneway ? InteractionPoint.ONEWAY_POA_IN :
				     InteractionPoint.POA_IN,  	        /* interaction point */
			"",											/* trail label - user defined label to mark a trail segment,
			                                             * not used by the trace viewer yet (giving trail segments a
			                                             * different color for example */
            message_id + "F",                           /* The message id to associate the originator with the receiver of the invocation */
			exec_thread,	        					/* simulating this is a collocated call (same thread of execution
														 * as the stub */
			trail_id,									/* trail id	*/
			event_counter++,							/* logical clock incremented for every interaction point along the trail*/
			operation_name,								/* operation name */
			to,										    /* object that receives invocation */
			parameters,									/* parameters (dir, type, name)*/
			values);									/* parameter values	*/

		if ( oneway )
		{
			// a oneway invocation contains two event - ONEWAY_STUB_OUT and ONEWAY_POA_IN

			TraceEvent[] events = new TraceEvent[2];

            events[0] = stub_out_event;
            events[1] = poa_in_event;

            connection.receiveEvent(events);

			return;
		}

		// create event for POA_OUT

		TraceEvent poa_out_event = new TraceEvent (
			System.currentTimeMillis(),					/* physical time */
			InteractionPoint.POA_OUT,  	                /* interaction point */
			"",											/* trail label - user defined label to mark a trail segment,
			                                             * not used by the trace viewer yet (giving trail segments a
			                                             * different color for example */
            message_id + "B",                           /* The message id to associate the originator with the receiver of the invocation */
			exec_thread,	        					/* simulating this is a collocated call (same thread of execution
														 * as the stub */
			trail_id,									/* trail id	*/
			event_counter++,							/* logical clock incremented for every interaction point along the trail*/
			operation_name,								/* operation name */
			to,										    /* object that receives invocation */
			parameters,									/* parameters (dir, type, name)*/
			values);									/* parameter values	*/

		// create event for STUB_IN

		TraceEvent stub_in_event = new TraceEvent (
			System.currentTimeMillis(),					/* physical time */
			InteractionPoint.STUB_IN,  	                /* interaction point */
			"",											/* trail label - user defined label to mark a trail segment,
			                                             * not used by the trace viewer yet (giving trail segments a
			                                             * different color for example */
            message_id + "B",                           /* The message id to associate the originator with the receiver of the invocation */
			exec_thread,	        					/* simulating this is a collocated call (same thread of execution
														 * as the skeleton */
			trail_id,									/* trail id	*/
			event_counter++,							/* logical clock incremented for every interaction point along the trail*/
			operation_name,								/* operation name */
			from,										/* object that receives invocation */
			parameters,									/* parameters (dir, type, name)*/
			values);									/* parameter values	*/

		TraceEvent[] events = new TraceEvent[4];

        events[0] = stub_out_event;
        events[1] = poa_in_event;
        events[2] = poa_out_event;
        events[3] = stub_in_event;

        connection.receiveEvent(events);
	}


    public IdentityDescriptor createObject(	String process_id,
    										String container_name,
    										String component_name,
    										String object_name)
    {
	    // used to describe a sending or receiving entity such as a facet instance, component instance etc.

    	IdentityDescriptor identity = new IdentityDescriptor();

    	try
    	{
			identity.process_id 			= process_id;
			identity.node_name 				= InetAddress.getLocalHost().getHostName();
			identity.node_ip 				= InetAddress.getLocalHost().getHostAddress();
			identity.object_instance_id 	= object_name;
			identity.object_repository_id 	= "facet";
			identity.cmp_name 				= component_name;
			identity.cmp_type 				= "component";
			identity.cnt_name 				= container_name;
			identity.cnt_type 				= "container";
		}
		catch (Exception _ex)
		{
			_ex.printStackTrace();
		}

		return identity;
   	}
}

