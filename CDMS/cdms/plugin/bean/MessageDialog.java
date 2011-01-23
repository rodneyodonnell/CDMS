
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

package cdms.plugin.bean;

/**
 * Pop up a (modal) Message dialog and wait for a user to press "continue".
 */

import java.awt.*;
import java.awt.event.*;

public class MessageDialog extends Dialog implements ActionListener {

    /** Serial ID required to evolve class while maintaining serialisation compatibility. */
	private static final long serialVersionUID = 6835911679197425724L;

	public MessageDialog(Frame frame, String title, String message) {
	this(frame, title, message, false);
    }

    public MessageDialog(Frame frame, String title,
			 String message, boolean leftIndented) {
	super(frame, title, true);
    	new WindowCloser(this);
	
	GridBagLayout gridBag = new GridBagLayout();
	setLayout(gridBag);
	GridBagConstraints cons = new GridBagConstraints();
	cons.gridwidth = GridBagConstraints.REMAINDER;
	if (leftIndented) {
	    cons.anchor = GridBagConstraints.WEST;
	}

	// Add a "Label" for reach line of text.
	int width = 400;
	int height = 5;
	while (message.length() > 0) {
	    int ix = message.indexOf('\n');
	    String line;
	    if (ix >= 0) {
		line = message.substring(0, ix);
		message = message.substring(ix+1);
	    } else {
		line = message;
		message = "";
	    }
	    Label l = new Label(line);
	    gridBag.setConstraints(l, cons);
	    add(l);
	    height += 20;
	}

	cons.anchor = GridBagConstraints.CENTER;
	Button b = new Button("Continue");
	b.addActionListener(this);
	gridBag.setConstraints(b, cons);
	add(b);
	height += 25;
	height += 35;

        int x = frame.getLocation().x + 30;
        int y = frame.getLocation().y + 100;
        setBounds(x, y, width, height+5);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent evt) {
	// our button got pushed.
        dispose();
    }

}
