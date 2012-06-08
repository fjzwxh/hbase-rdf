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

package com.talis.hbase.rdf.examples.benchmarks.lubm;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.mindswap.pellet.jena.PelletInfGraph;
import org.mindswap.pellet.jena.PelletReasonerFactory;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import com.talis.hbase.rdf.HBaseRdfFactory;
import com.talis.hbase.rdf.Store;

public class LubmAll
{
	public static void main(String[] args)
	{
		try
		{
			int numOfRuns = new Integer( args[0] ).intValue() ;
			Store store = HBaseRdfFactory.connectStore( args[1] ) ;
            if( args[2].equals( "--format=true" ) ) store.getTableFormatter().format() ;
            
            Model schema = HBaseRdfFactory.connectNamedModel( store, args[3] ) ;
            long startTime = System.nanoTime() ;
            schema.read( "http://utdallas.edu/~vvk072000/Research/Jena-HBase-Ext/univ-bench.owl" );

            OntModel m = ModelFactory.createOntologyModel( PelletReasonerFactory.THE_SPEC, schema ) ;
            String sInputDirectory = args[4] ;
            File inputDirectory = new File( sInputDirectory ) ;
            String[] sFilenames = inputDirectory.list( new OWLFilenameFilter() ) ;
            for( int i = 0 ; i < sFilenames.length ; i++ )
            {
                    InputStream in = FileManager.get().open( sInputDirectory + sFilenames[i] ) ;
                    if( in == null ) { throw new IllegalArgumentException( "File: " + sFilenames[i] + " not found" ) ; }
                    m.read( in, "http://utdallas.edu/~vvk072000/Research/Jena-HBase-Ext/univ-bench.owl", "RDF/XML-ABBREV" ) ;
                    in.close();
            }
            long endTime = System.nanoTime() ;
            System.out.println( "Time to load:: " + ( endTime - startTime ) * 1e-6 + " ms." ) ;
            System.out.println( "Total size:: " + store.getTotalSize() ) ;

            long count = 0L ;
            String queryString = "" ;
			Query query = null ;
			QueryExecution qexec = null ;
			ResultSet rs = null ;
			startTime = System.nanoTime() ;
			( ( PelletInfGraph ) m.getGraph() ).classify() ;
			endTime = System.nanoTime() ;
			System.out.println( "Time to run initial query, Q1:: " + ( endTime - startTime ) * 1e-6 + " ms.\n\n" ) ;
			
			//Queries not to be executed
			List<String> lstQueries = new ArrayList<String>() ;
			String[] arrQueries = args[5].split( "\\s" ) ;
			for( int i = 0 ; i < arrQueries.length ; i++ ) lstQueries.add( arrQueries[i] ) ;
			
			//Query 1
			if( !lstQueries.contains( "Q1" ) )
			{
				for( int x = 1 ; x <= numOfRuns ; x++ )
				{
					count = 0L;
					startTime = System.nanoTime() ;
					queryString = 
						" PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
						" PREFIX ub: <http://utdallas.edu/~vvk072000/Research/Jena-HBase-Ext/univ-bench.owl#> " + 
						" SELECT * WHERE " +
						" {	" +
						"		?x rdf:type ub:GraduateStudent . " +
						"		?x ub:takesCourse <http://www.Department0.University0.edu/GraduateCourse0> . " +
						" }";
		
					query = QueryFactory.create(queryString);
					qexec = QueryExecutionFactory.create(query, m);
					rs = qexec.execSelect();
					while( rs.hasNext() )
					{ count++; rs.nextSolution(); }
					qexec.close();
					System.out.println("count = " + count);
					endTime = System.nanoTime() ;
					System.out.println( "Time to run Q1:: " + ( endTime - startTime ) * 1e-6 + " ms." ) ;
				}
			}
			
			//Query 2
			if( !lstQueries.contains( "Q2" ) )
			{
				for( int x = 1 ; x <= numOfRuns ; x++ )
				{
					count = 0L;
					startTime = System.nanoTime() ;
					queryString = 
						" PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
						" PREFIX ub: <http://utdallas.edu/~vvk072000/Research/Jena-HBase-Ext/univ-bench.owl#> " +
						" SELECT ?X ?Y ?Z " +
						" WHERE " +
						" { " +
						"		?X rdf:type ub:GraduateStudent . " +
						"		?Y rdf:type ub:University . " +
						"		?Z rdf:type ub:Department . " +
						"		?X ub:memberOf ?Z . " +
						"		?Z ub:subOrganizationOf ?Y . " +
						"		?X ub:undergraduateDegreeFrom ?Y " +
						" }";
		
					query = QueryFactory.create(queryString);
					qexec = QueryExecutionFactory.create(query, m);
					rs = qexec.execSelect();
					while( rs.hasNext() )
					{ count++; rs.nextSolution(); }
					qexec.close();
					System.out.println("count = " + count);
					endTime = System.nanoTime() ;
					System.out.println( "Time to run Q2:: " + ( endTime - startTime ) * 1e-6 + " ms." ) ;
				}
			}
			
			//Query 3
			if( !lstQueries.contains( "Q3" ) )
			{		
				for( int x = 1 ; x <= numOfRuns ; x++ )
				{
					count = 0L;
					startTime = System.nanoTime() ;
					queryString = 
						" PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
						" PREFIX ub: <http://utdallas.edu/~vvk072000/Research/Jena-HBase-Ext/univ-bench.owl#> " +
						" SELECT ?X " +
						" WHERE " +
						" { " +
						" 		?X rdf:type ub:Publication . " +
						"		?X ub:publicationAuthor <http://www.Department0.University0.edu/AssistantProfessor0> " +
						" } ";
		
					query = QueryFactory.create(queryString);
					qexec = QueryExecutionFactory.create(query, m);
					rs = qexec.execSelect();
					while( rs.hasNext() )
					{ count++; rs.nextSolution(); }
					qexec.close();
					System.out.println("count = " + count);
					endTime = System.nanoTime() ;
					System.out.println( "Time to run Q3:: " + ( endTime - startTime ) * 1e-6 + " ms." ) ;
				}
			}
			
			//Query 4
			if( !lstQueries.contains( "Q4" ) )
			{
				for( int x = 1 ; x <= numOfRuns ; x++ )
				{
					count = 0L;
					startTime = System.nanoTime() ;
					queryString = 
						" PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
						" PREFIX ub: <http://utdallas.edu/~vvk072000/Research/Jena-HBase-Ext/univ-bench.owl#> " +
						" SELECT ?X ?Y1 ?Y2 ?Y3 " +
						" WHERE " +
						" { " +
						"		?X rdf:type ub:Professor . " +
						"		?X ub:worksFor <http://www.Department0.University0.edu> . " +
						"		?X ub:name ?Y1 . " +
						"		?X ub:emailAddress ?Y2 . " +
						"		?X ub:telephone ?Y3 " +
						" } ";
		
					query = QueryFactory.create(queryString);
					qexec = QueryExecutionFactory.create(query, m);
					rs = qexec.execSelect();
					while( rs.hasNext() )
					{ count++; rs.nextSolution(); }
					qexec.close();
					System.out.println("count = " + count);
					endTime = System.nanoTime() ;
					System.out.println( "Time to run Q4:: " + ( endTime - startTime ) * 1e-6 + " ms." ) ;
				}
			}
			
			//Query 5
			if( !lstQueries.contains( "Q5" ) )
			{
				for( int x = 1 ; x <= numOfRuns ; x++ )
				{
					count = 0L;
					startTime = System.nanoTime() ;
					queryString = 
						" PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
						" PREFIX ub: <http://utdallas.edu/~vvk072000/Research/Jena-HBase-Ext/univ-bench.owl#> " +
						" SELECT ?X " +
						" WHERE " +
						" { " +
						"		?X rdf:type ub:Person . " +
						"		?X ub:memberOf <http://www.Department0.University0.edu> " +
						" } ";
		
					query = QueryFactory.create(queryString);
					qexec = QueryExecutionFactory.create(query, m);
					rs = qexec.execSelect();
					while( rs.hasNext() )
					{ count++; rs.nextSolution(); }
					qexec.close();
					System.out.println("count = " + count);
					endTime = System.nanoTime() ;
					System.out.println( "Time to run Q5:: " + ( endTime - startTime ) * 1e-6 + " ms." ) ;
				}
			}
			
			//Query 6
			if( !lstQueries.contains( "Q6" ) )
			{
				for( int x = 1 ; x <= numOfRuns ; x++ )
				{
					count = 0L;
					startTime = System.nanoTime() ;
					queryString = 
						" PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
						" PREFIX ub: <http://utdallas.edu/~vvk072000/Research/Jena-HBase-Ext/univ-bench.owl#> " +
						" SELECT ?X WHERE { ?X rdf:type ub:Student } ";
		
					query = QueryFactory.create(queryString);
					qexec = QueryExecutionFactory.create(query, m);
					rs = qexec.execSelect();
					while( rs.hasNext() )
					{ count++; rs.nextSolution(); }
					qexec.close();
					System.out.println("count = " + count);
					endTime = System.nanoTime() ;
					System.out.println( "Time to run Q6:: " + ( endTime - startTime ) * 1e-6 + " ms." ) ;
				}
			}
			
			//Query 7
			if( !lstQueries.contains( "Q7" ) )
			{
				for( int x = 1 ; x <= numOfRuns ; x++ )
				{
					count = 0L;
					startTime = System.nanoTime() ;
					queryString = 
						" PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
						" PREFIX ub: <http://utdallas.edu/~vvk072000/Research/Jena-HBase-Ext/univ-bench.owl#> " +
						" SELECT ?X ?Y " +
						" WHERE " +
						" { " +
						"		?X rdf:type ub:Student . " +
						"		?Y rdf:type ub:Course . " +
						"		?X ub:takesCourse ?Y . " +
						"		<http://www.Department0.University0.edu/AssociateProfessor0> ub:teacherOf ?Y " +
						" } ";
		
					query = QueryFactory.create(queryString);
					qexec = QueryExecutionFactory.create(query, m);
					rs = qexec.execSelect();
					while( rs.hasNext() )
					{ count++; rs.nextSolution(); }
					qexec.close();
					System.out.println("count = " + count);
					endTime = System.nanoTime() ;
					System.out.println( "Time to run Q7:: " + ( endTime - startTime ) * 1e-6 + " ms." ) ;
				}
			}
			
			//Query 8
			if( !lstQueries.contains( "Q8" ) )
			{
				for( int x = 1 ; x <= numOfRuns ; x++ )
				{
					count = 0L;
					startTime = System.nanoTime() ;
					queryString = 
						" PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
						" PREFIX ub: <http://utdallas.edu/~vvk072000/Research/Jena-HBase-Ext/univ-bench.owl#> " +
						" SELECT ?X ?Y ?Z " +
						" WHERE " +
						" { " +
						" 		?X rdf:type ub:Student . " + 
						"		?Y rdf:type ub:Department . " +
						"		?X ub:memberOf ?Y . " +
						"		?Y ub:subOrganizationOf <http://www.University0.edu> . " +
						"		?X ub:emailAddress ?Z " +
						" } ";
		
					query = QueryFactory.create(queryString);
					qexec = QueryExecutionFactory.create(query, m);
					rs = qexec.execSelect();
					while( rs.hasNext() )
					{ count++; rs.nextSolution(); }
					qexec.close();
					System.out.println("count = " + count);
					endTime = System.nanoTime() ;
					System.out.println( "Time to run Q8:: " + ( endTime - startTime ) * 1e-6 + " ms." ) ;
				}
			}
			
			//Query 9
			if( !lstQueries.contains( "Q9" ) )
			{
				for( int x = 1 ; x <= numOfRuns ; x++ )
				{
					count = 0L;
					startTime = System.nanoTime() ;
					queryString = 
						" PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
						" PREFIX ub: <http://utdallas.edu/~vvk072000/Research/Jena-HBase-Ext/univ-bench.owl#> " +
						" SELECT ?X ?Y ?Z " +
						" WHERE " +
						" { " +
						" 		?X rdf:type ub:Student . " +
						"		?Y rdf:type ub:Faculty . " +
						"		?Z rdf:type ub:Course . " +
						"		?X ub:advisor ?Y . " +
						"		?Y ub:teacherOf ?Z . " +
						"		?X ub:takesCourse ?Z " +
						" } ";
		
					query = QueryFactory.create(queryString);
					qexec = QueryExecutionFactory.create(query, m);
					rs = qexec.execSelect();
					while( rs.hasNext() )
					{ count++; rs.nextSolution(); }
					qexec.close();
					System.out.println("count = " + count);
					endTime = System.nanoTime() ;
					System.out.println( "Time to run Q9:: " + ( endTime - startTime ) * 1e-6 + " ms." ) ;
				}
			}
			
			//Query 10
			if( !lstQueries.contains( "Q10" ) )
			{
				for( int x = 1 ; x <= numOfRuns ; x++ )
				{
					count = 0L;
					startTime = System.nanoTime() ;
					queryString = 
						" PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
						" PREFIX ub: <http://utdallas.edu/~vvk072000/Research/Jena-HBase-Ext/univ-bench.owl#> " +
						" SELECT ?X " +
						" WHERE " +
						" { " +
						" 		?X rdf:type ub:Student . " +
						"		?X ub:takesCourse <http://www.Department0.University0.edu/GraduateCourse0> " +
						" } ";
		
					query = QueryFactory.create(queryString);
					qexec = QueryExecutionFactory.create(query, m);
					rs = qexec.execSelect();
					while( rs.hasNext() )
					{ count++; rs.nextSolution(); }
					qexec.close();
					System.out.println("count = " + count);
					endTime = System.nanoTime() ;
					System.out.println( "Time to run Q10:: " + ( endTime - startTime ) * 1e-6 + " ms." ) ;
				}
			}
				
			//Query 11
			if( !lstQueries.contains( "Q11" ) )
			{
				for( int x = 1 ; x <= numOfRuns ; x++ )
				{
					count = 0L;
					startTime = System.nanoTime() ;
					queryString = 
						" PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
						" PREFIX ub: <http://utdallas.edu/~vvk072000/Research/Jena-HBase-Ext/univ-bench.owl#> " +
						" SELECT ?X " +
						" WHERE " +
						" { " +
						"		?X rdf:type ub:ResearchGroup . " +
						"		?X ub:subOrganizationOf <http://www.University0.edu> " +
						" } ";
		
					query = QueryFactory.create(queryString);
					qexec = QueryExecutionFactory.create(query, m);
					rs = qexec.execSelect();
					while( rs.hasNext() )
					{ count++; rs.nextSolution(); }
					qexec.close();
					System.out.println("count = " + count);
					endTime = System.nanoTime() ;
					System.out.println( "Time to run Q11:: " + ( endTime - startTime ) * 1e-6 + " ms." ) ;
				}
			}
			
			//Query 12
			if( !lstQueries.contains( "Q12" ) )
			{
				for( int x = 1 ; x <= numOfRuns ; x++ )
				{
					count = 0L;
					startTime = System.nanoTime() ;
					queryString = 
						" PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
						" PREFIX ub: <http://utdallas.edu/~vvk072000/Research/Jena-HBase-Ext/univ-bench.owl#> " +
						" SELECT ?X ?Y " +
						" WHERE " +
						" { " +
						" 		?X rdf:type ub:Chair . " +
						"		?Y rdf:type ub:Department . " +
						"		?X ub:worksFor ?Y . " +
						"		?Y ub:subOrganizationOf <http://www.University0.edu> " +
						" } ";
		
					query = QueryFactory.create(queryString);
					qexec = QueryExecutionFactory.create(query, m);
					rs = qexec.execSelect();
					while( rs.hasNext() )
					{ count++; rs.next(); }
					qexec.close();
					System.out.println("count = " + count);
					endTime = System.nanoTime() ;
					System.out.println( "Time to run Q12:: " + ( endTime - startTime ) * 1e-6 + " ms." ) ;
				}
			}
			
			//Query 13
			if( !lstQueries.contains( "Q13" ) )
			{
				for( int x = 1 ; x <= numOfRuns ; x++ )
				{
					count = 0L;
					startTime = System.nanoTime() ;
					queryString = 
						" PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
						" PREFIX ub: <http://utdallas.edu/~vvk072000/Research/Jena-HBase-Ext/univ-bench.owl#> " +
						" SELECT ?X " +
						" WHERE " +
						" { " +
						"		?X rdf:type ub:Person . " +
						"		<http://www.University0.edu> ub:hasAlumnus ?X " +
						" } ";
		
					query = QueryFactory.create(queryString);
					qexec = QueryExecutionFactory.create(query, m);
					rs = qexec.execSelect();
					while( rs.hasNext() )
					{ count++; rs.nextSolution(); }
					qexec.close();
					System.out.println("count = " + count);
					endTime = System.nanoTime() ;
					System.out.println( "Time to run Q13:: " + ( endTime - startTime ) * 1e-6 + " ms." ) ;
				}
			}
			
			//Query 14
			if( !lstQueries.contains( "Q14" ) )
			{
				for( int x = 1 ; x <= numOfRuns ; x++ )
				{
					count = 0L;
					startTime = System.nanoTime() ;
					queryString = 
						" PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
						" PREFIX ub: <http://utdallas.edu/~vvk072000/Research/Jena-HBase-Ext/univ-bench.owl#> " +
						" SELECT ?X " +
						" WHERE { ?X rdf:type ub:UndergraduateStudent } ";
		
					query = QueryFactory.create(queryString);
					qexec = QueryExecutionFactory.create(query, m);
					rs = qexec.execSelect();
					while( rs.hasNext() )
					{ count++; rs.nextSolution(); }
					qexec.close();
					System.out.println("count = " + count);
					endTime = System.nanoTime() ;
					System.out.println( "Time to run Q14:: " + ( endTime - startTime ) * 1e-6 + " ms." ) ;
				}
			}
		}
		catch(Exception e) { e.printStackTrace(); }
	}
}