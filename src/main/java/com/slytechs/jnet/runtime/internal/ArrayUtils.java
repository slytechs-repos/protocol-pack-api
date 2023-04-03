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
package com.slytechs.jnet.runtime.internal;

import java.nio.ByteOrder;
import java.util.Random;

/**
 * The Class ArrayUtils.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public class ArrayUtils {

	/** The Constant RANDOM. */
	private static final Random RANDOM = new Random(System.currentTimeMillis());

	/**
	 * Gets the byte.
	 *
	 * @param heap       the heap
	 * @param byteOffset the byte offset
	 * @return the byte
	 */
	public static byte getByte(byte[] heap, int byteOffset) {
		if ((byteOffset < 0) || (byteOffset + 1) > heap.length) {
			throw new IndexOutOfBoundsException();
		}

		return heap[byteOffset];
	}

	/**
	 * Gets the byte.
	 *
	 * @param array      the array
	 * @param byteOffset the byte offset
	 * @return the byte
	 */
	public static byte getByte(int[] array, int byteOffset) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	/**
	 * Gets the byte.
	 *
	 * @param array      the array
	 * @param byteOffset the byte offset
	 * @return the byte
	 */
	public static byte getByte(long[] array, int byteOffset) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	/**
	 * Gets the byte.
	 *
	 * @param array      the array
	 * @param byteOffset the byte offset
	 * @return the byte
	 */
	public static byte getByte(short[] array, int byteOffset) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	/**
	 * Gets the char.
	 *
	 * @param heap       the heap
	 * @param byteOffset the byte offset
	 * @param order      the order
	 * @return the char
	 */
	public static char getChar(byte[] heap, int byteOffset, ByteOrder order) {
		return (char) getShort(heap, byteOffset, order);
	}

	/**
	 * Gets the double.
	 *
	 * @param heap       the heap
	 * @param byteOffset the byte offset
	 * @param order      the order
	 * @return the double
	 */
	public static double getDouble(byte[] heap, int byteOffset, ByteOrder order) {
		return Double.longBitsToDouble(getLong(heap, byteOffset, order));
	}

	/**
	 * Gets the float.
	 *
	 * @param heap       the heap
	 * @param byteOffset the byte offset
	 * @param order      the order
	 * @return the float
	 */
	public static float getFloat(byte[] heap, int byteOffset, ByteOrder order) {
		return Float.intBitsToFloat(getInt(heap, byteOffset, order));
	}

	/**
	 * Gets the int.
	 *
	 * @param heap       the heap
	 * @param byteOffset the byte offset
	 * @param big        the big
	 * @return the int
	 */
	public static int getInt(byte[] heap, int byteOffset, boolean big) {
		if ((byteOffset < 0) || (byteOffset + 4) > heap.length)
			throw new IndexOutOfBoundsException("" + byteOffset);

		return big
				? (((heap[byteOffset + 0] & 0xff) << 24) + ((heap[byteOffset + 1] & 0xff) << 16)
						+ ((heap[byteOffset + 2] & 0xff) << 8) + ((heap[byteOffset + 3] & 0xff) << 0))

				: (((heap[byteOffset + 3] & 0xff) << 24) + ((heap[byteOffset + 2] & 0xff) << 16)
						+ ((heap[byteOffset + 1] & 0xff) << 8) + ((heap[byteOffset + 0] & 0xff) << 0));
	}

	/**
	 * Gets the int.
	 *
	 * @param heap       the heap
	 * @param byteOffset the byte offset
	 * @param order      the order
	 * @return the int
	 */
	public static int getInt(byte[] heap, int byteOffset, ByteOrder order) {
		assert order != null;
		return getInt(heap, byteOffset, order == ByteOrder.BIG_ENDIAN);
	}

	/**
	 * Gets the int.
	 *
	 * @param array      the array
	 * @param byteOffset the byte offset
	 * @param order      the order
	 * @return the int
	 */
	public static int getInt(int[] array, int byteOffset, ByteOrder order) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	/**
	 * Gets the int.
	 *
	 * @param array      the array
	 * @param byteOffset the byte offset
	 * @param order      the order
	 * @return the int
	 */
	public static int getInt(long[] array, int byteOffset, ByteOrder order) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	/**
	 * Gets the int.
	 *
	 * @param array      the array
	 * @param byteOffset the byte offset
	 * @param order      the order
	 * @return the int
	 */
	public static int getInt(short[] array, int byteOffset, ByteOrder order) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	/**
	 * Gets the long.
	 *
	 * @param heap       the heap
	 * @param byteOffset the byte offset
	 * @param big        the big
	 * @return the long
	 */
	public static long getLong(byte[] heap, int byteOffset, boolean big) {
		if ((byteOffset < 0) || (byteOffset + 8) > heap.length)
			throw new IndexOutOfBoundsException();

		return big
				? (((long) heap[byteOffset + 0] << 56) + ((long) (heap[byteOffset + 1] & 0xff) << 48)
						+ ((long) (heap[byteOffset + 2] & 0xff) << 40) + ((long) (heap[byteOffset + 3] & 0xff) << 32)
						+ ((long) (heap[byteOffset + 4] & 0xff) << 24) + ((heap[byteOffset + 5] & 0xff) << 16)
						+ ((heap[byteOffset + 6] & 0xff) << 8) + ((heap[byteOffset + 7] & 0xff) << 0))

				: (((long) heap[byteOffset + 7] << 56) + ((long) (heap[byteOffset + 6] & 0xff) << 48)
						+ ((long) (heap[byteOffset + 5] & 0xff) << 40) + ((long) (heap[byteOffset + 4] & 0xff) << 32)
						+ ((long) (heap[byteOffset + 3] & 0xff) << 24) + ((heap[byteOffset + 2] & 0xff) << 16)
						+ ((heap[byteOffset + 1] & 0xff) << 8) + ((heap[byteOffset + 0] & 0xff) << 0));
	}

	/**
	 * Gets the long.
	 *
	 * @param heap       the heap
	 * @param byteOffset the byte offset
	 * @param order      the order
	 * @return the long
	 */
	public static long getLong(byte[] heap, int byteOffset, ByteOrder order) {
		assert order != null;
		return getLong(heap, byteOffset, order == ByteOrder.BIG_ENDIAN);

	}

	/**
	 * Gets the long.
	 *
	 * @param d          the d
	 * @param byteOffset the byte offset
	 * @param order      the order
	 * @return the long
	 */
	public static long getLong(int[] d, int byteOffset, ByteOrder order) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	/**
	 * Gets the long.
	 *
	 * @param d          the d
	 * @param byteOffset the byte offset
	 * @param order      the order
	 * @return the long
	 */
	public static long getLong(long[] d, int byteOffset, ByteOrder order) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	/**
	 * Gets the long.
	 *
	 * @param d          the d
	 * @param byteOffset the byte offset
	 * @param order      the order
	 * @return the long
	 */
	public static long getLong(short[] d, int byteOffset, ByteOrder order) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	/**
	 * Gets the short.
	 *
	 * @param heap       the heap
	 * @param byteOffset the byte offset
	 * @param big        the big
	 * @return the short
	 */
	public static short getShort(byte[] heap, int byteOffset, boolean big) {
		if ((byteOffset < 0) || (byteOffset + 2) > heap.length) {
			throw new IndexOutOfBoundsException();
		}

		return big ? (short) (((heap[byteOffset + 0] & 0xff) << 8) + ((heap[byteOffset + 1] & 0xff) << 0))

				: (short) (((heap[byteOffset + 1] & 0xff) << 8) + ((heap[byteOffset + 0] & 0xff) << 0));
	}

	/**
	 * Gets the short.
	 *
	 * @param heap       the heap
	 * @param byteOffset the byte offset
	 * @param order      the order
	 * @return the short
	 */
	public static short getShort(byte[] heap, int byteOffset, ByteOrder order) {
		assert order != null;
		return getShort(heap, byteOffset, order == ByteOrder.BIG_ENDIAN);
	}

	/**
	 * Gets the short.
	 *
	 * @param array      the array
	 * @param byteOffset the byte offset
	 * @param order      the order
	 * @return the short
	 */
	public static short getShort(int[] array, int byteOffset, ByteOrder order) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	/**
	 * Gets the short.
	 *
	 * @param array      the array
	 * @param byteOffset the byte offset
	 * @param order      the order
	 * @return the short
	 */
	public static short getShort(long[] array, int byteOffset, ByteOrder order) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	/**
	 * Gets the short.
	 *
	 * @param array      the array
	 * @param byteOffset the byte offset
	 * @param order      the order
	 * @return the short
	 */
	public static short getShort(short[] array, int byteOffset, ByteOrder order) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	/**
	 * Put byte.
	 *
	 * @param heap       the heap
	 * @param byteOffset the byte offset
	 * @param v          the v
	 * @return the int
	 */
	public static int putByte(byte[] heap, int byteOffset, byte v) {
		heap[byteOffset] = v;

		return 2;
	}

	/**
	 * Put byte.
	 *
	 * @param array      the array
	 * @param byteOffset the byte offset
	 * @param value      the value
	 * @return the byte
	 */
	public static byte putByte(int[] array, int byteOffset, byte value) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	/**
	 * Put byte.
	 *
	 * @param array      the array
	 * @param byteOffset the byte offset
	 * @param value      the value
	 * @return the byte
	 */
	public static byte putByte(long[] array, int byteOffset, byte value) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	/**
	 * Put byte.
	 *
	 * @param array      the array
	 * @param byteOffset the byte offset
	 * @param value      the value
	 * @return the byte
	 */
	public static byte putByte(short[] array, int byteOffset, byte value) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	/**
	 * Put char.
	 *
	 * @param heap       the heap
	 * @param byteOffset the byte offset
	 * @param v          the v
	 * @param order      the order
	 * @return the int
	 */
	public static int putChar(byte[] heap, int byteOffset, char v, ByteOrder order) {
		return putShort(heap, byteOffset, (short) v, order);
	}

	/**
	 * Put double.
	 *
	 * @param heap       the heap
	 * @param byteOffset the byte offset
	 * @param v          the v
	 * @param order      the order
	 * @return the int
	 */
	public static int putDouble(byte[] heap, int byteOffset, double v, ByteOrder order) {
		return putLong(heap, byteOffset, Double.doubleToLongBits(v), order);
	}

	/**
	 * Put float.
	 *
	 * @param heap       the heap
	 * @param byteOffset the byte offset
	 * @param v          the v
	 * @param order      the order
	 * @return the int
	 */
	public static int putFloat(byte[] heap, int byteOffset, float v, ByteOrder order) {
		return putInt(heap, byteOffset, Float.floatToIntBits(v), order);
	}

	/**
	 * Put int.
	 *
	 * @param heap       the heap
	 * @param byteOffset the byte offset
	 * @param v          the v
	 * @param big        the big
	 * @return the int
	 */
	public static int putInt(byte[] heap, int byteOffset, int v, boolean big) {
		if (big) {

			heap[byteOffset + 0] = (byte) (v >>> 24);
			heap[byteOffset + 1] = (byte) (v >>> 16);
			heap[byteOffset + 2] = (byte) (v >>> 8);
			heap[byteOffset + 3] = (byte) (v >>> 0);

		} else {

			heap[byteOffset + 0] = (byte) (v >>> 0);
			heap[byteOffset + 1] = (byte) (v >>> 8);
			heap[byteOffset + 2] = (byte) (v >>> 16);
			heap[byteOffset + 3] = (byte) (v >>> 24);

		}

		return 4;
	}

	/**
	 * Put int.
	 *
	 * @param heap       the heap
	 * @param byteOffset the byte offset
	 * @param v          the v
	 * @param order      the order
	 * @return the int
	 */
	public static int putInt(byte[] heap, int byteOffset, int v, ByteOrder order) {
		assert order != null;

		return putInt(heap, byteOffset, v, order == ByteOrder.BIG_ENDIAN);
	}

	/**
	 * Put int.
	 *
	 * @param array      the array
	 * @param byteOffset the byte offset
	 * @param value      the value
	 * @param order      the order
	 * @return the int
	 */
	public static int putInt(int[] array, int byteOffset, int value, ByteOrder order) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	/**
	 * Put int.
	 *
	 * @param array      the array
	 * @param byteOffset the byte offset
	 * @param value      the value
	 * @param order      the order
	 * @return the int
	 */
	public static int putInt(long[] array, int byteOffset, int value, ByteOrder order) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	/**
	 * Put int.
	 *
	 * @param array      the array
	 * @param byteOffset the byte offset
	 * @param value      the value
	 * @param order      the order
	 * @return the int
	 */
	public static int putInt(short[] array, int byteOffset, int value, ByteOrder order) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	/**
	 * Put long.
	 *
	 * @param heap       the heap
	 * @param byteOffset the byte offset
	 * @param v          the v
	 * @param big        the big
	 * @return the int
	 */
	public static int putLong(byte[] heap, int byteOffset, long v, boolean big) {
		if (big) {

			heap[byteOffset + 0] = (byte) (v >>> 56);
			heap[byteOffset + 1] = (byte) (v >>> 48);
			heap[byteOffset + 2] = (byte) (v >>> 40);
			heap[byteOffset + 3] = (byte) (v >>> 32);
			heap[byteOffset + 4] = (byte) (v >>> 24);
			heap[byteOffset + 5] = (byte) (v >>> 16);
			heap[byteOffset + 6] = (byte) (v >>> 8);
			heap[byteOffset + 7] = (byte) (v >>> 0);

		} else {

			heap[byteOffset + 0] = (byte) (v >>> 0);
			heap[byteOffset + 1] = (byte) (v >>> 8);
			heap[byteOffset + 2] = (byte) (v >>> 16);
			heap[byteOffset + 3] = (byte) (v >>> 24);
			heap[byteOffset + 4] = (byte) (v >>> 32);
			heap[byteOffset + 5] = (byte) (v >>> 40);
			heap[byteOffset + 6] = (byte) (v >>> 48);
			heap[byteOffset + 7] = (byte) (v >>> 56);

		}

		return 8;
	}

	/**
	 * Put long.
	 *
	 * @param heap       the heap
	 * @param byteOffset the byte offset
	 * @param v          the v
	 * @param order      the order
	 * @return the int
	 */
	public static int putLong(byte[] heap, int byteOffset, long v, ByteOrder order) {
		assert order != null;

		return putLong(heap, byteOffset, v, order == ByteOrder.BIG_ENDIAN);

	}

	/**
	 * Put long.
	 *
	 * @param array      the array
	 * @param byteOffset the byte offset
	 * @param value      the value
	 * @param order      the order
	 * @return the long
	 */
	public static long putLong(int[] array, int byteOffset, long value, ByteOrder order) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	/**
	 * Put long.
	 *
	 * @param array      the array
	 * @param byteOffset the byte offset
	 * @param value      the value
	 * @param order      the order
	 * @return the long
	 */
	public static long putLong(long[] array, int byteOffset, long value, ByteOrder order) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	/**
	 * Put long.
	 *
	 * @param array      the array
	 * @param byteOffset the byte offset
	 * @param value      the value
	 * @param order      the order
	 * @return the long
	 */
	public static long putLong(short[] array, int byteOffset, long value, ByteOrder order) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	/**
	 * Put short.
	 *
	 * @param heap       the heap
	 * @param byteOffset the byte offset
	 * @param v          the v
	 * @param big        the big
	 * @return the int
	 */
	public static int putShort(byte[] heap, int byteOffset, short v, boolean big) {
		if (big) {

			heap[byteOffset + 0] = (byte) (v >>> 8);
			heap[byteOffset + 1] = (byte) (v >>> 0);

		} else {

			heap[byteOffset + 0] = (byte) (v >>> 0);
			heap[byteOffset + 1] = (byte) (v >>> 8);

		}

		return 2;
	}

	/**
	 * Put short.
	 *
	 * @param heap       the heap
	 * @param byteOffset the byte offset
	 * @param v          the v
	 * @param order      the order
	 * @return the int
	 */
	public static int putShort(byte[] heap, int byteOffset, short v, ByteOrder order) {
		assert order != null;

		return putShort(heap, byteOffset, v, order == ByteOrder.BIG_ENDIAN);
	}

	/**
	 * Put short.
	 *
	 * @param array      the array
	 * @param byteOffset the byte offset
	 * @param value      the value
	 * @param order      the order
	 * @return the short
	 */
	public static short putShort(int[] array, int byteOffset, short value, ByteOrder order) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	/**
	 * Put short.
	 *
	 * @param array      the array
	 * @param byteOffset the byte offset
	 * @param value      the value
	 * @param order      the order
	 * @return the short
	 */
	public static short putShort(long[] array, int byteOffset, short value, ByteOrder order) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	/**
	 * Put short.
	 *
	 * @param array      the array
	 * @param byteOffset the byte offset
	 * @param value      the value
	 * @param order      the order
	 * @return the short
	 */
	public static short putShort(short[] array, int byteOffset, short value, ByteOrder order) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	/**
	 * Random byte array.
	 *
	 * @param length the length
	 * @return the byte[]
	 */
	public static byte[] randomByteArray(int length) {
		byte[] array = new byte[length];
		RANDOM.nextBytes(array);

		return array;
	}

	/**
	 * Strip array slice.
	 *
	 * @param id the id
	 * @return the string
	 */
	public static String stripArraySlice(String id) {
		String noSlice = id.replaceFirst("(.*)(\\[.*\\])", "$1");

		return noSlice;
	}

	/**
	 * Hash code.
	 *
	 * @param a the a
	 * @return the int
	 */
	public static int hashCode(byte[] a) {
		return hashCode(a, 0, a.length);
	}

	/**
	 * Hash code.
	 *
	 * @param a      the a
	 * @param offset the offset
	 * @param length the length
	 * @return the int
	 */
	public static int hashCode(byte[] a, int offset, int length) {
		if (a == null)
			return 0;

		int result = 1;
		for (int i = 0; i < length; i++) {
			byte element = a[i + offset];
			result = 31 * result + element;
		}

		return result;
	}

	/**
	 * Hash code with netmask.
	 *
	 * @param a    the a
	 * @param mask the mask
	 * @return the int
	 */
	public static int hashCodeWithNetmask(byte[] a, int mask) {
		return hashCodeWithNetmask(a, 0, a.length, mask);
	}

	/**
	 * Hash code with netmask.
	 *
	 * @param a      the a
	 * @param offset the offset
	 * @param length the length
	 * @param mask   the mask
	 * @return the int
	 */
	public static int hashCodeWithNetmask(byte[] a, int offset, int length, int mask) {
		if (a == null || length < (mask >> 3))
			return 0;

		int result = 1;
		for (int i = 0; (i < length) && (mask > 0); i++) {
			byte element = a[i + offset];

			if (mask >= 8) {
				result = 31 * result + element;
				mask -= 8;
			} else {
				element &= ~((1 << mask) - 1) & 0xFF;
				result = 31 * result + element;
				mask = 0;

			}
		}

		return result;
	}

	/**
	 * Equals.
	 *
	 * @param a1   the a 1
	 * @param a2   the a 2
	 * @param mask the mask
	 * @return true, if successful
	 */
	public static boolean equals(byte[] a1, byte[] a2, int mask) {
		if (((a1.length << 3) < mask) || ((a2.length << 3) < mask))
			return false;

		for (int i = 0; mask > 0; i++) {
			if (mask < 8) {
				int m = ~(1 << (mask) - 1) & 0xFF;

				return (a1[i] & m) == (a2[i] & m);

			} else {
				if (a1[i] != a2[i])
					return false;

				mask -= 8;
			}
		}

		return true;
	}

}
