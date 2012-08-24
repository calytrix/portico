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
package org.portico.impl.hla1516e.handlers;

import hla.rti1516e.RestoreFailureReason;

import java.util.Map;

import org.portico.lrc.services.saverestore.msg.RestoreComplete;
import org.portico.utils.messaging.MessageContext;
import org.portico.utils.messaging.MessageHandler;

/**
 * Generates federationRestored() & federationNotRestored() callbacks to a HLA 1.3 compliant
 * federate ambassador
 */
@MessageHandler(modules="lrc13-callback",
                keywords= {"lrc1516e"},
                sinks="incoming",
                priority=3,
                messages=RestoreComplete.class)
public class RestoreCompleteCallbackHandler extends HLA1516eCallbackHandler
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
	public void initialize( Map<String,Object> properties )
	{
		super.initialize( properties );
	}
	
	public void process( MessageContext context ) throws Exception
	{
		RestoreComplete request = context.getRequest( RestoreComplete.class, this );
		boolean success = request.isSuccessful();

		if( success )
		{
			if( logger.isTraceEnabled() )
				logger.trace( "CALLBACK federationRestored()" );
			
			fedamb().federationRestored();
		}
		else
		{
			if( logger.isTraceEnabled() )
				logger.trace( "CALLBACK federationNotRestored()" );

			// failure reason hard coded - needs to be fixed!
			fedamb().federationNotRestored( RestoreFailureReason.RTI_UNABLE_TO_RESTORE );
		}

		// mark the call as successful
		context.success();
	}

	//----------------------------------------------------------
	//                     STATIC METHODS
	//----------------------------------------------------------
}
