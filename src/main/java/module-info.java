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
 * <dt>Stp - Spanning Tree Protocol</dt>
 * <dt>Ip - Internet Protocol IPv4 and IPv6</dt>
 * <dt>Icmp - Internet Control Message Protocol</dt>
 * <dt>Tcp - Transmission Control Protocol</dt>
 * <dt>Udp - User Datagram Protocol</dt>
 * </dl>
 * 
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
module com.slytechs.protocol {

	/* Private API */
	exports com.slytechs.protocol.runtime.internal;
	exports com.slytechs.protocol.runtime.internal.layout;
	exports com.slytechs.protocol.runtime.internal.foreign;
	exports com.slytechs.protocol.runtime.internal.concurrent;
	exports com.slytechs.protocol.runtime.internal.json to
			com.slytechs.protocol.pack.web;

	/* Public API */
	exports com.slytechs.protocol;
	exports com.slytechs.protocol.pack;
	exports com.slytechs.protocol.pack.core;
	exports com.slytechs.protocol.pack.core.constants;
	exports com.slytechs.protocol.descriptor;
	exports com.slytechs.protocol.meta;
	exports com.slytechs.protocol.runtime;
	exports com.slytechs.protocol.runtime.time;
	exports com.slytechs.protocol.runtime.util;
	exports com.slytechs.protocol.runtime.internal.util.function;
	exports com.slytechs.protocol.runtime.internal.util.format;

	requires java.logging;
}
