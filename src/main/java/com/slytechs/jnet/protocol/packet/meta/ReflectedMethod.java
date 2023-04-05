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
package com.slytechs.jnet.protocol.packet.meta;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.slytechs.jnet.runtime.util.json.JsonObject;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
class ReflectedMethod extends ReflectedMember {
	private static final Logger LOGGER = Logger.getLogger(ReflectedMethod.class.getPackageName());

	public static ReflectedMethod[] parse(Class<?> cl, JsonObject jsonFields) {
		var list = new ArrayList<ReflectedMethod>();

		parseMethodsRecursively(cl, list, jsonFields);

		return list.toArray(ReflectedMethod[]::new);
	}

	private static void parseMethodsRecursively(Class<?> cl, List<ReflectedMethod> list, JsonObject jsonFields) {

		if (cl.getSuperclass() == Object.class)
			return;

		parseMethodsRecursively(cl.getSuperclass(), list, jsonFields);

		for (Method m : cl.getDeclaredMethods()) {
			if (m.isAnnotationPresent(Meta.class))
				list.add(parseMethod(m, jsonFields));
		}

	}

	private static ReflectedMethod parseMethod(Method method, JsonObject jsonFields) {
		String name = method.getName();

		JsonObject field = null;
		if (jsonFields != null)
			field = jsonFields.getJsonObject(name);

		var container = MetaInfo.parse(method, name, field);
		return new ReflectedMethod(method, container);
	}

	private final Method method;

	/**
	 * @param method
	 * @param f
	 * @param parent
	 * 
	 */
	private ReflectedMethod(Method method, MetaInfo metaInfo) {
		super(metaInfo);
		this.method = method;
	}

	public Method getMethod() {
		return method;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getValue(Object target, Object... args) {
		try {
			if (isStatic(method))
				return (T) method.invoke(null, args);
			else
				return (T) method.invoke(target, args);
		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			throw new MetaException("unable to get reflected method value [%s]"
					.formatted(method.toString()), unwindCauseException(e));
		}
	}

	@Override
	public void setValue(Object target, Object... args) {
		try {
			if (isStatic(method))
				method.invoke(null, args);
			else
				method.invoke(target, args);
		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			throw new MetaException("unable to set reflected method value [%s]"
					.formatted(method.toString()), unwindCauseException(e));
		}
	}

	/**
	 * @return
	 */
	@Override
	public Class<?> getValueType() {
		return method.getReturnType();
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.meta.ReflectedMember#getMember()
	 */
	@Override
	protected Member getMember() {
		return getMethod();
	}
}
