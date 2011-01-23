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

/** Minimum Message Length using the Fisher information (MML87) module. */
public class Mml87 extends Module
{
  public static java.net.URL helpURL = Module.createStandardURL(Mml87.class);

  public String getModuleName()
  {
    return "MML87";
  }

  public java.net.URL getHelp()
  {
    return helpURL;
  }

  public void install(Value params) throws Exception
  {
    add("multinomialEstimator", UniformMultinomialEstimator.uniformMultinomialEstimator, 
        "Function to estimate parameters for a multinomial distribution (MML87 uniform prior)");
    add("multinomialCoster", UniformMultinomialCoster.uniformMultinomialCoster,
        "Function to give message length of (Multinomial Model, Params, Data)");
    add("multinomialCoster_1", UniformMultinomialCoster_1.uniformMultinomialCoster_1,
        "Function to give 1st part of the message length of (Multinomial Model, Params, Data)");
    add("normalEstimator", UniformNormal.normalEstimator, 
        "Function to estimate the parameters for the normal distribution with uniform prior on mu and log(sigma).");
    add("normalCoster", UniformNormal.normalCoster, 
        "Function to compute the message length for the normal distribution with uniform prior on mu and log(sigma).");
    add("jcNormalCoster", UniformNormal.arbitraryNormalCoster, 
        "Function to compute the message length for the normal distribution with uniform prior on mu and log(sigma), with mu and sigma as parameters.");
    add("jcNormalCoster_1", UniformNormal.arbitraryNormalCosterHypothesisOnly, 
        "Function to compute the 1st part of the message length (hypothesis only) for the normal distribution with uniform prior on mu and log(sigma), with mu and sigma as parameters.");
    add("weightedNormalEstimator", UniformNormal.weightedNormalEstimator,
        "Function to estimate the parameters for the weighted normal distribution with uniform prior on mu and log(sigma).");

  }
}
