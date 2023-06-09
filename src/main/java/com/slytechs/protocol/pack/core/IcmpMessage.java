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

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 *
 */
public interface IcmpMessage {

	
	int type();

	/**
	 * The ICMP code field is an 8-bit field in the ICMP header that specifies the
	 * subtype of the ICMP message. The code field is used to provide additional
	 * information about the purpose of the ICMP message.
	 *
	 * 
	 * @return the unsigned 8-bit field value
	 */
	int code();
	
	/**
	 * The ICMP checksum field is a 16-bit field in the ICMP header that is used to
	 * verify the integrity of the ICMP message. The checksum is calculated by
	 * adding the 16-bit values of all the fields in the ICMP header and the data
	 * field, and then taking the one's complement of the result.
	 * <p>
	 * If the checksum of an ICMP message does not match, the message is discarded.
	 * This helps to prevent errors from occurring when ICMP messages are
	 * transmitted over the network.
	 * </p>
	 * <p>
	 * The ICMP checksum field is a valuable tool for troubleshooting network
	 * problems. By verifying the checksum of an ICMP message, you can quickly
	 * identify if the message has been corrupted in transit.
	 * </p>
	 *
	 * @return the unsigned 16-bit field value
	 */	int checksum();
}
