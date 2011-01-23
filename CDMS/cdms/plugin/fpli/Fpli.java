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

package cdms.plugin.fpli;

import cdms.core.*;
import java.io.*;

/** Lambda Script module. */
public class Fpli extends Module.StaticFunctionModule
{
  public Fpli()
  {
    super("Fpli",Module.createStandardURL(Fpli.class),Fpli.class);
  }

  public static Type.Function interpreterType = new Type.Function(Type.STRING,Type.TYPE,false,false);

  /** <code>String -> t</code> Interpreters the parameters using the Lambda Script interpreter. */
  public static Value.Function interpreter = new Interpreter();

  /** <code>String -> t</code> 
      <p>
      Interpreters the parameters using the Lambda Script interpreter. 
  */
  public static class Interpreter extends Value.Function
  {
    /** Serial ID required to evolve class while maintaining serialisation compatibility. */
	private static final long serialVersionUID = -6021816807864449597L;

	public Interpreter()
    {
      super(interpreterType);
    }

    public Value apply(Value v)
    {
      try
      {
        Syntax syn = new Syntax( new Lexical( 
                         new ByteArrayInputStream(((Value.Str) v).getString().getBytes()) ) 
                     );
        Expression e = syn.exp();    // parse the Expression
        return e.eval(new Environment(new Environment(null)));  // Don't use boot environment.
      }
      catch (Exception e)
      {
        e.printStackTrace();
        return new Value.Str(e.toString());
      }     
    }
  }

  /** <code>String -> t</code> Performs syntax and type checking on the parameter. */
  public static Value.Function checker = new Checker();

  /** <code>String -> t</code> 
      <p>
      Performs syntax and type checking on the parameter.
  */
  public static class Checker extends Value.Function
  {
    /** Serial ID required to evolve class while maintaining serialisation compatibility. */
	private static final long serialVersionUID = -8080828818346060618L;

	public Checker()
    {
      super(interpreterType);
    }

    public Value apply(Value v)
    {
      return new Value.Str("The syntax and type checker does not exist.");
    }
  }


}
