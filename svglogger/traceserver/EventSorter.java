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

import java.util.*;

public class EventSorter
{
    private Hashtable processTable = new Hashtable();
    private EventDataBase eventDB;
    private long eventCount;
    private SortedEventList sortedEventList = new SortedEventList();
    
    public EventSorter(EventDataBase db)
    {
        eventDB = db;
    }

    public synchronized void add(EventRecord eventRecord)
    {
        eventCount++;       
        // Obtain the process list for this event
        EventList eventList = (EventList)processTable.get(eventRecord.getProcessId());

        if (eventList == null)
        {
            eventList = new EventList();
            processTable.put(eventRecord.getProcessId(), eventList);
        }
        
        // Create an ordered event
        OrderedEvent orderedEvent = new OrderedEvent(eventRecord.getIndex());
        orderedEvent.key = eventRecord.getKey();
        orderedEvent.isOutgoing = eventRecord.isOutgoingMessage();
        if (eventRecord.getLink() != -1)
        {
            EventRecord link = eventDB.readEvent(eventRecord.getLink());
            if (!link.getProcessId().equals(eventRecord.getProcessId()))
            {
                // We are only concerned with messages between different processes, not within a process
                EventList linkList = (EventList)processTable.get(link.getProcessId());
                if (linkList == null)
                {        
                    throw new RuntimeException("Unexpected empty list for process: " + link.getProcessId());
                }
                // link the two events
                OrderedEvent peer = linkList.getAbsoluteIndex(link.getIndex());
                orderedEvent.link = peer;
                peer.link = orderedEvent;
            }
        }
        
        eventList.add(orderedEvent);
        sortedEventList.insert(orderedEvent);

//        verify();
    }

    public synchronized long getEventIndex(long key)
    {
        return sortedEventList.indexOf(key);
    }
                
    public synchronized EventRecord getEventAt(long index)
    {
        OrderedEvent e = sortedEventList.get(index);
        EventRecord r = eventDB.readEvent(e.key);
        r.currentIndex = index;
        return r;
    }        

    /**
     * Returns the set of events from startIndex to startIndex + range plus
     * all events linked to within that range.
     */
    public synchronized EventRecord[] getEvents(long startIndex, long endIndex)
    {
        TreeMap map = new TreeMap();
        
        if (endIndex >= sortedEventList.size())
        {
            endIndex = sortedEventList.size() - 1;
        }
        if (startIndex < 0 || startIndex >= sortedEventList.size())
        {
            return new EventRecord[0];
        }
        
        for (long i = startIndex; i <= endIndex; i++)
        {
            OrderedEvent e = sortedEventList.get(i);
            if (e == null)
            {
                break;
            }
            map.put(new Long(i), e);
            if (e.link != null)
            {
                map.put(new Long(sortedEventList.indexOf(e.link)), e.link);
            }
        }
        
        EventRecord[] events = new EventRecord[map.size()];
        Iterator it = map.keySet().iterator();
        int i = 0;
        while (it.hasNext())
        {
            Long index = (Long)it.next();
            OrderedEvent e = (OrderedEvent)map.get(index);
            EventRecord r = eventDB.readEvent(e.key);
            r.currentIndex = index.longValue();
            events[i++] = r;
        }
        return events;
    }
                
