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

package org.jowidgets.cap.service.api.entity;

import org.jowidgets.cap.common.api.service.IBeanServicesProvider;
import org.jowidgets.cap.common.api.service.ICreatorService;
import org.jowidgets.cap.common.api.service.IDeleterService;
import org.jowidgets.cap.common.api.service.IReaderService;
import org.jowidgets.cap.common.api.service.IRefreshService;
import org.jowidgets.cap.common.api.service.IUpdaterService;
import org.jowidgets.cap.service.api.creator.ISyncCreatorService;
import org.jowidgets.cap.service.api.deleter.ISyncDeleterService;
import org.jowidgets.cap.service.api.reader.ISyncReaderService;
import org.jowidgets.cap.service.api.refresh.ISyncRefreshService;
import org.jowidgets.cap.service.api.updater.ISyncUpdaterService;

public interface IBeanServicesProviderBuilder<BEAN_TYPE> {

	IBeanServicesProviderBuilder<BEAN_TYPE> setReaderService(IReaderService<Void> readerService);

	IBeanServicesProviderBuilder<BEAN_TYPE> setReaderService(ISyncReaderService<Void> readerService);

	IBeanServicesProviderBuilder<BEAN_TYPE> setCreatorService(ICreatorService creatorService);

	IBeanServicesProviderBuilder<BEAN_TYPE> setCreatorService(ISyncCreatorService creatorService);

	IBeanServicesProviderBuilder<BEAN_TYPE> setRefreshService(IRefreshService refreshService);

	IBeanServicesProviderBuilder<BEAN_TYPE> setRefreshService(ISyncRefreshService refreshService);

	IBeanServicesProviderBuilder<BEAN_TYPE> setUpdaterService(IUpdaterService updaterService);

	IBeanServicesProviderBuilder<BEAN_TYPE> setUpdaterService(ISyncUpdaterService updaterService);

	IBeanServicesProviderBuilder<BEAN_TYPE> setDeleterService(IDeleterService deleterService);

	IBeanServicesProviderBuilder<BEAN_TYPE> setDeleterService(ISyncDeleterService deleterService);

	IBeanServicesProvider<BEAN_TYPE> build();

}
