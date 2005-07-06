package org.coach.tracing.api;

/**
 * The i_EventDataBaseConnection interface is used to retrieve TraceRecords from an
 * EventDataBase. The connection is either realtime or on an existing, stored database,
 * depending on the way the connection was opened. With realtime connections, the number of events
 * will increase as new events are received by the database server.
 */
public abstract class i_EventDataBaseConnectionPOA extends org.omg.PortableServer.Servant
        implements i_EventDataBaseConnectionOperations, org.omg.CORBA.portable.InvokeHandler
{
    public i_EventDataBaseConnection _this()
    {
        return i_EventDataBaseConnectionHelper.narrow(_this_object());
    }

    public i_EventDataBaseConnection _this(org.omg.CORBA.ORB orb)
    {
        return i_EventDataBaseConnectionHelper.narrow(_this_object(orb));
    }

    private static String [] _ids_list =
    {
        "IDL:org/coach/tracing/api/i_EventDataBaseConnection:1.0"
    };

    public String[] _all_interfaces(org.omg.PortableServer.POA poa, byte [] objectId)
    {
        return _ids_list;
    }

    private static final java.util.Map operationMap = new java.util.HashMap();

    static {
            operationMap.put("getEvent",
                    new Operation_getEvent());
            operationMap.put("getEventAt",
                    new Operation_getEventAt());
            operationMap.put("getEventCount",
                    new Operation_getEventCount());
            operationMap.put("getEventIndex",
                    new Operation_getEventIndex());
            operationMap.put("getIdentity",
                    new Operation_getIdentity());
            operationMap.put("getIdentityCount",
                    new Operation_getIdentityCount());
            operationMap.put("getMessageCount",
                    new Operation_getMessageCount());
            operationMap.put("getParameterValues",
                    new Operation_getParameterValues());
            operationMap.put("getUnmatchedEventCount",
                    new Operation_getUnmatchedEventCount());
            operationMap.put("getXmlEvent",
                    new Operation_getXmlEvent());
            operationMap.put("getXmlEventAt",
                    new Operation_getXmlEventAt());
            operationMap.put("getXmlEvents",
                    new Operation_getXmlEvents());
            operationMap.put("getXmlIdentities",
                    new Operation_getXmlIdentities());
    }

    public final org.omg.CORBA.portable.OutputStream _invoke(final String opName,
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler)
    {

        final AbstractOperation operation = (AbstractOperation)operationMap.get(opName);

        if (null == operation) {
            throw new org.omg.CORBA.BAD_OPERATION(opName);
        }

        return operation.invoke(this, _is, handler);
    }

    // helper methods
    private org.omg.CORBA.portable.OutputStream _invoke_getEventAt(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        long arg0_in = _is.read_longlong();

        try
        {
            org.coach.tracing.api.TraceRecord _arg_result = getEventAt(arg0_in);

            _output = handler.createReply();
            org.coach.tracing.api.TraceRecordHelper.write(_output,_arg_result);

        }
        catch (org.coach.tracing.api.IndexOutOfRange _exception)
        {
            _output = handler.createExceptionReply();
            org.coach.tracing.api.IndexOutOfRangeHelper.write(_output,_exception);
        }
        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_getXmlEventAt(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        long arg0_in = _is.read_longlong();

        try
        {
            String _arg_result = getXmlEventAt(arg0_in);

            _output = handler.createReply();
            _output.write_string(_arg_result);

        }
        catch (org.coach.tracing.api.IndexOutOfRange _exception)
        {
            _output = handler.createExceptionReply();
            org.coach.tracing.api.IndexOutOfRangeHelper.write(_output,_exception);
        }
        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_getXmlEvents(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        long arg0_in = _is.read_longlong();
        int arg1_in = _is.read_long();

        try
        {
            String _arg_result = getXmlEvents(arg0_in, arg1_in);

            _output = handler.createReply();
            _output.write_string(_arg_result);

        }
        catch (org.coach.tracing.api.IndexOutOfRange _exception)
        {
            _output = handler.createExceptionReply();
            org.coach.tracing.api.IndexOutOfRangeHelper.write(_output,_exception);
        }
        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_getXmlIdentities(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        long[] arg0_in = org.coach.tracing.api.KeyListHelper.read(_is);

        try
        {
            String _arg_result = getXmlIdentities(arg0_in);

            _output = handler.createReply();
            _output.write_string(_arg_result);

        }
        catch (org.coach.tracing.api.InvalidKey _exception)
        {
            _output = handler.createExceptionReply();
            org.coach.tracing.api.InvalidKeyHelper.write(_output,_exception);
        }
        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_getEvent(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        long arg0_in = _is.read_longlong();

        try
        {
            org.coach.tracing.api.TraceRecord _arg_result = getEvent(arg0_in);

            _output = handler.createReply();
            org.coach.tracing.api.TraceRecordHelper.write(_output,_arg_result);

        }
        catch (org.coach.tracing.api.InvalidKey _exception)
        {
            _output = handler.createExceptionReply();
            org.coach.tracing.api.InvalidKeyHelper.write(_output,_exception);
        }
        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_getXmlEvent(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        long arg0_in = _is.read_longlong();

        try
        {
            String _arg_result = getXmlEvent(arg0_in);

            _output = handler.createReply();
            _output.write_string(_arg_result);

        }
        catch (org.coach.tracing.api.InvalidKey _exception)
        {
            _output = handler.createExceptionReply();
            org.coach.tracing.api.InvalidKeyHelper.write(_output,_exception);
        }
        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_getEventIndex(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        long arg0_in = _is.read_longlong();

        try
        {
            long _arg_result = getEventIndex(arg0_in);

            _output = handler.createReply();
            _output.write_longlong(_arg_result);

        }
        catch (org.coach.tracing.api.InvalidKey _exception)
        {
            _output = handler.createExceptionReply();
            org.coach.tracing.api.InvalidKeyHelper.write(_output,_exception);
        }
        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_getEventCount(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;

        long _arg_result = getEventCount();

        _output = handler.createReply();
        _output.write_longlong(_arg_result);

        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_getUnmatchedEventCount(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;

        long _arg_result = getUnmatchedEventCount();

        _output = handler.createReply();
        _output.write_longlong(_arg_result);

        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_getMessageCount(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;

        long _arg_result = getMessageCount();

        _output = handler.createReply();
        _output.write_longlong(_arg_result);

        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_getIdentityCount(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;

        int _arg_result = getIdentityCount();

        _output = handler.createReply();
        _output.write_long(_arg_result);

        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_getIdentity(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        long arg0_in = _is.read_longlong();

        try
        {
            org.coach.tracing.api.Identity _arg_result = getIdentity(arg0_in);

            _output = handler.createReply();
            org.coach.tracing.api.IdentityHelper.write(_output,_arg_result);

        }
        catch (org.coach.tracing.api.InvalidKey _exception)
        {
            _output = handler.createExceptionReply();
            org.coach.tracing.api.InvalidKeyHelper.write(_output,_exception);
        }
        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_getParameterValues(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        long arg0_in = _is.read_longlong();

        try
        {
            String _arg_result = getParameterValues(arg0_in);

            _output = handler.createReply();
            _output.write_string(_arg_result);

        }
        catch (org.coach.tracing.api.InvalidKey _exception)
        {
            _output = handler.createExceptionReply();
            org.coach.tracing.api.InvalidKeyHelper.write(_output,_exception);
        }
        return _output;
    }

    // operation classes
    private abstract static class AbstractOperation {
        protected abstract org.omg.CORBA.portable.OutputStream invoke(
                i_EventDataBaseConnectionPOA target,
                org.omg.CORBA.portable.InputStream _is,
                org.omg.CORBA.portable.ResponseHandler handler);
    }

    private static final class Operation_getEventAt extends AbstractOperation
    {
        protected org.omg.CORBA.portable.OutputStream invoke(
                final i_EventDataBaseConnectionPOA target,
                final org.omg.CORBA.portable.InputStream _is,
                final org.omg.CORBA.portable.ResponseHandler handler) {
            return target._invoke_getEventAt(_is, handler);
        }
    }

    private static final class Operation_getXmlEventAt extends AbstractOperation
    {
        protected org.omg.CORBA.portable.OutputStream invoke(
                final i_EventDataBaseConnectionPOA target,
                final org.omg.CORBA.portable.InputStream _is,
                final org.omg.CORBA.portable.ResponseHandler handler) {
            return target._invoke_getXmlEventAt(_is, handler);
        }
    }

    private static final class Operation_getXmlEvents extends AbstractOperation
    {
        protected org.omg.CORBA.portable.OutputStream invoke(
                final i_EventDataBaseConnectionPOA target,
                final org.omg.CORBA.portable.InputStream _is,
                final org.omg.CORBA.portable.ResponseHandler handler) {
            return target._invoke_getXmlEvents(_is, handler);
        }
    }

    private static final class Operation_getXmlIdentities extends AbstractOperation
    {
        protected org.omg.CORBA.portable.OutputStream invoke(
                final i_EventDataBaseConnectionPOA target,
                final org.omg.CORBA.portable.InputStream _is,
                final org.omg.CORBA.portable.ResponseHandler handler) {
            return target._invoke_getXmlIdentities(_is, handler);
        }
    }

    private static final class Operation_getEvent extends AbstractOperation
    {
        protected org.omg.CORBA.portable.OutputStream invoke(
                final i_EventDataBaseConnectionPOA target,
                final org.omg.CORBA.portable.InputStream _is,
                final org.omg.CORBA.portable.ResponseHandler handler) {
            return target._invoke_getEvent(_is, handler);
        }
    }

    private static final class Operation_getXmlEvent extends AbstractOperation
    {
        protected org.omg.CORBA.portable.OutputStream invoke(
                final i_EventDataBaseConnectionPOA target,
                final org.omg.CORBA.portable.InputStream _is,
                final org.omg.CORBA.portable.ResponseHandler handler) {
            return target._invoke_getXmlEvent(_is, handler);
        }
    }

    private static final class Operation_getEventIndex extends AbstractOperation
    {
        protected org.omg.CORBA.portable.OutputStream invoke(
                final i_EventDataBaseConnectionPOA target,
                final org.omg.CORBA.portable.InputStream _is,
                final org.omg.CORBA.portable.ResponseHandler handler) {
            return target._invoke_getEventIndex(_is, handler);
        }
    }

    private static final class Operation_getEventCount extends AbstractOperation
    {
        protected org.omg.CORBA.portable.OutputStream invoke(
                final i_EventDataBaseConnectionPOA target,
                final org.omg.CORBA.portable.InputStream _is,
                final org.omg.CORBA.portable.ResponseHandler handler) {
            return target._invoke_getEventCount(_is, handler);
        }
    }

    private static final class Operation_getUnmatchedEventCount extends AbstractOperation
    {
        protected org.omg.CORBA.portable.OutputStream invoke(
                final i_EventDataBaseConnectionPOA target,
                final org.omg.CORBA.portable.InputStream _is,
                final org.omg.CORBA.portable.ResponseHandler handler) {
            return target._invoke_getUnmatchedEventCount(_is, handler);
        }
    }

    private static final class Operation_getMessageCount extends AbstractOperation
    {
        protected org.omg.CORBA.portable.OutputStream invoke(
                final i_EventDataBaseConnectionPOA target,
                final org.omg.CORBA.portable.InputStream _is,
                final org.omg.CORBA.portable.ResponseHandler handler) {
            return target._invoke_getMessageCount(_is, handler);
        }
    }

    private static final class Operation_getIdentityCount extends AbstractOperation
    {
        protected org.omg.CORBA.portable.OutputStream invoke(
                final i_EventDataBaseConnectionPOA target,
                final org.omg.CORBA.portable.InputStream _is,
                final org.omg.CORBA.portable.ResponseHandler handler) {
            return target._invoke_getIdentityCount(_is, handler);
        }
    }

    private static final class Operation_getIdentity extends AbstractOperation
    {
        protected org.omg.CORBA.portable.OutputStream invoke(
                final i_EventDataBaseConnectionPOA target,
                final org.omg.CORBA.portable.InputStream _is,
                final org.omg.CORBA.portable.ResponseHandler handler) {
            return target._invoke_getIdentity(_is, handler);
        }
    }

    private static final class Operation_getParameterValues extends AbstractOperation
    {
        protected org.omg.CORBA.portable.OutputStream invoke(
                final i_EventDataBaseConnectionPOA target,
                final org.omg.CORBA.portable.InputStream _is,
                final org.omg.CORBA.portable.ResponseHandler handler) {
            return target._invoke_getParameterValues(_is, handler);
        }
    }

}