    /**
     * For testing only.
     * This method verifies the consistency of the event tables.
     * Rule 1: all events in the same process list must have increasing relative and absolute indices
     * Rule 2: all messages must have a larger relative index at the receiving event then the sending event.
     */
    private void verify()
    {
        Iterator it = processTable.keySet().iterator();
        int checked = 0;
        while(it.hasNext())
        {
            String processId = (String)it.next();
            EventList eventList = (EventList)processTable.get(processId);
            OrderedEvent e = eventList.getAbsoluteIndex(0);
            int index = 0;
            while(e != null)
            {
                if (e.next != null)
                {
                    if (e.next.prev != e)
                    {
                        dump();
                        throw new RuntimeException("Event link inconsistency in process table: " + processId + " at index: " + index);                        
                    }
                    if (e.absoluteIndex >= e.next.absoluteIndex)
                    {
                        dump();
                        throw new RuntimeException("Event order inconsistency in process table: " + processId + " at index: " + index + " " + e.absoluteIndex + " >= " + e.next.absoluteIndex);
                    }
                    if (e.relativeIndex >= e.next.relativeIndex)
                    {
                        dump();
                        throw new RuntimeException("Event relative index inconsistency in process table: " + processId + " at index: " + index + " " + e.relativeIndex + " >= " + e.next.relativeIndex);
                    }
                }
                if (e.link != null)
                {
                    if (e.isOutgoing)
                    {
                        if (e.relativeIndex >= e.link.relativeIndex)
                        {
                            dump();
                            throw new RuntimeException("Message inconsistency in process table: " + processId + " at index: " + index + " " + e.relativeIndex + " >= " + e.link.relativeIndex);
                        }
                    }
                    else
                    {
                        if (e.relativeIndex <= e.link.relativeIndex)
                        {
                            dump();
                            throw new RuntimeException("Message inconsistency in process table: " + processId + " at index: " + index + " " + e.relativeIndex + " <= " + e.link.relativeIndex);
                        }
                    }
                }
                if (e.key != -1) {
                    checked++;
                }
                e = e.next;
                index++;
            }            
        }
        if (checked != eventCount)
        {
            dump();
            throw new RuntimeException("Event count (" + eventCount + ") and checked events (" + checked + ") don't match");
        }            
        if (sortedEventList.size() != eventCount)
        {
            dump();
            throw new RuntimeException("Event count (" + eventCount + ") and sortedEventList size (" + sortedEventList.size() + ") don't match");
        }
    }

    private void dump()
    {
        Iterator it = processTable.keySet().iterator();
        while(it.hasNext())
        {
            String processId = (String)it.next();
            System.err.println("**** " + processId + " ****");
            EventList eventList = (EventList)processTable.get(processId);
            OrderedEvent e = eventList.getAbsoluteIndex(0);
            int index = 0;
            while(e != null)
            {
                System.err.print(e.absoluteIndex + ": key: " + e.key + " relative index: " + e.relativeIndex);
                if (e.link != null)
                {
                    if (e.isOutgoing)
                    {
                        System.err.println(" -> " + e.link.key);
                    }
                    else
                    {
                        System.err.println(" <- " + e.link.key);
                    }                        
                }
                else
                {
                    System.err.println();
                }
                e = e.next;
                index++;
            }            
        }
    }

    class OrderedEvent implements Comparable
    {
        public long key = -1;
        public long absoluteIndex;
        public long relativeIndex;
        // pointers to the message counter part
        public OrderedEvent link;
        public boolean isOutgoing;

        public OrderedEvent prev;
        public OrderedEvent next;
        
        public OrderedEvent(long index)
        {
            absoluteIndex = index;
            relativeIndex = index;
        }
        
        public int compareTo(Object obj)
        {
            EventRecord e1 = eventDB.readEvent(key);
            EventRecord e2 = eventDB.readEvent(((OrderedEvent)obj).key);
            
            if (e1.getTrailId().equals(e2.getTrailId()) || e1.getProcessId().equals(e2.getProcessId()))
            {
                // compare relative index
                if (relativeIndex < ((OrderedEvent)obj).relativeIndex)
                {
                    return -1;
                }
                if (relativeIndex > ((OrderedEvent)obj).relativeIndex)
                {
                    return 1;
                }
            }
            
            // compare physical clock
    
            // on fast machines the time resolution is not sufficient and identical physical clock values
            // occur. When the physical clocks are identical the object hashcode is used to differentiate.
            // The returned value cannot be 0 otherwise the event store will overwite a previous entry!
            if (e1.getTime() == e2.getTime()) {
                // if they are still equal differentiate by hashcode
                if (e1.hashCode() == e2.hashCode()) {
                    return 0;
                } else {
                    return (e1.hashCode() > e2.hashCode()) ? 1 : -1;
                }
            }
            return (e1.getTime() > e2.getTime()) ? 1 : -1;
        }        
    }
    
