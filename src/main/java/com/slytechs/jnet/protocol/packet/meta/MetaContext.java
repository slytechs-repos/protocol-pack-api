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

import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntFunction;

public interface MetaContext extends MetaDomain {

	interface MetaIndexed extends MetaContext {
		<V> void append(V value);

		int capacity();

		<V> V getAtIndex(int index);

		default <V> V getAtIndexOrCompute(int index, IntFunction<V> func) {
			V v = getAtIndex(index);

			if (v == null) {
				v = func.apply(index);
				setAtIndex(index, v);
			}

			return v;
		}

		int limit();

		<V> void limit(int newLimit, IntFunction<V> func);

		default int remaining() {
			return capacity() - limit();
		}

		default <V> void setAll(IntFunction<V> func) {
			int count = limit();
			for (int i = 0; i < count; i++)
				setAtIndex(i, func.apply(i));
		}

		<V> void setAtIndex(int index, V value);
	}

	interface MetaMapped extends MetaContext {

		public interface Proxy extends MetaMapped {

			static MetaMapped of(MetaDomain parent, MetaMapped proxy) {
				return new Proxy() {

					@Override
					public MetaContext searchForDomain(MetaPath path) {
						return parent.searchForDomain(path);
					}

					@Override
					public Optional<MetaField> searchForField(MetaPath path) {
						return parent.searchForField(path);
					}

					@Override
					public MetaMapped getProxy() {
						return proxy;
					}

					@Override
					public <K, V> Optional<V> searchFor(MetaPath domain, K key, Class<V> valueType) {
						return parent.searchFor(null, key, null);
					}

					@Override
					public <K, V> Optional<V> searchFor(K key, Class<V> valueType) {
						return parent.searchFor(key, valueType);
					}

				};
			}

			MetaMapped getProxy();

			/**
			 * @see com.slytechs.jnet.protocol.packet.meta.MetaContext.MetaMapped#capacity()
			 */
			@Override
			default int capacity() {
				return getProxy().capacity();
			}

			/**
			 * @see com.slytechs.jnet.protocol.packet.meta.MetaContext.MetaMapped#get(java.lang.Object)
			 */
			@Override
			default <K, V> V get(K key) {
				return getProxy().get(key);
			}

			/**
			 * @see com.slytechs.jnet.protocol.packet.meta.MetaContext.MetaMapped#getField(java.lang.String)
			 */
			@Override
			default MetaField getField(String name) {
				return getProxy().getField(name);
			}

			/**
			 * @see com.slytechs.jnet.protocol.packet.meta.MetaContext.MetaMapped#getOrCompute(java.lang.Object,
			 *      java.util.function.Function)
			 */
			@Override
			default <K, V> V getOrCompute(K key, Function<K, V> func) {
				return getProxy().getOrCompute(key, func);
			}

			/**
			 * @see com.slytechs.jnet.protocol.packet.meta.MetaContext.MetaMapped#remaining()
			 */
			@Override
			default int remaining() {
				return getProxy().remaining();
			}

			/**
			 * @see com.slytechs.jnet.protocol.packet.meta.MetaContext.MetaMapped#set(java.lang.Object,
			 *      java.lang.Object)
			 */
			@Override
			default <K, V> void set(K key, V value) {
				getProxy().set(key, value);
			}

			/**
			 * @see com.slytechs.jnet.protocol.packet.meta.MetaContext.MetaMapped#size()
			 */
			@Override
			default int size() {
				return getProxy().size();
			}

		}

		default int capacity() {
			return size();
		}

		<K, V> V get(K key);

		default MetaField getField(String name) {
			return get(name);
		}
		
		default Optional<MetaField> findField(String name) {
			return Optional.ofNullable(getField(name));
		}

		default <K, V> V getOrCompute(K key, Function<K, V> func) {
			V v = get(key);

			if (v == null) {
				v = func.apply(key);
				set(key, v);
			}

			return v;
		}

		default int remaining() {
			return capacity() - size();
		}

		default <K, V> void set(K key, V value) {
			throw new UnsupportedOperationException("setters not supported for this domain type");
		}

		int size();
	}

	String GLOBAL_DOMAINNAME = "global";

	@SuppressWarnings("unchecked")
	default <T extends MetaContext> T cast() {
		return (T) this;
	}
}
