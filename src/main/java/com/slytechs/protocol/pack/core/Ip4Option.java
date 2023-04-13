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
import com.slytechs.protocol.pack.core.Ip.IpOption;
import com.slytechs.protocol.pack.core.constants.Ip4OptionInfo;

/**
 * The Class Ip4Option.
 */
public abstract class Ip4Option extends IpOption {

	/**
	 * The Class Ip4OptRouter.
	 */
	@Meta
	public static class Ip4RouterOption extends Ip4Option {

		/** The Constant ID. */
		public static final int ID = Ip4OptionInfo.IPv4_OPT_ID_RTRALT;

		/**
		 * Instantiates a new ip 4 opt router.
		 */
		public Ip4RouterOption() {
			super(ID);
		}

		/**
		 * Examine packet.
		 *
		 * @return true, if successful
		 */
		public boolean examinePacket() {
			return buffer().getShort(2) == 0;
		}

		@Meta
		public int optionType() {
			return Byte.toUnsignedInt(buffer().get(0));
		}

		@Meta
		public int optionLength() {
			return Byte.toUnsignedInt(buffer().get(1));
		}

	}

	/**
	 * Instantiates a new ip 4 option.
	 *
	 * @param id the id
	 */
	protected Ip4Option(int id) {
		super(id);
	}

}
