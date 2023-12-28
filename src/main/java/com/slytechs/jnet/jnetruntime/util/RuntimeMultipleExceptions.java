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
package com.slytechs.jnet.jnetruntime.util;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;

/**
 * Throws a runtime exception with multiple cause exceptions.
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public class RuntimeMultipleExceptions extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 910550746521088540L;

	/** The causes. */
	private final Collection<? extends Throwable> causes;

	/**
	 * Instantiates a new runtime multiple exceptions.
	 *
	 * @param causes the causes
	 */
	public RuntimeMultipleExceptions(Throwable... causes) {
		this(Arrays.asList(causes));
	}

	/**
	 * Instantiates a new runtime multiple exceptions.
	 *
	 * @param causes the causes
	 */
	public RuntimeMultipleExceptions(Collection<? extends Throwable> causes) {
		this.causes = causes;
	}

	/**
	 * Instantiates a new runtime multiple exceptions.
	 *
	 * @param message the message
	 * @param causes  the causes
	 */
	public RuntimeMultipleExceptions(String message, Throwable... causes) {
		this(message, Arrays.asList(causes));
	}

	/**
	 * Instantiates a new runtime multiple exceptions.
	 *
	 * @param message the message
	 * @param causes  the causes
	 */
	public RuntimeMultipleExceptions(String message, Collection<? extends Throwable> causes) {
		super(message);
		this.causes = causes;
	}

	/**
	 * Gets the causes.
	 *
	 * @return the causes
	 */
	public Collection<? extends Throwable> getCauses() {
		return causes;
	}

	/**
	 * @see java.lang.Throwable#toString()
	 */
	@Override
	public String toString() {
		var b = new StringBuilder();

		if (getMessage() != null)
			b.append(getMessage())
					.append("\n");

		if (!causes.isEmpty()) {
			b.append("caused by=");

			for (Throwable e : causes) {
				b.append(e.getMessage())
						.append(',');
			}

			b.append(" exceptions");

		}

		return b.toString();
	}

	/**
	 * @see java.lang.Throwable#printStackTrace(java.io.PrintStream)
	 */
	@Override
	public void printStackTrace(PrintStream s) {
		super.printStackTrace(s);

		for (Throwable e : causes) {
			e.printStackTrace(s);
		}
	}

}
