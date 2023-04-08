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
package com.slytechs.jnet.runtime.internal.layout;

import java.util.function.Function;
import java.util.function.LongUnaryOperator;

/**
 * The Class FormattedBitField.
 */
public class FormattedBitField implements BitField.Proxy {

	/**
	 * The Interface ValueFormatter.
	 *
	 * @param <T> the generic type
	 */
	public interface ValueFormatter<T> {

		/**
		 * The Interface OfLong.
		 */
		public interface OfLong extends ValueFormatter<Long> {
			
			/**
			 * @see com.slytechs.jnet.runtime.internal.layout.FormattedBitField.ValueFormatter#formatObject(java.lang.Object)
			 */
			@Override
			Object formatObject(Long value);
		}

		/**
		 * Format object.
		 *
		 * @param value the value
		 * @return the object
		 */
		Object formatObject(T value);

		/**
		 * Format.
		 *
		 * @param value the value
		 * @return the string
		 */
		default String format(T value) {
			return formatObject(value).toString();
		}

		/**
		 * Format.
		 *
		 * @param <Q>     the generic type
		 * @param value   the value
		 * @param adapter the adapter
		 * @return the string
		 */
		default <Q> String format(Q value, Function<Q, T> adapter) {
			return formatObject(adapter.apply(value)).toString();
		}

	}

	/**
	 * The Interface FieldFormatter.
	 */
	public interface FieldFormatter {
		
		/**
		 * Format.
		 *
		 * @param field the field
		 * @param value the value
		 * @return the string
		 */
		String format(BinaryField field, String value);

		/**
		 * Format.
		 *
		 * @param field the field
		 * @param value the value
		 * @param data  the data
		 * @param seq   the seq
		 * @return the string
		 */
		default String format(BinaryField field, String value, Object data, long[] seq) {
			return format(field, value);
		}
	}

	/**
	 * The Interface BitFieldFormatter.
	 */
	public interface BitFieldFormatter {

		/**
		 * Format.
		 *
		 * @param field     the field
		 * @param data      the data
		 * @param sequences the sequences
		 * @return the string
		 */
		String format(BitField field, Object data, long... sequences);

	}

	/**
	 * To string.
	 *
	 * @param field     the field
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the string
	 */
	public static String toString(BitField field, Object data, long[] sequences) {
		return String.format("%2$20s = %d/0x%1$x",
				field.getUnsignedNumber(data, sequences),
				field.fieldName().orElse("field"),
				data,
				sequences);
	}

	/**
	 * To string.
	 *
	 * @param format    the format
	 * @param field     the field
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the string
	 */
	public static String toString(String format, BitField field, Object data, long[] sequences) {
		return String.format(format,
				field.getUnsignedNumber(data, sequences),
				field.fieldName().orElse("field"),
				data,
				sequences);
	}

	/** The proxy field. */
	private final BitField proxyField;

	/** The formatter. */
	private BitFieldFormatter formatter = FormattedBitField::toString;

	/**
	 * Instantiates a new formatted bit field.
	 *
	 * @param proxy the proxy
	 */
	FormattedBitField(BitField proxy) {
		this.proxyField = proxy;
	}

	/**
	 * Format.
	 *
	 * @param format the format
	 * @return the bit field
	 */
	public BitField format(String format) {
		this.formatter = FormattedBitField::toString;
		return this;
	}

	/**
	 * Format.
	 *
	 * @param format    the format
	 * @param formatter the formatter
	 * @return the bit field
	 */
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

	/**
	 * Format.
	 *
	 * @param format    the format
	 * @param formatter the formatter
	 * @param adapter   the adapter
	 * @return the bit field
	 */
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

	/**
	 * Formatter.
	 *
	 * @param formatter the formatter
	 * @return the bit field
	 */
	public BitField formatter(BitFieldFormatter formatter) {
		this.formatter = formatter;

		return this;
	}

	/**
	 * Formatter.
	 *
	 * @param formatter the formatter
	 * @return the bit field
	 */
	public BitField formatter(ValueFormatter.OfLong formatter) {
		return format("%2$20s = %s", formatter);
	}

	/**
	 * Formatter.
	 *
	 * @param formatter the formatter
	 * @param adapter   the adapter
	 * @return the bit field
	 */
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
	 * To string.
	 *
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the string
	 * @see com.slytechs.jnet.runtime.internal.layout.BitField.Proxy#toString(java.lang.Object,
	 *      long[])
	 */
	@Override
	public String toString(Object data, long... sequences) {
		return formatter.format(proxyField, data, sequences);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return proxyBitField().toString();
	}

}