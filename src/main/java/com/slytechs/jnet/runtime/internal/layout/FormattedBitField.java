/*
 * MIT License
 * 
 * Copyright (c) 2020 Sly Technologies Inc.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.slytechs.jnet.runtime.internal.layout;

import java.util.function.Function;
import java.util.function.LongUnaryOperator;

/**
 * The Class FormattedBitField.
 */
public class FormattedBitField implements BitField.Proxy {

	public interface ValueFormatter<T> {

		public interface OfLong extends ValueFormatter<Long> {
			@Override
			Object formatObject(Long value);
		}

		Object formatObject(T value);

		default String format(T value) {
			return formatObject(value).toString();
		}

		default <Q> String format(Q value, Function<Q, T> adapter) {
			return formatObject(adapter.apply(value)).toString();
		}

	}

	public interface FieldFormatter {
		String format(BinaryField field, String value);

		default String format(BinaryField field, String value, Object data, long[] seq) {
			return format(field, value);
		}
	}

	public interface BitFieldFormatter {

		String format(BitField field, Object data, long... sequences);

	}

	public static String toString(BitField field, Object data, long[] sequences) {
		return String.format("%2$20s = %d/0x%1$x",
				field.getUnsignedNumber(data, sequences),
				field.fieldName().orElse("field"),
				data,
				sequences);
	}

	public static String toString(String format, BitField field, Object data, long[] sequences) {
		return String.format(format,
				field.getUnsignedNumber(data, sequences),
				field.fieldName().orElse("field"),
				data,
				sequences);
	}

	private final BitField proxyField;

	private BitFieldFormatter formatter = FormattedBitField::toString;

	FormattedBitField(BitField proxy) {
		this.proxyField = proxy;
	}

	public BitField format(String format) {
		this.formatter = FormattedBitField::toString;
		return this;
	}

	public BitField format(String format, ValueFormatter.OfLong formatter) {
		this.formatter = (field, data, seq) -> {
			long v = field.getLong(data, seq);

			return String.format(format,
					formatter.format(v),
					field.fieldName().orElse("field"),
					data,
					seq);
		};

		return this;
	}

	public BitField format(String format, ValueFormatter.OfLong formatter, LongUnaryOperator adapter) {
		this.formatter = (field, data, seq) -> {
			long v = adapter.applyAsLong(field.getLong(data, seq));

			return String.format(format,
					formatter.format(v),
					field.fieldName().orElse("field"),
					data,
					seq);
		};

		return this;
	}

	public BitField formatter(BitFieldFormatter formatter) {
		this.formatter = formatter;

		return this;
	}

	public BitField formatter(ValueFormatter.OfLong formatter) {
		return format("%2$20s = %s", formatter);
	}

	public BitField formatter(ValueFormatter.OfLong formatter, LongUnaryOperator adapter) {
		return format("%2$20s = %s", formatter, adapter);

	}

	/**
	 * Proxy bit field.
	 *
	 * @return the bit field
	 * @see com.slytechs.jnet.runtime.internal.layout.BitField.Proxy#proxyBitField()
	 */
	@Override
	public BitField proxyBitField() {
		return proxyField;
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.layout.BitField.Proxy#toString(java.lang.Object, long[])
	 */
	@Override
	public String toString(Object data, long... sequences) {
		return formatter.format(proxyField, data, sequences);
	}

	@Override
	public String toString() {
		return proxyBitField().toString();
	}

}