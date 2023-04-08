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
 * The Interface HasExtension.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 * @param <T> the generic type
 */
public interface HasExtension<T extends Header> {

	/**
	 * Gets the extension.
	 *
	 * @param extension the extension
	 * @param depth     the depth
	 * @return the extension
	 * @throws HeaderNotFound the header not found
	 */
	T getExtension(T extension, int depth) throws HeaderNotFound;

	/**
	 * Checks for extension.
	 *
	 * @param extensionId the extension id
	 * @return true, if successful
	 */
	default boolean hasExtension(int extensionId) {
		return hasExtension(extensionId, 0);
	}

	/**
	 * Checks for extension.
	 *
	 * @param extensionId the extension id
	 * @param depth       the depth
	 * @return true, if successful
	 */
	boolean hasExtension(int extensionId, int depth);

	/**
	 * Checks for extension.
	 *
	 * @param extension the extension
	 * @return true, if successful
	 */
	default boolean hasExtension(T extension) {
		return peekExtension(extension, 0) != null;
	}

	/**
	 * Checks for extension.
	 *
	 * @param extension the extension
	 * @param depth     the depth
	 * @return true, if successful
	 */
	default boolean hasExtension(T extension, int depth) {
		return peekExtension(extension, depth) != null;
	}

	/**
	 * Peek extension.
	 *
	 * @param extension the extension
	 * @return the t
	 */
	default T peekExtension(T extension) {
		return peekExtension(extension, 0);
	}

	/**
	 * Peek extension.
	 *
	 * @param extension the extension
	 * @param depth     the depth
	 * @return the t
	 */
	T peekExtension(T extension, int depth);

}
