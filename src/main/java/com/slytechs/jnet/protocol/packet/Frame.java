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

import java.util.function.LongSupplier;

import com.slytechs.jnet.protocol.constants.CoreHeaderInfo;
import com.slytechs.jnet.protocol.packet.descriptor.PacketDescriptor;
import com.slytechs.jnet.protocol.packet.meta.Meta;
import com.slytechs.jnet.protocol.packet.meta.MetaResource;

/**
 * 
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
@MetaResource("frame-meta.json") // in src/main/resource
public final class Frame extends Header {
	public static final int ID = CoreHeaderInfo.CORE_ID_FRAME;

	public interface FrameNumber {

		static FrameNumber of() {
			return starting(0);
		}

		static FrameNumber starting(long startingNo, LongSupplier onRollOver) {
			return new FrameNumber() {

				long frameNo = startingNo;

				@Override
				public long getAndIncrement() {
					if (frameNo == Long.MAX_VALUE)
						return frameNo = onRollOver.getAsLong();

					return frameNo++;
				}

			};
		}

		static FrameNumber starting(long startingNo) {
			return starting(startingNo, () -> 0);
		}

		long getAndIncrement();

		default long getUsing(long timestamp, int portNo) {
			return getAndIncrement();
		}
	}

	@Meta
	public long frameNo() {
		return descriptor.frameNo();
	}

	private PacketDescriptor descriptor;

	public Frame() {
		super(ID);
	}

	@Meta
	public long timestamp() {
		return descriptor.timestamp();
	}

	@Meta
	public int wireLength() {
		return descriptor.wireLength();
	}

	@Meta
	public int captureLength() {
		return descriptor.captureLength();
	}

	void bindDescriptor(PacketDescriptor descriptor) {
		this.descriptor = descriptor;
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.Header#onUnbind()
	 */
	@Override
	protected void onUnbind() {
		descriptor = null;
	}

}
