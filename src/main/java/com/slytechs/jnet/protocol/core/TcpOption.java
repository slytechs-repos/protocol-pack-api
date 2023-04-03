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
package com.slytechs.jnet.protocol.core;

import java.nio.ByteBuffer;

import com.slytechs.jnet.protocol.constants.CoreConstants;
import com.slytechs.jnet.protocol.constants.TcpOptionInfo;
import com.slytechs.jnet.protocol.packet.Header;

public class TcpOption extends Header {
	public static class TcpEndOfListOption extends TcpOption {
		public static final int ID = TcpOptionInfo.TCP_OPT_ID_EOL;

		public TcpEndOfListOption() {
			super(ID, CoreConstants.TCP_OPTION_KIND_EOL, CoreConstants.TCP_OPTION_LEN_EOL);
		}

		@Override
		public int length() {
			return 1;
		}
	}

	public static class TcpFastOpenOption extends TcpOption {
		public static final int ID = TcpOptionInfo.TCP_OPT_ID_FASTOPEN;

		public TcpFastOpenOption() {
			super(ID, CoreConstants.TCP_OPTION_KIND_FASTOPEN, CoreConstants.TCP_OPTION_LEN_FASTOPEN);
		}

		public byte[] cookie(byte[] array, int offset) {
			buffer().get(2, array, offset, 16);

			return array;
		}

	}

	public static class TcpMSSOption extends TcpOption {
		public static final int ID = TcpOptionInfo.TCP_OPT_ID_MSS;

		public TcpMSSOption() {
			super(ID, CoreConstants.TCP_OPTION_KIND_MSS, CoreConstants.TCP_OPTION_LEN_MSS);
		}

		public int mss() {
			return Short.toUnsignedInt(buffer().getShort(2));
		}

	}

	public static class TcpSelectiveAckOption extends TcpOption {
		public static final int ID = TcpOptionInfo.TCP_OPT_ID_SACK;

		public TcpSelectiveAckOption() {
			super(ID, CoreConstants.TCP_OPTION_KIND_SACK);
		}

		@Override
		public int length() {
			return super.length() << 3;
		}

		public int count() {
			return super.length();
		}

		public int blockAt(int index) {
			return buffer().getInt(2 + (index << 3));
		}

		public int[] toArray() {
			int[] array = new int[count()];

			return toArray(array, 0);
		}

		public int[] toArray(int[] array) {
			return toArray(array, 0);
		}

		public int[] toArray(int[] array, int offset) {
			ByteBuffer buffer = buffer();

			for (int i = 0; i < array.length; i++)
				array[i] = buffer.getInt(2 + (i << 3));

			return array;
		}

	}

	public static class TcpNoOption extends TcpOption {
		public static final int ID = TcpOptionInfo.TCP_OPT_ID_NOP;

		public TcpNoOption() {
			super(ID, CoreConstants.TCP_OPTION_KIND_NOP, CoreConstants.TCP_OPTION_LEN_NOP);
		}

		@Override
		public int length() {
			return 1;
		}
	};

	public static class TcpTimestampOption extends TcpOption {
		public static final int ID = TcpOptionInfo.TCP_OPT_ID_TIMESTAMP;

		public TcpTimestampOption() {
			super(ID, CoreConstants.TCP_OPTION_KIND_TIMESTAMP, CoreConstants.TCP_OPTION_LEN_TIMESTAMP);
		}

		public long timestamp() {
			return Integer.toUnsignedLong(buffer().getInt(2));
		}

		public long timestampEchoReply() {
			return Integer.toUnsignedLong(buffer().getInt(6));
		}

	};

	public static class TcpWindowScaleOption extends TcpOption {
		public static final int ID = TcpOptionInfo.TCP_OPT_ID_WIN_SCALE;

		public TcpWindowScaleOption() {
			super(ID, CoreConstants.TCP_OPTION_KIND_WIN_SCALE, CoreConstants.TCP_OPTION_LEN_WIN_SCALE);
		}

		public int scaleWindow(int window) {
			return window << bitShift();
		}

		public int bitShift() {
			return Byte.toUnsignedInt(buffer().get(3));
		}

	};

	private final int kind;
	private final int length;;

	public TcpOption(int id, int kind) {
		super(id);
		this.kind = kind;
		this.length = -1; // Dynamic length
	}

	public TcpOption(int id, int kind, int constantLength) {
		super(id);
		this.kind = kind;
		this.length = constantLength;
	}

	public int kind() {
		return kind;
	}

	public void kind(int kind) {
		buffer().put(CoreConstants.TCP_OPTION_FIELD_KIND, (byte) kind);
	}

	@Override
	public int length() {
		return (length != -1)
				? length
				: Byte.toUnsignedInt(buffer().get(CoreConstants.TCP_OPTION_FIELD_LENGTH));
	}

	public void length(int length) {
		buffer().put(CoreConstants.TCP_OPTION_FIELD_LENGTH, (byte) kind);
	}

}