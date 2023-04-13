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
package com.slytechs.protocol.pack.core;

import static com.slytechs.protocol.pack.core.constants.CoreConstants.*;

import java.nio.ByteBuffer;

import com.slytechs.protocol.Header;
import com.slytechs.protocol.meta.Meta;
import com.slytechs.protocol.pack.core.constants.CoreConstants;
import com.slytechs.protocol.pack.core.constants.TcpOptionInfo;

/**
 * The Class TcpOption.
 */
public class TcpOption extends Header {

	/**
	 * The Class TcpEndOfListOption.
	 */
	@Meta
	public static class TcpEndOfListOption extends TcpOption {

		/** The Constant ID. */
		public static final int ID = TcpOptionInfo.TCP_OPT_ID_EOL;

		/**
		 * Instantiates a new tcp end of list option.
		 */
		public TcpEndOfListOption() {
			super(ID, CoreConstants.TCP_OPTION_KIND_EOL, CoreConstants.TCP_OPTION_LEN_EOL);
		}

		/**
		 * Length.
		 *
		 * @return the int
		 * @see com.slytechs.protocol.pack.core.TcpOption#length()
		 */
		@Override
		public int length() {
			return 1;
		}
	}

	/**
	 * The Class TcpFastOpenOption.
	 */
	@Meta
	public static class TcpFastOpenOption extends TcpOption {

		/** The Constant ID. */
		public static final int ID = TcpOptionInfo.TCP_OPT_ID_FASTOPEN;

		/**
		 * Instantiates a new tcp fast open option.
		 */
		public TcpFastOpenOption() {
			super(ID, CoreConstants.TCP_OPTION_KIND_FASTOPEN, CoreConstants.TCP_OPTION_LEN_FASTOPEN);
		}

		/**
		 * Cookie.
		 *
		 * @param array  the array
		 * @param offset the offset
		 * @return the byte[]
		 */
		public byte[] cookie(byte[] array, int offset) {
			buffer().get(2, array, offset, 16);

			return array;
		}

	}

	/**
	 * The Class TcpMSSOption.
	 */
	@Meta
	public static class TcpMSSOption extends TcpOption {

		/** The Constant ID. */
		public static final int ID = TcpOptionInfo.TCP_OPT_ID_MSS;

		/**
		 * Instantiates a new tcp MSS option.
		 */
		public TcpMSSOption() {
			super(ID, CoreConstants.TCP_OPTION_KIND_MSS, CoreConstants.TCP_OPTION_LEN_MSS);
		}

		/**
		 * Mss.
		 *
		 * @return the int
		 */
		public int mss() {
			return Short.toUnsignedInt(buffer().getShort(2));
		}

	}

	/**
	 * The Class TcpSelectiveAckOption.
	 */
	@Meta
	public static class TcpSelectiveAckOption extends TcpOption {

		/** The Constant ID. */
		public static final int ID = TcpOptionInfo.TCP_OPT_ID_SACK;

		/**
		 * Instantiates a new tcp selective ack option.
		 */
		public TcpSelectiveAckOption() {
			super(ID, CoreConstants.TCP_OPTION_KIND_SACK);
		}

		/**
		 * Length.
		 *
		 * @return the int
		 * @see com.slytechs.protocol.pack.core.TcpOption#length()
		 */
		@Override
		public int length() {
			return super.length() << 3;
		}

		/**
		 * Count.
		 *
		 * @return the int
		 */
		public int count() {
			return super.length();
		}

		/**
		 * Block at.
		 *
		 * @param index the index
		 * @return the int
		 */
		public int blockAt(int index) {
			return buffer().getInt(2 + (index << 3));
		}

		/**
		 * To array.
		 *
		 * @return the int[]
		 */
		public int[] toArray() {
			int[] array = new int[count()];

			return toArray(array, 0);
		}

		/**
		 * To array.
		 *
		 * @param array the array
		 * @return the int[]
		 */
		public int[] toArray(int[] array) {
			return toArray(array, 0);
		}

