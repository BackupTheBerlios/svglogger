/*
 *   ViewBox.js
 *   http://www.kevlindev.com/gui/utilities/viewbox/index.htm
 *   copyright 2002, Kevin Lindsey
 */

ViewBox.VERSION = "1.0";

function ViewBox(svgNode)
{
    if ( arguments.length > 0 )
        this.init(svgNode);
}

ViewBox.prototype.init = function(svgNode)
{
    var viewBox 			= svgNode.getAttributeNS(null, "viewBox");
    var preserveAspectRatio = svgNode.getAttributeNS(null, "preserveAspectRatio");

    if ( viewBox != "" )
    {
        var params = viewBox.split(/\s*,\s*|\s+/);

        this.x      = parseFloat( params[0] );
        this.y      = parseFloat( params[1] );
        this.width  = parseFloat( params[2] );
        this.height = parseFloat( params[3] );
    }
    else
    {
        this.x      = 0;
        this.y      = 0;
        this.width  = innerWidth;
        this.height = innerHeight;
    }

    this.setPAR(preserveAspectRatio);
};

ViewBox.prototype.getTM = function()
{
    var svgRoot      = svgDocument.documentElement;
    var matrix       = svgDocument.documentElement.createSVGMatrix();
    var windowWidth  = svgRoot.getAttributeNS(null, "width");
    var windowHeight = svgRoot.getAttributeNS(null, "height");

    windowWidth  = ( windowWidth  != "" ) ? parseFloat(windowWidth)  : innerWidth;
    windowHeight = ( windowHeight != "" ) ? parseFloat(windowHeight) : innerHeight;

    var x_ratio = this.width  / windowWidth;
    var y_ratio = this.height / windowHeight;

    matrix = matrix.translate(this.x, this.y);

    if ( this.alignX == "none" )
    {
        matrix = matrix.scaleNonUniform( x_ratio, y_ratio );
    }
    else
    {
        if ( x_ratio < y_ratio && this.meetOrSlice == "meet" || x_ratio > y_ratio && this.meetOrSlice == "slice" )
        {
            var x_trans = 0;
            var x_diff  = windowWidth*y_ratio - this.width;

            if ( this.alignX == "Mid" )
                x_trans = -x_diff/2;
            else if ( this.alignX == "Max" )
                x_trans = -x_diff;

            matrix = matrix.translate(x_trans, 0);
            matrix = matrix.scale( y_ratio );
        }
        else if ( x_ratio > y_ratio && this.meetOrSlice == "meet" || x_ratio < y_ratio && this.meetOrSlice == "slice"   )
        {
            var y_trans = 0;
            var y_diff  = windowHeight*x_ratio - this.height;

            if ( this.alignY == "Mid" )
                y_trans = -y_diff/2;
            else if ( this.alignY == "Max" )
                y_trans = -y_diff;

            matrix = matrix.translate(0, y_trans);
            matrix = matrix.scale( x_ratio );
        }
        else
            matrix = matrix.scale( x_ratio );
    }

    return matrix;
}


ViewBox.prototype.setPAR = function(par)
{
	if ( par )
    {
        var params = par.split(/\s+/);
        var align  = params[0];

        if ( align == "none" )
        {
            this.alignX = "none";
            this.alignY = "none";
        }
        else
        {
            this.alignX = align.substring(1,4);
            this.alignY = align.substring(5,9);
        }

        if ( params.length == 2 )
            this.meetOrSlice = params[1];
        else
            this.meetOrSlice = "meet";
    }
    else
    {
        this.align  		= "xMidYMid";
        this.alignX 		= "Mid";
        this.alignY 		= "Mid";
        this.meetOrSlice 	= "meet";
    }
};

