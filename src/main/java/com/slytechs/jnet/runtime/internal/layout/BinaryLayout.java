/*
 * MIT License
 * 
 * Copyright (c) 2020 Sly Technologies Inc.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.slytechs.jnet.runtime.internal.layout;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.stream.Collectors;

/**
 * The Interface BinaryLayout.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public interface BinaryLayout {

	/**
	 * The Interface HasDataCarrier.
	 */
	public interface HasDataCarrier {

		/**
		 * Carrier offset.
		 *
		 * @return the long
		 */
		long carrierOffset();

		/**
		 * Carrier size.
		 *
		 * @return the long
		 */
		long carrierSize();

		/**
		 * Carrier type.
		 *
		 * @return the class
		 */
		Class<?> carrierType();
	}

	/**
	 * The Interface Proxy.
	 */
	public interface Proxy extends BinaryLayout {

		@Override
		default BinaryLayout whenEnabled(boolean enabled) {
			return proxyLayout().whenEnabled(enabled);
		}

		@Override
		default boolean isEnabled() {
			return proxyLayout().isEnabled();
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#arrayField(java.lang.String)
		 */
		@Override
		default ArrayField arrayField(String path) {
			return proxyLayout().arrayField(path);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#bitAlignment()
		 */
		@Override
		default int bitAlignment() {
			return proxyLayout().bitAlignment();
		}

		@Override
		default BitField bitField(Path... elements) {
			return proxyLayout().bitField(elements);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#bitField(java.lang.String)
		 */
		@Override
		default BitField bitField(String path) {
			return proxyLayout().bitField(path);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#bitSize()
		 */
		@Override
		default long bitSize() {
			return proxyLayout().bitSize();
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#byteAlignment()
		 */
		@Override
		default long byteAlignment() {
			return proxyLayout().byteAlignment();
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#byteSize()
		 */
		@Override
		default long byteSize() {
			return proxyLayout().byteSize();
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#layoutName()
		 */
		@Override
		default Optional<String> layoutName() {
			return proxyLayout().layoutName();
		}

		/**
		 * Proxy layout.
		 *
		 * @return the binary layout
		 */
		BinaryLayout proxyLayout();

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#size()
		 */
		@Override
		default OptionalLong size() {
			return proxyLayout().size();
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#withBitAlignment(int)
		 */
		@Override
		default BinaryLayout withBitAlignment(int alignment) {
			return proxyLayout().withBitAlignment(alignment);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout#withName(java.lang.String)
		 */
		@Override
		default BinaryLayout withName(String name) {
			return proxyLayout().withName(name);
		}

		@Override
		default ArrayField arrayField(Path... elements) {
			return proxyLayout().arrayField(elements);
		}

	}

	/**
	 * Padding.
	 *
	 * @param size the size
	 * @return the padding layout
	 */
	public static PaddingLayout padding(long size) {
		return new PaddingLayout(size);
	}

	/**
	 * Resolve proxy.
	 *
	 * @param layout the layout
	 * @return the binary layout
	 */
	private static BinaryLayout resolveProxy(BinaryLayout layout) {
		while (layout instanceof Proxy proxy)
			layout = proxy.proxyLayout();

		return layout;
	}

	/**
	 * Sequence layout.
	 *
	 * @param element the element
	 * @return the sequence layout
	 */
	public static SequenceLayout sequenceLayout(BinaryLayout element) {
		return new SequenceLayout(resolveProxy(element));
	}

	/**
	 * Sequence layout.
	 *
	 * @param count   the count
	 * @param element the element
	 * @return the sequence layout
	 */
	public static SequenceLayout sequenceLayout(long count, BinaryLayout element) {
		return new SequenceLayout(count, resolveProxy(element));
	}

	/**
	 * Struct layout.
	 *
	 * @param layout the layout
	 * @return the struct layout
	 */
	public static StructLayout structLayout(BinaryLayout... layout) {
		return new StructLayout(toList(layout));
	}

	/**
	 * To list.
	 *
	 * @param array the array
	 * @return the list
	 */
	private static List<BinaryLayout> toList(BinaryLayout[] array) {
		return Collections.unmodifiableList(Arrays.asList(array)
				.stream()
				.map(BinaryLayout::resolveProxy)
				.collect(Collectors.toList()));
	}

	/**
	 * Union layout.
	 *
	 * @param layout the layout
	 * @return the union layout
	 */
	public static UnionLayout unionLayout(BinaryLayout... layout) {
		return new UnionLayout(toList(layout));
	}

	/**
	 * Array field.
	 *
	 * @param path the path
	 * @return the array field
	 */
	ArrayField arrayField(Path... elements);

	ArrayField arrayField(String path);

	/**
	 * Bit alignment.
	 *
	 * @return the int
	 */
	int bitAlignment();

	BitField bitField(Path... elements);

	/**
	 * Bit field.
	 *
	 * @param path the path
	 * @return the bit field
	 */
	BitField bitField(String path);

	/**
	 * Bit size.
	 *
	 * @return the long
	 */
	long bitSize();

	/**
	 * Byte alignment.
	 *
	 * @return the long
	 */
	long byteAlignment();

	default int byteIntSize() {
		return (int) byteSize();
	}

	/**
	 * Byte size.
	 *
	 * @return the long
	 */
	long byteSize();

	/**
	 * Layout name.
	 *
	 * @return the optional
	 */
	Optional<String> layoutName();

	/**
	 * Size.
	 *
	 * @return the optional long
	 */
	OptionalLong size();

	/**
	 * With bit alignment.
	 *
	 * @param alignment the alignment
	 * @return the binary layout
	 */
	BinaryLayout withBitAlignment(int alignment);

	/**
	 * With name.
	 *
	 * @param name the name
	 * @return the binary layout
	 */
	BinaryLayout withName(String name);

	BinaryLayout whenEnabled(boolean enabled);

	boolean isEnabled();
}
