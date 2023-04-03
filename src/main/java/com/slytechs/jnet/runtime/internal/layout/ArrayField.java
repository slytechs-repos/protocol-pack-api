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

import java.util.Optional;
import java.util.OptionalLong;

import com.slytechs.jnet.runtime.util.ByteArray;

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

		@Override
		default byte[] getByteArrayAt(long byteOffset, byte[] array, int offset, int length, Object data) {
			return proxyArrayField().getByteArrayAt(byteOffset, array, offset, length, data);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryField#bitOffset()
		 */
		default long bitOffset() {
			return proxyArrayField().bitOffset();
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryField#bitOffset(long[])
		 */
		default long bitOffset(long... sequences) {
			return proxyArrayField().bitOffset(sequences);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryField#bitSize()
		 */
		default long bitSize() {
			return proxyArrayField().bitSize();
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryField#byteOffset()
		 */
		default long byteOffset() {
			return proxyArrayField().byteOffset();
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryField#byteOffset(long[])
		 */
		default long byteOffset(long... sequences) {
			return proxyArrayField().byteOffset(sequences);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryField#fieldName()
		 */
		default Optional<String> fieldName() {
			return proxyArrayField().fieldName();
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField#getByteArray(byte[], int, int,
		 *      java.lang.Object)
		 */
		default byte[] getByteArray(byte[] array, int offset, int length, Object data) {
			return proxyArrayField().getByteArray(array, offset, length, data);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField#getByteArray(byte[], int, int,
		 *      java.lang.Object, long[])
		 */
		default byte[] getByteArray(byte[] array, int offset, int length, Object data, long... sequences) {
			return proxyArrayField().getByteArray(array, offset, length, data, sequences);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField#getByteArray(byte[], java.lang.Object)
		 */
		default byte[] getByteArray(byte[] array, Object data) {
			return proxyArrayField().getByteArray(array, data);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField#getByteArray(byte[], java.lang.Object,
		 *      long[])
		 */
		default byte[] getByteArray(byte[] array, Object data, long... sequences) {
			return proxyArrayField().getByteArray(array, data, sequences);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField#getByteArray(org.jnet.buffer.ByteArray,
		 *      java.lang.Object)
		 */
		default ByteArray getByteArray(ByteArray array, Object data) {
			return proxyArrayField().getByteArray(array, data);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField#getByteArray(org.jnet.buffer.ByteArray,
		 *      java.lang.Object, long[])
		 */
		default ByteArray getByteArray(ByteArray array, Object data, long... sequences) {
			return proxyArrayField().getByteArray(array, data, sequences);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField#getByteArray(java.lang.Object)
		 */
		default byte[] getByteArray(Object data) {
			return proxyArrayField().getByteArray(data);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField#getByteArray(java.lang.Object, long[])
		 */
		default byte[] getByteArray(Object data, long... sequences) {
			return proxyArrayField().getByteArray(data, sequences);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField#getIntArray(int[], int, int,
		 *      java.lang.Object)
		 */
		default int[] getIntArray(int[] array, int offset, int length, Object data) {
			return proxyArrayField().getIntArray(array, offset, length, data);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField#getIntArray(int[], int, int,
		 *      java.lang.Object, long[])
		 */
		default int[] getIntArray(int[] array, int offset, int length, Object data, long... sequences) {
			return proxyArrayField().getIntArray(array, offset, length, data, sequences);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField#getIntArray(int[], java.lang.Object)
		 */
		default int[] getIntArray(int[] array, Object data) {
			return proxyArrayField().getIntArray(array, data);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField#getIntArray(int[], java.lang.Object, long[])
		 */
		default int[] getIntArray(int[] array, Object data, long... sequences) {
			return proxyArrayField().getIntArray(array, data, sequences);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField#getIntArray(java.lang.Object)
		 */
		default int[] getIntArray(Object data) {
			return proxyArrayField().getIntArray(data);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField#getIntArray(java.lang.Object, long[])
		 */
		default int[] getIntArray(Object data, long... sequences) {
			return proxyArrayField().getIntArray(data, sequences);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField#getLongArray(long[], int, int,
		 *      java.lang.Object)
		 */
		default long[] getLongArray(long[] array, int offset, int length, Object data) {
			return proxyArrayField().getLongArray(array, offset, length, data);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField#getLongArray(long[], int, int,
		 *      java.lang.Object, long[])
		 */
		default long[] getLongArray(long[] array, int offset, int length, Object data, long... sequences) {
			return proxyArrayField().getLongArray(array, offset, length, data, sequences);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField#getLongArray(long[], java.lang.Object)
		 */
		default long[] getLongArray(long[] array, Object data) {
			return proxyArrayField().getLongArray(array, data);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField#getLongArray(long[], java.lang.Object,
		 *      long[])
		 */
		default long[] getLongArray(long[] array, Object data, long... sequences) {
			return proxyArrayField().getLongArray(array, data, sequences);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField#getLongArray(java.lang.Object)
		 */
		default long[] getLongArray(Object data) {
			return proxyArrayField().getLongArray(data);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField#getLongArray(java.lang.Object, long[])
		 */
		default long[] getLongArray(Object data, long... sequences) {
			return proxyArrayField().getLongArray(data, sequences);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField#getShortArray(java.lang.Object)
		 */
		default short[] getShortArray(Object data) {
			return proxyArrayField().getShortArray(data);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField#getShortArray(java.lang.Object, long[])
		 */
		default short[] getShortArray(Object data, long... sequences) {
			return proxyArrayField().getShortArray(data, sequences);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField#getShortArray(short[], int, int,
		 *      java.lang.Object)
		 */
		default short[] getShortArray(short[] array, int offset, int length, Object data) {
			return proxyArrayField().getShortArray(array, offset, length, data);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField#getShortArray(short[], int, int,
		 *      java.lang.Object, long[])
		 */
		default short[] getShortArray(short[] array, int offset, int length, Object data, long... sequences) {
			return proxyArrayField().getShortArray(array, offset, length, data, sequences);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField#getShortArray(short[], java.lang.Object)
		 */
		default short[] getShortArray(short[] array, Object data) {
			return proxyArrayField().getShortArray(array, data);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField#getShortArray(short[], java.lang.Object,
		 *      long[])
		 */
		default short[] getShortArray(short[] array, Object data, long... sequences) {
			return proxyArrayField().getShortArray(array, data, sequences);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryField#layout()
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
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField#setByteArray(byte[], int, int,
		 *      java.lang.Object)
		 */
		default byte[] setByteArray(byte[] array, int offset, int length, Object data) {
			return proxyArrayField().setByteArray(array, offset, length, data);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField#setByteArray(byte[], int, int,
		 *      java.lang.Object, long[])
		 */
		default byte[] setByteArray(byte[] array, int offset, int length, Object data, long... sequences) {
			return proxyArrayField().setByteArray(array, offset, length, data, sequences);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField#setByteArray(byte[], java.lang.Object)
		 */
		default byte[] setByteArray(byte[] array, Object data) {
			return proxyArrayField().setByteArray(array, data);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField#setByteArray(byte[], java.lang.Object,
		 *      long[])
		 */
		default byte[] setByteArray(byte[] array, Object data, long... sequences) {
			return proxyArrayField().setByteArray(array, data, sequences);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField#setByteArray(org.jnet.buffer.ByteArray,
		 *      java.lang.Object)
		 */
		default ByteArray setByteArray(ByteArray array, Object data) {
			return proxyArrayField().setByteArray(array, data);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField#setByteArray(org.jnet.buffer.ByteArray,
		 *      java.lang.Object, long[])
		 */
		default ByteArray setByteArray(ByteArray array, Object data, long... sequences) {
			return proxyArrayField().setByteArray(array, data, sequences);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField#setIntArray(int[], int, int,
		 *      java.lang.Object)
		 */
		default int[] setIntArray(int[] array, int offset, int length, Object data) {
			return proxyArrayField().setIntArray(array, offset, length, data);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField#setIntArray(int[], int, int,
		 *      java.lang.Object, long[])
		 */
		default int[] setIntArray(int[] array, int offset, int length, Object data, long... sequences) {
			return proxyArrayField().setIntArray(array, offset, length, data, sequences);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField#setIntArray(int[], java.lang.Object)
		 */
		default int[] setIntArray(int[] array, Object data) {
			return proxyArrayField().setIntArray(array, data);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField#setIntArray(int[], java.lang.Object, long[])
		 */
		default int[] setIntArray(int[] array, Object data, long... sequences) {
			return proxyArrayField().setIntArray(array, data, sequences);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField#setLongArray(long[], int, int,
		 *      java.lang.Object)
		 */
		default long[] setLongArray(long[] array, int offset, int length, Object data) {
			return proxyArrayField().setLongArray(array, offset, length, data);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField#setLongArray(long[], int, int,
		 *      java.lang.Object, long[])
		 */
		default long[] setLongArray(long[] array, int offset, int length, Object data, long... sequences) {
			return proxyArrayField().setLongArray(array, offset, length, data, sequences);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField#setLongArray(long[], java.lang.Object)
		 */
		default long[] setLongArray(long[] array, Object data) {
			return proxyArrayField().setLongArray(array, data);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField#setLongArray(long[], java.lang.Object,
		 *      long[])
		 */
		default long[] setLongArray(long[] array, Object data, long... sequences) {
			return proxyArrayField().setLongArray(array, data, sequences);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField#setShortArray(short[], int, int,
		 *      java.lang.Object)
		 */
		default short[] setShortArray(short[] array, int offset, int length, Object data) {
			return proxyArrayField().setShortArray(array, offset, length, data);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField#setShortArray(short[], int, int,
		 *      java.lang.Object, long[])
		 */
		default short[] setShortArray(short[] array, int offset, int length, Object data, long... sequences) {
			return proxyArrayField().setShortArray(array, offset, length, data, sequences);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField#setShortArray(short[], java.lang.Object)
		 */
		default short[] setShortArray(short[] array, Object data) {
			return proxyArrayField().setShortArray(array, data);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField#setShortArray(short[], java.lang.Object,
		 *      long[])
		 */
		default short[] setShortArray(short[] array, Object data, long... sequences) {
			return proxyArrayField().setShortArray(array, data, sequences);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryField#size()
		 */
		default OptionalLong size() {
			return proxyArrayField().size();
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryField#toString(java.lang.Object, long[])
		 */
		default String toString(Object data, long... sequences) {
			return proxyArrayField().toString(data, sequences);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField#wrap(byte[])
		 */
		default ByteArray wrap(byte[] data) {
			return proxyArrayField().wrap(data);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField#wrap(byte[], long[])
		 */
		default ByteArray wrap(byte[] data, long... sequences) {
			return proxyArrayField().wrap(data, sequences);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField#wrapConsumer(org.jnet.buffer.ByteArray)
		 */
		default ByteArray wrap(ByteArray data) {
			return proxyArrayField().wrap(data);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField#wrap(org.jnet.buffer.ByteArray, long[])
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
		 * @see com.slytechs.jnet.runtime.internal.layout.BitField.Proxy#proxyBitField()
		 */
		@Override
		public ArrayField proxyArrayField() {
			return proxy;
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.ArrayField.Proxy#toString(java.lang.Object, long[])
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
