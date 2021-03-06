package org.coach.tracing.api;

/**
 * Enum definition : IdentityKind
 *
 * @author OpenORB Compiler
*/
public final class IdentityKind implements org.omg.CORBA.portable.IDLEntity
{
    /**
     * Enum member CCM_DOMAIN value 
     */
    public static final int _CCM_DOMAIN = 0;

    /**
     * Enum member CCM_DOMAIN
     */
    public static final IdentityKind CCM_DOMAIN = new IdentityKind(_CCM_DOMAIN);

    /**
     * Enum member CCM_NODE value 
     */
    public static final int _CCM_NODE = 1;

    /**
     * Enum member CCM_NODE
     */
    public static final IdentityKind CCM_NODE = new IdentityKind(_CCM_NODE);

    /**
     * Enum member CCM_PROCESS value 
     */
    public static final int _CCM_PROCESS = 2;

    /**
     * Enum member CCM_PROCESS
     */
    public static final IdentityKind CCM_PROCESS = new IdentityKind(_CCM_PROCESS);

    /**
     * Enum member CCM_CONTAINER value 
     */
    public static final int _CCM_CONTAINER = 3;

    /**
     * Enum member CCM_CONTAINER
     */
    public static final IdentityKind CCM_CONTAINER = new IdentityKind(_CCM_CONTAINER);

    /**
     * Enum member CCM_COMPONENT value 
     */
    public static final int _CCM_COMPONENT = 4;

    /**
     * Enum member CCM_COMPONENT
     */
    public static final IdentityKind CCM_COMPONENT = new IdentityKind(_CCM_COMPONENT);

    /**
     * Enum member CCM_OBJECT value 
     */
    public static final int _CCM_OBJECT = 5;

    /**
     * Enum member CCM_OBJECT
     */
    public static final IdentityKind CCM_OBJECT = new IdentityKind(_CCM_OBJECT);

    /**
     * Internal member value 
     */
    private final int _IdentityKind_value;

    /**
     * Private constructor
     * @param  the enum value for this new member
     */
    private IdentityKind( final int value )
    {
        _IdentityKind_value = value;
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
        return _IdentityKind_value;
    }

    /**
     * Return a enum member from its value
     * @param  an enum value
     * @return an enum member
         */
    public static IdentityKind from_int(int value)
    {
        switch (value)
        {
        case 0 :
            return CCM_DOMAIN;
        case 1 :
            return CCM_NODE;
        case 2 :
            return CCM_PROCESS;
        case 3 :
            return CCM_CONTAINER;
        case 4 :
            return CCM_COMPONENT;
        case 5 :
            return CCM_OBJECT;
        }
        throw new org.omg.CORBA.BAD_OPERATION();
    }

    /**
     * Return a string representation
     * @return a string representation of the enumeration
     */
    public java.lang.String toString()
    {
        switch (_IdentityKind_value)
        {
        case 0 :
            return "CCM_DOMAIN";
        case 1 :
            return "CCM_NODE";
        case 2 :
            return "CCM_PROCESS";
        case 3 :
            return "CCM_CONTAINER";
        case 4 :
            return "CCM_COMPONENT";
        case 5 :
            return "CCM_OBJECT";
        }
        throw new org.omg.CORBA.BAD_OPERATION();
    }

}
