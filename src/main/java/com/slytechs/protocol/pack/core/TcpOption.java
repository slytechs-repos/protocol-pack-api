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

import static com.slytechs.protocol.pack.core.constants.CoreConstants.*;

import java.nio.ByteBuffer;

import com.slytechs.protocol.Header;
import com.slytechs.protocol.meta.Meta;
import com.slytechs.protocol.meta.Meta.MetaType;
import com.slytechs.protocol.meta.MetaResource;
import com.slytechs.protocol.pack.core.constants.CoreConstants;
import com.slytechs.protocol.pack.core.constants.TcpOptionId;

/**
 * Base header class for all TCP options.
 * <p>
 * TCP options are optional fields in the TCP header that can be used to control
 * the behavior of the TCP protocol. There are a number of different TCP
 * options, but some of the most common ones include:
 * <p>
 * <ul>
 * <li>Maximum Segment Size (MSS): The MSS option is used to negotiate the
 * maximum size of a TCP segment that can be sent over the connection. This is
 * important because it helps to prevent data from being fragmented, which can
 * lead to performance problems.</li>
 * <li>Window Scale Option: The Window Scale option is used to increase the
 * window size of a TCP connection beyond the default 65,535 bytes. This can be
 * beneficial for connections with high bandwidth and low latency.</li>
 * <li>Selective Acknowledgments (SACK) Option: The SACK option allows the
 * receiver to acknowledge individual segments of data, rather than just the
 * entire window. This can help to improve the performance of TCP connections by
 * reducing the number of retransmissions that are needed.</li>
 * <li>Timestamp Option: The Timestamp option allows the sender and receiver to
 * keep track of the time that data is sent and received. This can be used to
 * help diagnose performance problems and to detect lost or corrupted data.</li>
 * </ul>
 * <p>
 * TCP options are a valuable tool for improving the performance and reliability
 * of TCP connections. They can be used to control the behavior of the TCP
 * protocol and to adapt the protocol to the specific needs of the network.
 * </p>
 * <p>
 * Here are some additional details about TCP options:
 * </p>
 * <ul>
 * <li>TCP options are optional fields in the TCP header.</li>
 * <li>TCP options are used to control the behavior of the TCP protocol.</li>
 * <li>There are a number of different TCP options, but some of the most common
 * ones include MSS, Window Scale, SACK, and Timestamp.</li>
 * <li>TCP options can be used to improve the performance and reliability of TCP
 * connections.</li>
 * </ul>
 */
@MetaResource("tcp-opt-meta.json")
public class TcpOption extends Header {

	/**
	 * The TCP option End of List (EOL) is a special option that marks the end of
	 * the list of TCP options in a TCP segment. The EOL option is always the last
	 * option in the list, and it has a kind field value of 0.
	 */
	@Meta
	public static class TcpEndOfListOption extends TcpOption {

		/** The Constant ID. */
		public static final int ID = TcpOptionId.TCP_OPT_ID_EOL;

		/**
		 * Instantiates a new tcp end of list option.
		 */
		public TcpEndOfListOption() {
			super(ID, CoreConstants.TCP_OPTION_KIND_EOL, CoreConstants.TCP_OPTION_LEN_EOL);
		}

		/**
		 * The EOL option length field (1 byte).
		 *
		 * @return the option length field in units of bytes
		 * @see com.slytechs.protocol.pack.core.TcpOption#length()
		 */
		@Override
		public int length() {
			return 1;
		}
	}

	/**
	 * The TCP option Fast Open (TFO) is an optional mechanism within TCP that
	 * allows endpoints that have established a full TCP connection in the past
	 * eliminate a round-trip of the handshake and send data right away.
	 * <p>
	 * <p>
	 * TFO works by using a TFO cookie (a TCP option), which is a cryptographic
	 * cookie stored on the client and set upon the initial connection with the
	 * server. When the client later reconnects, it sends the initial SYN packet
	 * along with the TFO cookie data to authenticate itself.
	 * </p>
	 * <p>
	 * If the server accepts the TFO cookie, it will send a SYN-ACK packet with the
	 * ACK flag set, allowing the client to send data immediately. This can
	 * significantly improve the performance of TCP connections by reducing the time
	 * it takes to establish the connection.
	 * </p>
	 * <p>
	 * TFO is supported by most modern operating systems and web browsers. To use
	 * TFO, both the client and server must support it.
	 * </p>
	 * <p>
	 * Here are some of the benefits of using TFO:
	 * </p>
	 * <ul>
	 * <li>Reduced connection establishment time: TFO can reduce the time it takes
	 * to establish a TCP connection by eliminating the need for the three-way
	 * handshake.</li>
	 * <li>Increased throughput: TFO can increase the throughput of TCP connections
	 * by allowing the client to send data immediately after the connection is
	 * established.</li>
	 * <li>Improved reliability: TFO can improve the reliability of TCP connections
	 * by reducing the number of retransmissions that are needed.</li>
	 * </ul>
	 * <p>
	 * TFO is a valuable tool for improving the performance and reliability of TCP
	 * connections. It can be used to improve the performance of web browsing, file
	 * transfers, and other applications that use TCP.
	 * </p>
	 * 
	 */
	@MetaResource("tcp-opt-fast-open-meta.json")
	public static class TcpFastOpenOption extends TcpOption {

