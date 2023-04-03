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

import com.slytechs.jnet.runtime.internal.layout.Path.PathKind;

/**
 * The Class SequenceLayout.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public class SequenceLayout extends AbstractLayout {

	/**
	 * Calc size.
	 *
	 * @param count the count
	 * @param size  the size
	 * @return the optional long
	 */
	private static OptionalLong calcSize(OptionalLong count, OptionalLong size) {
		if (count.isEmpty() || size.isEmpty())
			return OptionalLong.empty();

		return OptionalLong.of(count.getAsLong() * size.getAsLong());
	}

	/** The count. */
	private final OptionalLong count;
	
	/** The layout. */
	private final AbstractLayout layout;

	/**
	 * Instantiates a new sequence layout.
	 *
	 * @param element the element
	 */
	SequenceLayout(BinaryLayout element) {
		this(OptionalLong.empty(), element, element.bitAlignment(), Optional.empty());
	}

	/**
	 * Instantiates a new sequence layout.
	 *
	 * @param count   the count
	 * @param element the element
	 */
	SequenceLayout(long count, BinaryLayout element) {
		this(OptionalLong.of(count), element, element.bitAlignment(), Optional.empty());
	}

	/**
	 * Instantiates a new sequence layout.
	 *
	 * @param count     the count
	 * @param element   the element
	 * @param alignment the alignment
	 * @param name      the name
	 */
	private SequenceLayout(OptionalLong count, BinaryLayout element, int alignment, Optional<String> name) {
		super(PathKind.SEQUENCE, calcSize(count, element.size()), alignment, name);
		this.count = count;
		this.layout = (AbstractLayout) element;
	}

	/**
	 * With bit alignment.
	 *
	 * @param alignment the alignment
	 * @return the binary layout
	 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#withBitAlignment(long)
	 */
	@Override
	public BinaryLayout withBitAlignment(int alignment) {
		return new SequenceLayout(count, layout, alignment, name);
	}

	/**
	 * With name.
	 *
	 * @param name the name
	 * @return the binary layout
	 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#withName(java.lang.String)
	 */
	@Override
	public BinaryLayout withName(String name) {
		return new SequenceLayout(count, layout, alignment, Optional.of(name));
	}

	/**
	 * Stride size.
	 *
	 * @return the long
	 */
	public long strideSize() {
		return layout.bitSize();
	}

	/**
	 * Element.
	 *
	 * @return the binary layout
	 */
	public BinaryLayout element() {
		return layout;
	}

}
