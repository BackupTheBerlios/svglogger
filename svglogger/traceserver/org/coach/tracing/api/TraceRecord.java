package org.coach.tracing.api;

/**
 * Struct definition : TraceRecord
 * 
 * @author OpenORB Compiler
*/
public final class TraceRecord implements org.omg.CORBA.portable.IDLEntity
{
    /**
     * Struct member key_id
     */
    public long key_id;

    /**
     * Struct member time_stamp
     */
    public long time_stamp;

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
     * Struct member event_counter
     */
    public int event_counter;

    /**
     * Struct member thread_id
     */
    public String thread_id;

    /**
     * Struct member op_name
     */
    public String op_name;

    /**
     * Struct member interaction_point
     */
    public org.coach.tracing.api.InteractionPoint interaction_point;

    /**
     * Struct member identityKey
     */
    public long identityKey;

    /**
     * Struct member parametersKey
     */
    public long parametersKey;

    /**
     * Struct member linkKey
     */
    public long linkKey;

    /**
     * Default constructor
     */
    public TraceRecord()
    { }

    /**
     * Constructor with fields initialization
     * @param key_id key_id struct member
     * @param time_stamp time_stamp struct member
     * @param trail_label trail_label struct member
     * @param message_id message_id struct member
     * @param trail_id trail_id struct member
     * @param event_counter event_counter struct member
     * @param thread_id thread_id struct member
     * @param op_name op_name struct member
     * @param interaction_point interaction_point struct member
     * @param identityKey identityKey struct member
     * @param parametersKey parametersKey struct member
     * @param linkKey linkKey struct member
     */
    public TraceRecord(long key_id, long time_stamp, String trail_label, String message_id, String trail_id, int event_counter, String thread_id, String op_name, org.coach.tracing.api.InteractionPoint interaction_point, long identityKey, long parametersKey, long linkKey)
    {
        this.key_id = key_id;
        this.time_stamp = time_stamp;
        this.trail_label = trail_label;
        this.message_id = message_id;
        this.trail_id = trail_id;
        this.event_counter = event_counter;
        this.thread_id = thread_id;
        this.op_name = op_name;
        this.interaction_point = interaction_point;
        this.identityKey = identityKey;
        this.parametersKey = parametersKey;
        this.linkKey = linkKey;
    }

}
