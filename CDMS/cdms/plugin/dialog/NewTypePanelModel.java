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

// File: NewTypeDialogVector.java
// Class: NewTypeDialogVector
// Authors: {joshc}@cs.monash.edu.au

package cdms.plugin.dialog;

import javax.swing.*;
import java.awt.*;
import cdms.core.*;

public class NewTypePanelModel extends NewPanel implements
  TypeFieldSelector.TypeFieldSelectorListener
{
  /** Serial ID required to evolve class while maintaining serialisation compatibility. */
	private static final long serialVersionUID = -2056178084356578840L;
private Type result = null;
  private JPanel northPanel = new JPanel();
  private TypeFieldSelector paramSelector;
  private TypeFieldSelector dataSelector;
  private TypeFieldSelector sharedSelector;
  private TypeFieldSelector sufficientSelector;
  private Type.Model tf;

  public Object getResult()
  {
    return result;
  }

  public boolean getInitialOKState()
  {
    return true;
  }

  public NewTypePanelModel(Type.Model t)
  {
    super();
    tf = t;
    northPanel.setBorder(BorderFactory.createEtchedBorder());
    setLayout(new BorderLayout());
    northPanel.setLayout(new GridLayout(4,1));
    paramSelector = new TypeFieldSelector("Parameter Space:", "Select Parameter Space...", this,
      tf.paramSpace);
    dataSelector = new TypeFieldSelector("Data Space:", "Select Data Space...", this,
      tf.dataSpace);
    sharedSelector = new TypeFieldSelector("Shared (input) Space:", "Select Shared Space...", this,
      tf.sharedSpace);
    sufficientSelector = new TypeFieldSelector("Sufficient Space:", "Select Sufficient Space...",
      this, tf.sufficientSpace);
    northPanel.add(paramSelector);
    northPanel.add(dataSelector);
    northPanel.add(sharedSelector);
    northPanel.add(sufficientSelector);
    add(northPanel, BorderLayout.CENTER);
  }

  public void selectionChanged()
  {
    if((paramSelector.getSelection() != null)&&(dataSelector.getSelection() !=
      null)&&(sharedSelector.getSelection() != null)&&(sufficientSelector.getSelection() != null))
    {
      result = new Type.Model(dataSelector.getSelection(),
        paramSelector.getSelection(), sharedSelector.getSelection(),
        sufficientSelector.getSelection());
      okListener.okEvent(true);
    }
    else
    {
      result = null;
      okListener.okEvent(false);
    }
  }
}