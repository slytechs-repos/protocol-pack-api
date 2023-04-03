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
package com.slytechs.jnet.runtime.util.json;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Objects;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public final class Json {

	public static JsonBuilderFactory createBuilderFactory() {
		return new JsonBuilderFactory();
	}

	public static JsonReader createReader(InputStream in) {
		return createReader(new InputStreamReader(Objects.requireNonNull(in, "in")));
	}

	public static JsonReader createReader(Reader reader) {
		return new ReaderImpl(new BufferedReader(Objects.requireNonNull(reader, "reader")));
	}

	private Json() {
	}
}
