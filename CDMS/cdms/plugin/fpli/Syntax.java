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

package cdms.plugin.fpli;

// Parser, also see Lexical.java


public class Syntax
{ 
  private final Lexical lex;

  private final int applicPriority = 7;  // i.e. of   f x

  // L is 64 bit.
  private final long                                     // useful Symbol Sets
    unOprs = (1L << Lexical.minus)
              | (1L << Lexical.hdSy)   | (1L << Lexical.tlSy)
              | (1L << Lexical.nullSy) | (1L << Lexical.notSy),

    binOprs= (1L << Lexical.consSy)
              | (1L << Lexical.plus) | (1L << Lexical.minus)
              | (1L << Lexical.times)| (1L << Lexical.over)
              | (1L << Lexical.eq) | (1L << Lexical.ne) | (1L << Lexical.le)
              | (1L << Lexical.lt) | (1L << Lexical.ge) | (1L << Lexical.gt)
              | (1L << Lexical.andSy) | (1L << Lexical.orSy),

    rightAssoc = (1L << Lexical.consSy);

  private final long
    startsExp = unOprs | (1L << Lexical.word) | (1L << Lexical.numeral_integer)
                | (1L << Lexical.numeral_double)
                | (1L << Lexical.string) | (1L << Lexical.triv)
                | (1L << Lexical.nilSy)
                | (1L << Lexical.trueSy) | (1L << Lexical.falseSy)
                | (1L << Lexical.open) | (1L << Lexical.letSy)
                | (1L << Lexical.ifSy) | (1L << Lexical.lambdaSy)
                | (1L << Lexical.sqopen);

  int[] oprPriority = new int[Lexical.eofSy];

  void init()
  {  
    for (int i = 0; i < oprPriority.length; i++) 
      oprPriority[i] = 0;

    oprPriority[Lexical.consSy] = 1;
    oprPriority[Lexical.orSy]   = 2;
    oprPriority[Lexical.andSy]  = 3;

    for(int i = Lexical.eq; i <= Lexical.ge; i++) 
      oprPriority[i] = 4;

    oprPriority[Lexical.plus]  = 5;  
    oprPriority[Lexical.minus] = 5;
    oprPriority[Lexical.times] = 6;  
    oprPriority[Lexical.over]  = 6;
  }


  public Syntax(Lexical lex)                                     //constructor
  { 
    this.lex = lex; 
    init(); 
  }

  private void check(int sym) // check and skip a particular symbol
  { 
    if( lex.sy() == sym ) lex.insymbol();
      else error( Lexical.Symbol[sym] + " Expected" );
  }

  public Expression exp()                                             // exp()
  { 
    Expression e = exp(1); 
    check(Lexical.eofSy); 
    return e; 
  }

