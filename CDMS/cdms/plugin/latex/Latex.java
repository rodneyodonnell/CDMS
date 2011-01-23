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

// File: Latex.java
// Authors: {leighf,joshc}@csse.monash.edu.au

package cdms.plugin.latex;

import cdms.core.*;

/** Latex module. */ 
public class Latex extends Module.StaticFunctionModule
{
  public Latex()
  {
    super("Latex",Module.createStandardURL(Latex.class),Latex.class);
  }
 
  /** <code>[(...)] -> Str</code> Creates a Latex tabular from a vector of structured data. */
  public static Value.Function tabular = new Tabular();

  /** <code>[(...)] -> Str</code> 
      <p>
      Creates a Latex tabular from a vector of structured data.
  */
  public static class Tabular extends Value.Function
  {
    /** Serial ID required to evolve class while maintaining serialisation compatibility. */
	private static final long serialVersionUID = 3158243596054761557L;

	public static final String CR = System.getProperty("line.separator");

    public static final Type.Function TT = 
      new Type.Function(new Type.Vector(Type.STRUCTURED),Type.STRING);

    public Tabular()
    {
      super(TT);
    }

    public Value apply(Value param)
    {
      Value.Vector vv = (Value.Vector) param;
      Type.Structured eltt = (Type.Structured) ((Type.Vector) vv.t).elt;

      StringBuffer res = new StringBuffer();
      res.append("\\begin{table}[htp] " + CR + "\\center" + CR + "\\caption{}" + CR);
      res.append("\\begin{tabular}{|");
      // All columns centered for now.
      for (int j = 0; j < eltt.cmpnts.length; j++) res.append("c");
      res.append("|} \\hline" + CR);
      for (int i = 0; i < vv.length(); i++)
      {
        Value.Structured elti = (Value.Structured) vv.elt(i);
        for (int j = 0; j < elti.length(); j++)
        {
          Value cmpntj = elti.cmpnt(j);
          if (cmpntj instanceof Value.Str)
            res.append(((Value.Str) cmpntj).getString());
          else res.append(cmpntj.toString());
          if (j != elti.length() - 1)
          {
            res.append("&"); 
          }
          else 
          {
            if (i != vv.length() - 1) res.append("\\tabularnewline" + CR);
              else res.append("\\tabularnewline \\hline" + CR);
          }
        }      
      }
      res.append("\\end{tabular}" + CR + "\\end{table}");

      return new Value.Str(res.toString());
    }
  } 

} 
