/*
 *   Copyright 2006 The Portico Project
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
package org.portico.impl;

/**
 * Enumeration to signal the HLA specification version is in use.
 */
public enum HLAVersion
{
	HLA13,      // standard HLA v1.3 interface
	JAVA1,      // the "java1" HLA v1.3 interface (rti13.java1 package from old DMSO java bindings)
	IEEE1516,   // standard IEEE 1516-2000 interface
	IEEE1516e;  // standard IEEE 1516-2010 interface
}
