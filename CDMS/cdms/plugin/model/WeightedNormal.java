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

// File: Normal.java
// Author: {leighf,joshc}@csse.monash.edu.au

package cdms.plugin.model;

import java.io.*;
import java.awt.*;
import javax.swing.*;
import cdms.core.*;

/** Gaussian model parameterised by the mean and standard deviation. */
public class WeightedNormal extends Normal
{

  /** Serial ID required to evolve class while maintaining serialisation compatibility. */
	private static final long serialVersionUID = 2797595601113621104L;

public static final Type.Structured SUFFICIENTSPACE = 
    new Type.Structured(new Type[] { Type.CONTINUOUS, Type.CONTINUOUS, Type.CONTINUOUS },
                        new String[] { "N", "SUM", "SUMSQR" });
                        
  public static final Type.Model TT = new Type.Model(Normal.DATASPACE,Normal.PARAMSPACE,Type.TRIV,SUFFICIENTSPACE);
  
  public static final WeightedNormal weightedNormal = new WeightedNormal();

  /** The sufficient statistics for a sample from the Normal distribution. */
  public static class Sufficient extends Value.Structured implements Serializable
  {
    /** Serial ID required to evolve class while maintaining serialisation compatibility. */
	private static final long serialVersionUID = -4386252245738696067L;
	public double n;
    public double sum;
    public double sumsqr;

    public Sufficient(double n, double sum, double sumsqr)
    {
      super(SUFFICIENTSPACE);
      this.n = n;
      this.sum = sum;
      this.sumsqr = sumsqr;
    }

    public Sufficient(Value.Vector v)
    {
      super(SUFFICIENTSPACE);
      sum = 0;
      sumsqr = 0;
      n = 0;
      for (int i = 0; i < v.length(); i++)
      {
        double vi = v.doubleAt(i);
        double wi = v.weight(i);
        sum += vi * wi;
        sumsqr += vi * vi * wi;
        n += wi; 
      }
    }

    public Value cmpnt(int i)
    {
      switch(i)
      {
        case 0 : return new Value.Continuous(n);
        case 1 : return new Value.Continuous(sum);
        case 2 : return new Value.Continuous(sumsqr);
        default : throw new RuntimeException("Structured index out of range.");
      }
    }

    public int length()
    {
      return 3;
    }

    public double doubleCmpnt(int i)
    {
      switch(i)
      {
        case 0 : return n;
        case 1 : return sum;
        case 2 : return sumsqr;
        default : throw new RuntimeException("Structured index out of range.");
      }
    }

    public int intCmpnt(int i)
    {
      throw new RuntimeException("Structured index out of range.");
    }
  }

  public WeightedNormal()
  {
    super(TT);
  }

  /** This method takes the weight of each element into account. */
  public double logP(Value.Vector x, Value y, Value.Vector z)
  {
    return logPSufficient(new Sufficient(x),y);
  }

  public Value getSufficient(Value.Vector x, Value.Vector z)
  {
    return new Sufficient(x);
  }

  /** This method takes the weight of each element into account. */
  public double logPSufficient(Value s, Value y)
  {
    double n = ((Value.Structured) s).doubleCmpnt(0);
    double sum = ((Value.Structured) s).doubleCmpnt(1);
    double sumsqr = ((Value.Structured) s).doubleCmpnt(2);
    double mean = ((Value.Structured) y).doubleCmpnt(0);
    double sd = ((Value.Structured) y).doubleCmpnt(1);
    double sumsqrdiff = sumsqr - (2 * mean * sum) + n * mean * mean;
    return - n * (logr2PI + Math.log(sd)) - sumsqrdiff / (2.0 * sd * sd);
  }

  public Component displayParams(Value y)
  {
    java.text.DecimalFormat df = new java.text.DecimalFormat();
    df.setMaximumFractionDigits(3);
    double m = ((Value.Continuous)((Value.Structured)y).cmpnt(0)).getContinuous();
    double s = ((Value.Continuous)((Value.Structured)y).cmpnt(1)).getContinuous();
    double x;
    Value[] array = new Value[100];
    int count;
    for(count = 0; count < 100; count++)
    {
      x = (m - 2 * s) + (count * 0.04 * s);
      array[count] = new Value.DefStructured(new Value[]{new Value.Continuous(x), new Value.Continuous(java.lang.Math.exp(logP(new Value.Continuous(x),y,null)))});
    }
    JLabel l = new JLabel("m: " + df.format(m) + ", sd: " + df.format(s));
    JPanel p = new JPanel();
    Component comp = (Component)((Value.Obj)cdms.plugin.twodplot.TwoDPlot.ploto.apply(new VectorFN.FatVector(array))).getObj();
    p.setLayout(new BorderLayout());
    p.add(l, BorderLayout.SOUTH);
    p.add(comp, BorderLayout.CENTER);
    return p;
  }

  public String toString()
  {
    return "Weighted Normal(x|mu,sigma)";
  }
}
