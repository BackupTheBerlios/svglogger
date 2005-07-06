/*
 *
 *  AUTHOR:      Justyna Zander, George Din
 *  DATE:        2004-2005
 * ----------------------------------------------------------------------------
 */
package com.testingtech.ttcn.logging;

import org.coach.tracing.api.IdentityDescriptor;
import org.coach.tracing.api.InteractionPoint;
import org.coach.tracing.api.Parameter;
import org.coach.tracing.api.TraceEvent;
import org.coach.tracing.api.i_Trace;
import org.coach.tracing.api.i_TraceHelper;

import org.etsi.ttcn.tci.TciBehaviourId;
import org.etsi.ttcn.tci.TciModuleId;
import org.etsi.ttcn.tci.TciParameterList;
import org.etsi.ttcn.tci.TciSignatureId;
import org.etsi.ttcn.tci.TciTestCaseId;
import org.etsi.ttcn.tci.Type;
import org.etsi.ttcn.tci.Value;
import org.etsi.ttcn.tci.VerdictValue;
import org.etsi.ttcn.tri.TriAddress;
import org.etsi.ttcn.tri.TriComponentId;
import org.etsi.ttcn.tri.TriException;
import org.etsi.ttcn.tri.TriFunctionId;
import org.etsi.ttcn.tri.TriMessage;
import org.etsi.ttcn.tri.TriParameter;
import org.etsi.ttcn.tri.TriParameterList;
import org.etsi.ttcn.tri.TriPortId;
import org.etsi.ttcn.tri.TriPortIdList;
import org.etsi.ttcn.tri.TriSignatureId;
import org.etsi.ttcn.tri.TriTimerDuration;
import org.etsi.ttcn.tri.TriTimerId;

import org.omg.CORBA.Any;

import java.net.InetAddress;

import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;

import com.testingtech.ttcn.extension.Logging;
import com.testingtech.util.logging.MessageHandler;

/**
 * This class implements the TciLogging interface.
 * 
 * @author gedi
 * @version $Revision: 1.1 $ $Date: 2005/07/06 15:03:40 $
 */
public class EventLoggingImpl implements TXILoggingInterface, Logging {
	private String session_id;
	
	private String container_id = "container1";

	private i_Trace connection;

	private org.omg.CORBA.ORB orb;

	private int session_counter = 0;
	// process specific event counter
	private int event_counter = 0;

	private String appl_id;

	private HashMap components;

	private IdentityDescriptor control;

	private IdentityDescriptor mtc;

	private IdentityDescriptor system;

	private boolean isFunctional = true;
	
	
	String[] args = { "corbaloc:iiop:127.0.0.1:3000/NameService" };

	/**
	 * Constructor for the Logging object.
	 * 
	 * @param container
	 *            an implementation of the ContainerTM interface provided by the
	 *            container
	 */
	public EventLoggingImpl() {
		components = new HashMap();
		//		init(args);
	}

	public boolean init() {
		org.omg.CORBA.Object obj = null;

		try {
			Properties properties = new Properties();

			properties.setProperty("org.omg.CORBA.ORBClass",
					"org.openorb.CORBA.ORB");
			properties.setProperty("org.omg.CORBA.ORBSingletonClass",
					"org.openorb.CORBA.ORBSingleton");

			if (args[0].startsWith("corbaloc")) {
				System.out.println("Init NamingServer");
				properties.setProperty("ORBInitRef.NameService", args[0]);

				Vector vector = new Vector();

				vector.addElement("-ORBInitRef");
				vector.addElement("NameService=" + args[0]);

				String[] s = new String[vector.size()];

				vector.copyInto(s);

				orb = org.omg.CORBA.ORB.init(s, properties);

				obj = orb.resolve_initial_references("NameService");

				org.omg.CosNaming.NamingContext nc = org.omg.CosNaming.NamingContextHelper
						.narrow(obj);

				org.omg.CosNaming.NameComponent[] ncomp = new org.omg.CosNaming.NameComponent[1];

				ncomp[0] = new org.omg.CosNaming.NameComponent("TracingServer",
						"");

				obj = nc.resolve(ncomp);

			} else {
				orb = org.omg.CORBA.ORB.init(args, properties);

				// read ior from file
				try {
					java.io.FileInputStream file = new java.io.FileInputStream(
							"ior");
					java.io.BufferedReader myInput = new java.io.BufferedReader(
							new java.io.InputStreamReader(file));

					String stringTarget = myInput.readLine();

					obj = orb.string_to_object(stringTarget);
				} catch (java.io.IOException ex) {
					System.out.println("File error");
					//					System.exit(0);
					return false;
				}
			}

			connection = i_TraceHelper.narrow(obj);

			java.io.File tmp = java.io.File.createTempFile("tmp", "");

			tmp.deleteOnExit();

			appl_id = tmp.getName().substring(3);

			return true;
		} catch (Exception _ex) {
			//			_ex.printStackTrace();
			if (_ex.getCause() != null)
				System.out
						.println("No naming service found for SVG logging plugin: "
								+ _ex.getCause().getMessage());
			else
				System.out
						.println("No naming service found for SVG logging plugin: "
								+ _ex.getMessage());
			return false;
		}
	}

	public boolean isFunctional() {
		return isFunctional;
	}

	private void log(String log) {
	}

	/**
	 * Log the start of a Testcase.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param testCaseId
	 *            the Id of the testcase to be started.
	 * @param parameterList
	 *            the list of parameters used to start the testcase.
	 * @param message
	 *            an additional message to this event.
	 */
	public void logStartTestCase(long time, Object source, String testCaseId,
			TciParameterList parameterList, String message) {
		System.out.println("******** logstartTC");
	}

	/**
	 * Log the stop of a Testcase.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param message
	 *            an additional message to this event.
	 */
	public void logStopTestCase(long time, Object source, String message) {
	}

	/**
	 * Log the start of the control part.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param module
	 *            the module whose control part is started.
	 * @param message
	 *            an additional message to this event.
	 */
	public void logStartControl(long time, Object source, TciModuleId module,
			String message) {
		String session_id = new Integer(session_counter++).toString();
		System.out.println("******** logstartControl");
	}

	/**
	 * Log the stop of the control part.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param message
	 *            an additional message to this event.
	 */
	public void logStopControl(long time, Object source, String message) {
		System.out.println("logStopControl");

		long tim = time;
		IdentityDescriptor cmp = control;
		Parameter[] parameter = new Parameter[] { new Parameter("in", "string",
				"message") };
		Any[] value = new Any[1];
		value[0] = orb.create_any();
		value[0].insert_string(message);
		invocation3("StopControl", cmp, cmp, parameter, value, tim);
	}

	/**
	 * Log the event generated when a testcase was started.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param testCaseId
	 *            the Id of the started testcase.
	 * @param parameterList
	 *            the list of parameters used to start the testcase.
	 * @param timer
	 * @param systemPorts
	 *            the system ports for this testcase
	 * @param message
	 *            an additional message to this event.
	 */
	public void logTestCaseStarted(long time, Object source,
			TciTestCaseId testCaseId, TciParameterList parameterList,
			Float timer, TriPortIdList systemPorts, String message) {
		System.out.println("******** logTCaseStarted");
		System.out.println("logTestCaseStarted");
		long tim = time;
		IdentityDescriptor cmp = mtc;

		Parameter[] parameter = new Parameter[] { new Parameter("in", "string",
				"TESTCASE") };

		Any[] value = new Any[1];
		value[0] = orb.create_any();
		value[0].insert_string(testCaseId.baseName());
		invocation3("testCaseStarted", cmp, cmp, parameter, value, tim);
	}

	/**
	 * Log the events generated when a testcase terminates.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param verdict
	 *            the verdict of the testcase.
	 * @param parameterList
	 *            the parameters used to start the testcase.
	 * @param message
	 *            an additional message to this event.
	 */
	public void logTestCaseTerminated(long time, Object source,
			VerdictValue verdict, TciParameterList parameterList, String message) {
		//justka
		System.out.println("logTestCaseTerminated");
		long tim = time;
		IdentityDescriptor cmp = mtc;

		Parameter[] parameter = new Parameter[] { new Parameter("in", "string",
				"TESTCASE") };

		Any[] value = new Any[1];
		value[0] = orb.create_any();
		value[0].insert_string(verdict.toString());

		invocation3("TestCaseTerminated", cmp, cmp, parameter, value, tim);
	}

	/**
	 * Log the events generated when the control part terminates.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param message
	 *            an additional message to this event.
	 */
	public void logControlTerminated(long time, Object source, String message) {
		System.out.println("ControlTerminated");
		long tim = time;
		IdentityDescriptor cmp = control;
		Parameter[] parameter = new Parameter[] { new Parameter("in", "string",
				"message") };

		Any[] value = new Any[1];
		value[0] = orb.create_any();
		value[0].insert_string(message);

		invocation3("ControlTerminated", cmp, cmp, parameter, value, tim);
	}

	/**
	 * Log the termination of a test component.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param comp
	 *            Identifier of the component that has terminated.
	 * @param verdict
	 *            Verdict after termination of the component.
	 * @param status
	 *            indicate if the components terminates successfully or not.
	 * @param error
	 *            the error message if any.
	 */
	public void logTestComponentTerminated(long time, Object source,
			TriComponentId comp, VerdictValue verdict, boolean status,
			String error) {
		System.out
				.println("TestComponentTerminated   " + comp.getComponentId());

		long tim = time;
		IdentityDescriptor cmp = (IdentityDescriptor) components.get(comp
				.getComponentId());
		Parameter[] parameter = new Parameter[] {
				new Parameter("in", "string", "verdict"),
				new Parameter("in", "string", "error"),
				new Parameter("in", "string", "comp") };

		Any[] value = new Any[3];
		value[0] = orb.create_any();
		value[0].insert_string(verdict.toString());
		value[1] = orb.create_any();
		value[1].insert_string(error.toString());
		value[2] = orb.create_any();
		value[2].insert_string(comp.getComponentId());

		invocation3("TestComponentTerminated", cmp, cmp, parameter, value, tim);

	}

	/**
	 * Log the log statements.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param testComponentId
	 *            the Id of the component on which the log event occurs.
	 * @param message
	 *            the message contained in the log message.
	 */
	public void logLogStatement(long time, Object source,
			TriComponentId testComponentId, String message) {
		System.out.println("logStatement");
		long tim = time;
		IdentityDescriptor cmp = (IdentityDescriptor) components
				.get(testComponentId.getComponentId());

		Parameter[] parameter = new Parameter[] { new Parameter("in", "string",
				"message") };

		Any[] value = new Any[1];
		value[0] = orb.create_any();
		value[0].insert_string(message);

		invocation("logStatement", cmp, cmp, parameter, value, tim);
	}

	/**
	 * Log different statements in the TTCN3 generated code.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param message
	 *            the statement.
	 */
	public void logStatement(long time, Object source, int line, String message) {
	}

	/**
	 * Log the unmatching of a message.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param tsiPortId
	 *            identifier of the receiving test component
	 * @param address
	 *            sender address from SUT
	 * @param receivedMessage
	 *            the encoded received message
	 * @param template
	 *            the template that does not match the received message
	 * @param message
	 *            an additional message to this event.
	 */
	public void logUnmatchedMsg(long time, Object source, TriPortId tsiPortId,
			TriAddress address, Value receivedMessage, Value template,
			String message) {
		System.out.println("logUnmatchedMsg");
		long tim = time;
		IdentityDescriptor cmp = (IdentityDescriptor) components.get(tsiPortId
				.getComponent().getComponentId());
		Parameter[] parameter = new Parameter[] {
				new Parameter("in", "string", "template"),
				new Parameter("in", "string", "message"),
				new Parameter("in", "string", "receivedMessage") };

		Any[] value = new Any[3];
		value[0] = orb.create_any();
		value[0].insert_string(template.toString());

		value[1] = orb.create_any();
		value[1].insert_string(message);
		value[2] = orb.create_any();
		value[2].insert_string(receivedMessage.toString());
		invocation3("UnmatchedMsg", cmp, cmp, parameter, value, tim);
	}

