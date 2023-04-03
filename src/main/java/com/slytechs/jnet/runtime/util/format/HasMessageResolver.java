/*
 * Apache License, Version 2.0
 * 
 * Copyright 2013-2022 Sly Technologies Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.slytechs.jnet.runtime.util.format;

/**
 * The Interface HasMessageResolver.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author mark
 * @param <T> the generic type
 */
public interface HasMessageResolver<T> {

	/**
	 * Gets the message resolver.
	 *
	 * @return the message resolver
	 */
	MessageResolver<T> getMessageResolver();

	/**
	 * Sets the message resolver.
	 *
	 * @param newResolver the new message resolver
	 */
	void setMessageResolver(MessageResolver<T> newResolver);
}
