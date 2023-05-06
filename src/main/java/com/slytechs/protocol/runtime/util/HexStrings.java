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

import static java.lang.Integer.*;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.function.IntFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.slytechs.protocol.runtime.internal.util.ByteArraySlice;

/**
 * The Class HexStrings.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public class HexStrings {

	/**
	 * The Interface Spacer.
	 */
	public interface Spacer {

		/**
		 * 1 space between every column and 2 spaces every 4 cols
		 * 
		 * ie.: {@code [4A 75 73 74  20 73 6F 6D  65 20 64 61  74 61 2E AA]}
		 */
		Spacer HEXDUMP_COLUMN = (col) -> (col == 0)
				? "" // No spaces
				: (((col % 4) == 0) && (col != 0))
						? "  " // 2 spaces
						: " "; // 1 space

		/** The no space. */
		Spacer NO_SPACE = (col) -> "";

		/** The hexdump to textdump. */
		Spacer HEXDUMP_TO_TEXTDUMP = (width) -> "    ";

		/**
		 * Spaces.
		 *
		 * @param colOrWidth the col or width
		 * @return the string
		 */
		String spaces(int colOrWidth);
	}

	/** The Constant DEFAULT_HEXDUMP_WIDTH. */
	public static final int DEFAULT_HEXDUMP_WIDTH = 16;

	/** Wireshark like "slice" attribute modifier regex pattern. */
	static final Pattern SLICE_PATTERN = Pattern.compile(""
	// @formatter:off
	+ "^(([-]?\\d+):(\\d+))$" + "|" // 1 [i:j] i = start_offset, j = length
	+ "^((\\d+)-(\\d+))$"     + "|" // 2 [i-j] i = start_offset, j = end_offset, inclusive.
	+ "^([-]?\\d+)$"          + "|" // 3 [i] i = start_offset, length = 1
	+ "^:(\\d+)$"             + "|" // 4 [:j] start_offset = 0, length = j
	+ "^([-]?\\d+):$"         + ""  // 5 [i:] start_offset = i, end_offset = end_of_field
	+ "");
	// @formatter:on
	/** The Constant DEFAULT_HEX_SEPARATOR. */
	public static final char DEFAULT_HEX_SEPARATOR = ':';

	/** The Constant DEFAULT_HEX_ALT_SEPARATOR. */
	public static final char DEFAULT_HEX_ALT_SEPARATOR = '-';

	/** The Constant DEFAULT_DEC_SEPARATOR. */
	public static final char DEFAULT_DEC_SEPARATOR = '.';

	/** The Constant DEFAULT_CONTROL_CHAR. */
	public static final char DEFAULT_CONTROL_CHAR = '.';

	/** The Constant DEFAULT_HEXDUMP_PREFIX. */
	public static final IntFunction<String> DEFAULT_HEXDUMP_PREFIX = i -> "%04X: ".formatted(i);

	/**
	 * Parses the array.
	 *
	 * @param encodedString the encoded string
	 * @param byteSeparator the byte separator
	 * @param radix         the radix
	 * @return the byte[]
	 */
	public static byte[] parseArray(String encodedString, char byteSeparator, int radix) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Parses the array or integer number.
	 *
	 * @param sourceString the source string
	 * @return the object
	 */
	public static Object parseArrayOrIntegerNumber(String sourceString) {
		try {
			return Long.parseLong(sourceString);
		} catch (Throwable e) {}

		try {
			return HexStrings.parseArray(sourceString, DEFAULT_HEX_SEPARATOR, 16); // In hex format 00:11:22:33:44:55
																					// etc...
		} catch (Throwable e) {}

		try {
			return HexStrings.parseArray(sourceString, DEFAULT_DEC_SEPARATOR, 10); // In decimal format 192.168.0.0
		} catch (Throwable e) {}

		throw new IllegalArgumentException("unable to parse string to a byte array or a number " + sourceString);
	}

	/**
	 * Parses the array slice.
	 *
	 * @param id the id
	 * @return the byte array slice
	 */
	public static ByteArraySlice parseArraySlice(String id) {
		String[] sliceStr = id.replaceFirst(".*\\[([\\d-:,]+)\\]", "$1")
				.split(",");
		if (sliceStr.length == 1)
			return parseSingleSlice(sliceStr[0]);

		ByteArraySlice[] slices = new ByteArraySlice[sliceStr.length];

		for (int i = 0; i < sliceStr.length; i++) {
			String str = sliceStr[i];
			slices[i] = parseSingleSlice(str);
		}

		return new ByteArraySlice(slices);
	}

	/**
	 * Parses the byte array.
	 *
	 * @param sourceString the source string
	 * @return the byte[]
	 */
	public static byte[] parseByteArray(String sourceString) {

		try {
			return parseArray(sourceString, DEFAULT_HEX_SEPARATOR, 16); // In hex format 00:11:22:33:44:55 etc...
		} catch (Throwable e) {}

		try {
			return parseArray(sourceString, DEFAULT_DEC_SEPARATOR, 10); // In decimal format 192.168.0.0
		} catch (Throwable e) {}

		throw new IllegalArgumentException("unable to parse string to a byte array " + sourceString);
	}

	/**
	 * Parses the hex string.
	 *
	 * @param hexString the hex string
	 * @return the byte[]
	 */
	public static byte[] parseHexString(String hexString) {
		hexString = hexString.replaceAll("[:\\-\\s]", "");
		if ((hexString.length() % 2) != 0)
			throw new IllegalArgumentException("odd number of hex digits [%d]"
					.formatted(hexString.length()));

		byte[] array = new byte[hexString.length() / 2];
		for (int i = 0; i < array.length; i++)
			array[i] = (byte) Integer.parseInt(hexString, (i * 2), ((i * 2) + 2), 16);

		return array;
	}

	/**
	 * Parses the hex string.
	 *
	 * @param hexStringLines the hex string lines
	 * @return the byte[]
	 */
	public static byte[] parseHexString(String... hexStringLines) {
		return parseHexString(Arrays.stream(hexStringLines)
				.collect(Collectors.joining()));
	}

	/**
	 * Parses the single slice.
	 *
	 * @param str the str
	 * @return the byte array slice
	 */
	private static ByteArraySlice parseSingleSlice(String str) {
		Matcher matcher = SLICE_PATTERN.matcher(str);

		if (!matcher.find())
			return null;

		for (int j = 1; j < matcher.groupCount() + 1; j++) {
			if (matcher.group(j) == null)
				continue;

			return switch (j) {

			// @formatter:off
			case /* [i:j] */ 1 -> ByteArraySlice.range   (parseInt(matcher.group(2)), parseInt(matcher.group(3)));
			case /* [i-j] */ 4 -> ByteArraySlice.between (parseInt(matcher.group(5)), parseInt(matcher.group(6)));
			case /* [i]   */ 7 -> ByteArraySlice.byteAt  (parseInt(matcher.group(7)));
			case /* [:j]  */ 8 -> ByteArraySlice.begining(parseInt(matcher.group(8)));
			case /* [i:]  */ 9 -> ByteArraySlice.from    (parseInt(matcher.group(9)));
			// @formatter:on

			default -> throw new IllegalArgumentException("no match for data slice: [" + str + "]");
			};
		}

		throw new IllegalStateException();
	}

	/**
	 * To dec string prefixed.
	 *
	 * @param array     the array
	 * @param offset    the offset
	 * @param length    the length
	 * @param delimiter the delimiter
	 * @param prefix    the prefix
	 * @param suffix    the suffix
	 * @return the string
	 */
	public static String toDecStringPrefixed(byte[] array, int offset, int length, String delimiter, String prefix,
			String suffix) {
		return IntStream.range(offset, length)
				.mapToObj(i -> String.format("%d", Byte.toUnsignedInt(array[i])))
				.collect(Collectors.joining(delimiter, prefix, suffix));
	}

	public static String toHexDump(
			byte[] array, int offset, int length) {
		return toHexDump(new StringBuilder(), array, offset, length, DEFAULT_HEXDUMP_WIDTH, i -> "%04X: ".formatted(i))
				.toString();
	}

	public static String toHexDump(
			byte[] array, int offset, int length,
			int width,
			IntFunction<String> prefix) {
		return toHexDump(new StringBuilder(), array, offset, length, width, prefix).toString();
	}

	/**
	 * To hex dump.
	 *
	 * @param sb     the sb
	 * @param array  the array
	 * @param offset the offset
	 * @param length the length
	 * @param width  the width
	 * @param prefix the prefix
	 * @return the string builder
	 */
	public static StringBuilder toHexDump(
			StringBuilder sb,
			byte[] array, int offset, int length,
			int width,
			IntFunction<String> prefix) {

		for (int i = offset; i < length; i += width) {
			int len = width;

			if ((i + len) > array.length) {
				len = array.length - i;
				width = len;
			}

			sb.append(prefix.apply(i));
			toHexDumpLine(sb, array, i, len, Spacer.HEXDUMP_COLUMN);
			sb.append("\n");
		}

		return sb;
	}

	/**
	 * To hex dump line.
	 *
	 * @param sb     the sb
	 * @param array  the array
	 * @param offset the offset
	 * @param length the length
	 * @param spacer the spacer
	 * @return the string builder
	 */
	private static StringBuilder toHexDumpLine(
			StringBuilder sb,
			byte[] array, int offset, int length,
			Spacer spacer) {

		for (int i = 0; i < DEFAULT_HEXDUMP_WIDTH; i++) {

			/* Add padding at the end of the line if needed */
			if (i >= length) {
				/* Insert white space */
				sb.append(spacer.spaces(i));

				sb.append("  ");
				continue;
			}

			int b = Byte.toUnsignedInt(array[offset + i]);

			/* Insert white space */
			sb.append(spacer.spaces(i));

			/* Insert HEX value, pad with '0' if necessary */
			if (b < 16)
				sb.append("0");

			sb.append(Integer.toHexString(b).toUpperCase());
		}

		return sb;
	}

	/**
	 * To hex dump.
	 *
	 * @param buffer the buffer
	 * @return the string
	 */
	public static String toHexDump(ByteBuffer buffer) {
		int pos = buffer.position();
		byte[] array = new byte[buffer.remaining()];

		buffer.get(pos, array, 0, buffer.remaining());

		return toHexDump(array, 0, array.length);
	}

	/**
	 * To hex string.
	 *
	 * @param array the array
	 * @return the string
	 */
	public static String toHexString(ByteBuffer buffer) {
		byte[] array = new byte[buffer.remaining()];
		buffer.get(buffer.position(), array);

		return toHexString(array);
	}

	/**
	 * To hex string.
	 *
	 * @param array the array
	 * @return the string
	 */
	public static String toHexString(byte[] array) {
		return toStringInRadix(16, array, 0, array.length);
	}

	/**
	 * To hex string.
	 *
	 * @param array  the array
	 * @param offset the offset
	 * @param length the length
	 * @return the string
	 */
	public static String toHexString(byte[] array, int offset, int length) {
		return toStringInRadix(16, array, offset, length);
	}

	/**
	 * To hex string.
	 *
	 * @param v the v
	 * @return the string
	 */
	public static String toHexString(int v) {
		return toHexString(v, new StringBuilder(2)).toString();
	}

	/**
	 * To hex string.
	 *
	 * @param v  the v
	 * @param sb the sb
	 * @return the string builder
	 */
	public static StringBuilder toHexString(int v, StringBuilder sb) {

		// Pad with a ZERO if single hex digit
		if ((v & 0xff) < 16)
			sb.append('0');

		sb.append(Integer.toHexString(v));

		return sb;
	}

	/**
	 * To hex string prefixed.
	 *
	 * @param array     the array
	 * @param offset    the offset
	 * @param length    the length
	 * @param delimiter the delimiter
	 * @param prefix    the prefix
	 * @param suffix    the suffix
	 * @return the string
	 */
	public static String toHexStringPrefixed(byte[] array, int offset, int length, String delimiter, String prefix,
			String suffix) {
		return IntStream.range(offset, offset + length)
				.mapToObj(i -> String.format("%02X", Byte.toUnsignedInt(array[i])))
				.collect(Collectors.joining(delimiter, prefix, suffix));
	}

	/**
	 * To hex text dump.
	 *
	 * @param sb    the sb
	 * @param array the array
	 * @return the string builder
	 */
	public static StringBuilder toHexTextDump(
			StringBuilder sb,
			byte[] array) {
		return toHexTextDump(sb, array, 0, array.length, DEFAULT_HEXDUMP_WIDTH);
	}

	/**
	 * To hex text dump.
	 *
	 * @param sb    the sb
	 * @param array the array
	 * @param width the width
	 * @return the string builder
	 */
	public static StringBuilder toHexTextDump(
			StringBuilder sb,
			byte[] array,
			int width) {
		return toHexTextDump(sb, array, 0, array.length, width);
	}

	/**
	 * To hex text dump.
	 *
	 * @param sb     the sb
	 * @param array  the array
	 * @param offset the offset
	 * @param length the length
	 * @param width  the width
	 * @return the string builder
	 */
	public static StringBuilder toHexTextDump(
			StringBuilder sb,
			byte[] array, int offset, int length,
			int width) {

		return toHexTextDump(sb, array, offset, length, width, i -> "");
	}

	/**
	 * To hex text dump.
	 *
	 * @param sb     the sb
	 * @param array  the array
	 * @param offset the offset
	 * @param length the length
	 * @param width  the width
	 * @param prefix the prefix
	 * @return the string builder
	 */
	public static StringBuilder toHexTextDump(
			StringBuilder sb,
			byte[] array, int offset, int length,
			int width,
			IntFunction<String> prefix) {

		for (int i = offset; i < length; i += width) {
			int len = width;

			if ((i + len) > array.length) {
				len = array.length - i;
				width = len;
			}

			sb.append(prefix.apply(i));
			toHexTextDumpLine(sb, array, i, len, Spacer.HEXDUMP_COLUMN);
			sb.append("\n");
		}

		return sb;
	}

	/**
	 * To hex text dump.
	 *
	 * @param sb     the sb
	 * @param array  the array
	 * @param width  the width
	 * @param prefix the prefix
	 * @return the string builder
	 */
	public static StringBuilder toHexTextDump(
			StringBuilder sb,
			byte[] array,
			int width,
			IntFunction<String> prefix) {
		return toHexTextDump(sb, array, 0, array.length, width, prefix);
	}

	/**
	 * To hex text dump.
	 *
	 * @param sb     the sb
	 * @param array  the array
	 * @param prefix the prefix
	 * @return the string builder
	 */
	public static StringBuilder toHexTextDump(
			StringBuilder sb,
			byte[] array,
			IntFunction<String> prefix) {
		return toHexTextDump(sb, array, 0, array.length, DEFAULT_HEXDUMP_WIDTH, prefix);
	}

	/**
	 * To hex text dump.
	 *
	 * @param buffer the buffer
	 * @return the string
	 */
	public static String toHexTextDump(ByteBuffer buffer) {
		return toHexTextDump(new StringBuilder(), buffer, DEFAULT_HEXDUMP_PREFIX)
				.toString();
	}

	/**
	 * To hex text dump.
	 *
	 * @param buffer the buffer
	 * @param prefix the prefix
	 * @return the string
	 */
	public static String toHexTextDump(ByteBuffer buffer, IntFunction<String> prefix) {
		return toHexTextDump(new StringBuilder(), buffer, prefix).toString();
	}

	/**
	 * To hex text dump.
	 *
	 * @param sb     the sb
	 * @param buffer the buffer
	 * @param prefix the prefix
	 * @return the string builder
	 */
	public static StringBuilder toHexTextDump(StringBuilder sb, ByteBuffer buffer, IntFunction<String> prefix) {
		int length = buffer.remaining();
		byte[] array = buffer.hasArray() ? buffer.array() : new byte[length];
		int offset = buffer.hasArray() ? buffer.position() : 0;

		if (!buffer.hasArray()) {
			buffer.get(buffer.position(), array);
			offset = 0;
		}

		return toHexTextDump(sb, array, offset, length,
				DEFAULT_HEXDUMP_WIDTH, HexStrings.DEFAULT_HEXDUMP_PREFIX);
	}

	/**
	 * To hex text dump line.
	 *
	 * @param sb     the sb
	 * @param array  the array
	 * @param offset the offset
	 * @param length the length
	 * @param spacer the spacer
	 * @return the string builder
	 */
	private static StringBuilder toHexTextDumpLine(
			StringBuilder sb,
			byte[] array, int offset, int length,
			Spacer spacer) {

		toHexDumpLine(sb, array, offset, length, spacer);

		/* Space between hexdump and textdump columns */
		sb.append(Spacer.HEXDUMP_TO_TEXTDUMP.spaces(length));

		toTextDumpLine(sb, array, offset, length);

		return sb;
	}

	/**
	 * To ip 4 string.
	 *
	 * @param array the array
	 * @return the string
	 */
	public static String toIp4String(byte[] array) {
		return toDecStringPrefixed(array, 0, array.length, "" + DEFAULT_DEC_SEPARATOR, "", "");
	}

	/**
	 * To ip 6 string.
	 *
	 * @param array the array
	 * @return the string
	 */
	public static String toIp6String(byte[] array) {
		return toHexStringPrefixed(array, 0, array.length, "" + DEFAULT_HEX_SEPARATOR, "", "");
	}

	/**
	 * To ip 4 string.
	 *
	 * @param array the array
	 * @return the string
	 */
	public static String toIpString(byte[] array) {
		if (array.length == 4)
			return toIp4String(array);

		if (array.length == 6)
			return toIp6String(array);

		return toHexStringPrefixed(array, 0, array.length, "" + DEFAULT_HEX_SEPARATOR, "", "");
	}

	/**
	 * To mac string.
	 *
	 * @param array the array
	 * @return the string
	 */
	public static String toMacString(byte[] array) {
		return toHexStringPrefixed(array, 0, array.length, "" + DEFAULT_HEX_SEPARATOR, "", "");
	}

	/**
	 * To mac string.
	 *
	 * @param mac          the mac
	 * @param oui          the oui
	 * @param prefixLength the prefix length
	 * @return the string
	 */
	public static String toMacString(byte[] mac, String oui, int prefixLength) {
		String fm = HexStrings.toHexStringPrefixed(
				mac,
				prefixLength,
				mac.length - prefixLength,
				":", oui + "_", "");

		return fm;
	}

	/**
	 * To string.
	 *
	 * @param array the array
	 * @return the string
	 */
	public static String toString(byte[] array) {
		return toString(array, 0, array.length, DEFAULT_HEX_SEPARATOR);
	}

	/**
	 * To string.
	 *
	 * @param array         the array
	 * @param separatorChar the separator char
	 * @return the string
	 */
	public static String toString(byte[] array, char separatorChar) {
		return toString(array, 0, array.length, separatorChar);
	}

	/**
	 * To string.
	 *
	 * @param array  the array
	 * @param offset the offset
	 * @param length the length
	 * @return the string
	 */
	public static String toString(byte[] array, int offset, int length) {
		return toString(array, offset, length, DEFAULT_HEX_SEPARATOR);
	}

	/**
	 * To string.
	 *
	 * @param array         the array
	 * @param offset        the offset
	 * @param length        the length
	 * @param separatorChar the separator char
	 * @return the string
	 */
	public static String toString(byte[] array, int offset, int length, char separatorChar) {
		String fmt;

		if ((separatorChar == DEFAULT_HEX_SEPARATOR) || (separatorChar == DEFAULT_HEX_ALT_SEPARATOR))
			fmt = "%02x";
		else if (separatorChar == DEFAULT_DEC_SEPARATOR)
			fmt = "%d";
		else
			fmt = "%d";

		int end = offset + length;

		StringBuilder b = new StringBuilder();
		for (int i = offset; i < end; i++) {
			b.append(String.format(fmt, Byte.toUnsignedInt(array[i]))
					.toString());

			if (i < (end - 1))
				b.append(separatorChar);
		}

		return b.toString();
	}

	/**
	 * To string prefixed.
	 *
	 * @param array  the array
	 * @param offset the offset
	 * @param length the length
	 * @return the string
	 */
	public static String toStringInRadix(byte[] array, int offset, int length) {
		return toStringInRadix(10, array, 0, array.length);
	}

	/**
	 * To string prefixed.
	 *
	 * @param base  the base
	 * @param array the array
	 * @return the string
	 */
	public static String toStringInRadix(int base, byte[] array) {
		return toStringInRadix(base, array, 0, array.length);
	}

	/**
	 * To string prefixed.
	 *
	 * @param radix  the radix
	 * @param array  the array
	 * @param offset the offset
	 * @param length the length
	 * @return the string
	 */
	public static String toStringInRadix(int radix, byte[] array, int offset, int length) {
		return IntStream.range(offset, offset + length)
				.mapToObj(i -> Integer.toString(Byte.toUnsignedInt(array[i]), radix))
				.collect(Collectors.joining(""));
	}

	/**
	 * To string prefixed.
	 *
	 * @param radix     the radix
	 * @param array     the array
	 * @param offset    the offset
	 * @param length    the length
	 * @param delimiter the delimiter
	 * @param prefix    the prefix
	 * @param suffix    the suffix
	 * @return the string
	 */
	public static String toStringInRadix(int radix, byte[] array, int offset, int length, String delimiter,
			String prefix,
			String suffix) {
		return IntStream.range(offset, offset + length)
				.mapToObj(i -> Integer.toString(Byte.toUnsignedInt(array[i]), radix))
				.collect(Collectors.joining(delimiter, prefix, suffix));
	}

	/**
	 * To text dump line.
	 *
	 * @param array the array
	 * @return the string
	 */
	public static String toTextDumpLine(byte[] array) {
		return toTextDumpLine(array, 0, array.length);
	}

	/**
	 * To text dump line.
	 *
	 * @param array  the array
	 * @param offset the offset
	 * @param length the length
	 * @return the string
	 */
	public static String toTextDumpLine(byte[] array, int offset, int length) {
		return toTextDumpLine(new StringBuilder(), array, offset, length).toString();
	}

	/**
	 * To text dump line.
	 *
	 * @param sb     the sb
	 * @param array  the array
	 * @param offset the offset
	 * @param length the length
	 * @return the string builder
	 */
	public static StringBuilder toTextDumpLine(StringBuilder sb, byte[] array, int offset, int length) {

		if ((offset + length) > array.length)
			length = (array.length - offset);

		for (int i = 0; i < DEFAULT_HEXDUMP_WIDTH; i++) {
			if (i >= length) {
				sb.append(' ');
				continue;
			}

			int cp = Byte.toUnsignedInt(array[i + offset]);

			/* Check for control otherwise printable characters */
			if (Character.isISOControl(cp))
				sb.append(DEFAULT_CONTROL_CHAR);
			else
				sb.append((char) cp);
		}

		return sb;
	}

	/**
	 * Instantiates a new hex strings.
	 */
	private HexStrings() {
		/* Not instantiable */
	}

}