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
package com.slytechs.jnet.runtime.resource;

/**
 * The Class BindingException.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public class BindingException extends RuntimeException {
	
	/**
	 * Instantiates a new binding exception.
	 *
	 * @param msg the msg
	 */
	public BindingException(String msg) {
		super(msg);
	}

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3389422447027941144L;
}