	/**
	 * Log the matching of a message.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param tsiPortId
	 *            identifier of the receiving test component
	 * @param address
	 *            sender address from SUT
	 * @param receivedMessage
	 *            the encoded received message
	 * @param template
	 *            the template that matches to the received message
	 * @param message
	 *            an additional message to this event.
	 */
	public void logMatchedMsg(long time, Object source, TriPortId tsiPortId,
			TriAddress address, Value receivedMessage, Value template,
			String message) {
		System.out.println("MatchedMsg");
		long tim = time;
		IdentityDescriptor cmp = (IdentityDescriptor) components.get(tsiPortId
				.getComponent().getComponentId());
		Parameter[] parameter = new Parameter[] {
				new Parameter("in", "string", "template"),
				new Parameter("in", "string", "message"),
				new Parameter("in", "string", "receivedMessage") };

		Any[] value = new Any[3];
		value[0] = orb.create_any();
		value[0].insert_string(template.toString());

		value[1] = orb.create_any();
		value[1].insert_string(message);
		value[2] = orb.create_any();
		value[2].insert_string(receivedMessage.toString());
		invocation3("MatchedMsg", cmp, cmp, parameter, value, tim);
	}

	/**
	 * Log the unmatching of a call.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param tsiPortId
	 *            identifier of the receiving test component.
	 * @param signature
	 *            the encoded received signature.
	 * @param signatureTemplate
	 *            the template that does not match the received call signature.
	 * @param receivedParameter
	 *            the encoded received call parameter.
	 * @param paramTemplate
	 *            the template that does not match the received call parameter
	 * @param flag
	 *            if signature does not match then it should be set to 0, if
	 *            param does not match then to 1
	 * @param message
	 *            an additional message to this event.
	 */

	public void logUnmatchedCall(long time, Object source, TriPortId tsiPortId,
			TriAddress address, String signature, String signatureTemplate,
			Value receivedParameter, Value paramTemplate, int flag,
			String message) {
		System.out.println("UnmatchedCall");
		long tim = time;
		IdentityDescriptor cmp = (IdentityDescriptor) components.get(tsiPortId
				.getComponent().getComponentId());
		Parameter[] parameter = new Parameter[] {
				new Parameter("in", "string", "receivedParameter"),
				new Parameter("in", "string", "paramTemplate"),
				new Parameter("in", "string", "message") };

		Any[] value = new Any[3];
		value[0] = orb.create_any();
		value[0].insert_string(receivedParameter.toString());

		value[1] = orb.create_any();
		value[1].insert_string(paramTemplate.toString());
		value[2] = orb.create_any();
		value[2].insert_string(message);
		invocation3("UnmatchedCall", cmp, cmp, parameter, value, tim);
	}

	/**
	 * Log the matching of a call.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param receiverPortId
	 *            identifier of the receiving test component.
	 * @param signature
	 *            the encoded received signature.
	 * @param signatureTemplate
	 *            the template that matches the received call signature.
	 * @param receivedParameter
	 *            the encoded received call parameter.
	 * @param paramTemplate
	 *            the template that matches the received call parameter.
	 * @param flag
	 *            if signature has matched then it should be set to 0, if param
	 *            matched then to 1 and if call matched then to 2
	 * @param message
	 *            an additional message to this event.
	 */

	public void logMatchedCall(long time, Object source,
			TriPortId receiverPortId, TriAddress address, String signature,
			String signatureTemplate, Value receivedParameter,
			Value paramTemplate, int flag, String message) {
		System.out.println("MatchedCall");
		long tim = time;
		IdentityDescriptor cmp = (IdentityDescriptor) components
				.get(receiverPortId.getComponent().getComponentId());
		Parameter[] parameter = new Parameter[] {
				new Parameter("in", "string", "receivedParameter"),
				new Parameter("in", "string", "paramTemplate"),
				new Parameter("in", "string", "message") };

		Any[] value = new Any[3];
		value[0] = orb.create_any();
		value[0].insert_string(receivedParameter.toString());

		value[1] = orb.create_any();
		value[1].insert_string(paramTemplate.toString());
		value[2] = orb.create_any();
		value[2].insert_string(message);
		invocation3("MatchedCall", cmp, cmp, parameter, value, tim);
	}

	/**
	 * Log the unmatching of a reply.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param tsiPortId
	 *            identifier of the receiving test component.
	 * @param signature
	 *            the encoded received signature.
	 * @param signatureTemplate
	 *            the template that does not match the received reply signature.
	 * @param paramOrReturn
	 *            the encoded param or return value.
	 * @param template
	 *            the template that does not match the param or the return
	 *            value.
	 * @param flag
	 *            if signature does not match then it should be set to 0, if
	 *            param to 1, if return value to 2
	 * @param message
	 *            an additional message to this event.
	 */
	public void logUnmatchedReply(long time, Object source,
			TriPortId tsiPortId, TriAddress address, String signature,
			String signatureTemplate, Value paramOrReturn, Value template,
			int flag, String message) {
		System.out.println("logUnmatchedReply");
		long tim = time;
		IdentityDescriptor cmp = (IdentityDescriptor) components.get(tsiPortId
				.getComponent().getComponentId());
		Parameter[] parameter = new Parameter[] {
				new Parameter("in", "string", "paramOrReturn"),
				new Parameter("in", "string", "template"),
				new Parameter("in", "string", "message") };

		Any[] value = new Any[3];
		value[0] = orb.create_any();
		value[0].insert_string(paramOrReturn.toString());

		value[1] = orb.create_any();
		value[1].insert_string(template.toString());
		value[2] = orb.create_any();
		value[2].insert_string(message);
		invocation3("UnmatchedReply", cmp, cmp, parameter, value, tim);
	}

	/**
	 * Log the matching of a reply.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param receiverPortId
	 *            identifier of the receiving test component.
	 * @param signature
	 *            the encoded received signature.
	 * @param signatureTemplate
	 *            the template that matches to the received call signature.
	 * @param paramOrReturn
	 *            the encoded param or return value.
	 * @param template
	 *            the template that matches to the param or the return value.
	 * @param flag
	 *            if signature has matched then it should be set to 0, if param
	 *            to 1, if return value to 2 and if reply to 3
	 * @param message
	 *            an additional message to this event.
	 */
	public void logMatchedReply(long time, Object source,
			TriPortId receiverPortId, TriAddress address, String signature,
			String signatureTemplate, Value paramOrReturn, Value template,
			int flag, String message) {
		System.out.println("MatchedReply");
		long tim = time;
		IdentityDescriptor cmp = (IdentityDescriptor) components
				.get(receiverPortId.getComponent().getComponentId());
		Parameter[] parameter = new Parameter[] {
				new Parameter("in", "string", "paramOrReturn"),
				new Parameter("in", "string", "template"),
				new Parameter("in", "string", "message") };

		Any[] value = new Any[3];
		value[0] = orb.create_any();
		value[0].insert_string(paramOrReturn.toString());

		value[1] = orb.create_any();
		value[1].insert_string(template.toString());
		value[2] = orb.create_any();
		value[2].insert_string(message);
		invocation3("MatchedReply", cmp, cmp, parameter, value, tim);
	}

	/**
	 * Log the unmatching of a raise.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param tsiPortId
	 *            identifier of the receiving test component
	 * @param signature
	 *            the encoded received signature.
	 * @param signatureTemplate
	 *            the template that matches the received signature.
	 * @param receivedException
	 *            the encoded received exception.
	 * @param exceptionTemplate
	 *            the template that matches to the received exception.
	 * @param flag
	 *            if signature does not match then it should be set to 0, if
	 *            exception does not match then to 1
	 * @param message
	 *            an additional message to this event.
	 */
	public void logUnmatchedRaise(long time, Object source,
			TriPortId tsiPortId, TriAddress address, String signature,
			String signatureTemplate, Value receivedException,
			Value exceptionTemplate, int flag, String message) {
		System.out.println("UnmatchedRaise");
		long tim = time;
		IdentityDescriptor cmp = (IdentityDescriptor) components.get(tsiPortId
				.getComponent().getComponentId());
		Parameter[] parameter = new Parameter[] {
				new Parameter("in", "string", "receivedException"),
				new Parameter("in", "string", "exceptionTemplate"),
				new Parameter("in", "string", "message") };

		Any[] value = new Any[3];
		value[0] = orb.create_any();
		value[0].insert_string(receivedException.toString());

		value[1] = orb.create_any();
		value[1].insert_string(exceptionTemplate.toString());
		value[2] = orb.create_any();
		value[2].insert_string(message);
		invocation3("UnmatchedRaise", cmp, cmp, parameter, value, tim);
	}

	/**
	 * Log the matching of a raise.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param receiverPortId
	 *            identifier of the receiving test component.
	 * @param signature
	 *            the encoded received signature.
	 * @param signatureTemplate
	 *            the template that matches the received signature.
	 * @param receivedException
	 *            the encoded received exception.
	 * @param exceptionTemplate
	 *            the template that matches to the received exception.
	 * @param flag
	 *            if signature has matched then it should be set to 0, if
	 *            exception matched then to 1 and if raise matched then ti 2
	 * @param message
	 *            an additional message to this event.
	 */
	public void logMatchedRaise(long time, Object source,
			TriPortId receiverPortId, TriAddress address, String signature,
			String signatureTemplate, Value receivedException,
			Value exceptionTemplate, int flag, String message) {
		System.out.println("logMatchedRaise");
		long tim = time;
		IdentityDescriptor cmp = (IdentityDescriptor) components
				.get(receiverPortId.getComponent().getComponentId());
		Parameter[] parameter = new Parameter[] {
				new Parameter("in", "string", "receivedException"),
				new Parameter("in", "string", "exceptionTemplate"),
				new Parameter("in", "string", "message") };

		Any[] value = new Any[3];
		value[0] = orb.create_any();
		value[0].insert_string(receivedException.toString());

		value[1] = orb.create_any();
		value[1].insert_string(exceptionTemplate.toString());
		value[2] = orb.create_any();
		value[2].insert_string(message);
		invocation3("MatchedRaise", cmp, cmp, parameter, value, tim);
	}

	/**
	 * Log the sending of a message over a connected port.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param senderPort
	 *            Port identifier at the sending component via which the message
	 *            is sent.
	 * @param receiverComp
	 *            Identifier of the receiving component.
	 * @param sendMessage
	 *            The message to be send.
	 * @param message
	 *            an additional message to this event.
	 */
	public void logSendConnected(long time, Object source,
			TriPortId senderPort, TriComponentId receiverComp,
			Value sendMessage, String message) {
		System.out.println("logSendConnected");
		long tim = time;
		IdentityDescriptor cmp1 = (IdentityDescriptor) components
				.get(senderPort.getComponent().getComponentId());
		IdentityDescriptor cmp2 = (IdentityDescriptor) components
				.get(receiverComp.getComponentId());

		Parameter[] parameter = new Parameter[] {
				new Parameter("in", "string", "senderPort"),
				new Parameter("in", "string", "receiverComp"),
				new Parameter("in", "string", "sendMessage"),
				new Parameter("in", "string", "message") };

		Any[] value = new Any[4];
		value[0] = orb.create_any();
		value[0].insert_string(senderPort.getComponent().getComponentId());
		value[1] = orb.create_any();
		value[1].insert_string(receiverComp.getComponentId());
		value[2] = orb.create_any();
		value[2].insert_string(sendMessage.toString());
		value[3] = orb.create_any();
		value[3].insert_string(message);

		invocation2("send", cmp1, cmp2, parameter, value, tim);
	}

