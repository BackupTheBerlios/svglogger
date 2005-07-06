package org.coach.tracing.api;

/**
 * Holder class for : PropagationContext
 * 
 * @author OpenORB Compiler
 */
final public class PropagationContextHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal PropagationContext value
     */
    public org.coach.tracing.api.PropagationContext value;

    /**
     * Default constructor
     */
    public PropagationContextHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public PropagationContextHolder(org.coach.tracing.api.PropagationContext initial)
    {
        value = initial;
    }

    /**
     * Read PropagationContext from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = PropagationContextHelper.read(istream);
    }

    /**
     * Write PropagationContext into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        PropagationContextHelper.write(ostream,value);
    }

    /**
     * Return the PropagationContext TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return PropagationContextHelper.type();
    }

}
