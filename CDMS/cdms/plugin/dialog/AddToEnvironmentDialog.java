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

package cdms.plugin.dialog;

import cdms.core.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;


public class AddToEnvironmentDialog extends CDMSDialog
{
  /** Serial ID required to evolve class while maintaining serialisation compatibility. */
	private static final long serialVersionUID = 5637347071372177576L;
	
  private JTextField comment = new JTextField("012345678901234567890");
  private JLabel lComment = new JLabel("Comment: ");
  private JPanel pComment = new JPanel();
  private JTextField name = new JTextField("01234567890");
  private JLabel lName = new JLabel("Name: ");
  private JPanel pName = new JPanel();
  private JButton ok = new JButton("OK");
  private JButton cancel = new JButton("Cancel");
  private JPanel pButton = new JPanel();
  private Value val;
  

  public AddToEnvironmentDialog(Value v)
  {
    super("Add Value to Environment...");
    val = v;
    ok.addActionListener(new ActionListener()
    { 
      public void actionPerformed(ActionEvent e)
      {
        Environment.env.add(name.getText(), "Standard", val, comment.getText());
        dispose();
      }
    });
    cancel.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        dispose();
      }
    });
    name.addKeyListener(new KeyListener()
    {
      public void keyPressed(KeyEvent e)
      {}
      public void keyTyped(KeyEvent e)
      {}
      public void keyReleased(KeyEvent e)
      {
        try
        {
          if(Environment.env.getObject(name.getText(), "Standard") != null)
          {
            ok.setEnabled(false);
          }
          else
          {
            ok.setEnabled(true);
          }
        }
        catch(Exception ex)
        {
          ok.setEnabled(true);
        }
      }
    });
    getContentPane().setLayout(new GridLayout(3,1));
    pName.setLayout(new BorderLayout());
    pName.add(name, BorderLayout.CENTER);
    pName.add(lName, BorderLayout.WEST);
    pComment.setLayout(new BorderLayout());
    pComment.add(comment, BorderLayout.CENTER);
    pComment.add(lComment, BorderLayout.WEST);
    pButton.setLayout(new GridLayout(1,2));
    pButton.add(ok);
    pButton.add(cancel);
    getContentPane().add(pName);
    getContentPane().add(pComment);
    getContentPane().add(pButton);
    pack();
    comment.setText("");
    name.setText("");
    center();
    setVisible(true);
  }
}
