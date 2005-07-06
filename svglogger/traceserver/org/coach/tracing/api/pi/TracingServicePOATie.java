package org.coach.tracing.api.pi;

/**
 * Interface definition : TracingService
 * 
 * @author OpenORB Compiler
 */
public class TracingServicePOATie extends TracingServicePOA
{

    //
    // Private reference to implementation object
    //
    private TracingServiceOperations _tie;

    //
    // Private reference to POA
    //
    private org.omg.PortableServer.POA _poa;

    /**
     * Constructor
     */
    public TracingServicePOATie(TracingServiceOperations tieObject)
    {
        _tie = tieObject;
    }

    /**
     * Constructor
     */
    public TracingServicePOATie(TracingServiceOperations tieObject, org.omg.PortableServer.POA poa)
    {
        _tie = tieObject;
        _poa = poa;
    }

    /**
     * Get the delegate
     */
    public TracingServiceOperations _delegate()
    {
        return _tie;
    }

    /**
     * Set the delegate
     */
    public void _delegate(TracingServiceOperations delegate_)
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
     * Operation start
     */
    public void start()
    {
        _tie.start();
    }

    /**
     * Operation stop
     */
    public void stop()
    {
        _tie.stop();
    }

}
