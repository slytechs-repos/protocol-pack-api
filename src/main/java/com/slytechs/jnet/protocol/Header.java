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

import java.nio.ByteBuffer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

import com.slytechs.jnet.protocol.packet.meta.Meta;
import com.slytechs.jnet.protocol.packet.meta.Meta.MetaType;
import com.slytechs.jnet.protocol.packet.meta.PacketFormat;
import com.slytechs.jnet.runtime.MemoryBinding;
import com.slytechs.jnet.runtime.util.Detail;
import com.slytechs.jnet.runtime.util.HexStrings;

/**
 * The Class Header.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public abstract class Header extends MemoryBinding {

	/** The lock factory. */
	private static Supplier<Lock> LOCK_FACTORY = ReentrantLock::new;

	/** The id. */
	protected final int id;
	
	/** The lock. */
	private volatile Lock lock; // Lazy & concurrent allocation

	/** The payload length. */
	private int offset, length, payloadLength;
	
	/** The formatter. */
	private PacketFormat formatter;

	/**
	 * Instantiates a new header.
	 *
	 * @param id the id
	 */
	protected Header(int id) {
		this.id = id;
	}

	/**
	 * Instantiates a new header.
	 *
	 * @param id   the id
	 * @param lock the lock
	 */
	protected Header(int id, Lock lock) {
		this.id = id;
		this.lock = lock;
	}

	/**
	 * Bind header extensions and options.
	 *
	 * @param sourceBuffer the source buffer or typically the buffer containing the
	 *                     source packet
	 * @param lookup       the extension header lookup
	 * @param meta         the meta data, such as descriptor/lookup specific data
	 *                     which aids in extension header lookup
	 */
	void bindExtensionsToPacket(ByteBuffer sourceBuffer, HeaderLookup lookup, int meta) {
		// Do nothing by default
	}

	/**
	 * Bind header to packet.
	 *
	 * @param packet        the packet
	 * @param offset        the offset
	 * @param length        the length
	 * @param payloadLength the payload length
	 */
	final void bindHeaderToPacket(ByteBuffer packet, int offset, int length, int payloadLength) {
		this.offset = offset;
		this.length = length;
		this.payloadLength = payloadLength;

		super.bind(packet.slice(offset, length));
	}

	/**
	 * Gets the lock.
	 *
	 * @return the lock
	 */
	public final Lock getLock() {
		if (this.lock == null) { // Lazy allocate
			Lock lock = LOCK_FACTORY.get(); // get on local thread context (allows multiple threads)

			while (this.lock == null) // No lock volatile initialization, only 1 thread wins
				this.lock = lock;
		}

		return this.lock;
	}

	/**
	 * Id.
	 *
	 * @return the int
	 */
	@Meta(MetaType.ATTRIBUTE)
	public final int id() {
		return id;
	}

	/**
	 * Length.
	 *
	 * @return the int
	 */
	@Meta(MetaType.ATTRIBUTE)
	public int length() {
		return length;
	}

	/**
	 * Name.
	 *
	 * @return the string
	 */
	@Meta(MetaType.ATTRIBUTE)
	public String name() {
		return getClass().getSimpleName();
	}

	/**
	 * Offset.
	 *
	 * @return the int
	 */
	@Meta(MetaType.ATTRIBUTE)
	public final int offset() {
		return offset;
	}

	/**
	 * On unbind.
	 *
	 * @see com.slytechs.jnet.runtime.MemoryBinding#onUnbind()
	 */
	@Override
	protected void onUnbind() {
		offset = length = payloadLength = 0;
	}

	/**
	 * Payload length.
	 *
	 * @return the int
	 */
	@Meta(MetaType.ATTRIBUTE)
	public int payloadLength() {
		return payloadLength;
	}

	/**
	 * Payload offset.
	 *
	 * @return the int
	 */
	@Meta(MetaType.ATTRIBUTE)
	public int payloadOffset() {
		return offset + length;
	}

	/**
	 * Sets the formatter.
	 *
	 * @param formatter the new formatter
	 */
	void setFormatter(PacketFormat formatter) {
		this.formatter = formatter;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return toString(Detail.MEDIUM);
	}

	/**
	 * To string.
	 *
	 * @param detail the detail
	 * @return the string
	 */
	public String toString(Detail detail) {
		if (formatter != null)
			return formatter.format(this, detail);

		return switch (detail) {
		case NONE -> "";
		case LOW -> name();

		case MEDIUM -> "%s [offset=%d, length=%d]"
				.formatted(name(), offset(), length());

		case HIGH -> "%s [offset=%d, length=%d]%n%s"
				.formatted(name(), offset(), length(),
						HexStrings.toHexTextDump(buffer(),
								i -> "%04X: ".formatted(i + offset())));

		case DEBUG -> "%s [offset=%d, length=%d, payload=%d, id=%04X]"
				.formatted(name(), offset(), length(), payloadLength(), id());
		};
	}
}
