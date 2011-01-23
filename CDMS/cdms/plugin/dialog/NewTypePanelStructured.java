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

// File: NewTypeDialogVector.java
// Authors: {joshc}@cs.monash.edu.au

package cdms.plugin.dialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import cdms.core.*;

public class NewTypePanelStructured extends NewPanel implements
  TypeFieldSelector.TypeFieldSelectorListener
{
  /** Serial ID required to evolve class while maintaining serialisation compatibility. */
	private static final long serialVersionUID = 1444692965954626696L;
private StructuredTypeFieldSelector[] tfsArray = null;
  private JPanel tfsArrayPanel = new JPanel();
  private JScrollPane scroll = new JScrollPane(tfsArrayPanel);
  private JTextField nComp = new JTextField("1");
  private JLabel nCompLabel = new JLabel("Number of Components:");
  private JPanel nCompPanel = new JPanel();
  private JPanel applyButtonPanel = new JPanel();
  private JButton applyButton = new JButton("Apply");
  private Type result = null;
  private Type.Structured ts;

  public NewTypePanelStructured(Type.Structured t)
  {
    super();
    ts = t;
    if(ts.ckCmpnts)
    {
      setNumberOfCmpnts(ts.cmpnts.length);
      nComp.setEnabled(false);
      applyButton.setEnabled(false);
    }
    else
    {
      setNumberOfCmpnts(1);
      nComp.addKeyListener(new KeyListener()
      {
        public void keyPressed(KeyEvent e){}
        public void keyTyped(KeyEvent e){} 
        public void keyReleased(KeyEvent e)
        {
          int n;
          try
          {
            n = Integer.parseInt(nComp.getText());
          }
          catch(Exception exc)
          {
            n = 0;
          }
          if(n > 0)
          {
            applyButton.setEnabled(true);
          }
          else
          {
            applyButton.setEnabled(false);
          }
        }
      });
      applyButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          try
          {
            setNumberOfCmpnts(Integer.parseInt(nComp.getText()));
          }
          catch(Exception exc)
          {
            exc.printStackTrace();
          }
        }
      });
    }
    scroll.setPreferredSize(new Dimension((int)tfsArray[0].getPreferredSize().getWidth() + (int)getFontMetrics(getFont()).stringWidth("        "), (int)getFontMetrics(getFont()).getHeight()*10));
    applyButton.setEnabled(false);
    nCompPanel.setBorder(BorderFactory.createEtchedBorder());
    nCompPanel.setLayout(new FlowLayout());
    applyButtonPanel.add(applyButton);
    nComp.setPreferredSize(new Dimension((int)(getFontMetrics(getFont()).stringWidth("1234")),(int)(getFontMetrics(getFont()).getHeight())));
    nCompPanel.add(nCompLabel);
    nCompPanel.add(nComp);
    nCompPanel.add(applyButtonPanel);
    setLayout(new BorderLayout());
    add(nCompPanel, BorderLayout.NORTH);
    add(scroll, BorderLayout.CENTER);
  }

  public void selectionChanged()
  {
    okListener.okEvent(okEnable());
  }

  public boolean getInitialOKState()
  {
    return true;
  }

  private boolean okEnable()
  {
    int count;
    String[] names = new String[tfsArray.length];
    Type[] typeArray = new Type[tfsArray.length];
    boolean[] checkCmpntNames = new boolean[tfsArray.length];
    
    for(count = 0; count < tfsArray.length; count++)
    {
      if(tfsArray[count].tfs.getSelection() == null)
      {
        result = null;
        return false;
      }
      else
      {
        names[count] = tfsArray[count].tf.getText();
        typeArray[count] = tfsArray[count].tfs.getSelection();
        checkCmpntNames[count] = tfsArray[count].check.isSelected();
      }
    }
    result = new Type.Structured(typeArray, names, checkCmpntNames);
    return true;
  }

  private void setNumberOfCmpnts(int n)
  {
    if (n > 0)
    {
      int count, oldn;
      StructuredTypeFieldSelector[] tmp = tfsArray;
      if(tfsArray != null)
      {
        oldn = tfsArray.length;
      }
      else
      {
        oldn = 0;
      }
      if(n > oldn)
      {
        tfsArray = new StructuredTypeFieldSelector[n];
        for(count = 0; count < oldn; count++)
        {
          tfsArray[count] = tmp[count];
        }
        if(ts.ckCmpnts)
        {
          for(count = oldn; count < n; count++)
          {
            tfsArray[count] = new StructuredTypeFieldSelector(new TypeFieldSelector(
              "Cmpnt "+(count+1), "Select Type of Component "+(count+1),this,ts.cmpnts[count]),
              ts.labels[count], ts.checkCmpntsNames[count]);
          }
        }
        else
        {
          for(count = oldn; count < n; count++)
          {
            tfsArray[count] = new StructuredTypeFieldSelector(new TypeFieldSelector("Cmpnt"
              +(count+1), "Select Type of Component "+(count+1),this));
          }
        }
      }
      else
      {
        tfsArray = new StructuredTypeFieldSelector[n];
        for(count = 0; count < n; count++)
        {
          tfsArray[count] = tmp[count];
        }
      }
      tfsArrayPanel.removeAll();
      tfsArrayPanel.setLayout(new GridLayout(n,1));
      for(count = 0; count < n; count++)
      {
        tfsArrayPanel.add(tfsArray[count]);
      }
      scroll.setViewportView(tfsArrayPanel);
    }
    nComp.setText(n +"");
    okEnable();
  }

  public Object getResult()
  {
    return result;
  }

  public class StructuredTypeFieldSelector extends JPanel
  {
    /** Serial ID required to evolve class while maintaining serialisation compatibility. */
	private static final long serialVersionUID = 5158056881557804307L;
	public TypeFieldSelector tfs;
    public JTextField tf = new JTextField("");
    public JCheckBox check = new JCheckBox("Check cmpnt name", false);
    private JPanel panel = new JPanel();

    public StructuredTypeFieldSelector(TypeFieldSelector tfs)
    {
      super();
      this.tfs = tfs;
      tf.setPreferredSize(new Dimension(getFontMetrics(getFont()).stringWidth("                          "), (int)tf.getPreferredSize().getHeight()));
      tf.addKeyListener(new KeyListener()
      {
         public void keyPressed(KeyEvent e){}
         public void keyTyped(KeyEvent e){}
         public void keyReleased(KeyEvent e)
         {
           okEnable();
         }
      });
      check.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          okEnable();
        }
      });
      panel.setLayout(new BorderLayout());
      panel.add(check, BorderLayout.EAST);
      panel.add(this.tfs, BorderLayout.CENTER);
      panel.add(tf, BorderLayout.WEST);
      add(panel);
    }

    public StructuredTypeFieldSelector(TypeFieldSelector tfs, String cmpntName, boolean checkName)
    {
      super();
      this.tfs = tfs;
      tf.setPreferredSize(new Dimension(getFontMetrics(getFont()).stringWidth(
        "                          "), (int)tf.getPreferredSize().getHeight()));
      tf.setText(cmpntName);
      if(checkName)
      {
        check.setSelected(true);
        this.tfs.setEnabled(false);
        this.tfs.chooseButton.setEnabled(false);
      }
      tf.addKeyListener(new KeyListener()
      {
         public void keyPressed(KeyEvent e){}
         public void keyTyped(KeyEvent e){}
         public void keyReleased(KeyEvent e)
         {
           okEnable();
         }
      });
      check.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          okEnable();
        }
      });
      panel.setLayout(new BorderLayout());
      panel.add(check, BorderLayout.EAST);
      panel.add(this.tfs, BorderLayout.CENTER);
      panel.add(tf, BorderLayout.WEST);
      add(panel);
    }
  }
}
