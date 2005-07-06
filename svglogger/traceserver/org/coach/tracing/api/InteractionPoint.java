package org.coach.tracing.api;

/**
 * Enum definition : InteractionPoint
 *
 * @author OpenORB Compiler
*/
public final class InteractionPoint implements org.omg.CORBA.portable.IDLEntity
{
    /**
     * Enum member STUB_OUT value 
     */
    public static final int _STUB_OUT = 0;

    /**
     * Enum member STUB_OUT
     */
    public static final InteractionPoint STUB_OUT = new InteractionPoint(_STUB_OUT);

    /**
     * Enum member POA_IN value 
     */
    public static final int _POA_IN = 1;

    /**
     * Enum member POA_IN
     */
    public static final InteractionPoint POA_IN = new InteractionPoint(_POA_IN);

    /**
     * Enum member POA_OUT value 
     */
    public static final int _POA_OUT = 2;

    /**
     * Enum member POA_OUT
     */
    public static final InteractionPoint POA_OUT = new InteractionPoint(_POA_OUT);

    /**
     * Enum member POA_OUT_EXCEPTION value 
     */
    public static final int _POA_OUT_EXCEPTION = 3;

    /**
     * Enum member POA_OUT_EXCEPTION
     */
    public static final InteractionPoint POA_OUT_EXCEPTION = new InteractionPoint(_POA_OUT_EXCEPTION);

    /**
     * Enum member STUB_IN value 
     */
    public static final int _STUB_IN = 4;

    /**
     * Enum member STUB_IN
     */
    public static final InteractionPoint STUB_IN = new InteractionPoint(_STUB_IN);

    /**
     * Enum member STUB_IN_EXCEPTION value 
     */
    public static final int _STUB_IN_EXCEPTION = 5;

    /**
     * Enum member STUB_IN_EXCEPTION
     */
    public static final InteractionPoint STUB_IN_EXCEPTION = new InteractionPoint(_STUB_IN_EXCEPTION);

    /**
     * Enum member ONEWAY_STUB_OUT value 
     */
    public static final int _ONEWAY_STUB_OUT = 6;

    /**
     * Enum member ONEWAY_STUB_OUT
     */
    public static final InteractionPoint ONEWAY_STUB_OUT = new InteractionPoint(_ONEWAY_STUB_OUT);

    /**
     * Enum member ONEWAY_POA_IN value 
     */
    public static final int _ONEWAY_POA_IN = 7;

    /**
     * Enum member ONEWAY_POA_IN
     */
    public static final InteractionPoint ONEWAY_POA_IN = new InteractionPoint(_ONEWAY_POA_IN);

    /**
     * Internal member value 
     */
    private final int _InteractionPoint_value;

    /**
     * Private constructor
     * @param  the enum value for this new member
     */
    private InteractionPoint( final int value )
    {
        _InteractionPoint_value = value;
    }

    /**
     * Maintains singleton property for serialized enums.
     * Issue 4271: IDL/Java issue, Mapping for IDL enum.
     */
    public java.lang.Object readResolve() throws java.io.ObjectStreamException
    {
        return from_int( value() );
    }

    /**
     * Return the internal member value
     * @return the member value
     */
    public int value()
    {
        return _InteractionPoint_value;
    }

    /**
     * Return a enum member from its value
     * @param  an enum value
     * @return an enum member
         */
    public static InteractionPoint from_int(int value)
    {
        switch (value)
        {
        case 0 :
            return STUB_OUT;
        case 1 :
            return POA_IN;
        case 2 :
            return POA_OUT;
        case 3 :
            return POA_OUT_EXCEPTION;
        case 4 :
            return STUB_IN;
        case 5 :
            return STUB_IN_EXCEPTION;
        case 6 :
            return ONEWAY_STUB_OUT;
        case 7 :
            return ONEWAY_POA_IN;
        }
        throw new org.omg.CORBA.BAD_OPERATION();
    }

    /**
     * Return a string representation
     * @return a string representation of the enumeration
     */
    public java.lang.String toString()
    {
        switch (_InteractionPoint_value)
        {
        case 0 :
            return "STUB_OUT";
        case 1 :
            return "POA_IN";
        case 2 :
            return "POA_OUT";
        case 3 :
            return "POA_OUT_EXCEPTION";
        case 4 :
            return "STUB_IN";
        case 5 :
            return "STUB_IN_EXCEPTION";
        case 6 :
            return "ONEWAY_STUB_OUT";
        case 7 :
            return "ONEWAY_POA_IN";
        }
        throw new org.omg.CORBA.BAD_OPERATION();
    }

}
