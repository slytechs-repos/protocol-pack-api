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
package com.slytechs.protocol.runtime.internal.util.format;

import java.util.Optional;

import com.slytechs.protocol.runtime.internal.util.ByteArraySlice;

/**
 * The Interface FormatProperties.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author mark
 */
public interface FormatProperties {

	/**
	 * Find property.
	 *
	 * @param name the name
	 * @return the optional
	 */
	default Optional<FormatProperty> findProperty(String name) {
		return Optional.ofNullable(getProperty(name, Optional.empty()));
	}

	/**
	 * Find property.
	 *
	 * @param name  the name
	 * @param slice the slice
	 * @return the optional
	 */
	default Optional<FormatProperty> findProperty(String name, Optional<ByteArraySlice> slice) {
		return Optional.ofNullable(getProperty(name, slice));
	}

	/**
	 * Find property.
	 *
	 * @param name  the name
	 * @param slice the slice
	 * @return the optional
	 */
	default Optional<FormatProperty> findProperty(String name, ByteArraySlice slice) {
		return Optional.ofNullable(getProperty(name, Optional.ofNullable(slice)));
	}

	/**
	 * Gets the property.
	 *
	 * @param name the name
	 * @return the property
	 */
	default FormatProperty getProperty(String name) {
		return getProperty(name, Optional.empty());
	}

	/**
	 * Gets the property.
	 *
	 * @param name  the name
	 * @param slice the slice
	 * @return the property
	 */
	FormatProperty getProperty(String name, Optional<ByteArraySlice> slice);
}
