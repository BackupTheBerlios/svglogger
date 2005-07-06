package org.coach.tracing.api;

/**
 * Holder class for : InteractionPoint
 * 
 * @author OpenORB Compiler
 */
final public class InteractionPointHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal InteractionPoint value
     */
    public org.coach.tracing.api.InteractionPoint value;

    /**
     * Default constructor
     */
    public InteractionPointHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public InteractionPointHolder(org.coach.tracing.api.InteractionPoint initial)
    {
        value = initial;
    }

    /**
     * Read InteractionPoint from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = InteractionPointHelper.read(istream);
    }

    /**
     * Write InteractionPoint into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        InteractionPointHelper.write(ostream,value);
    }

    /**
     * Return the InteractionPoint TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return InteractionPointHelper.type();
    }

}
