package org.coach.tracing.api;

/** 
 * Helper class for : i_EventDataBaseConnection
 *  
 * @author OpenORB Compiler
 */ 
public class i_EventDataBaseConnectionHelper
{
    /**
     * Insert i_EventDataBaseConnection into an any
     * @param a an any
     * @param t i_EventDataBaseConnection value
     */
    public static void insert(org.omg.CORBA.Any a, org.coach.tracing.api.i_EventDataBaseConnection t)
    {
        a.insert_Object(t , type());
    }

    /**
     * Extract i_EventDataBaseConnection from an any
     * @param a an any
     * @return the extracted i_EventDataBaseConnection value
     */
    public static org.coach.tracing.api.i_EventDataBaseConnection extract(org.omg.CORBA.Any a)
    {
        if (!a.type().equal(type()))
            throw new org.omg.CORBA.MARSHAL();
        try {
            return org.coach.tracing.api.i_EventDataBaseConnectionHelper.narrow(a.extract_Object());
        } catch (final org.omg.CORBA.BAD_PARAM e) {
            throw new org.omg.CORBA.MARSHAL(e.getMessage());
        }
    }

    //
    // Internal TypeCode value
    //
    private static org.omg.CORBA.TypeCode _tc = null;

    /**
     * Return the i_EventDataBaseConnection TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type()
    {
        if (_tc == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            _tc = orb.create_interface_tc(id(),"i_EventDataBaseConnection");
        }
        return _tc;
    }

    /**
     * Return the i_EventDataBaseConnection IDL ID
     * @return an ID
     */
    public static String id()
    {
        return _id;
    }

    private final static String _id = "IDL:org/coach/tracing/api/i_EventDataBaseConnection:1.0";

    /**
     * Read i_EventDataBaseConnection from a marshalled stream
     * @param istream the input stream
     * @return the readed i_EventDataBaseConnection value
     */
    public static org.coach.tracing.api.i_EventDataBaseConnection read(org.omg.CORBA.portable.InputStream istream)
    {
        return(org.coach.tracing.api.i_EventDataBaseConnection)istream.read_Object(org.coach.tracing.api._i_EventDataBaseConnectionStub.class);
    }

    /**
     * Write i_EventDataBaseConnection into a marshalled stream
     * @param ostream the output stream
     * @param value i_EventDataBaseConnection value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, org.coach.tracing.api.i_EventDataBaseConnection value)
    {
        ostream.write_Object((org.omg.CORBA.portable.ObjectImpl)value);
    }

    /**
     * Narrow CORBA::Object to i_EventDataBaseConnection
     * @param obj the CORBA Object
     * @return i_EventDataBaseConnection Object
     */
    public static i_EventDataBaseConnection narrow(org.omg.CORBA.Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof i_EventDataBaseConnection)
            return (i_EventDataBaseConnection)obj;

        if (obj._is_a(id()))
        {
            _i_EventDataBaseConnectionStub stub = new _i_EventDataBaseConnectionStub();
            stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
            return stub;
        }

        throw new org.omg.CORBA.BAD_PARAM();
    }

    /**
     * Unchecked Narrow CORBA::Object to i_EventDataBaseConnection
     * @param obj the CORBA Object
     * @return i_EventDataBaseConnection Object
     */
    public static i_EventDataBaseConnection unchecked_narrow(org.omg.CORBA.Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof i_EventDataBaseConnection)
            return (i_EventDataBaseConnection)obj;

        _i_EventDataBaseConnectionStub stub = new _i_EventDataBaseConnectionStub();
        stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
        return stub;

    }

}
