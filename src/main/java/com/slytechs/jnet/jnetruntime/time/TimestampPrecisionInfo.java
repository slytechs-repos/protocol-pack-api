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
package com.slytechs.jnet.jnetruntime.time;

import java.util.concurrent.TimeUnit;

/**
 * The Interface TimestampPrecisionInfo.
 */
public interface TimestampPrecisionInfo {

	/**
	 * The precision of the time stamp or how many digits after the decimal will the
	 * time stamp provide. Note that resolution, if known, can differ from precision
	 * and provide less time stamp information that what the precision reports. That
	 * is, a millisecond precision may only have a 10 millisecond resolution, if
	 * using a standard system clock.
	 *
	 * @return The precision is returned as a {@link TimeUnit} constant and only the
	 *         constants which represent time units of fraction of a second are
	 *         supported.
	 */
	TimeUnit precisionTimeUnit();

	/**
	 * The precision of the time stamp or how many digits after the decimal will the
	 * time stamp provide. Note that resolution, if known, can differ from precision
	 * and provide less time stamp information that what the precision reports. That
	 * is, a millisecond precision may only have a 10 millisecond resolution, if
	 * using a standard system clock on some systems.
	 *
	 * @return The precision is returned as the absolute exponent value (i.e. 10^-9
	 *         will return 9) or how many zeros after the decimal place.
	 */
	int precision();

}