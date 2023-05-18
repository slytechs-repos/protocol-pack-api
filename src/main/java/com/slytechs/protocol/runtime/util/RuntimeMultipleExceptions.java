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
package com.slytechs.protocol.runtime.util;

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

	private static final long serialVersionUID = 910550746521088540L;

	private final Collection<? extends Throwable> causes;

	/**
	 * @param cause
	 */
	public RuntimeMultipleExceptions(Throwable... causes) {
		this(Arrays.asList(causes));
	}

	/**
	 * @param cause
	 */
	public RuntimeMultipleExceptions(Collection<? extends Throwable> causes) {
		this.causes = causes;
	}

	/**
	 * @param message
	 * @param cause
	 */
	public RuntimeMultipleExceptions(String message, Throwable... causes) {
		this(message, Arrays.asList(causes));
	}

	/**
	 * @param message
	 * @param cause
	 */
	public RuntimeMultipleExceptions(String message, Collection<? extends Throwable> causes) {
		super(message);
		this.causes = causes;
	}

	/**
	 * @return the causes
	 */
	public Collection<? extends Throwable> getCauses() {
		return causes;
	}

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

	@Override
	public void printStackTrace(PrintStream s) {
		super.printStackTrace(s);

		for (Throwable e : causes) {
			e.printStackTrace(s);
		}
	}

}
