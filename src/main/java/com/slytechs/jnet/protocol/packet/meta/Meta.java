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

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.slytechs.jnet.protocol.packet.meta.MetaValue.ValueFormatter;

@Retention(RUNTIME)
@Target({ TYPE,
		METHOD,
		FIELD })
/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public @interface Meta {

	public enum MetaType {

		/** A displayble field. */
		FIELD,

		/**
		 * Attributes do not show up as fields when displayed, but can be accessed like
		 * any field from any meta element.
		 */
		ATTRIBUTE,
	}

	public enum Formatter implements ValueFormatter {
		NONE(MetaValues::none),
		AUTO(MetaValues::auto),
		MAC_ADDRESS(MetaValues::macAddress),
		IP_ADDRESS(MetaValues::ipAddress),
		IPv4_ADDRESS(MetaValues::ip4Address),
		IPv6_ADDRESS(MetaValues::ip6Address),
		HEX_LOWERCASE(MetaValues::hexLowercase),
		HEX_UPPERCASE(MetaValues::hexUppercase),
		HEX_UPPERCASE_0X(MetaValues::hexUppercase0x),
		HEX_LOWERCASE_0x(MetaValues::hexLowercase0x),

		;

		private final ValueFormatter formatter;

		Formatter(ValueFormatter formatter) {
			this.formatter = formatter;
		}

		/**
		 * @see com.slytechs.jnet.protocol.packet.meta.MetaValue.ValueFormatter#format(java.lang.Object)
		 */
		@Override
		public String format(Object value) {
			return formatter.format(value);
		}

	}

	int ordinal() default -1;

	String abbr() default "";

	String name() default "";

	String note() default "";

	Formatter formatter() default Formatter.AUTO;

	MetaType value() default MetaType.FIELD;

}
