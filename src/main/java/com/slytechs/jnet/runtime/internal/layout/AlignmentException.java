/*
 * MIT License
 * 
 * Copyright (c) 2020 Sly Technologies Inc.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.slytechs.jnet.runtime.internal.layout;

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
