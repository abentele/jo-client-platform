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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.jowidgets.cap.common.api.CapCommonToolkit;
import org.jowidgets.cap.common.api.bean.IBeanDto;
import org.jowidgets.cap.common.api.bean.IBeanKey;
import org.jowidgets.cap.common.api.bean.IBeanKeyBuilder;
import org.jowidgets.cap.ui.api.bean.IBeanKeyFactory;
import org.jowidgets.cap.ui.api.bean.IBeanProxy;
import org.jowidgets.util.Assert;

final class BeanKeyFactoryImpl implements IBeanKeyFactory {

	BeanKeyFactoryImpl() {}

	@Override
	public List<IBeanKey> createKeys(final Collection<? extends IBeanProxy<?>> beanProxies) {
		Assert.paramNotNull(beanProxies, "beanProxies");

		final List<IBeanKey> result = new LinkedList<IBeanKey>();
		for (final IBeanProxy<?> beanProxy : beanProxies) {
			result.add(createKey(beanProxy));
		}

		return result;
	}

	@Override
	public IBeanKey createKey(final IBeanProxy<?> beanProxy) {
		final IBeanKeyBuilder builder = CapCommonToolkit.beanKeyBuilder();
		return builder.setId(beanProxy.getId()).setVersion(beanProxy.getVersion()).build();
	}

	@Override
	public List<IBeanKey> createKeysFromDtos(final Collection<? extends IBeanDto> beanDtos) {
		Assert.paramNotNull(beanDtos, "beanDtos");

		final List<IBeanKey> result = new LinkedList<IBeanKey>();
		for (final IBeanDto dto : beanDtos) {
			result.add(createKeyFromDto(dto));
		}

		return result;
	}

	@Override
	public IBeanKey createKeyFromDto(final IBeanDto beanDto) {
		final IBeanKeyBuilder builder = CapCommonToolkit.beanKeyBuilder();
		return builder.setId(beanDto.getId()).setVersion(beanDto.getVersion()).build();
	}

}
