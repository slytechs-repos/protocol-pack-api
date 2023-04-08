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

		/**
		 * Instantiates a new array field builder.
		 *
		 * @param index    the index
		 * @param elements the elements
		 */
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

		/**
		 * Duplicate builder.
		 *
		 * @return the array field builder
		 */
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
	 * To string.
	 *
	 * @return the string
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ArrayFieldContext [carrierOffset=" + carrierOffset + ", carrierSize=" + carrierSize + ", strides="
				+ strides + ", binaryLayout=" + binaryLayout + "]";
	}

}
