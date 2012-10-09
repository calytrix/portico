/*
 *   Copyright 2012 The Portico Project
 *
 *   This file is part of portico.
 *
 *   portico is free software; you can redistribute it and/or modify
 *   it under the terms of the Common Developer and Distribution License (CDDL) 
 *   as published by Sun Microsystems. For more information see the LICENSE file.
 *   
 *   Use of this software is strictly AT YOUR OWN RISK!!!
 *   If something bad happens you do not have permission to come crying to me.
 *   (that goes for your lawyer as well)
 *
 */
package hlaunit.hla1516e.federation;

import java.net.URL;

import hla.rti1516e.exceptions.CouldNotOpenFDD;
import hla.rti1516e.exceptions.ErrorReadingFDD;
import hla.rti1516e.exceptions.FederationExecutionAlreadyExists;
import hla.rti1516e.exceptions.InconsistentFDD;
import hla.rti1516e.exceptions.RTIinternalError;
import hlaunit.hla1516e.common.Abstract1516eTest;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Test(sequential=true, groups={"CreateFederationTest", "basic", "create", "federationManagement"})
public class CreateFederationTest extends Abstract1516eTest
{
	//----------------------------------------------------------
	//                    STATIC VARIABLES
	//----------------------------------------------------------

	//----------------------------------------------------------
	//                   INSTANCE VARIABLES
	//----------------------------------------------------------

	//----------------------------------------------------------
	//                      CONSTRUCTORS
	//----------------------------------------------------------

	//----------------------------------------------------------
	//                    INSTANCE METHODS
	//----------------------------------------------------------
	
	@BeforeClass(alwaysRun=true)
	public void beforeClass()
	{
		super.beforeClass();
	}
	
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod()
	{
		
	}
	
	@AfterMethod(alwaysRun=true)
	public void afterMethod()
	{
		defaultFederate.quickResignTolerant();
		defaultFederate.quickDestroyTolerant( defaultFederate.simpleName );
	}
	
	@AfterClass(alwaysRun=true)
	public void afterClass()
	{
		super.afterClass();
	}

	//////////////////////////////////////////////////////////////////////////////
	// IEEE-1516e specifies a number of createFederationOverloads. The tests    //
	// are split up based on each overload in order to keep them in some sense  //
	// and order. Some overloads just call into others, so some of the testing  //
	// is redundant, but still important in detecting regressions incase we     //
	// have to split some of those implementations out on their own.            //
	//                                                                          //
	// The list below contains all the 1516e createFederation overloads         //
	//    * createFederation( String, URL )                                     //
	//    * createFederation( String, URL[] )                                   //
	//    * createFederation( String, URL[], URL mim )                          //
	//    * createFederation( String, URL[], String time )                      //
	//    * createFederation( String, URL[], URL mim, String time )             //
	//                                                                          //
	//////////////////////////////////////////////////////////////////////////////

	// TODO: Add tests for NotConnected
	
	//////////////////////////////////////////////////////////////////////////////
	// createFederationExecution( String, URL ) //////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////
	
	///////////////////////////////////////////////////////
	// TEST: (valid) testCreateFederationWithSingleFom() //
	///////////////////////////////////////////////////////
	@Test
	public void testCreateFederationWithSingleFom()
	{
		// create a link to the FOM //
		URL fom = ClassLoader.getSystemResource( "fom/ieee1516e/HLAstandardMIM.xml" );
		
		// try and create a valid federation //
		try
		{
			defaultFederate.rtiamb.createFederationExecution( defaultFederate.simpleName, fom );
		}
		catch( Exception e )
		{
			Assert.fail( "Could not create valid federation", e );
		}
		
		// ensure that federation can't be created again (now that it has already been created //
		try
		{
			defaultFederate.rtiamb.createFederationExecution( defaultFederate.simpleName, fom );
			Assert.fail( "Could not ensure that valid federation was created" );
		}
		catch( FederationExecutionAlreadyExists ae )
		{
			// SUCCESS!
		}
		catch( Exception e )
		{
			Assert.fail( "Wrong exception while testing creation of existing federation", e );
		}
	}

	////////////////////////////////////////////////////////
	// TEST: testCreateFederationWithInvalidFomLocation() //
	////////////////////////////////////////////////////////
	@Test
	public void testCreateFederationWithInvalidFomLocation()
	{
		// attempt to create with invalid fom URL //
		try
		{
			URL url = new URL( "http://localhost/dummyURL" );
			defaultFederate.rtiamb.createFederationExecution( defaultFederate.simpleName, url );
			Assert.fail( "No exception while creating federation with invalid FOM (invalid URL)" );
		}
		catch( CouldNotOpenFDD cnof )
		{
			// SUCCESS!
		}
		catch( Exception e )
		{
			Assert.fail( "Wrong exception while testing create with null FOM", e );
		}
	}
	
