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

// File: NewTypePanelVector.java
// Authors: {joshc}@cs.monash.edu.au

package cdms.plugin.dialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import cdms.core.*;

public class NewTypePanelVector extends NewPanel implements TypeFieldSelector.TypeFieldSelectorListener
{
  /** Serial ID required to evolve class while maintaining serialisation compatibility. */
	private static final long serialVersionUID = 5440875441728233558L;
private Type result = null;
//  private JPanel mainPanel = new JPanel();
  private JPanel northPanel = new JPanel();
  private JPanel centerPanel = new JPanel();
  private JPanel checkBoxPanel = new JPanel();
  private JPanel radioPanel = new JPanel();
  private TypeFieldSelector eltSelector;
  private TypeFieldSelector indexSelector;
  private ButtonGroup sequenceGroup = new ButtonGroup();
  private JRadioButton isSequence = new JRadioButton("Sequential", false);
  private JRadioButton isNotSequence = new JRadioButton("Not Sequential", false);
  private JRadioButton unspecified = new JRadioButton("Unspecified", true);
  private JCheckBox checkEltName = new JCheckBox("Check Elt Type Name", false);
  private JCheckBox checkIndexName = new JCheckBox("Check Index Type Name", false);
  private Type.Vector tv;

  public Object getResult()
  {
    return result;
  }

  public NewTypePanelVector(Type.Vector t)
  {
    super();
    tv = t;

    checkEltName.addActionListener(new checkListener());
    checkIndexName.addActionListener(new checkListener());
    isSequence.addActionListener(new checkListener());
    isNotSequence.addActionListener(new checkListener());
    unspecified.addActionListener(new checkListener());

    northPanel.setBorder(BorderFactory.createEtchedBorder());
    radioPanel.setBorder(BorderFactory.createEtchedBorder());
    checkBoxPanel.setBorder(BorderFactory.createEtchedBorder());
    sequenceGroup.add(isSequence);
    sequenceGroup.add(isNotSequence);
    sequenceGroup.add(unspecified);
//    mainPanel.setLayout(new BorderLayout());
    setLayout(new BorderLayout());

    checkEltName.setSelected(tv.checkEltName);
    checkIndexName.setSelected(tv.checkIndexName);
    if(tv.ckIsSequence)
    {
      if(tv.isSequence)
      {
        isSequence.setSelected(true);
      }
      else
      {
        isNotSequence.setSelected(true);
      }
      isSequence.setEnabled(false);
      isNotSequence.setEnabled(false);
      unspecified.setEnabled(false);
    }
    else
    {
      unspecified.setSelected(true);
    }
    northPanel.setLayout(new GridLayout(2,1));
    centerPanel.setLayout(new GridLayout(1,2));
    centerPanel.add(checkBoxPanel);
    centerPanel.add(radioPanel);
    checkBoxPanel.setLayout(new GridLayout(2,1));
    checkBoxPanel.add(checkEltName);
    checkBoxPanel.add(checkIndexName);
    radioPanel.setLayout(new GridLayout(3,1));
    radioPanel.add(isSequence);
    radioPanel.add(isNotSequence);
    radioPanel.add(unspecified);
    eltSelector = new TypeFieldSelector("Element Type:", "Select Element Type...", this, tv.elt);
    indexSelector = new TypeFieldSelector("Index Type:", "Select Index Type...", this, tv.index);
    northPanel.add(eltSelector);
    northPanel.add(indexSelector);
//    mainPanel.add(centerPanel, BorderLayout.CENTER);
//    mainPanel.add(northPanel, BorderLayout.NORTH);
//    add(mainPanel);
    add(centerPanel, BorderLayout.CENTER);
    add(northPanel, BorderLayout.NORTH);
  }

  public boolean getInitialOKState()
  {
    return false;
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
    if((eltSelector.getSelection() != null)&&(indexSelector.getSelection() != null))
    {
      boolean ckIsSequence = !unspecified.isSelected();
      boolean is = false;
      if(ckIsSequence)
      {
        is = isSequence.isSelected();
      }
      result = new Type.Vector(indexSelector.getSelection(), eltSelector.getSelection(), ckIsSequence, is, checkEltName.isSelected(), checkIndexName.isSelected());
      okListener.okEvent(true);
    }
    else
    {
      result = null;
      okListener.okEvent(false);
    }
  }
}
