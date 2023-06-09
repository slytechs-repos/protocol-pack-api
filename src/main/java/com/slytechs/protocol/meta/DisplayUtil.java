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

import java.util.Optional;
import java.util.regex.Pattern;

import com.slytechs.protocol.runtime.util.Detail;

/**
 * The Class DisplayUtil.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
final class DisplayUtil {

	/**
	 * The Replacing.
	 */
	private record Replacing(Pattern pattern, String replacement) {
		
		/**
		 * Instantiates a new replacing.
		 *
		 * @param pattern     the pattern
		 * @param replacement the replacement
		 */
		Replacing(String pattern, String replacement) {
			this(Pattern.compile(pattern), replacement);
		}

		/**
		 * Replace all.
		 *
		 * @param str the str
		 * @return the string
		 */
		public String replaceAll(String str) {
			return pattern.matcher(str).replaceAll(replacement());
		}
	}

	/** The Constant DISPLAY_FMT_REPLACEMENTS. */
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

	/**
	 * Rewrite display format.
	 *
	 * @param fmt the fmt
	 * @return the string
	 */
	public static String rewriteDisplayFormat(String fmt) {
		String str = rewriteDisplayFormat0(fmt);
		return str;
	}

	/**
	 * Rewrite display format 0.
	 *
	 * @param fmt the fmt
	 * @return the string
	 */
	public static String rewriteDisplayFormat0(String fmt) {

		for (Replacing replacing : DISPLAY_FMT_REPLACEMENTS)
			fmt = replacing.replaceAll(fmt);

		return fmt;
	}

	/**
	 * Checks if is detail.
	 *
	 * @param details the details
	 * @param detail  the detail
	 * @return true, if is detail
	 */
	public static boolean isDetail(Detail[] details, Detail detail) {
		for (Detail d : details) {
			if (d == detail)
				return true;
		}

		return details.length == 0;
	}

	/**
	 * Select display.
	 *
	 * @param multiple the multiple
	 * @param single   the single
	 * @param detail   the detail
	 * @return the optional
	 */
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

	/**
	 * Instantiates a new display util.
	 */
	private DisplayUtil() {
	}

	/**
	 * Bitshift int left.
	 *
	 * @param v     the v
	 * @param shift the shift
	 * @return the string
	 */
	public static String bitshiftIntLeft(Object v, int shift) {
		if (!(v instanceof Number n))
			return String.valueOf(v);

		long l = (n.longValue() << shift);

		return Long.toString(l);
	}
}
