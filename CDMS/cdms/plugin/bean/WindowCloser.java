
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
 * Utility class to catch window close events on a target
 * window and actually dispose the window.
 */

import java.awt.*;
import java.awt.event.*;

public class WindowCloser implements WindowListener {

   /**
    * Create an adaptor to listen for window closing events
    * on the given window and actually perform the close.
    */

    public WindowCloser(Window w) {
	this(w, false);
    }

   /**
    * Create an adaptor to listen for window closing events
    * on the given window and actually perform the close.
    * If "exitOnClose" is true we do a System.exit on close.
    */

    public WindowCloser(Window w, boolean exitOnClose) {
	this.exitOnClose = exitOnClose;
	w.addWindowListener(this);
    }


    public void windowOpened(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
	if (exitOnClose) {
            System.exit(0);
	}
	e.getWindow().dispose();
    }

    public void windowClosed(WindowEvent e) {
	if (exitOnClose) {
            System.exit(0);
	}
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }

    private boolean exitOnClose;
}
