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
package com.slytechs.protocol;

import java.nio.ByteBuffer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

import com.slytechs.protocol.meta.Meta;
import com.slytechs.protocol.meta.Meta.MetaType;
import com.slytechs.protocol.meta.PacketFormat;
import com.slytechs.protocol.runtime.MemoryBinding;
import com.slytechs.protocol.runtime.util.Detail;
import com.slytechs.protocol.runtime.util.DetailedString;
import com.slytechs.protocol.runtime.util.HexStrings;

/**
 * A "reusable" protocol header that can be bound and unbound to data in memory.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public abstract class Header extends MemoryBinding implements DetailedString {

	/** The lock factory */
	private static Supplier<Lock> LOCK_FACTORY = ReentrantLock::new;

	/** The numerical protocol header id assigned at "pack" level. */
	protected final int id;

	/** A multipurpose lock, lazily allocated if needed */
	private volatile Lock lock; // Lazy & concurrent allocation

	/** Some important header attributes */
	private int offset, length, payloadLength;

	/**
	 * A pretty pring formatted, if will be used to generated {@code toString()}
	 * output
	 */
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
	 * Bind header extensions and options, but only if HeaderExtension is a
	 * subclass, otherwise do nothing.
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
	 * Gets a header specific lock for synchronizing access to header fields. The
	 * lock is initialized lazily and only when requested, otherwise the lock field
	 * remains uninitialized.
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
	 * The protocol header's unique numerical ID assigned at the pack level.
	 *
	 * @return the id
	 */
	@Meta(MetaType.ATTRIBUTE)
	public final int id() {
		return id;
	}

	/**
	 * The header length.
	 *
	 * @return length in bytes
	 */
	@Meta(MetaType.ATTRIBUTE)
	public int length() {
		return length;
	}

	/**
	 * Name of this protocol header. If a specific header is not assigned, the
	 * method defaults to acquiring a name from the sub-class classname.
	 *
	 * @return the string
	 */
	@Meta(MetaType.ATTRIBUTE)
	public String name() {
		return getClass().getSimpleName();
	}

	/**
	 * Offset of the begining of this header from the start of the packet.
	 *
	 * @return offset in bytes
	 */
	@Meta(MetaType.ATTRIBUTE)
	public final int offset() {
		return offset;
	}

	/**
	 * @see com.slytechs.protocol.runtime.MemoryBinding#onUnbind()
	 */
	@Override
	protected void onUnbind() {
		offset = length = payloadLength = 0;
	}

	/**
	 * Calculated payload data length for this protocol.
	 *
	 * @return length in bytes
	 */
	@Meta(MetaType.ATTRIBUTE)
	public int payloadLength() {
		return payloadLength;
	}

	/**
	 * Offset to the start of the payload for this specific protocol from the start
	 * of the packet.
	 *
	 * @return offset in bytes
	 */
	@Meta(MetaType.ATTRIBUTE)
	public int payloadOffset() {
		return offset + length;
	}

	/**
	 * Sets a new data formatter for this protocol header to use in generating
	 * {@link #toString()} method output. If the formatter is not set, the
	 * {@link #toString()} method defaults to minimum header state information such
	 * as offset, length, etc...
	 *
	 * @param formatter the new formatter
	 */
	void setFormatter(PacketFormat formatter) {
		this.formatter = formatter;
	}

	/**
	 * Generates a simple string representing the state of this header or uses the
	 * formatter if set.
	 *
	 * @return the generated string from the fields of this header
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		return toString(Detail.DEFAULT);
	}

	/**
	 * Generates a simple string representing the state of this header or uses the
	 * formatter if set.
	 *
	 * @param detail generates the header string at specified detail level
	 * @return the generated string from the fields of this header
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString(Detail detail) {
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