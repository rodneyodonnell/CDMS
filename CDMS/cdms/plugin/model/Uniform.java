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

// File: Uniform.java
// Author: {leighf,joshc}@csse.monash.edu.au

package cdms.plugin.model;

import java.io.*;
import java.util.Random;

import cdms.core.*;

/** Continuous Uniform model. */
public class Uniform extends Value.Model
{
  /** Serial ID required to evolve class while maintaining serialisation compatibility. */
	private static final long serialVersionUID = 1138119179759475922L;

public static Type dataSpace = Type.CONTINUOUS;

  public static Type.Structured paramSpace = 
    new Type.Structured(new Type[] { Type.CONTINUOUS, Type.CONTINUOUS },
                                     new String[] { "LWB", "UPB" });

  public static Type.Structured sufficientSpace = 
    new Type.Structured(new Type[] { Type.CONTINUOUS, Type.CONTINUOUS, Type.DISCRETE },
                                     new String[] { "LWB", "UPB", "N" });

  public static Type.Model thisType = new Type.Model(dataSpace,paramSpace,Type.TRIV,sufficientSpace); 

  public static final Uniform uniform = new Uniform();

  public static class Sufficient extends Value.Structured implements Serializable
  {
    /** Serial ID required to evolve class while maintaining serialisation compatibility. */
	private static final long serialVersionUID = 345248523535560266L;
	public int n;
    public double lwb;
    public double upb;

    public Sufficient(double lwb, double upb, int n)
    {
      super(sufficientSpace);
      this.n = n;
      this.lwb = lwb;
      this.upb = upb;
    }

    public Sufficient(Value.Vector v)
    {
      super(sufficientSpace);
      n = v.length();
      lwb = Double.MAX_VALUE;
      upb = - Double.MAX_VALUE;
      for (int i = 0; i < n; i++)
      {
        double vi = v.doubleAt(i);
        if (vi < lwb) lwb = vi;
        if (vi > upb) upb = vi;
      }
    }

    public Value cmpnt(int i)
    {
      if (i == 0) return new Value.Continuous(lwb);
      if (i == 1) return new Value.Continuous(upb);
      if (i == 2) return new Value.Discrete(n);
      throw new RuntimeException("Structured index out of range.");
    }

    public int length()
    {
      return 3;
    }

    public double doubleCmpnt(int i)
    {
      if (i == 0) return lwb;
      if (i == 1) return upb;
      if (i == 2) return n;
      throw new RuntimeException("Structured index out of range.");
    }

    public int intCmpnt(int i)
    {
      if (i == 2) return n;
      throw new RuntimeException("Structured index out of range.");
    }
  }

  public Uniform()
  {
    super(thisType);
  }

  public double logP(Value x, Value y, Value z)
  {
    double xval = ((Value.Scalar) x).getContinuous();
    double lwb = ((Value.Structured) y).doubleCmpnt(0);
    double upb = ((Value.Structured) y).doubleCmpnt(1);
    if (xval < lwb || xval > upb) return Math.log(0);
    return - Math.log(upb - lwb);
  }

  public Value.Vector generate(Random rand, int n, Value y, Value z)
  {
    double lwb = ((Value.Structured) y).doubleCmpnt(0);
    double upb = ((Value.Structured) y).doubleCmpnt(1);
    return new UniformDataVector(lwb,upb,rand,n);
  }

  public Value predict(Value y, Value z)
  {
    double lwb = ((Value.Structured) y).doubleCmpnt(0);
    double upb = ((Value.Structured) y).doubleCmpnt(1);
    return new Value.Continuous(upb-lwb);
  }

  public Value.Vector predict(Value y, Value.Vector z)
  {
    double lwb = ((Value.Structured) y).doubleCmpnt(0);
    double upb = ((Value.Structured) y).doubleCmpnt(1);
    Value mean = new Value.Continuous(upb-lwb);
    if (z == null) return VectorFN.emptyVector;
      else return new VectorFN.ConstantVector(z.length(),mean);
  }

  public double logP(Value.Vector x, Value y, Value.Vector z)
  {
    return logPSufficient(new Sufficient(x),y);
  }

  public Value getSufficient(Value.Vector x, Value.Vector z)
  {
    return new Sufficient(x);
  }

  public double logPSufficient(Value s, Value y)
  {
    double slwb = ((Value.Structured) s).doubleCmpnt(0);
    double supb = ((Value.Structured) s).doubleCmpnt(1);
    double sn = ((Value.Structured) s).intCmpnt(2);
    double lwb = ((Value.Structured) y).doubleCmpnt(0);
    double upb = ((Value.Structured) y).doubleCmpnt(1);
    if (slwb < lwb || supb > upb) return Math.log(0);
    return - sn * Math.log(upb - lwb);
  }

  public String toString()
  {
    return "Uniform(x|lwb,upb)";
  }

  public static Type.Vector uniformDataVectorType = 
    new Type.Vector(Type.DISCRETE,dataSpace,true,true,false,false);

  public static class UniformDataVector extends Value.Vector
  {
    /** Serial ID required to evolve class while maintaining serialisation compatibility. */
	private static final long serialVersionUID = -3975491980699485797L;
	protected double data[];
    protected Value v[];

    public UniformDataVector(double lwb, double upb, Random rand, int length)
    {
      super(uniformDataVectorType);
      data = new double[length];
      v = new Value[length];
      double range = upb - lwb;
      for (int i = 0; i < length; i++)
        data[i] = lwb + rand.nextDouble() * range;
    }

    public double doubleAt(int i)
    {
      return data[i];
    }

    public int length() 
    { 
      return data.length; 
    }

    public Value elt(int i) 
    {
      if (v[i] == null)
        v[i] = new Value.Continuous(data[i]);
      return v[i];
    }
  }

}
