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

/**
 * The Class ArrayFieldContext.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
class ArrayFieldContext extends FieldCarrierContext {

	/**
	 * The Class ArrayFieldBuilder.
	 */
	static class ArrayFieldBuilder extends FieldCarrierContext.CarrierBuilder<ArrayFieldContext> {

		/** The field offset. */
		private long fieldOffset;

		public ArrayFieldBuilder(int index, Path[] elements) {
			super(index, elements);
		}

		/**
		 * From layout.
		 *
		 * @param sequenceLayout the sequence layout
		 * @param offset         the offset
		 * @return the array field context
		 * @see com.slytechs.jnet.runtime.internal.layout.FieldCarrierContext.CarrierBuilder#fromLayout(com.slytechs.jnet.runtime.internal.layout.SequenceLayout,
		 *      long)
		 */
		@Override
		public ArrayFieldContext fromLayout(SequenceLayout sequenceLayout, long offset) {
			ArrayFieldContext ctx = null;

			if (isPartialMatch(sequenceLayout)) {
				if (!(sequenceLayout.element() instanceof ValueLayout value))
					throw new IllegalArgumentException("BitField on supports ValueLayouts on sequences");

				this.carrierSize = value.carrierSize();
				this.align = value.bitAlignment();

				ctx = new ArrayFieldContext(
						sequenceLayout,
						sequenceLayout.bitSize(),
						sequenceLayout.bitAlignment(),
						offset);

			} else if (!(sequenceLayout.element() instanceof ValueLayout)) {
				ctx = duplicateBuilder()
						.fromLayout(sequenceLayout.element(), offset);

				if (ctx != null) // Insert our stride at start of stride list
					ctx.insertFirst(sequenceLayout.element().bitSize());
			}

			return ctx;
		}

		private ArrayFieldBuilder duplicateBuilder() {
			return new ArrayFieldBuilder(elementIndex, elements);
		}

		/**
		 * From layout.
		 *
		 * @param structLayout the struct layout
		 * @param offset       the offset
		 * @return the array field context
		 * @see com.slytechs.jnet.runtime.internal.layout.FieldCarrierContext.CarrierBuilder#fromLayout(com.slytechs.jnet.runtime.internal.layout.StructLayout,
		 *      long)
		 */
		@Override
		public ArrayFieldContext fromLayout(StructLayout structLayout, long offset) {
			for (AbstractLayout layout : structLayout.asAbstractLayouts()) {
				switch (layout.kind()) {

				case SEQUENCE:
				case GROUP: {
					ArrayFieldContext ctx = duplicateBuilder()
							.fromLayout(layout, fieldOffset + offset);

					if (ctx != null)
						return ctx;
				}

				case VALUE:
				case PAD:
					fieldOffset += layout.bitSize();
					break;

				default:
					throw new IllegalStateException("unrecognized layout type " + layout.getClass().getSimpleName());
				}
			}

			return null;
		}

		/**
		 * From layout.
		 *
		 * @param unionLayout the union layout
		 * @param offset      the offset
		 * @return the array field context
		 * @see com.slytechs.jnet.runtime.internal.layout.FieldCarrierContext.CarrierBuilder#fromLayout(com.slytechs.jnet.runtime.internal.layout.UnionLayout,
		 *      long)
		 */
		@Override
		public ArrayFieldContext fromLayout(UnionLayout unionLayout, long offset) {
			for (AbstractLayout layout : unionLayout.asAbstractLayouts()) {
				switch (layout.kind()) {

				case SEQUENCE:
				case GROUP: {
					ArrayFieldContext ctx = duplicateBuilder()
							.fromLayout(layout, offset);

					if (ctx != null)
						return ctx;
				}

				case VALUE:
				case PAD:
					break;

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
		 * @return the array field context
		 * @see com.slytechs.jnet.runtime.internal.layout.FieldCarrierContext.CarrierBuilder#fromLayout(com.slytechs.jnet.runtime.internal.layout.ValueLayout,
		 *      long)
		 */
		@Override
		public ArrayFieldContext fromLayout(ValueLayout layout, long offset) {
			return null;
		}

	}

	/** The carrier offset. */
	private final long carrierOffset;

	/**
	 * Instantiates a new array field context.
	 *
	 * @param binaryLayout  the binary layout
	 * @param carrierSize   the carrier size
	 * @param align         the align
	 * @param carrierOffset the carrier offset
	 */
	public ArrayFieldContext(BinaryLayout binaryLayout,
			long carrierSize,
			long align,
			long carrierOffset) {
		super(binaryLayout, carrierSize, align);
		this.carrierOffset = carrierOffset;
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
		return (carrierOffset + strideOffset);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ArrayFieldContext [carrierOffset=" + carrierOffset + ", carrierSize=" + carrierSize + ", strides="
				+ strides + ", binaryLayout=" + binaryLayout + "]";
	}

}
