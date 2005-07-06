/***************************************************************************/
/* COACH: Component Based Open Source Architecture for                     */
/*        Distributed Telecom Applications                                 */
/* See:   http://www.objectweb.org/                                        */
/*                                                                         */
/* Copyright (C) 2003 Lucent Technologies Nederland BV                     */
/*                    Bell Labs Advanced Technologies - EMEA               */
/*                                                                         */
/* Initial developer(s): Harold Batteram                                   */
/*                                                                         */
/* This library is free software; you can redistribute it and/or           */
/* modify it under the terms of the GNU Lesser General Public              */
/* License as published by the Free Software Foundation; either            */
/* version 2.1 of the License, or (at your option) any later version.      */
/*                                                                         */
/* This library is distributed in the hope that it will be useful,         */
/* but WITHOUT ANY WARRANTY; without even the implied warranty of          */
/* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU        */
/* Lesser General Public License for more details.                         */
/*                                                                         */
/* You should have received a copy of the GNU Lesser General Public        */
/* License along with this library; if not, write to the Free Software     */
/* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA */
/***************************************************************************/
package org.coach.idltree;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.InputSource;
import org.w3c.dom.*;
import javax.swing.tree.*;

import java.util.*;
import java.io.*;
import java.lang.reflect.*;

public class XmlNode {
//    static org.objectweb.ccm.IDL3.Repository repository;
//    static org.objectweb.ccm.ComponentRepository repositoryRef;
    static Hashtable repositoryCache = new Hashtable();
    static org.omg.CORBA.ORB orb = null;
/*
    static {
        try {
            if ( orb == null ){
                orb = org.objectweb.ccm.CORBA.TheORB.getORB();
            }
            // Obtain the Interface Repository.
            repositoryRef =
                org.objectweb.ccm.ComponentRepositoryHelper.narrow(
                    org.objectweb.ccm.CORBA.TheInterfaceRepository.getRepository());

            // Start an IDL3 Repository
            repository = new org.objectweb.ccm.IDL3.Repository(repositoryRef.as_IDL2_repository());

        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }*/

    public static void setOrb(org.omg.CORBA.ORB ob) {
        orb = ob;
    }

    static Node getNode(String xml) {
        Node node = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource  is = new InputSource(new StringReader(xml));
            is.setSystemId("file://" + System.getProperty("dsc.home") + "/etc/");
            node = builder.parse(is);
            node.normalize();
        } catch (SAXParseException spe) {
            throw new RuntimeException("Xml parsing error, line " + spe.getLineNumber() + ", uri " + spe.getSystemId() + "   " + spe.getMessage());
        } catch (Exception x) {
            x.printStackTrace();
        }
        return firstChildElement(node);
    }

    static String getId(Node n) {
        return getAttribute(n, "id");
    }

    static String getName(Node n) {
        return getAttribute(n, "name");
    }

    static int getLength(Node n) {
        String a = getAttribute(n, "length");
        try {
            return Integer.parseInt(a);
        } catch (Exception e) {
            throw new RuntimeException("unable to read length attribute");
        }
    }

    static String getValue(Node n) {
        return getAttribute(n, "value");
    }

    /**
     * Returns a list of operations for the IDL interface indicated by the repository id.
     *
     * @return An array with operation names.
     */
