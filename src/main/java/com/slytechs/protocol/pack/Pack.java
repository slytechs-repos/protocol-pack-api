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
package com.slytechs.protocol.pack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.slytechs.protocol.Header;
import com.slytechs.protocol.HeaderExtensionInfo;
import com.slytechs.protocol.HeaderInfo;
import com.slytechs.protocol.HeaderNotFound;
import com.slytechs.protocol.Other;

/**
 * A protocol pack information and instance.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 * @param <E> the element type
 */
public abstract class Pack<E extends Enum<? extends HeaderInfo>> {

	/**
	 * An empty protocol pack used as place holder.
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
	 * A placeholder protocol pack used to represent all of the defined packs, which
	 * are not yet loaded. When a real pack is actually loaded, the PackNotLoaded
	 * stub is replaced with real protocol pack information.
	 */
	private static class PackNotLoaded extends Pack<EmptyPack> {

		/**
		 * Instantiates a new pack not loaded.
		 *
		 * @param id the id
		 */
		protected PackNotLoaded(DeclaredPackIds id) {
			super(id, id.getPackName(), null);
		}

		/**
		 * @see com.slytechs.protocol.pack.Pack#isDetectable()
		 */
		@Override
		protected boolean isDetectable() {
			return Pack.isPackDetected(getPackId());
		}

		/**
		 * Checks if is pack loaded.
		 *
		 * @return true, if is pack loaded
		 * @see com.slytechs.protocol.pack.Pack#isPackLoaded()
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
		 * @see com.slytechs.protocol.pack.Pack#getHeader(int)
		 */
		@Override
		public HeaderInfo getHeader(int id) throws HeaderNotFound {
			throw new UnsupportedOperationException("pack is not loaded");
		}

	}

	/**
	 * A specialized pack used to represent all of the protocol header extensions
	 * and options.
	 */
	private static class PackVariableOptions extends Pack<EmptyPack> {

		/**
		 * Instantiates a new pack variable options.
		 */
		protected PackVariableOptions() {
			super(DeclaredPackIds.OPTS, DeclaredPackIds.OPTS.getPackName(), null);
		}

		/**
		 * @see com.slytechs.protocol.pack.Pack#isDetectable()
		 */
		@Override
		protected boolean isDetectable() {
			return true;
		}

		/**
		 * Checks if is pack loaded.
		 *
		 * @return true, if is pack loaded
		 * @see com.slytechs.protocol.pack.Pack#isPackLoaded()
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
		 * @see com.slytechs.protocol.pack.Pack#getHeader(int)
		 */
		@Override
		public HeaderInfo getHeader(int id) throws HeaderNotFound {
			throw new UnsupportedOperationException("not implemented yet");
		}

	}

	/** The Constant packTable. */
	private static final Pack<?>[] packTable = new Pack[DeclaredPackIds.values().length];

	static {
		DeclaredPackIds[] values = DeclaredPackIds.values();

		for (int i = 0; i < values.length; i++)
			packTable[i] = new PackNotLoaded(values[i]);

		if (!loadPack(DeclaredPackIds.CORE))
			throw new IllegalStateException("unable to load core protocol pack");

		packTable[DeclaredPackIds.OPTS.ordinal()] = new PackVariableOptions();
	}

