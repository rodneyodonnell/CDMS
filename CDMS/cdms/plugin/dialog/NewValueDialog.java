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

// File: NewValueDialog.java
// Authors: {leighf,joshc}@csse.monash.edu.au

package cdms.plugin.dialog;

import cdms.core.*;

public class NewValueDialog extends NewDialog
{

  /** Serial ID required to evolve class while maintaining serialisation compatibility. */
	private static final long serialVersionUID = -5946129392887657748L;

public NewValueDialog(Type root, String title, Boolean forceAdd)
  {
    super(title,new NewValuePanelType(root),Environment.env);
    genericSetup(forceAdd.booleanValue());
  }

  public NewValueDialog(Type.Triv root, String title, Boolean forceAdd)
  {
    super(title,new NewValuePanelTriv(root),Environment.env);
    genericSetup(forceAdd.booleanValue());
  }

  public NewValueDialog(Type.Str root, String title, Boolean forceAdd)
  {
    super(title,new NewValuePanelString(root),Environment.env);
    genericSetup(forceAdd.booleanValue());
  }

  public NewValueDialog(Type.Scalar root, String title, Boolean forceAdd)
  {
    super(title,new NewValuePanelScalar(root),Environment.env);
    genericSetup(forceAdd.booleanValue());
  }

  public NewValueDialog(Type.Discrete root, String title, Boolean forceAdd)
  {
    super(title,new NewValuePanelDiscrete(root),Environment.env);
    genericSetup(forceAdd.booleanValue());
  }

  public NewValueDialog(Type.Continuous root, String title, Boolean forceAdd)
  {
    super(title,new NewValuePanelContinuous(root),Environment.env);
    genericSetup(forceAdd.booleanValue());
  }

  public NewValueDialog(Type.Symbolic root, String title, Boolean forceAdd)
  {
    super(title,new NewValuePanelSymbolic(root),Environment.env);
    genericSetup(forceAdd.booleanValue());
  }

  public NewValueDialog(Type.Vector root, String title, Boolean forceAdd)
  {
    super(title,new NewValuePanelVector(root),Environment.env);
    genericSetup(forceAdd.booleanValue());
  }

  public NewValueDialog(Type.Function root, String title, Boolean forceAdd)
  {
    super(title,new NewValuePanelFunction(root),Environment.env);
    genericSetup(forceAdd.booleanValue());
  }

  public NewValueDialog(Type.Structured root, String title, Boolean forceAdd)
  {
    super(title,new NewValuePanelStructured(root),Environment.env);
    genericSetup(forceAdd.booleanValue());
  }

}
