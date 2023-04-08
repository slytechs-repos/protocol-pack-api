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
package com.slytechs.jnet.protocol.packet.descriptor;

import com.slytechs.jnet.protocol.Descriptor;
import com.slytechs.jnet.protocol.core.constants.IpfDescriptorType;

/**
 * A IP fragment tracking descriptor. This descriptor type, tracks IP fragments
 * as they are captured, reassembled and dispatched to program packet handlers.
 * This descriptor is supplied in addition to the regular type descriptors and
 * can forward header lookup calls for protocol resolution.
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public final class IpfDescriptor extends Descriptor {

	/**
	 * Instantiates a new ipf descriptor.
	 */
	public IpfDescriptor() {
		super(IpfDescriptorType.IPF);
	}

	/**
	 * @see com.slytechs.jnet.protocol.Descriptor#type()
	 */
	@Override
	public IpfDescriptorType type() {
		return IpfDescriptorType.IPF;
	}

}
