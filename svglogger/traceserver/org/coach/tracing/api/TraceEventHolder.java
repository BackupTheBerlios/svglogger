package org.coach.tracing.api;

/**
 * Holder class for : TraceEvent
 * 
 * @author OpenORB Compiler
 */
final public class TraceEventHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal TraceEvent value
     */
    public org.coach.tracing.api.TraceEvent value;

    /**
     * Default constructor
     */
    public TraceEventHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public TraceEventHolder(org.coach.tracing.api.TraceEvent initial)
    {
        value = initial;
    }

    /**
     * Read TraceEvent from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = TraceEventHelper.read(istream);
    }

    /**
     * Write TraceEvent into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        TraceEventHelper.write(ostream,value);
    }

    /**
     * Return the TraceEvent TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return TraceEventHelper.type();
    }

}
