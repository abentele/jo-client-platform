/*
 * Copyright (c) 2012, grossmann
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

package org.jowidgets.cap.service.tools;

import java.util.List;

import org.jowidgets.cap.common.api.bean.IBean;
import org.jowidgets.cap.common.api.execution.IExecutableChecker;
import org.jowidgets.cap.common.api.service.IExecutorService;
import org.jowidgets.cap.common.api.service.ILookUpService;
import org.jowidgets.cap.service.api.CapServiceToolkit;
import org.jowidgets.cap.service.api.adapter.ISyncLookUpService;
import org.jowidgets.cap.service.api.bean.IBeanAccess;
import org.jowidgets.cap.service.api.executor.IBeanExecutor;
import org.jowidgets.cap.service.api.executor.IBeanListExecutor;
import org.jowidgets.cap.service.api.executor.IExecutorServiceBuilder;
import org.jowidgets.service.api.IServiceId;
import org.jowidgets.service.tools.ServiceId;
import org.jowidgets.service.tools.ServiceProviderBuilder;
import org.jowidgets.util.IAdapterFactory;

public class CapServiceProviderBuilder extends ServiceProviderBuilder {

	public void addLookUpService(final Object lookUpId, final ISyncLookUpService lookUpService) {
		final IAdapterFactory<ILookUpService, ISyncLookUpService> adapterFactoryProvider;
		adapterFactoryProvider = CapServiceToolkit.adapterFactoryProvider().lookup();
		final ILookUpService asyncService = adapterFactoryProvider.createAdapter(lookUpService);
		final ServiceId<ILookUpService> serviceId = new ServiceId<ILookUpService>(lookUpId, ILookUpService.class);
		addService(serviceId, asyncService);
	}

	public <BEAN_TYPE extends IBean, PARAM_TYPE> void addExecutorService(
		final IServiceId<? extends IExecutorService<? extends PARAM_TYPE>> id,
		final IBeanExecutor<? extends BEAN_TYPE, ? extends PARAM_TYPE> beanExecutor,
		final IBeanAccess<? extends BEAN_TYPE> beanAccess,
		final List<String> propertyNames) {

		addExecutorService(id, beanExecutor, null, beanAccess, propertyNames);
	}

	public <BEAN_TYPE extends IBean, PARAM_TYPE> void addExecutorService(
		final IServiceId<? extends IExecutorService<? extends PARAM_TYPE>> id,
		final IBeanExecutor<? extends BEAN_TYPE, ? extends PARAM_TYPE> beanExecutor,
		final IExecutableChecker<? extends BEAN_TYPE> executableChecker,
		final IBeanAccess<? extends BEAN_TYPE> beanAccess,
		final List<String> propertyNames) {

		final IExecutorServiceBuilder<BEAN_TYPE, PARAM_TYPE> builder = CapServiceToolkit.executorServiceBuilder(beanAccess);
		builder.setExecutor(beanExecutor);
		if (executableChecker != null) {
			builder.setExecutableChecker(executableChecker);
		}
		builder.setBeanDtoFactory(propertyNames);

		addService(id, builder.build());
	}

	public <BEAN_TYPE extends IBean, PARAM_TYPE> void addExecutorService(
		final IServiceId<? extends IExecutorService<? extends PARAM_TYPE>> id,
		final IBeanListExecutor<? extends BEAN_TYPE, ? extends PARAM_TYPE> beanExecutor,
		final IExecutableChecker<? extends BEAN_TYPE> executableChecker,
		final IBeanAccess<? extends BEAN_TYPE> beanAccess,
		final List<String> propertyNames) {

		final IExecutorServiceBuilder<BEAN_TYPE, PARAM_TYPE> builder = CapServiceToolkit.executorServiceBuilder(beanAccess);
		builder.setExecutor(beanExecutor);
		if (executableChecker != null) {
			builder.setExecutableChecker(executableChecker);
		}
		builder.setBeanDtoFactory(propertyNames);

		addService(id, builder.build());
	}

}
