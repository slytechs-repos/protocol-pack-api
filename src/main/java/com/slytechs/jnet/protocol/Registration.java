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

import java.io.IOException;

/**
 * A common registration action, that represents some registration that occurred
 * and provides an {@link #unregister()} method to undo that registration. For
 * example, this saves an implementing class of providing a separate combo of
 * addListener/removeListener method, where the addListener implementation can
 * return a {@code Registration} object that provides a way to undo or remove
 * the previously added listener.
 *
 * @author Sly Technologies
 */
public interface Registration {

	/**
	 * Empty.
	 *
	 * @return the registration
	 */
	static Registration empty() {
		return () -> {};
	}

	/**
	 * And then.
	 *
	 * @param after the after
	 * @return the registration
	 */
	default Registration andThen(Registration after) {
		return () -> {
			this.unregister();
			if (after != null)
				after.unregister();
		};
	}

	/**
	 * Log to appendable.
	 *
	 * @param msg the msg
	 * @param out the out
	 * @return the registration
	 */
	default Registration logToAppendable(String msg, Appendable out) {
		return andThen(() -> {
			try {
				out.append(msg);
			} catch (IOException e) {}
		});
	}

	/**
	 * Log to stdout.
	 *
	 * @param msg the msg
	 * @return the registration
	 */
	default Registration logToStdout(String msg) {
		return andThen(logToAppendable(msg, System.out));
	}

	/**
	 * On unregister run action.
	 *
	 * @param action the action
	 * @return the registration
	 */
	default Registration onUnregisterRunAction(Runnable action) {
		return andThen(() -> action.run());
	}

	/**
	 * Unregister.
	 */
	void unregister();
}
