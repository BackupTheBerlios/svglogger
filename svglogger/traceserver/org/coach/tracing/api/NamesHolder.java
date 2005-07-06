package org.coach.tracing.api;

/**
 * Holder class for : Names
 * 
 * @author OpenORB Compiler
 */
final public class NamesHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal Names value
     */
    public String[] value;

    /**
     * Default constructor
     */
    public NamesHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public NamesHolder(String[] initial)
    {
        value = initial;
    }

    /**
     * Read Names from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = NamesHelper.read(istream);
    }

    /**
     * Write Names into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        NamesHelper.write(ostream,value);
    }

    /**
     * Return the Names TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return NamesHelper.type();
    }

}
