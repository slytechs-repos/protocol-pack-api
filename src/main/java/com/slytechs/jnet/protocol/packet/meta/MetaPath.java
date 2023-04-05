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
import java.util.Optional;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public final class MetaPath implements Iterable<String> {

	public static final String GLOBAL_DOMAINNAME = "$$";
	public static final String ROOT_NAME = "$";
	public static final String UP_NAME = "..";

	private final int offset;
	private final String path;
	private final String[] stack;

	public MetaPath(String path) {
		this.path = path;
		this.stack = path.split("\\.");
		this.offset = 0;
	}

	@SuppressWarnings("unused")
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

	public boolean match(String name, int offset) {
		return stack[offset].equals(name);
	}

	public boolean match(int index, String name) {
		if (index < 0 || index >= (stack.length - offset))
			return false;

		return stack[offset + index].equals(name);
	}

	public boolean matchLast(String name) {
		return match(stack.length - 1, name);
	}

	private boolean isUp(int offset) {
		return match(offset, UP_NAME);
	}

	private boolean isLast(int offset) {
		return ((stack.length - 1) == offset);
	}

	public int size() {
		return stack.length - offset;
	}

	private String last() {
		return stack[stack.length - 1];
	}

	/**
	 * @return
	 */
	public boolean isEmpty() {
		return size() == 0;
	}

	public Optional<MetaField> searchForField(MetaDomain domain) {
		return search(domain);
	}

	public <V> Optional<V> search(MetaDomain domain) {
		return search(domain, this.offset);
	}

	private <K, V> Optional<V> search(MetaDomain domain, int offset) {
		if (offset >= stack.length)
			return Optional.empty();

		if (domain == null)
			return Optional.empty();

		if (match(GLOBAL_DOMAINNAME, offset))
			return search(MetaDomain.getGlobalDomain(), offset + 1);

		if (isUp(offset))
			return search(domain.parent(), offset + 1);

		if (isLast(offset))
			return domain.findKey(last());

		MetaDomain childDomain = domain.findDomain(stack[offset]);
		if (childDomain == null)
			return Optional.empty();

		return search(childDomain, offset + 1);

	}
}
