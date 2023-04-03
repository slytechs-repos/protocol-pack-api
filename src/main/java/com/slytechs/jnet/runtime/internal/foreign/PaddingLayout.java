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
package com.slytechs.jnet.runtime.internal.foreign;

import static java.lang.foreign.ValueLayout.JAVA_BYTE;

import java.lang.foreign.MemoryLayout;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public final class PaddingLayout {

	public static final MemoryLayout PAD_01 = MemoryLayout.sequenceLayout(1, JAVA_BYTE);
	public static final MemoryLayout PAD_02 = MemoryLayout.sequenceLayout(2, JAVA_BYTE);
	public static final MemoryLayout PAD_03 = MemoryLayout.sequenceLayout(3, JAVA_BYTE);
	public static final MemoryLayout PAD_04 = MemoryLayout.sequenceLayout(4, JAVA_BYTE);
	public static final MemoryLayout PAD_05 = MemoryLayout.sequenceLayout(5, JAVA_BYTE);
	public static final MemoryLayout PAD_06 = MemoryLayout.sequenceLayout(6, JAVA_BYTE);
	public static final MemoryLayout PAD_07 = MemoryLayout.sequenceLayout(7, JAVA_BYTE);
	public static final MemoryLayout PAD_08 = MemoryLayout.sequenceLayout(8, JAVA_BYTE);

	private PaddingLayout() {
	}

}
