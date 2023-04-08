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
package com.slytechs.jnet.runtime.internal.foreign;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.MemorySession;
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

	/** The Constant C_LINKER. */
	private static final Linker C_LINKER = Linker.nativeLinker();

	/** The message. */
	private final String message; // Stub error handler
	
	/** The cause. */
	private final Throwable cause; // Stub error handler
	
	/** The handle. */
	private final MethodHandle handle;
	
	/** The descriptor. */
	private final FunctionDescriptor descriptor;

	/**
	 * Instantiates a new foreign upcall.
	 *
	 * @param handle     the handle
	 * @param descriptor the descriptor
	 */
	ForeignUpcall(MethodHandle handle, FunctionDescriptor descriptor) {
		this.handle = handle;
		this.descriptor = descriptor;
		this.message = null;
		this.cause = null;
	}

	/**
	 * Instantiates a new foreign upcall.
	 *
	 * @param message the message
	 * @param cause   the cause
	 */
	ForeignUpcall(String message, Throwable cause) {
		this.message = message;
		this.cause = cause;
		this.handle = null;
		this.descriptor = null;
	}

	/**
	 * Throw if errors.
	 */
	private void throwIfErrors() {
		if (cause != null)
			throw (cause instanceof RuntimeException e)
					? e
					: new RuntimeException(message, cause);
	}

	/**
	 * Virtual stub pointer.
	 *
	 * @param target the target
	 * @return the memory segment
	 */
	public MemorySegment virtualStubPointer(T target) {
		return virtualStubPointer(target, MemorySession.openImplicit());
	}

	/**
	 * Virtual stub pointer.
	 *
	 * @param target the target
	 * @param scope  the scope
	 * @return the memory segment
	 */
	public MemorySegment virtualStubPointer(T target, MemorySession scope) {
		throwIfErrors();

		MethodHandle handle = this.handle.bindTo(target);

		return C_LINKER
				.upcallStub(handle, descriptor, scope);

	}

	/**
	 * Static stub pointer.
	 *
	 * @return the memory segment
	 */
	public MemorySegment staticStubPointer() {
		return staticStubPointer(MemorySession.openImplicit());
	}

	/**
	 * Static stub pointer.
	 *
	 * @param scope the scope
	 * @return the memory segment
	 */
	public MemorySegment staticStubPointer(MemorySession scope) {
		throwIfErrors();

		return C_LINKER.upcallStub(handle, descriptor, scope);
	}
}
