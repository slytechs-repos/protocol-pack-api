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
package com.slytechs.protocol.descriptor;

import java.nio.ByteBuffer;

import com.slytechs.protocol.pack.core.constants.CoreConstants;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 *
 */
public class Type1DissectorJavaImpl extends PacketDissectorJava {

	/**
	 * @see com.slytechs.protocol.descriptor.PacketDissectorJava#writeDescriptor(java.nio.ByteBuffer)
	 */
	@Override
	public int writeDescriptor(ByteBuffer desc) {
		int len = writeDescriptorUsingLayout(desc);

		desc.position(desc.position() + len);

		return len;
	}

	/**
	 * Write descriptor using layout.
	 *
	 * @param desc the desc
	 * @return the int
	 */
	private int writeDescriptorUsingLayout(ByteBuffer desc) {

		Type1DescriptorLayout.TIMESTAMP.setLong(super.timestamp, desc);

		Type1DescriptorLayout.CAPLEN.setInt(super.captureLength, desc);
		Type1DescriptorLayout.L2_FRAME_TYPE.setInt(super.l2Type, desc);
		Type1DescriptorLayout.L3_OFFSET.setInt(super.l3Offset, desc);
		Type1DescriptorLayout.L3_SIZE.setInt(super.l3Size, desc);

		Type1DescriptorLayout.WIRELEN.setInt(super.wireLength, desc);
		Type1DescriptorLayout.VLAN_COUNT.setInt(super.vlanCount, desc);
		Type1DescriptorLayout.MPLS_COUNT.setInt(super.mplsCount, desc);
		Type1DescriptorLayout.L3_FRAME_TYPE.setInt(super.l3Type, desc);
		Type1DescriptorLayout.L4_FRAME_TYPE.setInt(super.l4Type, desc);
		Type1DescriptorLayout.L4_SIZE.setInt(super.l4Size, desc);

		return CoreConstants.DESC_TYPE1_BYTE_SIZE;
	}

	public Type1DissectorJavaImpl() {
	}

	/**
	 * @see com.slytechs.protocol.descriptor.PacketDissectorJava#destroyDissector()
	 */
	@Override
	protected void destroyDissector() {
	}

	/**
	 * @see com.slytechs.protocol.descriptor.PacketDissector#reset()
	 */
	@Override
	public void reset() {
	}

	/**
	 * @see com.slytechs.protocol.descriptor.PacketDissectorJava#dissectExtensionType(java.nio.ByteBuffer,
	 *      int, int, int)
	 */
	@Override
	protected void dissectExtensionType(ByteBuffer buf, int offset, int id, int nextHeader) {
	}

	/**
	 * @see com.slytechs.protocol.descriptor.PacketDissectorJava#dissectIp4Options(int,
	 *      int, int)
	 */
	@Override
	protected void dissectIp4Options(int offset, int hlen, int nextHeader) {
	}

	/**
	 * @see com.slytechs.protocol.descriptor.PacketDissectorJava#dissectIp6Options(int,
	 *      int)
	 */
	@Override
	protected void dissectIp6Options(int offset, int nextHeader) {
	}

	/**
	 * @see com.slytechs.protocol.descriptor.PacketDissectorJava#addRecord(int, int,
	 *      int)
	 */
	@Override
	protected boolean addRecord(int id, int offset, int length) {
		return true;
	}

	/**
	 * @see com.slytechs.protocol.descriptor.PacketDissectorJava#dissectUdp(int)
	 */
	@Override
	protected void dissectUdp(int offset) {
	}

	/**
	 * @see com.slytechs.protocol.descriptor.PacketDissectorJava#dissectGre(int)
	 */
	@Override
	protected void dissectGre(int offset) {
	}

	/**
	 * @see com.slytechs.protocol.descriptor.PacketDissectorJava#dissectIcmp6(int)
	 */
	@Override
	protected void dissectIcmp6(int offset) {
	}

	/**
	 * @see com.slytechs.protocol.descriptor.PacketDissectorJava#dissectIcmp4(int)
	 */
	@Override
	protected void dissectIcmp4(int offset) {
	}

	/**
	 * @see com.slytechs.protocol.descriptor.PacketDissectorJava#dissectSctp(int)
	 */
	@Override
	protected void dissectSctp(int offset) {
	}

	/**
	 * @see com.slytechs.protocol.descriptor.PacketDissectorJava#dissectExtensionPorts(java.nio.ByteBuffer,
	 *      int, int, int, int)
	 */
	@Override
	protected void dissectExtensionPorts(ByteBuffer buf, int offset, int id, int src, int dst) {
	}

	/**
	 * @see com.slytechs.protocol.descriptor.PacketDissectorJava#dissectTcpOptions(int,
	 *      int)
	 */
	@Override
	protected void dissectTcpOptions(int offset, int tcpHeaderLenth) {
	}

}
