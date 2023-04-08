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

import com.slytechs.jnet.protocol.HeaderExtension;
import com.slytechs.jnet.protocol.Packet;
import com.slytechs.jnet.protocol.core.constants.CoreHeaderInfo;
import com.slytechs.jnet.protocol.meta.Meta;
import com.slytechs.jnet.protocol.meta.MetaResource;

/**
 * The Class Tcp.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */

@MetaResource("tcp-meta.json")
public final class Tcp extends HeaderExtension<TcpOption> {

	/** The Constant ID. */
	public static final int ID = CoreHeaderInfo.CORE_ID_TCP;
	
	/** The Constant FLAGS_FORMAT. */
	private static final String FLAGS_FORMAT = "..B WEUA PRSF";

	/**
	 * Instantiates a new tcp.
	 */
	public Tcp() {
		super(ID);
	}

//	@Override
//	public TcpProtocol getProtocol() {
//		return (TcpProtocol) super.getProtocol();
//	}

	/**
 * Ack.
 *
 * @return the long
 */
@Meta
	public long ack() {
		return TcpStruct.ACK.getUnsignedInt(buffer());
	}

	/**
	 * Ack.
	 *
	 * @param ack the ack
	 */
	public void ack(long ack) {
		TcpStruct.ACK.setInt((int) ack, buffer());
	}

	/**
	 * Checksum.
	 *
	 * @return the int
	 */
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

	/**
 * Checksum.
 *
 * @param checksum the checksum
 */
public void checksum(int checksum) {
		TcpStruct.CHECKSUM.setInt(checksum, buffer());
	}

	/**
	 * Dst port.
	 *
	 * @return the int
	 */
	@Meta
	public int dstPort() {
		return TcpStruct.DST_PORT.getUnsignedShort(buffer());
	}

	/**
	 * Dst port.
	 *
	 * @param newPort the new port
	 */
	public void dstPort(int newPort) {
		TcpStruct.DST_PORT.setInt(newPort, buffer());
	}

	/**
	 * Flags.
	 *
	 * @return the int
	 */
	@Meta
	public int flags() {
		return TcpStruct.FLAGS.getUnsignedShort(buffer());
	}

	/**
	 * Flags.
	 *
	 * @param flags the flags
	 */
	public void flags(int flags) {
		TcpStruct.FLAGS.setInt(flags, buffer());
	}

	/**
	 * Hdr len.
	 *
	 * @return the int
	 */
	@Meta
	public int hdrLen() {
		return TcpStruct.HDR_LEN.getUnsignedShort(buffer());
	}

	/**
	 * Hdr len.
	 *
	 * @param hdrLen the hdr len
	 */
	public void hdrLen(int hdrLen) {
		TcpStruct.HDR_LEN.setInt(hdrLen, buffer());
	}

	/**
	 * Hdr len bytes.
	 *
	 * @return the int
	 */
	public int hdrLenBytes() {
		return TcpStruct.HDR_LEN.getUnsignedShort(buffer()) << 2;
	}

	/**
	 * Res.
	 *
	 * @return the int
	 */
	public int res() {
		return TcpStruct.RESERVED.getUnsignedShort(buffer());
	}

	/**
	 * Reserved.
	 *
	 * @param res the res
	 */
	public void reserved(int res) {
		TcpStruct.RESERVED.setInt(res, buffer());
	}

	/**
	 * Seq.
	 *
	 * @return the long
	 */
	@Meta
	public long seq() {
		return TcpStruct.SEQ.getUnsignedInt(buffer());
	}

	/**
	 * Seq.
	 *
	 * @param seq the seq
	 */
	public void seq(long seq) {
		TcpStruct.SEQ.setInt((int) seq, buffer());
	}

	/**
	 * Src port.
	 *
	 * @return the int
	 */
	@Meta
	public int srcPort() {
		return TcpStruct.SRC_PORT.getUnsignedShort(buffer());
	}

	/**
	 * Src port.
	 *
	 * @param newPort the new port
	 */
	public void srcPort(int newPort) {
		TcpStruct.SRC_PORT.setInt(newPort, buffer());
	}

	/**
	 * Urgent pointer.
	 *
	 * @return the int
	 */
	public int urgentPointer() {
		return TcpStruct.URGENT_POINTER.getUnsignedShort(buffer());
	}

	/**
	 * Urgent pointer.
	 *
	 * @param urgentPointer the urgent pointer
	 */
	public void urgentPointer(int urgentPointer) {
		TcpStruct.URGENT_POINTER.setInt(urgentPointer, buffer());
	}

	/**
	 * Window unscaled value.
	 *
	 * @return the int
	 */
	public int windowUnscaledValue() {
		return TcpStruct.WIN_SIZE.getUnsignedShort(buffer());
	}

	/**
	 * Window unscaled value.
	 *
	 * @param size the size
	 */
	public void windowUnscaledValue(int size) {
		TcpStruct.WIN_SIZE.setInt(size, buffer());
	}

	/**
	 * Checks if is reassembled.
	 *
	 * @return true, if is reassembled
	 */
	public boolean isReassembled() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/**
	 * Reaseembled segments.
	 *
	 * @return the packet[]
	 */
	public Packet[] reaseembledSegments() {
		throw new UnsupportedOperationException("not implemented yet");
	}
}