		/** The Constant ID. */
		public static final int ID = TcpOptionId.TCP_OPT_ID_FASTOPEN;

		/**
		 * Instantiates a new tcp fast open option.
		 */
		public TcpFastOpenOption() {
			super(ID, CoreConstants.TCP_OPTION_KIND_FASTOPEN, CoreConstants.TCP_OPTION_LEN_FASTOPEN);
		}

		/**
		 * Gets the TCP TFO option cookie field value.
		 * <p>
		 * The TCP option fast open cookie field is a 16-byte field that is used to
		 * authenticate the client's (source) IP address of the SYN packet. The IP
		 * address may be an IPv4 or IPv6 address. The cookie is generated by the server
		 * and encrypted using the client's IP address. The cookie is sent to the client
		 * in the SYN-ACK packet. The client then decrypts the cookie using its own IP
		 * address and compares it to the cookie that it generated. If the cookies
		 * match, then the client knows that it is talking to the correct server.
		 * </p>
		 * <p>
		 * The fast open cookie field is used to improve the performance of TCP
		 * connections by eliminating the need for the three-way handshake. With the
		 * three-way handshake, the client and server exchange three packets before data
		 * can be sent. With fast open, the client can send data in the first packet
		 * after the SYN-ACK packet.
		 * </p>
		 * <p>
		 * The fast open cookie field is supported by most modern TCP implementations.
		 * It is enabled by default in most operating systems and web browsers.
		 * </p>
		 * <p>
		 * Here are some of the benefits of using the fast open cookie field:
		 * </p>
		 * <ul>
		 * <li>Improved performance: The fast open cookie field can improve the
		 * performance of TCP connections by eliminating the need for the three-way
		 * handshake.</li>
		 * <li>Reduced latency: The fast open cookie field can reduce the latency of TCP
		 * connections by allowing the client to send data in the first packet after the
		 * SYN-ACK packet.</li>
		 * <li>Improved security: The fast open cookie field can improve the security of
		 * TCP connections by providing an additional layer of authentication.</li>
		 * </ul>
		 * <p>
		 * The fast open cookie field is a valuable tool for improving the performance,
		 * reliability, and security of TCP connections. It can be used to improve the
		 * performance of web browsing, file transfers, and other applications that use
		 * TCP.
		 * </p>
		 *
		 * @param dst    the destination array where to store the cookie's value
		 * @param offset the offset into the dst array
		 * @return the supplied array reference
		 */
		@Meta
		public byte[] cookie(byte[] dst, int offset) {
			buffer().get(2, dst, offset, 16);

			return dst;
		}

	}

	/**
	 * The TCP MSS option is a TCP option that specifies the maximum segment size
	 * (MSS) that can be sent over the connection. The MSS is the largest amount of
	 * data, specified in bytes, that a computer or communications device can
	 * receive in a single TCP segment. MSS does not include the TCP header or the
	 * IP header.
	 * <p>
	 * The MSS option is used to prevent data from being fragmented, which can lead
	 * to performance problems. When a TCP segment is fragmented, it is broken up
	 * into smaller pieces that must be reassembled by the receiver. This can add
	 * overhead and delay to the transmission of data.
	 * </p>
	 * <p>
	 * The MSS option is negotiated during the TCP handshake. Both the sender and
	 * receiver send their MSS values in the SYN packet. The MSS value that is used
	 * for the connection is the smallest of the two values.
	 * </p>
	 * <p>
	 * The default MSS value is 536 bytes. However, this value can be changed if the
	 * network or the application requires a larger or smaller MSS. For example, if
	 * the network has a small MTU, the MSS must be reduced to avoid fragmentation.
	 * </p>
	 * <p>
	 * The MSS option is an important part of the TCP protocol. It helps to ensure
	 * that data is transmitted efficiently and reliably.
	 * </p>
	 * <p>
	 * Here are some additional details about the TCP MSS option:
	 * </p>
	 * <ul>
	 * <li>The TCP MSS option is a 4-byte option.</li>
	 * <li>The TCP MSS option is used to specify the maximum segment size (MSS) that
	 * can be sent over the connection.</li>
	 * <li>The MSS is the largest amount of data, specified in bytes, that a
	 * computer or communications device can receive in a single TCP segment.</li>
	 * <li>MSS does not include the TCP header or the IP header.</li>
	 * <li>The MSS option is negotiated during the TCP handshake.</li>
	 * <li>The MSS value that is used for the connection is the smallest of the two
	 * values.</li>
	 * <li>The default MSS value is 536 bytes.</li>
	 * <li>The MSS value can be changed if the network or the application requires a
	 * larger or smaller MSS.</li>
	 * </ul>
	 */
	@MetaResource("tcp-opt-mss-meta.json")
	public static class TcpMSSOption extends TcpOption {

