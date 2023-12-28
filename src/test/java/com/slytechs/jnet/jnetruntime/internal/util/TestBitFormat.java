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
package com.slytechs.jnet.jnetruntime.internal.util;

import static java.lang.Integer.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import com.slytechs.jnet.jnetruntime.internal.util.format.BitFormat2;
import com.slytechs.test.Tests;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 *
 */
class TestBitFormat {

	private static byte parseByteBinary(String pattern) {
		pattern = pattern
				.replaceAll("\\s", "")
				.replaceAll("\\.", "0");
		return (byte) Integer.parseInt(pattern, 2);
	}

	@Test
	void patt_arr2(TestInfo test) {
		var PATTERN = "1101 1111"; // 1234567
		var EXPECTED = PATTERN;
		byte[] DATA = { parseByteBinary(PATTERN),
				0 };
		BitFormat2 bf = new BitFormat2(PATTERN);
		var RESULT = bf.format(DATA, 0);

		Tests.out.println(test.getDisplayName() + "::\n" 
				+ "BF=" + bf
				+ ", DATA[0]=" + toBinaryString(parseByteBinary(PATTERN) & 0xFF)
				+ ", RESULT=" + RESULT
				+ "");

		assertEquals(EXPECTED, RESULT);
	}

	@Test
	void patt_arr3(TestInfo test) {
		var PATTERN = "11.1 .1.1"; // 12345678
		var EXPECTED = "11.0 .0.1";
		byte[] DATA = {0, parseByteBinary("1100 0001"),
				0 };
		BitFormat2 bf = new BitFormat2(PATTERN);
		var RESULT = bf.format(DATA, 8);

		Tests.out.println(test.getDisplayName() + "::\n" 
				+ "BF=" + bf
				+ ", PATTERN=" + toBinaryString(parseByteBinary(PATTERN) & 0xFF)
				+ ", RESULT=" + RESULT
				+ "");

		assertEquals(EXPECTED, RESULT);
	}
	
	@Test
	void patt_arr4(TestInfo test) {
		var PATTERN = "11.1 .1.1 1111"; // 12345678
		var EXPECTED = "11.0 .0.1 1011";
		byte[] DATA = {0, 
				parseByteBinary("1100 0001"),
				parseByteBinary("1011 0000"),
				0 };
		BitFormat2 bf = new BitFormat2(PATTERN);
		var RESULT = bf.format(DATA, 8);

		Tests.out.println(test.getDisplayName() + "::\n" 
				+ "  BF=" + bf
				+ ",\n  PATTERN=" + toBinaryString(parseByteBinary(PATTERN) & 0xFF)
				+ ",\n  RESULT=" + RESULT
				+ "");

		assertEquals(EXPECTED, RESULT);
	}

	@Test
	void patt_byte1(TestInfo test) {
		var PATTERN = "1111 1111";
		var EXPECTED = PATTERN;
		BitFormat2 bf = new BitFormat2(PATTERN);
		var RESULT = bf.format(parseByteBinary(PATTERN));

		Tests.out.println(test.getDisplayName() + "::\n" 
				+ "BF=" + bf);

		assertEquals(EXPECTED, RESULT);
	}

	@Test
	void patt_flags1(TestInfo test) {
		var PATTERN = ".ABC DEF.";
		var EXPECTED = PATTERN;
		BitFormat2 bf = new BitFormat2(PATTERN);
		var RESULT = bf.format(0b1111_1111);

		Tests.out.println(test.getDisplayName() + "::\n" 
				+ "BF=" + bf);

		assertEquals(EXPECTED, RESULT);
	}

	@Test
	void patt_num1(TestInfo test) {
		var PATTERN = "1111 1111";
		var EXPECTED = PATTERN;
		BitFormat2 bf = new BitFormat2(PATTERN);
		var RESULT = bf.format(0b1111_1111);

		Tests.out.println(test.getDisplayName() + "::\n" 
				+ "BF=" + bf);

		assertEquals(EXPECTED, RESULT);
	}

	@Test
	void patt_num2(TestInfo test) {
		var PATTERN = ".110 111.";
		var EXPECTED = ".111 111.";
		BitFormat2 bf = new BitFormat2(PATTERN);
		var RESULT = bf.format(0b1111_1111);

		Tests.out.println(test.getDisplayName() + "::\n" 
				+ "BF=" + bf);

		assertEquals(EXPECTED, RESULT);
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

}
