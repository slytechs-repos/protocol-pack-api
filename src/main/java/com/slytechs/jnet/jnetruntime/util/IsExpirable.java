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

import java.util.concurrent.TimeUnit;

import com.slytechs.jnet.jnetruntime.time.TimestampSource;

public interface IsExpirable {

	static boolean isExpirable(IsExpirable obj) {
		return obj == null || obj.isExpired();
	}

	boolean isExpired();

	default boolean isNotExpired() {
		return !isExpired();
	}

	long expiresIn(TimeUnit unit);

	TimestampSource timestampSource();

	void expire();
}