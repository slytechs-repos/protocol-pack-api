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

import java.util.function.LongSupplier;

import com.slytechs.jnet.protocol.core.constants.CoreHeaderInfo;
import com.slytechs.jnet.protocol.descriptor.PacketDescriptor;
import com.slytechs.jnet.protocol.packet.meta.Meta;
import com.slytechs.jnet.protocol.packet.meta.MetaResource;

/**
 * The Class Frame.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
@MetaResource("frame-meta.json") // in src/main/resource
public final class Frame extends Header {
	
	/** The Constant ID. */
	public static final int ID = CoreHeaderInfo.CORE_ID_FRAME;

	/**
	 * The Interface FrameNumber.
	 */
	public interface FrameNumber {

		/**
		 * Of.
		 *
		 * @return the frame number
		 */
		static FrameNumber of() {
			return starting(0);
		}

		/**
		 * Starting.
		 *
		 * @param startingNo the starting no
		 * @param onRollOver the on roll over
		 * @return the frame number
		 */
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

		/**
		 * Starting.
		 *
		 * @param startingNo the starting no
		 * @return the frame number
		 */
		static FrameNumber starting(long startingNo) {
			return starting(startingNo, () -> 0);
		}

		/**
		 * Gets the and increment.
		 *
		 * @return the and increment
		 */
		long getAndIncrement();

		/**
		 * Gets the using.
		 *
		 * @param timestamp the timestamp
		 * @param portNo    the port no
		 * @return the using
		 */
		default long getUsing(long timestamp, int portNo) {
			return getAndIncrement();
		}
	}

	/**
	 * Frame no.
	 *
	 * @return the long
	 */
	@Meta
	public long frameNo() {
		return descriptor.frameNo();
	}

	/** The descriptor. */
	private PacketDescriptor descriptor;

	/**
	 * Instantiates a new frame.
	 */
	public Frame() {
		super(ID);
	}

	/**
	 * Timestamp.
	 *
	 * @return the long
	 */
	@Meta
	public long timestamp() {
		return descriptor.timestamp();
	}

	/**
	 * Wire length.
	 *
	 * @return the int
	 */
	@Meta
	public int wireLength() {
		return descriptor.wireLength();
	}

	/**
	 * Capture length.
	 *
	 * @return the int
	 */
	@Meta
	public int captureLength() {
		return descriptor.captureLength();
	}

	/**
	 * Bind descriptor.
	 *
	 * @param descriptor the descriptor
	 */
	void bindDescriptor(PacketDescriptor descriptor) {
		this.descriptor = descriptor;
	}

	/**
	 * On unbind.
	 *
	 * @see com.slytechs.jnet.protocol.Header#onUnbind()
	 */
	@Override
	protected void onUnbind() {
		descriptor = null;
	}

}
