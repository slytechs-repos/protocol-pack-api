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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;

/**
 * The Class BlockStringControl.
 */
public class BlockStringControl extends Control {

	/**
	 * New bundle.
	 *
	 * @param baseName the base name
	 * @param locale   the locale
	 * @param format   the format
	 * @param loader   the loader
	 * @param reload   the reload
	 * @return the resource bundle
	 * @throws IllegalAccessException the illegal access exception
	 * @throws InstantiationException the instantiation exception
	 * @throws IOException            Signals that an I/O exception has occurred.
	 * @see java.util.ResourceBundle.Control#newBundle(java.lang.String,
	 *      java.util.Locale, java.lang.String, java.lang.ClassLoader, boolean)
	 */
	public ResourceBundle newBundle(String baseName ,
			Locale locale ,
			String format ,
			ClassLoader loader ,
			boolean reload) throws IllegalAccessException, InstantiationException, IOException {
		if (baseName == null || locale == null || format == null || loader == null)
			throw new NullPointerException();

		ResourceBundle bundle = null;
		String bundleName = toBundleName(baseName, locale);
		String resourceName = toResourceName(bundleName, format);
		InputStream stream = null;
		if (reload) {
			URL url = loader.getResource(resourceName);
			if (url != null) {
				URLConnection connection = url.openConnection();
				if (connection != null) {
// Disable caches to get fresh data for
// reloading.
					connection.setUseCaches(false);
					stream = connection.getInputStream();
				}
			}
		} else {
			stream = loader.getResourceAsStream(resourceName);
		}
		if (stream != null) {
			InputStream bis = rewriteStringBlocks(stream);
			bundle = new PropertyResourceBundle(bis);
			bis.close();
		}

		return bundle;
	}

	/**
	 * Rewrite string blocks.
	 *
	 * @param in the in
	 * @return the input stream
	 */
	private static InputStream rewriteStringBlocks(InputStream in) {
		System.out.println("BlockStringControl::rewriteStringBlocks HERE");
		return new InputStream() {

			// Buffer up to 2 characters
			int nextch1 = -1;
			int nextch2 = -1;

			// In rewrite, every \n is rewritten as \\\n in between block quotes """..."""
			boolean rewriteNewLine = false;

			@Override
			public synchronized int read() throws IOException {

				if (nextch1 != -1) {
					int ch = nextch1;
					nextch1 = nextch2;
					nextch2 = -1;
					return ch;
				}

				int ch = in.read();

				// Check for a start of a potential block quote terminator and start caching
				if (ch == '"') {
					nextch1 = in.read();

					if (nextch1 == '"') {
						nextch2 = in.read();

						if (nextch2 == '"') {
							// Consume the actual """ block quote terminators
							nextch1 = nextch2 = -1;
							rewriteNewLine = !rewriteNewLine; // toggle

							in.read(); // Consume \n on """ terminator

							ch = in.read();
						}
					}

				}

				if (rewriteNewLine && (ch == '\n') || Character.isWhitespace(ch)) {
					nextch1 = ch;
					ch = '\\';
				}

				return ch;
			}

		};
	}

	/** The Constant BLOCK_QUOTE. */
	public static final String BLOCK_QUOTE = """
			qrt
			\"""
				abc\"""
			""";

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException {

		InputStream in = rewriteStringBlocks(new ByteArrayInputStream(BLOCK_QUOTE.getBytes()));

		int i = 0;

		System.out.print('[');
		while ((i = in.read()) != -1)
			System.out.print((char) i);
		System.out.print(']');
	}

}