	/**
	 * Log the call operations over a connected port.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param senderPort
	 *            Port identifier at the sending component via which the message
	 *            is sent.
	 * @param receiverComp
	 *            Identifier of the receiving component.
	 * @param signature
	 *            Identifier of the signature of the procedure call.
	 * @param parameterList
	 *            A list of value parameters which are part of the indicated
	 *            signature. The parameters in parameterList are ordered as they
	 *            appear in the TTCN-3 signature declaration.
	 * @param message
	 *            an additional message to this event.
	 */
	public void logCallConnected(long time, Object source,
			TriPortId senderPort, TriComponentId receiverComp,
			TriSignatureId signature, TciParameterList parameterList,
			String message) {
		System.out.println("CallConnected");
		long tim = time;
		IdentityDescriptor cmp1 = (IdentityDescriptor) components
				.get(senderPort.getComponent().getComponentId());
		IdentityDescriptor cmp2 = (IdentityDescriptor) components
				.get(receiverComp.getComponentId());

		Parameter[] parameter = new Parameter[] {
				new Parameter("in", "string", "senderPort"),
				new Parameter("in", "string", "receiverComp"),
				new Parameter("in", "string", "parameterList"),
				new Parameter("in", "string", "message") };

		Any[] value = new Any[4];
		value[0] = orb.create_any();
		value[0].insert_string(senderPort.getComponent().getComponentId());
		value[1] = orb.create_any();
		value[1].insert_string(receiverComp.getComponentId());
		value[2] = orb.create_any();
		value[2].insert_string(parameterList.toString());
		value[3] = orb.create_any();
		value[3].insert_string(message);

		invocation3("CallConnected", cmp1, cmp2, parameter, value, tim);
	}

	/**
	 * Log the reply operations.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param senderPort
	 *            Identifier of the port sending the reply.
	 * @param receiverComp
	 *            Identifier of the component receiving the reply.
	 * @param signature
	 *            Identifier of the signature of the procedure call.
	 * @param parameterList
	 *            A list of encoded parameters which are part of the indicated
	 *            signature. The parameters in parameterList are ordered as they
	 *            appear in the TTCN-3 signature declaration.
	 * @param returnValue
	 *            (Optional) return value of the procedure call.
	 * @param message
	 *            an additional message to this event.
	 */
	public void logReplyConnected(long time, Object source,
			TriPortId senderPort, TriComponentId receiverComp,
			TriSignatureId signature, TciParameterList parameterList,
			Value returnValue, String message) {
		System.out.println("ReplyConnected");
		long tim = time;
		IdentityDescriptor cmp1 = (IdentityDescriptor) components
				.get(senderPort.getComponent().getComponentId());
		IdentityDescriptor cmp2 = (IdentityDescriptor) components
				.get(receiverComp.getComponentId());

		Parameter[] parameter = new Parameter[] {
				new Parameter("in", "string", "parameterList"),
				new Parameter("in", "string", "returnValue"),
				new Parameter("in", "string", "MESSAGE") };

		Any[] value = new Any[3];
		value[0] = orb.create_any();
		value[0].insert_string(parameterList.toString());
		value[1] = orb.create_any();
		value[1].insert_string(returnValue.toString());
		value[2] = orb.create_any();
		value[2].insert_string(message);

		invocation3("ReplyConnected", cmp1, cmp2, parameter, value, tim);
	}

	/**
	 * Log the raise operations.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param senderPort
	 *            Identifier of the port sending the reply.
	 * @param receiverComp
	 *            Identifier of the component receiving the reply.
	 * @param signature
	 *            Identifier of the signature of the procedure call.
	 * @param except
	 *            The exception value.
	 * @param message
	 *            an additional message to this event.
	 */
	public void logRaiseConnected(long time, Object source,
			TriPortId senderPort, TriComponentId receiverComp,
			TciSignatureId signature, Value except, String message) {
		System.out.println("RaiseConnected");
		long tim = time;
		IdentityDescriptor cmp1 = (IdentityDescriptor) components
				.get(senderPort.getComponent().getComponentId());
		IdentityDescriptor cmp2 = (IdentityDescriptor) components
				.get(receiverComp.getComponentId());

		Parameter[] parameter = new Parameter[] {
				new Parameter("in", "string", "except"),
				new Parameter("in", "string", "MESSAGE") };

		Any[] value = new Any[2];
		value[0] = orb.create_any();
		value[0].insert_string(except.toString());
		value[1] = orb.create_any();
		value[1].insert_string(message);

		invocation3("RaiseConnected", cmp1, cmp2, parameter, value, tim);
	}

	/**
	 * Log the creation of a TestComponent.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param kind
	 *            The kind of component that shall be created, either mtc, ptc
	 *            or control.
	 * @param componentType
	 *            Identifier of the TTCN-3 component type that shall be created.
	 * @param message
	 *            an additional message to this event.
	 */
	public void logCreateTestComponentReq(long time, Object source, int kind,
			Type componentType, String message) {
		//leer
	}

	/**
	 * Log the start of a test behavior.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param comp
	 *            Identifier of the component to be started.
	 * @param behavior
	 *            Identifier of the behavior to be started on the component.
	 * @param parameterList
	 *            A list of Values where each value defines a parameter from the
	 *            parameter list as defined in the TTCN-3 function declaration
	 *            of the function being started.
	 * @param message
	 *            an additional message to this event.
	 */
	public void logStartTestComponentReq(long time, Object source,
			TriComponentId comp, TciBehaviourId behavior,
			TciParameterList parameterList, String message) {
		System.out.println("StartTestComponentReq");
		long tim = time;
		IdentityDescriptor cmp = (IdentityDescriptor) components.get(comp
				.getComponentId());
		Parameter[] parameter = new Parameter[] {
				new Parameter("in", "string", "behavior"),
				new Parameter("in", "string", "parameterList"),
				new Parameter("in", "string", "message") };

		Any[] value = new Any[3];
		value[0] = orb.create_any();
		value[0].insert_string(behavior.toString());
		value[1] = orb.create_any();
		value[1].insert_string(parameterList.toString());
		value[2] = orb.create_any();
		value[2].insert_string(message);
		invocation2("StartTestComponentReq", cmp, cmp, parameter, value, tim);
	}

	/**
	 * Log the stop of a Test Component.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param comp
	 *            Identifier of the component to be stopped.
	 * @param message
	 *            an additional message to this event.
	 */
	public void logStopTestComponentReq(long time, Object source,
			TriComponentId comp, String message) {
		System.out.println("StopTestComponentReq");
		long tim = time;
		IdentityDescriptor cmp = (IdentityDescriptor) components.get(comp
				.getComponentId());
		Parameter[] parameter = new Parameter[] { new Parameter("in", "string",
				"message") };

		Any[] value = new Any[1];
		value[0] = orb.create_any();
		value[0].insert_string(message);
		invocation2("StopTestComponentReq", cmp, cmp, parameter, value, tim);
	}

	/**
	 * Log the connect request of two ports.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param fromPort
	 *            Identifier of the test component port to be connected from.
	 * @param toPort
	 *            Identifier of the test component port to be connected to.
	 * @param message
	 *            an additional message to this event.
	 */
	public void logConnectReq(long time, Object source, TriPortId fromPort,
			TriPortId toPort, String message) {
		System.out.println("ConnectReq");
		long tim = time;
		IdentityDescriptor cmp1 = (IdentityDescriptor) components.get(fromPort
				.getComponent().getComponentId());
		IdentityDescriptor cmp2 = (IdentityDescriptor) components.get(toPort
				.getComponent().getComponentId());
		Parameter[] parameter = new Parameter[] {
				new Parameter("in", "string", "fromPort"),
				new Parameter("in", "string", "toPort"),
				new Parameter("in", "string", "message") };

		Any[] value = new Any[3];
		value[0] = orb.create_any();
		value[0].insert_string(fromPort.getComponent().getComponentId());
		value[1] = orb.create_any();
		value[1].insert_string(toPort.getComponent().getComponentId());
		value[2] = orb.create_any();
		value[2].insert_string(message);

		invocation2("ConnectReq", cmp1, cmp2, parameter, value, tim);
	}

	/**
	 * Log the disconnect request of two ports.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param fromPort
	 *            Identifier of the test component port to be disconnected.
	 * @param toPort
	 *            Identifier of the test component port to be disconnected.
	 * @param message
	 *            an additional message to this event.
	 */
	public void logDisconnectReq(long time, Object source, TriPortId fromPort,
			TriPortId toPort, String message) {
		System.out.println("DisconnectReq");
		long tim = time;
		IdentityDescriptor cmp1 = (IdentityDescriptor) components.get(fromPort
				.getComponent().getComponentId());
		IdentityDescriptor cmp2 = (IdentityDescriptor) components.get(toPort
				.getComponent().getComponentId());
		Parameter[] parameter = new Parameter[] {
				new Parameter("in", "string", "fromPort"),
				new Parameter("in", "string", "toPort"),
				new Parameter("in", "string", "message") };

		Any[] value = new Any[3];
		value[0] = orb.create_any();
		value[0].insert_string(fromPort.getComponent().getComponentId());
		value[1] = orb.create_any();
		value[1].insert_string(toPort.getComponent().getComponentId());
		value[2] = orb.create_any();
		value[2].insert_string(message);
		invocation2("DisconnectReq", cmp1, cmp2, parameter, value, tim);
	}

	/**
	 * Log the map request of two ports.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param fromPort
	 *            Identifier of the test component port to be mapped from.
	 * @param toPort
	 *            Identifier of the test component port to be mapped to.
	 * @param message
	 *            an additional message to this event.
	 */
	public void logMapReq(long time, Object source, TriPortId fromPort,
			TriPortId toPort, String message) {
		System.out.println("logMapReq");
		long tim = time;

		int Punkt2 = fromPort.getComponent().getComponentTypeName().toString()
				.lastIndexOf('.');
		String fromPortName = "";
		fromPortName = fromPort.getComponent().getComponentTypeName()
				.toString().substring(Punkt2 + 1);

		IdentityDescriptor cmp1 = (IdentityDescriptor) components.get(fromPort
				.getComponent().getComponentId());

		String CompType = "";
		int Punkt = toPort.getComponent().getComponentTypeName().indexOf('.');
		CompType = toPort.getComponent().getComponentTypeName().substring(
				Punkt + 1);

		String SUTport = "";

		
		SUTport = toPort.getPortName() + toPort.getPortIndex();

		IdentityDescriptor cmp = createObject(appl_id, container_id,
				CompType, SUTport);
		components.put(SUTport, cmp);

		Parameter[] parameter = new Parameter[] {
				new Parameter("in", "string", "fromPort"),
				new Parameter("in", "string", "toPort"),
				new Parameter("in", "string", "CompType"),
				new Parameter("in", "string", "message") };

		Any[] value = new Any[4];
		value[0] = orb.create_any();
		value[0].insert_string(fromPortName);
		value[1] = orb.create_any();
		value[1].insert_string(SUTport);
		value[2] = orb.create_any();
		value[2].insert_string(CompType);
		value[3] = orb.create_any();
		value[3].insert_string(message);
		invocation("mapReq", cmp1, cmp, parameter, value, tim);

	}

