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

import java.lang.reflect.AnnotatedElement;

import com.slytechs.jnet.protocol.packet.meta.Meta.Formatter;
import com.slytechs.jnet.protocol.packet.meta.Meta.MetaType;
import com.slytechs.jnet.runtime.internal.json.JsonObject;
import com.slytechs.jnet.runtime.internal.json.JsonObjectBuilder;
import com.slytechs.jnet.runtime.internal.json.JsonValue;
import com.slytechs.jnet.runtime.internal.json.JsonValue.ValueType;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public class MetaInfo implements Comparable<MetaInfo>, MetaInfoType {

	public static MetaInfo parse(AnnotatedElement element, String name, JsonObject jsonDefaults) {

		if (!element.isAnnotationPresent(Meta.class) && jsonDefaults == null)
			throw new IllegalArgumentException("Meta resource or direct annotation not found [%s]"
					.formatted(name));

		JsonValue jsonMeta = null;
		if (jsonDefaults != null)
			jsonMeta = jsonDefaults.get("meta");

		MetaInfo meta = new MetaInfo();

		if (jsonMeta != null)
			parseJsonMeta(meta, jsonMeta);

		if (element.isAnnotationPresent(Meta.class))
			parseMetaAnnotation(meta, element, name);

		if (meta.abbr == null)
			meta.abbr = meta.name;

		meta.displaysInfo = DisplaysInfo.parse(element, name, jsonDefaults);
		meta.resolversInfo = ResolversInfo.parse(element, name, jsonDefaults);

		return meta;
	}

	private static void parseJsonMeta(MetaInfo meta, JsonValue jsonValue) {

		JsonObject jsonMeta;
		if (jsonValue.getValueType() == ValueType.OBJECT)
			jsonMeta = (JsonObject) jsonValue;
		else
			jsonMeta = new JsonObjectBuilder()
					.add("ordinal", jsonValue)
					.build();

		String name = jsonMeta.getString("name", null);
		String abbr = jsonMeta.getString("abbr", null);
		String note = jsonMeta.getString("note", null);
		int ordinal = jsonMeta.getInt("ordinal", -1);
		String typeName = jsonMeta.getString("formatter", null);

		if (name != null)
			meta.name = name;

		if (abbr != null)
			meta.abbr = abbr;

		if (note != null)
			meta.note = note;

		if (ordinal != -1)
			meta.ordinal = ordinal;

		if (typeName != null) {
			Formatter type = Formatter.valueOf(typeName);
			meta.formatter = type;
		}
	}

	private static void parseMetaAnnotation(MetaInfo meta, AnnotatedElement element, String name) {
		Meta ma = element.getAnnotation(Meta.class);
		if (ma == null)
			return;

		if (meta.name == null)
			meta.name = name; // Default name

		/* Meta annotations override any previously JSON defined attributes */
		if (!ma.name().isBlank())
			meta.name = ma.name();

		if (!ma.abbr().isBlank())
			meta.abbr = ma.abbr();

		if (!ma.note().isBlank())
			meta.note = ma.note();

		if (ma.ordinal() != -1)
			meta.ordinal = ma.ordinal();

		if (meta.formatter == null || ma.formatter() != Formatter.NONE) {
			meta.formatter = ma.formatter();
		}

		meta.metaType = ma.value();

	}

	private String name;

	private String abbr;
	private String note = "";
	private int ordinal = -1;
	private Formatter formatter;
	private DisplaysInfo displaysInfo;
	private ResolversInfo resolversInfo;
	private MetaType metaType;
	private MetaInfo() {
	}

	/**
	 * @return the abbr
	 */
	protected String abbr() {
		return abbr;
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(MetaInfo o) {
		return ordinal() - o.ordinal();
	}

	/**
	 * @return the type
	 */
	public Formatter formatter() {
		return formatter;
	}

	@SuppressWarnings("unchecked")
	public <T extends MetaInfoType> T getMetaType(Class<T> type) {
		if (type == MetaInfo.class)
			return (T) this;

		if (type == DisplaysInfo.class)
			return (T) displaysInfo;

		if (type == ResolversInfo.class)
			return (T) resolversInfo;

		return null;
	}

	public MetaType metaType() {
		return metaType;
	}

	/**
	 * @return the name
	 */
	public String name() {
		return name;
	}

	/**
	 * @return the note
	 */
	public String note() {
		return note;
	}

	/**
	 * @return the ordinal
	 */
	public int ordinal() {
		return ordinal;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MetaInfo [name=" + name + ", abbr=" + abbr + ", note=" + note + ", ordinal=" + ordinal + ", type="
				+ formatter + ", displaysInfo=" + displaysInfo + ", resolversInfo=" + resolversInfo + "]";
	}
}
