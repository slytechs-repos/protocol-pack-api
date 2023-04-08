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

/**
 * The Interface HasHeader.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public interface HasHeader {

	/**
	 * Gets the header.
	 *
	 * @param <T>    the generic type
	 * @param header the header
	 * @return the header
	 * @throws HeaderNotFound the header not found
	 */
	default <T extends Header> T getHeader(T header) throws HeaderNotFound {
		return getHeader(header, 0);
	}

	/**
	 * Gets the header.
	 *
	 * @param <T>    the generic type
	 * @param header the header
	 * @param depth  the depth
	 * @return the header
	 * @throws HeaderNotFound the header not found
	 */
	<T extends Header> T getHeader(T header, int depth) throws HeaderNotFound;

	/**
	 * Checks for header.
	 *
	 * @param headerId the header id
	 * @return true, if successful
	 */
	default boolean hasHeader(int headerId) {
		return hasHeader(headerId, 0);
	}

	/**
	 * Checks for header.
	 *
	 * @param headerId the header id
	 * @param depth    the depth
	 * @return true, if successful
	 */
	boolean hasHeader(int headerId, int depth);

	/**
	 * Checks for header.
	 *
	 * @param <T>    the generic type
	 * @param header the header
	 * @return true, if successful
	 */
	default <T extends Header> boolean hasHeader(T header) {
		return peekHeader(header, 0) != null;
	}

	/**
	 * Checks for header.
	 *
	 * @param <T>    the generic type
	 * @param header the header
	 * @param depth  the depth
	 * @return true, if successful
	 */
	default <T extends Header> boolean hasHeader(T header, int depth) {
		return peekHeader(header, depth) != null;
	}

	/**
	 * Peek header.
	 *
	 * @param <T>    the generic type
	 * @param header the header
	 * @return the t
	 */
	default <T extends Header> T peekHeader(T header) {
		return peekHeader(header, 0);
	}

	/**
	 * Peek header.
	 *
	 * @param <T>    the generic type
	 * @param header the header
	 * @param depth  the depth
	 * @return the t
	 */
	<T extends Header> T peekHeader(T header, int depth);

}
