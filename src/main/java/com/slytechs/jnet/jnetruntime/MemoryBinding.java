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
package com.slytechs.jnet.jnetruntime;

import java.lang.foreign.MemorySegment;
import java.nio.ByteBuffer;

import com.slytechs.jnet.jnetruntime.internal.foreign.ForeignUtils;

/**
 * Host memory binding base class. This class is designed to be sbuclassed and
 * manages a data binding (a reference) to a data buffer allowing the same
 * subclassed objects to be reused by unbinding old data and rebinding with new.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public abstract class MemoryBinding implements Cloneable, Binding {

	/** The address. */
	private MemorySegment address = null;

	/** The buffer. */
	private ByteBuffer buffer;

	/**
	 * Instantiates a new memory binding.
	 */
	protected MemoryBinding() {
	}

	/**
	 * Address.
	 *
	 * @return the memory segment
	 */
	public final MemorySegment address() {
		if (address == null)
			deriveAddressAndSessionFromBuffer();

		return address;
	}

	/**
	 * Bind data array source to this object.
	 *
	 * @param src data array
	 */
	public final void bind(byte[] src) {
		bind(ByteBuffer.wrap(src, 0, src.length));
	}

	/**
	 * Bind data array source to this object.
	 *
	 * @param src     the src
	 * @param address the address
	 */
	public final void bind(byte[] src, MemorySegment address) {
		bind(ByteBuffer.wrap(src, 0, src.length), address);
	}

	/**
	 * Bind data array source to this object.
	 *
	 * @param src    the src
	 * @param offset the offset
	 * @param length the length
	 */
	public final void bind(byte[] src, int offset, int length) {
		bind(ByteBuffer.wrap(src, offset, length));
	}

	/**
	 * Bind data array source to this object.
	 *
	 * @param src     the src
	 * @param offset  the offset
	 * @param length  the length
	 * @param address the address
	 */
	public final void bind(byte[] src, int offset, int length, MemorySegment address) {
		bind(ByteBuffer.wrap(src, offset, length), address);
	}

	/**
	 * Us.
	 *
	 * @param <T> the generic type
	 * @return the t
	 */
	@SuppressWarnings("unchecked")
	private <T> T us() {
		return (T) this;
	}

	/**
	 * Fluent bind with data buffer.
	 *
	 * @param <T>    the generic type
	 * @param buffer the buffer
	 * @return the t
	 */
	public <T> T withBinding(ByteBuffer buffer) {
		bind(buffer);

		return us();
	}

	/**
	 * Bind with data buffer.
	 *
	 * @param buffer the buffer
	 */
	public final void bind(ByteBuffer buffer) {
		unbind();

		bind0(buffer, null);

		onBind();
	}

	/**
	 * Fluent bind with data buffer.
	 *
	 * @param <T>     the generic type
	 * @param buffer  the buffer
	 * @param address the address
	 * @return the t
	 */
	public <T> T withBinding(ByteBuffer buffer, MemorySegment address) {
		bind(buffer, address);

		return us();
	}

	/**
	 * Bind with data buffer.
	 *
	 * @param buffer  the buffer
	 * @param address the address
	 */
	public final void bind(ByteBuffer buffer, MemorySegment address) {
		unbind();

		bind0(buffer, address);

		onBind();
	}

	/**
	 * Bind 0.
	 *
	 * @param buffer  the buffer
	 * @param address the address
	 */
	private final void bind0(ByteBuffer buffer, MemorySegment address) {

		if (buffer.capacity() == 0)
			throw new IllegalStateException("%s: empty binding! %s"
					.formatted(getClass().getSimpleName(), buffer));

		this.buffer = buffer;
		this.address = address;
	}

	/**
	 * Get the currently bound data buffer.
	 *
	 * @return the byte buffer
	 * @throws IllegalStateException throws when not bound to any data
	 */
	public final ByteBuffer buffer() throws IllegalStateException {
		if (buffer == null)
			throw new IllegalStateException("no binding!");

		return buffer;
	}

	/**
	 * Clone this binding along with the data buffer by allocating new space and
	 * making a copy.
	 *
	 * @return the memory binding
	 * @see java.lang.Object#clone()
	 */
	@Override
	public MemoryBinding clone() {
		return cloneTo(ByteBuffer.allocateDirect(buffer.capacity()));
	}

	/**
	 * Clone data buffer to this specific dst buffer.
	 *
	 * @param dst the dst
	 * @return the memory binding
	 */
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

	/**
	 * Derive address and session from buffer.
	 */
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

	/**
	 * Checks if this binding is bound to any data.
	 *
	 * @return true, if is bound
	 */
	@Override
	public final boolean isBound() {
		if (buffer == null)
			return false;

		/* If we already know the buffer's memory session, use it */
		if (!ForeignUtils.isNullAddress(address))
			return address.scope().isAlive();

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

	/**
	 * Called after a new data binding has occured.
	 */
	protected void onBind() {
	}

	/**
	 * Called before the current data binding is about to be unbound.
	 */
	protected void onUnbind() {
	}

	/**
	 * Unbinds the current data binding, if one exists. Otherwise this call has no
	 * effect.
	 */
	@Override
	public final void unbind() {
		if (buffer == null)
			return;

		onUnbind();

		address = null;
		buffer = null;
	}

}
