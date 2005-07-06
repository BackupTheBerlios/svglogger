package org.coach.tracing.api;

/**
 * Interface definition : i_Trace
 * 
 * @author OpenORB Compiler
 */
public class _i_TraceStub extends org.omg.CORBA.portable.ObjectImpl
        implements i_Trace
{
    static final String[] _ids_list =
    {
        "IDL:org/coach/tracing/api/i_Trace:1.0"
    };

    public String[] _ids()
    {
     return _ids_list;
    }

    private final static Class _opsClass = org.coach.tracing.api.i_TraceOperations.class;

    /**
     * Operation receiveEvent
     */
    public void receiveEvent(org.coach.tracing.api.TraceEvent[] events)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("receiveEvent",true);
                    org.coach.tracing.api.TraceEventsHelper.write(_output,events);
                    _input = this._invoke(_output);
                    return;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: "+ _exception_id);
                }
                finally
                {
                    this._releaseReply(_input);
                }
            }
            else
            {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("receiveEvent",_opsClass);
                if (_so == null)
                   continue;
                org.coach.tracing.api.i_TraceOperations _self = (org.coach.tracing.api.i_TraceOperations) _so.servant;
                try
                {
                    _self.receiveEvent( events);
                    return;
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

}