	/**
	 * Log the unmap request of two ports.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param fromPort
	 *            Identifier of the test component port to be unmapped.
	 * @param toPort
	 *            Identifier of the test component port to be unmapped.
	 * @param message
	 *            an additional message to this event.
	 */
	public void logUnmapReq(long time, Object source, TriPortId fromPort,
			TriPortId toPort, String message) {
		System.out.println("logUnmapReq");
		long tim = time;

		int Punkt2 = fromPort.getComponent().getComponentTypeName().toString()
				.lastIndexOf('.');
		String fromPortName = "";
		fromPortName = fromPort.getComponent().getComponentTypeName()
				.toString().substring(Punkt2 + 1);

		IdentityDescriptor cmp1 = (IdentityDescriptor) components.get(fromPort
				.getComponent().getComponentId());
		//	IdentityDescriptor cmp2 = (IdentityDescriptor)
		// components.get(toPort.getComponent()
		//																						.getComponentId());

		String CompType = "";
		int Punkt = toPort.getComponent().getComponentTypeName().indexOf('.');
		CompType = toPort.getComponent().getComponentTypeName().substring(
				Punkt + 1);

		String SUTport = "";

		SUTport = toPort.getPortName()  + toPort.getPortIndex();

		IdentityDescriptor cmp = createObject(appl_id, container_id,
				CompType, SUTport);
		components.put(SUTport, cmp);

		Parameter[] parameter = new Parameter[] {
				new Parameter("in", "string", "fromPort"),
				new Parameter("in", "string", "toPort"),
				new Parameter("in", "string", "CompType"),
				new Parameter("in", "string", "message") };

		Any[] value = new Any[4];
		value[0] = orb.create_any();
		value[0].insert_string(fromPortName);
		value[1] = orb.create_any();
		value[1].insert_string(SUTport);
		value[2] = orb.create_any();
		value[2].insert_string(CompType);
		value[3] = orb.create_any();
		value[3].insert_string(message);
		invocation("UnmapReq", cmp1, cmp, parameter, value, tim);
	}

	/**
	 * Log the execute test case request called in TciCHProvided
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param testComponentId
	 *            the component id where to execute the test case.
	 * @param tsiPortList
	 *            a list of the system ports.
	 * @param message
	 *            an additional message to this event.
	 */
	public void logExecuteTestCaseReq(long time, Object source,
			TriComponentId testComponentId, TriPortIdList tsiPortList,
			String message) {
		System.out.println("logExecuteTestCaseReq");
		long tim = time;
		IdentityDescriptor cmp = (IdentityDescriptor) components
				.get(testComponentId.getComponentId());
		Parameter[] parameter = new Parameter[] {
				new Parameter("in", "string", "tsiPortList"),
				new Parameter("in", "string", "message") };

		Any[] value = new Any[2];
		value[0] = orb.create_any();
		value[0].insert_string(message);
		value[1] = orb.create_any();
		value[1].insert_string(tsiPortList.toString());
		invocation2("ExecuteTestCaseReq", cmp, cmp, parameter, value, tim);
	}

	/**
	 * Log the receiving of a message.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param senderPort
	 *            Port identifier at the sending component via which the message
	 *            is send.
	 * @param receiverComp
	 *            Identifier of the receiving component.
	 * @param receivedMessage
	 *            The value to be enqueued.
	 * @param message
	 *            an additional message to this event.
	 */
	public void logEnqueueMsgConnected(long time, Object source,
			TriPortId senderPort, TriComponentId receiverComp,
			Value receivedMessage, String message) {
		System.out.println("logEnqueueMsgConnected");
		long tim = time;
		String port = "";
		int Punkt = receiverComp.getComponentTypeName().indexOf('.');
		port = receiverComp.getComponentTypeName().substring(Punkt + 1);

		IdentityDescriptor cmp1 = (IdentityDescriptor) components
				.get(senderPort.getComponent().getComponentId());
		IdentityDescriptor cmp2 = (IdentityDescriptor) components.get(port);

		Parameter[] parameter = new Parameter[] {
				new Parameter("in", "string", "senderPort"),
				new Parameter("in", "string", "receiverComp"),
				new Parameter("in", "string", "receivedMessage"),
				new Parameter("in", "string", "message") };

		Any[] value = new Any[4];
		value[0] = orb.create_any();
		value[0].insert_string(senderPort.getComponent().getComponentId());
		value[1] = orb.create_any();
		value[1].insert_string(port);
		value[2] = orb.create_any();
		value[2].insert_string(receivedMessage.toString());
		value[3] = orb.create_any();
		value[3].insert_string(message);

		invocation3("EnqueueMsgConnected", cmp2, cmp1, parameter, value, tim);
	}

	/**
	 * Log the receive of call events.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param senderPort
	 *            Port identifier at the sending component via which the message
	 *            is send.
	 * @param receiverComp
	 *            Identifier of the receiving component.
	 * @param signature
	 *            Identifier of the signature of the procedure call.
	 * @param parameterList
	 *            A list of value parameters which are part of the indicated
	 *            signature. The parameters in parameterList are ordered as they
	 *            appear in the TTCN-3 signature declaration.
	 * @param message
	 *            an additional message to this event.
	 */
	public void logEnqueueCallConnected(long time, Object source,
			TriPortId senderPort, TriComponentId receiverComp,
			TriSignatureId signature, TciParameterList parameterList,
			String message) {
		System.out.println("logEnqueueCallConnected");
		long tim = time;
		String port = "";
		int Punkt = receiverComp.getComponentTypeName().indexOf('.');
		port = receiverComp.getComponentTypeName().substring(Punkt + 1);

		IdentityDescriptor cmp1 = (IdentityDescriptor) components
				.get(senderPort.getComponent().getComponentId());
		IdentityDescriptor cmp2 = (IdentityDescriptor) components.get(port);

		Parameter[] parameter = new Parameter[] {
				new Parameter("in", "string", "parameterList"),
				new Parameter("in", "string", "MESSAGE") };

		Any[] value = new Any[2];
		value[0] = orb.create_any();
		value[0].insert_string(new String(parameterList.toString()));
		value[1] = orb.create_any();
		value[1].insert_string(new String(message));

		invocation3("EnqueueCallConnected", cmp2, cmp1, parameter, value, tim);
	}

	/**
	 * Log the reply events.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param senderPort
	 *            Identifier of the port sending the reply.
	 * @param receiverComp
	 *            Identifier of the component receiving the reply.
	 * @param signature
	 *            Identifier of the signature of the procedure call.
	 * @param parameterList
	 *            A list of value parameters which are part of the indicated
	 *            signature. The parameters in parameterList are ordered as they
	 *            appear in the TTCN-3 signature declaration.
	 * @param returnValue
	 *            (Optional) return value of the procedure call.
	 * @param message
	 *            an additional message to this event.
	 */
	public void logEnqueueReplyConnected(long time, Object source,
			TriPortId senderPort, TriComponentId receiverComp,
			TriSignatureId signature, TciParameterList parameterList,
			Value returnValue, String message) {
		System.out.println("logEnqueueCallConnected");
		long tim = time;
		String port = "";
		int Punkt = receiverComp.getComponentTypeName().indexOf('.');
		port = receiverComp.getComponentTypeName().substring(Punkt + 1);

		IdentityDescriptor cmp1 = (IdentityDescriptor) components
				.get(senderPort.getComponent().getComponentId());
		IdentityDescriptor cmp2 = (IdentityDescriptor) components.get(port);

		Parameter[] parameter = new Parameter[] {
				new Parameter("in", "string", "parameterList"),
				new Parameter("in", "string", "returnValue"),
				new Parameter("in", "string", "MESSAGE") };

		Any[] value = new Any[3];
		value[0] = orb.create_any();
		value[0].insert_string(new String(parameterList.toString()));
		value[1] = orb.create_any();
		value[1].insert_string(new String(returnValue.toString()));
		value[2] = orb.create_any();
		value[2].insert_string(new String(message));

		invocation3("EnqueueCallConnected", cmp2, cmp1, parameter, value, tim);
	}

	/**
	 * Log the raise events.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param senderPort
	 *            Identifier of the port sending the reply.
	 * @param receiverComp
	 *            Identifier of the component receiving the reply.
	 * @param signature
	 *            Identifier of the signature of the procedure call.
	 * @param except
	 *            The exception.
	 * @param message
	 *            an additional message to this event.
	 */
	public void logEnqueueRaiseConnected(long time, Object source,
			TriPortId senderPort, TriComponentId receiverComp,
			TriSignatureId signature, Value except, String message) {
		System.out.println("logEnqueueRaiseConnected");
		long tim = time;
		String port = "";
		int Punkt = receiverComp.toString().indexOf('.');
		port = receiverComp.toString().substring(Punkt + 1);

		IdentityDescriptor cmp1 = (IdentityDescriptor) components
				.get(senderPort.getComponent().getComponentId());
		IdentityDescriptor cmp2 = (IdentityDescriptor) components.get(port);

		Parameter[] parameter = new Parameter[] {
				new Parameter("in", "string", "except"),
				new Parameter("in", "string", "MESSAGE") };

		Any[] value = new Any[2];
		value[0] = orb.create_any();
		value[0].insert_string(new String(except.toString()));
		value[1] = orb.create_any();
		value[1].insert_string(new String(message));

		invocation3("EnqueueRaiseConnected", cmp2, cmp1, parameter, value, tim);
	}

	/**
	 * Log the creation of a test component.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param kind
	 *            The kind of component that shall be created, either mtc, ptc
	 *            or control.
	 * @param componentType
	 *            Identifier of the TTCN-3 component type that shall be created.
	 * @param message
	 *            an additional message to this event.
	 */
	public void logCreateTestComponent(long time, Object source, int kind,
			Type componentType, String id, String message) {
		System.out.println("logCreateTestComponent");
		System.out.println("ComponentType  " + componentType.getName());

		String compType = "";

		/*
		 * if (componentType.getName().startsWith("Interface_TTCN3")) { compType =
		 * componentType.getName().substring(16); } else { compType = "control"; }
		 */
		long tim = time;
		compType = componentType.getName();

		//if (component = compType )
		//	 control = compType ;

		IdentityDescriptor cmp = createObject(appl_id, container_id,
				compType, id);
		components.put(id, cmp);

		if (kind == 0) {
			control = cmp;
		} else if (kind == 1) {
			mtc = cmp;
		} else if (kind == 3) {
			system = cmp;
		}

		Parameter[] parameter = new Parameter[] {
				new Parameter("in", "string", "ID"),
				new Parameter("in", "string", "TYPE") };

		Any[] value = new Any[2];
		value[0] = orb.create_any();
		value[0].insert_string(id);
		value[1] = orb.create_any();
		value[1].insert_string(compType);

		invocation2("createTestComponent", cmp, cmp, parameter, value, tim);
	}

	/**
	 * Log the start of a test component.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param comp
	 *            Identifier of the component to be started. Refers to an
	 *            identifier previously created through a call to
	 *            tciCreateTestComponent
	 * @param behavior
	 *            Identifier of the behavior to be started on the component.
	 * @param parameterList
	 *            A list of Values where each value defines a parameter from the
	 *            parameter list as defined in the TTCN-3 function declaration
	 *            of the function being started. The parameters in parameterList
	 *            are ordered as they appear in the TTCN-3 signature of the test
	 *            case. If no parameters have to be passed either the null value
	 *            or an empty parameterList, i.e. a list of length zero shall be
	 *            passed.
	 * @param message
	 *            an additional message to this event.
	 */
	public void logStartTestComponent(long time, Object source,
			TriComponentId comp, TciBehaviourId behavior,
			TciParameterList parameterList, String message) {
		System.out.println("logStartTestComponent");
		long tim = time;
		IdentityDescriptor cmp = (IdentityDescriptor) components.get(comp
				.getComponentId());

		Parameter[] parameter = new Parameter[] { new Parameter("in", "string",
				"BEHAVIOR") };

		Any[] value = new Any[1];
		value[0] = orb.create_any();
		value[0].insert_string(behavior.baseName());

		invocation("startTestComponent", cmp, cmp, parameter, value, tim);
	}

