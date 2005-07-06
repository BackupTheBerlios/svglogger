package org.coach.tracing.api;

/**
 * Holder class for : i_EventDataBaseConnection
 * 
 * @author OpenORB Compiler
 */
final public class i_EventDataBaseConnectionHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal i_EventDataBaseConnection value
     */
    public org.coach.tracing.api.i_EventDataBaseConnection value;

    /**
     * Default constructor
     */
    public i_EventDataBaseConnectionHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public i_EventDataBaseConnectionHolder(org.coach.tracing.api.i_EventDataBaseConnection initial)
    {
        value = initial;
    }

    /**
     * Read i_EventDataBaseConnection from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = i_EventDataBaseConnectionHelper.read(istream);
    }

    /**
     * Write i_EventDataBaseConnection into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        i_EventDataBaseConnectionHelper.write(ostream,value);
    }

    /**
     * Return the i_EventDataBaseConnection TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return i_EventDataBaseConnectionHelper.type();
    }

}
