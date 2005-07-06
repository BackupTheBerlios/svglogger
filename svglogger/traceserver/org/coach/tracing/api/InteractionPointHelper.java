package org.coach.tracing.api;

/** 
 * Helper class for : InteractionPoint
 *  
 * @author OpenORB Compiler
 */ 
public class InteractionPointHelper
{
    /**
     * Insert InteractionPoint into an any
     * @param a an any
     * @param t InteractionPoint value
     */
    public static void insert(org.omg.CORBA.Any a, org.coach.tracing.api.InteractionPoint t)
    {
        a.type(type());
        write(a.create_output_stream(),t);
    }

    /**
     * Extract InteractionPoint from an any
     * @param a an any
     * @return the extracted InteractionPoint value
     */
    public static org.coach.tracing.api.InteractionPoint extract(org.omg.CORBA.Any a)
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
     * Return the InteractionPoint TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type()
    {
        if (_tc == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            String []_members = new String[8];
            _members[0] = "STUB_OUT";
            _members[1] = "POA_IN";
            _members[2] = "POA_OUT";
            _members[3] = "POA_OUT_EXCEPTION";
            _members[4] = "STUB_IN";
            _members[5] = "STUB_IN_EXCEPTION";
            _members[6] = "ONEWAY_STUB_OUT";
            _members[7] = "ONEWAY_POA_IN";
            _tc = orb.create_enum_tc(id(),"InteractionPoint",_members);
        }
        return _tc;
    }

    /**
     * Return the InteractionPoint IDL ID
     * @return an ID
     */
    public static String id()
    {
        return _id;
    }

    private final static String _id = "IDL:org/coach/tracing/api/InteractionPoint:1.0";

    /**
     * Read InteractionPoint from a marshalled stream
     * @param istream the input stream
     * @return the readed InteractionPoint value
     */
    public static org.coach.tracing.api.InteractionPoint read(org.omg.CORBA.portable.InputStream istream)
    {
        return InteractionPoint.from_int(istream.read_ulong());
    }

    /**
     * Write InteractionPoint into a marshalled stream
     * @param ostream the output stream
     * @param value InteractionPoint value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, org.coach.tracing.api.InteractionPoint value)
    {
        ostream.write_ulong(value.value());
    }

}
