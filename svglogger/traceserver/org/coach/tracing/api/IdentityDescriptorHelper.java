package org.coach.tracing.api;

/** 
 * Helper class for : IdentityDescriptor
 *  
 * @author OpenORB Compiler
 */ 
public class IdentityDescriptorHelper
{
    private static final boolean HAS_OPENORB;
    static {
        boolean hasOpenORB = false;
        try {
            Thread.currentThread().getContextClassLoader().loadClass("org.openorb.CORBA.Any");
            hasOpenORB = true;
        }
        catch(ClassNotFoundException ex) {
        }
        HAS_OPENORB = hasOpenORB;
    }
    /**
     * Insert IdentityDescriptor into an any
     * @param a an any
     * @param t IdentityDescriptor value
     */
    public static void insert(org.omg.CORBA.Any a, org.coach.tracing.api.IdentityDescriptor t)
    {
        a.insert_Streamable(new org.coach.tracing.api.IdentityDescriptorHolder(t));
    }

    /**
     * Extract IdentityDescriptor from an any
     * @param a an any
     * @return the extracted IdentityDescriptor value
     */
    public static org.coach.tracing.api.IdentityDescriptor extract(org.omg.CORBA.Any a)
    {
        if (!a.type().equal(type()))
            throw new org.omg.CORBA.MARSHAL();
        if (HAS_OPENORB && a instanceof org.openorb.CORBA.Any) {
            // streamable extraction. The jdk stubs incorrectly define the Any stub
            org.openorb.CORBA.Any any = (org.openorb.CORBA.Any)a;
            try {
                org.omg.CORBA.portable.Streamable s = any.extract_Streamable();
                if(s instanceof org.coach.tracing.api.IdentityDescriptorHolder)
                    return ((org.coach.tracing.api.IdentityDescriptorHolder)s).value;
            } catch (org.omg.CORBA.BAD_INV_ORDER ex) {
            }
            org.coach.tracing.api.IdentityDescriptorHolder h = new org.coach.tracing.api.IdentityDescriptorHolder(read(a.create_input_stream()));
            a.insert_Streamable(h);
            return h.value;
        }
        return read(a.create_input_stream());
    }

    //
    // Internal TypeCode value
    //
    private static org.omg.CORBA.TypeCode _tc = null;
    private static boolean _working = false;

    /**
     * Return the IdentityDescriptor TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type()
    {
        if (_tc == null) {
            synchronized(org.omg.CORBA.TypeCode.class) {
                if (_tc != null)
                    return _tc;
                if (_working)
                    return org.omg.CORBA.ORB.init().create_recursive_tc(id());
                _working = true;
                org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
                org.omg.CORBA.StructMember []_members = new org.omg.CORBA.StructMember[9];

                _members[0] = new org.omg.CORBA.StructMember();
                _members[0].name = "node_name";
                _members[0].type = orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_string);
                _members[1] = new org.omg.CORBA.StructMember();
                _members[1].name = "node_ip";
                _members[1].type = orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_string);
                _members[2] = new org.omg.CORBA.StructMember();
                _members[2].name = "process_id";
                _members[2].type = orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_string);
                _members[3] = new org.omg.CORBA.StructMember();
                _members[3].name = "cnt_name";
                _members[3].type = orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_string);
                _members[4] = new org.omg.CORBA.StructMember();
                _members[4].name = "cnt_type";
                _members[4].type = orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_string);
                _members[5] = new org.omg.CORBA.StructMember();
                _members[5].name = "cmp_name";
                _members[5].type = orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_string);
                _members[6] = new org.omg.CORBA.StructMember();
                _members[6].name = "cmp_type";
                _members[6].type = orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_string);
                _members[7] = new org.omg.CORBA.StructMember();
                _members[7].name = "object_instance_id";
                _members[7].type = orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_string);
                _members[8] = new org.omg.CORBA.StructMember();
                _members[8].name = "object_repository_id";
                _members[8].type = orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_string);
                _tc = orb.create_struct_tc(id(),"IdentityDescriptor",_members);
                _working = false;
            }
        }
        return _tc;
    }

    /**
     * Return the IdentityDescriptor IDL ID
     * @return an ID
     */
    public static String id()
    {
        return _id;
    }

    private final static String _id = "IDL:org/coach/tracing/api/IdentityDescriptor:1.0";

    /**
     * Read IdentityDescriptor from a marshalled stream
     * @param istream the input stream
     * @return the readed IdentityDescriptor value
     */
    public static org.coach.tracing.api.IdentityDescriptor read(org.omg.CORBA.portable.InputStream istream)
    {
        org.coach.tracing.api.IdentityDescriptor new_one = new org.coach.tracing.api.IdentityDescriptor();

        new_one.node_name = istream.read_string();
        new_one.node_ip = istream.read_string();
        new_one.process_id = istream.read_string();
        new_one.cnt_name = istream.read_string();
        new_one.cnt_type = istream.read_string();
        new_one.cmp_name = istream.read_string();
        new_one.cmp_type = istream.read_string();
        new_one.object_instance_id = istream.read_string();
        new_one.object_repository_id = istream.read_string();

        return new_one;
    }

    /**
     * Write IdentityDescriptor into a marshalled stream
     * @param ostream the output stream
     * @param value IdentityDescriptor value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, org.coach.tracing.api.IdentityDescriptor value)
    {
        ostream.write_string(value.node_name);
        ostream.write_string(value.node_ip);
        ostream.write_string(value.process_id);
        ostream.write_string(value.cnt_name);
        ostream.write_string(value.cnt_type);
        ostream.write_string(value.cmp_name);
        ostream.write_string(value.cmp_type);
        ostream.write_string(value.object_instance_id);
        ostream.write_string(value.object_repository_id);
    }

}
