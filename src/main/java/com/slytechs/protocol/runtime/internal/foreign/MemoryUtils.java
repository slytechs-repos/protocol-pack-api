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
package com.slytechs.protocol.runtime.internal.foreign;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemoryLayout.PathElement;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SequenceLayout;
import java.lang.invoke.VarHandle;
import java.util.function.Function;
import java.util.function.IntFunction;

/**
 * The Class MemoryUtils.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public final class MemoryUtils {

	/**
	 * Var handle.
	 *
	 * @param LAYOUT the layout
	 * @param path   the path
	 * @return the var handle
	 */
	public static VarHandle varHandle(MemoryLayout LAYOUT, String path) {
		return LAYOUT.varHandle(path(path));
	}

	/**
	 * Byte offset.
	 *
	 * @param layout the layout
	 * @param path   the path
	 * @return the long
	 */
	public static long byteOffset(MemoryLayout layout, String path) {
		return layout.byteOffset(path(path));
	}

	/**
	 * Path.
	 *
	 * @param path the path
	 * @return the path element[]
	 */
	public static PathElement[] path(String path) {
		String[] elements = path.split("\\.|\\$");

		return path(elements);
	}

	/**
	 * Path.
	 *
	 * @param elements the elements
	 * @return the path element[]
	 */
	private static PathElement[] path(String... elements) {
		PathElement[] array = new PathElement[elements.length];

		for (int i = 0; i < elements.length; i++) {
			array[i] = parsePathElement(elements[i]);
		}

		return array;
	}

	/**
	 * Parses the path element.
	 *
	 * @param name the name
	 * @return the path element
	 */
	private static PathElement parsePathElement(String name) {
		if (name.equals("[]")) {
			return PathElement.sequenceElement();
		}

		return PathElement.groupElement(name);
	}

	/**
	 * Instantiates a new memory utils.
	 */
	private MemoryUtils() {
	}

	/**
	 * Select.
	 *
	 * @param layout the layout
	 * @param path   the path
	 * @return the memory layout
	 */
	public static MemoryLayout select(MemoryLayout layout, String path) {
		return layout.select(path(path));
	}

	/**
	 * Select sequence.
	 *
	 * @param layout the layout
	 * @param path   the path
	 * @return the sequence layout
	 */
	public static SequenceLayout selectSequence(MemoryLayout layout, String path) {
		return (SequenceLayout) layout.select(path(path));
	}

	/**
	 * The Class SequenceVar.
	 *
	 * @param <T> the generic type
	 */
	public static class SequenceVar<T> implements IntFunction<T> {

		/** The array. */
		private Object[] array;
		
		/** The factory. */
		private Function<MemorySegment, T> factory;
		
		/** The segment. */
		private MemorySegment segment;
		
		/** The byte size. */
		private long byteSize;

		/**
		 * Instantiates a new sequence var.
		 *
		 * @param segment the segment
		 * @param layout  the layout
		 * @param factory the factory
		 */
		public SequenceVar(MemorySegment segment, SequenceLayout layout,
				Function<MemorySegment, T> factory) {
			this.segment = segment;
			this.byteSize = layout.elementLayout().byteSize();
			this.factory = factory;

			array = new Object[(int) layout.elementCount()];
		}

		/**
		 * Apply.
		 *
		 * @param index the index
		 * @return the t
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

	/**
	 * Array of.
	 *
	 * @param <T>            the generic type
	 * @param segment        the segment
	 * @param layout         the layout
	 * @param arrayFactory   the array factory
	 * @param elementFactory the element factory
	 * @return the t[]
	 */
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
