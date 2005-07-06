/***************************************************************************/
/* COACH: Component Based Open Source Architecture for                     */
/*        Distributed Telecom Applications                                 */
/* See:   http://www.objectweb.org/                                        */
/*                                                                         */
/* Copyright (C) 2003 Lucent Technologies Nederland BV                     */
/*                    Bell Labs Advanced Technologies - EMEA               */
/*                                                                         */
/* Initial developer(s): Nikolay Diakov                                    */
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
import java.net.*;
import java.io.*;
import org.coach.tracing.service.ntp.*;
import org.omg.CORBA.*;
import org.omg.CosNaming.*;

public class PhysicalTime
{
    private static boolean NTP_enabled = true;
    private static String NTP_server = "ntp0.fau.de";
    private static int avg_from = 3;
    private static long NTP_offset = 0;
    private static long NTP_last_updated = 0;
    private static long NTP_delta = 3600000; //every hour sync
    private static PhysicalTime clock = new PhysicalTime(System.getProperty("tracing.ntp.server", "none"));

    private PhysicalTime(String server)
    {
        if (server.equalsIgnoreCase("none"))
        {
            NTP_enabled = false;
        }
        else if (server != null)
        {
            NTP_server = server;
        }
    }

    public static PhysicalTime getInstance()
    {
        return clock;
    }

    //get the current time, but updated with the local offset to a central NTP server
    public long getTime()
    {
        long t = System.currentTimeMillis();
        long o;
        synchronized ( clock )
        {
            o = NTP_offset;
        }

        if ((t - NTP_last_updated) > NTP_delta)
        {
            //refresh the offset
            o = calcOffset();
            NTP_last_updated = t;
        }
        //call it again to be more precise
        t = System.currentTimeMillis();
        return (t + NTP_offset);
    }

    private long seed;
    //NTP offset calculation
    private synchronized long calcOffset()
    {
        long res = 0, d = 0;

        if (NTP_enabled)
        {
            //here calculate the NTP offset using the NTP servers
            for (int i = 0; i < avg_from; i++)
            {
                try
                {
                    System.out.println("Updating NTP offset from: " + NTP_server);
                    NtpConnection ntpConnection = new NtpConnection(InetAddress.getByName(NTP_server));
                    res += ntpConnection.getInfo().offset;
                    d++;
                    ntpConnection.close();
                }
                catch (Exception e)
                {
                    System.out.println("Couldn't update the NTP offset from: " + NTP_server);
                }
            }

            if (d > 0)
            {
                res = res / d;
            }
        }
        System.out.println("New NTP offset value: " + res);
        NTP_offset = res;
        return res;
    }
}