		/** The Constant ID. */
		public static final int ID = TcpOptionId.TCP_OPT_ID_MSS;

		/**
		 * Instantiates a new tcp MSS option.
		 */
		public TcpMSSOption() {
			super(ID, CoreConstants.TCP_OPTION_KIND_MSS, CoreConstants.TCP_OPTION_LEN_MSS);
		}

		/**
		 * Gets the TCP MSS mss field value.
		 * <p>
		 * The MSS option is a 16-bit option. The first 8 bits of the option contain the
		 * MSS value for the sending host. The second 8 bits of the option contain the
		 * MSS value for the receiving host.
		 * </p>
		 * <p>
		 * If the MSS options are not negotiated, the default MSS value is 536 bytes.
		 * However, the MSS value can be negotiated to a larger value if the network
		 * supports it.
		 * </p>
		 * <p>
		 * The MSS option is important for two reasons. First, it helps to prevent
		 * fragmentation. Fragmentation occurs when a TCP segment is too large to be
		 * sent in a single IP packet. When fragmentation occurs, the IP layer breaks
		 * the TCP segment into multiple IP packets. This can cause problems, such as
		 * lost data and increased latency.
		 * </p>
		 * <p>
		 * Second, the MSS option helps to improve performance. By limiting the size of
		 * TCP segments, the MSS option helps to reduce the number of IP packets that
		 * need to be sent. This can improve performance by reducing the amount of time
		 * it takes to send data over the network.
		 * </p>
		 * <p>
		 * The MSS option is supported by most TCP implementations. It is enabled by
		 * default in most operating systems and web browsers.
		 * </p>
		 * <p>
		 * Here are some of the benefits of using the TCP MSS option:
		 * </p>
		 * <ul>
		 * <li>Reduced fragmentation: The TCP MSS option can help to reduce
		 * fragmentation by limiting the size of TCP segments.</li>
		 * <li>Improved performance: The TCP MSS option can improve performance by
		 * reducing the number of IP packets that need to be sent.</li>
		 * <li>Improved reliability: The TCP MSS option can improve reliability by
		 * reducing the chance of data loss.</li>
		 * </ul>
		 * <p>
		 * The TCP MSS option is a valuable tool for improving the performance,
		 * reliability, and security of TCP connections. It can be used to improve the
		 * performance of web browsing, file transfers, and other applications that use
		 * TCP.
		 * </p>
		 * 
		 * @return the unsigned 16-bit field value
		 */
		@Meta
		public int mss() {
			return Short.toUnsignedInt(buffer().getShort(2));
		}

	}

	/**
	 * 
	 * The TCP option nop is a 1-byte option that does nothing. It is used to pad
	 * the options field in the TCP header to an even 32-bit boundary. This is
	 * sometimes done to improve the performance of TCP connections.
	 */
	@MetaResource("tcp-opt-nop-meta.json")
	public static class TcpNoOption extends TcpOption {

		/** The Constant ID. */
		public static final int ID = TcpOptionId.TCP_OPT_ID_NOP;

		/**
		 * Instantiates a new tcp no option.
		 */
		public TcpNoOption() {
			super(ID, TCP_OPTION_KIND_NOP, TCP_OPTION_LEN_NOP);
		}

		/**
		 * The NOP option length field (1 byte).
		 *
		 * @return the option length field in units of bytes
		 * @see com.slytechs.protocol.pack.core.TcpOption#length()
		 */
		@Override
		public int length() {
			return 1;
		}
	}

