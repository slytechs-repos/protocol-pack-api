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
import java.util.Objects;

import com.slytechs.protocol.Header;
import com.slytechs.protocol.HeaderExtension;
import com.slytechs.protocol.descriptor.IpfReassembly;
import com.slytechs.protocol.descriptor.IpfTracking;
import com.slytechs.protocol.pack.core.Ip.IpOption;
import com.slytechs.protocol.pack.core.constants.CoreConstants;
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
//@MetaResource("ip-meta.json")
public abstract class Ip<T extends IpOption> extends HeaderExtension<T> {

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

	/**
	 * To string.
	 *
	 * @param ipAddress the ip address
	 * @return the string
	 */
	public static String toString(byte[] ipAddress) {
		return toString(ipAddress, new StringBuilder(CoreConstants.IPv6_ADDRESS_STRING_SIZE)).toString();
	}

	/**
	 * To string.
	 *
	 * @param ipAddress the ip address
	 * @param b         the b
	 * @return the string builder
	 */
	public static StringBuilder toString(byte[] ipAddress, StringBuilder b) {
		if ((ipAddress.length != CoreConstants.IPv4_ADDRESS_SIZE)
				&& (ipAddress.length != CoreConstants.IPv6_ADDRESS_SIZE))
			throw new IllegalArgumentException("invalid IP address length, must be either 4 or 16");

		return (ipAddress.length == 4)
				? formatIp4Address(ipAddress, b)
				: formatIp6Address(ipAddress, b);
	}

	/**
	 * Converts IPv4 binary address into a string suitable for presentation.
	 *
	 * @param src a byte array representing an IPv4 numeric address
	 * @param b   the b
	 * @return a String representing the IPv4 address in textual representation
	 *         format
	 */
	private static StringBuilder formatIp4Address(byte[] src, StringBuilder b) {

		b.append(src[0] & 0xff)
				.append(".")
				.append(src[1] & 0xff)
				.append(".")
				.append(src[2] & 0xff)
				.append(".")
				.append(src[3] & 0xff);

		return b;
	}

	/**
	 * Convert IPv6 binary address into presentation (printable) format.
	 *
	 * @param src a byte array representing the IPv6 numeric address
	 * @param b   the b
	 * @return a String representing an IPv6 address in textual representation
	 *         format
	 */
	private static StringBuilder formatIp6Address(byte[] src, StringBuilder b) {

		for (int i = 0; i < (CoreConstants.IPv6_ADDRESS_SIZE / 2); i++) {
			b.append(Integer.toHexString(((src[i << 1] << 8) & 0xff00) | (src[(i << 1) + 1] & 0xff)));
			if (i < (CoreConstants.IPv6_ADDRESS_SIZE / 2) - 1) {
				b.append(":");
			}
		}

		return b;
	}

	/**
	 * Strip netmask.
	 *
	 * @param address the address
	 * @return the string
	 */
	private static String stripNetmask(String address) {
		if (address.contains("/"))
			address = address.replaceFirst("^(.+)/.+$", "$1"); // Strip netmask

		return address;
	}

	/**
	 * Parses the ip 4 address string.
	 *
	 * @param ipAddress the ip address
	 * @return the byte[]
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	private static byte[] parseIp4AddressString(String ipAddress) throws IllegalArgumentException {
		ipAddress = stripNetmask(ipAddress);

		String[] c = ipAddress.split("\\.");
		if (c.length != 4)
			throw new IllegalArgumentException("invalid IPv4 address format " + ipAddress);

		byte[] result = new byte[4];
		result[0] = (byte) Integer.parseInt(c[0]);
		result[1] = (byte) Integer.parseInt(c[1]);
		result[2] = (byte) Integer.parseInt(c[2]);
		result[3] = (byte) Integer.parseInt(c[3]);

		return result;
	}

	/**
	 * Parses the ip 6 address string.
	 *
	 * @param ipAddress the ip address
	 * @return the byte[]
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	private static byte[] parseIp6AddressString(String ipAddress) throws IllegalArgumentException {
		ipAddress = stripNetmask(ipAddress);

		/**
		 * Check for short hand notation:
		 * 
		 * <pre>
		 * ::
		 * ::1
		 * 64:ff9b::255.255.255.255
		 * </pre>
		 */
		String[] z = ipAddress.split("::");
		String[] c = ipAddress.split("[:-]");
		if (c.length != 4)
			throw new IllegalArgumentException("invalid IPv4 address format " + ipAddress);

		byte[] result = new byte[4];
		result[0] = (byte) Integer.parseInt(c[0]);
		result[1] = (byte) Integer.parseInt(c[1]);
		result[2] = (byte) Integer.parseInt(c[2]);
		result[3] = (byte) Integer.parseInt(c[3]);

		return result;
	}

	/**
	 * Parses the ip address string.
	 *
	 * @param ipAddress the ip address
	 * @return the byte[]
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public static byte[] parseIpAddressString(String ipAddress) throws IllegalArgumentException {
		Objects.requireNonNull(ipAddress, "ipAddress");

		if (ipAddress.contains("."))
			return parseIp4AddressString(ipAddress);
		else
			return parseIp6AddressString(ipAddress);
	}

	/**
	 * Src.
	 *
	 * @return the byte[]
	 */
//	@Meta
	public abstract byte[] src();

	/**
	 * Src get as address.
	 *
	 * @return the ip address
	 */
	public abstract IpAddress srcGetAsAddress();

	/**
	 * Dst.
	 *
	 * @return the byte[]
	 */
//	@Meta
	public abstract byte[] dst();

	/**
	 * Dst address.
	 *
	 * @return the ip address
	 */
	public abstract IpAddress dstAddress();

	/**
	 * Instantiates a new ip.
	 *
	 * @param id the id
	 */
	protected Ip(int id) {
		super(id);
	}

	/**
	 * Version.
	 *
	 * @return the int
	 */
//	@Meta
	public abstract int version();

	/**
	 * Payload length.
	 *
	 * @return the int
	 * @see com.slytechs.protocol.Header#payloadLength()
	 */
	@Override
	public abstract int payloadLength();

	/**
	 * Checks if this IP datagram has been reassembled.
	 *
	 * @return true, if is reassembled
	 */
	public final boolean isReassembled() {
		return super.descriptor().hasDescriptor(IpfDescriptorType.IPF_REASSEMBLY);
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

	public final ByteBuffer getReassembledBuffer() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public final IpfReassembly getIpfReassemblyDescriptor() {
		return (IpfReassembly) super.descriptor().peekDescriptor(IpfDescriptorType.IPF_REASSEMBLY);
	}

	public final IpfTracking getIpfTrackingDescriptor() {
		return (IpfTracking) super.descriptor().peekDescriptor(IpfDescriptorType.IPF_TRACKING);
	}
}
