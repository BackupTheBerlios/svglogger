package org.coach.tracing.api.pi;

/**
 * Interface definition : TracingService
 * 
 * @author OpenORB Compiler
 */
public abstract class TracingServicePOA extends org.omg.PortableServer.Servant
        implements TracingServiceOperations, org.omg.CORBA.portable.InvokeHandler
{
    public TracingService _this()
    {
        return TracingServiceHelper.narrow(_this_object());
    }

    public TracingService _this(org.omg.CORBA.ORB orb)
    {
        return TracingServiceHelper.narrow(_this_object(orb));
    }

    private static String [] _ids_list =
    {
        "IDL:org/coach/tracing/api/pi/TracingService:1.0"
    };

    public String[] _all_interfaces(org.omg.PortableServer.POA poa, byte [] objectId)
    {
        return _ids_list;
    }

    public final org.omg.CORBA.portable.OutputStream _invoke(final String opName,
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler)
    {

        if (opName.equals("start")) {
                return _invoke_start(_is, handler);
        } else if (opName.equals("stop")) {
                return _invoke_stop(_is, handler);
        } else {
            throw new org.omg.CORBA.BAD_OPERATION(opName);
        }
    }

    // helper methods
    private org.omg.CORBA.portable.OutputStream _invoke_start(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;

        start();

        _output = handler.createReply();

        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_stop(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;

        stop();

        _output = handler.createReply();

        return _output;
    }

}
