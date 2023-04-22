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

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 *
 */
class TestSystemProperties {

	static Random RANDOM = new Random();

	static String makeRandomizedPropertyName(String baseName) {
		return "%s_%016X".formatted(baseName, RANDOM.nextLong(0, Long.MAX_VALUE));
	}

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void intValue_NoUnits() {
		int EXPECTED = 10;
		int value = SystemProperties.intValue("ABC", EXPECTED);

		assertEquals(EXPECTED, value);
	}

	@Test
	void intValue_WithUnits() {
		int EXPECTED = 10 * 1024;
		int value = SystemProperties.intValue("ABC", 10, MemoryUnit.KILOBYTES);

		assertEquals(EXPECTED, value);
	}

	@Test
	void intValue_WithUnits_FromEnvironment() {
		final String PROPERTY = makeRandomizedPropertyName("intValue");

		System.setProperty(PROPERTY, "10 (kb)");

		int EXPECTED = MemoryUnit.KILOBYTES.toBytesAsInt(10);
		int value = SystemProperties.intValue(PROPERTY, 5, MemoryUnit.MEGABYTES);

		assertEquals(EXPECTED, value);
	}

}
