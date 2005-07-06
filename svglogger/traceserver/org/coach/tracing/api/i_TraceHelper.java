package org.coach.tracing.api;

/** 
 * Helper class for : i_Trace
 *  
 * @author OpenORB Compiler
 */ 
public class i_TraceHelper
{
    /**
     * Insert i_Trace into an any
     * @param a an any
     * @param t i_Trace value
     */
    public static void insert(org.omg.CORBA.Any a, org.coach.tracing.api.i_Trace t)
    {
        a.insert_Object(t , type());
    }

    /**
     * Extract i_Trace from an any
     * @param a an any
     * @return the extracted i_Trace value
     */
    public static org.coach.tracing.api.i_Trace extract(org.omg.CORBA.Any a)
    {
        if (!a.type().equal(type()))
            throw new org.omg.CORBA.MARSHAL();
        try {
            return org.coach.tracing.api.i_TraceHelper.narrow(a.extract_Object());
        } catch (final org.omg.CORBA.BAD_PARAM e) {
            throw new org.omg.CORBA.MARSHAL(e.getMessage());
        }
    }

    //
    // Internal TypeCode value
    //
    private static org.omg.CORBA.TypeCode _tc = null;

    /**
     * Return the i_Trace TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type()
    {
        if (_tc == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            _tc = orb.create_interface_tc(id(),"i_Trace");
        }
        return _tc;
    }

    /**
     * Return the i_Trace IDL ID
     * @return an ID
     */
    public static String id()
    {
        return _id;
    }

    private final static String _id = "IDL:org/coach/tracing/api/i_Trace:1.0";

    /**
     * Read i_Trace from a marshalled stream
     * @param istream the input stream
     * @return the readed i_Trace value
     */
    public static org.coach.tracing.api.i_Trace read(org.omg.CORBA.portable.InputStream istream)
    {
        return(org.coach.tracing.api.i_Trace)istream.read_Object(org.coach.tracing.api._i_TraceStub.class);
    }

    /**
     * Write i_Trace into a marshalled stream
     * @param ostream the output stream
     * @param value i_Trace value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, org.coach.tracing.api.i_Trace value)
    {
        ostream.write_Object((org.omg.CORBA.portable.ObjectImpl)value);
    }

    /**
     * Narrow CORBA::Object to i_Trace
     * @param obj the CORBA Object
     * @return i_Trace Object
     */
    public static i_Trace narrow(org.omg.CORBA.Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof i_Trace)
            return (i_Trace)obj;

        if (obj._is_a(id()))
        {
            _i_TraceStub stub = new _i_TraceStub();
            stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
            return stub;
        }

        throw new org.omg.CORBA.BAD_PARAM();
    }

    /**
     * Unchecked Narrow CORBA::Object to i_Trace
     * @param obj the CORBA Object
     * @return i_Trace Object
     */
    public static i_Trace unchecked_narrow(org.omg.CORBA.Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof i_Trace)
            return (i_Trace)obj;

        _i_TraceStub stub = new _i_TraceStub();
        stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
        return stub;

    }

}
