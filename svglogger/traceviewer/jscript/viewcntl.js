
/*
###########################################################################
# COACH: Component Based Open Source Architecture for                     #
#        Distributed Telecom Applications                                 #
# See:   http://www.objectweb.org/                                        #
#                                                                         #
# Copyright (C) 2003 Lucent Technologies Nederland BV                     #
#                    Bell Labs Advanced Technologies - EMEA               #
#                                                                         #
# Initial developer(s): Wim Hellenthal                                    #
# Further developer(s): Justyna Zander  2004                              #
# Fraunhofer Institut FOKUS                                               #
#                                                                         #
# This library is free software; you can redistribute it and/or           #
# modify it under the terms of the GNU Lesser General Public              #
# License as published by the Free Software Foundation; either            #
# version 2.1 of the License, or (at your option) any later version.      #
#                                                                         #
# This library is distributed in the hope that it will be useful,         #
# but WITHOUT ANY WARRANTY; without even the implied warranty of          #
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU        #
# Lesser General Public License for more details.                         #
#                                                                         #
# You should have received a copy of the GNU Lesser General Public        #
# License along with this library; if not, write to the Free Software     #
# Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA #
###########################################################################
*/

/**
 * This class represents a tree node within the entity/indentity hierarchy.
 */
function EntityNode (type, value, key)
{
	/**
	 * Tracing server database key.
	 */
	this.key = key;

	/**
	 * Node can be one of the following types:
	 *   DOMAIN
	 *   PLATFORM
	 *   SESSION
	 *   CONTAINER
	 *   COMPONENT_Type
	 *   COMPONENT_id
	 *
	 */
	this.type = type;

	/**
	 * Node name. The name for a DOMAIN node could be for example lucent.com
	 */
	this.value 		= value;

	/**
	 * Branch id for this node within the EntityManager tree.
	 */
	this.id			= null;

	/**
	 * Parent node which contains this node as a child.
	 */
	this.parent		= null;

	/**
	 * Flag indicating if node is expanded.
	 */
	this.expanded  	= true;

	/**
	 * Flag indicating if this node is visible.
	 */
	this.visible	= true;

	/**
	 * Children belonging to this node.
	 */
	this.children 	= new Array();
}

/**
 * Flag to filter out children of collapsed tree nodes.
 */
EntityNode.collapsedNodeChildren = 0x01;

/**
 * Flag to filter out expanded nodes that have children (not leafs).
 */
EntityNode.expandedNodesWithChildren = 0x02;

/**
 * Flag to filter out nodes that have children (not leafs).
 */
EntityNode.nodesWithChildren = 0x04;

/**
 * Fills an array that traverses the subtree rooted at the specified node in preorder.
 *
 * @param node The subtree to traverse.
 * @param filter The array content depends on the filter flags set.
 * @param array The array that will be filled with the traversed subtree.
 */
EntityNode.walk = function (node, filter, array)
{
	if ( (filter & EntityNode.nodesWithChildren) && (node.children.length == 0) )
		array[array.length] = node;

	for (var i in node.children)
	{
		if ( (filter & EntityNode.collapsedNodeChildren) && !node.children[i].expanded )
		{
			array[array.length] = node.children[i];
			continue;
		}

		EntityNode.walk (node.children[i], filter, array);

		if ( ((filter & EntityNode.expandedNodesWithChildren) && node.children[i].expanded && (node.children[i].children.length == 0)) || (filter==0))
			array[array.length] = node.children[i];
	}
}

/**
 * Appends specified node to this node's children array.
 *
 * @param node  Node instance to append.
 */
EntityNode.prototype.appendChild = function (node)
{
	this.children[this.children.length] = node;

	node.id	= this.id ?  this.id + '_' + this.children.length : this.children.length;
	node.parent = this;

	return node;
}

/**
 * Returns the first child node of this node's children array with the name that equals the specified value. The value
 * represents the name of the entity/identity type. For a node of type <b>CCM_NODE</b> the name could be for example
 * <b>chaos.lucent.com</b>
 *
 * @param value  Name of the node to retrieve.
 * @returns First child node or null if this node has no children that match the specified name.
 */
EntityNode.prototype.hasChild = function (value)
{
	for (var i in this.children)
		if ( this.children[i].value == value )
			return this.children[i];

	return null;
}

/**
 * Traverses the subtree rooted at the specified node to find all the leafs. If no node is specified then the
 * search starts at this node.
 *
 * @param node The subtree to traverse.
 * @returns An array containing all leaf nodes or null if the specified nodes has no children.
 */
EntityNode.prototype.getLeafs = function (node)
{
	var array = new Array();

	// filter out all nodes that have children

	EntityNode.walk (node ? node : this, EntityNode.nodesWithChildren, array);

	return array;
}

/**
 * This class implements the entity/identity hierarchy tree used by the SVGController to determine
 * the entity/identity SVG elements to create.
 *
 * @constructor
 */
function EntityManager()
{
}

EntityManager.prototype = new EntityNode('root');

/**
 * Returns all the entity/identity nodes visible to the user. Visible in this context means that a node must be either
 * collapsed or expanded in which case to node should have no children (leaf). For nodes that comply to the
 * previous requirements the <b>visible</b> flag must also be set (true). Note that children of collapsed nodes are
 * filtered out.
 *
 * Whether a node is collapsed/expanded and visible or not, is under control of the user by means of a HTML tree (Entity Control).
 * This class reflects the HTML tree state.
 *
 * @returns An array containing all visible entity/identity nodes.
 */
EntityManager.prototype.getVisibles = function ()
{
	var nodes = new Array();

	EntityNode.walk (this, EntityNode.collapsedNodeChildren | EntityNode.expandedNodesWithChildren, nodes);

	// visible flag must be set to true

	var visibles = new Array();

	for (var i in nodes)
	{
		if ( nodes[i].visible )
			visibles[visibles.length] = nodes[i];
	}

	return visibles;
}

/**
 * Returns all the leaf nodes that have the visible flag set false
 *
 * @returns Array containing the hidden leaf nodes.
 */
EntityManager.prototype.getHidden = function ()
{
	var hidden = new Array();
	var leafs  = this.getLeafs();

	for (var i=0; i < leafs.length; i++)
	{
		if ( !leafs[i].visible )
			hidden[hidden.length] = leafs[i].key;
	}
	return hidden;
}

/**
 * Returns an array containing all the entities that reside underneath collapsed parts of the entity/identity tree.
 * For every array item the following format is used to present the information:
 * <pre>
 * &ltentity key&gt:&ltentityNode.type&gt_&ltentityNode.value&gt
 * </pre>
 * The first part identifies the entity that is underneath a collapsed part of the entity/identity tree. The second part
 * identifies the node that is collapsed. If for example two entities with id=4 and id=8 are underneath the collapsed
 * CCM_DOMAIN node with name 'lucent.com' then the array content would look like follows (pseudo code):
 * <pre>
 * array = {4:CCM_DOMAIN_lucent.com, 8:CCM_DOMAIN_lucent.com}
 * </pre>
 * The information is used by the tracing server to identify the operations that have there begin and endpoint lying on the
 * same entity/identity line (operation to self) so that they can be filtered out if requested.
 *
 * @returns The leafs that reside underneath collapsed parts of the entity/identity tree.
 */
EntityManager.prototype.getCollapsed = function ()
{
	var nodes 		= new Array();
	var collapsed	= new Array();

	// walk the tree to find all the collapsed nodes

	EntityNode.walk (this, EntityNode.collapsedNodeChildren | EntityNode.expandedNodesWithChildren, nodes);

	for (var i=0; i < nodes.length; i++)
	{
		if ( nodes[i].children.length != 0 )
		{
			// collapsed node with one or more leafs

			var leafs = this.getLeafs (nodes[i]);

			for ( var j=0; j < leafs.length; j++)
				collapsed[collapsed.length] = leafs[j].key + ':' + nodes[i].type + '_' + nodes[i].value;
		}
	}

	return collapsed;
}

/**
 * Returns the entity/identity node using the specified branch id. The branch id represents the node position
 * within the tree. For a tree with three child nodes this will be for example:
 * <pre>
 * first child of root (N1):   id = 1
 * second child of root (N2):  id = 2
 * first child of N2:          id = 2_1
 * </pre>
 *
 * @param branch_id Branch id of the node to retrieve.
 *
 * @returns The node specified by the branch id.
 */
EntityManager.prototype.getNode = function (branch_id)
{
	var branch  = branch_id.split('_');
	var node	= this;

	for (var i in branch)
	{
		node = node.children[parseInt(branch[i]-1)]

		if ( !node )
			return null;
	}

	return node;
}

/**
 * This class represents an event received from the tracing server.
 *
 * @param xml_event  XML document containing the event parameters.
 * @param controller SVGController instance.
 */
function Event (xml_event, controller)
{
	/**
	 * SVGController instance
	 */
	this.controller = controller;

	/**
	 * Tracing server database key for this event.
	 */
	this.id			= xml_event.getAttribute('id');

	node = this.nextElement (xml_event.firstChild());

	/**
	 * Event interaction point
	 */
	this.interactionPoint = node.hasChildNodes() ? node.getFirstChild().data(): '';

	node = this.nextElement (node);

	/**
	 * Trail label used for coloring invocation belonging to the same trail
	 */
	this.trail_label = node.hasChildNodes() ? node.getFirstChild().data(): '';

	node = this.nextElement (node);

	/**
	 * Id specifying a specific trail
	 */
	this.trail_id = node.hasChildNodes() ? node.getFirstChild().data(): '';

	node = this.nextElement (node);

	/**
	 * The tracing server puts all the event in the right order. yposIndex specifies the ordering between the events
	 */
	this.yposIndex = node.hasChildNodes() ? node.getFirstChild().data(): '';

	node = this.nextElement (node);

	/**
	 * Event creation timestamp.
	 */
	this.timeStamp = node.hasChildNodes() ? node.getFirstChild().data(): '';

	node = this.nextElement (node);

	/**
	 * tracing server database key for the peer event. If there is no peer event this event is unmatched
	 */
	this.peerEventKey = node.hasChildNodes() ? node.getFirstChild().data(): '';

	node = this.nextElement (node);

	/**
	 * Operation name.
	 */
	this.operation = node.hasChildNodes() ? node.getFirstChild().data(): '';

	node = this.nextElement (node);

	/**
	 * used to retrieve the entity that sent this event.
	 */
	this.entityKey = node.hasChildNodes() ? node.getFirstChild().data(): '';

	node = this.nextElement (node);



	/**
	 * used to retrieve the event parameters
	 */
	this.parametersKey = node.hasChildNodes() ? node.getFirstChild().data(): '';

	if (!controller.operationList.containsKey (this.operation))
	{
		// add new operation type to list - no filtering (value=false)

		controller.operationList.put (this.operation, false);

		controller.updateFilterMenus();
	}
}

/**
 * Used to get the next 'element' within the XML document. This routine is only used internally.
 *
 * @param node  Current 'element' node within the document.
 * @returns The next 'element' node or null if there is no 'element node following the current 'element' node.
 */
Event.prototype.nextElement = function(node)
{
	node = node.nextSibling();

	while (node)
	{
		if ( node.nodeType() == 1 ) /* element type */
			return node;

		node = node.nextSibling();
	}

	return null;
}

/**
 * Used to update the SVG operation group that gives a graphical representation of all the operation lines.
 * This routine appends a SVG fragment to the operation group that reflects the operation line between this event
 * and its peer. If this is an unmatched event no operation line can and will be drawn. Exception type of operation are
 * displayed in red, normal operation in black. This coloring schema is overruled by trail label coloring.
 */
