package org.coach.tracing.api;

/**
 * The i_EventDataBaseConnection interface is used to retrieve TraceRecords from an
 * EventDataBase. The connection is either realtime or on an existing, stored database,
 * depending on the way the connection was opened. With realtime connections, the number of events
 * will increase as new events are received by the database server.
 */
public interface i_EventDataBaseConnectionOperations
{
    /**
     * Retrieves an event by its index number
     */
    public org.coach.tracing.api.TraceRecord getEventAt(long index)
        throws org.coach.tracing.api.IndexOutOfRange;

    /**
     * Retrieves an event by its index number
     */
    public String getXmlEventAt(long index)
        throws org.coach.tracing.api.IndexOutOfRange;

    /**
     * Retrieves multiple events by index number and length
     */
    public String getXmlEvents(long index, int length)
        throws org.coach.tracing.api.IndexOutOfRange;

    /**
     * Retrieves one or more identities by there key values - returns result in XML format
     */
    public String getXmlIdentities(long[] keys)
        throws org.coach.tracing.api.InvalidKey;

    /**
     * Retrieves an event by its key value
     */
    public org.coach.tracing.api.TraceRecord getEvent(long key_id)
        throws org.coach.tracing.api.InvalidKey;

    /**
     * Retrieves an event by its key value
     */
    public String getXmlEvent(long key_id)
        throws org.coach.tracing.api.InvalidKey;

    /**
     * Retrieves the current index order of an event.
     * The index order is not constant for realtime connections as new events
     * may be inserted before the current index.
     * The index order value can never decrease though.
     */
    public long getEventIndex(long key_id)
        throws org.coach.tracing.api.InvalidKey;

    /**
     * Returns the number of events in the database.
     */
    public long getEventCount();

    /**
     * Returns the number of unmatched events in the database.
     */
    public long getUnmatchedEventCount();

    /**
     * Returns the number of messages in the database.
     */
    public long getMessageCount();

    /**
     * Retrieves the list of identities.
     */
    public int getIdentityCount();

    /**
     * Retrieves an identity by its key value
     */
    public org.coach.tracing.api.Identity getIdentity(long identityKey)
        throws org.coach.tracing.api.InvalidKey;

    /**
     * Retrieves an event parameter value in XML format by its parameter key value
     */
    public String getParameterValues(long parameterKey)
        throws org.coach.tracing.api.InvalidKey;

}
