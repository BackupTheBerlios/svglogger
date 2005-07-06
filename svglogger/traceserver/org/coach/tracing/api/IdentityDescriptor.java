package org.coach.tracing.api;

/**
 * Struct definition : IdentityDescriptor
 * 
 * @author OpenORB Compiler
*/
public final class IdentityDescriptor implements org.omg.CORBA.portable.IDLEntity
{
    /**
     * Struct member node_name
     */
    public String node_name;

    /**
     * Struct member node_ip
     */
    public String node_ip;

    /**
     * Struct member process_id
     */
    public String process_id;

    /**
     * Struct member cnt_name
     */
    public String cnt_name;

    /**
     * Struct member cnt_type
     */
    public String cnt_type;

    /**
     * Struct member cmp_name
     */
    public String cmp_name;

    /**
     * Struct member cmp_type
     */
    public String cmp_type;

    /**
     * Struct member object_instance_id
     */
    public String object_instance_id;

    /**
     * Struct member object_repository_id
     */
    public String object_repository_id;

    /**
     * Default constructor
     */
    public IdentityDescriptor()
    { }

    /**
     * Constructor with fields initialization
     * @param node_name node_name struct member
     * @param node_ip node_ip struct member
     * @param process_id process_id struct member
     * @param cnt_name cnt_name struct member
     * @param cnt_type cnt_type struct member
     * @param cmp_name cmp_name struct member
     * @param cmp_type cmp_type struct member
     * @param object_instance_id object_instance_id struct member
     * @param object_repository_id object_repository_id struct member
     */
    public IdentityDescriptor(String node_name, String node_ip, String process_id, String cnt_name, String cnt_type, String cmp_name, String cmp_type, String object_instance_id, String object_repository_id)
    {
        this.node_name = node_name;
        this.node_ip = node_ip;
        this.process_id = process_id;
        this.cnt_name = cnt_name;
        this.cnt_type = cnt_type;
        this.cmp_name = cmp_name;
        this.cmp_type = cmp_type;
        this.object_instance_id = object_instance_id;
        this.object_repository_id = object_repository_id;
    }

}
