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
package com.slytechs.protocol.runtime.internal.util.function;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public final class ReflectedSupplier<T> implements Supplier<T> {

	private final String moduleName;
	private final String className;
	private Constructor<T> constructor;

	public ReflectedSupplier(String moduleName, String className) {
		this.moduleName = moduleName;
		this.className = className;

	}

	@SuppressWarnings("unchecked")
	private Constructor<T> createConstructor() throws ClassNotFoundException, NoSuchMethodException,
			SecurityException {

		Optional<Module> module = ModuleLayer.boot()
				.findModule(moduleName);

		Class<T> clazz;
		if (module.isPresent())
			clazz = (Class<T>) Class
					.forName(module.get(), className);
		else
			clazz = (Class<T>) Class
					.forName(className);

		if (clazz == null)
			throw new IllegalStateException("descriptor not found [%s, %s]"
					.formatted(moduleName, className));

		return clazz.getConstructor();
	}

	/**
	 * @see java.util.function.Supplier#get()
	 */
	@Override
	public T get() {
		try {
			if (constructor == null)
				constructor = createConstructor();

			return constructor.newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | ClassNotFoundException | NoSuchMethodException | SecurityException e) {
			throw new IllegalStateException("class not found [%s, %s]"
					.formatted(moduleName, className), e);
		}
	}

}
