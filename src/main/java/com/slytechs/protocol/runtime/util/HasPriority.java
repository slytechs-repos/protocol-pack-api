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

import java.util.Comparator;

/**
 * Used to prioritized a protocol process of some kind based on a numerical
 * priority value.
 * 
 * <p>
 * The lower positive numerical value has the highest priority. This make value
 * of {@code 0} the highest priority possible.
 * </p>
 * <p>
 * When 2 or more priority values are the same, it is undefined which one takes
 * precedence.
 * </p>
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public interface HasPriority {

	/** Default comparator as a constant */
	Comparator<HasPriority> COMPARATOR = HasPriority::compare;

	/**
	 * Compares priorities of a and b.
	 *
	 * @param a first object to priority compare
	 * @param b the second object to priority compare
	 * @return either -1, 0 or 1 as a result of the compare
	 */
	static int compare(HasPriority a, HasPriority b) {
		return a.priority() - b.priority();
	}

	/**
	 * This object's priority value.
	 *
	 * @return the priority value between {@code 0} and {@code Integer.MAX_VALUE}.
	 */
	int priority();

}
