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

import java.util.Objects;
import java.util.Optional;

import com.slytechs.jnet.runtime.util.Detail;
import com.slytechs.jnet.runtime.util.NotFound;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public abstract class MetaComponent
		extends MetaElement
		implements Comparable<MetaComponent> {

	private final String name;
	private final String abbr;
	private final String note;
	private final int ordinal;

	protected MetaComponent(MetaContext context, String name, String abbr, String note, int ordinal) {
		super(context);

		this.name = Objects.requireNonNull(name, "name");
		this.abbr = abbr;
		this.note = note;
		this.ordinal = ordinal;
	}

	protected MetaComponent(MetaContext context, MetaInfo metaInfo) {
		this(
				context,
				metaInfo.name(),
				metaInfo.abbr(),
				metaInfo.note(),
				metaInfo.ordinal());
	}

	public final Optional<String> abbr() {
		return Optional.ofNullable(abbr);
	}

	private StringBuilder buildComponentString(StringBuilder b, Detail detail) {
		if (detail.isLow())
			return buildDetailedString(b, detail);

		b.append(getClass().getSimpleName()).append(" [");
		b.append("name=\"%s\"".formatted(name));

		if (detail.isHigh()) {
			b.append("")
					.append(", abbr=\"%s\"".formatted(abbr().orElse("")))
					.append(", note=\"%s\"".formatted(note().orElse("")));
		}

		buildDetailedString(b, detail);

		b.append("]");

		return b;
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public final int compareTo(MetaComponent o) {
		return ordinal - o.ordinal;
	}

	protected abstract StringBuilder buildDetailedString(StringBuilder b, Detail detail);

	/**
	 * @see com.slytechs.jnet.protocol.packet.meta.MetaElement#buildString(java.lang.StringBuilder,
	 *      com.slytechs.jnet.runtime.util.Detail)
	 */
	@Override
	public final StringBuilder buildString(StringBuilder b, Detail detail) {
		return buildComponentString(b, detail);
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		return Objects.equals(name, obj);
	}

	public <T extends MetaField> Optional<T> findField(String name) {
		return Optional.empty();
	}

	protected String[] normalizePath(String... path) {
		if (path.length == 1)
			path = path[0].split("\\.");

		return path;
	}

	public MetaField getField(String... path) throws NotFound {
		throw new NotFound(path);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	public final String name() {
		return name;
	}

	public final Optional<String> note() {
		return Optional.ofNullable(note);
	}

	public String label(Detail detail) {
		if (detail.isLow())
			return (abbr == null) ? name : abbr;

		return name;
	}

}
