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

import com.slytechs.protocol.HeaderExtension;
import com.slytechs.protocol.Packet;
import com.slytechs.protocol.meta.Meta;
import com.slytechs.protocol.meta.MetaResource;
import com.slytechs.protocol.pack.core.TcpOption.TcpWindowScaleOption;
import com.slytechs.protocol.pack.core.constants.CoreIdTable;

/**
 * Transmission Control Protocol (TCP).
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
@MetaResource("tcp-meta.json")
public final class Tcp extends HeaderExtension<TcpOption> {

	/** The Constant ID. */
	public static final int ID = CoreIdTable.CORE_ID_TCP;

	/** The Constant FLAGS_FORMAT. */
	@SuppressWarnings("unused")
	private static final String FLAGS_FORMAT = "..B WEUA PRSF";

	/**
	 * The wscale option. We persist the object, once its lazily created in case it
	 * will be needed again during the next header binding. The option is unbound
	 * when the main TCP header is unbound.
	 */
	private TcpWindowScaleOption wscaleOption;

	/**
	 * Instantiates a new tcp.
	 */
	public Tcp() {
		super(ID);
	}

	/**
	 * Acknowledgment number (32 bits). If the ACK flag is set then the value of
	 * this field is the next sequence number that the sender of the ACK is
	 * expecting. This acknowledges receipt of all prior bytes (if any). The first
	 * ACK sent by each end acknowledges the other end's initial sequence number
	 * itself, but no data.
	 *
	 * @return the long
	 */
	@Meta
	public long ack() {
		return TcpStruct.ACK.getUnsignedInt(buffer());
	}

	/**
	 * Set a new ack number.
	 *
	 * @param ack the ack
	 */
	public void ack(long ack) {
		TcpStruct.ACK.setInt((int) ack, buffer());
	}

	/**
	 * Checksum (16 bits). The 16-bit checksum field is used for error-checking of
	 * the TCP header, the payload and an IP pseudo-header. The pseudo-header
	 * consists of the source IP address, the destination IP address, the protocol
	 * number for the TCP protocol (6) and the length of the TCP headers and payload
	 * (in bytes).
	 * 
	 * @return the int
	 */
	@Meta
	public int checksum() {
		return TcpStruct.CHECKSUM.getUnsignedShort(buffer());
	}

	/**
	 * Set a new checksum value.
	 *
	 * @param checksum the checksum
	 */
	public void checksum(int checksum) {
		TcpStruct.CHECKSUM.setInt(checksum, buffer());
	}

	/**
	 * Data offset in 32-bit words (4 bits). Specifies the size of the TCP header in
	 * 32-bit words. The minimum size header is 5 words and the maximum is 15 words
	 * thus giving the minimum size of 20 bytes and maximum of 60 bytes, allowing
	 * for up to 40 bytes of options in the header. This field gets its name from
	 * the fact that it is also the offset from the start of the TCP segment to the
	 * actual data.
	 *
	 * 
	 * @return the int
	 */
	@Meta
	public int dataOffset() {
		return TcpStruct.HDR_LEN.getUnsignedShort(buffer());
	}

	/**
	 * Set a new data offset.
	 *
	 * @param newOffset the hdr len
	 */
	public void dataOffset(int newOffset) {
		TcpStruct.HDR_LEN.setInt(newOffset, buffer());
	}

	/**
	 * Destination port (16 bits). Identifies the receiving port.
	 *
	 * @return the int
	 */
	@Meta
	public int dstPort() {
		return TcpStruct.DST_PORT.getUnsignedShort(buffer());
	}

	/**
	 * Set a new destination port.
	 *
	 * @param newPort the new port
	 */
	public void dstPort(int newPort) {
		TcpStruct.DST_PORT.setInt(newPort, buffer());
	}

	/**
	 * Flags (8 bits).
	 * <p>
	 * Contains 8 1-bit flags (control bits) as follows:
	 * </p>
	 * <ul>
	 * <li>CWR (1 bit): Congestion window reduced (CWR) flag is set by the sending
	 * host to indicate that it received a TCP segment with the ECE flag set and had
	 * responded in congestion control mechanism.</li>
	 * <li>ECE (1 bit): ECN-Echo has a dual role, depending on the value of the SYN
	 * flag. It indicates:
	 * <ul>
	 * <li>If the SYN flag is set (1), that the TCP peer is ECN capable.</li>
	 * <li>If the SYN flag is clear (0), that a packet with Congestion Experienced
	 * flag set (ECN=11) in the IP header was received during normal
	 * transmission.[a] This serves as an indication of network congestion (or
	 * impending congestion) to the TCP sender.</li>
	 * </ul>
	 * </li>
	 * <li>URG (1 bit): Indicates that the Urgent pointer field is significant</li>
	 * <li>ACK (1 bit): Indicates that the Acknowledgment field is significant. All
	 * packets after the initial SYN packet sent by the client should have this flag
	 * set.</li>
	 * <li>PSH (1 bit): Push function. Asks to push the buffered data to the
	 * receiving application.</li>
	 * <li>RST (1 bit): Reset the connection</li>
	 * <li>SYN (1 bit): Synchronize sequence numbers. Only the first packet sent
	 * from each end should have this flag set. Some other flags and fields change
	 * meaning based on this flag, and some are only valid when it is set, and
	 * others when it is clear.</li>
	 * <li>FIN (1 bit): Last packet from sender</li>
	 * </ul>
	 *
	 * @return the int
	 */
	@Meta
	public int flags() {
		return TcpStruct.FLAGS.getUnsignedShort(buffer());
	}

	/**
	 * Sets new flags.
	 *
	 * @param flags the flags
	 */
	public void flags(int flags) {
		TcpStruct.FLAGS.setInt(flags, buffer());
	}

	/**
	 * Data offset in bytes (4 bits).
	 *
	 * @return the int
	 */
	public int hdrLenBytes() {
		return TcpStruct.HDR_LEN.getUnsignedShort(buffer()) << 2;
	}

	/**
	 * Checks if is this TCP frame is part of a reassembled TCP stream.
	 *
	 * @return true, if is reassembled
	 */
	public boolean isReassembled() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/**
	 * On unbind.
	 *
	 * @see com.slytechs.protocol.HeaderExtension#onUnbind()
	 */
	@Override
	protected void onUnbind() {
		super.onUnbind();

		if (wscaleOption != null)
			wscaleOption.unbind();
	}

	/**
	 * Reaseembled segments.
	 *
	 * @return the packet[]
	 */
	public Packet[] reaseembledSegments() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/**
	 * Reserved (4 bits). For future use and should be set to zero. The last bit was
	 * previously defined by Historic RFC 3540 as NS flag (Nonce Sum).
	 *
	 * 
	 * @return the int
	 */
	public int reserved() {
		return TcpStruct.RESERVED.getUnsignedShort(buffer());
	}

	/**
	 * Set a new value in reserved field.
	 *
	 * @param res the res
	 */
	public void reserved(int res) {
		TcpStruct.RESERVED.setInt(res, buffer());
	}

	/**
	 * Sequence number (32 bits).
	 * <p>
	 * Has a dual role:
	 * </p>
	 * <ul>
	 * <li>If the SYN flag is set (1), then this is the initial sequence number. The
	 * sequence number of the actual first data byte and the acknowledged number in
	 * the corresponding ACK are then this sequence number plus 1.</li>
	 * <li>If the SYN flag is clear (0), then this is the accumulated sequence
	 * number of the first data byte of this segment for the current session.</li>
	 * </ul>
	 *
	 * @return the long
	 */
	@Meta
	public long seq() {
		return TcpStruct.SEQ.getUnsignedInt(buffer());
	}

	/**
	 * Sets a new sequence number.
	 *
	 * @param seq the seq
	 */
	public void seq(long seq) {
		TcpStruct.SEQ.setInt((int) seq, buffer());
	}

	/**
	 * Source port (16 bits). Identifies the sending port.
	 * 
	 * @return the int
	 */
	@Meta
	public int srcPort() {
		return TcpStruct.SRC_PORT.getUnsignedShort(buffer());
	}

	/**
	 * Sets a new source prot.
	 *
	 * @param newPort the new port
	 */
	public void srcPort(int newPort) {
		TcpStruct.SRC_PORT.setInt(newPort, buffer());
	}

	/**
	 * Urgent pointer (16 bits). If the URG flag is set, then this 16-bit field is
	 * an offset from the sequence number indicating the last urgent data byte.
	 *
	 * @return the int
	 */
	public int urgentPointer() {
		return TcpStruct.URGENT_POINTER.getUnsignedShort(buffer());
	}

	/**
	 * Sets a new urgent pointer.
	 *
	 * @param urgentPointer the urgent pointer
	 */
	public void urgentPointer(int urgentPointer) {
		TcpStruct.URGENT_POINTER.setInt(urgentPointer, buffer());
	}

	/**
	 * Window size in "receiver window" units (16 bits). The size of the receive
	 * window, which specifies the number of window size units that the sender of
	 * this segment is currently willing to receive.
	 *
	 * @return the int
	 */
	public int windowSize() {
		return TcpStruct.WIN_SIZE.getUnsignedShort(buffer());
	}

	/**
	 * Sets a new window size (16 bits).
	 *
	 * @param size the size
	 */
	public void windowSize(int size) {
		TcpStruct.WIN_SIZE.setInt(size, buffer());
	}

	/**
	 * A scaled window size in units of bytes. If the TCP header has the
	 * window-scale option the shift count of the option will be used. If no TCP
	 * window-scale option, then a shift 0 will be used.
	 *
	 * @return the scaled window value
	 */
	public int windowSizeScaled() {

		if (wscaleOption == null)
			wscaleOption = new TcpWindowScaleOption();

		int shiftCount = 0;
		if (hasExtension(wscaleOption))
			shiftCount = wscaleOption.shiftCount();

		return windowSizeScaled(shiftCount);
	}

	/**
	 * A scaled window size in units of bytes.
	 *
	 * @param scale the bitshift scale to use
	 * @return the scaled window value
	 */
	public int windowSizeScaled(int scale) {
		return windowSize() << scale;
	}
}
