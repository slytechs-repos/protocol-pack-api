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

import java.util.Iterator;

import com.slytechs.protocol.HasExtension;
import com.slytechs.protocol.Header;
import com.slytechs.protocol.HeaderNotFound;
import com.slytechs.protocol.meta.Meta;
import com.slytechs.protocol.meta.Meta.MetaType;
import com.slytechs.protocol.meta.MetaResource;
import com.slytechs.protocol.pack.core.constants.CoreId;
import com.slytechs.protocol.pack.core.constants.Icmp6Mlr2RecordType;

/**
 * ICMPv6 Multicast Listener Report Message v2
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
@MetaResource("icmp6-mlr2-meta.json")
public final class Icmp6Mlr2
		extends Icmp6
		implements HasExtension<Icmp6Mlr2.Mlr2Record>, Iterable<Icmp6Mlr2.MulticastAddressRecord> {
	private static final int ICMPv6_MLRv2_HEADER_LEN = 8;
	private static final int RECORD_START_OFFSET = ICMPv6_MLRv2_HEADER_LEN;

	@MetaResource("icmp6-mlr2-record-meta.json")
	public static class Mlr2Record extends Header {

		public static final int ID = Icmp6Mlr2RecordType.ICMPv6_ID_OPT_MLRv2_CHANGE_TO_INCLUDE;

		protected Mlr2Record(int id) {
			super(id);
		}

		public Mlr2Record() {
			super(ID);
		}

		@Meta
		public int recordType() {
			return Byte.toUnsignedInt(buffer().get(0));
		}

		@Meta
		public int auxDataLen() {
			return Byte.toUnsignedInt(buffer().get(1));
		}

		@Meta(MetaType.ATTRIBUTE)
		public int auxDataLenBytes() {
			return auxDataLen() * 4;
		}

		@Meta
		public int numOfSources() {
			return Short.toUnsignedInt(buffer().getShort(2));
		}

		@Meta
		public Ip6Address multicastAddress() {
			return Ip6Address.get(4, buffer());
		}
	}

	@MetaResource("icmp6-mlr2-change-in-meta.json")
	public static class Mlr2ChangeInRecord extends Mlr2Record {
		public static final int ID = Icmp6Mlr2RecordType.ICMPv6_ID_OPT_MLRv2_CHANGE_TO_INCLUDE;

		public Mlr2ChangeInRecord() {
			super(ID);
		}
	}

	@MetaResource("icmp6-mlr2-change-ex-meta.json")
	public static class Mlr2ChangeExRecord extends Mlr2Record {
		public static final int ID = Icmp6Mlr2RecordType.ICMPv6_ID_OPT_MLRv2_CHANGE_TO_EXCLUDE;

		public Mlr2ChangeExRecord() {
			super(ID);
		}
	}

	@MetaResource("icmp6-mlr2-source-address-meta.json")
	public static class Mlr2SourceAddress extends Header {
		public static final int ID = Icmp6Mlr2RecordType.ICMPv6_ID_OPT_MLRv2_ADDRESS;

		public Mlr2SourceAddress() {
			super(ID);
		}

		@Meta
		public Ip6Address sourceAddress() {
			return Ip6Address.get(0, buffer());
		}
	}

	public record MulticastAddressRecord(int recordType, String recordTypeDesc, int auxDataLen, int numberOfSources,
			Ip6Address multicastAddress) {

		/**
		 * 
		 */

		public int byteSize() {
			return ICMPv6_MLRv2_HEADER_LEN
					+ IpAddress.IPv6_ADDRESS_SIZE
					+ (numberOfSources * IpAddress.IPv6_ADDRESS_SIZE)
					+ (auxDataLen * 4);
		}
	}

	/** The Constant ID. */
	public static final int ID = CoreId.CORE_ID_ICMPv6_MULTICAST_LISTENER_REPORTv2;

	/**
	 * New ICMPv6 Multicast Listener Report Message v2.
	 */
	public Icmp6Mlr2() {
		super(ID);
	}

	@Meta
	public int reserved() {
		return Short.toUnsignedInt(buffer().getShort(4));
	}

	@Meta
	public int numberOfRecords() {
		return Short.toUnsignedInt(buffer().getShort(6));
	}

	@Meta
	public MulticastAddressRecord record() {
		return recordAtOffset(RECORD_START_OFFSET);
	}

	public MulticastAddressRecord recordAtOffset(int offset) {
		int recordType = Byte.toUnsignedInt(buffer().get(offset + 0));
		int auxDataLen = Byte.toUnsignedInt(buffer().get(offset + 1));
		int numberOfSources = Short.toUnsignedInt(buffer().getShort(offset + 2));
		Ip6Address multicastAddress = Ip6Address.get(offset + 4, buffer());

		return new MulticastAddressRecord(recordType, Icmp6Mlr2RecordType.resolve(recordType), auxDataLen,
				numberOfSources, multicastAddress);
	}

	/**
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<MulticastAddressRecord> iterator() {
		return new Iterator<Icmp6Mlr2.MulticastAddressRecord>() {
			final int num = numberOfRecords();
			int index = 0;
			int offset = RECORD_START_OFFSET;

			@Override
			public boolean hasNext() {
				return index < num;
			}

			@Override
			public MulticastAddressRecord next() {
				MulticastAddressRecord record = recordAtOffset(offset);
				offset += record.byteSize();

				return record;
			}
		};
	}

	/**
	 * @see com.slytechs.protocol.HasExtension#getExtension(com.slytechs.protocol.Header,
	 *      int)
	 */
	@Override
	public <E extends Mlr2Record> E getExtension(E extension, int depth) throws HeaderNotFound {
		return super.getExtensionHeader(extension, depth);
	}

	/**
	 * @see com.slytechs.protocol.HasExtension#hasExtension(int, int)
	 */
	@Override
	public boolean hasExtension(int extensionId, int depth) {
		return super.hasExtensionHeader(extensionId, depth);
	}

	/**
	 * @see com.slytechs.protocol.HasExtension#peekExtension(com.slytechs.protocol.Header,
	 *      int)
	 */
	@Override
	public <E extends Mlr2Record> E peekExtension(E extension, int depth) {
		return super.peekExtensionHeader(extension, depth);
	}
}
