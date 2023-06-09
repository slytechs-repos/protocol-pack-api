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

/**
 * Interface which describes a segment or collection of elements (ie. bytes in a
 * buffer, a set of elements, etc) and provides various segment related
 * operations such as union, intersection, etc.
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public interface IntSegment {

	/**
	 * The Interface IntDisjointOp.
	 *
	 * @param <T> the generic type
	 */
	public interface IntDisjointOp<T extends IntSegment> {

		/**
		 * Record.
		 *
		 * @param offset the offset
		 * @param space  the space
		 * @param t      the t
		 */
		void record(int offset, int space, T t);
	}

	/**
	 * The Interface IntOp.
	 *
	 * @param <T> the generic type
	 */
	public interface IntOp<T extends IntSegment> {

		/**
		 * Record.
		 *
		 * @param t0    the t 0
		 * @param t1    the t 1
		 * @param value the value
		 */
		void record(T t0, T t1, int value);
	}

	/**
	 * Disjoint.
	 *
	 * @param <T>     the generic type
	 * @param offset  the offset
	 * @param segment the segment
	 * @return the int
	 */
	public static <T extends IntSegment> int disjoint(int offset, T segment) {
		int start = segment.start();
		return (offset >= start)
				? 0
				: start - offset;
	}

	/**
	 * Disjoint.
	 *
	 * @param <T>      the generic type
	 * @param segment1 the segment 1
	 * @param segment2 the segment 2
	 * @return the int
	 */
	public static <T extends IntSegment> int disjoint(T segment1, T segment2) {
		return disjoint(segment1.end(), segment2);
	}

	/**
	 * Disjoint.
	 *
	 * @param <T>      the generic type
	 * @param segments the segments
	 * @return the int
	 */
	public static <T extends IntSegment> int disjoint(T[] segments) {
		return disjoint(segments, 0, segments.length);
	}

	/**
	 * Disjoint.
	 *
	 * @param <T>      the generic type
	 * @param segments the segments
	 * @param offset   the offset
	 * @param length   the length
	 * @return the int
	 */
	public static <T extends IntSegment> int disjoint(T[] segments, int offset, int length) {
		int total = 0;
		int pos = 0;
		int limit = offset + length;
		for (int i = offset; i < limit; i++) {
			final T seg = segments[i];

			final int hole = disjoint(pos, seg);

			total += hole;
			pos = seg.endExclusive();
		}

		return total;
	}

	/**
	 * Disjoint.
	 *
	 * @param <T>        the generic type
	 * @param segments   the segments
	 * @param offset     the offset
	 * @param length     the length
	 * @param perSegment the per segment
	 * @return the int
	 */
	public static <T extends IntSegment> int disjoint(T[] segments, int offset, int length,
			IntDisjointOp<T> perSegment) {

		int total = 0;
		int pos = 0;
		int limit = offset + length;
		for (int i = offset; i < limit; i++) {
			final T seg = segments[i];

			final int hole = disjoint(pos, seg);

			perSegment.record(pos, hole, seg);

			total += hole;
			pos = seg.endExclusive();
		}

		return total;
	}

	/**
	 * Disjoint.
	 *
	 * @param <T>        the generic type
	 * @param segments   the segments
	 * @param perSegment the per segment
	 * @return the int
	 */
	public static <T extends IntSegment> int disjoint(T[] segments, IntDisjointOp<T> perSegment) {
		return disjoint(segments, 0, segments.length, perSegment);
	}

	/**
	 * If the two segments intersect, the number of elements that overlap will be
	 * returned.
	 *
	 * @param <T>      the generic segment type of like elements
	 * @param segment1 the first segment
	 * @param segment2 the second segment
	 * @return number of overlapping elements due to the intersection, or 0 if they
	 *         two segments do not intersect
	 */
	public static <T extends IntSegment> int intersection(T segment1, T segment2) {
		final int start0 = segment1.start(), start1 = segment2.start();
		final int end0 = segment1.end(), end1 = segment2.end();

		if ((end0 < start1) || (start0 > end1))
			return 0;

		final int start, end;

		if ((start0 >= start1) && (start0 <= end1)) {
			start = start0;
		} else
			start = start1;

		if ((end0 >= start1) && (end0 <= end1))
			end = end0;
		else
			end = end1;

		return (end - start) + 1;
	}

	/**
	 * Intersection.
	 *
	 * @param <T>      the generic type
	 * @param segments the segments
	 * @return the int
	 */
	public static <T extends IntSegment> int intersection(T[] segments) {
		return intersection(segments, 0, segments.length);
	}

	/**
	 * Intersection.
	 *
	 * @param <T>      the generic type
	 * @param segments the segments
	 * @param offset   the offset
	 * @param length   the length
	 * @return the int
	 */
	public static <T extends IntSegment> int intersection(T[] segments, int offset, int length) {
		int total = 0;
		int limit = offset + length;
		for (int i = offset; i < limit; i++) {
			final T t0 = segments[i];

			for (int j = i + 1; j < limit; j++) {
				final T t1 = segments[j];

				/*
				 * Check if t0 is overlayed by t1 and calculate the duplicate size in bytes
				 */
				final int overlap = IntSegment.intersection(t0, t1);
				if (overlap == 0)
					break;

				total += overlap;
			}
		}

		return total;
	}

	/**
	 * Intersection.
	 *
	 * @param <T>        the generic type
	 * @param segments   the segments
	 * @param offset     the offset
	 * @param length     the length
	 * @param perSegment the per segment
	 * @return the int
	 */
	public static <T extends IntSegment> int intersection(T[] segments, int offset, int length,
			IntOp<T> perSegment) {

		int total = 0;
		int limit = offset + length;
		for (int i = offset; i < limit; i++) {
			final T t0 = segments[i];

			for (int j = i + 1; j < limit; j++) {
				final T t1 = segments[j];

				/*
				 * Check if t0 is overlayed by t1 and calculate the duplicate size in bytes
				 */
				final int overlap = IntSegment.intersection(t0, t1);
				if (overlap == 0)
					break;

				total += overlap;
				perSegment.record(t0, t1, overlap);
			}
		}

		return total;
	}

	/**
	 * Intersection.
	 *
	 * @param <T>        the generic type
	 * @param segments   the segments
	 * @param perSegment the per segment
	 * @return the int
	 */
	public static <T extends IntSegment> int intersection(T[] segments, IntOp<T> perSegment) {
		return intersection(segments, 0, segments.length, perSegment);
	}

	/**
	 * End.
	 *
	 * @return the int
	 */
	default int end() {
		return start() + length() - 1;
	}

	/**
	 * End exclusive.
	 *
	 * @return the int
	 */
	default int endExclusive() {
		return start() + length();
	}

	/**
	 * Length.
	 *
	 * @return the int
	 */
	int length();

	/**
	 * Start.
	 *
	 * @return the int
	 */
	int start();
}
