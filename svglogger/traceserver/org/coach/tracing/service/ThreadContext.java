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

import org.omg.CORBA.Any;
import java.util.*;
import org.coach.tracing.api.*;

/**
 * This class is used to maintain context information associated with an execution thread and the
 * current active request.
 */
public class ThreadContext
{
    private static int frameCounter;
    private String trailLabel = "";
    private String threadId = "";
    private String trailId = "";
    private String senderThreadId = "";
    private Stack requestStack;
    private RequestContext requestContext;
    private static int eventCounter = -1; // absolute event counter per process or virtual machine
    private static String vmId;
    private static HashMap contextMap = new HashMap();  
    private static Hashtable frameRequestContext = new Hashtable();
    
    static
    {
        try
        {
            // create an unique application id
            java.io.File tmp = java.io.File.createTempFile("coach", "");
            tmp.deleteOnExit();
            vmId = java.net.InetAddress.getLocalHost().getHostName().replace('.', '_') + "_" + tmp.getName().substring(5);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Private contructor.
     * New instances are created as needed when getCurrentThreadContext is called.
     */
    private ThreadContext()
    {
        threadId = vmId + "_" + Thread.currentThread().getName();
        trailId = threadId; // default
        //creation of this objects associates the current thread with the object
        requestStack = new Stack();
        requestContext = new RequestContext();        
    }

    /**
     * Returns the ThreadContext for the current thread.
     */
    public static synchronized ThreadContext getCurrentThreadContext()
    {
        ThreadContext tc = null;
        if (Thread.currentThread().getName().startsWith("AWT"))
        {
            // Associate the thread context with the current active frame rather then the
            // thread id because Java uses a single thread for all GUI activities.
            java.awt.Frame f = getCurrentFrame();
            if (f != null)
            {
                tc = (ThreadContext)(contextMap.get(f));
            }
        }
        if (tc == null)
        {
            tc = (ThreadContext)(contextMap.get(vmId + "_" + Thread.currentThread().getName()));
        }
        if (tc == null)
        {
            tc = new ThreadContext();
            contextMap.put(tc.getThreadId(), tc);
        }
        return tc;
    }

    /**
     * Returns the ThreadContext data structure for the given thread id.
     */
    public static synchronized ThreadContext getThreadContext(String id)
    {
        ThreadContext tc = null;
        if (id.indexOf("AWT-EventQueue") >= 0)
        {
            java.awt.Frame f = getCurrentFrame();
            if (f != null)
            {
                tc = (ThreadContext)(contextMap.get(f));
//System.err.println("*** getThreadContext: " + id + "\nComponent: " + tc.getComponentName() + "\nThreadId: " + tc.getThreadId());
            }
        }            
        if (tc == null)
        {
            tc = (ThreadContext)(contextMap.get(id));
        }
        if (tc == null)
        {
            tc = new ThreadContext();
            contextMap.put(tc.getThreadId(), tc);
        }
        return tc;
    }

    private void setThreadId(String threadId)
    {
        this.threadId = threadId;
    }

    /**
     * Returns the trail label for the current thread.
     *
     * @return The trail label for the current thread.
     */
    public String getTrailLabel()
    {
        return trailLabel;
    }

    /**
     * Assigns a trail label to the current thread.
     *
     * @param label The trail label for the current thread.
     */
    public void setTrailLabel(String label)
    {
        trailLabel = label;
    }

    /**
     * Returns the current active operation for the current thread.
     *
     * @return The current active operation for the current thread.
     */
    public String getCurrentOperation()
    {
        return requestContext.getCurrentOperation();
    }

    /**
     * Assigns the current active operation to the current thread.
     *
     * @param opr The opertion name.
     */
    public void setCurrentOperation(String opr)
    {
        requestContext.setCurrentOperation(opr);
    }
        
    /**
     * Returns the current active message id for the current thread.
     *
     * @return The current active message id for the current thread.
     */
    public String getMessageId()
    {
        return requestContext.getMessageId();
    }

    /**
     * Assigns the current active message id to the current thread.
     *
     * @param id The message id.
     */
    public void setMessageId(String id)
    {
        requestContext.setMessageId(id);
    }

    /**
     * Returns the current active interface id for the current thread.
     *
     * @return The current active interface id for the current thread.
     */
    public String getCurrentInterfaceId()
    {
        return requestContext.getCurrentInterfaceId();
    }

    /**
     * Assigns the current active interface id to the current thread.
     *
     * @param id The interface id.
     */
    public void setCurrentInterfaceId(String id)
    {
        requestContext.setCurrentInterfaceId(id);
    }
            
    /**
     * Returns the id for the current thread.
     *
     * @return The id for the current thread.
     */
    public String getThreadId()
    {
        return threadId;
    }

    /**
     * Returns the trail id for the current thread.
     *
     * @return The trail id for the current thread.
     */
    public String getTrailId()
    {
        return trailId;
    }
        
    /**
     * Assigns the trail id to the current thread.
     *
     * @param id The trail id.
     */
    public void setTrailId(String id)
    {
        trailId = id;
    }  

    /**
     * Returns the total number of events in the process.
     *
     * @return The total number of events in the process.
     */
    public static int getEventCounter()
    {
        return eventCounter;
    }

    /**
     * Increments and returns the total number of events in the process.
     *
     * @return The total number of events in the process plus one.
     */
    public static int incrementEventCounter()
    {
        return ++eventCounter;
    }
        
    /**
     * Returns the process id.
     *
     * @return The process id.
     */
    public String getVmId()
    {
        return vmId;
    }

    /**
     * Returns the component name for the current active request.
     *
     * @return The name of the current component.
     */        
    public String getComponentName()
    {
        return requestContext.getComponentName();
    }
    
    /**
     * Assigns the component name to the current request context.
     *
     * @param name The component name.
     */
    public void setComponentName(String name)
    {
        requestContext.setComponentName(name);
    }

    /**
     * Returns the component type name for the current active request.
     *
     * @return The type name of the current component.
     */
    public String getComponentType()
    {
        return requestContext.getComponentType();
    }

    /**
     * Assigns the component type name to the current request context.
     *
     * @param type The component type name.
     */
    public void setComponentType(String type)
    {
        requestContext.setComponentType(type);
    }
    
    /**
     * Returns the container name for the current active request.
     *
     * @return The name of the current container.
     */
    public static String getContainerName()
    {
        return RequestContext.getContainerName();
    }

    /**
     * Assigns the container name to the current request context.
     *
     * @param name The container name.
     */
    public static void setContainerName(String name)
    {
        RequestContext.setContainerName(name);
    }
        
    /**
     * Returns the container type name for the current active request.
     *
     * @return The type name of the current container.
     */
    public static String getContainerType()
    {
        return RequestContext.getContainerType();
    }

    /**
     * Assigns the container type name to the current request context.
     *
     * @param type The container type name.
     */
    public static void setContainerType(String type)
    {
        RequestContext.setContainerType(type);
    }

    /**
     * Returns the object instance name for the current active request.
     *
     * @return The name of the current object instance.
     */
    public String getObjectInstanceId()
    {
        return requestContext.getObjectInstanceId();
    }
        
    /**
     * Assigns the object instance id to the current request context.
     *
     * @param id The object instance id.
     */
    public void setObjectInstanceId(String id)
    {
        requestContext.setObjectInstanceId(id);
    } 

    /**
     * Returns the object repository id for the current active request.
     *
     * @return The id of the current CORBA object.
     */
    public String getObjectRepositoryId()
    {
        return requestContext.getObjectRepositoryId();
    }
    
    /**
     * Assigns the object repository id to the current request context.
     *
     * @param id The object repository id.
     */
    public void setObjectRepositoryId(String id)
    {
        requestContext.setObjectRepositoryId(id);
    } 
              
    /**
     * Pushes the current request context on a stack.
     * This should be called just after an outgoing invocation for synchronous (two way) operations.
     * The popRequestContext method should be called just before the return.
     */
    public void pushRequestContext()
    {
        requestStack.push(requestContext);
        // clone the current request context to inherit component name, type etc.
        requestContext = (RequestContext)requestContext.clone();
    }
    
    /**
     * Restores the request context from the top of the stack.
     */
    public void popRequestContext()
    {
        try
        {
            requestContext = (RequestContext)requestStack.pop();
        }
        catch (Exception e)
        {
        }
    }

    private RequestContext getRequestContext()
    {
          return requestContext;
    }
    
    private void setRequestContext(RequestContext ctx)
    {
        requestContext = (RequestContext)ctx.clone();
    }

    /**
     * Associate a component context with a frame object.
     * First the thread context for the frame object is found, or created if it does not exist.
     * Then the component context is set in the thread context.
     *
     * @param frame The frame object reference to associate with the component context.
     */
    public static void setFrameThreadContext(java.awt.Frame frame)
    {
        ThreadContext ftc = (ThreadContext)(contextMap.get(frame));
        if (ftc == null)
        {
            // create a new thread context for this frame
            ftc = new ThreadContext();
            ftc.setThreadId(ftc.getThreadId() + "_GuiFrame_" + frameCounter++); 
            // set a unique trail id per frame
            ftc.setTrailId(ftc.getThreadId());
        }
        
        if (Thread.currentThread().getName().startsWith("AWT"))
        {
            // This frame is created from another AWT thread
            // find the parent frame and obtain its context
            java.awt.Frame parent = getCurrentFrame();
            if (parent != null)
            {
                ThreadContext parentFrameContext = (ThreadContext)(contextMap.get(parent));
                ftc.setRequestContext(parentFrameContext.getRequestContext());
            }
        }
        else
        {
            // The frame context inherits the request context from the current thread
            ftc.setRequestContext(getCurrentThreadContext().getRequestContext());
        }
        contextMap.put(frame, ftc);
//System.err.println("*** setFrameThreadContext:\nComponent: " + ftc.getComponentName() + "\nThreadId: " + ftc.getThreadId());

    }   

    /**
     * This method searches through the event source chain starting with the most current awt event until the parent frame is
     * found.
     */    
    private static java.awt.Frame getCurrentFrame()
    {
        java.awt.AWTEvent awtEvent = java.awt.EventQueue.getCurrentEvent();
        if (awtEvent != null)
        {
            Object evtSrc = awtEvent.getSource();
            while (evtSrc instanceof java.awt.Component)
            {
                if (evtSrc instanceof java.awt.Frame)
                {
                    // find the associated context
                    return (java.awt.Frame)evtSrc;
                }
                evtSrc = ((java.awt.Component)evtSrc).getParent();
            }
        }
        return null;
    }

    /**
     * Returns IDL struct PropagationContext which is used to pass
     * information between CORBA calls.
     */
    public synchronized PropagationContext getPropagationContext()
    {
        PropagationContext propagationContext = new PropagationContext();
        propagationContext.trail_label = getTrailLabel();
        propagationContext.trail_id = getTrailId();  
        propagationContext.message_id = getMessageId(); 
        return propagationContext;
    }

    /**
     * The propagation context is passed through the iiop request.
     */
    public synchronized void setPropagationContext(PropagationContext propagationContext)
    {
        setTrailLabel(propagationContext.trail_label);
        setTrailId(propagationContext.trail_id);
        setMessageId(propagationContext.message_id);
    }

    /**
     * The ORB request processor typicaly uses a thread pool from which worker threads are assigned
     * to handle a request. When the request is completed, the current thread context must be
     * cleared.
     */
    public synchronized void clearContext()
    {
        if (requestStack.size() == 0)
        {
            setTrailLabel("");
            setTrailId("");
            setMessageId("");
            requestContext.clear();
            requestStack.clear();
        }       
    }  

    
    /**
     * The RequestContext class contains all variables associated with a request.
     * When a new thread is spawn, the component name and type are inherited from the parent thread.
     * Therefore, this class extends InheritableThreadLocal and overwrites the childValue method.
     */
    static class RequestContext extends InheritableThreadLocal implements Cloneable
    {
        private static String node_name = "";
        private static String cnt_name = "";
        private static String cnt_type = "";
        // The componentId is inherited from the parent thread when a new child thread is started
        private ComponentId componentId;
        private String object_instance_id;
        private String object_repository_id;
        private String message_id = "";
        private String currentInterfaceId = "";
        private String currentOperation = "";
        
        public RequestContext()
        {
            // Use the ComponentId associated with the current thread if there is one.
            componentId = (ComponentId)get();
            if (componentId == null)
            {
                // This thread does not have a ComponentId yet, create a new one.
                componentId = new ComponentId("", "");
                set(componentId);
            }
        }
    
        public RequestContext(String name, String type)
        {
            componentId = new ComponentId(name, type);
            // Set the componentId as thread local data so that it can be
            // propagated to child threads.
            set(componentId);
        }   

        public void setComponentName(String name)
        {
            componentId.name = name;
        }
        
        public void setComponentType(String type)
        {
            componentId.type = type;
        }        
        
        public String getCurrentInterfaceId()
        {
            return currentInterfaceId;
        }
    
        public void setCurrentInterfaceId(String id)
        {
            currentInterfaceId = id;
        }

        public String getCurrentOperation()
        {
            return currentOperation;
        }
    
        public void setCurrentOperation(String opr)
        {
            currentOperation = opr;
        }
            
        public String getComponentName()
        {
            return componentId.name;
        }
    
        public String getComponentType()
        {
            return componentId.type;
        }
    
        public static String getContainerName()
        {
            return cnt_name;
        }
    
        public static String getContainerType()
        {
            return cnt_type;
        }

        public void setObjectInstanceId(String instance_id)
        {
            object_instance_id = instance_id;
        } 
        
        public void setObjectRepositoryId(String repository_id)
        {
            object_repository_id = repository_id;
        } 
        
        public String getObjectInstanceId()
        {
            return object_instance_id;
        } 
        
        public String getObjectRepositoryId()
        {
            return object_repository_id;
        }
            
        public static void setContainerName(String name)
        {
            cnt_name = name;
        }      

        public static void setContainerType(String type)
        {
            cnt_type = type;
        } 
        
        public void setMessageId(String id)
        {
            message_id = id;
        }

        public String getMessageId()
        {
            return message_id;
        }
        
        public void clear()
        {
            componentId.name = "";
            componentId.type = "";
            object_instance_id = "";
            object_repository_id = "";
            message_id = "";
        }
        
        public Object clone()
        {
            RequestContext r = new RequestContext(getComponentName(), getComponentType());
            r.object_instance_id = object_instance_id;
            r.object_repository_id = object_repository_id;
            r.currentInterfaceId = currentInterfaceId;
            return r;
        }
        
        /**
         * Overloaded member from InheritableThreadLocal to propagate parent data to
         * the child thread. In our case, only the componentId is inherited because all other fields
         * are set when an invocation is done.
         */
        protected Object childValue(Object parentValue)
        {
            return ((ComponentId)parentValue).clone();
        }
        
        class ComponentId implements Cloneable {
            public String name;
            public String type;
            
            public ComponentId(String name, String type)
            {
                this.name = name;
                this.type = type;
            }
            
            public Object clone()
            {
                return new ComponentId(name, type);
            }
        }   
    }
}