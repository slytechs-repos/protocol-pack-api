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
package com.slytechs.protocol.runtime.internal;

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

	/**
	 * The Interface DurationResult.
	 */
	public interface DurationResult {
		
		/**
		 * Duration.
		 *
		 * @param tsecs the tsecs
		 */
		void duration(double tsecs);
	}

	/**
	 * Not public API - keep a few utility methods private.
	 */
	final class Impl {
		
		/** The Constant EMPTY. */
		private static final Benchmark EMPTY = () -> System.out.println("EMPTY");

		/**
		 * Current time millis.
		 *
		 * @return the long
		 */
		static long currentTimeMillis() {
			return System.currentTimeMillis();
		}

		/**
		 * Start timed.
		 *
		 * @param start the start
		 * @param a     the a
		 * @return the benchmark
		 */
		static Benchmark startTimed(long start, TimedResult a) {
			return () -> {
				long end = Impl.currentTimeMillis();
				double tsecs = Impl.tsecs(start, end, TimeUnit.MILLISECONDS);

				a.timed(start, end, tsecs);
			};
		}

		/**
		 * Tsecs.
		 *
		 * @param ts   the ts
		 * @param te   the te
		 * @param unit the unit
		 * @return the double
		 */
		static double tsecs(long ts, long te, TimeUnit unit) {
			long td = te - ts;
			long nanos = unit.toNanos(td);

			return nanos / 1_000_000_000.;
		}

		/**
		 * Instantiates a new impl.
		 */
		private Impl() {
		}
	}

	/**
	 * The Interface RateDetailedReport.
	 */
	public interface RateDetailedReport {
		
		/**
		 * Detailed result.
		 *
		 * @param ts    the ts
		 * @param te    the te
		 * @param tsecs the tsecs
		 * @param rate  the rate
		 * @param total the total
		 */
		void detailedResult(long ts, long te, double tsecs, double rate, long total);
	}

	/**
	 * The Interface RateSummaryReport.
	 */
	public interface RateSummaryReport {
		
		/**
		 * Summary result.
		 *
		 * @param tsecs the tsecs
		 * @param rate  the rate
		 * @param total the total
		 */
		void summaryResult(double tsecs, double rate, long total);
	}

	/**
	 * The Interface TimedResult.
	 */
	public interface TimedResult {
		
		/**
		 * Timed.
		 *
		 * @param start the start
		 * @param end   the end
		 * @param tsecs the tsecs
		 */
		void timed(long start, long end, double tsecs);
	}

	/**
	 * Empty.
	 *
	 * @return the benchmark
	 */
	static Benchmark empty() {
		return Impl.EMPTY;
	}

	/**
	 * Setup.
	 *
	 * @return the benchmark
	 */
	static Benchmark setup() {
		return () -> {};
	}

	/**
	 * Setup.
	 *
	 * @param action the action
	 * @return the benchmark
	 */
	static Benchmark setup(Runnable action) {
		return action::run;
	}

	/**
	 * And then.
	 *
	 * @param after the after
	 * @return the benchmark
	 */
	default Benchmark andThen(Benchmark after) {
		return () -> {
			complete();
			after.complete();
		};
	}

	/**
	 * Calculate.
	 *
	 * @param result the result
	 * @return the benchmark
	 */
	default Benchmark calculate(DurationResult result) {
		Benchmark after = Impl.startTimed(Impl.currentTimeMillis(),
				(ts, te, tsecs) -> result.duration(tsecs));

		return andThen(after);
	}

	/**
	 * Calculate.
	 *
	 * @param result the result
	 * @return the benchmark
	 */
	default Benchmark calculate(TimedResult result) {
		Benchmark after = Impl.startTimed(Impl.currentTimeMillis(), result);

		return andThen(after);
	}

	/**
	 * Report rate.
	 *
	 * @param total    the total
	 * @param detailed the detailed
	 * @return the benchmark
	 */
	default Benchmark reportRate(long total, RateDetailedReport detailed) {
		Benchmark after = Impl.startTimed(Impl.currentTimeMillis(), (ts, te, tsecs) -> {
			detailed.detailedResult(ts, te, tsecs, total / tsecs, total);
		});

		return andThen(after);
	}

	/**
	 * Report rate.
	 *
	 * @param total   the total
	 * @param summary the summary
	 * @return the benchmark
	 */
	default Benchmark reportRate(long total, RateSummaryReport summary) {
		Benchmark after = Impl.startTimed(Impl.currentTimeMillis(), (ts, te, tsecs) -> {
			summary.summaryResult(tsecs, total / tsecs, total);
		});

		return andThen(after);
	}

	/**
	 * Complete.
	 */
	void complete();

}
