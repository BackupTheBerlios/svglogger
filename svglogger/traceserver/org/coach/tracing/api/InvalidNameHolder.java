package org.coach.tracing.api;

/**
 * Holder class for : InvalidName
 * 
 * @author OpenORB Compiler
 */
final public class InvalidNameHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal InvalidName value
     */
    public org.coach.tracing.api.InvalidName value;

    /**
     * Default constructor
     */
    public InvalidNameHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public InvalidNameHolder(org.coach.tracing.api.InvalidName initial)
    {
        value = initial;
    }

    /**
     * Read InvalidName from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = InvalidNameHelper.read(istream);
    }

    /**
     * Write InvalidName into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        InvalidNameHelper.write(ostream,value);
    }

    /**
     * Return the InvalidName TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return InvalidNameHelper.type();
    }

}
