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
package com.slytechs.jnet.protocol.packet.format;

import java.util.Objects;

import com.slytechs.jnet.runtime.util.Detail;
import com.slytechs.jnet.runtime.util.Enums;
import com.slytechs.jnet.runtime.util.json.JsonObject;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
interface DisplayInfo {

	class JsonFilter implements DisplayInfo {

		private final JsonObject json;
		private final DisplayInfo next;

		JsonFilter(JsonObject display, DisplayInfo next) {
			this.json = Objects.requireNonNull(display, "display");
			this.next = Objects.requireNonNull(next, "next");

			Detail jsonDetail = Enums.getEnum(Detail.class, display.getString("detail", "LOW"));
			if (jsonDetail != next.detail())
				throw new IllegalStateException("incompatible display detail setting");
		}

		/**
		 * @see com.slytechs.jnet.protocol.packet.meta.DisplayInfo#value()
		 */
		@Override
		public String value() {
			String value = next.value();
			if (value.isBlank())
				value = json.getString("value", "");

			return value;
		}

		/**
		 * @see com.slytechs.jnet.protocol.packet.meta.DisplayInfo#label()
		 */
		@Override
		public String label() {
			String label = next.label();
			if (label.isBlank())
				label = json.getString("label", "");

			return label;
		}

		/**
		 * @see com.slytechs.jnet.protocol.packet.meta.DisplayInfo#detail()
		 */
		@Override
		public Detail detail() {
			return next.detail();
		}

	}

	class AnnotatedFilter implements DisplayInfo {

		private final Display display;

		AnnotatedFilter(Display display) {
			this.display = display;
		}

		/**
		 * @see com.slytechs.jnet.protocol.packet.meta.DisplayInfo#value()
		 */
		@Override
		public String value() {
			return display.value();
		}

		/**
		 * @see com.slytechs.jnet.protocol.packet.meta.DisplayInfo#label()
		 */
		@Override
		public String label() {
			return display.label();
		}

		/**
		 * @see com.slytechs.jnet.protocol.packet.meta.DisplayInfo#detail()
		 */
		@Override
		public Detail detail() {
			return display.detail();
		}

	}

	String value();

	String label();

	Detail detail();
}
