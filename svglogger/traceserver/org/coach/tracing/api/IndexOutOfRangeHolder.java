package org.coach.tracing.api;

/**
 * Holder class for : IndexOutOfRange
 * 
 * @author OpenORB Compiler
 */
final public class IndexOutOfRangeHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal IndexOutOfRange value
     */
    public org.coach.tracing.api.IndexOutOfRange value;

    /**
     * Default constructor
     */
    public IndexOutOfRangeHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public IndexOutOfRangeHolder(org.coach.tracing.api.IndexOutOfRange initial)
    {
        value = initial;
    }

    /**
     * Read IndexOutOfRange from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = IndexOutOfRangeHelper.read(istream);
    }

    /**
     * Write IndexOutOfRange into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        IndexOutOfRangeHelper.write(ostream,value);
    }

    /**
     * Return the IndexOutOfRange TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return IndexOutOfRangeHelper.type();
    }

}
