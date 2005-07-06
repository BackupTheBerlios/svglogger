package org.coach.tracing.api;

/**
 * Struct definition : TraceEvent
 * 
 * @author OpenORB Compiler
*/
public final class TraceEvent implements org.omg.CORBA.portable.IDLEntity
{
    /**
     * Struct member time_stamp
     */
    public long time_stamp;

    /**
     * Struct member interaction_point
     */
    public org.coach.tracing.api.InteractionPoint interaction_point;

    /**
     * Struct member trail_label
     */
    public String trail_label;

    /**
     * Struct member message_id
     */
    public String message_id;

    /**
     * Struct member thread_id
     */
    public String thread_id;

    /**
     * Struct member trail_id
     */
    public String trail_id;

    /**
     * Struct member event_counter
     */
    public int event_counter;

    /**
     * Struct member op_name
     */
    public String op_name;

    /**
     * Struct member identity
     */
    public org.coach.tracing.api.IdentityDescriptor identity;

    /**
     * Struct member parameters
     */
    public org.coach.tracing.api.Parameter[] parameters;

    /**
     * Struct member parameter_values
     */
    public org.omg.CORBA.Any[] parameter_values;

    /**
     * Default constructor
     */
    public TraceEvent()
    { }

    /**
     * Constructor with fields initialization
     * @param time_stamp time_stamp struct member
     * @param interaction_point interaction_point struct member
     * @param trail_label trail_label struct member
     * @param message_id message_id struct member
     * @param thread_id thread_id struct member
     * @param trail_id trail_id struct member
     * @param event_counter event_counter struct member
     * @param op_name op_name struct member
     * @param identity identity struct member
     * @param parameters parameters struct member
     * @param parameter_values parameter_values struct member
     */
    public TraceEvent(long time_stamp, org.coach.tracing.api.InteractionPoint interaction_point, String trail_label, String message_id, String thread_id, String trail_id, int event_counter, String op_name, org.coach.tracing.api.IdentityDescriptor identity, org.coach.tracing.api.Parameter[] parameters, org.omg.CORBA.Any[] parameter_values)
    {
        this.time_stamp = time_stamp;
        this.interaction_point = interaction_point;
        this.trail_label = trail_label;
        this.message_id = message_id;
        this.thread_id = thread_id;
        this.trail_id = trail_id;
        this.event_counter = event_counter;
        this.op_name = op_name;
        this.identity = identity;
        this.parameters = parameters;
        this.parameter_values = parameter_values;
    }

}
