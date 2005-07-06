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

/**
 * This routine returns the HTML document element identified by the id parameter. Is is a wrapper routine that should work
 * independent of the browser used.
 *
 * @param id The id of the element to retrieve.
 * @param doc The HTML document to retrieve the element from. If omitted, the element is retrieved from the document
 * (html page) this javascript routine is called from.
 *
 * @returns The requested element if present, null otherwise.
 */
function get_element(id, doc)
{
	return (document.all ? ((doc ? doc.all[id] : document.all[id])) : ((doc ? doc.getElementById(id) : document.getElementById(id))));
}

if( !Array.prototype.push )
{
	/**
	 * Implements the array_push routine in case the javascript engine of the used browser does not support it.
	 */
	function array_push()
	{
		for(var i=0;i<arguments.length;i++)
			this[this.length]=arguments[i];

		return this.length;
	}

	Array.prototype.push = array_push;
}

if( !Array.prototype.pop )
{
	/**
	 * Implements the array_pop routine in case the javascript engine of the used browser does not support it.
	 */
	function array_pop()
	{
		lastElement = this[this.length-1];

		this.length = Math.max(this.length-1,0);

		return lastElement;
	}

	Array.prototype.pop = array_pop;
}
