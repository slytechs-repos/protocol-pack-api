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
package com.slytechs.jnet.runtime.util.format;

import static java.lang.Character.isWhitespace;
import static java.util.Objects.checkIndex;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

/**
 * The Class BitFormat.
 */
@SuppressWarnings("preview")
public class BitFormat extends Format {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4051297746712793825L;
	
	/** The Constant DEFAULT_BITPATTERN_IGNORE_CHAR. */
	public static final char DEFAULT_BITPATTERN_IGNORE_CHAR = '.';

	/** The bit pattern. */
	protected final String bitPattern;
	
	/** The mask long. */
	protected long maskLong;
	
	/** The mask array. */
	protected byte[] maskArray;
	
	/** The bit len. */
	protected final int bitLen;
	
	/** The off char. */
	private char offChar;
	
	/** The sparse mode. */
	private boolean sparseMode;

	/**
	 * Instantiates a new bit format.
	 *
	 * @param bitPattern the bit pattern
	 */
	public BitFormat(String bitPattern) {
		this(bitPattern, DEFAULT_BITPATTERN_IGNORE_CHAR);
	}

	/**
	 * Instantiates a new bit format.
	 *
	 * @param bitPattern the bit pattern
	 * @param offChar    the off char
	 */
	public BitFormat(String bitPattern, char offChar) {
		this.bitPattern = bitPattern;
		this.offChar = offChar;
		this.bitLen = checkIndex(bitPattern.replaceAll("\s", "")
				.length(), 64);
		this.maskLong = bitPatternToLongMask(bitPattern);
		this.maskArray = bitPatternToArrayMask(bitPattern);
	}

	/**
	 * Bit pattern to array mask.
	 *
	 * @param bitPattern the bit pattern
	 * @return the byte[]
	 */
	private byte[] bitPatternToArrayMask(String bitPattern) {
		bitPattern = bitPattern.replaceAll("\s", ""); // Remove all whitespace

		final int bitPadding = ((8 - (bitLen % 8)) % 8); // 0-7 bits inclusive
		final int byteLen = (bitLen >> 3);
		final int bytePadding = (bitPadding == 0) ? 0 : 1;

		final byte[] mask = new byte[byteLen + bytePadding];

		// i = bit index, k = pattern index
		for (int i = bitPadding, k = 0; i < bitLen + bitPadding; i++, k++) {
			char ch = bitPattern.charAt(k);
			int j = (i >> 3); // j = byte index

			if (ch != offChar)
				mask[j] |= (1 << (7 - (i & 0x7)));
		}

		return mask;
	}

	/**
	 * Sparse mode.
	 *
	 * @param b the b
	 * @return the bit format
	 */
	public BitFormat sparseMode(boolean b) {
		this.sparseMode = b;

		return this;
	}

	/**
	 * Bit pattern to long mask.
	 *
	 * @param bitPattern the bit pattern
	 * @return the long
	 */
	private long bitPatternToLongMask(String bitPattern) {
		bitPattern = bitPattern.replaceAll("\s", ""); // Remove all whitespace

		final int bitPadding = ((64 - (bitLen & 0x3F)) & 0x3F); // 0-63 bits inclusive

		long mask = 0;

		// i = bit index, k = pattern index
		for (int i = bitPadding, k = 0; i < 64; i++, k++) {
			char ch = bitPattern.charAt(k);

			if (ch != offChar)
				mask |= (1 << (7 - (i & 0x7)));
		}

		return mask;
	}

	/**
	 * Format.
	 *
	 * @param array the array
	 * @return the string
	 */
	public String format(byte[] array) {
		String str = formatArray(array, new StringBuffer(), new FieldPosition(0)).toString();
		if (sparseMode)
			str = str.replaceAll("[\\s" + offChar + "]", "");

		return str;
	}

	/**
	 * Format.
	 *
	 * @param intNumber the int number
	 * @return the string
	 */
	public String format(int intNumber) {
		String str = formatLong(intNumber, new StringBuffer(), new FieldPosition(0)).toString();
		if (sparseMode)
			str = str.replaceAll("[\\s" + offChar + "]", "");

		return str;
	}

