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
import java.util.Optional;
import java.util.OptionalLong;
import java.util.stream.Collectors;

import com.slytechs.jnet.runtime.internal.layout.Path.PathKind;

/**
 * The Class GroupLayout.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public abstract class GroupLayout extends AbstractLayout {

	/** The layouts. */
	protected final List<BinaryLayout> layouts;

	/** The alayouts. */
	protected final List<AbstractLayout> alayouts;

	/**
	 * Instantiates a new group layout.
	 *
	 * @param size      the size
	 * @param alignment the alignment
	 * @param name      the name
	 * @param layouts   the layouts
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	GroupLayout(OptionalLong size, int alignment, Optional<String> name, List<BinaryLayout> layouts) {
		super(PathKind.GROUP, size, alignment, name);

		this.layouts = layouts;
		this.alayouts = (List) layouts.stream()
				.filter(BinaryLayout::isEnabled)
				.collect(Collectors.toList());
	}

	/**
	 * Contains path element.
	 *
	 * @param element the element
	 * @return true, if successful
	 */
	protected boolean containsPathElement(Path element) {
		return (element.kind() == super.kind)
				&& element.name().isPresent()
				|| layouts.stream()
						.filter(l -> l.layoutName().isPresent())
						.map(l -> l.layoutName().get())
						.anyMatch(n -> element.name().get().equals(n));
	}

	/**
	 * As abstract layouts.
	 *
	 * @return the list
	 */
	List<AbstractLayout> asAbstractLayouts() {
		return alayouts;
	}
}