	/**
	 * The TCP Selective Acknowledgment (SACK) option is a TCP option that allows
	 * the receiver to acknowledge non-consecutive data, so that the sender can
	 * retransmit only what is missing at the receiver's end.
	 * <p>
	 * SACK works by the receiver including a list of ranges of data that it has
	 * received in its acknowledgment packets. The sender then uses this list to
	 * determine which segments it needs to retransmit.
	 * </p>
	 * <p>
	 * SACK is a more efficient way to handle lost data than the traditional
	 * cumulative acknowledgment scheme used by TCP. With cumulative
	 * acknowledgments, the sender only knows which segments have been received if
	 * they are at the beginning of the receive window. With SACK, the sender can
	 * know which segments have been received even if they are not at the beginning
	 * of the receive window.
	 * </p>
	 * <p>
	 * SACK is supported by most modern TCP implementations. It is enabled by
	 * default in most operating systems and web browsers.
	 * </p>
	 * <p>
	 * Here are some of the benefits of using SACK:
	 * </p>
	 * <ul>
	 * <li>Reduced retransmissions: SACK can reduce the number of retransmissions
	 * needed by the sender. This is because the sender only needs to retransmit the
	 * segments that have not been received, rather than the entire window.</li>
	 * <li>Increased throughput: SACK can increase the throughput of TCP connections
	 * by allowing the sender to send more data before it needs to wait for an
	 * acknowledgment.</li>
	 * <li>Improved reliability: SACK can improve the reliability of TCP connections
	 * by reducing the number of lost packets.</li>
	 * </ul>
	 * <p>
	 * SACK is a valuable tool for improving the performance and reliability of TCP
	 * connections. It can be used to improve the performance of web browsing, file
	 * transfers, and other applications that use TCP.
	 * </p>
	 */
	@MetaResource("tcp-opt-sack-meta.json")
	public static class TcpSackOption extends TcpOption {

		/** The Constant ID. */
		public static final int ID = TcpOptionId.TCP_OPT_ID_SACK;

		/**
		 * Instantiates a new tcp selective ack option.
		 */
		public TcpSackOption() {
			super(ID, CoreConstants.TCP_OPTION_KIND_SACK);
		}

		/**
		 * Gets the TCP SACK block field value.
		 * <p>
		 * The TCP SACK block field is a field in the TCP Selective Acknowledgment
		 * (SACK) option that is used to identify a range of data that has been received
		 * by the receiver. The SACK block field is 8 bytes long and consists of two
		 * 32-bit unsigned integers:
		 * </p>
		 * <ul>
		 * <li><b>Left edge of block:</b> This is the first sequence number of the range
		 * of data that has been received.</li>
		 * <li><b>Right edge of block:</b> This is the sequence number immediately
		 * following the last sequence number of the range of data that has been
		 * received.</li>
		 * </ul>
		 * <p>
		 * The SACK block field is used by the sender to determine which segments it
		 * needs to retransmit. If the sender receives a SACK option from the receiver
		 * that includes a SACK block, then the sender knows that the receiver has
		 * received all of the data in the range of sequence numbers specified by the
		 * SACK block. The sender can then discard any segments that are in the range of
		 * sequence numbers specified by the SACK block. The SACK block field is
		 * supported by most modern TCP implementations. It is enabled by default in
		 * most operating systems and web browsers.
		 * </p>
		 * <p>
		 * Here are some of the benefits of using the SACK block field:
		 * </p>
		 * <ul>
		 * <li>Reduced retransmissions: The SACK block field can reduce the number of
		 * retransmissions needed by the sender. This is because the sender only needs
		 * to retransmit the segments that have not been received, rather than the
		 * entire window.</li>
		 * <li>Increased throughput: The SACK block field can increase the throughput of
		 * TCP connections by allowing the sender to send more data before it needs to
		 * wait for an acknowledgment.</li>
		 * <li>Improved reliability: The SACK block field can improve the reliability of
		 * TCP connections by reducing the number of lost packets.</li>
		 * </ul>
		 * <p>
		 * The SACK block field is a valuable tool for improving the performance and
		 * reliability of TCP connections. It can be used to improve the performance of
		 * web browsing, file transfers, and other applications that use TCP.
		 * </p>
		 *
		 * @param index the index
		 * @return the int
		 */
		public int blockAt(int index) {
			return buffer().getInt(2 + (index << 3));
		}

		/**
		 * Count.
		 *
		 * @return the int
		 */
		public int blockCount() {
			return super.length() >> 3;
		}

		/**
		 * Copies all of the TCP SACK block values into a newly allocated array.
		 *
		 * @return the array containing all of this options block values
		 */
		public long[] blockToArray() {
			long[] array = new long[blockCount()];

			return blockToArray(array, 0);
		}

		/**
		 * Copies all of the TCP SACK block values into the supplied array.
		 *
		 * @param dst destination array where to copy all of the SACK block values
		 * @return the supplied array containing all of this options block values
		 */
		public long[] blockToArray(long[] dst) {
			return blockToArray(dst, 0);
		}

