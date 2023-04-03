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
import com.slytechs.jnet.protocol.packet.Header;

/**
 * 
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public class Udp extends Header {

	public static final int ID = CoreHeaderInfo.CORE_ID_UDP;

	public Udp() {
		super(ID);
	}

	public int checksum() {
		return UdpStruct.CHECKSUM.getUnsignedShort(buffer());
	}

	public void checksum(int checksum) {
		UdpStruct.CHECKSUM.setInt(checksum, buffer());
	}

	/**
	 * Dst port.
	 *
	 * @return the int
	 */
	public int dstPort() {
		return UdpStruct.DST_PORT.getUnsignedShort(buffer());
	}

	public void dstPort(int dstPort) {
		UdpStruct.DST_PORT.setInt(dstPort, buffer());
	}

	@Override
	public int length() {
		return UdpStruct.LENGTH.getUnsignedShort(buffer());
	}

	public void length(int length) {
		UdpStruct.LENGTH.setInt(length, buffer());
	}

	public int srcPort() {
		return UdpStruct.SRC_PORT.getUnsignedShort(buffer());
	}

	public void srcPort(int srcPort) {
		UdpStruct.SRC_PORT.setInt(srcPort, buffer());
	}

}
