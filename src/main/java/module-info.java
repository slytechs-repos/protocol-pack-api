
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

/**
 * Base protocol support for packet dissection, protocol classification, IP
 * fragment reassembly, UDP and TCP stream reassembly.
 * <p>
 * The following protocols are supported:
 * </p>
 * <dl>
 * <dt>Ethernet - IEEE 802.3, Ethernet2</dt>
 * <dt>Llc - provides LLC functions to IEEE 802 MAC layers</dt>
 * <dt>Vlan - IEEE 802.1q datalink Vlan tags</dt>
 * <dt>MPLS - MPLS labels</dt>
 * <dt>Ip - Internet Protocol IPv4 and IPv6</dt>
 * <dt>Icmp - Internet Control Message Protocol</dt>
 * <dt>Stp - Spanning Tree Protocol</dt>
 * <dt>Stp - Spanning Tree Protocol</dt>
 * </dl>
 * 
 * @author Sly Technologies
 * @author mark
 * @author repos@slytechs.com
 */
module com.slytechs.jnet.protocol.core {

	exports com.slytechs.jnet.protocol;
	exports com.slytechs.jnet.protocol.core;
	exports com.slytechs.jnet.protocol.core.constants;
	exports com.slytechs.jnet.protocol.descriptor;
	exports com.slytechs.jnet.protocol.meta;
	exports com.slytechs.jnet.protocol.util;

	/* Private API */
	exports com.slytechs.jnet.runtime.internal;
	exports com.slytechs.jnet.runtime.internal.layout;
	exports com.slytechs.jnet.runtime.internal.foreign;
	exports com.slytechs.jnet.runtime.internal.concurrent;
	exports com.slytechs.jnet.runtime.internal.json to
			com.slytechs.jnet.protocol.core,
			com.slytechs.jnet.protocol.web;

	/* Public API */
	exports com.slytechs.jnet.runtime;
	exports com.slytechs.jnet.runtime.time;
	exports com.slytechs.jnet.runtime.util;
	exports com.slytechs.jnet.runtime.internal.util.function;
	exports com.slytechs.jnet.runtime.internal.util.format;

	requires java.logging;
}
