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
 * This routine stores a cookie associated with the current document
 *
 * @param name The name of the name=value pair stored.
 * @param value The value of the name=value pair stored.
 * @param expires The expires parameter lets you determine the lifetime of the cookie.
 * @param path Sets the URL path the cookie is valid within. Pages outside of that path cannot read or use the cookie.
 * @param domain Cookies can be assigned to individual machines, or to an entire Internet domain. The only restrictions on this value is that it must contain at least two dots (.myserver.com, not myserver.com)
 * @param secure The secure parameter is a flag indicating that a cookie should only be used under a secure server condition, such as SSL.
 */
function set_cookie(name, value, expires, path, domain, secure)
{
	document.cookie = escape(name) 	+ '=' + escape(value)
									+ (expires ? '; expires=' + expires.toGMTString() : '')
									+ (path ? '; path=' + path : '')
									+ (domain ? '; domain=' + domain : '')
									+ (secure ? '; secure=' : '');
}

/**
 * This routine retrieves the value of the name=value pair from the cookies that apply to the current document.
 *
 * @param name The name of the name=value pair.
 * @returns The string containing the value of the name=value pair.
 */
function get_cookie(name)
{
	var value    = '';
	var posName  = document.cookie.indexOf(escape(name) + '=');

	if (posName != -1)
	{
		var posValue = posName + (escape(name) + '=').length;
		var endPos   = document.cookie.indexOf(';', posValue);

		if (endPos != -1)
			value = unescape(document.cookie.substring(posValue, endPos));
		else
			value = unescape(document.cookie.substring(posValue));
	}
	return (value);
}		