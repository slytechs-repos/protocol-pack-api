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
package com.slytechs.jnet.jnetruntime.internal.layout;

/**
 * The Class AlignmentException.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public class AlignmentException extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7404793075584701674L;

	/**
	 * Instantiates a new alignment exception.
	 */
	public AlignmentException() {
	}

	/**
	 * Instantiates a new alignment exception.
	 *
	 * @param message the message
	 */
	public AlignmentException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new alignment exception.
	 *
	 * @param cause the cause
	 */
	public AlignmentException(Throwable cause) {
		super(cause);
	}

	/**
	 * Instantiates a new alignment exception.
	 *
	 * @param message the message
	 * @param cause   the cause
	 */
	public AlignmentException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new alignment exception.
	 *
	 * @param message            the message
	 * @param cause              the cause
	 * @param enableSuppression  the enable suppression
	 * @param writableStackTrace the writable stack trace
	 */
	public AlignmentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
