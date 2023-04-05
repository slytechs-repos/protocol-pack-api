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
package com.slytechs.jnet.protocol.packet;

import java.nio.ByteBuffer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

import com.slytechs.jnet.protocol.packet.meta.Meta;
import com.slytechs.jnet.protocol.packet.meta.Meta.MetaType;
import com.slytechs.jnet.protocol.packet.meta.PacketFormat;
import com.slytechs.jnet.runtime.resource.MemoryBinding;
import com.slytechs.jnet.runtime.util.Detail;
import com.slytechs.jnet.runtime.util.HexStrings;

/**
 * 
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public abstract class Header extends MemoryBinding {

	private static Supplier<Lock> LOCK_FACTORY = ReentrantLock::new;

	protected final int id;
	private volatile Lock lock; // Lazy & concurrent allocation

	private int offset, length, payloadLength;
	private PacketFormat formatter;

	protected Header(int id) {
		this.id = id;
	}

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

	final void bindHeaderToPacket(ByteBuffer packet, int offset, int length, int payloadLength) {
		this.offset = offset;
		this.length = length;
		this.payloadLength = payloadLength;

		super.bind(packet.slice(offset, length));
	}

	public final Lock getLock() {
		if (this.lock == null) { // Lazy allocate
			Lock lock = LOCK_FACTORY.get(); // get on local thread context (allows multiple threads)

			while (this.lock == null) // No lock volatile initialization, only 1 thread wins
				this.lock = lock;
		}

		return this.lock;
	}

	@Meta(MetaType.ATTRIBUTE)
	public final int id() {
		return id;
	}

	@Meta(MetaType.ATTRIBUTE)
	public int length() {
		return length;
	}

	@Meta(MetaType.ATTRIBUTE)
	public String name() {
		return getClass().getSimpleName();
	}

	@Meta(MetaType.ATTRIBUTE)
	public final int offset() {
		return offset;
	}

	/**
	 * @see com.slytechs.jnet.runtime.resource.MemoryBinding#onUnbind()
	 */
	@Override
	protected void onUnbind() {
		offset = length = payloadLength = 0;
	}

	@Meta(MetaType.ATTRIBUTE)
	public int payloadLength() {
		return payloadLength;
	}

	@Meta(MetaType.ATTRIBUTE)
	public int payloadOffset() {
		return offset + length;
	}

	void setFormatter(PacketFormat formatter) {
		this.formatter = formatter;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return toString(Detail.MEDIUM);
	}

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