Event.prototype.updateSVGOperation = function()
{
	// only update SVG document when this event has a peer event

	if ( !controller.events.get(this.peerEventKey) )
		return;

	var xpos = controller.events.get(this.peerEventKey).xpos;
	var ypos = controller.events.get(this.peerEventKey).ypos;
	var w	 = xpos - this.xpos;
	var h	 = this.ypos - ypos;

	var svg	= new StringBuffer();

	// draw exceptions in red, normal invocations in black. note that this is overruled by trail label coloring


	var color = this.interactionPoint.match('EXCEPTION') ? 'red' : 'black';

	if (controller.trailColors.containsKey (this.trail_label + '_' + this.trail_id) )
		color = controller.trailColors.get(this.trail_label + '_' + this.trail_id);

	// reply direcion will be drawn using a dashed line

	var dash = this.interactionPoint.match('POA') ? 'stroke-dasharray:4,2;' : '';

	if ( this.xpos == xpos )
	{
		svg.write ("<g>");

//justka
// representation of source-self-destination cases is deleted , only circles remained
//the rows below must be deleted beacause they are not useful any more, (or some will remain), we need others graphical symbols so as to show the pogress of the testCase
//		svg.write ("	<path d='M " + (xpos + 4) + " " + this.ypos + " h 30 v " + (ypos - this.ypos) + " h -30' style='fill:none; stroke:" + color + "; " + dash + " stroke-width:1.5;'/>");
//		svg.write ("	<path d='M " + (xpos + 4) + " " + (ypos ) + " l 15 -5 v 10 z' fill='"+ color + "' />");
//		svg.write ("	<text class='stxt' style='text-anchor:start;' x='" + (xpos + 40) + "' y='" + (this.ypos + 10) + "' pointer-events='none'>" + this.operation + "</text>");
		svg.write ("	<a>");
//afterwards : to do : change the range of onmouseclick cases 
		svg.write ("		<rect id='invocation' event_id='" + this.id + "' x='" + (xpos) + "' y='" + (this.ypos) + "' width='100' height='30' style='fill:none;' pointer-events='all' onclick='controller.toggleControlView(evt)' onmouseover='controller.set_mouse_ctx(evt)' onmouseout='controller.clear_mouse_ctx(evt)'/>");
		svg.write ("	</a>");


//justka
	if   (this.operation.match('testCaseStarted'))
	{

		svg.write("<g >");
		svg.write("<rect id='tcase' fill ='blue' x='" + (this.xpos - 44) + "' y='"+ (this.ypos  ) + "'  width='88' height='16' stroke='darkblue' stroke-width='1' />");
		svg.write("</g >");
		svg.write ("<text  x='" + (this.xpos - 39) + "' y='" + (this.ypos+12) + "' font-family='Verdana' font-size='10'  fill='white' >TestCaseStarted</text>");

	}

	if   (this.operation.match('ControlTerminated'))
	{

		svg.write("<g >");
		svg.write("	<circle cx='"+ this.xpos +"' cy='"+ (this.ypos+7) +"' r='10' stroke='black' stroke-width='1.5' fill='white'/>");
		svg.write("</g >");
		svg.write("<g >");
		svg.write("	<circle cx='"+ this.xpos +"' cy='"+ (this.ypos+7) +"' r='7' stroke='white' stroke-width='1.5' fill='black'/>");
		svg.write("</g >");
	}
	else if (this.operation.match('StopControl'))
	{

		svg.write("<g >");
		svg.write("	<circle cx='"+ this.xpos +"' cy='"+ (this.ypos+7) +"' r='10' stroke='red' stroke-width='1.5' fill='white'/>");
		svg.write("</g >");
		svg.write("<g >");
		svg.write("	<circle cx='"+ this.xpos +"' cy='"+ (this.ypos+7) +"' r='7' stroke='white' stroke-width='1.5' fill='red'/>");
		svg.write("</g >");
	}


//justka
	if   (this.operation.match('MatchedMsg'))
	{

		svg.write("<g >");
		svg.write("<rect id='match' x='" + (this.xpos - 35) + "' y='"+ (this.ypos  ) + "'  width='70' height='16' stroke='green' fill='lightyellow' stroke-width='1' />");
		svg.write("</g >");
		svg.write ("<text  x='" + (this.xpos - 33) + "' y='" + (this.ypos+12) + "' font-family='Verdana' font-size='10'  fill='black' >MatchedMsg</text>");

	}

	if   (this.operation.match('MatchedCall'))
	{

		svg.write("<g >");
		svg.write("<rect id='match' x='" + (this.xpos - 35) + "' y='"+ (this.ypos  ) + "'  width='70' height='16' stroke='green' fill='lightyellow' stroke-width='1' />");
		svg.write("</g >");
		svg.write ("<text  x='" + (this.xpos - 33) + "' y='" + (this.ypos+12) + "' font-family='Verdana' font-size='10'  fill='black' >MatchedCall</text>");

	}

	if   (this.operation.match('MatchedReply'))
	{

		svg.write("<g >");
		svg.write("<rect id='match' x='" + (this.xpos - 35) + "' y='"+ (this.ypos  ) + "'  width='70' height='16' stroke='green' fill='lightyellow' stroke-width='1' />");
		svg.write("</g >");
		svg.write ("<text  x='" + (this.xpos - 33) + "' y='" + (this.ypos+12) + "' font-family='Verdana' font-size='10'  fill='black' >MatchedReply</text>");

	}

	if   (this.operation.match('MatchedRaise'))
	{

		svg.write("<g >");
		svg.write("<rect id='match' x='" + (this.xpos - 35) + "' y='"+ (this.ypos  ) + "'  width='70' height='16' stroke='green' fill='lightyellow' stroke-width='1' />");
		svg.write("</g >");
		svg.write ("<text  x='" + (this.xpos - 33) + "' y='" + (this.ypos+12) + "' font-family='Verdana' font-size='10'  fill='black' >MatchedRaise</text>");

	}



	else if   (this.operation.match('UnmatchedMsg'))
	{

		svg.write("<g >");
		svg.write("<rect id='unmatch' x='" + (this.xpos - 35) + "' y='"+ (this.ypos  ) + "'  width='70' height='16' stroke='green' fill='lightblue' stroke-width='1' />");
		svg.write("</g >");
		svg.write ("<text  x='" + (this.xpos - 33) + "' y='" + (this.ypos+12) + "' font-family='Verdana' font-size='10'  fill='white' >UnmatchedMsg</text>");

	}

	else if   (this.operation.match('UnmatchedCall'))
	{

		svg.write("<g >");
		svg.write("<rect id='unmatch' x='" + (this.xpos - 35) + "' y='"+ (this.ypos  ) + "'  width='70' height='16' stroke='green' fill='lightblue' stroke-width='1' />");
		svg.write("</g >");
		svg.write ("<text  x='" + (this.xpos - 33) + "' y='" + (this.ypos+12) + "' font-family='Verdana' font-size='10'  fill='white' >UnmatchedCall</text>");

	}


	else if   (this.operation.match('UnmatchedReply'))
	{

		svg.write("<g >");
		svg.write("<rect id='unmatch' x='" + (this.xpos - 35) + "' y='"+ (this.ypos  ) + "'  width='70' height='16' stroke='green' fill='lightblue' stroke-width='1' />");
		svg.write("</g >");
		svg.write ("<text  x='" + (this.xpos - 33) + "' y='" + (this.ypos+12) + "' font-family='Verdana' font-size='10'  fill='white' >UnmatchedReply</text>");

	}


	else if   (this.operation.match('UnmatchedRaise'))
	{

		svg.write("<g >");
		svg.write("<rect id='unmatch' x='" + (this.xpos - 35) + "' y='"+ (this.ypos  ) + "'  width='70' height='16' stroke='green' fill='lightblue' stroke-width='1' />");
		svg.write("</g >");
		svg.write ("<text  x='" + (this.xpos - 33) + "' y='" + (this.ypos+12) + "' font-family='Verdana' font-size='10'  fill='white' >UnmatchedRaise</text>");

	}


//all the operations which are representing ttcn3 action get the values stroke='black' stroke-width='2'
	if (  (this.operation.match('startTestComponent')))
{

		svg.write("<g >");
		svg.write("<rect id='sichel' x='" + (this.xpos - 37) + "' y='"+ (this.ypos) + "'  fill ='lightyellow'  width='74' height='16' stroke='black' stroke-width='2' />");
		svg.write("</g >");
		svg.write ("<text  x='" + (this.xpos - 27) + "' y='" + (this.ypos+12) + "' font-family='Verdana' font-size='10'  fill='black' >Behaviour</text>");
}



	if ((this.operation.match('xecuteTest')))
	{		
		svg.write("<g >");
		svg.write("<rect id='sichel' x='" + (this.xpos - 37) + "' y='"+ (this.ypos) + "'  fill ='violet'  width='74' height='16' stroke='black' stroke-width='2' />");
		svg.write("</g >");
		svg.write ("<text  x='" + (this.xpos - 16) + "' y='" + (this.ypos+12) + "' font-family='Verdana' font-size='10'  fill='black' >ExecuteTCase</text>");
	}


	if (  (this.operation.match('tartTestComponentReq')))
{

		svg.write("<g >");
		svg.write("<rect id='sichel' x='" + (this.xpos - 47) + "' y='"+ (this.ypos) + "'  fill ='lightGreen'  width='94' height='16' stroke='darkblue' stroke-width='1' />");
		svg.write("</g >");
		svg.write ("<text  x='" + (this.xpos - 45) + "' y='" + (this.ypos+12) + "' font-family='Verdana' font-size='10'  fill='black' >startTestCompReq</text>");
}


	if (  (this.operation.match('StopTestComponentReq')))
{

		svg.write("<g >");
		svg.write("<rect id='sichel' x='" + (this.xpos - 47) + "' y='"+ (this.ypos) + "'  fill ='lightGreen'  width='94' height='16' stroke='darkblue' stroke-width='1' />");
		svg.write("</g >");
		svg.write ("<text  x='" + (this.xpos - 45) + "' y='" + (this.ypos+12) + "' font-family='Verdana' font-size='10'  fill='black' >stopTestCompReq</text>");
}

	else if (  (this.operation.match('StopTestComponent')))
{

		svg.write("<g >");
		svg.write("<rect id='sichel' x='" + (this.xpos - 47) + "' y='"+ (this.ypos) + "'  fill ='lightBlue'  width='94' height='16' stroke='black' stroke-width='1' />");
		svg.write("</g >");
		svg.write ("<text  x='" + (this.xpos - 36) + "' y='" + (this.ypos+12) + "' font-family='Verdana' font-size='10'  fill='black' >stopTestComp</text>");
}


	if (this.operation.match('TestComponentTerminated'))
	{
		svg.write("<g >");
		svg.write("<image x='" + (this.xpos -25) + "' y='" + (this.ypos) + "' xlink:href='verdict.jpg' width='50' height='16' />");
		svg.write("</g>");
		svg.write ("<text  x='" + (this.xpos - 16) + "' y='" + (this.ypos+12) + "' font-family='Verdana' font-size='10'  fill='black' >verdict</text>");
	}

	if (this.operation.match('TestCaseTerminated') )
	{
		svg.write("<g >");
		svg.write("<image x='" + (this.xpos -25) + "' y='" + (this.ypos) + "' xlink:href='verdict.jpg' width='50' height='16' />");
		svg.write("</g>");
		svg.write ("<text  x='" + (this.xpos - 17) + "' y='" + (this.ypos+12) + "' font-family='Verdana' font-size='10'  fill='black' >verdict</text>");
	}

	if (this.operation.match('StartTime') )
	{
		svg.write("<g >");
		svg.write("<image x='" + (this.xpos -31) + "' y='" + (this.ypos - 4) + "' xlink:href='startTim.jpg' width='30' height='22' />");
		svg.write("</g>");
	}
//just// april
	if (this.operation.match('xecute') )
	{
		svg.write("<g >");
		svg.write("<rect id='sichel' x='" + (this.xpos - 47) + "' y='"+ (this.ypos) + "'  fill ='lightGreen'  width='94' height='16' stroke='blue' stroke-width='1' />");
		svg.write("</g >");
		svg.write ("<text  x='" + (this.xpos - 45) + "' y='" + (this.ypos+12) + "' font-family='Verdana' font-size='10'  fill='white' >execute</text>");
	}

	if (this.operation.match('StopTime') )
	{
		svg.write("<g >");
		svg.write("<image x='" + (this.xpos -30) + "' y='" + (this.ypos - 5) + "' xlink:href='stopTim.jpg' width='30' height='22' />");
		svg.write("</g>");
	}

	if (this.operation.match('imeout') )
	{
		svg.write("<g >");
		svg.write("<image x='" + (this.xpos -31) + "' y='" + (this.ypos - 5) + "' xlink:href='timOut.jpg' width='30' height='22' />");
		svg.write("</g>");
	}


	if (  (this.operation.match('createTestComponent')))
{

		svg.write("<g >");
		svg.write("<rect id='sichel' fill ='skyBlue'  x='" + (this.xpos - 37) + "' y='"+ (this.ypos) + "'  width='74' height='16' stroke='black' stroke-width='2' />");
		svg.write("</g >");
		svg.write ("<text  x='" + (this.xpos - 33) + "' y='" + (this.ypos+12) + "' font-family='Verdana' font-size='8'  fill='white' >createTestComp</text>");
}

	svg.write ("</g>");
	svg.write ("</g>");
	}
	else
	{
		var angle 		= Math.atan2 (w, h);
		var dx 		= 4 * Math.sin (angle);
		var dy 		= 4 * Math.cos (angle);

		var rotate = (angle/Math.PI) * 180 - 270;

		svg.write ("<g>");
		svg.write ("	<line x1='" + this.xpos + "' y1='" + this.ypos + "' x2='" + xpos + "' y2='" + ypos + "' style='fill:none; stroke:" + color + "; " + dash + " stroke-width:1.5;'/>");
		svg.write ("	<path d='M " + (xpos - dx) + " " + (ypos + dy) + " l 15 -5 v 10 z' fill='" + color + "' transform='rotate(" + rotate + ", "+ (xpos - dx) + "," + (ypos + dy) + ")'/>");


		svg.write ("	<text class='stxt' x='" + (this.xpos + Math.round(w/2)) + "' y='" + this.ypos + "' pointer-events='none'>" + this.operation + "</text>");
		svg.write ("	<a>");
		svg.write ("		<rect id='invocation' event_id='" + this.id + "' x='" + (this.xpos + Math.round(w/2) - 50) + "' y='" + (this.ypos - 15) + "' width='100' height='30' style='fill:none;' pointer-events='all' onclick='controller.toggleControlView(evt)' onmouseover='controller.set_mouse_ctx(evt)' onmouseout='controller.clear_mouse_ctx(evt)'/>");
		svg.write ("	</a>");
	svg.write ("</g>");
	}

	var frag = parseXML(svg.toString(), document);

	// by inserting the lines and text before the events into the document the lines can be drawn from the event center positions (lower z-order)
	// the operation group is defined before the events group so the prior group has a lower z-order.

	controller.svg_operation_group.appendChild (frag);
}

