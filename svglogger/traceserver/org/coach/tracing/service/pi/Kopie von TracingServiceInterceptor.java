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
package org.coach.tracing.service.pi;

import org.omg.CORBA.*;
import org.omg.IOP.*;
import org.omg.IOP.CodecPackage.*;
import org.omg.PortableInterceptor.*;
import java.util.Hashtable;
import org.omg.PortableInterceptor.Current;
import org.coach.tracing.api.*;
import org.coach.tracing.api.pi.*;
import org.coach.tracing.service.*;
import org.coach.util.IorPrinter;
import org.omg.Dynamic.Parameter;
import org.omg.PortableServer.*;

public class TracingServiceInterceptor extends LocalObject implements ClientRequestInterceptor, ServerRequestInterceptor
{
    private int slotId;
    private Codec codec;
    private Current piCurrent;
    private static boolean ready;
    private static Sender sender;
    private static ORB orb;
    private static Hashtable requestContextTable = new Hashtable();

    private static final int serviceContextId = 100;

    public TracingServiceInterceptor(int slotId)
    {
        this.slotId = slotId;
//System.err.println("TracingServiceInterceptor slot: " + slotId);
    }

    void init(Current piCurrent, Codec codec)
    {
        this.piCurrent = piCurrent;
        this.codec = codec;
    }

    public static void init()
    {
        if (sender == null && !Sender.isInitializing())
        {
            try
            {
                orb = org.objectweb.ccm.CORBA.TheORB.getORB();
                if (orb != null)
                {
                    sender = Sender.createSender(orb);
                    ready = true;
                }
            }
            catch (Throwable e)
            {
            }
        }
    }

    //
    // Interceptor operations
    //

    public String name()
    {
        return "TracingServiceInterceptor";
    }

    public void destroy()
    {
    }

    //
    // ClientRequestInterceptor operations
    //
    public void send_request(ClientRequestInfo ri)
    {
        // send_request does not execute in the client thread!
        if (ready && !ri.operation().equals("receiveEvent"))
        {
            try
            {
                // Obtain the any containing a ServiceData struct from the PICurrent slot.
                // This slot was filled by the operations in the local object ServiceImpl.
                Any any = ri.get_slot(slotId);
                if (any != null && any.type().kind().equals(TCKind.tk_string))
                {
                    String key = any.extract_string();
                    String ior = orb.object_to_string(ri.effective_target());
                    IorPrinter iorPrinter = new IorPrinter(ior);
                    String id = iorPrinter.getTypeId();
                    
//System.err.println("TracingServiceInterceptor (" + ri.operation() + ") send_request: " + id);
//                    if (!sender.exclude(id))
//                    {
                        ThreadContext tc = ThreadContext.getThreadContext(key);
                        tc.setObjectInstanceId("Stub_" + ri.effective_target().hashCode());
                        tc.setObjectRepositoryId(id);
                        if (ri.response_expected())
                        {
                            Integer rid = new Integer(ri.request_id());
                            tc.setCurrentOperation(ri.operation());
                            requestContextTable.put(rid, tc);
                        }
                        String name = tc.getThreadId();
//System.err.println("stubPreInvoke: " + id + ", " + ri.operation() + ", key: " + key);
                        sender.stubPreInvoke(tc, ri.operation(), null, null, !ri.response_expected());
    
                        // insert the updated ServiceData in an any
                        any = orb.create_any();
                        PropagationContext pc = tc.getPropagationContext();
                        PropagationContextHelper.insert(any, pc);
                        // encode the any to a byte array and create a new ServiceContext with it.
                        byte[] serviceContextData = codec.encode(any);
                        ServiceContext serviceContext = new ServiceContext(serviceContextId, serviceContextData);
    
                        // Add the ServiceContext to the outgoing request.
                        ri.add_request_service_context(serviceContext, true);
                        if (ri.response_expected())
                        {
                            tc.pushRequestContext();
                        }
//                    }
                }
            }
            catch (InvalidSlot e)
            {
            }
            catch (Exception e)
            {
                  e.printStackTrace();
            }
        }
    }

    public void send_poll(ClientRequestInfo ri)
    {
//        System.err.println("TracingServiceInterceptor (" + ri.operation() + ") send_poll");
    }

    public void receive_reply(ClientRequestInfo ri)
    {
        ThreadContext tc = (ThreadContext)requestContextTable.remove(new Integer(ri.request_id()));
        if (tc != null)
        {
            try
            {
                tc.popRequestContext();

//System.err.println("stubPostInvoke: " + id + ", " + ri.operation() + ", key: " + name);
                sender.stubPostInvoke(tc, ri.operation(), null, null, false, false);
            }
            catch (Exception e)
            {
            }
        }
    }

