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
package com.slytechs.jnet.protocol.packet.meta;

import java.util.Iterator;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public final class MetaPath implements Iterable<String> {

	public static final String ROOT_PATH = ".";
	public static final String UP_PATH = "..";

	private final int offset;
	private final String path;
	private final String[] stack;

	public MetaPath(String path) {
		this.path = path;
		this.stack = path.split("\\.");
		this.offset = 0;
	}

	private MetaPath(String path, String[] stack, int offset) {
		this.path = path;
		this.stack = stack;
		this.offset = offset;
	}

	/**
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<String> iterator() {
		return new Iterator<String>() {
			int i = 0;

			@Override
			public boolean hasNext() {
				return i < stack.length;
			}

			@Override
			public String next() {
				return stack[i++];
			}

		};
	}

	@Override
	public String toString() {
		return path;
	}

	public String[] stack() {
		return stack;
	}

	public String stack(int index) {
		return stack[index];
	}

	public boolean match(String name) {
		return path.equals(name);
	}

	public boolean match(int index, String name) {
		if (index < 0 || index >= (stack.length - offset))
			return false;

		return stack[offset + index].equals(name);
	}

	public boolean matchFirst(String name) {
		return match(0, name);
	}

	public boolean matchLast(String name) {
		return match(stack.length - 1, name);
	}

	public MetaPath pop() {
		return new MetaPath(path, stack, offset + 1);
	}

	public boolean isUp() {
		return matchFirst(UP_PATH);
	}

	public boolean isRoot() {
		return matchFirst(ROOT_PATH);
	}

	public int size() {
		return stack.length - offset;
	}

	public MetaPath last() {
		return new MetaPath(path, stack, stack.length - 1);
	}

	public String toStringLast() {
		return stack[stack.length - 1];
	}

	/**
	 * @return
	 */
	public boolean isEmpty() {
		return size() == 0;
	}
}
