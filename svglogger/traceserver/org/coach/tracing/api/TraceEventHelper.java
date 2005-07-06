package org.coach.tracing.api;

/** 
 * Helper class for : TraceEvent
 *  
 * @author OpenORB Compiler
 */ 
public class TraceEventHelper
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
     * Insert TraceEvent into an any
     * @param a an any
     * @param t TraceEvent value
     */
    public static void insert(org.omg.CORBA.Any a, org.coach.tracing.api.TraceEvent t)
    {
        a.insert_Streamable(new org.coach.tracing.api.TraceEventHolder(t));
    }

    /**
     * Extract TraceEvent from an any
     * @param a an any
     * @return the extracted TraceEvent value
     */
    public static org.coach.tracing.api.TraceEvent extract(org.omg.CORBA.Any a)
    {
        if (!a.type().equal(type()))
            throw new org.omg.CORBA.MARSHAL();
        if (HAS_OPENORB && a instanceof org.openorb.CORBA.Any) {
            // streamable extraction. The jdk stubs incorrectly define the Any stub
            org.openorb.CORBA.Any any = (org.openorb.CORBA.Any)a;
            try {
                org.omg.CORBA.portable.Streamable s = any.extract_Streamable();
                if(s instanceof org.coach.tracing.api.TraceEventHolder)
                    return ((org.coach.tracing.api.TraceEventHolder)s).value;
            } catch (org.omg.CORBA.BAD_INV_ORDER ex) {
            }
            org.coach.tracing.api.TraceEventHolder h = new org.coach.tracing.api.TraceEventHolder(read(a.create_input_stream()));
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
     * Return the TraceEvent TypeCode
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
                org.omg.CORBA.StructMember []_members = new org.omg.CORBA.StructMember[11];

                _members[0] = new org.omg.CORBA.StructMember();
                _members[0].name = "time_stamp";
                _members[0].type = orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_longlong);
                _members[1] = new org.omg.CORBA.StructMember();
                _members[1].name = "interaction_point";
                _members[1].type = org.coach.tracing.api.InteractionPointHelper.type();
                _members[2] = new org.omg.CORBA.StructMember();
                _members[2].name = "trail_label";
                _members[2].type = orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_string);
                _members[3] = new org.omg.CORBA.StructMember();
                _members[3].name = "message_id";
                _members[3].type = orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_string);
                _members[4] = new org.omg.CORBA.StructMember();
                _members[4].name = "thread_id";
                _members[4].type = orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_string);
                _members[5] = new org.omg.CORBA.StructMember();
                _members[5].name = "trail_id";
                _members[5].type = orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_string);
                _members[6] = new org.omg.CORBA.StructMember();
                _members[6].name = "event_counter";
                _members[6].type = orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_long);
                _members[7] = new org.omg.CORBA.StructMember();
                _members[7].name = "op_name";
                _members[7].type = orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_string);
                _members[8] = new org.omg.CORBA.StructMember();
                _members[8].name = "identity";
                _members[8].type = org.coach.tracing.api.IdentityDescriptorHelper.type();
                _members[9] = new org.omg.CORBA.StructMember();
                _members[9].name = "parameters";
                _members[9].type = org.coach.tracing.api.ParameterListHelper.type();
                _members[10] = new org.omg.CORBA.StructMember();
                _members[10].name = "parameter_values";
                _members[10].type = org.coach.tracing.api.AnyListHelper.type();
                _tc = orb.create_struct_tc(id(),"TraceEvent",_members);
                _working = false;
            }
        }
        return _tc;
    }

    /**
     * Return the TraceEvent IDL ID
     * @return an ID
     */
    public static String id()
    {
        return _id;
    }

    private final static String _id = "IDL:org/coach/tracing/api/TraceEvent:1.0";

    /**
     * Read TraceEvent from a marshalled stream
     * @param istream the input stream
     * @return the readed TraceEvent value
     */
    public static org.coach.tracing.api.TraceEvent read(org.omg.CORBA.portable.InputStream istream)
    {
        org.coach.tracing.api.TraceEvent new_one = new org.coach.tracing.api.TraceEvent();

        new_one.time_stamp = istream.read_longlong();
        new_one.interaction_point = org.coach.tracing.api.InteractionPointHelper.read(istream);
        new_one.trail_label = istream.read_string();
        new_one.message_id = istream.read_string();
        new_one.thread_id = istream.read_string();
        new_one.trail_id = istream.read_string();
        new_one.event_counter = istream.read_long();
        new_one.op_name = istream.read_string();
        new_one.identity = org.coach.tracing.api.IdentityDescriptorHelper.read(istream);
        new_one.parameters = org.coach.tracing.api.ParameterListHelper.read(istream);
        new_one.parameter_values = org.coach.tracing.api.AnyListHelper.read(istream);

        return new_one;
    }

    /**
     * Write TraceEvent into a marshalled stream
     * @param ostream the output stream
     * @param value TraceEvent value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, org.coach.tracing.api.TraceEvent value)
    {
        ostream.write_longlong(value.time_stamp);
        org.coach.tracing.api.InteractionPointHelper.write(ostream,value.interaction_point);
        ostream.write_string(value.trail_label);
        ostream.write_string(value.message_id);
        ostream.write_string(value.thread_id);
        ostream.write_string(value.trail_id);
        ostream.write_long(value.event_counter);
        ostream.write_string(value.op_name);
        org.coach.tracing.api.IdentityDescriptorHelper.write(ostream,value.identity);
        org.coach.tracing.api.ParameterListHelper.write(ostream,value.parameters);
        org.coach.tracing.api.AnyListHelper.write(ostream,value.parameter_values);
    }

}
