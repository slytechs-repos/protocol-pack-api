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

import com.slytechs.protocol.meta.Meta;
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
public final class Icmp6Mlr2 extends Icmp6 implements Iterable<Icmp6Mlr2.MulticastAddressRecord> {
	private static final int ICMPv6_MLRv2_HEADER_LEN = 8;
	private static final int RECORD_START_OFFSET = ICMPv6_MLRv2_HEADER_LEN;

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

		return new MulticastAddressRecord(recordType, Icmp6Mlr2RecordType.resolve(recordType), auxDataLen, numberOfSources, multicastAddress);
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
}
