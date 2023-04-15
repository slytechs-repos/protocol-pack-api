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
package com.slytechs.protocol.pack.core.constants;

import java.util.function.Supplier;

import com.slytechs.protocol.descriptor.DescriptorType;
import com.slytechs.protocol.descriptor.IpfDescriptor;

/**
 * The Enum IpfDescriptorType.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public enum IpfDescriptorType implements DescriptorType<IpfDescriptor> {

	/** IP fragmentation tracking and reassembly descriptor. */
	IPF(DescriptorType.DESCRIPTOR_TYPE_IPF, IpfDescriptor::new),

	;

	/** The type. */
	private final int type;

	/** The factory. */
	private final Supplier<IpfDescriptor> factory;

	/**
	 * Instantiates a new packet descriptor type.
	 *
	 * @param type    the type
	 * @param factory the factory
	 */
	IpfDescriptorType(int type, Supplier<IpfDescriptor> factory) {
		this.factory = factory;
		this.type = type;
	}

	/**
	 * Gets the as int.
	 *
	 * @return the as int
	 * @see com.slytechs.protocol.descriptor.DescriptorType#getAsInt()
	 */
	@Override
	public int getAsInt() {
		return type;
	}

	/**
	 * New descriptor.
	 *
	 * @return the ipf descriptor
	 * @see com.slytechs.protocol.descriptor.DescriptorType#newDescriptor()
	 */
	@Override
	public IpfDescriptor newDescriptor() {
		return factory.get();
	}

}
