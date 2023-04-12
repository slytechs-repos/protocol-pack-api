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

import com.slytechs.protocol.runtime.internal.util.ByteArray;

/**
 * The Class ArrayFieldImplementation.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
class ArrayFieldImplementation implements ArrayField {

	/** The carrier. */
	private final ArrayCarrier carrier;

	/** The context. */
	private final ArrayFieldContext context;

	/**
	 * Instantiates a new array field implementation.
	 *
	 * @param context the context
	 */
	public ArrayFieldImplementation(ArrayFieldContext context) {
		this.context = context;
		this.carrier = new ArrayCarrierImplementation();
	}

	/**
	 * Bit offset.
	 *
	 * @return the long
	 * @see com.slytechs.protocol.runtime.internal.layout.BinaryField#bitOffset()
	 */
	@Override
	public long bitOffset() {
		return context.carrierOffset(0);
	}

	/**
	 * Bit offset.
	 *
	 * @param sequences the sequences
	 * @return the long
	 * @see com.slytechs.protocol.runtime.internal.layout.BinaryField#bitOffset(long[])
	 */
	@Override
	public long bitOffset(long... sequences) {
		return context.carrierOffset(context.strideOffset(sequences));
	}

	/**
	 * Gets the byte array.
	 *
	 * @param array  the array
	 * @param offset the offset
	 * @param length the length
	 * @param data   the data
	 * @return the byte array
	 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#getByteArray(byte[], int, int,
	 *      java.lang.Object)
	 */
	@Override
	public byte[] getByteArray(byte[] array, int offset, int length, Object data) {
		return carrier.getByteArrayAtOffset(array, offset, length, data, byteOffset());
	}

	/**
	 * Gets the byte array.
	 *
	 * @param array     the array
	 * @param offset    the offset
	 * @param length    the length
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the byte array
	 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#getByteArray(byte[], int, int,
	 *      java.lang.Object, long[])
	 */
	@Override
	public byte[] getByteArray(byte[] array, int offset, int length, Object data, long... sequences) {
		return carrier.getByteArrayAtOffset(array, offset, length, data, byteOffset(sequences));
	}

	/**
	 * Gets the byte array at.
	 *
	 * @param byteOffset the byte offset
	 * @param array      the array
	 * @param offset     the offset
	 * @param length     the length
	 * @param data       the data
	 * @return the byte array at
	 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#getByteArrayAt(long,
	 *      byte[], int, int, java.lang.Object)
	 */
	@Override
	public byte[] getByteArrayAt(long byteOffset, byte[] array, int offset, int length, Object data) {
		return carrier.getByteArrayAtOffset(array, offset, length, data, byteOffset() + byteOffset);
	}

	/**
	 * Gets the int array.
	 *
	 * @param array  the array
	 * @param offset the offset
	 * @param length the length
	 * @param data   the data
	 * @return the int array
	 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#getIntArray(int[], int, int,
	 *      java.lang.Object)
	 */
	@Override
	public int[] getIntArray(int[] array, int offset, int length, Object data) {
		return carrier.getIntArrayAtOffset(array, offset, length, data, byteOffset());
	}

	/**
	 * Gets the int array.
	 *
	 * @param array     the array
	 * @param offset    the offset
	 * @param length    the length
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the int array
	 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#getIntArray(int[], int, int,
	 *      java.lang.Object, long[])
	 */
	@Override
	public int[] getIntArray(int[] array, int offset, int length, Object data, long... sequences) {
		return carrier.getIntArrayAtOffset(array, offset, length, data, byteOffset(sequences));
	}

	/**
	 * Gets the long array.
	 *
	 * @param array  the array
	 * @param offset the offset
	 * @param length the length
	 * @param data   the data
	 * @return the long array
	 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#getLongArray(long[], int, int,
	 *      java.lang.Object)
	 */
	@Override
	public long[] getLongArray(long[] array, int offset, int length, Object data) {
		return carrier.getLongArrayAtOffset(array, offset, length, data, byteOffset());
	}

	/**
	 * Gets the long array.
	 *
	 * @param array     the array
	 * @param offset    the offset
	 * @param length    the length
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the long array
	 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#getLongArray(long[], int, int,
	 *      java.lang.Object, long[])
	 */
	@Override
	public long[] getLongArray(long[] array, int offset, int length, Object data, long... sequences) {
		return carrier.getLongArrayAtOffset(array, offset, length, data, byteOffset(sequences));
	}

	/**
	 * Gets the short array.
	 *
	 * @param array  the array
	 * @param offset the offset
	 * @param length the length
	 * @param data   the data
	 * @return the short array
	 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#getShortArray(short[], int, int,
	 *      java.lang.Object)
	 */
	@Override
	public short[] getShortArray(short[] array, int offset, int length, Object data) {
		return carrier.getShortArrayAtOffset(array, offset, length, data, byteOffset());
	}

	/**
	 * Gets the short array.
	 *
	 * @param array     the array
	 * @param offset    the offset
	 * @param length    the length
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the short array
	 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#getShortArray(short[], int, int,
	 *      java.lang.Object, long[])
	 */
	@Override
	public short[] getShortArray(short[] array, int offset, int length, Object data, long... sequences) {
		return carrier.getShortArrayAtOffset(array, offset, length, data, byteOffset(sequences));
	}

	/**
	 * Layout.
	 *
	 * @return the binary layout
	 * @see com.slytechs.protocol.runtime.internal.layout.BinaryField#layout()
	 */
	@Override
	public BinaryLayout layout() {
		return context.binaryLayout();
	}

	/**
	 * Sets the byte array.
	 *
	 * @param array  the array
	 * @param offset the offset
	 * @param length the length
	 * @param data   the data
	 * @return the byte[]
	 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#setByteArray(byte[], int, int,
	 *      java.lang.Object)
	 */
	@Override
	public byte[] setByteArray(byte[] array, int offset, int length, Object data) {
		return carrier.setByteArrayAtOffset(array, offset, length, data, byteOffset());
	}

	/**
	 * Sets the byte array.
	 *
	 * @param array     the array
	 * @param offset    the offset
	 * @param length    the length
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the byte[]
	 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#setByteArray(byte[], int, int,
	 *      java.lang.Object, long[])
	 */
	@Override
	public byte[] setByteArray(byte[] array, int offset, int length, Object data, long... sequences) {
		return carrier.setByteArrayAtOffset(array, offset, length, data, byteOffset(sequences));
	}

	/**
	 * Sets the int array.
	 *
	 * @param array  the array
	 * @param offset the offset
	 * @param length the length
	 * @param data   the data
	 * @return the int[]
	 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#setIntArray(int[], int, int,
	 *      java.lang.Object)
	 */
	@Override
	public int[] setIntArray(int[] array, int offset, int length, Object data) {
		return carrier.setIntArrayAtOffset(array, offset, length, data, byteOffset());
	}

	/**
	 * Sets the int array.
	 *
	 * @param array     the array
	 * @param offset    the offset
	 * @param length    the length
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the int[]
	 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#setIntArray(int[], int, int,
	 *      java.lang.Object, long[])
	 */
	@Override
	public int[] setIntArray(int[] array, int offset, int length, Object data, long... sequences) {
		return carrier.setIntArrayAtOffset(array, offset, length, data, byteOffset(sequences));
	}

	/**
	 * Sets the long array.
	 *
	 * @param array  the array
	 * @param offset the offset
	 * @param length the length
	 * @param data   the data
	 * @return the long[]
	 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#setLongArray(long[], int, int,
	 *      java.lang.Object)
	 */
	@Override
	public long[] setLongArray(long[] array, int offset, int length, Object data) {
		return carrier.setLongArrayAtOffset(array, offset, length, data, byteOffset());
	}

	/**
	 * Sets the long array.
	 *
	 * @param array     the array
	 * @param offset    the offset
	 * @param length    the length
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the long[]
	 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#setLongArray(long[], int, int,
	 *      java.lang.Object, long[])
	 */
	@Override
	public long[] setLongArray(long[] array, int offset, int length, Object data, long... sequences) {
		return carrier.setLongArrayAtOffset(array, offset, length, data, byteOffset(sequences));
	}

	/**
	 * Sets the short array.
	 *
	 * @param array  the array
	 * @param offset the offset
	 * @param length the length
	 * @param data   the data
	 * @return the short[]
	 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#setShortArray(short[], int, int,
	 *      java.lang.Object)
	 */
	@Override
	public short[] setShortArray(short[] array, int offset, int length, Object data) {
		return carrier.setShortArrayAtOffset(array, offset, length, data, byteOffset());
	}

	/**
	 * Sets the short array.
	 *
	 * @param array     the array
	 * @param offset    the offset
	 * @param length    the length
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the short[]
	 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#setShortArray(short[], int, int,
	 *      java.lang.Object, long[])
	 */
	@Override
	public short[] setShortArray(short[] array, int offset, int length, Object data, long... sequences) {
		return carrier.setShortArrayAtOffset(array, offset, length, data, byteOffset(sequences));
	}

	/**
	 * To string.
	 *
	 * @return the string
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ArrayFieldImplementation [context=" + context + "]";
	}

	/**
	 * To string.
	 *
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the string
	 * @see com.slytechs.protocol.runtime.internal.layout.BinaryField#toString(java.lang.Object, long[])
	 */
	@Override
	public String toString(Object data, long... sequences) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	/**
	 * Wrap.
	 *
	 * @param data the data
	 * @return the byte array
	 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#wrap(byte[])
	 */
	@Override
	public ByteArray wrap(byte[] data) {
		return carrier.wrapAtOffset(data, byteOffset(0), byteSize());
	}

	/**
	 * Wrap.
	 *
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the byte array
	 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#wrap(byte[], long[])
	 */
	@Override
	public ByteArray wrap(byte[] data, long... sequences) {
		return carrier.wrapAtOffset(data, byteOffset(sequences), byteSize());
	}

	/**
	 * Wrap.
	 *
	 * @param data the data
	 * @return the byte array
	 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#wrapConsumer(org.jnet.buffer.ByteArray)
	 */
	@Override
	public ByteArray wrap(ByteArray data) {
		return carrier.wrapAtOffset(data, byteOffset(0), byteSize());
	}

	/**
	 * Wrap.
	 *
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the byte array
	 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#wrap(org.jnet.buffer.ByteArray, long[])
	 */
	@Override
	public ByteArray wrap(ByteArray data, long... sequences) {
		return carrier.wrapAtOffset(data, byteOffset(sequences), byteSize());

	}

}
