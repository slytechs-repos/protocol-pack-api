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

import com.slytechs.protocol.Header;
import com.slytechs.protocol.meta.Meta;
import com.slytechs.protocol.pack.core.constants.CorePackIds;

/**
 * The Class Udp.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
@Meta
public class Udp extends Header {

	/** The Constant ID. */
	public static final int ID = CorePackIds.CORE_ID_UDP;

	/**
	 * Instantiates a new udp.
	 */
	public Udp() {
		super(ID);
	}

	/**
	 * Checksum.
	 *
	 * @return the int
	 */
	public int checksum() {
		return UdpStruct.CHECKSUM.getUnsignedShort(buffer());
	}

	/**
	 * Checksum.
	 *
	 * @param checksum the checksum
	 */
	public void checksum(int checksum) {
		UdpStruct.CHECKSUM.setInt(checksum, buffer());
	}

	/**
	 * Dst port.
	 *
	 * @return the int
	 */
	@Meta
	public int dstPort() {
		return UdpStruct.DST_PORT.getUnsignedShort(buffer());
	}

	/**
	 * Dst port.
	 *
	 * @param dstPort the dst port
	 */
	public void dstPort(int dstPort) {
		UdpStruct.DST_PORT.setInt(dstPort, buffer());
	}

	/**
	 * @see com.slytechs.protocol.Header#length()
	 */
	@Override
	@Meta
	public int length() {
		return UdpStruct.LENGTH.getUnsignedShort(buffer());
	}

	/**
	 * Length.
	 *
	 * @param length the length
	 */
	public void length(int length) {
		UdpStruct.LENGTH.setInt(length, buffer());
	}

	/**
	 * Src port.
	 *
	 * @return the int
	 */
	@Meta
	public int srcPort() {
		return UdpStruct.SRC_PORT.getUnsignedShort(buffer());
	}

	/**
	 * Src port.
	 *
	 * @param srcPort the src port
	 */
	public void srcPort(int srcPort) {
		UdpStruct.SRC_PORT.setInt(srcPort, buffer());
	}

}
