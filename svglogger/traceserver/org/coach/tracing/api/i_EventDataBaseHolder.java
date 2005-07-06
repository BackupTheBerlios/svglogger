package org.coach.tracing.api;

/**
 * Holder class for : i_EventDataBase
 * 
 * @author OpenORB Compiler
 */
final public class i_EventDataBaseHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal i_EventDataBase value
     */
    public org.coach.tracing.api.i_EventDataBase value;

    /**
     * Default constructor
     */
    public i_EventDataBaseHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public i_EventDataBaseHolder(org.coach.tracing.api.i_EventDataBase initial)
    {
        value = initial;
    }

    /**
     * Read i_EventDataBase from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = i_EventDataBaseHelper.read(istream);
    }

    /**
     * Write i_EventDataBase into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        i_EventDataBaseHelper.write(ostream,value);
    }

    /**
     * Return the i_EventDataBase TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return i_EventDataBaseHelper.type();
    }

}
