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

import com.fastcgi.FCGIInterface;
import org.coach.tracing.api.*;
import java.io.*;
import java.util.*;

public class ServerMain
{
	private Queue 			    queue = new Queue();
    private EventDataBase 	    db;
    private org.omg.CORBA.ORB   orb = null;

	public ServerMain ()
	{
    	db = EventDataBase.getEventDB();

        /*if (System.getProperty("coach.tracing.viewer", "false").equals("true"))
        {
            // Show the internal tracing viewer

            org.coach.tracing.server.viewer.TraceViewerFrame viewer = new org.coach.tracing.server.viewer.TraceViewerFrame();

            viewer.setEventDB(db);

            viewer.show();
        }*/
	}

    public class Queue
    {
        private LinkedList list = new LinkedList();

        public synchronized void add(TraceEvent[] events)
        {
            list.add(events);

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
            return (TraceEvent[])list.removeFirst();
        }
    }

    public class EventProcessor extends Thread
    {
        public void run()
        {
            while(true)
            {
                db.addEvents(queue.take());
            }
        }
    }

    public class OrbMain extends Thread
    {
    	private org.omg.CORBA.ORB orb;

    	public OrbMain (org.omg.CORBA.ORB orb)
    	{
    		this.orb = orb;
    	}

        public void run()
        {
			orb.run();
        }
    }

    /**
     * This routine registers the i_Facet interface at the Name Service and starts all processing
     *
     * @param args The command line arguments.
     */
	void init (String args[])
	{
//		org.omg.CORBA.ORB orb = null;

		try
		{
			Properties properties = new Properties();

			properties.setProperty("org.omg.CORBA.ORBClass", "org.openorb.CORBA.ORB");
			properties.setProperty("org.omg.CORBA.ORBSingletonClass", "org.openorb.CORBA.ORBSingleton");

           	if ( args.length > 0 )
           	{
            	properties.setProperty("ORBInitRef.NameService", args[0]);

        		Vector vector = new Vector();

        		vector.addElement("-ORBInitRef");
        		vector.addElement("NameService=" + args[0]);

      			String[] s = new String[vector.size()];

        		vector.copyInto(s);

        		orb = org.omg.CORBA.ORB.init(s, properties);
			}
			else
				orb = org.omg.CORBA.ORB.init(args, properties);

            try
            {
                /* Need to set the orb in case we use the IDL Tree in a stand-alone environment (not openccm) */

                org.coach.idltree.IdlNode.setOrb (orb);
                org.coach.idltree.XmlNode.setOrb (orb);
            }
            catch (Error _ex)
            {
            }

  			org.omg.CORBA.Object obj = orb.resolve_initial_references( "RootPOA" );

			org.omg.PortableServer.POA rootPOA = org.omg.PortableServer.POAHelper.narrow(obj);

            org.omg.CORBA.Policy [] policies = new org.omg.CORBA.Policy[ 3 ];

            policies[0] = rootPOA.create_id_uniqueness_policy(org.omg.PortableServer.IdUniquenessPolicyValue.MULTIPLE_ID );
            policies[1] = rootPOA.create_request_processing_policy(org.omg.PortableServer.RequestProcessingPolicyValue.USE_DEFAULT_SERVANT);
            policies[2] = rootPOA.create_id_assignment_policy(org.omg.PortableServer.IdAssignmentPolicyValue.USER_ID );

            org.omg.PortableServer.POA childPOA = rootPOA.create_POA("_POA", rootPOA.the_POAManager(), policies );

			i_TraceImpl impl = new i_TraceImpl(queue);

            byte[] servantId = (new String("i_Trace")).getBytes();

			childPOA.activate_object_with_id (servantId, impl);

           	if ( args.length > 0 )
           	{
				obj = orb.resolve_initial_references("NameService");

				org.omg.CosNaming.NamingContext nc = org.omg.CosNaming.NamingContextHelper.narrow(obj);

				obj = childPOA.id_to_reference (servantId);

				org.omg.CosNaming.NameComponent[] ncomp = new org.omg.CosNaming.NameComponent[1];

				ncomp[0] = new org.omg.CosNaming.NameComponent("TracingServer", "");

				nc.rebind(ncomp, obj);
			}
			else
			{
				// write IOR to file

				obj = childPOA.id_to_reference(servantId);

            	String reference = orb.object_to_string(obj);

	            try
	            {
	                java.io.FileOutputStream file = new java.io.FileOutputStream("ior" );
	                java.io.PrintStream pfile = new java.io.PrintStream( file );
	                pfile.println( reference );
	                file.close();
	            }
	            catch ( java.io.IOException ex )
	            {
	                System.out.println( "File error" );
	            }
			}

			rootPOA.the_POAManager().activate();

			new EventProcessor().start();
			new OrbMain(orb).start();
		}
		catch (Exception _ex)
		{
			_ex.printStackTrace();

			System.exit(-1);
		}
	}

