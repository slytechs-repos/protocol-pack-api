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

import static com.slytechs.jnet.runtime.util.function.CheckedConsumer.*;

import java.io.IOException;

import com.slytechs.jnet.protocol.packet.Header;
import com.slytechs.jnet.runtime.util.Detail;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public class HeaderFormatter extends FieldFormatter {

	public HeaderFormatter(MetaContext context, String string) {
		super(context);
	}

	public HeaderFormatter(String formatString) {
		super(formatString);
	}

	private void formatLine(Appendable out, String prefix, MetaField field, Detail detail) throws IOException {
		out.append(prefix);
		super.formatField(out, field, detail);
		out.append("\n");
	}

	public String formatHeader(Header header, Detail detail) {
		return formatHeader(new StringBuilder(), header, detail).toString();
	}

	public Appendable formatHeader(Appendable out, Header header, Detail detail) {
		
		

		return out;
	}

	public Appendable formatHeader(Appendable out, MetaHeader header) {
		return formatHeader(out, header, Detail.DEFAULT);
	}

	public Appendable formatHeader(Appendable out, MetaHeader header, Detail detail) {
		String prefix = header.abbr().map(s -> s + ":").orElse(header.name() + ":");

		header.streamFields(detail)
				.sorted() // Sorted according to Meta.ordinal() value
				.forEach(wrapConsumer(field -> formatLine(out, prefix, field, detail)));

		return out;
	}

	public String formatHeader(MetaHeader header) {
		return formatHeader(header, Detail.DEFAULT);

	}

	public String formatHeader(MetaHeader header, Detail detail) {
		return formatHeader(new StringBuilder(), header, detail).toString();

	}
}
