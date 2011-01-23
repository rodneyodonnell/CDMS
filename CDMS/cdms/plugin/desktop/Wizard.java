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

package cdms.plugin.desktop;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

/** A generic "Wizard" component that navigates back and forth between panels. */
public class Wizard extends JPanel implements WizardEnableListener, ActionListener, Serializable
{
  /** Serial ID required to evolve class while maintaining serialisation compatibility. */
	private static final long serialVersionUID = 1569878039977102067L;

public static abstract class WizardPanel extends JPanel
  {
    private boolean nextEnabled = true;
    private boolean backEnabled = true;
    protected java.util.Vector listeners = new java.util.Vector();

    /** Returns next panel or returns null if an external event was spawned. */
    public abstract WizardPanel getNext();
   
    /** Returns the url for a help file on the wizard panel.  Returning null will
        disable the help button on the Wizard for this panel.
    */
    public java.net.URL getHelp(){ return null; }
  
    public boolean wantScroller() { return true; }

    public boolean getNextEnabled() { return nextEnabled; }
    public boolean getBackEnabled() { return backEnabled; }

    public void setNextEnabled(boolean en)
    {
      nextEnabled = en;
      notifyListenersNext();
    }

    public void setBackEnabled(boolean en)
    {
      backEnabled = en;
      notifyListenersBack();
    }
 
    public void addEnableListener(WizardEnableListener el)
    {
      listeners.add(el);
      el.enableNext(getNextEnabled());
      el.enableBack(getBackEnabled());
    }

    public void notifyListenersNext()
    {
      for (int i = 0; i < listeners.size(); i++)
        ((WizardEnableListener) listeners.elementAt(i)).enableNext(getNextEnabled());
    }

    public void notifyListenersBack()
    {
      for (int i = 0; i < listeners.size(); i++)
        ((WizardEnableListener) listeners.elementAt(i)).enableBack(getBackEnabled());
    }
  }

  JScrollPane mainPanel = new JScrollPane();
  JPanel scrollPanel = new JPanel();
  JButton nextBtn = new JButton("Next");
  JButton backBtn = new JButton("Back");
  JButton printBtn = new JButton("Print");
  JButton helpBtn = new JButton("Help");

  java.util.Vector panels = new java.util.Vector();

  public static void main(String argv[])
  {
    JFrame f = new JFrame("Wizard Test");
    f.getContentPane().add(new Wizard(null));
    f.setSize(400,300);
    f.setVisible(true);
  }

  public Wizard(WizardPanel panel)
  {
    super(new BorderLayout());

    panel.addEnableListener(this);
    panels.add(panel);

    nextBtn.setMnemonic('N');     
    nextBtn.addActionListener(this); 
    backBtn.setMnemonic('B');     
    backBtn.addActionListener(this); 
    printBtn.setMnemonic('P');     
    printBtn.addActionListener(this); 
    helpBtn.setMnemonic('H');     
    helpBtn.addActionListener(this); 

    if (panel.wantScroller())
    {
      scrollPanel.add(panel);
      mainPanel.setViewportView(scrollPanel);
      add(mainPanel,BorderLayout.CENTER);
    }
    else add(panel,BorderLayout.CENTER);

    JPanel southPanel = new JPanel();
    southPanel.add(backBtn);
    southPanel.add(nextBtn);
    southPanel.add(printBtn);
    southPanel.add(helpBtn);
    add(southPanel,BorderLayout.SOUTH);
    backBtn.setEnabled(panel.getBackEnabled());
    nextBtn.setEnabled(panel.getNextEnabled());
    helpBtn.setEnabled(panel.getHelp() != null);
  }

  public void enableNext(boolean e)
  {
    nextBtn.setEnabled(e);
  }

  public void enableBack(boolean e)
  {
    backBtn.setEnabled(e);
  }

  public void actionPerformed(java.awt.event.ActionEvent e) 
  {
    WizardPanel currentPanel = (WizardPanel) panels.elementAt(panels.size()-1); 
    if (e.getSource() == nextBtn) 
    {
      try
      {
        WizardPanel p = currentPanel.getNext();
        if (p != null) 
        {
          p.addEnableListener(this);
          panels.add(p);

          if (currentPanel.wantScroller())
            scrollPanel.remove(currentPanel);
          else remove(currentPanel);

          if (p.wantScroller())
          {
            scrollPanel.add(p);
            if (!currentPanel.wantScroller())
              add(mainPanel,BorderLayout.CENTER);
            mainPanel.setViewportView(scrollPanel);
          }
          else
          {
            if (currentPanel.wantScroller())
              remove(mainPanel);
            add(p,BorderLayout.CENTER);
          }
          
          nextBtn.setEnabled(p.getNextEnabled());
          backBtn.setEnabled(p.getBackEnabled());
          helpBtn.setEnabled(p.getHelp() != null);
          validate();
          repaint();
        }
      }
      catch (Exception exc)
      {
        System.out.println(exc);
        exc.printStackTrace();
      }
    }
    else
    {
      if(e.getSource() == backBtn)
      {
        if (panels.size() > 1)  // must have 1 panel.
        {
          panels.removeElementAt(panels.size()-1);

          WizardPanel p = (WizardPanel)panels.elementAt(panels.size()-1);

          if (currentPanel.wantScroller())
            scrollPanel.remove(currentPanel);
          else remove(currentPanel);

          if (p.wantScroller())
          {
            scrollPanel.add(p);
            if (!currentPanel.wantScroller())
              add(mainPanel,BorderLayout.CENTER);
            mainPanel.setViewportView(scrollPanel);
          }
          else
          {
            if (currentPanel.wantScroller())
              remove(mainPanel);
            add(p,BorderLayout.CENTER);
          }
          nextBtn.setEnabled(p.getNextEnabled());
          backBtn.setEnabled(p.getBackEnabled());
          helpBtn.setEnabled(p.getHelp() != null);
          validate();
          repaint();
        }
        if (panels.size() == 1) backBtn.setEnabled(false);
      }
      else if (e.getSource() == printBtn)
      {
        Desktop.PrintUtilities.printComponent(currentPanel,"CDMS Wizard Panel"); 
      }
      else if (e.getSource() == helpBtn)
      {
        DesktopFrame.makeWindow("Wizard Help", 
          new cdms.core.Value.Obj(new Desktop.ViewHTML.ScrollHTMLPane(currentPanel.getHelp())));
      }

    }
  }

}
