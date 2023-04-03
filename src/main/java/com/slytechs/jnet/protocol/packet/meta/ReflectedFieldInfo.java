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

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.slytechs.jnet.runtime.util.json.JsonObject;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
class ReflectedFieldInfo extends ReflectedMemberInfo {

	public static ReflectedFieldInfo[] parse(Class<?> cl, JsonObject jsonFields) {
		var list = new ArrayList<ReflectedFieldInfo>();

		parseFieldsRecursively(cl, list, jsonFields);

		return list.toArray(ReflectedFieldInfo[]::new);
	}

	public static void parseFieldsRecursively(Class<?> cl, List<ReflectedFieldInfo> list, JsonObject jsonFields) {
		if (cl.getSuperclass() == Object.class)
			return;

		parseFieldsRecursively(cl.getSuperclass(), list, jsonFields);

		for (Field f : cl.getDeclaredFields()) {
			if (f.isAnnotationPresent(Meta.class))
				list.add(parseField(f, jsonFields));
		}

	}

	private static ReflectedFieldInfo parseField(Field field, JsonObject jsonFields) {

		JsonObject jsonDisplays = Optional.ofNullable(jsonFields)
				.filter(js -> js.isPresent(field.getName()))
				.map(js -> js.getJsonObject(field.getName()))
				.map(js -> js.getJsonObject("displays"))
				.orElse(null);

		MetaInfo meta = MetaInfo.parse(field.getName(), jsonDisplays, field.getAnnotation(Meta.class));
		if (meta == null)
			throw new IllegalStateException("field annotation missing " + field.getName());

		Displays multiple = field.getAnnotation(Displays.class);
		Display single = field.getAnnotation(Display.class);
		DisplaysInfo displays = DisplaysInfo.parse(jsonDisplays, multiple, single);

		return new ReflectedFieldInfo(field, meta, displays);
	}

	private final Field field;

	/**
	 * @param f
	 * @param field
	 * @param ordinal
	 * @param parent
	 * 
	 */
	private ReflectedFieldInfo(Field field, MetaInfo metaInfo, DisplaysInfo displays) {
		super(metaInfo, displays);
		this.field = field;
	}

	public Field getField() {
		return field;
	}

	@SuppressWarnings("unchecked")
	public <T> T getValue(Object target) {

		try {
			if (isStatic(field))
				return (T) field.get(null);
			else
				return (T) field.get(target);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new MetaException("unable to get reflected field value [%s]".formatted(field.toString()), e);
		}
	}

	public void setValue(Object target, Object newValue) {
		try {
			if (isStatic(field))
				field.set(null, newValue);
			else
				field.set(target, newValue);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new MetaException("unable to set reflected field value [%s] of '%s'".formatted(field.toString(),
					String.valueOf(newValue)), e);
		}
	}

	/**
	 * @return
	 */
	@Override
	public Class<?> getValueType() {
		return field.getType();
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.meta.ReflectedComponentInfo#getValue(java.lang.Object,
	 *      java.lang.Object[])
	 */
	@Override
	public <T> T getValue(Object target, Object... args) {
		return getValue(target);
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.meta.ReflectedComponentInfo#setValue(java.lang.Object,
	 *      java.lang.Object[])
	 */
	@Override
	public void setValue(Object target, Object... args) {
		setValue(target, args[0]);
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.meta.ReflectedMemberInfo#getMember()
	 */
	@Override
	protected Member getMember() {
		return getField();
	}
}