	/**
	 * Log the stop of a test component.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param comp
	 *            Identifier of the component to be stopped.
	 * @param message
	 *            an additional message to this event.
	 */
	public void logStopTestComponent(long time, Object source,
			TriComponentId comp, String message) {
		System.out.println("logStopTestComponent");
		long tim = time;
		IdentityDescriptor cmp = (IdentityDescriptor) components.get(comp
				.getComponentId());

		Parameter[] parameter = new Parameter[] { new Parameter("in", "string",
				"message") };

		Any[] value = new Any[1];
		value[0] = orb.create_any();
		value[0].insert_string(message);

		invocation3("StopTestComponent", cmp, cmp, parameter, value, tim);
	}

	/**
	 * Log the connection of two ports.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param fromPort
	 *            Identifier of the test component port to be connected from.
	 * @param toPort
	 *            Identifier of the test component port to be connected to.
	 * @param message
	 *            an additional message to this event.
	 */
	public void logConnect(long time, Object source, TriPortId fromPort,
			TriPortId toPort, String message) {
		System.out.println("logConnect");
		long tim = time;
		IdentityDescriptor cmp1 = (IdentityDescriptor) components.get(fromPort
				.getComponent().getComponentId());
		IdentityDescriptor cmp2 = (IdentityDescriptor) components.get(toPort
				.getComponent().getComponentId());
		//	czy tu nie ma triku ? z kropka ?
		Parameter[] parameter = new Parameter[] {
				new Parameter("in", "string", "fromPort"),
				new Parameter("in", "string", "toPort"),
				new Parameter("in", "string", "message") };

		Any[] value = new Any[3];
		value[0] = orb.create_any();
		value[0].insert_string(fromPort.getComponent().getComponentId());
		value[1] = orb.create_any();
		value[1].insert_string(toPort.getComponent().getComponentId());
		value[2] = orb.create_any();
		value[2].insert_string(message);

		invocation3("Connect", cmp2, cmp1, parameter, value, tim);
	}

	/**
	 * Log the disconnection of two ports.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param fromPort
	 *            Identifier of the test component port to be connected from.
	 * @param toPort
	 *            Identifier of the test component port to be connected to.
	 * @param message
	 *            an additional message to this event.
	 */
	public void logDisconnect(long time, Object source, TriPortId fromPort,
			TriPortId toPort, String message) {
		System.out.println("logDisconnect");
		long tim = time;
		IdentityDescriptor cmp1 = (IdentityDescriptor) components.get(fromPort
				.getComponent().getComponentId());
		IdentityDescriptor cmp2 = (IdentityDescriptor) components.get(toPort
				.getComponent().getComponentId());
		//				czy tu nie ma triku ? z kropka ?
		Parameter[] parameter = new Parameter[] {
				new Parameter("in", "string", "fromPort"),
				new Parameter("in", "string", "toPort"),
				new Parameter("in", "string", "message") };

		Any[] value = new Any[3];
		value[0] = orb.create_any();
		value[0].insert_string(fromPort.getComponent().getComponentId());
		value[1] = orb.create_any();
		value[1].insert_string(toPort.getComponent().getComponentId());
		value[2] = orb.create_any();
		value[2].insert_string(message);

		invocation3("Disconnect", cmp2, cmp1, parameter, value, tim);
	}

	/**
	 * Log the mapping of two ports.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param fromPort
	 *            Identifier of the test component port to be mapped from.
	 * @param toPort
	 *            Identifier of the test component port to be mapped to.
	 * @param message
	 *            an additional message to this event.
	 */
	public void logMap(long time, Object source, TriPortId fromPort,
			TriPortId toPort, String message) {
		System.out.println("logMap");
		long tim = time;

		int Punkt2 = fromPort.getComponent().getComponentTypeName().toString()
				.lastIndexOf('.');
		String fromPortName = "";
		fromPortName = fromPort.getComponent().getComponentTypeName()
				.toString().substring(Punkt2 + 1);
		IdentityDescriptor cmp1 = (IdentityDescriptor) components.get(fromPort
				.getComponent().getComponentId());

		String CompType = "";
		int Punkt = toPort.getComponent().getComponentTypeName().indexOf('.');
		CompType = toPort.getComponent().getComponentTypeName().substring(
				Punkt + 1);

		String SUTport = "";

		SUTport = toPort.getPortName()  + toPort.getPortIndex();

		IdentityDescriptor cmp = createObject(appl_id, container_id,
				CompType, SUTport);
		components.put(SUTport, cmp);

		Parameter[] parameter = new Parameter[] {
				new Parameter("in", "string", "fromPort"),
				new Parameter("in", "string", "toPort"),
				//jza
				new Parameter("in", "string", "CompType"),
				new Parameter("in", "string", "message") };

		Any[] value = new Any[4];
		value[0] = orb.create_any();
		value[0].insert_string(fromPortName);
		value[1] = orb.create_any();
		value[1].insert_string(SUTport);
		value[2] = orb.create_any();
		value[2].insert_string(CompType);
		value[3] = orb.create_any();
		value[3].insert_string(message);

		invocation("map", cmp1, cmp, parameter, value, tim);
	}

	/**
	 * Log the unmap of two ports.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param fromPort
	 *            Identifier of the test component port to be mapped from.
	 * @param toPort
	 *            Identifier of the test component port to be mapped to.
	 * @param message
	 *            an additional message to this event.
	 */
	public void logUnmap(long time, Object source, TriPortId fromPort,
			TriPortId toPort, String message) {
		System.out.println("logUnmap");
		long tim = time;
		IdentityDescriptor cmp1 = (IdentityDescriptor) components.get(fromPort
				.getComponent().getComponentId());
		IdentityDescriptor cmp2 = (IdentityDescriptor) components.get(toPort
				.getComponent().getComponentId());
		String CompType = "";
		int Punkt = toPort.getComponent().getComponentTypeName().indexOf('.');
		CompType = toPort.getComponent().getComponentTypeName().substring(
				Punkt + 1);

		String SUTport = "";

		SUTport = toPort.getPortName()  + toPort.getPortIndex();

		IdentityDescriptor cmp = createObject(appl_id, container_id,
				CompType, SUTport);
		components.put(SUTport, cmp);

		Parameter[] parameter = new Parameter[] {
				new Parameter("in", "string", "fromPort"),
				new Parameter("in", "string", "toPort"),
				new Parameter("in", "string", "message") };

		Any[] value = new Any[3];
		value[0] = orb.create_any();
		value[0].insert_string(fromPort.toString());
		value[1] = orb.create_any();
		value[1].insert_string(toPort.toString().substring(Punkt + 1));
		value[2] = orb.create_any();
		value[2].insert_string(message);

		invocation("unmap", cmp1, cmp, parameter, value, tim);
	}

	/**
	 * Log the reset operation.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param message
	 *            an additional message to this event.
	 */
	public void logReset(long time, Object source, String message) {
	}

	/**
	 * Log the execute test case request called in TciCHRequired
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param testComponentId
	 *            the component id where to execute the test case.
	 * @param tsiPortList
	 *            a list of the system ports.
	 * @param message
	 *            an additional message to this event.
	 */
	public void logExecuteTestCase(long time, Object source,
			TciTestCaseId TestCaseId, TriPortIdList tsiPortList, String message) {
		long tim = time;
		System.out.println("ExeTestCase");

		IdentityDescriptor cmp = mtc;

		Parameter[] parameter = new Parameter[] { new Parameter("in", "string",
				"TESTCASE") };

		Any[] value = new Any[1];
		value[0] = orb.create_any();
		value[0].insert_string(TestCaseId.baseName());

		invocation3("ExecuteTestCase", cmp, cmp, parameter, value, tim);
	}

	/**
	 * Log the sending of a message over a system port
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param componentId
	 *            identifier of the sending test component.
	 * @param tsiPortId
	 *            identifier of the test system interface port via which the
	 *            message is sent to the SUT Adapter.
	 * @param address
	 *            (optional) destination address within the SUT
	 * @param mes
	 *            the message before the encoding
	 * @param sendMessage
	 *            the encoded message to be send
	 * @param message
	 *            an additional message to this event.
	 */

	public void logSendMapped(long time, Object source,
			TriComponentId componentId, TriPortId tsiPortId,
			TriAddress address, TriPortId senderPortId, Value mes,
			TriMessage sendMessage, String message) {
		System.out.println("logSendMapped");
		long tim = time;

		int Punkt2 = componentId.getComponentId().toString().lastIndexOf('.');
		String componentIdName = "";
		componentIdName = componentId.getComponentId().toString().substring(
				Punkt2 + 1);

		IdentityDescriptor cmp1 = (IdentityDescriptor) components
				.get(componentId.getComponentId());

		String CompType = "";
		int Punkt = tsiPortId.getComponent().getComponentTypeName()
				.indexOf('.');
		CompType = tsiPortId.getComponent().getComponentTypeName().substring(
				Punkt + 1);

		String SUTport = "";

		SUTport = tsiPortId.getPortName()  + tsiPortId.getPortIndex();

		IdentityDescriptor cmp = createObject(appl_id, container_id,
				CompType, SUTport);
		components.put(SUTport, cmp);

		Parameter[] parameter = new Parameter[] {
				new Parameter("in", "string", "componentId"),
				new Parameter("in", "string", "tsiPortId"),
				new Parameter("in", "string", "sendMessage"),
				new Parameter("in", "string", "message") };

		Any[] value = new Any[4];
		value[0] = orb.create_any();
		value[0].insert_string(componentIdName);
		value[1] = orb.create_any();
		value[1].insert_string(SUTport);
		value[2] = orb.create_any();
		value[2].insert_string(sendMessage.toString());
		value[3] = orb.create_any();
		value[3].insert_string(message);

		invocation2("sendMapped", cmp1, cmp, parameter, value, tim);
	}

	/**
	 * Log the calling of a signature over a system port
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param componentId
	 *            identifier of the test component issuing the procedure call.
	 * @param tsiPortId
	 *            identifier of the test system interface port via which the
	 *            procedure call is sent to the SUT Adapter.
	 * @param sutAddress
	 *            (optional) destination address within the SUT.
	 * @param signatureId
	 *            identifier of the signature of the procedure call.
	 * @param parameterList
	 *            a list of encoded parameters which are part of the indicated
	 *            signature.
	 * @param message
	 *            an additional message to this event.
	 */
	public void logCallMapped(long time, Object source,
			TriComponentId componentId, TriPortId tsiPortId,
			TriAddress sutAddress, TriSignatureId signatureId,
			TriParameterList parameterList, String message) {
		System.out.println("logCallMapped");
		long tim = time;
		IdentityDescriptor cmp1 = (IdentityDescriptor) components
				.get(componentId.getComponentId());
		IdentityDescriptor cmp2 = (IdentityDescriptor) components.get(tsiPortId
				.getComponent().getComponentId());
		String CompType = "";
		int Punkt = tsiPortId.getComponent().getComponentTypeName()
				.indexOf('.');
		CompType = tsiPortId.getComponent().getComponentTypeName().substring(
				Punkt + 1);

		String SUTport = "";

		SUTport = tsiPortId.getPortName()  + tsiPortId.getPortIndex();

		IdentityDescriptor cmp = createObject(appl_id, container_id,
				CompType, SUTport);
		components.put(SUTport, cmp);

		Parameter[] parameter = new Parameter[] {
				new Parameter("in", "string", "componentId"),
				new Parameter("in", "string", "tsiPortId"),
				new Parameter("in", "string", "parameterList"),
				new Parameter("in", "string", "message") };

		Any[] value = new Any[4];
		value[0] = orb.create_any();
		value[0].insert_string(componentId.getComponentId());
		value[1] = orb.create_any();
		value[1].insert_string(SUTport);
		value[2] = orb.create_any();
		value[2].insert_string(parameterList.toString());
		value[3] = orb.create_any();
		value[3].insert_string(message);

		invocation("CallMapped", cmp, cmp1, parameter, value, tim);
	}

