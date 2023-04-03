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
 * The Class UnionLayout.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public class UnionLayout extends GroupLayout {

	/**
	 * Calc size.
	 *
	 * @param layouts the layouts
	 * @return the optional long
	 */
	private static OptionalLong calcSize(List<BinaryLayout> layouts) {
		return layouts.stream()
				.map(Objects::requireNonNull)
				.filter(BinaryLayout::isEnabled)
				.mapToLong(BinaryLayout::bitSize)
				.max();
	}

	/**
	 * Instantiates a new union layout.
	 *
	 * @param layouts the layouts
	 */
	UnionLayout(List<BinaryLayout> layouts) {
		this(calcSize(layouts), ValueLayout.DEFAULT_ALIGNMENT, Optional.empty(), layouts);
	}

	/**
	 * Instantiates a new union layout.
	 *
	 * @param size      the size
	 * @param alignment the alignment
	 * @param name      the name
	 * @param layouts   the layouts
	 */
	private UnionLayout(OptionalLong size, int alignment, Optional<String> name, List<BinaryLayout> layouts) {
		super(size, alignment, name, layouts);
	}

	/**
	 * With bit alignment.
	 *
	 * @param alignment the alignment
	 * @return the union layout
	 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#withBitAlignment(long)
	 */
	@Override
	public UnionLayout withBitAlignment(int alignment) {
		return new UnionLayout(size, alignment, name, layouts);
	}

	/**
	 * With name.
	 *
	 * @param name the name
	 * @return the union layout
	 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#withName(java.lang.String)
	 */
	@Override
	public UnionLayout withName(String name) {
		return new UnionLayout(size, alignment, Optional.ofNullable(name), layouts);
	}

}
