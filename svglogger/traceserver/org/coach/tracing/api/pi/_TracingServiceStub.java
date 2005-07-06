package org.coach.tracing.api.pi;

/**
 * Interface definition : TracingService
 * 
 * @author OpenORB Compiler
 */
public class _TracingServiceStub extends org.omg.CORBA.portable.ObjectImpl
        implements TracingService
{
    static final String[] _ids_list =
    {
        "IDL:org/coach/tracing/api/pi/TracingService:1.0"
    };

    public String[] _ids()
    {
     return _ids_list;
    }

    private final static Class _opsClass = org.coach.tracing.api.pi.TracingServiceOperations.class;

    /**
     * Operation start
     */
    public void start()
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("start",true);
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("start",_opsClass);
                if (_so == null)
                   continue;
                org.coach.tracing.api.pi.TracingServiceOperations _self = (org.coach.tracing.api.pi.TracingServiceOperations) _so.servant;
                try
                {
                    _self.start();
                    return;
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation stop
     */
    public void stop()
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("stop",true);
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("stop",_opsClass);
                if (_so == null)
                   continue;
                org.coach.tracing.api.pi.TracingServiceOperations _self = (org.coach.tracing.api.pi.TracingServiceOperations) _so.servant;
                try
                {
                    _self.stop();
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
