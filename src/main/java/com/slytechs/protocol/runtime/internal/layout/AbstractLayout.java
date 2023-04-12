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

import java.util.Arrays;
import java.util.Optional;
import java.util.OptionalLong;

import com.slytechs.protocol.runtime.internal.layout.BitFieldContext.BitFieldBuilder;
import com.slytechs.protocol.runtime.internal.layout.Path.PathKind;

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

	/** The enabled. */
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

	/**
	 * Array field.
	 *
	 * @param elements the elements
	 * @return the array field
	 * @see com.slytechs.protocol.runtime.internal.layout.BinaryLayout#arrayField(com.slytechs.protocol.runtime.internal.layout.Path[])
	 */
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
	 * Array field.
	 *
	 * @param path the path
	 * @return the array field
	 * @see com.slytechs.protocol.runtime.internal.layout.BinaryLayout#arrayField(java.lang.String)
	 */
	@Override
	public ArrayField arrayField(String path) {
		return arrayField(Path.sequenceElement(path));
	}

	/**
	 * Bit alignment.
	 *
	 * @return the int
	 * @see com.slytechs.protocol.runtime.internal.layout.BinaryLayout#bitAlignment()
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
	 * Bit field.
	 *
	 * @param elementName the element name
	 * @return the bit field
	 * @see com.slytechs.protocol.runtime.internal.layout.BinaryLayout#bitField(java.lang.String)
	 */
	@Override
	public final BitField bitField(String elementName) {
		return bitField(Path.valueElement(elementName));
	}

	/**
	 * Bit size.
	 *
	 * @return the long
	 * @see com.slytechs.protocol.runtime.internal.layout.BinaryLayout#bitSize()
	 */
	@Override
	public long bitSize() {
		return this.size.orElseThrow(AbstractLayout::badSizeException);
	}

	/**
	 * Byte alignment.
	 *
	 * @return the long
	 * @see com.slytechs.protocol.runtime.internal.layout.BinaryLayout#byteAlignment()
	 */
	@Override
	public long byteAlignment() {
		return (this.alignment >> 3);
	}

	/**
	 * Byte size.
	 *
	 * @return the long
	 * @see com.slytechs.protocol.runtime.internal.layout.BinaryLayout#byteSize()
	 */
	@Override
	public long byteSize() {
		return (bitSize() >> 3);
	}

	/**
	 * Checks if is enabled.
	 *
	 * @return the enabled
	 * @see com.slytechs.protocol.runtime.internal.layout.BinaryLayout#isEnabled()
	 */
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
	 * @see com.slytechs.protocol.runtime.internal.layout.BinaryLayout#layoutName()
	 */
	@Override
	public Optional<String> layoutName() {
		return this.name;
	}

	/**
	 * Size.
	 *
	 * @return the optional long
	 * @see com.slytechs.protocol.runtime.internal.layout.BinaryLayout#size()
	 */
	@Override
	public OptionalLong size() {
		return size;
	}

	/**
	 * To string.
	 *
	 * @return the string
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

	/**
	 * When enabled.
	 *
	 * @param enabled the enabled
	 * @return the binary layout
	 * @see com.slytechs.protocol.runtime.internal.layout.BinaryLayout#whenEnabled(boolean)
	 */
	@Override
	public final BinaryLayout whenEnabled(boolean enabled) {
		AbstractLayout next = (AbstractLayout) withBitAlignment(alignment);

		next.enabled = enabled;

		return next;
	}

}
