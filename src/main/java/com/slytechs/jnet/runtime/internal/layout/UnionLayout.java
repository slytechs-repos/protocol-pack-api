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
