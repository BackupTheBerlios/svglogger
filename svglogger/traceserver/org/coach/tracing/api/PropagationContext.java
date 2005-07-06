package org.coach.tracing.api;

/**
 * Struct definition : PropagationContext
 * 
 * @author OpenORB Compiler
*/
public final class PropagationContext implements org.omg.CORBA.portable.IDLEntity
{
    /**
     * Struct member trail_label
     */
    public String trail_label;

    /**
     * Struct member message_id
     */
    public String message_id;

    /**
     * Struct member trail_id
     */
    public String trail_id;

    /**
     * Default constructor
     */
    public PropagationContext()
    { }

    /**
     * Constructor with fields initialization
     * @param trail_label trail_label struct member
     * @param message_id message_id struct member
     * @param trail_id trail_id struct member
     */
    public PropagationContext(String trail_label, String message_id, String trail_id)
    {
        this.trail_label = trail_label;
        this.message_id = message_id;
        this.trail_id = trail_id;
    }

}
