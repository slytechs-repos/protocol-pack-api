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
package com.slytechs.protocol.pack.core;

import com.slytechs.protocol.AddressType;

/**
 * The Class Ip4Address.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author mark
 */
public final class Ip4Address extends IpAddress {

	/** The Constant IP4_ADDRESS_SIZE. */
	public final static int IP4_ADDRESS_SIZE = 4;

	/**
	 * To ip 4 address array.
	 *
	 * @param address the address
	 * @return the byte[]
	 */
	public static byte[] toIp4AddressArray(int address) {

		// 4 bytes in length
		return new byte[] {
				(byte) ((address >> 24) & 0xFF),
				(byte) ((address >> 16) & 0xFF),
				(byte) ((address >> 8) & 0xFF),
				(byte) ((address >> 0) & 0xFF),
		};
	}

	/** The bytes. */
	private final byte[] bytes;

	public Ip4Address() {
		this(new byte[IP4_ADDRESS_SIZE]);
	}

	/**
	 * Instantiates a new ip 4 address.
	 *
	 * @param address the address
	 */
	public Ip4Address(byte[] address) {
		super(AddressType.IPv4);
		this.bytes = address;

		if (address.length != 4)
			throw new IllegalArgumentException("invalid IPv4 address length " + address.length);

	}

	/**
	 * To array.
	 *
	 * @return the byte[]
	 */
	@Override
	public byte[] asArray() {
		return bytes;
	}

}
