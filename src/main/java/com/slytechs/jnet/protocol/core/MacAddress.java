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

import com.slytechs.jnet.runtime.util.HexStrings;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public class MacAddress {

	/**
	 * @param destination
	 */
	public MacAddress(byte[] destination) {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param destination
	 * @return
	 */
	public static String toOuiString(byte[] destination) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/**
	 * @param address
	 * @return
	 */
	public static String toString(byte[] address) {
		return HexStrings.toMacString(address);
	}

}
