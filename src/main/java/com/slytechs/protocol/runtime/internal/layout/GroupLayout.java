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

import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.stream.Collectors;

import com.slytechs.protocol.runtime.internal.layout.Path.PathKind;

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
