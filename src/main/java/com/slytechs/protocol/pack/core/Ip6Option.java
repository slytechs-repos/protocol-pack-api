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
package com.slytechs.protocol.pack.core;

import com.slytechs.protocol.pack.core.Ip.IpOption;
import com.slytechs.protocol.pack.core.constants.Ip6OptionInfo;

/**
 * The Class Ip6Option.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public class Ip6Option extends IpOption {

	/**
	 * The Class Ip6AuthenticationOption.
	 */
	public static class Ip6AuthenticationOption extends Ip6Option {

		/** The id. */
		public static int ID = Ip6OptionInfo.IPv6_OPTION_ID_AUTHENTICATION;

		/**
		 * Instantiates a new ip 6 authentication option.
		 */
		public Ip6AuthenticationOption() {
			super(ID);
		}
	}

	/**
	 * The Class Ip6DestinationOption.
	 */
	public static class Ip6DestinationOption extends Ip6Option {

		/** The id. */
		public static int ID = Ip6OptionInfo.IPv6_OPTION_ID_DESTINATION;

		/**
		 * Instantiates a new ip 6 destination option.
		 */
		public Ip6DestinationOption() {
			super(ID);
		}
	}

	/**
	 * The Class Ip6FragmentOption.
	 */
	public static class Ip6FragmentOption extends Ip6Option {

		/** The id. */
		public static int ID = Ip6OptionInfo.IPv6_OPTION_ID_FRAGMENT;

		/**
		 * Instantiates a new ip 6 fragment option.
		 */
		public Ip6FragmentOption() {
			super(ID, Ip6OptionInfo.IPv6_OPTION_TYPE_FRAGMENT_LEN);
		}

		/**
		 * Fragment offset.
		 *
		 * @return the int
		 */
		public int fragmentOffset() {
			return Short.toUnsignedInt(buffer().getShort(2)) & 0x1FF;
		}

		/**
		 * Fragment byte offset.
		 *
		 * @return the int
		 */
		public int fragmentByteOffset() {
			return fragmentOffset() << 3;
		}
	}

	/**
	 * The Class Ip6HopByHopOption.
	 */
	public static class Ip6HopByHopOption extends Ip6Option {

		/** The id. */
		public static int ID = Ip6OptionInfo.IPv6_OPTION_ID_HOP_BY_HOP;

		/**
		 * Instantiates a new ip 6 hop by hop option.
		 */
		public Ip6HopByHopOption() {
			super(ID);
		}
	}

	/**
	 * The Class Ip6IdentityOption.
	 */
	public static class Ip6IdentityOption extends Ip6Option {

		/** The id. */
		public static int ID = Ip6OptionInfo.IPv6_OPTION_ID_IDENTITY;

		/**
		 * Instantiates a new ip 6 identity option.
		 */
		public Ip6IdentityOption() {
			super(ID);
		}
	}

	/**
	 * The Class Ip6MobilityOption.
	 */
	public static class Ip6MobilityOption extends Ip6Option {

		/** The id. */
		public static int ID = Ip6OptionInfo.IPv6_OPTION_ID_MOBILITY;

		/**
		 * Instantiates a new ip 6 mobility option.
		 */
		public Ip6MobilityOption() {
			super(ID);
		}
	}

	/**
	 * The Class Ip6RoutingOption.
	 */
	public static class Ip6RoutingOption extends Ip6Option {

		/**
		 * The Enum RoutingType.
		 */
		public enum RoutingType {

			/** The type0. */
			TYPE0(0),

			/** The type1. */
			TYPE1(1),

			/** The type2. */
			TYPE2(2),

			/** The type3. */
			TYPE3(3),

			/** The type4. */
			TYPE4(4),

			/** The type253. */
			TYPE253(253),

			/** The type254. */
			TYPE254(254),

			;

			/** The type. */
			private final int type;

			/**
			 * Instantiates a new routing type.
			 *
			 * @param type the type
			 */
			RoutingType(int type) {
				this.type = type;
			}

			/**
			 * Value of.
			 *
			 * @param type the type
			 * @return the routing type
			 */
			public static RoutingType valueOf(int type) {
				for (RoutingType t : values()) {
					if (t.type == type)
						return t;
				}

				return null;
			}

			/**
			 * Type.
			 *
			 * @return the int
			 */
			public int type() {
				return type;
			}
		}

		/** The id. */
		public static int ID = Ip6OptionInfo.IPv6_OPTION_ID_ROUTING;

		/**
		 * Instantiates a new ip 6 routing option.
		 */
		public Ip6RoutingOption() {
			super(ID);
		}

		/**
		 * Type.
		 *
		 * @return the int
		 */
		public int type() {
			return Byte.toUnsignedInt(buffer().get(2));
		}

		/**
		 * Segments left.
		 *
		 * @return the int
		 */
		public int segmentsLeft() {
			return Byte.toUnsignedInt(buffer().get(3));
		}
	}

	/**
	 * The Class Ip6SecurityOption.
	 */
	public static class Ip6SecurityOption extends Ip6Option {

		/** The id. */
		public static int ID = Ip6OptionInfo.IPv6_OPTION_ID_SECURITY;

		/**
		 * Instantiates a new ip 6 security option.
		 */
		public Ip6SecurityOption() {
			super(ID);
		}
	}

	/**
	 * The Class Ip6Shim6Option.
	 */
	public static class Ip6Shim6Option extends Ip6Option {

		/** The id. */
		public static int ID = Ip6OptionInfo.IPv6_OPTION_ID_SHIMv6;

		/**
		 * Instantiates a new ip 6 shim 6 option.
		 */
		public Ip6Shim6Option() {
			super(ID);
		}
	}

	/** The constant length. */
	private final int constantLength;

	/**
	 * Instantiates a new ip 6 option.
	 *
	 * @param id the id
	 */
	protected Ip6Option(int id) {
		super(id);
		this.constantLength = -1;
	}

	/**
	 * Instantiates a new ip 6 option.
	 *
	 * @param id             the id
	 * @param constantLength the constant length
	 */
	protected Ip6Option(int id, int constantLength) {
		super(id);
		this.constantLength = constantLength;
	}

	/**
	 * Next header.
	 *
	 * @return the int
	 */
	public int nextHeader() {
		return Byte.toUnsignedInt(buffer().get(0));
	}

	/**
	 * Extension length.
	 *
	 * @return the int
	 */
	public int extensionLength() {
		return Byte.toUnsignedInt(buffer().get(1));
	}

	/**
	 * Length.
	 *
	 * @return the int
	 */
	public int length() {
		return (constantLength != -1) ? constantLength : (extensionLength() << 3);
	}
}
