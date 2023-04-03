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

import com.slytechs.jnet.protocol.constants.CoreHeaderInfo;
import com.slytechs.jnet.protocol.packet.ExtendableHeader;
import com.slytechs.jnet.protocol.packet.Packet;
import com.slytechs.jnet.protocol.packet.meta.Meta;
import com.slytechs.jnet.protocol.packet.meta.MetaResource;

/**
 * 
 * @author Sly Technologies
 * @author repos@slytechs.com
 */

@MetaResource("tcp-meta.json")
public final class Tcp extends ExtendableHeader<TcpOption> {

	public static final int ID = CoreHeaderInfo.CORE_ID_TCP;
	private static final String FLAGS_FORMAT = "..B WEUA PRSF";

	public Tcp() {
		super(ID);
	}

//	@Override
//	public TcpProtocol getProtocol() {
//		return (TcpProtocol) super.getProtocol();
//	}

	@Meta
	public long ack() {
		return TcpStruct.ACK.getUnsignedInt(buffer());
	}

	public void ack(long ack) {
		TcpStruct.ACK.setInt((int) ack, buffer());
	}

	@Meta
	public int checksum() {
		return TcpStruct.CHECKSUM.getUnsignedShort(buffer());
	}

//	public int checksumValidation() {
//		if (!getProtocol().booleanProperty(TcpProperty.TCP_CHECKSUM_ENABLE))
//			return 0;
//
//		int checksum = checksum();
//
////		int len = hdrLenBytes();
//		ByteBuffer b = buffer();
//		int crcOffset = (int) (TcpStruct.CHECKSUM.byteOffset() + position());
//		b.putShort(crcOffset, (short) 0);
////		Ip4Layout.CHECKSUM.setShort((short) 0, b);
//
//		int validated = Checksums.crc16(b);
////		System.out.printf("Tcp::checksumValidation validation=0x%x checksum=0x%x b=%s%n", validated, checksum, ArrayUtils.toString(b.array(), b.arrayOffset(), b.limit()));
//		b.putShort(crcOffset, (short) checksum);
//
//		return validated;
//	}

//	public int checksumStatus() {
//		int validation = checksumValidation();
//		if (validation == 0)
//			return -1;
//
//		boolean valid = (checksum() == validation);
//
//		return valid ? 1 : 0;
//	}

	public void checksum(int checksum) {
		TcpStruct.CHECKSUM.setInt(checksum, buffer());
	}

	@Meta
	public int dstPort() {
		return TcpStruct.DST_PORT.getUnsignedShort(buffer());
	}

	public void dstPort(int newPort) {
		TcpStruct.DST_PORT.setInt(newPort, buffer());
	}

	@Meta
	public int flags() {
		return TcpStruct.FLAGS.getUnsignedShort(buffer());
	}

	public void flags(int flags) {
		TcpStruct.FLAGS.setInt(flags, buffer());
	}

	@Meta
	public int hdrLen() {
		return TcpStruct.HDR_LEN.getUnsignedShort(buffer());
	}

	public void hdrLen(int hdrLen) {
		TcpStruct.HDR_LEN.setInt(hdrLen, buffer());
	}

	public int hdrLenBytes() {
		return TcpStruct.HDR_LEN.getUnsignedShort(buffer()) << 2;
	}

	public int res() {
		return TcpStruct.RESERVED.getUnsignedShort(buffer());
	}

	public void reserved(int res) {
		TcpStruct.RESERVED.setInt(res, buffer());
	}

	@Meta
	public long seq() {
		return TcpStruct.SEQ.getUnsignedInt(buffer());
	}

	public void seq(long seq) {
		TcpStruct.SEQ.setInt((int) seq, buffer());
	}

	@Meta
	public int srcPort() {
		return TcpStruct.SRC_PORT.getUnsignedShort(buffer());
	}

	public void srcPort(int newPort) {
		TcpStruct.SRC_PORT.setInt(newPort, buffer());
	}

	public int urgentPointer() {
		return TcpStruct.URGENT_POINTER.getUnsignedShort(buffer());
	}

	public void urgentPointer(int urgentPointer) {
		TcpStruct.URGENT_POINTER.setInt(urgentPointer, buffer());
	}

	public int windowUnscaledValue() {
		return TcpStruct.WIN_SIZE.getUnsignedShort(buffer());
	}

	public void windowUnscaledValue(int size) {
		TcpStruct.WIN_SIZE.setInt(size, buffer());
	}

	/**
	 * @return
	 */
	public boolean isReassembled() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/**
	 * @return
	 */
	public Packet[] reaseembledSegments() {
		throw new UnsupportedOperationException("not implemented yet");
	}
}
