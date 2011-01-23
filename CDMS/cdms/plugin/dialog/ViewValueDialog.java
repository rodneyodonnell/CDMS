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

// File: ViewValueDialog.java
// Authors: {leighf,joshc}@csse.monash.edu.au

package cdms.plugin.dialog;

import cdms.core.*;

public class ViewValueDialog extends ViewDialog
{
  /** Serial ID required to evolve class while maintaining serialisation compatibility. */
	private static final long serialVersionUID = -1251926838837235569L;

public ViewValueDialog(Type t)
  {
    super("View Value",new NewValuePanelType(t));
  }

  public ViewValueDialog(Type.Triv t)
  {
    super("View Value",new NewValuePanelTriv(t));
  }

  public ViewValueDialog(Type.Str t)
  {
    super("View Value",new NewValuePanelString(t));
  }

  public ViewValueDialog(Type.Scalar t)
  {
    super("View Value",new NewValuePanelScalar(t));
  }

  public ViewValueDialog(Type.Discrete t)
  {
    super("View Value",new NewValuePanelDiscrete(t));
  }

  public ViewValueDialog(Type.Continuous t)
  {
    super("View Value",new NewValuePanelContinuous(t));
  }

  public ViewValueDialog(Type.Vector t)
  {
    super("View Value",new NewValuePanelVector(t));
  }

  public ViewValueDialog(Type.Function t)
  {
    super("View Value",new NewValuePanelFunction(t));
  }

  public ViewValueDialog(Type.Symbolic t)
  {
    super("View Value",new NewValuePanelSymbolic(t));
  }

  public ViewValueDialog(Type.Structured t)
  {
    super("View Value",new NewValuePanelStructured(t));
  }

}
