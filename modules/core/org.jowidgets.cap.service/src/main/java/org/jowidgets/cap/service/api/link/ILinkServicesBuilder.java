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

package org.jowidgets.cap.service.api.link;

import java.util.Collection;

import org.jowidgets.cap.common.api.bean.IBean;
import org.jowidgets.cap.common.api.entity.IEntityLinkProperties;
import org.jowidgets.cap.common.api.service.ICreatorService;
import org.jowidgets.cap.common.api.service.IDeleterService;
import org.jowidgets.cap.common.api.service.ILinkCreatorService;
import org.jowidgets.cap.common.api.service.ILinkDeleterService;
import org.jowidgets.cap.common.api.service.IReaderService;
import org.jowidgets.cap.service.api.bean.IBeanAccess;
import org.jowidgets.cap.service.api.bean.IBeanDtoFactory;

public interface ILinkServicesBuilder<SOURCE_BEAN_TYPE extends IBean, LINKED_BEAN_TYPE extends IBean> {

	ILinkServicesBuilder<SOURCE_BEAN_TYPE, LINKED_BEAN_TYPE> setSourceBeanAccess(IBeanAccess<SOURCE_BEAN_TYPE> beanAccess);

	ILinkServicesBuilder<SOURCE_BEAN_TYPE, LINKED_BEAN_TYPE> setSourceDtoFactory(IBeanDtoFactory<SOURCE_BEAN_TYPE> dtoFactory);

	ILinkServicesBuilder<SOURCE_BEAN_TYPE, LINKED_BEAN_TYPE> setSourceDtoFactory(
		Class<? extends SOURCE_BEAN_TYPE> beanType,
		Collection<String> propertyNames);

	ILinkServicesBuilder<SOURCE_BEAN_TYPE, LINKED_BEAN_TYPE> setLinkBeanType(Class<? extends IBean> beanType);

	ILinkServicesBuilder<SOURCE_BEAN_TYPE, LINKED_BEAN_TYPE> setLinkedBeanAccess(IBeanAccess<LINKED_BEAN_TYPE> beanAccess);

	ILinkServicesBuilder<SOURCE_BEAN_TYPE, LINKED_BEAN_TYPE> setLinkedDtoFactory(IBeanDtoFactory<LINKED_BEAN_TYPE> dtoFactory);

	ILinkServicesBuilder<SOURCE_BEAN_TYPE, LINKED_BEAN_TYPE> setLinkedDtoFactory(
		Class<? extends LINKED_BEAN_TYPE> beanType,
		Collection<String> propertyNames);

	ILinkServicesBuilder<SOURCE_BEAN_TYPE, LINKED_BEAN_TYPE> setSourceCreatorService(ICreatorService creatorService);

	ILinkServicesBuilder<SOURCE_BEAN_TYPE, LINKED_BEAN_TYPE> setSourceDeleterService(IDeleterService deleterService);

	ILinkServicesBuilder<SOURCE_BEAN_TYPE, LINKED_BEAN_TYPE> setAllLinksReaderService(IReaderService<Void> readerService);

	ILinkServicesBuilder<SOURCE_BEAN_TYPE, LINKED_BEAN_TYPE> setLinkCreatorService(ICreatorService creatorService);

	ILinkServicesBuilder<SOURCE_BEAN_TYPE, LINKED_BEAN_TYPE> setLinkDeleterService(IDeleterService deleterService);

	ILinkServicesBuilder<SOURCE_BEAN_TYPE, LINKED_BEAN_TYPE> setLinkableReaderService(IReaderService<?> readerService);

	ILinkServicesBuilder<SOURCE_BEAN_TYPE, LINKED_BEAN_TYPE> setLinkableCreatorService(ICreatorService creatorService);

	ILinkServicesBuilder<SOURCE_BEAN_TYPE, LINKED_BEAN_TYPE> setLinkableDeleterService(IDeleterService creatorService);

	/**
	 * Sets the symmetric property, the default a 'false'
	 * 
	 * The link is symmetric if
	 * 'a' is linked with 'b' implies 'b' is linked with 'a'
	 * 
	 * If the link will be set to symmetric, the default deleter service will also try to delete
	 * the reverse link, e.g. if 'a' should be unlinked with 'b', 'b' will also be unlinked from 'a'.
	 * 
	 * @param symmetric If true the links is symmetric, false otherwise
	 * 
	 * @return This builder
	 */
	ILinkServicesBuilder<SOURCE_BEAN_TYPE, LINKED_BEAN_TYPE> setSymmetric(boolean symmetric);

	ILinkServicesBuilder<SOURCE_BEAN_TYPE, LINKED_BEAN_TYPE> setSourceProperties(IEntityLinkProperties properties);

	ILinkServicesBuilder<SOURCE_BEAN_TYPE, LINKED_BEAN_TYPE> setSourceProperties(
		String keyPropertyName,
		String foreignKeyPropertyname);

	ILinkServicesBuilder<SOURCE_BEAN_TYPE, LINKED_BEAN_TYPE> setSourceProperties(String foreignKeyPropertyName);

	ILinkServicesBuilder<SOURCE_BEAN_TYPE, LINKED_BEAN_TYPE> setDestinationProperties(IEntityLinkProperties properties);

	ILinkServicesBuilder<SOURCE_BEAN_TYPE, LINKED_BEAN_TYPE> setDestinationProperties(
		String keyPropertyName,
		String foreignKeyPropertyname);

	ILinkServicesBuilder<SOURCE_BEAN_TYPE, LINKED_BEAN_TYPE> setDestinationProperties(String foreignKeyPropertyName);

	ILinkCreatorService buildCreatorService();

	/**
	 * Tries to build the creator service. If this is not possible, null will be returned;
	 * 
	 * @return The creator service or null
	 */
	ILinkCreatorService tryBuildCreatorService();

	ILinkDeleterService buildDeleterService();

	/**
	 * Tries to build the deleter service. If this is not possible, null will be returned;
	 * 
	 * @return The deleter service or null
	 */
	ILinkDeleterService tryBuildDeleterService();

}
