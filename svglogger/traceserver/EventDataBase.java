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
import java.io.*;
import java.util.*;

public class EventDataBase
{
    private static int           CashSize           = 1001;
    private EventRecord[]        eventCash          = new EventRecord[CashSize];

    private RandomAccessFile     valueFile          = null;
    private RandomAccessFile     eventFile          = null;
    private Hashtable            nameIndexTable     = new Hashtable();
    private Hashtable            indexNameTable     = new Hashtable();
    private HashMap              identityTreeTable  = new HashMap();
    private HashMap              identityIndexTable = new HashMap();
    private Identity             rootIdentity       = new Identity(IdentityKind.CCM_NODE, "", "", (long)-1);
    private static Long          rootKey            = new Long(-1);
    private static HashMap       trailMap           = new HashMap();
    private static HashMap       unmatched          = new HashMap();
    private static EventSorter   eventSorter        = null;
    private static EventDataBase eventDB            = null;

    private File    dbDir           = null;
    private long    eventCount      = 0;
    private int     identityCount   = 0;
    private long    messageCount    = 0;
    private int     nameIndex       = 0;
    private int     unmatchCount    = 0;
    
    public static EventDataBase getEventDB()
    {
        if (eventDB == null)
        {
            eventDB = new EventDataBase();
            eventSorter = new EventSorter(eventDB);
        }
        return eventDB;
    }
                 
