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

var icons = new Array(9);

/**
 * Loads offscreen tree images into cache
 */
function preloadTreeImages()
{
	icons[0] 		= new Image();
	icons[0].src	= top.path + '/images/empty.gif';
	icons[1] 		= new Image();
	icons[1].src 	= top.path + '/images/line.gif';
	icons[2] 		= new Image();
	icons[2].src 	= top.path + '/images/root.gif';
	icons[3] 		= new Image();
	icons[3].src 	= top.path + '/images/plusbottom.gif';
	icons[4] 		= new Image();
	icons[4].src	= top.path + '/images/minusbottom.gif';
	icons[5] 		= new Image();
	icons[5].src	= top.path + '/images/joinbottom.gif';
	icons[6] 		= new Image();
	icons[6].src	= top.path + '/images/join.gif';
	icons[7] 		= new Image();
	icons[7].src	= top.path + '/images/minus.gif';
	icons[8] 		= new Image();
	icons[8].src	= top.path + '/images/plus.gif';
}

preloadTreeImages();

/**
 * This class provides the methods to create an HTML tree dynamically. Creation usually takes the following form:
 * <pre>
 *     var tree = new Tree ('my tree');
 *
 *	   var firstChild  = tree.createNode (icon, 'tooltip text 1', value1);
 *	   var secondChild = tree.createNode (icon, 'tooltip text 2', value2);
 *
 *     tree.appendChild (firstChild);
 *     tree.appendChild (secondChild);
 *
 * 	   tree.refresh();
 *     tree.show();
 * </pre>
 * @param name Tree name (<b>warning:</b> The name must be unique in case there are multiple trees within a single HTML document.)
 */
function Tree (name)
{
	this.hidden		= false;
	this.target 	= null;
	this.id 		= '0';
	this.value		= name;
	this.tooltip	= 'root';
	this.parent		= null;
	this.root		= this;
	this.nodes		= new Array();
	this.children	= new Array();
	this.dom 		= document.createElement ('div');

	this.dom.setAttribute ('id', name +'_div_0');

	this.dom.style.display = 'none';
	this.dom.tree		   = this;

	document.body.appendChild (this.dom);
}

Tree.prototype 	= new TreeNode();

/**
 * Visualizes the tree.
 */
Tree.prototype.show = function ()
{
	this.dom.style.display = 'block';

	this.hidden = false;
}

/**
 * Hides the tree (default).
 */
Tree.prototype.hide = function ()
{
	this.dom.style.display = 'none';

	this.hidden = true;
}

/**
 * Removes all tree nodes except for the root node.
 */
Tree.prototype.clear = function ()
{
	var item = this.firstChild();

	while (item)
	{
	   var next	= item;
	   item 	= next.nextSibling();

	   this.removeChild(next);
	}
}

/**
 * Returns the node for the given branch id. The branch id represents the node position within the tree.
 * For a tree with two child nodes this will be for example:
 * <pre>
 * root:                  id = 0
 * first child of root:   id = 0_1
 * second child of root:  id = 0_2
 * </pre>
 * @param id Branch id of the requested node.
 * @returns The node for the given branch id or null if not present.
 */
Tree.prototype.getNode = function (id)
{
	if ( id == '0' )
		return this;

	var branch  = id.split('_');
	var node	= this;

	for (var i = 1; i < branch.length; i++)
	{
		node = node.children[parseInt(branch[i])-1]

		if ( !node )
			return null;
	}

	return node;
}

/**
 * Creates a new tree node.
 * @param target Tree event callbacks class instance, may be omitted.
 * @param value  The tree node text.
 * @param tooltip The popup text shown when hoovering with the mouse over the tree node icon.
 * @param icon The tree node icon (size must be 19x16)
 * @returns The created node.
 */
Tree.prototype.createNode = function (icon, tooltip, value, target)
{
	return new TreeNode (icon, tooltip, value, target);
}


/**
 * Updates the HTML document (the tree view). This routine must be called after modifying the tree to visualize the changes.
 */
Tree.prototype.refresh = function ()
{
	this.dom.innerHTML = this.updateDOM();
}

/**
 * Private method used by the refresh() routine.
 */
