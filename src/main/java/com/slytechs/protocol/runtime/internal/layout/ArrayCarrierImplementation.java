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
package com.slytechs.protocol.runtime.internal.layout;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.ByteBuffer;

import com.slytechs.protocol.runtime.internal.util.ByteArray;

/**
 * The Class ArrayCarrierImplementation.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
@SuppressWarnings("preview")
class ArrayCarrierImplementation implements ArrayCarrier {

	/**
	 * Memory to array.
	 *
	 * @param src    the src
	 * @param srcPos the src pos
	 * @param dst    the dst
	 * @param dstPos the dst pos
	 * @param length the length
	 */
	private static void memoryToArray(MemorySegment src, long srcPos, byte[] dst, int dstPos, int length) {
		for (int i = 0; i < length; i++)
			dst[dstPos + i] = src.get(ValueLayout.JAVA_BYTE, srcPos + i);
	}

	/**
	 * Memory to array.
	 *
	 * @param src    the src
	 * @param srcPos the src pos
	 * @param dst    the dst
	 * @param dstPos the dst pos
	 * @param length the length
	 */
	private static void memoryToArray(MemorySegment src, long srcPos, int[] dst, int dstPos, int length) {
		for (int i = 0; i < length; i++)
			dst[dstPos + i] = src.get(ValueLayout.JAVA_INT, srcPos + i);
	}

	/**
	 * Memory to array.
	 *
	 * @param src    the src
	 * @param srcPos the src pos
	 * @param dst    the dst
	 * @param dstPos the dst pos
	 * @param length the length
	 */
	private static void memoryToArray(MemorySegment src, long srcPos, long[] dst, int dstPos, int length) {
		for (int i = 0; i < length; i++)
			dst[dstPos + i] = src.get(ValueLayout.JAVA_LONG, srcPos + i);
	}

	/**
	 * Memory to array.
	 *
	 * @param src    the src
	 * @param srcPos the src pos
	 * @param dst    the dst
	 * @param dstPos the dst pos
	 * @param length the length
	 */
	private static void memoryToArray(MemorySegment src, long srcPos, short[] dst, int dstPos, int length) {
		for (int i = 0; i < length; i++)
			dst[dstPos + i] = src.get(ValueLayout.JAVA_SHORT, srcPos + i);
	}

	/**
	 * Array to memory.
	 *
	 * @param src    the src
	 * @param srcPos the src pos
	 * @param dst    the dst
	 * @param dstPos the dst pos
	 * @param length the length
	 */
	private void arrayToMemory(byte[] src, int srcPos, MemorySegment dst, long dstPos, int length) {
		for (int i = 0; i < length; i++)
			dst.set(ValueLayout.JAVA_BYTE, dstPos + i, src[srcPos + i]);
	}

	/**
	 * Array to memory.
	 *
	 * @param src    the src
	 * @param srcPos the src pos
	 * @param dst    the dst
	 * @param dstPos the dst pos
	 * @param length the length
	 */
	private void arrayToMemory(short[] src, int srcPos, MemorySegment dst, long dstPos, int length) {
		for (int i = 0; i < length; i++)
			dst.set(ValueLayout.JAVA_SHORT, dstPos + i, src[srcPos + i]);
	}

	/**
	 * Array to memory.
	 *
	 * @param src    the src
	 * @param srcPos the src pos
	 * @param dst    the dst
	 * @param dstPos the dst pos
	 * @param length the length
	 */
	private void arrayToMemory(int[] src, int srcPos, MemorySegment dst, long dstPos, int length) {
		for (int i = 0; i < length; i++)
			dst.set(ValueLayout.JAVA_INT, dstPos + i, src[srcPos + i]);
	}

	/**
	 * Array to memory.
	 *
	 * @param src    the src
	 * @param srcPos the src pos
	 * @param dst    the dst
	 * @param dstPos the dst pos
	 * @param length the length
	 */
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
	 * @see com.slytechs.protocol.runtime.internal.layout.ArrayCarrier#getByteArrayAtOffset(byte[], int, int,
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
	 * @see com.slytechs.protocol.runtime.internal.layout.ArrayCarrier#getIntArrayAtOffset(int[], int, int,
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
	 * @see com.slytechs.protocol.runtime.internal.layout.ArrayCarrier#getLongArrayAtOffset(long[], int, int,
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
	 * @see com.slytechs.protocol.runtime.internal.layout.ArrayCarrier#getShortArrayAtOffset(short[], int, int,
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
	 * @see com.slytechs.protocol.runtime.internal.layout.ArrayCarrier#setByteArrayAtOffset(byte[], int, int,
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
	 * @see com.slytechs.protocol.runtime.internal.layout.ArrayCarrier#setIntArrayAtOffset(int[], int, int,
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
	 * @see com.slytechs.protocol.runtime.internal.layout.ArrayCarrier#setLongArrayAtOffset(long[], int, int,
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
	 * @see com.slytechs.protocol.runtime.internal.layout.ArrayCarrier#setShortArrayAtOffset(short[], int, int,
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
	 * @see com.slytechs.protocol.runtime.internal.layout.ArrayCarrier#wrapAtOffset(byte[], long, long)
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
	 * @see com.slytechs.protocol.runtime.internal.layout.ArrayCarrier#wrapAtOffset(org.jnet.buffer.ByteArray,
	 *      long, long)
	 */
	@Override
	public ByteArray wrapAtOffset(ByteArray data, long byteOffset, long byteSize) {
		return ByteArray.wrap(data.array(), (int) (data.arrayOffset() + byteOffset), (int) byteSize);
	}

}
