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

import static com.slytechs.jnet.runtime.util.Bits.*;

import java.nio.ByteOrder;

/**
 * The Class BitFieldContext.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
abstract class BitFieldContext extends FieldCarrierContext {

	/**
	 * The Class BitFieldBuilder.
	 */
	static class BitFieldBuilder extends FieldCarrierContext.CarrierBuilder<BitFieldContext> {

		/** The remaining bits. */
		private long remainingBits;

		/** The autopad. */
		private boolean autopad;

		/** The carrier type. */
		private Class<?> carrierType;

		/** The field offset. */
		protected long fieldOffset;

		/**
		 * Instantiates a new bit field builder.
		 *
		 * @param name the name
		 */
//		public BitFieldBuilder(String name) {
//			super(name);
//		}

		public BitFieldBuilder(int index, Path... elements) {
			super(index, elements);
		}

		/**
		 * Builds the.
		 *
		 * @param offset the offset
		 * @return the bit field context
		 */
		public BitFieldContext build(long offset) {
			return new ConstBitFieldContext(
					binaryLayout, carrierSize, carrierOffset + offset, align,
					binaryLayout.bitSize(), fieldOffset + offset);
		}

		/**
		 * Field offset.
		 *
		 * @return the long
		 */
		public long fieldOffset() {
			return fieldOffset;
		}

		/**
		 * From layout.
		 *
		 * @param sequenceLayout the sequence layout
		 * @param offset         the offset
		 * @return the bit field context
		 * @see com.slytechs.jnet.runtime.internal.layout.FieldCarrierContext.CarrierBuilder#fromLayout(com.slytechs.jnet.runtime.internal.layout.SequenceLayout,
		 *      long)
		 */
		@Override
		public BitFieldContext fromLayout(SequenceLayout sequenceLayout, long offset) {
			BitFieldContext ctx = null;

			if (isPartialMatch(sequenceLayout)) {
				if (!(sequenceLayout.element() instanceof ValueLayout value))
					throw new IllegalArgumentException("BitField on supports ValueLayouts on sequences");

				/* Copy sequence name to element if element not specifically set */
				if (value.name.isEmpty())
					value = value.withName(name);

				this.carrierSize = value.carrierSize();
				this.carrierType = value.carrierType;
				this.align = value.bitAlignment();

				if (value.carrierSize() == value.bitSize()) {
					ctx = new ConstBitFieldContext(value, carrierSize, carrierOffset + offset, align,
							value.bitSize(), fieldOffset + offset);
				} else {
					ctx = new CalculatedBitFieldContext(sequenceLayout, offset, value.carrierSize(),
							sequenceLayout.bitAlignment(), value.bitSize(), fieldOffset + offset);
				}

			} else if (!(sequenceLayout.element() instanceof ValueLayout)) {
				ctx = duplicateBuilder().fromLayout(sequenceLayout.element(), offset);
			}

			if (ctx != null) // Insert our stride at start of stride list
				ctx.insertFirst(sequenceLayout.element().bitSize());

			return ctx;
		}

		private BitFieldBuilder duplicateBuilder() {
			return new BitFieldBuilder(elementIndex, elements);
		}

		/**
		 * From layout.
		 *
		 * @param structLayout the struct layout
		 * @param offset       the offset
		 * @return the bit field context
		 * @see com.slytechs.jnet.runtime.internal.layout.FieldCarrierContext.CarrierBuilder#fromLayout(com.slytechs.jnet.runtime.internal.layout.StructLayout,
		 *      long)
		 */
		@Override
		public BitFieldContext fromLayout(StructLayout structLayout, long offset) {

			for (AbstractLayout layout : structLayout.asAbstractLayouts()) {
				switch (layout.kind()) {

				case SEQUENCE:
				case GROUP: {
					BitFieldContext ctx = duplicateBuilder()
							.fromLayout(layout, fieldOffset + offset);

					if (ctx != null)
						return ctx;

					pad(layout.bitSize());
					break;
				}

				case VALUE: {
					ValueLayout value = (ValueLayout) layout;

					struct(value);

					if (isPartialMatch(value))
						return build(offset);

					break;
				}

				case PAD: {
					pad(layout.bitSize());
					break;
				}

				default:
					throw new IllegalStateException("unrecognized layout type " + layout.getClass().getSimpleName());
				}
			}

			return null;
		}

		/**
		 * From layout.
		 *
		 * @param groupLayout the group layout
		 * @param offset      the offset
		 * @return the bit field context
		 * @see com.slytechs.jnet.runtime.internal.layout.FieldCarrierContext.CarrierBuilder#fromLayout(com.slytechs.jnet.runtime.internal.layout.UnionLayout,
		 *      long)
		 */
		@Override
		public BitFieldContext fromLayout(UnionLayout groupLayout, long offset) {

			for (AbstractLayout layout : groupLayout.asAbstractLayouts()) {
				switch (layout.kind()) {

				case SEQUENCE:
				case GROUP: {
					BitFieldContext ctx = duplicateBuilder().fromLayout(layout, offset);
					if (ctx != null)
						return ctx;

					break;
				}

				case VALUE: {
					ValueLayout value = (ValueLayout) layout;

					union(value);

//					System.out.printf("UionLayout::findBitField name=%s%n", value.layoutName().get());

					if (isPartialMatch(value))
						return build(offset);

					break;
				}

				case PAD: {
					break;
				}

				default:
					throw new IllegalStateException("unrecognized layout type " + layout.getClass().getSimpleName());
				}

			}
			return null;
		}

		/**
		 * From layout.
		 *
		 * @param layout the layout
		 * @param offset the offset
		 * @return the bit field context
		 * @see com.slytechs.jnet.runtime.internal.layout.FieldCarrierContext.CarrierBuilder#fromLayout(com.slytechs.jnet.runtime.internal.layout.ValueLayout,
		 *      long)
		 */
		@Override
		public BitFieldContext fromLayout(ValueLayout layout, long offset) {
			return !isPartialMatch(layout) || false
					? null
					: new ConstBitFieldContext(
							layout,
							layout.carrierSize(),
							0, // carrier offset
							layout.bitAlignment(),
							layout.bitSize(),
							0 // field offset
					);
		}

		/**
		 * Pad.
		 *
		 * @param len the len
		 */
		public void pad(long len) {
			fieldOffset += len;
			remainingBits -= len;
		}

		/**
		 * Start new carrier.
		 *
		 * @param value the value
		 */
		private void startNewCarrier(ValueLayout value) {

			if (autopad && remainingBits > 0)
				pad(remainingBits); // Pad with bits remaining in carrier

			carrierSize = value.carrierSize();
			carrierType = value.carrierType;
			carrierOffset = fieldOffset;
			remainingBits = value.carrierSize();
			carrierType = value.carrierType();
			binaryLayout = value;
			align = value.bitAlignment();
		}

		/**
		 * Struct.
		 *
		 * @param value the value
		 * @return the bit field builder
		 */
		public BitFieldBuilder struct(ValueLayout value) {
			long bitSize = value.bitSize();
			binaryLayout = value;

			if ((remainingBits <= 0) || (bitSize == 0) || (value.carrierType() != carrierType))
				startNewCarrier(value);

			remainingBits -= bitSize;

			if (!isPartialMatch(value))
				fieldOffset += bitSize;

//			System.out.printf("StructSearch::value %s %s%n", value.layoutName().orElse(""), toString());

			return this;
		}

		/**
		 * Union.
		 *
		 * @param value the value
		 * @return the bit field builder
		 */
		public BitFieldBuilder union(ValueLayout value) {
			startNewCarrier(value);
			return this;
		}
	}

	/**
	 * The Class CalculatedBitFieldContext.
	 */
	public static class CalculatedBitFieldContext extends BitFieldContext {

		/** The offset. */
		private final long offset;

		/** The align mask. */
		private final long alignMask;

		/**
		 * Instantiates a new calculated bit field context.
		 *
		 * @param binaryLayout the binary layout
		 * @param offset       the offset
		 * @param carrierSize  the carrier size
		 * @param align        the align
		 * @param fieldSize    the field size
		 * @param fieldOffset  the field offset
		 */
		public CalculatedBitFieldContext(
				BinaryLayout binaryLayout,
				long offset,
				long carrierSize,
				long align,
				long fieldSize,
				long fieldOffset) {
			super(binaryLayout, carrierSize, align, fieldSize, fieldOffset);
			this.offset = offset;
			this.alignMask = ~aligmentMaskFromBits(align);
		}

		/**
		 * Carrier offset.
		 *
		 * @param strideOffset the stride offset
		 * @return the long
		 * @see com.slytechs.jnet.runtime.internal.layout.FieldCarrierContext#carrierOffset(long)
		 */
		@Override
		public long carrierOffset(long strideOffset) {
			long off = (offset + strideOffset) & alignMask;

//			System.out.printf("BitFieldContext::carrierOffset off=%-2d alignMask=0x%X%n", off, alignMask);

			return off;
		}

		/**
		 * Field bitshift.
		 *
		 * @param strideOffset the stride offset
		 * @return the int
		 * @see com.slytechs.jnet.runtime.internal.layout.BitFieldContext#fieldBitshift(long)
		 */
		@Override
		public int fieldBitshift(long strideOffset) {
			int shift = calcBitshift(fieldOffset + strideOffset, carrierOffset(strideOffset));

//			System.out.printf("BitFieldContext::fieldBitshift %s shift=%d field=%d carrier=%d%n",
//					binaryLayout().layoutName().orElse(""),
//					shift,
//					fieldOffset + strideOffset,
//					carrierOffset(strideOffset));

			return shift;
		}
	}

	/**
	 * The Class ConstBitFieldContext.
	 */
	public static class ConstBitFieldContext extends BitFieldContext {

		/** The field shift. */
		protected final int fieldShift;

		/** The carrier offset. */
		protected final long carrierOffset;

		/**
		 * Instantiates a new const bit field context.
		 *
		 * @param binaryLayout  the binary layout
		 * @param carrierSize   the carrier size
		 * @param carrierOffset the carrier offset
		 * @param align         the align
		 * @param fieldSize     the field size
		 * @param fieldOffset   the field offset
		 */
		public ConstBitFieldContext(
				BinaryLayout binaryLayout,
				long carrierSize,
				long carrierOffset,
				long align,
				long fieldSize,
				long fieldOffset) {
			super(binaryLayout, carrierSize, align, fieldSize, fieldOffset);

			assert ((fieldOffset - carrierOffset + fieldSize) <= carrierSize) : ""
					+ "field doesn't fit inside the carrier " + toString();

			this.carrierOffset = carrierOffset;
			this.fieldShift = calcBitshift(fieldOffset, carrierOffset);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.FieldCarrierContext#carrierOffset(long)
		 */
		@Override
		public long carrierOffset(long strideOffset) {
			return checkAlignment(strideOffset + carrierOffset);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BitFieldContext#fieldBitshift(long)
		 */
		@Override
		public int fieldBitshift(long strideOffset) {
			return fieldShift;
		}

		/**
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "ConstBitFieldContext"
					+ " [fieldSize=" + fieldSize
					+ ", carrierSize=" + carrierSize
					+ ", fieldOffset=" + fieldOffset
					+ ", carrierOffset=" + carrierOffset
					+ ", fieldShift=" + fieldShift
					+ ", fieldMask=0x" + Long.toHexString(fieldMask)
					+ ", binaryLayout=" + binaryLayout
					+ "]";
		}

	}

	/** The field size. */
	protected final long fieldSize;

	/** The field offset. */
	protected final long fieldOffset;

	/** The field mask. */
	protected final long fieldMask;

	/**
	 * Instantiates a new bit field context.
	 *
	 * @param binaryLayout the binary layout
	 * @param carrierSize  the carrier size
	 * @param align        the align
	 * @param fieldSize    the field size
	 * @param fieldOffset  the field offset
	 */
	public BitFieldContext(BinaryLayout binaryLayout,
			long carrierSize,
			long align,
			long fieldSize,
			long fieldOffset) {
		super(binaryLayout, carrierSize, align);

		this.fieldSize = fieldSize;
		this.fieldOffset = fieldOffset;
		this.fieldMask = valueMaskFromSize(fieldSize);

		if (binaryLayout instanceof ValueLayout value)
			order(value.order);
	}

	/**
	 * Calc bitshift.
	 *
	 * @param fieldOffset   the field offset
	 * @param carrierOffset the carrier offset
	 * @return the int
	 * @see https://stackoverflow.com/questions/6043483/why-bit-endianness-is-an-issue-in-bitfields
	 */
	protected final int calcBitshift(long fieldOffset, long carrierOffset) {
		long fieldShift;

		if (order() == ByteOrder.BIG_ENDIAN) {
			fieldShift = ((carrierSize + carrierOffset) - (fieldOffset + fieldSize));

		} else {
			fieldShift = (fieldOffset - carrierOffset);
		}

		/* Check bit wrap */
		if (fieldShift == carrierSize)
			fieldShift = 0;

		assert (fieldShift >= 0) : "failed to calculate field bit shift (negative value) " + toString();
		assert (fieldShift < carrierSize) : "failed to calculate field bit shift (larger than carrier) "
				+ toString();

		return (int) fieldShift;

	}

	/**
	 * Field bitmask.
	 *
	 * @return the long
	 */
	public final long fieldBitmask() {
		return fieldMask;
	}

	/**
	 * Field bitshift.
	 *
	 * @param strideOffset the stride offset
	 * @return the int
	 */
	public abstract int fieldBitshift(long strideOffset);

	/**
	 * Field offset.
	 *
	 * @param strideOffset the stride offset
	 * @return the long
	 */
	public final long fieldOffset(long strideOffset) {
		return (fieldOffset + strideOffset);
	}

	/**
	 * Field size.
	 *
	 * @return the long
	 */
	public long fieldSize() {
		return fieldSize;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BitFieldContext [fieldSize=" + fieldSize + ", fieldOffset=" + fieldOffset + ", fieldMask=" + fieldMask
				+ "]";
	}
}
