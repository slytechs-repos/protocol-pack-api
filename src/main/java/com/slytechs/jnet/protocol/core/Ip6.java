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
package com.slytechs.jnet.protocol.core;

import com.slytechs.jnet.protocol.constants.CoreHeaderInfo;

/**
 * 
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public class Ip6 extends Ip<Ip6Option> {

	public static final int ID = CoreHeaderInfo.CORE_ID_IPv6;

	/**
	 * @param id
	 */
	public Ip6() {
		super(ID);
	}

	public int dsfield() {
		return Ip6Layout.DSFIELD.getInt(buffer());
	}

	public void dsfield(int dsfield) {
		Ip6Layout.DSFIELD.setInt(dsfield, buffer());
	}

	@Override
	public byte[] dst() {
		return Ip6Layout.DST_BYTES.getByteArray(buffer());
	}

	@Override
	public IpAddress dstAddress() {
		return new Ip6Address(dst());
	}

	public int dstAsInt(int index) {
		return Ip6Layout.DST_AS_INT.getInt(buffer(), index);
	}

	public void dstAsInt(int dst, int index) {
		Ip6Layout.DST_AS_INT.setInt(dst, buffer(), index);
	}

	public long dstAsLong(int index) {
		return Ip6Layout.DST_AS_LONG.getInt(buffer(), index);
	}

	public void dstAsLong(long dst, int index) {
		Ip6Layout.DST_AS_LONG.setLong(dst, buffer(), index);
	}

	public int flow() {
		return Ip6Layout.FLOW.getInt(buffer());
	}

	public void flow(int flow) {
		Ip6Layout.FLOW.setInt(flow, buffer());
	}

	public int hopLimit() {
		return Ip6Layout.HOP_LIMIT.getInt(buffer());
	}

	public void hopLimit(int hopLimit) {
		Ip6Layout.HOP_LIMIT.setInt(hopLimit, buffer());
	}

	public int next() {
		return Ip6Layout.NEXT.getInt(buffer());
	}

	public void next(int next) {
		Ip6Layout.NEXT.setInt(next, buffer());
	}

	@Override
	public int payloadLength() {
		return Ip6Layout.PAYLOAD_LENGTH.getInt(buffer());
	}

	public void payloadLength(int payloadLength) {
		Ip6Layout.PAYLOAD_LENGTH.setInt(payloadLength, buffer());
	}

	@Override
	public byte[] src() {
		return Ip6Layout.SRC_BYTES.getByteArray(buffer());
	}

	@Override
	public IpAddress srcGetAsAddress() {
		return new Ip6Address(src());
	}

	public int srcAsInt(int index) {
		return Ip6Layout.SRC_AS_INT.getInt(buffer(), index);
	}

	public void srcAsInt(int src, int index) {
		Ip6Layout.DST_AS_INT.setInt(src, buffer(), index);
	}

	public long srcAsLong(int index) {
		return Ip6Layout.SRC_AS_LONG.getLong(buffer(), index);
	}

	public void srcAsLong(long src, int index) {
		Ip6Layout.DST_AS_LONG.setLong(src, buffer(), index);
	}

	/**
	 * @see com.slytechs.jnet.runtime.protocol.core.Ip#version()
	 */
	@Override
	public int version() {
		return Ip6Layout.VERSION.getInt(buffer());
	}
}
