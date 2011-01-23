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

// File: Dialog.java
// Authors: {leighf,joshc}@csse.monash.edu.au

package cdms.plugin.dialog;

import cdms.core.*;
import cdms.plugin.desktop.*;

public class Dialog extends Module.StaticFunctionModule
{
  public Dialog() 
  {
    super("Dialog",Module.createStandardURL(Dialog.class),
          new Class[] { Dialog.class, FileLoader.class, DelimitedFile.class });
  }

  public static final ApplyFunction apply = new ApplyFunction();
  public static class ApplyFunction extends Value.Function
  {
    /** Serial ID required to evolve class while maintaining serialisation compatibility. */
	private static final long serialVersionUID = -3200628117623694511L;

	public ApplyFunction()
    {
      super(Type.FUNCTION);
    }

    public Value apply(Value v)
    {
      // Only allow functions with the correct parameter type.
      Type.Function withMember = new Type.Function(v.t, new Type.Variable(), false, false); 

      String msg = "Select function to apply...";
      Value.Function f = 
        (Function) new SelectDialog(msg, Type.FUNCTION, withMember, true).getResult();

      if (f != null)
      {
        Value result = f.apply(v);
        if(result != Value.TRIV) DesktopFrame.makeWindow("Result",result);
        return result;
      }
      else return Value.TRIV;
    }
  }

  public static final AddToEnvironmentDialog addToEnvironmentDialog = new AddToEnvironmentDialog();
  public static class AddToEnvironmentDialog extends Value.Function
  {
    /** Serial ID required to evolve class while maintaining serialisation compatibility. */
	private static final long serialVersionUID = 9183808734140868004L;

	public AddToEnvironmentDialog()
    {
      super(new Type.Function(Type.TYPE, Type.TRIV));
    }

    public Value apply(Value v)
    {
      new cdms.plugin.dialog.AddToEnvironmentDialog(v);
      return Value.TRIV;
    }
  }

  public static final ViewTypeFN viewTypeFN = new ViewTypeFN();
  public static class ViewTypeFN extends Value.Function
  {
    /** Serial ID required to evolve class while maintaining serialisation compatibility. */
	private static final long serialVersionUID = 4566053874208176795L;

	public ViewTypeFN()
    {
      super(new Type.Function(Type.TYPE, Type.TRIV));
    }

    public Value apply(Value v)
    {
      Type t = v.t;
      try
      {
        NewTypeDialog.class.getConstructor(new Class[] {t.getClass(), String.class, Boolean.class}).newInstance(new Object[] {t, "Type information", new Boolean(false)});
      }
      catch(Exception ex)
      {
        ex.printStackTrace();
      }
      return Value.TRIV;
    }
  }
}
