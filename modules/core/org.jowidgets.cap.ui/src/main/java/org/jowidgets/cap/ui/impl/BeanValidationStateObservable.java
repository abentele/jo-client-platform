/*
 * Copyright (c) 2011, grossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 *   names of its contributors may be used to endorse or promote products
 *   derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL jo-widgets.org BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */

package org.jowidgets.cap.ui.impl;

import org.jowidgets.cap.ui.api.bean.IBeanProxy;
import org.jowidgets.cap.ui.api.bean.IBeanValidationStateListener;
import org.jowidgets.cap.ui.api.bean.IBeanValidationStateObservable;
import org.jowidgets.util.collection.IObserverSet;
import org.jowidgets.util.collection.IObserverSetFactory.Strategy;
import org.jowidgets.util.collection.ObserverSetFactory;

class BeanValidationStateObservable<BEAN_TYPE> implements IBeanValidationStateObservable<BEAN_TYPE> {

	private final IObserverSet<IBeanValidationStateListener<BEAN_TYPE>> listeners;

	BeanValidationStateObservable() {
		this.listeners = ObserverSetFactory.create(Strategy.LOW_MEMORY);
	}

	@Override
	public final void addValidationStateListener(final IBeanValidationStateListener<BEAN_TYPE> listener) {
		listeners.add(listener);
	}

	@Override
	public final void removeValidationStateListener(final IBeanValidationStateListener<BEAN_TYPE> listener) {
		listeners.remove(listener);
	}

	public final void fireValidationStateChanged(final IBeanProxy<BEAN_TYPE> bean) {
		for (final IBeanValidationStateListener<BEAN_TYPE> listener : listeners) {
			listener.validationStateChanged(bean);
		}
	}

	public final void dispose() {
		listeners.clear();
	}
}
