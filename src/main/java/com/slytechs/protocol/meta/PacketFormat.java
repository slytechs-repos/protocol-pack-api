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
package com.slytechs.protocol.meta;

import java.util.Optional;

import com.slytechs.protocol.Header;
import com.slytechs.protocol.Packet;
import com.slytechs.protocol.runtime.util.Detail;
import com.slytechs.protocol.runtime.util.HexStrings;

/**
 * The Class PacketFormat.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public final class PacketFormat extends MetaFormat {

	/** The Constant HEADER_LINE_FORMAT_ATTRIBUTE. */
	private static final String HEADER_LINE_FORMAT_ATTRIBUTE = "headerLineFormat";
	
	/** The Constant DEFAULT_LINE_FORMAT. */
	private static final String DEFAULT_LINE_FORMAT = "%15s = %-30s";

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -729988509291767979L;

	/** The field line. */
	private final String fieldLine;

	/**
	 * Instantiates a new packet format.
	 */
	public PacketFormat() {
		this(Detail.DEFAULT);
	}

	/**
	 * Instantiates a new packet format.
	 *
	 * @param detail the detail
	 */
	public PacketFormat(Detail detail) {
		this(new MapMetaContext("packetFormat", 1), detail);
	}

	/**
	 * Instantiates a new packet format.
	 *
	 * @param domain the domain
	 * @param detail the detail
	 */
	public PacketFormat(MetaDomain domain, Detail detail) {
		super(domain, detail);

		this.fieldLine = DEFAULT_LINE_FORMAT;

//		this.fieldLine = domain.searchFor(null, HEADER_LINE_FORMAT_ATTRIBUTE, String.class)
//				.orElse(DEFAULT_LINE_FORMAT);
	}

	/**
	 * Convert to meta if possible.
	 *
	 * @param obj the obj
	 * @return the object
	 * @see com.slytechs.protocol.meta.MetaFormat#convertToMetaIfPossible(java.lang.Object)
	 */
	@Override
	protected Object convertToMetaIfPossible(Object obj) {
		if (obj instanceof Header header)
			return new MetaHeader(getContext(), header);

		if (obj instanceof Packet packet)
			return new MetaPacket(getContext(), packet);

		return obj;
	}

	/**
	 * Format field.
	 *
	 * @param field      the field
	 * @param toAppendTo the to append to
	 * @param detail     the detail
	 * @return the string builder
	 */
	public StringBuilder formatField(MetaField field, StringBuilder toAppendTo, Detail detail) {
		var display = field.getMeta(DisplaysInfo.class).select(detail);
		if (display == null) // Not visible
			return toAppendTo;

		var meta = field.getMeta(MetaInfo.class);
		var label = display.label(meta);
		var displayFormat = display.value();

		try {
			if (label.equals("HEXDUMP"))
				return formatHexdump(field, toAppendTo);

			var valueArgs = buildDisplayArgs(field.getParentHeader(), field, displayFormat);
			displayFormat = super.rewriteDisplayArgs(displayFormat);
			
//		System.out.printf("formatField::%s displayFormat=%s, args=%s%n", 
//				field.name(), 
////				display.value(),
//				displayFormat,
//				Arrays.asList(valueArgs));

			var labelComponent = label;
			var valueComponent = displayFormat.formatted(valueArgs);
			var line = fieldLine.formatted(labelComponent, valueComponent);

			toAppendTo.append(line);

			return toAppendTo;
		} catch (Throwable e) {
			throw e;
//			throw new IllegalStateException("Field '%s.%s': %s"
//					.formatted(field.getParentHeader().name(), field.name(), e
//							.getMessage()));
		}
	}

	/**
	 * Format header.
	 *
	 * @param header     the header
	 * @param toAppendTo the to append to
	 * @param detail     the detail
	 * @return the string builder
	 */
	public StringBuilder formatHeader(MetaHeader header, StringBuilder toAppendTo, Detail detail) {
		var display = header.getMeta(DisplaysInfo.class).select(detail);
		if (display == null) // Not visible
			return toAppendTo;

		var meta = header.getMeta(MetaInfo.class);
		var fields = header.listFields();
		var label = display.label(meta);

		if (!display.value().isBlank()) {
			formatLeft(label, toAppendTo)
					.append("  ");

			formatSummary(header, toAppendTo, detail);
		}

		formatLeft(label, toAppendTo).append("\n");

		for (MetaField field : fields) {
			formatLeft(label, toAppendTo);

			formatField(field, toAppendTo, detail);

			toAppendTo
					.append("\n");
		}
		formatLeft(label, toAppendTo).append("\n");

		return toAppendTo;
	}

	/**
	 * Format hexdump.
	 *
	 * @param array       the array
	 * @param labelOffset the label offset
	 * @param toAppendTo  the to append to
	 * @return the string builder
	 */
	private StringBuilder formatHexdump(byte[] array, int labelOffset, StringBuilder toAppendTo) {
		return HexStrings.toHexTextDump(
				toAppendTo,
				array,
				HexStrings.DEFAULT_HEXDUMP_PREFIX);
	}

	/**
	 * Format hexdump.
	 *
	 * @param field the field
	 * @return the string
	 */
	public String formatHexdump(MetaField field) {
		return formatHexdump(field, new StringBuilder()).toString();
	}

	/**
	 * Format hexdump.
	 *
	 * @param field      the field
	 * @param toAppendTo the to append to
	 * @return the string builder
	 */
	public StringBuilder formatHexdump(MetaField field, StringBuilder toAppendTo) {
		var offsetField = field.searchForField(new MetaPath("offset"))
				.orElse(null);
		int offset = offsetField.get();
		byte[] array = field.get();

		return formatHexdump(array, offset, toAppendTo);
	}

	/**
	 * Format hexdump.
	 *
	 * @param header the header
	 * @return the string
	 */
	public String formatHexdump(Header header) {
		return formatHexdump(new MetaHeader(this, header));

	}

	/**
	 * Format hexdump.
	 *
	 * @param header the header
	 * @return the string
	 */
	public String formatHexdump(MetaHeader header) {
		return formatHexdump(header, new StringBuilder()).toString();
	}

	/**
	 * Format hexdump.
	 *
	 * @param header     the header
	 * @param toAppendTo the to append to
	 * @return the string builder
	 */
	public StringBuilder formatHexdump(MetaHeader header, StringBuilder toAppendTo) {
		int offset = header.offset();
		byte[] array = new byte[header.length()];
		header.buffer().get(0, array);

		return formatHexdump(array, offset, toAppendTo);
	}

	/**
	 * Format hexdump.
	 *
	 * @param packet the packet
	 * @return the string
	 */
	public String formatHexdump(Packet packet) {
		return formatHexdump(new MetaPacket(this, packet));
	}

	/**
	 * Format hexdump.
	 *
	 * @param packet the packet
	 * @return the string
	 */
	public String formatHexdump(MetaPacket packet) {
		return formatHexdump(packet, new StringBuilder()).toString();
	}

	/**
	 * Format hexdump.
	 *
	 * @param packet     the packet
	 * @param toAppendTo the to append to
	 * @return the string builder
	 */
	public StringBuilder formatHexdump(MetaPacket packet, StringBuilder toAppendTo) {
		int offset = 0;
		byte[] array = new byte[packet.captureLength()];
		packet.buffer().get(0, array);

		return formatHexdump(array, offset, toAppendTo);
	}

	/**
	 * Format left.
	 *
	 * @param label      the label
	 * @param toAppendTo the to append to
	 * @return the string builder
	 */
	private StringBuilder formatLeft(String label, StringBuilder toAppendTo) {
		return toAppendTo
				.append("%-10s".formatted(label + ":"));
	}

	/**
	 * Format meta.
	 *
	 * @param element    the element
	 * @param toAppendTo the to append to
	 * @param detail     the detail
	 * @return the string builder
	 * @see com.slytechs.protocol.meta.MetaFormat#formatMeta(com.slytechs.protocol.meta.MetaElement,
	 *      java.lang.StringBuilder, Detail)
	 */
	@Override
	public StringBuilder formatMeta(MetaElement element, StringBuilder toAppendTo, Detail detail) {

		if (element instanceof MetaPacket packet)
			return formatPacket(packet, toAppendTo, detail);

		if (element instanceof MetaHeader header)
			return formatHeader(header, toAppendTo, detail);

		if (element instanceof MetaField field)
			return formatField(field, toAppendTo, detail);

		throw new IllegalArgumentException("Unsupported Meta object [%s]"
				.formatted(element.getClass().getSimpleName()));
	}

	/**
	 * Format packet.
	 *
	 * @param packet     the packet
	 * @param toAppendTo the to append to
	 * @param detail     the detail
	 * @return the string builder
	 */
	public StringBuilder formatPacket(MetaPacket packet, StringBuilder toAppendTo, Detail detail) {
		var display = packet.getMeta(DisplaysInfo.class).select(detail);
		if (display == null) // Not visible
			return toAppendTo;

		String label = display.label(packet.getMeta(MetaInfo.class));

		formatLeft(label, toAppendTo)
				.append("  ");
		formatSummary(packet, toAppendTo, detail);

		var headers = packet.listHeaders();

		for (MetaHeader header : headers)
			formatHeader(header, toAppendTo, detail);

		return toAppendTo;
	}

	/**
	 * Format summary.
	 *
	 * @param element    the element
	 * @param toAppendTo the to append to
	 * @param detail     the detail
	 * @return the string builder
	 */
	private StringBuilder formatSummary(MetaElement element, StringBuilder toAppendTo, Detail detail) {
		var display = element.getMeta(DisplaysInfo.class).select(detail);
		if (display == null)
			return toAppendTo;

		var displayFormat = display.value();
		if (displayFormat.isBlank())
			return toAppendTo;

		var args = super.buildDisplayArgs(element, element, displayFormat);
		displayFormat = super.rewriteDisplayArgs(displayFormat);

		try {
			var summaryLine = displayFormat.formatted(args);

			toAppendTo
//				.append(" ****** ")
//				.append(" + ")
					.append(summaryLine)
//				.append(" ******")
					.append("\n");

			return toAppendTo;
		} catch (Throwable e) {
			toAppendTo
					.append("ERROR: %s".formatted(e.getMessage()));

//			return toAppendTo;

			throw new IllegalStateException(display.label(), e);
		}
	}

	/**
	 * Name.
	 *
	 * @return the string
	 * @see com.slytechs.protocol.meta.MetaDomain#name()
	 */
	@Override
	public String name() {
		return "";
	}

	/**
	 * Parent.
	 *
	 * @return the meta domain
	 * @see com.slytechs.protocol.meta.MetaDomain#parent()
	 */
	@Override
	public MetaDomain parent() {
		return Global.get();
	}

	/**
	 * Find key.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param key the key
	 * @return the optional
	 * @see com.slytechs.protocol.meta.MetaDomain#findKey(java.lang.Object)
	 */
	@Override
	public <K, V> Optional<V> findKey(K key) {
		return Optional.empty();
	}

	/**
	 * Find domain.
	 *
	 * @param name the name
	 * @return the meta domain
	 * @see com.slytechs.protocol.meta.MetaDomain#findDomain(java.lang.String)
	 */
	@Override
	public MetaDomain findDomain(String name) {
		return null;
	}

}
