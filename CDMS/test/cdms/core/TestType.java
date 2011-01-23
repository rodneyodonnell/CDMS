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

// File: TestType.java
// Modified : rodo@dgs.monash.edu.au

package test.cdms.core;

import junit.framework.*;

import cdms.core.Type;
import java.io.*;

/**
 * Test routine for Type.java
 * Initially only tests serialization
 *
 * @author Rodney O'Donnell <rodo@dgs.monash.edu.au>
 * @version $Revision: 1.3 $ $Date: 2006/08/22 22:40:05 $
 * $Source: /u/csse/public/bai/bepi/cvs/CAMML/CDMS/test/cdms/core/TestType.java,v $
 */
public class TestType extends TestCase
{
	public TestType(String name) 
	{
		super(name);
	}
	
	protected void setUp() 
	{
	}
	
	public static Test suite() 
	{
		return new TestSuite(TestType.class);
	}
	
	/** Serialise an object o then read it back in. Used for testing serialisation. */
	public static Object rewrite(Object o) throws IOException, ClassNotFoundException {
		// Open output stream
		ObjectOutputStream oStream = new ObjectOutputStream(new FileOutputStream("temp.out"));
		
		// Write object
		oStream.writeObject(o);
		oStream.close();

		// reread object
		ObjectInputStream iStream = new ObjectInputStream( new FileInputStream("temp.out"));
		return iStream.readObject();	
	}

	/**
	 * Call rewrite on several types. Standard sytem types should "==" eachother,
	 * non-standard types should not.
	 */
	public void testSerialize()  throws IOException, ClassNotFoundException
	{
		// Create array of "Standard" and "Non-Standard" types
		Type[] standard = new Type[] {Type.STRUCTURED, Type.VECTOR, Type.TRIV, Type.TYPE};

		// All Standard types should return true to "==" after serialization
		Type[] sCopy = new Type[standard.length];
		for ( int i = 0 ; i < standard.length; i++ ) {
			sCopy[i] = (Type)rewrite(standard[i]);
			assertEquals( standard[i], sCopy[i] );
		}

		// Non-Standard types may or may not return true to "==".
		// Subtypes (eg. vector elements) should return true to  "=="
		Type[] nonStandard = new Type[standard.length];
		Type[] nsCopy = new Type[nonStandard.length];
		for ( int i = 0 ; i < nonStandard.length; i++ ) {
			nonStandard[i] = new Type.Vector(standard[i]);
			nsCopy[i] = (Type)rewrite(nonStandard[i]);
			assertEquals( ((Type.Vector)nonStandard[i]).elt, ((Type.Vector)nsCopy[i]).elt );
		}
			
	}
	
}
