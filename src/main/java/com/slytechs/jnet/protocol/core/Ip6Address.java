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
package com.slytechs.jnet.protocol.core;

import com.slytechs.jnet.protocol.AddressType;
import com.slytechs.jnet.runtime.internal.ArrayUtils;
import com.slytechs.jnet.runtime.util.HexStrings;

/**
 * The Class Ip6Address.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author mark
 */
public final class Ip6Address extends IpAddress {

	/** The Constant IP6_ADDRESS_SIZE. */
	public static final int IP6_ADDRESS_SIZE = 16;

	/**
	 * To ip 6 address array.
	 *
	 * @param high the high
	 * @param low  the low
	 * @return the byte[]
	 */
	public static byte[] toIp6AddressArray(long high, long low) {

		// 16 bytes in length
		return new byte[] {
				(byte) ((high >> 56) & 0xFF),
				(byte) ((high >> 48) & 0xFF),
				(byte) ((high >> 40) & 0xFF),
				(byte) ((high >> 32) & 0xFF),
				(byte) ((high >> 24) & 0xFF),
				(byte) ((high >> 16) & 0xFF),
				(byte) ((high >> 8) & 0xFF),
				(byte) ((high >> 0) & 0xFF),

				(byte) ((low >> 56) & 0xFF),
				(byte) ((low >> 48) & 0xFF),
				(byte) ((low >> 40) & 0xFF),
				(byte) ((low >> 32) & 0xFF),
				(byte) ((low >> 24) & 0xFF),
				(byte) ((low >> 16) & 0xFF),
				(byte) ((low >> 8) & 0xFF),
				(byte) ((low >> 0) & 0xFF),
		};
	}

	/** The high. */
	private final long high;

	/** The low. */
	private final long low;

	/** The bytes. */
	private byte[] bytes;

	/**
	 * Instantiates a new ip 6 address.
	 *
	 * @param address the address
	 */
	public Ip6Address(byte[] address) {
		super(AddressType.IPv6);

		this.high = ArrayUtils.getLong(address, 0, true);
		this.low = ArrayUtils.getLong(address, 8, true);
	}

	/**
	 * To array.
	 *
	 * @return the byte[]
	 */
	public byte[] toArray() {
		if (bytes == null)
			bytes = Ip6Address.toIp6AddressArray(high, low);

		return bytes;
	}

	/**
	 * To int.
	 *
	 * @return the int
	 */
	public int toInt() {
		return (int) ((low ^ (high >> 32)) & 0xFFFFFFFF);
	}

	/**
	 * To long.
	 *
	 * @return the long
	 */
	public long toLong() {
		return (low ^ high);
	}

	/**
	 * To long high.
	 *
	 * @return the long
	 */
	public long toLongHigh() {
		return high;
	}

	/**
	 * To long low.
	 *
	 * @return the long
	 */
	public long toLongLow() {
		return low;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return HexStrings.toIp6String(bytes);
	}
}
