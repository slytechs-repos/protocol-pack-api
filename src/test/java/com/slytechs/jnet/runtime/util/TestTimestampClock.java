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

import java.util.function.LongSupplier;

import org.junit.jupiter.api.Test;

class TestTimestampClock {

	/**
	 * Measure milli clock resolution.
	 *
	 * @param cps the cps
	 * @return the int
	 */
	private int measureMilliClockResolution(long cps) {

		for (int i = 1; i < 4; i++) {
			long scale = (long) Math.pow(10, (3 - i));
			long count = cps / (long) Math.pow(10, i);

			long last = 0;

			// Wind up the milli-clock so we measure full time unit
			while ((last = measureLowMilliTick(scale, last)) == 0)
				;

			final long SKIP = 1000;
			last = 0;
			long miss = 0;
			for (long j = 0; j < count; j++) {
				if ((j % SKIP) == 0 && (last = measureLowMilliTick(scale, last)) == 0)
					miss += 1;
			}

			System.out.printf("milli: i=%d count=%,-10d miss=%,-10d scale=%,d%n", i, count, miss, scale);
			if (miss > 100 || count < 10) {
				return i - 1;
			}
		}

		return 3;
	}

	private static final long MISS_TOLERANCE = 3;

	private int measureNanoClockResolution(long cps) {

		for (int i = 1; i < 10; i++) {
			long scale = (long) Math.pow(10, (9 - i));
			long count = cps / (long) Math.pow(10, i);

			long last = 0;

			// Wind up the milli-clock so we measure full time unit
			while ((last = measureLowNanoTick(scale, last)) == 0)
				;

			final long SKIP = 1;
			last = 0;
			long miss = 0;
			for (long j = 0; j < count; j++) {
				if ((count < 10_000_000 || (j % SKIP) == 0) && (last = measureLowNanoTick(scale, last)) == 0)
					miss++;
			}

			System.out.printf("nano: i=%d count=%,-10d miss=%,-10d scale=%,d%n", i, count, miss, scale);
			if (miss > 100 || count < 10) {
				return i - 1;
			}
		}

		return 9;
	}

	private long measureLowMilliTick(long scale, long last) {
		long ts = System.currentTimeMillis() / scale;

		return (ts == last || last == 0) ? ts : 0;
	}

	private long measureLowNanoTick(long scale, long last) {
		long ts = System.nanoTime() / scale;

		return (ts == last || last == 0) ? ts : 0;
	}

	private long measureCallsPerSecond(LongSupplier time, long scale) {

		double factor = 1;
		long callCount = 1;
		for (long count = 1_000_000; count > 0; count = callCount) {

			callCount = 0;
			long ts = time.getAsLong();
			for (long i = 0; i < count; i++) {
				callCount++;
			}

			long te = time.getAsLong();
			long td = te - ts;

			factor = (double) td / scale;

			System.out.printf("calls=%,d%n", callCount);
//			System.out.printf("timed=%,dns%n", td);
			System.out.printf("factor=%,f count=%,d%n", factor, count);

			callCount = (long) (callCount / (factor - .01d));

			if (factor >= 1.0)
				return callCount;
		}

		return callCount;
	}

	@Test
	void test() {
		long cps = measureCallsPerSecond(System::nanoTime, 1000_000_000l);
		System.out.printf("%,d calls/sec%n", cps);

		cps = measureCallsPerSecond(System::currentTimeMillis, 1000l);
		System.out.printf("%,d calls/sec%n", cps);

//		int milliRes = measureMilliClockResolution(cps);
//		System.out.printf("milli resolution=%d%n", milliRes);

//		int nanoRes = measureNanoClockResolution(cps);
//		System.out.printf("nano resolution=%d%n", nanoRes);

//		measureNanoClockResolution(cps);
	}

}
