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

import org.jowidgets.cap.common.api.bean.IBeanDtoDescriptor;
import org.jowidgets.cap.common.api.service.IEntityService;
import org.jowidgets.cap.ui.api.CapUiToolkit;
import org.jowidgets.cap.ui.api.command.ICapActionFactory;
import org.jowidgets.cap.ui.api.command.IDeleterActionBuilder;
import org.jowidgets.cap.ui.api.image.ImageResolver;
import org.jowidgets.cap.ui.api.table.IBeanTableModel;
import org.jowidgets.cap.ui.api.widgets.IBeanTable;
import org.jowidgets.common.image.IImageProvider;
import org.jowidgets.service.api.ServiceProvider;

final class BeanTableDeleterActionBuilderFactory {

	private BeanTableDeleterActionBuilderFactory() {}

	static <BEAN_TYPE> IDeleterActionBuilder<BEAN_TYPE> createBuilder(final IBeanTable<BEAN_TYPE> table) {
		final IBeanTableModel<BEAN_TYPE> model = table.getModel();
		final ICapActionFactory actionFactory = CapUiToolkit.actionFactory();
		final IDeleterActionBuilder<BEAN_TYPE> builder = actionFactory.deleterActionBuilder(model);
		builder.setEntityLabelSingular(model.getEntityLabelSingular());
		builder.setEntityLabelPlural(model.getEntityLabelPlural());
		builder.setDeleterService(model.getDeleterService());
		final Object entityId = model.getEntityId();
		if (entityId != null) {
			final IEntityService entityService = ServiceProvider.getService(IEntityService.ID);
			if (entityService != null) {
				final IBeanDtoDescriptor descriptor = entityService.getDescriptor(entityId);
				if (descriptor != null) {
					final Object icon = descriptor.getDeleteIconDescriptor();
					if (icon != null) {
						final IImageProvider imageProvider = ImageResolver.resolve(icon);
						if (imageProvider != null) {
							builder.setIcon(imageProvider);
						}
					}
				}
			}
		}

		return builder;
	}

}
