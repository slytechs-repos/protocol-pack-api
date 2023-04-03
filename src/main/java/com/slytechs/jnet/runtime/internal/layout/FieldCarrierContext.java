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
import java.util.Objects;

import com.slytechs.jnet.runtime.util.Bits;
import com.slytechs.jnet.runtime.util.collection.ArrayListLong;
import com.slytechs.jnet.runtime.util.collection.ListLong;

/**
 * The Class FieldCarrierContext.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
abstract class FieldCarrierContext {

	/**
	 * The Class CarrierBuilder.
	 *
	 * @param <T> the generic type
	 */
	static abstract class CarrierBuilder<T> {

		/** The name. */
		protected String name;

		/** The carrier size. */
		public long carrierSize;

		/** The carrier offset. */
		public long carrierOffset;

		/** The align. */
		public long align;

		/** The binary layout. */
		public BinaryLayout binaryLayout;

		/** The strides. */
		public final ListLong strides = new ArrayListLong();

		protected Path[] elements;
		protected int elementIndex;

		/**
		 * Instantiates a new carrier builder.
		 *
		 * @param name the name
		 */
		public CarrierBuilder(String name) {
			this.name = name;
			this.elements = new Path[0];
		}

		public CarrierBuilder(int index, Path[] elements) {
			this.elements = elements;
			this.elementIndex = index;
		}

		/**
		 * Checks if is name match.
		 *
		 * @param value the value
		 * @return true, if is name match
		 */
		public final boolean isPartialMatch(BinaryLayout value) {
//			return value.layoutName().filter(name::equals).isPresent();

			if (true
					&& (elements.length != 0)
					&& (elementIndex < elements.length)
					&& elements[elementIndex].isMatch((AbstractLayout) value)) {
				elementIndex++;
			}

			return (elements.length <= elementIndex);
		}

		/**
		 * From layout.
		 *
		 * @param layout the layout
		 * @param offset the offset
		 * @return the t
		 */
		public abstract T fromLayout(StructLayout layout, long offset);

		/**
		 * From layout.
		 *
		 * @param layout the layout
		 * @param offset the offset
		 * @return the t
		 */
		public abstract T fromLayout(UnionLayout layout, long offset);

		/**
		 * From layout.
		 *
		 * @param layout the layout
		 * @param offset the offset
		 * @return the t
		 */
		public abstract T fromLayout(SequenceLayout layout, long offset);

		/**
		 * From layout.
		 *
		 * @param layout the layout
		 * @param offset the offset
		 * @return the t
		 */
		public abstract T fromLayout(ValueLayout layout, long offset);

		/**
		 * From layout.
		 *
		 * @param layout the layout
		 * @param offset the offset
		 * @return the t
		 */
		public final T fromLayout(BinaryLayout layout, long offset) {
			return switch (layout) {
			// @formatter:off
			case StructLayout   l -> fromLayout(l, offset);
			case UnionLayout    l -> fromLayout(l, offset);
			case SequenceLayout l -> fromLayout(l, offset);
			case ValueLayout    l -> fromLayout(l, offset);
			case PaddingLayout  l -> null;
			// @formatter:on

			default -> throw new IllegalStateException("layout not supported " + layout.getClass());
			};
		}
	}

	/**
	 * Instantiates a new field carrier context.
	 *
	 * @param binaryLayout the binary layout
	 * @param carrierSize  the carrier size
	 * @param align        the align
	 */
	public FieldCarrierContext(BinaryLayout binaryLayout, long carrierSize, long align) {
		super();
		this.binaryLayout = Objects.requireNonNull(binaryLayout, "binaryLayout");
		this.carrierSize = carrierSize;
		this.align = align;

		assert (carrierSize > 0) : "carrier size " + carrierSize;
		assert (align > 0) : "align " + align;
	}

	/** The carrier size. */
	protected final long carrierSize;

	/** The align. */
	protected final long align;

	/** The strides. */
	protected final ListLong strides = new ArrayListLong();

	/** The binary layout. */
	protected final BinaryLayout binaryLayout;

	/** The order. */
	protected ByteOrder order = ByteOrder.BIG_ENDIAN;

	/**
	 * Binary layout.
	 *
	 * @return the binary layout
	 */
	public BinaryLayout binaryLayout() {
		return this.binaryLayout;
	}

	/**
	 * Order.
	 *
	 * @return the byte order
	 */
	public final ByteOrder order() {
		return order;
	}

	/**
	 * Order.
	 *
	 * @param newOrder the new order
	 */
	public final void order(ByteOrder newOrder) {
		this.order = newOrder;
	}

	/**
	 * Carrier size.
	 *
	 * @return the long
	 */
	public final long carrierSize() {
		return carrierSize;
	}

	/**
	 * Carrier offset.
	 *
	 * @param strideOffset the stride offset
	 * @return the long
	 */
	public abstract long carrierOffset(long strideOffset);

	/**
	 * Carrier byte offset.
	 *
	 * @param strideOffset the stride offset
	 * @return the long
	 */
	public long carrierByteOffset(long strideOffset) {
		return carrierOffset(strideOffset) >> Bits.SHIFT_BITS_TO_BYTES;
	}

	/**
	 * Check alignment.
	 *
	 * @param offset the offset
	 * @return the long
	 */
	public final long checkAlignment(long offset) {
		return offset;
	}

	/**
	 * Align.
	 *
	 * @return the long
	 */
	public final long align() {
		return align;
	}

	/**
	 * Stride offset.
	 *
	 * @param sequences the sequences
	 * @return the long
	 */
	public final long strideOffset(long[] sequences) {
		long offset = 0;
		for (int i = 0; i < sequences.length; i++)
			offset += (sequences[i] * strides.get(i));

		return offset;
	}

	/**
	 * Insert first.
	 *
	 * @param stride the stride
	 */
	public final void insertFirst(long stride) {
		strides.addLong(0, stride);
	}
}
