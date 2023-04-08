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

import com.slytechs.protocol.runtime.internal.foreign.struct.StructMemberName;


/**
 * The Interface PaddedField.
 */
public interface PaddedField {

	/**
	 * The Class ToStructuredField.
	 */
	public class ToStructuredField implements PaddedField {

		/** The layout. */
		private final MemoryLayout layout;

		/**
		 * Instantiates a new to structured field.
		 *
		 * @param layout the layout
		 */
		public ToStructuredField(MemoryLayout layout) {
			this.layout = layout;
		}

		/**
		 * Bit size.
		 *
		 * @return the long
		 * @see com.slytechs.jnet.jnapatech.internal.foreign.PaddedField#bitSize()
		 */
		@Override
		public long bitSize() {
			return layout.bitSize();
		}

		/**
		 * As memory layout.
		 *
		 * @return the memory layout
		 * @see com.slytechs.jnet.jnapatech.internal.foreign.PaddedField#asMemoryLayout()
		 */
		@Override
		public MemoryLayout asMemoryLayout() {
			return layout;
		}

		/**
		 * @see com.slytechs.protocol.runtime.internal.foreign.PaddedField#bitAlignment()
		 */
		@Override
		public long bitAlignment() {
			return layout.bitAlignment();
		}

		/**
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return layout.toString();
		}
	}

	/**
	 * Byte size.
	 *
	 * @return the long
	 */
	default long byteSize() {
		return bitSize() / 8;
	}

	/**
	 * Bit size.
	 *
	 * @return the long
	 */
	long bitSize();

	/**
	 * As memory layout.
	 *
	 * @return the memory layout
	 */
	MemoryLayout asMemoryLayout();

	/**
	 * As memory layout.
	 *
	 * @param name the name
	 * @return the memory layout
	 */
	default MemoryLayout asMemoryLayout(String name) {
		return asMemoryLayout().withName(name);
	}

	/**
	 * As memory layout.
	 *
	 * @param member the member
	 * @return the memory layout
	 */
	default MemoryLayout asMemoryLayout(StructMemberName member) {
		return asMemoryLayout().withName(member.memberName());
	}

	/**
	 * Bit alignment.
	 *
	 * @return the long
	 */
	long bitAlignment();

}