		/**
		 * Copies all of the TCP SACK block values into the supplied array.
		 *
		 * @param dst    destination array where to copy all of the SACK block values
		 * @param offset the offset into the array of the first index into the array
		 *               where to start the copy
		 * @return the supplied array containing all of this options block values
		 */
		public long[] blockToArray(long[] array, int offset) {
			ByteBuffer buffer = buffer();

			for (int i = 0; i < array.length; i++)
				array[i] = buffer.getLong(2 + (i << 3));

			return array;
		}

	}

	/**
	 * The TCP SACK-permitted option is a TCP option that indicates that the sender
	 * is willing to receive SACK options from the receiver. This option is sent by
	 * the sender in the SYN packet during the TCP connection setup.
	 * <p>
	 * The SACK-permitted option is a 1-byte option. The first bit of the option
	 * indicates whether or not the sender is willing to receive SACK options. If
	 * the bit is set to 1, then the sender is willing to receive SACK options. If
	 * the bit is set to 0, then the sender is not willing to receive SACK options.
	 * </p>
	 * <p>
	 * The SACK-permitted option is an important part of the TCP Selective
	 * Acknowledgment (SACK) mechanism. SACK is a mechanism that allows the receiver
	 * to acknowledge non-consecutive data, so that the sender can retransmit only
	 * what is missing at the receiver's end.
	 * </p>
	 * <p>
	 * If the sender is willing to receive SACK options, then the receiver can use
	 * SACK to improve the reliability of the TCP connection. This is because SACK
	 * allows the receiver to tell the sender which segments have been received,
	 * even if they are not at the beginning of the receive window.
	 * </p>
	 * <p>
	 * The SACK-permitted option is supported by most modern TCP implementations. It
	 * is enabled by default in most operating systems and web browsers.
	 * </p>
	 * <p>
	 * Here are some of the benefits of using SACK:
	 * </p>
	 * <ul>
	 * <li>Reduced retransmissions: SACK can reduce the number of retransmissions
	 * needed by the sender. This is because the sender only needs to retransmit the
	 * segments that have not been received, rather than the entire window.</li>
	 * <li>Increased throughput: SACK can increase the throughput of TCP connections
	 * by allowing the sender to send more data before it needs to wait for an
	 * acknowledgment.</li>
	 * <li>Improved reliability: SACK can improve the reliability of TCP connections
	 * by reducing the number of lost packets.</li>
	 * </ul>
	 * <p>
	 * SACK is a valuable tool for improving the performance and reliability of TCP
	 * connections. It can be used to improve the performance of web browsing, file
	 * transfers, and other applications that use TCP.
	 * </p>
	 */
	@MetaResource("tcp-opt-sack-perm-meta.json")
	public static class TcpSackPermOption extends TcpOption {
		/** The Constant ID. */
		public static final int ID = TcpOptionId.TCP_OPT_ID_SACK_PERMITTED;

		/**
		 * Instantiates a new tcp selective ack option.
		 */
		public TcpSackPermOption() {
			super(ID, CoreConstants.TCP_OPTION_KIND_SACK_PERMITTED);
		}
	}

	/**
	 * The TCP timestamp option is a TCP option that allows each end of a TCP
	 * connection to keep track of the round-trip time (RTT) of packets sent over
	 * the connection. The RTT is the time it takes for a packet to travel from one
	 * end of the connection to the other and back again.
	 * <p>
	 * The TCP timestamp option is a 16-byte option. The first 8 bytes of the option
	 * contain the timestamp value for the sending end of the connection. The second
	 * 8 bytes of the option contain the timestamp value for the receiving end of
	 * the connection.
	 * </p>
	 * <p>
	 * The TCP timestamp option is used by both the sender and receiver to calculate
	 * the RTT of packets sent over the connection. The RTT is used by the TCP
	 * congestion control algorithm to determine how quickly data can be sent over
	 * the connection.
	 * </p>
	 * <p>
	 * The TCP timestamp option is supported by most modern TCP implementations. It
	 * is enabled by default in most operating systems and web browsers.
	 * </p>
	 * <p>
	 * Here are some of the benefits of using the TCP timestamp option:
	 * </p>
	 * <ul>
	 * <li>Improved congestion control: The TCP timestamp option can help the TCP
	 * congestion control algorithm to more accurately determine the RTT of packets
	 * sent over the connection. This can lead to improved performance and
	 * reliability of TCP connections.</li>
	 * <li>Improved debugging: The TCP timestamp option can be used to help debug
	 * TCP connections. By looking at the RTT values, it can be determined where
	 * bottlenecks are in the network.</li>
	 * </ul>
	 * <p>
	 * The TCP timestamp option is a valuable tool for improving the performance,
	 * reliability, and debugging of TCP connections. It can be used to improve the
	 * performance of web browsing, file transfers, and other applications that use
	 * TCP.
	 * </p>
	 */
	@MetaResource("tcp-opt-tstamp-meta.json")
	public static class TcpTimestampOption extends TcpOption {

