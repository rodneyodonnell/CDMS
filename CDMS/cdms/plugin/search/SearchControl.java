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

// File: SearchControl.java
// Author: {leighf,joshc}@csse.monash.edu.au

package cdms.plugin.search;

import java.awt.*;
import javax.swing.*;

/** A search control panel with an error plot, control buttons and summary view. */ 
public class SearchControl extends JPanel
{
  /** Serial ID required to evolve class while maintaining serialisation compatibility. */
	private static final long serialVersionUID = -7114367125527104636L;
protected Search search;
  protected JSplitPane centerPanel;
  protected Box southBox;

  public SearchControl(Search search, Component display)
  {
    super(new BorderLayout());
    this.search = search;

    centerPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    centerPanel.setDividerSize(10);
    centerPanel.setOneTouchExpandable(true);
    centerPanel.setLeftComponent(new SearchErrorPlot(search));
    if (display != null) centerPanel.setRightComponent(display);
    centerPanel.resetToPreferredSizes();
    add(centerPanel, BorderLayout.CENTER);

    southBox = new Box(BoxLayout.Y_AXIS);
    southBox.add(new SearchProgress(search,search.getSearchObject().getPercentage() >= 0)); 
    southBox.add(new SearchButtons(search)); 
    add(southBox, BorderLayout.SOUTH);

    setVisible(true);
  }


}