	////////////////////////////////////////////////
	// TEST: testCreateFederationWithInvalidFom() //
	////////////////////////////////////////////////
	@Test
	public void testCreateFederationWithInvalidFom()
	{
		// attempt to create with invalid fom //
		try
		{
			URL invalid = ClassLoader.getSystemResource( "fom/testfom-invalid.xml" );
			defaultFederate.rtiamb.createFederationExecution( defaultFederate.simpleName, invalid );
			Assert.fail( "No exception while creating federation with invalid FOM" );
		}
		catch( ErrorReadingFDD erf )
		{
			// SUCCESS!
		}
		catch( Exception e )
		{
			Assert.fail( "Wrong exception while testing create with invalid FOM", e );
		}
	}
	
	/////////////////////////////////////////////
	// TEST: testCreateFederationWithNullFom() //
	/////////////////////////////////////////////
	@Test
	public void testCreateFederationWithNullFom()
	{
		// attempt to create with null fom //
		try
		{
			URL[] foms = new URL[]{ null };
			defaultFederate.rtiamb.createFederationExecution( defaultFederate.simpleName, foms );
			Assert.fail( "No exception while creating federation with null FOM" );
		}
		catch( CouldNotOpenFDD cnof )
		{
			// SUCCESS!
		}
		catch( Exception e )
		{
			Assert.fail( "Wrong exception while testing create with null FOM", e );
		}
	}
	
	//////////////////////////////////////////////
	// TEST: testCreateFederationWithNullName() //
	//////////////////////////////////////////////
	@Test
	public void testCreateFederationWithNullName()
	{
		// attempt create with dodgy name //
		try
		{
			URL validFom = ClassLoader.getSystemResource( "fom/testfom.xml" );
			defaultFederate.rtiamb.createFederationExecution( null, validFom );
			Assert.fail( "No exception while creating federation with null name" );
		}
		catch( RTIinternalError rtie )
		{
			// SUCCESS!
		}
		catch( Exception e )
		{
			Assert.fail( "Wrong exception while testing create with create with invalid name", e );
		}
	}

	//////////////////////////////////////////////////////////////////////////////
	// createFederationExecution( String, URL[] ) ////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////

	/////////////////////////////////////////////////////
	// TEST: (valid) testCreateFederationWithModules() //
	/////////////////////////////////////////////////////
	@Test(groups="temp")
	public void testCreateFederationWithModules()
	{
		// create a link to the FOM //
		URL[] modules = new URL[]{
			ClassLoader.getSystemResource( "fom/ieee1516e/HLAstandardMIM.xml" ),
			ClassLoader.getSystemResource( "fom/ieee1516e/restaurant/RestaurantProcesses.xml" ),
			ClassLoader.getSystemResource( "fom/ieee1516e/restaurant/RestaurantFood.xml" ),
			ClassLoader.getSystemResource( "fom/ieee1516e/restaurant/RestaurantDrinks.xml" ),
			ClassLoader.getSystemResource( "fom/ieee1516e/restaurant/RestaurantSoup.xml" ),
		};
		
		// try and create a valid federation //
		try
		{
			defaultFederate.rtiamb.createFederationExecution( defaultFederate.simpleName, modules );
		}
		catch( Exception e )
		{
			Assert.fail( "Could not create valid federation", e );
		}

		// quickly join so that we can query the RTIambassador for handle information
		// to validate that the FOMs got merged successfully and everything is present.
		defaultFederate.quickJoin();

		// validate that the merged object model contains all the right pieces
		// classes from RestaurantProcess.xml
		defaultFederate.quickOCHandle( "HLAobjectRoot.Employee.Waiter" );
		defaultFederate.quickICHandle( "HLAinteractionRoot.CustomerTransactions.FoodServed" );
		
		// classes from RestaurantFood.xml
		defaultFederate.quickOCHandle( "HLAobjectRoot.Food.SideDish.Corn" );
		
		// classes from RestaurantDrinks.xml
		defaultFederate.quickOCHandle( "HLAobjectRoot.Food.Drink.Coffee" );
		defaultFederate.quickOCHandle( "HLAobjectRoot.Food.Drink.Soda" );
		// get attributes - one declared in this FOM, the other inherted and declared in food FOM
		defaultFederate.quickACHandle( "HLAobjectRoot.Food.Drink.Soda", "Flavor" );     // declared
		defaultFederate.quickACHandle( "HLAobjectRoot.Food.Drink.Soda", "NumberCups" ); // inherited
		
		// classes from RestaurantSoup.xml
		defaultFederate.quickOCHandle( "HLAobjectRoot.Food.Appetizers.Soup.ClamChowder.NewEngland" );
	}

