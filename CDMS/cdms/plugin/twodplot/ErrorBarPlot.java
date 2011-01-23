/*
 *  [The "BSD license"]
 *  Copyright (c) 1997-2011, Leigh Fitzgibbon, Josh Comley, Lloyd Allison and Rodney O'Donnell
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *    1. Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *    2. Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *    3. The name of the author may not be used to endorse or promote products
 *       derived from this software without specific prior written permission.*
 *
 *  THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 *  IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 *  IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 *  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 *  NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 *  DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 *  THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 *  THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package cdms.plugin.twodplot;

import cdms.core.*;
import cdms.plugin.desktop.*;

/** ("xlabel","ylabel",[[(val1, er1), ..., (val_index,er_index)]])*/
public class ErrorBarPlot extends Value.Function
{
  /** Serial ID required to evolve class while maintaining serialisation compatibility. */
	private static final long serialVersionUID = -5586077974283604128L;

public ErrorBarPlot()
  {
    super(new Type.Function(new Type.Vector(new Type.Vector(new Type.Structured(new Type[]{Type.SCALAR, Type.SCALAR}))), Type.TRIV));
  }

  public Value apply(Value v)
  {
    Value.Structured vs = (Value.Structured)v;
    String xLabel = ((Value.Str)vs.cmpnt(0)).getString();
    String yLabel = ((Value.Str)vs.cmpnt(1)).getString();
    Value.Vector bigVector = (Value.Vector)vs.cmpnt(2);
    Value.Vector eltVector;
    Value.Structured s;
    Value.Structured xs;
    Plot.NativeLinePlot nlp,dot;
    double[] xv = new double[bigVector.length()];
    double[] xe = new double[bigVector.length()];
    double[][] yv = new double[bigVector.length()][];
    double[][] ye = new double[bigVector.length()][];
    float xmin = Float.MAX_VALUE, xmax = -Float.MAX_VALUE, ymin= Float.MAX_VALUE, ymax = -Float.MAX_VALUE;
    int eltCount, eltCount2;
    for(eltCount = 0; eltCount < bigVector.length(); eltCount++)
    {
      eltVector = (Value.Vector)bigVector.elt(eltCount);
      xs = (Value.Structured)eltVector.elt(eltVector.length() - 1);
      xv[eltCount] = ((Value.Scalar)xs.cmpnt(0)).getContinuous();
      xe[eltCount] = ((Value.Scalar)xs.cmpnt(1)).getContinuous();

      if(xv[eltCount] - xe[eltCount] < xmin) xmin = (float)(xv[eltCount] - xe[eltCount]);
      if(xv[eltCount] + xe[eltCount] > xmax) xmax = (float)(xv[eltCount] + xe[eltCount]);

      yv[eltCount] = new double[eltVector.length()];
      ye[eltCount] = new double[eltVector.length()];
      for(eltCount2 = 0; eltCount2 < eltVector.length() - 1; eltCount2++)
      {
        s = (Value.Structured)eltVector.elt(eltCount2);
        yv[eltCount][eltCount2] = ((Value.Scalar)s.cmpnt(0)).getContinuous();
        ye[eltCount][eltCount2] = ((Value.Scalar)s.cmpnt(1)).getContinuous();

        if(yv[eltCount][eltCount2] - ye[eltCount][eltCount2] < ymin) ymin = (float)(yv[eltCount][eltCount2] - ye[eltCount][eltCount2]);
        if(yv[eltCount][eltCount2] + ye[eltCount][eltCount2] > ymax) ymax = (float)(yv[eltCount][eltCount2] + ye[eltCount][eltCount2]);
      }
    }
    
    // give a bit of space between the most extreme data points and the axes.
    float x_border = (float)(0.05 * (xmax - xmin));
    float y_border = (float)(0.05 * (ymax - ymin));

    Plot.PlotSpace plotSpace = new Plot.PlotSpace(xmin - x_border, xmax + x_border, ymin - y_border, ymax + y_border);

    for(eltCount = 0; eltCount < xv.length; eltCount++)
    {
      for(eltCount2 = 0; eltCount2 < yv[eltCount].length - 1; eltCount2++)
      {
        nlp = new Plot.NativeLinePlot();
        dot = new Plot.NativeLinePlot();
        nlp.addData(null,(float)(xv[eltCount] - xe[eltCount]), (float)(yv[eltCount][eltCount2] - ye[eltCount][eltCount2]));
        nlp.addData(null,(float)(xv[eltCount] - xe[eltCount]), (float)(yv[eltCount][eltCount2] + ye[eltCount][eltCount2]));
        nlp.addData(null,(float)(xv[eltCount] + xe[eltCount]), (float)(yv[eltCount][eltCount2] + ye[eltCount][eltCount2]));
        nlp.addData(null,(float)(xv[eltCount] + xe[eltCount]), (float)(yv[eltCount][eltCount2] - ye[eltCount][eltCount2]));
        nlp.addData(null,(float)(xv[eltCount] - xe[eltCount]), (float)(yv[eltCount][eltCount2] - ye[eltCount][eltCount2]));
        plotSpace.addDrawShape(nlp);
        dot.addData(null,(float)xv[eltCount],(float)yv[eltCount][eltCount2]);
        dot.setMarker(new Plot.CrossMarker(5));
        plotSpace.addDrawShape(dot);
      }
    }

    plotSpace.addFillShape(new Plot.YAxis(yLabel,2));
    plotSpace.addFillShape(new Plot.XAxis(xLabel,2));

    DesktopFrame.makeWindow("error bar plot",plotSpace);
    return Value.TRIV;
  }
}
