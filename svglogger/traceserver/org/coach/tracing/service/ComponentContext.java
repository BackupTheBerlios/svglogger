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

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import org.coach.tracing.api.pi.*;

public class ComponentContext
{
    private static TracingService tracingService;
    private static boolean awtInitialized = false;
    private static int componentCount;

    /**
     * Java handles all GUI calls within a single awt thread. This means ThreadContext information
     * cannot be maintained on the basis of the tread id alone.
     * When a new AWT frame is created the ComponentContext of the thread creating the frame
     * is stored in a table and accociated with the frame id.
     */
    public static synchronized void setFrameContext(java.awt.Frame frame)
    {
        if (!awtInitialized)
        {
            try
            {
                // Obtain the reference to the tracing service.
//System.err.println("initializing tracing service in FrameContext");
                tracingService = org.coach.tracing.api.pi.TracingServiceHelper.narrow(org.objectweb.ccm.CORBA.TheORB.getORB().resolve_initial_references("TracingService"));     
                // Start the tracing service in the default AWT thread
                javax.swing.SwingUtilities.invokeLater(new AwtInitializer(tracingService));
                awtInitialized = true;
            }
            catch (Throwable pix)
            {
                System.err.println("Failed to locate tracing service: " + pix);
            }
        }
        ThreadContext.setFrameThreadContext(frame);
        // Start the tracing service in the main thread
        tracingService.start();
    }

    public static synchronized void setComponentContext(Object ccmObject)
    {
        String componentName = "CCMComponent_" + ccmObject.hashCode();
        String componentType = ccmObject.getClass().getName();
        
        if (tracingService == null)
        {
            try
            {
                // Obtain the reference to the tracing service.
                tracingService = org.coach.tracing.api.pi.TracingServiceHelper.narrow(org.objectweb.ccm.CORBA.TheORB.getORB().resolve_initial_references("TracingService"));     
            }
            catch (Throwable pix)
            {
                System.err.println("Failed to locate tracing service: " + pix);
            }
        }

        ThreadContext tc = ThreadContext.getCurrentThreadContext();
//System.err.println("*** setComponentContext: " + tc.getThreadId() + "\ncomponent: " + componentName + "\ntype: " + componentType + "\n" + Thread.currentThread().getName());
        tc.setComponentName(componentName);
        tc.setComponentType(componentType);
        tc.setContainerName("MyContainer");
        tc.setContainerType("MyContainerType");

        // Start the tracing service in the main thread
        tracingService.start();
    }
}

/**
 * The run method of this class is called from the AWT thread.
 */
class AwtInitializer implements Runnable
{
    TracingService tracingService;
    
    public AwtInitializer(TracingService tracingService)
    {
        this.tracingService = tracingService;
    }
    
    public void run()
    {
//System.err.println("************ AwtInitializer: " + Thread.currentThread().getName());
        // start the tracing service in the AWT thread
        tracingService.start();
    }
}      
        
