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

import java.util.Objects;

import com.slytechs.protocol.Address;
import com.slytechs.protocol.AddressType;
import com.slytechs.protocol.pack.core.constants.CoreConstants;

/**
 * The Class IpAddress.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public abstract sealed class IpAddress implements Address
		permits Ip4Address, Ip6Address {

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

	/** The type. */
	private final AddressType type;

	/**
	 * Instantiates a new ip address.
	 *
	 * @param type the type
	 */
	protected IpAddress(AddressType type) {
		super();
		this.type = type;
	}

	/**
	 * Type.
	 *
	 * @return the address type
	 * @see com.slytechs.protocol.Address#type()
	 */
	@Override
	public AddressType type() {
		return this.type;
	}

	public abstract byte[] asArray();

	@Override
	public String toString() {
		return toString(asArray());
	}
}
