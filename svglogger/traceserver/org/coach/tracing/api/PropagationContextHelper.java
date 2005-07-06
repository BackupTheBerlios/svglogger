package org.coach.tracing.api;

/** 
 * Helper class for : PropagationContext
 *  
 * @author OpenORB Compiler
 */ 
public class PropagationContextHelper
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
     * Insert PropagationContext into an any
     * @param a an any
     * @param t PropagationContext value
     */
    public static void insert(org.omg.CORBA.Any a, org.coach.tracing.api.PropagationContext t)
    {
        a.insert_Streamable(new org.coach.tracing.api.PropagationContextHolder(t));
    }

    /**
     * Extract PropagationContext from an any
     * @param a an any
     * @return the extracted PropagationContext value
     */
    public static org.coach.tracing.api.PropagationContext extract(org.omg.CORBA.Any a)
    {
        if (!a.type().equal(type()))
            throw new org.omg.CORBA.MARSHAL();
        if (HAS_OPENORB && a instanceof org.openorb.CORBA.Any) {
            // streamable extraction. The jdk stubs incorrectly define the Any stub
            org.openorb.CORBA.Any any = (org.openorb.CORBA.Any)a;
            try {
                org.omg.CORBA.portable.Streamable s = any.extract_Streamable();
                if(s instanceof org.coach.tracing.api.PropagationContextHolder)
                    return ((org.coach.tracing.api.PropagationContextHolder)s).value;
            } catch (org.omg.CORBA.BAD_INV_ORDER ex) {
            }
            org.coach.tracing.api.PropagationContextHolder h = new org.coach.tracing.api.PropagationContextHolder(read(a.create_input_stream()));
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
     * Return the PropagationContext TypeCode
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
                org.omg.CORBA.StructMember []_members = new org.omg.CORBA.StructMember[3];

                _members[0] = new org.omg.CORBA.StructMember();
                _members[0].name = "trail_label";
                _members[0].type = orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_string);
                _members[1] = new org.omg.CORBA.StructMember();
                _members[1].name = "message_id";
                _members[1].type = orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_string);
                _members[2] = new org.omg.CORBA.StructMember();
                _members[2].name = "trail_id";
                _members[2].type = orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_string);
                _tc = orb.create_struct_tc(id(),"PropagationContext",_members);
                _working = false;
            }
        }
        return _tc;
    }

    /**
     * Return the PropagationContext IDL ID
     * @return an ID
     */
    public static String id()
    {
        return _id;
    }

    private final static String _id = "IDL:org/coach/tracing/api/PropagationContext:1.0";

    /**
     * Read PropagationContext from a marshalled stream
     * @param istream the input stream
     * @return the readed PropagationContext value
     */
    public static org.coach.tracing.api.PropagationContext read(org.omg.CORBA.portable.InputStream istream)
    {
        org.coach.tracing.api.PropagationContext new_one = new org.coach.tracing.api.PropagationContext();

        new_one.trail_label = istream.read_string();
        new_one.message_id = istream.read_string();
        new_one.trail_id = istream.read_string();

        return new_one;
    }

    /**
     * Write PropagationContext into a marshalled stream
     * @param ostream the output stream
     * @param value PropagationContext value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, org.coach.tracing.api.PropagationContext value)
    {
        ostream.write_string(value.trail_label);
        ostream.write_string(value.message_id);
        ostream.write_string(value.trail_id);
    }

}
