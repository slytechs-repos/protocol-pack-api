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
package com.slytechs.protocol.runtime.util;

import java.nio.ByteBuffer;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import com.slytechs.protocol.runtime.internal.ArrayUtils;

/**
 * Utility class and a collection of different kinds of checksum generation
 * methods.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public class Checksums {

	/**
	 * Implements the IP Checksum 16 algorithm, for calculating the complement of 16
	 * bit checksum.
	 *
	 * @author Sly Technologies Inc
	 * @author repos@slytechs.com
	 * @author Mark Bednarczyk
	 */
	public static class CRC16 implements Checksum {

		/** The value. */
		private long value;

		/**
		 * Instantiates a new crc16.
		 */
		public CRC16() {
		}

		/**
		 * Update.
		 *
		 * @param b the b
		 * @see java.util.zip.Checksum#update(int)
		 */
		@Override
		public void update(int b) {
			value += b;
		}

		/**
		 * Update.
		 *
		 * @param b   the b
		 * @param off the off
		 * @param len the len
		 * @see java.util.zip.Checksum#update(byte[], int, int)
		 */
		@Override
		public void update(byte[] b, int off, int len) {
			for (int i = 0; i < len; i += 2) {
				int v = Short.toUnsignedInt(ArrayUtils.getShort(b, off + i, true));

				value += v;
			}
		}

		/**
		 * Gets the value.
		 *
		 * @return the value
		 * @see java.util.zip.Checksum#getValue()
		 */
		@Override
		public long getValue() {
			value = ((value & 0xFFFF) + (value >> 16) & 0xFFFF);
			return Short.toUnsignedLong((short) ~value);
		}

		/**
		 * Reset.
		 *
		 * @see java.util.zip.Checksum#reset()
		 */
		@Override
		public void reset() {
			value = 0;
		}

	}

	/**
	 * Instantiates a new checksum utils.
	 */
	public Checksums() {
	}

	/**
	 * Crc 16.
	 *
	 * @param bytes the bytes
	 * @return the int
	 */
	public static int crc16(byte[] bytes) {
		Checksum checksum = new CRC16();
		checksum.update(bytes, 0, bytes.length);

		return (int) checksum.getValue();
	}

	/**
	 * Crc 16.
	 *
	 * @param bytes the bytes
	 * @return the int
	 */
	public static int crc16(ByteBuffer bytes) {
		Checksum checksum = new CRC16();
		checksum.update(bytes);

		return (int) checksum.getValue();
	}

	/**
	 * Crc 32.
	 *
	 * @param bytes the bytes
	 * @return the long
	 */
	public static long crc32(byte[] bytes) {
		Checksum checksum = new CRC32();
		checksum.update(bytes, 0, bytes.length);

		return checksum.getValue();
	}

	/**
	 * Crc 32.
	 *
	 * @param bytes the bytes
	 * @return the long
	 */
	public static long crc32(ByteBuffer bytes) {
		Checksum checksum = new CRC32();
		checksum.update(bytes);

		return checksum.getValue();
	}
}
