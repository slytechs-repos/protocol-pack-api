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
package com.slytechs.jnet.protocol.constants;

import static com.slytechs.jnet.runtime.util.Strings.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.slytechs.jnet.protocol.HeaderInfo;
import com.slytechs.jnet.protocol.HeaderId;
import com.slytechs.jnet.protocol.packet.Header;
import com.slytechs.jnet.protocol.packet.HeaderExtensionInfo;
import com.slytechs.jnet.protocol.packet.HeaderNotFound;
import com.slytechs.jnet.protocol.packet.Other;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public abstract class Pack<E extends Enum<? extends HeaderInfo>> {

	private enum EmptyPack implements HeaderInfo {
		;

		@Override
		public int getHeaderId() {
			throw new UnsupportedOperationException("not implemented yet");
		}

		/**
		 * @see com.slytechs.jnet.protocol.HeaderSupplier#newHeaderInstance()
		 */
		@Override
		public Header newHeaderInstance() {
			return new Other();
		}

	}

	private static class PackNotLoaded extends Pack<EmptyPack> {

		protected PackNotLoaded(PackInfo id) {
			super(id, id.getPackName(), null);
		}

		@Override
		protected boolean isDetectable() {
			return Pack.isPackDetected(getPackId());
		}

		/**
		 * @see com.slytechs.jnet.protocol.constants.Pack#isPackLoaded()
		 */
		@Override
		public boolean isPackLoaded() {
			return false;
		}

		/**
		 * @see com.slytechs.jnet.protocol.constants.Pack#getHeader(int)
		 */
		@Override
		public HeaderInfo getHeader(int id) throws HeaderNotFound {
			throw new UnsupportedOperationException("pack is not loaded");
		}

	}

	private static class PackVariableOptions extends Pack<EmptyPack> {

		protected PackVariableOptions() {
			super(PackInfo.OPTS, PackInfo.OPTS.getPackName(), null);
		}

		@Override
		protected boolean isDetectable() {
			return true;
		}

		/**
		 * @see com.slytechs.jnet.protocol.constants.Pack#isPackLoaded()
		 */
		@Override
		public boolean isPackLoaded() {
			return true;
		}

		/**
		 * @see com.slytechs.jnet.protocol.constants.Pack#getHeader(int)
		 */
		@Override
		public HeaderInfo getHeader(int id) throws HeaderNotFound {
			throw new UnsupportedOperationException("not implemented yet");
		}

	}

	private static final Pack<?>[] packTable = new Pack[PackInfo.values().length];

	static {
		PackInfo[] values = PackInfo.values();

		for (int i = 0; i < values.length; i++)
			packTable[i] = new PackNotLoaded(values[i]);

		if (!loadPack(PackInfo.CORE))
			throw new IllegalStateException("unable to load core protocol pack");

		packTable[PackInfo.OPTS.ordinal()] = new PackVariableOptions();
	}

	public static Optional<? extends HeaderInfo> findHeader(int id) {
		int packId = HeaderId.decodePackId(id);
		Pack<?> pack = getLoadedPack(packId);
		if (pack == null)
			return Optional.empty();

		try {
			return Optional.of(pack.getHeader(id));
		} catch (HeaderNotFound e) {
			throw new IllegalStateException("Missing header in pack [%d]"
					.formatted(id));
		}
	}

	public static Optional<? extends HeaderInfo> findExtension(int id, int extensionId) {
		Optional<? extends HeaderInfo> parent = findHeader(id);
		if (parent.isEmpty())
			return parent;

		var extInfos = parent.get().getExtensionInfos();
		int extOrdinal = HeaderId.decodeIdOrdinal(extensionId);

		return Optional.of(extInfos[extOrdinal]);
	}

	public abstract HeaderInfo getHeader(int id) throws HeaderNotFound;

	private static <E extends Enum<? extends HeaderInfo>> int countAllPackExtensions() {
		int count = 0;

		for (Pack<?> pack : packTable) {
			if (!pack.isPackLoaded() || (pack.packInfo == PackInfo.OPTS))
				continue;

			count += countSinglePackExtensions(pack.protocolIds);
		}

		return count;
	}

	private static <E extends Enum<? extends HeaderInfo>> int countAllProtocols(E[] ids) {
		int count = ids.length;

		for (E id : ids) {
			HeaderInfo i = (HeaderInfo) id;
			if (i.getExtensionInfoClass() == null)
				continue;

			HeaderExtensionInfo[] exts = (HeaderExtensionInfo[]) i
					.getExtensionInfoClass()
					.getEnumConstants();
			if (exts != null)
				count += exts.length;
		}

		return count;
	}

	private static <E extends Enum<? extends HeaderInfo>> int countSinglePackExtensions(E[] ids) {
		int count = 0;

		for (E id : ids) {
			HeaderInfo i = (HeaderInfo) id;

			HeaderExtensionInfo[] exts = (HeaderExtensionInfo[]) i
					.getExtensionInfoClass()
					.getEnumConstants();
			if (exts != null)
				count += exts.length;
		}

		return count;
	}

	@SuppressWarnings("unchecked")
	public static <T_PACK extends Pack<?>> T_PACK getDetectedPack(int packId) {
		int packOrdinal = HeaderId.decodePackOrdinal(packId);

		Pack<?> pack = packTable[packOrdinal];
		if (!pack.isDetectable())
			return null;

		return (T_PACK) pack;
	}

	@SuppressWarnings("unchecked")
	public static <T_PACK extends Pack<?>> T_PACK getLoadedPack(int packId) {
		int packOrdinal = HeaderId.decodePackOrdinal(packId);

		Pack<?> pack = packTable[packOrdinal];
		if (!pack.isPackLoaded())
			return null;

		return (T_PACK) pack;
	}

	public static <T_PACK extends Pack<?>> T_PACK getDetectedPack(PackInfo id) {
		return getDetectedPack(id.getPackIdAsInt());
	}

	public static boolean isPackDetected(PackInfo packInfo) {
		try {
			String moduleName = packInfo.getPackModuleName();
			String className = packInfo.getPackClassName();

			if (className == null)
				return false;

			Optional<Module> module = Optional.empty();
			if (moduleName != null)
				module = ModuleLayer.boot()
						.findModule(moduleName);

			if (module.isPresent())
				return (Class.forName(module.get(), className) != null);
			else
				return (Class.forName(className, false, Pack.class.getClassLoader()) != null);

		} catch (SecurityException | ClassNotFoundException e) {
			return false;
		}
	}

	public static List<Pack<?>> listAllDeclaredPacks() {
		return Arrays.asList(packTable);
	}

	public static List<Pack<?>> listAllDetectablePacks() {
		return Arrays.asList(packTable)
				.stream()
				.filter(Pack::isDetectable)
				.collect(Collectors.toList());
	}

	public static List<Pack<?>> listAllLoadedPacks() {
		return Arrays.asList(packTable)
				.stream()
				.filter(Pack::isPackLoaded)
				.collect(Collectors.toList());
	}

	public static synchronized boolean loadPack(int id) {
		PackInfo packInfo = PackInfo.values()[HeaderId.decodePackOrdinal(id)];

		return loadPack(packInfo);
	}

	public static synchronized boolean loadPack(PackInfo packInfo) {
		if (packTable[packInfo.ordinal()].isPackLoaded())
			return true;

		Pack<?> pack = loadPackClass(
				packInfo.getPackModuleName(),
				packInfo.getPackClassName());
		if (pack == null)
			return false;

		packTable[packInfo.ordinal()] = pack;

		return true;
	}

	@SuppressWarnings("unchecked")
	private static Pack<?> loadPackClass(String moduleName, String className) {
		try {

			if (className == null)
				return null;

			Optional<Module> module = Optional.empty();
			if (moduleName != null)
				module = ModuleLayer.boot()
						.findModule(moduleName);

			Class<Pack<?>> clazz;
			if (module.isPresent())
				clazz = (Class<Pack<?>>) Class
						.forName(module.get(), className);
			else
				clazz = (Class<Pack<?>>) Class
						.forName(className);
			if (clazz == null)
				return null;

			Method singletonMethod = clazz.getDeclaredMethod("singleton");

			Pack<?> pack = (Pack<?>) singletonMethod.invoke(null);

			return pack;

		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static synchronized boolean unloadPack(PackInfo packInfo) {

		if (!packTable[packInfo.ordinal()].isPackLoaded())
			return false;

		packTable[packInfo.ordinal()] = new PackNotLoaded(packInfo);

		return true;
	}

	private final PackInfo packInfo;
	private final String name;
	private final int intId;
	private final E[] protocolIds;
	private Module module;

	protected Pack(PackInfo id, E[] protocolIds) {
		this.packInfo = id;
		this.name = id.getPackName();
		this.protocolIds = Objects.requireNonNull(protocolIds, "protocolIds");
		this.intId = id.getPackIdAsInt();
		this.module = ModuleLayer.boot()
				.findModule(id.getPackModuleName())
				.orElse(null);
	}

	protected Pack(PackInfo id, String name, E[] protocolIds) {
		this.packInfo = id;
		this.name = name;
		this.protocolIds = protocolIds;
		this.intId = id.getPackIdAsInt();
	}

	public PackInfo getPackId() {
		return packInfo;
	}

	public String getPackClassName() {
		return packInfo.getPackClassName();
	}

	public String getPackModuleName() {
		return packInfo.getPackModuleName();
	}

	public Module getModule() {
		return module;
	}

	public int getPackIdAsInt() {
		return intId;
	}

	public String getPackName() {
		return name;
	}

	public E[] getProtocolIds() {
		if (protocolIds == null)
			throw new UnsupportedOperationException();

		return protocolIds;
	}

	protected boolean isDetectable() {
		return true;
	}

	public boolean isPackLoaded() {
		return true;
	}

	public HeaderInfo[] toArray() {
		return (HeaderInfo[]) protocolIds;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		int id = getPackIdAsInt();
		int ordinal = HeaderId.decodePackOrdinal(id);

		boolean isOptions = (packInfo == PackInfo.OPTS);
		boolean isDetect = isDetectable();
		boolean isLoaded = isPackLoaded();

		String status;
		if (isOptions) {
			int count = countAllPackExtensions();
			status = "loaded=%2s definitions".formatted(count);

		} else if (isLoaded) {
			int count = countAllProtocols(protocolIds);
			status = "loaded=%2s definitions".formatted(count);

		} else {
			status = isDetect
					? "<pack not loaded but detected>"
					: "<pack not loaded>";
		}

		return "Pack ["
				+ "name=%-16s".formatted(dquote(name))
				+ " (%2d/0x%03X)".formatted(ordinal, id)
				+ ", " + status
				+ "]";
	}
}
