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



