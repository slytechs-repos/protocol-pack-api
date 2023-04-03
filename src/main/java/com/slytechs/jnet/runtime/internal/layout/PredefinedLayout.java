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

import static com.slytechs.jnet.runtime.util.Bits.*;

import java.nio.ByteOrder;
import java.util.Optional;

import com.slytechs.jnet.runtime.internal.layout.ValueLayout.OfAddress;
import com.slytechs.jnet.runtime.internal.layout.ValueLayout.OfByte;
import com.slytechs.jnet.runtime.internal.layout.ValueLayout.OfChar;
import com.slytechs.jnet.runtime.internal.layout.ValueLayout.OfDouble;
import com.slytechs.jnet.runtime.internal.layout.ValueLayout.OfFloat;
import com.slytechs.jnet.runtime.internal.layout.ValueLayout.OfInt;
import com.slytechs.jnet.runtime.internal.layout.ValueLayout.OfLong;
import com.slytechs.jnet.runtime.internal.layout.ValueLayout.OfShort;

/**
 * The Class PredefinedLayout.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public final class PredefinedLayout {

	/**
	 * The Enum IntN16.
	 */
	public enum Int16 implements BinaryLayout.Proxy {

		/** The bits 00. */
		BITS_00,

		/** The bits 01. */
		BITS_01,

		/** The bits 02. */
		BITS_02,

		/** The bits 03. */
		BITS_03,

		/** The bits 04. */
		BITS_04,

		/** The bits 05. */
		BITS_05,

		/** The bits 06. */
		BITS_06,

		/** The bits 07. */
		BITS_07,

		/** The bits 08. */
		BITS_08,

		/** The bits 09. */
		BITS_09,

		/** The bits 10. */
		BITS_10,

		/** The bits 11. */
		BITS_11,

		/** The bits 12. */
		BITS_12,

		/** The bits 13. */
		BITS_13,

		/** The bits 14. */
		BITS_14,

		/** The bits 15. */
		BITS_15,

		/** The bits 16. */
		BITS_16

		;

		/** The Constant JAVA_ShORT. */
		public final static ValueLayout.OfShort JAVA_ShORT = new ValueLayout.OfShort(ByteOrder.nativeOrder(), 16)
				.withBitAlignment(DEFAULT_ALIGNMENT);

		/** The layout. */
		private final ValueLayout.OfShort layout;

		/**
		 * Instantiates a new int N 16.
		 */
		Int16() {
			long size = ordinal() > 16 ? 16 : ordinal();
			this.layout = new ValueLayout.OfShort(ByteOrder.nativeOrder(), size)
					.withBitAlignment(DEFAULT_ALIGNMENT);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout.Proxy#proxyLayout()
		 */
		@Override
		public BinaryLayout proxyLayout() {
			return layout;
		}
	}

	/**
	 * The Enum IntB16.
	 */
	public enum Int16be implements BinaryLayout.Proxy {

		/** The bits 00. */
		BITS_00,

		/** The bits 01. */
		BITS_01,

		/** The bits 02. */
		BITS_02,

		/** The bits 03. */
		BITS_03,

		/** The bits 04. */
		BITS_04,

		/** The bits 05. */
		BITS_05,

		/** The bits 06. */
		BITS_06,

		/** The bits 07. */
		BITS_07,

		/** The bits 08. */
		BITS_08,

		/** The bits 09. */
		BITS_09,

		/** The bits 10. */
		BITS_10,

		/** The bits 11. */
		BITS_11,

		/** The bits 12. */
		BITS_12,

		/** The bits 13. */
		BITS_13,

		/** The bits 14. */
		BITS_14,

		/** The bits 15. */
		BITS_15,

		/** The bits 16. */
		BITS_16

		;

		/** The Constant JAVA_ShORT. */
		public final static ValueLayout.OfShort JAVA_ShORT = new ValueLayout.OfShort(ByteOrder.BIG_ENDIAN, 16)
				.withBitAlignment(DEFAULT_ALIGNMENT);

		/** The layout. */
		private final ValueLayout.OfShort layout;

		/**
		 * Instantiates a new int B 16.
		 */
		Int16be() {
			long size = ordinal() > 16 ? 16 : ordinal();
			this.layout = new ValueLayout.OfShort(ByteOrder.BIG_ENDIAN, size)
					.withBitAlignment(DEFAULT_ALIGNMENT);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout.Proxy#proxyLayout()
		 */
		@Override
		public BinaryLayout proxyLayout() {
			return layout;
		}
	}

	/**
	 * The Enum IntL16.
	 */
	public enum Int16le implements BinaryLayout.Proxy {

		/** The bits 00. */
		BITS_00,

		/** The bits 01. */
		BITS_01,

		/** The bits 02. */
		BITS_02,

		/** The bits 03. */
		BITS_03,

		/** The bits 04. */
		BITS_04,

		/** The bits 05. */
		BITS_05,

		/** The bits 06. */
		BITS_06,

		/** The bits 07. */
		BITS_07,

		/** The bits 08. */
		BITS_08,

		/** The bits 09. */
		BITS_09,

		/** The bits 10. */
		BITS_10,

		/** The bits 11. */
		BITS_11,

		/** The bits 12. */
		BITS_12,

		/** The bits 13. */
		BITS_13,

		/** The bits 14. */
		BITS_14,

		/** The bits 15. */
		BITS_15,

		/** The bits 16. */
		BITS_16

		;

		/** The Constant JAVA_ShORT. */
		public final static ValueLayout.OfShort JAVA_ShORT = new ValueLayout.OfShort(ByteOrder.LITTLE_ENDIAN, 16)
				.withBitAlignment(DEFAULT_ALIGNMENT);

		/** The layout. */
		private final ValueLayout.OfShort layout;

		/**
		 * Instantiates a new int L 16.
		 */
		Int16le() {
			long size = ordinal() > 16 ? 16 : ordinal();
			this.layout = new ValueLayout.OfShort(ByteOrder.LITTLE_ENDIAN, size)
					.withBitAlignment(DEFAULT_ALIGNMENT);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout.Proxy#proxyLayout()
		 */
		@Override
		public BinaryLayout proxyLayout() {
			return layout;
		}
	}

	/**
	 * The Enum IntN32.
	 */
	public enum Int32 implements BinaryLayout.Proxy {

		/** The bits 00. */
		BITS_00,

		/** The bits 01. */
		BITS_01,

		/** The bits 02. */
		BITS_02,

		/** The bits 03. */
		BITS_03,

		/** The bits 04. */
		BITS_04,

		/** The bits 05. */
		BITS_05,

		/** The bits 06. */
		BITS_06,

		/** The bits 07. */
		BITS_07,

		/** The bits 08. */
		BITS_08,

		/** The bits 09. */
		BITS_09,

		/** The bits 10. */
		BITS_10,

		/** The bits 11. */
		BITS_11,

		/** The bits 12. */
		BITS_12,

		/** The bits 13. */
		BITS_13,

		/** The bits 14. */
		BITS_14,

		/** The bits 15. */
		BITS_15,

		/** The bits 16. */
		BITS_16,

		/** The bits 17. */
		BITS_17,

		/** The bits 18. */
		BITS_18,

		/** The bits 19. */
		BITS_19,

		/** The bits 20. */
		BITS_20,

		/** The bits 21. */
		BITS_21,

		/** The bits 22. */
		BITS_22,

		/** The bits 23. */
		BITS_23,

		/** The bits 24. */
		BITS_24,

		/** The bits 25. */
		BITS_25,

		/** The bits 26. */
		BITS_26,

		/** The bits 27. */
		BITS_27,

		/** The bits 28. */
		BITS_28,

		/** The bits 29. */
		BITS_29,

		/** The bits 30. */
		BITS_30,

		/** The bits 31. */
		BITS_31,

		/** The bits 32. */
		BITS_32,

		;

		/** The Constant JAVA_INT. */
		public final static ValueLayout.OfInt JAVA_INT = new ValueLayout.OfInt(ByteOrder.nativeOrder(), 32)
				.withBitAlignment(DEFAULT_ALIGNMENT);

		/** The layout. */
		private final ValueLayout.OfInt layout;

		/**
		 * Instantiates a new int N 32.
		 */
		Int32() {
			long size = ordinal() > 32 ? 32 : ordinal();
			this.layout = new ValueLayout.OfInt(ByteOrder.nativeOrder(), size)
					.withBitAlignment(DEFAULT_ALIGNMENT);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout.Proxy#proxyLayout()
		 */
		@Override
		public BinaryLayout proxyLayout() {
			return layout;
		}
	}

	/**
	 * The Enum IntB32.
	 */
	public enum Int32be implements BinaryLayout.Proxy {

		/** The bits 00. */
		BITS_00,

		/** The bits 01. */
		BITS_01,

		/** The bits 02. */
		BITS_02,

		/** The bits 03. */
		BITS_03,

		/** The bits 04. */
		BITS_04,

		/** The bits 05. */
		BITS_05,

		/** The bits 06. */
		BITS_06,

		/** The bits 07. */
		BITS_07,

		/** The bits 08. */
		BITS_08,

		/** The bits 09. */
		BITS_09,

		/** The bits 10. */
		BITS_10,

		/** The bits 11. */
		BITS_11,

		/** The bits 12. */
		BITS_12,

		/** The bits 13. */
		BITS_13,

		/** The bits 14. */
		BITS_14,

		/** The bits 15. */
		BITS_15,

		/** The bits 16. */
		BITS_16,

		/** The bits 17. */
		BITS_17,

		/** The bits 18. */
		BITS_18,

		/** The bits 19. */
		BITS_19,

		/** The bits 20. */
		BITS_20,

		/** The bits 21. */
		BITS_21,

		/** The bits 22. */
		BITS_22,

		/** The bits 23. */
		BITS_23,

		/** The bits 24. */
		BITS_24,

		/** The bits 25. */
		BITS_25,

		/** The bits 26. */
		BITS_26,

		/** The bits 27. */
		BITS_27,

		/** The bits 28. */
		BITS_28,

		/** The bits 29. */
		BITS_29,

		/** The bits 30. */
		BITS_30,

		/** The bits 31. */
		BITS_31,

		/** The bits 32. */
		BITS_32,

		;

		/** The Constant JAVA_INT. */
		public final static ValueLayout.OfInt JAVA_INT = new ValueLayout.OfInt(ByteOrder.BIG_ENDIAN, 32)
				.withBitAlignment(DEFAULT_ALIGNMENT);

		/** The layout. */
		private final ValueLayout.OfInt layout;

		/**
		 * Instantiates a new int B 32.
		 */
		Int32be() {
			long size = ordinal() > 32 ? 32 : ordinal();
			this.layout = new ValueLayout.OfInt(ByteOrder.BIG_ENDIAN, size)
					.withBitAlignment(DEFAULT_ALIGNMENT);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout.Proxy#proxyLayout()
		 */
		@Override
		public BinaryLayout proxyLayout() {
			return layout;
		}
	}

	/**
	 * The Enum IntL32.
	 */
	public enum Int32le implements BinaryLayout.Proxy {

		/** The bits 00. */
		BITS_00,

		/** The bits 01. */
		BITS_01,

		/** The bits 02. */
		BITS_02,

		/** The bits 03. */
		BITS_03,

		/** The bits 04. */
		BITS_04,

		/** The bits 05. */
		BITS_05,

		/** The bits 06. */
		BITS_06,

		/** The bits 07. */
		BITS_07,

		/** The bits 08. */
		BITS_08,

		/** The bits 09. */
		BITS_09,

		/** The bits 10. */
		BITS_10,

		/** The bits 11. */
		BITS_11,

		/** The bits 12. */
		BITS_12,

		/** The bits 13. */
		BITS_13,

		/** The bits 14. */
		BITS_14,

		/** The bits 15. */
		BITS_15,

		/** The bits 16. */
		BITS_16,

		/** The bits 17. */
		BITS_17,

		/** The bits 18. */
		BITS_18,

		/** The bits 19. */
		BITS_19,

		/** The bits 20. */
		BITS_20,

		/** The bits 21. */
		BITS_21,

		/** The bits 22. */
		BITS_22,

		/** The bits 23. */
		BITS_23,

		/** The bits 24. */
		BITS_24,

		/** The bits 25. */
		BITS_25,

		/** The bits 26. */
		BITS_26,

		/** The bits 27. */
		BITS_27,

		/** The bits 28. */
		BITS_28,

		/** The bits 29. */
		BITS_29,

		/** The bits 30. */
		BITS_30,

		/** The bits 31. */
		BITS_31,

		/** The bits 32. */
		BITS_32,

		;

		/** The Constant JAVA_INT. */
		public final static ValueLayout.OfInt JAVA_INT = new ValueLayout.OfInt(ByteOrder.LITTLE_ENDIAN, 32)
				.withBitAlignment(DEFAULT_ALIGNMENT);

		/** The layout. */
		private final ValueLayout.OfInt layout;

		/**
		 * Instantiates a new int L 32.
		 */
		Int32le() {
			long size = ordinal() > 32 ? 32 : ordinal();
			this.layout = new ValueLayout.OfInt(ByteOrder.LITTLE_ENDIAN, size)
					.withBitAlignment(DEFAULT_ALIGNMENT);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout.Proxy#proxyLayout()
		 */
		@Override
		public BinaryLayout proxyLayout() {
			return layout;
		}
	}

	/**
	 * The Enum IntN64.
	 */
	public enum Int64 implements BinaryLayout.Proxy {

		/** The bits 00. */
		BITS_00,

		/** The bits 01. */
		BITS_01,

		/** The bits 02. */
		BITS_02,

		/** The bits 03. */
		BITS_03,

		/** The bits 04. */
		BITS_04,

		/** The bits 05. */
		BITS_05,

		/** The bits 06. */
		BITS_06,

		/** The bits 07. */
		BITS_07,

		/** The bits 08. */
		BITS_08,

		/** The bits 09. */
		BITS_09,

		/** The bits 10. */
		BITS_10,

		/** The bits 11. */
		BITS_11,

		/** The bits 12. */
		BITS_12,

		/** The bits 13. */
		BITS_13,

		/** The bits 14. */
		BITS_14,

		/** The bits 15. */
		BITS_15,

		/** The bits 16. */
		BITS_16,

		/** The bits 17. */
		BITS_17,

		/** The bits 18. */
		BITS_18,

		/** The bits 19. */
		BITS_19,

		/** The bits 20. */
		BITS_20,

		/** The bits 21. */
		BITS_21,

		/** The bits 22. */
		BITS_22,

		/** The bits 23. */
		BITS_23,

		/** The bits 24. */
		BITS_24,

		/** The bits 25. */
		BITS_25,

		/** The bits 26. */
		BITS_26,

		/** The bits 27. */
		BITS_27,

		/** The bits 28. */
		BITS_28,

		/** The bits 29. */
		BITS_29,

		/** The bits 30. */
		BITS_30,

		/** The bits 31. */
		BITS_31,

		/** The bits 32. */
		BITS_32,

		/** The bits 33. */
		BITS_33,

		/** The bits 34. */
		BITS_34,

		/** The bits 35. */
		BITS_35,

		/** The bits 36. */
		BITS_36,

		/** The bits 37. */
		BITS_37,

		/** The bits 38. */
		BITS_38,

		/** The bits 39. */
		BITS_39,

		/** The bits 40. */
		BITS_40,

		/** The bits 41. */
		BITS_41,

		/** The bits 42. */
		BITS_42,

		/** The bits 43. */
		BITS_43,

		/** The bits 44. */
		BITS_44,

		/** The bits 45. */
		BITS_45,

		/** The bits 46. */
		BITS_46,

		/** The bits 47. */
		BITS_47,

		/** The bits 48. */
		BITS_48,

		/** The bits 49. */
		BITS_49,

		/** The bits 50. */
		BITS_50,

		/** The bits 51. */
		BITS_51,

		/** The bits 52. */
		BITS_52,

		/** The bits 53. */
		BITS_53,

		/** The bits 54. */
		BITS_54,

		/** The bits 55. */
		BITS_55,

		/** The bits 56. */
		BITS_56,

		/** The bits 57. */
		BITS_57,

		/** The bits 58. */
		BITS_58,

		/** The bits 59. */
		BITS_59,

		/** The bits 60. */
		BITS_60,

		/** The bits 61. */
		BITS_61,

		/** The bits 62. */
		BITS_62,

		/** The bits 63. */
		BITS_63,

		/** The bits 64. */
		BITS_64,

		;

		/** The Constant JAVA_LONG. */
		public final static ValueLayout.OfLong JAVA_LONG = new ValueLayout.OfLong(ByteOrder.nativeOrder(), 64)
				.withBitAlignment(DEFAULT_ALIGNMENT);

		/** The layout. */
		private final OfLong layout;

		/**
		 * Instantiates a new int N 64.
		 */
		Int64() {
			long size = ordinal() > 64 ? 64 : ordinal();
			this.layout = new ValueLayout.OfLong(ByteOrder.nativeOrder(), size)
					.withBitAlignment(DEFAULT_ALIGNMENT);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout.Proxy#proxyLayout()
		 */
		@Override
		public BinaryLayout proxyLayout() {
			return layout;
		}
	}

	/**
	 * The Enum IntB64.
	 */
	public enum Int64be implements BinaryLayout.Proxy {

		/** The bits 00. */
		BITS_00,

		/** The bits 01. */
		BITS_01,

		/** The bits 02. */
		BITS_02,

		/** The bits 03. */
		BITS_03,

		/** The bits 04. */
		BITS_04,

		/** The bits 05. */
		BITS_05,

		/** The bits 06. */
		BITS_06,

		/** The bits 07. */
		BITS_07,

		/** The bits 08. */
		BITS_08,

		/** The bits 09. */
		BITS_09,

		/** The bits 10. */
		BITS_10,

		/** The bits 11. */
		BITS_11,

		/** The bits 12. */
		BITS_12,

		/** The bits 13. */
		BITS_13,

		/** The bits 14. */
		BITS_14,

		/** The bits 15. */
		BITS_15,

		/** The bits 16. */
		BITS_16,

		/** The bits 17. */
		BITS_17,

		/** The bits 18. */
		BITS_18,

		/** The bits 19. */
		BITS_19,

		/** The bits 20. */
		BITS_20,

		/** The bits 21. */
		BITS_21,

		/** The bits 22. */
		BITS_22,

		/** The bits 23. */
		BITS_23,

		/** The bits 24. */
		BITS_24,

		/** The bits 25. */
		BITS_25,

		/** The bits 26. */
		BITS_26,

		/** The bits 27. */
		BITS_27,

		/** The bits 28. */
		BITS_28,

		/** The bits 29. */
		BITS_29,

		/** The bits 30. */
		BITS_30,

		/** The bits 31. */
		BITS_31,

		/** The bits 32. */
		BITS_32,

		/** The bits 33. */
		BITS_33,

		/** The bits 34. */
		BITS_34,

		/** The bits 35. */
		BITS_35,

		/** The bits 36. */
		BITS_36,

		/** The bits 37. */
		BITS_37,

		/** The bits 38. */
		BITS_38,

		/** The bits 39. */
		BITS_39,

		/** The bits 40. */
		BITS_40,

		/** The bits 41. */
		BITS_41,

		/** The bits 42. */
		BITS_42,

		/** The bits 43. */
		BITS_43,

		/** The bits 44. */
		BITS_44,

		/** The bits 45. */
		BITS_45,

		/** The bits 46. */
		BITS_46,

		/** The bits 47. */
		BITS_47,

		/** The bits 48. */
		BITS_48,

		/** The bits 49. */
		BITS_49,

		/** The bits 50. */
		BITS_50,

		/** The bits 51. */
		BITS_51,

		/** The bits 52. */
		BITS_52,

		/** The bits 53. */
		BITS_53,

		/** The bits 54. */
		BITS_54,

		/** The bits 55. */
		BITS_55,

		/** The bits 56. */
		BITS_56,

		/** The bits 57. */
		BITS_57,

		/** The bits 58. */
		BITS_58,

		/** The bits 59. */
		BITS_59,

		/** The bits 60. */
		BITS_60,

		/** The bits 61. */
		BITS_61,

		/** The bits 62. */
		BITS_62,

		/** The bits 63. */
		BITS_63,

		/** The bits 64. */
		BITS_64,

		;

		/** The Constant JAVA_LONG. */
		public final static ValueLayout.OfLong JAVA_LONG = new ValueLayout.OfLong(ByteOrder.BIG_ENDIAN, 64)
				.withBitAlignment(DEFAULT_ALIGNMENT);

		/** The layout. */
		private final OfLong layout;

		/**
		 * Instantiates a new int B 64.
		 */
		Int64be() {
			long size = (ordinal() > 64) ? 64 : ordinal();
			this.layout = new ValueLayout.OfLong(ByteOrder.BIG_ENDIAN, size)
					.withBitAlignment(DEFAULT_ALIGNMENT);
		}

		/**
		 * Proxy layout.
		 *
		 * @return the binary layout
		 * @see com.slytechs.jnet.runtime.binary.layout.Proxy#proxyLayout()
		 */
		@Override
		public BinaryLayout proxyLayout() {
			return layout;
		}
	}

	/**
	 * The Enum IntL64.
	 */
	public enum Int64le implements BinaryLayout.Proxy {

		/** The bits 00. */
		BITS_00,

		/** The bits 01. */
		BITS_01,

		/** The bits 02. */
		BITS_02,

		/** The bits 03. */
		BITS_03,

		/** The bits 04. */
		BITS_04,

		/** The bits 05. */
		BITS_05,

		/** The bits 06. */
		BITS_06,

		/** The bits 07. */
		BITS_07,

		/** The bits 08. */
		BITS_08,

		/** The bits 09. */
		BITS_09,

		/** The bits 10. */
		BITS_10,

		/** The bits 11. */
		BITS_11,

		/** The bits 12. */
		BITS_12,

		/** The bits 13. */
		BITS_13,

		/** The bits 14. */
		BITS_14,

		/** The bits 15. */
		BITS_15,

		/** The bits 16. */
		BITS_16,

		/** The bits 17. */
		BITS_17,

		/** The bits 18. */
		BITS_18,

		/** The bits 19. */
		BITS_19,

		/** The bits 20. */
		BITS_20,

		/** The bits 21. */
		BITS_21,

		/** The bits 22. */
		BITS_22,

		/** The bits 23. */
		BITS_23,

		/** The bits 24. */
		BITS_24,

		/** The bits 25. */
		BITS_25,

		/** The bits 26. */
		BITS_26,

		/** The bits 27. */
		BITS_27,

		/** The bits 28. */
		BITS_28,

		/** The bits 29. */
		BITS_29,

		/** The bits 30. */
		BITS_30,

		/** The bits 31. */
		BITS_31,

		/** The bits 32. */
		BITS_32,

		/** The bits 33. */
		BITS_33,

		/** The bits 34. */
		BITS_34,

		/** The bits 35. */
		BITS_35,

		/** The bits 36. */
		BITS_36,

		/** The bits 37. */
		BITS_37,

		/** The bits 38. */
		BITS_38,

		/** The bits 39. */
		BITS_39,

		/** The bits 40. */
		BITS_40,

		/** The bits 41. */
		BITS_41,

		/** The bits 42. */
		BITS_42,

		/** The bits 43. */
		BITS_43,

		/** The bits 44. */
		BITS_44,

		/** The bits 45. */
		BITS_45,

		/** The bits 46. */
		BITS_46,

		/** The bits 47. */
		BITS_47,

		/** The bits 48. */
		BITS_48,

		/** The bits 49. */
		BITS_49,

		/** The bits 50. */
		BITS_50,

		/** The bits 51. */
		BITS_51,

		/** The bits 52. */
		BITS_52,

		/** The bits 53. */
		BITS_53,

		/** The bits 54. */
		BITS_54,

		/** The bits 55. */
		BITS_55,

		/** The bits 56. */
		BITS_56,

		/** The bits 57. */
		BITS_57,

		/** The bits 58. */
		BITS_58,

		/** The bits 59. */
		BITS_59,

		/** The bits 60. */
		BITS_60,

		/** The bits 61. */
		BITS_61,

		/** The bits 62. */
		BITS_62,

		/** The bits 63. */
		BITS_63,

		/** The bits 64. */
		BITS_64,

		;

		/** The Constant JAVA_LONG. */
		public final static ValueLayout.OfLong JAVA_LONG = new ValueLayout.OfLong(ByteOrder.LITTLE_ENDIAN, 64)
				.withBitAlignment(DEFAULT_ALIGNMENT);

		/** The layout. */
		private final OfLong layout;

		/**
		 * Instantiates a new int L 64.
		 */
		Int64le() {
			long size = ordinal() > 64 ? 64 : ordinal();
			this.layout = new ValueLayout.OfLong(ByteOrder.LITTLE_ENDIAN, size)
					.withBitAlignment(DEFAULT_ALIGNMENT);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout.Proxy#proxyLayout()
		 */
		@Override
		public BinaryLayout proxyLayout() {
			return layout;
		}
	}

	/**
	 * The Enum Int8.
	 */
	public enum Int8 implements BinaryLayout.Proxy {

		/** The bits 00. */
		BITS_00,

		/** The bits 01. */
		BITS_01,

		/** The bits 02. */
		BITS_02,

		/** The bits 03. */
		BITS_03,

		/** The bits 04. */
		BITS_04,

		/** The bits 05. */
		BITS_05,

		/** The bits 06. */
		BITS_06,

		/** The bits 07. */
		BITS_07,

		/** The bits 08. */
		BITS_08,

		;

		/** The Constant JAVA_SHORT. */
		public final static ValueLayout.OfByte JAVA_SHORT = new ValueLayout.OfByte(8);

		/** The layout. */
		private final ValueLayout.OfByte layout;

		/**
		 * Instantiates a new int 8.
		 */
		Int8() {
			long size = (ordinal() > 8) ? 8 : ordinal();
			this.layout = new ValueLayout.OfByte(ByteOrder.nativeOrder(), size, 1, Optional.empty());
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout.Proxy#proxyLayout()
		 */
		@Override
		public BinaryLayout proxyLayout() {
			return layout;
		}
	}

	/**
	 * The Enum Int8.
	 */
	public enum Int8be implements BinaryLayout.Proxy {

		/** The bits 00. */
		BITS_00,

		/** The bits 01. */
		BITS_01,

		/** The bits 02. */
		BITS_02,

		/** The bits 03. */
		BITS_03,

		/** The bits 04. */
		BITS_04,

		/** The bits 05. */
		BITS_05,

		/** The bits 06. */
		BITS_06,

		/** The bits 07. */
		BITS_07,

		/** The bits 08. */
		BITS_08,

		;

		/** The Constant JAVA_SHORT. */
		public final static ValueLayout.OfByte JAVA_SHORT = new ValueLayout.OfByte(8);

		/** The layout. */
		private final ValueLayout.OfByte layout;

		/**
		 * Instantiates a new int 8.
		 */
		Int8be() {
			long size = (ordinal() > 8) ? 8 : ordinal();
			this.layout = new ValueLayout.OfByte(ByteOrder.BIG_ENDIAN, size, 8, Optional.empty());
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout.Proxy#proxyLayout()
		 */
		@Override
		public BinaryLayout proxyLayout() {
			return layout;
		}
	}

	/**
	 * The Enum Int8.
	 */
	public enum Int8le implements BinaryLayout.Proxy {

		/** The bits 00. */
		BITS_00,

		/** The bits 01. */
		BITS_01,

		/** The bits 02. */
		BITS_02,

		/** The bits 03. */
		BITS_03,

		/** The bits 04. */
		BITS_04,

		/** The bits 05. */
		BITS_05,

		/** The bits 06. */
		BITS_06,

		/** The bits 07. */
		BITS_07,

		/** The bits 08. */
		BITS_08,

		;

		/** The Constant JAVA_SHORT. */
		public final static ValueLayout.OfByte JAVA_SHORT = new ValueLayout.OfByte(8);

		/** The layout. */
		private final ValueLayout.OfByte layout;

		/**
		 * Instantiates a new int 8.
		 */
		Int8le() {
			long size = (ordinal() > 8) ? 8 : ordinal();
			this.layout = new ValueLayout.OfByte(ByteOrder.LITTLE_ENDIAN, size, 8, Optional.empty());
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout.Proxy#proxyLayout()
		 */
		@Override
		public BinaryLayout proxyLayout() {
			return layout;
		}
	}

	/**
	 * The Enum Padding.
	 */
	public enum Padding implements BinaryLayout.Proxy {

		/** The bits 00. */
		BITS_00,

		/** The bits 01. */
		BITS_01,

		/** The bits 02. */
		BITS_02,

		/** The bits 03. */
		BITS_03,

		/** The bits 04. */
		BITS_04,

		/** The bits 05. */
		BITS_05,

		/** The bits 06. */
		BITS_06,

		/** The bits 07. */
		BITS_07,

		/** The bits 08. */
		BITS_08,

		/** The bits 09. */
		BITS_09,

		/** The bits 10. */
		BITS_10,

		/** The bits 11. */
		BITS_11,

		/** The bits 12. */
		BITS_12,

		/** The bits 13. */
		BITS_13,

		/** The bits 14. */
		BITS_14,

		/** The bits 15. */
		BITS_15,

		/** The bits 16. */
		BITS_16,

		/** The bits 17. */
		BITS_17,

		/** The bits 18. */
		BITS_18,

		/** The bits 19. */
		BITS_19,

		/** The bits 20. */
		BITS_20,

		/** The bits 21. */
		BITS_21,

		/** The bits 22. */
		BITS_22,

		/** The bits 23. */
		BITS_23,

		/** The bits 24. */
		BITS_24,

		/** The bits 25. */
		BITS_25,

		/** The bits 26. */
		BITS_26,

		/** The bits 27. */
		BITS_27,

		/** The bits 28. */
		BITS_28,

		/** The bits 29. */
		BITS_29,

		/** The bits 30. */
		BITS_30,

		/** The bits 31. */
		BITS_31,

		/** The bits 32. */
		BITS_32,

		/** The bits 33. */
		BITS_33,

		/** The bits 34. */
		BITS_34,

		/** The bits 35. */
		BITS_35,

		/** The bits 36. */
		BITS_36,

		/** The bits 37. */
		BITS_37,

		/** The bits 38. */
		BITS_38,

		/** The bits 39. */
		BITS_39,

		/** The bits 40. */
		BITS_40,

		/** The bits 41. */
		BITS_41,

		/** The bits 42. */
		BITS_42,

		/** The bits 43. */
		BITS_43,

		/** The bits 44. */
		BITS_44,

		/** The bits 45. */
		BITS_45,

		/** The bits 46. */
		BITS_46,

		/** The bits 47. */
		BITS_47,

		/** The bits 48. */
		BITS_48,

		/** The bits 49. */
		BITS_49,

		/** The bits 50. */
		BITS_50,

		/** The bits 51. */
		BITS_51,

		/** The bits 52. */
		BITS_52,

		/** The bits 53. */
		BITS_53,

		/** The bits 54. */
		BITS_54,

		/** The bits 55. */
		BITS_55,

		/** The bits 56. */
		BITS_56,

		/** The bits 57. */
		BITS_57,

		/** The bits 58. */
		BITS_58,

		/** The bits 59. */
		BITS_59,

		/** The bits 60. */
		BITS_60,

		/** The bits 61. */
		BITS_61,

		/** The bits 62. */
		BITS_62,

		/** The bits 63. */
		BITS_63,

		/** The bits 64. */
		BITS_64,

		;

		/** The layout. */
		private final PaddingLayout layout;

		/**
		 * Instantiates a new padding.
		 */
		Padding() {
			long size = ordinal() > 64 ? 64 : ordinal();
			this.layout = new PaddingLayout(size).withBitAlignment(1);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.BinaryLayout.Proxy#proxyLayout()
		 */
		@Override
		public BinaryLayout proxyLayout() {
			return layout;
		}
	}

	/** The Constant JAVA_BYTE. */
	public final static OfByte JAVA_BYTE = new OfByte();

	/** The Constant JAVA_CHAR. */
	public final static OfChar JAVA_CHAR = new OfChar(ByteOrder.nativeOrder());

	/** The Constant JAVA_SHORT. */
	public final static OfShort JAVA_SHORT = new OfShort(ByteOrder.nativeOrder());

	/** The Constant JAVA_INT. */
	public final static OfInt JAVA_INT = new OfInt(ByteOrder.nativeOrder());

	/** The Constant JAVA_LONG. */
	public final static OfLong JAVA_LONG = new OfLong(ByteOrder.nativeOrder());

	/** The Constant JAVA_FLOAT. */
	public final static OfFloat JAVA_FLOAT = new OfFloat(ByteOrder.nativeOrder());

	/** The Constant JAVA_DOUBLE. */
	public final static OfDouble JAVA_DOUBLE = new OfDouble(ByteOrder.nativeOrder());

	/** The Constant ADDRESS. */
	public final static OfAddress ADDRESS = new OfAddress(ByteOrder.nativeOrder());

	/**
	 * Instantiates a new predefined layout.
	 */
	private PredefinedLayout() {
		// One of these
	}

}
