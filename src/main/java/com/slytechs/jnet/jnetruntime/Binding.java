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
package com.slytechs.jnet.jnetruntime;

/**
 * A opaque binding to a reusable object. The most used binding type is by
 * {@link MemoryBinding} which binds data to protocol specific objects such as
 * packets and headers.
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public interface Binding {

	/**
	 * Unbinds the reusable object and make it available for another binding.
	 */
	void unbind();

	/**
	 * Checks if this reusable object is bound.
	 *
	 * @return true, if is bound
	 */
	boolean isBound();
}
