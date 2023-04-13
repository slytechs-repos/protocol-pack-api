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
package com.slytechs.protocol.pack.core.constants;

import com.slytechs.protocol.HeaderExtension;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 *
 */
public class Icmp4Option extends HeaderExtension<Icmp4Option> {

	protected Icmp4Option(int id) {
		super(id);
	}

	public static class Icmp4OptEchoRequest extends Icmp4Option {

		/**
		 * @param id
		 */
		protected Icmp4OptEchoRequest(int id) {
			super(id);
			// TODO Auto-generated constructor stub
		}

	}

}
