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

import com.slytechs.jnet.protocol.core.constants.PackInfo;
import com.slytechs.jnet.protocol.descriptor.CompactDescriptor;

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

	/** The Constant PROTO_MASK_ORDINAL. */
	// @formatter:off
	public static final int PROTO_MASK_ORDINAL  = 0x0000003F;  // 05:00 - 6 bits, protocol number
	
	/** The Constant PROTO_MASK_PACK. */
	public static final int PROTO_MASK_PACK     = 0x000003C0;  // 09:06 - 4 bits, protocol pack number
	
	/** The Constant PROTO_MASK_UNPACK. */
	public static final int PROTO_MASK_UNPACK   = 0x000003FF;  // 09:00 - Pack + Ordinal

	/** The Constant RECORD_MASK_ORDINAL. */
	public static final int RECORD_MASK_ORDINAL = 0x0000003F; // 05:00 - 6 bits, protocol number
	
	/** The Constant RECORD_MASK_PACK. */
	public static final int RECORD_MASK_PACK    = 0x000003C0; // 09:06 - 4 bits, protocol pack number
	
	/** The Constant RECORD_MASK_UNPACK. */
	public static final int RECORD_MASK_UNPACK  = 0x000003FF; // 09:00 - Pack + Ordinal
	
	/** The Constant RECORD_MASK_SIZE. */
	public static final int RECORD_MASK_SIZE    = 0x001FFC00; // 20:10 - 11 bits (in units of 32 bits)
	
	/** The Constant RECORD_MASK_OFFSET. */
	public static final int RECORD_MASK_OFFSET  = 0xFFE00000; // 31:21 - 11 bits (in units of 8 bits)
	// @formatter:on

	/** The Constant PROTO_SHIFT_ORDINAL. */
	// @formatter:off
	public static final int PROTO_SHIFT_ORDINAL  = 0;
	
	/** The Constant PROTO_SHIFT_PACK. */
	public static final int PROTO_SHIFT_PACK     = 6;

	/** The Constant RECORD_SHIFT_ORDINAL. */
	public static final int RECORD_SHIFT_ORDINAL = 0;
	
	/** The Constant RECORD_SHIFT_PACK. */
	public static final int RECORD_SHIFT_PACK    = 6;
	
	/** The Constant RECORD_SHIFT_SIZE. */
	public static final int RECORD_SHIFT_SIZE    = 10;
	
	/** The Constant RECORD_SHIFT_OFFSET. */
	public static final int RECORD_SHIFT_OFFSET  = 21;
	// @formatter:on

	/** The Constant PROTO_MAX_PACKS. */
	public static final int PROTO_MAX_PACKS = 16;
	
	/** The Constant PROTO_MAX_ORDINALS. */
	public static final int PROTO_MAX_ORDINALS = 64;

	/**
	 * Bitmask check.
	 *
	 * @param mask the mask
	 * @param id   the id
	 * @return true, if successful
	 */
	public static boolean bitmaskCheck(int mask, int id) {
		return ((1 << decodeIdOrdinal(id)) & mask) != 0;
	}

	/**
	 * Bitmask set.
	 *
	 * @param mask the mask
	 * @param id   the id
	 * @return the int
	 */
	public static int bitmaskSet(int mask, int id) {
		return (1 << decodeIdOrdinal(id)) | mask;
	}

	/**
	 * Decode id ordinal.
	 *
	 * @param id the id
	 * @return the int
	 */
	public static int decodeIdOrdinal(int id) {
		return (id & PROTO_MASK_ORDINAL) >> PROTO_SHIFT_ORDINAL;
	}

	/**
	 * Decode pack id.
	 *
	 * @param id the id
	 * @return the int
	 */
	public static int decodePackId(int id) {
		return (id & PROTO_MASK_PACK);
	}

	/**
	 * Decode pack ordinal.
	 *
	 * @param id the id
	 * @return the int
	 */
	public static int decodePackOrdinal(int id) {
		return (id & PROTO_MASK_PACK) >> PROTO_SHIFT_PACK;
	}

	/**
	 * Decode record offset.
	 *
	 * @param record the record
	 * @return the int
	 */
	public static int decodeRecordOffset(int record) {
		return (record & RECORD_MASK_OFFSET) >> RECORD_SHIFT_OFFSET;
	}

	/**
	 * Decode record ordinal.
	 *
	 * @param record the record
	 * @return the int
	 */
	public static int decodeRecordOrdinal(int record) {
		return (record & RECORD_MASK_ORDINAL) >> RECORD_SHIFT_ORDINAL;
	}

	/**
	 * Decode record pack ordinal.
	 *
	 * @param record the record
	 * @return the int
	 */
	public static int decodeRecordPackOrdinal(int record) {
		return (record & RECORD_MASK_PACK) >> RECORD_SHIFT_PACK;
	}

	/**
	 * Decode record size.
	 *
	 * @param record the record
	 * @return the int
	 */
	public static int decodeRecordSize(int record) {
		return (record & RECORD_MASK_SIZE) >> RECORD_SHIFT_SIZE;
	}

	/**
	 * Encode id.
	 *
	 * @param packOrdinal the pack ordinal
	 * @param ordinal     the ordinal
	 * @return the int
	 */
	public static int encodeId(int packOrdinal, int ordinal) {
		return ((ordinal << RECORD_SHIFT_ORDINAL) & RECORD_MASK_ORDINAL) |
				((packOrdinal << RECORD_SHIFT_PACK) & RECORD_MASK_PACK);
	}

	/**
	 * Encode id.
	 *
	 * @param pack    the pack
	 * @param ordinal the ordinal
	 * @return the int
	 */
	public static int encodeId(PackInfo pack, int ordinal) {
		return HeaderId.encodeId(pack.ordinal(), ordinal);
	}

	/**
	 * Encode record.
	 *
	 * @param id     the id
	 * @param offset the offset
	 * @param size   the size
	 * @return the int
	 */
	public static int encodeRecord(int id, int offset, int size) {
		assert (id & PROTO_MASK_UNPACK) == id : "id has offset/length encoded";

		return id |
				((offset << RECORD_SHIFT_OFFSET) & RECORD_MASK_OFFSET) |
				((size << RECORD_SHIFT_SIZE) & RECORD_MASK_SIZE);
	}

	/**
	 * Encode record id.
	 *
	 * @param record the record
	 * @return the int
	 */
	public static int encodeRecordId(int record) {
		return record & PROTO_MASK_UNPACK;
	}

	/**
	 * Encode record pack id.
	 *
	 * @param record the record
	 * @return the int
	 */
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

	/**
	 * Record equals pack.
	 *
	 * @param record the record
	 * @param pack   the pack
	 * @return true, if successful
	 */
	public static boolean recordEqualsPack(int record, int pack) {
		pack <<= PROTO_SHIFT_PACK;

		return (pack & record) == pack;
	}

	/**
	 * Record to compact descriptor.
	 *
	 * @param record the record
	 * @return the long
	 */
	public static long recordToCompactDescriptor(int record) {
		int offset = decodeRecordOffset(record);
		int length = decodeRecordSize(record);
		int id = encodeRecordId(record);

		return CompactDescriptor.encode(id, offset, length);
	}

	/**
	 * Record to compact descriptor.
	 *
	 * @param record      the record
	 * @param id          the id
	 * @param recordIndex the record index
	 * @return the long
	 */
	public static long recordToCompactDescriptor(int record, int id, int recordIndex) {
		int offset = decodeRecordOffset(record);
		int length = decodeRecordSize(record);

		return CompactDescriptor.encode(id, offset, length, recordIndex);
	}

	/**
	 * Instantiates a new header id.
	 */
	private HeaderId() {
	}

}