	////////////////////////////////////////////////////////////////////////////
	// TEST: testCreateFederationWithModulesAndNonEquivalentObjectHierarchy() //
	////////////////////////////////////////////////////////////////////////////
	@Test
	public void testCreateFederationWithModulesAndNonEquivalentObjectHierarchy()
	{
		// create a link to the FOM //
		URL[] modules = new URL[]{
			ClassLoader.getSystemResource( "fom/ieee1516e/HLAstandardMIM.xml" ),
			ClassLoader.getSystemResource( "fom/ieee1516e/non-equiv/NonEquivalentMIM-Objects.xml" )
		};
		
		// try and create a valid federation //
		try
		{
			defaultFederate.rtiamb.createFederationExecution( defaultFederate.simpleName, modules );
			Assert.fail( "Expected exception when merging modules with non-equivalent hierarchies" );
		}
		catch( InconsistentFDD ifdd )
		{
			// success!
		}
		catch( Exception e )
		{
			unexpectedException( "Merging modules with non-equivalent object hierarchies", e );
		}
	}

	/////////////////////////////////////////////////////////////////////////////////
	// TEST: testCreateFederationWithModulesAndNonEquivalentInteractionHierarchy() //
	/////////////////////////////////////////////////////////////////////////////////
	@Test
	public void testCreateFederationWithModulesAndNonEquivalentInteractionHierarchy()
	{
		// create a link to the FOM //
		URL[] modules = new URL[]{
			ClassLoader.getSystemResource( "fom/ieee1516e/HLAstandardMIM.xml" ),
			ClassLoader.getSystemResource( "fom/ieee1516e/non-equiv/NonEquivalentMIM-Interactions.xml" )
		};
		
		// try and create a valid federation //
		try
		{
			defaultFederate.rtiamb.createFederationExecution( defaultFederate.simpleName, modules );
			Assert.fail( "Expected exception when merging modules with non-equivalent hierarchies" );
		}
		catch( InconsistentFDD ifdd )
		{
			// success!
		}
		catch( Exception e )
		{
			unexpectedException( "Merging modules with non-equivalent interaction hierarchies", e );
		}
	}

	//////////////////////////////////////////////////////////////////////////////
	// createFederationExecution( String, URL[], Mim ) ///////////////////////////
	//////////////////////////////////////////////////////////////////////////////

	///////////////////////////////////////////////////////////
	// TEST: (valid) testCreateFederationWithModulesAndMim() //
	///////////////////////////////////////////////////////////
	@Test
	public void testCreateFederationWithModulesAndMim()
	{
		// create a link to the FOM //
		URL mim = ClassLoader.getSystemResource( "fom/ieee1516e/HLAstandardMIM.xml" );
		URL[] modules = new URL[]{
			ClassLoader.getSystemResource( "fom/ieee1516e/restaurant/RestaurantProcesses.xml" ),
			ClassLoader.getSystemResource( "fom/ieee1516e/restaurant/RestaurantFood.xml" ),
			ClassLoader.getSystemResource( "fom/ieee1516e/restaurant/RestaurantDrinks.xml" ),
			ClassLoader.getSystemResource( "fom/ieee1516e/restaurant/RestaurantSoup.xml" ),
		};
		
		// try and create a valid federation //
		try
		{
			defaultFederate.rtiamb.createFederationExecution( defaultFederate.simpleName,
			                                                  modules,
			                                                  mim );
		}
		catch( Exception e )
		{
			Assert.fail( "Could not create valid federation", e );
		}

		// quickly join so that we can query the RTIambassador for handle information
		// to validate that the FOMs got merged successfully and everything is present.
		defaultFederate.quickJoin();

		// validate that the merged object model contains all the right pieces
		// classes from RestaurantProcess.xml
		defaultFederate.quickOCHandle( "HLAobjectRoot.Employee.Waiter" );
		defaultFederate.quickICHandle( "HLAinteractionRoot.CustomerTransactions.FoodServed" );
		
		// classes from RestaurantFood.xml
		defaultFederate.quickOCHandle( "HLAobjectRoot.Food.SideDish.Corn" );
		
		// classes from RestaurantDrinks.xml
		defaultFederate.quickOCHandle( "HLAobjectRoot.Food.Drink.Coffee" );
		
		// classes from RestaurantSoup.xml
		defaultFederate.quickOCHandle( "HLAobjectRoot.Food.Appetizers.Soup.ClamChowder.NewEngland" );
	}

