package org.coach.tracing.api;

/**
 * Holder class for : IdentityKind
 * 
 * @author OpenORB Compiler
 */
final public class IdentityKindHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal IdentityKind value
     */
    public org.coach.tracing.api.IdentityKind value;

    /**
     * Default constructor
     */
    public IdentityKindHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public IdentityKindHolder(org.coach.tracing.api.IdentityKind initial)
    {
        value = initial;
    }

    /**
     * Read IdentityKind from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = IdentityKindHelper.read(istream);
    }

    /**
     * Write IdentityKind into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        IdentityKindHelper.write(ostream,value);
    }

    /**
     * Return the IdentityKind TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return IdentityKindHelper.type();
    }

}