	/**
	 * Format.
	 *
	 * @param longNumber the long number
	 * @return the string
	 */
	public String format(long longNumber) {
		String str = formatLong(longNumber, new StringBuffer(), new FieldPosition(0)).toString();
		if (sparseMode)
			str = str.replaceAll("[\\s" + offChar + "]", "");

		return str;
	}

	/**
	 * @see java.text.Format#format(java.lang.Object, java.lang.StringBuffer, java.text.FieldPosition)
	 */
	@Override
	public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
		return switch (obj) {
		case Number n -> formatLong(n.longValue(), toAppendTo, pos);
		case byte[] a -> formatArray(a, toAppendTo, pos);
		default -> throw new IllegalArgumentException("Cannot format given Object as a bit field " + obj);
		};
	}

	/**
	 * Format array.
	 *
	 * @param value      the value
	 * @param toAppendTo the to append to
	 * @param pos        the pos
	 * @return the string buffer
	 */
	private StringBuffer formatArray(byte[] value, StringBuffer toAppendTo, FieldPosition pos) {

		String str = formatBitPattern(bitPattern, value);

		toAppendTo.append(str);
		pos.setEndIndex(str.length());

		return toAppendTo;
	}

	/**
	 * Format bit pattern.
	 *
	 * @param bitPattern the bit pattern
	 * @param value      the value
	 * @return the string
	 */
	private String formatBitPattern(String bitPattern, byte[] value) {
		if (value.length < maskArray.length)
			throw new IllegalArgumentException("value and mask lengths not equal");

		StringBuilder b = new StringBuilder();

		final int bitPadding = ((8 - (bitLen & 0x7)) & 0x7); // 0-7 bits inclusive

		// i = bit index, k = pattern index, j = byte index
		for (int i = bitPadding, k = 0; i < bitLen; i++) {
			int j = (i >> 3);
			boolean bit = (value[j] & maskArray[j] & (1 << (7 - (i % 8)))) != 0;

			char ch;
			while (isWhitespace(ch = bitPattern.charAt(k++))) // Echo whitespace
				b.append(ch);

			if (ch == '1')
				b.append(bit ? ch : '0');
			else
				b.append(bit ? ch : offChar);

		}

		return b.toString();
	}

	/**
	 * Format bit pattern.
	 *
	 * @param bitPattern the bit pattern
	 * @param value      the value
	 * @return the string
	 */
	private String formatBitPattern(String bitPattern, long value) {
		StringBuilder b = new StringBuilder();

		final int bitPadding = ((64 - (bitLen & 0x3F)) & 0x3F); // 0-63 bits inclusive

		// i = bit index, k = pattern index
		for (int i = bitPadding, k = 0; i < 64; i++) {
			boolean bit = (value & maskLong & (1 << (63 - (i % 64)))) != 0;

			char ch;
			while (isWhitespace(ch = bitPattern.charAt(k++))) // Echo whitespace
				b.append(ch);

			if (ch == '1')
				b.append(bit ? ch : '0');
			else
				b.append(bit ? ch : offChar);

		}

		return b.toString();
	}

	/**
	 * Format long.
	 *
	 * @param value      the value
	 * @param toAppendTo the to append to
	 * @param pos        the pos
	 * @return the string buffer
	 */
	private StringBuffer formatLong(long value, StringBuffer toAppendTo, FieldPosition pos) {

		long bits = value;
		String str = formatBitPattern(bitPattern, bits);

		toAppendTo.append(str);
		pos.setEndIndex(str.length());

		return toAppendTo;
	}

	/**
	 * Optional and unsupported operation.
	 *
	 * @param source the source
	 * @param pos    the pos
	 * @return the object
	 * @throws UnsupportedOperationException the unsupported operation exception
	 */
	@Override
	public Object parseObject(String source, ParsePosition pos) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

}
