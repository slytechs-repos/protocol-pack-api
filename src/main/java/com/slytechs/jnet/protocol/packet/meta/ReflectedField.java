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

import com.slytechs.jnet.runtime.util.json.JsonObject;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
class ReflectedField extends ReflectedMember {

	public static ReflectedField[] parse(Class<?> cl, JsonObject jsonFields) {
		var list = new ArrayList<ReflectedField>();

		parseFieldsRecursively(cl, list, jsonFields);

		return list.toArray(ReflectedField[]::new);
	}

	public static void parseFieldsRecursively(Class<?> cl, List<ReflectedField> list, JsonObject jsonFields) {
		if (cl.getSuperclass() == Object.class)
			return;

		parseFieldsRecursively(cl.getSuperclass(), list, jsonFields);

		for (Field f : cl.getDeclaredFields()) {
			if (f.isAnnotationPresent(Meta.class))
				list.add(parseField(f, jsonFields));
		}

	}

	private static ReflectedField parseField(Field field, JsonObject jsonFields) {
		String name = field.getName();
		var container = MetaInfo.parse(field, name, jsonFields);

		return new ReflectedField(field, container);
	}

	private final Field field;

	/**
	 * @param f
	 * @param field
	 * @param ordinal
	 * @param parent
	 * 
	 */
	private ReflectedField(Field field, MetaInfo metaContainer) {
		super(metaContainer);
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
	 * @see com.slytechs.jnet.protocol.packet.meta.ReflectedComponent#getValue(java.lang.Object,
	 *      java.lang.Object[])
	 */
	@Override
	public <T> T getValue(Object target, Object... args) {
		return getValue(target);
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.meta.ReflectedComponent#setValue(java.lang.Object,
	 *      java.lang.Object[])
	 */
	@Override
	public void setValue(Object target, Object... args) {
		setValue(target, args[0]);
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.meta.ReflectedMember#getMember()
	 */
	@Override
	protected Member getMember() {
		return getField();
	}
}
