package org.coach.tracing.api;

/**
 * Exception definition : IndexOutOfRange
 * 
 * @author OpenORB Compiler
 */
public final class IndexOutOfRange extends org.omg.CORBA.UserException
{
    /**
     * Default constructor
     */
    public IndexOutOfRange()
    {
        super(IndexOutOfRangeHelper.id());
    }

    /**
     * Full constructor with fields initialization
     */
    public IndexOutOfRange(String orb_reason)
    {
        super(IndexOutOfRangeHelper.id() +" " +  orb_reason);
    }

}
