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
package com.slytechs.jnet.runtime.util;

import java.util.Objects;

/**
 * Semantic Versioning 2.0.0. Implements the specification for Semantic
 * Versioning 2.0.0 found on at the link: https://semver.org.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public class Version implements Comparable<Version> {

	/**
	 * A getter for classes that provide version information.
	 *
	 * @author Sly Technologies
	 * @author repos@slytechs.com
	 */
	public interface HasVersion {

		/**
		 * Gets the version.
		 *
		 * @return the version
		 */
		Version getVersion();

	}

	/**
	 * Check major version.
	 *
	 * @param v1 the v 1
	 * @param v2 the v 2
	 * @throws InvalidVersionException the invalid version exception
	 */
	public static void checkMajorVersion(String v1, String v2) throws InvalidVersionException {
		fromString(v1).checkMajorVersion(fromString(v2));
	}

	/**
	 * Check minor version.
	 *
	 * @param v1 the v 1
	 * @param v2 the v 2
	 * @throws InvalidVersionException the invalid version exception
	 */
	public static void checkMinorVersion(String v1, String v2) throws InvalidVersionException {
		fromString(v1).checkMinorVersion(fromString(v2));
	}

	/**
	 * From string.
	 *
	 * @param v the v
	 * @return the version
	 * @throws InvalidVersionException the invalid version exception
	 */
	public static Version fromString(String v) throws InvalidVersionException {
		String[] c = Objects.requireNonNull(v, "Version.fromString(v)").split("[.]");

//		System.out.println("Version::fromString c.length=" + c.length);

		try {
			int major = Integer.parseInt(c[0]);
			int minor = (c.length > 1) ? Integer.parseInt(c[1]) : 0;
			int maintenance = (c.length > 2) ? Integer.parseInt(c[2]) : 0;
			String build = (c.length > 3) ? c[3] : null;

			return new Version(major, minor, maintenance, build);
		} catch (Throwable e) {
			throw new InvalidVersionException("invalid format [" + v + "]", e);
		}

	}

	/**
	 * Of.
	 *
	 * @param v the v
	 * @return the version
	 */
	public static Version of(String v) {
		try {
			return Version.fromString(v);
		} catch (InvalidVersionException e) {
			throw new IllegalStateException("malformed version string", e);
		}
	}

	/** The major. */
	private int major;

	/** The minor. */
	private int minor;

	/** The maintenance. */
	private int maintenance;

	/** The build. */
	private String build;

	/**
	 * Instantiates a new version.
	 *
	 * @param major the major
	 * @param minor the minor
	 */
	public Version(int major, int minor) {
		this(major, minor, 0, null);
	}

	/**
	 * Instantiates a new version.
	 *
	 * @param major       the major
	 * @param minor       the minor
	 * @param maintenance the maintenance
	 */
	public Version(int major, int minor, int maintenance) {
		this(major, minor, maintenance, null);
	}

	/**
	 * Instantiates a new version.
	 *
	 * @param major       the major
	 * @param minor       the minor
	 * @param maintenance the maintenance
	 * @param build       the build
	 */
	public Version(int major, int minor, int maintenance, String build) {
		this.major = major;
		this.minor = minor;
		this.maintenance = maintenance;
		this.build = build;
	}

	/**
	 * Check major version.
	 *
	 * @param runtimeVersion the runtime version
	 * @throws InvalidVersionException the invalid version exception
	 */
	public void checkMajorVersion(HasVersion runtimeVersion) throws InvalidVersionException {
		checkMajorVersion(runtimeVersion.getVersion());
	}

	/**
	 * Check major version.
	 *
	 * @param runtimeVersion the runtime version
	 * @throws InvalidVersionException the invalid version exception
	 */
	public void checkMajorVersion(Version runtimeVersion) throws InvalidVersionException {
		if (runtimeVersion.major < this.major) {
			throw new InvalidVersionException("runtime major version incompatible " + runtimeVersion + " with " + this);
		}
	}

	/**
	 * Check minor version.
	 *
	 * @param runtimeVersion the runtime version
	 * @throws InvalidVersionException the invalid version exception
	 */
	public void checkMinorVersion(HasVersion runtimeVersion) throws InvalidVersionException {
		checkMinorVersion(runtimeVersion.getVersion());
	}

	/**
	 * Check minor version.
	 *
	 * @param runtimeVersion the runtime version
	 * @throws InvalidVersionException the invalid version exception
	 */
	public void checkMinorVersion(Version runtimeVersion) throws InvalidVersionException {
		if (runtimeVersion.major < this.major) {
			throw new InvalidVersionException("runtime major version incompatible " + runtimeVersion + " with " + this);
		}

		if (runtimeVersion.minor < this.minor) {
			throw new InvalidVersionException("runtime minor version incompatible" + runtimeVersion + " with " + this);
		}
	}

	/**
	 * Compare to.
	 *
	 * @param o the o
	 * @return the int
	 */
	public int compareTo(String o) {
		return toString().compareTo(o);
	}

	/**
	 * Compare to.
	 *
	 * @param o the o
	 * @return the int
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Version o) {
		return compareTo(o.toString());
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "" + major
				+ "." + minor
				+ "." + maintenance
				+ (build == null ? "" : "." + build);
	}

}
