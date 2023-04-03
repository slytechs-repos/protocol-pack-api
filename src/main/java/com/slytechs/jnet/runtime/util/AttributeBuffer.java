/*
 * Apache License, Version 2.0
 * 
 * Copyright 2013-2022 Sly Technologies Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.slytechs.jnet.runtime.util;

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

import com.slytechs.jnet.runtime.resource.MemoryBinding;
import com.slytechs.jnet.runtime.util.Attribute.Builtin;
import com.slytechs.jnet.runtime.util.Attribute.Value;

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

	private class Record extends MemoryBinding implements Value {

		/** 2-bytes ID + 2-bytes record length */
		private static int RECORD_HEADER_LENGTH = 4;

		int id;
		int recordLength;
		int recordOffset;

		@Override
		public int addInt(int delta) {
			int oldValue = getInt();

			return setInt(oldValue + delta);
		}

		/**
		 * @see com.slytechs.jnet.runtime.util.Attribute.Value#getBoolean()
		 */
		@Override
		public boolean getBoolean() {
			return buffer().get(RECORD_HEADER_LENGTH) != 0;
		}

		/**
		 * @see com.slytechs.jnet.runtime.util.Attribute.Value#getByteArray()
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
		 * @see com.slytechs.jnet.runtime.util.Attribute.Value#getDouble()
		 */
		@Override
		public double getDouble() {
			return getValueBuffer().getDouble();
		}

		/**
		 * @see com.slytechs.jnet.runtime.util.Attribute.Value#getFloat()
		 */
		@Override
		public float getFloat() {
			return getValueBuffer().getFloat();
		}

		/**
		 * @see com.slytechs.jnet.runtime.util.Attribute.Value#getInt()
		 */
		@Override
		public int getInt() {
			return getValueBuffer().getInt();
		}

		/**
		 * @see com.slytechs.jnet.runtime.util.Attribute.Value#getLong()
		 */
		@Override
		public long getLong() {
			return getValueBuffer().getLong();
		}

		/**
		 * @see com.slytechs.jnet.runtime.util.Attribute.Value#getMemorySegment(java.lang.foreign.MemorySession)
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
		 * @see com.slytechs.jnet.runtime.util.Attribute.Value#getShort()
		 */
		@Override
		public short getShort() {
			return getValueBuffer().getShort();
		}

		/**
		 * @see com.slytechs.jnet.runtime.util.Attribute.Value#getString()
		 */
		@Override
		public String getString() {

			byte[] bytes = getByteArray();
			int len = bytes.length - 1; // Drop the '\0' at the end

			return new String(bytes, 0, len, StandardCharsets.UTF_8);
		}

		private ByteBuffer getValueBuffer() {
			return buffer()
					.position(RECORD_HEADER_LENGTH);
		}

		/**
		 * @see com.slytechs.jnet.runtime.util.Attribute.Value#id()
		 */
		@Override
		public int id() {
			return id;
		}

		@Override
		public int incInt() {
			return addInt(1);
		}

		/**
		 * @see com.slytechs.jnet.runtime.util.Attribute.Value#offset()
		 */
		@Override
		public int offset() {
			return recordOffset;
		}

		/**
		 * @see com.slytechs.jnet.runtime.util.Attribute.Value#setInt(int)
		 */
		@Override
		public int setInt(int newValue) {
			buffer().putInt(RECORD_HEADER_LENGTH, newValue);

			return newValue;
		}

		/**
		 * @see com.slytechs.jnet.runtime.util.Attribute.Value#valueBuffer()
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
		 * @see com.slytechs.jnet.runtime.util.Attribute.Value#valueLength()
		 */
		@Override
		public int valueLength() {
			return recordLength - RECORD_HEADER_LENGTH;
		}

	}

	private static ByteBuffer writeRecordHeaderAtLimit(ByteBuffer b, Attribute attr, int valueLength) {
		int len = (valueLength + Record.RECORD_HEADER_LENGTH);
		int l = b.limit();
		b.position(l)
				.limit(l + len)
				.putInt(attr.id())
				.putInt(len);

		return b;
	}

	private Record recordCount;

	public AttributeBuffer() {
	}

	public AttributeBuffer(int capacity) {
		ByteBuffer b = ByteBuffer.allocateDirect(capacity);

		writeRecordHeaderAtLimit(b.flip(), Builtin.RECORD_COUNT, 4)
				.clear();

		super.bind(b);
	}

	public boolean contains(Attribute attr) {
		return findOffset(attr) != -1;
	}

	public Optional<Value> find(Attribute attr) {
		int offset = findOffset(attr);
		if (offset == -1)
			return Optional.empty();

		return Optional.of(readRecord(offset));
	}

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
	 * @see com.slytechs.jnet.runtime.util.Attribute.Attributes#limit()
	 */
	public int limit() {
		return buffer().limit();
	}

	/**
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
	 * @see com.slytechs.jnet.runtime.resource.MemoryBinding#onBind()
	 */
	@Override
	protected void onBind() {
		this.recordCount = readRecord(0, Builtin.RECORD_COUNT);
	}

	/**
	 * @see com.slytechs.jnet.runtime.resource.MemoryBinding#onUnbind()
	 */
	@Override
	protected void onUnbind() {
		recordCount = null;
	}

	/**
	 * @see com.slytechs.jnet.runtime.util.Attribute.Attributes#read(com.slytechs.jnet.runtime.util.Attribute)
	 */
	public Value read(Attribute attr) throws NotFound {
		int offset = findOffset(attr);
		if (offset == -1)
			throw new NotFound(attr.toString());

		return readRecord(offset);
	}

	/**
	 * @see com.slytechs.jnet.runtime.util.Attribute.Attributes#read(com.slytechs.jnet.runtime.util.Attribute,
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
	 * @see com.slytechs.jnet.runtime.util.Attribute.Attributes#remaining()
	 */
	public int remaining() {
		return buffer().capacity() - buffer().limit();
	}

	/**
	 * @see com.slytechs.jnet.runtime.util.Attribute.Attributes#toArray()
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

	@Override
	public String toString() {
		return buffer().toString();
	}

	public AttributeBuffer write(Attribute attr, boolean v) {
		writeRecordHeaderAtLimit(attr, 1)
				.put(v ? (byte) 1 : (byte) 0);

		return this;
	}

	public AttributeBuffer write(Attribute attr, byte[] v) {
		writeRecordHeaderAtLimit(attr, v.length)
				.put(v);

		return this;
	}

	public AttributeBuffer write(Attribute attr, byte[] v, int offset, int length) {
		writeRecordHeaderAtLimit(attr, length)
				.put(v, offset, length);

		return this;
	}

	public AttributeBuffer write(Attribute attr, ByteBuffer v) {
		writeRecordHeaderAtLimit(attr, v.remaining())
				.put(v);

		return this;
	}

	public AttributeBuffer write(Attribute attr, double v) {
		writeRecordHeaderAtLimit(attr, 8)
				.putDouble(v);

		return this;
	}

	public AttributeBuffer write(Attribute attr, float v) {
		writeRecordHeaderAtLimit(attr, 4)
				.putFloat(v);

		return this;
	}

	public AttributeBuffer write(Attribute attr, int v) {
		writeRecordHeaderAtLimit(attr, 4)
				.putInt(v);

		return this;
	}

	public AttributeBuffer write(Attribute attr, long v) {
		writeRecordHeaderAtLimit(attr, 8)
				.putLong(v);

		return this;
	}

	public AttributeBuffer write(Attribute attr, MemorySegment memorySegmentPointer) {
		writeRecordHeaderAtLimit(attr, 16)
				.putLong(memorySegmentPointer.address().toRawLongValue())
				.putLong(memorySegmentPointer.byteSize());

		return this;
	}

	public AttributeBuffer write(Attribute attr, short v) {
		writeRecordHeaderAtLimit(attr, 2)
				.putShort(v);

		return this;
	}

	public AttributeBuffer write(Attribute attr, String v) {
		int len = v.length() + 1; // We append a '\0' for C compatibility
		byte[] bytes = v.getBytes(StandardCharsets.UTF_8);
		writeRecordHeaderAtLimit(attr, len)
				.put(bytes)
				.put((byte) 0);

		return this;
	}

	public <T extends MemoryBinding> AttributeBuffer write(Attribute attr, T obj) {
		ByteBuffer b = obj.buffer();
		MemorySegment mseg = obj.address() instanceof MemorySegment seg
				? seg
				: MemorySegment.ofBuffer(b);

		return write(attr, mseg);
	}

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
