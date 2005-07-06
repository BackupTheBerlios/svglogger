package org.coach.tracing.api;

/**
 * Holder class for : AnyList
 * 
 * @author OpenORB Compiler
 */
final public class AnyListHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal AnyList value
     */
    public org.omg.CORBA.Any[] value;

    /**
     * Default constructor
     */
    public AnyListHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public AnyListHolder(org.omg.CORBA.Any[] initial)
    {
        value = initial;
    }

    /**
     * Read AnyList from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = AnyListHelper.read(istream);
    }

    /**
     * Write AnyList into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        AnyListHelper.write(ostream,value);
    }

    /**
     * Return the AnyList TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return AnyListHelper.type();
    }

}