	/**
	 * Finds information for a protocol header with a given id within this pack.
	 *
	 * @param id the header id
	 * @return optional header information
	 */
	public static Optional<? extends HeaderInfo> findHeader(int id) {
		int packId = PackId.decodePackId(id);
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

	/**
	 * Finds information for a protocol header extension of a parent header within
	 * this pack.
	 *
	 * @param id          parent header id
	 * @param extensionId the header extension id
	 * @return optional header information
	 */
	public static Optional<? extends HeaderInfo> findExtension(int id, int extensionId) {
		Optional<? extends HeaderInfo> parent = findHeader(id);
		if (parent.isEmpty())
			return parent;

		var extInfos = parent.get().getExtensionInfos();
		int extOrdinal = PackId.decodeIdOrdinal(extensionId);

		return Optional.of(extInfos[extOrdinal]);
	}

	/**
	 * Gets information for a protocol header extension of a parent header within
	 * this pack.
	 *
	 * @param id the header id
	 * @return the header information, does not return null
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

		for (Pack<?> pack : packTable) {
			if (!pack.isPackLoaded() || (pack.declaredPackIds == DeclaredPackIds.OPTS))
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
	 * Gets all of the detected packs.
	 *
	 * @param <T_PACK> the generic type
	 * @param packId   the pack id
	 * @return the detected pack
	 */
	@SuppressWarnings("unchecked")
	public static <T_PACK extends Pack<?>> T_PACK getDetectedPack(int packId) {
		int packOrdinal = PackId.decodePackOrdinal(packId);

		Pack<?> pack = packTable[packOrdinal];
		if (!pack.isDetectable())
			return null;

		return (T_PACK) pack;
	}

	/**
	 * Gets all of the loaded packs.
	 *
	 * @param <T_PACK> the generic type
	 * @param packId   the pack id
	 * @return the loaded pack
	 */
	@SuppressWarnings("unchecked")
	public static <T_PACK extends Pack<?>> T_PACK getLoadedPack(int packId) {
		int packOrdinal = PackId.decodePackOrdinal(packId);

		Pack<?> pack = packTable[packOrdinal];
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
	public static <T_PACK extends Pack<?>> T_PACK getDetectedPack(DeclaredPackIds id) {
		return getDetectedPack(id.getPackIdAsInt());
	}

	/**
	 * Checks if is pack detected.
	 *
	 * @param declaredPackIds the pack info
	 * @return true, if is pack detected
	 */
	public static boolean isPackDetected(DeclaredPackIds declaredPackIds) {
		try {
			String moduleName = declaredPackIds.getPackModuleName();
			String className = declaredPackIds.getPackClassName();

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

	/**
	 * List all declared packs.
	 *
	 * @return the list
	 */
	public static List<Pack<?>> listAllDeclaredPacks() {
		return Arrays.asList(packTable);
	}

	/**
	 * List all detectable packs.
	 *
	 * @return the list
	 */
	public static List<Pack<?>> listAllDetectablePacks() {
		return Arrays.asList(packTable)
				.stream()
				.filter(Pack::isDetectable)
				.collect(Collectors.toList());
	}

	/**
	 * List all loaded packs.
	 *
	 * @return the list
	 */
	public static List<Pack<?>> listAllLoadedPacks() {
		return Arrays.asList(packTable)
				.stream()
				.filter(Pack::isPackLoaded)
				.collect(Collectors.toList());
	}

	/**
	 * Load pack.
	 *
	 * @param id the id
	 * @return true, if successful
	 */
	public static synchronized boolean loadPack(int id) {
		DeclaredPackIds declaredPackIds = DeclaredPackIds.values()[PackId.decodePackOrdinal(id)];

		return loadPack(declaredPackIds);
	}

	/**
	 * Load pack.
	 *
	 * @param declaredPackIds the pack info
	 * @return true, if successful
	 */
	public static synchronized boolean loadPack(DeclaredPackIds declaredPackIds) {
		if (packTable[declaredPackIds.ordinal()].isPackLoaded())
			return true;

		String moduleName = declaredPackIds.getPackModuleName();
		String className = declaredPackIds.getPackClassName();

		Pack<?> pack = loadPackClass(moduleName, className);
		if (pack == null)
			return false;

		packTable[declaredPackIds.ordinal()] = pack;

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

	/**
	 * Unload pack.
	 *
	 * @param declaredPackIds the pack info
	 * @return true, if successful
	 */
	public static synchronized boolean unloadPack(DeclaredPackIds declaredPackIds) {

		if (!packTable[declaredPackIds.ordinal()].isPackLoaded())
			return false;

		packTable[declaredPackIds.ordinal()] = new PackNotLoaded(declaredPackIds);

		return true;
	}

	/** The pack info. */
	private final DeclaredPackIds declaredPackIds;

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
	protected Pack(DeclaredPackIds id, E[] protocolIds) {
		this.declaredPackIds = id;
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
	protected Pack(DeclaredPackIds id, String name, E[] protocolIds) {
		this.declaredPackIds = id;
		this.name = name;
		this.protocolIds = protocolIds;
		this.intId = id.getPackIdAsInt();
	}

	/**
	 * Gets the pack id.
	 *
	 * @return the pack id
	 */
	public DeclaredPackIds getPackId() {
		return declaredPackIds;
	}

	/**
	 * Gets the pack class name.
	 *
	 * @return the pack class name
	 */
	public String getPackClassName() {
		return declaredPackIds.getPackClassName();
	}

	/**
	 * Gets the pack module name.
	 *
	 * @return the pack module name
	 */
	public String getPackModuleName() {
		return declaredPackIds.getPackModuleName();
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
		int ordinal = PackId.decodePackOrdinal(id);

		boolean isOptions = (declaredPackIds == DeclaredPackIds.OPTS);
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
