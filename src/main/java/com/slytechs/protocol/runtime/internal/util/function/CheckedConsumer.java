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

import java.util.function.Consumer;

/**
 * The Interface CheckedConsumer.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 * @param <T> the generic type
 */
public interface CheckedConsumer<T> {

	/**
	 * Wrap a consumer for checked exceptions. Any checked exception will be
	 * re-thrown as a unchecked exception and processing will stop.
	 *
	 * @param <T>             the generic type
	 * @param checkedConsumer the checked consumer
	 * @return the consumer
	 */
	static <T> Consumer<T> wrapConsumer(CheckedConsumer<T> checkedConsumer) {
		return t -> {
			try {
				checkedConsumer.accept(t);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		};
	}

	/**
	 * Accept.
	 *
	 * @param t the t
	 * @throws Exception the exception
	 */
	void accept(T t) throws Exception;
}
