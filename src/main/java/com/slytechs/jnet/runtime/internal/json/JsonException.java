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
package com.slytechs.jnet.runtime.internal.json;

/**
 * The Class JsonException.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public class JsonException extends Exception {

	/**
	 * Instantiates a new json exception.
	 */
	public JsonException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Instantiates a new json exception.
	 *
	 * @param message the message
	 */
	public JsonException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Instantiates a new json exception.
	 *
	 * @param cause the cause
	 */
	public JsonException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Instantiates a new json exception.
	 *
	 * @param message the message
	 * @param cause   the cause
	 */
	public JsonException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Instantiates a new json exception.
	 *
	 * @param message            the message
	 * @param cause              the cause
	 * @param enableSuppression  the enable suppression
	 * @param writableStackTrace the writable stack trace
	 */
	public JsonException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