  /**
   * Log the calling of a signature over a system port
   *
   * @param time the time when the event occurs.
   * @param source the object which generates the event.
   * @param componentId identifier of the test component issuing 
   * the procedure call.
   * @param tsiPortId identifier of the test system interface port via 
   * which the procedure call is sent to the SUT Adapter.
   * @param sutAddress (optional) destination address within the SUT.
   * @param sourcePortId identifier of the source port.
   * @param signatureId identifier of the signature of the procedure call.
   * @param parameters	the parameter list as an array of TCI Values.
   * @param parameterList a list of encoded parameters which are part 
   * 			of the indicated signature. 
   * @param message an additional message to this event.
   */
  public void logCallMapped(long time, Object source, TriComponentId componentId, TriPortId tsiPortId, TriAddress sutAddress, TriPortId sourcePortId, TriSignatureId signatureId, Value[] parameters, TriParameterList parameterList, String message) {
    // TODO Auto-generated method stub
    
  }


	
	/**
	 * Log the replying of a call over a system port
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param componentId
	 *            identifier of the replying test component.
	 * @param tsiPortId
	 *            identifier of the test system interface port via which the
	 *            reply is sent to the SUT Adapter.
	 * @param sutAddress
	 *            (optional) destination address within the SUT.
	 * @param signatureId
	 *            identifier of the signature of the procedure call.
	 * @param parameterList
	 *            a list of encoded parameters which are part of the indicated
	 *            signature.
	 * @param returnValue
	 *            (optional) encoded return value of the procedure call.
	 * @param message
	 *            an additional message to this event.
	 */
	public void logReplyMapped(long time, Object source,
			TriComponentId componentId, TriPortId tsiPortId,
			TriAddress sutAddress, TriSignatureId signatureId,
			TriParameterList parameterList, TriParameter returnValue,
			String message) {
		System.out.println("logReplyMapped");
		long tim = time;
		IdentityDescriptor cmp1 = (IdentityDescriptor) components
				.get(componentId.getComponentId());
		IdentityDescriptor cmp2 = (IdentityDescriptor) components.get(tsiPortId
				.getComponent().getComponentId());
		String CompType = "";
		int Punkt = tsiPortId.getComponent().getComponentTypeName()
				.indexOf('.');
		CompType = tsiPortId.getComponent().getComponentTypeName().substring(
				Punkt + 1);

		String SUTport = "";

		SUTport = tsiPortId.getPortName()  + tsiPortId.getPortIndex();

		IdentityDescriptor cmp = createObject(appl_id, container_id,
				CompType, SUTport);
		components.put(SUTport, cmp);

		Parameter[] parameter = new Parameter[] {
				new Parameter("in", "string", "parameterList"),
				new Parameter("in", "string", "returnValue"),
				new Parameter("in", "string", "MESSAGE") };

		Any[] value = new Any[3];
		value[0] = orb.create_any();
		value[0].insert_string(new String(parameterList.toString()));
		value[1] = orb.create_any();
		value[1].insert_string(new String(returnValue.toString()));
		value[2] = orb.create_any();
		value[2].insert_string(new String(message));
		invocation("ReplyMapped", cmp, cmp1, parameter, value, tim);
	}

  /**
   * Log the replying of a call over a system port
   *
   * @param time the time when the event occurs.
   * @param source the object which generates the event.
   * @param componentId identifier of the replying test component.
   * @param tsiPortId identifier of the test system interface port 
   * via which the reply is sent to the SUT Adapter.
   * @param sutAddress (optional) destination address within the SUT.
   * @param sourcePortId identifier of the source port.
   * @param signatureId identifier of the signature of the procedure call.
   * @param parameters	the parameter list as an array of TCI Values.
   * @param parameterList a list of encoded parameters which are 
   * part of the indicated signature.
   * @param tciReturnValue the return value as TCI Value.
   * @param returnValue (optional) encoded return value of the procedure call.
   * @param message an additional message to this event.
   */
  public void logReplyMapped(long time, Object source, TriComponentId componentId, TriPortId tsiPortId, TriAddress sutAddress, TriPortId sourcePortId, TriSignatureId signatureId, Value[] parameters, TriParameterList parameterList, Value tciReturnValue, TriParameter returnValue, String message) {
    // TODO Auto-generated method stub
    
  }

	/**
	 * Log a raise of a system port
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param componentId
	 *            identifier of the test component raising the exception.
	 * @param tsiPortId
	 *            identifier of the test system interface port via which the
	 *            exception is sent to the SUT Adapter.
	 * @param sutAddress
	 *            (optional) destination address within the SUT.
	 * @param signatureId
	 *            identifier of the signature of the procedure call which the
	 *            exception is associated with
	 * @param exception
	 *            the encoded exception
	 * @param message
	 *            an additional message to this event.
	 */
	public void logRaiseMapped(long time, Object source,
			TriComponentId componentId, TriPortId tsiPortId,
			TriAddress sutAddress, TriSignatureId signatureId,
			TriException exception, String message) {
		System.out.println("logRaiseMapped");
		long tim = time;
		IdentityDescriptor cmp1 = (IdentityDescriptor) components
				.get(componentId.getComponentId());
		IdentityDescriptor cmp2 = (IdentityDescriptor) components.get(tsiPortId
				.getComponent().getComponentId());
		String CompType = "";
		int Punkt = tsiPortId.getComponent().getComponentTypeName()
				.indexOf('.');
		CompType = tsiPortId.getComponent().getComponentTypeName().substring(
				Punkt + 1);

		String SUTport = "";

		SUTport = tsiPortId.getPortName()  + tsiPortId.getPortIndex();

		IdentityDescriptor cmp = createObject(appl_id, container_id,
				CompType, SUTport);
		components.put(SUTport, cmp);

		Parameter[] parameter = new Parameter[] {
				new Parameter("in", "string", "exception"),
				new Parameter("in", "string", "MESSAGE") };

		Any[] value = new Any[3];
		value[0] = orb.create_any();
		value[0].insert_string(new String(exception.toString()));
		value[1] = orb.create_any();
		value[1].insert_string(new String(message));
		invocation("RaiseMapped", cmp, cmp1, parameter, value, tim);
	}

  /**
   * Log a raise of a system port
   *
   * @param time the time when the event occurs.
   * @param source the object which generates the event.
   * @param componentId identifier of the test component raising the exception.
   * @param tsiPortId identifier of the test system interface port 
   * via which the exception is sent to the SUT Adapter.
   * @param sutAddress (optional) destination address within the SUT.
   * @param sourcePortId identifier of the source port.
   * @param signatureId identifier of the signature of the procedure call 
   * which the exception is associated with
   * @param tciValueException the exception as TCI Value.
   * @param exception the encoded exception
   * @param message an additional message to this event.
   */
  public void logRaiseMapped(long time, Object source,
      TriComponentId componentId, TriPortId tsiPortId, TriAddress sutAddress,
      TriPortId sourcePortId, TriSignatureId signatureId,
      Value tciValueException, TriException exception, String message) {
    // TODO Auto-generated method stub

  }

	/**
	 * Log the starting of timers.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param timerId
	 *            identifier of the timer instance
	 * @param timerDuration
	 *            duration of the timer in seconds
	 * @param message
	 *            an additional message to this event.
	 */
	public void logStartTimer(long time, Object source, TriTimerId timerId,
			TriTimerDuration timerDuration, String message) {
		System.out.println("logStartTimer");
		long tim = time;
		String TimCompN = "";

		int Punkt = timerId.toString().indexOf('.');

		TimCompN = timerId.toString().substring(0, Punkt);
		IdentityDescriptor cmp = (IdentityDescriptor) components.get(TimCompN);

		Parameter[] parameter = new Parameter[] {
				new Parameter("in", "string", "timerId"),
				new Parameter("in", "string", "timerDuration"),
				new Parameter("in", "string", "message") };

		Any[] value = new Any[3];
		value[0] = orb.create_any();
		value[0].insert_string(timerId.toString());
		value[1] = orb.create_any();
		value[1].insert_string(timerDuration.toString());
		value[2] = orb.create_any();
		value[2].insert_string(message);

		invocation("StartTimer", cmp, cmp, parameter, value, tim);
	}

	/**
	 * Log the stop of the timer.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param timerId
	 *            identifier of the timer instance
	 * @param duration
	 *            the time the timer was running
	 * @param message
	 *            an additional message to this event.
	 */
	public void logStopTimer(long time, Object source, TriTimerId timerId,
			TriTimerDuration duration, String message) {
		System.out.println("logStopTimer");
		long tim = time;
		String TimCompN = "";

		int Punkt = timerId.toString().indexOf('.');

		TimCompN = timerId.toString().substring(0, Punkt);

		IdentityDescriptor cmp = (IdentityDescriptor) components.get(TimCompN);

		Parameter[] parameter = new Parameter[] {
				new Parameter("in", "string", "timerId"),
				new Parameter("in", "string", "timerDuration"),
				new Parameter("in", "string", "message") };

		Any[] value = new Any[3];
		value[0] = orb.create_any();
		value[0].insert_string(timerId.toString());
		value[1] = orb.create_any();
		value[1].insert_string(duration.toString());
		value[2] = orb.create_any();
		value[2].insert_string(message);

		invocation("StopTimer", cmp, cmp, parameter, value, tim);
	}

	/**
	 * Log the timer timeout events.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param timerId
	 *            identifier of the timer instance
	 * @param message
	 *            an additional message to this event.
	 */
	public void logTimeout(long time, Object source, TriTimerId timerId,
			String message) {
		System.out.println("logTimeout");
		long tim = time;
		String TimCompN = "";

		int Punkt = timerId.toString().indexOf('.');

		TimCompN = timerId.toString().substring(0, Punkt);
		IdentityDescriptor cmp = (IdentityDescriptor) components.get(TimCompN);

		Parameter[] parameter = new Parameter[] {
				new Parameter("in", "string", "timerId"),
				new Parameter("in", "string", "message") };

		Any[] value = new Any[2];
		value[0] = orb.create_any();
		value[0].insert_string(timerId.toString());
		value[1] = orb.create_any();
		value[1].insert_string(message);

		invocation2("Timeout", cmp, cmp, parameter, value, tim);
	}

