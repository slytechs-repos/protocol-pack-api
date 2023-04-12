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
package com.slytechs.protocol.descriptor;

import java.util.Iterator;
import java.util.Optional;

import com.slytechs.protocol.runtime.Binding;
import com.slytechs.protocol.runtime.MemoryBinding;

/**
 * Common base class for all descriptors.
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public abstract class Descriptor
		extends MemoryBinding
		implements Binding, Iterable<Descriptor> {

	/** The type. */
	private final DescriptorType<?> type;
	
	/** The next. */
	private Descriptor next;

	/**
	 * New descriptor constructor.
	 *
	 * @param type the descriptor type
	 */
	protected Descriptor(DescriptorType<?> type) {
		this.type = type;
	}

	/**
	 * Adds a new descriptor to the descriptor chain.
	 *
	 * @param nextDescriptor new next descriptor to be added to the chain
	 * @return the next descriptor added
	 */
	public final Descriptor addDescriptor(Descriptor nextDescriptor) {

		nextDescriptor.next = this.next;
		this.next = nextDescriptor;

		return nextDescriptor;
	}

	/**
	 * Searches for a specific descriptor type in the chain of descriptors.
	 *
	 * @param <T>  the descriptor class type
	 * @param type the descriptor type
	 * @return the first descriptor of the correct type or empty optional
	 */
	public final <T extends Descriptor> Optional<T> findDescriptor(DescriptorType<T> type) {
		return Optional.ofNullable(peekDescriptor(type));
	}

	/**
	 * Iterator.
	 *
	 * @return the iterator
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public final Iterator<Descriptor> iterator() {
		return new Iterator<Descriptor>() {
			Descriptor next = Descriptor.this;

			@Override
			public boolean hasNext() {
				return (next != null);
			}

			@Override
			public Descriptor next() {
				Descriptor prev = next;

				if (next != null)
					next = next.next;

				return prev;
			}

		};
	}

	/**
	 * Next descriptor in a daisy chain of descriptors. Multiple descriptors can be
	 * linked together.
	 *
	 * @return the next descriptor in a chain or null if this is the last one
	 */
	public final Descriptor nextDescriptor() {
		return next;
	}

	/**
	 * On unbind.
	 *
	 * @see com.slytechs.protocol.runtime.MemoryBinding#onUnbind()
	 */
	@Override
	protected void onUnbind() {
		super.onUnbind();

		if (next != null) {
			next.unbind();
			next = null;
		}
	}

	/**
	 * Searches for a specific descriptor type in the chain of descriptors.
	 *
	 * @param <T>  the descriptor class type
	 * @param type the descriptor type
	 * @return the first descriptor of the correct type or null
	 */
	@SuppressWarnings("unchecked")
	public final <T extends Descriptor> T peekDescriptor(DescriptorType<T> type) {
		Descriptor current = this;

		while ((current != null) && (current.type() != type))
			current = current.nextDescriptor();

		return (T) current;
	}

	/**
	 * Get the descriptor type for this descriptor.
	 *
	 * @return the descriptor type
	 */
	public DescriptorType<?> type() {
		return type;
	}

}
