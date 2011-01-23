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

// File: SearchButtons.java
// Author: {leighf,joshc}@csse.monash.edu.au

package cdms.plugin.search;

import java.awt.*;
import javax.swing.*;

/** A panel containing search control buttons: start, stop, reset, step. */
public class SearchButtons extends JPanel implements Search.SearchListener
{
  /** Serial ID required to evolve class while maintaining serialisation compatibility. */
	private static final long serialVersionUID = 5490704765096228393L;
protected Search search;
  protected JButton startButton = new JButton("Start");
  protected JButton stopButton = new JButton("Stop");
  protected JButton resetButton = new JButton("Reset");
  protected JButton stepButton = new JButton("Step");

  public SearchButtons(Search search)
  {
    super(new FlowLayout(FlowLayout.CENTER));

    this.search = search;
    search.addSearchListener(this);

    startButton.setMnemonic('a');
    stopButton.setMnemonic('o');
    resetButton.setMnemonic('r');
    stepButton.setMnemonic('S');

    add(startButton);
    add(stopButton);
    add(resetButton);
    add(stepButton);

    startButton.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent e)
      {
        SearchButtons.this.search.start();
      }
    });

    stopButton.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent e)
      {
        SearchButtons.this.search.stop();
        stopButton.setEnabled(false);
      }
    });

    resetButton.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent e)
      {
        SearchButtons.this.search.reset();
      }
    });

    stepButton.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent e)
      {
        SearchButtons.this.search.step();
      }
    });

  }
 
  public void reset(final boolean fin)
  {
    Runnable doReset = new Runnable()
    { 
      public void run()
      {
        if (fin)
        {
          startButton.setEnabled(false);
          stopButton.setEnabled(false);
          stepButton.setEnabled(false);
          resetButton.setEnabled(true);
        }
        else
        {
          startButton.setEnabled(true);
          stepButton.setEnabled(true);
          stopButton.setEnabled(false);
          resetButton.setEnabled(true);
        }
      }
    };
    SwingUtilities.invokeLater(doReset);
  }

  public void reset(Search sender)
  {
    reset(search.getSearchObject().isFinished());
  }

  public void beforeEpoch(final boolean stepping)
  {
    Runnable doBefore = new Runnable()
    { 
      public void run()
      {
        startButton.setEnabled(false); // After an epoch has started, we only want the stop button to be available.
        stepButton.setEnabled(false);
        resetButton.setEnabled(false);
        if(stepping)        // ... unless the epoch is just a single step, after which the search will stop anyway.
        {                              //     in this case we want no buttons available.
          stopButton.setEnabled(false);
        }
        else
        {
          stopButton.setEnabled(true);
        }
      }
    };
    SwingUtilities.invokeLater(doBefore);
  }

  public void beforeEpoch(Search sender)
  {
    beforeEpoch(search.isStepping());
  }

  public void afterEpoch(Search sender)
  {
    if (search.getSearchObject().isFinished())   // search has finished...
    {
      Runnable doAfter = new Runnable()
      {  
        public void run()
        {
          startButton.setEnabled(false); // After an epoch has started, we only want the stop button to be available.
          startButton.setEnabled(false);
          stopButton.setEnabled(false);
          stepButton.setEnabled(false);
          resetButton.setEnabled(true);
        }
      };
      SwingUtilities.invokeLater(doAfter);
    }
    else
    {
      if (search.st.stop || search.isStepping()) // if search is about to stop...
      {
        Runnable doAfter = new Runnable()
        {  
          public void run()
          {
            startButton.setEnabled(true);
            stepButton.setEnabled(true);
            stopButton.setEnabled(false);
            resetButton.setEnabled(true);
          }
        };
        SwingUtilities.invokeLater(doAfter);
      }
      else                                       // search is just running...
      {
        Runnable doAfter = new Runnable()
        {  
          public void run()
          {
            startButton.setEnabled(false);
            stepButton.setEnabled(false);
            stopButton.setEnabled(true);
            resetButton.setEnabled(false);
          }
        };
        SwingUtilities.invokeLater(doAfter);
      }
    }
  }

  public void onCompletion(Search sender){}
}

