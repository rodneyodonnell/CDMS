
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

// Support for PropertyEditor with custom editors.

import java.awt.*;
import java.awt.event.*;
import java.beans.*;

class PropertyDialog extends Dialog implements ActionListener {

    /** Serial ID required to evolve class while maintaining serialisation compatibility. */
	private static final long serialVersionUID = -1723315683063479239L;
	private Button doneButton;
    private Component body;
    private final static int vPad = 5;
    private final static int hPad = 4;

    PropertyDialog(Frame frame, PropertyEditor pe, int x, int y) {
	super(frame, pe.getClass().getName(), true);
	new WindowCloser(this);
	setLayout(null);

	body = pe.getCustomEditor();
	add(body);

	doneButton = new Button("Done");
	doneButton.addActionListener(this);
	add(doneButton);

	setLocation(x, y);
	setVisible(true);
    }

    public void actionPerformed(ActionEvent evt) {
        // Button down.
	dispose();
    }

    public void doLayout() {
        Insets ins = getInsets();
	Dimension bodySize = body.getPreferredSize();
	Dimension buttonSize = doneButton.getPreferredSize();

	int width = ins.left + 2*hPad + ins.right + bodySize.width;
	int height = ins.top + 3*vPad + ins.bottom + bodySize.height +
							buttonSize.height;

        body.setBounds(ins.left+hPad, ins.top+vPad,
				bodySize.width, bodySize.height);

	doneButton.setBounds((width-buttonSize.width)/2,
				ins.top+(2*hPad) + bodySize.height,
				buttonSize.width, buttonSize.height);

	setSize(width, height);

    }

}

