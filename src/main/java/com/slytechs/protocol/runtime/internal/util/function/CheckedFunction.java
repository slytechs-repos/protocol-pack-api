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
package com.slytechs.protocol.runtime.internal.util.function;

import java.util.function.Function;

/**
 * The Interface CheckedFunction.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 * @param <T> the generic type
 * @param <R> the generic type
 */
public interface CheckedFunction<T, R> {
	
	/**
	 * Wrap function.
	 *
	 * @param <T>             the generic type
	 * @param <R>             the generic type
	 * @param checkedFunction the checked function
	 * @return the function
	 */
	static <T, R> Function<T, R> wrapFunction(CheckedFunction<T, R> checkedFunction) {
		return t -> {
			try {
				return checkedFunction.apply(t);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		};
	}

	/**
	 * Apply.
	 *
	 * @param t the t
	 * @return the r
	 * @throws Exception the exception
	 */
	R apply(T t) throws Exception;
}
