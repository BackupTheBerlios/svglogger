package org.coach.tracing.api;

/** 
 * Helper class for : i_EventDataBase
 *  
 * @author OpenORB Compiler
 */ 
public class i_EventDataBaseHelper
{
    /**
     * Insert i_EventDataBase into an any
     * @param a an any
     * @param t i_EventDataBase value
     */
    public static void insert(org.omg.CORBA.Any a, org.coach.tracing.api.i_EventDataBase t)
    {
        a.insert_Object(t , type());
    }

    /**
     * Extract i_EventDataBase from an any
     * @param a an any
     * @return the extracted i_EventDataBase value
     */
    public static org.coach.tracing.api.i_EventDataBase extract(org.omg.CORBA.Any a)
    {
        if (!a.type().equal(type()))
            throw new org.omg.CORBA.MARSHAL();
        try {
            return org.coach.tracing.api.i_EventDataBaseHelper.narrow(a.extract_Object());
        } catch (final org.omg.CORBA.BAD_PARAM e) {
            throw new org.omg.CORBA.MARSHAL(e.getMessage());
        }
    }

    //
    // Internal TypeCode value
    //
    private static org.omg.CORBA.TypeCode _tc = null;

    /**
     * Return the i_EventDataBase TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type()
    {
        if (_tc == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            _tc = orb.create_interface_tc(id(),"i_EventDataBase");
        }
        return _tc;
    }

    /**
     * Return the i_EventDataBase IDL ID
     * @return an ID
     */
    public static String id()
    {
        return _id;
    }

    private final static String _id = "IDL:org/coach/tracing/api/i_EventDataBase:1.0";

    /**
     * Read i_EventDataBase from a marshalled stream
     * @param istream the input stream
     * @return the readed i_EventDataBase value
     */
    public static org.coach.tracing.api.i_EventDataBase read(org.omg.CORBA.portable.InputStream istream)
    {
        return(org.coach.tracing.api.i_EventDataBase)istream.read_Object(org.coach.tracing.api._i_EventDataBaseStub.class);
    }

    /**
     * Write i_EventDataBase into a marshalled stream
     * @param ostream the output stream
     * @param value i_EventDataBase value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, org.coach.tracing.api.i_EventDataBase value)
    {
        ostream.write_Object((org.omg.CORBA.portable.ObjectImpl)value);
    }

    /**
     * Narrow CORBA::Object to i_EventDataBase
     * @param obj the CORBA Object
     * @return i_EventDataBase Object
     */
    public static i_EventDataBase narrow(org.omg.CORBA.Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof i_EventDataBase)
            return (i_EventDataBase)obj;

        if (obj._is_a(id()))
        {
            _i_EventDataBaseStub stub = new _i_EventDataBaseStub();
            stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
            return stub;
        }

        throw new org.omg.CORBA.BAD_PARAM();
    }

    /**
     * Unchecked Narrow CORBA::Object to i_EventDataBase
     * @param obj the CORBA Object
     * @return i_EventDataBase Object
     */
    public static i_EventDataBase unchecked_narrow(org.omg.CORBA.Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof i_EventDataBase)
            return (i_EventDataBase)obj;

        _i_EventDataBaseStub stub = new _i_EventDataBaseStub();
        stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
        return stub;

    }

}
