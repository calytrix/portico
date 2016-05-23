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

import hla.rti1516e.SaveFailureReason;

import java.util.Map;

import org.portico.lrc.services.saverestore.msg.SaveComplete;
import org.portico.utils.messaging.MessageContext;
import org.portico.utils.messaging.MessageHandler;

/**
 * Generates federationSaved() & federationNotSaved() callbacks to a IEEE-1516e compliant
 * federate ambassador
 */
@MessageHandler(modules="lrc1516e-callback",
                keywords= {"lrc1516e"},
                sinks="incoming",
                priority=3,
                messages=SaveComplete.class)
public class SaveCompleteCallbackHandler extends HLA1516eCallbackHandler
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
		SaveComplete request = context.getRequest( SaveComplete.class, this );
		boolean success = request.isSuccessful();

		if( success )
		{
			if( logger.isTraceEnabled() )
				logger.trace( "CALLBACK federationSaved()" );
			
			fedamb().federationSaved();
			
			if( logger.isTraceEnabled() )
				logger.trace( "         federationSaved() complete" );
		}
		else
		{
			if( logger.isTraceEnabled() )
				logger.trace( "CALLBACK federationNotSaved()" );
			
			// failure reason hard coded - needs to be fixed!
			fedamb().federationNotSaved( SaveFailureReason.RTI_UNABLE_TO_SAVE );

			if( logger.isTraceEnabled() )
				logger.trace( "         federationNotSaved()" );
		}

		// mark the call as successful
		context.success();
	}

	//----------------------------------------------------------
	//                     STATIC METHODS
	//----------------------------------------------------------
}
