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

package org.jowidgets.cap.ui.impl;

import java.util.List;

import org.jowidgets.cap.common.api.bean.IBeanDtoDescriptor;
import org.jowidgets.cap.common.api.bean.IBeanFormInfoDescriptor;
import org.jowidgets.cap.common.api.service.IEntityService;
import org.jowidgets.cap.ui.api.CapUiToolkit;
import org.jowidgets.cap.ui.api.attribute.IAttribute;
import org.jowidgets.service.api.ServiceProvider;

final class EntityServiceHelper {

	private EntityServiceHelper() {}

	static List<IAttribute<Object>> createAttributes(final Object entityId) {
		try {
			return CapUiToolkit.attributeToolkit().createAttributes(entityId);
		}
		catch (final Exception e) {
			return null;
		}
	}

	static Class<?> getBeanType(final Object entityId) {
		final IBeanDtoDescriptor dtoDescriptor = getDtoDescriptor(entityId);
		if (dtoDescriptor != null) {
			return dtoDescriptor.getBeanType();
		}
		return null;
	}

	static Object getBeanTypeId(final Object entityId) {
		final IBeanDtoDescriptor dtoDescriptor = getDtoDescriptor(entityId);
		if (dtoDescriptor != null) {
			return dtoDescriptor.getBeanTypeId();
		}
		return null;
	}

	static IBeanDtoDescriptor getDtoDescriptor(final Object entityId) {
		if (entityId != null) {
			final IEntityService entityService = ServiceProvider.getService(IEntityService.ID);
			if (entityService != null) {
				return entityService.getDescriptor(entityId);
			}
		}
		return null;
	}

	static IBeanFormInfoDescriptor getCreateFormInfo(final Object entityId) {
		final IBeanDtoDescriptor dtoDescriptor = getDtoDescriptor(entityId);
		if (dtoDescriptor != null) {
			return dtoDescriptor.getCreateModeFormInfo();
		}
		return null;
	}

	static IBeanFormInfoDescriptor getEditFormInfo(final Object entityId) {
		final IBeanDtoDescriptor dtoDescriptor = getDtoDescriptor(entityId);
		if (dtoDescriptor != null) {
			return dtoDescriptor.getEditModeFormInfo();
		}
		return null;
	}
}
