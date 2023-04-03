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
package com.slytechs.jnet.runtime.internal.foreign;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemoryLayout.PathElement;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SequenceLayout;
import java.lang.invoke.VarHandle;
import java.util.function.Function;
import java.util.function.IntFunction;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public final class MemoryUtils {

	public static VarHandle varHandle(MemoryLayout LAYOUT, String path) {
		return LAYOUT.varHandle(path(path));
	}

	public static long byteOffset(MemoryLayout layout, String path) {
		return layout.byteOffset(path(path));
	}

	public static PathElement[] path(String path) {
		String[] elements = path.split("\\.|\\$");

		return path(elements);
	}

	private static PathElement[] path(String... elements) {
		PathElement[] array = new PathElement[elements.length];

		for (int i = 0; i < elements.length; i++) {
			array[i] = parsePathElement(elements[i]);
		}

		return array;
	}

	private static PathElement parsePathElement(String name) {
		if (name.equals("[]")) {
			return PathElement.sequenceElement();
		}

		return PathElement.groupElement(name);
	}

	private MemoryUtils() {
	}

	public static MemoryLayout select(MemoryLayout layout, String path) {
		return layout.select(path(path));
	}

	public static SequenceLayout selectSequence(MemoryLayout layout, String path) {
		return (SequenceLayout) layout.select(path(path));
	}

	public static class SequenceVar<T> implements IntFunction<T> {

		private Object[] array;
		private Function<MemorySegment, T> factory;
		private MemorySegment segment;
		private long byteSize;

		public SequenceVar(MemorySegment segment, SequenceLayout layout,
				Function<MemorySegment, T> factory) {
			this.segment = segment;
			this.byteSize = layout.elementLayout().byteSize();
			this.factory = factory;

			array = new Object[(int) layout.elementCount()];
		}

		/**
		 * @see java.util.function.IntFunction#apply(int)
		 */
		@Override
		public T apply(int index) {
			Object t = array[index];

			if (t == null)
				t = factory.apply(segment.asSlice(index * byteSize));

			return (T) t;
		}

	}

	public static <T> T[] arrayOf(MemorySegment segment, SequenceLayout layout,
			IntFunction<T[]> arrayFactory, Function<MemorySegment, T> elementFactory) {
	
		int count = (int) layout.elementCount();
		long size = layout.elementLayout().byteSize();
		T[] array = arrayFactory.apply(count);
		
		if (segment.byteSize() == 0)
			throw new IllegalStateException("size is zero");
		
	
		for (int i = 0; i < count; i++) {
			long offset = i * size;
			
			MemorySegment newSegment = segment.asSlice(offset, size);
	
			array[i] = elementFactory.apply(newSegment);
		}
	
		return array;
	}
}
