/*
 * Apache License, Version 2.0
 * 
 * Copyright 2013-2022 Sly Technologies Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.slytechs.jnet.protocol.core;

import com.slytechs.jnet.runtime.util.HexStrings;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author mark
 *
 */
public final class Ip4Address extends IpAddress {

	public final static int IP4_ADDRESS_SIZE = 4;

	public static byte[] toIp4AddressArray(int address) {

		// 4 bytes in length
		return new byte[] {
				(byte) ((address >> 24) & 0xFF),
				(byte) ((address >> 16) & 0xFF),
				(byte) ((address >> 8) & 0xFF),
				(byte) ((address >> 0) & 0xFF),
		};
	}

	private final byte[] bytes;

	public Ip4Address(byte[] address) {
		this.bytes = address;

		if (address.length != 4)
			throw new IllegalArgumentException("invalid IPv4 address length " + address.length);

	}

	public byte[] toArray() {
		return bytes;
	}

	@Override
	public String toString() {
		return HexStrings.toIp4String(bytes);
	}
}
