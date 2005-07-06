package org.coach.tracing.api;

/**
 * Holder class for : ParameterList
 * 
 * @author OpenORB Compiler
 */
final public class ParameterListHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal ParameterList value
     */
    public org.coach.tracing.api.Parameter[] value;

    /**
     * Default constructor
     */
    public ParameterListHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public ParameterListHolder(org.coach.tracing.api.Parameter[] initial)
    {
        value = initial;
    }

    /**
     * Read ParameterList from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = ParameterListHelper.read(istream);
    }

    /**
     * Write ParameterList into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        ParameterListHelper.write(ostream,value);
    }

    /**
     * Return the ParameterList TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return ParameterListHelper.type();
    }

}