  private Expression exp(int priority)                             // exp(...)
  { 
    if( priority < applicPriority )
    { 
      Expression e = exp(priority+1);
      int sym = lex.sy();

      if( member(sym, binOprs) && member(sym, rightAssoc) // eg 1:2:3:nil
          && oprPriority[sym] == priority )
      { 
        lex.insymbol();
        return new Expression.Binary(sym, e, exp(priority));
      }
      else
      while( member(sym, binOprs) && !member(sym, rightAssoc)
             && oprPriority[sym] == priority ) // e.g. 1+2+3
      { 
        lex.insymbol();
        e = new Expression.Binary(sym, e, exp(priority+1));
        sym = lex.sy();
      }

      return e;
    }// < applicPriority

    else if( priority == applicPriority )                     // e.g. f g h x
    { 
      Expression e = exp(priority+1);
      int sym = lex.sy();
      while( member(sym, startsExp) && ! member(sym, binOprs) )
      { 
        e = new Expression.Application(e, exp(priority+1));
        sym = lex.sy();
      }
      return e;
    }

    else if( member(lex.sy(), unOprs) )               // e.g. not p,  e.g. -3
    { 
      int sym = lex.sy(); lex.insymbol();
      return new Expression.Unary(sym, exp(priority));
    }

    else                                                           // operand
    { 
      Expression e = null;
      switch( lex.sy() )
      { 
        case Lexical.word:
          e = new Expression.Ident(lex.theWord); 
          lex.insymbol(); 
          break;
        case Lexical.numeral_integer:
          e = new Expression.IntCon(lex.theInt); 
          lex.insymbol(); 
          break;
        case Lexical.numeral_double:
          e = new Expression.DoubleCon(lex.theDouble); 
          lex.insymbol(); 
          break;
        case Lexical.string:
          e = new Expression.Str(lex.theWord);
          lex.insymbol(); 
          break;
        case Lexical.triv:
          e = Expression.triv; 
          lex.insymbol(); 
          break;
        case Lexical.nilSy:
          e = Expression.nilCon;   
          lex.insymbol(); 
          break;
        case Lexical.trueSy:
          e = Expression.ttrue;    
          lex.insymbol(); 
          break;
        case Lexical.falseSy:
          e = Expression.ffalse;   
          lex.insymbol(); 
          break;
        case Lexical.open:                                           // (e)
          lex.insymbol();
          e = exp(1); 

          if (lex.sy() == Lexical.comma)                            // n-tuple.
          {
            java.util.Vector es = new java.util.Vector();
            es.add(e);
            while (lex.sy() == Lexical.comma)
            {
              lex.insymbol();
              es.add(exp(1));
            }
            e = new Expression.Tuple(es);
          }

          check(Lexical.close);
	  break;
        case Lexical.sqopen:                                         // [
          lex.insymbol();
          java.util.Vector v = new java.util.Vector();
          while (lex.sy() != Lexical.sqclose)
          {
            v.add(exp(1));
            if (lex.sy() == Lexical.comma) lex.insymbol();
              else break;
          }
          e = new Expression.Vector(v);
          check(Lexical.sqclose);
          break;
        case Lexical.letSy:                             // let [rec] d in e
          lex.insymbol();
          Declaration d = dec();
          check(Lexical.inSy);
          e = new Expression.Block(d, exp(1));
          break;
        case Lexical.ifSy:                          // if e then eT else eF
          lex.insymbol();
          e = exp(1);
          check(Lexical.thenSy); 
          Expression eT = exp(1);
          check(Lexical.elseSy); 
          Expression eF = exp(1);
          e = new Expression.IfExp(e, eT, eF);
          break;
        case Lexical.lambdaSy:                           // e.g. lambda x.e
          lex.insymbol();
          Expression fp = param(); 
          check(Lexical.dot);
	  e = new Expression.LambdaExp(fp, exp(1));
          break;
          default:  error("bad operand");
      }//switch

      return e;
    }//if

  }//exp(...)


  private Expression param()
  { 
    Expression e = null;

    if( lex.sy() == Lexical.word )            // lambda x.e
      e = new Expression.Ident(lex.theWord);
    else if( lex.sy() == Lexical.triv )       // lambda ().e
      e = Expression.triv;
    else error("Lambda expression: bad formal parameter");

    lex.insymbol();
    return e;
  }

  private Declaration dec()
  { 
    boolean isRec = false;
    if( lex.sy() == Lexical.recSy ) 
    { 
      isRec = true; 
      lex.insymbol(); 
    }
    return dec1(isRec);
  }

  private Declaration dec1(boolean isRec)
  { 
    if( lex.sy() == Lexical.word )
    { 
      String id = lex.theWord(); 
      lex.insymbol();  // x
      check( Lexical.eq );                        // =
      Expression e = exp(1);                      // e
      Declaration d = null;
      if( lex.sy() == Lexical.comma )             // ,
      { 
        lex.insymbol(); 
        d = dec1(isRec); 
      }       // ...
      return new Declaration(isRec, id, e, d);
    }
   /* else */ error("no identifier in declaration"); return null;
  }

  boolean member(int n, long s)            // ? is n a member of the "set" s ?
  { 
    return ((1L << n) & s) != 0; 
  }

  void error(String msg) 
  { 
    lex.error("Syntax: " + msg); 
  } 


  // the following allows Syntx to be tested on its own
  public static void main(String[] argv)
  { 
    System.out.println(" --- Testing Syntax, L.Allison, CSSE, "
                         + "Monash Uni, .au ---");

    Syntax syn = new Syntax( new Lexical(System.in) );
    Expression e = syn.exp();
    System.out.println( e.toString() );

    System.out.println("\n --- done ---");
  }//main


}

