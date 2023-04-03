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
package com.slytechs.jnet.protocol.packet.format;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.slytechs.jnet.protocol.packet.Header;
import com.slytechs.jnet.protocol.packet.meta.DisplayUtil;
import com.slytechs.jnet.protocol.packet.meta.MetaValue.ValueResolver;
import com.slytechs.jnet.runtime.util.Detail;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public class FieldFormatter extends Formatter {

	@Display
	private static final Display DEFAULT_DISPLAY_ANNOTATION;

	@Resolver
	private static final Resolver DEFAULT_VALUE_RESOLVER;

	private static final int MAX_FORMATTER_ARGUMENTS = 6;

	static {
		try {

			DEFAULT_DISPLAY_ANNOTATION = FieldFormatter.class
					.getDeclaredField("DEFAULT_DISPLAY_ANNOTATION")
					.getAnnotation(Display.class);

			DEFAULT_VALUE_RESOLVER = FieldFormatter.class
					.getDeclaredField("DEFAULT_VALUE_RESOLVER")
					.getAnnotation(Resolver.class);

		} catch (NoSuchFieldException | SecurityException e) {
			throw new IllegalStateException(e);
		}
	}

	private final String formatString;

	public FieldFormatter(MetaContext context) {
		this("%s = %s");
	}

	public FieldFormatter(String formatString) {
		this.formatString = formatString;
	}

	protected String formatField(Header header, String field, Detail detail) {
		return formatField(new StringBuilder(), header, field, detail).toString();
	}

	protected Appendable formatField(Appendable out, Header header, String field, Detail detail) {

		return out;
	}

	public Appendable formatField(Appendable out, MetaField field) throws IOException {
		return formatField(out, field, Detail.DEFAULT);
	}

	public Appendable formatField(Appendable out, MetaField field, Detail detail) throws IOException {
		out.append(formatField(field, detail));

		return out;
	}

	public String formatField(MetaField field) {
		return formatField(field, Detail.DEFAULT);
	}

	public String formatField(MetaField field, Detail detail) {
		Displays multiple = field.getAnnotation(Displays.class);
		Display single = field.getAnnotation(Display.class);

		Display display = DisplayUtil.selectDisplay(multiple, single, detail)
				.orElse(getDefault(Display.class));

		String label = display.label().isBlank()
				? field.label(detail)
				: display.label();

		Object value = field.get();
		String valueFormatted = field.getFormatted();

		/* Args passed to formatter on the value side */
		Object[] valueArgs = new Object[MAX_FORMATTER_ARGUMENTS];
		valueArgs[0] = value;
		valueArgs[1] = valueFormatted;
		runValueResolvers(field, valueArgs, 2);

		String valueFormatString = display.value().isBlank()
				? "%2$s"
				: DisplayUtil.rewriteDisplayFormat(display.value());

		String valueComponent = valueFormatString.formatted(valueArgs);
		String labelComponent = label;

		return String.format(formatString, labelComponent, valueComponent);
	}

	private int runValueResolvers(MetaField field, Object[] valueArgs, int offset) {
		ValueResolver[] resolvers = field.getResolvers();
		Object value = field.get();
		String valueFormatted = field.getFormatted();

		for (int i = 0; i < resolvers.length; i++) {
			String resolvedValue = resolvers[i].resolveValue(value);
			if (resolvedValue == null)
				resolvedValue = resolvers[i].isDefaultToFormatted()
						? valueFormatted
						: "";

			valueArgs[i + offset] = resolvedValue;
		}

		return resolvers.length;
	}

	@SuppressWarnings("unchecked")
	private <A extends Annotation> A getDefault(Class<A> a) {
		if (a == Display.class)
			return (A) DEFAULT_DISPLAY_ANNOTATION;

		return null;
	}
}
