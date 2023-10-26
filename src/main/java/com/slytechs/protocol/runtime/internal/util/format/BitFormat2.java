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
package com.slytechs.protocol.runtime.internal.util.format;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.Objects;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 *
 */
public class BitFormat2 extends Format {

	private static final long serialVersionUID = -860206735691484555L;

	private char skipChars[] = " \t_".toCharArray();
	private char offChars[] = ".".toCharArray();
	private char onChars[] = "01".toCharArray();
	private char offChar = '0';
	private char onChar = '1';

	private final int maskBitLen;
	private final String formatString;
	private final char[] onMask;
	private final char[] offMask;

	public BitFormat2(String formatString) {
		this.formatString = formatString;
		this.onMask = calcOnMask(formatString);
		this.offMask = calcOffMask(formatString);
		this.maskBitLen = calcMaskLen(onMask);
	}

	private int calcMaskLen(char[] mask) {

		int count = 0;
		for (char c : mask)
			if (!isSkipChar(c))
				count++;

		return count;
	}

	private char[] calcOffMask(String fmt) {
		char[] offMask = fmt.toCharArray();

		for (int i = 0; i < offMask.length; i++)
			if (!isSkipChar(offMask[i]) && !isOffChar(offMask[i]))
				offMask[i] = offChar;

		return offMask;
	}

	private char[] calcOnMask(String fmt) {
		char[] onMask = fmt.toCharArray();

		for (int i = 0; i < onMask.length; i++)
			if (isOnChar(onMask[i]))
				onMask[i] = onChar;

		return onMask;
	}

	public String format(Object obj, int bitOffset) {
		return format(obj, bitOffset, new StringBuilder(), new FieldPosition(0)).toString();
	}

	/**
	 * @see java.text.Format#format(java.lang.Object, java.lang.StringBuffer,
	 *      java.text.FieldPosition)
	 */
	@Override
	public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
		var sbuilder = format(obj, 0, new StringBuilder(), pos);

		return toAppendTo.append(sbuilder);
	}

	public StringBuilder format(Object obj, int bitOffset, StringBuilder toAppendTo, FieldPosition pos) {

		if (obj instanceof Number num)
			return formatNumber(num, bitOffset, toAppendTo, pos);

		if (obj instanceof byte[] arr)
			return formatArray(arr, bitOffset, toAppendTo, pos);

		throw new IllegalArgumentException("object type [%s] is not supported for a per bit-formatting"
				.formatted(obj.getClass()));
	}

	public StringBuilder formatArray(byte[] arr, int bitOffset, StringBuilder toAppendTo,
			FieldPosition pos) {
		return formatArrayBE(arr, bitOffset, toAppendTo, pos);
	}

	private StringBuilder formatArrayBE(byte[] arr, int bitOffset, StringBuilder toAppendTo,
			FieldPosition pos) {

		Objects.checkIndex(bitOffset, arr.length * 8);

		int tlen = arr.length * 8;

		for (int i = 0, m = 0; i < maskBitLen; i++, m++) {
			while (isSkipChar(onMask[m]))
				toAppendTo.append(onMask[m++]);

			int dataIndex = (i + bitOffset);

			byte val = arr[dataIndex / 8];
			long b = ((val >> (7 - (dataIndex) % 8)) & 0x1);

			toAppendTo.append((b == 1)
					? onMask[m]
					: offMask[m]);
		}

		return toAppendTo;
	}

	public StringBuilder formatArray(byte[] arr, StringBuilder toAppendTo, FieldPosition pos) {
		return formatArray(arr, 0, toAppendTo, pos);
	}

	public StringBuilder formatNumber(Number num, int bitOffset, StringBuilder toAppendTo,
			FieldPosition pos) {
		long val = num.longValue();

		for (int i = bitOffset, m = 0; i < maskBitLen; i++, m++) {
			while (isSkipChar(onMask[m]))
				toAppendTo.append(onMask[m++]);

			long b = ((val >> i) & 0x1);

			toAppendTo.append((b == 1)
					? onMask[m]
					: offMask[m]);
		}

		return toAppendTo;
	}

	public StringBuilder formatNumber(Number num, StringBuilder toAppendTo, FieldPosition pos) {
		return formatNumber(num, 0, toAppendTo, pos);
	}

	private boolean isSkipChar(char ch) {
		for (char c : skipChars)
			if (ch == c)
				return true;

		return false;
	}

	private boolean isOffChar(char ch) {
		for (char c : offChars)
			if (ch == c)
				return true;

		return false;
	}

	private boolean isOnChar(char ch) {
		for (char c : onChars)
			if (ch == c)
				return true;

		return false;
	}

	/**
	 * @see java.text.Format#parseObject(java.lang.String, java.text.ParsePosition)
	 */
	@Override
	public Object parseObject(String source, ParsePosition pos) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public BitFormat2 setOffChar(char offChar) {
		this.offChar = offChar;

		return this;
	}

	public BitFormat2 setSkipChars(char... chars) {
		this.skipChars = chars;

		return this;
	}

	@Override
	public String toString() {
		return "BitFormat [onMask=\"%s\", len=%d, offMask=\"%s\"]"
				.formatted(this.formatString, maskBitLen, new String(offMask));
	}
}
