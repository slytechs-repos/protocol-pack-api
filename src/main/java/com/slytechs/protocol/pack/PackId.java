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
package com.slytechs.protocol.pack;

import java.util.Optional;

import com.slytechs.protocol.HeaderNotFound;
import com.slytechs.protocol.descriptor.CompactDescriptor;
import com.slytechs.protocol.runtime.NotFound;

/**
 * A unique 32-bit protocol ID or a 32-bit record and a box class. A protocol
 * pack ID is unique ID across all protocol packs and identifies specific
 * headers and protocol packs.
 * 
 * <p>
 * This class provides numerous static utility methods for encoding and decoding
 * a 4-bit pack-id and a 6-bit header/protocol id as well as bit-wise compatible
 * 32-bit records.
 * </p>
 * 
 * <h2>32-bit header 'ID' encoding</h2>
 * <p>
 * In unique ID use case, the header and pack ID are encoded in the 1st 12-bits.
 * The ID is a combination of a pack-ID (6-bits) and header-ID (6-bits). The
 * combination of pack-ID + header-ID provide a unique ID for each header across
 * all protocol packs.
 * </p>
 * 
 * <pre>
 * struct pack_id_s {
 * 	uint32_t
 * 		ordinal:6, // Index within the protocol pack
 * 		pack:4,    // Protocol pack unique number
 * 	    unused:22  // Unused bits in a unique ID use case
 * }
 * </pre>
 * 
 * <h2>32-bit 'record' encoding</h2>
 * <p>
 * In record use case, records are used to associate header offset and length
 * with a particular ID. The {@code Type2Descriptor} uses an array of 32-bit
 * records to record header offsets/length after packet dissection.
 * </p>
 * 
 * <p>
 * Masking off Id component from a record is easy using a simple bitwise AND
 * operation {@link PcakId#RECORD_MASK_UNPACK} constant such as
 * {@code record & RECORD_MASK_UNPACK} or use one of the utility methods
 * {@link PcapId#decodeRecordId(int)}.
 * </p>
 * 
 * <pre>
 * struct pack_record_s {
 * 	uint32_t
 * 		ordinal:6,  // Index within the protocol pack
 * 		pack:4,     // Protocol pack unique number
 * 		size:11,    // (Optional) Size of the protocol header (in units of 32-bits)
 * 		offset:11;  // (Optional) Offset into the packet (in units of 8-bit bytes)
 * }
 * </pre>
 * <p>
 * For an ID, offset and size fields are both set to 0.
 * </p>
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 *
 */
public interface PackId {

	// @formatter:off
	int PACK_MASK_ORDINAL  = 0x0000003F;  // 05:00 - 6 bits, protocol number
	int PACK_MASK_PACK     = 0x000003C0;  // 09:06 - 4 bits, protocol pack number
	int PACK_MASK_UNPACK   = 0x000003FF;  // 09:00 - Pack + Ordinal
	int RECORD_MASK_ORDINAL = 0x0000003F; // 05:00 - 6 bits, protocol number
	int RECORD_MASK_PACK    = 0x000003C0; // 09:06 - 4 bits, protocol pack number
	int RECORD_MASK_UNPACK  = 0x000003FF; // 09:00 - Pack + Ordinal
	int RECORD_MASK_SIZE    = 0x001FFC00; // 20:10 - 11 bits (in units of 32 bits)
	int RECORD_MASK_OFFSET  = 0xFFE00000; // 31:21 - 11 bits (in units of 8 bits)
	// @formatter:on

	// @formatter:off
	int PACK_SHIFT_ORDINAL  = 0;
	int PACK_SHIFT_PACK     = 6;
	int RECORD_SHIFT_ORDINAL = 0;
	int RECORD_SHIFT_PACK    = 6;
	int RECORD_SHIFT_SIZE    = 10;
	int RECORD_SHIFT_OFFSET  = 21;
	// @formatter:on

	static final int PACK_MAXCOUNT_PACKS = 16;
	static final int PACK_MAXCOUNT_ORDINALS = 64;

	/**
	 * Bitmask check.
	 *
	 * @param mask the mask
	 * @param id   the id
	 * @return true, if successful
	 */
	static boolean bitmaskCheck(int mask, int id) {
		return ((1 << decodeIdOrdinal(id)) & mask) != 0;
	}

	/**
	 * Bitmask set.
	 *
	 * @param mask the mask
	 * @param id   the id
	 * @return the int
	 */
	static int bitmaskSet(int mask, int id) {
		return (1 << decodeIdOrdinal(id)) | mask;
	}

	/**
	 * Decode id ordinal.
	 *
	 * @param id the id
	 * @return the int
	 */
	static int decodeIdOrdinal(int id) {
		return (id & PACK_MASK_ORDINAL) >> PACK_SHIFT_ORDINAL;
	}

	/**
	 * Decode pack id.
	 *
	 * @param id the id
	 * @return the int
	 */
	static int decodePackId(int id) {
		return (id & PACK_MASK_PACK);
	}

