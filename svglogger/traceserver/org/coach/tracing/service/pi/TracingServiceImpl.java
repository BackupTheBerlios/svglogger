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

import org.omg.CORBA.Any;
import org.omg.CORBA.OctetSeqHelper;
import org.omg.CORBA.TCKind;
import org.omg.CORBA.LocalObject;
import org.omg.CORBA.ORB;
import org.omg.PortableInterceptor.Current;
import org.omg.PortableInterceptor.CurrentHelper;
import org.omg.PortableInterceptor.InvalidSlot;
import java.util.Hashtable;
import org.coach.tracing.service.*;
import org.coach.tracing.api.pi.*;
import java.io.*;

class TracingServiceImpl extends LocalObject implements TracingService
{
    private int slotId;
    private int currentServiceId = 0;
    private Current piCurrent;
    private Any NOT_IN_EFFECT;
    private Hashtable dataTable = new Hashtable();
    private ORB orb;

    public TracingServiceImpl(int slotId)
    {
        this.slotId = slotId;
//System.err.println("TracingServiceImpl slot: " + slotId);
    }

    // Package protected so the TracingService ORBInitializer can access this
    // non-IDL defined method.
    void setPICurrent(Current piCurrent)
    {
        this.piCurrent = piCurrent;
    }

    private void init()
    {
        if (orb == null)
        {
//System.err.println("TracingServiceImpl init()");
            orb = ORB.init(new String[0], null);
            NOT_IN_EFFECT = orb.create_any();
            TracingServiceInterceptor.init();
        }
    }

    public synchronized void start()
    {
        init();
        try
        {
            // This is called from a client thread scope.
            Any any = orb.create_any();
            ThreadContext tc = ThreadContext.getCurrentThreadContext();
            String tid = tc.getThreadId();
//System.err.println("TracingServiceImpl start(): " + tid);
            any.insert_string(tid);
            piCurrent.set_slot(slotId, any);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void stop()
    {
        try
        {
            piCurrent.set_slot(slotId, NOT_IN_EFFECT);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}