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
package com.slytechs.jnet.runtime.internal;

import java.util.concurrent.TimeUnit;

/**
 * A simple benchmark harness.
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public interface Benchmark {

	public interface DurationResult {
		void duration(double tsecs);
	}

	/** Not public API - keep a few utility methods private */
	final class Impl {
		private static final Benchmark EMPTY = () -> System.out.println("EMPTY");

		static long currentTimeMillis() {
			return System.currentTimeMillis();
		}

		static Benchmark startTimed(long start, TimedResult a) {
			return () -> {
				long end = Impl.currentTimeMillis();
				double tsecs = Impl.tsecs(start, end, TimeUnit.MILLISECONDS);

				a.timed(start, end, tsecs);
			};
		}

		static double tsecs(long ts, long te, TimeUnit unit) {
			long td = te - ts;
			long nanos = unit.toNanos(td);

			return nanos / 1_000_000_000.;
		}

		private Impl() {
		}
	}

	public interface RateDetailedReport {
		void detailedResult(long ts, long te, double tsecs, double rate, long total);
	}

	public interface RateSummaryReport {
		void summaryResult(double tsecs, double rate, long total);
	}

	public interface TimedResult {
		void timed(long start, long end, double tsecs);
	}

	static Benchmark empty() {
		return Impl.EMPTY;
	}

	static Benchmark setup() {
		return () -> {};
	}

	static Benchmark setup(Runnable action) {
		return action::run;
	}

	default Benchmark andThen(Benchmark after) {
		return () -> {
			complete();
			after.complete();
		};
	}

	default Benchmark calculate(DurationResult result) {
		Benchmark after = Impl.startTimed(Impl.currentTimeMillis(),
				(ts, te, tsecs) -> result.duration(tsecs));

		return andThen(after);
	}

	default Benchmark calculate(TimedResult result) {
		Benchmark after = Impl.startTimed(Impl.currentTimeMillis(), result);

		return andThen(after);
	}

	default Benchmark reportRate(long total, RateDetailedReport detailed) {
		Benchmark after = Impl.startTimed(Impl.currentTimeMillis(), (ts, te, tsecs) -> {
			detailed.detailedResult(ts, te, tsecs, total / tsecs, total);
		});

		return andThen(after);
	}

	default Benchmark reportRate(long total, RateSummaryReport summary) {
		Benchmark after = Impl.startTimed(Impl.currentTimeMillis(), (ts, te, tsecs) -> {
			summary.summaryResult(tsecs, total / tsecs, total);
		});

		return andThen(after);
	}

	void complete();

}
