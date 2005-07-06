/***************************************************************************/
/* COACH: Component Based Open Source Architecture for                     */
/*        Distributed Telecom Applications                                 */
/* See:   http://www.objectweb.org/                                        */
/*                                                                         */
/* Copyright (C) 2003 Lucent Technologies Nederland BV                     */
/*                    Bell Labs Advanced Technologies - EMEA               */
/*                                                                         */
/* Initial developer(s): Wim Hellenthal                                   */
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

import java.util.*;
import org.coach.idltree.*;
import org.coach.tracing.api.*;

public class DbToXml
{
    private EventDataBase db;

    public DbToXml(EventDataBase db)
    {
    	this.db  = db;
    }

    public String getXmlEventAt(long index)
    {
        EventRecord tr = null;

		tr = db.getEventAt(index);

		return tr.toXml(db);
    }

    public String getXmlEvent(long key_id)
    {
        EventRecord tr = null;

        tr = db.readEvent(key_id);

        return tr.toXml(db);
    }

    public String getXmlEvents(long index, int len, Hashtable operation_filter, Hashtable entity_filter, Hashtable toself_filter, String filter_unmatched, long hide_to)
    {
		EventRecord[]  er 	    = null;
		EventRecord    ser      = null;
		StringBuffer   sb 	    = new StringBuffer();
		int			   cnt		= 0;
		int 		   visibles = 0;
		long           offset   = 0;

       	sb.append ("<trace_events>\n");

        // pretend we cleared the database up to index 'hide_to'

        index += hide_to;

       	er = db.getEvents(index, index + len);

 		while ( visibles < len )
		{
    	   	try
    		{
    		    if ( cnt == len )
    		    {
    		       index += len;

    		       er = db.getEvents(index, index + len);

    		       cnt = 0;
    		    }

    		    ser = er[cnt++];

    			long link_key = ser.getTraceRecord().linkKey;

                // filter on unmatched

                if ( filter_unmatched.equals("false") || link_key != -1 )
                {
				    // filter on operation name

    				if (!operation_filter.containsKey (ser.getTraceRecord().op_name) )
    				{
    					// filter on entity (including peer)

    					String identity_key = Long.toString(ser.getTraceRecord().identityKey);

                        EventRecord peer = null;

    					if ( link_key != -1 )
    					{
    						peer = db.readEvent(link_key);

                            String peer_identity_key = Long.toString(peer.getTraceRecord().identityKey);

    						if ( !entity_filter.containsKey(identity_key) && !entity_filter.containsKey(peer_identity_key) )
    						{
    							// filter on operations to self

                                if ( toself_filter.containsKey (identity_key) && toself_filter.containsKey(peer_identity_key) )
                                {
                                    /* check if they have the same collapsed node as ancestor meaning that both events are positioned on
                                     * the same entity/identity line
                                     */

                                    if ( !((String)toself_filter.get(identity_key)).equals((String)toself_filter.get(peer_identity_key)) )
                                    {
                                        sb.append (ser.toXml(db));
                                        visibles++;

                                        if ( visibles == 1 )
                                            offset = ser.getCurrentIndex();
                                    }
                                }
                                else
                                {
                                    sb.append (ser.toXml(db));
                                    visibles++;

                                    if ( visibles == 1 )
                                        offset = ser.getCurrentIndex();
                                }
    			            }
    			        }
    			        else
    			        {
    			            // unmatched event for specified entity (no unmatched event filtering)

    						if ( !entity_filter.containsKey(identity_key) )
    						{
    			        		sb.append (ser.toXml(db));

    			        		visibles++;

                                if ( visibles == 1 )
                                    offset = ser.getCurrentIndex();
    			        	}
    			        }
    	        	}
     	        }
        	}
			catch (Exception _ex)
			{
        		break;
        	}
        }

        sb.append ("<count>" + Long.toString(db.getEventCount() - hide_to ) + "</count>\n");
        sb.append ("<offset>" + Long.toString(offset) + "</offset>\n");

		sb.append ("</trace_events>\n");

		return sb.toString();
 	}

	public String getXmlIdentities(long[] keys) throws org.coach.tracing.api.InvalidKey
	{
		StringBuffer sb = new StringBuffer();

       	sb.append ("<trace_entities>\n");

		for (int i = 0; i < keys.length; i++)
		{
			try
			{
				sb.append ("<entity id='" + keys[i] + "'>\n");
      			sb.append ("  <identityHierarchy>\n");

				Identity id = null;

				long identityKey = keys[i];

       			while(identityKey != -1)
       			{
            		try
            		{
                		id = db.getIdentity(new Long(identityKey));

						if ( id == null )
							break;

						sb.append ("    <" + EventRecord.getIdentityKindName(id.kind) + " name='" + id.name + "' type='" + id.type +  "'/>\n");
            		}
            		catch (Exception _ex)
            		{
                		break;
            		}

            		identityKey = id.linkKey;
        		}

 		        sb.append ("  </identityHierarchy>\n");

				sb.append ("</entity>\n");
			}
			catch (Exception _ex)
			{
            	break;
        	}
		}

		sb.append ("</trace_entities>\n");

		return sb.toString();
	}

    public String getXmlParameterValues(long parameterKey)
    {
        IdlNode idlNode = db.readValue(parameterKey);

        if (idlNode == null)
        {
            // no parameter information available.

			XmlWriter w = new XmlWriter();

            return w.toString();
        }

        XmlWriter w = new XmlWriter();

        XmlNode.write(idlNode, w);

        return w.toString();
    }
}