/*    static String[] getOperations(String id) {
        String[] operations = new String[0];
        // We use reflection here and rely on the presence of the HelperExt class.
        // Actualy the IR server should be used.
         if (repositoryRef != null) {
            try {
                org.omg.CORBA.Contained c = lookup(id);
                Vector v = new Vector();
                addOperations(v, id);
                operations = new String[v.size()];
                v.toArray(operations);
            } catch (Exception ex) {
                System.err.println(ex.toString());
            }
        }
        return operations;
    }
*/
/*    static void addOperations(Vector v, String id) {
        if (repositoryRef != null) {
            try {
                org.omg.CORBA.Contained c = lookup(id);
                org.omg.CORBA.InterfaceDef interfaceDef = org.omg.CORBA.InterfaceDefHelper.narrow(c);
                org.omg.CORBA.InterfaceDefPackage.FullInterfaceDescription desc = interfaceDef.describe_interface();
                for (int i = 0; i < desc.operations.length; i++) {
                    v.add(desc.operations[i].name);
                }
                for(int i = 0; i < desc.base_interfaces.length; i++) {
                    addOperations(v, desc.base_interfaces[i]);
                }
            } catch (Exception ex) {
                System.err.println(ex.toString());
            }
        }
    }
*/
/*    static void addOperations(Hashtable v, String id) {
        if (repositoryRef != null) {
            try {
                org.omg.CORBA.Contained c = lookup(id);
                org.omg.CORBA.InterfaceDef interfaceDef = org.omg.CORBA.InterfaceDefHelper.narrow(c);
                org.omg.CORBA.InterfaceDefPackage.FullInterfaceDescription desc = interfaceDef.describe_interface();
                for (int i = 0; i < desc.operations.length; i++) {
                    v.put(desc.operations[i].name, desc.operations[i]);
                }
                for(int i = 0; i < desc.base_interfaces.length; i++) {
                    addOperations(v, desc.base_interfaces[i]);
                }
            } catch (Exception ex) {
                System.err.println(ex.toString());
            }
        }
    }
*/
    /**
     * Returns a list of parameter names for the IDL interface indicated by the repository id.
     *
     * @return An array with parameter names.
     */
/*    static String[] getParameterNames(String id, String name) {
        String[] names = new String[0];
        if (repositoryRef != null) {
            try {
                Hashtable t = new Hashtable();
                addOperations(t, id);
                org.omg.CORBA.OperationDescription opDesc = (org.omg.CORBA.OperationDescription)t.get(name);
                if (opDesc == null) {
                    throw new RuntimeException("no operation " + name + " found in interface " + id);
                }
                names = new String[opDesc.parameters.length + 1];
                for (int j = 0; j < opDesc.parameters.length; j++) {
                    names[j] = opDesc.parameters[j].name;
                }
                if (opDesc.mode == org.omg.CORBA.OperationMode.OP_ONEWAY) {
                    names[names.length - 1] = "oneway";
                } else {
                    names[names.length - 1] = "return";
                }
            } catch (Exception ex) {
                System.err.println(ex.toString());
            }
        }
        return names;
    }
*/
    /**
     * Returns a list of parameter directions for the IDL interface indicated by the repository id.
     *
     * Directions are in, out, inout, return, oneway
     * @return An array with parameter directions.
     */
/*    static String[] getParameterDirections(String id, String name) {
        String[] directions = new String[0];
        if (repositoryRef != null) {
            try {
                Hashtable t = new Hashtable();
                addOperations(t, id);
                org.omg.CORBA.OperationDescription opDesc = (org.omg.CORBA.OperationDescription)t.get(name);
                if (opDesc == null) {
                    throw new RuntimeException("no operation " + name + " found in interface " + id);
                }
                directions = new String[opDesc.parameters.length + 1];
                for (int j = 0; j < opDesc.parameters.length; j++) {
                    directions[j] = parameterMode(opDesc.parameters[j].mode);
                }
                if (opDesc.mode == org.omg.CORBA.OperationMode.OP_ONEWAY) {
                    directions[directions.length - 1] = "oneway";
                } else {
                    directions[directions.length - 1] = "return";
                }
            } catch (Exception ex) {
                System.err.println(ex.toString());
            }
        }

        return directions;
    }
*/
    private static String parameterMode(org.omg.CORBA.ParameterMode m) {
        if (m == org.omg.CORBA.ParameterMode.PARAM_IN) {
            return "in";
        }
        if (m == org.omg.CORBA.ParameterMode.PARAM_OUT) {
            return "out";
        }
        if (m == org.omg.CORBA.ParameterMode.PARAM_INOUT) {
            return "inout";
        }
        return "";
    }

    /**
     * Returns a list of parameter types for the IDL interface indicated by the repository id.
     *
     * The type names can be primitive types such as string, short, ulong or the repository id of a user defined type.
     *
     * @return An array with parameter type names.
     */
