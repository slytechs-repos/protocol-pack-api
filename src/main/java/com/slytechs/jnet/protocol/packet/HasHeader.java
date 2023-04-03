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

/**
 * 
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public interface HasHeader {

	default <T extends Header> T getHeader(T header) throws HeaderNotFound {
		return getHeader(header, 0);
	}

	<T extends Header> T getHeader(T header, int depth) throws HeaderNotFound;

	default boolean hasHeader(int headerId) {
		return hasHeader(headerId, 0);
	}

	boolean hasHeader(int headerId, int depth);

	default <T extends Header> boolean hasHeader(T header) {
		return peekHeader(header, 0) != null;
	}

	default <T extends Header> boolean hasHeader(T header, int depth) {
		return peekHeader(header, depth) != null;
	}

	default <T extends Header> T peekHeader(T header) {
		return peekHeader(header, 0);
	}

	<T extends Header> T peekHeader(T header, int depth);

}
