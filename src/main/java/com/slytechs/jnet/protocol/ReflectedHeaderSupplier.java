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
package com.slytechs.jnet.protocol;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.slytechs.jnet.protocol.packet.Header;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
class ReflectedHeaderSupplier implements HeaderSupplier {

	private final String moduleName;
	private final String className;
	private Constructor<? extends Header> constructor;

	/**
	 * @see com.slytechs.jnet.protocol.HeaderSupplier#newHeaderInstance()
	 */
	@Override
	public Header newHeaderInstance() {
		try {
			return getConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw unableToAllocateException(e);
		}
	}

	@SuppressWarnings("unchecked")
	private Constructor<? extends Header> getConstructor() {

		if (constructor == null) {
			Module module = ModuleLayer.boot()
					.findModule(moduleName)
					.orElseThrow(this::unableToAllocateException);

			try {
				this.constructor = (Constructor<? extends Header>) Class
						.forName(module, className)
						.getConstructor();
			} catch (NoSuchMethodException | SecurityException e) {
				throw unableToAllocateException(e);
			}
		}

		return constructor;
	}

	private IllegalStateException unableToAllocateException(Throwable e) throws IllegalStateException {
		return new IllegalStateException("unable to allocate header [%s in module %s]"
				.formatted(className, moduleName), e);
	}

	private IllegalStateException unableToAllocateException() throws IllegalStateException {
		return unableToAllocateException(null);
	}

	public ReflectedHeaderSupplier(String moduleName, String className) {
		this.moduleName = moduleName;
		this.className = className;
	}

}
