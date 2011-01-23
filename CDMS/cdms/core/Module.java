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

// File: Module.java
// Authors: {leighf,joshc}@csse.monash.edu.au

package cdms.core;

import java.lang.reflect.*;

/** Modules represent the packaging of type and values together.  We use Haskell style
    modules which provide a flat namespace.  Modules will typically be installed by the
    system at startup (in the bootstrap).  Those wishing to package together a set
    of types and values will either extend the Module class or extend the StaticFunctionModule
    class which is a non-abstract descendant of Module which adds to the environment
    all static Value.Function variables in a set of names classes.
*/
public abstract class Module
{
  private static java.util.Vector<Module> installed = new java.util.Vector<Module>();

  public static Module getModuleByName(String name)
  {
    for (int i = 0; i < installed.size(); i++)
      if (installed.elementAt(i).getModuleName().compareTo(name) == 0)
        return installed.elementAt(i);
    return null;
  }

  /** The name of this module. */
  public abstract String getModuleName();

  /** The HTML help for this module. */
  public abstract java.net.URL getHelp();

  /** Creates a URL for a standard module.  The help URL is created using the class 
      package name: file://doc/api-docs/packagename/package-frame.html 
  */ 
  public static java.net.URL createStandardURL(Class c) 
  {
    try
    {
      return new java.net.URL("file:doc/api-docs/" + 
                              c.getPackage().getName().replace('.','/') + 
                              "/package-frame.html");
    }
    catch (Exception e)
    {
      System.out.println("Bad help URL for Java package " + c.getPackage());
      return null;
    }
  }

  /** Installs the module into the environment. */
  public abstract void install(Value params) throws Exception;

  /** Convenience method for adding values and types to the environment using the module
      name. 
  */
  public void add(String name, Object o, String desc)
  {
    Environment.env.add(name,getModuleName(),o,desc);
  }

  /** A simple function which given a pair whose first entry is the full class name of the
      module and whose second entry is a value representing parameters for the install method,
      installs the module: <code>(String,t) -> ()</code>
      <p>
      Throws an exception if the module cannot be installed. 
      <p>
      Note: There is no need to create an instance of InstallModule since this will 
      normally be the first function to be created in the bootstrap using <code>cv</code>.
  */
  public static class InstallModule extends Value.Function
  {
    /** Serial ID required to evolve class while maintaining serialisation compatibility. */
	private static final long serialVersionUID = 8487047787911079399L;
	public static Type.Function thisType = 
      new Type.Function(new Type.Structured(new Type[] { Type.STRING, Type.TYPE },
                                            new boolean[] { false, false }),
                        Type.TRIV,false,false);

    public InstallModule()
    {
      super(thisType);
    }

    public Value apply(Value v)
    {
      String className = ((Value.Str) ((Value.Structured) v).cmpnt(0)).getString();
      Value param = ((Value.Structured) v).cmpnt(1);

      try
      {
        Module m = (Module) Class.forName(className).newInstance();
        System.out.println("Installing module " + m.getModuleName() + " from " + className);
        m.install(param);
        installed.add(m);
      }
      catch (Exception e)
      {
	  throw new RuntimeException(e);
	  // throw new RuntimeException("Unable to install module " + className + ".  " + e.toString());
      }

      return Value.TRIV;
    }

  }

  /** A concrete version of Module which installs all Value.Function members found
      in a set of named classes. 
  */
  public static class StaticFunctionModule extends Module
  {
    protected String moduleName;
    protected java.net.URL help;
    protected Class c[];

    public StaticFunctionModule(String moduleName, java.net.URL help, Class c)
    {
      this.moduleName = moduleName;
      this.help = help;
      this.c = new Class[] { c };
    }

    public StaticFunctionModule(String moduleName, java.net.URL help, Class c[])
    {
      this.moduleName = moduleName;
      this.help = help;
      this.c = c;
    }
   
    public String getModuleName() { return moduleName; }
    public java.net.URL getHelp() { return help; }

    public void install(Value params) throws IllegalAccessException
    {
      for (int j = 0; j < c.length; j++)
      {
        Field fields[] = c[j].getFields();

        for (int i = 0; i < fields.length; i++)
        {
          if (Value.Function.class.isAssignableFrom(fields[i].getType()) &&  // Only FN's.
              Modifier.isStatic(fields[i].getModifiers()))
          {
            Environment.env.add(fields[i].getName(),getModuleName(),(Value) fields[i].get(null),
                                fields[i].getName() + " function");
          }
        }
      }
    }

  }

}
