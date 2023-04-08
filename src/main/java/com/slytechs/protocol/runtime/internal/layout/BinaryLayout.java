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
package com.slytechs.protocol.runtime.internal.layout;

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

		/**
		 * @see com.slytechs.protocol.runtime.internal.layout.BinaryLayout#whenEnabled(boolean)
		 */
		@Override
		default BinaryLayout whenEnabled(boolean enabled) {
			return proxyLayout().whenEnabled(enabled);
		}

		/**
		 * @see com.slytechs.protocol.runtime.internal.layout.BinaryLayout#isEnabled()
		 */
		@Override
		default boolean isEnabled() {
			return proxyLayout().isEnabled();
		}

		/**
		 * Array field.
		 *
		 * @param path the path
		 * @return the array field
		 * @see com.slytechs.protocol.runtime.internal.layout.BinaryLayout#arrayField(java.lang.String)
		 */
		@Override
		default ArrayField arrayField(String path) {
			return proxyLayout().arrayField(path);
		}

		/**
		 * Bit alignment.
		 *
		 * @return the int
		 * @see com.slytechs.protocol.runtime.internal.layout.BinaryLayout#bitAlignment()
		 */
		@Override
		default int bitAlignment() {
			return proxyLayout().bitAlignment();
		}

		/**
		 * @see com.slytechs.protocol.runtime.internal.layout.BinaryLayout#bitField(com.slytechs.protocol.runtime.internal.layout.Path[])
		 */
		@Override
		default BitField bitField(Path... elements) {
			return proxyLayout().bitField(elements);
		}

		/**
		 * Bit field.
		 *
		 * @param path the path
		 * @return the bit field
		 * @see com.slytechs.protocol.runtime.internal.layout.BinaryLayout#bitField(java.lang.String)
		 */
		@Override
		default BitField bitField(String path) {
			return proxyLayout().bitField(path);
		}

		/**
		 * Bit size.
		 *
		 * @return the long
		 * @see com.slytechs.protocol.runtime.internal.layout.BinaryLayout#bitSize()
		 */
		@Override
		default long bitSize() {
			return proxyLayout().bitSize();
		}

		/**
		 * Byte alignment.
		 *
		 * @return the long
		 * @see com.slytechs.protocol.runtime.internal.layout.BinaryLayout#byteAlignment()
		 */
		@Override
		default long byteAlignment() {
			return proxyLayout().byteAlignment();
		}

		/**
		 * Byte size.
		 *
		 * @return the long
		 * @see com.slytechs.protocol.runtime.internal.layout.BinaryLayout#byteSize()
		 */
		@Override
		default long byteSize() {
			return proxyLayout().byteSize();
		}

		/**
		 * Layout name.
		 *
		 * @return the optional
		 * @see com.slytechs.protocol.runtime.internal.layout.BinaryLayout#layoutName()
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
		 * Size.
		 *
		 * @return the optional long
		 * @see com.slytechs.protocol.runtime.internal.layout.BinaryLayout#size()
		 */
		@Override
		default OptionalLong size() {
			return proxyLayout().size();
		}

		/**
		 * With bit alignment.
		 *
		 * @param alignment the alignment
		 * @return the binary layout
		 * @see com.slytechs.protocol.runtime.internal.layout.BinaryLayout#withBitAlignment(int)
		 */
		@Override
		default BinaryLayout withBitAlignment(int alignment) {
			return proxyLayout().withBitAlignment(alignment);
		}

		/**
		 * With name.
		 *
		 * @param name the name
		 * @return the binary layout
		 * @see com.slytechs.protocol.runtime.internal.layout.BinaryLayout#withName(java.lang.String)
		 */
		@Override
		default BinaryLayout withName(String name) {
			return proxyLayout().withName(name);
		}

		/**
		 * @see com.slytechs.protocol.runtime.internal.layout.BinaryLayout#arrayField(com.slytechs.protocol.runtime.internal.layout.Path[])
		 */
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
	 * @param elements the elements
	 * @return the array field
	 */
	ArrayField arrayField(Path... elements);

	/**
	 * Array field.
	 *
	 * @param path the path
	 * @return the array field
	 */
	ArrayField arrayField(String path);

	/**
	 * Bit alignment.
	 *
	 * @return the int
	 */
	int bitAlignment();

	/**
	 * Bit field.
	 *
	 * @param elements the elements
	 * @return the bit field
	 */
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

	/**
	 * Byte int size.
	 *
	 * @return the int
	 */
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

	/**
	 * When enabled.
	 *
	 * @param enabled the enabled
	 * @return the binary layout
	 */
	BinaryLayout whenEnabled(boolean enabled);

	/**
	 * Checks if is enabled.
	 *
	 * @return true, if is enabled
	 */
	boolean isEnabled();
}
