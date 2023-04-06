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
 * The Interface HeaderLookup.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public interface HeaderLookup {

	/**
	 * Checks if is header extension supported.
	 *
	 * @return true, if is header extension supported
	 */
	boolean isHeaderExtensionSupported();

	/**
	 * List headers.
	 *
	 * @return the long[]
	 */
	long[] listHeaders();

	/**
	 * Lookup header.
	 *
	 * @param id    the id
	 * @param depth the depth
	 * @return the long
	 */
	long lookupHeader(int id, int depth);

	/**
	 * Lookup header extension.
	 *
	 * @param headerId        the header id
	 * @param extId           the ext id
	 * @param depth           the depth
	 * @param recordIndexHint the record index hint
	 * @return the long
	 */
	long lookupHeaderExtension(int headerId, int extId, int depth, int recordIndexHint);

}
