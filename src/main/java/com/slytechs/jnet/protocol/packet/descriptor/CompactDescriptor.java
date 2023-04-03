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
package com.slytechs.jnet.protocol.packet.descriptor;

import static com.slytechs.jnet.runtime.internal.layout.BinaryLayout.*;

import com.slytechs.jnet.protocol.constants.CoreHeaderInfo;
import com.slytechs.jnet.runtime.internal.layout.BinaryLayout;
import com.slytechs.jnet.runtime.internal.layout.PredefinedLayout.Int64;

/**
 * A compact descriptor that encodes a 32-bit ID, a 16-bit offset and 16-length
 * fields into a single 64-bit (8-byte) value. Typical usage is to encode in
 * some kind descriptors, a table made up of 64-bit entries, a list of headers
 * and offsets into a packet.
 */
public interface CompactDescriptor {

	@FunctionalInterface
	public interface DecodeConsumer {
		void accept(int id, int offset, int length);
	}

	@FunctionalInterface
	public interface EncodeConsumer {
		void accept(long compact);
	}

	public static final long ID_NOT_FOUND = -1L;

	public static final long COMPACT_ID_MASK = 0x0000_0000_0000FFFFL;
	public static final long COMPACT_META_MASK = 0x0000_0000_FFFF0000L;
	public static final long COMPACT_OFF_MASK = 0x0000_FFFF_00000000L;
	public static final long COMPACT_LEN_MASK = 0xFFFF_0000_00000000L;

	public static final int COMPACT_ID_SHIFT = 0;
	public static final int COMPACT_META_SHIFT = 16;
	public static final int COMPACT_OFF_SHIFT = 32;
	public static final int COMPACT_LEN_SHIFT = 48;

	public static final BinaryLayout COMPACT_LAYOUT = structLayout(
			Int64.BITS_16.withName("length"),
			Int64.BITS_16.withName("offset"),
			Int64.BITS_32.withName("id"));

	public static void decode(CompactDescriptor compact, DecodeConsumer consumer) {
		consumer.accept(compact.id(), compact.offset(), compact.length());
	}

	public static void decode(long compact, DecodeConsumer consumer) {
		consumer.accept(decodeId(compact), decodeOffset(compact), decodeLength(compact));
	}

	public static int decodeId(long compact) {
		return (int) ((compact & COMPACT_ID_MASK) >> COMPACT_ID_SHIFT);
	}

	public static int decodeLength(long compact) {
		return (int) ((compact & COMPACT_LEN_MASK) >> COMPACT_LEN_SHIFT);
	}

	public static int decodeMeta(long compact) {
		return (int) ((compact & COMPACT_META_MASK) >> COMPACT_META_SHIFT);
	}

	public static int decodeOffset(long compact) {
		return (int) ((compact & COMPACT_OFF_MASK) >> COMPACT_OFF_SHIFT);
	}

	public static long encode(int id, int offset, int length) {
		return 0l
				| (((long) id << COMPACT_ID_SHIFT) & COMPACT_ID_MASK)
				| (((long) offset << COMPACT_OFF_SHIFT) & COMPACT_OFF_MASK)
				| (((long) length << COMPACT_LEN_SHIFT) & COMPACT_LEN_MASK);
	}

	public static void encode(int id, int offset, int length, EncodeConsumer consumer) {
		consumer.accept(encode(id, offset, length));
	}

	public static long encode(int id, int offset, int length, int meta) {
		return 0l
				| (((long) id << COMPACT_ID_SHIFT) & COMPACT_ID_MASK)
				| (((long) meta << COMPACT_META_SHIFT) & COMPACT_META_MASK)
				| (((long) offset << COMPACT_OFF_SHIFT) & COMPACT_OFF_MASK)
				| (((long) length << COMPACT_LEN_SHIFT) & COMPACT_LEN_MASK);
	}

	public static int indexOfInTable(int id, long[] table, int limit) {

		for (int i = 0; i < limit; i++) {
			long ent = (table[i] & CompactDescriptor.COMPACT_ID_MASK);
			if (ent == id)
				return i;
		}

		return -1;
	}

	public static long lookupIdInTable(int id, long[] table, int limit) {

		for (int i = 0; i < limit; i++) {
			long ent = (table[i] & CompactDescriptor.COMPACT_ID_MASK);
			if (ent == id)
				return table[i];
		}

		return CompactDescriptor.ID_NOT_FOUND;
	}

	public static CompactDescriptor of(int id, int offset, int length) {
		final long compact = encode(id, offset, length);
		return new CompactDescriptor() {

			@Override
			public long compact() {
				return compact;
			}

			@Override
			public int id() {
				return id;
			}

			@Override
			public int length() {
				return length;
			}

			@Override
			public int offset() {
				return offset;
			}

			@Override
			public String toString() {
				return String.format("%08x off=%d len=%d", id, offset, length);
			}
		};
	}

	public static CompactDescriptor of(long compact) {
		int id = decodeId(compact);
		int offset = decodeOffset(compact);
		int length = decodeLength(compact);

		return new CompactDescriptor() {

			@Override
			public long compact() {
				return compact;
			}

			@Override
			public int id() {
				return id;
			}

			@Override
			public int length() {
				return length;
			}

			@Override
			public int offset() {
				return offset;
			}

		};
	}

	public static String toString(long encoded) {
		return "CompactDescriptor"
				+ "[id=" + String.format("%s", CoreHeaderInfo.toStringId(decodeId(encoded)))
				+ " off=" + decodeOffset(encoded)
				+ " len=" + decodeLength(encoded)
//				+ " compact=" + String.format("0x%016X", encoded)
				+ "]";
	}

	long compact();

	int id();

	int length();

	int offset();
}
