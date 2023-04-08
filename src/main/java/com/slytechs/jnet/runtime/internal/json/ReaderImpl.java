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
package com.slytechs.jnet.runtime.internal.json;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * The Class ReaderImpl.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
class ReaderImpl implements JsonReader {

	/** The reader. */
	private final BufferedReader reader;

	/**
	 * Instantiates a new reader impl.
	 *
	 * @param reader the reader
	 */
	public ReaderImpl(BufferedReader reader) {
		this.reader = reader;
	}

	/**
	 * Close.
	 *
	 * @throws JsonException the json exception
	 * @see com.slytechs.jnet.runtime.internal.json.JsonReader#close()
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
	 * Read.
	 *
	 * @return the json structure
	 * @throws JsonException the json exception
	 * @see com.slytechs.jnet.runtime.internal.json.JsonReader#read()
	 */
	@Override
	public JsonStructure read() throws JsonException {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/**
	 * Read array.
	 *
	 * @return the json array
	 * @throws JsonException the json exception
	 * @see com.slytechs.jnet.runtime.internal.json.JsonReader#readArray()
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
	 * Read object.
	 *
	 * @return the json object
	 * @throws JsonException the json exception
	 * @see com.slytechs.jnet.runtime.internal.json.JsonReader#readObject()
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

	/**
	 * Read quoted token.
	 *
	 * @return the string
	 * @throws JsonException the json exception
	 */
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

	/**
	 * Read token.
	 *
	 * @return the string
	 * @throws JsonException the json exception
	 */
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

	/**
	 * Read value.
	 *
	 * @return the json value
	 * @throws JsonException the json exception
	 */
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

	/**
	 * Read constant.
	 *
	 * @return the json value
	 * @throws JsonException the json exception
	 */
	private JsonValue readConstant() throws JsonException {
		String token = readToken();

		return switch (token) {
		case "null" -> JsonValue.NULL;
		case "true" -> JsonValue.TRUE;
		case "false" -> JsonValue.FALSE;

		default -> throw new JsonParsingException("invalid json constant " + token);
		};
	}

	/**
	 * Read string.
	 *
	 * @return the json string
	 * @throws JsonException the json exception
	 */
	private JsonString readString() throws JsonException {
		return new StringImpl(readQuotedToken());

	}

	/**
	 * Read number.
	 *
	 * @return the json number
	 * @throws JsonException the json exception
	 */
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

	/**
	 * Skip char.
	 *
	 * @throws JsonException the json exception
	 */
	private void skipChar() throws JsonException {
		try {
			reader.skip(1);
		} catch (IOException e) {
			throw new JsonException(e);
		}
	}

	/**
	 * Peek char.
	 *
	 * @return the char
	 * @throws JsonException the json exception
	 */
	private char peekChar() throws JsonException {
		char ch = readChar();
		resetReadChar();

		return ch;
	}

	/**
	 * Reset read char.
	 *
	 * @throws JsonException the json exception
	 */
	private void resetReadChar() throws JsonException {
		try {
			reader.reset();
		} catch (IOException e) {
			throw new JsonException(e);
		}
	}

	/**
	 * Read char.
	 *
	 * @return the char
	 * @throws JsonException the json exception
	 */
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

	/**
	 * Skip whitespace.
	 *
	 * @throws JsonException the json exception
	 */
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