		/**
		 * To array.
		 *
		 * @param array  the array
		 * @param offset the offset
		 * @return the int[]
		 */
		public int[] toArray(int[] array, int offset) {
			ByteBuffer buffer = buffer();

			for (int i = 0; i < array.length; i++)
				array[i] = buffer.getInt(2 + (i << 3));

			return array;
		}

	}

	/**
	 * The Class TcpNoOption.
	 */
	@Meta
	public static class TcpNoOption extends TcpOption {

		/** The Constant ID. */
		public static final int ID = TcpOptionInfo.TCP_OPT_ID_NOP;

		/**
		 * Instantiates a new tcp no option.
		 */
		public TcpNoOption() {
			super(ID, TCP_OPTION_KIND_NOP, TCP_OPTION_LEN_NOP);
		}

		/**
		 * Length.
		 *
		 * @return the int
		 * @see com.slytechs.protocol.pack.core.TcpOption#length()
		 */
		@Override
		public int length() {
			return 1;
		}
	};

	/**
	 * The Class TcpTimestampOption.
	 */
	@Meta
	public static class TcpTimestampOption extends TcpOption {

		/** The Constant ID. */
		public static final int ID = TcpOptionInfo.TCP_OPT_ID_TIMESTAMP;

		/**
		 * Instantiates a new tcp timestamp option.
		 */
		public TcpTimestampOption() {
			super(ID, TCP_OPTION_KIND_TIMESTAMP, TCP_OPTION_LEN_TIMESTAMP);
		}

		/**
		 * Timestamp.
		 *
		 * @return the long
		 */
		public long timestamp() {
			return Integer.toUnsignedLong(buffer().getInt(2));
		}

		/**
		 * Timestamp echo reply.
		 *
		 * @return the long
		 */
		public long timestampEchoReply() {
			return Integer.toUnsignedLong(buffer().getInt(6));
		}

	};

	/**
	 * The Class TcpWindowScaleOption.
	 */
	@Meta
	public static class TcpWindowScaleOption extends TcpOption {

		/** The Constant ID. */
		public static final int ID = TcpOptionInfo.TCP_OPT_ID_WIN_SCALE;

		/**
		 * Instantiates a new tcp window scale option.
		 */
		public TcpWindowScaleOption() {
			super(ID, TCP_OPTION_KIND_WIN_SCALE, TCP_OPTION_LEN_WIN_SCALE);
		}

		/**
		 * Scale window.
		 *
		 * @param window the window
		 * @return the int
		 */
		public int scaleWindow(int window) {
			return window << shiftCount();
		}

		/**
		 * Bit shift.
		 *
		 * @return the int
		 */
		public int shiftCount() {
			return Byte.toUnsignedInt(buffer().get(TCP_OPTION_FIELD_DATA));
		}

	};

	/** The kind. */
	private final int kind;

	/** The length. */
	private final int length;;

	/**
	 * Instantiates a new tcp option.
	 *
	 * @param id   the id
	 * @param kind the kind
	 */
	public TcpOption(int id, int kind) {
		super(id);
		this.kind = kind;
		this.length = -1; // Dynamic length
	}

	/**
	 * Instantiates a new tcp option.
	 *
	 * @param id             the id
	 * @param kind           the kind
	 * @param constantLength the constant length
	 */
	public TcpOption(int id, int kind, int constantLength) {
		super(id);
		this.kind = kind;
		this.length = constantLength;
	}

	/**
	 * Kind.
	 *
	 * @return the int
	 */
	@Meta
	public int kind() {
		return kind;
	}

	/**
	 * Kind.
	 *
	 * @param kind the kind
	 */
	public void kind(int kind) {
		buffer().put(CoreConstants.TCP_OPTION_FIELD_KIND, (byte) kind);
	}

	/**
	 * Length.
	 *
	 * @return the int
	 */
	@Meta
	public int length() {
		return ((length != -1)
				? length
				: Byte.toUnsignedInt(buffer().get(TCP_OPTION_FIELD_LENGTH)));
	}

	/**
	 * Length.
	 *
	 * @param length the length
	 */
	public void length(int length) {
		buffer().put(TCP_OPTION_FIELD_LENGTH, (byte) kind);
	}

}