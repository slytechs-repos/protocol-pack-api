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
package com.slytechs.jnet.runtime.util;

import static java.lang.Long.*;

/**
 * The Class BitConstants.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public final class Bits {

	/** The Constant MASK_01. */
	public static final long MASK_01 = 0x00000000_00000001l;

	/** The Constant MASK_04. */
	public static final long MASK_04 = 0x00000000_0000000Fl;

	/** The Constant MASK_08. */
	public static final long MASK_08 = 0x00000000_000000FFl;

	/** The Constant MASK_16. */
	public static final long MASK_16 = 0x00000000_0000FFFFl;

	/** The Constant MASK_24. */
	public static final long MASK_24 = 0x00000000_00FFFFFFl;

	/** The Constant MASK_32. */
	public static final long MASK_32 = 0x00000000_FFFFFFFFl;

	/** The Constant MASK_40. */
	public static final long MASK_40 = 0x000000FF_FFFFFFFFl;

	/** The Constant MASK_48. */
	public static final long MASK_48 = 0x0000FFFF_FFFFFFFFl;

	/** The Constant MASK_56. */
	public static final long MASK_56 = 0x00FFFFFF_FFFFFFFFl;

	/** The Constant MASK_64. */
	public static final long MASK_64 = 0xFFFFFFFF_FFFFFFFFl;

	/** The Constant MASK_01_INV. */
	public static final long MASK_01_INV = ~MASK_01;

	/** The Constant MASK_04_INV. */
	public static final long MASK_04_INV = ~MASK_04;

	/** The Constant MASK_08_INV. */
	public static final long MASK_08_INV = ~MASK_08;

	/** The Constant MASK_16_INV. */
	public static final long MASK_16_INV = ~MASK_16;

	/** The Constant MASK_24_INV. */
	public static final long MASK_24_INV = ~MASK_24;

	/** The Constant MASK_32_INV. */
	public static final long MASK_32_INV = ~MASK_32;

	/** The Constant MASK_40_INV. */
	public static final long MASK_40_INV = ~MASK_40;

	/** The Constant MASK_48_INV. */
	public static final long MASK_48_INV = ~MASK_48;

	/** The Constant MASK_56_INV. */
	public static final long MASK_56_INV = ~MASK_56;

	/** The Constant MASK_64_INV. */
	public static final long MASK_64_INV = ~MASK_64;

	/** The Constant ALIGN_01_BIT. */
	public static final int ALIGN_01_BIT = 1;

	/** The Constant ALIGN_08_BITS. */
	public static final int ALIGN_08_BITS = 8;

	/** The Constant ALIGN_16_BITS. */
	public static final int ALIGN_16_BITS = 16;

	/** The Constant ALIGN_32_BITS. */
	public static final int ALIGN_32_BITS = 32;

	/** The Constant ALIGN_64_BITS. */
	public static final int ALIGN_64_BITS = 64;

	/** The Constant DEFAULT_ALIGNMENT. */
	public static final int DEFAULT_ALIGNMENT = 8;

	/** The Constant SHIFT_BITS_TO_BYTES. */
	public static final int SHIFT_BITS_TO_BYTES = 3;

	/** The Constant SHIFT_BITS_TO_SHORTS. */
	public static final int SHIFT_BITS_TO_SHORTS = 4;

	/** The Constant SHIFT_BITS_TO_INTS. */
	public static final int SHIFT_BITS_TO_INTS = 5;

	/** The Constant SHIFT_BITS_TO_LONGS. */
	public static final int SHIFT_BITS_TO_LONGS = 6;

	/** The Constant SHIFT_BYTES_TO_BITS. */
	public static final int SHIFT_BYTES_TO_BITS = 3;

	/** The Constant SHIFT_SHORTS_TO_BITS. */
	public static final int SHIFT_SHORTS_TO_BITS = 4;

	/** The Constant SHIFT_INTS_TO_BITS. */
	public static final int SHIFT_INTS_TO_BITS = 5;

	/** The Constant SHIFT_LONGS_TO_BITS. */
	public static final int SHIFT_LONGS_TO_BITS = 6;

	/** The Constant BITS_00. */
	public static final int BITS_00 = 0b00000000_00000000;
	
	/** The Constant BITS_01. */
	public static final int BITS_01 = 0b00000000_00000001;
	
	/** The Constant BITS_02. */
	public static final int BITS_02 = 0b00000000_00000011;
	
	/** The Constant BITS_03. */
	public static final int BITS_03 = 0b00000000_00000111;
	
	/** The Constant BITS_04. */
	public static final int BITS_04 = 0b00000000_00001111;
	
	/** The Constant BITS_05. */
	public static final int BITS_05 = 0b00000000_00011111;
	
	/** The Constant BITS_06. */
	public static final int BITS_06 = 0b00000000_00111111;
	
	/** The Constant BITS_07. */
	public static final int BITS_07 = 0b00000000_01111111;
	
	/** The Constant BITS_08. */
	public static final int BITS_08 = 0b00000000_11111111;
	
	/** The Constant BITS_09. */
	public static final int BITS_09 = 0b00000001_11111111;
	
	/** The Constant BITS_10. */
	public static final int BITS_10 = 0b00000011_11111111;
	
	/** The Constant BITS_11. */
	public static final int BITS_11 = 0b00000111_11111111;
	
	/** The Constant BITS_16. */
	public static final int BITS_16 = 0b11111111_11111111;
	
	/** The Constant BITS_24. */
	public static final int BITS_24 = 0x00FFFFFF;
	
	/** The Constant BITS_32. */
	public static final int BITS_32 = 0xFFFFFFFF;

	/**
	 * Aligment mask from bits.
	 *
	 * @param bits the bits
	 * @return the long
	 */
	public static long aligmentMaskFromBits(long bits) {
		long mask = (bits == 64) ? MASK_64 : (bits - 1l);

//		System.out.printf("BitConstants:: mask=0x%08X/0x%016X/%d bits=%d%n",
//				mask,
//				~mask,
//				Long.bitCount(~mask),
//				bits);

		return mask;
	}

	/**
	 * Bits to bytes.
	 *
	 * @param bitIndex the bit index
	 * @return the long
	 */
	public static long bitsToBytes(long bitIndex) {
		return (bitIndex >> SHIFT_BITS_TO_BYTES);
	}

	/**
	 * Bytes to bits.
	 *
	 * @param byteIndex the byte index
	 * @return the long
	 */
	public static long bytesToBits(long byteIndex) {
		return (byteIndex << SHIFT_BYTES_TO_BITS);
	}

	/**
	 * Check aligment using bits.
	 *
	 * @param index the index
	 * @param bits  the bits
	 * @return the long
	 */
	public static long checkAligmentUsingBits(long index, int bits) {
		return checkAligmentUsingMask(index, aligmentMaskFromBits(bits));
	}

	/**
	 * Check aligment using mask.
	 *
	 * @param index the index
	 * @param mask  the mask
	 * @return the long
	 */
	public static long checkAligmentUsingMask(long index, long mask) {
		if ((index & mask) != 0)
			throw new DataAlignmentException(""
					+ (numberOfLeadingZeros(mask)) + " bit boundary at index " + index);

		return index;
	}

	/**
	 * Checks if is aligned using mask.
	 *
	 * @param index the index
	 * @param mask  the mask
	 * @return true, if is aligned using mask
	 */
	public static boolean isAlignedUsingMask(long index, long mask) {
		return ((index & mask) != 0);
	}

	/**
	 * Value mask from size.
	 *
	 * @param bitSize the bit size
	 * @return the long
	 */
	public static long valueMaskFromSize(long bitSize) {
		return (bitSize == 64) ? MASK_64 : ((1L << (bitSize)) - 1);
	}
}
