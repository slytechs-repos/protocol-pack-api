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
import java.util.Optional;
import java.util.logging.Logger;

import com.slytechs.jnet.runtime.util.json.JsonObject;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
class ReflectedMethodInfo extends ReflectedMemberInfo {
	private static final Logger LOGGER = Logger.getLogger(ReflectedMethodInfo.class.getPackageName());

	public static ReflectedMethodInfo[] parse(Class<?> cl, JsonObject jsonFields) {
		var list = new ArrayList<ReflectedMethodInfo>();

		parseMethodsRecursively(cl, list, jsonFields);

		return list.toArray(ReflectedMethodInfo[]::new);
	}

	private static void parseMethodsRecursively(Class<?> cl, List<ReflectedMethodInfo> list, JsonObject jsonFields) {

		if (cl.getSuperclass() == Object.class)
			return;

		parseMethodsRecursively(cl.getSuperclass(), list, jsonFields);

		for (Method m : cl.getDeclaredMethods()) {
			if (m.isAnnotationPresent(Meta.class))
				list.add(parseMethod(m, jsonFields));
		}

	}

	private static ReflectedMethodInfo parseMethod(Method method, JsonObject jsonFields) {
		String name = method.getName();

		JsonObject jsonDisplays = Optional.ofNullable(jsonFields)
				.map(fields -> fields.getJsonObject(name))
				.map(fields -> fields.getJsonObject("displays"))
				.orElse(null);

		MetaInfo meta = parseMetaAnnotation(method, jsonDisplays);
		DisplaysInfo displays = parseDisplaysAnnotation(method, jsonDisplays);

		return new ReflectedMethodInfo(method, meta, displays);
	}

	private static DisplaysInfo parseDisplaysAnnotation(Method method, JsonObject json) {
		Displays multiple = method.getAnnotation(Displays.class);
		Display single = method.getAnnotation(Display.class);

		DisplaysInfo displays = DisplaysInfo.parse(json, multiple, single);

		return displays;
	}

	private static MetaInfo parseMetaAnnotation(Method method, JsonObject json) {
		Meta meta = method.getAnnotation(Meta.class);
		MetaInfo info = MetaInfo.parse(method.getName(), json, meta);
		if (meta == null)
			throw new IllegalStateException("field meta annotation missing " + method.getName());

		return new MetaInfoImpl(info, method.getName());
	}

	private final Method method;

	/**
	 * @param f
	 * @param method
	 * @param displays
	 * @param parent
	 * 
	 */
	private ReflectedMethodInfo(Method method, MetaInfo metaInfo, DisplaysInfo displays) {
		super(metaInfo, displays);
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
	 * @see com.slytechs.jnet.protocol.packet.meta.ReflectedMemberInfo#getMember()
	 */
	@Override
	protected Member getMember() {
		return getMethod();
	}
}
