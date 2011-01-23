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

// File: NewValuePanelVector.java
// Authors: {leighf,joshc}@@csse.monash.edu.au

package cdms.plugin.dialog;

import javax.swing.*;
import cdms.core.*;
import java.awt.*;
import java.awt.event.*;

public class NewValuePanelVector extends NewPanel
{
  /** Serial ID required to evolve class while maintaining serialisation compatibility. */
	private static final long serialVersionUID = 5269890860418831350L;
private Type.Vector tv;
  private JTextField lengthField = new JTextField("1");
  private JList list = new JList();
  private JScrollPane scroll = new JScrollPane(list);
  private Value[] values;
  private String[] listData;
  private JButton select = new JButton("Select value");
  private JButton apply = new JButton("Apply");
  private JPanel applyPanel = new JPanel();
  private JPanel scrollPanel = new JPanel();
  private JLabel lengthLabel = new JLabel("Length: ");

  public NewValuePanelVector(Type.Vector t)
  {
    tv = t;
    select.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        int index = list.getSelectedIndex();
        values[index] = (Value)new SelectDialog("Select value for element " + index, tv.elt, null, true).getResult();
        setupList(index);
        checkOK();
      }
    });
    apply.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        changeLength(Integer.parseInt(lengthField.getText()));
      }
    });
    lengthField.addKeyListener(new KeyListener()
    {
      public void keyPressed(KeyEvent e){}
      public void keyTyped(KeyEvent e){} 
      public void keyReleased(KeyEvent e)
      {
        try
        {
          int tmp = Integer.parseInt(lengthField.getText());
          apply.setEnabled(tmp > -1);  // make sure we have a non-negative vector length.
        }
        catch(Exception ex)
        {
          apply.setEnabled(false);
        }
      }
    });
    changeLength(1);
    list.setSelectedIndex(0);
    setLayout(new BorderLayout());
    applyPanel.setLayout(new BorderLayout());
    applyPanel.add(lengthLabel, BorderLayout.WEST);
    applyPanel.add(lengthField, BorderLayout.CENTER);
    applyPanel.add(apply, BorderLayout.EAST);
    applyPanel.setBorder(BorderFactory.createEtchedBorder());
    scrollPanel.setLayout(new BorderLayout());
    scrollPanel.add(scroll, BorderLayout.CENTER);
    scrollPanel.add(select, BorderLayout.SOUTH);
    add(applyPanel, BorderLayout.NORTH);
    add(scrollPanel, BorderLayout.CENTER);     
  }

  private String getString(int elt)
  {
    if(values[elt] == null)
    {
      return "Element " + elt + ": ";
    }
    else
    {
      return "Element " + elt + ": " + values[elt].toString();
    }
  }

  private void setupList()
  {
    int count;
    listData = new String[values.length];
    for(count = 0; count < values.length; count++)
    {
      listData[count] = getString(count);
    }
    list.setListData(listData);
  }

  private void setupList(int elt)
  {
    listData[elt] = getString(elt);
    list.setListData(listData);
  }

  private void checkOK()
  {
    if(okListener != null)
    {
      int count;
      boolean ok = true;
      for(count = 0; count < values.length; count++)
      {
        if(values[count] == null)
        {
          ok = false;
          break;
        }
      }
      okListener.okEvent(ok);
    }
  }

  private void changeLength(int newLength)
  {
    lengthField.setText("" + newLength);
    int oldLength;
    Value[] tmp = new Value[newLength];
    int count;
    if(values != null)
      oldLength = values.length;
    else
      oldLength = 0;

    if(oldLength > newLength)
    {
      for(count = 0; count < newLength; count++)
      {
        tmp[count] = values[count];
      }
    }
    else
    {
      for(count = 0; count < oldLength; count++)
      {
        tmp[count] = values[count];
      }
      for(count = oldLength; count < newLength; count++)
      {
        tmp[count] = null;
      }
    }
    values = tmp;
    setupList();
    checkOK();
  }

  public boolean getInitialOKState()
  {
    return false;
  }

  public Object getResult()
  {
    return new cdms.core.VectorFN.FatVector(values, tv);
  }
}
