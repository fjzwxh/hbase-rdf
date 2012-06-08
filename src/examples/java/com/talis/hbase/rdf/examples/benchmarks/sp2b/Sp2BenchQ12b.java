/*
 * Copyright © 2010, 2011, 2012 Talis Systems Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.talis.hbase.rdf.examples.benchmarks.sp2b;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.talis.hbase.rdf.HBaseRdfFactory;
import com.talis.hbase.rdf.Store;

public class Sp2BenchQ12b
{
	public static void main(String[] args)
	{
		Store store = HBaseRdfFactory.connectStore( args[0] ) ;
		Model model = HBaseRdfFactory.connectNamedModel( store, args[1] ) ;
		int numOfRuns = new Integer( args[2] ).intValue() ;
		
		for( int i = 1 ; i <= numOfRuns ; i++ )
		{
			long startTime = System.nanoTime() ;
			try
			{
				String queryString = 
				" PREFIX xsd:  <http://www.w3.org/2001/XMLSchema#> " +
				" PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
				" PREFIX foaf: <http://xmlns.com/foaf/0.1/> " +
				" PREFIX dc:   <http://purl.org/dc/elements/1.1/> " +
				" ASK { " +
				" 		?erdoes rdf:type foaf:Person . " +
				"		?erdoes foaf:name \"Paul Erdoes\"^^xsd:string . " +
				"		{ " +
				"			?document dc:creator ?erdoes . " +
				"			?document dc:creator ?author . " +
				"			?document2 dc:creator ?author . " +
			    "			?document2 dc:creator ?author2 . " +
				"			?author2 foaf:name ?name " +
				"			FILTER (?author!=?erdoes && ?document2!=?document && ?author2!=?erdoes && ?author2!=?author) " +
				"		} UNION { " +
				"					?document dc:creator ?erdoes . " +
				"					?document dc:creator ?author . " +
				"					?author foaf:name ?name " +
				"					FILTER (?author!=?erdoes) " +
				"				} " +
				"	  } ";
				QueryExecution qexec = QueryExecutionFactory.create(queryString, model); 
				if ( qexec.execAsk() )
					System.out.println("executed ask query correctly");
				else
					System.out.println("did not execute ask query");
				qexec.close();
			}
			catch(Exception e) { e.printStackTrace(); }
			long endTime = System.nanoTime() ;
			System.out.println( "Time to run Q12b:: " + ( endTime - startTime ) * 1e-6 + " ms." ) ;
		}
	}
}
/** Copyright (c) 2008, 2009, The University of Texas at Dallas
* All rights reserved.
*
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are met:
*     * Redistributions of source code must retain the above copyright
*       notice, this list of conditions and the following disclaimer.
*     * Redistributions in binary form must reproduce the above copyright
*       notice, this list of conditions and the following disclaimer in the
*       documentation and/or other materials provided with the distribution.
*     * Neither the name of the The University of Texas at Dallas nor the
*       names of its contributors may be used to endorse or promote products
*       derived from this software without specific prior written permission.
*
* THIS SOFTWARE IS PROVIDED BY The University of Texas at Dallas ''AS IS'' AND ANY
* EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
* WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
* DISCLAIMED. IN NO EVENT SHALL The University of Texas at Dallas BE LIABLE FOR ANY
* DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
* (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
* LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
* ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
* (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
* SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
