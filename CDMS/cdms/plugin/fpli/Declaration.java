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

// class Declaration the abstract syntax (parse tree) of [rec] x1=e1, x2=e2,...

package cdms.plugin.fpli;

public class Declaration implements java.io.Serializable
{ 
  /** Serial ID required to evolve class while maintaining serialisation compatibility. */
	private static final long serialVersionUID = 2327687139846635269L;
public final String id;        
  public final Expression e;
  public final Declaration next; 
  public final boolean isRec;

  public Declaration(boolean isRec, String id, Expression e, Declaration next)
  { 
    this.isRec = isRec; 
    this.id = id; 
    this.e = e; 
    this.next = next; 
  }

  public void appendSB(StringBuffer sb) // for printing - efficiency!
  { 
    Declaration d = this;
    sb.append( isRec ? Lexical.Symbol[Lexical.recSy]+" " : "" );
    while( true )
    { 
      sb.append( d.id + " = " );   d.e.appendSB(sb);
      d = d.next;  
      if( d == null ) break;
      sb.append(",\n");
    }  
  }
}//Declaration class


// L. Allison, November 2000,
// School of Computer Science and Software Engineering,
// Monash University, Australia 3168
// see http://www.csse.monash.edu.au/~lloyd/tildeFP/Lambda/
