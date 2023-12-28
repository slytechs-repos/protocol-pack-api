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
package com.slytechs.jnet.jnetruntime.util;

/**
 * Thrown when data access across a byte array, ByteBuffer or native memory
 * segment is determined to be missaligned.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public class DataAlignmentException extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7871196235551105601L;

	/**
	 * Instantiates a new data alignment exception.
	 *
	 * @param msg the msg
	 */
	public DataAlignmentException(String msg) {
		super(msg);
	}

}
