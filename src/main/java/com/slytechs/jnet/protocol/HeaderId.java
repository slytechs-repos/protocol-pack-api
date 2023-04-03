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
package com.slytechs.jnet.protocol;

import com.slytechs.jnet.protocol.constants.PackInfo;
import com.slytechs.jnet.protocol.packet.descriptor.CompactDescriptor;

/**
 * Various protocol and protocol ID encoding utilities.
 * 
 * <h2>32-bit protocol 'ID' and 'record' encoding</h2>
 * <p>
 * Here is the 32-bit encoding of a protocol ID, where offset/size are optional.
 * They are used by certain descriptors as records, to encode protocol
 * information along with size and offset.
 * </p>
 * 
 * <pre>
 * struct {
 * 	uint32_t
 * 		ordinal:6,
 * 		pack:4,
 * 		offset:11,
 * 		size:11;
 * }
 * </pre>
 * <p>
 * For an ID, offset and size fields are both set to 0.
 * </p>
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public final class HeaderId {

	// @formatter:off
	public static final int PROTO_MASK_ORDINAL  = 0x0000003F;  // 05:00 - 6 bits, protocol number
	public static final int PROTO_MASK_PACK     = 0x000003C0;  // 09:06 - 4 bits, protocol pack number
	public static final int PROTO_MASK_UNPACK   = 0x000003FF;  // 09:00 - Pack + Ordinal

	public static final int RECORD_MASK_ORDINAL = 0x0000003F; // 05:00 - 6 bits, protocol number
	public static final int RECORD_MASK_PACK    = 0x000003C0; // 09:06 - 4 bits, protocol pack number
	public static final int RECORD_MASK_UNPACK  = 0x000003FF; // 09:00 - Pack + Ordinal
	public static final int RECORD_MASK_SIZE    = 0x001FFC00; // 20:10 - 11 bits (in units of 32 bits)
	public static final int RECORD_MASK_OFFSET  = 0xFFE00000; // 31:21 - 11 bits (in units of 8 bits)
	// @formatter:on

	// @formatter:off
	public static final int PROTO_SHIFT_ORDINAL  = 0;
	public static final int PROTO_SHIFT_PACK     = 6;

	public static final int RECORD_SHIFT_ORDINAL = 0;
	public static final int RECORD_SHIFT_PACK    = 6;
	public static final int RECORD_SHIFT_SIZE    = 10;
	public static final int RECORD_SHIFT_OFFSET  = 21;
	// @formatter:on

	public static final int PROTO_MAX_PACKS = 16;
	public static final int PROTO_MAX_ORDINALS = 64;

	public static boolean bitmaskCheck(int mask, int id) {
		return ((1 << decodeIdOrdinal(id)) & mask) != 0;
	}

	public static int bitmaskSet(int mask, int id) {
		return (1 << decodeIdOrdinal(id)) | mask;
	}

	public static int decodeIdOrdinal(int id) {
		return (id & PROTO_MASK_ORDINAL) >> PROTO_SHIFT_ORDINAL;
	}

	public static int decodePackId(int id) {
		return (id & PROTO_MASK_PACK);
	}

	public static int decodePackOrdinal(int id) {
		return (id & PROTO_MASK_PACK) >> PROTO_SHIFT_PACK;
	}

	public static int decodeRecordOffset(int record) {
		return (record & RECORD_MASK_OFFSET) >> RECORD_SHIFT_OFFSET;
	}

	public static int decodeRecordOrdinal(int record) {
		return (record & RECORD_MASK_ORDINAL) >> RECORD_SHIFT_ORDINAL;
	}

	public static int decodeRecordPackOrdinal(int record) {
		return (record & RECORD_MASK_PACK) >> RECORD_SHIFT_PACK;
	}

	public static int decodeRecordSize(int record) {
		return (record & RECORD_MASK_SIZE) >> RECORD_SHIFT_SIZE;
	}

	public static int encodeId(int packOrdinal, int ordinal) {
		return ((ordinal << RECORD_SHIFT_ORDINAL) & RECORD_MASK_ORDINAL) |
				((packOrdinal << RECORD_SHIFT_PACK) & RECORD_MASK_PACK);
	}

	public static int encodeId(PackInfo pack, int ordinal) {
		return HeaderId.encodeId(pack.ordinal(), ordinal);
	}

	public static int encodeRecord(int id, int offset, int size) {
		assert (id & PROTO_MASK_UNPACK) == id : "id has offset/length encoded";

		return id |
				((offset << RECORD_SHIFT_OFFSET) & RECORD_MASK_OFFSET) |
				((size << RECORD_SHIFT_SIZE) & RECORD_MASK_SIZE);
	}

	public static int encodeRecordId(int record) {
		return record & PROTO_MASK_UNPACK;
	}

	public static int encodeRecordPackId(int record) {
		return (record & RECORD_MASK_PACK);
	}

	/**
	 * Compares the protocol ID portions of id and encoded parameters to be equal.
	 * 
	 * @param record id which has offset/length encoded
	 * @param id     clean protocol id which does not have offset/length encoded
	 *
	 * @return true, if protocol IDs of both id and encoded are equals
	 */
	public static boolean recordEqualsId(int record, int id) {
		assert (id & PROTO_MASK_UNPACK) == id : "id has offset/length encoded";

		return (id & record) == id;
	}

	public static boolean recordEqualsPack(int record, int pack) {
		pack <<= PROTO_SHIFT_PACK;

		return (pack & record) == pack;
	}

	public static long recordToCompactDescriptor(int record) {
		int offset = decodeRecordOffset(record);
		int length = decodeRecordSize(record);
		int id = encodeRecordId(record);

		return CompactDescriptor.encode(id, offset, length);
	}

	public static long recordToCompactDescriptor(int record, int id, int recordIndex) {
		int offset = decodeRecordOffset(record);
		int length = decodeRecordSize(record);

		return CompactDescriptor.encode(id, offset, length, recordIndex);
	}

	private HeaderId() {
	}

}
