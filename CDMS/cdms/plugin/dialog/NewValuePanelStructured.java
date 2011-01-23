
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

// File: NewValuePanelStructured.java
// Class: NewValuePanelStructured
// Authors: {leighf,joshc}@csse.monash.edu.au

package cdms.plugin.dialog;

import javax.swing.*;
import cdms.core.*;
import java.awt.*;
import java.awt.event.*;

public class NewValuePanelStructured extends NewPanel
{
  /** Serial ID required to evolve class while maintaining serialisation compatibility. */
	private static final long serialVersionUID = -1809778111998862949L;
private JButton select = new JButton("Select value");
  private JPanel selectPanel = new JPanel();
  private Value[] values;
  private JList cmpntList = new JList();
  private JScrollPane scroll = new JScrollPane(cmpntList);
  private Type.Structured ts;
  private String[] listData;

  public NewValuePanelStructured(Type.Structured t)
  {
    ts = t;
    if(ts.ckCmpnts)
    {
      values = new Value[ts.cmpnts.length];
      listData = new String[ts.cmpnts.length];
      select.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          int index = cmpntList.getSelectedIndex();
          values[index] = (Value)new SelectDialog("Select existing value for " + getCmpntName(index), ts.cmpnts[index], null, true).getResult();
          updateList(index);
          checkList();
        }
      });
      updateList();
      cmpntList.setSelectedIndex(0);
    }

    selectPanel.add(select);
    setLayout(new BorderLayout());
    add(scroll, BorderLayout.CENTER);
    add(selectPanel, BorderLayout.SOUTH);
  }

  private void updateList()
  {
    int count;
    for(count = 0; count < ts.cmpnts.length; count++)
    {
      if(values[count] != null) listData[count] = getCmpntName(count) + values[count].toString();
      else listData[count] = getCmpntName(count) + "?";
    }
    cmpntList.setListData(listData);
  }

  private void updateList(int cmpnt)
  {
    if(values[cmpnt] != null) listData[cmpnt] = getCmpntName(cmpnt) + values[cmpnt].toString();
    else listData[cmpnt] = getCmpntName(cmpnt) + "?";
    cmpntList.setListData(listData);
  }

  private void checkList()
  {
    if(okListener != null)
    {
      int count;
      for(count = 0; count < ts.cmpnts.length; count++)
      {
        if(values[count] == null)
        {
          okListener.okEvent(false);
          return;
        }
      }
      okListener.okEvent(true);
    }
  }

  private String getCmpntName(int cmpnt)
  {
    if (ts.labels != null && ts.labels[cmpnt] != null)
    {
      return ts.labels[cmpnt] + ": ";
    }
    else
    {
      return "Cmpnt " + cmpnt + ": ";
    }
  }

  public boolean getInitialOKState()
  {
     return !ts.ckCmpnts;  // If we know about the components, we must define them.   
                           // Otherwise, no details are required, and the initial ok state is true.
  }

  public Object getResult()
  {
    if(ts.ckCmpnts)
    {
      return new Value.DefStructured(ts, values);
    }
    else
    {
      return Value.TRIV;
    }
  }
}
