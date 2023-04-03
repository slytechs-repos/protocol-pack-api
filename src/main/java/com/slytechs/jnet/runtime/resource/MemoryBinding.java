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

import java.lang.foreign.MemorySegment;
import java.nio.ByteBuffer;

/**
 * Host memory binding.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public class MemoryBinding implements Cloneable {

	private MemorySegment address = null;
	private ByteBuffer buffer;

	public final MemorySegment address() {
		if (address == null)
			deriveAddressAndSessionFromBuffer();

		return address;
	}

	public final void bind(byte[] src) {
		bind(ByteBuffer.wrap(src, 0, src.length));
	}

	public final void bind(byte[] src, MemorySegment address) {
		bind(ByteBuffer.wrap(src, 0, src.length), address);
	}

	public final void bind(byte[] src, int offset, int length) {
		bind(ByteBuffer.wrap(src, offset, length));
	}

	public final void bind(byte[] src, int offset, int length, MemorySegment address) {
		bind(ByteBuffer.wrap(src, offset, length), address);
	}

	@SuppressWarnings("unchecked")
	private <T> T us() {
		return (T) this;
	}

	public <T> T withBinding(ByteBuffer buffer) {
		bind(buffer);

		return us();
	}

	public final void bind(ByteBuffer buffer) {
		unbind();

		bind0(buffer, null);

		onBind();
	}

	public <T> T withBinding(ByteBuffer buffer, MemorySegment address) {
		bind(buffer, address);

		return us();
	}

	public final void bind(ByteBuffer buffer, MemorySegment address) {
		unbind();

		bind0(buffer, address);

		onBind();
	}

	private final void bind0(ByteBuffer buffer, MemorySegment address) {
		this.buffer = buffer;
		this.address = address;
	}

	public final ByteBuffer buffer() {
		if (buffer == null)
			throw new IllegalStateException("no binding!");

		return buffer;
	}

	@Override
	public MemoryBinding clone() {
		return cloneTo(ByteBuffer.allocateDirect(buffer.capacity()));
	}

	public MemoryBinding cloneTo(ByteBuffer dst) {
		try {
			var clone = (MemoryBinding) super.clone();

			ByteBuffer src = this.buffer();
			int p = dst.position(), l = dst.limit();

			/* Set to position/limit to where copied data resides */
			dst.put(src)
					.flip()
					.position(p);

			clone.bind0(dst, null);

			/* Leave dst buffer as if relative put was done on it */
			dst.position(dst.limit())
					.limit(l);

			return clone;
		} catch (CloneNotSupportedException e) {
			throw new IllegalStateException(e);
		}
	}

	private void deriveAddressAndSessionFromBuffer() {
		if (buffer == null)
			throw new IllegalStateException("no binding!");

		if (buffer.isDirect()) {
			MemorySegment mseg = MemorySegment.ofBuffer(buffer);
			this.address = mseg;

		} else {
			this.address = null;
		}
	}

	public final boolean isBound() {
		if (buffer == null)
			return false;

		/* If we already know the buffer's memory session, use it */
		if (address != null)
			return address.session().isAlive();

		/*
		 * Avoid creating a MemorySegment for a direct buffer to just find out the
		 * memory status. Calling any buffer's accessors on a closed direct byte buffer,
		 * will throw an exception.
		 */
		if (buffer.isDirect()) {
			try {
				buffer.get(0);
				// If direct buffer MemorySession is closed, throws an error
			} catch (UnsupportedOperationException e) {
				return false;
			}
		}

		return true;
	}

	protected void onBind() {
	}

	protected void onUnbind() {
	}

	public final void unbind() {
		if (buffer == null)
			return;

		onUnbind();

		address = null;
		buffer = null;
	}

}
