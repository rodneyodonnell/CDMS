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
// Author: leighf@csse.monash.edu.au

package cdms.plugin.ml;

import cdms.core.*;

/**
  Maximum likelihood estimator the Normal distribution.
*/
public class Normal
{
  
  /** <code>(n,sum,sumsqr) -> (mu,sigma)</code> Maximum likelihood Normal estimator function. */
  public static final NormalEstimator normalEstimator = new NormalEstimator();

  /** <code>(n,sum,sumsqr) -> (mu,sigma)</code> 
      <p>
      Maximum likelihood Normal estimator function.
  */
  public static class NormalEstimator extends Value.Function
  {
    /** Serial ID required to evolve class while maintaining serialisation compatibility. */
	private static final long serialVersionUID = -4341049189463539775L;
	public static final Type.Function TT = 
      new Type.Function(cdms.plugin.model.Normal.SUFFICIENTSPACE,
                        cdms.plugin.model.Normal.PARAMSPACE);

    public NormalEstimator()
    {
      super(TT);
    }

    public Value apply(Value v)
    {
      Value.Structured sv = (Value.Structured) v;
      int n = sv.intCmpnt(0);
      double sum = sv.doubleCmpnt(1);
      double sumsqr = sv.doubleCmpnt(2);
      double sumsqrdiff = sumsqr - (sum * sum) / (double) n;
      double sd = sumsqrdiff > 0 ? Math.sqrt(sumsqrdiff / (double) n) : 0;
      return new NormalParams(sum / (double) n, sd);
    }

    public class NormalParams extends Value.Structured
    {
      /** Serial ID required to evolve class while maintaining serialisation compatibility. */
		private static final long serialVersionUID = 327164460918785954L;
	private double mean, sd;

      public NormalParams(double mean, double sd)
      {
        super(cdms.plugin.model.Normal.PARAMSPACE);
        this.mean = mean;
        this.sd = sd;
      }

      public int length() 
      {
        return 2;
      }

      public double doubleCmpnt(int idx)
      {
        if (idx == 0) return mean; else return sd;
      }

      public Value cmpnt(int idx)
      {
        return new Value.Continuous(doubleCmpnt(idx));
      }
    }
  }

}
