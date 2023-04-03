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
package com.slytechs.jnet.protocol.core;

import java.util.Set;

import com.slytechs.jnet.protocol.constants.CoreConstants;
import com.slytechs.jnet.protocol.constants.CoreHeaderInfo;
import com.slytechs.jnet.protocol.constants.Ip4Flag;
import com.slytechs.jnet.protocol.constants.IpType;
import com.slytechs.jnet.protocol.packet.Packet;
import com.slytechs.jnet.protocol.packet.format.Display;
import com.slytechs.jnet.protocol.packet.meta.Meta;
import com.slytechs.jnet.protocol.packet.meta.Meta.MetaType;
import com.slytechs.jnet.protocol.packet.meta.MetaResource;

/**
 * 
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
@MetaResource("ip4-meta.json")
public class Ip4 extends Ip<Ip4Option> {
	public static final int ID = CoreHeaderInfo.CORE_ID_IPv4;

	public Ip4() {
		super(ID);
	}

	/**
	 * Checksum status.
	 *
	 * @return -1 = Unverified, 0 = Verfication Failed, 1 = Verfication OK
	 */
//	int checksumStatus() {
//		return getProtocol(Ip4Protocol.class)
//				.checksumStatus(this);
//	}

//	int checksumValidation() {
//		return getProtocol(Ip4Protocol.class)
//				.calculateChecksum(this);
//	}

	public int dsfield() {
		return Ip4Layout.DSFIELD.getInt(buffer());
	}

	public int dsfieldDscp() {
		return Ip4Layout.DSFIELD_DSCP.getInt(buffer());
	}

	public int dsfieldDscpCode() {
		return Ip4Layout.DSFIELD_DSCP_CODE.getInt(buffer());
	}

	String dsfieldDscpInfo() {
		StringBuilder b = new StringBuilder();

		if (dsfieldDscp() == 0)
			b.append("Default (")
					.append(dsfieldDscp())
					.append(")");

		else
			b.append(dsfieldDscp());

		return b.toString();
	}

	int dsfieldDscpSelect() {
		return Ip4Layout.DSFIELD_DSCP_SELECT.getInt(buffer());
	}

	int dsfieldEcn() {
		return Ip4Layout.DSFIELD_ECN.getInt(buffer());
	}

	/**
	 * @return
	 */
	String dsfieldInfo() {
		StringBuilder b = new StringBuilder();

		if (dsfieldDscpSelect() == 0)
			b.append("DSCP: CS").append(dsfieldDscpCode());

		else if (dsfieldDscpSelect() == 1)
			b.append("DSCP: EF").append(dsfieldDscpCode());

		else if (dsfieldDscpSelect() == 2)
			b.append("DSCP: AF").append(dsfieldDscpCode());

		if (dsfieldEcn() == 0)
			b.append(", ENC: Not-ECT");

		return b.toString();
	}

	@Meta
	@Override
	public byte[] dst() {
		return dst(new byte[CoreConstants.IPv4_FIELD_DST_LEN], 0);
	}

	public byte[] dst(byte[] dst) {
		return dst(dst, 0);
	}

	public byte[] dst(byte[] dst, int offset) {
		buffer().get(CoreConstants.IPv4_FIELD_DST, dst, offset, CoreConstants.IPv4_FIELD_DST_LEN);

		return dst;
	}

	@Override
	public Ip4Address dstAddress() {
		return new Ip4Address(dst());
	}

	public int dstAsInt() {
		return Ip4Layout.DST.getInt(buffer());
	}

	@Meta
	public int flags() {
		return Ip4Layout.FLAGS.getInt(buffer());
	}

	public int flagsByte() {
		return Ip4Layout.FLAGS_BYTE.getInt(buffer());
	}

	public int flagsDf() {
		return Ip4Layout.FLAGS_DF.getInt(buffer());
	}

	/**
	 * @return
	 */
	@Meta(MetaType.ATTRIBUTE)
	public String flagsInfo() {
		if (flagsDf() != 0)
			return "Don't fragment";

		if (flagsMf() != 0)
			return "More fragments";

		return "No flags";
	}

	public int flagsMf() {
		return Ip4Layout.FLAGS_MF.getInt(buffer());
	}

	public int flagsNibble() {
		return Ip4Layout.FLAGS_NIBBLE.getInt(buffer());
	}

	public int flagsRb() {
		return Ip4Layout.FLAGS_RB.getInt(buffer());
	}

	@Meta(MetaType.ATTRIBUTE)
	public Set<Ip4Flag> flagsSet() {
		return Ip4Flag.valueOf(flags());
	}

	public int fragOffset() {
		return Ip4Layout.FRAG_OFFSET.getInt(buffer());
	}

	@Meta
	public int hdrLen() {
		return Ip4Layout.HDR_LEN.getInt(buffer());
	}

	@Meta(MetaType.ATTRIBUTE)
	public int hdrLenBytes() {
		return hdrLen() * 4;
	}

	@Meta
	public int identification() {
		return Ip4Layout.ID.getUnsignedShort(buffer());
	}

	/**
	 * @return
	 */
	public boolean isReassembled() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public int payloadLength() {
		return totalLength() - hdrLenBytes();
	}

	@Meta
	public int protocol() {
		return Ip4Layout.PROTO.getInt(buffer());
	}

	public String protocolInfo() {
		return IpType.valueOfIpType(protocol()).name();
	}

	/**
	 * @return
	 */
	public Packet[] reassembledFragments() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	@Meta
	public byte[] src() {
		return src(new byte[CoreConstants.IPv4_FIELD_SRC_LEN], 0);
	}

	public byte[] src(byte[] dst) {
		return src(dst, 0);
	}

	public byte[] src(byte[] dst, int offset) {
		byte[] debug = new byte[20];
		buffer().get(0, debug);
		buffer().get(CoreConstants.IPv4_FIELD_SRC, dst, offset, CoreConstants.IPv4_FIELD_SRC_LEN);

		return dst;
	}

	@Override
	public Ip4Address srcGetAsAddress() {
		return new Ip4Address(src());
	}

	public int srcGetAsInt() {
		return Ip4Layout.SRC.getInt(buffer());
	}

	@Meta
	public int totalLength() {
		return Ip4Layout.TOTAL_LENGTH.getInt(buffer());
	}

	@Meta
	public int ttl() {
		return Ip4Layout.TTL.getInt(buffer());
	}

	/**
	 * @see com.slytechs.jnet.runtime.protocol.core.Ip#version()
	 */
	@Override
	@Meta
	@Display("%d")
	public int version() {
		return Ip4Layout.VERSION.getInt(buffer());
	}
}
