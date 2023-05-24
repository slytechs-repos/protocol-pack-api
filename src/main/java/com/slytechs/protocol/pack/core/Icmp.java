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

import com.slytechs.protocol.Header;
import com.slytechs.protocol.meta.Meta;
import com.slytechs.protocol.meta.Meta.MetaType;
import com.slytechs.protocol.meta.MetaResource;
import com.slytechs.protocol.pack.PackId;
import com.slytechs.protocol.pack.core.constants.CoreId;

/**
 * The Class Icmp.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
@MetaResource("icmp-meta.json")
public sealed class Icmp extends Header
		permits Icmp4, Icmp6 {

	public static int ID = CoreId.CORE_ID_ICMP;
	private int version;

	public Icmp() {
		super(ID);
	}

	/**
	 * Instantiates a new icmp.
	 *
	 * @param id the id
	 */
	protected Icmp(int id) {
		super(id);
	}

	/**
	 * Instantiates a new icmp.
	 *
	 * @param id   the id
	 * @param lock the lock
	 */
	protected Icmp(int id, Lock lock) {
		super(id, lock);
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

	@Override
	protected void onBind() {
		super.onBind();

		int effectiveId = getHeaderDescriptor().getEffectiveId();

		this.version = PackId.classmaskCheck(CoreId.CORE_CLASS_V4, effectiveId)
				? 4
				: 6;

	}

	@Meta(MetaType.ATTRIBUTE)
	public int version() {
		return this.version;
	}
}
