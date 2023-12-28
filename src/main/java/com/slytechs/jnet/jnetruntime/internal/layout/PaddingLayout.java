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

import java.util.Optional;
import java.util.OptionalLong;

import com.slytechs.jnet.jnetruntime.internal.layout.Path.PathKind;

/**
 * The Class PaddingLayout.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public class PaddingLayout extends AbstractLayout {

	/**
	 * Instantiates a new padding layout.
	 *
	 * @param size the size
	 */
	PaddingLayout(long size) {
		super(PathKind.PAD, OptionalLong.of(size), 1, Optional.empty());
	}

	/**
	 * Instantiates a new padding layout.
	 *
	 * @param size      the size
	 * @param alignment the alignment
	 * @param name      the name
	 */
	PaddingLayout(long size, int alignment, Optional<String> name) {
		super(PathKind.PAD, OptionalLong.of(size), alignment, name);
	}

	/**
	 * With bit alignment.
	 *
	 * @param alignment the alignment
	 * @return the padding layout
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BinaryLayout#withBitAlignment(long)
	 */
	@Override
	public PaddingLayout withBitAlignment(int alignment) {
		return new PaddingLayout(bitSize(), alignment, name);
	}

	/**
	 * With name.
	 *
	 * @param name the name
	 * @return the padding layout
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BinaryLayout#withName(java.lang.String)
	 */
	@Override
	public PaddingLayout withName(String name) {
		return new PaddingLayout(bitSize(), alignment, Optional.ofNullable(name));
	}

}
