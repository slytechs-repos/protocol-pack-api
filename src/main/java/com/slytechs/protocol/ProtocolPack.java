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
package com.slytechs.protocol;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.slytechs.protocol.pack.core.constants.PackInfo;

/**
 * The Class Pack.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 * @param <E> the element type
 */
public abstract class ProtocolPack<E extends Enum<? extends HeaderInfo>> {

	/**
	 * The Enum EmptyPack.
	 */
	private enum EmptyPack implements HeaderInfo {
		;

		/**
		 * @see com.slytechs.protocol.HeaderInfo#getHeaderId()
		 */
		@Override
		public int getHeaderId() {
			throw new UnsupportedOperationException("not implemented yet");
		}

		/**
		 * New header instance.
		 *
		 * @return the header
		 * @see com.slytechs.protocol.HeaderSupplier#newHeaderInstance()
		 */
		@Override
		public Header newHeaderInstance() {
			return new Other();
		}

	}

	/**
	 * The Class PackNotLoaded.
	 */
	private static class PackNotLoaded extends ProtocolPack<EmptyPack> {

		/**
		 * Instantiates a new pack not loaded.
		 *
		 * @param id the id
		 */
		protected PackNotLoaded(PackInfo id) {
			super(id, id.getPackName(), null);
		}

		/**
		 * @see com.slytechs.protocol.ProtocolPack#isDetectable()
		 */
		@Override
		protected boolean isDetectable() {
			return ProtocolPack.isPackDetected(getPackId());
		}

		/**
		 * Checks if is pack loaded.
		 *
		 * @return true, if is pack loaded
		 * @see com.slytechs.protocol.ProtocolPack#isPackLoaded()
		 */
		@Override
		public boolean isPackLoaded() {
			return false;
		}

		/**
		 * Gets the header.
		 *
		 * @param id the id
		 * @return the header
		 * @throws HeaderNotFound the header not found
		 * @see com.slytechs.protocol.ProtocolPack#getHeader(int)
		 */
		@Override
		public HeaderInfo getHeader(int id) throws HeaderNotFound {
			throw new UnsupportedOperationException("pack is not loaded");
		}

	}

	/**
	 * The Class PackVariableOptions.
	 */
	private static class PackVariableOptions extends ProtocolPack<EmptyPack> {

		/**
		 * Instantiates a new pack variable options.
		 */
		protected PackVariableOptions() {
			super(PackInfo.OPTS, PackInfo.OPTS.getPackName(), null);
		}

		/**
		 * @see com.slytechs.protocol.ProtocolPack#isDetectable()
		 */
		@Override
		protected boolean isDetectable() {
			return true;
		}

		/**
		 * Checks if is pack loaded.
		 *
		 * @return true, if is pack loaded
		 * @see com.slytechs.protocol.ProtocolPack#isPackLoaded()
		 */
		@Override
		public boolean isPackLoaded() {
			return true;
		}

		/**
		 * Gets the header.
		 *
		 * @param id the id
		 * @return the header
		 * @throws HeaderNotFound the header not found
		 * @see com.slytechs.protocol.ProtocolPack#getHeader(int)
		 */
		@Override
		public HeaderInfo getHeader(int id) throws HeaderNotFound {
			throw new UnsupportedOperationException("not implemented yet");
		}

	}

	/** The Constant packTable. */
	private static final ProtocolPack<?>[] packTable = new ProtocolPack[PackInfo.values().length];

	static {
		PackInfo[] values = PackInfo.values();

		for (int i = 0; i < values.length; i++)
			packTable[i] = new PackNotLoaded(values[i]);

		if (!loadPack(PackInfo.CORE))
			throw new IllegalStateException("unable to load core protocol pack");

		packTable[PackInfo.OPTS.ordinal()] = new PackVariableOptions();
	}

	/**
	 * Find header.
	 *
	 * @param id the id
	 * @return the optional<? extends header info>
	 */
	public static Optional<? extends HeaderInfo> findHeader(int id) {
		int packId = HeaderId.decodePackId(id);
		ProtocolPack<?> pack = getLoadedPack(packId);
		if (pack == null)
			return Optional.empty();

		try {
			return Optional.of(pack.getHeader(id));
		} catch (HeaderNotFound e) {
			throw new IllegalStateException("Missing header in pack [%d]"
					.formatted(id));
		}
	}

	/**
	 * Find extension.
	 *
	 * @param id          the id
	 * @param extensionId the extension id
	 * @return the optional<? extends header info>
	 */
	public static Optional<? extends HeaderInfo> findExtension(int id, int extensionId) {
		Optional<? extends HeaderInfo> parent = findHeader(id);
		if (parent.isEmpty())
			return parent;

		var extInfos = parent.get().getExtensionInfos();
		int extOrdinal = HeaderId.decodeIdOrdinal(extensionId);

		return Optional.of(extInfos[extOrdinal]);
	}

