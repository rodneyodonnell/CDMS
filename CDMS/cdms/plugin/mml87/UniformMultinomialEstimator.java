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

package cdms.plugin.mml87;

import cdms.core.*;
import cdms.plugin.model.*;

/** MML87 Multinomial estimator using a prior. */
public class UniformMultinomialEstimator extends Value.Function
{
  /** Serial ID required to evolve class while maintaining serialisation compatibility. */
	private static final long serialVersionUID = -5635618470333630887L;
public static Type.Discrete modelDataSpace = 
    new Type.Discrete(Integer.MIN_VALUE + 1,Integer.MAX_VALUE - 1,false,false,false,false);
  public static Value.Function uniformMultinomialEstimator = new UniformMultinomialEstimator();

  public UniformMultinomialEstimator() 
  {
    super(new Type.Function(new Type.Vector(new Type.Variable(), modelDataSpace, false, false, false, false), 
                            Type.STRUCTURED, 
                            false, 
                            false));
  }

  public String toString()
  {
    return "MML (uniform prior) multinomial estimator";
  }

  public Value apply(Value v)
  {
    Type.Discrete td = (Type.Discrete)((Type.Vector)v.t).elt;
    Value.Vector vv = (Value.Vector)v;
    int lwb = (int)td.LWB;
    int upb = (int)td.UPB;
    int count;
    double total = 0;
    double[] tallies = new double[upb-lwb+1];
    Value[] tallyVals = new Value[upb - lwb + 1];
    for(count = 0; count < tallies.length; count++) tallies[count] = 0.5;
    for(count = 0; count < vv.length(); count++)
    {
      total += vv.weight(count);
      tallies[vv.intAt(count) - lwb] += vv.weight(count);
    }
    for(count = 0; count < tallies.length; count++)
    {
      tallies[count] /= (total + (0.5 * tallies.length));
      tallyVals[count] = new Value.Continuous(tallies[count]);
    }
    return new Value.DefStructured(Multinomial.makeParamSpace(lwb, upb), tallyVals);
  }
}
