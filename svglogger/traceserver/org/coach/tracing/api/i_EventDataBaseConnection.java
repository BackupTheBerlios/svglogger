package org.coach.tracing.api;

/**
 * The i_EventDataBaseConnection interface is used to retrieve TraceRecords from an
 * EventDataBase. The connection is either realtime or on an existing, stored database,
 * depending on the way the connection was opened. With realtime connections, the number of events
 * will increase as new events are received by the database server.
 */
public interface i_EventDataBaseConnection extends i_EventDataBaseConnectionOperations, org.omg.CORBA.Object, org.omg.CORBA.portable.IDLEntity
{
}
