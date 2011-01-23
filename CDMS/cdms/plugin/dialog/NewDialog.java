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

// File: NewDialog.java
// Authors: {leighf,joshc}@csse.monash.edu.au

package cdms.plugin.dialog;

import javax.swing.*;
import cdms.core.*;
import java.awt.*;
import java.awt.event.*;

public class NewDialog extends CDMSDialog implements NewPanel.OKListener
{
  /** Serial ID required to evolve class while maintaining serialisation compatibility. */
	private static final long serialVersionUID = 6447500765559666903L;
protected JPanel southPanel = new JPanel();
  protected JPanel okPanel = new JPanel();
  protected JPanel cancelPanel = new JPanel();
  protected JPanel okCancelPanel = new JPanel();
  protected NewPanel centerPanel;
  protected JButton cancelButton = new JButton("Cancel");
  protected JButton okButton = new JButton("OK");
  protected JCheckBox addToEnvironment = new JCheckBox("Add to Environment", false);
  protected JTextField name = new JTextField("123334567890");
  protected JTextField comment = new JTextField("");
  protected JLabel commentLabel = new JLabel("Comment: ");
  protected JPanel commentPanel = new JPanel();
  protected JLabel nameLabel = new JLabel("Name: ");
  protected JPanel namePanel = new JPanel();
  protected JPanel bigSouthPanel = new JPanel();
  protected Object result = null;
  protected boolean badText = true;
  protected boolean badDetails = false;
  protected Environment env;

  public NewDialog(String title, NewPanel centerPanel, Environment env)
  {
    super(title);
    this.centerPanel = centerPanel;
    this.env = env;
  }

  protected void genericSetup(boolean forceAdd)
  {
    centerPanel.okListener = this;
    getContentPane().setLayout(new BorderLayout());
    name.addKeyListener(new NewNameKeyListener());
    southPanel.setLayout(new BorderLayout());
    southPanel.add(addToEnvironment, BorderLayout.WEST);
    namePanel.setLayout(new BorderLayout());
    namePanel.add(name, BorderLayout.CENTER);
    namePanel.add(nameLabel, BorderLayout.WEST);
    southPanel.add(namePanel, BorderLayout.CENTER);

    if (forceAdd)
    {
      addToEnvironment.setSelected(true);
      addToEnvironment.setEnabled(false);
      name.setEnabled(true);
      name.setVisible(true);
      comment.setVisible(true);
    }
    else
    {
      addToEnvironment.setSelected(true);
      addToEnvironment.setEnabled(true);
      name.setEnabled(true);
      name.setVisible(true);
      comment.setVisible(true);
    }

    okPanel.add(okButton);
    badDetails = !centerPanel.getInitialOKState();
    cancelPanel.add(cancelButton);
    okCancelPanel.setLayout(new GridLayout(1,2));
    okCancelPanel.add(okPanel);
    okCancelPanel.add(cancelPanel);
    southPanel.add(okCancelPanel, BorderLayout.SOUTH);
    commentPanel.setLayout(new BorderLayout());
    commentPanel.add(comment, BorderLayout.CENTER);
    commentPanel.add(commentLabel, BorderLayout.WEST);
    bigSouthPanel.setLayout(new BorderLayout());
    bigSouthPanel.add(commentPanel, BorderLayout.CENTER);
    bigSouthPanel.add(southPanel, BorderLayout.SOUTH);
    
    getContentPane().add(bigSouthPanel, BorderLayout.SOUTH);
    getContentPane().add(centerPanel, BorderLayout.CENTER);

    addToEnvironment.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        if(addToEnvironment.isSelected())
        {
          name.setEnabled(true);
          name.setVisible(true);
          comment.setVisible(true);
          setOKButton();
        }
        else
        {
          name.setText("");
          name.setEnabled(false);
          name.setVisible(false);
          comment.setVisible(false);
          setOKButton();
        }
      }
    });


    setupButtons();
    pack();
    name.setText("");
    center();
    setVisible(true);
  }

  private void setupButtons()
  {
    okButton.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        result = centerPanel.getResult();
        if(addToEnvironment.isSelected())
        {
          env.add(name.getText(), "Standard", result, comment.getText());
        }
        dispose();
      }
    });
    cancelButton.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        result = null;
        dispose();
      }
    });
    setOKButton();
  }

  public Object getResult()
  {
    return result;
  }

  private void setOKButton()
  {
    if(addToEnvironment.isSelected())
    {
      okButton.setEnabled((!badText)&&(!badDetails));
    }
    else
    {
      okButton.setEnabled(!badDetails);
    }
  }

  public void okEvent(boolean enabled)
  {
    badDetails = !enabled;
    setOKButton();
  }

  private class NewNameKeyListener implements java.awt.event.KeyListener
  {
    public void keyPressed(KeyEvent e){}
    public void keyTyped(KeyEvent e){} 
    public void keyReleased(KeyEvent e)
    {
      String tmp = name.getText();
      if ((env.getObject(tmp,"Standard") != null) || (tmp.length() == 0))
      {
        badText = true;
        setOKButton();
      }
      else
      {
        badText = false;
        setOKButton();
      }
    }
  }
}
