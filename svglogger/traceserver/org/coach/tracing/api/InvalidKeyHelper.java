package org.coach.tracing.api;

/** 
 * Helper class for : InvalidKey
 *  
 * @author OpenORB Compiler
 */ 
public class InvalidKeyHelper
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
     * Insert InvalidKey into an any
     * @param a an any
     * @param t InvalidKey value
     */
    public static void insert(org.omg.CORBA.Any a, org.coach.tracing.api.InvalidKey t)
    {
        a.insert_Streamable(new org.coach.tracing.api.InvalidKeyHolder(t));
    }

    /**
     * Extract InvalidKey from an any
     * @param a an any
     * @return the extracted InvalidKey value
     */
    public static org.coach.tracing.api.InvalidKey extract(org.omg.CORBA.Any a)
    {
        if (!a.type().equal(type()))
            throw new org.omg.CORBA.MARSHAL();
        if (HAS_OPENORB && a instanceof org.openorb.CORBA.Any) {
            // streamable extraction. The jdk stubs incorrectly define the Any stub
            org.openorb.CORBA.Any any = (org.openorb.CORBA.Any)a;
            try {
                org.omg.CORBA.portable.Streamable s = any.extract_Streamable();
                if(s instanceof org.coach.tracing.api.InvalidKeyHolder)
                    return ((org.coach.tracing.api.InvalidKeyHolder)s).value;
            } catch (org.omg.CORBA.BAD_INV_ORDER ex) {
            }
            org.coach.tracing.api.InvalidKeyHolder h = new org.coach.tracing.api.InvalidKeyHolder(read(a.create_input_stream()));
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
     * Return the InvalidKey TypeCode
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

                _tc = orb.create_exception_tc(id(),"InvalidKey",_members);
                _working = false;
            }
        }
        return _tc;
    }

    /**
     * Return the InvalidKey IDL ID
     * @return an ID
     */
    public static String id()
    {
        return _id;
    }

    private final static String _id = "IDL:org/coach/tracing/api/InvalidKey:1.0";

    /**
     * Read InvalidKey from a marshalled stream
     * @param istream the input stream
     * @return the readed InvalidKey value
     */
    public static org.coach.tracing.api.InvalidKey read(org.omg.CORBA.portable.InputStream istream)
    {
        org.coach.tracing.api.InvalidKey new_one = new org.coach.tracing.api.InvalidKey();

        if (!istream.read_string().equals(id()))
         throw new org.omg.CORBA.MARSHAL();

        return new_one;
    }

    /**
     * Write InvalidKey into a marshalled stream
     * @param ostream the output stream
     * @param value InvalidKey value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, org.coach.tracing.api.InvalidKey value)
    {
        ostream.write_string(id());
    }

}
