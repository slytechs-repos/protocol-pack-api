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

import com.slytechs.jnet.protocol.constants.L2FrameType;
import com.slytechs.jnet.protocol.constants.PacketDescriptorType;
import com.slytechs.jnet.protocol.packet.HeaderLookup;
import com.slytechs.jnet.protocol.packet.meta.Meta;
import com.slytechs.jnet.protocol.packet.meta.Meta.MetaType;
import com.slytechs.jnet.runtime.resource.MemoryBinding;
import com.slytechs.jnet.runtime.time.TimestampUnit;
import com.slytechs.jnet.runtime.util.Detail;
import com.slytechs.jnet.runtime.util.StringBuildable;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public abstract class PacketDescriptor
		extends MemoryBinding
		implements HeaderLookup, StringBuildable {

	private final PacketDescriptorType type;

	@Meta(MetaType.ATTRIBUTE)
	public long frameNo;

	private TimestampUnit timestampUnit = TimestampUnit.PCAP_MICRO;

	protected PacketDescriptor(PacketDescriptorType type) {
		this.type = type;
	}

	public final PacketDescriptorType type() {
		return type;
	}

	public int byteSizeMin() {
		return byteSize();
	}

	public int byteSizeMax() {
		return byteSize();
	}

	public long frameNo() {
		return frameNo;
	}

	public PacketDescriptor frameNo(long newNumber) {
		this.frameNo = newNumber;

		return this;
	}

	public abstract int byteSize();

	public abstract long timestamp();

	public abstract int captureLength();

	public abstract int wireLength();

	public int l2FrameType() {
		return L2FrameType.L2_FRAME_TYPE_UNKNOWN; // Layer2 frame type unknown
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		return buildString();
	}

	@Override
	public final StringBuilder buildString(StringBuilder b, Detail detail) {
		String newLine = detail.isLow() ? "" : "\n";

		b.append(getClass().getSimpleName());
		b.append(" [").append(newLine);

		buildDetailedString(b, detail);

		b.append("]");

		return b;
	}

	protected abstract StringBuilder buildDetailedString(StringBuilder b, Detail detail);

	/**
	 * @param timestampUnit
	 */
	public void timestampUnit(TimestampUnit timestampUnit) {
		this.timestampUnit = timestampUnit;
	}

	public TimestampUnit timestampUnit() {
		return this.timestampUnit;
	}

}