	/**
	 * Gets the header.
	 *
	 * @param id the id
	 * @return the header
	 * @throws HeaderNotFound the header not found
	 */
	public abstract HeaderInfo getHeader(int id) throws HeaderNotFound;

	/**
	 * Count all pack extensions.
	 *
	 * @param <E> the element type
	 * @return the int
	 */
	private static <E extends Enum<? extends HeaderInfo>> int countAllPackExtensions() {
		int count = 0;

		for (ProtocolPack<?> pack : packTable) {
			if (!pack.isPackLoaded() || (pack.packInfo == PackInfo.OPTS))
				continue;

			count += countSinglePackExtensions(pack.protocolIds);
		}

		return count;
	}

	/**
	 * Count all protocols.
	 *
	 * @param <E> the element type
	 * @param ids the ids
	 * @return the int
	 */
	private static <E extends Enum<? extends HeaderInfo>> int countAllProtocols(E[] ids) {
		int count = ids.length;

		for (E id : ids) {
			HeaderInfo i = (HeaderInfo) id;

			HeaderExtensionInfo[] exts = i.getExtensionInfos();
			if (exts.length > 0)
				count += exts.length;
		}

		return count;
	}

	/**
	 * Count single pack extensions.
	 *
	 * @param <E> the element type
	 * @param ids the ids
	 * @return the int
	 */
	private static <E extends Enum<? extends HeaderInfo>> int countSinglePackExtensions(E[] ids) {
		int count = 0;

		for (E id : ids) {
			HeaderInfo i = (HeaderInfo) id;

			HeaderExtensionInfo[] exts = i.getExtensionInfos();
			if (exts.length > 0)
				count += exts.length;
		}

		return count;
	}

	/**
	 * Gets the detected pack.
	 *
	 * @param <T_PACK> the generic type
	 * @param packId   the pack id
	 * @return the detected pack
	 */
	@SuppressWarnings("unchecked")
	public static <T_PACK extends ProtocolPack<?>> T_PACK getDetectedPack(int packId) {
		int packOrdinal = HeaderId.decodePackOrdinal(packId);

		ProtocolPack<?> pack = packTable[packOrdinal];
		if (!pack.isDetectable())
			return null;

		return (T_PACK) pack;
	}

	/**
	 * Gets the loaded pack.
	 *
	 * @param <T_PACK> the generic type
	 * @param packId   the pack id
	 * @return the loaded pack
	 */
	@SuppressWarnings("unchecked")
	public static <T_PACK extends ProtocolPack<?>> T_PACK getLoadedPack(int packId) {
		int packOrdinal = HeaderId.decodePackOrdinal(packId);

		ProtocolPack<?> pack = packTable[packOrdinal];
		if (!pack.isPackLoaded())
			return null;

		return (T_PACK) pack;
	}

	/**
	 * Gets the detected pack.
	 *
	 * @param <T_PACK> the generic type
	 * @param id       the id
	 * @return the detected pack
	 */
	public static <T_PACK extends ProtocolPack<?>> T_PACK getDetectedPack(PackInfo id) {
		return getDetectedPack(id.getPackIdAsInt());
	}

