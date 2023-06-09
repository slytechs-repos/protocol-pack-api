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

import com.slytechs.protocol.meta.Meta;
import com.slytechs.protocol.pack.core.constants.CoreConstants;
import com.slytechs.protocol.pack.core.constants.TcpOptionId;

/**
 * The TCP option End of List (EOL) is a special option that marks the end of
 * the list of TCP options in a TCP segment. The EOL option is always the last
 * option in the list, and it has a kind field value of 0.
 */
@Meta
public class TcpEol extends TcpOption {

	/** The Constant ID. */
	public static final int ID = TcpOptionId.TCP_OPT_ID_EOL;

	/**
	 * Instantiates a new tcp end of list option.
	 */
	public TcpEol() {
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