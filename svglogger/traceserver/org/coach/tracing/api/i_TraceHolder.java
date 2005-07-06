package org.coach.tracing.api;

/**
 * Holder class for : i_Trace
 * 
 * @author OpenORB Compiler
 */
final public class i_TraceHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal i_Trace value
     */
    public org.coach.tracing.api.i_Trace value;

    /**
     * Default constructor
     */
    public i_TraceHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public i_TraceHolder(org.coach.tracing.api.i_Trace initial)
    {
        value = initial;
    }

    /**
     * Read i_Trace from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = i_TraceHelper.read(istream);
    }

    /**
     * Write i_Trace into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        i_TraceHelper.write(ostream,value);
    }

    /**
     * Return the i_Trace TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return i_TraceHelper.type();
    }

}