/**
 * This class is the 'engine' of the tracing viewer and is responsible for processing user interaction request,
 * retrieving information from the tracing server, and building up the SVG document needed for the graphical
 * representation of the invocation traces.
 */
function SVGController()
{
	/**
	 * Flag used as a primitive lock mechanism to avoid user interactions while still updating as a result of a previous
	 * interaction
	 */
	this.mtx				= false;

	/**
	 *	The number of events within the tracing server database. This number is retrieved by requests to the server
	 */
	this.eventCount			= 0;

	/**
	 *	the vertical scrollbar offset of the v-slider on the page control panel (between 0 - 1)
	 */
	this.vScrollOffset		= 0;

	/**
	 *	the horizontal scrollbar offset of the h-slider on the page control panel (between 0 - 1)
	 */
	this.hScrollOffset		= 0;

	/**
	 *	Position in the tracing server database from where to start retrieving events
	 */
	this.eventCursor		= 0;

	/**
	 * If flag is set then matched events that appear on the same entity/identity line will be invisible
	 */
	this.toSelf				= false;

	/**
	 * If flag is set then unmatched events will be invisible
	 */
	this.unmatched			= false;

	/**
	 * View panel height. This value normally is the same as the window height but can change in case the
	 * user zooms in or out.
	 */
	this.height				= innerHeight;

	/**
	 * View panel window width
	 */
	this.width 				= innerWidth;

	/**
	 * Number of events that can be displayed on view panel window. This value can change if user zooms in or out.
	 */
	this.events_on_page 	= Math.round (this.height / SVGController.Y_SPACE);

	/**
	 * Entity/Identity tree
	 */
	this.entities			= new EntityManager();

	/**
	 * Lookup table used to retrieve the EntityNode branch id given a certain entity key. Usefull to determine the
	 * event to entity mapping.
	 */
	this.entityList			= new Array();

	/**
	 * SVG document fragment where the SVG elements that represent the entities are placed into.
	 */
	this.svg_entity_group	= document.getElementById('entity group');

	/**
	 * SVG document fragment where the SVG elements represent the events.
	 */
	this.svg_event_group	= document.getElementById('event group');

	/**
	 * SVG document fragment where the SVG elements represent the invocation lines.
	 */
	this.svg_operation_group = document.getElementById('operation group');

	/**
	 * Used to store Event class instances using the event id as key.
	 */
	this.events				= new Hashtable();

	/**
	 * Visible events. Used to sort and draw.
	 */
	this.visibleEvents		= new Array ();

	/**
	 * Used to store the entities that are visible to the user.
	 */
	this.visibleEntities	= new Array ();

	/**
	 * Tracing server request parameters.
	 */
	this.params				= new Array ();

	/**
	 * List containing all operations (keys) with corresponding value (boolean) indicating if an operation must be filtered out.
	 */
	this.operationList		= new Hashtable();

	/**
	 * List containing trail ids of which the color is not the default one.
	 */
	this.trailColors		= new Hashtable();

	this.createTooltipPanel();

	/**
	 * Context menu to set the trail color.
	 */
	this.color_menu 	    = parseXML(printNode(document.getElementById('color_menu')), contextMenu);

	/**
	 * Context menu to hide event range.
	 */
	this.event_menu 	    = parseXML(printNode(document.getElementById('event_menu')), contextMenu);

	/**
	 * All events from the top to the cursor are hidden as if they don't exist.
	 */
	this.hideEventsTo        = 0;

	/**
	 * Default context menu (Adobe)
	 */
	this.svg_menu 		= contextMenu.firstChild;

	/**
	 * Adobe events listeners
	 */
	document.rootElement.addEventListener("SVGZoom",   SVGController.zoom, false);
	document.rootElement.addEventListener("SVGScroll", SVGController.scroll, false);

	/**
	 * west and east button step size
	 */
	this.step_size_x = innerWidth * 0.25;

	window.ctrl = this;

	/**
	 * Set to true when busy retrieving information from server.
	 */
	this.wait           = false;

	/**
	 * 500 ms timer.
	 */
	this.timer_id       = setInterval ("window.ctrl.timer();", 500);
}

/**
 * Order function used by the visible events array to sort events with the yposIndex parameter as sort criteria.
 *
 * @param a First event of the two events to sort
 * @param b Second event of the two events to sort
 */
SVGController.yposIndexOrder = function(a,b)
{
	return a.yposIndex - b.yposIndex;
}

/**
 * 500 ms timer used to update the status line when retrieving information from the server.
 */
SVGController.prototype.timer = function ()
{
	if ( this.wait)
		this.ctrl_panel.top.defaultStatus += ".";
}

/**
 * Used to prevent user interactions while busy
 */
SVGController.prototype.acquireLock = function ()
{
	if ( this.mtx )
	{
		// can not lock

		return false;
	}

	this.mtx = true;

	return true;
}


/**
 * This routine (re)calculates the position of the events on the view panel window and calls the
 * <a href='#updateSVGDocument'>updateSVGDocument</a> routine to update the SVG document.
 */
SVGController.prototype.refresh = function ()
{
	this.visibleEntities = this.entities.getVisibles();

	var ids	= new Array();

	document.rootElement.currentTranslate.x =  -SVGController.X_SPACE * this.visibleEntities.length * this.hScrollOffset;

	for (var i in this.visibleEntities )
		ids[i] = '(' + this.visibleEntities[i].id + '.*)'

	var pattern = ids.join('|');

	var events  = this.events.toArray();

	for (var i = 0; i < events.length; i++ )
	{
		var branch_id = this.entityList[events[i].entityKey];

		// calculate event x position

		var branch_id = this.entityList[events[i].entityKey];
		var result 	= branch_id.match (pattern);

		var j;

		if ( !result )
			continue;

		for (j = 1; j < result.length; j++)
		{
			if (result[j].length > 0  )
			{
				j--;
				break;
			}
		}

		events[i].xpos = Math.round(SVGController.X_SPACE/ 2)  + j * SVGController.X_SPACE;

		this.visibleEvents[this.visibleEvents.length] = events[i];
	}

	// sort the visible events using the yposIndex as sort criteria

	this.visibleEvents.sort (SVGController.yposIndexOrder);

	// update the SVG document

	this.updateSVGDocument ();

	// clear and remove lock

	this.visibleEvents.length  	= 0;

	this.mtx = false;
}

/**
 * Updates the SVG document to reflect all the changes.
 */