		/** The Constant ID. */
		public static final int ID = TcpOptionId.TCP_OPT_ID_TIMESTAMP;

		/**
		 * Instantiates a new tcp timestamp option.
		 */
		public TcpTimestampOption() {
			super(ID, TCP_OPTION_KIND_TIMESTAMP, TCP_OPTION_LEN_TIMESTAMP);
		}

		/**
		 * The TCP timestamp option timestamp field is a 32-bit field that contains the
		 * timestamp of the segment. The timestamp is the number of milliseconds since
		 * the TCP connection was established.
		 * <p>
		 * The timestamp field is used by both the sender and receiver to calculate the
		 * round-trip time (RTT) of packets sent over the connection. The RTT is the
		 * time it takes for a packet to travel from one end of the connection to the
		 * other and back again.
		 * </p>
		 * .
		 *
		 * @return the unsigned 32-bit field value
		 */
		@Meta
		public long tsValue() {
			return Integer.toUnsignedLong(buffer().getInt(2));
		}

		/**
		 * Gets the TCP Timestamp option timestamp echo field value.
		 * <p>
		 * The TCP timestamp option timestamp echo field is a 32-bit field that contains
		 * the timestamp of the segment that was acknowledged. The timestamp is the
		 * number of milliseconds since the TCP connection was established.
		 * </p>
		 * <p>
		 * The timestamp echo field is used by both the sender and receiver to calculate
		 * the round-trip time (RTT) of packets sent over the connection. The RTT is the
		 * time it takes for a packet to travel from one end of the connection to the
		 * other and back again.
		 * </p>
		 * <p>
		 * The RTT is used by the TCP congestion control algorithm to determine how
		 * quickly data can be sent over the connection. The congestion control
		 * algorithm uses the RTT to calculate the window size, which is the amount of
		 * data that the sender can send before it needs to wait for an acknowledgment.
		 * </p>
		 * <p>
		 * The timestamp echo field is supported by most modern TCP implementations. It
		 * is enabled by default in most operating systems and web browsers.
		 * </p>
		 *
		 * @return the unsigned 32-bit field value
		 */
		@Meta
		public long tsEchoReply() {
			return Integer.toUnsignedLong(buffer().getInt(6));
		}

	};

	/**
	 * The TCP window scale option is a TCP option that allows the sender and
	 * receiver to increase the size of the receive window beyond the default value
	 * of 65,535 bytes. The receive window is the amount of data that the receiver
	 * can buffer before it needs to send an acknowledgment to the sender.
	 * <p>
	 * The TCP window scale option is a 3-byte option. The first byte of the option
	 * contains the window scale factor. The window scale factor is a number between
	 * 0 and 14. The window scale factor is multiplied by the default receive window
	 * size of 65,535 bytes to calculate the actual receive window size.
	 * </p>
	 * <p>
	 * For example, if the window scale factor is 2, then the actual receive window
	 * size is 2 * 65,535 = 131,072 bytes.
	 * </p>
	 * <p>
	 * The TCP window scale option is supported by most modern TCP implementations.
	 * It is enabled by default in most operating systems and web browsers.
	 * </p>
	 * <p>
	 * Here are some of the benefits of using the TCP window scale option:
	 * </p>
	 * <ul>
	 * <li>Increased throughput: The TCP window scale option can increase the
	 * throughput of TCP connections by allowing the sender to send more data before
	 * it needs to wait for an acknowledgment.</li>
	 * <li>Improved reliability: The TCP window scale option can improve the
	 * reliability of TCP connections by reducing the number of retransmissions
	 * needed.</li>
	 * </ul>
	 * <p>
	 * The TCP window scale option is a valuable tool for improving the performance
	 * and reliability of TCP connections. It can be used to improve the performance
	 * of web browsing, file transfers, and other applications that use TCP.
	 * </p>
	 */
	@MetaResource("tcp-opt-win-scale-meta.json")
	public static class TcpWindowScaleOption extends TcpOption {

		/** The Constant ID. */
		public static final int ID = TcpOptionId.TCP_OPT_ID_WIN_SCALE;

		/**
		 * Instantiates a new tcp window scale option.
		 */
		public TcpWindowScaleOption() {
			super(ID, TCP_OPTION_KIND_WIN_SCALE, TCP_OPTION_LEN_WIN_SCALE);
		}