    class EventList
    {
        public OrderedEvent head;
        public OrderedEvent tail;
        
        public EventList() {
        }   
        
        
        /**
         * Insert the event at the absolute index position in the list.
         * If the event is out of order, placeholders will be inserted for missing events.
         */
        private void insert(OrderedEvent e)
        {   
            OrderedEvent prev = null;
            if (e.absoluteIndex == 0)
            {
                if (head != null && head.key == -1)
                {
                    // The current head was a placeholder.
                    e.next = head.next;
                    if (e.next != null) {
                        e.next.prev = e;
                    }
                }                    
                head = e;
            }
            else
            {
                prev = getAbsoluteIndex(e.absoluteIndex - 1);
                if (prev == null)
                {
                    // The current event is ahead and the previous event is not yet inserted.
                    // Insert a placeholder for the previous event.
                    prev = new OrderedEvent(e.absoluteIndex - 1);
                    // link the two events
                    e.prev = prev;
                    prev.next = e;
                    insert(prev);
                }
            }
            
            if (prev != null)
            {
                if (prev.next != null && prev.next.key == -1)
                {
                    // There was a placeholder for the current event.
                    // Since the placeholder is replaced by the current event, link the event following
                    // the placeholder to the current event.
                    e.next = prev.next.next;
                    if (e.next != null) {
                        e.next.prev = e;
                    }
                }
                // Now link the previous event with the current event.
                prev.next = e;
                e.prev = prev;
                // Now calculate the relativeIndex
                e.relativeIndex = e.prev.relativeIndex + 1;
            }
            
            if (e.next == null)
            {
                // The current event is at the end of the list.
                tail = e;
            }                    
        }
        
        public void add(OrderedEvent e)
        {
            // Insert the event in the list.
            insert(e);
            
            if (e.link != null && e.link.isOutgoing)
            {
                // This event is the receiving side of a message 
                if (e.link.relativeIndex >= e.relativeIndex)
                {
                    // Advance the relative index to one more of the senders relativeIndex
                    e.relativeIndex = e.link.relativeIndex + 1;
                }
            }
            if (e.next != null || (e.link != null && e.isOutgoing))
            {
                // The event was inserted out of order.
                // All dependend relative indices must be recalculated.
                recalculate(e);               
            }
//dump();
        }        
        
        /**
         * Searches the event list for the event with the given index.
         */
        public OrderedEvent getAbsoluteIndex(long index)
        {
            // search from the tail up
            OrderedEvent e = tail;
            while (e != null)
            {
                if (e.absoluteIndex == index)
                {
                    return e;
                }
                if (e.absoluteIndex < index)
                {
                    return null;
                }
                else
                {
                    e = e.prev;
                }
            }
            return null;
        }
    
        public OrderedEvent getRelativeIndex(long index)
        {
            // search from the tail up
            OrderedEvent e = tail;
            while (e != null)
            {
                if (e.relativeIndex <= index)
                {
                    return e;
                }
                e = e.prev;
            }
            return null;
        }
        
