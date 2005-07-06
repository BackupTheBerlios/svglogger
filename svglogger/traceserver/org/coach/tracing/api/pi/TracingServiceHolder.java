package org.coach.tracing.api.pi;

/**
 * Holder class for : TracingService
 * 
 * @author OpenORB Compiler
 */
final public class TracingServiceHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal TracingService value
     */
    public org.coach.tracing.api.pi.TracingService value;

    /**
     * Default constructor
     */
    public TracingServiceHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public TracingServiceHolder(org.coach.tracing.api.pi.TracingService initial)
    {
        value = initial;
    }

    /**
     * Read TracingService from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = TracingServiceHelper.read(istream);
    }

    /**
     * Write TracingService into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        TracingServiceHelper.write(ostream,value);
    }

    /**
     * Return the TracingService TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return TracingServiceHelper.type();
    }

}
