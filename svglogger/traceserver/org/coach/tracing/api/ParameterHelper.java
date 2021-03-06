package org.coach.tracing.api;

/** 
 * Helper class for : Parameter
 *  
 * @author OpenORB Compiler
 */ 
public class ParameterHelper
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
     * Insert Parameter into an any
     * @param a an any
     * @param t Parameter value
     */
    public static void insert(org.omg.CORBA.Any a, org.coach.tracing.api.Parameter t)
    {
        a.insert_Streamable(new org.coach.tracing.api.ParameterHolder(t));
    }

    /**
     * Extract Parameter from an any
     * @param a an any
     * @return the extracted Parameter value
     */
    public static org.coach.tracing.api.Parameter extract(org.omg.CORBA.Any a)
    {
        if (!a.type().equal(type()))
            throw new org.omg.CORBA.MARSHAL();
        if (HAS_OPENORB && a instanceof org.openorb.CORBA.Any) {
            // streamable extraction. The jdk stubs incorrectly define the Any stub
            org.openorb.CORBA.Any any = (org.openorb.CORBA.Any)a;
            try {
                org.omg.CORBA.portable.Streamable s = any.extract_Streamable();
                if(s instanceof org.coach.tracing.api.ParameterHolder)
                    return ((org.coach.tracing.api.ParameterHolder)s).value;
            } catch (org.omg.CORBA.BAD_INV_ORDER ex) {
            }
            org.coach.tracing.api.ParameterHolder h = new org.coach.tracing.api.ParameterHolder(read(a.create_input_stream()));
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
     * Return the Parameter TypeCode
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
                _members[0].name = "dir";
                _members[0].type = orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_string);
                _members[1] = new org.omg.CORBA.StructMember();
                _members[1].name = "type";
                _members[1].type = orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_string);
                _members[2] = new org.omg.CORBA.StructMember();
                _members[2].name = "name";
                _members[2].type = orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_string);
                _tc = orb.create_struct_tc(id(),"Parameter",_members);
                _working = false;
            }
        }
        return _tc;
    }

    /**
     * Return the Parameter IDL ID
     * @return an ID
     */
    public static String id()
    {
        return _id;
    }

    private final static String _id = "IDL:org/coach/tracing/api/Parameter:1.0";

    /**
     * Read Parameter from a marshalled stream
     * @param istream the input stream
     * @return the readed Parameter value
     */
    public static org.coach.tracing.api.Parameter read(org.omg.CORBA.portable.InputStream istream)
    {
        org.coach.tracing.api.Parameter new_one = new org.coach.tracing.api.Parameter();

        new_one.dir = istream.read_string();
        new_one.type = istream.read_string();
        new_one.name = istream.read_string();

        return new_one;
    }

    /**
     * Write Parameter into a marshalled stream
     * @param ostream the output stream
     * @param value Parameter value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, org.coach.tracing.api.Parameter value)
    {
        ostream.write_string(value.dir);
        ostream.write_string(value.type);
        ostream.write_string(value.name);
    }

}
