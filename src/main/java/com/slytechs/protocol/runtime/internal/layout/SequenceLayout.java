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

import java.util.Optional;
import java.util.OptionalLong;

import com.slytechs.protocol.runtime.internal.layout.Path.PathKind;

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
	 * @see com.slytechs.protocol.runtime.internal.layout.BinaryLayout#withBitAlignment(long)
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
	 * @see com.slytechs.protocol.runtime.internal.layout.BinaryLayout#withName(java.lang.String)
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
