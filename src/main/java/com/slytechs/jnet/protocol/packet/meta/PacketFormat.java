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

import java.util.Optional;

import com.slytechs.jnet.protocol.packet.Header;
import com.slytechs.jnet.protocol.packet.Packet;
import com.slytechs.jnet.runtime.util.Detail;
import com.slytechs.jnet.runtime.util.HexStrings;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public final class PacketFormat extends MetaFormat {

	private static final String HEADER_LINE_FORMAT_ATTRIBUTE = "headerLineFormat";
	private static final String DEFAULT_LINE_FORMAT = "%15s = %-30s";

	private static final long serialVersionUID = -729988509291767979L;

	private final String fieldLine;

	public PacketFormat() {
		this(Detail.DEFAULT);
	}

	public PacketFormat(Detail detail) {
		this(new MapMetaContext("packetFormat", 1), detail);
	}

	public PacketFormat(MetaDomain domain, Detail detail) {
		super(domain, detail);

		this.fieldLine = DEFAULT_LINE_FORMAT;

//		this.fieldLine = domain.searchFor(null, HEADER_LINE_FORMAT_ATTRIBUTE, String.class)
//				.orElse(DEFAULT_LINE_FORMAT);
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.meta.MetaFormat#convertToMetaIfPossible(java.lang.Object)
	 */
	@Override
	protected Object convertToMetaIfPossible(Object obj) {
		if (obj instanceof Header header)
			return new MetaHeader(getContext(), header);

		if (obj instanceof Packet packet)
			return new MetaPacket(getContext(), packet);

		return obj;
	}

	public StringBuilder formatField(MetaField field, StringBuilder toAppendTo, Detail detail) {
		var display = field.getMeta(DisplaysInfo.class).select(detail);
		if (display == null) // Not visible
			return toAppendTo;

		var meta = field.getMeta(MetaInfo.class);
		var label = display.label(meta);
		var displayFormat = display.value();

		if (label.equals("HEXDUMP"))
			return formatHexdump(field, toAppendTo);

		var valueArgs = buildDisplayArgs(field.getParentHeader(), field, displayFormat);
		displayFormat = super.rewriteDisplayArgs(displayFormat);
//		System.out.printf("formatField:: displayFormat=%s%n", displayFormat);

		try {
			var labelComponent = label;
			var valueComponent = displayFormat.formatted(valueArgs);
			var line = fieldLine.formatted(labelComponent, valueComponent);

			toAppendTo.append(line);

			return toAppendTo;
		} catch (Throwable e) {
			throw new IllegalStateException("Field '%s.%s': %s"
					.formatted(field.getParentHeader().name(), field.name(), e
							.getMessage()));
		}
	}

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

	private StringBuilder formatHexdump(byte[] array, int labelOffset, StringBuilder toAppendTo) {
		return HexStrings.toHexTextDump(
				toAppendTo,
				array,
				HexStrings.DEFAULT_HEXDUMP_PREFIX);
	}

	public String formatHexdump(MetaField field) {
		return formatHexdump(field, new StringBuilder()).toString();
	}

	public StringBuilder formatHexdump(MetaField field, StringBuilder toAppendTo) {
		var offsetField = field.searchForField(new MetaPath("offset"))
				.orElse(null);
		int offset = offsetField.get();
		byte[] array = field.get();

		return formatHexdump(array, offset, toAppendTo);
	}

	public String formatHexdump(Header header) {
		return formatHexdump(new MetaHeader(this, header));

	}

	public String formatHexdump(MetaHeader header) {
		return formatHexdump(header, new StringBuilder()).toString();
	}

	public StringBuilder formatHexdump(MetaHeader header, StringBuilder toAppendTo) {
		int offset = header.offset();
		byte[] array = new byte[header.length()];
		header.buffer().get(0, array);

		return formatHexdump(array, offset, toAppendTo);
	}

	public String formatHexdump(Packet packet) {
		return formatHexdump(new MetaPacket(this, packet));
	}

	public String formatHexdump(MetaPacket packet) {
		return formatHexdump(packet, new StringBuilder()).toString();
	}

	public StringBuilder formatHexdump(MetaPacket packet, StringBuilder toAppendTo) {
		int offset = 0;
		byte[] array = new byte[packet.captureLength()];
		packet.buffer().get(0, array);

		return formatHexdump(array, offset, toAppendTo);
	}

	private StringBuilder formatLeft(String label, StringBuilder toAppendTo) {
		return toAppendTo
				.append("%-10s".formatted(label + ":"));
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.meta.MetaFormat#formatMeta(com.slytechs.jnet.protocol.packet.meta.MetaElement,
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

	private StringBuilder formatSummary(MetaElement element, StringBuilder toAppendTo, Detail detail) {
		var display = element.getMeta(DisplaysInfo.class).select(detail);
		if (display == null)
			return toAppendTo;

		var displayFormat = display.value();
		if (displayFormat.isBlank())
			return toAppendTo;

		var args = super.buildDisplayArgs(element, element, displayFormat);
		displayFormat = super.rewriteDisplayArgs(displayFormat);

		var summaryLine = displayFormat.formatted(args);

		toAppendTo
//				.append(" ****** ")
//				.append(" + ")
				.append(summaryLine)
//				.append(" ******")
				.append("\n");

		return toAppendTo;
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.meta.MetaDomain#name()
	 */
	@Override
	public String name() {
		return "";
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.meta.MetaDomain#parent()
	 */
	@Override
	public MetaDomain parent() {
		return Global.get();
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.meta.MetaDomain#findKey(java.lang.Object)
	 */
	@Override
	public <K, V> Optional<V> findKey(K key) {
		return Optional.empty();
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.meta.MetaDomain#findDomain(java.lang.String)
	 */
	@Override
	public MetaDomain findDomain(String name) {
		return null;
	}

}
