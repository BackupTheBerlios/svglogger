package org.coach.tracing.api;

/** 
 * Helper class for : IdentityKind
 *  
 * @author OpenORB Compiler
 */ 
public class IdentityKindHelper
{
    /**
     * Insert IdentityKind into an any
     * @param a an any
     * @param t IdentityKind value
     */
    public static void insert(org.omg.CORBA.Any a, org.coach.tracing.api.IdentityKind t)
    {
        a.type(type());
        write(a.create_output_stream(),t);
    }

    /**
     * Extract IdentityKind from an any
     * @param a an any
     * @return the extracted IdentityKind value
     */
    public static org.coach.tracing.api.IdentityKind extract(org.omg.CORBA.Any a)
    {
        if (!a.type().equal(type()))
            throw new org.omg.CORBA.MARSHAL();
        return read(a.create_input_stream());
    }

    //
    // Internal TypeCode value
    //
    private static org.omg.CORBA.TypeCode _tc = null;

    /**
     * Return the IdentityKind TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type()
    {
        if (_tc == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            String []_members = new String[6];
            _members[0] = "DOMAIN";
            _members[1] = "PLATFORM";
            _members[2] = "SESSION";
            _members[3] = "CONTAINER";
            _members[4] = "COMPONENT_TYPE";
            _members[5] = "ComponentId";
            _tc = orb.create_enum_tc(id(),"IdentityKind",_members);
        }
        return _tc;
    }

    /**
     * Return the IdentityKind IDL ID
     * @return an ID
     */
    public static String id()
    {
        return _id;
    }

    private final static String _id = "IDL:org/coach/tracing/api/IdentityKind:1.0";

    /**
     * Read IdentityKind from a marshalled stream
     * @param istream the input stream
     * @return the readed IdentityKind value
     */
    public static org.coach.tracing.api.IdentityKind read(org.omg.CORBA.portable.InputStream istream)
    {
        return IdentityKind.from_int(istream.read_ulong());
    }

    /**
     * Write IdentityKind into a marshalled stream
     * @param ostream the output stream
     * @param value IdentityKind value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, org.coach.tracing.api.IdentityKind value)
    {
        ostream.write_ulong(value.value());
    }

}
