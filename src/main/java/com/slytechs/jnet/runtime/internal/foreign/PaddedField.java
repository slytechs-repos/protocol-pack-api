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

import com.slytechs.jnet.runtime.internal.foreign.struct.StructMemberName;


public interface PaddedField {

	public class ToStructuredField implements PaddedField {

		private final MemoryLayout layout;

		public ToStructuredField(MemoryLayout layout) {
			this.layout = layout;
		}

		/**
		 * @see com.slytechs.jnet.jnapatech.internal.foreign.PaddedField#bitSize()
		 */
		@Override
		public long bitSize() {
			return layout.bitSize();
		}

		/**
		 * @see com.slytechs.jnet.jnapatech.internal.foreign.PaddedField#asMemoryLayout()
		 */
		@Override
		public MemoryLayout asMemoryLayout() {
			return layout;
		}

		@Override
		public long bitAlignment() {
			return layout.bitAlignment();
		}

		@Override
		public String toString() {
			return layout.toString();
		}
	}

	default long byteSize() {
		return bitSize() / 8;
	}

	long bitSize();

	MemoryLayout asMemoryLayout();

	default MemoryLayout asMemoryLayout(String name) {
		return asMemoryLayout().withName(name);
	}

	default MemoryLayout asMemoryLayout(StructMemberName member) {
		return asMemoryLayout().withName(member.memberName());
	}

	long bitAlignment();

}