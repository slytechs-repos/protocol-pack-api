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
package com.slytechs.jnet.runtime.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.slytechs.jnet.runtime.time.Timestamp;
import com.slytechs.jnet.runtime.time.TimestampClock;
import com.slytechs.jnet.runtime.time.TimestampUnit;

class TestTimestamps {

	private static final TimestampClock clock = new TimestampClock(TimestampUnit.EPOCH_MILLI);

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	void ts_clock() {
		long ts = clock.timestamp();

		System.out.println(Instant.ofEpochMilli(ts));
	}

	@Test
	void clock_testResolution_1ns() {
		TimestampClock clock = new TimestampClock(TimestampUnit.EPOCH_NANO);

//		System.out.printf("ts_clock: measuredResolution=%s%n", clock.resolution());

		assertEquals(9, clock.resolution());

	}

	@Test
	void ts_PCAP_MICRO() {
		TimestampUnit unit = TimestampUnit.PCAP_MICRO;
		int seconds = 1299012313;
		int micros = 266821;

		long ts = unit.ofSecond(seconds, micros);

		assertEquals(0x4d6d5ad900041245l, ts);
		assertEquals("2011-03-01 15:45:13.266821000", new com.slytechs.jnet.runtime.time.Timestamp(ts, unit).toString());
	}

	@Test
	void ts_PCAP_NANO() {
		TimestampUnit unit = TimestampUnit.PCAP_NANO;
		int seconds = 1299012313; // 4d:6d:5a:d9 "2011-03-01 15:45:13"
		int nanos = 266821321; // 0f:e7:5e:c9 ".266821321"

		long ts = unit.ofSecond(seconds, nanos);

		assertEquals(0x4d6d5ad90fe75ec9l, ts);
		assertEquals("2011-03-01 15:45:13.266821321", new Timestamp(ts, unit).toString());
	}

	@Test
	void ts_EPOCH_MILLI() {
		TimestampUnit unit = TimestampUnit.EPOCH_MILLI;
		int seconds = 1299012313;
		int millis = 266;

		long ts = unit.ofSecond(seconds, millis);

		assertEquals(0x0000012e732ae0b2l, ts);
		assertEquals("2011-03-01 15:45:13.266000000", new Timestamp(ts, unit).toString());
	}

	@Test
	void ts_EPOCH_MICRO() {
		TimestampUnit unit = TimestampUnit.EPOCH_MICRO;
		int seconds = 1299012313;
		int micros = 266821;

		long ts = unit.ofSecond(seconds, micros);

		assertEquals(0x00049d71df7dba85l, ts);
		assertEquals("2011-03-01 15:45:13.266821000", new Timestamp(ts, unit).toString());
	}

	@Test
	void ts_EPOCH_NANO() {
		TimestampUnit unit = TimestampUnit.EPOCH_NANO;
		int seconds = 1299012313;
		int nanos = 266821321;

		long ts = unit.ofSecond(seconds, nanos);

		assertEquals(0x120704d1032098c9l, ts);
		assertEquals("2011-03-01 15:45:13.266821321", new Timestamp(ts, unit).toString());
	}

	@Test
	void ts_EPOCH_10NANO() {
		TimestampUnit unit = TimestampUnit.EPOCH_10NANO;
		int seconds = 1299012313;
		int nanos10 = 26682100;

		long ts = unit.ofSecond(seconds, nanos10);

		assertEquals(0x01cd807b4d1cdbf4l, ts);
		assertEquals("2011-03-01 15:45:13.266821000", new Timestamp(ts, unit).toString());
	}

}
