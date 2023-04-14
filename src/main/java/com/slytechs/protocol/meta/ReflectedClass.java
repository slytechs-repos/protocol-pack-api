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
package com.slytechs.protocol.meta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.slytechs.protocol.runtime.internal.json.JsonException;
import com.slytechs.protocol.runtime.internal.json.JsonObject;

/**
 * The Class ReflectedClass.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
class ReflectedClass extends ReflectedComponent {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getLogger(ReflectedClass.class.getPackageName());

	/**
	 * Parses the.
	 *
	 * @param cl the cl
	 * @return the reflected class
	 */
	static ReflectedClass parse(Class<?> cl) {
		JsonObject jsonConf = null;
		JsonObject jsonFields = null;

		MetaResource res = cl.getAnnotation(MetaResource.class);
		if (res != null) {
			try {
				jsonConf = readMetaResource(res.value());
				jsonFields = jsonConf.getJsonObject("field");

			} catch (JsonException e) {
				LOGGER.log(Level.WARNING, "unable to load meta resource [%s]"
						.formatted(res.value()), e);
			}
		}

		String name = cl.getSimpleName();
		var metaInfo = MetaInfo.parse(cl, name, jsonConf);

		ReflectedMethod[] methods = ReflectedMethod.parse(cl, jsonFields);
		ReflectedField[] fields = ReflectedField.parse(cl, jsonFields);

		return new ReflectedClass(cl, metaInfo, methods, fields);
	}

	/**
	 * Read meta resource.
	 *
	 * @param resourceName the resource name
	 * @return the json object
	 * @throws JsonException the json exception
	 */
	static JsonObject readMetaResource(String resourceName) throws JsonException {
		return new MetaResourceShortformReader(resourceName)
				.toJsonObj();
	}

	/**
	 * To sorted array.
	 *
	 * @param methods the methods
	 * @param fields  the fields
	 * @return the reflected member[]
	 */
	private static ReflectedMember[] toSortedUniqueArray(ReflectedMethod[] methods, ReflectedField[] fields) {
		Set<String> dups = new HashSet<>();

		List<ReflectedMember> list = new ArrayList<ReflectedMember>();
		Arrays.stream(methods)
				.filter(m -> !dups.contains(m.name()))
				.peek(m -> dups.add(m.name()))
				.forEach(list::add);

		Arrays.stream(fields)
				.filter(m -> !dups.contains(m.name()))
				.peek(m -> dups.add(m.name()))
				.forEach(list::add);

		Collections.sort(list);

		return list.toArray(ReflectedMember[]::new);
	}

	/** The class type. */
	private final Class<?> classType;

	/** The member methods. */
	private final ReflectedMethod[] memberMethods;

	/** The member fields. */
	private final ReflectedField[] memberFields;

	/** The fields array. */
	private final ReflectedMember[] memberArray;

	/** The fields map. */
	private final Map<String, ReflectedMember> fieldsMap;

	/**
	 * Instantiates a new reflected class.
	 *
	 * @param cl       the cl
	 * @param metaInfo the meta info
	 * @param methods  the methods
	 * @param fields   the fields
	 */
	private ReflectedClass(Class<?> cl, MetaInfo metaInfo,
			ReflectedMethod[] methods,
			ReflectedField[] fields) {
		super(metaInfo);
		this.classType = cl;
		this.memberMethods = methods;
		this.memberFields = fields;
		this.memberArray = toSortedUniqueArray(methods, fields);
		this.fieldsMap = Arrays.stream(this.memberArray)
				.collect(Collectors.toMap(ReflectedMember::name, v -> v));
	}

	/**
	 * Gets the class type.
	 *
	 * @return the classType
	 */
	public Class<?> getClassType() {
		return classType;
	}

	/**
	 * Gets the fields.
	 *
	 * @return the fields
	 */
	public ReflectedMember[] getFields() {
		return memberArray;
	}

	/**
	 * Gets the member fields.
	 *
	 * @return the member fields
	 */
	public ReflectedField[] getMemberFields() {
		return memberFields;
	}

	/**
	 * Gets the member methods.
	 *
	 * @return the member methods
	 */
	public ReflectedMethod[] getMemberMethods() {
		return memberMethods;
	}

	/**
	 * Gets the field.
	 *
	 * @param name the name
	 * @return the field
	 */
	public ReflectedMember getField(String name) {
		return fieldsMap.get(name);
	}
}
