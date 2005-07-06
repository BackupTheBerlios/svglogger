package org.coach.tracing.api;

/**
 * Interface definition : i_EventDataBase
 * 
 * @author OpenORB Compiler
 */
public class _i_EventDataBaseStub extends org.omg.CORBA.portable.ObjectImpl
        implements i_EventDataBase
{
    static final String[] _ids_list =
    {
        "IDL:org/coach/tracing/api/i_EventDataBase:1.0"
    };

    public String[] _ids()
    {
     return _ids_list;
    }

    private final static Class _opsClass = org.coach.tracing.api.i_EventDataBaseOperations.class;

    /**
     * Operation open
     */
    public org.coach.tracing.api.i_EventDataBaseConnection open(String name)
        throws org.coach.tracing.api.InvalidName
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("open",true);
                    _output.write_string(name);
                    _input = this._invoke(_output);
                    org.coach.tracing.api.i_EventDataBaseConnection _arg_ret = org.coach.tracing.api.i_EventDataBaseConnectionHelper.read(_input);
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    if (_exception_id.equals(org.coach.tracing.api.InvalidNameHelper.id()))
                    {
                        throw org.coach.tracing.api.InvalidNameHelper.read(_exception.getInputStream());
                    }

                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: "+ _exception_id);
                }
                finally
                {
                    this._releaseReply(_input);
                }
            }
            else
            {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("open",_opsClass);
                if (_so == null)
                   continue;
                org.coach.tracing.api.i_EventDataBaseOperations _self = (org.coach.tracing.api.i_EventDataBaseOperations) _so.servant;
                try
                {
                    return _self.open( name);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation close
     */
    public void close(org.coach.tracing.api.i_EventDataBaseConnection connection)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("close",true);
                    org.coach.tracing.api.i_EventDataBaseConnectionHelper.write(_output,connection);
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("close",_opsClass);
                if (_so == null)
                   continue;
                org.coach.tracing.api.i_EventDataBaseOperations _self = (org.coach.tracing.api.i_EventDataBaseOperations) _so.servant;
                try
                {
                    _self.close( connection);
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
     * Operation list
     */
    public String[] list()
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("list",true);
                    _input = this._invoke(_output);
                    String[] _arg_ret = org.coach.tracing.api.NamesHelper.read(_input);
                    return _arg_ret;
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("list",_opsClass);
                if (_so == null)
                   continue;
                org.coach.tracing.api.i_EventDataBaseOperations _self = (org.coach.tracing.api.i_EventDataBaseOperations) _so.servant;
                try
                {
                    return _self.list();
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation connect
     */
    public org.coach.tracing.api.i_EventDataBaseConnection connect()
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("connect",true);
                    _input = this._invoke(_output);
                    org.coach.tracing.api.i_EventDataBaseConnection _arg_ret = org.coach.tracing.api.i_EventDataBaseConnectionHelper.read(_input);
                    return _arg_ret;
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("connect",_opsClass);
                if (_so == null)
                   continue;
                org.coach.tracing.api.i_EventDataBaseOperations _self = (org.coach.tracing.api.i_EventDataBaseOperations) _so.servant;
                try
                {
                    return _self.connect();
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation save
     */
    public void save(String name)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("save",true);
                    _output.write_string(name);
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("save",_opsClass);
                if (_so == null)
                   continue;
                org.coach.tracing.api.i_EventDataBaseOperations _self = (org.coach.tracing.api.i_EventDataBaseOperations) _so.servant;
                try
                {
                    _self.save( name);
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
