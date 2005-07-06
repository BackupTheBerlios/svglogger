package org.coach.tracing.api;

/**
 * Interface definition : i_EventDataBase
 * 
 * @author OpenORB Compiler
 */
public abstract class i_EventDataBasePOA extends org.omg.PortableServer.Servant
        implements i_EventDataBaseOperations, org.omg.CORBA.portable.InvokeHandler
{
    public i_EventDataBase _this()
    {
        return i_EventDataBaseHelper.narrow(_this_object());
    }

    public i_EventDataBase _this(org.omg.CORBA.ORB orb)
    {
        return i_EventDataBaseHelper.narrow(_this_object(orb));
    }

    private static String [] _ids_list =
    {
        "IDL:org/coach/tracing/api/i_EventDataBase:1.0"
    };

    public String[] _all_interfaces(org.omg.PortableServer.POA poa, byte [] objectId)
    {
        return _ids_list;
    }

    public final org.omg.CORBA.portable.OutputStream _invoke(final String opName,
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler)
    {

        if (opName.equals("close")) {
                return _invoke_close(_is, handler);
        } else if (opName.equals("connect")) {
                return _invoke_connect(_is, handler);
        } else if (opName.equals("list")) {
                return _invoke_list(_is, handler);
        } else if (opName.equals("open")) {
                return _invoke_open(_is, handler);
        } else if (opName.equals("save")) {
                return _invoke_save(_is, handler);
        } else {
            throw new org.omg.CORBA.BAD_OPERATION(opName);
        }
    }

    // helper methods
    private org.omg.CORBA.portable.OutputStream _invoke_open(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        String arg0_in = _is.read_string();

        try
        {
            org.coach.tracing.api.i_EventDataBaseConnection _arg_result = open(arg0_in);

            _output = handler.createReply();
            org.coach.tracing.api.i_EventDataBaseConnectionHelper.write(_output,_arg_result);

        }
        catch (org.coach.tracing.api.InvalidName _exception)
        {
            _output = handler.createExceptionReply();
            org.coach.tracing.api.InvalidNameHelper.write(_output,_exception);
        }
        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_close(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        org.coach.tracing.api.i_EventDataBaseConnection arg0_in = org.coach.tracing.api.i_EventDataBaseConnectionHelper.read(_is);

        close(arg0_in);

        _output = handler.createReply();

        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_list(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;

        String[] _arg_result = list();

        _output = handler.createReply();
        org.coach.tracing.api.NamesHelper.write(_output,_arg_result);

        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_connect(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;

        org.coach.tracing.api.i_EventDataBaseConnection _arg_result = connect();

        _output = handler.createReply();
        org.coach.tracing.api.i_EventDataBaseConnectionHelper.write(_output,_arg_result);

        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_save(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        String arg0_in = _is.read_string();

        save(arg0_in);

        _output = handler.createReply();

        return _output;
    }

}
