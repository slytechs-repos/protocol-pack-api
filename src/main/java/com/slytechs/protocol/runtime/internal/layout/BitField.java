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

import java.lang.constant.Constable;
import java.lang.constant.ConstantDesc;
import java.nio.ByteOrder;
import java.util.Optional;
import java.util.OptionalLong;

/**
 * A <code>BinaryField</code> that extracts certain bits from a binary
 * structure. The layout of the binary structure and each field is defined using
 * <code>BinaryLayout</code> class.
 * 
 * The <code>BinaryField</code> is built as follows to perform a chain of
 * operations described below from left to right:
 * 
 * <pre>
 * (Field)<=>[readonly]<=>[Lock]<=>[Cache]<=>(Carrier)
 * </pre>
 * 
 * Where operation between [] is optional, enabled by an appropriate call. The
 * initial state of the BinaryField is simply:
 * 
 * <pre>
 * (Field)<=>(Carrier)
 * </pre>
 * 
 * Field bits are extracted from user supplied data source object first by the
 * carrier and then limited to the bits defined by layout. If you make a call to
 * {@link BitField#cached}, the <code>BinaryField</code> will look like this:
 * 
 * <pre>
 * (Field)<=>(Cache)<=>(Carrier)
 * </pre>
 * 
 * <h3>Currently supported Data source objects</h3>
 * <p>
 * The current list of data object types supported by <code>BinaryField</code>
 * implementation is as follows:
 * </p>
 * <ul>
 * <li>Java primitives: <code>byte</code>, <code>short</code>, <code>int</code>,
 * <code>long</code>, <code>byte[]</code>, <code>short[]</code>, <code>int[]</code> and <code>long[]</code></li>
 * <li><code>java.nio.ByteBuffer</code></li>
 * <li><code>jdk.incubator.foreign.MemorySegment</code></li>
 * <li><code>com.slytechs.jnet.runtime.buffer.ByteData<code></li>
 * </ul>
 * 
 * <h3>Caching</h3>
 * <p>
 * To improve performance, a caching mechanism can be enabled using the call
 * {@link #cache()} which will enable caching of carrier data. When a field is
 * read or written, the carrier primitive is accessed first and then appropriate
 * bits are extracted or set according to the field bit layout. Caching will
 * cache the carrier value on read so it doesn't have to be read again from the
 * same data source at the same offset, possibly saving many intermediate
 * operations to reassemble the carrier data from lets say from individual bytes
 * of a byte array. Setting a field with a new value will always update the
 * cache with the new value and also flush the new value to the data source
 * object, ie update the data byte array with new values.
 * </p>
 * 
 * <h3>Sycnronization</h3>
 * <p>
 * As a convenience, a synchronized BinaryField is provided where each read or
 * write operation on the data object is synchronized on the user supplied data
 * object. This can be accomplished externally as well, but as a convenience you
 * can also call {@link #synchronize()} once to always synchronize access on the
 * data object provided at the time of the getter or setter call.
 * 
 * <pre>
 * <code>
 * BinaryField field = ...;
 * BinaryField synchronizedField = field.synchronize();
 * byte[] myBinaryData = ...;
 * 
 * int value = synchronizedField.getInt(myBinaryData);
 * </code>
 * </pre>
 * 
 * is equivelent to the following externally synchronized snippet:
 * 
 * <pre>
 * <code>
 * BinaryField field = ...;
 * byte[] myBinaryData = ...;
 * 
 * synchronize(myBinaryData) {
 * 	int value = field.getInt(myBinaryData);
 * }
 * </code>
 * </pre>
 * </p>
 * 
 * <h3>Readonly</h4>
 * <p>
 * It is possible to limit the <code>BinaryField</code> API to only read
 * operations by calling {@link #readonly()} method. This will throw a readonly
 * type exception whenever a set* operation is attempted. Only get operations
 * are allowed.
 * </p>
 * 
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public interface BitField extends BinaryField, Constable {
	
	/**
	 * The Interface Proxy.
	 */
	public interface Proxy  extends BitField {
		
		/**
		 * Bit offset.
		 *
		 * @return the long
		 * @see com.slytechs.protocol.runtime.internal.layout.BinaryField#bitOffset()
		 */
		@Override
		default long bitOffset() {
			return proxyBitField().bitOffset();
		}

		/**
		 * Bit offset.
		 *
		 * @param sequences the sequences
		 * @return the long
		 * @see com.slytechs.protocol.runtime.internal.layout.BinaryField#bitOffset(long[])
		 */
		@Override
		default long bitOffset(long...sequences) {
			return proxyBitField().bitOffset(sequences);
		}

		/**
		 * Cache.
		 *
		 * @return the bit field
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#cache()
		 */
		@Override
		default BitField cache() {
			return proxyBitField().cache();
		}

		/**
		 * Describe constable.
		 *
		 * @return the optional<? extends constant desc>
		 * @see java.lang.constant.Constable#describeConstable()
		 */
		@Override
		default Optional<? extends ConstantDesc> describeConstable() {
			return proxyBitField().describeConstable();
		}

		/**
		 * Field name.
		 *
		 * @return the optional
		 * @see com.slytechs.protocol.runtime.internal.layout.BinaryField#fieldName()
		 */
		@Override
		default Optional<String> fieldName() {
			return proxyBitField().fieldName();
		}

		/**
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#getBit(java.lang.Object)
		 */
		@Override
		default boolean getBit(Object data) {
			return proxyBitField().getBit(data);
		}

		/**
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#getBit(java.lang.Object, long[])
		 */
		@Override
		default boolean getBit(Object data, long... sequences) {
			return proxyBitField().getBit(data, sequences);
			
		}

		/**
		 * Gets the byte.
		 *
		 * @param data the data
		 * @return the byte
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#getByte(java.lang.Object)
		 */
		@Override
		default byte getByte(Object data) {
			return proxyBitField().getByte(data);
		}

		/**
		 * Gets the byte.
		 *
		 * @param data      the data
		 * @param sequences the sequences
		 * @return the byte
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#getByte(java.lang.Object,
		 *      long[])
		 */
		@Override
		default byte getByte(Object data, long... sequences) {
			return proxyBitField().getByte(data, sequences);
		}

		/**
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#getByteAt(long, java.lang.Object)
		 */
		@Override
		default byte getByteAt(long offset, Object data) {
			return proxyBitField().getByteAt(offset, data);
		}

		/**
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#getByteAt(long, java.lang.Object, long[])
		 */
		@Override
		default byte getByteAt(long offset, Object data, long... sequences) {
			return proxyBitField().getByteAt(offset, data, sequences);
		}

		/**
		 * Gets the double.
		 *
		 * @param data the data
		 * @return the double
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#getDouble(java.lang.Object)
		 */
		@Override
		default double getDouble(Object data) {
			return proxyBitField().getDouble(data);
		}

		/**
		 * Gets the double.
		 *
		 * @param data      the data
		 * @param sequences the sequences
		 * @return the double
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#getDouble(java.lang.Object,
		 *      long[])
		 */
		@Override
		default double getDouble(Object data, long... sequences) {
			return proxyBitField().getDouble(data, sequences);
		}

		/**
		 * Gets the float.
		 *
		 * @param data the data
		 * @return the float
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#getFloat(java.lang.Object)
		 */
		@Override
		default float getFloat(Object data) {
			return proxyBitField().getFloat(data);
		}

		/**
		 * Gets the float.
		 *
		 * @param data      the data
		 * @param sequences the sequences
		 * @return the float
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#getFloat(java.lang.Object,
		 *      long[])
		 */
		@Override
		default float getFloat(Object data, long... sequences) {
			return proxyBitField().getFloat(data, sequences);
		}

		/**
		 * Gets the int.
		 *
		 * @param data the data
		 * @return the int
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#getInt(java.lang.Object)
		 */
		@Override
		default int getInt(Object data) {
			return proxyBitField().getInt(data);
		}

		/**
		 * Gets the int.
		 *
		 * @param data      the data
		 * @param sequences the sequences
		 * @return the int
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#getInt(java.lang.Object,
		 *      long[])
		 */
		@Override
		default int getInt(Object data, long... sequences) {
			return proxyBitField().getInt(data, sequences);
		}

		/**
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#getIntAt(long, java.lang.Object)
		 */
		@Override
		default int getIntAt(long offset, Object data) {
			return proxyBitField().getIntAt(offset, data);
		}

		/**
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#getIntAt(long, java.lang.Object, long[])
		 */
		@Override
		default int getIntAt(long offset, Object data, long... sequences) {
			return proxyBitField().getIntAt(offset, data, sequences);
		}

		/**
		 * Gets the long.
		 *
		 * @param data the data
		 * @return the long
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#getLong(java.lang.Object)
		 */
		@Override
		default long getLong(Object data) {
			return proxyBitField().getLong(data);
		}

		/**
		 * Gets the long.
		 *
		 * @param data      the data
		 * @param sequences the sequences
		 * @return the long
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#getLong(java.lang.Object,
		 *      long[])
		 */
		@Override
		default long getLong(Object data, long... sequences) {
			return proxyBitField().getLong(data, sequences);
		}

		/**
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#getLongAt(long, java.lang.Object)
		 */
		@Override
		default long getLongAt(long offset, Object data) {
			return proxyBitField().getLongAt(offset, data);
		}

		/**
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#getLongAt(long, java.lang.Object, long[])
		 */
		@Override
		default long getLongAt(long offset, Object data, long... sequences) {
			return proxyBitField().getLongAt(offset, data, sequences);
		}

		/**
		 * Gets the number.
		 *
		 * @param data the data
		 * @return the number
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#getNumber(java.lang.Object)
		 */
		@Override
		default Number getNumber(Object data) {
			return proxyBitField().getNumber(data);
		}

		/**
		 * Gets the number.
		 *
		 * @param data      the data
		 * @param sequences the sequences
		 * @return the number
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#getNumber(java.lang.Object,
		 *      long[])
		 */
		@Override
		default Number getNumber(Object data, long... sequences) {
			return proxyBitField().getNumber(data, sequences);
		}

		/**
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#getNumberAt(long, java.lang.Object)
		 */
		@Override
		default Number getNumberAt(long offset, Object data) {
			return proxyBitField().getNumberAt(offset, data);
		}

		/**
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#getNumberAt(long, java.lang.Object, long[])
		 */
		@Override
		default Number getNumberAt(long offset, Object data, long... sequences) {
			return proxyBitField().getNumberAt(offset, data, sequences);
		}

		/**
		 * Gets the short.
		 *
		 * @param data the data
		 * @return the short
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#getShort(java.lang.Object)
		 */
		@Override
		default short getShort(Object data) {
			return proxyBitField().getShort(data);
		}

		/**
		 * Gets the short.
		 *
		 * @param data      the data
		 * @param sequences the sequences
		 * @return the short
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#getShort(java.lang.Object,
		 *      long[])
		 */
		@Override
		default short getShort(Object data, long... sequences) {
			return proxyBitField().getShort(data, sequences);
		}

		/**
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#getShortAt(long, java.lang.Object)
		 */
		@Override
		default short getShortAt(long offset, Object data) {
			return proxyBitField().getShortAt(offset, data);
		}

		/**
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#getShortAt(long, java.lang.Object, long[])
		 */
		@Override
		default short getShortAt(long offset, Object data, long... sequences) {
			return proxyBitField().getShortAt(offset, data, sequences);
		}

		/**
		 * Gets the unsigned byte.
		 *
		 * @param data the data
		 * @return the unsigned byte
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#getUnsignedByte(java.lang.Object)
		 */
		@Override
		default int getUnsignedByte(Object data) {
			return proxyBitField().getUnsignedByte(data);
		}

		/**
		 * Gets the unsigned byte.
		 *
		 * @param data      the data
		 * @param sequences the sequences
		 * @return the unsigned byte
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#getUnsignedByte(java.lang.Object,
		 *      long[])
		 */
		@Override
		default int getUnsignedByte(Object data, long... sequences) {
			return proxyBitField().getUnsignedByte(data, sequences);
		}
		
		/**
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#getUnsignedByteAt(long, java.lang.Object)
		 */
		@Override
		default int getUnsignedByteAt(long offset, Object data) {
			return proxyBitField().getUnsignedByteAt(offset, data);
		}

		/**
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#getUnsignedByteAt(long, java.lang.Object, long[])
		 */
		@Override
		default int getUnsignedByteAt(long offset, Object data, long... sequences) {
			return proxyBitField().getUnsignedByteAt(offset, data, sequences);
		}

		/**
		 * Gets the unsigned int.
		 *
		 * @param data the data
		 * @return the unsigned int
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#getUnsignedInt(java.lang.Object)
		 */
		@Override
		default long getUnsignedInt(Object data) {
			return proxyBitField().getUnsignedInt(data);
		}

		/**
		 * Gets the unsigned int.
		 *
		 * @param data      the data
		 * @param sequences the sequences
		 * @return the unsigned int
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#getUnsignedInt(java.lang.Object,
		 *      long[])
		 */
		@Override
		default long getUnsignedInt(Object data, long... sequences) {
			return proxyBitField().getUnsignedInt(data, sequences);
		}

		/**
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#getUnsignedIntAt(long, java.lang.Object)
		 */
		@Override
		default long getUnsignedIntAt(long offset, Object data) {
			return proxyBitField().getUnsignedIntAt(offset, data);
		}

		/**
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#getUnsignedIntAt(long, java.lang.Object, long[])
		 */
		@Override
		default long getUnsignedIntAt(long offset, Object data, long... sequences) {
			return proxyBitField().getUnsignedIntAt(offset, data, sequences);
		}

		/**
		 * Gets the unsigned number.
		 *
		 * @param data the data
		 * @return the unsigned number
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#getUnsignedNumber(java.lang.Object)
		 */
		@Override
		default Number getUnsignedNumber(Object data) {
			return proxyBitField().getUnsignedNumber(data);
		}

		/**
		 * Gets the unsigned number.
		 *
		 * @param data      the data
		 * @param sequences the sequences
		 * @return the unsigned number
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#getUnsignedNumber(java.lang.Object,
		 *      long[])
		 */
		@Override
		default Number getUnsignedNumber(Object data, long... sequences) {
			return proxyBitField().getUnsignedNumber(data, sequences);
		}

		/**
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#getUnsignedNumberAt(long, java.lang.Object)
		 */
		@Override
		default Number getUnsignedNumberAt(long offset, Object data) {
			return proxyBitField().getUnsignedNumberAt(offset, data);
		}

		/**
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#getUnsignedNumberAt(long, java.lang.Object, long[])
		 */
		@Override
		default Number getUnsignedNumberAt(long offset, Object data, long... sequences) {
			return proxyBitField().getUnsignedNumberAt(offset, data, sequences);
		}

		/**
		 * Gets the unsigned short.
		 *
		 * @param data the data
		 * @return the unsigned short
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#getUnsignedShort(java.lang.Object)
		 */
		@Override
		default int getUnsignedShort(Object data) {
			return proxyBitField().getUnsignedShort(data);
		}

		/**
		 * Gets the unsigned short.
		 *
		 * @param data      the data
		 * @param sequences the sequences
		 * @return the unsigned short
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#getUnsignedShort(java.lang.Object,
		 *      long[])
		 */
		@Override
		default int getUnsignedShort(Object data, long... sequences) {
			return proxyBitField().getUnsignedShort(data, sequences);
		}

		/**
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#getUnsignedShortAt(long, java.lang.Object)
		 */
		@Override
		default int getUnsignedShortAt(long offset, Object data) {
			return proxyBitField().getUnsignedShortAt(offset, data);
		}

		/**
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#getUnsignedShortAt(long, java.lang.Object, long[])
		 */
		@Override
		default int getUnsignedShortAt(long offset, Object data, long... sequences) {
			return proxyBitField().getUnsignedShortAt(offset, data, sequences);
		}

		/**
		 * Layout.
		 *
		 * @return the binary layout
		 * @see com.slytechs.protocol.runtime.internal.layout.BinaryField#layout()
		 */
		@Override
		default BinaryLayout layout() {
			return proxyBitField().layout();
		}

		/**
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#nonProxy()
		 */
		@Override
		default BitField nonProxy() {
			return proxyBitField().nonProxy();
		}


		/**
		 * Order.
		 *
		 * @return the byte order
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#order()
		 */
		@Override
		default ByteOrder order() {
			return proxyBitField().order();
		}

		/**
		 * Order.
		 *
		 * @param newOrder the new order
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#order(java.nio.ByteOrder)
		 */
		@Override
		default void order(ByteOrder newOrder) {
			proxyBitField().order(newOrder);
		}

		/**
		 * Proxy bit field.
		 *
		 * @return the bit field
		 */
		BitField proxyBitField();

		/**
		 * Readonly.
		 *
		 * @return the bit field
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#readonly()
		 */
		@Override
		default BitField readonly() {
			return proxyBitField().readonly();
		}

		/**
		 * Sets the byte.
		 *
		 * @param value the value
		 * @param data  the data
		 * @return the byte
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#setByte(byte,
		 *      java.lang.Object)
		 */
		@Override
		default byte setByte(byte value, Object data) {
			return proxyBitField().setByte(value, data);
		}

		/**
		 * Sets the byte.
		 *
		 * @param value     the value
		 * @param data      the data
		 * @param sequences the sequences
		 * @return the byte
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#setByte(byte,
		 *      java.lang.Object, long[])
		 */
		@Override
		default byte setByte(byte value, Object data, long... sequences) {
			return proxyBitField().setByte(value, data, sequences);
		}

		/**
		 * Sets the double.
		 *
		 * @param value the value
		 * @param data  the data
		 * @return the double
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#setDouble(double,
		 *      java.lang.Object)
		 */
		@Override
		default double setDouble(double value, Object data) {
			return proxyBitField().setDouble(value, data);
		}

		/**
		 * Sets the double.
		 *
		 * @param value     the value
		 * @param data      the data
		 * @param sequences the sequences
		 * @return the double
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#setDouble(double,
		 *      java.lang.Object, long[])
		 */
		@Override
		default double setDouble(double value, Object data, long... sequences) {
			return proxyBitField().setDouble(value, data, sequences);
		}

		/**
		 * Sets the float.
		 *
		 * @param value the value
		 * @param data  the data
		 * @return the float
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#setFloat(float,
		 *      java.lang.Object)
		 */
		@Override
		default float setFloat(float value, Object data) {
			return proxyBitField().setFloat(value, data);
		}

		/**
		 * Sets the float.
		 *
		 * @param value     the value
		 * @param data      the data
		 * @param sequences the sequences
		 * @return the float
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#setFloat(float,
		 *      java.lang.Object, long[])
		 */
		@Override
		default float setFloat(float value, Object data, long... sequences) {
			return proxyBitField().setFloat(value, data, sequences);
		}

		/**
		 * Sets the int.
		 *
		 * @param value the value
		 * @param data  the data
		 * @return the int
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#setInt(int,
		 *      java.lang.Object)
		 */
		@Override
		default int setInt(int value, Object data) {
			return proxyBitField().setInt(value, data);
		}

		/**
		 * Sets the int.
		 *
		 * @param value     the value
		 * @param data      the data
		 * @param sequences the sequences
		 * @return the int
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#setInt(int,
		 *      java.lang.Object, long[])
		 */
		@Override
		default int setInt(int value, Object data, long... sequences) {
			return proxyBitField().setInt(value, data, sequences);
		}

		/**
		 * Sets the long.
		 *
		 * @param value the value
		 * @param data  the data
		 * @return the long
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#setLong(int,
		 *      java.lang.Object)
		 */
		@Override
		default long setLong(long value, Object data) {
			return proxyBitField().setLong(value, data);
		}

		/**
		 * Sets the long.
		 *
		 * @param value     the value
		 * @param data      the data
		 * @param sequences the sequences
		 * @return the long
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#setLong(long,
		 *      java.lang.Object, long[])
		 */
		@Override
		default long setLong(long value, Object data, long... sequences) {
			return proxyBitField().setLong(value, data, sequences);
		}

		/**
		 * Sets the short.
		 *
		 * @param value the value
		 * @param data  the data
		 * @return the short
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#setShort(short,
		 *      java.lang.Object)
		 */
		@Override
		default short setShort(short value, Object data) {
			return proxyBitField().setShort(value, data);
		}

		/**
		 * Sets the short.
		 *
		 * @param value     the value
		 * @param data      the data
		 * @param sequences the sequences
		 * @return the short
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#setShort(short,
		 *      java.lang.Object, long[])
		 */
		@Override
		default short setShort(short value, Object data, long... sequences) {
			return proxyBitField().setShort(value, data, sequences);
		}

		/**
		 * Size.
		 *
		 * @return the optional long
		 * @see com.slytechs.protocol.runtime.internal.layout.BinaryField#size()
		 */
		@Override
		default OptionalLong size() {
			return proxyBitField().size();
		}

		/**
		 * Synchronize.
		 *
		 * @return the bit field
		 * @see com.slytechs.protocol.runtime.internal.layout.BitField#synchronize()
		 */
		@Override
		default BitField synchronize() {
			return proxyBitField().synchronize();
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
		@Override
		default String toString(Object data, long... sequences) {
			return proxyBitField().toString(data, sequences);
		}
	}
	
	/**
	 * Cache.
	 *
	 * @return the bit field
	 */
	BitField cache();
	
	/**
	 * Formatted.
	 *
	 * @return the formatted bit field
	 */
	default FormattedBitField formatted() {
		return new FormattedBitField(nonProxy());
	}
	
	/**
	 * Gets the bit.
	 *
	 * @param data the data
	 * @return the bit
	 */
	boolean getBit(Object data);
	
	/**
	 * Gets the bit.
	 *
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the bit
	 */
	boolean getBit(Object data, long...sequences);
	
	/**
	 * Gets the byte.
	 *
	 * @param data the data
	 * @return the byte
	 */
	byte getByte(Object data);
	
	/**
	 * Gets the byte.
	 *
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the byte
	 */
	byte getByte(Object data, long... sequences);
	
	/**
	 * Gets the byte at.
	 *
	 * @param offset the offset
	 * @param data   the data
	 * @return the byte at
	 */
	byte getByteAt(long offset, Object data);
	
	/**
	 * Gets the byte at.
	 *
	 * @param offset    the offset
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the byte at
	 */
	byte getByteAt(long offset, Object data, long... sequences);
	
	/**
	 * Gets the double.
	 *
	 * @param data the data
	 * @return the double
	 */
	default double getDouble(Object data) { return Double.longBitsToDouble(getLong(data)); }

	/**
	 * Gets the double.
	 *
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the double
	 */
	default double getDouble(Object data, long... sequences) { return Double.longBitsToDouble(getLong(data, sequences)); }
	
	/**
	 * Gets the double at.
	 *
	 * @param data   the data
	 * @param offset the offset
	 * @return the double at
	 */
	default double getDoubleAt(Object data, long offset) { return Double.longBitsToDouble(getLongAt(offset, data)); }
	
	/**
	 * Gets the double at.
	 *
	 * @param data      the data
	 * @param offset    the offset
	 * @param sequences the sequences
	 * @return the double at
	 */
	default double getDoubleAt(Object data, long offset, long... sequences) { return Double.longBitsToDouble(getLongAt(offset, data, sequences)); }

	/**
	 * Gets the float.
	 *
	 * @param data the data
	 * @return the float
	 */
	default float getFloat (Object data) { return Float.intBitsToFloat(getInt(data)); }
	
	/**
	 * Gets the float.
	 *
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the float
	 */
	default float getFloat (Object data, long... sequences) { return Float.floatToIntBits(getInt(data, sequences)); }
	
	/**
	 * Gets the float at.
	 *
	 * @param data   the data
	 * @param offset the offset
	 * @return the float at
	 */
	default float getFloatAt (Object data, long offset) { return Float.intBitsToFloat(getIntAt(offset, data)); }
	
	/**
	 * Gets the float at.
	 *
	 * @param data      the data
	 * @param offset    the offset
	 * @param sequences the sequences
	 * @return the float at
	 */
	default float getFloatAt (Object data, long offset, long... sequences) { return Float.floatToIntBits(getIntAt(offset, data, sequences)); }

	/**
	 * Gets the int.
	 *
	 * @param data the data
	 * @return the int
	 */
	int getInt(Object data);
	
	/**
	 * Gets the int.
	 *
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the int
	 */
	int getInt(Object data, long... sequences);
	
	/**
	 * Gets the int at.
	 *
	 * @param offset the offset
	 * @param data   the data
	 * @return the int at
	 */
	int getIntAt(long offset, Object data);
	
	/**
	 * Gets the int at.
	 *
	 * @param offset    the offset
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the int at
	 */
	int getIntAt(long offset, Object data, long... sequences);

	/**
	 * Gets the long.
	 *
	 * @param data the data
	 * @return the long
	 */
	long getLong(Object data);
	
	/**
	 * Gets the long.
	 *
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the long
	 */
	long getLong(Object data, long... sequences);
	
	/**
	 * Gets the long at.
	 *
	 * @param offset the offset
	 * @param data   the data
	 * @return the long at
	 */
	long getLongAt(long offset, Object data);
	
	/**
	 * Gets the long at.
	 *
	 * @param offset    the offset
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the long at
	 */
	long getLongAt(long offset, Object data, long... sequences);

	/**
	 * Gets the number.
	 *
	 * @param data the data
	 * @return the number
	 */
	Number getNumber(Object data);
	
	/**
	 * Gets the number.
	 *
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the number
	 */
	Number getNumber(Object data, long... sequences);
	
	/**
	 * Gets the number at.
	 *
	 * @param offset the offset
	 * @param data   the data
	 * @return the number at
	 */
	Number getNumberAt(long offset, Object data);
	
	/**
	 * Gets the number at.
	 *
	 * @param offset    the offset
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the number at
	 */
	Number getNumberAt(long offset, Object data, long... sequences);

	/**
	 * Gets the short.
	 *
	 * @param data the data
	 * @return the short
	 */
	short getShort(Object data);
	
	/**
	 * Gets the short.
	 *
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the short
	 */
	short getShort(Object data, long... sequences);
	
	/**
	 * Gets the short at.
	 *
	 * @param offset the offset
	 * @param data   the data
	 * @return the short at
	 */
	short getShortAt(long offset, Object data);
	
	/**
	 * Gets the short at.
	 *
	 * @param offset    the offset
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the short at
	 */
	short getShortAt(long offset, Object data, long... sequences);
	
	/**
	 * Gets the unsigned byte.
	 *
	 * @param data the data
	 * @return the unsigned byte
	 */
	int getUnsignedByte(Object data);
	
	/**
	 * Gets the unsigned byte.
	 *
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the unsigned byte
	 */
	int getUnsignedByte(Object data, long... sequences);
	
	/**
	 * Gets the unsigned byte at.
	 *
	 * @param offset the offset
	 * @param data   the data
	 * @return the unsigned byte at
	 */
	int getUnsignedByteAt(long offset, Object data);
	
	/**
	 * Gets the unsigned byte at.
	 *
	 * @param offset    the offset
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the unsigned byte at
	 */
	int getUnsignedByteAt(long offset, Object data, long... sequences);
	
	/**
	 * Gets the unsigned int.
	 *
	 * @param data the data
	 * @return the unsigned int
	 */
	long getUnsignedInt(Object data);
	
	/**
	 * Gets the unsigned int.
	 *
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the unsigned int
	 */
	long getUnsignedInt(Object data, long... sequences);
	
	/**
	 * Gets the unsigned int at.
	 *
	 * @param offset the offset
	 * @param data   the data
	 * @return the unsigned int at
	 */
	long getUnsignedIntAt(long offset, Object data);
	
	/**
	 * Gets the unsigned int at.
	 *
	 * @param offset    the offset
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the unsigned int at
	 */
	long getUnsignedIntAt(long offset, Object data, long... sequences);

	/**
	 * Gets the unsigned number.
	 *
	 * @param data the data
	 * @return the unsigned number
	 */
	Number getUnsignedNumber(Object data);
	
	/**
	 * Gets the unsigned number.
	 *
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the unsigned number
	 */
	Number getUnsignedNumber(Object data, long... sequences);
	
	/**
	 * Gets the unsigned number at.
	 *
	 * @param offset the offset
	 * @param data   the data
	 * @return the unsigned number at
	 */
	Number getUnsignedNumberAt(long offset, Object data);
	
	/**
	 * Gets the unsigned number at.
	 *
	 * @param offset    the offset
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the unsigned number at
	 */
	Number getUnsignedNumberAt(long offset, Object data, long... sequences);
	
	/**
	 * Gets the unsigned short.
	 *
	 * @param data the data
	 * @return the unsigned short
	 */
	int getUnsignedShort (Object data);
	
	/**
	 * Gets the unsigned short.
	 *
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the unsigned short
	 */
	int getUnsignedShort(Object data, long... sequences);
	
	/**
	 * Gets the unsigned short at.
	 *
	 * @param offset the offset
	 * @param data   the data
	 * @return the unsigned short at
	 */
	int getUnsignedShortAt (long offset, Object data);
	
	/**
	 * Gets the unsigned short at.
	 *
	 * @param offset    the offset
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the unsigned short at
	 */
	int getUnsignedShortAt (long offset, Object data, long... sequences);
	
	/**
	 * Non proxy.
	 *
	 * @return the bit field
	 */
	BitField nonProxy();
	
	/**
	 * Order.
	 *
	 * @return the byte order
	 */
	ByteOrder order();
	
	/**
	 * Order.
	 *
	 * @param newOrder the new order
	 */
	void order(ByteOrder newOrder);
	
	/**
	 * Readonly.
	 *
	 * @return the bit field
	 */
	BitField readonly();
	
	/**
	 * Sets the byte.
	 *
	 * @param value the value
	 * @param data  the data
	 * @return the byte
	 */
	byte setByte(byte value, Object data);
	
	/**
	 * Sets the byte.
	 *
	 * @param value     the value
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the byte
	 */
	byte setByte(byte value, Object data, long... sequences);
	
	/**
	 * Sets the double.
	 *
	 * @param value the value
	 * @param data  the data
	 * @return the double
	 */
	default double setDouble(double value, Object data) { return setLong(Double.doubleToLongBits(value), data); }
	
	/**
	 * Sets the double.
	 *
	 * @param value     the value
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the double
	 */
	default double setDouble(double value, Object data, long... sequences) { return setLong(Double.doubleToLongBits(value), data, sequences); }

	/**
	 * Sets the float.
	 *
	 * @param value the value
	 * @param data  the data
	 * @return the float
	 */
	default float setFloat(float value, Object data) { return setInt(Float.floatToIntBits(value), data); }
	
	/**
	 * Sets the float.
	 *
	 * @param value     the value
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the float
	 */
	default float setFloat (float value, Object data, long... sequences) { return setInt(Float.floatToIntBits(value), data, sequences); }

	/**
	 * Sets the int.
	 *
	 * @param value the value
	 * @param data  the data
	 * @return the int
	 */
	int setInt(int value, Object data);

	/**
	 * Sets the int.
	 *
	 * @param value     the value
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the int
	 */
	int setInt(int value, Object data, long... sequences);

	/**
	 * Sets the long.
	 *
	 * @param value the value
	 * @param data  the data
	 * @return the long
	 */
	long setLong(long value, Object data);

	/**
	 * Sets the long.
	 *
	 * @param value     the value
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the long
	 */
	long setLong(long value, Object data, long... sequences);

	/**
	 * Sets the short.
	 *
	 * @param value the value
	 * @param data  the data
	 * @return the short
	 */
	short setShort(short  value, Object data);
	
	/**
	 * Sets the short.
	 *
	 * @param value     the value
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the short
	 */
	short setShort(short  value, Object data, long... sequences);

	/**
	 * Synchronizes read and write on the data object.
	 *
	 * @return the bit field
	 */
	BitField synchronize();
}
