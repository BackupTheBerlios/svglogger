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
package org.coach.tracing.server;

import org.coach.idltree.*;
import org.coach.tracing.*;
import org.coach.tracing.api.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class EventRecord
{
    private static EventDataBase eventDB;
    TraceRecord tr;
    public IdlNode idlNode;
    long currentIndex;
    IdentityDescriptor identity;
    private int indent = 4;

    public EventRecord()
    {
        tr = new TraceRecord();
    }

    public EventRecord(TraceEvent e)
    {
        this();
        try
        {
            tr.trail_label = e.trail_label;
            tr.message_id = e.message_id;
            identity = e.identity;
            tr.trail_id = e.trail_id;
            tr.event_counter = e.event_counter;
            tr.thread_id = e.thread_id;
            tr.op_name = e.op_name;
            tr.interaction_point = e.interaction_point;
            tr.time_stamp = e.time_stamp;
            tr.linkKey = -1;

            if (tr.interaction_point == InteractionPoint.STUB_OUT || tr.interaction_point == InteractionPoint.POA_IN)
            {
                idlNode = new IdlOperation(e.identity.object_repository_id, tr.op_name, e.parameters, e.parameter_values, false);
            }
            if (tr.interaction_point == InteractionPoint.ONEWAY_STUB_OUT || tr.interaction_point == InteractionPoint.ONEWAY_POA_IN)
            {
                idlNode = new IdlOperation(e.identity.object_repository_id, tr.op_name, e.parameters, e.parameter_values, true);
            }
            if (tr.interaction_point == InteractionPoint.STUB_IN || tr.interaction_point == InteractionPoint.POA_OUT)
            {
                idlNode = new IdlReply(e.identity.object_repository_id, tr.op_name, e.parameters, e.parameter_values);
            }
            if (tr.interaction_point == InteractionPoint.STUB_IN_EXCEPTION || tr.interaction_point == InteractionPoint.POA_OUT_EXCEPTION)
            {
                idlNode = new IdlReply(e.identity.object_repository_id, tr.op_name, e.parameter_values[0]);
            }
/*
            try
            {
                System.out.println(getParameterValues());
            }
            catch (Throwable tx)
            {
                tx.printStackTrace();
            } */
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public static void setEventDB(EventDataBase db) {
        eventDB = db;
    }

    public TraceRecord getTraceRecord()
    {
        return tr;
    }

    public Long key()
    {
        return new Long(tr.key_id);
    }

    public long getKey()
    {
        return tr.key_id;
    }

    public Long getLinkKey()
    {
        return new Long(tr.linkKey);
    }

    public long getLink()
    {
        return tr.linkKey;
    }

    public Long getIdentityKey()
    {
        return new Long(tr.identityKey);
    }

    public InteractionPoint getInteractionPoint()
    {
        return tr.interaction_point;
    }

    public String getProcessId()
    {
        return identity.process_id;
    }

    public String getMessageId()
    {
        return tr.message_id;
    }

    public long getIndex()
    {
        return tr.event_counter;
    }

    public int getEventCounter() {
        return tr.event_counter;
    }

    public boolean isOutgoingMessage()
    {
        return (tr.interaction_point == InteractionPoint.STUB_OUT || tr.interaction_point == InteractionPoint.POA_OUT || tr.interaction_point == InteractionPoint.POA_OUT_EXCEPTION || tr.interaction_point == InteractionPoint.ONEWAY_STUB_OUT);
    }

    public boolean isIncommingMessage()
    {
        return !isOutgoingMessage();
    }

    public boolean isReturn()
    {
        return ((tr.interaction_point == InteractionPoint.POA_OUT) || (tr.interaction_point == InteractionPoint.POA_OUT_EXCEPTION) || (tr.interaction_point == InteractionPoint.STUB_IN) || (tr.interaction_point == InteractionPoint.STUB_IN_EXCEPTION));
    }

    public boolean isForward()
    {
        return !isReturn();
    }

    public boolean isException()
    {
        return((tr.interaction_point == InteractionPoint.POA_OUT_EXCEPTION) || (tr.interaction_point == InteractionPoint.STUB_IN_EXCEPTION));
    }

    public boolean isOneway()
    {
        return ((tr.interaction_point == InteractionPoint.ONEWAY_STUB_OUT) || (tr.interaction_point == InteractionPoint.ONEWAY_POA_IN));
    }

    public boolean isLinked()
    {
        return tr.linkKey != -1;
    }

    public String getTimeStamp()
    {
        java.util.Date date = new java.util.Date(tr.time_stamp);
        SimpleDateFormat simpledateformat = (SimpleDateFormat)DateFormat.getDateTimeInstance(1, 1);
        simpledateformat.applyPattern("EEE MMM d H:mm:ss:SS yyyy");
        String time = simpledateformat.format(date);

        return time;
    }

    public long getTime()
    {
        return tr.time_stamp;
    }

    public String getTrailLabel() {
        return tr.trail_label;
    }

    public String getTrailId()
    {
        return tr.trail_id;
    }

    public String getOpName() {
        return tr.op_name;
    }

    public String getThreadId()
    {
        return tr.thread_id;
    }

    public String getInteractionPointName()
    {
        switch (tr.interaction_point.value())
        {
            case InteractionPoint._STUB_OUT: return "STUB_OUT";
            case InteractionPoint._STUB_IN: return "STUB_IN";
            case InteractionPoint._POA_OUT: return "POA_OUT";
            case InteractionPoint._POA_IN: return "POA_IN";
            case InteractionPoint._STUB_IN_EXCEPTION: return "STUB_IN_EXCEPTION";
            case InteractionPoint._POA_OUT_EXCEPTION: return "POA_OUT_EXCEPTION";
            case InteractionPoint._ONEWAY_STUB_OUT: return "ONEWAY_STUB_OUT";
            case InteractionPoint._ONEWAY_POA_IN: return "ONEWAY_POA_IN";
        }
        return "";
    }

    static public String getIdentityKindName(IdentityKind kind)
    {
        switch (kind.value())
        {
            case IdentityKind._CCM_CONTAINER: return "CONTAINER";
            case IdentityKind._CCM_COMPONENT: return "COMPONENT_Type";
            case IdentityKind._CCM_OBJECT: return "ComponentID";
            case IdentityKind._CCM_PROCESS: return "SESSION";
            case IdentityKind._CCM_NODE: return "PLATFORM";
            case IdentityKind._CCM_DOMAIN: return "DOMAIN";
        }
        return "";
    }

    public String getParameterValues()
    {
        if (idlNode == null) {
            // no parameter information available.
            return "";
        }
        XmlWriter w = new XmlWriter(indent);
        XmlNode.write(idlNode, w);
        return w.toString();
    }

    public IdlNode getParameters()
    {
//        if (idlNode == null) {
//            String xmlString = null;
//            try {
//                xmlString = eventDB.getParameterValues(tr.parametersKey);
//                if (!xmlString.equals("")) {
//                    idlNode = XmlNode.getIdlNodeXml(xmlString);
//                }
//            } catch (Exception e) {
//                System.out.println(xmlString);
//                e.printStackTrace();
//            }
//        }
//        return idlNode;
        return null;
    }

    public void setCurrentIndex(long i)
    {
        currentIndex = i;
    }

    public long getCurrentIndex()
    {
        return currentIndex;
    }

    public String toXml(EventDataBase db)
    {
        StringBuffer sb = new StringBuffer();

        sb.append(indent() + "<traceEvent id='" + getKey() + "'>\n");
        indent++;
        sb.append(indent() + "<interactionPoint>" + getInteractionPointName() + "</interactionPoint>\n");
        sb.append(indent() + "<trailLabel>" + tr.trail_label + "</trailLabel>\n");
        sb.append(indent() + "<originator>" + getTrailId() + "</originator>\n");
        sb.append(indent() + "<yposIndex>" + currentIndex + "</yposIndex>\n");
        sb.append(indent() + "<timeStamp>" + getTimeStamp() + "</timeStamp>\n");
        sb.append(indent() + "<linkedEvent>" + tr.linkKey + "</linkedEvent>\n");
        sb.append(indent() + "<operation>" + getOpName() + "</operation>\n");
		sb.append(indent() + "<identityKey>" + tr.identityKey + "</identityKey>\n");
		sb.append(indent() + "<parametersKey>" + tr.parametersKey + "</parametersKey>\n");
//just
//		sb.append(indent() + "<parametersVal>" +  getParameterValues() + "</parametersVal>\n");



        indent--;
        sb.append(indent() + "</traceEvent>\n");

        return sb.toString();
    }

    private String indent()
    {
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < indent; i++)
        {
            sb.append(" ");
        }
        return sb.toString();
    }
}

