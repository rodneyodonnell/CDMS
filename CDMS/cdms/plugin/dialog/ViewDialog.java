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

// File: ViewDialog.java
// Authors: {leighf,joshc}@csse.monash.edu.au

package cdms.plugin.dialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ViewDialog extends CDMSDialog
{
  /** Serial ID required to evolve class while maintaining serialisation compatibility. */
	private static final long serialVersionUID = 5459623759748050338L;
private JButton ok = new JButton("OK");
  private JPanel panel = new JPanel();
  private JPanel buttonPanel = new JPanel();
  private NewPanel ntp;
  private JLabel commentLabel = new JLabel("Comment: ");
  private JLabel comment = new JLabel();

  public ViewDialog(String title,NewPanel ntp,String comment)
  {
    super(title);
    this.ntp = ntp;
    this.comment.setText(comment);
    genericSetup();
  }

  public ViewDialog(String title,NewPanel ntp)
  {
    super(title);
    this.ntp = ntp;
    genericSetup();
  }

  protected void genericSetup()
  {
    NewPanel.recursiveDisable(ntp);
    panel.setLayout(new BorderLayout());
    buttonPanel.add(ok);
    panel.add(buttonPanel, BorderLayout.SOUTH);
    panel.add(commentLabel, BorderLayout.WEST);
    panel.add(comment, BorderLayout.CENTER);
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(panel, BorderLayout.SOUTH);
    getContentPane().add(ntp, BorderLayout.CENTER);
    ok.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        dispose();
      }
    });
    pack();
    center();
    setVisible(true);
  }
}
