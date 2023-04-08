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
package com.slytechs.jnet.protocol.meta;

import java.util.function.Function;

import com.slytechs.jnet.runtime.internal.json.JsonObject;
import com.slytechs.jnet.runtime.internal.json.JsonString;
import com.slytechs.jnet.runtime.util.Detail;

/**
 * The DisplayInfo.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public record DisplayInfo(String value, String label, Detail detail) implements MetaInfoType {

	/** The Constant EMPTY_HEADER_DEFAULT_DISPLAY. */
	@Display(label = "", value = "")
	private static final Display EMPTY_HEADER_DEFAULT_DISPLAY;

	/** The Constant EMPTY_FIELD_DEFAULT_DISPLAY. */
	@Display(label = "", value = "%{:F}s")
	private static final Display EMPTY_FIELD_DEFAULT_DISPLAY;

	static {
		try {
			EMPTY_HEADER_DEFAULT_DISPLAY = DisplayInfo.class
					.getDeclaredField("EMPTY_HEADER_DEFAULT_DISPLAY")
					.getAnnotation(Display.class);

			EMPTY_FIELD_DEFAULT_DISPLAY = DisplayInfo.class
					.getDeclaredField("EMPTY_FIELD_DEFAULT_DISPLAY")
					.getAnnotation(Display.class);

		} catch (NoSuchFieldException | SecurityException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	/**
	 * Parses the json.
	 *
	 * @param jsonDisplay    the json display
	 * @param detail         the detail
	 * @param defaultDisplay the default display
	 * @return the display info
	 */
	public static DisplayInfo parseJson(JsonObject jsonDisplay, Detail detail,
			Function<Detail, DisplayInfo> defaultDisplay) {

		if (jsonDisplay == null)
			return defaultDisplay.apply(detail);

		if (jsonDisplay.get("DEFAULT") instanceof JsonString jsonValue) {
			return new DisplayInfo(jsonValue.getString(), "", detail);
		}

		if (jsonDisplay.get("DEFAULT") instanceof JsonObject jsonDef) {
			jsonDisplay = jsonDef;
		}

		String value = jsonDisplay.getString("value", null);
		String label = jsonDisplay.getString("label", "");

		return new DisplayInfo(value, label, detail);
	}

	/**
	 * Default header display.
	 *
	 * @param detail the detail
	 * @return the display info
	 */
	public static DisplayInfo defaultHeaderDisplay(Detail detail) {
		return parseAnnotation(EMPTY_HEADER_DEFAULT_DISPLAY, detail);
	}

	/**
	 * Default field display.
	 *
	 * @param detail the detail
	 * @return the display info
	 */
	public static DisplayInfo defaultFieldDisplay(Detail detail) {
		return parseAnnotation(EMPTY_FIELD_DEFAULT_DISPLAY, detail);
	}

	/**
	 * Parses the annotation.
	 *
	 * @param displayAnnotation the display annotation
	 * @param detail            the detail
	 * @return the display info
	 */
	public static DisplayInfo parseAnnotation(Display displayAnnotation, Detail detail) {
		return new DisplayInfo(
				displayAnnotation.value(),
				displayAnnotation.label(),
				detail);
	}

	/**
	 * Label.
	 *
	 * @param metaInfo the meta info
	 * @return the string
	 */
	public String label(MetaInfo metaInfo) {
		String label = label();
		if (label.isBlank())
			return metaInfo.name();

		return label;
	}
}