	/**
	 * Decode pack id.
	 *
	 * @param id the id
	 * @return the int
	 */
	static int decodeRecordId(int id) {
		return (id & PACK_MASK_UNPACK);
	}

	/**
	 * Decode pack ordinal.
	 *
	 * @param id the id
	 * @return the int
	 */
	static int decodePackOrdinal(int id) {
		return (id & PACK_MASK_PACK) >> PACK_SHIFT_PACK;
	}

	/**
	 * Decode record offset.
	 *
	 * @param record the record
	 * @return the int
	 */
	static int decodeRecordOffset(int record) {
		return (record & RECORD_MASK_OFFSET) >> RECORD_SHIFT_OFFSET;
	}

	/**
	 * Decode record ordinal.
	 *
	 * @param record the record
	 * @return the int
	 */
	static int decodeRecordOrdinal(int record) {
		return (record & RECORD_MASK_ORDINAL) >> RECORD_SHIFT_ORDINAL;
	}

	/**
	 * Decode record pack ordinal.
	 *
	 * @param record the record
	 * @return the int
	 */
	static int decodeRecordPackOrdinal(int record) {
		return (record & RECORD_MASK_PACK) >> RECORD_SHIFT_PACK;
	}

	/**
	 * Decode record size.
	 *
	 * @param record the record
	 * @return the int
	 */
	static int decodeRecordSize(int record) {
		return (record & RECORD_MASK_SIZE) >> RECORD_SHIFT_SIZE;
	}

	/**
	 * Encode id.
	 *
	 * @param packOrdinal the pack ordinal
	 * @param ordinal     the ordinal
	 * @return the int
	 */
	static int encodeId(int packOrdinal, int ordinal) {
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
	static int encodeId(ProtocolPackTable pack, int ordinal) {
		return PackId.encodeId(pack.ordinal(), ordinal);
	}

	/**
	 * Encode record.
	 *
	 * @param id     the id
	 * @param offset the offset
	 * @param size   the size
	 * @return the int
	 */
	static int encodeRecord(int id, int offset, int size) {
		assert (id & PACK_MASK_UNPACK) == id : "id has offset/length encoded";

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
	static int encodeRecordId(int record) {
		return record & PACK_MASK_UNPACK;
	}

	/**
	 * Encode record pack id.
	 *
	 * @param record the record
	 * @return the int
	 */
	static int encodeRecordPackId(int record) {
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
	static boolean recordEqualsId(int record, int id) {
		assert (id & PACK_MASK_UNPACK) == id : "id has offset/length encoded";

		return (record & PACK_MASK_UNPACK) == id;
	}

	/**
	 * Record equals pack.
	 *
	 * @param record the record
	 * @param pack   the pack
	 * @return true, if successful
	 */
	static boolean recordEqualsPack(int record, int pack) {
		pack <<= PACK_SHIFT_PACK;

		return (record & PACK_MASK_PACK) == pack;
	}

	/**
	 * Record to compact descriptor.
	 *
	 * @param record the record
	 * @return the long
	 */
	static long recordToCompactDescriptor(int record) {
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
	static long recordToCompactDescriptor(int record, int id, int recordIndex) {
		int offset = decodeRecordOffset(record);
		int length = decodeRecordSize(record);

		return CompactDescriptor.encode(id, offset, length, recordIndex);
	}

	static PackId get(int id) throws NotFound {
		PackId pid = peek(id);
		if (pid == null)
			throw new NotFound("0x04X".formatted(id));

		return pid;
	}

	static PackId peek(int id) {
		int packComponent = decodePackId(id);
		Pack<?> pack = Pack.getLoadedPack(packComponent);
		if (pack == null)
			return null;

		try {
			var hdr = pack.getHeader(id);
			if (hdr instanceof PackId packId)
				return packId;

			return null;
		} catch (HeaderNotFound e) {
			throw new IllegalStateException("Missing header in pack [%d]"
					.formatted(id));
		}
	}

	static Optional<PackId> find(int id) {
		return Optional.ofNullable(peek(id));
	}

	static String toString(int id) {
		return find(id)
				.map(PackId::toString)
				.orElse("0x04X".formatted(id));
	}

	/**
	 * Decode 32-bit pack id from the full id.
	 *
	 * @return pack only id
	 */
	default int packId() {
		return decodePackId(id());
	}

	/**
	 * Decode 32-bit pack ordinal or zero-based index.
	 *
	 * @return the pack ordinal index
	 */
	default int packOrdinal() {
		return decodePackOrdinal(id());
	}

	/**
	 * Decode 32-bit id ordinal or index.
	 *
	 * @return the ordinal index
	 */
	default int idOrdinal() {
		return decodeIdOrdinal(id());
	}

	/**
	 * Decode offset in bytes if id is a record.
	 *
	 * @return offset in units of bytes
	 */
	default int offset() {
		return decodeRecordOffset(id());
	}

	/**
	 * Decode size in bytes if id is a record.
	 *
	 * @return size in units of bytes
	 */
	default int size() {
		return decodeRecordSize(id());
	}

	/**
	 * Gets a 32-bit pack id for this object.
	 *
	 * @return the id
	 */
	int id();
}
