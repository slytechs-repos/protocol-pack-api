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
package com.slytechs.protocol.runtime.internal.util;

import java.lang.foreign.MemoryAddress;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.MemorySession;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import com.slytechs.protocol.runtime.MemoryBinding;
import com.slytechs.protocol.runtime.NotFound;
import com.slytechs.protocol.runtime.internal.util.Attribute.Builtin;
import com.slytechs.protocol.runtime.internal.util.Attribute.Value;

/**
 * An attribute buffer which stores attribute values.
 * 
 * The buffer's layout is always as follows:
 * 
 * <pre>
 * Buffer        ::= RecordCount Record ...
 * RecordCount   ::= Record
 * Record        ::= AttributeId Length Value
 * AttributeId   ::= <16-bit int>
 * Length        ::= <16-bit int>
 * Value         ::= Boolean | Number | Array | String
 * Number        ::= <8-bit int> | 
 *                   <16-bit int> | 
 *                   <32-bit int> | 
 *                   <64-bit int> | 
 *                   <32-bit float> | 
 *                   <64-bit double>
 * Boolean       ::= <8-bit int>
 * Array         ::= <8-bit byte> ...
 * String        ::= <8-bit char> ... '0'
 * </pre>
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public class AttributeBuffer extends MemoryBinding implements Iterable<Attribute.Value> {

	/**
	 * The Class Record.
	 */
	private class Record extends MemoryBinding implements Value {

		/** 2-bytes ID + 2-bytes record length. */
		private static int RECORD_HEADER_LENGTH = 4;

		/** The id. */
		int id;
		
		/** The record length. */
		int recordLength;
		
		/** The record offset. */
		int recordOffset;

		/**
		 * Adds the int.
		 *
		 * @param delta the delta
		 * @return the int
		 * @see com.slytechs.protocol.runtime.internal.util.Attribute.Value#addInt(int)
		 */
		@Override
		public int addInt(int delta) {
			int oldValue = getInt();

			return setInt(oldValue + delta);
		}

		/**
		 * Gets the boolean.
		 *
		 * @return the boolean
		 * @see com.slytechs.protocol.runtime.internal.util.Attribute.Value#getBoolean()
		 */
		@Override
		public boolean getBoolean() {
			return buffer().get(RECORD_HEADER_LENGTH) != 0;
		}

		/**
		 * Gets the byte array.
		 *
		 * @return the byte array
		 * @see com.slytechs.protocol.runtime.internal.util.Attribute.Value#getByteArray()
		 */
		@Override
		public byte[] getByteArray() {
			int len = valueLength();
			byte[] ba = new byte[len];

			getValueBuffer()
					.get(ba);

			return ba;
		}

		/**
		 * Gets the double.
		 *
		 * @return the double
		 * @see com.slytechs.protocol.runtime.internal.util.Attribute.Value#getDouble()
		 */
		@Override
		public double getDouble() {
			return getValueBuffer().getDouble();
		}

		/**
		 * Gets the float.
		 *
		 * @return the float
		 * @see com.slytechs.protocol.runtime.internal.util.Attribute.Value#getFloat()
		 */
		@Override
		public float getFloat() {
			return getValueBuffer().getFloat();
		}

		/**
		 * Gets the int.
		 *
		 * @return the int
		 * @see com.slytechs.protocol.runtime.internal.util.Attribute.Value#getInt()
		 */
		@Override
		public int getInt() {
			return getValueBuffer().getInt();
		}

		/**
		 * Gets the long.
		 *
		 * @return the long
		 * @see com.slytechs.protocol.runtime.internal.util.Attribute.Value#getLong()
		 */
		@Override
		public long getLong() {
			return getValueBuffer().getLong();
		}

		/**
		 * Gets the memory segment.
		 *
		 * @param scope the scope
		 * @return the memory segment
		 * @see com.slytechs.protocol.runtime.internal.util.Attribute.Value#getMemorySegment(java.lang.foreign.MemorySession)
		 */
		@Override
		public MemorySegment getMemorySegment(MemorySession scope) {
			ByteBuffer b = getValueBuffer();
			long offset = b.getLong();
			long byteSize = b.getLong();
			MemoryAddress ptr = MemoryAddress.ofLong(offset);

			return MemorySegment.ofAddress(ptr, byteSize, scope);
		}

		/**
		 * Gets the short.
		 *
		 * @return the short
		 * @see com.slytechs.protocol.runtime.internal.util.Attribute.Value#getShort()
		 */
		@Override
		public short getShort() {
			return getValueBuffer().getShort();
		}

		/**
		 * Gets the string.
		 *
		 * @return the string
		 * @see com.slytechs.protocol.runtime.internal.util.Attribute.Value#getString()
		 */
		@Override
		public String getString() {

			byte[] bytes = getByteArray();
			int len = bytes.length - 1; // Drop the '\0' at the end

			return new String(bytes, 0, len, StandardCharsets.UTF_8);
		}

		/**
		 * Gets the value buffer.
		 *
		 * @return the value buffer
		 */
		private ByteBuffer getValueBuffer() {
			return buffer()
					.position(RECORD_HEADER_LENGTH);
		}

		/**
		 * Id.
		 *
		 * @return the int
		 * @see com.slytechs.protocol.runtime.internal.util.Attribute.Value#id()
		 */
		@Override
		public int id() {
			return id;
		}

		/**
		 * Inc int.
		 *
		 * @return the int
		 * @see com.slytechs.protocol.runtime.internal.util.Attribute.Value#incInt()
		 */
		@Override
		public int incInt() {
			return addInt(1);
		}

		/**
		 * Offset.
		 *
		 * @return the int
		 * @see com.slytechs.protocol.runtime.internal.util.Attribute.Value#offset()
		 */
		@Override
		public int offset() {
			return recordOffset;
		}

		/**
		 * Sets the int.
		 *
		 * @param newValue the new value
		 * @return the int
		 * @see com.slytechs.protocol.runtime.internal.util.Attribute.Value#setInt(int)
		 */
		@Override
		public int setInt(int newValue) {
			buffer().putInt(RECORD_HEADER_LENGTH, newValue);

			return newValue;
		}

		/**
		 * Value buffer.
		 *
		 * @return the byte buffer
		 * @see com.slytechs.protocol.runtime.internal.util.Attribute.Value#valueBuffer()
		 */
		@Override
		public ByteBuffer valueBuffer() {
			ByteBuffer recordBuffer = buffer();
			ByteBuffer valueBuffer = recordBuffer
					.position(RECORD_HEADER_LENGTH)
					.slice();

			recordBuffer.clear();

			return valueBuffer;
		}

		/**
		 * Value length.
		 *
		 * @return the int
		 * @see com.slytechs.protocol.runtime.internal.util.Attribute.Value#valueLength()
		 */
		@Override
		public int valueLength() {
			return recordLength - RECORD_HEADER_LENGTH;
		}

	}

	/**
	 * Write record header at limit.
	 *
	 * @param b           the b
	 * @param attr        the attr
	 * @param valueLength the value length
	 * @return the byte buffer
	 */
	private static ByteBuffer writeRecordHeaderAtLimit(ByteBuffer b, Attribute attr, int valueLength) {
		int len = (valueLength + Record.RECORD_HEADER_LENGTH);
		int l = b.limit();
		b.position(l)
				.limit(l + len)
				.putInt(attr.id())
				.putInt(len);

		return b;
	}

	/** The record count. */
	private Record recordCount;

	/**
	 * Instantiates a new attribute buffer.
	 */
	public AttributeBuffer() {
	}

	/**
	 * Instantiates a new attribute buffer.
	 *
	 * @param capacity the capacity
	 */
	public AttributeBuffer(int capacity) {
		ByteBuffer b = ByteBuffer.allocateDirect(capacity);

		writeRecordHeaderAtLimit(b.flip(), Builtin.RECORD_COUNT, 4)
				.clear();

		super.bind(b);
	}

	/**
	 * Contains.
	 *
	 * @param attr the attr
	 * @return true, if successful
	 */
	public boolean contains(Attribute attr) {
		return findOffset(attr) != -1;
	}

	/**
	 * Find.
	 *
	 * @param attr the attr
	 * @return the optional
	 */
	public Optional<Value> find(Attribute attr) {
		int offset = findOffset(attr);
		if (offset == -1)
			return Optional.empty();

		return Optional.of(readRecord(offset));
	}

	/**
	 * Find offset.
	 *
	 * @param attr the attr
	 * @return the int
	 */
	private int findOffset(Attribute attr) {
		int count = recordCount.getInt();
		int next = recordCount.recordLength;
		int targetId = attr.id();

		ByteBuffer b = buffer();

		while (count > 0) {
			short id = b.getShort(next + 0);
			short len = b.getShort(next + 2);

			if (id == targetId)
				return next;

			next += len;
			count--;
		}

		return -1;
	}

	/**
	 * Iterator.
	 *
	 * @return the iterator
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Value> iterator() {
		return new Iterator<Attribute.Value>() {

			int count = recordCount.getInt();
			int next = recordCount.recordLength;

			@Override
			public boolean hasNext() {
				return count > 0;
			}

			@Override
			public Value next() {
				Record record = readRecord(next);

				next += record.recordLength;
				count--;

				return record;
			}
		};
	}

	/**
	 * Limit.
	 *
	 * @return the int
	 * @see com.slytechs.jnet.runtime.util.Attribute.Attributes#limit()
	 */
	public int limit() {
		return buffer().limit();
	}

	/**
	 * List.
	 *
	 * @return the list
	 * @see com.slytechs.jnet.runtime.util.Attribute.Attributes#list()
	 */
	public List<Value> list() {
		int recordCount = this.recordCount.getInt();
		if (recordCount == 0)
			return Collections.emptyList();

		List<Value> list = new ArrayList<>(recordCount);

		for (Value value : this)
			list.add(value);

		return list;
	}

	/**
	 * On bind.
	 *
	 * @see com.slytechs.protocol.runtime.MemoryBinding#onBind()
	 */
	@Override
	protected void onBind() {
		this.recordCount = readRecord(0, Builtin.RECORD_COUNT);
	}

	/**
	 * On unbind.
	 *
	 * @see com.slytechs.protocol.runtime.MemoryBinding#onUnbind()
	 */
	@Override
	protected void onUnbind() {
		recordCount = null;
	}

	/**
	 * Read.
	 *
	 * @param attr the attr
	 * @return the value
	 * @throws NotFound the not found
	 * @see com.slytechs.jnet.runtime.util.Attribute.Attributes#read(com.slytechs.protocol.runtime.internal.util.Attribute)
	 */
	public Value read(Attribute attr) throws NotFound {
		int offset = findOffset(attr);
		if (offset == -1)
			throw new NotFound(attr.toString());

		return readRecord(offset);
	}

	/**
	 * Read.
	 *
	 * @param <T>          the generic type
	 * @param attr         the attr
	 * @param defaultValue the default value
	 * @return the value
	 * @see com.slytechs.jnet.runtime.util.Attribute.Attributes#read(com.slytechs.protocol.runtime.internal.util.Attribute,
	 *      java.lang.Object)
	 */
	public <T> Value read(Attribute attr, T defaultValue) {
		int offset = findOffset(attr);
		if (offset == -1) {
			write(attr, isBound());
			offset = findOffset(attr);
		}

		return readRecord(offset);
	}

	/**
	 * Read record.
	 *
	 * @param offset the offset
	 * @return the record
	 */
	private Record readRecord(int offset) {
		ByteBuffer b = super.buffer().clear();

		Record record = new Record();
		record.id = b.getShort(offset + 0);
		record.recordLength = b.getShort(offset + 2);
		record.recordOffset = offset;

		ByteBuffer recordBuffer = b.position(offset)
				.limit(offset + record.recordLength)
				.slice();
		record.bind(recordBuffer);

		return record;
	}

	/**
	 * Read record.
	 *
	 * @param offset            the offset
	 * @param expectedAttribute the expected attribute
	 * @return the record
	 */
	private Record readRecord(int offset, Attribute expectedAttribute) {
		ByteBuffer b = super.buffer();

		short id = b.getShort(offset + 0);
		if (id != expectedAttribute.id())
			throw new IllegalStateException("invalid attribute [%d]at offset [%d], expected [%d/%s]"
					.formatted(id, offset, expectedAttribute.id(), expectedAttribute));

		Record record = new Record();
		record.id = id;
		record.recordLength = b.getShort(offset + 2);
		record.recordOffset = offset;

		ByteBuffer recordBuffer = b.position(offset)
				.limit(offset + record.recordLength)
				.slice();
		record.bind(recordBuffer);

		return record;
	}

	/**
	 * Remaining.
	 *
	 * @return the int
	 * @see com.slytechs.jnet.runtime.util.Attribute.Attributes#remaining()
	 */
	public int remaining() {
		return buffer().capacity() - buffer().limit();
	}

	/**
	 * To array.
	 *
	 * @return the value[]
	 * @see com.slytechs.jnet.runtime.util.Attribute.Attributes#toDescriptorArray()
	 */
	public Value[] toArray() {
		int recordCount = this.recordCount.getInt();
		if (recordCount == 0)
			return new Value[0];

		Value[] array = new Value[recordCount];

		int i = 0;
		for (Value value : this)
			array[i] = value;

		return array;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return buffer().toString();
	}

	/**
	 * Write.
	 *
	 * @param attr the attr
	 * @param v    the v
	 * @return the attribute buffer
	 */
	public AttributeBuffer write(Attribute attr, boolean v) {
		writeRecordHeaderAtLimit(attr, 1)
				.put(v ? (byte) 1 : (byte) 0);

		return this;
	}

	/**
	 * Write.
	 *
	 * @param attr the attr
	 * @param v    the v
	 * @return the attribute buffer
	 */
	public AttributeBuffer write(Attribute attr, byte[] v) {
		writeRecordHeaderAtLimit(attr, v.length)
				.put(v);

		return this;
	}

	/**
	 * Write.
	 *
	 * @param attr   the attr
	 * @param v      the v
	 * @param offset the offset
	 * @param length the length
	 * @return the attribute buffer
	 */
	public AttributeBuffer write(Attribute attr, byte[] v, int offset, int length) {
		writeRecordHeaderAtLimit(attr, length)
				.put(v, offset, length);

		return this;
	}

	/**
	 * Write.
	 *
	 * @param attr the attr
	 * @param v    the v
	 * @return the attribute buffer
	 */
	public AttributeBuffer write(Attribute attr, ByteBuffer v) {
		writeRecordHeaderAtLimit(attr, v.remaining())
				.put(v);

		return this;
	}

	/**
	 * Write.
	 *
	 * @param attr the attr
	 * @param v    the v
	 * @return the attribute buffer
	 */
	public AttributeBuffer write(Attribute attr, double v) {
		writeRecordHeaderAtLimit(attr, 8)
				.putDouble(v);

		return this;
	}

	/**
	 * Write.
	 *
	 * @param attr the attr
	 * @param v    the v
	 * @return the attribute buffer
	 */
	public AttributeBuffer write(Attribute attr, float v) {
		writeRecordHeaderAtLimit(attr, 4)
				.putFloat(v);

		return this;
	}

	/**
	 * Write.
	 *
	 * @param attr the attr
	 * @param v    the v
	 * @return the attribute buffer
	 */
	public AttributeBuffer write(Attribute attr, int v) {
		writeRecordHeaderAtLimit(attr, 4)
				.putInt(v);

		return this;
	}

	/**
	 * Write.
	 *
	 * @param attr the attr
	 * @param v    the v
	 * @return the attribute buffer
	 */
	public AttributeBuffer write(Attribute attr, long v) {
		writeRecordHeaderAtLimit(attr, 8)
				.putLong(v);

		return this;
	}

	/**
	 * Write.
	 *
	 * @param attr                 the attr
	 * @param memorySegmentPointer the memory segment pointer
	 * @return the attribute buffer
	 */
	public AttributeBuffer write(Attribute attr, MemorySegment memorySegmentPointer) {
		writeRecordHeaderAtLimit(attr, 16)
				.putLong(memorySegmentPointer.address().toRawLongValue())
				.putLong(memorySegmentPointer.byteSize());

		return this;
	}

	/**
	 * Write.
	 *
	 * @param attr the attr
	 * @param v    the v
	 * @return the attribute buffer
	 */
	public AttributeBuffer write(Attribute attr, short v) {
		writeRecordHeaderAtLimit(attr, 2)
				.putShort(v);

		return this;
	}

	/**
	 * Write.
	 *
	 * @param attr the attr
	 * @param v    the v
	 * @return the attribute buffer
	 */
	public AttributeBuffer write(Attribute attr, String v) {
		int len = v.length() + 1; // We append a '\0' for C compatibility
		byte[] bytes = v.getBytes(StandardCharsets.UTF_8);
		writeRecordHeaderAtLimit(attr, len)
				.put(bytes)
				.put((byte) 0);

		return this;
	}

	/**
	 * Write.
	 *
	 * @param <T>  the generic type
	 * @param attr the attr
	 * @param obj  the obj
	 * @return the attribute buffer
	 */
	public <T extends MemoryBinding> AttributeBuffer write(Attribute attr, T obj) {
		ByteBuffer b = obj.buffer();
		MemorySegment mseg = obj.address() instanceof MemorySegment seg
				? seg
				: MemorySegment.ofBuffer(b);

		return write(attr, mseg);
	}

	/**
	 * Write record header at limit.
	 *
	 * @param attr        the attr
	 * @param valueLength the value length
	 * @return the byte buffer
	 */
	private ByteBuffer writeRecordHeaderAtLimit(Attribute attr, int valueLength) {
		short len = (short) (valueLength + Record.RECORD_HEADER_LENGTH);
		ByteBuffer b = buffer();
		int l = b.limit();
		b.position(l)
				.limit(l + len)
				.putInt(attr.id())
				.putInt(len);

		recordCount.incInt();

		return b;
	}
}
