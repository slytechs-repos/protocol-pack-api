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
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryField#bitOffset()
		 */
		@Override
		default long bitOffset() {
			return proxyBitField().bitOffset();
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryField#bitOffset(long[])
		 */
		@Override
		default long bitOffset(long...sequences) {
			return proxyBitField().bitOffset(sequences);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BitField#cache()
		 */
		@Override
		default BitField cache() {
			return proxyBitField().cache();
		}

		/**
		 * @see java.lang.constant.Constable#describeConstable()
		 */
		@Override
		default Optional<? extends ConstantDesc> describeConstable() {
			return proxyBitField().describeConstable();
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryField#fieldName()
		 */
		@Override
		default Optional<String> fieldName() {
			return proxyBitField().fieldName();
		}

		@Override
		default boolean getBit(Object data) {
			return proxyBitField().getBit(data);
		}

		@Override
		default boolean getBit(Object data, long... sequences) {
			return proxyBitField().getBit(data, sequences);
			
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BitField#getByte(java.lang.Object)
		 */
		@Override
		default byte getByte(Object data) {
			return proxyBitField().getByte(data);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BitField#getByte(java.lang.Object, long[])
		 */
		@Override
		default byte getByte(Object data, long... sequences) {
			return proxyBitField().getByte(data, sequences);
		}

		@Override
		default byte getByteAt(long offset, Object data) {
			return proxyBitField().getByteAt(offset, data);
		}

		@Override
		default byte getByteAt(long offset, Object data, long... sequences) {
			return proxyBitField().getByteAt(offset, data, sequences);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BitField#getDouble(java.lang.Object)
		 */
		@Override
		default double getDouble(Object data) {
			return proxyBitField().getDouble(data);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BitField#getDouble(java.lang.Object, long[])
		 */
		@Override
		default double getDouble(Object data, long... sequences) {
			return proxyBitField().getDouble(data, sequences);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BitField#getFloat(java.lang.Object)
		 */
		@Override
		default float getFloat(Object data) {
			return proxyBitField().getFloat(data);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BitField#getFloat(java.lang.Object, long[])
		 */
		@Override
		default float getFloat(Object data, long... sequences) {
			return proxyBitField().getFloat(data, sequences);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BitField#getInt(java.lang.Object)
		 */
		@Override
		default int getInt(Object data) {
			return proxyBitField().getInt(data);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BitField#getInt(java.lang.Object, long[])
		 */
		@Override
		default int getInt(Object data, long... sequences) {
			return proxyBitField().getInt(data, sequences);
		}

		@Override
		default int getIntAt(long offset, Object data) {
			return proxyBitField().getIntAt(offset, data);
		}

		@Override
		default int getIntAt(long offset, Object data, long... sequences) {
			return proxyBitField().getIntAt(offset, data, sequences);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BitField#getLong(java.lang.Object)
		 */
		@Override
		default long getLong(Object data) {
			return proxyBitField().getLong(data);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BitField#getLong(java.lang.Object, long[])
		 */
		@Override
		default long getLong(Object data, long... sequences) {
			return proxyBitField().getLong(data, sequences);
		}

		@Override
		default long getLongAt(long offset, Object data) {
			return proxyBitField().getLongAt(offset, data);
		}

		@Override
		default long getLongAt(long offset, Object data, long... sequences) {
			return proxyBitField().getLongAt(offset, data, sequences);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BitField#getNumber(java.lang.Object)
		 */
		@Override
		default Number getNumber(Object data) {
			return proxyBitField().getNumber(data);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BitField#getNumber(java.lang.Object, long[])
		 */
		@Override
		default Number getNumber(Object data, long... sequences) {
			return proxyBitField().getNumber(data, sequences);
		}

		@Override
		default Number getNumberAt(long offset, Object data) {
			return proxyBitField().getNumberAt(offset, data);
		}

		@Override
		default Number getNumberAt(long offset, Object data, long... sequences) {
			return proxyBitField().getNumberAt(offset, data, sequences);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BitField#getShort(java.lang.Object)
		 */
		@Override
		default short getShort(Object data) {
			return proxyBitField().getShort(data);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BitField#getShort(java.lang.Object, long[])
		 */
		@Override
		default short getShort(Object data, long... sequences) {
			return proxyBitField().getShort(data, sequences);
		}

		@Override
		default short getShortAt(long offset, Object data) {
			return proxyBitField().getShortAt(offset, data);
		}

		@Override
		default short getShortAt(long offset, Object data, long... sequences) {
			return proxyBitField().getShortAt(offset, data, sequences);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BitField#getUnsignedByte(java.lang.Object)
		 */
		@Override
		default int getUnsignedByte(Object data) {
			return proxyBitField().getUnsignedByte(data);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BitField#getUnsignedByte(java.lang.Object, long[])
		 */
		@Override
		default int getUnsignedByte(Object data, long... sequences) {
			return proxyBitField().getUnsignedByte(data, sequences);
		}
		
		@Override
		default int getUnsignedByteAt(long offset, Object data) {
			return proxyBitField().getUnsignedByteAt(offset, data);
		}

		@Override
		default int getUnsignedByteAt(long offset, Object data, long... sequences) {
			return proxyBitField().getUnsignedByteAt(offset, data, sequences);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BitField#getUnsignedInt(java.lang.Object)
		 */
		@Override
		default long getUnsignedInt(Object data) {
			return proxyBitField().getUnsignedInt(data);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BitField#getUnsignedInt(java.lang.Object, long[])
		 */
		@Override
		default long getUnsignedInt(Object data, long... sequences) {
			return proxyBitField().getUnsignedInt(data, sequences);
		}

		@Override
		default long getUnsignedIntAt(long offset, Object data) {
			return proxyBitField().getUnsignedIntAt(offset, data);
		}

		@Override
		default long getUnsignedIntAt(long offset, Object data, long... sequences) {
			return proxyBitField().getUnsignedIntAt(offset, data, sequences);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BitField#getUnsignedNumber(java.lang.Object)
		 */
		@Override
		default Number getUnsignedNumber(Object data) {
			return proxyBitField().getUnsignedNumber(data);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BitField#getUnsignedNumber(java.lang.Object, long[])
		 */
		@Override
		default Number getUnsignedNumber(Object data, long... sequences) {
			return proxyBitField().getUnsignedNumber(data, sequences);
		}

		@Override
		default Number getUnsignedNumberAt(long offset, Object data) {
			return proxyBitField().getUnsignedNumberAt(offset, data);
		}

		@Override
		default Number getUnsignedNumberAt(long offset, Object data, long... sequences) {
			return proxyBitField().getUnsignedNumberAt(offset, data, sequences);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BitField#getUnsignedShort(java.lang.Object)
		 */
		@Override
		default int getUnsignedShort(Object data) {
			return proxyBitField().getUnsignedShort(data);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BitField#getUnsignedShort(java.lang.Object, long[])
		 */
		@Override
		default int getUnsignedShort(Object data, long... sequences) {
			return proxyBitField().getUnsignedShort(data, sequences);
		}

		@Override
		default int getUnsignedShortAt(long offset, Object data) {
			return proxyBitField().getUnsignedShortAt(offset, data);
		}

		@Override
		default int getUnsignedShortAt(long offset, Object data, long... sequences) {
			return proxyBitField().getUnsignedShortAt(offset, data, sequences);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryField#layout()
		 */
		@Override
		default BinaryLayout layout() {
			return proxyBitField().layout();
		}

		@Override
		default BitField nonProxy() {
			return proxyBitField().nonProxy();
		}


		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BitField#order()
		 */
		@Override
		default ByteOrder order() {
			return proxyBitField().order();
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BitField#order(java.nio.ByteOrder)
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
		 * @see com.slytechs.jnet.runtime.internal.layout.BitField#readonly()
		 */
		@Override
		default BitField readonly() {
			return proxyBitField().readonly();
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BitField#setByte(byte, java.lang.Object)
		 */
		@Override
		default byte setByte(byte value, Object data) {
			return proxyBitField().setByte(value, data);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BitField#setByte(byte, java.lang.Object, long[])
		 */
		@Override
		default byte setByte(byte value, Object data, long... sequences) {
			return proxyBitField().setByte(value, data, sequences);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BitField#setDouble(double, java.lang.Object)
		 */
		@Override
		default double setDouble(double value, Object data) {
			return proxyBitField().setDouble(value, data);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BitField#setDouble(double, java.lang.Object, long[])
		 */
		@Override
		default double setDouble(double value, Object data, long... sequences) {
			return proxyBitField().setDouble(value, data, sequences);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BitField#setFloat(float, java.lang.Object)
		 */
		@Override
		default float setFloat(float value, Object data) {
			return proxyBitField().setFloat(value, data);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BitField#setFloat(float, java.lang.Object, long[])
		 */
		@Override
		default float setFloat(float value, Object data, long... sequences) {
			return proxyBitField().setFloat(value, data, sequences);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BitField#setInt(int, java.lang.Object)
		 */
		@Override
		default int setInt(int value, Object data) {
			return proxyBitField().setInt(value, data);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BitField#setInt(int, java.lang.Object, long[])
		 */
		@Override
		default int setInt(int value, Object data, long... sequences) {
			return proxyBitField().setInt(value, data, sequences);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BitField#setLong(int, java.lang.Object)
		 */
		@Override
		default long setLong(long value, Object data) {
			return proxyBitField().setLong(value, data);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BitField#setLong(long, java.lang.Object, long[])
		 */
		@Override
		default long setLong(long value, Object data, long... sequences) {
			return proxyBitField().setLong(value, data, sequences);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BitField#setShort(short, java.lang.Object)
		 */
		@Override
		default short setShort(short value, Object data) {
			return proxyBitField().setShort(value, data);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BitField#setShort(short, java.lang.Object, long[])
		 */
		@Override
		default short setShort(short value, Object data, long... sequences) {
			return proxyBitField().setShort(value, data, sequences);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryField#size()
		 */
		@Override
		default OptionalLong size() {
			return proxyBitField().size();
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BitField#synchronize()
		 */
		@Override
		default BitField synchronize() {
			return proxyBitField().synchronize();
		}
		
		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryField#toString(java.lang.Object, long[])
		 */
		@Override
		default String toString(Object data, long... sequences) {
			return proxyBitField().toString(data, sequences);
		}
	}
	
	BitField cache();
	
	default FormattedBitField formatted() {
		return new FormattedBitField(nonProxy());
	}
	
	boolean getBit(Object data);
	
	boolean getBit(Object data, long...sequences);
	
	byte getByte(Object data);
	
	byte getByte(Object data, long... sequences);
	
	byte getByteAt(long offset, Object data);
	
	byte getByteAt(long offset, Object data, long... sequences);
	
	default double getDouble(Object data) { return Double.longBitsToDouble(getLong(data)); }

	default double getDouble(Object data, long... sequences) { return Double.longBitsToDouble(getLong(data, sequences)); }
	
	default double getDoubleAt(Object data, long offset) { return Double.longBitsToDouble(getLongAt(offset, data)); }
	
	default double getDoubleAt(Object data, long offset, long... sequences) { return Double.longBitsToDouble(getLongAt(offset, data, sequences)); }

	default float getFloat (Object data) { return Float.intBitsToFloat(getInt(data)); }
	
	default float getFloat (Object data, long... sequences) { return Float.floatToIntBits(getInt(data, sequences)); }
	
	default float getFloatAt (Object data, long offset) { return Float.intBitsToFloat(getIntAt(offset, data)); }
	
	default float getFloatAt (Object data, long offset, long... sequences) { return Float.floatToIntBits(getIntAt(offset, data, sequences)); }

	int getInt(Object data);
	
	int getInt(Object data, long... sequences);
	
	int getIntAt(long offset, Object data);
	
	int getIntAt(long offset, Object data, long... sequences);

	long getLong(Object data);
	
	long getLong(Object data, long... sequences);
	
	long getLongAt(long offset, Object data);
	
	long getLongAt(long offset, Object data, long... sequences);

	Number getNumber(Object data);
	
	Number getNumber(Object data, long... sequences);
	
	Number getNumberAt(long offset, Object data);
	
	Number getNumberAt(long offset, Object data, long... sequences);

	short getShort(Object data);
	
	short getShort(Object data, long... sequences);
	
	short getShortAt(long offset, Object data);
	
	short getShortAt(long offset, Object data, long... sequences);
	
	int getUnsignedByte(Object data);
	
	int getUnsignedByte(Object data, long... sequences);
	
	int getUnsignedByteAt(long offset, Object data);
	
	int getUnsignedByteAt(long offset, Object data, long... sequences);
	
	long getUnsignedInt(Object data);
	
	long getUnsignedInt(Object data, long... sequences);
	
	long getUnsignedIntAt(long offset, Object data);
	
	long getUnsignedIntAt(long offset, Object data, long... sequences);

	Number getUnsignedNumber(Object data);
	
	Number getUnsignedNumber(Object data, long... sequences);
	
	Number getUnsignedNumberAt(long offset, Object data);
	
	Number getUnsignedNumberAt(long offset, Object data, long... sequences);
	
	int getUnsignedShort (Object data);
	
	int getUnsignedShort(Object data, long... sequences);
	
	int getUnsignedShortAt (long offset, Object data);
	
	int getUnsignedShortAt (long offset, Object data, long... sequences);
	
	BitField nonProxy();
	
	ByteOrder order();
	
	void order(ByteOrder newOrder);
	
	BitField readonly();
	
	byte setByte(byte value, Object data);
	
	byte setByte(byte value, Object data, long... sequences);
	
	default double setDouble(double value, Object data) { return setLong(Double.doubleToLongBits(value), data); }
	
	default double setDouble(double value, Object data, long... sequences) { return setLong(Double.doubleToLongBits(value), data, sequences); }

	default float setFloat(float value, Object data) { return setInt(Float.floatToIntBits(value), data); }
	
	default float setFloat (float value, Object data, long... sequences) { return setInt(Float.floatToIntBits(value), data, sequences); }

	int setInt(int value, Object data);

	int setInt(int value, Object data, long... sequences);

	long setLong(long value, Object data);

	long setLong(long value, Object data, long... sequences);

	short setShort(short  value, Object data);
	
	short setShort(short  value, Object data, long... sequences);

	/**
	 * Synchronizes read and write on the data object.
	 *
	 * @return the bit field
	 */
	BitField synchronize();
}
