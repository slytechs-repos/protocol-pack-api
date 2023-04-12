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

import java.util.Optional;
import java.util.OptionalLong;

import com.slytechs.protocol.runtime.internal.util.ByteArray;

/**
 * The Interface ArrayField.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public interface ArrayField extends BinaryField {

	/**
	 * The Interface Proxy.
	 */
	public interface Proxy extends ArrayField {

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
		default byte[] getByteArrayAt(long byteOffset, byte[] array, int offset, int length, Object data) {
			return proxyArrayField().getByteArrayAt(byteOffset, array, offset, length, data);
		}

		/**
		 * Bit offset.
		 *
		 * @return the long
		 * @see com.slytechs.protocol.runtime.internal.layout.BinaryField#bitOffset()
		 */
		default long bitOffset() {
			return proxyArrayField().bitOffset();
		}

		/**
		 * Bit offset.
		 *
		 * @param sequences the sequences
		 * @return the long
		 * @see com.slytechs.protocol.runtime.internal.layout.BinaryField#bitOffset(long[])
		 */
		default long bitOffset(long... sequences) {
			return proxyArrayField().bitOffset(sequences);
		}

		/**
		 * Bit size.
		 *
		 * @return the long
		 * @see com.slytechs.protocol.runtime.internal.layout.BinaryField#bitSize()
		 */
		default long bitSize() {
			return proxyArrayField().bitSize();
		}

		/**
		 * Byte offset.
		 *
		 * @return the long
		 * @see com.slytechs.protocol.runtime.internal.layout.BinaryField#byteOffset()
		 */
		default long byteOffset() {
			return proxyArrayField().byteOffset();
		}

		/**
		 * Byte offset.
		 *
		 * @param sequences the sequences
		 * @return the long
		 * @see com.slytechs.protocol.runtime.internal.layout.BinaryField#byteOffset(long[])
		 */
		default long byteOffset(long... sequences) {
			return proxyArrayField().byteOffset(sequences);
		}

		/**
		 * Field name.
		 *
		 * @return the optional
		 * @see com.slytechs.protocol.runtime.internal.layout.BinaryField#fieldName()
		 */
		default Optional<String> fieldName() {
			return proxyArrayField().fieldName();
		}

		/**
		 * Gets the byte array.
		 *
		 * @param array  the array
		 * @param offset the offset
		 * @param length the length
		 * @param data   the data
		 * @return the byte array
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#getByteArray(byte[],
		 *      int, int, java.lang.Object)
		 */
		default byte[] getByteArray(byte[] array, int offset, int length, Object data) {
			return proxyArrayField().getByteArray(array, offset, length, data);
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
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#getByteArray(byte[],
		 *      int, int, java.lang.Object, long[])
		 */
		default byte[] getByteArray(byte[] array, int offset, int length, Object data, long... sequences) {
			return proxyArrayField().getByteArray(array, offset, length, data, sequences);
		}

		/**
		 * Gets the byte array.
		 *
		 * @param array the array
		 * @param data  the data
		 * @return the byte array
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#getByteArray(byte[],
		 *      java.lang.Object)
		 */
		default byte[] getByteArray(byte[] array, Object data) {
			return proxyArrayField().getByteArray(array, data);
		}

		/**
		 * Gets the byte array.
		 *
		 * @param array     the array
		 * @param data      the data
		 * @param sequences the sequences
		 * @return the byte array
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#getByteArray(byte[],
		 *      java.lang.Object, long[])
		 */
		default byte[] getByteArray(byte[] array, Object data, long... sequences) {
			return proxyArrayField().getByteArray(array, data, sequences);
		}

		/**
		 * Gets the byte array.
		 *
		 * @param array the array
		 * @param data  the data
		 * @return the byte array
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#getByteArray(org.jnet.buffer.ByteArray,
		 *      java.lang.Object)
		 */
		default ByteArray getByteArray(ByteArray array, Object data) {
			return proxyArrayField().getByteArray(array, data);
		}

		/**
		 * Gets the byte array.
		 *
		 * @param array     the array
		 * @param data      the data
		 * @param sequences the sequences
		 * @return the byte array
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#getByteArray(org.jnet.buffer.ByteArray,
		 *      java.lang.Object, long[])
		 */
		default ByteArray getByteArray(ByteArray array, Object data, long... sequences) {
			return proxyArrayField().getByteArray(array, data, sequences);
		}

		/**
		 * Gets the byte array.
		 *
		 * @param data the data
		 * @return the byte array
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#getByteArray(java.lang.Object)
		 */
		default byte[] getByteArray(Object data) {
			return proxyArrayField().getByteArray(data);
		}

		/**
		 * Gets the byte array.
		 *
		 * @param data      the data
		 * @param sequences the sequences
		 * @return the byte array
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#getByteArray(java.lang.Object,
		 *      long[])
		 */
		default byte[] getByteArray(Object data, long... sequences) {
			return proxyArrayField().getByteArray(data, sequences);
		}

		/**
		 * Gets the int array.
		 *
		 * @param array  the array
		 * @param offset the offset
		 * @param length the length
		 * @param data   the data
		 * @return the int array
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#getIntArray(int[],
		 *      int, int, java.lang.Object)
		 */
		default int[] getIntArray(int[] array, int offset, int length, Object data) {
			return proxyArrayField().getIntArray(array, offset, length, data);
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
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#getIntArray(int[],
		 *      int, int, java.lang.Object, long[])
		 */
		default int[] getIntArray(int[] array, int offset, int length, Object data, long... sequences) {
			return proxyArrayField().getIntArray(array, offset, length, data, sequences);
		}

		/**
		 * Gets the int array.
		 *
		 * @param array the array
		 * @param data  the data
		 * @return the int array
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#getIntArray(int[],
		 *      java.lang.Object)
		 */
		default int[] getIntArray(int[] array, Object data) {
			return proxyArrayField().getIntArray(array, data);
		}

		/**
		 * Gets the int array.
		 *
		 * @param array     the array
		 * @param data      the data
		 * @param sequences the sequences
		 * @return the int array
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#getIntArray(int[],
		 *      java.lang.Object, long[])
		 */
		default int[] getIntArray(int[] array, Object data, long... sequences) {
			return proxyArrayField().getIntArray(array, data, sequences);
		}

		/**
		 * Gets the int array.
		 *
		 * @param data the data
		 * @return the int array
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#getIntArray(java.lang.Object)
		 */
		default int[] getIntArray(Object data) {
			return proxyArrayField().getIntArray(data);
		}

		/**
		 * Gets the int array.
		 *
		 * @param data      the data
		 * @param sequences the sequences
		 * @return the int array
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#getIntArray(java.lang.Object,
		 *      long[])
		 */
		default int[] getIntArray(Object data, long... sequences) {
			return proxyArrayField().getIntArray(data, sequences);
		}

		/**
		 * Gets the long array.
		 *
		 * @param array  the array
		 * @param offset the offset
		 * @param length the length
		 * @param data   the data
		 * @return the long array
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#getLongArray(long[],
		 *      int, int, java.lang.Object)
		 */
		default long[] getLongArray(long[] array, int offset, int length, Object data) {
			return proxyArrayField().getLongArray(array, offset, length, data);
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
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#getLongArray(long[],
		 *      int, int, java.lang.Object, long[])
		 */
		default long[] getLongArray(long[] array, int offset, int length, Object data, long... sequences) {
			return proxyArrayField().getLongArray(array, offset, length, data, sequences);
		}

		/**
		 * Gets the long array.
		 *
		 * @param array the array
		 * @param data  the data
		 * @return the long array
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#getLongArray(long[],
		 *      java.lang.Object)
		 */
		default long[] getLongArray(long[] array, Object data) {
			return proxyArrayField().getLongArray(array, data);
		}

		/**
		 * Gets the long array.
		 *
		 * @param array     the array
		 * @param data      the data
		 * @param sequences the sequences
		 * @return the long array
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#getLongArray(long[],
		 *      java.lang.Object, long[])
		 */
		default long[] getLongArray(long[] array, Object data, long... sequences) {
			return proxyArrayField().getLongArray(array, data, sequences);
		}

		/**
		 * Gets the long array.
		 *
		 * @param data the data
		 * @return the long array
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#getLongArray(java.lang.Object)
		 */
		default long[] getLongArray(Object data) {
			return proxyArrayField().getLongArray(data);
		}

		/**
		 * Gets the long array.
		 *
		 * @param data      the data
		 * @param sequences the sequences
		 * @return the long array
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#getLongArray(java.lang.Object,
		 *      long[])
		 */
		default long[] getLongArray(Object data, long... sequences) {
			return proxyArrayField().getLongArray(data, sequences);
		}

		/**
		 * Gets the short array.
		 *
		 * @param data the data
		 * @return the short array
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#getShortArray(java.lang.Object)
		 */
		default short[] getShortArray(Object data) {
			return proxyArrayField().getShortArray(data);
		}

		/**
		 * Gets the short array.
		 *
		 * @param data      the data
		 * @param sequences the sequences
		 * @return the short array
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#getShortArray(java.lang.Object,
		 *      long[])
		 */
		default short[] getShortArray(Object data, long... sequences) {
			return proxyArrayField().getShortArray(data, sequences);
		}

		/**
		 * Gets the short array.
		 *
		 * @param array  the array
		 * @param offset the offset
		 * @param length the length
		 * @param data   the data
		 * @return the short array
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#getShortArray(short[],
		 *      int, int, java.lang.Object)
		 */
		default short[] getShortArray(short[] array, int offset, int length, Object data) {
			return proxyArrayField().getShortArray(array, offset, length, data);
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
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#getShortArray(short[],
		 *      int, int, java.lang.Object, long[])
		 */
		default short[] getShortArray(short[] array, int offset, int length, Object data, long... sequences) {
			return proxyArrayField().getShortArray(array, offset, length, data, sequences);
		}

		/**
		 * Gets the short array.
		 *
		 * @param array the array
		 * @param data  the data
		 * @return the short array
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#getShortArray(short[],
		 *      java.lang.Object)
		 */
		default short[] getShortArray(short[] array, Object data) {
			return proxyArrayField().getShortArray(array, data);
		}

		/**
		 * Gets the short array.
		 *
		 * @param array     the array
		 * @param data      the data
		 * @param sequences the sequences
		 * @return the short array
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#getShortArray(short[],
		 *      java.lang.Object, long[])
		 */
		default short[] getShortArray(short[] array, Object data, long... sequences) {
			return proxyArrayField().getShortArray(array, data, sequences);
		}

		/**
		 * Layout.
		 *
		 * @return the binary layout
		 * @see com.slytechs.protocol.runtime.internal.layout.BinaryField#layout()
		 */
		default BinaryLayout layout() {
			return proxyArrayField().layout();
		}

		/**
		 * Proxy array field.
		 *
		 * @return the array field
		 */
		ArrayField proxyArrayField();

		/**
		 * Sets the byte array.
		 *
		 * @param array  the array
		 * @param offset the offset
		 * @param length the length
		 * @param data   the data
		 * @return the byte[]
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#setByteArray(byte[],
		 *      int, int, java.lang.Object)
		 */
		default byte[] setByteArray(byte[] array, int offset, int length, Object data) {
			return proxyArrayField().setByteArray(array, offset, length, data);
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
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#setByteArray(byte[],
		 *      int, int, java.lang.Object, long[])
		 */
		default byte[] setByteArray(byte[] array, int offset, int length, Object data, long... sequences) {
			return proxyArrayField().setByteArray(array, offset, length, data, sequences);
		}

		/**
		 * Sets the byte array.
		 *
		 * @param array the array
		 * @param data  the data
		 * @return the byte[]
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#setByteArray(byte[],
		 *      java.lang.Object)
		 */
		default byte[] setByteArray(byte[] array, Object data) {
			return proxyArrayField().setByteArray(array, data);
		}

		/**
		 * Sets the byte array.
		 *
		 * @param array     the array
		 * @param data      the data
		 * @param sequences the sequences
		 * @return the byte[]
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#setByteArray(byte[],
		 *      java.lang.Object, long[])
		 */
		default byte[] setByteArray(byte[] array, Object data, long... sequences) {
			return proxyArrayField().setByteArray(array, data, sequences);
		}

		/**
		 * Sets the byte array.
		 *
		 * @param array the array
		 * @param data  the data
		 * @return the byte array
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#setByteArray(org.jnet.buffer.ByteArray,
		 *      java.lang.Object)
		 */
		default ByteArray setByteArray(ByteArray array, Object data) {
			return proxyArrayField().setByteArray(array, data);
		}

		/**
		 * Sets the byte array.
		 *
		 * @param array     the array
		 * @param data      the data
		 * @param sequences the sequences
		 * @return the byte array
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#setByteArray(org.jnet.buffer.ByteArray,
		 *      java.lang.Object, long[])
		 */
		default ByteArray setByteArray(ByteArray array, Object data, long... sequences) {
			return proxyArrayField().setByteArray(array, data, sequences);
		}

		/**
		 * Sets the int array.
		 *
		 * @param array  the array
		 * @param offset the offset
		 * @param length the length
		 * @param data   the data
		 * @return the int[]
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#setIntArray(int[],
		 *      int, int, java.lang.Object)
		 */
		default int[] setIntArray(int[] array, int offset, int length, Object data) {
			return proxyArrayField().setIntArray(array, offset, length, data);
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
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#setIntArray(int[],
		 *      int, int, java.lang.Object, long[])
		 */
		default int[] setIntArray(int[] array, int offset, int length, Object data, long... sequences) {
			return proxyArrayField().setIntArray(array, offset, length, data, sequences);
		}

		/**
		 * Sets the int array.
		 *
		 * @param array the array
		 * @param data  the data
		 * @return the int[]
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#setIntArray(int[],
		 *      java.lang.Object)
		 */
		default int[] setIntArray(int[] array, Object data) {
			return proxyArrayField().setIntArray(array, data);
		}

		/**
		 * Sets the int array.
		 *
		 * @param array     the array
		 * @param data      the data
		 * @param sequences the sequences
		 * @return the int[]
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#setIntArray(int[],
		 *      java.lang.Object, long[])
		 */
		default int[] setIntArray(int[] array, Object data, long... sequences) {
			return proxyArrayField().setIntArray(array, data, sequences);
		}

		/**
		 * Sets the long array.
		 *
		 * @param array  the array
		 * @param offset the offset
		 * @param length the length
		 * @param data   the data
		 * @return the long[]
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#setLongArray(long[],
		 *      int, int, java.lang.Object)
		 */
		default long[] setLongArray(long[] array, int offset, int length, Object data) {
			return proxyArrayField().setLongArray(array, offset, length, data);
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
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#setLongArray(long[],
		 *      int, int, java.lang.Object, long[])
		 */
		default long[] setLongArray(long[] array, int offset, int length, Object data, long... sequences) {
			return proxyArrayField().setLongArray(array, offset, length, data, sequences);
		}

		/**
		 * Sets the long array.
		 *
		 * @param array the array
		 * @param data  the data
		 * @return the long[]
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#setLongArray(long[],
		 *      java.lang.Object)
		 */
		default long[] setLongArray(long[] array, Object data) {
			return proxyArrayField().setLongArray(array, data);
		}

		/**
		 * Sets the long array.
		 *
		 * @param array     the array
		 * @param data      the data
		 * @param sequences the sequences
		 * @return the long[]
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#setLongArray(long[],
		 *      java.lang.Object, long[])
		 */
		default long[] setLongArray(long[] array, Object data, long... sequences) {
			return proxyArrayField().setLongArray(array, data, sequences);
		}

		/**
		 * Sets the short array.
		 *
		 * @param array  the array
		 * @param offset the offset
		 * @param length the length
		 * @param data   the data
		 * @return the short[]
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#setShortArray(short[],
		 *      int, int, java.lang.Object)
		 */
		default short[] setShortArray(short[] array, int offset, int length, Object data) {
			return proxyArrayField().setShortArray(array, offset, length, data);
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
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#setShortArray(short[],
		 *      int, int, java.lang.Object, long[])
		 */
		default short[] setShortArray(short[] array, int offset, int length, Object data, long... sequences) {
			return proxyArrayField().setShortArray(array, offset, length, data, sequences);
		}

		/**
		 * Sets the short array.
		 *
		 * @param array the array
		 * @param data  the data
		 * @return the short[]
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#setShortArray(short[],
		 *      java.lang.Object)
		 */
		default short[] setShortArray(short[] array, Object data) {
			return proxyArrayField().setShortArray(array, data);
		}

		/**
		 * Sets the short array.
		 *
		 * @param array     the array
		 * @param data      the data
		 * @param sequences the sequences
		 * @return the short[]
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#setShortArray(short[],
		 *      java.lang.Object, long[])
		 */
		default short[] setShortArray(short[] array, Object data, long... sequences) {
			return proxyArrayField().setShortArray(array, data, sequences);
		}

		/**
		 * Size.
		 *
		 * @return the optional long
		 * @see com.slytechs.protocol.runtime.internal.layout.BinaryField#size()
		 */
		default OptionalLong size() {
			return proxyArrayField().size();
		}

		/**
		 * To string.
		 *
		 * @param data      the data
		 * @param sequences the sequences
		 * @return the string
		 * @see com.slytechs.protocol.runtime.internal.layout.BinaryField#toString(java.lang.Object,
		 *      long[])
		 */
		default String toString(Object data, long... sequences) {
			return proxyArrayField().toString(data, sequences);
		}

		/**
		 * Wrap.
		 *
		 * @param data the data
		 * @return the byte array
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#wrap(byte[])
		 */
		default ByteArray wrap(byte[] data) {
			return proxyArrayField().wrap(data);
		}

		/**
		 * Wrap.
		 *
		 * @param data      the data
		 * @param sequences the sequences
		 * @return the byte array
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#wrap(byte[],
		 *      long[])
		 */
		default ByteArray wrap(byte[] data, long... sequences) {
			return proxyArrayField().wrap(data, sequences);
		}

		/**
		 * Wrap.
		 *
		 * @param data the data
		 * @return the byte array
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#wrapConsumer(org.jnet.buffer.ByteArray)
		 */
		default ByteArray wrap(ByteArray data) {
			return proxyArrayField().wrap(data);
		}

		/**
		 * Wrap.
		 *
		 * @param data      the data
		 * @param sequences the sequences
		 * @return the byte array
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField#wrap(org.jnet.buffer.ByteArray,
		 *      long[])
		 */
		default ByteArray wrap(ByteArray data, long... sequences) {
			return proxyArrayField().wrap(data, sequences);
		}
	}

	/**
	 * The Interface ArrayFieldFormatter.
	 */
	public interface ArrayFieldFormatter {

		/**
		 * Of.
		 *
		 * @param formatString the format string
		 * @return the array field formatter
		 */
		static ArrayFieldFormatter of(String formatString) {
			return (f, d, seq) -> String.format(formatString, f.getByteArray(d, seq), f.fieldName().orElse("field"), d,
					seq);
		}

		/**
		 * Format.
		 *
		 * @param field     the field
		 * @param data      the data
		 * @param sequences the sequences
		 * @return the string
		 */
		String format(ArrayField field, Object data, long... sequences);
	}

	/**
	 * The Class FormattedArrayField.
	 */
	class FormattedArrayField implements ArrayField.Proxy {

		/** The proxy. */
		private final ArrayField proxy;

		/** The formatter. */
		private ArrayFieldFormatter formatter;

		/**
		 * Instantiates a new formatted array field.
		 *
		 * @param format the format
		 * @param proxy  the proxy
		 */
		public FormattedArrayField(String format, ArrayField proxy) {
			this(ArrayFieldFormatter.of(format), proxy);
		}

		/**
		 * Instantiates a new formatted array field.
		 *
		 * @param formatter the formatter
		 * @param proxy     the proxy
		 */
		public FormattedArrayField(ArrayFieldFormatter formatter, ArrayField proxy) {
			this.proxy = proxy;
		}

		/**
		 * Proxy array field.
		 *
		 * @return the array field
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField.Proxy#proxyBitField()
		 */
		@Override
		public ArrayField proxyArrayField() {
			return proxy;
		}

		/**
		 * To string.
		 *
		 * @param data      the data
		 * @param sequences the sequences
		 * @return the string
		 * @see com.slytechs.protocol.runtime.internal.layout.ArrayField.Proxy#toString(java.lang.Object,
		 *      long[])
		 */
		@Override
		public String toString(Object data, long... sequences) {
			return formatter.format(proxy, data, sequences);
		}

	}

	/**
	 * Gets the byte array.
	 *
	 * @param array  the array
	 * @param offset the offset
	 * @param length the length
	 * @param data   the data
	 * @return the byte array
	 */
	// @formatter:off
    byte[]  getByteArray(byte[] array, int offset, int length, Object data);
    
    /**
	 * Gets the byte array at.
	 *
	 * @param byteOffset the byte offset
	 * @param array      the array
	 * @param offset     the offset
	 * @param length     the length
	 * @param data       the data
	 * @return the byte array at
	 */
    byte[]  getByteArrayAt(long byteOffset, byte[] array, int offset, int length, Object data);
	        
        	/**
			 * Gets the byte array.
			 *
			 * @param array     the array
			 * @param offset    the offset
			 * @param length    the length
			 * @param data      the data
			 * @param sequences the sequences
			 * @return the byte array
			 */
        	byte[]  getByteArray(byte[] array, int offset, int length, Object data, long... sequences);
	
	/**
	 * Gets the byte array.
	 *
	 * @param array the array
	 * @param data  the data
	 * @return the byte array
	 */
    default byte[]  getByteArray(byte[] array, Object data)                         { return getByteArray(array, 0, array.length, data); }
    
    /**
	 * Gets the byte array at.
	 *
	 * @param byteOffset the byte offset
	 * @param array      the array
	 * @param data       the data
	 * @return the byte array at
	 */
    default byte[]  getByteArrayAt(long byteOffset, byte[] array, Object data)      { return getByteArrayAt(byteOffset, array, 0, array.length, data); }
	
	/**
	 * Gets the byte array.
	 *
	 * @param array     the array
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the byte array
	 */
	default byte[]  getByteArray(byte[] array, Object data, long... sequences)      { return getByteArray(array, 0, array.length, data, sequences); }
	
	/**
	 * Gets the byte array.
	 *
	 * @param array the array
	 * @param data  the data
	 * @return the byte array
	 */
	default ByteArray getByteArray(ByteArray array, Object data)                    { getByteArray(array.array(), array.arrayOffset(), array.arrayLength(), data); return array; } 
	
	/**
	 * Gets the byte array.
	 *
	 * @param array     the array
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the byte array
	 */
	default ByteArray getByteArray(ByteArray array, Object data, long... sequences) { getByteArray(array.array(), array.arrayOffset(), array.arrayLength(), data, sequences); return array; } 

	/**
	 * Gets the byte array.
	 *
	 * @param data the data
	 * @return the byte array
	 */
	default byte[]  getByteArray(Object data)                                       { return getByteArray(new byte[(int) byteSize()], data); }
	
	/**
	 * Gets the byte array at.
	 *
	 * @param byteOffset the byte offset
	 * @param data       the data
	 * @return the byte array at
	 */
	default byte[]  getByteArrayAt(long byteOffset, Object data)                    { return getByteArrayAt(byteOffset, new byte[(int) byteSize()], data); }
	
	/**
	 * Gets the byte array.
	 *
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the byte array
	 */
	default byte[]  getByteArray(Object data, long... sequences)                    { return getByteArray(new byte[(int) byteSize()], data, sequences); }
	
	        /**
			 * Gets the int array.
			 *
			 * @param array  the array
			 * @param offset the offset
			 * @param length the length
			 * @param data   the data
			 * @return the int array
			 */
        	int[]   getIntArray(int[] array, int offset, int length, Object data);
	        
        	/**
			 * Gets the int array.
			 *
			 * @param array     the array
			 * @param offset    the offset
			 * @param length    the length
			 * @param data      the data
			 * @param sequences the sequences
			 * @return the int array
			 */
        	int[]   getIntArray(int[] array, int offset, int length, Object data, long... sequences);
	
	/**
	 * Gets the int array.
	 *
	 * @param array the array
	 * @param data  the data
	 * @return the int array
	 */
	default int[]   getIntArray(int[] array, Object data)                           { return getIntArray(array, 0, array.length, data); }
	
	/**
	 * Gets the int array.
	 *
	 * @param array     the array
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the int array
	 */
	default int[]   getIntArray(int[] array, Object data, long... sequences)        { return getIntArray(array, 0, array.length, data, sequences); }

	/**
	 * Gets the int array.
	 *
	 * @param data the data
	 * @return the int array
	 */
	default int[]   getIntArray(Object data)                                        { return getIntArray(new int[(int) byteSize()], data); }
	
	/**
	 * Gets the int array.
	 *
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the int array
	 */
	default int[]   getIntArray(Object data, long... sequences)                     { return getIntArray(new int[(int) byteSize()], data, sequences); }
	
	        /**
			 * Gets the long array.
			 *
			 * @param array  the array
			 * @param offset the offset
			 * @param length the length
			 * @param data   the data
			 * @return the long array
			 */
        	long[]  getLongArray(long[] array, int offset, int length, Object data);
	        
        	/**
			 * Gets the long array.
			 *
			 * @param array     the array
			 * @param offset    the offset
			 * @param length    the length
			 * @param data      the data
			 * @param sequences the sequences
			 * @return the long array
			 */
        	long[]  getLongArray(long[] array, int offset, int length, Object data, long... sequences);
	
	/**
	 * Gets the long array.
	 *
	 * @param array the array
	 * @param data  the data
	 * @return the long array
	 */
	default long[]  getLongArray(long[] array, Object data)                         { return getLongArray(array, 0, array.length, data); }
	
	/**
	 * Gets the long array.
	 *
	 * @param array     the array
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the long array
	 */
	default long[]  getLongArray(long[] array, Object data, long... sequences)      { return getLongArray(array, 0, array.length, data, sequences); }

	/**
	 * Gets the long array.
	 *
	 * @param data the data
	 * @return the long array
	 */
	default long[]  getLongArray(Object data)                                       { return getLongArray(new long[(int) byteSize()], data); }
	
	/**
	 * Gets the long array.
	 *
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the long array
	 */
	default long[]  getLongArray(Object data, long... sequences)                    { return getLongArray(new long[(int) byteSize()], data, sequences); }
	
	/**
	 * Gets the short array.
	 *
	 * @param data the data
	 * @return the short array
	 */
	default short[] getShortArray(Object data)                                      { return getShortArray(new short[(int) byteSize()], data); }
	
	/**
	 * Gets the short array.
	 *
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the short array
	 */
	default short[] getShortArray(Object data, long... sequences)                   { return getShortArray(new short[(int) byteSize()], data, sequences); }
	        
        	/**
			 * Gets the short array.
			 *
			 * @param array  the array
			 * @param offset the offset
			 * @param length the length
			 * @param data   the data
			 * @return the short array
			 */
        	short[] getShortArray(short[] array, int offset, int length, Object data);
	        
        	/**
			 * Gets the short array.
			 *
			 * @param array     the array
			 * @param offset    the offset
			 * @param length    the length
			 * @param data      the data
			 * @param sequences the sequences
			 * @return the short array
			 */
        	short[] getShortArray(short[] array, int offset, int length, Object data, long... sequences);

	/**
	 * Gets the short array.
	 *
	 * @param array the array
	 * @param data  the data
	 * @return the short array
	 */
	default short[] getShortArray(short[] array, Object data)                       { return getShortArray(new short[(int) byteSize()], data); }
	
	/**
	 * Gets the short array.
	 *
	 * @param array     the array
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the short array
	 */
	default short[] getShortArray(short[] array, Object data, long... sequences)    { return getShortArray(new short[(int) byteSize()], data, sequences); }
	
	        /**
			 * Sets the byte array.
			 *
			 * @param array  the array
			 * @param offset the offset
			 * @param length the length
			 * @param data   the data
			 * @return the byte[]
			 */
        	byte[]  setByteArray(byte[] array, int offset, int length, Object data);
	        
        	/**
			 * Sets the byte array.
			 *
			 * @param array     the array
			 * @param offset    the offset
			 * @param length    the length
			 * @param data      the data
			 * @param sequences the sequences
			 * @return the byte[]
			 */
        	byte[]  setByteArray(byte[] array, int offset, int length, Object data, long... sequences);
	
	/**
	 * Sets the byte array.
	 *
	 * @param array the array
	 * @param data  the data
	 * @return the byte[]
	 */
	default byte[]  setByteArray(byte[] array, Object data)                         { return setByteArray(array, 0, array.length, data); }
	
	/**
	 * Sets the byte array.
	 *
	 * @param array     the array
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the byte[]
	 */
	default byte[]  setByteArray(byte[] array, Object data, long... sequences)      { return setByteArray(array, 0, array.length, data, sequences); }
	
	/**
	 * Sets the byte array.
	 *
	 * @param array the array
	 * @param data  the data
	 * @return the byte array
	 */
	default ByteArray setByteArray(ByteArray array, Object data)                    { setByteArray(array.array(), array.arrayOffset(), array.arrayLength(), data); return array; }
	
	/**
	 * Sets the byte array.
	 *
	 * @param array     the array
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the byte array
	 */
	default ByteArray setByteArray(ByteArray array, Object data, long... sequences) { setByteArray(array.array(), array.arrayOffset(), array.arrayLength(), data, sequences); return array; }
	
            /**
			 * Sets the int array.
			 *
			 * @param array  the array
			 * @param offset the offset
			 * @param length the length
			 * @param data   the data
			 * @return the int[]
			 */
            int[]   setIntArray(int[] array, int offset, int length, Object data);
            
            /**
			 * Sets the int array.
			 *
			 * @param array     the array
			 * @param offset    the offset
			 * @param length    the length
			 * @param data      the data
			 * @param sequences the sequences
			 * @return the int[]
			 */
            int[]   setIntArray(int[] array, int offset, int length, Object data, long... sequences);
	
	/**
	 * Sets the int array.
	 *
	 * @param array the array
	 * @param data  the data
	 * @return the int[]
	 */
	default int[]   setIntArray(int[] array, Object data)                           { return setIntArray(array, 0, array.length, data); }
	
	/**
	 * Sets the int array.
	 *
	 * @param array     the array
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the int[]
	 */
	default int[]   setIntArray(int[] array, Object data, long... sequences)        { return setIntArray(array, 0, array.length, data, sequences); }
	
            /**
			 * Sets the long array.
			 *
			 * @param array  the array
			 * @param offset the offset
			 * @param length the length
			 * @param data   the data
			 * @return the long[]
			 */
            long[]  setLongArray(long[] array, int offset, int length, Object data);
	        
        	/**
			 * Sets the long array.
			 *
			 * @param array     the array
			 * @param offset    the offset
			 * @param length    the length
			 * @param data      the data
			 * @param sequences the sequences
			 * @return the long[]
			 */
        	long[]  setLongArray(long[] array, int offset, int length, Object data, long... sequences);
	
	/**
	 * Sets the long array.
	 *
	 * @param array the array
	 * @param data  the data
	 * @return the long[]
	 */
	default long[]  setLongArray(long[] array, Object data)                         { return setLongArray(array, 0, array.length, data); }
	
	/**
	 * Sets the long array.
	 *
	 * @param array     the array
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the long[]
	 */
	default long[]  setLongArray(long[] array, Object data, long... sequences)      { return setLongArray(array, 0, array.length, data, sequences); }
	
	        /**
			 * Sets the short array.
			 *
			 * @param array  the array
			 * @param offset the offset
			 * @param length the length
			 * @param data   the data
			 * @return the short[]
			 */
        	short[] setShortArray(short[] array, int offset, int length, Object data);
	        
        	/**
			 * Sets the short array.
			 *
			 * @param array     the array
			 * @param offset    the offset
			 * @param length    the length
			 * @param data      the data
			 * @param sequences the sequences
			 * @return the short[]
			 */
        	short[] setShortArray(short[] array, int offset, int length, Object data, long... sequences);
	
	/**
	 * Sets the short array.
	 *
	 * @param array the array
	 * @param data  the data
	 * @return the short[]
	 */
	default short[] setShortArray(short[] array, Object data)                       { return setShortArray(array, 0, array.length, data); }
	
	/**
	 * Sets the short array.
	 *
	 * @param array     the array
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the short[]
	 */
	default short[] setShortArray(short[] array, Object data, long... sequences)    { return setShortArray(array, 0, array.length, data, sequences); }

	/**
	 * Wrap.
	 *
	 * @param data the data
	 * @return the byte array
	 */
	ByteArray wrap(byte[] data);
	
	/**
	 * Wrap.
	 *
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the byte array
	 */
	ByteArray wrap(byte[] data, long...sequences);

	/**
	 * Wrap.
	 *
	 * @param data the data
	 * @return the byte array
	 */
	ByteArray wrap(ByteArray data);
	
	/**
	 * Wrap.
	 *
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the byte array
	 */
	ByteArray wrap(ByteArray data, long...sequences);

	// @formatter:on

	/**
	 * Formatted.
	 *
	 * @param format the format
	 * @return the array field
	 */
	default ArrayField formatted(String format) {
		return new FormattedArrayField(format, this);
	}
}
