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

// File: NewTypeDialogFunction.java
// Authors: {joshc}@cs.monash.edu.au

package cdms.plugin.dialog;

import javax.swing.*;
import cdms.core.*;
import java.awt.*;
import java.awt.event.*;

public class TypeFieldSelector extends JPanel
{
  /** Serial ID required to evolve class while maintaining serialisation compatibility. */
	private static final long serialVersionUID = -5358099099201566786L;
private JLabel label = new JLabel();
  private JLabel typeName = new JLabel("?");
  public JButton chooseButton = new JButton("Select...");
  private Type selection = Type.TYPE;
  private TypeFieldSelectorListener parent = null;
  private String selectorMessage;
  private JPanel typeNamePanel = new JPanel();
  private Type rootType;

  public TypeFieldSelector(String s, String sm, TypeFieldSelectorListener p)
  {
    this(s, sm, p, Type.TYPE);
  }

  public TypeFieldSelector(String s, String sm, TypeFieldSelectorListener p, Type root)
  {
    parent = p;
    selection = root;
    rootType = root;
    typeName.setText(selection.getTypeName());
    typeName.setPreferredSize(new Dimension(getFontMetrics(getFont()).stringWidth("                  "), (int)getFontMetrics(getFont()).getHeight()));
    selectorMessage = sm;
    setLayout(new BorderLayout());
    setBorder(BorderFactory.createEtchedBorder());
    label.setText(s);
    add(label, BorderLayout.WEST);
    add(chooseButton, BorderLayout.EAST);
    add(typeNamePanel, BorderLayout.CENTER);
    typeNamePanel.add(typeName);
    chooseButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e)
      {
        selection = (Type)new SelectDialog(selectorMessage, rootType, null, false).getResult();
        if(selection==null)
        { 
          selection = rootType;
        }
        typeName.setText(getTypeName(selection));
        parent.selectionChanged();
      }
    });
  }

  private String getTypeName(Type t)
  {
    String tmp = Type.byType(t);
    if(tmp != null)
    {
      return tmp;
    }
    else
    {
      return t.getTypeName();
    }
  }

  public Type getSelection()
  {
    return selection;
  }

  public interface TypeFieldSelectorListener
  {
    public void selectionChanged();
  }
}
