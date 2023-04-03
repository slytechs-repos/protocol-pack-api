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

import com.slytechs.jnet.protocol.constants.Ip4OptionInfo;
import com.slytechs.jnet.protocol.core.Ip.IpOption;

public abstract class Ip4Option extends IpOption {

	public static class Ip4OptRouter extends Ip4Option {
		public static final int ID = Ip4OptionInfo.IPv4_OPT_ID_RTRALT;

		public Ip4OptRouter() {
			super(ID);
		}

		public boolean examinePacket() {
			return buffer().getShort(2) == 0;
		}

	}

	protected Ip4Option(int id) {
		super(id);
	}

}
