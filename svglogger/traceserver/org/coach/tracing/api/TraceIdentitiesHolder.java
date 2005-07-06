package org.coach.tracing.api;

/**
 * Holder class for : TraceIdentities
 * 
 * @author OpenORB Compiler
 */
final public class TraceIdentitiesHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal TraceIdentities value
     */
    public org.coach.tracing.api.IdentityDescriptor[] value;

    /**
     * Default constructor
     */
    public TraceIdentitiesHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public TraceIdentitiesHolder(org.coach.tracing.api.IdentityDescriptor[] initial)
    {
        value = initial;
    }

    /**
     * Read TraceIdentities from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = TraceIdentitiesHelper.read(istream);
    }

    /**
     * Write TraceIdentities into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        TraceIdentitiesHelper.write(ostream,value);
    }

    /**
     * Return the TraceIdentities TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return TraceIdentitiesHelper.type();
    }

}
