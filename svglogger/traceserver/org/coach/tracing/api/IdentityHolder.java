package org.coach.tracing.api;

/**
 * Holder class for : Identity
 * 
 * @author OpenORB Compiler
 */
final public class IdentityHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal Identity value
     */
    public org.coach.tracing.api.Identity value;

    /**
     * Default constructor
     */
    public IdentityHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public IdentityHolder(org.coach.tracing.api.Identity initial)
    {
        value = initial;
    }

    /**
     * Read Identity from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = IdentityHelper.read(istream);
    }

    /**
     * Write Identity into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        IdentityHelper.write(ostream,value);
    }

    /**
     * Return the Identity TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return IdentityHelper.type();
    }

}