SVGController.prototype.updateSVGDocument = function ()
{
	// clear svg event group

	var item = this.svg_event_group.firstChild();

	while (item)
	{
	   var next	= item;
	   item 	= next.nextSibling();

	   this.svg_event_group.removeChild(next);
	}

	// clear svg entity group

	var item = this.svg_entity_group.firstChild();

	while (item)
	{
	   var next	= item;
	   item 	= next.nextSibling();

	   this.svg_entity_group.removeChild(next);
	}

	// clear svg operation group

	var item = this.svg_operation_group.firstChild();

	while (item)
	{
	   var next	= item;
	   item 	= next.nextSibling();

	   this.svg_operation_group.removeChild(next);
	}

	// (re)draw entities/identities

//justka
//tu mozna pozamieniac kolory Axela- gorne texty na inne kolory zrobic 
	for (var i in this.visibleEntities)
	{

		var short_name = '';

		// prevent overlapping names (number of char is experimentally defined)

		if ( this.visibleEntities[i].value.length > 18 )
			short_name = this.visibleEntities[i].value.substring (0, 18);
		else
			short_name = this.visibleEntities[i].value;


		// calculate x position

		this.visibleEntities[i].xpos = Math.round(SVGController.X_SPACE/2) + i * SVGController.X_SPACE;

		// svg code

		var svg = new StringBuffer();

svg.write("<g id='" + this.visibleEntities[i].id +  "' style='visibility:visible;'>");
//justka
//colors for different types 

if ( this.visibleEntities[i].parent.value.match('system'))
	{
		svg.write("	<text font-size='10pt'  font='modern' text-anchor='middle'  fill='green'  x='" + this.visibleEntities[i].xpos + "' y='12' pointer-events='none'>" + short_name + "</text>");
		svg.write("	<path fill='black' d='M " + this.visibleEntities[i].xpos + ",22 v 8192 z' />");
		svg.write("	<rect style='fill:black' x='" + (this.visibleEntities[i].xpos - 10) + "' y='17' width='20' height='5'/>");
		svg.write(" <a>");
		svg.write("		<rect id='timeline' style='fill:none;' pointer-events='all' x='" + (this.visibleEntities[i].xpos - 10) + "' y='22' width='20' height='8192'  onmouseover='controller.set_mouse_ctx(evt);' onmouseout='controller.clear_mouse_ctx(evt);' onclick='controller.toggleControlView(evt)' />");
		svg.write("		<rect id='entity' style='fill:none;' pointer-events='all' x='" + (this.visibleEntities[i].xpos - 50) + "' y='0'  width='100' height='24'   onmouseover='controller.set_mouse_ctx(evt);' onmouseout='controller.clear_mouse_ctx(evt);' onclick='controller.toggleControlView(evt)' />");
		svg.write("		<rect id='endCompo' style='fill:black' x='" + (this.visibleEntities[i].xpos - 40) + "' y='840' width='80' height='7'/>");
		svg.write("	</a>");
		svg.write("</g>");	
}

if ( this.visibleEntities[i].parent.value.match('control'))
	{
		svg.write("	<text font-size='10pt'  font='modern' text-anchor='middle' fill='black'  x='" + this.visibleEntities[i].xpos + "' y='12' pointer-events='none'>" + short_name + "</text>");
		svg.write("	<path fill='black' d='M " + this.visibleEntities[i].xpos + ",22 v 8192 z' />");
		svg.write("	<rect style='fill:black' x='" + (this.visibleEntities[i].xpos - 10) + "' y='17' width='20' height='5'/>");
		svg.write(" <a>");
		svg.write("		<rect id='timeline' style='fill:none;' pointer-events='all' x='" + (this.visibleEntities[i].xpos - 10) + "' y='22' width='20' height='8192'  onmouseover='controller.set_mouse_ctx(evt);' onmouseout='controller.clear_mouse_ctx(evt);' onclick='controller.toggleControlView(evt)' />");
		svg.write("		<rect id='entity' style='fill:none;' pointer-events='all' x='" + (this.visibleEntities[i].xpos - 50) + "' y='0'  width='100' height='24'   onmouseover='controller.set_mouse_ctx(evt);' onmouseout='controller.clear_mouse_ctx(evt);' onclick='controller.toggleControlView(evt)' />");
		svg.write("		<rect id='endCompo' style='fill:black' x='" + (this.visibleEntities[i].xpos - 40) + "' y='840' width='80' height='7'/>");
		svg.write("	</a>");
		svg.write("</g>");}

if ( this.visibleEntities[i].parent.value.match('mtc'))
	{

		svg.write("	<text font-size='10pt'  font='modern' text-anchor='middle' fill='grey' x='" + this.visibleEntities[i].xpos + "' y='12' pointer-events='none'>" + short_name + "</text>");
		svg.write("	<path fill='black' d='M " + this.visibleEntities[i].xpos + ",22 v 8192 z' />");
		svg.write("	<rect style='fill:black' x='" + (this.visibleEntities[i].xpos - 10) + "' y='17' width='20' height='5'/>");
		svg.write(" <a>");
		svg.write("		<rect id='timeline' style='fill:none;' pointer-events='all' x='" + (this.visibleEntities[i].xpos - 10) + "' y='22' width='20' height='8192'  onmouseover='controller.set_mouse_ctx(evt);' onmouseout='controller.clear_mouse_ctx(evt);' onclick='controller.toggleControlView(evt)' />");
		svg.write("		<rect id='entity' style='fill:none;' pointer-events='all' x='" + (this.visibleEntities[i].xpos - 50) + "' y='0'  width='100' height='24'   onmouseover='controller.set_mouse_ctx(evt);' onmouseout='controller.clear_mouse_ctx(evt);' onclick='controller.toggleControlView(evt)' />");
		svg.write("		<rect id='endCompo' style='fill:black' x='" + (this.visibleEntities[i].xpos - 40) + "' y='840' width='80' height='7'/>");
		svg.write("	</a>");
		svg.write("</g>");}

if ( this.visibleEntities[i].parent.value.match('ptc'))
	{
		svg.write("	<text font-size='10pt'  font='modern' text-anchor='middle'  fill='darkblue'  x='" + this.visibleEntities[i].xpos + "' y='12' pointer-events='none'>" + short_name + "</text>");
		svg.write("	<path fill='black' d='M " + this.visibleEntities[i].xpos + ",22 v 8192 z' />");
		svg.write("	<rect style='fill:black' x='" + (this.visibleEntities[i].xpos - 10) + "' y='17' width='20' height='5'/>");
		svg.write(" <a>");
		svg.write("		<rect id='timeline' style='fill:none; ' pointer-events='all' x='" + (this.visibleEntities[i].xpos - 10) + "' y='22' width='20' height='8192'  onmouseover='controller.set_mouse_ctx(evt);' onmouseout='controller.clear_mouse_ctx(evt);' onclick='controller.toggleControlView(evt)' />");
		svg.write("		<rect id='entity' style='fill:none; ' pointer-events='all' x='" + (this.visibleEntities[i].xpos - 50) + "' y='0'  width='100' height='24'   onmouseover='controller.set_mouse_ctx(evt);' onmouseout='controller.clear_mouse_ctx(evt);' onclick='controller.toggleControlView(evt)' />");
		svg.write("		<rect id='endCompo' style='fill:black' x='" + (this.visibleEntities[i].xpos - 40) + "' y='840' width='80' height='7'/>");
		svg.write("	</a>");
		svg.write("</g>");
}


		svg.write("	<text  font-size='10pt'  font='modern' text-anchor='middle'  fill='#0000FF'   x='" + this.visibleEntities[i].xpos + "' y='12' pointer-events='none'>" + short_name + "</text>");
// class='txt' problem
		//a tu powstaje linia zycia lifeline!!
		//zamiast wartsci dlugosci linniii 8192 narazie wstawilam 1192
		svg.write("	<path fill='black' d='M " + this.visibleEntities[i].xpos + ",22 v 8192 z' />");
		svg.write("	<rect style='fill:black' x='" + (this.visibleEntities[i].xpos - 10) + "' y='17' width='20' height='5'/>");
		svg.write(" <a>");
		svg.write("		<rect id='timeline' style='fill:none;' pointer-events='all'  x='" + (this.visibleEntities[i].xpos - 10) + "' y='22' width='20' height='8192'  onmouseover='controller.set_mouse_ctx(evt);' onmouseout='controller.clear_mouse_ctx(evt);' onclick='controller.toggleControlView(evt)' />");
		svg.write("		<rect id='entity' style='fill:none;' pointer-events='all' x='" + (this.visibleEntities[i].xpos - 50) + "' y='0'  width='100' height='24'   onmouseover='controller.set_mouse_ctx(evt);' onmouseout='controller.clear_mouse_ctx(evt);' onclick='controller.toggleControlView(evt)' />");
		//to do:
		//justka
		//ja dodam tu ograniczenie typu - zakoncz komponnet wg GFT
		svg.write("		<rect id='endCompo' style='fill:black' x='" + (this.visibleEntities[i].xpos - 40) + "' y='840' width='80' height='7'/>");
		svg.write("	</a>");

		svg.write("</g>");

		var frag = parseXML(svg.toString(), document);

		this.svg_entity_group.appendChild (frag);	
	}


	var operation = new Array();

	for (var i in this.visibleEvents )
	{
		// calculate event y position

		this.visibleEvents[i].ypos = 50 + i * SVGController.Y_SPACE;

		// give oneway invocation a different color (red)

		var unmatched = (this.events.get(this.visibleEvents[i].peerEventKey)==null);

//justka
//to do : use some parameter instead of hard words as the names of operations may vary and are defined in EventLoggingImpl.java file 

		var color2 = this.visibleEvents[i].interactionPoint.match('ONEWAY');
		var color3 = this.visibleEvents[i].operation.match('Time');
		var color4 = this.visibleEvents[i].operation.match('atch');
		var color5 = this.visibleEvents[i].operation.match('create');
		var color6 = this.visibleEvents[i].operation.match('erminat');


		if ((color2) && (!color3) && (!color4) && (!color5) && (!color6))
			color = 'red';
		else if ((!color2) && (!color3) && (!color4) && (!color5) && (!color6)) 
			color = 'blue';
		else if ((!color3) && (!color2) && (!color4) && (color5) && (!color6))
		// give operations which are connected with creation (create) a different color (cyan)
			color = 'cyan';
		else if ((!color3) && (!color2) && (color4) && (!color5) && (!color6))
			color = 'green';
		else if ((!color3) && (!color2) && (!color4) && (!color5) && (color6))
			color = 'black';

		else
		// give operations which are connected with time (StartTime, StopTime, TimeOut) a different color (yellow)
			color = 'yellow';


		// fill unmatched events with red color

		var fill  = unmatched ? 'red' : 'white';

		var svg = new StringBuffer();

		svg.write("<g id='" + this.visibleEvents[i].id +  "' style='visibility:visible;'>");

		//justka tu moznaby skasowac wg Axela niego ;) te koleczka 
		svg.write("	<circle cx='"+ this.visibleEvents[i].xpos +"' cy='"+ this.visibleEvents[i].ypos +"' r='3' stroke='" + color + "' stroke-width='1.5' fill='" + fill + "'/>");
 
		svg.write(" <a>");
		svg.write("		<rect id='event' style='fill:none;' pointer-events='all' x='" + (this.visibleEvents[i].xpos - 20) + "' y='"+ (this.visibleEvents[i].ypos -10) + "'  width='40' height='20'   onmouseover='controller.set_mouse_ctx(evt);' onmouseout='controller.clear_mouse_ctx(evt);' onclick='controller.toggleControlView(evt)' />");
		svg.write("	</a>");

		svg.write("</g>");

		var frag = parseXML(svg.toString(), document);

		this.visibleEvents[i].node = this.svg_event_group.appendChild (frag);

		// only draw operation line for the OUT types

		if ( this.visibleEvents[i].interactionPoint.match('OUT') )
			operation[operation.length] = this.visibleEvents[i];
	}


	// draw operation lines

	for ( var i in operation )
		operation[i].updateSVGOperation();
}

/**
 * Static event listener (Adobe SVGZoom events) callback routine that handles zoom in/out events the user can issue
 * using the default Adobe context menu.
 */
SVGController.zoom = function()
{
	if (!window.ctrl.acquireLock())
		return;

	var scale 	= document.rootElement.currentScale;
	var trans 	= document.rootElement.currentTranslate;

	// limit zoom out

	if ( scale < 0.25 )
		document.rootElement.currentScale = 0.25;

	// reposition

	document.rootElement.currentTranslate.x = 0;
	document.rootElement.currentTranslate.y = 0;

	// update content

	window.ctrl.height = innerHeight / scale;
	window.ctrl.events_on_page = Math.round (window.ctrl.height / SVGController.Y_SPACE);

	window.ctrl.getEvents();
}

/**
 * Static event listener (Adobe SVGScroll events) callback routine that handles panning events the user can issue by
 * holding the alt key and clicking and dragging with the mouse.
 */
SVGController.scroll = function()
{
	// prevent vertical movements for entity group

	var dy	= -document.rootElement.currentTranslate.y / document.rootElement.currentScale;

	var container = document.getElementById("entity group");

	container.setAttribute("transform", "translate(0, "+ dy + ")");
}

