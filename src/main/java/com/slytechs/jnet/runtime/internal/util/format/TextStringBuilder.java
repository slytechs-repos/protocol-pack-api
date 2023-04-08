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
package com.slytechs.jnet.runtime.internal.util.format;

import java.util.Optional;

/**
 * The Class TextStringBuilder.
 */
public class TextStringBuilder extends AbstractStringBuilder<TextStringBuilder> {

	/** The Constant OPEN_CH. */
	public static final char OPEN_CH = '(';
	
	/** The Constant CLOSE_CH. */
	public static final char CLOSE_CH = ')';

	/**
	 * Instantiates a new text string builder.
	 *
	 * @param out the out
	 */
	public TextStringBuilder(StringBuilder out) {
		super(out);
	}

	/**
	 * Append.
	 *
	 * @param value1 the value 1
	 * @param value2 the value 2
	 * @return the text string builder
	 */
	public TextStringBuilder append(String value1, String value2) {
		return append(value1, OPEN_CH, value2, CLOSE_CH);
	}

	/**
	 * Append.
	 *
	 * @param value1 the value 1
	 * @param value2 the value 2
	 * @return the text string builder
	 */
	public TextStringBuilder append(Optional<String> value1, String value2) {
		return append(value1, OPEN_CH, value2, CLOSE_CH);
	}

	/**
	 * Append.
	 *
	 * @param value1 the value 1
	 * @param open   the open
	 * @param value2 the value 2
	 * @param close  the close
	 * @return the text string builder
	 */
	public TextStringBuilder append(Optional<String> value1, char open, String value2, char close) {

		if (value1.isPresent()) {
			this.append(value1.get())
					.append(' ')
					.append(open)
					.append(value2)
					.append(close);
		} else {
			this.append(value2);
		}

		return this;
	}

	/**
	 * Append.
	 *
	 * @param value1 the value 1
	 * @param open   the open
	 * @param value2 the value 2
	 * @param close  the close
	 * @return the text string builder
	 */
	public TextStringBuilder append(String value1, char open, String value2, char close) {

		this.append(value1)
				.append(' ')
				.append(open)
				.append(value2)
				.append(close);

		return this;
	}

	/**
	 * Append.
	 *
	 * @param value1 the value 1
	 * @param value2 the value 2
	 * @return the text string builder
	 */
	public TextStringBuilder append(int value1, int value2) {
		return append(value1, OPEN_CH, value2, CLOSE_CH);
	}

	/**
	 * Append.
	 *
	 * @param value1 the value 1
	 * @param open   the open
	 * @param value2 the value 2
	 * @param close  the close
	 * @return the text string builder
	 */
	public TextStringBuilder append(int value1, char open, int value2, char close) {

		this.append(value1)
				.append(' ')
				.append(open)
				.append(value2)
				.append(close);

		return this;
	}

	/**
	 * Append.
	 *
	 * @param value1 the value 1
	 * @param value2 the value 2
	 * @return the text string builder
	 */
	public TextStringBuilder append(long value1, long value2) {
		return append(value1, OPEN_CH, value2, CLOSE_CH);
	}

	/**
	 * Append.
	 *
	 * @param value1 the value 1
	 * @param open   the open
	 * @param value2 the value 2
	 * @param close  the close
	 * @return the text string builder
	 */
	public TextStringBuilder append(long value1, char open, long value2, char close) {

		this.append(value1)
				.append(' ')
				.append(open)
				.append(value2)
				.append(close);

		return this;
	}

}
