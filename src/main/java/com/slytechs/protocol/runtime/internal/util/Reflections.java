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
package com.slytechs.protocol.runtime.internal.util;

import java.util.Optional;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 *
 */
public final class Reflections {

	private Reflections() {
	}

	@SuppressWarnings("unchecked")
	public static <T> Class<T> loadClass(String moduleName, String className) throws ClassNotFoundException {

		if (className == null)
			return null;

		Optional<Module> module = Optional.empty();
		if (moduleName != null)
			module = ModuleLayer.boot()
					.findModule(moduleName);

		Class<T> clazz;
		if (module.isPresent())
			clazz = (Class<T>) Class
					.forName(module.get(), className);
		else
			clazz = (Class<T>) Class
					.forName(className);

		return clazz;
	}

	public static Module loadModule(String moduleName) {
		Optional<Module> module = Optional.empty();
		if (moduleName != null)
			module = ModuleLayer.boot()
					.findModule(moduleName);

		return module.orElse(null);
	}

	@SuppressWarnings("unchecked")
	public static <T> Class<T> loadClass(Module module, String className) throws ClassNotFoundException {

		if (className == null)
			throw new ClassNotFoundException("null classname");

		Class<T> clazz;
		if (module != null)
			clazz = (Class<T>) Class.forName(module, className);
		else
			clazz = (Class<T>) Class.forName(className);

		if (clazz == null)
			throw new ClassNotFoundException("module: %s, classname=%s"
					.formatted(module.getName(), className));

		return clazz;
	}
}
