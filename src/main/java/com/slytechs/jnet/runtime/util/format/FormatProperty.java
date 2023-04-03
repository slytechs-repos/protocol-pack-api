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
package com.slytechs.jnet.runtime.util.format;

import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.function.Function;

import com.slytechs.jnet.runtime.util.ByteArraySlice;

/**
 * The Interface FormatProperty.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author mark
 */
public interface FormatProperty {

	/**
	 * The Interface OfByteArray.
	 */
	public interface OfByteArray extends FormatProperty {
		
		/**
		 * The Interface OfByteArrayFunction.
		 */
		public interface OfByteArrayFunction {
			
			/**
			 * Apply.
			 *
			 * @param array the array
			 * @param slice the slice
			 * @return the format property
			 */
			FormatProperty apply(byte[] array, Optional<ByteArraySlice> slice);
		}

		/**
		 * The Interface OfByteArrayFormattedFunction.
		 */
		public interface OfByteArrayFormattedFunction {
			
			/**
			 * Apply.
			 *
			 * @param array     the array
			 * @param slice     the slice
			 * @param formatter the formatter
			 * @return the format property
			 */
			FormatProperty apply(byte[] array, Optional<ByteArraySlice> slice, Function<byte[], String> formatter);
		}

		/**
		 * Array value.
		 *
		 * @return the byte[]
		 */
		byte[] arrayValue();

		/**
		 * Byte value.
		 *
		 * @return the byte
		 */
		default byte byteValue() {
			return arrayValue()[0];
		}

		/**
		 * @see com.slytechs.jnet.runtime.util.format.FormatProperty#value()
		 */
		@Override
		default Object value() {
			return arrayValue();
		}
	}

	/**
	 * The Interface OfNumber.
	 */
	public interface OfNumber extends FormatProperty {

		/**
		 * Int value.
		 *
		 * @return the int
		 */
		default int intValue() {
			return numberValue().intValue();
		}

		/**
		 * Long value.
		 *
		 * @return the int
		 */
		default int longValue() {
			return numberValue().intValue();
		}

		/**
		 * Number value.
		 *
		 * @return the number
		 */
		Number numberValue();

		/**
		 * @see com.slytechs.jnet.runtime.util.format.FormatProperty#value()
		 */
		@Override
		default Object value() {
			return numberValue();
		}
	}

	/**
	 * A.
	 *
	 * @param array the array
	 * @return the of byte array
	 */
	static OfByteArray a(byte[] array) {
		return arrayProperty(array, Optional.empty());
	}

	/**
	 * Array property.
	 *
	 * @param array the array
	 * @param slice the slice
	 * @return the of byte array
	 */
	static OfByteArray arrayProperty(byte[] array, Optional<ByteArraySlice> slice) {
		return () -> slice.map(s -> s.sliceArray(ByteBuffer.wrap(array))).orElse(array);
	}

	/**
	 * Array property.
	 *
	 * @param array   the array
	 * @param adapter the adapter
	 * @return the of byte array
	 */
	static OfByteArray arrayProperty(byte[] array, Function<byte[], String> adapter) {
		return arrayProperty(array, Optional.empty(), adapter);
	}

	/**
	 * Array property.
	 *
	 * @param array     the array
	 * @param slice     the slice
	 * @param formatter the formatter
	 * @return the of byte array
	 */
	static OfByteArray arrayProperty(byte[] array, Optional<ByteArraySlice> slice, Function<byte[], String> formatter) {
		return new OfByteArray() {
			@Override
			public byte[] arrayValue() {
				return slice.map(s -> s.sliceArray(ByteBuffer.wrap(array))).orElse(array);
			}

			@Override
			public String stringValue() {
				return formatter.apply(arrayValue());
			}
		};
	}

	/**
	 * Number property.
	 *
	 * @param n the n
	 * @return the of number
	 */
	static OfNumber numberProperty(Number n) {
		return () -> n;
	}

	/**
	 * Text property.
	 *
	 * @param text the text
	 * @return the format property
	 */
	static FormatProperty textProperty(String text) {
		return () -> text;
	}

	/**
	 * String value.
	 *
	 * @return the string
	 */
	default String stringValue() {
		return value().toString();
	}

	/**
	 * Value.
	 *
	 * @return the object
	 */
	Object value();

	/**
	 * As byte array.
	 *
	 * @return the of byte array
	 */
	default OfByteArray asByteArray() {
		return (OfByteArray) this;
	}

	/**
	 * As number.
	 *
	 * @return the of number
	 */
	default OfNumber asNumber() {
		return (OfNumber) this;
	}

}
