package org.coach.tracing.api;

/** 
 * Helper class for : InvalidName
 *  
 * @author OpenORB Compiler
 */ 
public class InvalidNameHelper
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
     * Insert InvalidName into an any
     * @param a an any
     * @param t InvalidName value
     */
    public static void insert(org.omg.CORBA.Any a, org.coach.tracing.api.InvalidName t)
    {
        a.insert_Streamable(new org.coach.tracing.api.InvalidNameHolder(t));
    }

    /**
     * Extract InvalidName from an any
     * @param a an any
     * @return the extracted InvalidName value
     */
    public static org.coach.tracing.api.InvalidName extract(org.omg.CORBA.Any a)
    {
        if (!a.type().equal(type()))
            throw new org.omg.CORBA.MARSHAL();
        if (HAS_OPENORB && a instanceof org.openorb.CORBA.Any) {
            // streamable extraction. The jdk stubs incorrectly define the Any stub
            org.openorb.CORBA.Any any = (org.openorb.CORBA.Any)a;
            try {
                org.omg.CORBA.portable.Streamable s = any.extract_Streamable();
                if(s instanceof org.coach.tracing.api.InvalidNameHolder)
                    return ((org.coach.tracing.api.InvalidNameHolder)s).value;
            } catch (org.omg.CORBA.BAD_INV_ORDER ex) {
            }
            org.coach.tracing.api.InvalidNameHolder h = new org.coach.tracing.api.InvalidNameHolder(read(a.create_input_stream()));
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
     * Return the InvalidName TypeCode
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
                org.omg.CORBA.StructMember []_members = new org.omg.CORBA.StructMember[0];

                _tc = orb.create_exception_tc(id(),"InvalidName",_members);
                _working = false;
            }
        }
        return _tc;
    }

    /**
     * Return the InvalidName IDL ID
     * @return an ID
     */
    public static String id()
    {
        return _id;
    }

    private final static String _id = "IDL:org/coach/tracing/api/InvalidName:1.0";

    /**
     * Read InvalidName from a marshalled stream
     * @param istream the input stream
     * @return the readed InvalidName value
     */
    public static org.coach.tracing.api.InvalidName read(org.omg.CORBA.portable.InputStream istream)
    {
        org.coach.tracing.api.InvalidName new_one = new org.coach.tracing.api.InvalidName();

        if (!istream.read_string().equals(id()))
         throw new org.omg.CORBA.MARSHAL();

        return new_one;
    }

    /**
     * Write InvalidName into a marshalled stream
     * @param ostream the output stream
     * @param value InvalidName value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, org.coach.tracing.api.InvalidName value)
    {
        ostream.write_string(id());
    }

}
