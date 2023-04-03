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
package com.slytechs.jnet.runtime.resource;

import java.lang.foreign.Addressable;
import java.lang.foreign.MemoryAddress;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.MemorySession;
import java.nio.ByteBuffer;
import java.util.Objects;

import com.slytechs.jnet.runtime.util.ByteArray;

/**
 * The Class BindingUtils.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public class BindingUtils {

	/**
	 * Instantiates a new binding utils.
	 */
	private BindingUtils() {
	}

	/**
	 * Check scope.
	 *
	 * @param scope the scope
	 */
	public static void checkScope(MemorySession scope) {
		if (!scope.isAlive())
			throw new IllegalStateException();

		if (scope.ownerThread() != null && Thread.currentThread().threadId() != scope.ownerThread().threadId())
			throw new IllegalStateException("wrong thread for confined scope");
	}

	/**
	 * Default length.
	 *
	 * @param data   the data
	 * @param offset the offset
	 * @return the long
	 */
	public static long defaultLength(Object data, long offset) {
		return switch (data) {
		case byte[] s -> s.length - (int) offset;
		case ByteArray s -> s.arrayLength() - (int) offset;
		case MemorySegment s -> s.byteSize();

		default -> throw new IllegalArgumentException("invalid binding data type " + data.getClass());
		};
	}

	/**
	 * Copy data.
	 *
	 * @param src    the src
	 * @param offset the offset
	 * @param length the length
	 * @param dst    the dst
	 * @return the long
	 */
	public static long copyData(Object src, long offset, long length, Object dst) {
		Objects.requireNonNull(src);
		Objects.requireNonNull(dst);

		return switch (src) {
		case byte[] s -> copyByteArray(s, offset, length, dst);
		case ByteArray s -> copyByteArray(s.array(), s.arrayOffset() + offset, s.arrayLength() + length, dst);

		default -> throw new IllegalArgumentException("invalid binding data type " + dst.getClass());
		};
	}

	/**
	 * Copy byte array.
	 *
	 * @param src  the src
	 * @param soff the soff
	 * @param slen the slen
	 * @param dst  the dst
	 * @return the int
	 */
	private static int copyByteArray(byte[] src, long soff, long slen, Object dst) {
		return switch (dst) {
		case byte[] a -> copyByteArrayToByteArray(src, soff, slen, a, 0, a.length);
		case ByteArray b -> copyByteArrayToByteArray(src, soff, slen, b.array(), b.arrayOffset(), b.arrayLength());

		default -> throw new IllegalArgumentException("invalid binding data type " + dst.getClass());
		};
	}

	/**
	 * Copy byte array to byte array.
	 *
	 * @param src  the src
	 * @param soff the soff
	 * @param slen the slen
	 * @param dst  the dst
	 * @param doff the doff
	 * @param dlen the dlen
	 * @return the int
	 */
	private static int copyByteArrayToByteArray(byte[] src, long soff, long slen, byte[] dst, int doff, int dlen) {
		int len = (int) ((slen < dlen) ? slen : dlen);

		System.arraycopy(src, (int) soff, dst, doff, len);

		return len;
	}

	/**
	 * Slice.
	 *
	 * @param data   the data
	 * @param offset the offset
	 * @param length the length
	 * @return the object
	 */
	public static Object slice(Object data, long offset, long length) {

		return switch (data) {
		case byte[] a -> ByteArray.wrap(a, offset, length);
		case ByteArray b -> ByteArray.wrap(b.array(), b.arrayOffset() + offset, length);
		case MemorySegment s -> s.asSlice(offset, length);

		default -> throw new IllegalArgumentException("invalid binding data type " + data.getClass());
		};

	}

	/**
	 * To memory addressable.
	 *
	 * @param data the data
	 * @return the addressable
	 */
	public static Addressable toMemoryAddressable(Object data) {
		return switch (data) {
		case MemoryAddress s -> s;
		case MemorySegment s -> s;
		case byte[] a -> MemorySegment.ofArray(a);
		case ByteBuffer b -> MemorySegment.ofBuffer(b);
		case ByteArray b -> MemorySegment.ofArray(b.array());
		default -> throw new IllegalArgumentException("invalid binding data type " + data.getClass());
		};
	}

	/**
	 * To memory segment.
	 *
	 * @param data the data
	 * @return the memory segment
	 */
	public static MemorySegment toMemorySegment(Object data) {
		return switch (data) {
		case MemoryAddress s ->
			throw new IllegalArgumentException("can not convert MemoryAddress to segment without a length");
		case MemorySegment s -> s;
		case byte[] s -> MemorySegment.ofArray(s);
		case ByteBuffer b -> MemorySegment.ofBuffer(b);
		case ByteArray b -> MemorySegment.ofArray(b.array());

		default -> throw new IllegalArgumentException("invalid binding data type " + data.getClass());
		};
	}

	/**
	 * To byte buffer.
	 *
	 * @param data   the data
	 * @param offset the offset
	 * @param length the length
	 * @return the byte buffer
	 */
	public static ByteBuffer toByteBuffer(Object data, long offset, long length) {
		int off = (int) offset;
		int len = (int) length;

		return switch (data) {
		case MemoryAddress s ->
			throw new IllegalArgumentException("can not convert MemoryAddress to segment without a length");
		case MemorySegment s -> s.asByteBuffer();
		case byte[] s -> ByteBuffer.wrap(s, off, len);
		case ByteBuffer b -> b.mark().position(off).limit(off + len).slice();
		case ByteArray b -> ByteBuffer.wrap(b.array(), off, len);

		default -> throw new IllegalArgumentException("invalid binding data type " + data.getClass());
		};
	}

}
