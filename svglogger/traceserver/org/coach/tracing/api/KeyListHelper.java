package org.coach.tracing.api;

/** 
 * Helper class for : KeyList
 *  
 * @author OpenORB Compiler
 */ 
public class KeyListHelper
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
     * Insert KeyList into an any
     * @param a an any
     * @param t KeyList value
     */
    public static void insert(org.omg.CORBA.Any a, long[] t)
    {
        a.insert_Streamable(new org.coach.tracing.api.KeyListHolder(t));
    }

    /**
     * Extract KeyList from an any
     * @param a an any
     * @return the extracted KeyList value
     */
    public static long[] extract(org.omg.CORBA.Any a)
    {
        if (!a.type().equal(type()))
            throw new org.omg.CORBA.MARSHAL();
        if(HAS_OPENORB && a instanceof org.openorb.CORBA.Any) {
            // streamable extraction. The jdk stubs incorrectly define the Any stub
            org.openorb.CORBA.Any any = (org.openorb.CORBA.Any)a;
            try {
                org.omg.CORBA.portable.Streamable s = any.extract_Streamable();
                if(s instanceof org.coach.tracing.api.KeyListHolder)
                    return ((org.coach.tracing.api.KeyListHolder)s).value;
            } catch (org.omg.CORBA.BAD_INV_ORDER ex) {
            }
            org.coach.tracing.api.KeyListHolder h = new org.coach.tracing.api.KeyListHolder(read(a.create_input_stream()));
            a.insert_Streamable(h);
            return h.value;
        }
        return read(a.create_input_stream());
    }

    //
    // Internal TypeCode value
    //
    private static org.omg.CORBA.TypeCode _tc = null;

    /**
     * Return the KeyList TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type()
    {
        if (_tc == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            _tc = orb.create_alias_tc(id(),"KeyList",orb.create_sequence_tc(0,orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_longlong)));
        }
        return _tc;
    }

    /**
     * Return the KeyList IDL ID
     * @return an ID
     */
    public static String id()
    {
        return _id;
    }

    private final static String _id = "IDL:org/coach/tracing/api/KeyList:1.0";

    /**
     * Read KeyList from a marshalled stream
     * @param istream the input stream
     * @return the readed KeyList value
     */
    public static long[] read(org.omg.CORBA.portable.InputStream istream)
    {
        long[] new_one;
        {
        int size7 = istream.read_ulong();
        new_one = new long[size7];
        istream.read_longlong_array(new_one, 0, new_one.length);
        }

        return new_one;
    }

    /**
     * Write KeyList into a marshalled stream
     * @param ostream the output stream
     * @param value KeyList value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, long[] value)
    {
        ostream.write_ulong(value.length);
        ostream.write_longlong_array(value, 0,value.length);
    }

}
