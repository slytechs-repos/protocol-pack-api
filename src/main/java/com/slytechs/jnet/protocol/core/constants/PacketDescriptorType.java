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

import java.util.function.IntSupplier;
import java.util.function.Supplier;

import com.slytechs.jnet.protocol.packet.descriptor.PacketDescriptor;
import com.slytechs.jnet.protocol.packet.descriptor.PcapDescriptor;
import com.slytechs.jnet.protocol.packet.descriptor.ReflectedDescriptorFactory;
import com.slytechs.jnet.protocol.packet.descriptor.Type1Descriptor;
import com.slytechs.jnet.protocol.packet.descriptor.Type2Descriptor;

/**
 * A constant table of packet descriptor types supported by jNet modules.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public enum PacketDescriptorType implements IntSupplier {

	/** 16 bytes PCAP descriptor. */
	PCAP(PcapDescriptor::new),

	/** 16 byte descriptor, providing basic l2, l3 and l4 information. */
	TYPE1(Type1Descriptor::new),

	/**
	 * 24-152 byte descriptor, providing RX and TX capabilities, as well as protocol
	 * dissection records.
	 */
	TYPE2(Type2Descriptor::new),

	/** IP fragmentation descriptor defined in <em>jnetpcap-pro</em> modules */
	IPF(20, PacketDescriptorType.IPF_DESC_MODULENAME, PacketDescriptorType.IPF_DESC_CLASSNAME),

	/** 16 byte Napatech STD descriptor. */
	DESCRIPTOR_TYPE_NT_STD(100, null)

	; // EOF Constant table

	/** Either 16 bytes. */
	public static final int PACKET_DESCRIPTOR_TYPE_PCAP = 0; // 16 bytes

	/** 16 byte CORE_PROTOCOLS descriptor. */
	public static final int PACKET_DESCRIPTOR_TYPE_TYPE1 = 1; // 16 byte CORE_PROTOCOLS descriptor

	/** The Constant DESCRIPTOR_TYPE_TYPE2. */
	public static final int PACKET_DESCRIPTOR_TYPE_TYPE2 = 2;

	/** 16 byte NT STD descriptor. */
	public static final int PACKET_DESCRIPTOR_TYPE_NT_STD = 100; // 16 byte Napatech STD descriptor

	private static final String IPF_DESC_MODULENAME = "com.slytechs.jnetpcap.pro";
	private static final String IPF_DESC_CLASSNAME = "com.slytechs.jnetpcap.pro.IpfDescriptor";

	/** The type. */
	private final int type;

	/** The factory. */
	private final Supplier<PacketDescriptor> factory;

	/**
	 * Instantiates a new packet descriptor type.
	 *
	 * @param factory the factory
	 */
	PacketDescriptorType(Supplier<PacketDescriptor> factory) {
		this.factory = factory;
		this.type = ordinal();
	}

	/**
	 * Instantiates a new packet descriptor type.
	 *
	 * @param type    the type
	 * @param factory the factory
	 */
	PacketDescriptorType(int type, Supplier<PacketDescriptor> factory) {
		this.factory = factory;
		this.type = type;
	}

	/**
	 * Instantiates a new packet descriptor type.
	 *
	 * @param type    the type
	 * @param factory the factory
	 */
	PacketDescriptorType(int type, String moduleName, String className) {
		this.factory = new ReflectedDescriptorFactory(moduleName, className);
		this.type = type;
	}

	/**
	 * Gets an equivalent numerical constant for this descriptor type. Suitable in
	 * passing to native jNet native library functions.
	 *
	 * @return numerical equivalent
	 * @see java.util.function.IntSupplier#getAsInt()
	 */
	@Override
	public int getAsInt() {
		return type;
	}

	/**
	 * New descriptor.
	 *
	 * @return the packet descriptor
	 */
	public PacketDescriptor newDescriptor() {
		return factory.get();
	}
}
