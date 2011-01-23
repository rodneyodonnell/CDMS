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

import java.util.*;

public class Environment implements java.io.Serializable
{
  /** Serial ID required to evolve class while maintaining serialisation compatibility. */
	private static final long serialVersionUID = -5162597859621118231L;
protected Environment parent;
  protected Hashtable nameHash = new Hashtable();

  public Environment(Environment parent)
  {
    this.parent = parent;
  }

  public void add(String name, Object o)
  {
    if (parent == null) 
      cdms.core.Environment.env.add(name,"Boot",o,"");
    else nameHash.put(name,o);
  }

  public Object getObject(String name)
  {
    Object o = nameHash.get(name);
 
    if (o == null)
    {
      if (parent != null) 
      {
        o = parent.getObject(name); 
      }
      else 
      {
        int idx = name.indexOf(".");
        if (idx != -1)
        {
          String fst = name.substring(idx+1);
          String snd = name.substring(0,idx);
          o = cdms.core.Environment.env.getObject(fst,snd);
        }
        else 
        {
          o = cdms.core.Environment.env.getObject(name,"Boot");
          if (o == null) o = cdms.core.Environment.env.getObject(name,"Standard");
        }
      }
    }
    return o;
  }
}
