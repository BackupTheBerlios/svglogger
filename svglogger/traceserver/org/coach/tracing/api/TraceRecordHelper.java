package org.coach.tracing.api;

/** 
 * Helper class for : TraceRecord
 *  
 * @author OpenORB Compiler
 */ 
public class TraceRecordHelper
{
    private static final boolean HAS_OPENORB;
    static {
        boolean hasOpenORB = false;
        try {
            Thread.currentThread().getContextClassLoader().loadClass("org.openorb.CORBA.Any");
            hasOpenORB = true;
        }
        catch(ClassNotFoundException ex) {
        }
        HAS_OPENORB = hasOpenORB;
    }
    /**
     * Insert TraceRecord into an any
     * @param a an any
     * @param t TraceRecord value
     */
    public static void insert(org.omg.CORBA.Any a, org.coach.tracing.api.TraceRecord t)
    {
        a.insert_Streamable(new org.coach.tracing.api.TraceRecordHolder(t));
    }

    /**
     * Extract TraceRecord from an any
     * @param a an any
     * @return the extracted TraceRecord value
     */
    public static org.coach.tracing.api.TraceRecord extract(org.omg.CORBA.Any a)
    {
        if (!a.type().equal(type()))
            throw new org.omg.CORBA.MARSHAL();
        if (HAS_OPENORB && a instanceof org.openorb.CORBA.Any) {
            // streamable extraction. The jdk stubs incorrectly define the Any stub
            org.openorb.CORBA.Any any = (org.openorb.CORBA.Any)a;
            try {
                org.omg.CORBA.portable.Streamable s = any.extract_Streamable();
                if(s instanceof org.coach.tracing.api.TraceRecordHolder)
                    return ((org.coach.tracing.api.TraceRecordHolder)s).value;
            } catch (org.omg.CORBA.BAD_INV_ORDER ex) {
            }
            org.coach.tracing.api.TraceRecordHolder h = new org.coach.tracing.api.TraceRecordHolder(read(a.create_input_stream()));
            a.insert_Streamable(h);
            return h.value;
        }
        return read(a.create_input_stream());
    }

    //
    // Internal TypeCode value
    //
    private static org.omg.CORBA.TypeCode _tc = null;
    private static boolean _working = false;

    /**
     * Return the TraceRecord TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type()
    {
        if (_tc == null) {
            synchronized(org.omg.CORBA.TypeCode.class) {
                if (_tc != null)
                    return _tc;
                if (_working)
                    return org.omg.CORBA.ORB.init().create_recursive_tc(id());
                _working = true;
                org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
                org.omg.CORBA.StructMember []_members = new org.omg.CORBA.StructMember[12];

                _members[0] = new org.omg.CORBA.StructMember();
                _members[0].name = "key_id";
                _members[0].type = orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_longlong);
                _members[1] = new org.omg.CORBA.StructMember();
                _members[1].name = "time_stamp";
                _members[1].type = orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_longlong);
                _members[2] = new org.omg.CORBA.StructMember();
                _members[2].name = "trail_label";
                _members[2].type = orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_string);
                _members[3] = new org.omg.CORBA.StructMember();
                _members[3].name = "message_id";
                _members[3].type = orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_string);
                _members[4] = new org.omg.CORBA.StructMember();
                _members[4].name = "trail_id";
                _members[4].type = orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_string);
                _members[5] = new org.omg.CORBA.StructMember();
                _members[5].name = "event_counter";
                _members[5].type = orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_long);
                _members[6] = new org.omg.CORBA.StructMember();
                _members[6].name = "thread_id";
                _members[6].type = orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_string);
                _members[7] = new org.omg.CORBA.StructMember();
                _members[7].name = "op_name";
                _members[7].type = orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_string);
                _members[8] = new org.omg.CORBA.StructMember();
                _members[8].name = "interaction_point";
                _members[8].type = org.coach.tracing.api.InteractionPointHelper.type();
                _members[9] = new org.omg.CORBA.StructMember();
                _members[9].name = "identityKey";
                _members[9].type = orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_longlong);
                _members[10] = new org.omg.CORBA.StructMember();
                _members[10].name = "parametersKey";
                _members[10].type = orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_longlong);
                _members[11] = new org.omg.CORBA.StructMember();
                _members[11].name = "linkKey";
                _members[11].type = orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_longlong);
                _tc = orb.create_struct_tc(id(),"TraceRecord",_members);
                _working = false;
            }
        }
        return _tc;
    }

    /**
     * Return the TraceRecord IDL ID
     * @return an ID
     */
    public static String id()
    {
        return _id;
    }

    private final static String _id = "IDL:org/coach/tracing/api/TraceRecord:1.0";

    /**
     * Read TraceRecord from a marshalled stream
     * @param istream the input stream
     * @return the readed TraceRecord value
     */
    public static org.coach.tracing.api.TraceRecord read(org.omg.CORBA.portable.InputStream istream)
    {
        org.coach.tracing.api.TraceRecord new_one = new org.coach.tracing.api.TraceRecord();

        new_one.key_id = istream.read_longlong();
        new_one.time_stamp = istream.read_longlong();
        new_one.trail_label = istream.read_string();
        new_one.message_id = istream.read_string();
        new_one.trail_id = istream.read_string();
        new_one.event_counter = istream.read_long();
        new_one.thread_id = istream.read_string();
        new_one.op_name = istream.read_string();
        new_one.interaction_point = org.coach.tracing.api.InteractionPointHelper.read(istream);
        new_one.identityKey = istream.read_longlong();
        new_one.parametersKey = istream.read_longlong();
        new_one.linkKey = istream.read_longlong();

        return new_one;
    }

    /**
     * Write TraceRecord into a marshalled stream
     * @param ostream the output stream
     * @param value TraceRecord value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, org.coach.tracing.api.TraceRecord value)
    {
        ostream.write_longlong(value.key_id);
        ostream.write_longlong(value.time_stamp);
        ostream.write_string(value.trail_label);
        ostream.write_string(value.message_id);
        ostream.write_string(value.trail_id);
        ostream.write_long(value.event_counter);
        ostream.write_string(value.thread_id);
        ostream.write_string(value.op_name);
        org.coach.tracing.api.InteractionPointHelper.write(ostream,value.interaction_point);
        ostream.write_longlong(value.identityKey);
        ostream.write_longlong(value.parametersKey);
        ostream.write_longlong(value.linkKey);
    }

}
