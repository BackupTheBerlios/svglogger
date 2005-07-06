package org.coach.tracing.api;

/**
 * Holder class for : IdentityDescriptor
 * 
 * @author OpenORB Compiler
 */
final public class IdentityDescriptorHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal IdentityDescriptor value
     */
    public org.coach.tracing.api.IdentityDescriptor value;

    /**
     * Default constructor
     */
    public IdentityDescriptorHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public IdentityDescriptorHolder(org.coach.tracing.api.IdentityDescriptor initial)
    {
        value = initial;
    }

    /**
     * Read IdentityDescriptor from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = IdentityDescriptorHelper.read(istream);
    }

    /**
     * Write IdentityDescriptor into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        IdentityDescriptorHelper.write(ostream,value);
    }

    /**
     * Return the IdentityDescriptor TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return IdentityDescriptorHelper.type();
    }

}
