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
package com.slytechs.jnet.jnetruntime.internal.util.format;

import java.util.stream.IntStream;

/**
 * The Class AbstractStringBuilder.
 *
 * @param <T_BUILDER> the generic type
 */
public abstract class AbstractStringBuilder<T_BUILDER extends AbstractStringBuilder<T_BUILDER>>
		implements Appendable, CharSequence {

	/** The out. */
	private final StringBuilder out;

	/**
	 * Instantiates a new abstract string builder.
	 *
	 * @param out the out
	 */
	public AbstractStringBuilder(StringBuilder out) {
		this.out = out;
	}

	/**
	 * Us.
	 *
	 * @return the t builder
	 */
	@SuppressWarnings("unchecked")
	private T_BUILDER us() {
		return (T_BUILDER) this;
	}

	/**
	 * Append.
	 *
	 * @param b the b
	 * @return the t builder
	 */
	public T_BUILDER append(boolean b) {
		out.append(b);

		return us();
	}

	/**
	 * Append.
	 *
	 * @param c the c
	 * @return the t builder
	 * @see java.lang.Appendable#append(char)
	 */
	@Override
	public T_BUILDER append(char c) {
		out.append(c);

		return us();
	}

	/**
	 * Append.
	 *
	 * @param str the str
	 * @return the t builder
	 */
	public T_BUILDER append(char[] str) {
		out.append(str);

		return us();
	}

	/**
	 * Append.
	 *
	 * @param str    the str
	 * @param offset the offset
	 * @param len    the len
	 * @return the t builder
	 */
	public T_BUILDER append(char[] str, int offset, int len) {
		out.append(str, offset, len);

		return us();
	}

	/**
	 * Append.
	 *
	 * @param s the s
	 * @return the t builder
	 * @see java.lang.Appendable#append(java.lang.CharSequence)
	 */
	@Override
	public T_BUILDER append(CharSequence s) {
		out.append(s);

		return us();
	}

	/**
	 * Append.
	 *
	 * @param s     the s
	 * @param start the start
	 * @param end   the end
	 * @return the t builder
	 * @see java.lang.Appendable#append(java.lang.CharSequence, int, int)
	 */
	@Override
	public T_BUILDER append(CharSequence s, int start, int end) {
		out.append(s, start, end);

		return us();
	}

	/**
	 * Append.
	 *
	 * @param d the d
	 * @return the t builder
	 */
	public T_BUILDER append(double d) {
		out.append(d);

		return us();
	}

	/**
	 * Append.
	 *
	 * @param f the f
	 * @return the t builder
	 */
	public T_BUILDER append(float f) {
		out.append(f);

		return us();
	}

	/**
	 * Append.
	 *
	 * @param i the i
	 * @return the t builder
	 */
	public T_BUILDER append(int i) {
		out.append(i);

		return us();
	}

	/**
	 * Append.
	 *
	 * @param lng the lng
	 * @return the t builder
	 */
	public T_BUILDER append(long lng) {
		out.append(lng);

		return us();
	}

	/**
	 * Append.
	 *
	 * @param obj the obj
	 * @return the t builder
	 */
	public T_BUILDER append(Object obj) {
		out.append(obj);

		return us();
	}

	/**
	 * Append.
	 *
	 * @param str the str
	 * @return the t builder
	 */
	public T_BUILDER append(String str) {
		out.append(str);

		return us();
	}

	/**
	 * Append.
	 *
	 * @param sb the sb
	 * @return the t builder
	 */
	public T_BUILDER append(StringBuffer sb) {
		out.append(sb);

		return us();
	}

	/**
	 * Append code point.
	 *
	 * @param codePoint the code point
	 * @return the t builder
	 */
	public T_BUILDER appendCodePoint(int codePoint) {
		out.appendCodePoint(codePoint);

		return us();
	}

	/**
	 * Capacity.
	 *
	 * @return the int
	 */
	public int capacity() {
		return out.capacity();
	}

	/**
	 * Char at.
	 *
	 * @param index the index
	 * @return the char
	 * @see java.lang.CharSequence#charAt(int)
	 */
	@Override
	public char charAt(int index) {
		return out.charAt(index);
	}

	/**
	 * Chars.
	 *
	 * @return the int stream
	 * @see java.lang.CharSequence#chars()
	 */
	@Override
	public IntStream chars() {
		return out.chars();
	}

	/**
	 * Code point at.
	 *
	 * @param index the index
	 * @return the int
	 */
	public int codePointAt(int index) {
		return out.codePointAt(index);
	}

	/**
	 * Code point before.
	 *
	 * @param index the index
	 * @return the int
	 */
	public int codePointBefore(int index) {
		return out.codePointBefore(index);
	}

	/**
	 * Code point count.
	 *
	 * @param beginIndex the begin index
	 * @param endIndex   the end index
	 * @return the int
	 */
	public int codePointCount(int beginIndex, int endIndex) {
		return out.codePointCount(beginIndex, endIndex);
	}

	/**
	 * Code points.
	 *
	 * @return the int stream
	 * @see java.lang.CharSequence#codePoints()
	 */
	@Override
	public IntStream codePoints() {
		return out.codePoints();
	}

	/**
	 * Compare to.
	 *
	 * @param another the another
	 * @return the int
	 */
	public int compareTo(StringBuilder another) {
		return out.compareTo(another);
	}

	/**
	 * Delete.
	 *
	 * @param start the start
	 * @param end   the end
	 * @return the t builder
	 */
	public T_BUILDER delete(int start, int end) {
		out.delete(start, end);

		return us();
	}

	/**
	 * Delete char at.
	 *
	 * @param index the index
	 * @return the t builder
	 */
	public T_BUILDER deleteCharAt(int index) {
		out.deleteCharAt(index);

		return us();
	}

	/**
	 * Ensure capacity.
	 *
	 * @param minimumCapacity the minimum capacity
	 */
	public void ensureCapacity(int minimumCapacity) {
		out.ensureCapacity(minimumCapacity);
	}

	/**
	 * Equals.
	 *
	 * @param obj the obj
	 * @return true, if successful
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		return out.equals(obj);
	}

	/**
	 * Gets the chars.
	 *
	 * @param srcBegin the src begin
	 * @param srcEnd   the src end
	 * @param dst      the dst
	 * @param dstBegin the dst begin
	 * @return the chars
	 */
	public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin) {
		out.getChars(srcBegin, srcEnd, dst, dstBegin);
	}

	/**
	 * Hash code.
	 *
	 * @return the int
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return out.hashCode();
	}

	/**
	 * Index of.
	 *
	 * @param str the str
	 * @return the int
	 */
	public int indexOf(String str) {
		return out.indexOf(str);
	}

	/**
	 * Index of.
	 *
	 * @param str       the str
	 * @param fromIndex the from index
	 * @return the int
	 */
	public int indexOf(String str, int fromIndex) {
		return out.indexOf(str, fromIndex);
	}

	/**
	 * Insert.
	 *
	 * @param offset the offset
	 * @param b      the b
	 * @return the t builder
	 */
	public T_BUILDER insert(int offset, boolean b) {
		out.insert(offset, b);

		return us();
	}

	/**
	 * Insert.
	 *
	 * @param offset the offset
	 * @param c      the c
	 * @return the t builder
	 */
	public T_BUILDER insert(int offset, char c) {
		out.insert(offset, c);

		return us();
	}

	/**
	 * Insert.
	 *
	 * @param offset the offset
	 * @param str    the str
	 * @return the t builder
	 */
	public T_BUILDER insert(int offset, char[] str) {
		out.insert(offset, str);

		return us();
	}

	/**
	 * Insert.
	 *
	 * @param index  the index
	 * @param str    the str
	 * @param offset the offset
	 * @param len    the len
	 * @return the t builder
	 */
	public T_BUILDER insert(int index, char[] str, int offset, int len) {
		out.insert(index, str, offset, len);

		return us();
	}

	/**
	 * Insert.
	 *
	 * @param dstOffset the dst offset
	 * @param s         the s
	 * @return the t builder
	 */
	public T_BUILDER insert(int dstOffset, CharSequence s) {
		out.insert(dstOffset, s);

		return us();
	}

	/**
	 * Insert.
	 *
	 * @param dstOffset the dst offset
	 * @param s         the s
	 * @param start     the start
	 * @param end       the end
	 * @return the t builder
	 */
	public T_BUILDER insert(int dstOffset, CharSequence s, int start, int end) {
		out.insert(dstOffset, s, start, end);

		return us();
	}

	/**
	 * Insert.
	 *
	 * @param offset the offset
	 * @param d      the d
	 * @return the t builder
	 */
	public T_BUILDER insert(int offset, double d) {
		out.insert(offset, d);

		return us();
	}

	/**
	 * Insert.
	 *
	 * @param offset the offset
	 * @param f      the f
	 * @return the t builder
	 */
	public T_BUILDER insert(int offset, float f) {
		out.insert(offset, f);

		return us();
	}

	/**
	 * Insert.
	 *
	 * @param offset the offset
	 * @param i      the i
	 * @return the t builder
	 */
	public T_BUILDER insert(int offset, int i) {
		out.insert(offset, i);

		return us();
	}

	/**
	 * Insert.
	 *
	 * @param offset the offset
	 * @param l      the l
	 * @return the t builder
	 */
	public T_BUILDER insert(int offset, long l) {
		out.insert(offset, l);

		return us();
	}

	/**
	 * Insert.
	 *
	 * @param offset the offset
	 * @param obj    the obj
	 * @return the t builder
	 */
	public T_BUILDER insert(int offset, Object obj) {
		out.insert(offset, obj);

		return us();
	}

	/**
	 * Insert.
	 *
	 * @param offset the offset
	 * @param str    the str
	 * @return the t builder
	 */
	public T_BUILDER insert(int offset, String str) {
		out.insert(offset, str);

		return us();
	}

	/**
	 * Checks if is empty.
	 *
	 * @return true, if is empty
	 * @see java.lang.CharSequence#isEmpty()
	 */
	@Override
	public boolean isEmpty() { return out.isEmpty(); }

	/**
	 * Last index of.
	 *
	 * @param str the str
	 * @return the int
	 */
	public int lastIndexOf(String str) {
		return out.lastIndexOf(str);
	}

	/**
	 * Last index of.
	 *
	 * @param str       the str
	 * @param fromIndex the from index
	 * @return the int
	 */
	public int lastIndexOf(String str, int fromIndex) {
		return out.lastIndexOf(str, fromIndex);
	}

	/**
	 * Length.
	 *
	 * @return the int
	 * @see java.lang.CharSequence#length()
	 */
	@Override
	public int length() {
		return out.length();
	}

	/**
	 * Offset by code points.
	 *
	 * @param index           the index
	 * @param codePointOffset the code point offset
	 * @return the int
	 */
	public int offsetByCodePoints(int index, int codePointOffset) {
		return out.offsetByCodePoints(index, codePointOffset);
	}

	/**
	 * Replace.
	 *
	 * @param start the start
	 * @param end   the end
	 * @param str   the str
	 * @return the t builder
	 */
	public T_BUILDER replace(int start, int end, String str) {
		out.replace(start, end, str);

		return us();
	}

	/**
	 * Reverse.
	 *
	 * @return the t builder
	 */
	public T_BUILDER reverse() {
		out.reverse();

		return us();
	}

	/**
	 * Sets the char at.
	 *
	 * @param index the index
	 * @param ch    the ch
	 */
	public void setCharAt(int index, char ch) {
		out.setCharAt(index, ch);
	}

	/**
	 * Sets the length.
	 *
	 * @param newLength the new length
	 */
	public void setLength(int newLength) {
		out.setLength(newLength);
	}

	/**
	 * Sub sequence.
	 *
	 * @param start the start
	 * @param end   the end
	 * @return the char sequence
	 * @see java.lang.CharSequence#subSequence(int, int)
	 */
	@Override
	public CharSequence subSequence(int start, int end) {
		return out.subSequence(start, end);
	}

	/**
	 * Substring.
	 *
	 * @param start the start
	 * @return the string
	 */
	public String substring(int start) {
		return out.substring(start);
	}

	/**
	 * Substring.
	 *
	 * @param start the start
	 * @param end   the end
	 * @return the string
	 */
	public String substring(int start, int end) {
		return out.substring(start, end);
	}

	/**
	 * To string.
	 *
	 * @return the string
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return out.toString();
	}

	/**
	 * Trim to size.
	 */
	public void trimToSize() {
		out.trimToSize();
	}

	/**
	 * As string builder.
	 *
	 * @return the string builder
	 */
	public StringBuilder asStringBuilder() {
		return out;
	}

}