/**
 * Replaced the default (Adobe) context menu with the color menu used to color an invocation trail.
 */
SVGController.prototype.set_color_menu = function()
{
	if (this.color_menu)
		contextMenu.replaceChild(this.color_menu.cloneNode(true), contextMenu.firstChild);
}

/**
 * Replaced the default (Adobe) context menu with the event menu.
 */
SVGController.prototype.set_event_menu = function()
{
	if (this.event_menu)
		contextMenu.replaceChild(this.event_menu.cloneNode(true), contextMenu.firstChild);
}

/**
 * Restores the default (Adobe) context menu
 */
SVGController.prototype.restore_default_menu = function()
{
	if (this.color_menu)
		contextMenu.replaceChild(this.svg_menu.cloneNode(true), contextMenu.firstChild);
}

/**
 * When the user moves the mouse over the graphical representation of an invocation, event, or entity/identity
 * the corresponding SVG element will fire an 'onmouseover' event that is captured using this routine. It is used
 * for one of the following actions:
 *
 * <ul>
 *   <li> Show a tooltip panel using <a href='#showTooltip'>showTooltip</a></li>
 *   <li> Set the color context menu using <a href='#set_color_menu'>set_color_menu</a></li>
 * 	 <li> The entity/identity line to show up in bold. </li>
 * </ul>
 */
SVGController.prototype.set_mouse_ctx = function (evt)
{
	this.mouse_target = evt.getTarget();

	var id = this.mouse_target.getAttribute ('id');

	if ( id == 'timeline' )
	{
		var node = this.mouse_target.parentNode().parentNode().getElementsByTagName ('path').item(0);

		if ( node )
			node.setAttribute ('stroke','black');

		return;
	}
	else if ( id == 'invocation')
	{
		this.set_color_menu();

		return;
	}
	else if ( id == 'event')
		this.set_event_menu();

	this.showTooltip();
}

/**
 * Restores the default behaviour previously changed by the <a href='#set_mouse_ctx'>set_mouse_ctx</a> routine.
 */
SVGController.prototype.clear_mouse_ctx = function(evt)
{
	this.mouse_target = evt.getTarget();

	var id = this.mouse_target.getAttribute ('id');

	if ( id == 'timeline' )
	{
		var node = this.mouse_target.parentNode().parentNode().getElementsByTagName ('path').item(0);

		if (node)
			node.setAttribute ('stroke','none');

		return;
	}
	else if ( id == 'invocation')
	{
		this.restore_default_menu();

		return;
	}
	else if ( id == 'event')
		this.restore_default_menu();

	this.hideTooltip();
}

/**
 * Callback routine called when the user acts on the operation context menu to color a trail.
 *
 * @param color Color to set for the selected trail.
 */
SVGController.prototype.setTrailColor = function (color)
{
	var evt_id = this.mouse_target.getAttribute ('event_id');
	var evt    = null;

	if ( evt_id )
	{
		evt = this.events.get (evt_id);

		if (evt)
			this.trailColors.put ((evt.trail_label + '_' + evt.trail_id), color);
	}

	this.refresh();
}

/**
 * Callback routine called when the user acts on the operation context menu to restore the default trail color.
 */
SVGController.prototype.clearTrailColor = function ()
{
	var evt_id = this.mouse_target.getAttribute ('event_id');
	var evt    = null;

	if ( evt_id )
	{
		evt = this.events.get (evt_id);

		if (evt)
			this.trailColors.remove ((evt.trail_label + '_' + evt.trail_id));
	}

	this.refresh();
}

/**
 * Callback routine called when the user acts on the event context menu to hide events from the top to
 * the current event position.
 */
SVGController.prototype.hideEvents = function ()
{
	if ( !this.acquireLock() )
		return;

	var id   = this.mouse_target.getParentNode().getParentNode().getAttribute ('id');
	var evt;

	if ( id )
	{
		evt = this.events.get (id);

		if ( evt)
		{
			this.hideEventsTo = parseInt(evt.yposIndex);

			this.vScrollOffset = 0;

			this.ctrl_panel.setVSlider (0);

			this.getEventCount();
		}
	}
}

/**
 * Callback routine called when the user acts on the event context menu to restore the default view
 */
SVGController.prototype.showEvents = function ()
{
	if ( !this.acquireLock() )
		return;

	var id   = this.mouse_target.getParentNode().getParentNode().getAttribute ('id');
	var evt;

	if ( id )
	{
		evt = this.events.get (id);

		this.vScrollOffset = evt.yposIndex / (this.eventCount + this.hideEventsTo);

		this.ctrl_panel.setVSlider (this.vScrollOffset);

		this.hideEventsTo = 0;
	 }

	 this.getEventCount();
}

/**
 * When the user clicks with the mouse on the graphical representation of an invocation, event, or entity/identity
 * the corresponding SVG element will fire an 'onclick' event that is captured using this routine. Depending on the
 * element clicked, the following happens:
 *
 * <ul>
 *   <li> <b>Invocation</b> - The HTML control panel will display a 'Operation Control' tab that gives the user
 *         control about the operations to filter.</li>
 *   <li> <b>Event</b> - The HTML control panel will display a 'Event Parameters' tab showing the event parameters.</li>
 * 	 <li> <b>Entity/Identity</b> - The HTML control panel will display the 'Entity Control' tab containing the entity/identity
 *           hierarchy tree.</li>
 * </ul>
 */
SVGController.prototype.toggleControlView = function (evt)
{
	var target 	= evt.getTarget()
	var type 	= target.getAttribute ('id');

	if ( type == 'entity' || type == 'timeline' )
	{
		this.caption.innerHTML = 'Entity Control';

		// hide other controls

		this.event_tree.hide();
		get_element('filter', this.tabs_doc).style.display="none";

		// show entity tree

		this.entity_tree.show();

		// update

		var node = this.entity_tree.getNode ('0_' + target.parentNode().parentNode().getAttribute('id'));

		if ( node )
		{
			node.selected = true;

			this.entity_tree.refresh();
		}
	}
	else if ( type == 'invocation' )
	{
		// hide other controls

		this.event_tree.hide();
		this.entity_tree.hide();

		this.caption.innerHTML = 'Operation Control';

		this.updateFilterMenus();

		// show filter control

		get_element('filter', this.tabs_doc).style.display="block";
	}
	else
	{
		this.caption.innerHTML = 'Event Parameters';

		// hide other controls

		this.entity_tree.hide();
		get_element('filter', this.tabs_doc).style.display="none";

		// update parameter tree

		var node  = this.event_tree.firstChild();

		if ( node )
			this.cookies[1] = node.expanded ? '1' : '0';

		this.event_tree.clear();

		var key			= evt.getTarget().getParentNode().getParentNode().getAttribute ('id');
		var event 			= this.events.get(key);
		var entity 			= this.entities.getNode(this.entityList[event.entityKey]);
		var properties 		= this.event_tree.createNode (image[6].src, '', 'properties');



		this.event_tree.appendChild (properties);

		// add properties

		properties.appendChild (this.event_tree.createNode (image[0].src, 'Entity' , entity.type + ': ' + entity.value));
		//we need it to be seen - as it gives the order of events in time 
		properties.appendChild (this.event_tree.createNode (image[0].src, 'Index' , event.yposIndex));
		properties.appendChild (this.event_tree.createNode (image[0].src, 'Operation', event.operation));


		properties.expanded = (this.cookies[1] == 1);

		this.callback 	= 'this.showEventTree()';
		this.request  	= 'parameters';
		this.serverRequest (event.parametersKey);
	}
}

/**
 * Modifies the size of the tooltip panel so that text set by the <a href='#addTooltipText'>addTooltipText</a> routine will fit.
 */
SVGController.prototype.setTooltipSize = function()
{
	//justka
	//var recMsg = 0; 
	var length = 0;
	var nodes  = this.tooltip_text.getElementsByTagName ('tspan');

	for (var i = 0; i < nodes.length; i++)
	{
		// find maximum text length

		var size = Math.round(nodes.item(i).getComputedTextLength());

		if ( size > length )
			length = size;
	}

	// set panel size

	var rc = this.tooltip_panel.firstChild;

	rc.setAttribute ("width",  length + 10);
	rc.setAttribute ("height", nodes.length * 16 + 5);
}

/**
 * Sets the tooltip text for an event or entity when hoovering with the mouse above the graphical representation.
 * This function is called by the <a href='#showTooltip'>showTooltip</a> routine.
 *
 * @param v1 Discriminator to identify the element type - event or entity.
 * @param v2 Element Id.
 */
SVGController.prototype.addTooltipText = function (v1, v2)
{
	if ( v1 == 'entity' )
	{
		var branch = v2.split ('_');

		node = this.entities;

		for (var i = 0; i < branch.length; i++)
		{
			node = node.children[parseInt(branch[i]-1)]

			var tspan  = document.createElement("tspan");

			tspan.setAttribute ('x', '5');
			tspan.setAttribute ('y', (i + 1)* 15);
			tspan.appendChild (document.createTextNode (node.type+ ': ' + node.value));

			if ( i == branch.length -1 )
				tspan.setAttribute ('style', 'fill:brown');

			this.tooltip_text.appendChild (tspan);
		}
	}
	else
	{
		var evt = this.events.get(v2);
		var e1 	= this.entities.getNode(this.entityList[evt.entityKey]);

		var e2 = null;

		// is there a peer event

		if ( this.events.get(evt.peerEventKey) )
			 e2	= this.entities.getNode(this.entityList[this.events.get(evt.peerEventKey).entityKey]);

		// event source

		var tspan  = document.createElement("tspan");

		tspan.setAttribute ('x', '5');
		tspan.setAttribute ('y', 15);
		tspan.setAttribute ('style', 'fill:brown');

		tspan.appendChild (document.createTextNode (e1.type + ": " + e1.value));
		this.tooltip_text.appendChild (tspan);

		// empty line

		var tspan  = document.createElement("tspan");

		tspan.setAttribute ('x', '5');
		tspan.setAttribute ('y', 30);

		tspan.appendChild (document.createTextNode (""));
		this.tooltip_text.appendChild (tspan);

		// operation

		var tspan  = document.createElement("tspan");

		tspan.setAttribute ('x', '5');
		tspan.setAttribute ('y', 45);

		tspan.appendChild (document.createTextNode ("operation: " + evt.operation));
		this.tooltip_text.appendChild (tspan);

		// interaction point

		//var tspan  = document.createElement("tspan");

		//tspan.setAttribute ('x', '5');
		//tspan.setAttribute ('y', 60);

		//tspan.appendChild (document.createTextNode ("interaction point: " + evt.interactionPoint));
		//this.tooltip_text.appendChild (tspan);

		// timestamp

		var tspan  = document.createElement("tspan");

		tspan.setAttribute ('x', '5');
		tspan.setAttribute ('y', 60);

		tspan.appendChild (document.createTextNode ("timestamp: " + evt.timeStamp));
		this.tooltip_text.appendChild (tspan);

//just

		// matched message

		var tspan  = document.createElement("tspan");
		tspan.setAttribute ('x', '5');
		tspan.setAttribute ('y', 90);
		tspan.setAttribute ('style', 'fill:violet');
//MatchedMsg
		//to do: how to get them from EventLoggingInt.java .. parameter not defined (parametersList).. 
		if (evt.operation.match('MatchedMsg'))
		{ 
		tspan.appendChild (document.createTextNode ("MatchedMsg_Content: " + evt.timeStamp));
		this.tooltip_text.appendChild (tspan);
		}

		// coming from or going to
		var tspan  = document.createElement("tspan");

		tspan.setAttribute ('x', '5');
		tspan.setAttribute ('y', 75);

//justka
//cos jest nie tak z source & destination 
		if (e2)
		{
			if ( evt.interactionPoint.match ('IN') )
				tspan.appendChild (document.createTextNode ("destination: " + e2.type + ", " + e2.value));
			else
				tspan.appendChild (document.createTextNode ("source: " + e2.type + ", " + e2.value));

			this.tooltip_text.appendChild (tspan);
		}
	}
	this.setTooltipSize();
}



