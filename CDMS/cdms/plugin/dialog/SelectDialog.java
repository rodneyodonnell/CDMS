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

// File: NewTypeDialog.java
// Authors: {joshc}@cs.monash.edu.au

package cdms.plugin.dialog;

import javax.swing.*;
import cdms.core.*;
import java.awt.*;
import java.awt.event.*;

public class SelectDialog extends CDMSDialog
{
  /** Serial ID required to evolve class while maintaining serialisation compatibility. */
	private static final long serialVersionUID = 391347010580577849L;

private SelectPanel selectPanel;

  private JButton okButton = new JButton("OK");
  private JButton cancelButton = new JButton("Cancel");
  private JPanel buttonPanel = new JPanel();
  private JPanel okPanel = new JPanel();
  private JPanel cancelPanel = new JPanel();
  private Object result = null;

  public SelectDialog(String message, Type root, Type withMember, boolean showValues)
  {
    super(message);
    selectPanel = new SelectPanel("",root,withMember,showValues);
    getContentPane().setLayout(new BorderLayout());
    okPanel.add(okButton);
    cancelPanel.add(cancelButton);
    buttonPanel.setLayout(new GridLayout(1,2));
    buttonPanel.add(okPanel);
    buttonPanel.add(cancelPanel);
    getContentPane().add(selectPanel, BorderLayout.CENTER);
    getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    
    cancelButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        result = null;
        dispose();
      }
    });

    okButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        // Getting the selected Type or Value.
        result = selectPanel.getResult();
        dispose();
      }
    });

    pack();
    center();
    setVisible(true);
  }  

  public Object getResult()
  {
    return result;
  }
}
