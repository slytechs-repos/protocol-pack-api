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
package com.slytechs.jnet.protocol.constants;

import com.slytechs.jnet.protocol.HeaderInfo;
import com.slytechs.jnet.protocol.HeaderSupplier;
import com.slytechs.jnet.protocol.core.Ethernet;
import com.slytechs.jnet.protocol.packet.Header;
import com.slytechs.jnet.protocol.packet.Other;

/**
 * 
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public enum L2FrameType implements HeaderInfo {

	OTHER(0),
	ETHER(CoreHeaderInfo.CORE_ID_ETHER, Ethernet::new),
	LLC(CoreHeaderInfo.CORE_ID_LLC),
	SNAP(CoreHeaderInfo.CORE_ID_SNAP),
	PPP(CoreHeaderInfo.CORE_ID_PPP),
	FDDI(CoreHeaderInfo.CORE_ID_FDDI),
	ATM(CoreHeaderInfo.CORE_ID_ATM),
	NOVELL_RAW(CoreHeaderInfo.CORE_ID_ETHER),
	ISL(CoreHeaderInfo.CORE_ID_ETHER),

	;

	private final int id;
	private final HeaderSupplier supplier;

	L2FrameType(int id) {
		this.id = id;
		this.supplier = Other::new;
	}

	L2FrameType(int id, HeaderSupplier supplier) {
		this.id = id;
		this.supplier = supplier;
	}

	public final static int L2_FRAME_TYPE_UNKNOWN = -1;
	public final static int L2_FRAME_TYPE_OTHER = 0;
	public final static int L2_FRAME_TYPE_ETHER = 1;
	public final static int L2_FRAME_TYPE_LLC = 2;
	public final static int L2_FRAME_TYPE_SNAP = 3;
	public final static int L2_FRAME_TYPE_NOVELL_RAW = 4;
	public final static int L2_FRAME_TYPE_ISL = 5;
	public final static int L2_FRAME_TYPE_PPP = 6;
	public final static int L2_FRAME_TYPE_FDDI = 7;
	public final static int L2_FRAME_TYPE_ATM = 8;

	public static L2FrameType valueOf(int l2FrameType) {
		return values()[l2FrameType];
	}

	public int getL2FrameTypeAsInt() {
		return ordinal();
	}

	/**
	 * @see com.slytechs.jnet.protocol.HeaderInfo#getHeaderId()
	 */
	@Override
	public int getHeaderId() {
		return id;
	}

	/**
	 * @see com.slytechs.jnet.protocol.HeaderSupplier#newHeaderInstance()
	 */
	@Override
	public Header newHeaderInstance() {
		return supplier != null ? supplier.newHeaderInstance() : null;
	}

}
