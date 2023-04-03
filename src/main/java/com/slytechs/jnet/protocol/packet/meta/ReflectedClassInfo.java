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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.slytechs.jnet.runtime.util.json.Json;
import com.slytechs.jnet.runtime.util.json.JsonException;
import com.slytechs.jnet.runtime.util.json.JsonObject;
import com.slytechs.jnet.runtime.util.json.JsonReader;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
class ReflectedClassInfo extends ReflectedComponent {

	private static final Logger LOGGER = Logger.getLogger(ReflectedClass.class.getPackageName());

	static ReflectedClass parse(Class<?> cl) {
		JsonObject jsonClass = null;
		JsonObject jsonFields = null;

		MetaResource res = cl.getAnnotation(MetaResource.class);
		if (res != null) {
			try {
				JsonObject conf = readMetaResource(res.value());
				jsonClass = conf.getJsonObject("meta");
				jsonFields = conf.getJsonObject("fields");

			} catch (JsonException e) {
				LOGGER.log(Level.WARNING, "unable to load meta resource [%s]"
						.formatted(res.value()), e);
			}
		}

		String name = cl.getSimpleName();
		var container = new MetaInfoContainer(cl, name, jsonClass);

		ReflectedMethod[] methods = ReflectedMethod.parse(cl, jsonFields);
		ReflectedField[] fields = ReflectedField.parse(cl, jsonFields);

		return new ReflectedClass(cl, container, methods, fields);
	}

	static JsonObject readMetaResource(String resourceName) throws JsonException {

		InputStream in = ReflectedClass.class.getResourceAsStream("/" + resourceName);
		if (in == null)
			return null;

		try (JsonReader reader = Json.createReader(in)) {
			JsonObject obj = reader.readObject();

			return obj;
		}
	}

	private static ReflectedMember[] toSortedArray(ReflectedMethod[] methods, ReflectedField[] fields) {
		List<ReflectedMember> list = new ArrayList<ReflectedMember>();
		Arrays.stream(methods)
				.forEach(list::add);

		Arrays.stream(fields)
				.forEach(list::add);

		Collections.sort(list);

		return list.toArray(ReflectedMember[]::new);
	}

	private final Class<?> classType;
	private final ReflectedMethod[] memberMethods;
	private final ReflectedField[] memberFields;

	private final ReflectedMember[] fieldsArray;
	private final Map<String, ReflectedMember> fieldsMap;

	private ReflectedClassInfo(Class<?> cl, MetaInfoContainer metaContainer,
			ReflectedMethod[] methods,
			ReflectedField[] fields) {
		super(metaContainer);
		this.classType = cl;
		this.memberMethods = methods;
		this.memberFields = fields;
		this.fieldsArray = toSortedArray(methods, fields);
		this.fieldsMap = Arrays.stream(this.fieldsArray)
				.collect(Collectors.toMap(ReflectedMember::name, v -> v));
	}

	/**
	 * @return the classType
	 */
	public Class<?> getClassType() {
		return classType;
	}

	public ReflectedMember[] getFields() {
		return fieldsArray;
	}

	public ReflectedField[] getMemberFields() {
		return memberFields;
	}

	public ReflectedMethod[] getMemberMethods() {
		return memberMethods;
	}

	public ReflectedMember getField(String name) {
		return fieldsMap.get(name);
	}
}
