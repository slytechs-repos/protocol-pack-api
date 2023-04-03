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

import java.lang.reflect.Member;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

import com.slytechs.jnet.protocol.packet.HeaderFactory;
import com.slytechs.jnet.protocol.packet.meta.MetaValue.ValueResolver;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public class PacketContext implements Cloneable, MetaContext {

	/*
	 * Multi-threaded Implementation note: As of March 2023, virtual threads do not
	 * work properly with "synchronize" statement in JDK19/20 by locking the host
	 * thread instead of parking the virtual thread. This may get fixed in the
	 * future, but we choose to use Locks instead in this library to avoid this
	 * issue and have more flexibility.
	 */

	public enum MetaAttribute {
		VALUE_RESOLVER,
		HEADER_FACTORY,
		HEADER_LINE_FORMAT,
	}

	public static final String DEFAULT_HEADER_LINE_FORMAT = "%15s = %-30s";
	private static final HeaderFactory DEFAULT_HEADER_FACTORY = HeaderFactory.syncLocal();

	// @formatter:off
	@SuppressWarnings("serial")
	private static final Map<Object, Object> staticAttributes = new HashMap<>() {{
		put(MetaAttribute.HEADER_FACTORY,         DEFAULT_HEADER_FACTORY);
		put(MetaAttribute.HEADER_LINE_FORMAT,     DEFAULT_HEADER_LINE_FORMAT);
	}};
	// @formatter:on

	@SuppressWarnings("unchecked")
	public static final synchronized <K, V> V computeStaticIfAbsent(K key, Function<? extends K, ? extends V> compute) {
		Function<Object, Object> func = (Function<Object, Object>) compute;
		return (V) staticAttributes.computeIfAbsent(key, func);
	}

	private final Map<Object, Object> dynamicAttributes = new HashMap<>();
	private final Lock padlock = new ReentrantLock();

	private final DomainContext parent;

	public PacketContext() {
		this.parent = null;
	}

	public PacketContext(DomainContext parent) {
		this.parent = parent;
	}

	/**
	 * @see java.lang.Object#clone()
	 */
	@Override
	public DomainContext clone() {
		DomainContext clone = new DomainContext();

		clone.dynamicAttributes.putAll(dynamicAttributes);

		return clone;
	}

	@SuppressWarnings("unchecked")
	public final <K, V> V computeDynamicIfAbsent(K key,
			Function<? extends K, ? extends V> compute) {

		try {
			padlock.lock();
			Function<Object, Object> func = (Function<Object, Object>) compute;

			return (V) dynamicAttributes.computeIfAbsent(key, func);
		} finally {
			padlock.unlock();
		}
	}

	@SuppressWarnings("unchecked")
	public final <K, V> V get(K key) {
		try {
			padlock.lock();
			V value = (V) dynamicAttributes.get(key);

			if (value == null)
				value = (V) staticAttributes.get(key);

			return value;
		} finally {
			padlock.unlock();
		}
	}

	public final <K, V> V get(K key, V defaultValue) {
		V value = get(key);

		if (value == null)
			return defaultValue;

		return value;
	}

	public final HeaderFactory getHeaderFactory() {
		return get(MetaAttribute.HEADER_FACTORY);
	}

	public final String getHeaderLineFormatString() {
		return get(MetaAttribute.HEADER_LINE_FORMAT);
	}

	final ReflectedClass getReflectedClass(Class<?> cl) {
		return computeStaticIfAbsent(cl, ReflectedClass::parse);
	}

	public final ValueResolver[] getValueResolver(Member member, Function<Member, ValueResolver[]> compute) {
		return computeDynamicIfAbsent(member, compute);
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.meta.MetaContext#searchField(java.lang.String)
	 */
	@Override
	public MetaField getField(MetaPath path) {
		return get(path.toString());
	}

	public final <K, V> V setDynamic(K key, V newValue) {
		try {
			padlock.lock();
			dynamicAttributes.put(key, newValue);

			return newValue;
		} finally {
			padlock.unlock();
		}
	}
}
