package org.coach.tracing.api;

/**
 * Holder class for : InvalidKey
 * 
 * @author OpenORB Compiler
 */
final public class InvalidKeyHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal InvalidKey value
     */
    public org.coach.tracing.api.InvalidKey value;

    /**
     * Default constructor
     */
    public InvalidKeyHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public InvalidKeyHolder(org.coach.tracing.api.InvalidKey initial)
    {
        value = initial;
    }

    /**
     * Read InvalidKey from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = InvalidKeyHelper.read(istream);
    }

    /**
     * Write InvalidKey into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        InvalidKeyHelper.write(ostream,value);
    }

    /**
     * Return the InvalidKey TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return InvalidKeyHelper.type();
    }

}
