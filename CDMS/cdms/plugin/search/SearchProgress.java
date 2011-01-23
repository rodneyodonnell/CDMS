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

// File: SearchProgress.java
// Author: leighf@csse.monash.edu.au

package cdms.plugin.search;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Calendar;

/** A progress bar and timer for search. */
public class SearchProgress extends JPanel implements Search.SearchListener
{
  /** Serial ID required to evolve class while maintaining serialisation compatibility. */
	private static final long serialVersionUID = -4840179697375150659L;
protected Search search;
  protected Calendar cal = Calendar.getInstance();
  protected long timeOffset = cal.get(Calendar.ZONE_OFFSET) + 
                              cal.get(Calendar.DST_OFFSET);
  protected Timer timer = new Timer(1000,new ActionListener() {
   public void actionPerformed(ActionEvent e) { updateTimes(); } });
  protected long startTime = 0;
  protected long cumTime = 0;
  protected JProgressBar progressBar = new JProgressBar(JProgressBar.HORIZONTAL,0,100);
  protected JLabel elapsedTime = new JLabel("Elapsed Time: 0");
  protected JLabel eta = new JLabel("Est. Time Rem.: 0");
  protected JLabel epoch = new JLabel("Epoch: 0");

  public static void main(String args[])
  {
    JFrame f = new JFrame("Progress Test");
    final SearchProgress sp = new SearchProgress(null,true);
    f.getContentPane().setLayout(new GridLayout(3,1));
    f.getContentPane().add(new JLabel("GO's"));
    f.getContentPane().add(sp);
    f.getContentPane().add(new JLabel("Buttons"));
    f.pack();
    f.setVisible(true);

    sp.on();
    Timer t = new Timer(250,new ActionListener() {
      int i = 0;
      public void actionPerformed(ActionEvent e) 
      { 
        if (i <= 1000) 
        { 
          sp.setValues(i,i / 10); 
        } 
        else if (i == 1001)
        {
          sp.off();
        }
        i++;
      }
    });
    t.start();
  }

  public SearchProgress(Search search, boolean showProgress)
  {
    super();

    this.search = search;  
    search.addSearchListener(this);

    setBorder(BorderFactory.createEtchedBorder());
    setLayout(new BorderLayout());

    progressBar.setBorderPainted(true);
    progressBar.setStringPainted(true);

    JPanel labelPanel = new JPanel();
    labelPanel.setLayout(new FlowLayout());
    labelPanel.add(epoch);
    labelPanel.add(elapsedTime);
    labelPanel.add(eta);
    add(labelPanel,"Center");
    add(progressBar,"South");

    eta.setVisible(showProgress);
    progressBar.setVisible(showProgress);

    reset();
  }

  protected void updateTimes()
  {
    Runnable doTimes = new Runnable()   
    { 
      public void run()
      {
        long totalTime = cumTime + System.currentTimeMillis() - startTime;
        long remTime = (long) ((100.0 / progressBar.getValue()) * totalTime - totalTime); 
        if (remTime < 0 || progressBar.getValue() == 100) remTime = 0;
        elapsedTime.setText("Elapsed Time: " + new java.sql.Time(totalTime - timeOffset)); 
        eta.setText("Est. Time Rem.: " + new java.sql.Time(remTime - timeOffset)); 
      }
    };
    SwingUtilities.invokeLater(doTimes);
  }

  public void on()
  {
    startTime = System.currentTimeMillis();
    timer.start();
  }

  public void off()
  {
    updateTimes();
    timer.stop();
    cumTime += System.currentTimeMillis() - startTime;
  }

  public void reset()
  {
    cumTime = 0;
    startTime = System.currentTimeMillis();
    setValues(0,0);
  }

  public void setValues(final int epoch, final int n)
  {
    Runnable doProgress = new Runnable()   // Runnable code not strictly necessary here, but serves as an example.
    { 
      public void run()
      {
        if (n >= 0) 
        {
          int perc = n;
          if (perc > 100) perc = 100;
          SearchProgress.this.epoch.setText("Epoch: " + epoch);
          progressBar.setValue(perc);
          progressBar.setString(perc + "%");
          updateTimes();
        }
      }
    };
    SwingUtilities.invokeLater(doProgress);
  }

  public void setFinished()
  {
    Runnable doProgress = new Runnable() 
    { 
      public void run()
      {
        progressBar.setValue(100);
        progressBar.setString("Finished");
      }
    };
    SwingUtilities.invokeLater(doProgress);
  }


  /* Listener Interface methods. */
  public void reset(Search sender)
  {
    reset();
  }

  public void beforeEpoch(Search sender)
  {
    on(); 
  }

  public void afterEpoch(Search sender)
  {
    off();
    setValues(sender.epoch(),(int) (sender.getSearchObject().getPercentage()*100));
  }

  public void onCompletion(Search sender){}
}
