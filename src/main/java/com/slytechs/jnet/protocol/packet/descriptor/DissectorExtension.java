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
package com.slytechs.jnet.protocol.packet.descriptor;

import java.nio.ByteBuffer;

/**
 * The Interface DissectorExtension.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public interface DissectorExtension {

	/** The empty. */
	DissectorExtension EMPTY = null;

	/**
	 * Dissect L 2.
	 *
	 * @param dlt    the dlt
	 * @param buffer the buffer
	 * @param offset the offset
	 * @return the int
	 */
	int dissectL2(int dlt, ByteBuffer buffer, int offset);
}
