
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

// Support for drawing a property value in a Canvas.

import java.awt.*;
import java.awt.event.*;
import java.beans.*;

class PropertyCanvas extends Canvas implements MouseListener {

    /** Serial ID required to evolve class while maintaining serialisation compatibility. */
	private static final long serialVersionUID = -8425896542948457736L;

	PropertyCanvas(Frame frame, PropertyEditor pe) {
	this.frame = frame;
	editor = pe;
	addMouseListener(this);
    }

    public void paint(Graphics g) {
	Rectangle box = new Rectangle(2, 2, getSize().width - 4, getSize().height - 4);
	editor.paintValue(g, box);
    }

    private static boolean ignoreClick = false;

    public void mouseClicked(MouseEvent evt) {
	if (! ignoreClick) {
	    try {
		ignoreClick = true;
		int x = frame.getLocation().x - 30;
		int y = frame.getLocation().y + 50;
		new PropertyDialog(frame, editor, x, y);
	    } finally {
		ignoreClick = false;
	    }
	}
    }

    public void mousePressed(MouseEvent evt) {
    }

    public void mouseReleased(MouseEvent evt) {
    }

    public void mouseEntered(MouseEvent evt) {
    }

    public void mouseExited(MouseEvent evt) {
    }

    private Frame frame;
    private PropertyEditor editor;
}