/**
 * Clears the tooltip text
 */
SVGController.prototype.clearTooltipText= function()
{
	var item = this.tooltip_text.firstChild();

	while (item)
	{
	   var next	= item;
	   item 	= next.nextSibling();

	   this.tooltip_text.removeChild(next);
	}
}

/**
 * called by the <a href='#set_mouse_ctx'>set_mouse_ctx</a> routine when the user is hoovering with the mouse over a
 * graphical representation of an event or entity/identity.
 */
SVGController.prototype.showTooltip = function()
{
	if (this.mtx)
		return;

	var v1 = this.mouse_target.getAttribute ('id');
	var v2 = this.mouse_target.getParentNode().getParentNode().getAttribute ('id');


	// add text for selected object type (entity / event)

	this.addTooltipText (v1, v2);

	// scale and move panel to the right position

	var scale = document.rootElement.currentScale;
	var trans = document.rootElement.currentTranslate;

	var dx = (evt.getClientX() + 15 - trans.x) / scale;
	var dy = (evt.getClientY() + 15 - trans.y) / scale;

	// only allow zooming in

	if ( scale >= 1 )
		this.tooltip_panel.setAttribute("transform", "translate(" + dx + "," + dy + ")");
	else
		this.tooltip_panel.setAttribute("transform", "translate(" + dx + "," + dy + ") scale(" +  (1/scale) + ")");

	// show tooltip panel

	this.tooltip_panel.setAttribute ("visibility","visible");
}

/**
 * called by the <a href='#clear_mouse_ctx'>clear_mouse_ctx</a> routine when the user is hoovering with the mouse from the
 * graphical representation of an event or entity/identity.
 */
SVGController.prototype.hideTooltip = function()
{
	// hide tooltip panel

	this.tooltip_panel.setAttribute ("visibility", "hidden");

	// clear tooltip text

	this.clearTooltipText();

	// ensure that tooltip panel stays on top of the z-order

	if ( document.rootElement.lastChild != this.tooltip_panel)
	{
		document.rootElement.removeChild (this.tooltip_panel);
		document.rootElement.appendChild (this.tooltip_panel);
	}
}

/**
 * This routine is called by the control_tabs.html javascript code after the index.html onload handler detected that all
 * the documents are loaded. Not only the SVG document but also all the HTML documents. The reason that this routine
 * is not called by the index.html javascript code is that we need the instances of the entity/identity hierarchy tree
 * and event parameter tree. Both trees are part of the control_tabs HTML document.
 *
 * @param server URL needed to place request to the tracing server.
 * @param entity_tree Entity/Identity hierarchy tree.
 * @param event_tree Event parameter tree.
 * @param caption HTML control panel caption element
 * @param tabs_doc HTML control_tabs document
 */
SVGController.prototype.init = function (server, entity_tree, event_tree, caption, tabs_doc     )
{
	this.url 			= server;
	this.event_tree 	= event_tree;
	this.entity_tree  	= entity_tree;
	this.caption		= caption;
	this.tabs_doc		= tabs_doc;

	this.caption.innerHTML = 'Entity Control';

	if (this.cookies[2]=='1')
	{
		this.toSelf = true;

		get_element('to_self', this.tabs_doc).checked = true;
	}

	if (this.cookies[3]=='1')
	{
		this.unmatched = true;

		get_element('unmatched', this.tabs_doc).checked = true;
	}

	this.entity_tree.refresh();
	this.entity_tree.show();
	this.getEventCount();
}

/**
 * Callback routine called from the control_tabs HTML page when the user operates on the 'operation to self' checkbox.
 *
 * @param checkbox Checkbox element.
 */
SVGController.prototype.option1 = function (checkbox)
{
	if ( !this.acquireLock() )
	{
		// restore previous state

		if ( checkbox.checked)
			checkbox.checked = false;
		else
			checkbox.checked = true;

		return;
	}

	this.toSelf = checkbox.checked;
	this.cookies[2] = checkbox.checked ? '1' : '0';

	this.getEvents();
}

/**
 * Callback routine called from the control_tabs HTML page when the user operates on the 'Hide unmatched events' checkbox.
 *
 * @param checkbox Checkbox element.
 */
SVGController.prototype.option2 = function (checkbox)
{
	if ( !this.acquireLock() )
	{
		// restore previous state

		if ( checkbox.checked )
			checkbox.checked = false;
		else
			checkbox.checked = true;

		return;
	}


	this.unmatched = checkbox.checked;
	this.cookies[3] = checkbox.checked ? '1' : '0';

	this.getEvents();
}

/**
 * Callback routine called from the control_tabs HTML page when the user operates on the 'Filter Out Operation:' menu.
 *
 * @param menu Menu element.
 */
SVGController.prototype.hideOperation = function (menu)
{
	if ( !this.acquireLock() )
	{
		// restore previous state

		this.updateFilterMenus();

		return;
	}

	if ( this.operationList.containsKey(menu.value) )
	{
		var flag = this.operationList.get (menu.value);

		if ( !this.operationList.get (menu.value) )
		{
			this.operationList.put(menu.value, true);

			this.updateFilterMenus();

			this.getEvents();
		}
	}
}

/**
 * Callback routine called from the control_tabs HTML page when the user operates on the 'Remove Operation Filter:' menu.
 *
 * @param menu Menu element.
 */
SVGController.prototype.showOperation = function (menu)
{
	if ( !this.acquireLock() )
	{
		// restore previous state

		this.updateFilterMenus();

		return;
	}

	if ( menu.value == 'all' )
	{
		for (var i in this.operationList.values() )
			this.operationList.values()[i] = false;

		this.updateFilterMenus();

		this.getEvents();
	}
	else if ( this.operationList.containsKey(menu.value) )
	{
		if ( this.operationList.get (menu.value) )
		{
			this.operationList.put(menu.value, false);

			this.updateFilterMenus();

			this.getEvents();
		}
	}
}

/**
 * Called when the user hoovers with the mouse over the text of an entity/identity tree node.
 *
 * @param window Web browser window.
 * @param id Branch id of the tree node.
 */
SVGController.prototype.updateStatus = function (window, id)
{
	id = id.substring (2, id.length);

	var node = this.entities.getNode (id);

	if ( node )
		window.status = node.type + ': ' + node.value;
}

/**
 * Callback routine called from the entity/identity tree when the user modifies the tree view to collapse or expand a
 * tree fragment.
 *
 * @param id Branch id of the tree node the user operates upon.
 * @param action Flag indication if the specified tree node is being collapsed or expanded.
 */
SVGController.prototype.collapseExpand = function(id, action)
{
	id = id.substring (2, id.length);

	var node = this.entities.getNode (id);

	if ( node )
	{
		if (action == 'collapse')
			node.expanded = false;
		else if (action == 'expand')
			node.expanded = true;

		this.getEvents();
	}
}

/**
 * Callback routine called from the entity/identity tree when the user modifies the tree view to visualize a
 * tree fragment.
 *
 * @param node Branch id of the tree node the user operates upon.
 * @param children All children underneath the specified node for which the visual flag must be set to true as well.
 */
SVGController.prototype.show = function (node, children)
{
	this.entities.getNode (node.id.substring(2, node.id.length)).visible = true;

	for (var i in children)
		this.entities.getNode (children[i].id.substring(2, children[i].id.length)).visible = true;

	this.getEvents();
}

/**
 * Callback routine called from the entity/identity tree when the user modifies the tree view to hide a
 * tree fragment.
 *
 * @param node Branch id of the tree node the user operates upon.
 * @param children All children underneath the specified node for which the visual flag must be set to false as well.
 */
SVGController.prototype.hide = function (node, children)
{
	this.entities.getNode (node.id.substring(2, node.id.length)).visible = false;

	for (var i in children)
		this.entities.getNode (children[i].id.substring(2, children[i].id.length)).visible = false;

	this.getEvents();
}

/**
 * This routine is called by the <a href='#operationComplete'>operationComplete</a> routine to update the entity/identity
 * hierarchy tree after retrieving entities from the server.
 *
 * @param id Branch id of the parent node this new node must be appended to as a child.
 * @param tooltip Tooltip to show when the user moves with the mouse over this node.
 * @param img Node tree images.
 * @param value Entity/Identity name.
 */
SVGController.prototype.updateControlView = function (id, tooltip, img, value)
{
	var child = this.entity_tree.createNode (img, tooltip, value, this);

	if (!id)
	{
		// update root

		this.entity_tree.appendChild (child);
	}
	else
	{
		// update node with given id

		var node = this.entity_tree.getNode ('0_' + id);

		if ( node )
			node.appendChild (child);
	}
}

/**
 * This routine is called by the index HTML page javascript code after all documents, SVG as well as HTML are loaded.
 *
 * @param panel SVG page control document.
 * @param cookies Setting stored during the previous session.
 * @param ctrl_panel_doc HTML control_panel document.
 */
SVGController.prototype.html_init = function(panel, cookies, ctrl_panel_doc)
{
	this.cookies = cookies;

	var p = parseInt(this.cookies[0])

	this.set_step_size (p);

	get_element ('step_size_ctrl', ctrl_panel_doc).selectedIndex = Math.round(p/25)-1;

	this.ctrl_panel = panel;
}

/**
 * Routine is used to set the page control step size. In the x direction the step size is defined as
 * a percentage of the view panel width, in the y direction as a percentage of the number of events that fit on
 * the visible view panel window area.
 *
 * @param p Percentage value set by the user.
 */
SVGController.prototype.set_step_size = function(p)
{
	this.step_size_y = p  / 100;

	this.cookies[0] = p;
}

/**
 * Called when the user clicks the page control 'west' button.
 */
SVGController.prototype.west = function ()
{
	if ( this.visibleEntities.length == 0 )
		return;

	document.rootElement.currentTranslate.x += this.step_size_x;

	if ( document.rootElement.currentTranslate.x  > 0 )
		document.rootElement.currentTranslate.x = 0;

	this.hScrollOffset	= -document.rootElement.currentTranslate.x/(SVGController.X_SPACE * this.visibleEntities.length);

	this.ctrl_panel.setHSlider(this.hScrollOffset);
}

/**
 * Called when the user clicks the page control 'east' button.
 */
SVGController.prototype.east = function ()
{
	if ( this.visibleEntities.length == 0 )
		return;

	document.rootElement.currentTranslate.x -= this.step_size_x

	this.hScrollOffset	=-document.rootElement.currentTranslate.x/(SVGController.X_SPACE * this.visibleEntities.length);

	this.ctrl_panel.setHSlider(this.hScrollOffset);
}

/**
 * Called when the user clicks the page control 'home' button.
 */
SVGController.prototype.home = function ()
{
	document.rootElement.currentTranslate.x = 0;
	document.rootElement.currentTranslate.y = 0;
	this.hScrollOffset						= 0;

	this.eventCursor = 0;

	this.ctrl_panel.setVSlider (0);
	this.ctrl_panel.setHSlider (0);

	this.getEvents();
}

/**
 * Called when the user clicks the page control 'north' button.
 */
