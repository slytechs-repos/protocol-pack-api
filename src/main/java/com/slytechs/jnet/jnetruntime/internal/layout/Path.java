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

	/**
	 * Value element.
	 *
	 * @param name the name
	 * @return the path
	 */
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

	/**
	 * Sequence element.
	 *
	 * @param name the name
	 * @return the path
	 */
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

	/**
	 * Checks if is match.
	 *
	 * @param layout the layout
	 * @return true, if is match
	 */
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
	 * To string.
	 *
	 * @return the string
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
