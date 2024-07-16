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

import com.slytechs.jnet.jnetruntime.pipeline.Pipeline;
import com.slytechs.jnet.protocol.core.TcpIpProtocolFamily.A;
import com.slytechs.jnet.protocol.core.TcpIpProtocolFamily.B;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 *
 */
public class TcpIpProtocolFamily extends Pipeline<A, B> {

	/**
	 * @param priority
	 * @param processor
	 * @param inputType
	 * @param outputType
	 */
	public TcpIpProtocolFamily(int priority) {
		super(priority, null, null, null);
	}

	public interface A {
		
	}
	
	public interface B {
		
	}
}
