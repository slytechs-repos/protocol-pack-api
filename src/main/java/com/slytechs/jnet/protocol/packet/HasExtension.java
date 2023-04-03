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
public interface HasExtension<T extends Header> {

	T getExtension(T extension, int depth) throws HeaderNotFound;

	default boolean hasExtension(int extensionId) {
		return hasExtension(extensionId, 0);
	}

	boolean hasExtension(int extensionId, int depth);

	default boolean hasExtension(T extension) {
		return peekExtension(extension, 0) != null;
	}

	default boolean hasExtension(T extension, int depth) {
		return peekExtension(extension, depth) != null;
	}

	default T peekExtension(T extension) {
		return peekExtension(extension, 0);
	}

	T peekExtension(T extension, int depth);

}
