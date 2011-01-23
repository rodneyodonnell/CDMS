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

/** ("xlabel","ylabel",[(y_val, y_error, string_x)])*/
public class NamedErrorBarPlot extends Value.Function
{
  /** Serial ID required to evolve class while maintaining serialisation compatibility. */
	private static final long serialVersionUID = 4392210102924528524L;

public NamedErrorBarPlot()
  {
    super(new Type.Function(new Type.Vector(new Type.Structured(new Type[]{Type.SCALAR, Type.SCALAR, Type.DISCRETE})), Type.TRIV));
  }

  public Value apply(Value v)
  {
    Value.Structured vs = (Value.Structured)v;
    String xLabel = ((Value.Str)vs.cmpnt(0)).getString();
    String yLabel = ((Value.Str)vs.cmpnt(1)).getString();
    Value.Vector bigVector = (Value.Vector)vs.cmpnt(2);
    Value.Structured s;
    Plot.NativeLinePlot nlp;
    double[] yv = new double[bigVector.length()];
    double[] ye = new double[bigVector.length()];
    String[] xs = new String[bigVector.length()];
    float xmin = 0, xmax = bigVector.length() - 1, ymin= Float.MAX_VALUE, ymax = -Float.MAX_VALUE;
    int eltCount;
    for(eltCount = 0; eltCount < bigVector.length(); eltCount++)
    {
      s = (Value.Structured)bigVector.elt(eltCount);
      yv[eltCount] = ((Value.Scalar)s.cmpnt(0)).getContinuous();
      ye[eltCount] = ((Value.Scalar)s.cmpnt(1)).getContinuous();
      xs[eltCount] = ((Value.Str)s.cmpnt(2)).getString();
      if(yv[eltCount] - ye[eltCount] < ymin) ymin = (float)(yv[eltCount] - ye[eltCount]);
      if(yv[eltCount] + ye[eltCount] > ymax) ymax = (float)(yv[eltCount] + ye[eltCount]);
    }

    float y_border = (float)(0.05 * (ymax - ymin));

    Plot.PlotSpace plotSpace = new Plot.PlotSpace((float)(xmin - 0.5), (float)(xmax + 0.5), ymin - y_border, ymax + y_border);

    for(eltCount = 0; eltCount < xs.length; eltCount++)
    {
      nlp = new Plot.NativeLinePlot();
      nlp.addData(null,(float)(eltCount - 0.5), (float)(yv[eltCount] - ye[eltCount]));
      nlp.addData(null,(float)(eltCount - 0.5), (float)(yv[eltCount] + ye[eltCount]));
      nlp.addData(null,(float)(eltCount + 0.5), (float)(yv[eltCount] + ye[eltCount]));
      nlp.addData(null,(float)(eltCount + 0.5), (float)(yv[eltCount] - ye[eltCount]));
      nlp.addData(null,(float)(eltCount - 0.5), (float)(yv[eltCount] - ye[eltCount]));
      plotSpace.addDrawShape(nlp);
      nlp = new Plot.NativeLinePlot();
      nlp.addData(null,(float)(eltCount - 0.5), (float)yv[eltCount]);
      nlp.addData(null,(float)(eltCount + 0.5), (float)yv[eltCount]);
      plotSpace.addDrawShape(nlp);
    }

    plotSpace.addFillShape(new Plot.YAxis(yLabel,2));
    plotSpace.addFillShape(new CustomAxis.DiscreteXAxis(xLabel, xs));

    DesktopFrame.makeWindow("discrete error bar plot",plotSpace);
    return Value.TRIV;
  }
}
