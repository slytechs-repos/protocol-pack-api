/*
 * Sly Technologies Free License
 * 
 * Copyright 2023 Sly Technologies Inc.
 *
 * Licensed under the Sly Technologies Free License (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.slytechs.com/free-license-text
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.slytechs.protocol.pack.core.constants;

import java.util.function.IntSupplier;

/**
 * Address Resolution Protocol (ARP) operation field values.
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public enum ArpOp implements IntSupplier {

	/** The noop. */
	NOOP,
	
	/** The request. */
	REQUEST,
	
	/** The reply. */
	REPLY,
	
	/** The reverse request. */
	REVERSE_REQUEST,
	
	/** The reserse reply. */
	RESERSE_REPLY,

	;

	/** The Constant ARP_OP_REPLY. */
	/* @formatter:off */
	public static final int ARP_OP_REPLY            = 1;
	
	/** The Constant ARP_OP_RESPONSE. */
	public static final int ARP_OP_RESPONSE         = 2;
	
	/** The Constant ARP_OP_REVERSE_REQUEST. */
	public static final int ARP_OP_REVERSE_REQUEST  = 3;
	
	/** The Constant ARP_OP_REVERSE_REPLY. */
	public static final int ARP_OP_REVERSE_REPLY    = 4;
	/* @formatter:on */

	/**
	 * Gets the as int.
	 *
	 * @return the as int
	 * @see java.util.function.IntSupplier#getAsInt()
	 */
	@Override
	public int getAsInt() {
		return ordinal();
	}

	/**
	 * Value of arp op.
	 *
	 * @param operation the operation
	 * @return the arp op
	 */
	public static ArpOp valueOfArpOp(int operation) {
		return values()[operation];
	}
	
	/**
	 * Resolve.
	 *
	 * @param intValue the int value
	 * @return the string
	 */
	public static String resolve(Object intValue) {
		if (intValue instanceof Number n)
			return valueOfArpOp(n.intValue()).name();
		
		return intValue.toString();
	}

}