Tree.prototype.updateDOM = function (node)
{
	// if node is null then we start at root level

	if (!node)
		node = this.root;

	var html = new StringBuffer();

	html.write("<table cellpadding='0' cellspacing='0' border='0'>");

	if ( node.parent )
	{
		// create horizontal offset by means of lines and empty icons depending on above tree structure

		var depth 	= node.id.split ('_').length -1;
		var	item 	= node;
		var offset	= [];

		for (var i = depth; i > 1; i--)
		{
			offset[i] = "<img align='absbottom' src='" + (item.parent.isLastSibling() ? icons[0].src : icons[1].src) + "'>";

			item = item.parent;
		}

		html.write("<td>" + (depth ? offset.join('') : '') + "</td>");

		// create a join - plus/minus for nodes with children

		if ( node.hasChildren() )
		{
			var join_img;

			if (node.expanded)
				join_img = node.isLastSibling() ? icons[4].src : icons[7].src;
			else
				join_img = node.isLastSibling() ? icons[3].src  : icons[8].src;

			html.write("<td onclick=\"TreeNode.collapseExpand('" + this.root.value + "', '" + node.id + "')\"><img align='absbottom' id='" + this.root.value + "_img_" + node.id + "'  src='" + join_img + "' ></td>");
		}
		else
			html.write("<td><img align='absbottom' src='" + (node.isLastSibling() ? icons[5].src : icons[6].src) + "'></td>");
	}

	// set user icon and text

	var style = 'style='+ (node.selected ? 'font-weight:bold;' : 'font-weight:normal;') + (node.visible ? 'color:blue;' : 'color:red;')

	html.write("<td ondblclick=\"TreeNode.collapseExpand('" + this.root.value + "', '" + node.id + "')\"><img id='" + this.root.value + "_" + node.id + "'class='tree_img' src='" + node.icon + "' alt='" + node.tooltip + "'></td>");
	html.write("<td nowrap='nowrap'><a id='" + this.root.value + "_txt_" + node.id + "' class='tree_txt' " + style + " href=\"javascript:TreeNode.anchor('" + this.root.value + "', '"+ node.id + "')\" onmouseover=\"TreeNode.updateStatus('" + this.root.value + "', '" + node.id + "'); return true;\" onmouseleave=\"status='';\">" + node.value + "</a></td>");
	html.write("</table>");

	node.selected = false;

	// handle child nodes

	if ( node.children.length > 0 )
	{
		html.write("<div id='" +  this.root.value + "_div_" + node.id + "_children' style='display:" + (node.expanded ? 'block' : 'none') + "'>");

		for (var i in node.children)
			html.write (this.updateDOM (node.children[i]));

		html.write("</div>");
	}

	return html.toString();
}

/**
 * Don't use the constructor, use the <a href='Tree.html#createNode'>Tree.createNode</a> routine instead.
 */
function TreeNode (icon, tooltip, value, target)
{
	this.visible		= true;
	this.selected		= false;
	this.icon  			= icon ? icon : icons[2].src;
	this.value 			= value;
	this.expanded		= true;
	this.target 		= target;
	this.tooltip		= tooltip;
	this.children		= new Array();
}

/**
 * Called when the user selects the node text. The text color will alter from blue(default) to red to mark the state change.
 * The next time the text is selected the state will change back. When the target (see <a href='Tree.html#createNode'>Tree.createNode</a>)
 * e.g. is an instance of the SVGController class, this routine will call the
 * <a href='SVGController.html#show'>SVGController.show</a> routine or
 * <a href='SVGController.html#hide'>SVGController.hide</a> routine depending on the state change.
 * @param name  Name of the tree.
 * @param id  Branch id.
 */
TreeNode.anchor = function(name, id)
{
	var node = get_element(name + '_div_0').tree.nodes[id];

	// this routine is for showing/hiding viewpanel elements. For general Tree purposes this routine does nothing

	if ( node && node.target )
	{
	    if ( !node.target.acquireLock() )
	        return;

		if ( node.visible )
		{
			node.visible = false;

			// hide all children as well

			var children = node.getAllChildren();

			for (var i in children )
			{
				children[i].visible = false;

				get_element (name + '_txt_' + children[i].id).style.color='red';
			}

			get_element (name + '_txt_' + id).style.color='red';

			// pass on node and children so that we don't have to determine them again in viewcntl

			node.target.hide(node, children);
		}
		else
		{
			node.visible  = true;

			// show all children as well

			var children = node.getAllChildren();

			for (var i in children )
			{
				children[i].visible = true;

				get_element (name + '_txt_' + children[i].id).style.color='blue';
			}

			get_element (name + '_txt_' + id).style.color='blue';

			// pass on node and children so that we don't have to determine them again in viewcntl

			node.target.show(node, children);
		}
	}
}

