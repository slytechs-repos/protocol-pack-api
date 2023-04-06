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
package com.slytechs.jnet.protocol.packet.descriptor;

/**
 * The Class JavaDissector.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public abstract class JavaDissector extends AbstractDissector {

	/**
	 * Instantiates a new java dissector.
	 */
	public JavaDissector() {
	}

	/**
	 * Checks if is native.
	 *
	 * @return true, if is native
	 * @see com.slytechs.jnet.protocol.packet.descriptor.PacketDissector#isNative()
	 */
	@Override
	public boolean isNative() {
		return false;
	}

	/**
	 * Destroy dissector.
	 */
	protected abstract void destroyDissector();
}