	/**
	 * Checks if is pack detected.
	 *
	 * @param packInfo the pack info
	 * @return true, if is pack detected
	 */
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
				return (Class.forName(className, false, ProtocolPack.class.getClassLoader()) != null);

		} catch (SecurityException | ClassNotFoundException e) {
			return false;
		}
	}

	/**
	 * List all declared packs.
	 *
	 * @return the list
	 */
	public static List<ProtocolPack<?>> listAllDeclaredPacks() {
		return Arrays.asList(packTable);
	}

	/**
	 * List all detectable packs.
	 *
	 * @return the list
	 */
	public static List<ProtocolPack<?>> listAllDetectablePacks() {
		return Arrays.asList(packTable)
				.stream()
				.filter(ProtocolPack::isDetectable)
				.collect(Collectors.toList());
	}

	/**
	 * List all loaded packs.
	 *
	 * @return the list
	 */
	public static List<ProtocolPack<?>> listAllLoadedPacks() {
		return Arrays.asList(packTable)
				.stream()
				.filter(ProtocolPack::isPackLoaded)
				.collect(Collectors.toList());
	}

	/**
	 * Load pack.
	 *
	 * @param id the id
	 * @return true, if successful
	 */
	public static synchronized boolean loadPack(int id) {
		PackInfo packInfo = PackInfo.values()[HeaderId.decodePackOrdinal(id)];

		return loadPack(packInfo);
	}

	/**
	 * Load pack.
	 *
	 * @param packInfo the pack info
	 * @return true, if successful
	 */
	public static synchronized boolean loadPack(PackInfo packInfo) {
		if (packTable[packInfo.ordinal()].isPackLoaded())
			return true;

		String moduleName = packInfo.getPackModuleName();
		String className = packInfo.getPackClassName();

		ProtocolPack<?> pack = loadPackClass(moduleName, className);
		if (pack == null)
			return false;

		packTable[packInfo.ordinal()] = pack;

		return true;
	}

	/**
	 * Load pack class.
	 *
	 * @param moduleName the module name
	 * @param className  the class name
	 * @return the pack
	 */
	@SuppressWarnings("unchecked")
	private static ProtocolPack<?> loadPackClass(String moduleName, String className) {
		try {

			if (className == null)
				return null;

			Optional<Module> module = Optional.empty();
			if (moduleName != null)
				module = ModuleLayer.boot()
						.findModule(moduleName);

			Class<ProtocolPack<?>> clazz;
			if (module.isPresent())
				clazz = (Class<ProtocolPack<?>>) Class
						.forName(module.get(), className);
			else
				clazz = (Class<ProtocolPack<?>>) Class
						.forName(className);
			if (clazz == null)
				return null;

			Method singletonMethod = clazz.getDeclaredMethod("singleton");

			ProtocolPack<?> pack = (ProtocolPack<?>) singletonMethod.invoke(null);

			return pack;

		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Unload pack.
	 *
	 * @param packInfo the pack info
	 * @return true, if successful
	 */
	public static synchronized boolean unloadPack(PackInfo packInfo) {

		if (!packTable[packInfo.ordinal()].isPackLoaded())
			return false;

		packTable[packInfo.ordinal()] = new PackNotLoaded(packInfo);

		return true;
	}

	/** The pack info. */
	private final PackInfo packInfo;
	
	/** The name. */
	private final String name;
	
	/** The int id. */
	private final int intId;
	
	/** The protocol ids. */
	private final E[] protocolIds;
	
	/** The module. */
	private Module module;

	/**
	 * Instantiates a new pack.
	 *
	 * @param id          the id
	 * @param protocolIds the protocol ids
	 */
	protected ProtocolPack(PackInfo id, E[] protocolIds) {
		this.packInfo = id;
		this.name = id.getPackName();
		this.protocolIds = Objects.requireNonNull(protocolIds, "protocolIds");
		this.intId = id.getPackIdAsInt();
		this.module = ModuleLayer.boot()
				.findModule(id.getPackModuleName())
				.orElse(null);
	}

	/**
	 * Instantiates a new pack.
	 *
	 * @param id          the id
	 * @param name        the name
	 * @param protocolIds the protocol ids
	 */
	protected ProtocolPack(PackInfo id, String name, E[] protocolIds) {
		this.packInfo = id;
		this.name = name;
		this.protocolIds = protocolIds;
		this.intId = id.getPackIdAsInt();
	}

	/**
	 * Gets the pack id.
	 *
	 * @return the pack id
	 */
	public PackInfo getPackId() {
		return packInfo;
	}

	/**
	 * Gets the pack class name.
	 *
	 * @return the pack class name
	 */
	public String getPackClassName() {
		return packInfo.getPackClassName();
	}

	/**
	 * Gets the pack module name.
	 *
	 * @return the pack module name
	 */
	public String getPackModuleName() {
		return packInfo.getPackModuleName();
	}

	/**
	 * Gets the module.
	 *
	 * @return the module
	 */
	public Module getModule() {
		return module;
	}

	/**
	 * Gets the pack id as int.
	 *
	 * @return the pack id as int
	 */
	public int getPackIdAsInt() {
		return intId;
	}

	/**
	 * Gets the pack name.
	 *
	 * @return the pack name
	 */
	public String getPackName() {
		return name;
	}

	/**
	 * Gets the protocol ids.
	 *
	 * @return the protocol ids
	 */
	public E[] getProtocolIds() {
		if (protocolIds == null)
			throw new UnsupportedOperationException();

		return protocolIds;
	}

	/**
	 * Checks if is detectable.
	 *
	 * @return true, if is detectable
	 */
	protected boolean isDetectable() {
		return true;
	}

	/**
	 * Checks if is pack loaded.
	 *
	 * @return true, if is pack loaded
	 */
	public boolean isPackLoaded() {
		return true;
	}

	/**
	 * To array.
	 *
	 * @return the header info[]
	 */
	public HeaderInfo[] toArray() {
		return (HeaderInfo[]) protocolIds;
	}

	/**
	 * To string.
	 *
	 * @return the string
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
				+ "name=%-16s".formatted("\"" + name + "\"")
				+ " (%2d/0x%03X)".formatted(ordinal, id)
				+ ", " + status
				+ "]";
	}
}
