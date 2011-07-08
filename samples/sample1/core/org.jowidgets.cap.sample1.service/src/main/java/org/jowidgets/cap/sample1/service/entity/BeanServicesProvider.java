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

package org.jowidgets.cap.sample1.service.entity;

import java.util.List;

import org.jowidgets.cap.common.api.bean.IBean;
import org.jowidgets.cap.common.api.service.IBeanServicesProvider;
import org.jowidgets.cap.common.api.service.IEntityService;
import org.jowidgets.cap.sample1.service.creator.SyncCreatorService;
import org.jowidgets.cap.sample1.service.datastore.AbstractData;
import org.jowidgets.cap.sample1.service.deleter.SyncDeleterService;
import org.jowidgets.cap.sample1.service.reader.SyncReaderService;
import org.jowidgets.cap.service.api.CapServiceToolkit;
import org.jowidgets.cap.service.api.entity.IBeanServicesProviderBuilder;
import org.jowidgets.cap.service.api.refresh.IRefreshServiceBuilder;
import org.jowidgets.cap.service.api.updater.IUpdaterServiceBuilder;
import org.jowidgets.service.api.IServiceId;
import org.jowidgets.service.api.IServiceRegistry;

public final class BeanServicesProvider<BEAN_TYPE extends IBean> {

	private final IBeanServicesProvider<BEAN_TYPE> beanServicesProvider;

	public BeanServicesProvider(
		final IServiceRegistry registry,
		final AbstractData<? extends BEAN_TYPE> data,
		final List<String> properties) {
		this(registry, IEntityService.ID, data, properties);
	}

	public BeanServicesProvider(
		final IServiceRegistry registry,
		final IServiceId<IEntityService> entityServiceId,
		final AbstractData<? extends BEAN_TYPE> data,
		final List<String> properties) {

		final IBeanServicesProviderBuilder<BEAN_TYPE> builder = CapServiceToolkit.beanServicesProviderBuilder(
				registry,
				entityServiceId,
				data.getBeanType());

		//reader service
		builder.setReaderService(new SyncReaderService<BEAN_TYPE>(data, properties));

		//creator service
		builder.setCreatorService(new SyncCreatorService<BEAN_TYPE>(data, properties));

		//deleter service
		builder.setDeleterService(new SyncDeleterService(data));

		//updater service
		final IUpdaterServiceBuilder<BEAN_TYPE> updaterBuilder = CapServiceToolkit.updaterServiceBuilder(data);
		updaterBuilder.setPropertyNames(properties);
		builder.setUpdaterService(updaterBuilder.build());

		//refresh service
		final IRefreshServiceBuilder<BEAN_TYPE> refreshBuilder = CapServiceToolkit.refreshServiceBuilder(data);
		refreshBuilder.setPropertyNames(properties);
		builder.setRefreshService(refreshBuilder.build());

		this.beanServicesProvider = builder.build();
	}

	public IBeanServicesProvider<BEAN_TYPE> getServices() {
		return beanServicesProvider;
	}

}