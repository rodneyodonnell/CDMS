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

// File: Model.java
// Authors: {leighf,joshc}@csse.monash.edu.au

package cdms.plugin.model;

import cdms.core.*;

/** Model module. */
public class Model extends Module
{
  public static java.net.URL helpURL = Module.createStandardURL(Model.class);

  public String getModuleName() { return "Model"; }
  public java.net.URL getHelp() { return helpURL; }

  public void install(Value params) throws Exception
  {
    Environment.env.add("uniform","Model",Uniform.uniform,
                        "Uniform(y|lwb,upb)");

    Environment.env.add("normal","Model",Normal.normal,
                        "Normal(y|mu,sigma)");

    Environment.env.add("weighted_normal","Model",WeightedNormal.weightedNormal,
                        "Weighted Normal (y|mu,sigma)");

    Environment.env.add("multinomialCreator", "Model", new Multinomial.MultinomialCreator(),
                        "Multinomial Creator  (lwb,upb) -> multinomial");
  }

}
