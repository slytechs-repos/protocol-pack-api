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

/**
 * The Class Path.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public class Path {

	/**
	 * The Enum PathKind.
	 */
	public enum PathKind {

		/** The group. */
		GROUP,

		/** The sequence. */
		SEQUENCE,

		/** The pad. */
		PAD,

		/** The value. */
		VALUE,
	}

	/**
	 * Group element.
	 *
	 * @param name the name
	 * @return the path
	 */
	public static Path groupElement(String name) {
		return new Path(PathKind.GROUP, name);
	}

	public static Path valueElement(String name) {
		return new Path(PathKind.VALUE, name);
	}

	/**
	 * Sequence element.
	 *
	 * @return the path
	 */
	public static Path sequenceElement() {
		return new Path(PathKind.SEQUENCE, OptionalLong.empty());
	}

	/**
	 * Sequence element.
	 *
	 * @param index the index
	 * @return the path
	 */
	public static Path sequenceElement(long index) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Sequence element.
	 *
	 * @param name  the name
	 * @param index the index
	 * @return the path
	 */
	public static Path sequenceElement(String name, long index) {
		throw new UnsupportedOperationException();
	}

	public static Path sequenceElement(String name) {
		return new Path(PathKind.SEQUENCE, name);
	}

	/** The kind. */
	private final PathKind kind;

	/** The name. */
	private final Optional<String> name;

	/** The index. */
	private final OptionalLong index;

	/**
	 * Instantiates a new path.
	 *
	 * @param kind  the kind
	 * @param index the index
	 */
	Path(PathKind kind, OptionalLong index) {
		this.kind = kind;
		this.index = index;
		this.name = Optional.empty();
	}

	/**
	 * Instantiates a new path.
	 *
	 * @param kind the kind
	 * @param name the name
	 */
	Path(PathKind kind, String name) {
		this.kind = kind;
		this.name = Optional.of(name);
		this.index = OptionalLong.empty();
	}

	boolean isMatch(AbstractLayout layout) {
//		if (kind != layout.kind)
//			return false;

		if (name.isPresent() && layout.name.isPresent())
			return name.get().equals(layout.name.get());

		return false;
	}

	/**
	 * Index.
	 *
	 * @return the optional long
	 */
	OptionalLong index() {
		return index;
	}

	/**
	 * Kind.
	 *
	 * @return the path kind
	 */
	PathKind kind() {
		return kind;
	}

	/**
	 * Name.
	 *
	 * @return the optional
	 */
	Optional<String> name() {
		return name;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Path ["
				+ "kind=" + kind
				+ ", name=\"" + name.orElse("") + "\""
				+ ", index=" + index
				+ "]";
	}

}
