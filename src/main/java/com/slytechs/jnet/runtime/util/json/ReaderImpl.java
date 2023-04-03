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
import java.io.IOException;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
class ReaderImpl implements JsonReader {

	private final BufferedReader reader;

	public ReaderImpl(BufferedReader reader) {
		this.reader = reader;
	}

	/**
	 * @see com.slytechs.jnet.runtime.util.json.JsonReader#close()
	 */
	@Override
	public void close() throws JsonException {
		try {
			reader.close();
		} catch (IOException e) {
			throw new JsonException(e);
		}
	}

	/**
	 * @see com.slytechs.jnet.runtime.util.json.JsonReader#read()
	 */
	@Override
	public JsonStructure read() throws JsonException {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/**
	 * @see com.slytechs.jnet.runtime.util.json.JsonReader#readArray()
	 */
	@Override
	public JsonArray readArray() throws JsonException {

		ArrayImpl array = new ArrayImpl();
		do {
			skipChar();
			skipWhitespace();

			JsonValue value = readValue();

			skipWhitespace();

			array.add(value);

		} while (peekChar() == ',');

		if (readChar() != ']')
			throw new JsonParsingException("expecting closing ']' for array value");

		return array;
	}

	/**
	 * @see com.slytechs.jnet.runtime.util.json.JsonReader#readObject()
	 */
	@Override
	public JsonObject readObject() throws JsonException {

		skipWhitespace();

		if (peekChar() != '{')
			throw new JsonParsingException("expecting opening '{' for object value");

		ObjectImpl obj = new ObjectImpl();

		do {
			skipChar();
			skipWhitespace();

			if (peekChar() != '\"')
				break;

			String name = readQuotedToken();

			skipWhitespace();

			if (readChar() != ':')
				throw new JsonParsingException("expecting a ':' separating new json value");

			skipWhitespace();

			JsonValue value = readValue();

			skipWhitespace();

			obj.put(name, value);
		} while (peekChar() == ',');

		if (readChar() != '}')
			throw new JsonParsingException("expecting closing '}' for object value");

		return obj;
	}

	private String readQuotedToken() throws JsonException {
		if (readChar() != '\"')
			throw new JsonParsingException("expecting '\"' around a string");

		StringBuilder b = new StringBuilder();

		while (true) {
			char ch = readChar();
			if (ch == '\"') {
				resetReadChar();
				break;
			}

			b.append(ch);
		}

		if (readChar() != '\"')
			throw new JsonParsingException("missing closing '\"' around a string");

		return b.toString();
	}

	private String readToken() throws JsonException {
		StringBuilder b = new StringBuilder();

		while (true) {
			char ch = readChar();
			if (Character.isWhitespace(ch) || ch == ',' || ch == ']' || ch == '}') {
				resetReadChar();
				break;
			}

			b.append(ch);
		}

		return b.toString();
	}

	private JsonValue readValue() throws JsonException {
		char ch = peekChar();

		if (ch == '[')
			return readArray();

		if (ch == '\"')
			return readString();

		if (ch == '{')
			return readObject();

		/* Check for true, false or null tokens */
		if (ch == 't' || ch == 'f' || ch == 'n')
			return readConstant();

		if (Character.isDigit(ch))
			return readNumber();

		throw new JsonParsingException("invalid value");
	}

	private JsonValue readConstant() throws JsonException {
		String token = readToken();

		return switch (token) {
		case "null" -> JsonValue.NULL;
		case "true" -> JsonValue.TRUE;
		case "false" -> JsonValue.FALSE;

		default -> throw new JsonParsingException("invalid json constant " + token);
		};
	}

	private JsonString readString() throws JsonException {
		return new StringImpl(readQuotedToken());

	}

	private JsonNumber readNumber() throws JsonException {
		String token = readToken();

		try {
			long v = Long.parseLong(token);

			return new NumberImpl(v);
		} catch (Throwable e) {
			try {
				double v = Double.parseDouble(token);

				return new NumberImpl(v);
			} catch (Throwable e1) {
				throw new JsonParsingException("invalid number [%s]".formatted(token));
			}
		}

	}

	private void skipChar() throws JsonException {
		try {
			reader.skip(1);
		} catch (IOException e) {
			throw new JsonException(e);
		}
	}

	private char peekChar() throws JsonException {
		char ch = readChar();
		resetReadChar();

		return ch;
	}

	private void resetReadChar() throws JsonException {
		try {
			reader.reset();
		} catch (IOException e) {
			throw new JsonException(e);
		}
	}

	private char readChar() throws JsonException {
		try {
			reader.mark(1);
			char ch = (char) reader.read();
			if (ch == -1 || ch == '\uffff')
				throw new IllegalStateException("end of stream");

			return ch;
		} catch (IOException e) {
			throw new JsonException(e);
		}
	}

	private void skipWhitespace() throws JsonException {
		while (true) {
			char ch = readChar();
			if (!Character.isWhitespace(ch)) {
				resetReadChar();
				return;
			}
		}
	}
}
