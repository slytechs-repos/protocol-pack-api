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
package com.slytechs.jnet.jnetruntime.internal.util;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * The Class ByteArraySlice.
 */
public class ByteArraySlice {

	/**
	 * Begining.
	 *
	 * @param length the length
	 * @return the byte array slice
	 */
	public static ByteArraySlice begining(int length) {
		return new ByteArraySlice(0, length);
	}

	/**
	 * Between.
	 *
	 * @param offset the offset
	 * @param end    the end
	 * @return the byte array slice
	 */
	public static ByteArraySlice between(int offset, int end) {
		return new ByteArraySlice(offset, end - offset + 1);
	}

	/**
	 * Byte at.
	 *
	 * @param offset the offset
	 * @return the byte array slice
	 */
	public static ByteArraySlice byteAt(int offset) {
		return new ByteArraySlice(offset, 1);
	}

	/**
	 * From.
	 *
	 * @param offset the offset
	 * @return the byte array slice
	 */
	public static ByteArraySlice from(int offset) {
		return new ByteArraySlice(offset, 0);
	}

	/**
	 * Range.
	 *
	 * @param offset the offset
	 * @param length the length
	 * @return the byte array slice
	 */
	public static ByteArraySlice range(int offset, int length) {
		return new ByteArraySlice(offset, length);
	}

	/** The offset. */
	private final int offset;
	
	/** The length. */
	private final int length;
	
	/** The slices. */
	private final ByteArraySlice[] slices;

	/**
	 * Instantiates a new byte array slice.
	 *
	 * @param offset the offset
	 * @param length the length
	 */
	private ByteArraySlice(int offset, int length) {
		this.offset = offset;
		this.length = length;
		this.slices = null;
	}

	/**
	 * Instantiates a new byte array slice.
	 *
	 * @param slices the slices
	 */
	public ByteArraySlice(ByteArraySlice... slices) {
		int offset = Integer.MAX_VALUE;
		int length = 0;

		for (int i = 0; i < slices.length; i++) {
			ByteArraySlice slice = slices[i];
			if (slice.isCompound())
				throw new IllegalArgumentException("recursive compound slices not supported " + slice);

			if (slice.offset < offset)
				offset = slice.offset;

			length += slice.length;
		}

		this.slices = slices;
		this.offset = offset;
		this.length = length;
	}

	/**
	 * Length.
	 *
	 * @param totalLen the total len
	 * @return the int
	 */
	public int length(int totalLen) {
		if (slices == null)
			return (length != 0) ? length : (totalLen - offset(totalLen));

		int len = 0;
		for (ByteArraySlice slice : slices)
			len += slice.length(totalLen);

		return len;
	}

	/**
	 * Offset.
	 *
	 * @param totalLen the total len
	 * @return the int
	 */
	public int offset(int totalLen) {
		if (slices == null)
			return (offset >= 0) ? offset : (totalLen + offset);

		int start = Integer.MAX_VALUE;
		for (ByteArraySlice slice : slices)
			if (slice.offset(totalLen) < start)
				start = slice.offset(totalLen);

		return start;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (offset == 0)
			return ":" + length;

		else if (length == 0)
			return "" + offset + ":";

		else if (length == 1)
			return "" + offset;

		else
			return "" + offset + ":" + length;
	}

	/**
	 * To string.
	 *
	 * @param totalLen the total len
	 * @return the string
	 */
	public String toString(int totalLen) {
		if (slices == null)
			return "Slice [" + offset(totalLen) + ":" + length(totalLen) + "]";

		return "Slice [" + offset(totalLen) + ":" + length(totalLen) + " " + Arrays.asList(slices).stream()
				.map(s -> s.toStringNoBrackets(totalLen)).collect(Collectors.joining(",", "-> [", "]")) + "]";
	}

	/**
	 * To string no brackets.
	 *
	 * @param totalLen the total len
	 * @return the string
	 */
	private String toStringNoBrackets(int totalLen) {
		return "" + offset(totalLen) + ":" + length(totalLen) + "";
	}

	/**
	 * Checks if is compound.
	 *
	 * @return true, if is compound
	 */
	public boolean isCompound() { return slices != null; }

	/**
	 * Slices.
	 *
	 * @return the byte array slice[]
	 */
	public ByteArraySlice[] slices() {
		return slices;
	}

	/**
	 * Slice array.
	 *
	 * @param src the src
	 * @return the byte[]
	 */
	public byte[] sliceArray(ByteBuffer src) {
		byte[] dst = new byte[length(src.capacity())];

		if (isCompound()) {
			int dstOff = 0;
			for (ByteArraySlice subSlice : slices) {
				int len = subSlice.length;
				int off = subSlice.offset;
				subSlice.sliceArray(src, dst, dstOff);
				
				dstOff += len;
			}

		} else
			this.sliceArray(src, dst, 0);

		return dst;
	}

	/**
	 * Slice array.
	 *
	 * @param src       the src
	 * @param dst       the dst
	 * @param dstOffset the dst offset
	 * @return the byte[]
	 */
	public byte[] sliceArray(ByteBuffer src, byte[] dst, int dstOffset) {

		int len = length(src.capacity());
		int off = offset(src.capacity());

		System.out.printf("ByteArraySlice::sliceArray off=%d len=%d%n", off, len);
		src.get(off, dst, dstOffset, len);

		return dst;
	}

}
