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
package com.slytechs.jnet.runtime.internal.util;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.MemorySession;
import java.nio.ByteBuffer;

import com.slytechs.jnet.runtime.MemoryBinding;

/**
 * The Interface Attribute.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public interface Attribute  {

	/**
	 * The Enum Builtin.
	 */
	public enum Builtin implements Attribute {
		
		/** The empty record. */
		EMPTY_RECORD,
		
		/** The record count. */
		RECORD_COUNT,;

		/**
		 * Family.
		 *
		 * @return the int
		 * @see com.slytechs.jnet.runtime.internal.util.Attribute#family()
		 */
		@Override
		public int family() {
			return 0;
		}
	}

	/**
	 * Attribute Value Pair.
	 */
	public interface Value {
		
		/**
		 * Adds the int.
		 *
		 * @param delta the delta
		 * @return the int
		 */
		int addInt(int delta);

		/**
		 * Gets the boolean.
		 *
		 * @return the boolean
		 */
		boolean getBoolean();

		/**
		 * Gets the byte array.
		 *
		 * @return the byte array
		 */
		byte[] getByteArray();

		/**
		 * Gets the double.
		 *
		 * @return the double
		 */
		double getDouble();

		/**
		 * Gets the float.
		 *
		 * @return the float
		 */
		float getFloat();

		/**
		 * Gets the int.
		 *
		 * @return the int
		 */
		int getInt();

		/**
		 * Gets the long.
		 *
		 * @return the long
		 */
		long getLong();

		/**
		 * Gets the memory segment.
		 *
		 * @return the memory segment
		 */
		default MemorySegment getMemorySegment() {
			return getMemorySegment(MemorySession.openImplicit());
		}

		/**
		 * Gets the memory segment.
		 *
		 * @param scope the scope
		 * @return the memory segment
		 */
		MemorySegment getMemorySegment(MemorySession scope);

		/**
		 * Gets the object.
		 *
		 * @param <T> the generic type
		 * @param obj the obj
		 * @return the object
		 */
		default <T extends MemoryBinding> T getObject(T obj) {
			return getObject(obj, MemorySession.openImplicit());
		}

		/**
		 * Gets the object.
		 *
		 * @param <T>     the generic type
		 * @param obj     the obj
		 * @param session the session
		 * @return the object
		 */
		default <T extends MemoryBinding> T getObject(T obj, MemorySession session) {
			MemorySegment mseg = getMemorySegment(session);
			ByteBuffer b = mseg.asByteBuffer();

			obj.bind(b, mseg);

			return obj;
		}

		/**
		 * Gets the short.
		 *
		 * @return the short
		 */
		short getShort();

		/**
		 * Gets the string.
		 *
		 * @return the string
		 */
		String getString();

		/**
		 * Id.
		 *
		 * @return the int
		 */
		int id();

		/**
		 * Inc int.
		 *
		 * @return the int
		 */
		int incInt();

		/**
		 * Offset.
		 *
		 * @return the int
		 */
		int offset();

		/**
		 * Sets the int.
		 *
		 * @param newValue the new value
		 * @return the int
		 */
		int setInt(int newValue);

		/**
		 * Value buffer.
		 *
		 * @return the byte buffer
		 */
		ByteBuffer valueBuffer();

		/**
		 * Value length.
		 *
		 * @return the int
		 */
		int valueLength();
	}

	/**
	 * Byte length.
	 *
	 * @return the short
	 */
	default short byteLength() {
		return 0;
	}

	/**
	 * Family.
	 *
	 * @return the int
	 */
	int family();

	/**
	 * Format value.
	 *
	 * @param buffer the buffer
	 * @return the string
	 */
	default String formatValue(ByteBuffer buffer) {
		return "";
	}

	/**
	 * Format value.
	 *
	 * @param sb     the sb
	 * @param buffer the buffer
	 * @return the string builder
	 */
	default StringBuilder formatValue(StringBuilder sb, ByteBuffer buffer) {
		return sb;
	}

	/**
	 * Id.
	 *
	 * @return the int
	 */
	default int id() {
		return ((family() << 16) | ordinal());
	}

	/**
	 * Name.
	 *
	 * @return the string
	 */
	String name();

	/**
	 * Ordinal.
	 *
	 * @return the int
	 */
	int ordinal();
}
