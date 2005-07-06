package org.coach.tracing.api;

/**
 * Interface definition : i_Trace
 * 
 * @author OpenORB Compiler
 */
public abstract class i_TracePOA extends org.omg.PortableServer.Servant
        implements i_TraceOperations, org.omg.CORBA.portable.InvokeHandler
{
    public i_Trace _this()
    {
        return i_TraceHelper.narrow(_this_object());
    }

    public i_Trace _this(org.omg.CORBA.ORB orb)
    {
        return i_TraceHelper.narrow(_this_object(orb));
    }

    private static String [] _ids_list =
    {
        "IDL:org/coach/tracing/api/i_Trace:1.0"
    };

    public String[] _all_interfaces(org.omg.PortableServer.POA poa, byte [] objectId)
    {
        return _ids_list;
    }

    public final org.omg.CORBA.portable.OutputStream _invoke(final String opName,
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler)
    {

        if (opName.equals("receiveEvent")) {
                return _invoke_receiveEvent(_is, handler);
        } else {
            throw new org.omg.CORBA.BAD_OPERATION(opName);
        }
    }

    // helper methods
    private org.omg.CORBA.portable.OutputStream _invoke_receiveEvent(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        org.coach.tracing.api.TraceEvent[] arg0_in = org.coach.tracing.api.TraceEventsHelper.read(_is);

        receiveEvent(arg0_in);

        _output = handler.createReply();

        return _output;
    }

}
