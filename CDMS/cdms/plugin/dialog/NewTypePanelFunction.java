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
// Authors: {joshc}@cs.monash.edu.au

package cdms.plugin.dialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import cdms.core.*;

public class NewTypePanelFunction extends NewPanel implements
  TypeFieldSelector.TypeFieldSelectorListener
{
  /** Serial ID required to evolve class while maintaining serialisation compatibility. */
	private static final long serialVersionUID = -1816981290637326096L;
private Type result = null;
  private JPanel northPanel = new JPanel();
  private JPanel checkBoxPanel = new JPanel();
  private TypeFieldSelector paramSelector;
  private TypeFieldSelector resultSelector;
  private JCheckBox checkParamName = new JCheckBox("Check Paramater Type Name", false);
  private JCheckBox checkResultName = new JCheckBox("Check Result Type Name", false);
  private Type.Function tf;

  public Object getResult()
  {
    return result;
  }

  public boolean getInitialOKState()
  {
    return true;
  }

  public NewTypePanelFunction(Type.Function t)
  {
    super();
    tf = t;
    checkParamName.addActionListener(new checkListener());
    checkResultName.addActionListener(new checkListener());
    northPanel.setBorder(BorderFactory.createEtchedBorder());
    checkBoxPanel.setBorder(BorderFactory.createEtchedBorder());
    setLayout(new BorderLayout());
    northPanel.setLayout(new GridLayout(2,1));
    checkBoxPanel.setLayout(new GridLayout(2,1));
    checkBoxPanel.add(checkParamName);
    checkBoxPanel.add(checkResultName);
    checkParamName.setSelected(tf.checkParamName);
    checkResultName.setSelected(tf.checkResultName);
    paramSelector = new TypeFieldSelector("Parameter Type:", "Select Parameter Type...", this,
      tf.param);
    resultSelector = new TypeFieldSelector("Result Type:", "Select Result Type...", this,
      tf.result);
    northPanel.add(paramSelector);
    northPanel.add(resultSelector);
    add(checkBoxPanel, BorderLayout.CENTER);
    add(northPanel, BorderLayout.NORTH);
  }

  private class checkListener implements ActionListener
  {
    public void actionPerformed(ActionEvent e)
    {
      selectionChanged();
    }
  }

  public void selectionChanged()
  {
    if((paramSelector.getSelection() != null)&&(resultSelector.getSelection() != null))
    {
      result = new Type.Function(paramSelector.getSelection(), resultSelector.getSelection(),
        checkParamName.isSelected(), checkResultName.isSelected());
      okListener.okEvent(true);
    }
    else
    {
      result = null;
      okListener.okEvent(false);
    }
  }
}
