package org.coach.tracing.api;

/**
 * Holder class for : TraceEvents
 * 
 * @author OpenORB Compiler
 */
final public class TraceEventsHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal TraceEvents value
     */
    public org.coach.tracing.api.TraceEvent[] value;

    /**
     * Default constructor
     */
    public TraceEventsHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public TraceEventsHolder(org.coach.tracing.api.TraceEvent[] initial)
    {
        value = initial;
    }

    /**
     * Read TraceEvents from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = TraceEventsHelper.read(istream);
    }

    /**
     * Write TraceEvents into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        TraceEventsHelper.write(ostream,value);
    }

    /**
     * Return the TraceEvents TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return TraceEventsHelper.type();
    }

}
