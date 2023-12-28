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

import java.util.ResourceBundle.Control;
import java.util.spi.ResourceBundleControlProvider;

/**
 * Provides support for java block strings terminated with triple-quotes """, in
 * resource bundle properties files. As the resource file is read, the provided
 * input stream, detects and rewrites the stream as a series of \\\n terminated
 * lines in between the block quotes """. The triple block quotes themselves,
 * are also consumed and omitted from the stream.
 * 
 * For example:
 * 
 * <pre>
 * MyMessages.0="""
 * Hello World
 * How Is Your Day?
 * """
 * </pre>
 * 
 * Will be translated, at the InputStream level, to:
 * 
 * <pre>
 * MyMessages.0=Hello World\n\
 * How Is Your Day?
 * </pre>
 * 
 * As per block quote specification, all white space between the triple block
 * quotes is also preserved by escaping each whitespace character in the input
 * stream.
 */
public class BlockStringResourceBundleControlProvider implements ResourceBundleControlProvider {
	
	/** The Constant BLOCK_STRING_CONTROL. */
	private static final BlockStringControl BLOCK_STRING_CONTROL = new BlockStringControl();

	/**
	 * Gets the control.
	 *
	 * @param baseName the base name
	 * @return the control
	 * @see java.util.spi.ResourceBundleControlProvider#getControl(java.lang.String)
	 */
	@Override
	public Control getControl(String baseName) {
		return BLOCK_STRING_CONTROL;
	}

}
