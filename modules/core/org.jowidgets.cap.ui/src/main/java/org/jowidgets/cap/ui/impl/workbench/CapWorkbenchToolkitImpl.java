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

package org.jowidgets.cap.ui.impl.workbench;

import org.jowidgets.cap.ui.api.workbench.ICapWorkbenchActionsProvider;
import org.jowidgets.cap.ui.api.workbench.ICapWorkbenchMenuFactory;
import org.jowidgets.cap.ui.api.workbench.ICapWorkbenchToolkit;
import org.jowidgets.cap.ui.api.workbench.IEntityComponentFactory;
import org.jowidgets.cap.ui.api.workbench.IEntityComponentNodesFactory;

public final class CapWorkbenchToolkitImpl implements ICapWorkbenchToolkit {

	private IEntityComponentFactory entityComponentFactory;
	private IEntityComponentNodesFactory entityComponentNodesFactory;
	private ICapWorkbenchActionsProvider capWorkbenchActionsProvider;
	private ICapWorkbenchMenuFactory capWorkbenchMenuFactory;

	public CapWorkbenchToolkitImpl() {}

	@Override
	public IEntityComponentFactory entityComponentFactory() {
		if (entityComponentFactory == null) {
			this.entityComponentFactory = new EntityComponentFactoryImpl();
		}
		return entityComponentFactory;
	}

	@Override
	public IEntityComponentNodesFactory entityComponentNodesFactory() {
		if (entityComponentNodesFactory == null) {
			this.entityComponentNodesFactory = new EntityComponentNodesFactoryImpl();
		}
		return entityComponentNodesFactory;
	}

	@Override
	public ICapWorkbenchActionsProvider workbenchActionsProvider() {
		if (capWorkbenchActionsProvider == null) {
			this.capWorkbenchActionsProvider = new CapWorkbenchActionsProviderImpl();
		}
		return capWorkbenchActionsProvider;
	}

	@Override
	public ICapWorkbenchMenuFactory workbenchMenuFactory() {
		if (capWorkbenchMenuFactory == null) {
			capWorkbenchMenuFactory = new CapWorkbenchMenuFactoryImpl();
		}
		return capWorkbenchMenuFactory;
	}

}
