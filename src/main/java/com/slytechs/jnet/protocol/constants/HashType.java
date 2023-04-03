/*
 * Apache License, Version 2.0
 * 
 * Copyright 2013-2022 Sly Technologies Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.slytechs.jnet.protocol.constants;

import java.util.function.IntSupplier;

/**
 * 
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public enum HashType implements IntSupplier {
	NONE,
	TUPLE2,
	TUPLE2_SORTED,
	TUPLE5,
	TUPLE5_SORTED,
	TUPLE3_GRE_V0,
	TUPLE3_GRE_V0_SORTED,
	TUPLE3_GTP_V0,
	TUPLE3_GTP_V0_SORTED,
	TUPLE3_GTP_V1_V2,
	TUPLE3_GTP_V1_V2_SORTED,
	TUPLE5_SCTP,
	TUPLE5_SCTP_SORTED,
	LAST_MPLS,
	ALL_MPLS_LABELS,
	LAST_VLAN_ID,
	ALL_VLAN_IDS,
	INNER_TUPLE2,
	INNER_TUPLE2_SORTED,
	INNER_TUPLE5,
	INNER_TUPLE5_SORTED,
	ROUND_ROBIN,

	;

	public int getAsInt() {
		return ordinal();
	}

	public static HashType valueOf(int type) {
		return values()[type];
	}
}
