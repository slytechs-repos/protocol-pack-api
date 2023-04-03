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

import java.util.Optional;
import java.util.regex.Pattern;

import com.slytechs.jnet.protocol.packet.format.Display;
import com.slytechs.jnet.protocol.packet.format.Displays;
import com.slytechs.jnet.runtime.util.Detail;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
final class DisplayUtil {

	private record Replacing(Pattern pattern, String replacement) {
		Replacing(String pattern, String replacement) {
			this(Pattern.compile(pattern), replacement);
		}

		public String replaceAll(String str) {
			return pattern.matcher(str).replaceAll(replacement());
		}
	}

	private static final Replacing[] DISPLAY_FMT_REPLACEMENTS = {
			new Replacing("\\$([\\d,.+-]*)F", "%2&#36;$1"),
			new Replacing("\\$([\\d,.+-]*)R1", "%3&#36;$1"),
			new Replacing("\\$([\\d,.+-]*)R2", "%4&#36;$1"),
			new Replacing("\\$([\\d,.+-]*)R3", "%5&#36;$1"),
			new Replacing("\\$([\\d,.+-]*)R4", "%6&#36;$1"),
			new Replacing("\\$([\\d,.+-]*)R", "%3&#36;$1"),
			new Replacing("\\$([\\d,.+-]*)([xX])", "0x%1&#36;$1$2"),
			new Replacing("\\$([\\d,.+-]*)([a-zA-Z])", "%1&#36;$1$2"),

			new Replacing("&#36;", "\\$"), // Restore escaped '$' character
	};

	public static String rewriteDisplayFormat(String fmt) {
		String str = rewriteDisplayFormat0(fmt);

//		System.out.printf("%n>>> before='%s'%n>>>  after='%s'%n", fmt, str);

		return str;
	}

	public static String rewriteDisplayFormat0(String fmt) {

		for (Replacing replacing : DISPLAY_FMT_REPLACEMENTS)
			fmt = replacing.replaceAll(fmt);

		return fmt;
	}

	public static boolean isDetail(Detail[] details, Detail detail) {
		for (Detail d : details) {
			if (d == detail)
				return true;
		}

		return details.length == 0;
	}

	public static Optional<Display> selectDisplay(Displays multiple, Display single, Detail detail) {

		if (multiple != null) {
			Display[] da = multiple.value();
			if (da.length == 0)
				return Optional.empty();

			for (Display display : da) {
				if (display.detail() == detail)
					return Optional.ofNullable(display);
			}

			return Optional.empty();
		}

		return Optional.empty();
	}

	private DisplayUtil() {
	}

	public static String bitshiftIntLeft(Object v, int shift) {
		if (!(v instanceof Number n))
			return String.valueOf(v);

		long l = (n.longValue() << shift);

		return Long.toString(l);
	}
}
