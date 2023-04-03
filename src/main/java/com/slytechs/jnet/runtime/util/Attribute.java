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

import java.lang.foreign.MemorySegment;
import java.lang.foreign.MemorySession;
import java.nio.ByteBuffer;

import com.slytechs.jnet.runtime.resource.MemoryBinding;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public interface Attribute  {

	public enum Builtin implements Attribute {
		EMPTY_RECORD,
		RECORD_COUNT,;

		/**
		 * @see com.slytechs.jnet.runtime.util.Attribute#family()
		 */
		@Override
		public int family() {
			return 0;
		}
	}

	/** Attribute Value Pair */
	public interface Value {
		int addInt(int delta);

		boolean getBoolean();

		byte[] getByteArray();

		double getDouble();

		float getFloat();

		int getInt();

		long getLong();

		default MemorySegment getMemorySegment() {
			return getMemorySegment(MemorySession.openImplicit());
		}

		MemorySegment getMemorySegment(MemorySession scope);

		default <T extends MemoryBinding> T getObject(T obj) {
			return getObject(obj, MemorySession.openImplicit());
		}

		default <T extends MemoryBinding> T getObject(T obj, MemorySession session) {
			MemorySegment mseg = getMemorySegment(session);
			ByteBuffer b = mseg.asByteBuffer();

			obj.bind(b, mseg);

			return obj;
		}

		short getShort();

		String getString();

		int id();

		int incInt();

		int offset();

		int setInt(int newValue);

		ByteBuffer valueBuffer();

		int valueLength();
	}

	default short byteLength() {
		return 0;
	}

	int family();

	default String formatValue(ByteBuffer buffer) {
		return "";
	}

	default StringBuilder formatValue(StringBuilder sb, ByteBuffer buffer) {
		return sb;
	}

	default int id() {
		return ((family() << 16) | ordinal());
	}

	String name();

	int ordinal();
}
