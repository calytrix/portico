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

import java.util.HashMap;
import java.util.Map;

import org.portico.impl.hla1516e.types.time.DoubleTime;
import org.portico.impl.hla1516e.types.HLA1516eHandle;
import org.portico.impl.hla1516e.types.HLA1516eParameterHandleValueMap;
import org.portico.lrc.PorticoConstants;
import org.portico.lrc.services.object.msg.SendInteraction;
import org.portico.utils.messaging.MessageContext;
import org.portico.utils.messaging.MessageHandler;

/**
 * Generate receiveInteraction() callbacks to a IEEE1516e compliant federate ambassador
 */
@MessageHandler(modules="lrc1516e-callback",
                keywords="lrc1516e",
                sinks="incoming",
                priority=3,
                messages=SendInteraction.class)
public class ReceiveInteractionCallbackHandler extends HLA1516eCallbackHandler
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
		SendInteraction request = context.getRequest( SendInteraction.class, this );
		int classHandle = request.getInteractionId();
		HashMap<Integer,byte[]> parameters = request.getParameters();
		double timestamp = request.getTimestamp();

		// convert the attributes into an appropriate form
		HLA1516eParameterHandleValueMap received = new HLA1516eParameterHandleValueMap(parameters);
		
		// do the callback
		if( request.isTimestamped() )
		{
			if( logger.isTraceEnabled() )
			{
				logger.trace( "CALLBACK receiveInteraction(class="+classHandle+",parameters="+
				              PorticoConstants.mapToStringWithSizes(parameters)+",time="+
				              timestamp+") (TSO)" );
			}
			
			fedamb().receiveInteraction( new HLA1516eHandle(classHandle),
			                             received,                  // map
			                             request.getTag(),          // tag
			                             null,                      // sent order
			                             null,                      // transport
			                             new DoubleTime(timestamp), // time 
			                             null,                      // received order
			                             new SupplementalInfo() );  // supplemental receive info
		}
		else
		{
			if( logger.isTraceEnabled() )
			{
				logger.trace( "CALLBACK receiveInteraction(class="+classHandle+",parameters="+
				              PorticoConstants.mapToStringWithSizes(parameters)+") (RO)" );
			}
			
			fedamb().receiveInteraction( new HLA1516eHandle(classHandle),
			                             received,          // map
			                             request.getTag(),  // tag
			                             null,              // sent order
			                             null,              // transport
			                             new SupplementalInfo() ); // supplemental receive info
		}
		
		context.success();
	}

	//----------------------------------------------------------
	//                     STATIC METHODS
	//----------------------------------------------------------
}
