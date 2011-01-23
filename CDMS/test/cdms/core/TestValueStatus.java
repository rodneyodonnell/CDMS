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

//File: TestType.java
//Modified : rodo@dgs.monash.edu.au

package test.cdms.core;

import junit.framework.*;

import cdms.core.Value;

import java.io.*;

/**
* Test routine for ValueStatus.java
* Initially only tests serialization
*
* @author Rodney O'Donnell <rodo@dgs.monash.edu.au>
* @version $Revision: 1.4 $ $Date: 2006/08/22 22:40:05 $
* $Source: /u/csse/public/bai/bepi/cvs/CAMML/CDMS/test/cdms/core/TestValueStatus.java,v $
*/
public class TestValueStatus extends TestCase
{
	public TestValueStatus(String name) 
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
	
	/**
	 * Call rewrite on several status values. Standard status values types should "==" eachother,
	 */
	public void testSerialize()  throws IOException, ClassNotFoundException
	{
		assertEquals(Value.S_INTERVENTION, TestType.rewrite(Value.S_INTERVENTION));
		assertEquals(Value.S_INVALID, TestType.rewrite(Value.S_INVALID));
		assertEquals(Value.S_IRRELEVANT, TestType.rewrite(Value.S_IRRELEVANT));
		assertEquals(Value.S_NA, TestType.rewrite(Value.S_NA));
		assertEquals(Value.S_PROPER, TestType.rewrite(Value.S_PROPER));
		assertEquals(Value.S_UNOBSERVED, TestType.rewrite(Value.S_UNOBSERVED));		
	}
	
}
