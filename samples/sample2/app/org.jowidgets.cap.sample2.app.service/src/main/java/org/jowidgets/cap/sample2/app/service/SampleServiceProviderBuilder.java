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

package org.jowidgets.cap.sample2.app.service;

import org.jowidgets.cap.common.api.service.IEntityService;
import org.jowidgets.cap.common.api.service.ILookUpService;
import org.jowidgets.cap.sample2.app.common.service.security.AuthorizationProviderServiceId;
import org.jowidgets.cap.sample2.app.service.entity.SampleEntityServiceBuilder;
import org.jowidgets.cap.sample2.app.service.lookup.CountriesLookUpService;
import org.jowidgets.cap.sample2.app.service.lookup.RolesLookUpService;
import org.jowidgets.cap.sample2.app.service.security.AuthorizationProviderServiceImpl;
import org.jowidgets.cap.service.api.CapServiceToolkit;
import org.jowidgets.cap.service.api.adapter.ISyncLookUpService;
import org.jowidgets.cap.service.jpa.api.IJpaServicesDecoratorProviderBuilder;
import org.jowidgets.cap.service.jpa.api.JpaServiceToolkit;
import org.jowidgets.service.api.IServicesDecoratorProvider;
import org.jowidgets.service.tools.ServiceId;
import org.jowidgets.service.tools.ServiceProviderBuilder;
import org.jowidgets.util.IAdapterFactory;

public class SampleServiceProviderBuilder extends ServiceProviderBuilder {

	public SampleServiceProviderBuilder() {
		super();

		addService(AuthorizationProviderServiceId.ID, new AuthorizationProviderServiceImpl());
		addService(IEntityService.ID, new SampleEntityServiceBuilder(this).build());

		addDBLookUpService(RolesLookUpService.LOOK_UP_ID, new RolesLookUpService());
		addDBLookUpService(CountriesLookUpService.LOOK_UP_ID, new CountriesLookUpService());

		addServiceDecorator(createJpaServiceDecoratorProvider());
	}

	private IServicesDecoratorProvider createJpaServiceDecoratorProvider() {
		final IJpaServicesDecoratorProviderBuilder builder = JpaServiceToolkit.serviceDecoratorProviderBuilder("sample2PersistenceUnit");
		builder.addEntityManagerServices(ILookUpService.class);
		return builder.build();
	}

	private void addDBLookUpService(final Object lookUpId, final ISyncLookUpService lookUpService) {
		final IAdapterFactory<ILookUpService, ISyncLookUpService> adapterFactoryProvider;
		adapterFactoryProvider = CapServiceToolkit.adapterFactoryProvider().lookup();
		final ILookUpService asyncService = adapterFactoryProvider.createAdapter(lookUpService);
		final ServiceId<ILookUpService> serviceId = new ServiceId<ILookUpService>(lookUpId, ILookUpService.class);
		addService(serviceId, asyncService);
	}
}