        /**
         * The recalculation algoritm updates relative indices of all dependend events.
         * The recalculation starts with an event from the vector list and then recalculates all indices in
         * the process list for that event downward.
         * If an event in the process list is the sending part of a message, the process list of the receiving event
         * (and all its dependencies) must also be recalculated. Therefore, the receiving event is added to the end of the
         * list.
         */
        private void recalculate(OrderedEvent e)
        {
System.err.println("************** recalculating ***********");
            // The reorderList is used to keep track of events who's relative index values are changed.
            // Since this also changes their order in the total ordering of events.
            Vector reorderList = new Vector();
            Vector v = new Vector();
            v.add(e);
            
            // Don't use an iterator to process the vector elements because new elements may be
            // added to the vector. An iterator will detect this and throws a ConcurrentModificationException
            for (int i = 0; i < v.size(); i++)
            {
                e = (OrderedEvent)(v.get(i));
                while (e != null)
                {
                    if (e.prev != null)
                    {
                        // The relative index of the next event in the same list must be at least one higher then the
                        // previous event.
                        if (e.relativeIndex <= e.prev.relativeIndex)
                        {
                            // Update the relative index
//System.err.println("************** recalculating: update relative index: " + e.absoluteIndex + " key: " + e.key + " from: " + e.relativeIndex + " to: " + e.prev.relativeIndex + 1);
                            e.relativeIndex = e.prev.relativeIndex + 1;
                            if (e.key != -1)
                            {
                                reorderList.add(e);
                            }
                        }
                    }
                    if (e.link != null)
                    {
                        if (e.isOutgoing)
                        {
                            if (e.relativeIndex >= e.link.relativeIndex)
                            {
                                // This is a forward message with a relative index higer or equal to the event it links to.
                                // That means that the link and all subsequent events must also be recalculated.
//System.err.println("************** recalculating: add link for key: " + e.key);
                                v.add(e.link);
                            }
                        }
                        else
                        {
                            // This event is the receiving side of a message 
                            if (e.relativeIndex <= e.link.relativeIndex)
                            {
                                // Update the relative index to one more of the senders relativeIndex
//System.err.println("************** recalculating: update relative index from link: " + e.absoluteIndex+ " key: " + e.key + " from: " + e.relativeIndex + " to: " + e.link.relativeIndex + 1);
                                e.relativeIndex = e.link.relativeIndex + 1;
                                if (e.key != -1)
                                {
                                    reorderList.add(e);
                                }
                            }
                        }
                    }
                    // recalculate the next event in the process list
                    e = e.next;
                }
            }
            
            for (int i = 0; i < reorderList.size(); i++)
            {
                // Reorder the events in the total ordering by removing and re-inserting them
                e = (OrderedEvent)reorderList.get(i);
                sortedEventList.remove(e);
                sortedEventList.insert(e);
            }
        }                
    }
    
    /**
     * This class maintains the total ordering of events.
     * The current implementation is optimized to retrieve the EventRecord at a given index fast.
     * The performance of insert and remove operations is very poor when the number of events grow.
     * Eventualy, this should be replaced by a balanced tree with rank fields as described in
     * The Art of Computer Programming, volume 3, page 471 - 473.
     */
    class SortedEventList
    {
        private ArrayList totalOrder = new ArrayList();
        
        public OrderedEvent get(long i)
        {
            return (OrderedEvent)totalOrder.get((int)i);
        }
        
        public void insert(OrderedEvent e)
        {
            if (totalOrder.size() == 0)
            {
                totalOrder.add(0, e);
                return;
            }
            // Since most events will be inserted at the end of the list, search bottom up
            // for the first event earlier then the current event
            for (int i = totalOrder.size() - 1; i >= 0; i--)
            {
                if (((OrderedEvent)totalOrder.get(i)).compareTo(e) == -1)
                {
                    totalOrder.add(i + 1, e);
                    return;
                }
            }
            // No other entry was earlier. Hence, the current event is the earliest event.
            // Insert it at the top of the list.
            totalOrder.add(0, e); 
        }
        
        public void remove(OrderedEvent e)
        {
            for (int i = totalOrder.size() - 1; i >= 0; i--)
            {
                if (totalOrder.get(i) == e)
                {
                    totalOrder.remove(i);
                }
            }            
        }

        public long indexOf(OrderedEvent e)
        {
            return (long)totalOrder.indexOf(e);
        }
                
        public long indexOf(long key)
        {
            for (int i = totalOrder.size() - 1; i >= 0; i--)
            {
                if (((OrderedEvent)totalOrder.get(i)).key == key)
                {
                    return (long)i;
                }
            }
            throw new RuntimeException("invalid record key! " + key);            
        }
        
        public int size() {
            return totalOrder.size();
        }
    }        
}   