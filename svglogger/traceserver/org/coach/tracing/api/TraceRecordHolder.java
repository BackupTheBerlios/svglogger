package org.coach.tracing.api;

/**
 * Holder class for : TraceRecord
 * 
 * @author OpenORB Compiler
 */
final public class TraceRecordHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal TraceRecord value
     */
    public org.coach.tracing.api.TraceRecord value;

    /**
     * Default constructor
     */
    public TraceRecordHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public TraceRecordHolder(org.coach.tracing.api.TraceRecord initial)
    {
        value = initial;
    }

    /**
     * Read TraceRecord from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = TraceRecordHelper.read(istream);
    }

    /**
     * Write TraceRecord into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        TraceRecordHelper.write(ostream,value);
    }

    /**
     * Return the TraceRecord TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return TraceRecordHelper.type();
    }

}
