/*
 *   Copyright 2018 The Portico Project
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
package org.portico2.common.services.federation.msg;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashSet;
import java.util.Set;

import org.portico.lrc.model.ObjectModel;
import org.portico2.common.PorticoConstants;

/**
 * This class is NOT a Portico Message. It is an object that should be bundled inside the response
 * to a successful joinFederation() message. It contains all the information that a newly joined
 * federate needs to initialize itself once it has joined a federation. This includes:
 * <ol>
 *   <li>The federate's assigned handle</li>
 *   <li>The handle of the federation</li>
 *   <li>The consolidated FOM</li>
 *   <li>...more...</li>
 * </ol>
 */
public class WelcomePack implements Externalizable
{
	//----------------------------------------------------------
	//                    STATIC VARIABLES
	//----------------------------------------------------------

	//----------------------------------------------------------
	//                   INSTANCE VARIABLES
	//----------------------------------------------------------
	private int federationHandle;
	private int federateHandle;
	private ObjectModel fom;

	// Federation State
	private Set<String> syncpoints;

	//----------------------------------------------------------
	//                      CONSTRUCTORS
	//----------------------------------------------------------
	public WelcomePack()
	{
		this.federationHandle = PorticoConstants.NULL_HANDLE;
		this.federateHandle   = PorticoConstants.NULL_HANDLE;
		this.fom = null;
		
		// Federation State
		this.syncpoints = new HashSet<>();
	}

	//----------------------------------------------------------
	//                    INSTANCE METHODS
	//----------------------------------------------------------

	public ObjectModel getFOM() { return this.fom; }
	public int         getFederationHandle() { return this.federationHandle; }
	public int         getFederateHandle()   { return this.federateHandle; }
	public Set<String> getSyncPoints( )      { return this.syncpoints; }

	public void setFederationHandle( int handle ) { this.federationHandle = handle; }
	public void setFederateHandle  ( int handle ) { this.federateHandle = handle; }
	public void setFOM             ( ObjectModel fom ) { this.fom = fom; }
	public void setSyncPoints      ( Set<String> points ) { this.syncpoints = points; }
	
	///////////////////////////////////////////////////////////////////////////////////////
	///  Serialization Methods  ///////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////
	public void readExternal( ObjectInput input ) throws IOException, ClassNotFoundException
	{
		this.federationHandle = input.readInt();
		this.federateHandle = input.readInt();
		this.fom = (ObjectModel)input.readObject();
		
		// sync points
		int count = input.readInt();
		for( int i = 0; i < count; i++ )
			this.syncpoints.add( input.readUTF() );
	}
	
	public void writeExternal( ObjectOutput output ) throws IOException
	{
		output.writeInt( federationHandle );
		output.writeInt( federateHandle );
		output.writeObject( fom );
		
		// sync points
		output.writeInt( syncpoints.size() );
		for( String syncpoint : syncpoints )
			output.writeUTF( syncpoint );
	}

	//----------------------------------------------------------
	//                     STATIC METHODS
	//----------------------------------------------------------
}
