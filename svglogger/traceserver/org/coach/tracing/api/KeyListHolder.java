package org.coach.tracing.api;

/**
 * Holder class for : KeyList
 * 
 * @author OpenORB Compiler
 */
final public class KeyListHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal KeyList value
     */
    public long[] value;

    /**
     * Default constructor
     */
    public KeyListHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public KeyListHolder(long[] initial)
    {
        value = initial;
    }

    /**
     * Read KeyList from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = KeyListHelper.read(istream);
    }

    /**
     * Write KeyList into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        KeyListHelper.write(ostream,value);
    }

    /**
     * Return the KeyList TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return KeyListHelper.type();
    }

}
