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
package com.slytechs.jnet.protocol;

/**
 * A factory for creating Pack objects.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
class PackFactory {

	/**
	 * The Class LazySupplier.
	 */
	private static class LazySupplier implements PackSupplier {

		/**
		 * Apply.
		 *
		 * @param packId the pack id
		 * @return the pack
		 * @see com.slytechs.jnet.protocol.PackSupplier#apply(int)
		 */
		@Override
		public ProtocolPack apply(int packId) {
			throw new UnsupportedOperationException("not implemented yet");
		}

	}
}