TreeNode.updateStatus = function(name, id)
{
    try
    {
    	var node = get_element(name + '_div_0').tree.nodes[id];

        if (node && node.target)
    		node.target.updateStatus(window, id)
    	else
    	{
    	    // local update

            if ( node.tooltip && node.tooltip.length > 0 )
        	    status = node.tooltip + ' - ' + node.value;
        	else
        	    status = node.value;
    	}
    }
    catch (Error)
    {
    }
}


/**
 * Called when the user collapses or expands a tree node. When the target (see <a href='Tree.html#createNode'>Tree.createNode</a>)
 * e.g. is an instance of the SVGController class, this routine will call the
 * <a href='SVGController.html#collapseExpand'>SVGController.collapseExpand</a> routine.
 * @param name  Name of the tree.
 * @param id  Branch id.
 */
TreeNode.collapseExpand =function(name, id)
{
	var node = get_element(name + '_div_0').tree.nodes[id];

	if (node && node.target && !node.target.acquireLock() )
	        return;

	if (node && node.expanded)
	{
		if ( node.children.length > 0)
		{
			get_element(name + '_div_' + id + '_children').style.display 	= 'none';
			get_element(name + '_img_' + id).src = node.isLastSibling() ? icons[3].src  : icons[8].src;
		}

		if (node.target)
			node.target.collapseExpand(id, 'collapse');

		node.expanded = false;
	}
	else if (node && !node.expanded )
	{
		if ( node.children.length > 0)
		{
			get_element(name + '_div_' + id + '_children').style.display 	= 'block';
			get_element(name + '_img_' + id).src = node.isLastSibling() ? icons[4].src : icons[7].src;
		}

		if (node.target)
			node.target.collapseExpand(id, 'expand');

		node.expanded = true;
	}
}

/**
 * Appends specified node to this node's children array.
 * @param node  Node instance to append.
 */
TreeNode.prototype.appendChild = function (node)
{
	this.children[this.children.length] = node;

	node.id		= this.id + '_' + this.children.length;
	node.parent = this;
	node.root 	= this.root;

	this.root.nodes[node.id] = node;
}

/**
 * Returns the first child node of this node's children array.
 * @returns First child node or null if this node has no children.
 */
TreeNode.prototype.firstChild = function ()
{
	if ( this.children.length > 0 )
		return this.children[0];

	return null;
}

/**
 * Returns the last child node of this node's children array.
 * @returns Last child node or null if this node has no children.
 */
TreeNode.prototype.lastChild = function ()
{
	if ( this.children.length > 0 )
		return this.children[this.children.length-1];

	return null;
}

/**
 * Returns the next sibling of this node in the parent's children array
 * @returns The sibling of this node that immediately follows this node or null if this node has no parent or is the parent's last child.
 */
TreeNode.prototype.nextSibling = function ()
{
	if (this.parent)
	{
		var i;

		for (i = 0; i < this.parent.children.length; i++)
		{
			if (this.parent.children[i] == this )
				break;
		}

		var next = i + 1;

		if ( next < this.parent.children.length )
			return this.parent.children[next];
	}

	return null;
}

/**
 * Returns true if this is the last node in the parent's children array.
 */
TreeNode.prototype.isLastSibling = function ()
{
	if (this.parent && this.parent.children[this.parent.children.length -1] == this )
		return true;

	return false;
}

/**
 * Removes the specified node from this node's children array.
 * @param node Node to remove.
 * @returns The removed node.
 */
TreeNode.prototype.removeChild = function (node)
{
	var new_array = new Array();

	for (var i in this.children)
	{
		if ( this.children[i] != node )
			new_array[new_array.length] = this.children[i];
	}

	this.children = new_array;

	return node;
}

/**
 * Returns true if this node has children.
 */
TreeNode.prototype.hasChildren = function ()
{
	return  (this.children.length > 0 );
}

/**
 * Returns an array containing all this node's children.
 */
TreeNode.prototype.getAllChildren = function ()
{
	var array = new Array();

	TreeNode.walk (this, array);

	return array;
}

/**
 * Fills an array that traverses the subtree rooted at the specified node in preorder.
 * @param node The subtree to traverse.
 * @param array The array that will be filled with the traversed subtree.
 */
TreeNode.walk = function(node, array)
{
	for (var i in node.children )
	{
		TreeNode.walk (node.children[i], array)

		array[array.length] = node.children[i];
	}
}

