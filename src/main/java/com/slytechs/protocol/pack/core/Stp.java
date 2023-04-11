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

import java.util.concurrent.locks.Lock;

import com.slytechs.protocol.Header;
import com.slytechs.protocol.meta.Meta;
import com.slytechs.protocol.pack.core.constants.CoreIdTable;

/**
 * The Spanning Tree Protocol (STP).
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
@Meta
public final class Stp extends Header {

	/** Core protocol pack assigned header ID. */
	public static int ID = CoreIdTable.CORE_ID_STP;

	/**
	 * Instantiates a new STP header.
	 */
	public Stp() {
		super(ID);
	}

	/**
	 * Instantiates a new STP header.
	 *
	 * @param lock the lock
	 */
	public Stp(Lock lock) {
		super(ID, lock);
	}

	/**
	 * Protocol identifier.
	 *
	 * @return the int
	 */
	@Meta
	public int protocol() {
		return StpLayout.PROTOCOL.getInt(buffer());
	}

	/**
	 * Version.
	 *
	 * @return the int
	 */
	@Meta
	public int version() {
		return StpLayout.VERSION.getInt(buffer());
	}

	/**
	 * Message type.
	 *
	 * @return the int
	 */
	@Meta
	public int type() {
		return StpLayout.TYPE.getInt(buffer());
	}

	/**
	 * Flags.
	 *
	 * @return the int
	 */
	@Meta
	public int flags() {
		return StpLayout.FLAGS.getInt(buffer());
	}

	/**
	 * Root id.
	 *
	 * @return the long
	 */
	@Meta
	public long rootId() {
		return StpLayout.ROOT_ID.getLong(buffer());
	}

	/**
	 * Root path cost.
	 *
	 * @return the int
	 */
	@Meta
	public int rootCost() {
		return StpLayout.ROOT_PATH_COST.getInt(buffer());
	}

	/**
	 * Bridge id.
	 *
	 * @return the long
	 */
	@Meta
	public long bridgeId() {
		return StpLayout.BRIDGE_ID.getLong(buffer());
	}

	/**
	 * Port id.
	 *
	 * @return the int
	 */
	@Meta
	public int portId() {
		return StpLayout.PORT_ID.getInt(buffer());
	}

	/**
	 * Message age.
	 *
	 * @return the int
	 */
	@Meta
	public int messageAge() {
		return StpLayout.MSG_AGE.getInt(buffer());
	}

	/**
	 * Maximum time.
	 *
	 * @return the int
	 */
	@Meta
	public int maxAge() {
		return StpLayout.MAX_AGE.getInt(buffer());
	}

	/**
	 * Hello time.
	 *
	 * @return the int
	 */
	@Meta
	public int helloTime() {
		return StpLayout.HELLO.getInt(buffer());
	}

	/**
	 * Forward delay.
	 *
	 * @return the int
	 */
	@Meta
	public int forwardDelay() {
		return StpLayout.FORWARD.getInt(buffer());
	}
}
