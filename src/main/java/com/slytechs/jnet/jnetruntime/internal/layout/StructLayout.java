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
package com.slytechs.jnet.jnetruntime.internal.layout;

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
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BinaryLayout#withBitAlignment(long)
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
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BinaryLayout#withName(java.lang.String)
	 */
	@Override
	public StructLayout withName(String name) {
		return new StructLayout(size, alignment, Optional.of(name), layouts);
	}
}
