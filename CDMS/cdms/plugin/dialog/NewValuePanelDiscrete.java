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

// File: NewValuePanelDiscrete.java
// Authors: {leighf,joshc}@csse.monash.edu.au

package cdms.plugin.dialog;

import javax.swing.*;
import cdms.core.*;
import java.awt.*;
import java.awt.event.*;

public class NewValuePanelDiscrete extends NewPanel
{
  /** Serial ID required to evolve class while maintaining serialisation compatibility. */
	private static final long serialVersionUID = 4222601717092464410L;
private JTextField valueField = new JTextField();
  private JLabel valueLabel = new JLabel("Value: ");
  protected int result = 0;
  private Type.Discrete td;

  public NewValuePanelDiscrete(Type.Discrete t)
  {
    td = t;
    valueField.addKeyListener(new KeyListener()
    {
      public void keyPressed(KeyEvent e){}

      public void keyReleased(KeyEvent e)
      {
        try
        {
          result = Integer.parseInt(valueField.getText());
          if(okListener != null) okListener.okEvent(true);
        }
        catch(Exception ex)
        {
          if(okListener != null) okListener.okEvent(false);
        }
      }

      public void keyTyped(KeyEvent e){}
    });

    setLayout(new GridLayout(1,2));
    add(valueLabel);
    add(valueField);
  }

  public boolean getInitialOKState()
  {
    return false;
  }

  public Object getResult()
  {
    ValueStatus vs = Value.S_PROPER;
    if (result > td.UPB) vs = Value.S_INVALID;
    if(result < td.LWB) vs = Value.S_INVALID;
    return new Value.Discrete(td, vs, result);
  }
}