    public void receive_exception(ClientRequestInfo ri)
    {
//        System.err.println("TracingServiceInterceptor (" + ri.operation() + ") receive_exception");
    }

    public void receive_other(ClientRequestInfo ri)
    {
//        System.err.println("TracingServiceInterceptor (" + ri.operation() + ") receive_other");
    }

    //
    // ServerRequestInterceptor operations
    //
    public void receive_request_service_contexts(ServerRequestInfo ri)
    {
        init();
//        System.err.println("TracingServiceInterceptor (" + ri.operation() + ") receive_request_service_contexts");

        if (ready && !ri.operation().equals("receiveEvent"))
        {
//            System.err.println("TracingServiceInterceptor (" + ri.operation() + ") receive_request_service_contexts");
            try
            {
                // Obtain the service context from the request buffer
                ServiceContext serviceContext = ri.get_request_service_context(serviceContextId);
                Any any = codec.decode(serviceContext.context_data);
                if (any.type().equal(PropagationContextHelper.type()))
                {
                    // store the any with the ServiceData struct in the PICurrent slot
//System.err.println("TracingServiceInterceptor set slot: " + slotId);
                    ri.set_slot(slotId, any);
                    // After the return of this method, the ORB copies the Request Scope Current to the Thread Scope Current.
                    // The ORB first handles all receive_request_service_contexts methods of all interceptors and then
                    // calls the receive_request methods.
                }
            }
            catch (Exception e)
            {
//                e.printStackTrace();
            }
        }
    }

    public void receive_request(ServerRequestInfo ri)
    {
//        System.err.println("TraceServiceInterceptor (" + ri.operation() + ") receive_request");

        // This method is called in the same thread as the target operation. See page Chapter 21 21-37 of
        // the CORBA v2.5 (September 2001) document.
        // Therefor write access to the Thread Scope Current is possible.
        if (ready && !ri.operation().equals("receiveEvent"))
        {
//            System.err.println("TraceServiceInterceptor (" + ri.operation() + ") receive_request");
            try
            {
                Any tsc = ri.get_slot(slotId);
                PropagationContext pc = null;
                if (tsc != null && tsc.type().equal(PropagationContextHelper.type()))
                {
                    // receive_request_service_contexts has copied the ServiceData from the RSC to the TSC
                    // Extract the data and store it in the dataTable using the current thread id as key.
                    // Replace the any in the TSC slot with an any that contains this thread id so that it can
                    // be used as a key for subsequent operations.
                    try
                    {
                        pc = PropagationContextHelper.extract(tsc);

                        ThreadContext tc = ThreadContext.getCurrentThreadContext();
                        String id = ri.target_most_derived_interface();
                        tc.setCurrentInterfaceId(id);
                        String key = tc.getThreadId();
                        // Here the proper component name and type should be set in the ThreadContext.
                        // However, with the PI approach there is no way to get to that information therefore
                        // we use a dummy name.
                        tc.setComponentName("CCM_Component_" + id.hashCode());
                        tc.setComponentType(id);

                        tc.setObjectInstanceId("Poa_" + id);
                        tc.setObjectRepositoryId(id);
                        tc.setPropagationContext(pc);

//System.err.println("poaPreInvoke: " + id + ", " + ri.operation() + ", key: " + key);
                        sender.poaPreInvoke(tc, ri.operation(), null, null, !ri.response_expected());

                        // Store the request id in the requestIdList so that we can match the reply
                        if (ri.response_expected())
                        {
                            Integer rid = new Integer(ri.request_id());
                            tc.setCurrentOperation(ri.operation());
                            requestContextTable.put(rid, tc);
                        }

                        Any any = orb.create_any();
                        any.insert_string(key);
                        ri.set_slot(slotId, any);
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void send_reply(ServerRequestInfo ri)
    {
        // This method is called in the same thread as the target operation. See page Chapter 21 21-37 of
        // the CORBA v2.5 (September 2001) document.
        ThreadContext tc = (ThreadContext)requestContextTable.remove(new Integer(ri.request_id()));
        if (tc != null)
        {
            try
            {
//System.err.println("poaPostInvoke: " + id + ", " + ri.operation() + ", key: " + name);
                sender.poaPostInvoke(tc, ri.operation(), null, null);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void send_exception(ServerRequestInfo ri)
    {
        // not implemented yet
//        System.err.println("TracingServiceInterceptor (" + ri.operation() + ") send_exception");
    }

    public void send_other(ServerRequestInfo ri)
    {
//        System.err.println("TracingServiceInterceptor (" + ri.operation() + ") send_other");
    }
}
