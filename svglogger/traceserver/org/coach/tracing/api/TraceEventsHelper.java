package org.coach.tracing.api;

/** 
 * Helper class for : TraceEvents
 *  
 * @author OpenORB Compiler
 */ 
public class TraceEventsHelper
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
     * Insert TraceEvents into an any
     * @param a an any
     * @param t TraceEvents value
     */
    public static void insert(org.omg.CORBA.Any a, org.coach.tracing.api.TraceEvent[] t)
    {
        a.insert_Streamable(new org.coach.tracing.api.TraceEventsHolder(t));
    }

    /**
     * Extract TraceEvents from an any
     * @param a an any
     * @return the extracted TraceEvents value
     */
    public static org.coach.tracing.api.TraceEvent[] extract(org.omg.CORBA.Any a)
    {
        if (!a.type().equal(type()))
            throw new org.omg.CORBA.MARSHAL();
        if(HAS_OPENORB && a instanceof org.openorb.CORBA.Any) {
            // streamable extraction. The jdk stubs incorrectly define the Any stub
            org.openorb.CORBA.Any any = (org.openorb.CORBA.Any)a;
            try {
                org.omg.CORBA.portable.Streamable s = any.extract_Streamable();
                if(s instanceof org.coach.tracing.api.TraceEventsHolder)
                    return ((org.coach.tracing.api.TraceEventsHolder)s).value;
            } catch (org.omg.CORBA.BAD_INV_ORDER ex) {
            }
            org.coach.tracing.api.TraceEventsHolder h = new org.coach.tracing.api.TraceEventsHolder(read(a.create_input_stream()));
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
     * Return the TraceEvents TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type()
    {
        if (_tc == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            _tc = orb.create_alias_tc(id(),"TraceEvents",orb.create_sequence_tc(0,org.coach.tracing.api.TraceEventHelper.type()));
        }
        return _tc;
    }

    /**
     * Return the TraceEvents IDL ID
     * @return an ID
     */
    public static String id()
    {
        return _id;
    }

    private final static String _id = "IDL:org/coach/tracing/api/TraceEvents:1.0";

    /**
     * Read TraceEvents from a marshalled stream
     * @param istream the input stream
     * @return the readed TraceEvents value
     */
    public static org.coach.tracing.api.TraceEvent[] read(org.omg.CORBA.portable.InputStream istream)
    {
        org.coach.tracing.api.TraceEvent[] new_one;
        {
        int size7 = istream.read_ulong();
        new_one = new org.coach.tracing.api.TraceEvent[size7];
        for (int i7=0; i7<new_one.length; i7++)
         {
            new_one[i7] = org.coach.tracing.api.TraceEventHelper.read(istream);

         }
        }

        return new_one;
    }

    /**
     * Write TraceEvents into a marshalled stream
     * @param ostream the output stream
     * @param value TraceEvents value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, org.coach.tracing.api.TraceEvent[] value)
    {
        ostream.write_ulong(value.length);
        for (int i7=0; i7<value.length; i7++)
        {
            org.coach.tracing.api.TraceEventHelper.write(ostream,value[i7]);

        }
    }

}
