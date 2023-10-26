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
package com.slytechs.protocol.runtime.internal.foreign;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentScope;
import java.lang.invoke.MethodHandle;

/**
 * The Class ForeignUpcall.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author mark
 * @param <T> the generic type
 */
public class ForeignUpcall<T> {

	private static final Linker C_LINKER = Linker.nativeLinker();

	private final String message; // Stub error handler
	private final Throwable cause; // Stub error handler
	private final MethodHandle handle;
	private final FunctionDescriptor descriptor;

	ForeignUpcall(MethodHandle handle, FunctionDescriptor descriptor) {
		this.handle = handle;
		this.descriptor = descriptor;
		this.message = null;
		this.cause = null;
	}

	ForeignUpcall(String message, Throwable cause) {
		this.message = message;
		this.cause = cause;
		this.handle = null;
		this.descriptor = null;
	}

	private void throwIfErrors() {
		if (cause != null)
			throw (cause instanceof RuntimeException e)
					? e
					: new RuntimeException(message, cause);
	}

	public MemorySegment virtualStubPointer(T target) {
		return virtualStubPointer(target, SegmentScope.auto());
	}

	public MemorySegment virtualStubPointer(T target, SegmentScope scope) {
		throwIfErrors();

		MethodHandle handle = this.handle.bindTo(target);

		return C_LINKER
				.upcallStub(handle, descriptor, scope);

	}

	public MemorySegment staticStubPointer() {
		return staticStubPointer(SegmentScope.auto());
	}

	public MemorySegment staticStubPointer(SegmentScope scope) {
		throwIfErrors();

		return C_LINKER.upcallStub(handle, descriptor, scope);
	}
}