    private EventDataBase()
    {
        if (eventFile == null)
        {
            try
            {
                identityTreeTable.put(rootIdentity, new HashMap());
                identityIndexTable.put(rootKey, rootIdentity);
                
                dbDir = new File(System.getProperty("coach.tracing.db.dir", "."));
                dbDir.mkdirs();
                
                File f = new File(dbDir, "value.db");
                if (f.exists())
                {
                    f.delete();
                }
                valueFile = new RandomAccessFile(f.getPath(), "rw");

                f = new File(dbDir, "event.db");
                if (f.exists())
                {
                    f.delete();
                }
                eventFile = new RandomAccessFile(f.getPath(), "rw");

                f = new File(dbDir, "identity.db");
                if (f.exists())
                {
                    f.delete();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

//    public EventDataBase(String name)
//    {
//        load(name);
//    }

    public synchronized void save(String name)
    {
        try
        {
            File dbDir = new File(System.getProperty("coach.event.db", "."));
            File saveDir = new File(dbDir, name);
            eventFile.close();
            valueFile.close();   
            saveDir.mkdirs();

            saveIdentities(new File(saveDir, "identity.db"));
            
            File f1 = new File(saveDir, "names.db");
            PrintWriter nameFile = new PrintWriter(new FileWriter(f1.getPath()));
            for (int i = 0; i < nameIndex; i++)
            {
                nameFile.println(readName(i));
            }
            nameFile.close();
            
            f1 = new File(dbDir, "value.db");
            File f2 = new File(saveDir, "value.db");
            f1.renameTo(f2);
            valueFile = new RandomAccessFile(f1.getPath(), "rw");

            f1 = new File(dbDir, "event.db");
            f2 = new File(saveDir, "event.db");
            f1.renameTo(f2);
            eventFile = new RandomAccessFile(f1.getPath(), "rw");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException("Failed to save events");
        }
    }
    
    public synchronized void load(String name)
    {
        try
        {    
            File dbDir = new File(System.getProperty("coach.event.db", "."));
            File saveDir = new File(dbDir, name);
            File f = new File(saveDir, "names.db");

            BufferedReader nameFile = new BufferedReader(new FileReader(f.getPath()));
            nameIndexTable.clear();
            indexNameTable.clear();
            identityTreeTable.clear();
            identityIndexTable.clear();
            identityCount = 0;
            
            nameIndex = 0;
            String line = nameFile.readLine();
            while (line != null)
            {
                writeName(line);
                line = nameFile.readLine();
            }
            nameFile.close();

            f = new File(saveDir, "value.db");
            valueFile = new RandomAccessFile(f.getPath(), "rw");
            f = new File(saveDir, "event.db");
            eventFile = new RandomAccessFile(f.getPath(), "rw");

//            eventCount = keyFile.length() / 8;
            
            f = new File(saveDir, "identity.db");
            loadIdentities(f);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException("Failed to load events");
        }
    }
    
    public synchronized void clear()
    {
        try
        {
            // close all open files
            if (eventFile != null)
            {
                eventFile.close();
                valueFile.close();   
            }

            File f = new File(dbDir, "value.db");
            if (f.exists())
            {
                f.delete();
            }
            valueFile = new RandomAccessFile(f.getPath(), "rw");

            f = new File(dbDir, "event.db");
            if (f.exists())
            {
                f.delete();
            }
            eventFile = new RandomAccessFile(f.getPath(), "rw");

            f = new File(dbDir, "identity.db");
            if (f.exists())
            {
                f.delete();
            }

            f = new File(dbDir, "key.db");
            if (f.exists())
            {
                f.delete();
            }
            nameIndexTable.clear();
            indexNameTable.clear();
            nameIndex = 0;
            eventCount = 0;
            identityCount = 0;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }        
    }

    public synchronized void addEvents(TraceEvent[] events)
    {
        for (int i = 0; i < events.length; i++)
        {
            EventRecord r = new EventRecord(events[i]);
            writeEvent(r);      
        }
    }
    
    public synchronized EventRecord getEventAt(long index)
    {
        return eventSorter.getEventAt(index);
    }

    public synchronized long getEventIndex(long key)
    {
        return eventSorter.getEventIndex(key);
    }

    public synchronized EventRecord[] getEvents(long start, long end)
    {
        return eventSorter.getEvents(start, end);
    }
    
    public void link(EventRecord r1, EventRecord r2)
    {
        r2.tr.linkKey = r1.getKey();
        r1.tr.linkKey = r2.getKey();
        updateEvent(r1);
        updateEvent(r2);       
        messageCount++;
    }
    
    private String readName(int index)
    {
        return (String)indexNameTable.get(new Integer(index));
    }            

    private int writeName(String name)
    {
        if (name == null)
        {
            throw new RuntimeException("name should not be null");
        }
        Integer index = (Integer)nameIndexTable.get(name);
        if (index == null)
        {
            index = new Integer(nameIndex++);
            nameIndexTable.put(name, index);
            indexNameTable.put(index, name);
        }
        return index.intValue();
    }  

    private long writeValue(IdlNode idlNode)
    {
        if (idlNode == null)
        {
            // no parameter infromation available.
            return -1;
        }
        try
        {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bout);
            out.writeObject(idlNode);
            out.close();
            byte[] buf = bout.toByteArray();
            long index = valueFile.length();
            valueFile.seek(index);
            valueFile.writeInt(buf.length);
            valueFile.write(buf);
            return index;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        throw new RuntimeException("Failed to write event");
    }

    public String getParameterValues(long key) {
        IdlNode idlNode = readValue(key);
        if (idlNode == null) {
            // no parameter information available.
            return "";
        }
        XmlWriter w = new XmlWriter();
        XmlNode.write(idlNode, w);
        return w.toString();
    }

    public IdlNode readValue(long index)
    {
        if (index == -1)
        {
            // no paramater information available
            return null;
        }
        try
        {
            valueFile.seek(index);
            int size = valueFile.readInt();
            byte[] buf = new byte[size];
            valueFile.read(buf);
            ByteArrayInputStream bin = new ByteArrayInputStream(buf);
            ObjectInputStream in = new ObjectInputStream(bin);
            IdlNode n = (IdlNode)in.readObject();
            in.close();
            return n;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        throw new RuntimeException("Failed to read value");
    }
   
    public long getEventCount()
    {
        return eventCount;
    }

    public int getIdentityCount()
    {
        return identityCount;
    }
    
    public long getMessageCount()
    {
        return messageCount;
    }

    public long getUnmatchedEventCount()
    {
        return eventCount - (messageCount * 2);
    }
            
    public synchronized void writeEvent(EventRecord e)
    {
        try
        {
            e.tr.key_id = eventFile.length(); 
            writeEvent(e, e.tr.key_id);
            eventCash[(int)(e.tr.key_id % CashSize)] = e;
            eventCount++;

            match(e);

            eventSorter.add(e);
            return;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        throw new RuntimeException("Failed to write event");
    }

    public synchronized void updateEvent(EventRecord e)
    {
        writeEvent(e, e.tr.key_id);
        eventCash[(int)(e.tr.key_id % CashSize)] = e;
    }
    
    private void writeEvent(EventRecord e, long index)
    {
        try
        {
            eventFile.seek(e.tr.key_id);
            // Take care that the write and read order match!
            eventFile.writeLong(e.tr.key_id);
            if (e.identity != null)
            {
                e.tr.identityKey = writeIdentity(e.identity);
            }
            eventFile.writeLong(e.tr.identityKey);
            eventFile.writeInt(writeName(e.tr.trail_label));
            eventFile.writeInt(writeName(e.tr.message_id));
            eventFile.writeInt(writeName(e.tr.trail_id));
            eventFile.writeInt(e.tr.event_counter);
            eventFile.writeInt(writeName(e.tr.op_name));
            eventFile.writeInt(e.tr.interaction_point.value());
            e.tr.parametersKey = writeValue(e.idlNode);
            eventFile.writeLong(e.tr.parametersKey);  
            eventFile.writeLong(e.tr.linkKey);
            eventFile.writeInt(writeName(e.tr.thread_id));
            eventFile.writeLong(e.tr.time_stamp);
            return;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        throw new RuntimeException("Failed to write event");
    }       

    public synchronized EventRecord readEvent(long key)
    {
        EventRecord e = eventCash[(int)(key % CashSize)];
        if (e != null && e.getKey() == key)
        {
            return e;
        }
       
        try
        {
            eventFile.seek(key);

            e = new EventRecord();
            // Take care that the write and read order match!
            e.tr.key_id = eventFile.readLong();
            if (e.tr.key_id != key)
            {
                throw new RuntimeException("Key does not match database record key value!");
            }
            e.tr.identityKey = eventFile.readLong();
            e.tr.trail_label = readName(eventFile.readInt());
            e.tr.message_id = readName(eventFile.readInt());
            e.tr.trail_id = readName(eventFile.readInt());
            e.tr.event_counter = eventFile.readInt();
            e.tr.op_name = readName(eventFile.readInt());
            e.tr.interaction_point = InteractionPoint.from_int(eventFile.readInt());
            e.tr.parametersKey = eventFile.readLong();
            e.tr.linkKey = eventFile.readLong();
            e.tr.thread_id = readName(eventFile.readInt());
            e.tr.time_stamp = eventFile.readLong();

            eventCash[(int)(e.tr.key_id % CashSize)] = e;

            return e;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        throw new RuntimeException("Failed to read event");
    }

    private long writeIdentity(IdentityDescriptor d)
    {
        Long parent = rootKey;
        Identity id = null;

        Long key = getIdentityKey(parent, d.node_name);

        if (key == null)
        {
            id = new Identity();
            id.kind = IdentityKind.CCM_NODE;
            id.name = d.node_name;
            id.type = d.node_ip;
            id.linkKey = parent.longValue();
            key = putIdentity(parent, id);
        }
        parent = key;

        key = getIdentityKey(parent, d.process_id);

        if (key == null)
        {
            id = new Identity();
            id.kind = IdentityKind.CCM_PROCESS;
            id.name = d.process_id;
            id.type = "";
            id.linkKey = parent.longValue();
            key = putIdentity(parent, id);
        }

        parent = key;

        if (!d.cnt_name.equals(""))
        {
            key = getIdentityKey(parent, d.cnt_name);

            if (key == null)
            {
                id = new Identity();
                id.kind = IdentityKind.CCM_CONTAINER;
                id.name = d.cnt_name;
                id.type = d.cnt_type;
                id.linkKey = parent.longValue();
                key = putIdentity(parent, id);
            }

            parent = key;
        }

        if (!d.cmp_name.equals(""))
        {
            key = getIdentityKey(parent, d.cmp_name);

            if (key == null)
            {
                id = new Identity();
                id.kind = IdentityKind.CCM_COMPONENT;
                id.name = d.cmp_name;
                id.type = d.cmp_type;
                id.linkKey = parent.longValue();
                key = putIdentity(parent, id);
            }

            parent = key;
        }

        key = getIdentityKey(parent, d.object_instance_id);

        if (key == null)
        {
            id = new Identity();
            id.kind = IdentityKind.CCM_OBJECT;
            id.name = d.object_instance_id;
            id.type = d.object_repository_id;
            id.linkKey = parent.longValue();
            key = putIdentity(parent, id);
        }

        return key.longValue();
    }

    private Long getIdentityKey(Long parent, String name)
    {
        Identity id = (Identity)identityIndexTable.get(parent);

        if (id == null)
            throw new RuntimeException("unexpected null entry in identityIndexTable for: " + parent);

        HashMap t = (HashMap)identityTreeTable.get(id);

        if (t == null)
            return null;

        return (Long)t.get(name);
    }

    private Long putIdentity(Long parent, Identity id)
    {
        Identity parentId = (Identity)identityIndexTable.get(parent);

        if (parentId == null)
            throw new RuntimeException("unexpected null entry in identityIndexTable for: " + parent);

        // Get the child map for this parent.

        HashMap t = (HashMap)identityTreeTable.get(parentId);

        if (t == null)
        {
            // there are no children yet, create the child map

            t = new HashMap();

            identityTreeTable.put(parentId, t);
        }

        // Get the key if it already exists.

        Long key = (Long)t.get(id.name);

        if (key == null)
        {
            // The identity is new, add it to the tree.

            key = new Long(identityCount++);

            identityIndexTable.put(key, id);

            t.put(id.name, key);
        }

        return key;
    }

    private void saveIdentities(File f)
    {
        try
        {
            DataOutputStream identityFile = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(f)));

            for (long i = 0; i < identityCount; i++)
            {
                Identity id = (Identity)identityIndexTable.get(new Long(i));
                identityFile.writeInt(id.kind.value());
                identityFile.writeInt(writeName(id.name));
                identityFile.writeInt(writeName(id.type));
                identityFile.writeLong(id.linkKey);
            }

            identityFile.close();
        }
        catch (Exception _ex)
        {
            _ex.printStackTrace();

            throw new RuntimeException("Failed to write identity");
        }
    }

    private void loadIdentities(File f)
    {
        try
        {
            DataInputStream identityFile = new DataInputStream(new BufferedInputStream(new FileInputStream(f)));
            identityCount = 0;

            while(identityFile.available() > 0)
            {
                Identity id = new Identity();
                id.kind = IdentityKind.from_int(identityFile.readInt());
                id.name = readName(identityFile.readInt());
                id.type = readName(identityFile.readInt());
                id.linkKey = identityFile.readLong();
                identityIndexTable.put(new Long(identityCount++), id);
            }
            identityFile.close();
        }
        catch (Exception _ex)
        {
            _ex.printStackTrace();
            throw new RuntimeException("Failed to read identity");
        }
    }
            
    public Identity getIdentity(Long key)
    {
        return (Identity)identityIndexTable.get(key);
    }

    private void match(EventRecord r)
    {
        try
        {
            String messageId = r.getMessageId();
            // distinguish forward messages from return messages
            
            EventRecord r2 = (EventRecord)unmatched.get(messageId);
            if (r2 != null)
            {
                link(r, r2);
                unmatched.remove(messageId);
                unmatchCount--;
            }
            else
            {                        
                unmatched.put(messageId, r);
                unmatchCount++;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}

