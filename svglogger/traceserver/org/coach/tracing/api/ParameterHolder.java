package org.coach.tracing.api;

/**
 * Holder class for : Parameter
 * 
 * @author OpenORB Compiler
 */
final public class ParameterHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal Parameter value
     */
    public org.coach.tracing.api.Parameter value;

    /**
     * Default constructor
     */
    public ParameterHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public ParameterHolder(org.coach.tracing.api.Parameter initial)
    {
        value = initial;
    }

    /**
     * Read Parameter from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = ParameterHelper.read(istream);
    }

    /**
     * Write Parameter into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        ParameterHelper.write(ostream,value);
    }

    /**
     * Return the Parameter TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return ParameterHelper.type();
    }

}
