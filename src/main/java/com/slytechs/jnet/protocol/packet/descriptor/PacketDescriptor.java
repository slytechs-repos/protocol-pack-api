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

import com.slytechs.jnet.protocol.core.constants.L2FrameType;
import com.slytechs.jnet.protocol.core.constants.PacketDescriptorType;
import com.slytechs.jnet.protocol.packet.HeaderLookup;
import com.slytechs.jnet.protocol.packet.meta.Meta;
import com.slytechs.jnet.protocol.packet.meta.Meta.MetaType;
import com.slytechs.jnet.runtime.MemoryBinding;
import com.slytechs.jnet.runtime.time.TimestampUnit;
import com.slytechs.jnet.runtime.util.Detail;
import com.slytechs.jnet.runtime.util.StringBuildable;

/**
 * The Class PacketDescriptor.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public abstract class PacketDescriptor
		extends MemoryBinding
		implements HeaderLookup, StringBuildable {

	/** The type. */
	private final PacketDescriptorType type;

	/** The frame no. */
	@Meta(MetaType.ATTRIBUTE)
	public long frameNo;

	/** The timestamp unit. */
	private TimestampUnit timestampUnit = TimestampUnit.PCAP_MICRO;

	/**
	 * Instantiates a new packet descriptor.
	 *
	 * @param type the type
	 */
	protected PacketDescriptor(PacketDescriptorType type) {
		this.type = type;
	}

	/**
	 * Type.
	 *
	 * @return the packet descriptor type
	 */
	public final PacketDescriptorType type() {
		return type;
	}

	/**
	 * Byte size min.
	 *
	 * @return the int
	 */
	public int byteSizeMin() {
		return byteSize();
	}

	/**
	 * Byte size max.
	 *
	 * @return the int
	 */
	public int byteSizeMax() {
		return byteSize();
	}

	/**
	 * Frame no.
	 *
	 * @return the long
	 */
	public long frameNo() {
		return frameNo;
	}

	/**
	 * Frame no.
	 *
	 * @param newNumber the new number
	 * @return the packet descriptor
	 */
	public PacketDescriptor frameNo(long newNumber) {
		this.frameNo = newNumber;

		return this;
	}

	/**
	 * Byte size.
	 *
	 * @return the int
	 */
	public abstract int byteSize();

	/**
	 * Timestamp.
	 *
	 * @return the long
	 */
	public abstract long timestamp();

	/**
	 * Capture length.
	 *
	 * @return the int
	 */
	public abstract int captureLength();

	/**
	 * Wire length.
	 *
	 * @return the int
	 */
	public abstract int wireLength();

	/**
	 * L 2 frame type.
	 *
	 * @return the int
	 */
	public int l2FrameType() {
		return L2FrameType.L2_FRAME_TYPE_UNKNOWN; // Layer2 frame type unknown
	}

	/**
	 * To string.
	 *
	 * @return the string
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		return buildString();
	}

	/**
	 * @see com.slytechs.jnet.runtime.util.StringBuildable#buildString(java.lang.StringBuilder, com.slytechs.jnet.runtime.util.Detail)
	 */
	@Override
	public final StringBuilder buildString(StringBuilder b, Detail detail) {
		String newLine = detail.isLow() ? "" : "\n";

		b.append(getClass().getSimpleName());
		b.append(" [").append(newLine);

		buildDetailedString(b, detail);

		b.append("]");

		return b;
	}

	/**
	 * Builds the detailed string.
	 *
	 * @param b      the b
	 * @param detail the detail
	 * @return the string builder
	 */
	protected abstract StringBuilder buildDetailedString(StringBuilder b, Detail detail);

	/**
	 * Timestamp unit.
	 *
	 * @param timestampUnit the timestamp unit
	 */
	public void timestampUnit(TimestampUnit timestampUnit) {
		this.timestampUnit = timestampUnit;
	}

	/**
	 * Timestamp unit.
	 *
	 * @return the timestamp unit
	 */
	public TimestampUnit timestampUnit() {
		return this.timestampUnit;
	}

}
