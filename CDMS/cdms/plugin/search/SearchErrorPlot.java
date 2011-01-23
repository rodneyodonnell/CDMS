
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

// File: SearchErrorPlot.java
// Author: {leighf,joshc}@csse.monash.edu.au

package cdms.plugin.search;

import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;
import cdms.plugin.twodplot.*;

/** A real-time plotter for search error. */
public class SearchErrorPlot extends JPanel implements Search.SearchListener
{
  /** Serial ID required to evolve class while maintaining serialisation compatibility. */
	private static final long serialVersionUID = 1034684124522400985L;
Plot.PlotSpace plotSpace = new Plot.PlotSpace(0,10,0,1);
  Plot.NativeLinePlot lp = new Plot.NativeLinePlot();
  GeneralPath gp = new GeneralPath();
  boolean seenFirstPoint, adjusted;
  double firstPoint;

  public SearchErrorPlot(Search search)
  {
    super(new BorderLayout());

    search.addSearchListener(this);

    setBackground(Color.white);

    plotSpace.addDrawShape(lp);
    plotSpace.addFillShape(new Plot.XAxis("Epoch",2));
    plotSpace.addFillShape(new Plot.YAxis("Error",2));
    add(plotSpace,BorderLayout.CENTER);
  }

  public void reset(Search sender)
  {
    Runnable doGraph = new Runnable()  
    { 
      public void run()
      {
        adjusted = false;
        seenFirstPoint = false;
        plotSpace.removeShape(lp);
        lp = new Plot.NativeLinePlot();
        plotSpace.addDrawShape(lp);
        plotSpace.setXLower(0);
        plotSpace.setXUpper(10);
        plotSpace.setYLower(0);
        plotSpace.setYUpper(1);
        plotSpace.doLayout();
        repaint(plotSpace.getX(),plotSpace.getY(),
                plotSpace.getWidth(),plotSpace.getHeight());
      }
    };
    SwingUtilities.invokeLater(doGraph);
  }

  public void beforeEpoch(Search sender)
  {
    ;
  }


    float bestError = Float.MAX_VALUE;
  public void updateGraph(final float err, final int x)
  {
      if ( bestError <= err ) {
	  return;
      }
      bestError = err;
      //      System.out.println("Epoch = " + x + "\terr = " + err  );

    Runnable doGraph = new Runnable()  
    { 
      public void run()
      {
        boolean inv = false;
        if (x > plotSpace.getXUpper())
        {
          inv = true;
          plotSpace.setXUpper(x+9);
        }
 
        if (err > plotSpace.getYUpper())
        {
          inv = true;
          plotSpace.setYUpper(err);
        }

        if (err < plotSpace.getYLower())
        {
          inv = true;
          plotSpace.setYLower(err);
        }

        if (seenFirstPoint && !adjusted)
        {
          // Once we have seen two points that are not equal we adjust the bounds.
          if (err != firstPoint) 
          {
            inv = true;
            plotSpace.setYLower((float)Math.min(err,firstPoint));
            plotSpace.setYUpper((float)Math.max(err,firstPoint));
            adjusted = true;
          }
        }
        else
        {
          firstPoint = err;
          seenFirstPoint = true;
        }

        if (inv) 
        {
          lp.addData(null,x,err);
          plotSpace.doLayout();
        }
        else
        {
          gp.reset();
          lp.addData(gp,x,err);
          plotSpace.drawSubShape(lp,gp); 
        }

        repaint(plotSpace.getX(),plotSpace.getY(),
                plotSpace.getWidth(),plotSpace.getHeight());
      }
    };
    SwingUtilities.invokeLater(doGraph);
  }

  public void afterEpoch(Search sender)
  {
    float err = (float) sender.getLastMetric();
    int x = sender.epoch();
    updateGraph(err,x);
  }

  public void onCompletion(Search sender)
  {

  }
}

