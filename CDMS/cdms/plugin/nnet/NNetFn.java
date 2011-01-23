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
package cdms.plugin.nnet;

import cdms.core.*;
import cdms.plugin.desktop.*;
import cdms.plugin.search.*;
import java.awt.*;

/** Neural Network Functions. */
public class NNetFn
{

  public static Wizard wizard = new Wizard();
  public static class Wizard extends Value.Function
  {
    /** Serial ID required to evolve class while maintaining serialisation compatibility. */
	private static final long serialVersionUID = -1055897427061673410L;
	public static final Type.Function TT = new Type.Function(Type.VECTOR,Type.TRIV);

    public Wizard()
    {
      super(TT);
    }

    public Value apply(Value v)
    {
      cdms.plugin.desktop.Wizard w = new cdms.plugin.desktop.Wizard(new WizardPanel((Value.Vector) v));
      w.setPreferredSize(new Dimension(600,400));
      DesktopFrame.makeWindow("Neural Network Wizard",w);
      return Value.TRIV;
    }
  }

  /** The first panel for the NNet wizard.  Allows selection of input and output columns of data for training. */
  public static class WizardPanel extends cdms.plugin.desktop.Wizard.WizardPanel
  {
    /** Serial ID required to evolve class while maintaining serialisation compatibility. */
	private static final long serialVersionUID = -2341622273623487144L;
	cdms.plugin.desktop.Formatter formatter;

    public WizardPanel(Value.Vector data)
    {
      setLayout(new GridLayout(1,1));
      formatter = new cdms.plugin.desktop.Formatter(data);
      add(formatter);
      setBackEnabled(false);
    }

    public boolean wantScroller() { return false; }

    public cdms.plugin.desktop.Wizard.WizardPanel getNext()
    {
      return new WizardPanel2(formatter.getInputData(),formatter.getOutputData());
    }
  }
 
  /** The second panel for the NNet wizard.  Allows selection of network parameters. */ 
  public static class WizardPanel2 extends cdms.plugin.desktop.Wizard.WizardPanel
  {
    /** Serial ID required to evolve class while maintaining serialisation compatibility. */
	private static final long serialVersionUID = -8465077120162298775L;
	Value.Vector inputData, outputData;
    NNetControlPanel nnetControlPanel;

    public WizardPanel2(Value.Vector inputData, Value.Vector outputData)
    {
      setLayout(new GridLayout(1,1));
      this.inputData = inputData;
      this.outputData = outputData;
      nnetControlPanel = new NNetControlPanel();
      add(nnetControlPanel);
    }

    public boolean wantScroller() { return false; }

    public cdms.plugin.desktop.Wizard.WizardPanel getNext()
    {
      Type inputType = ((Type.Vector)(inputData.t)).elt;
      Type outputType = ((Type.Vector)(outputData.t)).elt;
      /* the next line is unnecessary if the appropriate check has already been made elsewhere... so either remove it, or this comment!*/
      if ((inputData.length()==0)||(outputData.length()==0)) 
        throw new IllegalArgumentException("Training data for neural net search must be non-empty");
      int inVectorLength = (inputType instanceof Type.Vector) ? ((Value.Vector)(inputData.elt(0))).length() : 0;
      int outVectorLength = (outputType instanceof Type.Vector) ? ((Value.Vector)(outputData.elt(0))).length() : 0;

      // Hidden layer sizes should be set here...temporarily set to 1 hidden layer with 10 units.
      int[] hiddenLayerSizes = new int[1];
      hiddenLayerSizes[0] = 10;

      NNetInt nnetInt = new NNetInt(inputType, inVectorLength, outputType, outVectorLength, hiddenLayerSizes);

      // Initialize nnetInt according to nnetControlPanel settings.
      nnetInt.nguyenWidrowInitWeights();

      NNetSearchObject nnetso = new NNetSearchObject(inputData,outputData,nnetInt);
      SearchControl sc = new SearchControl(new Search(nnetso),null);
      sc.setPreferredSize(new Dimension(600,400));
      DesktopFrame.makeWindow("NNet Training",sc);
      return null;
    }
  }

}