		/**
		 * Computes the scale multiplier based on the field values of this TCP option.
		 *
		 * @return the unsigned 32-bit calculated multiplier value to be used to
		 *         calculate window size values in units of bytes
		 */
		@Meta(MetaType.ATTRIBUTE)
		public int multiplier() {
			return (1 << shiftCount());
		}

		/**
		 * Computes a scaled window value by applying the bit shift field value to the
		 * supplied unscaled window size.
		 *
		 * @param window the unscaled window size to be scaled using the field value of
		 *               this option
		 * @return the unsigned 32-bit field value in units of bytes
		 */
		public int scaleWindow(int window) {
			return window << shiftCount();
		}

		/**
		 * Gets the TCP window scale option shift count field value.
		 * <p>
		 * The TCP window scale option window shift count field is a 4-bit field that
		 * specifies the number of bits to shift the receive window size. The receive
		 * window size is the amount of data that the receiver can buffer before it
		 * needs to send an acknowledgment to the sender.
		 * </p>
		 * <p>
		 * The window shift count field is used to increase the receive window size
		 * beyond the default value of 65,535 bytes. For example, if the window shift
		 * count is 2, then the receive window size is 2^2 * 65,535 = 262,144 bytes.
		 * </p>
		 * <p>
		 * The window shift count field is supported by most modern TCP implementations.
		 * It is enabled by default in most operating systems and web browsers.
		 * </p>
		 * <p>
		 * Per RFC-7323 (TCP Extensions for High Performance):
		 * <q>The maximum scale exponent is limited to 14 for a maximum permissible
		 * receive window size of 1 GiB (2^(14+16)).</q>
		 * </p>
		 *
		 * @return the int
		 */
		@Meta
		public int shiftCount() {
			return Byte.toUnsignedInt(buffer().get(TCP_OPTION_FIELD_DATA));
		}
	};

	/** The TCP base option header ID constant. */
	public static final int ID = TcpOptionId.TCP_OPT_ID_OPTION;;

	/** The kind. */
	private final int kind;

	/** The length. */
	private final int length;

	/**
	 * Instantiates a new tcp option.
	 */
	public TcpOption() {
		super(ID);

		this.length = length();
		this.kind = kind();
	}

	/**
	 * Instantiates a new tcp option.
	 *
	 * @param id   the id
	 * @param kind the kind
	 */
	protected TcpOption(int id, int kind) {
		super(id);
		this.kind = kind;
		this.length = -1; // Dynamic length
	}

	/**
	 * Instantiates a new tcp option.
	 *
	 * @param id             the id
	 * @param kind           the kind
	 * @param constantLength the constant length
	 */
	protected TcpOption(int id, int kind, int constantLength) {
		super(id);
		this.kind = kind;
		this.length = constantLength;
	}

	/**
	 * Gets the TCP option kind field value.
	 * <p>
	 * The TCP option kind field is a 1-byte field in the TCP header that identifies
	 * the type of TCP option. There are a number of different TCP options, each
	 * with its own unique kind field value.
	 * </p>
	 * <p>
	 * The following table lists the most common TCP options and their kind field
	 * values:
	 * </p>
	 * <table>
	 * <tr>
	 * <th>Option</th>
	 * <th>Kind Field Value</th>
	 * </tr>
	 * <tr>
	 * <td>Maximum Segment Size (MSS)</td>
	 * <td>1</td>
	 * </tr>
	 * <tr>
	 * <td>Window Scale</td>
	 * <td>2</td>
	 * </tr>
	 * <tr>
	 * <td>Selected Acknowledgments (SACK)</td>
	 * <td>4</td>
	 * </tr>
	 * <tr>
	 * <td>Timestamp</td>
	 * <td>8</td>
	 * </tr>
	 * <tr>
	 * <td>Nop</td>
	 * <td>0</td>
	 * </tr>
	 * </table>
	 * <p>
	 * The TCP option kind field is used by the sender and receiver to determine
	 * which TCP options are present in a TCP segment. The sender and receiver use
	 * this information to interpret the data in the TCP segment and to control the
	 * behavior of the TCP protocol.
	 * </p>
	 * <p>
	 * The TCP option kind field is an important part of the TCP protocol. It allows
	 * the sender and receiver to communicate with each other and to control the
	 * behavior of the TCP protocol.
	 * </p>
	 * 
	 * @return the unsigned 8-bit field value
	 */
	@Meta
	public int kind() {
		return kind;
	}

