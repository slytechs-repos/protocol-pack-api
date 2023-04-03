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

import java.util.function.Function;

import com.slytechs.jnet.runtime.util.Detail;
import com.slytechs.jnet.runtime.util.json.JsonObject;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public record DisplayInfo(String value, String label, Detail detail) implements MetaInfoType {

	@Display(label = "", value = "")
	private static final Display EMPTY_HEADER_DEFAULT_DISPLAY;

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

	public static DisplayInfo parseJson(JsonObject jsonDisplay, Detail detail,
			Function<Detail, DisplayInfo> defaultDisplay) {
		if (jsonDisplay == null)
			return defaultDisplay.apply(detail);

		String value = jsonDisplay.getString("value", null);
		String label = jsonDisplay.getString("label", "");

		return new DisplayInfo(value, label, detail);
	}

	public static DisplayInfo defaultHeaderDisplay(Detail detail) {
		return parseAnnotation(EMPTY_HEADER_DEFAULT_DISPLAY, detail);
	}

	public static DisplayInfo defaultFieldDisplay(Detail detail) {
		return parseAnnotation(EMPTY_FIELD_DEFAULT_DISPLAY, detail);
	}

	public static DisplayInfo parseAnnotation(Display displayAnnotation, Detail detail) {
		return new DisplayInfo(
				displayAnnotation.value(),
				displayAnnotation.label(),
				detail);
	}

	public String label(MetaInfo metaInfo) {
		String label = label();
		if (label.isBlank())
			return metaInfo.name();

		return label;
	}
}
