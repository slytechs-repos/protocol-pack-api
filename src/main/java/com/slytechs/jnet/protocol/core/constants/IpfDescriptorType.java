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
package com.slytechs.jnet.protocol.core.constants;

import java.util.function.Supplier;

import com.slytechs.jnet.protocol.descriptor.DescriptorType;
import com.slytechs.jnet.protocol.descriptor.IpfFragment;
import com.slytechs.jnet.protocol.descriptor.IpfReassembly;
import com.slytechs.jnet.protocol.descriptor.IpfTracking;
import com.slytechs.jnet.protocol.descriptor.Ipfdescriptor;

/**
 * The Enum IpfDescriptorType.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public enum IpfDescriptorType implements DescriptorType<Ipfdescriptor> {

	/**
	 * A descriptor used to describe the state on incoming IP fragments. The
	 * corresponding dissector identifies IP fragments, both IPv4 and IPv6 and
	 * creates a descriptor which provides information about the state and contents
	 * of a fragment. This information is then further used in IP reassembly and
	 * tracking.
	 */
	IPF_FRAG(DescriptorType.DESCRIPTOR_TYPE_IPF_FRAG, IpfFragment::new),

	/**
	 * A descriptor used in tracking IP fragments after they have been processed by
	 * the IPF services.
	 */
	IPF_TRACKING(DescriptorType.DESCRIPTOR_TYPE_IPF_TRACKING, IpfTracking::new),

	/** A descriptor used in IP fragment reassembly. */
	IPF_REASSEMBLY(DescriptorType.DESCRIPTOR_TYPE_IPF_REASSEMBLY, IpfReassembly::new),

	;

	/** The type. */
	private final int type;

	/** The factory. */
	private final Supplier<? extends Ipfdescriptor> factory;

	/**
	 * Instantiates a new packet descriptor type.
	 *
	 * @param type    the type
	 * @param factory the factory
	 */
	IpfDescriptorType(int type, Supplier<? extends Ipfdescriptor> factory) {
		this.factory = factory;
		this.type = type;
	}

	/**
	 * Gets the as int.
	 *
	 * @return the as int
	 * @see com.slytechs.jnet.protocol.descriptor.DescriptorType#getAsInt()
	 */
	@Override
	public int getAsInt() {
		return type;
	}

	/**
	 * New descriptor.
	 *
	 * @return the ipf descriptor
	 * @see com.slytechs.jnet.protocol.descriptor.DescriptorType#newDescriptor()
	 */
	@Override
	public Ipfdescriptor newDescriptor() {
		return factory.get();
	}

}
