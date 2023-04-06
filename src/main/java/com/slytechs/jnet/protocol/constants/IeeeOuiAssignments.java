/*
 * Apache License, Version 2.0
 * 
 * Copyright 2013-2022 Sly Technologies Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.slytechs.jnet.protocol.constants;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import com.slytechs.jnet.runtime.internal.ArrayUtils;
import com.slytechs.jnet.runtime.util.HexStrings;

public final class IeeeOuiAssignments {

	public static class OuiEntry {

		private final byte[] prefix;
		private final String name;
		private final String descriptrion;
		private final int mask;

		public OuiEntry(byte[] prefix, int mask, String name, String descriptrion) {
			this.prefix = prefix;
			this.mask = mask;
			this.name = name;
			this.descriptrion = descriptrion;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof byte[] array))
				return false;

			return ArrayUtils.equals(prefix, array, mask);
		}

		public String getDescriptrion() {
			return descriptrion;
		}

		public int getMask() {
			return mask;
		}

		public String getName() {
			return name;
		}

		public byte[] getPrefix() {
			return prefix;
		}

		@Override
		public int hashCode() {
			return ArrayUtils.hashCodeWithNetmask(prefix, 24);
		}

		@Override
		public String toString() {
			return "OuiEntry ["
					+ "prefix=" + HexStrings.toHexString(prefix)
					+ "/" + mask
					+ ", name=" + name
					+ ", descriptrion=" + descriptrion
					+ "]";
		}

	}

	private static class OuiTable {

		private static final int EUI64_BYTE_LENGTH = 8;
		private static final OuiTable OUI_MANUFACTURER_TABLE = new OuiTable();

		private static byte[] parseMac(String mac) {
			int index = mac.indexOf('/');
			if (index != -1)
				mac = mac.substring(0, index);

			byte[] array = HexStrings.parseHexString(mac);
			if (array.length != EUI64_BYTE_LENGTH) // Store all macs as EUI-64 in size
				array = Arrays.copyOf(array, EUI64_BYTE_LENGTH);

			return array;
		}

		private static int parseMask(String mac) {
			int mask = 24; // default mask

			int index = mac.indexOf('/');
			if (index != -1)
				mask = Integer.parseInt(mac.substring(index + 1));

			return mask;
		}

		private final Map<Integer, OuiEntry[]> map24BitHash = new HashMap<>();

		private int size = 0;

		public OuiTable() {

		}

		public void addEntry(byte[] prefix, int mask, String name, String description) {
			OuiEntry entry = new OuiEntry(prefix, mask, name, description);
			int hash = entry.hashCode();

			// Get entry and resize for new element
			OuiEntry[] row = map24BitHash.getOrDefault(hash, new OuiEntry[0]);
			row = Arrays.copyOf(row, row.length + 1);
			map24BitHash.put(hash, row);

			row[row.length - 1] = entry;
			Arrays.sort(row, (e1, e2) -> e2.mask - e1.mask); // longest bit-mask match first

			size++;
		}

		public boolean isLoaded() {
			return size > 0;
		}

		public void loadCvsTable(String cvsResourceName) throws IOException {
			InputStream in = IeeeOuiAssignments.class.getResourceAsStream(cvsResourceName);
			if (in == null)
				throw new FileNotFoundException("resource: " + cvsResourceName);

			readTable(in);
		}

		public Optional<OuiEntry> lookupEntry(byte[] mac) {

			int hash = ArrayUtils.hashCodeWithNetmask(mac, 24);

			OuiEntry[] e = map24BitHash.get(hash);
			if (e == null)
				return Optional.empty();

			for (int i = 0; i < e.length; i++)
				if (e[i].equals(mac))
					return Optional.of(e[i]);

			return Optional.empty();
		}

		public int readTable(InputStream in) throws IOException {

			try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {

				int errorCount = 0;
				int lineno = 0;
				String line = null;
				while ((line = reader.readLine()) != null) {
					lineno++;

					// Skip blank lines and comments
					if (line.isBlank() || line.startsWith("#"))
						continue;

					// Remove comments at the end of the line
					int indx = line.indexOf('#');
					if (indx != -1)
						line = line.substring(0, indx);

					String[] c = null;
					try {
						c = line.split("\t", 3);
						if (c.length == 3)
							addEntry(parseMac(c[0]), parseMask(c[0]), c[1], c[2]);

						else if (c.length == 2)
							addEntry(parseMac(c[0]), parseMask(c[0]), c[1], ""); // No description

						else
							throw new Error("Invalid line format");

					} catch (Throwable e) {
						if (errorCount++ < 36) // Stop reporting warnings after a few dozen
							LOG.warning("" + lineno + ": " + line + "! Invalid line format");
					}

				}

				return lineno;
			}
		}

		@Override
		public String toString() {
			return "OuiTable ["
					+ "size=" + size
					+ " rowOverlap=" + (size - map24BitHash.size())
					+ "]";
		}

		public void clear() {
			map24BitHash.clear();
			size = 0;
		}
	}

	private final static Logger LOG = Logger.getLogger(IeeeOuiAssignments.class.getCanonicalName());

	private static final String OUI_MANUFACTURE_CVS = "/ieee-oui-manufacturer-assignments.cvs";

	/**
	 * Loads IEEE assignment tables. If the tables have already been loaded, this
	 * method does nothing.
	 * 
	 * <p>
	 * To enable logging on load success {@link java.util.logging.Level#CONFIG}
	 * level.
	 * </p>
	 *
	 * @throws IllegalStateException since all of the tables are internal resources
	 *                               and included with the distribution, only
	 *                               abnormal circumstances would cause the tables
	 *                               to not be loaded
	 */
	public static void loadIeeeTables() throws IllegalStateException {
		if (!isManufacturerOuiLookupEnabled || OuiTable.OUI_MANUFACTURER_TABLE.isLoaded())
			return;

		try {
			long ts = System.currentTimeMillis();
			OuiTable.OUI_MANUFACTURER_TABLE.loadCvsTable(OUI_MANUFACTURE_CVS);

			LOG.config("loaded IEEE OUI manufacturer cvs lookup table in "
					+ (System.currentTimeMillis() - ts)
					+ "ms, with " + OuiTable.OUI_MANUFACTURER_TABLE.size + " entries");
		} catch (IOException e) {
			LOG.warning("Unable to local IEEE oui manufacturer cvs lookup table " + OUI_MANUFACTURE_CVS);

			throw new IllegalStateException("Unable to load internal IEEE oui cvs table", e);
		}
	}

	/**
	 * Looks up the provided MAC address against internal IEEE OUI manufacturer
	 * prefix table. If a match is found, using the provided MAC address and a
	 * netmask defined in the table.
	 * 
	 * <p>
	 * Note, that if the internal tables have not been loaded yet, a table load will
	 * be triggered. Therefore the first such lookup, may take significantly longer
	 * to complete as the tables are rather large. All subsequent lookups will be
	 * performed quickly with efficiency. It is recommended, that the user call
	 * {@link IeeeOuiAssignments#loadIeeeTables()} at an appropriate time, before
	 * calling the lookup method.
	 * </p>
	 *
	 * @param macAddress the MAC address to find a entry for
	 * @return if found the entry will be returned, otherwise an empty will be
	 *         returned instead
	 */
	public static Optional<OuiEntry> lookupManufacturerOui(byte[] macAddress) {
		if (!isManufacturerOuiLookupEnabled)
			return Optional.empty();

		if (!OuiTable.OUI_MANUFACTURER_TABLE.isLoaded())
			loadIeeeTables();

		return OuiTable.OUI_MANUFACTURER_TABLE.lookupEntry(macAddress);
	}

	public static boolean isManufacturerOuiLookupEnabled = true;

	public static void enableManufacturerOuiLookup(boolean b) {
		isManufacturerOuiLookupEnabled = b;

		if (!b && OuiTable.OUI_MANUFACTURER_TABLE.isLoaded())
			OuiTable.OUI_MANUFACTURER_TABLE.clear();
	}

	private IeeeOuiAssignments() {
	}

	public static Optional<String> lookupEtherTypeName(int type) {
		String name = switch (type) {
		case 0x0800 -> "IPv4";
		case 0x0806 -> "ARP";
		case 0x0842 -> "WAKE-on-LAN";
		case 0x22F0 -> "AVTP";
		case 0x8137 -> "IPX";
		case 0x86DD -> "IPv6";
		case 0x8847 -> "MPLS";
		case 0x8848 -> "MPLS";
		case 0x8863 -> "PPPoE";
		case 0x8864 -> "PPPoE";

		default -> null;
		};

		return Optional.ofNullable(name);
	}

	public static String resolveMacOuiName(Object obj) {
		throw new UnsupportedOperationException();
	}

	public static String formatPrefixMacWithOuiName(Object obj) {
		return "IeeOui:: format";
	}

	public static String resolveMacOuiDescription(Object obj) {
		return "IeeOui:: resolve";
	}
}
