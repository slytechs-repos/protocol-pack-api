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
package com.slytechs.jnet.protocol.core;

import com.slytechs.jnet.protocol.constants.Ip6OptionInfo;
import com.slytechs.jnet.protocol.core.Ip.IpOption;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public class Ip6Option extends IpOption {

	public static class Ip6AuthenticationOption extends Ip6Option {
		public static int ID = Ip6OptionInfo.IPv6_OPTION_ID_AUTHENTICATION;

		public Ip6AuthenticationOption() {
			super(ID);
		}
	}

	public static class Ip6DestinationOption extends Ip6Option {
		public static int ID = Ip6OptionInfo.IPv6_OPTION_ID_DESTINATION;

		public Ip6DestinationOption() {
			super(ID);
		}
	}

	public static class Ip6FragmentOption extends Ip6Option {
		public static int ID = Ip6OptionInfo.IPv6_OPTION_ID_FRAGMENT;

		public Ip6FragmentOption() {
			super(ID, Ip6OptionInfo.IPv6_OPTION_TYPE_FRAGMENT_LEN);
		}

		public int fragmentOffset() {
			return Short.toUnsignedInt(buffer().getShort(2)) & 0x1FF;
		}

		public int fragmentByteOffset() {
			return fragmentOffset() << 3;
		}
	}

	public static class Ip6HopByHopOption extends Ip6Option {
		public static int ID = Ip6OptionInfo.IPv6_OPTION_ID_HOP_BY_HOP;

		public Ip6HopByHopOption() {
			super(ID);
		}
	}

	public static class Ip6IdentityOption extends Ip6Option {
		public static int ID = Ip6OptionInfo.IPv6_OPTION_ID_IDENTITY;

		public Ip6IdentityOption() {
			super(ID);
		}
	}

	public static class Ip6MobilityOption extends Ip6Option {
		public static int ID = Ip6OptionInfo.IPv6_OPTION_ID_MOBILITY;

		public Ip6MobilityOption() {
			super(ID);
		}
	}

	public static class Ip6RoutingOption extends Ip6Option {

		public enum RoutingType {
			TYPE0(0),
			TYPE1(1),
			TYPE2(2),
			TYPE3(3),
			TYPE4(4),
			TYPE253(253),
			TYPE254(254),

			;

			private final int type;

			RoutingType(int type) {
				this.type = type;
			}

			public static RoutingType valueOf(int type) {
				for (RoutingType t : values()) {
					if (t.type == type)
						return t;
				}

				return null;
			}

			public int type() {
				return type;
			}
		}

		public static int ID = Ip6OptionInfo.IPv6_OPTION_ID_ROUTING;

		public Ip6RoutingOption() {
			super(ID);
		}

		public int type() {
			return Byte.toUnsignedInt(buffer().get(2));
		}

		public int segmentsLeft() {
			return Byte.toUnsignedInt(buffer().get(3));
		}
	}

	public static class Ip6SecurityOption extends Ip6Option {
		public static int ID = Ip6OptionInfo.IPv6_OPTION_ID_SECURITY;

		public Ip6SecurityOption() {
			super(ID);
		}
	}

	public static class Ip6Shim6Option extends Ip6Option {
		public static int ID = Ip6OptionInfo.IPv6_OPTION_ID_SHIMv6;

		public Ip6Shim6Option() {
			super(ID);
		}
	}

	private final int constantLength;

	protected Ip6Option(int id) {
		super(id);
		this.constantLength = -1;
	}

	protected Ip6Option(int id, int constantLength) {
		super(id);
		this.constantLength = constantLength;
	}

	public int nextHeader() {
		return Byte.toUnsignedInt(buffer().get(0));
	}

	public int extensionLength() {
		return Byte.toUnsignedInt(buffer().get(1));
	}

	@Override
	public int length() {
		return (constantLength != -1) ? constantLength : (extensionLength() << 3);
	}
}
