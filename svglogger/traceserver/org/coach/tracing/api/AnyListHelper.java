package org.coach.tracing.api;

/** 
 * Helper class for : AnyList
 *  
 * @author OpenORB Compiler
 */ 
public class AnyListHelper
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
     * Insert AnyList into an any
     * @param a an any
     * @param t AnyList value
     */
    public static void insert(org.omg.CORBA.Any a, org.omg.CORBA.Any[] t)
    {
        a.insert_Streamable(new org.coach.tracing.api.AnyListHolder(t));
    }

    /**
     * Extract AnyList from an any
     * @param a an any
     * @return the extracted AnyList value
     */
    public static org.omg.CORBA.Any[] extract(org.omg.CORBA.Any a)
    {
        if (!a.type().equal(type()))
            throw new org.omg.CORBA.MARSHAL();
        if(HAS_OPENORB && a instanceof org.openorb.CORBA.Any) {
            // streamable extraction. The jdk stubs incorrectly define the Any stub
            org.openorb.CORBA.Any any = (org.openorb.CORBA.Any)a;
            try {
                org.omg.CORBA.portable.Streamable s = any.extract_Streamable();
                if(s instanceof org.coach.tracing.api.AnyListHolder)
                    return ((org.coach.tracing.api.AnyListHolder)s).value;
            } catch (org.omg.CORBA.BAD_INV_ORDER ex) {
            }
            org.coach.tracing.api.AnyListHolder h = new org.coach.tracing.api.AnyListHolder(read(a.create_input_stream()));
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
     * Return the AnyList TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type()
    {
        if (_tc == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            _tc = orb.create_alias_tc(id(),"AnyList",orb.create_sequence_tc(0,orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_any)));
        }
        return _tc;
    }

    /**
     * Return the AnyList IDL ID
     * @return an ID
     */
    public static String id()
    {
        return _id;
    }

    private final static String _id = "IDL:org/coach/tracing/api/AnyList:1.0";

    /**
     * Read AnyList from a marshalled stream
     * @param istream the input stream
     * @return the readed AnyList value
     */
    public static org.omg.CORBA.Any[] read(org.omg.CORBA.portable.InputStream istream)
    {
        org.omg.CORBA.Any[] new_one;
        {
        int size7 = istream.read_ulong();
        new_one = new org.omg.CORBA.Any[size7];
        for (int i7=0; i7<new_one.length; i7++)
         {
            new_one[i7] = istream.read_any();

         }
        }

        return new_one;
    }

    /**
     * Write AnyList into a marshalled stream
     * @param ostream the output stream
     * @param value AnyList value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, org.omg.CORBA.Any[] value)
    {
        ostream.write_ulong(value.length);
        for (int i7=0; i7<value.length; i7++)
        {
            ostream.write_any(value[i7]);

        }
    }

}
