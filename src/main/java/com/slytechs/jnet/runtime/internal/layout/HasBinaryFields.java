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
package com.slytechs.jnet.runtime.internal.layout;

import java.nio.ByteBuffer;
import java.util.stream.Stream;

/**
 * The Interface HasBinaryFields.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public interface HasBinaryFields {

	/**
	 * Buffer.
	 *
	 * @return the byte buffer
	 */
	default ByteBuffer buffer() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Stream all fields formatted.
	 *
	 * @return the stream
	 */
	default Stream<String> streamAllFieldsFormatted() {
		return streamAllFields()
				.map(f -> f.toString(buffer()));
	}

	/**
	 * Stream all fields.
	 *
	 * @return the stream
	 */
	Stream<BinaryField> streamAllFields();
}
