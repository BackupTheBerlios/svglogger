package org.coach.tracing.api;

/**
 * The i_EventDataBaseConnection interface is used to retrieve TraceRecords from an
 * EventDataBase. The connection is either realtime or on an existing, stored database,
 * depending on the way the connection was opened. With realtime connections, the number of events
 * will increase as new events are received by the database server.
 */
public class i_EventDataBaseConnectionPOATie extends i_EventDataBaseConnectionPOA
{

    //
    // Private reference to implementation object
    //
    private i_EventDataBaseConnectionOperations _tie;

    //
    // Private reference to POA
    //
    private org.omg.PortableServer.POA _poa;

    /**
     * Constructor
     */
    public i_EventDataBaseConnectionPOATie(i_EventDataBaseConnectionOperations tieObject)
    {
        _tie = tieObject;
    }

    /**
     * Constructor
     */
    public i_EventDataBaseConnectionPOATie(i_EventDataBaseConnectionOperations tieObject, org.omg.PortableServer.POA poa)
    {
        _tie = tieObject;
        _poa = poa;
    }

    /**
     * Get the delegate
     */
    public i_EventDataBaseConnectionOperations _delegate()
    {
        return _tie;
    }

    /**
     * Set the delegate
     */
    public void _delegate(i_EventDataBaseConnectionOperations delegate_)
    {
        _tie = delegate_;
    }

    /**
     * _default_POA method
     */
    public org.omg.PortableServer.POA _default_POA()
    {
        if (_poa != null)
            return _poa;
        else
            return super._default_POA();
    }

    /**
     * Operation getEventAt
     */
    public org.coach.tracing.api.TraceRecord getEventAt(long index)
        throws org.coach.tracing.api.IndexOutOfRange
    {
        return _tie.getEventAt( index);
    }

    /**
     * Operation getXmlEventAt
     */
    public String getXmlEventAt(long index)
        throws org.coach.tracing.api.IndexOutOfRange
    {
        return _tie.getXmlEventAt( index);
    }

    /**
     * Operation getXmlEvents
     */
    public String getXmlEvents(long index, int length)
        throws org.coach.tracing.api.IndexOutOfRange
    {
        return _tie.getXmlEvents( index,  length);
    }

    /**
     * Operation getXmlIdentities
     */
    public String getXmlIdentities(long[] keys)
        throws org.coach.tracing.api.InvalidKey
    {
        return _tie.getXmlIdentities( keys);
    }

    /**
     * Operation getEvent
     */
    public org.coach.tracing.api.TraceRecord getEvent(long key_id)
        throws org.coach.tracing.api.InvalidKey
    {
        return _tie.getEvent( key_id);
    }

    /**
     * Operation getXmlEvent
     */
    public String getXmlEvent(long key_id)
        throws org.coach.tracing.api.InvalidKey
    {
        return _tie.getXmlEvent( key_id);
    }

    /**
     * Operation getEventIndex
     */
    public long getEventIndex(long key_id)
        throws org.coach.tracing.api.InvalidKey
    {
        return _tie.getEventIndex( key_id);
    }

    /**
     * Operation getEventCount
     */
    public long getEventCount()
    {
        return _tie.getEventCount();
    }

    /**
     * Operation getUnmatchedEventCount
     */
    public long getUnmatchedEventCount()
    {
        return _tie.getUnmatchedEventCount();
    }

    /**
     * Operation getMessageCount
     */
    public long getMessageCount()
    {
        return _tie.getMessageCount();
    }

    /**
     * Operation getIdentityCount
     */
    public int getIdentityCount()
    {
        return _tie.getIdentityCount();
    }

    /**
     * Operation getIdentity
     */
    public org.coach.tracing.api.Identity getIdentity(long identityKey)
        throws org.coach.tracing.api.InvalidKey
    {
        return _tie.getIdentity( identityKey);
    }

    /**
     * Operation getParameterValues
     */
    public String getParameterValues(long parameterKey)
        throws org.coach.tracing.api.InvalidKey
    {
        return _tie.getParameterValues( parameterKey);
    }

}
