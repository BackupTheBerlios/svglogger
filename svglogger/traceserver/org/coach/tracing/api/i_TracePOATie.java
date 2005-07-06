package org.coach.tracing.api;

/**
 * Interface definition : i_Trace
 * 
 * @author OpenORB Compiler
 */
public class i_TracePOATie extends i_TracePOA
{

    //
    // Private reference to implementation object
    //
    private i_TraceOperations _tie;

    //
    // Private reference to POA
    //
    private org.omg.PortableServer.POA _poa;

    /**
     * Constructor
     */
    public i_TracePOATie(i_TraceOperations tieObject)
    {
        _tie = tieObject;
    }

    /**
     * Constructor
     */
    public i_TracePOATie(i_TraceOperations tieObject, org.omg.PortableServer.POA poa)
    {
        _tie = tieObject;
        _poa = poa;
    }

    /**
     * Get the delegate
     */
    public i_TraceOperations _delegate()
    {
        return _tie;
    }

    /**
     * Set the delegate
     */
    public void _delegate(i_TraceOperations delegate_)
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
     * Operation receiveEvent
     */
    public void receiveEvent(org.coach.tracing.api.TraceEvent[] events)
    {
        _tie.receiveEvent( events);
    }

}
