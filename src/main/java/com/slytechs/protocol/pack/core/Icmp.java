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
package com.slytechs.protocol.pack.core;

import java.util.concurrent.locks.Lock;

import com.slytechs.protocol.Header;

/**
 * The Class Icmp.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public class Icmp extends Header {

	/**
	 * Instantiates a new icmp.
	 *
	 * @param id the id
	 */
	protected Icmp(int id) {
		super(id);
	}

	/**
	 * Instantiates a new icmp.
	 *
	 * @param id   the id
	 * @param lock the lock
	 */
	protected Icmp(int id, Lock lock) {
		super(id, lock);
	}

}
