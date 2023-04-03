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

import java.util.Arrays;
import java.util.Optional;
import java.util.OptionalLong;

import com.slytechs.jnet.runtime.internal.layout.BitFieldContext.BitFieldBuilder;
import com.slytechs.jnet.runtime.internal.layout.Path.PathKind;

/**
 * The Class AbstractLayout.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
abstract class AbstractLayout implements BinaryLayout {

	/**
	 * Bad size exception.
	 *
	 * @return the unsupported operation exception
	 */
	private static UnsupportedOperationException badSizeException() {
		return new UnsupportedOperationException(""
				+ "Cannot compute size of a layout which is, "
				+ "or depends on a sequence layout with unspecified size");
	}

	/** The size. */
	protected final OptionalLong size;

	/** The alignment. */
	protected final int alignment;

	/** The name. */
	protected final Optional<String> name;

	/** The kind. */
	protected final PathKind kind;

	/** The offset. */
	protected long offset;

	protected boolean enabled = true;

	/**
	 * Instantiates a new abstract layout.
	 *
	 * @param kind      the kind
	 * @param size      the size
	 * @param alignment the alignment
	 * @param name      the name
	 */
	AbstractLayout(PathKind kind, OptionalLong size, int alignment, Optional<String> name) {
		this.kind = kind;
		this.size = size;
		this.alignment = alignment;
		this.name = name;
	}

	@Override
	public ArrayField arrayField(Path... elements) {

		ArrayFieldContext ctx = new ArrayFieldContext.ArrayFieldBuilder(0, elements)
				.fromLayout(this, 0);

		if (ctx == null)
			throw new IllegalArgumentException("ArrayField not found: " + Arrays.asList(elements));

//		assert ctx.binaryLayout().layoutName().filter(n -> n.equals(bitFieldName)).isPresent() : "wrong field "
//				+ ctx.binaryLayout().layoutName().orElse("");

		return new ArrayFieldImplementation(ctx);
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#arrayField(java.lang.String)
	 */
	@Override
	public ArrayField arrayField(String path) {
		return arrayField(Path.sequenceElement(path));
	}

	/**
	 * Bit alignment.
	 *
	 * @return the int
	 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#bitAlignment()
	 */
	@Override
	public int bitAlignment() {
		return alignment;
	}

	/**
	 * Bit field.
	 *
	 * @param elements the elements
	 * @return the bit field
	 */
	@Override
	public final BitField bitField(Path... elements) {
		if (elements.length != 1)
			throw new UnsupportedOperationException("Only single element search is supported");

		String bitFieldName = elements[0].name()
				.orElseThrow(() -> new IllegalArgumentException("bitField missing"));

		BitFieldContext ctx = new BitFieldBuilder(0, elements)
				.fromLayout(this, 0);

		if (ctx == null)
			throw new IllegalArgumentException("BitField not found: " + Arrays.asList(elements));

//		assert ctx.binaryLayout().layoutName().filter(n -> n.equals(bitFieldName)).isPresent() : "wrong field "
//				+ ctx.binaryLayout().layoutName().orElse("");

		return new BitFieldImplementation(ctx);
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#bitField(java.lang.String)
	 */
	@Override
	public final BitField bitField(String elementName) {
		return bitField(Path.valueElement(elementName));
	}

	/**
	 * Bit size.
	 *
	 * @return the long
	 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#bitSize()
	 */
	@Override
	public long bitSize() {
		return this.size.orElseThrow(AbstractLayout::badSizeException);
	}

	/**
	 * Byte alignment.
	 *
	 * @return the long
	 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#byteAlignment()
	 */
	@Override
	public long byteAlignment() {
		return (this.alignment >> 3);
	}

	/**
	 * Byte size.
	 *
	 * @return the long
	 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#byteSize()
	 */
	@Override
	public long byteSize() {
		return (bitSize() >> 3);
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Checks if is named.
	 *
	 * @param layoutName the layout name
	 * @return true, if is named
	 */
	public boolean isNamed(String layoutName) {
		return this.name.filter(layoutName::equals).isPresent();
	}

	/**
	 * Kind.
	 *
	 * @return the path kind
	 */
	public final PathKind kind() {
		return this.kind;
	}

	/**
	 * Layout name.
	 *
	 * @return the optional
	 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#layoutName()
	 */
	@Override
	public Optional<String> layoutName() {
		return this.name;
	}

	/**
	 * Size.
	 *
	 * @return the optional long
	 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#size()
	 */
	@Override
	public OptionalLong size() {
		return size;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Layout"
				+ " [kind=" + kind
				+ ", name=\"" + name.orElse("") + "\""
				+ ", size=" + size
				+ ", offset=" + offset
				+ ", alignment=" + alignment
				+ "]";
	}

	@Override
	public final BinaryLayout whenEnabled(boolean enabled) {
		AbstractLayout next = (AbstractLayout) withBitAlignment(alignment);

		next.enabled = enabled;

		return next;
	}

}
