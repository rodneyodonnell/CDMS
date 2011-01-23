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

// File: ViewTypeDialog.java
// Authors: {leighf,joshc}@csse.monash.edu.au

package cdms.plugin.dialog;

import cdms.core.*;

public class ViewTypeDialog extends ViewDialog
{
  /** Serial ID required to evolve class while maintaining serialisation compatibility. */
	private static final long serialVersionUID = 7179086188138762073L;

private static String getDescription(Type t)
  {
    Environment.RegEntry r = Environment.env.getEntryByObject(t);
    if(r != null) return r.description;
    return "";
  }

  public ViewTypeDialog(Type t)
  {
    super("View Type",new NewTypePanelType(t), getDescription(t));
  }

  public ViewTypeDialog(Type.Triv t)
  {
    super("View Type",new NewTypePanelTriv(t), getDescription(t));
  }

  public ViewTypeDialog(Type.Str t)
  {
    super("View Type",new NewTypePanelString(t), getDescription(t));
  }

  public ViewTypeDialog(Type.Scalar t)
  {
    super("View Type",new NewTypePanelScalar(t), getDescription(t));
  }

  public ViewTypeDialog(Type.Discrete t)
  {
    super("View Type",new NewTypePanelDiscrete(t), getDescription(t));
  }

  public ViewTypeDialog(Type.Continuous t)
  {
    super("View Type",new NewTypePanelContinuous(t), getDescription(t));
  }

  public ViewTypeDialog(Type.Vector t)
  {
    super("View Type",new NewTypePanelVector(t), getDescription(t));
  }

  public ViewTypeDialog(Type.Function t)
  {
    super("View Type",new NewTypePanelFunction(t), getDescription(t));
  }

  public ViewTypeDialog(Type.Symbolic t)
  {
    super("View Type",new NewTypePanelSymbolic(t), getDescription(t));
  }

  public ViewTypeDialog(Type.Structured t)
  {
    super("View Type",new NewTypePanelStructured(t), getDescription(t));
  }

  /** Open a NewTypeTriv panel - cannot currently make new Type.Obj's. */
  public ViewTypeDialog(Type.Obj t)
  {
    super("View Type",new NewTypePanelTriv(t), getDescription(t));
  }

  public ViewTypeDialog(Type.Model t)
  {
    super("View Type",new NewTypePanelModel(t), getDescription(t));
  }

}