	//////////////////////////////////////////////////////////////////////////////
	// createFederationExecution( String, URL[], TimeType ) //////////////////////
	//////////////////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////
	// TEST: (valid) testCreateFederationWithModulesAndTime() //
	////////////////////////////////////////////////////////////
	@Test
	public void testCreateFederationWithModulesAndTime()
	{
		// create a link to the FOM //
		URL[] modules = new URL[]{
			ClassLoader.getSystemResource( "fom/ieee1516e/HLAstandardMIM.xml" ),
			ClassLoader.getSystemResource( "fom/ieee1516e/restaurant/RestaurantProcesses.xml" ),
			ClassLoader.getSystemResource( "fom/ieee1516e/restaurant/RestaurantFood.xml" ),
			ClassLoader.getSystemResource( "fom/ieee1516e/restaurant/RestaurantDrinks.xml" ),
			ClassLoader.getSystemResource( "fom/ieee1516e/restaurant/RestaurantSoup.xml" ),
		};
		
		// try and create a valid federation //
		try
		{
			defaultFederate.rtiamb.createFederationExecution( defaultFederate.simpleName,
			                                                  modules,
			                                                  "HLAfloat64Time" );
		}
		catch( Exception e )
		{
			Assert.fail( "Could not create valid federation", e );
		}

		// quickly join so that we can query the RTIambassador for handle information
		// to validate that the FOMs got merged successfully and everything is present.
		defaultFederate.quickJoin();

		// validate that the merged object model contains all the right pieces
		// classes from RestaurantProcess.xml
		defaultFederate.quickOCHandle( "HLAobjectRoot.Employee.Waiter" );
		defaultFederate.quickICHandle( "HLAinteractionRoot.CustomerTransactions.FoodServed" );
		
		// classes from RestaurantFood.xml
		defaultFederate.quickOCHandle( "HLAobjectRoot.Food.SideDish.Corn" );
		
		// classes from RestaurantDrinks.xml
		defaultFederate.quickOCHandle( "HLAobjectRoot.Food.Drink.Coffee" );
		
		// classes from RestaurantSoup.xml
		defaultFederate.quickOCHandle( "HLAobjectRoot.Food.Appetizers.Soup.ClamChowder.NewEngland" );
	}

	//////////////////////////////////////////////////////////////////////////////
	// createFederationExecution( String, URL[], MIM, TimeType ) /////////////////
	//////////////////////////////////////////////////////////////////////////////

	///////////////////////////////////////////////////////////////
	// TEST: (valid) testCreateFederationWithModulesMimAndTime() //
	///////////////////////////////////////////////////////////////
	@Test
	public void testCreateFederationWithModulesMimAndTime()
	{
		// create a link to the FOM //
		URL mim = ClassLoader.getSystemResource( "fom/ieee1516e/HLAstandardMIM.xml" );
		URL[] modules = new URL[]{
			ClassLoader.getSystemResource( "fom/ieee1516e/restaurant/RestaurantProcesses.xml" ),
			ClassLoader.getSystemResource( "fom/ieee1516e/restaurant/RestaurantFood.xml" ),
			ClassLoader.getSystemResource( "fom/ieee1516e/restaurant/RestaurantDrinks.xml" ),
			ClassLoader.getSystemResource( "fom/ieee1516e/restaurant/RestaurantSoup.xml" ),
		};
		
		// try and create a valid federation //
		try
		{
			defaultFederate.rtiamb.createFederationExecution( defaultFederate.simpleName,
			                                                  modules,
			                                                  mim, 
			                                                  "HLAfloat64Time");
		}
		catch( Exception e )
		{
			Assert.fail( "Could not create valid federation", e );
		}

		// quickly join so that we can query the RTIambassador for handle information
		// to validate that the FOMs got merged successfully and everything is present.
		defaultFederate.quickJoin();

		// validate that the merged object model contains all the right pieces
		// classes from RestaurantProcess.xml
		defaultFederate.quickOCHandle( "HLAobjectRoot.Employee.Waiter" );
		defaultFederate.quickICHandle( "HLAinteractionRoot.CustomerTransactions.FoodServed" );
		
		// classes from RestaurantFood.xml
		defaultFederate.quickOCHandle( "HLAobjectRoot.Food.SideDish.Corn" );
		
		// classes from RestaurantDrinks.xml
		defaultFederate.quickOCHandle( "HLAobjectRoot.Food.Drink.Coffee" );
		
		// classes from RestaurantSoup.xml
		defaultFederate.quickOCHandle( "HLAobjectRoot.Food.Appetizers.Soup.ClamChowder.NewEngland" );
	}

	//----------------------------------------------------------
	//                     STATIC METHODS
	//----------------------------------------------------------
}