SVGController.prototype.north = function ()
{
	this.eventCursor -= Math.round (this.step_size_y * this.events_on_page );

	if ( this.eventCursor <= 0 )
		this.eventCursor = 0;

	if (this.eventCount)
		this.ctrl_panel.setVSlider (this.eventCursor/this.eventCount);

	this.getEvents();
}

/**
 * Called when the user clicks the page control 'south' button.
 */
SVGController.prototype.south = function ()
{
	this.eventCursor += Math.round (this.step_size_y * this.events_on_page );

	if ( this.eventCursor > this.eventCount)
		this.eventCursor = this.eventCount;

	this.ctrl_panel.setVSlider (this.eventCursor/this.eventCount);

	this.getEvents();
}

/**
 * Called when the user acts on the page control vertical slider.
 */
SVGController.prototype.setVScrollOffset = function (py)
{
	this.vScrollOffset = py;

	this.getEventCount();
}

/**
 * Called when the user acts on the page control horizontal slider.
 */
SVGController.prototype.setHScrollOffset = function (px)
{
	this.hScrollOffset = px;

	document.rootElement.currentTranslate.x =  -SVGController.X_SPACE * this.visibleEntities.length * px;
}

SVGController.X_SPACE 		= 150;
SVGController.Y_SPACE 		= 15;
SVGController.identityKind 	= ['DOMAIN', 'PLATFORM', 'SESSION', 'CONTAINER', 'COMPONENT_Type', 'ComponentID'];

var image = new Array(7);

/**
 * Loads offscreen entity/identity images into cache
 */
function preloadIdentityImages()
{
	image[0] 	= new Image();
	image[0].src= top.path + '/images/leaf.gif';
	image[1] 	= new Image();
	image[1].src= top.path + '/images/leaf.gif';
	image[2] 	= new Image();
	image[2].src= top.path + '/images/leaf.gif';
	image[3] 	= new Image();
	image[3].src= top.path + '/images/leaf.gif';
	image[4] 	= new Image();
	image[4].src= top.path + '/images/leaf.gif';
	image[5] 	= new Image();
	image[5].src= top.path + '/images/leaf.gif';
	image[6] 	= new Image();
	image[6].src= top.path + '/images/properties.gif';
}

preloadIdentityImages();

SVGController.prototype.updateFilterMenus = function()
{
	var fo = get_element('filter_out', this.tabs_doc);
	var fi = get_element('filter_remove',  this.tabs_doc);

	fo.options.length = 0;
	fi.options.length = 0;

	var methods = this.operationList.keys();

	fo.options[0] = new Option('-------- select --------', '', true, false);
	fi.options[0] = new Option('-------- select --------', '', true, false);
	fi.options[1] = new Option('All Operations', 'all', false, false);

	for (var i in methods)
	{
		if ( !this.operationList.get(methods[i]) )
			fo.options[fo.options.length] = new Option(methods[i], methods[i], false, false);
		else
			fi.options[fi.options.length] = new Option(methods[i], methods[i], false, false);
	}

	if ( fi.options.length < 3 )
		fi.disabled  = true;
	else
		fi.disabled  = false;
}

SVGController.prototype.showEventTree = function ()
{
	this.event_tree.refresh();
	this.event_tree.show();
}

SVGController.prototype.getEntities = function ()
{
	if ( this.params.length > 0 )
	{
		this.request  	= 'entities';
		this.callback 	= 'this.refresh()';
		this.serverRequest ();
		return;
	}
	this.refresh();
}

SVGController.prototype.getEventCount = function ()
{
	this.callback 	= 'this.getEvents()';
	this.request  	= 'event_count';
	this.serverRequest ();
}

SVGController.prototype.getEvents = function ()
{
	this.callback 	 = 'this.getEntities()';
	this.request  	 = 'events';
	this.serverRequest ();
}

SVGController.prototype.operationComplete = function(status)
{
	this.wait = false;

	if (status.success)
	{
		if ( this.request == 'entities' )
		{
			// parse xml information

			var xml_doc = parseXML(status.content);

			var xml_units = xml_doc.getElementsByTagName ('entity');

			// create entity tree structure

			for (var i = 0; i < xml_units.length; i++)
			{
				var node 	= this.entities;
				var key		= xml_units.item(i).getAttribute('id');

				for (var j = 0; j < SVGController.identityKind.length; j++ )
				{
					var identity = xml_units.item(i).getElementsByTagName (SVGController.identityKind[j]);

					if ( identity && identity.getLength() > 0 )
					{
						var child = node.hasChild (identity.item(0).getAttribute('name'));

						if (!child)
						{
							this.updateControlView (node.id, SVGController.identityKind[j], image[j].src, identity.item(0).getAttribute('name'));

							node = node.appendChild (new EntityNode (SVGController.identityKind[j], identity.item(0).getAttribute('name'), key));
						}
						else
							node = child;
					}
				}

				this.entityList[key] = node.id;
			}

			this.entity_tree.refresh();
		}
		else if ( this.request == 'event_count' )
		{
			this.eventCount = parseInt(status.content) - this.hideEventsTo;

			this.eventCursor = Math.round (this.eventCount * this.vScrollOffset);
		}
		else if ( this.request == 'events')
		{
			// parse xml information

			var xml_doc = parseXML(status.content);

			this.eventCount = xml_doc.getElementsByTagName ('count').item(0).getFirstChild().data();

			var xml_events = xml_doc.getElementsByTagName ('traceEvent');

			this.events.clear();

			// clear params - used to retrieve new entities after this part is finished

			this.params = new Array();

			for (var i = 0; i < xml_events.length; i++)
			{
				// put events into lookup hashtable

				var event = new Event (xml_events.item(i), this);

				this.events.put (event.id, event);

				// check for new entities

				if (!this.entityList[event.entityKey] )
				{
					if ( !this.params.join(' ').match(event.entityKey) )
						this.params[this.params.length] = event.entityKey;
				}
			}
		}
		else if ( this.request == 'parameters' )
		{
			// parse xml information

			var xml_doc = parseXML(status.content);

			var tree = new XMLParameterTree (this.event_tree, xml_doc);
//23.04
//			var text = new XMLParameterText (this.event_tree, xml_doc);
//			text.update ();
			tree.update ();


		}

		// execute callback routine

		if ( this.callback )
			eval (this.callback);
	}
	else
		alert ("No connection with the Tracing Server could be made");

	this.ctrl_panel.top.defaultStatus = 'Done';
}

SVGController.prototype.serverRequest = function (value)
{
	this.ctrl_panel.top.defaultStatus = "Please wait...";
	this.wait = true;

	if ( this.request == 'events')
	{
		var operation_filter = new Array();

		for (var i in this.operationList.values())
		{
			if ( this.operationList.values()[i] )
				operation_filter[operation_filter.length] = i;
		}

		var hidden = this.entities.getHidden();

		var collapsed = this.entities.getCollapsed();

		var params =
			['start=' + (value ? value : this.eventCursor),
			 'length='+ this.events_on_page,
			 'operation_filter='+ operation_filter.join('+'),
			 'entity_filter=' + hidden.join('+'),
			 'toself_filter=' + (this.toSelf ? collapsed.join('+') : ''),
			 'unmatched_filter=' + (this.unmatched ? 'true' : 'false'),
			 'hide_to=' + this.hideEventsTo
			];

		getURL(this.url + '?' + this.request + '&' + params.join('&'), this);
	}
	else if ( this.request == 'event_count' )
	{
		getURL(this.url + '?' + this.request, this);
	}
	else if ( this.request == 'entities' )
	{
		getURL(this.url + '?' + this.request + '&keys=' + this.params.join('+'), this);
	}
	else if ( this.request == 'parameters' )
	{
		getURL(this.url + '?' + this.request + '&key=' + value, this);
	}
}

SVGController.prototype.createTooltipPanel = function()
{
	this.tooltip_panel = document.createElement ("g");

	this.tooltip_panel.setAttribute ("visibility", "hidden");

	var rect = document.createElement ("rect");

	rect.setAttribute ("style", "fill:#ffffcc; stroke:black; stroke-width:0.3");
	rect.setAttribute ("rx", "6");
	rect.setAttribute ("ry", "6");

	this.tooltip_panel.appendChild (rect);

	this.tooltip_text = document.createElement ("text");

	this.tooltip_text.setAttribute ("class", "tt");

	this.tooltip_panel.appendChild (this.tooltip_text);

	document.rootElement.appendChild (this.tooltip_panel);
}



var tree_image = new Array(2);

/**
 * Loads offscreen parameter images into cache
 */
function preloadParameterImages()
{
	tree_image[0] 		= new Image();
	tree_image[0].src	= top.path + '/images/folder.gif';
	tree_image[1] 		= new Image();
	tree_image[1].src	= top.path + '/images/leaf.gif';
}

preloadParameterImages();


/**
 * This class is used to interpret the received XML parameter information. The information is translated
 * into a HTML tree structure that gives the user a graphical representation of the invocation parameters.
 *
 * @param html_tree HTML tree object where the parameter information is placed into.
 * @param xml_doc Contains the parser XML parameter information.
 * @constructor
 */
function XMLParameterTree (html_tree, xml_doc)
{
	this.reply = false;
	this.xml   = xml_doc;
	this.valid = true;
	this.tree  = html_tree;

	// check if we have operation parameters

	this.invocation = xml_doc.getElementsByTagName ('operation');

	if (!this.invocation)
	{
		// this shouldn't happen

		this.valid = false;

		return;
	}

	if ( this.invocation.length == 0 )
	{
		// must be a reply

		this.invocation = xml_doc.getElementsByTagName ('reply');

		if ( this.invocation.length == 0 )
		{
			// this shouldn't happen

			this.valid = false;

			return;
		}

		this.reply = true;
	}
}

/**
 * This routine returns the first DOM child node of type 'element'.
 *
 * @param node DOM node to examine.
 * @returns The first child element of type 'element'. null if not present.
 */
XMLParameterTree.prototype.firstChildElement = function (node)
{
	node = node.firstChild();

	while (node)
	{
		if ( node.nodeType() == 1 ) // element type
			return node;

		if ( node.hasChildNodes() )
			this.walk (node.firstChild());

		node = node.nextSibling();
	}

	return null;
}

/**
 * This routine walks through a given DOM fragment looking for nodes of type 'element'. When an 'element' type is found
 * it calls the <a href='#updateTree'>updateTree</a> routine.
 *
 * @param dom_node DOM fragment to examine.
 * @param tree_node Argument to pass on to the updateTree routine.
 */
XMLParameterTree.prototype.walk = function (dom_node, tree_node)
{
	while (dom_node)
	{
		if ( dom_node.nodeType() == 1 )
			this.updateTree (dom_node, tree_node);
		else
		{
			if ( dom_node.hasChildNodes() )
				this.walk (dom_node.firstChild(), tree_node);
		}

		dom_node = dom_node.nextSibling();
	}
}

/**
 * This routine is called to update the HTML parameter tree. It is called by the
 * <a href='SVGController.html#operationComplete'>SVGController.operationComplete</a> routine upon
 * reception of the parameter information from the server.
 */
XMLParameterTree.prototype.update = function ()
{
	if (!this.valid)
	{
		this.root = this.tree.createNode (tree_image[0].src, '', 'No parameters');

		this.tree.appendChild (this.root);

		return;
	}

	this.root = this.tree.createNode (tree_image[0].src, '', (this.reply ? "Reply" : "Operation") + ": " + this.tree.getNode ("0_1_3").value);

	this.tree.appendChild (this.root);

	// walk the document

	this.walk (this.invocation.item(0).firstChild(), this.root);
}

/**
 * This routine acts as a switch statement to test for all possible parameter types that must be handled. If it finds a
 * known parameter type, a corresponding routine will update the HTML tree for that particular type.
 *
 * @param dom_node DOM fragment to examine.
 * @param tree_node The parameter information will be added as a child to this HTML tree node.
 */
