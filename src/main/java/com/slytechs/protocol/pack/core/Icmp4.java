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

import java.util.concurrent.locks.Lock;

import com.slytechs.protocol.meta.Meta;
import com.slytechs.protocol.meta.MetaResource;
import com.slytechs.protocol.pack.core.constants.CoreId;

/**
 * The Class Icmp4.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
@MetaResource("icmp4-meta.json")
public class Icmp4 extends Icmp {

	/** The Constant ID. */
	public static final int ID = CoreId.CORE_ID_ICMPv4;

	/**
	 * Instantiates a new icmp 4.
	 */
	public Icmp4() {
		super(ID);
	}

	/**
	 * Instantiates a new icmp 4.
	 *
	 * @param lock the lock
	 */
	public Icmp4(Lock lock) {
		super(ID, lock);
	}

	@Meta
	public int type() {
		return Byte.toUnsignedInt(buffer().get(ICMPv4_FIELD_TYPE));
	}

	@Meta
	public int code() {
		return Byte.toUnsignedInt(buffer().get(ICMPv4_FIELD_CODE));
	}

	@Meta
	public int checksum() {
		return Short.toUnsignedInt(buffer().get(ICMPv4_FIELD_CHECKSUM));
	}

}
