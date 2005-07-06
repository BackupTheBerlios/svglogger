/**
 * This class is used to interpret the received XML parameter information. The information is translated
 * into a HTML text structure that gives the user a graphical representation of the invocation parameters.
 *
 * @param html_txt HTML text object where the parameter information is placed into.
 * @param xml_doc Contains the parser XML parameter information.
 * @constructor
 */
function XMLParameterText (html_txt, xml_doc)
{
	this.reply = false;
	this.xml   = xml_doc;
	this.valid = true;
	this.text  = html_txt;

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

SVGController.prototype.addTooltipText = function (v1, v2)
{
	if ( v1 == 'parameter' )
	{

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

	}
	this.setTooltipSize();
}


