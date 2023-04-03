/*
 * MIT License
 * 
 * Copyright (c) 2020 Sly Technologies Inc.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.slytechs.jnet.runtime.internal.layout;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.ByteBuffer;

import com.slytechs.jnet.runtime.util.ByteArray;

/**
 * The Class ArrayCarrierImplementation.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
@SuppressWarnings("preview")
class ArrayCarrierImplementation implements ArrayCarrier {

	private static void memoryToArray(MemorySegment src, long srcPos, byte[] dst, int dstPos, int length) {
		for (int i = 0; i < length; i++)
			dst[dstPos + i] = src.get(ValueLayout.JAVA_BYTE, srcPos + i);
	}

	private static void memoryToArray(MemorySegment src, long srcPos, int[] dst, int dstPos, int length) {
		for (int i = 0; i < length; i++)
			dst[dstPos + i] = src.get(ValueLayout.JAVA_INT, srcPos + i);
	}

	private static void memoryToArray(MemorySegment src, long srcPos, long[] dst, int dstPos, int length) {
		for (int i = 0; i < length; i++)
			dst[dstPos + i] = src.get(ValueLayout.JAVA_LONG, srcPos + i);
	}

	private static void memoryToArray(MemorySegment src, long srcPos, short[] dst, int dstPos, int length) {
		for (int i = 0; i < length; i++)
			dst[dstPos + i] = src.get(ValueLayout.JAVA_SHORT, srcPos + i);
	}

	private void arrayToMemory(byte[] src, int srcPos, MemorySegment dst, long dstPos, int length) {
		for (int i = 0; i < length; i++)
			dst.set(ValueLayout.JAVA_BYTE, dstPos + i, src[srcPos + i]);
	}

	private void arrayToMemory(short[] src, int srcPos, MemorySegment dst, long dstPos, int length) {
		for (int i = 0; i < length; i++)
			dst.set(ValueLayout.JAVA_SHORT, dstPos + i, src[srcPos + i]);
	}

	private void arrayToMemory(int[] src, int srcPos, MemorySegment dst, long dstPos, int length) {
		for (int i = 0; i < length; i++)
			dst.set(ValueLayout.JAVA_INT, dstPos + i, src[srcPos + i]);
	}

	private void arrayToMemory(long[] src, int srcPos, MemorySegment dst, long dstPos, int length) {
		for (int i = 0; i < length; i++)
			dst.set(ValueLayout.JAVA_LONG, dstPos + i, src[srcPos + i]);
	}

	/**
	 * Gets the byte array at offset.
	 *
	 * @param array      the array
	 * @param offset     the offset
	 * @param length     the length
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @return the byte array at offset
	 * @see com.slytechs.jnet.runtime.internal.layout.ArrayCarrier#getByteArrayAtOffset(byte[], int, int,
	 *      java.lang.Object, long)
	 */
	@Override
	public byte[] getByteArrayAtOffset(byte[] array, int offset, int length, Object data, long byteOffset) {
		switch (data) {
		case byte[] d -> System.arraycopy(d, (int) byteOffset, array, offset, length);
		case ByteArray b -> System.arraycopy(b.array(), (int) (b.arrayOffset() + byteOffset), array, offset, length);
		case MemorySegment seg -> memoryToArray(seg, byteOffset, array, offset, length);
		case ByteBuffer b -> b.put(array, offset, length);
		default -> throw new IllegalArgumentException("unsupported data type " + data.getClass());
		}

		return array;
	}

	/**
	 * Gets the int array at offset.
	 *
	 * @param array      the array
	 * @param offset     the offset
	 * @param length     the length
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @return the int array at offset
	 * @see com.slytechs.jnet.runtime.internal.layout.ArrayCarrier#getIntArrayAtOffset(int[], int, int,
	 *      java.lang.Object, long)
	 */
	@Override
	public int[] getIntArrayAtOffset(int[] array, int offset, int length, Object data, long byteOffset) {
		switch (data) {
		case byte[] d -> System.arraycopy(d, (int) byteOffset, array, offset, length);
		case MemorySegment seg -> memoryToArray(seg, byteOffset, array, offset, length);

		default -> throw new IllegalArgumentException("unsupported data type " + data.getClass());
		}

		return array;
	}

	/**
	 * Gets the long array at offset.
	 *
	 * @param array      the array
	 * @param offset     the offset
	 * @param length     the length
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @return the long array at offset
	 * @see com.slytechs.jnet.runtime.internal.layout.ArrayCarrier#getLongArrayAtOffset(long[], int, int,
	 *      java.lang.Object, long)
	 */
	@Override
	public long[] getLongArrayAtOffset(long[] array, int offset, int length, Object data, long byteOffset) {
		switch (data) {
		case byte[] d -> System.arraycopy(d, (int) byteOffset, array, offset, length);
		case MemorySegment seg -> memoryToArray(seg, byteOffset, array, offset, length);

		default -> throw new IllegalArgumentException("unsupported data type " + data.getClass());
		}

		return array;
	}

	/**
	 * Gets the short array at offset.
	 *
	 * @param array      the array
	 * @param offset     the offset
	 * @param length     the length
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @return the short array at offset
	 * @see com.slytechs.jnet.runtime.internal.layout.ArrayCarrier#getShortArrayAtOffset(short[], int, int,
	 *      java.lang.Object, long)
	 */
	@Override
	public short[] getShortArrayAtOffset(short[] array, int offset, int length, Object data, long byteOffset) {
		switch (data) {
		case byte[] d -> System.arraycopy(d, (int) byteOffset, array, offset, length);
		case MemorySegment seg -> memoryToArray(seg, byteOffset, array, offset, length);

		default -> throw new IllegalArgumentException("unsupported data type " + data.getClass());
		}

		return array;
	}

	/**
	 * Sets the byte array at offset.
	 *
	 * @param array      the array
	 * @param offset     the offset
	 * @param length     the length
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @return the byte[]
	 * @see com.slytechs.jnet.runtime.internal.layout.ArrayCarrier#setByteArrayAtOffset(byte[], int, int,
	 *      java.lang.Object, long)
	 */
	@Override
	public byte[] setByteArrayAtOffset(byte[] array, int offset, int length, Object data, long byteOffset) {
		switch (data) {
		case byte[] d -> System.arraycopy(array, offset, d, (int) byteOffset, length);
		case MemorySegment seg -> arrayToMemory(array, offset, seg, byteOffset, length);

		default -> throw new IllegalArgumentException("unsupported data type " + data.getClass());
		}

		return array;
	}

	/**
	 * Sets the int array at offset.
	 *
	 * @param array      the array
	 * @param offset     the offset
	 * @param length     the length
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @return the int[]
	 * @see com.slytechs.jnet.runtime.internal.layout.ArrayCarrier#setIntArrayAtOffset(int[], int, int,
	 *      java.lang.Object, long)
	 */
	@Override
	public int[] setIntArrayAtOffset(int[] array, int offset, int length, Object data, long byteOffset) {
		switch (data) {
		case byte[] d -> System.arraycopy(array, offset, d, (int) byteOffset, length);
		case MemorySegment seg -> arrayToMemory(array, offset, seg, byteOffset, length);

		default -> throw new IllegalArgumentException("unsupported data type " + data.getClass());
		}

		return array;
	}

	/**
	 * Sets the long array at offset.
	 *
	 * @param array      the array
	 * @param offset     the offset
	 * @param length     the length
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @return the long[]
	 * @see com.slytechs.jnet.runtime.internal.layout.ArrayCarrier#setLongArrayAtOffset(long[], int, int,
	 *      java.lang.Object, long)
	 */
	@Override
	public long[] setLongArrayAtOffset(long[] array, int offset, int length, Object data, long byteOffset) {
		switch (data) {
		case byte[] d -> System.arraycopy(array, offset, d, (int) byteOffset, length);
		case MemorySegment seg -> arrayToMemory(array, offset, seg, byteOffset, length);

		default -> throw new IllegalArgumentException("unsupported data type " + data.getClass());
		}

		return array;
	}

	/**
	 * Sets the short array at offset.
	 *
	 * @param array      the array
	 * @param offset     the offset
	 * @param length     the length
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @return the short[]
	 * @see com.slytechs.jnet.runtime.internal.layout.ArrayCarrier#setShortArrayAtOffset(short[], int, int,
	 *      java.lang.Object, long)
	 */
	@Override
	public short[] setShortArrayAtOffset(short[] array, int offset, int length, Object data, long byteOffset) {
		switch (data) {
		case byte[] d -> System.arraycopy(array, offset, d, (int) byteOffset, length);
		case MemorySegment seg -> arrayToMemory(array, offset, seg, byteOffset, length);

		default -> throw new IllegalArgumentException("unsupported data type " + data.getClass());
		}

		return array;
	}

	/**
	 * Wrap at offset.
	 *
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @param byteSize   the byte size
	 * @return the byte array
	 * @see com.slytechs.jnet.runtime.internal.layout.ArrayCarrier#wrapAtOffset(byte[], long, long)
	 */
	@Override
	public ByteArray wrapAtOffset(byte[] data, long byteOffset, long byteSize) {
		return ByteArray.wrap(data, (int) byteOffset, (int) byteSize);
	}

	/**
	 * Wrap at offset.
	 *
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @param byteSize   the byte size
	 * @return the byte array
	 * @see com.slytechs.jnet.runtime.internal.layout.ArrayCarrier#wrapAtOffset(org.jnet.buffer.ByteArray,
	 *      long, long)
	 */
	@Override
	public ByteArray wrapAtOffset(ByteArray data, long byteOffset, long byteSize) {
		return ByteArray.wrap(data.array(), (int) (data.arrayOffset() + byteOffset), (int) byteSize);
	}

}
