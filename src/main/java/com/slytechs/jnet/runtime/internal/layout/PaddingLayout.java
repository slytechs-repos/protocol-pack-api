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
	 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#withBitAlignment(long)
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
	 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#withName(java.lang.String)
	 */
	@Override
	public PaddingLayout withName(String name) {
		return new PaddingLayout(bitSize(), alignment, Optional.ofNullable(name));
	}

}
