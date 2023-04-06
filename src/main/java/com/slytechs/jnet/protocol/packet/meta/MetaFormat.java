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

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.slytechs.jnet.runtime.util.Detail;

/**
 * The Class MetaFormat.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public abstract class MetaFormat extends Format implements MetaDomain {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4307770938567017464L;
	
	/** The context. */
	private final MetaDomain context;
	
	/** The detail. */
	private final Detail detail;

	/**
	 * Instantiates a new meta format.
	 *
	 * @param context the context
	 * @param detail  the detail
	 */
	protected MetaFormat(MetaDomain context, Detail detail) {
		this.context = context;
		this.detail = detail;

	}

	/**
	 * Format.
	 *
	 * @param obj    the obj
	 * @param detail the detail
	 * @return the string
	 */
	public final String format(Object obj, Detail detail) {
		return format(obj, new StringBuilder(), detail).toString();
	}

	/**
	 * Format.
	 *
	 * @param obj        the obj
	 * @param toAppendTo the to append to
	 * @param detail     the detail
	 * @return the string builder
	 */
	public final StringBuilder format(Object obj, StringBuilder toAppendTo, Detail detail) {
		obj = convertToMetaIfPossible(obj);

		if (!(obj instanceof MetaElement element))
			throw new IllegalArgumentException("Only Meta elements supported by MetaFormat");

		StringBuilder b = formatMeta(element, toAppendTo, detail);

		return b;
	}

	/**
	 * Format.
	 *
	 * @param obj        the obj
	 * @param toAppendTo the to append to
	 * @param pos        the pos
	 * @return the string buffer
	 * @see java.text.Format#format(java.lang.Object, java.lang.StringBuffer,
	 *      java.text.FieldPosition)
	 */
	@Override
	public final StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
		obj = convertToMetaIfPossible(obj);

		if (!(obj instanceof MetaElement element))
			throw new IllegalArgumentException("Only Meta elements supported by MetaFormat");

		StringBuilder b = formatMeta(element, new StringBuilder(), getDetail());

		return toAppendTo.append(b);
	}

	/**
	 * Parses the object.
	 *
	 * @param source the source
	 * @param pos    the pos
	 * @return the object
	 * @see java.text.Format#parseObject(java.lang.String, java.text.ParsePosition)
	 */
	@Override
	public Object parseObject(String source, ParsePosition pos) {
		throw new UnsupportedOperationException("Meta object is not parsable [\"%s\"]"
				.formatted(source));
	}

	/**
	 * Convert to meta if possible.
	 *
	 * @param obj the obj
	 * @return the object
	 */
	protected abstract Object convertToMetaIfPossible(Object obj);

	/**
	 * Format meta.
	 *
	 * @param element    the element
	 * @param toAppendTo the to append to
	 * @param detail     the detail
	 * @return the string builder
	 */
	public abstract StringBuilder formatMeta(MetaElement element, StringBuilder toAppendTo, Detail detail);

	/**
	 * Gets the context.
	 *
	 * @return the context
	 */
	public final MetaDomain getContext() {
		return context;
	}

	/**
	 * Gets the detail.
	 *
	 * @return the detail
	 */
	public final Detail getDetail() {
		return detail;
	}

	/** The Constant DISPLAY_ATTRIBUTE_PATTERN. */
	private static final Pattern DISPLAY_ATTRIBUTE_PATTERN = Pattern.compile(""
			+ "%\\{([\\w.]*):?([VFR]?[12345]*)\\}" // F=Formatted, R=Resolved
			+ "");

	/**
	 * Rewrite display args.
	 *
	 * @param summaryFormat the summary format
	 * @return the string
	 */
	protected String rewriteDisplayArgs(String summaryFormat) {
		return DISPLAY_ATTRIBUTE_PATTERN.matcher(summaryFormat).replaceAll("%");
	}

	/**
	 * Builds the display args.
	 *
	 * @param domain        the domain
	 * @param element       the element
	 * @param summaryFormat the summary format
	 * @return the object[]
	 */
	protected Object[] buildDisplayArgs(MetaDomain domain, MetaElement element, String summaryFormat) {
		Matcher matcher = DISPLAY_ATTRIBUTE_PATTERN.matcher(summaryFormat);
		var list = new ArrayList<>();
//		System.out.printf("buildDisplayArgs(%s):: summaryFormat=\"%s\"%n",
//				field.name(),
//				summaryFormat);

		while (matcher.find()) {
			var fieldName = matcher.group(1);
			var valueFormat = matcher.group(2);
//			System.out.printf("fieldName=%-15s valueFormat=%-15s%n", fieldName, valueFormat);

			MetaField selected = null;
			if (element instanceof MetaField field)
				selected = field;

			if (fieldName != null && !fieldName.isBlank())
				selected = new MetaPath(fieldName)
				.searchForField(domain)
						.orElse(null);
			else if (element instanceof MetaField field) {
				fieldName = field.name();
				selected = field;
			}

			if (fieldName == null || fieldName.isBlank())
				continue;

			if (valueFormat != null)
				valueFormat.toUpperCase();

			/* Unresolved fields are just passed through */
			if (selected == null) {
				list.add("{%s}".formatted(fieldName));
				continue;
			}

			Object arg = switch (valueFormat) {
			case "V" -> selected.get();
			case "F" -> selected.getFormatted();
			case "R" -> selected.getResolved(0);
			case "R1" -> selected.getResolved(0);
			case "R2" -> selected.getResolved(1);
			case "R3" -> selected.getResolved(2);
			case "R4" -> selected.getResolved(3);
			case "R5" -> selected.getResolved(4);
			default -> selected.get();
			};

			if (arg == null)
				arg = selected.getFormatted();

			list.add(arg);
		}

		return list.toArray();
	}

}
