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

import com.slytechs.jnet.protocol.constants.CoreHeaderInfo;
import com.slytechs.jnet.protocol.packet.meta.Meta;
import com.slytechs.jnet.protocol.packet.meta.MetaResource;

/**
 * 
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
@MetaResource("payload-meta.json")
public sealed class Payload extends Header permits Other {
	public static final int ID = CoreHeaderInfo.CORE_ID_PAYLOAD;

	public Payload() {
		super(ID);
	}

	@Meta
	public byte[] data() {
		byte[] array = new byte[length()];

		data(array);

		return array;
	}

	public int data(byte[] dst) {
		return data(dst, 0, dst.length);
	}

	public int data(byte[] dst, int offset, int length) {
		if (offset + length > length())
			length = length() - offset;

		buffer().get(dst, offset, length);

		return length;
	}

}
