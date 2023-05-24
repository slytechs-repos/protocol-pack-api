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
package com.slytechs.protocol.pack.core;

import java.nio.ByteBuffer;

import com.slytechs.protocol.Header;
import com.slytechs.protocol.HeaderExtension;
import com.slytechs.protocol.descriptor.IpfReassembly;
import com.slytechs.protocol.descriptor.IpfTracking;
import com.slytechs.protocol.descriptor.PacketDescriptor;
import com.slytechs.protocol.meta.Meta;
import com.slytechs.protocol.meta.MetaResource;
import com.slytechs.protocol.pack.core.Ip.IpOption;
import com.slytechs.protocol.pack.core.constants.CoreConstants;
import com.slytechs.protocol.pack.core.constants.CoreId;
import com.slytechs.protocol.pack.core.constants.IpfDescriptorType;
import com.slytechs.protocol.runtime.internal.util.collection.IntArrayList;
import com.slytechs.protocol.runtime.internal.util.collection.IntList;

/**
 * Internet Protocol base definition.
 * <p>
 * Internet Protocol is a widely used protocol in data communication over
 * various types of networks, particularly in packet-switched layer networks
 * like Ethernet. It provides a logical connection between network devices by
 * assigning identification to each device. IP uses a best effort delivery model
 * and is a connectionless protocol, meaning that neither delivery nor proper
 * sequencing or avoidance of duplicate delivery is guaranteed.
 * 
 * @author Sly Technologies
 * @author repos@slytechs.com
 * @param <T> the generic type
 */
@MetaResource("ip-meta.json")
public sealed class Ip<T extends IpOption> extends HeaderExtension<T>
		permits Ip4, Ip6 {

	/**
	 * The Class IpOption.
	 */
	public static abstract class IpOption extends Header {

		/**
		 * Instantiates a new ip option.
		 *
		 * @param id the id
		 */
		protected IpOption(int id) {
			super(id);
		}
	}

	/** The Constant generic IP header ID, classifier only. */
	public static final int ID = CoreId.CORE_ID_IP;

	private int version;

	public Ip() {
		super(ID);
	}

	/**
	 * Instantiates a new ip.
	 *
	 * @param id the id
	 */
	protected Ip(int id) {
		super(id);
	}

	@Meta
	public int protocol() {
		if (version == 4)
			return Ip4Struct.PROTO.getInt(buffer());
		else
			return Ip6Layout.NEXT.getInt(buffer());
	}

	/**
	 * Dst.
	 *
	 * @return the byte[]
	 */
	@Meta
	public byte[] dst() {
		if (version == 4) {
			return dst(new byte[CoreConstants.IPv4_FIELD_SRC_LEN], 0);
		} else {
			return dst(new byte[CoreConstants.IPv6_FIELD_SRC_LEN], 0);

		}
	}

	public byte[] dst(byte[] dstArray) {
		return dst(dstArray, 0);
	}

	public byte[] dst(byte[] dstArray, int offset) {
		if (version == 4)
			buffer().get(CoreConstants.IPv4_FIELD_DST, dstArray, offset, CoreConstants.IPv4_FIELD_DST_LEN);
		else
			buffer().get(CoreConstants.IPv6_FIELD_DST, dstArray, offset, CoreConstants.IPv6_FIELD_DST_LEN);

		return dstArray;
	}

	/**
	 * Dst address.
	 *
	 * @return the ip address
	 */
	public IpAddress dstAsAddress() {
		return version == 4 ? new Ip4Address(dst()) : new Ip6Address(dst());
	}

	/**
	 * A list of reassembled IP fragments.
	 *
	 * @return array of frame numbers for each of the fragments that were used to
	 *         reassemble this IP datagram. This method never returns null.
	 */
	public final long[] getIpfFrameIndexes() {
		IntList list = new IntArrayList();
		if (hasIpfTrackingDescriptor())
			throw new UnsupportedOperationException("tracking retrieval not implemented yet");
		else
			return new long[0];
	}

	public final IpfReassembly getIpfReassemblyDescriptor() {
		return (IpfReassembly) super.descriptor().peekDescriptor(IpfDescriptorType.IPF_REASSEMBLY);
	}

	public final IpfTracking getIpfTrackingDescriptor() {
		return (IpfTracking) super.descriptor().peekDescriptor(IpfDescriptorType.IPF_TRACKING);
	}

	public final ByteBuffer getReassembledBuffer() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/**
	 * Checks if is reassembly tracking.
	 *
	 * @return true, if is reassembly tracking
	 */
	public final boolean hasIpfTrackingDescriptor() {
		return super.descriptor().hasDescriptor(IpfDescriptorType.IPF_TRACKING);
	}

	/**
	 * Checks if this IP datagram has been reassembled.
	 *
	 * @return true, if is reassembled
	 */
	public final boolean isReassembled() {
		return super.descriptor().hasDescriptor(IpfDescriptorType.IPF_REASSEMBLY);
	}

	@Override
	protected void onBind() {
		/* Common to both v4 and v6 */
		this.version = Ip4Struct.VERSION.getInt(buffer());

		super.onBind();
	}

	/**
	 * Payload length.
	 *
	 * @return the int
	 * @see com.slytechs.protocol.Header#payloadLength()
	 */
	@Override
	public int payloadLength() {
		PacketDescriptor desc = descriptor();
		int payloadLen = desc.captureLength() - super.payloadOffset();

		return payloadLen;
	}

	/**
	 * Src.
	 *
	 * @return the byte[]
	 */
	@Meta
	public byte[] src() {
		if (version == 4)
			return src(new byte[CoreConstants.IPv4_FIELD_SRC_LEN], 0);
		else
			return src(new byte[CoreConstants.IPv6_FIELD_SRC_LEN], 0);
	}

	public byte[] src(byte[] srcArray) {
		return src(srcArray, 0);
	}

	public byte[] src(byte[] srcArray, int offset) {
		if (version == 4)
			buffer().get(CoreConstants.IPv4_FIELD_SRC, srcArray, offset, CoreConstants.IPv4_FIELD_SRC_LEN);
		else
			buffer().get(CoreConstants.IPv6_FIELD_SRC, srcArray, offset, CoreConstants.IPv6_FIELD_SRC_LEN);

		return srcArray;
	}

	/**
	 * Src get as address.
	 *
	 * @return the ip address
	 */
	public IpAddress srcAsAddress() {
		return version == 4
				? new Ip4Address(src())
				: new Ip6Address(src());
	}

	/**
	 * IP header version.
	 *
	 * @return the IP header version as read from the first byte of the header
	 *         buffer
	 */
	@Meta
	public int version() {
		return this.version;
	}
}
