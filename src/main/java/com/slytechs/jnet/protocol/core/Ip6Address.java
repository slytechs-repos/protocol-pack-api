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

import com.slytechs.jnet.runtime.internal.ArrayUtils;
import com.slytechs.jnet.runtime.util.HexStrings;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author mark
 *
 */
public final class Ip6Address extends IpAddress {
	public static final int IP6_ADDRESS_SIZE = 16;

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

	private final long high;
	private final long low;
	private byte[] bytes;

	public Ip6Address(byte[] address) {

		this.high = ArrayUtils.getLong(address, 0, true);
		this.low = ArrayUtils.getLong(address, 8, true);
	}

	public byte[] toArray() {
		if (bytes == null)
			bytes = Ip6Address.toIp6AddressArray(high, low);

		return bytes;
	}

	public int toInt() {
		return (int) ((low ^ (high >> 32)) & 0xFFFFFFFF);
	}

	public long toLong() {
		return (low ^ high);
	}

	public long toLongHigh() {
		return high;
	}

	public long toLongLow() {
		return low;
	}

	@Override
	public String toString() {
		return HexStrings.toIp6String(bytes);
	}
}
