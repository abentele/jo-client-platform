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

package org.jowidgets.cap.service.impl;

import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;

import org.jowidgets.cap.service.api.decorator.IExecutionInterceptor;
import org.jowidgets.service.api.IServiceId;
import org.jowidgets.service.api.IServicesDecoratorProvider;
import org.jowidgets.util.Assert;
import org.jowidgets.util.IDecorator;

final class AsyncDecoratorProvider implements IServicesDecoratorProvider {

	private final Executor executor;
	private final ScheduledExecutorService scheduledExecutorService;
	private final Long executionCallbackDelay;
	private final IExecutionInterceptor<Object> executionInterceptor;
	private final int order;

	AsyncDecoratorProvider(
		final Executor executor,
		final ScheduledExecutorService scheduledExecutorService,
		final Long executionCallbackDelay,
		final IExecutionInterceptor<Object> executionInterceptor,
		final int order) {
		Assert.paramNotNull(executor, "executor");
		Assert.paramNotNull(scheduledExecutorService, "scheduledExecutorService");
		this.executor = executor;
		this.scheduledExecutorService = scheduledExecutorService;
		this.executionCallbackDelay = executionCallbackDelay;
		this.executionInterceptor = executionInterceptor;
		this.order = order;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <SERVICE_TYPE> IDecorator<SERVICE_TYPE> getDecorator(final IServiceId<SERVICE_TYPE> id) {
		Assert.paramNotNull(id, "id");
		return (IDecorator<SERVICE_TYPE>) new GenericServiceAsyncDecorator(
			id.getServiceType(),
			executor,
			scheduledExecutorService,
			executionCallbackDelay,
			executionInterceptor);
	}

	@Override
	public int getOrder() {
		return order;
	}

}
