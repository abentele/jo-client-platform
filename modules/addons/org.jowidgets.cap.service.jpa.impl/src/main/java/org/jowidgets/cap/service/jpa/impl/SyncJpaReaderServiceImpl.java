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

package org.jowidgets.cap.service.jpa.impl;

import java.util.Collections;
import java.util.List;

import javax.persistence.Query;

import org.jowidgets.cap.common.api.bean.IBean;
import org.jowidgets.cap.common.api.bean.IBeanDto;
import org.jowidgets.cap.common.api.bean.IBeanKey;
import org.jowidgets.cap.common.api.execution.IExecutionCallback;
import org.jowidgets.cap.common.api.filter.IFilter;
import org.jowidgets.cap.common.api.sort.ISort;
import org.jowidgets.cap.service.api.CapServiceToolkit;
import org.jowidgets.cap.service.api.adapter.ISyncReaderService;
import org.jowidgets.cap.service.api.bean.IBeanDtoFactory;
import org.jowidgets.cap.service.jpa.api.query.IQueryCreator;
import org.jowidgets.cap.service.tools.bean.BeanDtoFactoryHelper;
import org.jowidgets.util.Assert;

final class SyncJpaReaderServiceImpl<PARAM_TYPE> implements ISyncReaderService<PARAM_TYPE> {

	private final IQueryCreator<PARAM_TYPE> queryCreator;
	private final IBeanDtoFactory<IBean> beanFactory;

	@SuppressWarnings({"rawtypes", "unchecked"})
	SyncJpaReaderServiceImpl(final IQueryCreator<PARAM_TYPE> queryCreator, final IBeanDtoFactory beanFactory) {
		Assert.paramNotNull(queryCreator, "queryCreator");
		Assert.paramNotNull(beanFactory, "beanFactory");
		this.queryCreator = queryCreator;
		this.beanFactory = beanFactory;
	}

	@Override
	public List<IBeanDto> read(
		final List<? extends IBeanKey> parentBeanKeys,
		final IFilter filter,
		final List<? extends ISort> sorting,
		final int firstRow,
		final int maxRows,
		final PARAM_TYPE parameter,
		final IExecutionCallback executionCallback) {

		final Query query = queryCreator.createReadQuery(EntityManagerProvider.get(), parentBeanKeys, filter, sorting, parameter);
		query.setFirstResult(firstRow);
		query.setMaxResults(maxRows);

		CapServiceToolkit.checkCanceled(executionCallback);
		@SuppressWarnings("unchecked")
		final List<IBean> result = query.getResultList();
		CapServiceToolkit.checkCanceled(executionCallback);
		if (result != null) {
			return BeanDtoFactoryHelper.createDtos(beanFactory, result);
		}
		return Collections.emptyList();
	}

	@Override
	public Integer count(
		final List<? extends IBeanKey> parentBeanKeys,
		final IFilter filter,
		final PARAM_TYPE parameter,
		final IExecutionCallback executionCallback) {

		final Query query = queryCreator.createCountQuery(EntityManagerProvider.get(), parentBeanKeys, filter, parameter);
		CapServiceToolkit.checkCanceled(executionCallback);
		final Object singleResult = query.getSingleResult();
		CapServiceToolkit.checkCanceled(executionCallback);
		if (singleResult != null) {
			return ((Number) singleResult).intValue();
		}
		else {
			return null;
		}
	}

}