/*    static String[] getParameterTypes(String id, String name) {
        String[] types = new String[0];
        if (repositoryRef != null) {
            try {
                Hashtable t = new Hashtable();
                addOperations(t, id);
                org.omg.CORBA.OperationDescription opDesc = (org.omg.CORBA.OperationDescription)t.get(name);
                if (opDesc == null) {
                    throw new RuntimeException("no operation " + name + " found in interface " + id);
                }
                types = new String[opDesc.parameters.length + 1];
                for (int j = 0; j < opDesc.parameters.length; j++) {
                    types[j] = parameterType(opDesc.parameters[j].type);
                }
                if (opDesc.mode == org.omg.CORBA.OperationMode.OP_ONEWAY) {
                    types[types.length - 1] = "";
                } else {
                    types[types.length - 1] = parameterType(opDesc.result);
                }
            } catch (Exception ex) {
                System.err.println(ex.toString());
            }
        }
        return types;
    }
*/
    private static String parameterType(org.omg.CORBA.TypeCode tc) {
        try {
            switch (tc.kind().value()) {
                case org.omg.CORBA.TCKind._tk_any: {
                    return "any";
                }
                case org.omg.CORBA.TCKind._tk_octet: {
                    return "octet";
                }
                case org.omg.CORBA.TCKind._tk_short: {
                    return "short";
                }
                case org.omg.CORBA.TCKind._tk_ushort: {
                    return "ushort";
                }
                case org.omg.CORBA.TCKind._tk_long: {
                    return "long";
                }
                case org.omg.CORBA.TCKind._tk_ulong: {
                    return "ulong";
                }
                case org.omg.CORBA.TCKind._tk_longlong: {
                    return "longlong";
                }
                case org.omg.CORBA.TCKind._tk_ulonglong: {
                    return "ulonglong";
                }
                case org.omg.CORBA.TCKind._tk_float: {
                    return "float";
                }
                case org.omg.CORBA.TCKind._tk_double: {
                    return "double";
                }
                case org.omg.CORBA.TCKind._tk_longdouble: {
                    return "longdouble";
                }
                case org.omg.CORBA.TCKind._tk_boolean: {
                    return "boolean";
                }
                case org.omg.CORBA.TCKind._tk_char: {
                    return "char";
                }
                case org.omg.CORBA.TCKind._tk_wchar: {
                    return "wchar";
                }
                case org.omg.CORBA.TCKind._tk_string: {
                    return "string";
                }
                case org.omg.CORBA.TCKind._tk_wstring: {
                    return "wstring";
                }
                case org.omg.CORBA.TCKind._tk_void: {
                    return "void";
                }
                case org.omg.CORBA.TCKind._tk_null: {
                    return "null";
                }
                default: {
                    return tc.id();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static String[] getParameterExceptions(String id, String name) {
        String[] exceptions = new String[0];
 /*       if (repositoryRef != null) {
            try {
                Hashtable t = new Hashtable();
                addOperations(t, id);
                org.omg.CORBA.OperationDescription opDesc = (org.omg.CORBA.OperationDescription)t.get(name);
                if (opDesc == null) {
                    throw new RuntimeException("no operation " + name + " found in interface " + id);
                }
                exceptions = new String[opDesc.exceptions.length];
                for (int j = 0; j < opDesc.exceptions.length; j++) {
                    exceptions[j] = opDesc.exceptions[j].id;
                }
            } catch (Exception ex) {
                System.err.println(ex.toString());
            }
        }*/

        return exceptions;
    }

    /**
     * Convert IDL:omg.org/CosNotification/StructuredEvent:1.0
     * into org.omg.CosNotification.StructuredEvent
     */
    static String idToType(String id) {
        String javaName = "";
        // strip off IDL: en version :1.0 sections
        if (id.indexOf(":") > 0) {
            id = id.substring(id.indexOf(":") + 1, id.lastIndexOf(":"));
        }
        if (id.indexOf(".") > 0) {
            // there is a pragma prefix
            String pragma = id.substring(0, id.indexOf("/"));
            String[] el = splitName(pragma, ".");
            String prefix = el[el.length - 1];
            for (int i = el.length - 2; i >= 0; i--) {
                prefix += "." + el[i];
            }
            javaName = prefix + id.substring(id.indexOf("/")).replace('/', '.');
        } else {
            javaName = id.replace('/', '.');
        }
        if (javaName.equals("org.omg.CORBA.Object")) {
            javaName = "object";
        }
        return javaName;
    }

    static String[] splitName(String name, String separator) {
        if (name == null) {
            return new String[0];
        }
        StringTokenizer st = new StringTokenizer(name, separator);
        Vector v = new Vector();
        if (name.startsWith(separator)) {
            v.addElement("");
        }
        while (st.hasMoreTokens()) {
            v.addElement(st.nextToken());
        }
        String[] s = new String[v.size()];
        v.copyInto(s);
        return s;
    }

    static String getText(Node domNode) {
        String text = "";
        NodeList list = domNode.getChildNodes();
        int items = list.getLength();
        for (int i = 0; i < items; i++) {
            if (list.item(i).getNodeType() == Node.TEXT_NODE) {
                text = list.item(i).getNodeValue().trim();
                if (!text.equals("")) {
                    return list.item(i).getNodeValue().trim();
                }
            }
        }
        return text;
    }

    static String getAttribute(Node domNode, String name) {
        NamedNodeMap map = domNode.getAttributes();
        Node attr = map.getNamedItem(name);
        if (attr != null) {
            return attr.getNodeValue();
        } else {
            return "";
        }
    }

    static Node nextElement(Node n) {
        if (n != null) {
            n = n.getNextSibling();
            while (n != null && n.getNodeType() != Node.ELEMENT_NODE) {
                n = n.getNextSibling();
            }
        }
        return n;
    }

    static Node firstElement(Node n) {
        while (n != null && n.getNodeType() != Node.ELEMENT_NODE) {
            System.out.println("" + n.getNodeType() + " " + n.getNodeName());
            n = n.getNextSibling();
        }
        return n;
    }

    static Node firstChildElement(Node n) {
        NodeList list = n.getChildNodes();
        int items = list.getLength();
        for (int i = 0; i < items; i++) {
            if (list.item(i).getNodeType() == Node.ELEMENT_NODE) {
                return list.item(i);
            }
        }
        return null;
    }

    static Node[] childElements(Node n) {
        Vector v = new Vector();
        NodeList list = n.getChildNodes();
        int items = list.getLength();
        for (int i = 0; i < items; i++) {
            if (list.item(i).getNodeType() == Node.ELEMENT_NODE) {
                v.add(list.item(i));
            }
        }
        Node[] nn = new Node[v.size()];
        v.toArray(nn);
        return nn;
    }

    static org.omg.CORBA.Contained lookup(String id) {
        org.omg.CORBA.Contained c = (org.omg.CORBA.Contained)repositoryCache.get(id);
 /*       if (c == null) {
//            System.err.println("***** lookup_id(" + id + ") ******");
            c = repositoryRef.lookup_id(id);
            if (c == null) {
                javax.swing.JOptionPane.showMessageDialog(null, id + " not found in InterfaceRepository.\nUse ir3_feed to populate the InterfaceRepository with the proper idl files");
                return null;
            }
            repositoryCache.put(id, c);
        }*/
        return c;
    }

    static public org.omg.CORBA.TypeCode type(String id) {
        org.omg.CORBA.TypeCode tc = null;
        String type = idToType(id);
        Integer index = (Integer)table.get(type.toLowerCase());

        if (index == null) {
            try {
                org.omg.CORBA.Contained c = lookup(id);
                if (c == null) {
                    throw new RuntimeException(id + " not found in InterfaceRepository.\nUse ir3_feed to populate the InterfaceRepository with the proper idl files");
                }
                try {
                    org.omg.CORBA.IDLType idlType = org.omg.CORBA.IDLTypeHelper.narrow(c);
                    tc = idlType.type();
                    return tc;
                } catch (Exception e1) {
                }
                org.omg.CORBA.ExceptionDef idlType = org.omg.CORBA.ExceptionDefHelper.narrow(c);
                tc = idlType.type();
                return tc;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            switch (index.intValue()) {
                case 0: return orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_boolean);
                case 1: return orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_octet);
                case 2: return orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_short);
                case 3: return orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_ushort);
                case 4: return orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_long);
                case 5: return orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_ulong);
                case 6: return orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_longlong);
                case 7: return orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_ulonglong);
                case 8: return orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_float);
                case 9: return orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                case 10: return orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_longdouble);
                case 11: return orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_char);
                case 12: return orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_wchar);
                case 13: return orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_string);
                case 14: return orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_wstring);
                case 15: return orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_objref);
                case 17: return orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_fixed);
                case 18: return orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_any);
                case 24: return orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_void);
            }
        }
