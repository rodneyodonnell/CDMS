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

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RandomSeedDialog extends CDMSDialog
{
  /** Serial ID required to evolve class while maintaining serialisation compatibility. */
	private static final long serialVersionUID = -8565255585981144935L;
private JLabel label = new JLabel("Enter new seed:");
  private JTextField field = new JTextField("0");
  private JButton ok = new JButton("OK");
  private JButton cancel = new JButton("Cancel");
  private JPanel buttonPanel = new JPanel();
  private JPanel okPanel = new JPanel();
  private JPanel cancelPanel = new JPanel();
  private long seed;

  public RandomSeedDialog()
  {
    super("Choose new random-number-generator seed...");
    field.setPreferredSize(new Dimension(getFontMetrics(getFont()).stringWidth("12345678901234567890"), getFontMetrics(getFont()).getHeight()));
    buttonPanel.setLayout(new GridLayout(1,2));
    okPanel.add(ok);
    cancelPanel.add(cancel);
    buttonPanel.add(okPanel);
    buttonPanel.add(cancelPanel);
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(label, BorderLayout.WEST);
    getContentPane().add(field, BorderLayout.CENTER);
    getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    field.addKeyListener(new KeyListener()
    {
      public void keyPressed(KeyEvent e){}
      public void keyTyped(KeyEvent e){} 
      public void keyReleased(KeyEvent e)
      {
        try
        {
          seed = Long.parseLong(field.getText());
          ok.setEnabled(true);
        }
        catch(Exception ex)
        {
          ok.setEnabled(false);
          seed = -1;
        }
      }
    });
    ok.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        dispose();
      }
    });
    cancel.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        seed = -1;
        dispose();
      }
    });
    pack();
    center();
    setVisible(true);
  }

  public long getResult()
  {
    return seed;
  }
}
