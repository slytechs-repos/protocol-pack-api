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
package com.slytechs.jnet.runtime.util;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public final class Strings {

	public static String dquote(String str) {
		return wrap(str, '\"');
	}

	public static String squote(String str) {
		return wrap(str, '\'');
	}

	public static String curley(String str) {
		return wrap(str, '{', '}');
	}

	public static String square(String str) {
		return wrap(str, '[', ']');
	}

	public static String angled(String str) {
		return wrap(str, '<', '>');
	}

	public static String wrap(String str, char openAndClose) {
		return wrap(str, openAndClose, openAndClose);
	}

	public static String wrap(String str, char open, char close) {
		if (str == null)
			str = "";

		return new StringBuilder()
				.append(open)
				.append(str)
				.append(close)
				.toString();
	}

	private Strings() {
	}
}
