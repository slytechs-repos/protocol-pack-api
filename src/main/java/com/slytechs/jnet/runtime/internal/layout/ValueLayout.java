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

import java.nio.ByteOrder;
import java.util.Optional;
import java.util.OptionalLong;

import com.slytechs.jnet.runtime.internal.layout.BinaryLayout.HasDataCarrier;
import com.slytechs.jnet.runtime.internal.layout.Path.PathKind;
import com.slytechs.jnet.runtime.util.Bits;

/**
 * <ul>
 * <li>All sizes are in bits
 * </ul>
 * .
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public abstract class ValueLayout extends AbstractLayout implements HasDataCarrier {

	/**
	 * The Class OfAddress.
	 */
	public static class OfAddress extends ValueLayout {
		
		/**
		 * Instantiates a new of address.
		 *
		 * @param order the order
		 */
		OfAddress(ByteOrder order) {
			super(long.class, order, ADDRESS_SIZE, ValueLayout.DEFAULT_ALIGNMENT);
		}

		/**
		 * Instantiates a new of address.
		 *
		 * @param order the order
		 * @param size  the size
		 */
		OfAddress(ByteOrder order, long size) {
			super(long.class, order, size, ValueLayout.DEFAULT_ALIGNMENT);

			if (size != ADDRESS_SIZE)
				throw new IllegalStateException("variable size not supported for address carrier");
		}

		/**
		 * Instantiates a new of address.
		 *
		 * @param order     the order
		 * @param size      the size
		 * @param alignment the alignment
		 * @param name      the name
		 */
		OfAddress(ByteOrder order, long size, int alignment, Optional<String> name) {
			super(long.class, order, size, alignment, name);
		}

		/**
		 * With bit alignment.
		 *
		 * @param alignment the alignment
		 * @return the of address
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#withBitAlignment(long)
		 */
		@Override
		public OfAddress withBitAlignment(int alignment) {
			return new OfAddress(order, size.getAsLong(), alignment, name);
		}

		/**
		 * With name.
		 *
		 * @param name the name
		 * @return the of address
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#withName(java.lang.String)
		 */
		@Override
		public OfAddress withName(String name) {
			return new OfAddress(order, size.getAsLong(), alignment, Optional.ofNullable(name));
		}

		/**
		 * Carrier size.
		 *
		 * @return the long
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#carrierSize()
		 */
		@Override
		public long carrierSize() {
			return ADDRESS_SIZE;
		}
	}

	/**
	 * The Class OfArray.
	 */
	public static class OfArray extends ValueLayout {
		
		/**
		 * Instantiates a new of array.
		 */
		OfArray() {
			super(byte.class, null, OptionalLong.empty());
		}

		/**
		 * Instantiates a new of array.
		 *
		 * @param size the size
		 */
		OfArray(long size) {
			super(byte.class, null, size, ValueLayout.DEFAULT_ALIGNMENT);
		}

		/**
		 * Instantiates a new of array.
		 *
		 * @param size      the size
		 * @param alignment the alignment
		 * @param name      the name
		 */
		OfArray(long size, int alignment, Optional<String> name) {
			super(int.class, null, size, alignment, name);
		}

		/**
		 * With bit alignment.
		 *
		 * @param alignment the alignment
		 * @return the of array
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#withBitAlignment(long)
		 */
		@Override
		public OfArray withBitAlignment(int alignment) {
			return new OfArray(size.getAsLong(), alignment, name);
		}

		/**
		 * With name.
		 *
		 * @param name the name
		 * @return the of array
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#withName(java.lang.String)
		 */
		@Override
		public OfArray withName(String name) {
			return new OfArray(size.getAsLong(), alignment, Optional.ofNullable(name));
		}

		/**
		 * Carrier size.
		 *
		 * @return the long
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#carrierSize()
		 */
		@Override
		public long carrierSize() {
			return bitSize();
		}
	}

	/**
	 * The Class OfByte.
	 */
	public static class OfByte extends ValueLayout {
		
		/**
		 * Instantiates a new of byte.
		 */
		OfByte() {
			super(byte.class, ByteOrder.BIG_ENDIAN, BYTE_SIZE, ValueLayout.DEFAULT_ALIGNMENT);
		}

		/**
		 * Instantiates a new of byte.
		 *
		 * @param size the size
		 */
		OfByte(long size) {
			super(byte.class, ByteOrder.BIG_ENDIAN, size, ValueLayout.DEFAULT_ALIGNMENT);

			if (size > BYTE_SIZE)
				throw new IllegalStateException("carrier too small for size " + size);
		}

		/**
		 * Instantiates a new of byte.
		 *
		 * @param order     the order
		 * @param size      the size
		 * @param alignment the alignment
		 * @param name      the name
		 */
		OfByte(ByteOrder order, long size, int alignment, Optional<String> name) {
			super(byte.class, order, size, alignment, name);
		}

		/**
		 * With bit alignment.
		 *
		 * @param alignment the alignment
		 * @return the of byte
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#withBitAlignment(long)
		 */
		@Override
		public OfByte withBitAlignment(int alignment) {
			return new OfByte(order, size.getAsLong(), alignment, name);
		}

		/**
		 * With name.
		 *
		 * @param name the name
		 * @return the of byte
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#withName(java.lang.String)
		 */
		@Override
		public OfByte withName(String name) {
			return new OfByte(order, size.getAsLong(), alignment, Optional.ofNullable(name));
		}

		/**
		 * Carrier size.
		 *
		 * @return the long
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#carrierSize()
		 */
		@Override
		public long carrierSize() {
			return BYTE_SIZE;
		}
	}

	/**
	 * The Class OfChar.
	 */
	public static class OfChar extends ValueLayout {
		
		/**
		 * Instantiates a new of char.
		 *
		 * @param order the order
		 */
		OfChar(ByteOrder order) {
			super(char.class, order, CHAR_SIZE, ValueLayout.DEFAULT_ALIGNMENT);
		}

		/**
		 * Instantiates a new of char.
		 *
		 * @param order the order
		 * @param size  the size
		 */
		OfChar(ByteOrder order, long size) {
			super(char.class, order, size, ValueLayout.DEFAULT_ALIGNMENT);

			if (size > CHAR_SIZE)
				throw new IllegalStateException("carrier too small for size " + size);
		}

		/**
		 * Instantiates a new of char.
		 *
		 * @param order     the order
		 * @param size      the size
		 * @param alignment the alignment
		 * @param name      the name
		 */
		OfChar(ByteOrder order, long size, int alignment, Optional<String> name) {
			super(char.class, order, size, alignment, name);
		}

		/**
		 * With bit alignment.
		 *
		 * @param alignment the alignment
		 * @return the of char
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#withBitAlignment(long)
		 */
		@Override
		public OfChar withBitAlignment(int alignment) {
			return new OfChar(order, size.getAsLong(), alignment, name);
		}

		/**
		 * With name.
		 *
		 * @param name the name
		 * @return the of char
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#withName(java.lang.String)
		 */
		@Override
		public OfChar withName(String name) {
			return new OfChar(order, size.getAsLong(), alignment, Optional.ofNullable(name));
		}

		/**
		 * Carrier size.
		 *
		 * @return the long
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#carrierSize()
		 */
		@Override
		public long carrierSize() {
			return CHAR_SIZE;
		}
	}

	/**
	 * The Class OfDouble.
	 */
	public static class OfDouble extends ValueLayout {
		
		/**
		 * Instantiates a new of double.
		 *
		 * @param order the order
		 */
		OfDouble(ByteOrder order) {
			super(double.class, order, DOUBLE_SIZE, ValueLayout.DEFAULT_ALIGNMENT);
		}

		/**
		 * Instantiates a new of double.
		 *
		 * @param order the order
		 * @param size  the size
		 */
		OfDouble(ByteOrder order, long size) {
			super(double.class, order, size, ValueLayout.DEFAULT_ALIGNMENT);

			if (size > DOUBLE_SIZE)
				throw new IllegalStateException("carrier too small for size " + size);
		}

		/**
		 * Instantiates a new of double.
		 *
		 * @param order     the order
		 * @param size      the size
		 * @param alignment the alignment
		 * @param name      the name
		 */
		OfDouble(ByteOrder order, long size, int alignment, Optional<String> name) {
			super(double.class, order, DOUBLE_SIZE, alignment, name);
		}

		/**
		 * With bit alignment.
		 *
		 * @param alignment the alignment
		 * @return the of double
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#withBitAlignment(long)
		 */
		@Override
		public OfDouble withBitAlignment(int alignment) {
			return new OfDouble(order, size.getAsLong(), alignment, name);
		}

		/**
		 * With name.
		 *
		 * @param name the name
		 * @return the of double
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#withName(java.lang.String)
		 */
		@Override
		public OfDouble withName(String name) {
			return new OfDouble(order, size.getAsLong(), alignment, Optional.ofNullable(name));
		}

		/**
		 * Carrier size.
		 *
		 * @return the long
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#carrierSize()
		 */
		@Override
		public long carrierSize() {
			return LONG_SIZE;
		}
	}

	/**
	 * The Class OfFloat.
	 */
	public static class OfFloat extends ValueLayout {
		
		/**
		 * Instantiates a new of float.
		 *
		 * @param order the order
		 */
		OfFloat(ByteOrder order) {
			super(float.class, order, FLOAT_SIZE, ValueLayout.DEFAULT_ALIGNMENT);
		}

		/**
		 * Instantiates a new of float.
		 *
		 * @param order the order
		 * @param size  the size
		 */
		OfFloat(ByteOrder order, long size) {
			super(float.class, order, size, ValueLayout.DEFAULT_ALIGNMENT);

			if (size > FLOAT_SIZE)
				throw new IllegalStateException("carrier too small for size " + size);
		}

		/**
		 * Instantiates a new of float.
		 *
		 * @param order     the order
		 * @param size      the size
		 * @param alignment the alignment
		 * @param name      the name
		 */
		OfFloat(ByteOrder order, long size, int alignment, Optional<String> name) {
			super(float.class, order, DOUBLE_SIZE, alignment, name);
		}

		/**
		 * With bit alignment.
		 *
		 * @param alignment the alignment
		 * @return the of float
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#withBitAlignment(long)
		 */
		@Override
		public OfFloat withBitAlignment(int alignment) {
			return new OfFloat(order, size.getAsLong(), alignment, name);
		}

		/**
		 * With name.
		 *
		 * @param name the name
		 * @return the of float
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#withName(java.lang.String)
		 */
		@Override
		public OfFloat withName(String name) {
			return new OfFloat(order, size.getAsLong(), alignment, Optional.ofNullable(name));
		}

		/**
		 * Carrier size.
		 *
		 * @return the long
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#carrierSize()
		 */
		@Override
		public long carrierSize() {
			return INT_SIZE;
		}
	}

	/**
	 * The Class OfInt.
	 */
	public static class OfInt extends ValueLayout {
		
		/**
		 * Instantiates a new of int.
		 *
		 * @param order the order
		 */
		OfInt(ByteOrder order) {
			super(int.class, order, INT_SIZE, ValueLayout.DEFAULT_ALIGNMENT);
		}

		/**
		 * Instantiates a new of int.
		 *
		 * @param order the order
		 * @param size  the size
		 */
		OfInt(ByteOrder order, long size) {
			super(int.class, order, size, ValueLayout.DEFAULT_ALIGNMENT, Optional.empty());

			if (size > INT_SIZE)
				throw new IllegalStateException("carrier too small for size " + size);
		}

		/**
		 * Instantiates a new of int.
		 *
		 * @param order     the order
		 * @param size      the size
		 * @param alignment the alignment
		 * @param name      the name
		 */
		OfInt(ByteOrder order, long size, int alignment, Optional<String> name) {
			super(int.class, order, size, alignment, name);
		}

		/**
		 * With bit alignment.
		 *
		 * @param alignment the alignment
		 * @return the of int
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#withBitAlignment(long)
		 */
		@Override
		public OfInt withBitAlignment(int alignment) {
			return new OfInt(order, size.getAsLong(), alignment, name);
		}

		/**
		 * With name.
		 *
		 * @param name the name
		 * @return the of int
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#withName(java.lang.String)
		 */
		@Override
		public OfInt withName(String name) {
			return new OfInt(order, size.getAsLong(), alignment, Optional.ofNullable(name));
		}

		/**
		 * Carrier size.
		 *
		 * @return the long
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#carrierSize()
		 */
		@Override
		public long carrierSize() {
			return INT_SIZE;
		}

	}

	/**
	 * The Class OfLong.
	 */
	public static class OfLong extends ValueLayout {
		
		/**
		 * Instantiates a new of long.
		 *
		 * @param order the order
		 */
		OfLong(ByteOrder order) {
			super(long.class, order, LONG_SIZE, ValueLayout.DEFAULT_ALIGNMENT);
		}

		/**
		 * Instantiates a new of long.
		 *
		 * @param order the order
		 * @param size  the size
		 */
		OfLong(ByteOrder order, long size) {
			super(long.class, order, size, ValueLayout.DEFAULT_ALIGNMENT);

			if (size > LONG_SIZE)
				throw new IllegalStateException("carrier too small for size " + size);
		}

		/**
		 * Instantiates a new of long.
		 *
		 * @param order     the order
		 * @param size      the size
		 * @param alignment the alignment
		 * @param name      the name
		 */
		OfLong(ByteOrder order, long size, int alignment, Optional<String> name) {
			super(long.class, order, size, alignment, name);
		}

		/**
		 * With bit alignment.
		 *
		 * @param alignment the alignment
		 * @return the of long
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#withBitAlignment(long)
		 */
		@Override
		public OfLong withBitAlignment(int alignment) {
			return new OfLong(order, size.getAsLong(), alignment, name);
		}

		/**
		 * With name.
		 *
		 * @param name the name
		 * @return the of long
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#withName(java.lang.String)
		 */
		@Override
		public OfLong withName(String name) {
			return new OfLong(order, size.getAsLong(), alignment, Optional.ofNullable(name));
		}

		/**
		 * Carrier size.
		 *
		 * @return the long
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#carrierSize()
		 */
		@Override
		public long carrierSize() {
			return LONG_SIZE;
		}
	}

	/**
	 * The Class OfShort.
	 */
	public static class OfShort extends ValueLayout {
		
		/**
		 * Instantiates a new of short.
		 *
		 * @param order the order
		 */
		OfShort(ByteOrder order) {
			super(short.class, order, SHORT_SIZE, ValueLayout.DEFAULT_ALIGNMENT);
		}

		/**
		 * Instantiates a new of short.
		 *
		 * @param order the order
		 * @param size  the size
		 */
		OfShort(ByteOrder order, long size) {
			super(short.class, order, size, ValueLayout.DEFAULT_ALIGNMENT);

			if (size > SHORT_SIZE)
				throw new IllegalStateException("carrier too small for size " + size);
		}

		/**
		 * Instantiates a new of short.
		 *
		 * @param order     the order
		 * @param size      the size
		 * @param alignment the alignment
		 * @param name      the name
		 */
		OfShort(ByteOrder order, long size, int alignment, Optional<String> name) {
			super(short.class, order, size, alignment, name);
		}

		/**
		 * With bit alignment.
		 *
		 * @param alignment the alignment
		 * @return the of short
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#withBitAlignment(long)
		 */
		@Override
		public OfShort withBitAlignment(int alignment) {
			return new OfShort(order, size.getAsLong(), alignment, name);
		}

		/**
		 * With name.
		 *
		 * @param name the name
		 * @return the of short
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#withName(java.lang.String)
		 */
		@Override
		public OfShort withName(String name) {
			return new OfShort(order, size.getAsLong(), alignment, Optional.ofNullable(name));
		}

		/**
		 * Carrier size.
		 *
		 * @return the long
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#carrierSize()
		 */
		@Override
		public long carrierSize() {
			return SHORT_SIZE;
		}
	}

	/** The Constant BYTE_SIZE. */
	public final static long BYTE_SIZE = 8;
	
	/** The Constant SHORT_SIZE. */
	public final static long SHORT_SIZE = 16;
	
	/** The Constant CHAR_SIZE. */
	public final static long CHAR_SIZE = 16;
	
	/** The Constant INT_SIZE. */
	public final static long INT_SIZE = 32;
	
	/** The Constant LONG_SIZE. */
	public final static long LONG_SIZE = 64;
	
	/** The Constant FLOAT_SIZE. */
	public final static long FLOAT_SIZE = 32;
	
	/** The Constant DOUBLE_SIZE. */
	public final static long DOUBLE_SIZE = 64;
	
	/** The Constant ADDRESS_SIZE. */
	public final static long ADDRESS_SIZE = 64;
	
	/** The Constant ARRAY_SIZE. */
	public final static long ARRAY_SIZE = Long.MAX_VALUE;
	
	/** The Constant DEFAULT_ALIGNMENT. */
	public final static int DEFAULT_ALIGNMENT = Bits.ALIGN_08_BITS;

	/** The carrier type. */
	protected final Class<?> carrierType;
	
	/** The carrier offset. */
	protected long carrierOffset;
	
	/** The order. */
	protected final ByteOrder order;

	/**
	 * Instantiates a new value layout.
	 *
	 * @param carrier   the carrier
	 * @param order     the order
	 * @param size      the size
	 * @param alignment the alignment
	 */
	ValueLayout(Class<?> carrier, ByteOrder order, long size, int alignment) {
		this(carrier, order, OptionalLong.of(size), alignment, Optional.empty());
	}

	/**
	 * Instantiates a new value layout.
	 *
	 * @param carrier   the carrier
	 * @param order     the order
	 * @param size      the size
	 * @param alignment the alignment
	 * @param name      the name
	 */
	ValueLayout(Class<?> carrier, ByteOrder order, long size, int alignment, Optional<String> name) {
		this(carrier, order, OptionalLong.of(size), alignment, name);
	}

	/**
	 * Instantiates a new value layout.
	 *
	 * @param carrier the carrier
	 * @param order   the order
	 * @param size    the size
	 */
	ValueLayout(Class<?> carrier, ByteOrder order, OptionalLong size) {
		this(carrier, order, size, Bits.ALIGN_08_BITS, Optional.empty());
	}

	/**
	 * Instantiates a new value layout.
	 *
	 * @param carrier   the carrier
	 * @param order     the order
	 * @param size      the size
	 * @param alignment the alignment
	 * @param name      the name
	 */
	ValueLayout(Class<?> carrier, ByteOrder order, OptionalLong size, int alignment, Optional<String> name) {
		super(PathKind.VALUE, size, alignment, name);
		this.carrierType = carrier;
		this.order = order;
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout.HasDataCarrier#carrierType()
	 */
	@Override
	public Class<?> carrierType() {
		return this.carrierType;
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout.HasDataCarrier#carrierOffset()
	 */
	@Override
	public long carrierOffset() {
		return carrierOffset;
	}

	/**
	 * Order.
	 *
	 * @return the byte order
	 */
	public ByteOrder order() {
		return this.order;
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#withBitAlignment(int)
	 */
	@Override
	public ValueLayout withBitAlignment(int alignment) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#withName(java.lang.String)
	 */
	@Override
	public ValueLayout withName(String name) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

}
