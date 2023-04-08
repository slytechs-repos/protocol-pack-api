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
package com.slytechs.jnet.protocol.meta;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.slytechs.jnet.protocol.core.constants.EtherType;
import com.slytechs.jnet.protocol.core.constants.IeeeOuiAssignments;
import com.slytechs.jnet.protocol.core.constants.IpType;
import com.slytechs.jnet.protocol.meta.MetaValue.ValueResolver;
import com.slytechs.jnet.runtime.time.Timestamp;

/**
 * Resolves field values to human readable representations. For example IP
 * addresses are can be resolved to host names, etc..
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
@Documented
@Repeatable(Resolvers.class)
@Retention(RUNTIME)
@Target({
		METHOD,
		FIELD })
public @interface Resolver {

	/**
	 * The Enum ResolverType.
	 */
	public enum ResolverType {
		
		/** The none. */
		NONE(null),
		
		/** The timestamp. */
		TIMESTAMP(Timestamp::formatTimestamp),
		
		/** The timestamp unit. */
		TIMESTAMP_UNIT(Timestamp::formatTimestampUnit),
		
		/** The timestamp in pcap micro. */
		TIMESTAMP_IN_PCAP_MICRO(Timestamp::formatTimestampPcapMicro),
		
		/** The timestamp in epoch milli. */
		TIMESTAMP_IN_EPOCH_MILLI(Timestamp::formatTimestampInEpochMilli),
		
		/** The ether type. */
		ETHER_TYPE(EtherType::resolve),
		
		/** The ip type. */
		IP_TYPE(IpType::resolve),
		
		/** The ether mac oui name. */
		ETHER_MAC_OUI_NAME(IeeeOuiAssignments::resolveMacOuiName),
		
		/** The ether mac oui name prefixed. */
		ETHER_MAC_OUI_NAME_PREFIXED(IeeeOuiAssignments::formatPrefixMacWithOuiName),
		
		/** The ether mac oui description. */
		ETHER_MAC_OUI_DESCRIPTION(IeeeOuiAssignments::resolveMacOuiDescription),

		/** The bitshift 1. */
		BITSHIFT_1(v -> DisplayUtil.bitshiftIntLeft(v, 1)),
		
		/** The bitshift 2. */
		BITSHIFT_2(v -> DisplayUtil.bitshiftIntLeft(v, 2)),
		
		/** The bitshift 3. */
		BITSHIFT_3(v -> DisplayUtil.bitshiftIntLeft(v, 3)),
		
		/** The bitshift 4. */
		BITSHIFT_4(v -> DisplayUtil.bitshiftIntLeft(v, 4)),
		
		/** The bitshift 5. */
		BITSHIFT_5(v -> DisplayUtil.bitshiftIntLeft(v, 5)),
		
		/** The bitshift 6. */
		BITSHIFT_6(v -> DisplayUtil.bitshiftIntLeft(v, 6)),
		
		/** The bitshift 7. */
		BITSHIFT_7(v -> DisplayUtil.bitshiftIntLeft(v, 7)),
		
		/** The bitshift 8. */
		BITSHIFT_8(v -> DisplayUtil.bitshiftIntLeft(v, 8)),
		;

		/** The resolver. */
		private final ValueResolver resolver;

		/**
		 * Instantiates a new resolver type.
		 *
		 * @param resolver the resolver
		 */
		ResolverType(ValueResolver resolver) {
			this.resolver = resolver;
		}

		/**
		 * Gets the resolver.
		 *
		 * @return the resolver
		 */
		public ValueResolver getResolver() {
			return this.resolver;
		}

	}

	/**
	 * Value.
	 *
	 * @return the resolver type
	 */
	ResolverType value() default ResolverType.NONE;

	/**
	 * Resolver class.
	 *
	 * @return the class<? extends value resolver>
	 */
	Class<? extends ValueResolver> resolverClass() default ValueResolver.class;

	/**
	 * If resolver does not exist or is unable to resolve the value, default to
	 * formatted value output {@link MetaField#getFormatted}.
	 *
	 * @return true, if successful
	 */
	boolean defaultToFormatted() default true;

	/**
	 * Default value.
	 *
	 * @return the string
	 */
	String defaultValue() default "";
}
