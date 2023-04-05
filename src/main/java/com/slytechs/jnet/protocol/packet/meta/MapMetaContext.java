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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.slytechs.jnet.protocol.packet.meta.MetaContext.MetaMapped;

public class MapMetaContext
		extends AbstractMetaContext
		implements MetaMapped {

	private int capacity = Integer.MAX_VALUE;

	/** Root domain */
	public MapMetaContext(String name, int capacity) {
		super(name);
		this.capacity = capacity;
	}

	/** Anchored domain */
	public MapMetaContext(MetaContext parent, String name, int capacity) {
		super(parent, name);
		this.capacity = capacity;
	}

	private final Map<Object, Object> map = new HashMap<>();

	/**
	 * @see com.slytechs.jnet.protocol.packet.meta.MetaContext.MetaMapped#get(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <K, V> V get(K key) {
		return (V) map.get(key);
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.meta.MetaContext.MetaMappedSetter#set(java.lang.Object,
	 *      java.lang.Object)
	 */
	@Override
	public <K, V> void set(K key, V value) {
		if (size() == capacity)
			throw new IllegalStateException("context over capacity [%d]"
					.formatted(capacity));

		map.put(key, value);
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.meta.MetaContext.MetaMapped#capacity()
	 */
	@Override
	public int capacity() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public void setCapacity(int newCapacity) {
		if (newCapacity < size())
			throw new IllegalStateException("new capacity is less than current size [size=%d, newCapacity=%d]"
					.formatted(size(), newCapacity));

		this.capacity = newCapacity;
	}

	public void clear() {
		map.clear();
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.meta.MetaContext.MetaMapped#size()
	 */
	@Override
	public int size() {
		return map.size();
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.meta.MetaDomain#findKey(java.lang.Object)
	 */
	@Override
	public <K, V> Optional<V> findKey(K key) {
		return get(key);
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.meta.MetaDomain#findDomain(java.lang.String)
	 */
	@Override
	public MetaDomain findDomain(String name) {
		Object obj = get(name);
		if (obj instanceof MetaDomain domain)
			return domain;

		return null;
	}
}
