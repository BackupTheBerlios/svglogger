package org.coach.tracing.api;

/**
 * Struct definition : Parameter
 * 
 * @author OpenORB Compiler
*/
public final class Parameter implements org.omg.CORBA.portable.IDLEntity
{
    /**
     * Struct member dir
     */
    public String dir;

    /**
     * Struct member type
     */
    public String type;

    /**
     * Struct member name
     */
    public String name;

    /**
     * Default constructor
     */
    public Parameter()
    { }

    /**
     * Constructor with fields initialization
     * @param dir dir struct member
     * @param type type struct member
     * @param name name struct member
     */
    public Parameter(String dir, String type, String name)
    {
        this.dir = dir;
        this.type = type;
        this.name = name;
    }

}
