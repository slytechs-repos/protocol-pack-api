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
package com.slytechs.jnet.jnetruntime.internal.concurrent;

import java.util.AbstractQueue;
import java.util.Iterator;

import com.slytechs.jnet.jnetruntime.util.IsExpirable;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 *
 */
public class ExpirationQueue<E extends IsExpirable> extends AbstractQueue<E> {

	/**
	 * @see java.util.Queue#offer(java.lang.Object)
	 */
	@Override
	public boolean offer(E e) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/**
	 * @see java.util.Queue#poll()
	 */
	@Override
	public E poll() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/**
	 * @see java.util.Queue#peek()
	 */
	@Override
	public E peek() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/**
	 * @see java.util.AbstractCollection#iterator()
	 */
	@Override
	public Iterator<E> iterator() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/**
	 * @see java.util.AbstractCollection#size()
	 */
	@Override
	public int size() {
		throw new UnsupportedOperationException("not implemented yet");
	}

}
