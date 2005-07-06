/***************************************************************************/
/* COACH: Component Based Open Source Architecture for                     */
/*        Distributed Telecom Applications                                 */
/* See:   http://www.objectweb.org/                                        */
/*                                                                         */
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
import java.util.*;

public class i_TraceImpl extends org.coach.tracing.api.i_TracePOA
{
	private ServerMain.Queue queue;

    public i_TraceImpl(ServerMain.Queue queue)
    {
    	this.queue = queue;
    }

    /*******************************************************************************
     * Methods for OMG IDL interface org::coach::tracing::i_Trace
	 *******************************************************************************/

    public void receiveEvent(TraceEvent[] events)
    {
		queue.add(events);
    }
}

