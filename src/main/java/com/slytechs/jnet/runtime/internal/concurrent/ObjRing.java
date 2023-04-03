/*
 * Apache License, Version 2.0
 * 
 * Copyright 2013-2022 Sly Technologies Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.slytechs.jnet.runtime.internal.concurrent;

import java.util.function.IntFunction;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public class ObjRing<T> extends Ring {

	private final T[] table;
	private final IntFunction<T[]> allocator;

	public ObjRing(int size, IntFunction<T[]> allocator) {
		super(size);
		this.table = allocator.apply(size);
		this.allocator = allocator;
	}

	public boolean enqueue(T value) {
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings("unchecked")
	public boolean bulkEnqueue(T... values) {
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings("unchecked")
	public int burstEnqueue(T... values) {
		throw new UnsupportedOperationException();
	}
}
