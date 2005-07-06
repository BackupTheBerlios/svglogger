package org.coach.tracing.api;

/**
 * Interface definition : i_EventDataBase
 * 
 * @author OpenORB Compiler
 */
public interface i_EventDataBaseOperations
{
    /**
     * Opens an existing database for viewing.
     */
    public org.coach.tracing.api.i_EventDataBaseConnection open(String name)
        throws org.coach.tracing.api.InvalidName;

    /**
     * Closes the database connection.
     */
    public void close(org.coach.tracing.api.i_EventDataBaseConnection connection);

    /**
     * Returns a list of database names which can be opened.
     */
    public String[] list();

    /**
     * Opens a realtime connection to the current active trace server.
     */
    public org.coach.tracing.api.i_EventDataBaseConnection connect();

    /**
     * Save the realtime state to the given database name.
     */
    public void save(String name);

}
