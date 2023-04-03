/*
 * Apache License, Version 2.0
 * 
 * Copyright 2013-2022 Sly Technologies Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.slytechs.jnet.runtime.util;

import java.util.Arrays;

/**
 * The Class NotFound.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author mark
 */
public class NotFound extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6006575620166935617L;

	/**
	 * Instantiates a new not found.
	 */
	public NotFound() {
	}

	/**
	 * Instantiates a new not found.
	 *
	 * @param message the message
	 */
	public NotFound(String message) {
		super(message);
	}

	public NotFound(String... path) {
		super(Arrays.asList(path).toString());
	}
}
