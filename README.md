# Core Protocol Pack
Core Protocol pack consisting of many common protocols runtime support for other modules.

## Where to use this module
This module, **core-protocols** is not usable by itself. You need to setup your application using either **jnetpcap-pro** or **jnetwork** modules which provide the main APIs for building applications using any of the protocol packs.

## What's inside
The mdule provides support for the following services:
- Runtime support for all of the modules using combination of public and private APIs
  - Private APIs are exported to other modules and provide common implementation features across all modules
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

| Builtin | Layer2  | Layer3 | Layer4 | Layer7 |
|---------|---------|--------|--------|--------|
|Payload  |Ethernet |IPv4    |TCP     |DHCP    |
|Frame    |LLC/SNAP |IPv6    |UDP     |
|         |VLAN     |IPX     |SCTP    |
|         |PPP      |MPLS    |        |
|         |STP      |IGMP    |        |
|         |ARP/RARP |ICMP    |        |

  
