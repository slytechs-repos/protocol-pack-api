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
package com.slytechs.jnet.protocol.packet.meta;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.slytechs.jnet.protocol.constants.EtherType;
import com.slytechs.jnet.protocol.constants.IeeeOuiAssignments;
import com.slytechs.jnet.protocol.packet.meta.MetaValue.ValueResolver;
import com.slytechs.jnet.runtime.time.Timestamp;

@Documented
@Repeatable(MetaValueResolverRepeatable.class)
@Retention(RUNTIME)
@Target({
		METHOD,
		FIELD })
/**
 * Resolves field values to human readable representations. For example IP
 * addresses are can be resolved to host names, etc..
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public @interface MetaValueResolver {

	public enum Type {
		NONE(null),
		TIMESTAMP_IN_PCAP_MICRO(Timestamp::formatTimestampPcapMicro),
		TIMESTAMP_IN_EPOCH_MILLI(Timestamp::formatTimestampInEpochMilli),
		ETHER_TYPE(EtherType::resolve),
		ETHER_MAC_OUI_NAME(IeeeOuiAssignments::resolveMacOuiName),
		ETHER_MAC_OUI_NAME_PREFIXED(IeeeOuiAssignments::formatPrefixMacWithOuiName),
		ETHER_MAC_OUI_DESCRIPTION(IeeeOuiAssignments::resolveMacOuiDescription),
		;

		private final ValueResolver resolver;

		/**
		 * @param object
		 */
		Type(ValueResolver resolver) {
			this.resolver = resolver;
		}

		public ValueResolver getResolver() {
			return this.resolver;
		}

	}

	Type value() default Type.NONE;

	Class<? extends ValueResolver> resolverClass() default ValueResolver.class;

	/**
	 * If resolver does not exist or is unable to resolve the value, default to
	 * formatted value output {@link MetaField#getFormatted}.
	 *
	 * @return true, if successful
	 */
	boolean defaultToFormatted() default true;

	String defaultValue() default "";
}
