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

import java.lang.reflect.AnnotatedElement;

import com.slytechs.jnet.protocol.packet.meta.MetaValue.ValueResolver;
import com.slytechs.jnet.runtime.internal.json.JsonArray;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public class ResolverInfo implements MetaInfoType {

	static ResolverInfo parse(AnnotatedElement element, String name, JsonArray jsonArray) {
		if (jsonArray != null)
			return parseJson(jsonArray);

		return parseAnnotations(element);
	}

	private static ResolverInfo parseAnnotations(AnnotatedElement element) {
		Resolvers multiple = element.getAnnotation(Resolvers.class);
		Resolver single = element.getAnnotation(Resolver.class);

		if (multiple != null) {
			Resolver[] resolversAnnotation = multiple.value();
			ValueResolver[] resolvers = new ValueResolver[resolversAnnotation.length];

			for (int i = 0; i < resolversAnnotation.length; i++)
				resolvers[i] = resolversAnnotation[i].value().getResolver();

			return new ResolverInfo(resolvers);

		} else if (single != null)
			return new ResolverInfo(single.value().getResolver());

		return new ResolverInfo();
	}

	private static ResolverInfo parseJson(JsonArray jsonArray) {
		ValueResolver[] resolvers = new ValueResolver[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {

		}

		return new ResolverInfo(resolvers);
	}

	private final ValueResolver[] resolvers;

	private ResolverInfo(ValueResolver... resolvers) {
		this.resolvers = resolvers;
	}

	public ValueResolver[] getValueResolvers() {
		return resolvers;
	}

}
