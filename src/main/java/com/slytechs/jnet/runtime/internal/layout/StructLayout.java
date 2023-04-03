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

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalLong;

/**
 * The Class StructLayout.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public class StructLayout extends GroupLayout {

	/**
	 * Calc size.
	 *
	 * @param layouts the layouts
	 * @return the optional long
	 */
	private static OptionalLong calcSize(List<BinaryLayout> layouts) {
		if (layouts.stream()
				.map(Objects::requireNonNull)
				.filter(BinaryLayout::isEnabled)
				.map(BinaryLayout::size)
				.anyMatch(s -> s.isEmpty())) {
			return OptionalLong.empty();
		}

		return OptionalLong.of(layouts.stream()
				.filter(BinaryLayout::isEnabled)
				.map(BinaryLayout::size)
				.mapToLong(OptionalLong::getAsLong)
				.sum());
	}

	/**
	 * Instantiates a new struct layout.
	 *
	 * @param layouts the layouts
	 */
	StructLayout(List<BinaryLayout> layouts) {
		this(calcSize(layouts), ValueLayout.DEFAULT_ALIGNMENT, Optional.empty(), layouts);
	}

	/**
	 * Instantiates a new struct layout.
	 *
	 * @param size      the size
	 * @param alignment the alignment
	 * @param name      the name
	 * @param layouts   the layouts
	 */
	private StructLayout(OptionalLong size, int alignment, Optional<String> name, List<BinaryLayout> layouts) {
		super(calcSize(layouts), alignment, name, layouts);
	}

	/**
	 * With bit alignment.
	 *
	 * @param alignment the alignment
	 * @return the struct layout
	 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#withBitAlignment(long)
	 */
	@Override
	public StructLayout withBitAlignment(int alignment) {
		return new StructLayout(size, alignment, name, layouts);
	}

	/**
	 * With name.
	 *
	 * @param name the name
	 * @return the struct layout
	 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#withName(java.lang.String)
	 */
	@Override
	public StructLayout withName(String name) {
		return new StructLayout(size, alignment, Optional.of(name), layouts);
	}
}
