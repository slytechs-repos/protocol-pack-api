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

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;
import java.util.spi.AbstractResourceBundleProvider;
import java.util.spi.ResourceBundleProvider;

/**
 * The Class BlockStringResourceBundleProvider.
 */
public class BlockStringResourceBundleProvider extends AbstractResourceBundleProvider
		implements ResourceBundleProvider {

	/**
	 * Rewrite string blocks.
	 *
	 * @param in the in
	 * @return the input stream
	 */
	private static InputStream rewriteStringBlocks(InputStream in) {
		return new InputStream() {

			// Buffer up to 3 characters
			int nextch1 = -1;
			int nextch2 = -1;
			int nextch3 = -1;

			// In rewrite, every \n is rewritten as \\\n in between block quotes """..."""
			boolean rewriteStringBlock = false;

			@Override
			public synchronized int read() throws IOException {

				if (nextch1 != -1) {
					int ch = nextch1;

					nextch1 = nextch2;
					nextch2 = nextch3;
					nextch3 = -1;

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
							nextch1 = nextch2 = nextch3 = -1;
							rewriteStringBlock = !rewriteStringBlock; // toggle

							in.read(); // Consume \n on """ terminator

							ch = in.read();
						}
					}

				}

				if (rewriteStringBlock && (ch == '\n')) {
					nextch1 = 'n';
					nextch2 = '\\';
					nextch3 = ch;
					ch = '\\';

				} else if (rewriteStringBlock && ((ch == '\t') || (ch == ' '))) {
					nextch1 = ch;
					ch = '\\';
				}

				return ch;
			}

		};
	}

	/** The module. */
	private final Module module = getClass().getModule();
	
	/** The control. */
	private final Control control = new Control() {
	};

	/**
	 * Gets the bundle.
	 *
	 * @param baseName the base name
	 * @param locale   the locale
	 * @return the bundle
	 * @see java.util.spi.AbstractResourceBundleProvider#getBundle(java.lang.String,
	 *      java.util.Locale)
	 */
	@Override
	public ResourceBundle getBundle(String baseName , Locale locale) {
		List<Locale> candidateLocales = control.getCandidateLocales(baseName, locale);
		InputStream in = null;

		for (Locale loc : candidateLocales) {
			String bundleName = super.toBundleName(baseName, loc);
			String resourceName = control.toResourceName(bundleName, "properties");

			in = module.getClassLoader().getResourceAsStream(resourceName);
			if (in != null)
				break;
		}

		if (in == null)
			throw new MissingResourceException("Can't find bundle for base name " + baseName + ", locale " + locale,
					baseName + "_" + locale, // className
					"");

		return newBundle(rewriteStringBlocks(in));

	}

	/**
	 * New bundle.
	 *
	 * @param in the in
	 * @return the resource bundle
	 */
	private ResourceBundle newBundle(InputStream in) {
		try {
			return new PropertyResourceBundle(in);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
