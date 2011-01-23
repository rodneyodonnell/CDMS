
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

// Support for a PropertyEditor that uses text.

import java.awt.*;
import java.awt.event.*;
import java.beans.*;

class PropertyText extends TextField implements KeyListener, FocusListener {

    /** Serial ID required to evolve class while maintaining serialisation compatibility. */
	private static final long serialVersionUID = -8432117484999110736L;

	PropertyText(PropertyEditor pe) {
	super(pe.getAsText());
	editor = pe;
	addKeyListener(this);
	addFocusListener(this);
    }

    public void repaint() {
	setText(editor.getAsText());
    }

    protected void updateEditor() {
	try {
	    editor.setAsText(getText());
	} catch (IllegalArgumentException ex) {
	    // Quietly ignore.
	}
    }
    
    //----------------------------------------------------------------------
    // Focus listener methods.

    public void focusGained(FocusEvent e) {
    }

    public void focusLost(FocusEvent e) {
    	updateEditor();
    }
    
    //----------------------------------------------------------------------
    // Keyboard listener methods.

    public void keyReleased(KeyEvent e) {
 	if (e.getKeyCode() == KeyEvent.VK_ENTER) {
	    updateEditor();
	}
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    //----------------------------------------------------------------------
    private PropertyEditor editor;
}
