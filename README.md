# Core Protocol Pack
Core Protocol pack consisting of many common protocols and runtime support for other modules.

### Latest status - [Pull Request #1](https://github.com/slytechs-repos/core-protocols/pull/4)
The module is currently almost ready for initial API delopment. Please see [pull #1](https://github.com/slytechs-repos/core-protocols/pull/4) for the latest status.

## Where to use this module
This module, **core-protocols** is an extension module which provides protocol level support for one of the main public APIs. You need to setup your application using either **jnetpcap-pro** or **jnetwork** modules which provide the main APIs for building applications using any of the protocol packs.

## What's inside
The **core-protocols** module provides support for the following services:
- Runtime support for all of the modules using combination of public and private APIs
  - Private APIs are exported to other modules and provide common implementation features across all modules
  - A small public API is also exported for things like capture `Timestamp` and `TimestampUnit` classes, etc...
- Raw packet dissection
  - Advanced packet descriptors store the results of the dissection process after packet capture
  - Information about the presence of each protocol header is recorded in `PacketDescriptorType.TYPE2`
- IP Fragmentation processing
  - IP fragment reassembly into full IP datagrams
  - IP fragment tracking regardless if reassembly is enabled
- A very efficient packet module using the `Packet` class
  - Instrumentation module using `MetaPacket` class, similar to java beans
- Packet formatters, various packet formatters for displaying packet state and fields
  - A pretty print formatter which dumps easy to read details about a packet and its headers
  - Coverters for XML, JSON and other output types
- A set of protocols which are considered **core** or common on most networks

### Protocol Table
Here is a table of all of the protocol definitions provided by this **core-protocols** module.

| Builtin | Layer2  | Layer3 | Layer4 | Layer7 |
|---------|---------|--------|--------|--------|
|[Payload][payload]  |[Ethernet][eth] |[IPv4][ip4]    |[TCP][tcp]     |DHCP    |
|[Frame][frame]    |LLC/SNAP |[IPv6][ip6]    |[UDP][udp]     |
|         |VLAN     |IPX     |SCTP    |
|         |PPP      |MPLS    |[ICMPv4][icmp4]
|         |STP      |IGMP    |[ICMPv6][icmp6]
|         |ATM      |        |        |
|         |FDDI     |        |        |
|         |[ARP][arp] |        |        |

[payload]: https://github.com/slytechs-repos/core-protocols/blob/develop/src/main/java/com/slytechs/protocol/Payload.java
[frame]: https://github.com/slytechs-repos/core-protocols/blob/develop/src/main/java/com/slytechs/protocol/Frame.java
[eth]: https://github.com/slytechs-repos/core-protocols/blob/develop/src/main/java/com/slytechs/protocol/pack/core/Ethernet.java
[arp]: https://github.com/slytechs-repos/core-protocols/blob/develop/src/main/java/com/slytechs/protocol/pack/core/Arp.java
[ip4]: https://github.com/slytechs-repos/core-protocols/blob/develop/src/main/java/com/slytechs/protocol/pack/core/Ip4.java
[ip6]: https://github.com/slytechs-repos/core-protocols/blob/develop/src/main/java/com/slytechs/protocol/pack/core/Ip6.java
[udp]: https://github.com/slytechs-repos/core-protocols/blob/develop/src/main/java/com/slytechs/protocol/pack/core/Udp.java
[tcp]: https://github.com/slytechs-repos/core-protocols/blob/develop/src/main/java/com/slytechs/protocol/pack/core/Tcp.java
[icmp4]: https://github.com/slytechs-repos/core-protocols/blob/develop/src/main/java/com/slytechs/protocol/pack/core/Icmp4.java
[icmp6]: https://github.com/slytechs-repos/core-protocols/blob/develop/src/main/java/com/slytechs/protocol/pack/core/Icmp6.java
