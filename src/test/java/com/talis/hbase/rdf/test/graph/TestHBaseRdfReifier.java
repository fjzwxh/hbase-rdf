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

package com.talis.hbase.rdf.test.graph;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.shared.ReificationStyle;
import com.talis.hbase.rdf.HBaseRdfFactory;
import com.talis.hbase.rdf.Store;
import com.talis.hbase.rdf.test.StoreCreator;

public class TestHBaseRdfReifier 
{
	public static Test suite()
	{
		TestSuite ts = new TestSuite() ;
		
		ts.addTestSuite( TestHBaseRdfSimpleReifier.class ) ;
		ts.addTestSuite( TestHBaseRdfVertPartReifier.class ) ;
		ts.addTestSuite( TestHBaseRdfIndexedReifier.class ) ;
		ts.addTestSuite( TestHBaseRdfVPIndexedReifier.class ) ;
		ts.addTestSuite( TestHBaseRdfHybridReifier.class ) ;
		ts.addTestSuite( TestHBaseRdfHashReifier.class ) ;
		
		return ts ;
	}
	
	public static class TestHBaseRdfSimpleReifier extends AbstractTestReifierHBaseRdf
	{
		public TestHBaseRdfSimpleReifier( String name ) { super( name ) ; }
		
		@Override
		public Graph getGraph()
		{
			Store store = StoreCreator.getStoreSimple() ;
			return HBaseRdfFactory.connectDefaultGraph( store, ReificationStyle.Standard );
		}
		
		@Override 
		public Graph getGraph( ReificationStyle style )
		{
			Store store = StoreCreator.getStoreSimple() ;
			return HBaseRdfFactory.connectDefaultGraph( store, style );		
		}
	}
	
	public static class TestHBaseRdfVertPartReifier extends AbstractTestReifierHBaseRdf
	{
		public TestHBaseRdfVertPartReifier( String name ) { super( name ) ; }
		
		@Override
		public Graph getGraph()
		{
			Store store = StoreCreator.getStoreVerticallyPartitioned() ;
			return HBaseRdfFactory.connectDefaultGraph( store, ReificationStyle.Standard );
		}
		
		@Override 
		public Graph getGraph( ReificationStyle style )
		{
			Store store = StoreCreator.getStoreVerticallyPartitioned() ;
			return HBaseRdfFactory.connectDefaultGraph( store, style );		
		}
	}
	
	public static class TestHBaseRdfIndexedReifier extends AbstractTestReifierHBaseRdf
	{
		public TestHBaseRdfIndexedReifier( String name ) { super( name ) ; }
		
		@Override
		public Graph getGraph()
		{
			Store store = StoreCreator.getStoreIndexed() ;
			return HBaseRdfFactory.connectDefaultGraph( store, ReificationStyle.Standard );
		}
		
		@Override 
		public Graph getGraph( ReificationStyle style )
		{
			Store store = StoreCreator.getStoreIndexed() ;
			return HBaseRdfFactory.connectDefaultGraph( store, style );		
		}
	}

	public static class TestHBaseRdfVPIndexedReifier extends AbstractTestReifierHBaseRdf
	{
		public TestHBaseRdfVPIndexedReifier( String name ) { super( name ) ; }
		
		@Override
		public Graph getGraph()
		{
			Store store = StoreCreator.getStoreVPIndexed() ;
			return HBaseRdfFactory.connectDefaultGraph( store, ReificationStyle.Standard );
		}
		
		@Override 
		public Graph getGraph( ReificationStyle style )
		{
			Store store = StoreCreator.getStoreVPIndexed() ;
			return HBaseRdfFactory.connectDefaultGraph( store, style );		
		}
	}

	public static class TestHBaseRdfHybridReifier extends AbstractTestReifierHBaseRdf
	{
		public TestHBaseRdfHybridReifier( String name ) { super( name ) ; }
		
		@Override
		public Graph getGraph()
		{
			Store store = StoreCreator.getStoreHybrid() ;
			return HBaseRdfFactory.connectDefaultGraph( store, ReificationStyle.Standard );
		}
		
		@Override 
		public Graph getGraph( ReificationStyle style )
		{
			Store store = StoreCreator.getStoreHybrid() ;
			return HBaseRdfFactory.connectDefaultGraph( store, style );		
		}
	}

	public static class TestHBaseRdfHashReifier extends AbstractTestReifierHBaseRdf
	{
		public TestHBaseRdfHashReifier( String name ) { super( name ) ; }
		
		@Override
		public Graph getGraph()
		{
			Store store = StoreCreator.getStoreHash() ;
			return HBaseRdfFactory.connectDefaultGraph( store, ReificationStyle.Standard );
		}
		
		@Override 
		public Graph getGraph( ReificationStyle style )
		{
			Store store = StoreCreator.getStoreHash() ;
			return HBaseRdfFactory.connectDefaultGraph( store, style );		
		}
	}

}