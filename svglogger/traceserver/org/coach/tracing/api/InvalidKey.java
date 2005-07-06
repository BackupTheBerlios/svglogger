package org.coach.tracing.api;

/**
 * Exception definition : InvalidKey
 * 
 * @author OpenORB Compiler
 */
public final class InvalidKey extends org.omg.CORBA.UserException
{
    /**
     * Default constructor
     */
    public InvalidKey()
    {
        super(InvalidKeyHelper.id());
    }

    /**
     * Full constructor with fields initialization
     */
    public InvalidKey(String orb_reason)
    {
        super(InvalidKeyHelper.id() +" " +  orb_reason);
    }

}