	/**
	 * Log the receiving of a message.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param sutAddress
	 *            (optional) source address within the SUT
	 * @param componentId
	 *            identifier of the receiving test component
	 * @param receivedMessage
	 *            the encoded received message
	 * @param message
	 *            an additional message to this event.
	 */
	public void logEnqueueMsg(long time, Object source, TriPortId tsiPortId,
			TriAddress sutAddress, TriComponentId componentId,
			TriMessage receivedMessage, String message) {
		System.out.println("logEnqueueMsg");
		long tim = time;
		IdentityDescriptor cmp2 = (IdentityDescriptor) components
				.get(componentId.getComponentId());

		String CompType = "";
		int Punkt = tsiPortId.getComponent().getComponentTypeName()
				.indexOf('.');
		CompType = tsiPortId.getComponent().getComponentTypeName().substring(
				Punkt + 1);

		String SUTport = "";

		SUTport = tsiPortId.getPortName()  + tsiPortId.getPortIndex();

		IdentityDescriptor cmp = createObject(appl_id, container_id,
				CompType, SUTport);
		components.put(SUTport, cmp);

		Parameter[] parameter = new Parameter[] {
				new Parameter("in", "string", "tsiPortId"),
				new Parameter("in", "string", "componentId"),
				new Parameter("in", "string", "sutAddressEncoded"),
				new Parameter("in", "string", "sutAddress"),
				new Parameter("in", "string", "receivedMESSAGE") };

		Any[] value = new Any[5];
		value[0] = orb.create_any();
		value[0].insert_string(new String(SUTport));
		value[1] = orb.create_any();
		value[1].insert_string(componentId.toString());
		value[2] = orb.create_any();
		value[2].insert_string(new String(sutAddress.getEncodedAddress()
				.toString()));
		value[3] = orb.create_any();
		value[3].insert_string(new String(sutAddress.toString()));
		value[4] = orb.create_any();
		value[4].insert_string(receivedMessage.toString());

		invocation3("enqueueMsg", cmp2, cmp, parameter, value, tim);
	}

	/**
	 * Log the receiving of a call.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param tsiPortId
	 *            identifier of the test system interface port via which the
	 *            procedure call is enqueued by the SUT Adapter
	 * @param SUTaddress
	 *            (optional) source address within the SUT
	 * @param componentId
	 *            identifier of the receiving test component
	 * @param signatureId
	 *            identifier of the signature of the procedure call
	 * @param parameterList
	 *            a list of encoded parameters which are part of the indicated
	 *            signature. The parameters in parameterList are ordered as they
	 *            appear in the TTCN-3 signature declaration.
	 * @param message
	 *            an additional message to this event.
	 */
	public void logEnqueueCall(long time, Object source, TriPortId tsiPortId,
			TriAddress SUTaddress, TriComponentId componentId,
			TriSignatureId signatureId, TriParameterList parameterList,
			String message) {
		System.out.println("logEnqueueCall");
		long tim = time;
		IdentityDescriptor cmp1 = (IdentityDescriptor) components.get(tsiPortId
				.getComponent().getComponentId());
		IdentityDescriptor cmp2 = (IdentityDescriptor) components
				.get(componentId.getComponentId());

		String CompType = "";
		int Punkt = tsiPortId.getComponent().getComponentTypeName()
				.indexOf('.');
		CompType = tsiPortId.getComponent().getComponentTypeName().substring(
				Punkt + 1);

		String SUTport = "";

		SUTport = tsiPortId.getPortName()  + tsiPortId.getPortIndex();

		IdentityDescriptor cmp = createObject(appl_id, container_id,
				CompType, SUTport);
		components.put(SUTport, cmp);

		Parameter[] parameter = new Parameter[] {
				new Parameter("in", "string", "parameterList"),
				new Parameter("in", "string", "MESSAGE") };

		Any[] value = new Any[2];
		value[0] = orb.create_any();
		value[0].insert_string(new String(parameterList.toString()));
		value[1] = orb.create_any();
		value[1].insert_string(new String(message));
		invocation3("enqueueCall", cmp2, cmp, parameter, value, tim);
	}

	/**
	 * Log the reply operations.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param tsiPortId
	 *            identifier of the test system interface port via which the
	 *            reply is enqueued by the SUT Adapter
	 * @param address
	 *            (optional) source address within the SUT
	 * @param componentId
	 *            identifier of the receiving test component
	 * @param signatureId
	 *            identifier of the signature of the procedure call
	 * @param parameterList
	 *            a list of encoded parameters which are part of the indicated
	 *            signature. The parameters in parameterList are ordered as they
	 *            appear in the TTCN-3 signature declaration.
	 * @param returnValue
	 *            (optional) encoded return value of the procedure call
	 * @param message
	 *            an additional message to this event.
	 */
	public void logEnqueueReply(long time, Object source, TriPortId tsiPortId,
			TriAddress address, TriComponentId componentId,
			TriSignatureId signatureId, TriParameterList parameterList,
			TriParameter returnValue, String message) {
		System.out.println("logEnqueueReply");
		long tim = time;
		IdentityDescriptor cmp1 = (IdentityDescriptor) components.get(tsiPortId
				.getComponent().getComponentId());
		IdentityDescriptor cmp2 = (IdentityDescriptor) components
				.get(componentId.getComponentId());

		String CompType = "";
		int Punkt = tsiPortId.getComponent().getComponentTypeName()
				.indexOf('.');
		CompType = tsiPortId.getComponent().getComponentTypeName().substring(
				Punkt + 1);

		String SUTport = "";

		SUTport = tsiPortId.getPortName()  + tsiPortId.getPortIndex();

		IdentityDescriptor cmp = createObject(appl_id, container_id,
				CompType, SUTport);
		components.put(SUTport, cmp);

		Parameter[] parameter = new Parameter[] {
				new Parameter("in", "string", "parameterList"),
				new Parameter("in", "string", "returnValue"),
				new Parameter("in", "string", "MESSAGE") };

		Any[] value = new Any[3];
		value[0] = orb.create_any();
		value[0].insert_string(new String(parameterList.toString()));
		value[1] = orb.create_any();
		value[1].insert_string(new String(returnValue.toString()));
		value[2] = orb.create_any();
		value[2].insert_string(new String(message));
		invocation3("EnqueueReply", cmp2, cmp, parameter, value, tim);
	}

	/**
	 * Log test exceptions.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param tsiPortId
	 *            identifier for the test system interface port via which the
	 *            exception is enqueued by the SUT Adapter
	 * @param sutAddress
	 *            (optional) source address within the SUT
	 * @param signatureId
	 *            identifier of the signature of the procedure call which the
	 *            exception is associated with
	 * @param exception
	 *            the encoded exception
	 * @param message
	 *            an additional message to this event.
	 */
	public void logEnqueueException(long time, Object source,
			TriPortId tsiPortId, TriAddress sutAddress,
			TriComponentId componentId, TriSignatureId signatureId,
			TriException exception, String message) {
		System.out.println("logEnqueueException");
		long tim = time;
		IdentityDescriptor cmp1 = (IdentityDescriptor) components.get(tsiPortId
				.getComponent().getComponentId());
		IdentityDescriptor cmp2 = (IdentityDescriptor) components
				.get(componentId.getComponentId());

		String CompType = "";
		int Punkt = tsiPortId.getComponent().getComponentTypeName()
				.indexOf('.');
		CompType = tsiPortId.getComponent().getComponentTypeName().substring(
				Punkt + 1);

		String SUTport = "";
		SUTport = tsiPortId.getPortName()  + tsiPortId.getPortIndex();

		IdentityDescriptor cmp = createObject(appl_id, container_id,
				CompType, SUTport);
		components.put(SUTport, cmp);

		Parameter[] parameter = new Parameter[] {
				new Parameter("in", "string", "exception"),
				new Parameter("in", "string", "returnValue"),
				new Parameter("in", "string", "MESSAGE") };

		Any[] value = new Any[2];
		value[0] = orb.create_any();
		value[0].insert_string(new String(exception.toString()));
		value[1] = orb.create_any();
		value[1].insert_string(new String(message));

		invocation3("EnqueueException", cmp2, cmp, parameter, value, tim);
	}

	/**
	 * Log the external function calls.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param functionId
	 *            identifier of the external function
	 * @param parameterList
	 *            a list of encoded parameters for the indicated function. The
	 *            parameters in parameterList are ordered as they appear in the
	 *            TTCN-3 function declaration.
	 * @param returnValue
	 *            (optional) encoded return value
	 * @param message
	 *            an additional message to this event.
	 */
	public void logExternalFunction(long time, Object source,
			TriFunctionId functionId, TriParameterList parameterList,
			TriParameter returnValue, String message) {
		System.out.println("logExternalFunction");
	}

	/**
	 * This method is used within the test adapter to log different actions.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param message
	 *            the message to be logged.
	 */
	public void logTestAdapter(long time, Object source, String message) {
		System.out.println("logTestAdapter");
	}

	/**
	 * This method is used within the Codec to log actions in the encoding.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param message
	 *            the message to be logged.
	 */
	public void logEncode(long time, Object source, String message) {
		System.out.println("logEncode");
	}

	/**
	 * This method is used within the Codec to log actions in the decoding.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param message
	 *            the message to be logged.
	 */
	public void logDecode(long time, Object source, String message) {
		System.out.println("logDecode");

	}

	/**
	 * Log the events generated when errors occur.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param message
	 *            the error message.
	 */
	public void logError(long time, Object source, String message) {
		System.out.println("logError");
	}

	/**
	 * Log the debug events.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param message
	 *            the debug message.
	 */
	public void logDebug(long time, Object source, String message) {
		System.out.println("logDebug");
	}

	/**
	 * Log different events in TE.
	 * 
	 * @param time
	 *            the time when the event occurs.
	 * @param source
	 *            the object which generates the event.
	 * @param message
	 *            the statement.
	 */
	public void logEvent(long time, Object source, String message) //, int cnt)
	{
		System.out.println("logEvent");
	}

	public void invocation(String operation_name, IdentityDescriptor from,
			IdentityDescriptor to, Parameter[] parameters, Any[] values,
			long timer) {
		/*
		 * thread of execution for a given interaction point. This must be a
		 * unique id meaning that you must be able to discriminate between
		 * threads with the same id running on two different machines for
		 * example
		 */
		String exec_thread = "";

		try {
			exec_thread = Thread.currentThread().getName() + "_"
					+ InetAddress.getLocalHost().getHostName();
		} catch (Exception _ex) {
			//			_ex.printStackTrace();
			return;
		}

		/*
		 * trail id - every trail (sequence of causaly related events) has a
		 * starting point identified by the trail id.
		 */
		String trail_id = new Integer(event_counter).toString();

		/*
		 * message id - each invocation has a unique message id to associate the
		 * invoker with the receiver.
		 */
		String message_id = trail_id + "_" + event_counter + "_";

		// create event for STUB_OUT

		/*
		 * The traceserver orders events per process using the process event
		 * counter. Events with the same message id are associated with each
		 * other and form a message between invoker and receiver. The trace
		 * server calculates the relative ordering of events using the message
		 * id, event counter and trail id. All events with the same trail id are
		 * causaly related to each other. The traceserver maintains a total
		 * ordering of events using the exact relative ordering of causaly
		 * related events and physical timestamp comparison for causaly
		 * unrelated event.
		 */
		TraceEvent stub_out_event = new TraceEvent(timer, /* physical time */
		InteractionPoint.ONEWAY_STUB_OUT, /* interaction point */
		"", /*
			 * trail label - user defined label to mark a trail segment, not
			 * used by the trace viewer yet (giving trail segments a different
			 * color for example
			 */
		message_id + "F", /*
						   * The message id to associate the originator with the
						   * receiver of the invocation
						   */
		exec_thread, /* thread of execution */
		trail_id, /* trail id */
		event_counter++, /*
						  * global event counter per process, incremented on
						  * every send and receive event
						  */
		operation_name, /* operation name */
		from, /* object that initiated the invocation */
		parameters, /* parameters (dir, type, name) */
		values); /* parameter values */

		// create event for POA_IN
		TraceEvent poa_in_event = new TraceEvent(timer, /* physical time */
		InteractionPoint.ONEWAY_POA_IN, /* interaction point */
		"", /*
			 * trail label - user defined label to mark a trail segment, not
			 * used by the trace viewer yet (giving trail segments a different
			 * color for example
			 */
		message_id + "F", /*
						   * The message id to associate the originator with the
						   * receiver of the invocation
						   */
		exec_thread, /*
					  * simulating this is a collocated call (same thread of
					  * execution as the stub
					  */
		trail_id, /* trail id */
		event_counter++, /*
						  * logical clock incremented for every interaction
						  * point along the trail
						  */
		operation_name, /* operation name */
		to, /* object that receives invocation */
		parameters, /* parameters (dir, type, name) */
		values); /* parameter values */

		TraceEvent[] events = new TraceEvent[2];

		events[0] = stub_out_event;
		events[1] = poa_in_event;

		try {
			connection.receiveEvent(events);
		} catch (Exception ex) {

		}
	}