    /**
     * The main method. This method initializes the server and handles the web server FCGI requests.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args)
    {
        ServerMain application = new ServerMain();

        application.init(args);

		while(new FCGIInterface().FCGIaccept()>= 0)
		{
			application.handleFCGIRequest (System.out, System.getProperty ("QUERY_STRING"));
		}
    }

	void handleFCGIRequest (java.io.OutputStream os, String query)
	{
		// create output print stream

		PrintStream out = new PrintStream (os, true);

		// set content type

		out.println ("Content-type: text/plain\r\n");

		try
		{
			// parse query string

			Hashtable pairs = parseQueryString (query);

			// the event_count request is the only request not returning XML

			if ( !pairs.containsKey("event_count") )
				System.out.println ("<?xml version='1.0' encoding='UTF-8'?>");

			// handle requests

			DbToXml dbc = new DbToXml (db);

			if (pairs.containsKey("entities"))
			{
				StringTokenizer tokenizer = new StringTokenizer((String)pairs.get("keys"));

      			long keys[] = new long[tokenizer.countTokens()];

				int index = 0;

				while (tokenizer.hasMoreTokens())
      			{
					keys[index] = (new Long(tokenizer.nextToken())).longValue();

					index++;
				}

				String result = dbc.getXmlIdentities(keys);

				System.out.println (result);
			}
			else if (pairs.containsKey("event_count"))
			{
				String result = new Long (db.getEventCount()).toString();

				System.out.println (result);
			}
			else if (pairs.containsKey("events") )
			{
				long start 	= Long.parseLong((String)pairs.get ("start"));
				int  len 	= Integer.parseInt((String)pairs.get ("length"));

				StringTokenizer tokenizer = new StringTokenizer((String)pairs.get("operation_filter"));
				Hashtable operation_filter = new Hashtable();

				while (tokenizer.hasMoreTokens())
					operation_filter.put (tokenizer.nextToken(),"");

				tokenizer  = new StringTokenizer((String)pairs.get("entity_filter"));
				Hashtable entity_filter = new Hashtable();

				while (tokenizer.hasMoreTokens())
					entity_filter.put(tokenizer.nextToken(),"");

				tokenizer  = new StringTokenizer((String)pairs.get("toself_filter"));
				Hashtable toself_filter = new Hashtable();

				while (tokenizer.hasMoreTokens())
				{
					StringTokenizer collapsed_node = new StringTokenizer(tokenizer.nextToken(), ":");

					String key 		= new String();
					String value 	= new String();

					if (collapsed_node.hasMoreTokens())
		            	key = collapsed_node.nextToken();
					else;

		          	if (collapsed_node.hasMoreTokens())
		            	value = collapsed_node.nextToken();
		          	else;

					toself_filter.put(key,value);
				}

				String  filter_unmatched = (String)pairs.get ("unmatched_filter");
                long    hide_to 	= Long.parseLong((String)pairs.get ("hide_to"));

				String result = dbc.getXmlEvents(start,len, operation_filter, entity_filter, toself_filter, filter_unmatched, hide_to);

				System.out.println (result);
			}
			else if (pairs.containsKey("parameters") )
			{
				String result = dbc.getXmlParameterValues(Long.parseLong((String)pairs.get("key")));

				System.out.println (result);
			}

			out.close();
		}
		catch (Exception _ex)
		{
			// print exception message

			System.err.println (_ex.getMessage());
		}
	}

	String urlDecode(String in)
	{
		StringBuffer out = new StringBuffer(in.length());
		int i = 0;
		int j = 0;

		while (i < in.length())
		{
			char ch = in.charAt(i);
			i++;

			if (ch == '+')
				ch = ' ';
			else if (ch == '%')
			{
				ch = (char)Integer.parseInt(in.substring(i,i+2), 16);
				i+=2;
			}
			out.append(ch);
			j++;
		}

		return new String(out);
	}

	Hashtable parseQueryString(String s)
	{
		Hashtable pairs = new Hashtable();

		StringTokenizer pair_tokenizer = new StringTokenizer(s, "&");

      	while (pair_tokenizer.hasMoreTokens())
      	{
          	String pair = urlDecode(pair_tokenizer.nextToken());

			StringTokenizer keyval_tokenizer = new StringTokenizer(pair, "=");

			String key 		= new String();
			String value 	= new String();

			if (keyval_tokenizer.hasMoreTokens())
            	key = keyval_tokenizer.nextToken();
			else;

          	if (keyval_tokenizer.hasMoreTokens())
            	value = keyval_tokenizer.nextToken();
          	else;

			pairs.put(key,value);
		}

		return pairs;
	}
}