	/**
	 * Sets the TCP option kind field value.
	 * <p>
	 * The TCP option kind field is a 1-byte field in the TCP header that identifies
	 * the type of TCP option. There are a number of different TCP options, each
	 * with its own unique kind field value.
	 * </p>
	 * <p>
	 * The following table lists the most common TCP options and their kind field
	 * values:
	 * </p>
	 * <table>
	 * <tr>
	 * <th>Option</th>
	 * <th>Kind Field Value</th>
	 * </tr>
	 * <tr>
	 * <td>Maximum Segment Size (MSS)</td>
	 * <td>1</td>
	 * </tr>
	 * <tr>
	 * <td>Window Scale</td>
	 * <td>2</td>
	 * </tr>
	 * <tr>
	 * <td>Selected Acknowledgments (SACK)</td>
	 * <td>4</td>
	 * </tr>
	 * <tr>
	 * <td>Timestamp</td>
	 * <td>8</td>
	 * </tr>
	 * <tr>
	 * <td>Nop</td>
	 * <td>0</td>
	 * </tr>
	 * </table>
	 * <p>
	 * The TCP option kind field is used by the sender and receiver to determine
	 * which TCP options are present in a TCP segment. The sender and receiver use
	 * this information to interpret the data in the TCP segment and to control the
	 * behavior of the TCP protocol.
	 * </p>
	 * <p>
	 * The TCP option kind field is an important part of the TCP protocol. It allows
	 * the sender and receiver to communicate with each other and to control the
	 * behavior of the TCP protocol.
	 * </p>
	 *
	 * @param newKind new unsigned 8-bit field value
	 */
	public TcpOption kind(int newKind) {
		buffer().put(CoreConstants.TCP_OPTION_FIELD_KIND, (byte) newKind);

		return this;
	}

	/**
	 * Gets the TCP option length field value.
	 * <p>
	 * The TCP option length field is a 1-byte field in the TCP header that
	 * indicates the length of the TCP option. The length of the TCP option is
	 * measured in bytes, and it includes the length of the kind field and the
	 * length of the option data.
	 * </p>
	 * <p>
	 * The TCP option length field is used by the sender and receiver to determine
	 * how much data to read from the TCP segment. The sender and receiver use this
	 * information to interpret the data in the TCP segment and to control the
	 * behavior of the TCP protocol.
	 * </p>
	 * <p>
	 * The TCP option length field is an important part of the TCP protocol. It
	 * allows the sender and receiver to communicate with each other and to control
	 * the behavior of the TCP protocol.
	 * </p>
	 * <p>
	 * Here are some additional details about the TCP option length field:
	 * </p>
	 * <ul>
	 * <li>The TCP option length field is a 1-byte field in the TCP header.</li>
	 * <li>The length of the TCP option is measured in bytes.</li>
	 * <li>The length of the TCP option includes the length of the kind field and
	 * the length of the option data.</li>
	 * <li>The TCP option length field is used by the sender and receiver to
	 * determine how much data to read from the TCP segment.</li>
	 * </ul>
	 * <p>
	 * The TCP option length field is an important part of the TCP protocol.
	 * </p>
	 * 
	 * @return the usigned 8-bit length field value in units of bytes
	 */
	@Meta
	public int length() {
		return ((length != -1)
				? length
				: Byte.toUnsignedInt(buffer().get(TCP_OPTION_FIELD_LENGTH)));
	}

	/**
	 * Sets the TCP option length field value.
	 * <p>
	 * The TCP option length field is a 1-byte field in the TCP header that
	 * indicates the length of the TCP option. The length of the TCP option is
	 * measured in bytes, and it includes the length of the kind field and the
	 * length of the option data.
	 * </p>
	 * <p>
	 * The TCP option length field is used by the sender and receiver to determine
	 * how much data to read from the TCP segment. The sender and receiver use this
	 * information to interpret the data in the TCP segment and to control the
	 * behavior of the TCP protocol.
	 * </p>
	 * <p>
	 * The TCP option length field is an important part of the TCP protocol. It
	 * allows the sender and receiver to communicate with each other and to control
	 * the behavior of the TCP protocol.
	 * </p>
	 * <p>
	 * Here are some additional details about the TCP option length field:
	 * </p>
	 * <ul>
	 * <li>The TCP option length field is a 1-byte field in the TCP header.</li>
	 * <li>The length of the TCP option is measured in bytes.</li>
	 * <li>The length of the TCP option includes the length of the kind field and
	 * the length of the option data.</li>
	 * <li>The TCP option length field is used by the sender and receiver to
	 * determine how much data to read from the TCP segment.</li>
	 * </ul>
	 * <p>
	 * The TCP option length field is an important part of the TCP protocol.
	 * </p>
	 *
	 * @param newLength new unsigned 8-bit length field value, in units of bytes
	 */
	public void length(int newLength) {
		buffer().put(TCP_OPTION_FIELD_LENGTH, (byte) kind);
	}

}