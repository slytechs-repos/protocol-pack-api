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

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public class IntRing extends Ring {

	private final int[] table;

	public IntRing(int size) {
		super(size);
		this.table = new int[size];
	}

	public boolean enqueue(int value) {
		throw new UnsupportedOperationException();
	}

	public boolean bulkEnqueue(int... values) {
		throw new UnsupportedOperationException();
	}

	public int burstEnqueue(int... values) {
		throw new UnsupportedOperationException();
	}

	public boolean dequeue(int[] dstOfSizeOne) {
		throw new UnsupportedOperationException();
	}

	public boolean bulkDequeue(int count, int[] dst) {
		throw new UnsupportedOperationException();
	}

	public int burstDequeue(int count, int[] dst) {
		throw new UnsupportedOperationException();
	}
}
