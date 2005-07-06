package org.coach.tracing.api;

/**
 * Struct definition : Identity
 * 
 * @author OpenORB Compiler
*/
public final class Identity implements org.omg.CORBA.portable.IDLEntity
{
    /**
     * Struct member kind
     */
    public org.coach.tracing.api.IdentityKind kind;

    /**
     * Struct member name
     */
    public String name;

    /**
     * Struct member type
     */
    public String type;

    /**
     * Struct member linkKey
     */
    public long linkKey;

    /**
     * Default constructor
     */
    public Identity()
    { }

    /**
     * Constructor with fields initialization
     * @param kind kind struct member
     * @param name name struct member
     * @param type type struct member
     * @param linkKey linkKey struct member
     */
    public Identity(org.coach.tracing.api.IdentityKind kind, String name, String type, long linkKey)
    {
        this.kind = kind;
        this.name = name;
        this.type = type;
        this.linkKey = linkKey;
    }

}
