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
package com.slytechs.jnet.jnetruntime.internal.util.format;

import java.util.Locale;

/**
 * The Interface MessageResolver.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author mark
 * @param <T> the generic type
 */
@FunctionalInterface
public interface MessageResolver<T> {

	/**
	 * Resolve message for.
	 *
	 * @param target the target
	 * @param locale the locale
	 * @param style  the style
	 * @return the string
	 */
	String resolveMessageFor(T target, Locale locale, FormatStyle style);

	/**
	 * And then.
	 *
	 * @param after the after
	 * @return the message resolver
	 */
	default MessageResolver<T> andThen(MessageResolver<T> after) {
		return (t, l, s) -> {
			String msg = resolveMessageFor(t, l, s);
			if (msg == null)
				msg = after.resolveMessageFor(t, l, s);

			return msg;
		};
	}

}
