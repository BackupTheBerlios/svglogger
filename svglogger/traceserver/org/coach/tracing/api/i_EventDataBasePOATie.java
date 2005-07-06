package org.coach.tracing.api;

/**
 * Interface definition : i_EventDataBase
 * 
 * @author OpenORB Compiler
 */
public class i_EventDataBasePOATie extends i_EventDataBasePOA
{

    //
    // Private reference to implementation object
    //
    private i_EventDataBaseOperations _tie;

    //
    // Private reference to POA
    //
    private org.omg.PortableServer.POA _poa;

    /**
     * Constructor
     */
    public i_EventDataBasePOATie(i_EventDataBaseOperations tieObject)
    {
        _tie = tieObject;
    }

    /**
     * Constructor
     */
    public i_EventDataBasePOATie(i_EventDataBaseOperations tieObject, org.omg.PortableServer.POA poa)
    {
        _tie = tieObject;
        _poa = poa;
    }

    /**
     * Get the delegate
     */
    public i_EventDataBaseOperations _delegate()
    {
        return _tie;
    }

    /**
     * Set the delegate
     */
    public void _delegate(i_EventDataBaseOperations delegate_)
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
     * Operation open
     */
    public org.coach.tracing.api.i_EventDataBaseConnection open(String name)
        throws org.coach.tracing.api.InvalidName
    {
        return _tie.open( name);
    }

    /**
     * Operation close
     */
    public void close(org.coach.tracing.api.i_EventDataBaseConnection connection)
    {
        _tie.close( connection);
    }

    /**
     * Operation list
     */
    public String[] list()
    {
        return _tie.list();
    }

    /**
     * Operation connect
     */
    public org.coach.tracing.api.i_EventDataBaseConnection connect()
    {
        return _tie.connect();
    }

    /**
     * Operation save
     */
    public void save(String name)
    {
        _tie.save( name);
    }

}
