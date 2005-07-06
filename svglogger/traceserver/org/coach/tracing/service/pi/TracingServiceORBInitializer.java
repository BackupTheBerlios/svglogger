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

import org.omg.IOP.Codec;
import org.omg.IOP.CodecFactory;
import org.omg.IOP.CodecFactoryHelper;
import org.omg.IOP.Encoding;
import org.omg.PortableInterceptor.*;
import org.omg.CORBA.LocalObject;
import org.coach.tracing.api.pi.*;

public class TracingServiceORBInitializer extends LocalObject implements ORBInitializer
{
    private TracingServiceImpl aServiceImpl;
    private TracingServiceInterceptor aServiceInterceptor;

    public void pre_init(ORBInitInfo info)
    {
    	try
        {
//            System.err.println("TracingServiceORBInitializer pre_init()");

    	    int id = info.allocate_slot_id();

    	    aServiceInterceptor = new TracingServiceInterceptor(id);

    	    info.add_client_request_interceptor(aServiceInterceptor);
    	    info.add_server_request_interceptor(aServiceInterceptor);

    	    // Create and register a reference to the service to be
    	    // used by client code.
    	    aServiceImpl = new TracingServiceImpl(id);
    	    info.register_initial_reference("TracingService", aServiceImpl);
    	}
    	catch (Throwable t)
        {
    	    t.printStackTrace();
    	}
    }

    public void post_init(ORBInitInfo info)
    {
    	try
        {
//            System.err.println("TracingServiceORBInitializer post_init()");
    	    Current piCurrent = CurrentHelper.narrow(info.resolve_initial_references("PICurrent"));
    	    CodecFactory codecFactory = CodecFactoryHelper.narrow(info.resolve_initial_references("CodecFactory"));
    //	    Encoding encoding = new Encoding((short)0, (byte)1, (byte)2);
    	    Encoding encoding = new Encoding((short)0, (byte)1, (byte)0);
    	    Codec codec = codecFactory.create_codec(encoding);
    	    aServiceInterceptor.init(piCurrent, codec);
            aServiceImpl.setPICurrent(piCurrent);
    	}
    	catch (Throwable t)
        {
    	    t.printStackTrace();
    	}
    }
}
