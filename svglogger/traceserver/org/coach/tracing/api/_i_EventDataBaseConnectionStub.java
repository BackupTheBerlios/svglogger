package org.coach.tracing.api;

/**
 * The i_EventDataBaseConnection interface is used to retrieve TraceRecords from an
 * EventDataBase. The connection is either realtime or on an existing, stored database,
 * depending on the way the connection was opened. With realtime connections, the number of events
 * will increase as new events are received by the database server.
 */
public class _i_EventDataBaseConnectionStub extends org.omg.CORBA.portable.ObjectImpl
        implements i_EventDataBaseConnection
{
    static final String[] _ids_list =
    {
        "IDL:org/coach/tracing/api/i_EventDataBaseConnection:1.0"
    };

    public String[] _ids()
    {
     return _ids_list;
    }

    private final static Class _opsClass = org.coach.tracing.api.i_EventDataBaseConnectionOperations.class;

    /**
     * Operation getEventAt
     */
    public org.coach.tracing.api.TraceRecord getEventAt(long index)
        throws org.coach.tracing.api.IndexOutOfRange
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("getEventAt",true);
                    _output.write_longlong(index);
                    _input = this._invoke(_output);
                    org.coach.tracing.api.TraceRecord _arg_ret = org.coach.tracing.api.TraceRecordHelper.read(_input);
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    if (_exception_id.equals(org.coach.tracing.api.IndexOutOfRangeHelper.id()))
                    {
                        throw org.coach.tracing.api.IndexOutOfRangeHelper.read(_exception.getInputStream());
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getEventAt",_opsClass);
                if (_so == null)
                   continue;
                org.coach.tracing.api.i_EventDataBaseConnectionOperations _self = (org.coach.tracing.api.i_EventDataBaseConnectionOperations) _so.servant;
                try
                {
                    return _self.getEventAt( index);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation getXmlEventAt
     */
    public String getXmlEventAt(long index)
        throws org.coach.tracing.api.IndexOutOfRange
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("getXmlEventAt",true);
                    _output.write_longlong(index);
                    _input = this._invoke(_output);
                    String _arg_ret = _input.read_string();
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    if (_exception_id.equals(org.coach.tracing.api.IndexOutOfRangeHelper.id()))
                    {
                        throw org.coach.tracing.api.IndexOutOfRangeHelper.read(_exception.getInputStream());
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getXmlEventAt",_opsClass);
                if (_so == null)
                   continue;
                org.coach.tracing.api.i_EventDataBaseConnectionOperations _self = (org.coach.tracing.api.i_EventDataBaseConnectionOperations) _so.servant;
                try
                {
                    return _self.getXmlEventAt( index);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation getXmlEvents
     */
    public String getXmlEvents(long index, int length)
        throws org.coach.tracing.api.IndexOutOfRange
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("getXmlEvents",true);
                    _output.write_longlong(index);
                    _output.write_long(length);
                    _input = this._invoke(_output);
                    String _arg_ret = _input.read_string();
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    if (_exception_id.equals(org.coach.tracing.api.IndexOutOfRangeHelper.id()))
                    {
                        throw org.coach.tracing.api.IndexOutOfRangeHelper.read(_exception.getInputStream());
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getXmlEvents",_opsClass);
                if (_so == null)
                   continue;
                org.coach.tracing.api.i_EventDataBaseConnectionOperations _self = (org.coach.tracing.api.i_EventDataBaseConnectionOperations) _so.servant;
                try
                {
                    return _self.getXmlEvents( index,  length);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation getXmlIdentities
     */
    public String getXmlIdentities(long[] keys)
        throws org.coach.tracing.api.InvalidKey
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("getXmlIdentities",true);
                    org.coach.tracing.api.KeyListHelper.write(_output,keys);
                    _input = this._invoke(_output);
                    String _arg_ret = _input.read_string();
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    if (_exception_id.equals(org.coach.tracing.api.InvalidKeyHelper.id()))
                    {
                        throw org.coach.tracing.api.InvalidKeyHelper.read(_exception.getInputStream());
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getXmlIdentities",_opsClass);
                if (_so == null)
                   continue;
                org.coach.tracing.api.i_EventDataBaseConnectionOperations _self = (org.coach.tracing.api.i_EventDataBaseConnectionOperations) _so.servant;
                try
                {
                    return _self.getXmlIdentities( keys);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation getEvent
     */
    public org.coach.tracing.api.TraceRecord getEvent(long key_id)
        throws org.coach.tracing.api.InvalidKey
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("getEvent",true);
                    _output.write_longlong(key_id);
                    _input = this._invoke(_output);
                    org.coach.tracing.api.TraceRecord _arg_ret = org.coach.tracing.api.TraceRecordHelper.read(_input);
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    if (_exception_id.equals(org.coach.tracing.api.InvalidKeyHelper.id()))
                    {
                        throw org.coach.tracing.api.InvalidKeyHelper.read(_exception.getInputStream());
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getEvent",_opsClass);
                if (_so == null)
                   continue;
                org.coach.tracing.api.i_EventDataBaseConnectionOperations _self = (org.coach.tracing.api.i_EventDataBaseConnectionOperations) _so.servant;
                try
                {
                    return _self.getEvent( key_id);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation getXmlEvent
     */
    public String getXmlEvent(long key_id)
        throws org.coach.tracing.api.InvalidKey
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("getXmlEvent",true);
                    _output.write_longlong(key_id);
                    _input = this._invoke(_output);
                    String _arg_ret = _input.read_string();
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    if (_exception_id.equals(org.coach.tracing.api.InvalidKeyHelper.id()))
                    {
                        throw org.coach.tracing.api.InvalidKeyHelper.read(_exception.getInputStream());
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getXmlEvent",_opsClass);
                if (_so == null)
                   continue;
                org.coach.tracing.api.i_EventDataBaseConnectionOperations _self = (org.coach.tracing.api.i_EventDataBaseConnectionOperations) _so.servant;
                try
                {
                    return _self.getXmlEvent( key_id);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation getEventIndex
     */
    public long getEventIndex(long key_id)
        throws org.coach.tracing.api.InvalidKey
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("getEventIndex",true);
                    _output.write_longlong(key_id);
                    _input = this._invoke(_output);
                    long _arg_ret = _input.read_longlong();
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    if (_exception_id.equals(org.coach.tracing.api.InvalidKeyHelper.id()))
                    {
                        throw org.coach.tracing.api.InvalidKeyHelper.read(_exception.getInputStream());
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getEventIndex",_opsClass);
                if (_so == null)
                   continue;
                org.coach.tracing.api.i_EventDataBaseConnectionOperations _self = (org.coach.tracing.api.i_EventDataBaseConnectionOperations) _so.servant;
                try
                {
                    return _self.getEventIndex( key_id);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation getEventCount
     */
    public long getEventCount()
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("getEventCount",true);
                    _input = this._invoke(_output);
                    long _arg_ret = _input.read_longlong();
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getEventCount",_opsClass);
                if (_so == null)
                   continue;
                org.coach.tracing.api.i_EventDataBaseConnectionOperations _self = (org.coach.tracing.api.i_EventDataBaseConnectionOperations) _so.servant;
                try
                {
                    return _self.getEventCount();
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation getUnmatchedEventCount
     */
    public long getUnmatchedEventCount()
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("getUnmatchedEventCount",true);
                    _input = this._invoke(_output);
                    long _arg_ret = _input.read_longlong();
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getUnmatchedEventCount",_opsClass);
                if (_so == null)
                   continue;
                org.coach.tracing.api.i_EventDataBaseConnectionOperations _self = (org.coach.tracing.api.i_EventDataBaseConnectionOperations) _so.servant;
                try
                {
                    return _self.getUnmatchedEventCount();
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation getMessageCount
     */
    public long getMessageCount()
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("getMessageCount",true);
                    _input = this._invoke(_output);
                    long _arg_ret = _input.read_longlong();
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getMessageCount",_opsClass);
                if (_so == null)
                   continue;
                org.coach.tracing.api.i_EventDataBaseConnectionOperations _self = (org.coach.tracing.api.i_EventDataBaseConnectionOperations) _so.servant;
                try
                {
                    return _self.getMessageCount();
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation getIdentityCount
     */
    public int getIdentityCount()
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("getIdentityCount",true);
                    _input = this._invoke(_output);
                    int _arg_ret = _input.read_long();
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getIdentityCount",_opsClass);
                if (_so == null)
                   continue;
                org.coach.tracing.api.i_EventDataBaseConnectionOperations _self = (org.coach.tracing.api.i_EventDataBaseConnectionOperations) _so.servant;
                try
                {
                    return _self.getIdentityCount();
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation getIdentity
     */
    public org.coach.tracing.api.Identity getIdentity(long identityKey)
        throws org.coach.tracing.api.InvalidKey
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("getIdentity",true);
                    _output.write_longlong(identityKey);
                    _input = this._invoke(_output);
                    org.coach.tracing.api.Identity _arg_ret = org.coach.tracing.api.IdentityHelper.read(_input);
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    if (_exception_id.equals(org.coach.tracing.api.InvalidKeyHelper.id()))
                    {
                        throw org.coach.tracing.api.InvalidKeyHelper.read(_exception.getInputStream());
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getIdentity",_opsClass);
                if (_so == null)
                   continue;
                org.coach.tracing.api.i_EventDataBaseConnectionOperations _self = (org.coach.tracing.api.i_EventDataBaseConnectionOperations) _so.servant;
                try
                {
                    return _self.getIdentity( identityKey);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation getParameterValues
     */
    public String getParameterValues(long parameterKey)
        throws org.coach.tracing.api.InvalidKey
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("getParameterValues",true);
                    _output.write_longlong(parameterKey);
                    _input = this._invoke(_output);
                    String _arg_ret = _input.read_string();
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    if (_exception_id.equals(org.coach.tracing.api.InvalidKeyHelper.id()))
                    {
                        throw org.coach.tracing.api.InvalidKeyHelper.read(_exception.getInputStream());
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getParameterValues",_opsClass);
                if (_so == null)
                   continue;
                org.coach.tracing.api.i_EventDataBaseConnectionOperations _self = (org.coach.tracing.api.i_EventDataBaseConnectionOperations) _so.servant;
                try
                {
                    return _self.getParameterValues( parameterKey);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

}