//System.out.println("XmlNode.type(" + id + ")" + " " + tc);
        return tc;
    }

    /**
     * Factory method to create a new IdlNode instance from a repository id value.
     *
     * @param id The repository value from which to create a new instance.
     *
     * @return A new IdlNode instance for the given repository id.
     */
    static IdlNode getIdlNode(String id) {
        if (id == null) {
            throw new RuntimeException("invalid id");
        }

        String type = idToType(id);
        Integer index = (Integer)table.get(type.toLowerCase());

        if (index == null) {
            // The type is not a CORBA primitive
            try {
                return IdlNode.create(XmlNode.type(id));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        switch (index.intValue()) {
            case 0: return new IdlBoolean();
            case 1: return new IdlOctet();
            case 2: return new IdlShort();
            case 3: return new IdlUshort();
            case 4: return new IdlLong();
            case 5: return new IdlUlong();
            case 6: return new IdlLonglong();
            case 7: return new IdlUlonglong();
            case 8: return new IdlFloat();
            case 9: return new IdlDouble();
            case 10: return new IdlLongdouble();
            case 11: return new IdlChar();
            case 12: return new IdlWchar();
            case 13: return new IdlString();
            case 14: return new IdlWstring();
            case 15: return new IdlObject();
            case 17: return new IdlFixed();
            case 18: return new IdlAny();
            default: return null;
        }
    }

    static public IdlNode getIdlNodeXml(String xml) {
        return getIdlNodeXml(null, xml);
    }

    static IdlNode getIdlNodeXml(org.omg.CORBA.TypeCode tc, String xml) {
        return getIdlNode(tc, getNode(xml));
    }

    static IdlNode getIdlNode(Node node) {
        return getIdlNode(null, node);
    }

    static IdlNode getIdlNode(org.omg.CORBA.TypeCode tc, Node node) {
        if (node == null) {
            throw new RuntimeException("empty node");
        }

        String kind = node.getNodeName().toLowerCase().trim();
        if (kind.equals("idlxml")) {
            node = firstChildElement(node);
            kind = node.getNodeName().toLowerCase().trim();
        }
        Integer index = (Integer)table.get(kind);

        if (index == null) {
            throw new RuntimeException("invalid node: " + kind);
        }

        switch (index.intValue()) {
            case 0: return new IdlBoolean(node);
            case 1: return new IdlOctet(node);
            case 2: return new IdlShort(node);
            case 3: return new IdlUshort(node);
            case 4: return new IdlLong(node);
            case 5: return new IdlUlong(node);
            case 6: return new IdlLonglong(node);
            case 7: return new IdlUlonglong(node);
            case 8: return new IdlFloat(node);
            case 9: return new IdlDouble(node);
            case 10: return new IdlLongdouble(node);
            case 11: return new IdlChar(node);
            case 12: return new IdlWchar(node);
            case 13: return new IdlString(node);
            case 14: return new IdlWstring(node);
            case 15: return new IdlObject(node);
            case 16: return new IdlInterface(node);
            case 17: return new IdlFixed(node);
            case 18: return new IdlAny(node);
            case 19: return new IdlEnum(node);
            case 20: return new IdlStruct(node);
            case 21: return new IdlUnion(node);
            case 22: return new IdlSequence(tc, node);
            case 23: return new IdlArray(tc, node);
            case 25: return new IdlOperation(node);
            case 26: return new IdlReply(node);
            case 27: return new IdlException(node);
            default: return null;
        }
    }


    static private final java.util.Hashtable table = new java.util.Hashtable();
    static {
        table.put("boolean", new Integer(0));
        table.put("octet", new Integer(1));
        table.put("short", new Integer(2));
        table.put("ushort", new Integer(3));
        table.put("unsigned short", new Integer(3));
        table.put("long", new Integer(4));
        table.put("ulong", new Integer(5));
        table.put("unsigned long", new Integer(5));
        table.put("longlong", new Integer(6));
        table.put("long long", new Integer(6));
        table.put("ulonglong", new Integer(7));
        table.put("unsigned long long", new Integer(7));
        table.put("float", new Integer(8));
        table.put("double", new Integer(9));
        table.put("longdouble", new Integer(10));
        table.put("long double", new Integer(10));
        table.put("char", new Integer(11));
        table.put("wchar", new Integer(12));
        table.put("string", new Integer(13));
        table.put("wstring", new Integer(14));
        table.put("object", new Integer(15));
        table.put("interface", new Integer(16));
        table.put("fixed", new Integer(17));
        table.put("any", new Integer(18));
        table.put("enum", new Integer(19));
        table.put("struct", new Integer(20));
        table.put("union", new Integer(21));
        table.put("sequence", new Integer(22));
        table.put("array", new Integer(23));
        table.put("void", new Integer(24));
        table.put("operation", new Integer(25));
        table.put("reply", new Integer(26));
        table.put("exception", new Integer(27));
        table.put("idlxml", new Integer(28));
    }

    static String replaceAll(String source, String what, String into) {
        int index = source.indexOf(what);
        if (index == -1) return source;
        String s1 = source.substring(0, index);
        String s2 = source.substring(index + what.length(), source.length());
        return  s1 + into + replaceAll(s2, what, into);
    }

    public static void write(IdlNode n, IdlWriter w) {
        if (n instanceof IdlObject) {
            w.write_Object(n.getValue());
            return;
        }
        if (n instanceof IdlInterface) {
            w.write_interface(((IdlSequence)n).getId(), n.getValue());
            return;
        }
        if (n instanceof IdlComponent) {
            w.write_interface(((IdlComponent)n).getId(), n.getValue());
            return;
        }
        if (n instanceof IdlValue) {
            IdlValue.write((IdlValue)n, w);
            return;
        }
        if (n instanceof IdlSequence) {
            IdlSequence.write((IdlSequence)n, w);
            return;
        }
        if (n instanceof IdlStruct) {
            IdlStruct.write((IdlStruct)n, w);
            return;
        }
        if (n instanceof IdlException) {
            IdlException.write((IdlException)n, w);
            return;
        }
        if (n instanceof IdlUnion) {
            IdlUnion.write((IdlUnion)n, w);
            return;
        }
        if (n instanceof IdlArray) {
            IdlArray.write((IdlArray)n, w);
            return;
        }
        if (n instanceof IdlOperation) {
            IdlOperation.write((IdlOperation)n, w);
            return;
        }
        if (n instanceof IdlParameter) {
            IdlParameter.write((IdlParameter)n, w);
            return;
        }
        if (n instanceof IdlReply) {
            IdlReply.write((IdlReply)n, w);
            return;
        }
        if (n instanceof IdlAny) {
            IdlAny.write((IdlAny)n, w);
            return;
        }
        if (n instanceof IdlEnum) {
            w.write_enum(n.getId(), n.getValue());
            return;
        }
        if (n instanceof IdlBoolean) {
            w.write_boolean(n.getValue());
            return;
        }
        if (n instanceof IdlOctet) {
            w.write_octet(n.getValue());
            return;
        }
        if (n instanceof IdlShort) {
            w.write_short(n.getValue());
            return;
        }
        if (n instanceof IdlUshort) {
            w.write_ushort(n.getValue());
            return;
        }
        if (n instanceof IdlLong) {
            w.write_long(n.getValue());
            return;
        }
        if (n instanceof IdlUlong) {
            w.write_ulong(n.getValue());
            return;
        }
        if (n instanceof IdlLonglong) {
            w.write_longlong(n.getValue());
            return;
        }
        if (n instanceof IdlUlonglong) {
            w.write_ulonglong(n.getValue());
            return;
        }
        if (n instanceof IdlChar) {
            w.write_char(n.getValue());
            return;
        }
        if (n instanceof IdlWchar) {
            w.write_wchar(n.getValue());
            return;
        }
        if (n instanceof IdlString) {
            w.write_string(n.getValue());
            return;
        }
        if (n instanceof IdlWstring) {
            w.write_wstring(n.getValue());
            return;
        }
        if (n instanceof IdlFloat) {
            w.write_float(n.getValue());
            return;
        }
        if (n instanceof IdlDouble) {
            w.write_double(n.getValue());
            return;
        }
        if (n instanceof IdlLongdouble) {
            w.write_longdouble(n.getValue());
            return;
        }
        if (n instanceof IdlFixed) {
            w.write_fixed(n.getValue());
            return;
        }
    }
}