XMLParameterTree.prototype.updateTree = function (dom_node, tree_node)
{
	this.direction 	(dom_node, tree_node);
	this.data 		(dom_node, tree_node);
	this.struct 	(dom_node, tree_node);
	this.exception 	(dom_node, tree_node);
	this.sequence   (dom_node, tree_node);
	this.member		(dom_node, tree_node);
	this.array		(dom_node, tree_node);
	this.any		(dom_node, tree_node);
	this.union		(dom_node, tree_node);
	this.interface  (dom_node, tree_node);
	this.valuetype  (dom_node, tree_node);
	this.enumeration(dom_node, tree_node);
}

/**
 * Updates the HTML tree with <b>directional</b> information.
 *
 * @param dom_node DOM fragment to examine.
 * @param tree_node The parameter information will be added as a child to this HTML tree node.
 */
XMLParameterTree.prototype.direction = function (dom_node, tree_node)
{
	var tag = dom_node.nodeName();

	if ( tag == 'in' || tag == 'inout' || tag == 'out' || tag == 'return' )
	{
		try
		{
			var child = this.tree.createNode (tree_image[0].src, '', tag + " " + this.firstChildElement(dom_node).nodeName() + " " + dom_node.getAttribute('name'));

			child.expanded = false;

			tree_node.appendChild (child);

			this.walk (dom_node.firstChild(), child);
		}
		catch (Error)
		{
			alert ("Empty " + tag + " element");
		}
	}
}

/**
 * Updates the HTML tree with <b>simple</b> parameter type information.
 *
 * @param dom_node DOM fragment to examine.
 * @param tree_node The parameter information will be added as a child to this HTML tree node.
 */
XMLParameterTree.prototype.data = function (dom_node, tree_node)
{
	var tag = dom_node.nodeName();

	if (tag == 'boolean' 	||
		tag == 'octet'		||
		tag == 'short'		||
		tag == 'ushort'		||
		tag == 'long'		||
		tag == 'ulong'		||
		tag == 'longlong'	||
		tag == 'ulonglong'	||
		tag == 'float'		||
		tag == 'double'		||
		tag == 'longdouble'	||
		tag == 'char'		||
		tag == 'wchar'		||
		tag == 'string'		||
		tag == 'wstring'	||
		tag == 'fixed'		||
		tag == 'object'		||
		tag == 'tag'		)

	{
		var data = '';

		if ( dom_node.firstChild() )
			data = dom_node.firstChild().data;

		var child = this.tree.createNode (tree_image[1].src, tag, data);

		tree_node.appendChild (child);
	}
}

/**
 * Updates the HTML tree with <b>enum</b> parameter information.
 *
 * @param dom_node DOM fragment to examine.
 * @param tree_node The parameter information will be added as a child to this HTML tree node.
 */
XMLParameterTree.prototype.enumeration = function (dom_node, tree_node)
{
	var tag = dom_node.nodeName();

	if ( tag == 'enum' )
	{
		var child = this.tree.createNode (tree_image[1].src, 'Enum', tag + " " + dom_node.getAttribute('id') + " = " + (dom_node.firstChild() ? dom_node.firstChild().data : ' '));

		tree_node.appendChild (child);
	}
}

/**
 * Updates the HTML tree with <b>struct</b> parameter information.
 *
 * @param dom_node DOM fragment to examine.
 * @param tree_node The parameter information will be added as a child to this HTML tree node.
 */
XMLParameterTree.prototype.struct = function (dom_node, tree_node)
{
	var tag = dom_node.nodeName();

	if ( tag == 'struct' )
	{
		var child = this.tree.createNode (tree_image[0].src, 'Struct', tag + " " + dom_node.getAttribute('id'));

		child.expanded = false;

		tree_node.appendChild (child);

		this.walk (dom_node.firstChild(), child);
	}
}

/**
 * Updates the HTML tree with <b>exception</b> parameter information.
 *
 * @param dom_node DOM fragment to examine.
 * @param tree_node The parameter information will be added as a child to this HTML tree node.
 */
XMLParameterTree.prototype.exception = function (dom_node, tree_node)
{
	var tag = dom_node.nodeName();

	if ( tag == 'exception' )
	{
		var child = this.tree.createNode (tree_image[0].src, 'Exception', tag + " " + dom_node.getAttribute('id'));

		child.expanded = false;

		tree_node.appendChild (child);

		this.walk (dom_node.firstChild(), child);
	}
}

/**
 * Updates the HTML tree with <b>sequence</b> parameter information.
 *
 * @param dom_node DOM fragment to examine.
 * @param tree_node The parameter information will be added as a child to this HTML tree node.
 */
XMLParameterTree.prototype.sequence = function (dom_node, tree_node)
{
	var tag = dom_node.nodeName();

	if ( tag == 'sequence' )
	{
		var child = this.tree.createNode (tree_image[0].src, 'Sequence', tag + " " + dom_node.getAttribute('id') + "  length = " + dom_node.getAttribute('length'));

		child.expanded = false;

		tree_node.appendChild (child);

		this.walk (dom_node.firstChild(), child);
	}
}

/**
 * Updates the HTML tree with <b>array</b> parameter information.
 *
 * @param dom_node DOM fragment to examine.
 * @param tree_node The parameter information will be added as a child to this HTML tree node.
 */
XMLParameterTree.prototype.array = function (dom_node, tree_node)
{
	var tag = dom_node.nodeName();

	if ( tag == 'array' )
	{
		var child = this.tree.createNode (tree_image[0].src, 'Array', tag + " " + dom_node.getAttribute('id') + "  length = " + dom_node.getAttribute('length'));

	//	child.expanded = false;

	//	tree_node.appendChild (child);

	//	this.walk (dom_node.firstChild(), child);
	}
}

/**
 * Updates the HTML tree with <b>any</b> parameter information.
 *
 * @param dom_node DOM fragment to examine.
 * @param tree_node The parameter information will be added as a child to this HTML tree node.
 */
XMLParameterTree.prototype.any = function (dom_node, tree_node)
{
	var tag = dom_node.nodeName();

	if ( tag == 'any' )
	{
		var data = this.firstChildElement(dom_node);

		var child = this.tree.createNode (tree_image[1].src, 'Any', (data ? data.nodeName() : ''));

		child.expanded = false;

		tree_node.appendChild (child);

		this.walk (dom_node.firstChild(), tree_node);
	}
}

/**
 * Updates the HTML tree with <b>member</b> parameter information.
 *
 * @param dom_node DOM fragment to examine.
 * @param tree_node The parameter information will be added as a child to this HTML tree node.
 */
XMLParameterTree.prototype.member = function (dom_node, tree_node)
{
	var tag = dom_node.nodeName();

	if ( tag == 'member' )
	{
		var child = this.tree.createNode (tree_image[0].src, 'Member', this.firstChildElement(dom_node).nodeName() + " " + dom_node.getAttribute('name'));

		child.expanded = false;

		tree_node.appendChild (child);

		this.walk (dom_node.firstChild(), child);
	}
}

/**
 * Updates the HTML tree with <b>valuetype</b> parameter information.
 *
 * @param dom_node DOM fragment to examine.
 * @param tree_node The parameter information will be added as a child to this HTML tree node.
 */
XMLParameterTree.prototype.valuetype = function (dom_node, tree_node)
{
	var tag = dom_node.nodeName();

	if ( tag == 'valuetype' )
	{
		var child = this.tree.createNode (tree_image[0].src, 'ValueType', tag + " " + dom_node.getAttribute('id'));

		child.expanded = false;

		tree_node.appendChild (child);

		this.walk (dom_node.firstChild(), child);
	}
}

/**
 * Updates the HTML tree with <b>union</b> parameter information.
 *
 * @param dom_node DOM fragment to examine.
 * @param tree_node The parameter information will be added as a child to this HTML tree node.
 */
XMLParameterTree.prototype.union = function (dom_node, tree_node)
{
	var tag = dom_node.nodeName();

	if ( tag == 'union' )
	{
		var child = this.tree.createNode (tree_image[0].src, 'Union', tag + " " + dom_node.getAttribute('id'));

		child.expanded = false;

		tree_node.appendChild (child);

		this.walk (dom_node.firstChild(), child);
	}
}

/**
 * Updates the HTML tree with <b>interface</b> parameter information.
 *
 * @param dom_node DOM fragment to examine.
 * @param tree_node The parameter information will be added as a child to this HTML tree node.
 */
XMLParameterTree.prototype.interface = function (dom_node, tree_node)
{
	var tag = dom_node.nodeName();

	if ( tag == 'interface' )
	{
		var child = this.tree.createNode (tree_image[1].src, 'Interface', tag + " " + dom_node.getAttribute('id') + " = " + (dom_node.firstChild() ? dom_node.firstChild().data : ' '));

		tree_node.appendChild (child);
	}
}


XMLParameterTree.prototype.addTooltipText2 = function (v22,dom_node, tree_node)
{

	{
	var tag = dom_node.nodeName();
var v22 = this.mouse_target.getParentNode().getParentNode().getAttribute ('id');

		var evt2 = this.events.get(v22);

		var e12 = this.entities.getNode(this.entityList[evt2.entityKey]);
//		var e22 = null;
// matched message

		var tspan  = document.createElement("tspan");
		tspan.setAttribute ('x', '5');
		tspan.setAttribute ('y', 90);
		tspan.setAttribute ('style', 'fill:cyan');
data(dom_node, tree_node);
		if (evt2.operation.match('MatchedMsg'))
		{ 

	var tag = dom_node.nodeName();

	if ( tag == 'string' )
	{
		var child = this.tree.createNode (tree_image[1].src, 'string', tag + " " + dom_node.getAttribute('id') + " = " + (dom_node.firstChild() ? dom_node.firstChild().data : ' '));

		tree_node.appendChild (child);
	}

		tspan.appendChild (document.createTextNode ("MatchedMsg_Content: " + evt2.timeStamp));
		this.tooltip_text.appendChild (tspan);
		}

		// coming from or going to
		var tspan  = document.createElement("tspan");

		tspan.setAttribute ('x', '115');
		tspan.setAttribute ('y', 75);
	}
	this.setTooltipSize();
}



SVGController.prototype.addTooltipText2 = function (v22, dom_node, tree_node)
{

	{
	var tag = dom_node.nodeName();
var v22 = this.mouse_target.getParentNode().getParentNode().getAttribute ('id');

		var evt2 = this.events.get(v22);

		var e12 = this.entities.getNode(this.entityList[evt2.entityKey]);
//		var e22 = null;
// matched message

		var tspan  = document.createElement("tspan");
		tspan.setAttribute ('x', '5');
		tspan.setAttribute ('y', 90);
		tspan.setAttribute ('style', 'fill:cyan');
data(dom_node, tree_node);
		if (evt2.operation.match('MatchedMsg'))
		{ 

	var tag = dom_node.nodeName();

	if ( tag == 'string' )
	{
		var child = this.tree.createNode (tree_image[1].src, 'string', tag + " " + dom_node.getAttribute('id') + " = " + (dom_node.firstChild() ? dom_node.firstChild().data : ' '));

		tree_node.appendChild (child);
	}

		tspan.appendChild (document.createTextNode ("MatchedMsg_Content: " + evt2.timeStamp));
		this.tooltip_text.appendChild (tspan);
		}

		// coming from or going to
		var tspan  = document.createElement("tspan");

		tspan.setAttribute ('x', '115');
		tspan.setAttribute ('y', 75);
	}
	this.setTooltipSize();
}
