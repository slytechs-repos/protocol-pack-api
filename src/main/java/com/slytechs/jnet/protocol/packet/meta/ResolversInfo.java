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
import com.slytechs.jnet.runtime.util.json.JsonArray;
import com.slytechs.jnet.runtime.util.json.JsonArrayBuilder;
import com.slytechs.jnet.runtime.util.json.JsonObject;
import com.slytechs.jnet.runtime.util.json.JsonValue;
import com.slytechs.jnet.runtime.util.json.JsonValue.ValueType;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public record ResolversInfo(ValueResolver... resolvers) implements MetaInfoType {

	static ResolversInfo parse(AnnotatedElement element, String name, JsonObject jsonDefaults) {
		JsonValue jsonValue = null;
		if (jsonDefaults != null)
			jsonValue = jsonDefaults.get("resolvers");

		if (jsonValue != null)
			return parseJson(jsonValue);

		return parseAnnotations(element);
	}

	private static ResolversInfo parseAnnotations(AnnotatedElement element) {
		Resolvers multiple = element.getAnnotation(Resolvers.class);
		Resolver single = element.getAnnotation(Resolver.class);

		if (multiple != null) {
			Resolver[] resolversAnnotation = multiple.value();
			ValueResolver[] resolvers = new ValueResolver[resolversAnnotation.length];

			for (int i = 0; i < resolversAnnotation.length; i++)
				resolvers[i] = resolversAnnotation[i].value().getResolver();

			return new ResolversInfo(resolvers);

		} else if (single != null)
			return new ResolversInfo(single.value().getResolver());

		return new ResolversInfo();
	}

	private static ResolversInfo parseJson(JsonValue jsonValue) {

		JsonArray jsonArray;
		if (jsonValue.getValueType() == ValueType.ARRAY) {
			jsonArray = (JsonArray) jsonValue;
		} else {
			jsonArray = new JsonArrayBuilder()
					.add(jsonValue)
					.build();
		}
		
		ValueResolver[] resolvers = new ValueResolver[jsonArray.size()];

		for (int i = 0; i < jsonArray.size(); i++) {
			resolvers[i] = Resolver.ResolverType
					.valueOf(jsonArray.getString(i))
					.getResolver();
		}

		return new ResolversInfo(resolvers);

	}
}
