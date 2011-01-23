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

// File: ScriptWindow.java
// Authors: {leighf,joshc}@csse.monash.edu.au

package cdms.plugin.desktop;

import java.awt.*;
import javax.swing.*;
import java.io.*;
import cdms.core.*;
import cdms.plugin.dialog.GenericFileFilter;

/** A configurable panel with toolbar for writing, running and debugging scripts. */ 
public class ScriptWindow extends JPanel implements Menu.ValueProducer, Serializable
{
  /** Serial ID required to evolve class while maintaining serialisation compatibility. */
	private static final long serialVersionUID = -6388711402024305689L;
private Value thisValue;
  protected JToolBar toolbar;
  public JEditorPane editorPane;

  public static void main(String argv[])
  {
    JFrame f = new JFrame("Script Test");
    f.getContentPane().add(new ScriptWindow());
    f.pack();
    f.setVisible(true);
  }

  public ScriptWindow()
  {
    super(new BorderLayout());
    Font font = new Font("monospaced",Font.PLAIN,12); 
    editorPane = new JEditorPane();
    editorPane.setFont(font);
    add(new JScrollPane(editorPane),BorderLayout.CENTER);
    setPreferredSize(new Dimension(800,600));
    thisValue = new Value.Obj(this);
  }

  public void clear()
  {
    editorPane.setText("");
  }

  public Value setToolbar(Value.Vector v)
  {
    if (toolbar != null) remove(toolbar);
    toolbar = new Menu.ToolBar(this,v,null);
    add(toolbar,BorderLayout.NORTH);
    return Value.TRIV;
  }

  public Type getApplyType()
  {
    return thisValue.t;
  }

  public Value getApplyValue()
  {
    return thisValue;
  }

  public Value setEditorText(Value.Str v)
  {
    editorPane.setText(v.getString());
    return Value.TRIV;
  }

  public Value.Str getEditorText(Value.Triv v)
  {
    return new Value.Str(editorPane.getText());
  }

  static final JFileChooser chooser = new JFileChooser();

  public Value save(Value.Str v)
  {
    String fname = v.getString();

    if (fname.compareTo("") == 0)
    {
      GenericFileFilter filter = new GenericFileFilter(new String[] { "fp" }, "Lambda Script");
      chooser.setFileFilter(filter);

      int res = chooser.showSaveDialog(this);
      chooser.resetChoosableFileFilters();
      if (res == JFileChooser.APPROVE_OPTION)
      {
        fname = chooser.getSelectedFile().getPath();
      }
      else return Value.TRIV;
    }

    try
    {
      File f = new File(fname);
      chooser.setSelectedFile(f);

      if (f.exists()) {
          int res = JOptionPane.showConfirmDialog(this, "Overwite file '"+fname+"' ?", "Overwrite file?",
                                                  JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
          if (res != JOptionPane.YES_OPTION)
              return Value.TRIV;
      }

      FileOutputStream fos = new FileOutputStream(f);
      fos.write( editorPane.getText().getBytes() );
      fos.close();
    }
    catch (Exception ex)
    {
      JOptionPane.showMessageDialog(this, "Error saving file: " + ex.toString(),"Error", JOptionPane.ERROR_MESSAGE);
    }
    return Value.TRIV;
  }

  public Value load(Value.Str v)
  {
    String fname = v.getString();

    if (fname.compareTo("") == 0)
    {
      GenericFileFilter filter = new GenericFileFilter(new String[] { "fp" }, "Lambda Script");
      chooser.setFileFilter(filter);

      int res = chooser.showOpenDialog(this);
      chooser.resetChoosableFileFilters();
      if (res == JFileChooser.APPROVE_OPTION) 
      {
        fname = chooser.getSelectedFile().getPath();
      }
      else return Value.TRIV;
    }

    try
    {
      clear();
      File f = new File(fname);
      chooser.setSelectedFile(f);
      FileInputStream fis = new FileInputStream(f);
      byte b[] = new byte[ (int) f.length() ];
      editorPane.read(fis,null);
      fis.read(b);
      fis.close();
    }
    catch (Exception ex)
    {
      JOptionPane.showMessageDialog(this, "Error opening file: " + ex.toString(),"Error", JOptionPane.ERROR_MESSAGE);
    }
    return Value.TRIV;
  }

}
