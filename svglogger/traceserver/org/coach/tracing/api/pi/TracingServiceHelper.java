package org.coach.tracing.api.pi;

/** 
 * Helper class for : TracingService
 *  
 * @author OpenORB Compiler
 */ 
public class TracingServiceHelper
{
    /**
     * Insert TracingService into an any
     * @param a an any
     * @param t TracingService value
     */
    public static void insert(org.omg.CORBA.Any a, org.coach.tracing.api.pi.TracingService t)
    {
        a.insert_Object(t , type());
    }

    /**
     * Extract TracingService from an any
     * @param a an any
     * @return the extracted TracingService value
     */
    public static org.coach.tracing.api.pi.TracingService extract(org.omg.CORBA.Any a)
    {
        if (!a.type().equal(type()))
            throw new org.omg.CORBA.MARSHAL();
        try {
            return org.coach.tracing.api.pi.TracingServiceHelper.narrow(a.extract_Object());
        } catch (final org.omg.CORBA.BAD_PARAM e) {
            throw new org.omg.CORBA.MARSHAL(e.getMessage());
        }
    }

    //
    // Internal TypeCode value
    //
    private static org.omg.CORBA.TypeCode _tc = null;

    /**
     * Return the TracingService TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type()
    {
        if (_tc == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            _tc = orb.create_interface_tc(id(),"TracingService");
        }
        return _tc;
    }

    /**
     * Return the TracingService IDL ID
     * @return an ID
     */
    public static String id()
    {
        return _id;
    }

    private final static String _id = "IDL:org/coach/tracing/api/pi/TracingService:1.0";

    /**
     * Read TracingService from a marshalled stream
     * @param istream the input stream
     * @return the readed TracingService value
     */
    public static org.coach.tracing.api.pi.TracingService read(org.omg.CORBA.portable.InputStream istream)
    {
        return(org.coach.tracing.api.pi.TracingService)istream.read_Object(org.coach.tracing.api.pi._TracingServiceStub.class);
    }

    /**
     * Write TracingService into a marshalled stream
     * @param ostream the output stream
     * @param value TracingService value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, org.coach.tracing.api.pi.TracingService value)
    {
        ostream.write_Object((org.omg.CORBA.portable.ObjectImpl)value);
    }

    /**
     * Narrow CORBA::Object to TracingService
     * @param obj the CORBA Object
     * @return TracingService Object
     */
    public static TracingService narrow(org.omg.CORBA.Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof TracingService)
            return (TracingService)obj;

        if (obj._is_a(id()))
        {
            _TracingServiceStub stub = new _TracingServiceStub();
            stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
            return stub;
        }

        throw new org.omg.CORBA.BAD_PARAM();
    }

    /**
     * Unchecked Narrow CORBA::Object to TracingService
     * @param obj the CORBA Object
     * @return TracingService Object
     */
    public static TracingService unchecked_narrow(org.omg.CORBA.Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof TracingService)
            return (TracingService)obj;

        _TracingServiceStub stub = new _TracingServiceStub();
        stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
        return stub;

    }

}
