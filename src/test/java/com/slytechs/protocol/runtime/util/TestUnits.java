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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 *
 */
class TestUnits {

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
	void parseUnits() {
		final var UNIT_CLASS = CountUnit.class;
		final var EXPECTED = CountUnit.KILO;

		assertEquals(EXPECTED, UnitUtils.parseUnits("10k", UNIT_CLASS));
		assertEquals(EXPECTED, UnitUtils.parseUnits("10 k", UNIT_CLASS));
		assertEquals(EXPECTED, UnitUtils.parseUnits("10-k", UNIT_CLASS));
		assertEquals(EXPECTED, UnitUtils.parseUnits("10_k", UNIT_CLASS));
		assertEquals(EXPECTED, UnitUtils.parseUnits("10 (k)", UNIT_CLASS));
		assertEquals(EXPECTED, UnitUtils.parseUnits("10@k", UNIT_CLASS));
	}

	@Test
	void stripUnits() {
		final var UNIT_CLASS = CountUnit.class;
		final var EXPECTED = "10";

		assertEquals(EXPECTED, UnitUtils.stripUnits("10k", UNIT_CLASS));
		assertEquals(EXPECTED, UnitUtils.stripUnits("10 k", UNIT_CLASS));
		assertEquals(EXPECTED, UnitUtils.stripUnits("10-k", UNIT_CLASS));
		assertEquals(EXPECTED, UnitUtils.stripUnits("10_k", UNIT_CLASS));
		assertEquals(EXPECTED, UnitUtils.stripUnits("10 (k)", UNIT_CLASS));
		assertEquals(EXPECTED, UnitUtils.stripUnits("10@k", UNIT_CLASS));
	}

}