	public void invocation3(String operation_name, IdentityDescriptor from,
			IdentityDescriptor to, Parameter[] parameters, Any[] values,
			long timer) {
		/*
		 * thread of execution for a given interaction point. This must be a
		 * unique id meaning that you must be able to discriminate between
		 * threads with the same id running on two different machines for
		 * example
		 */
		String exec_thread = "";

		try {
			exec_thread = Thread.currentThread().getName() + "_"
					+ InetAddress.getLocalHost().getHostName();
		} catch (Exception _ex) {
			//			_ex.printStackTrace();
			System.out.println("Logging Server is not responding");
			return;
		}

		/*
		 * trail id - every trail (sequence of causaly related events) has a
		 * starting point identified by the trail id.
		 */
		String trail_id = new Integer(event_counter).toString();

		/*
		 * message id - each invocation has a unique message id to associate the
		 * invoker with the receiver.
		 */
		String message_id = trail_id + "_" + event_counter + "_";

		// create event for STUB_OUT
		TraceEvent poa_out_event = new TraceEvent(timer, /* physical time */
		InteractionPoint.POA_OUT, /* interaction point */
		"", /*
			 * trail label - user defined label to mark a trail segment, not
			 * used by the trace viewer yet (giving trail segments a different
			 * color for example
			 */
		message_id + "B", /*
						   * The message id to associate the originator with the
						   * receiver of the invocation
						   */
		exec_thread, /*
					  * simulating this is a collocated call (same thread of
					  * execution as the stub
					  */
		trail_id, /* trail id */
		event_counter++, /*
						  * logical clock incremented for every interaction
						  * point along the trail
						  */
		operation_name, /* operation name */
		to, /* object that receives invocation */
		parameters, /* parameters (dir, type, name) */
		values); /* parameter values */

		// create event for STUB_IN
		TraceEvent stub_in_event = new TraceEvent(timer, /* physical time */
		InteractionPoint.STUB_IN, /* interaction point */
		"", /*
			 * trail label - user defined label to mark a trail segment, not
			 * used by the trace viewer yet (giving trail segments a different
			 * color for example
			 */
		message_id + "B", /*
						   * The message id to associate the originator with the
						   * receiver of the invocation
						   */
		exec_thread, /*
					  * simulating this is a collocated call (same thread of
					  * execution as the skeleton
					  */
		trail_id, /* trail id */
		event_counter++, /*
						  * logical clock incremented for every interaction
						  * point along the trail
						  */
		operation_name, /* operation name */
		from, /* object that receives invocation */
		parameters, /* parameters (dir, type, name) */
		values); /* parameter values */

		// a oneway invocation contains two event - ONEWAY_STUB_OUT and
		// ONEWAY_POA_IN
		TraceEvent[] events = new TraceEvent[2];

		events[0] = stub_in_event;
		events[1] = poa_out_event;

		try {
			connection.receiveEvent(events);
		} catch (java.lang.Exception ex) {
			//System.out.println("Logging Server is not responding");
			return;
		}

		return;
	}

	public void invocation2(String operation_name, IdentityDescriptor from,
			IdentityDescriptor to, Parameter[] parameters, Any[] values,
			long timer) {
		String exec_thread = "";

		try {
			exec_thread = Thread.currentThread().getName() + "_"
					+ InetAddress.getLocalHost().getHostName();
		} catch (Exception _ex) {
			//		    System.out.println("Logging Server is not responding");
			//			_ex.printStackTrace();
			return;
		}

		/*
		 * trail id - every trail (sequence of causaly related events) has a
		 * starting point identified by the trail id.
		 */
		String trail_id = new Integer(event_counter).toString();

		/*
		 * message id - each invocation has a unique message id to associate the
		 * invoker with the receiver.
		 */
		String message_id = trail_id + "_" + event_counter + "_";

		// create event for STUB_OUT
		TraceEvent stub_out_event = new TraceEvent(timer, /* physical time */
		InteractionPoint.STUB_OUT, /* interaction point */
		"", /*
			 * trail label - user defined label to mark a trail segment, not
			 * used by the trace viewer yet (giving trail segments a different
			 * color for example
			 */
		message_id + "F", /*
						   * The message id to associate the originator with the
						   * receiver of the invocation
						   */
		exec_thread, /* thread of execution */
		trail_id, /* trail id */
		event_counter++, /*
						  * global event counter per process, incremented on
						  * every send and receive event
						  */
		operation_name, /* operation name */
		from, /* object that initiated the invocation */
		parameters, /* parameters (dir, type, name) */
		values); /* parameter values */

		// create event for POA_IN
		TraceEvent poa_in_event = new TraceEvent(timer, /* physical time */
		InteractionPoint.POA_IN, /* interaction point */
		"", /*
			 * trail label - user defined label to mark a trail segment, not
			 * used by the trace viewer yet (giving trail segments a different
			 * color for example
			 */
		message_id + "F", /*
						   * The message id to associate the originator with the
						   * receiver of the invocation
						   */
		exec_thread, /*
					  * simulating this is a collocated call (same thread of
					  * execution as the stub
					  */
		trail_id, /* trail id */
		event_counter++, /*
						  * logical clock incremented for every interaction
						  * point along the trail
						  */
		operation_name, /* operation name */
		to, /* object that receives invocation */
		parameters, /* parameters (dir, type, name) */
		values); /* parameter values */

		TraceEvent[] events = new TraceEvent[2];

		events[0] = stub_out_event;
		events[1] = poa_in_event;

		try {
			connection.receiveEvent(events);
		} catch (java.lang.Exception ex) {
			System.out.println("here3");
			//		    System.out.println("Logging Server is not responding");
			return;
		}
	}

	public IdentityDescriptor createObject(String process_id,
			String container_name, String component_name, String object_name) {
		// used to describe a sending or receiving entity such as a facet
		// instance, component instance etc.
		IdentityDescriptor identity = new IdentityDescriptor();

		try {
			identity.process_id = process_id;
			identity.node_name = InetAddress.getLocalHost().getHostName();
			identity.node_ip = InetAddress.getLocalHost().getHostAddress();
			identity.object_instance_id = object_name;
			identity.object_repository_id = "facet";
			identity.cmp_name = component_name;
			identity.cmp_type = "component";
			identity.cnt_name = container_name;
			identity.cnt_type = "container";
		} catch (Exception _ex) {
			_ex.printStackTrace();
		}

		return identity;
	}

	public void logMatchedSender(long time, Object source,
			TriPortId receiverPortId, Value sender, Value expectedSender,
			String message) {
		String logMessage = "sender '" + sender + "' matched the expected one";
		//	queue.push(new Log(this, logMessage, time));
	}

	public void logUnmatchedSender(long time, Object source,
			TriPortId tsiPortId, Value sender, Value expectedSender,
			String message) {
		String logMessage = "sender '" + sender
				+ "' did not match the expected one '" + expectedSender + "'";
		//	queue.push(new Log(this, logMessage, time));
	}

	public void invocation4(String operation_name, IdentityDescriptor from,
			IdentityDescriptor to, Parameter[] parameters, Any[] values,
			long timer) {
		/*
		 * thread of execution for a given interaction point. This must be a
		 * unique id meaning that you must be able to discriminate between
		 * threads with the same id running on two different machines for
		 * example
		 */
		String exec_thread = "";

		try {
			exec_thread = Thread.currentThread().getName() + "_"
					+ InetAddress.getLocalHost().getHostName();
		} catch (Exception _ex) {
			_ex.printStackTrace();
		}

		/*
		 * trail id - every trail (sequence of causaly related events) has a
		 * starting point identified by the trail id.
		 */
		String trail_id = new Integer(event_counter).toString();

		/*
		 * message id - each invocation has a unique message id to associate the
		 * invoker with the receiver.
		 */
		String message_id = trail_id + "_" + event_counter + "_";

		// create event for STUB_OUT

		/*
		 * The traceserver orders events per process using the process event
		 * counter. Events with the same message id are associated with each
		 * other and form a message between invoker and receiver. The trace
		 * server calculates the relative ordering of events using the message
		 * id, event counter and trail id. All events with the same trail id are
		 * causaly related to each other. The traceserver maintains a total
		 * ordering of events using the exact relative ordering of causaly
		 * related events and physical timestamp comparison for causaly
		 * unrelated event.
		 */
		TraceEvent stub_out_event = new TraceEvent(timer, /* physical time */
		InteractionPoint.ONEWAY_STUB_OUT, /* interaction point */
		"", /*
			 * trail label - user defined label to mark a trail segment, not
			 * used by the trace viewer yet (giving trail segments a different
			 * color for example
			 */
		message_id + "F", /*
						   * The message id to associate the originator with the
						   * receiver of the invocation
						   */
		exec_thread, /* thread of execution */
		trail_id, /* trail id */
		event_counter++, /*
						  * global event counter per process, incremented on
						  * every send and receive event
						  */
		operation_name, /* operation name */
		from, /* object that initiated the invocation */
		parameters, /* parameters (dir, type, name) */
		values /* parameter values */
		); /* time of event */

		// create event for POA_IN
		TraceEvent poa_in_event = new TraceEvent(timer, /* physical time */
		InteractionPoint.ONEWAY_POA_IN, /* interaction point */
		"", /*
			 * trail label - user defined label to mark a trail segment, not
			 * used by the trace viewer yet (giving trail segments a different
			 * color for example
			 */
		message_id + "F", /*
						   * The message id to associate the originator with the
						   * receiver of the invocation
						   */
		exec_thread, /*
					  * simulating this is a collocated call (same thread of
					  * execution as the stub
					  */
		trail_id, /* trail id */
		event_counter++, /*
						  * logical clock incremented for every interaction
						  * point along the trail
						  */
		operation_name, /* operation name */
		to, /* object that receives invocation */
		parameters, /* parameters (dir, type, name) */
		values); /* parameter values */

		TraceEvent[] events = new TraceEvent[2];

		events[0] = stub_out_event;
		events[1] = poa_in_event;

		try {
			connection.receiveEvent(events);
		} catch (Exception ex) {

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.testingtech.ttcn.extension.Logging#getLogging()
	 */
	public TXILoggingInterface getLogging() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.testingtech.util.plugin.PluginInterface#initOptions()
	 */
	public void initOptions() {
		// nothing to do
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.testingtech.util.plugin.PluginInterface#setMessageHandler(com.testingtech.util.logging.MessageHandler)
	 */
	public void setMessageHandler(MessageHandler handler) {
		// nothing to do
	}
}