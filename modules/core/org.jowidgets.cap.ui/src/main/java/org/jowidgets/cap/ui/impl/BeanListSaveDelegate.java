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
import java.util.Set;

import org.jowidgets.cap.common.api.CapCommonToolkit;
import org.jowidgets.cap.common.api.bean.IBean;
import org.jowidgets.cap.common.api.bean.IBeanData;
import org.jowidgets.cap.common.api.bean.IBeanDataBuilder;
import org.jowidgets.cap.common.api.bean.IBeanDto;
import org.jowidgets.cap.common.api.bean.IBeanKey;
import org.jowidgets.cap.common.api.bean.IBeanModification;
import org.jowidgets.cap.common.api.execution.IResultCallback;
import org.jowidgets.cap.common.api.service.ICreatorService;
import org.jowidgets.cap.common.api.service.IUpdaterService;
import org.jowidgets.cap.ui.api.bean.IBeanExceptionConverter;
import org.jowidgets.cap.ui.api.bean.IBeanProxy;
import org.jowidgets.cap.ui.api.bean.IBeansStateTracker;
import org.jowidgets.cap.ui.api.execution.BeanExecutionPolicy;
import org.jowidgets.cap.ui.api.execution.IExecutionTask;
import org.jowidgets.cap.ui.api.model.IBeanListModel;
import org.jowidgets.i18n.api.IMessage;
import org.jowidgets.util.Assert;
import org.jowidgets.util.EmptyCheck;
import org.jowidgets.util.IProvider;

final class BeanListSaveDelegate<BEAN_TYPE> {

	private static final IMessage SAVE = Messages.getMessage("BeanListSaveDelegate.Save");
	private static final IMessage CREATE = Messages.getMessage("BeanListSaveDelegate.Create");
	private static final IMessage CREATION_FAILED = Messages.getMessage("BeanListSaveDelegate.Save_failed");
	private static final IMessage SAVE_FAILED = Messages.getMessage("BeanListSaveDelegate.Save_failed");

	private final IBeanListModel<BEAN_TYPE> listModel;
	private final IBeansStateTracker<BEAN_TYPE> beansStateTracker;
	private final IBeanExceptionConverter exceptionConverter;
	private final BeanExecutionPolicy beanExecutionPolicy;
	private final IUpdaterService updaterService;
	private final ICreatorService creatorService;
	private final Collection<String> propertyNames;
	private final IProvider<List<IBeanKey>> parentBeansProvider;
	private final boolean fireBeansChanged;

	BeanListSaveDelegate(
		final IBeanListModel<BEAN_TYPE> listModel,
		final IBeansStateTracker<BEAN_TYPE> beansStateTracker,
		final IBeanExceptionConverter exceptionConverter,
		final BeanExecutionPolicy beanExecutionPolicy,
		final IUpdaterService updaterService,
		final ICreatorService creatorService,
		final Collection<String> propertyNames,
		final IProvider<List<IBeanKey>> parentBeansProvider) {
		this(
			listModel,
			beansStateTracker,
			exceptionConverter,
			beanExecutionPolicy,
			updaterService,
			creatorService,
			propertyNames,
			parentBeansProvider,
			true);
	}

	BeanListSaveDelegate(
		final IBeanListModel<BEAN_TYPE> listModel,
		final IBeansStateTracker<BEAN_TYPE> beansStateTracker,
		final IBeanExceptionConverter exceptionConverter,
		final BeanExecutionPolicy beanExecutionPolicy,
		final IUpdaterService updaterService,
		final ICreatorService creatorService,
		final Collection<String> propertyNames,
		final IProvider<List<IBeanKey>> parentBeansProvider,
		final boolean fireBeansChanged) {

		Assert.paramNotNull(listModel, "listModel");
		Assert.paramNotNull(beansStateTracker, "beansStateTracker");
		Assert.paramNotNull(exceptionConverter, "exceptionConverter");
		Assert.paramNotNull(beanExecutionPolicy, "beanExecutionPolicy");
		Assert.paramNotNull(propertyNames, "propertyNames");

		this.listModel = listModel;
		this.beansStateTracker = beansStateTracker;
		this.exceptionConverter = exceptionConverter;
		this.beanExecutionPolicy = beanExecutionPolicy;
		this.updaterService = updaterService;
		this.creatorService = creatorService;
		this.propertyNames = new LinkedList<String>(propertyNames);
		this.parentBeansProvider = parentBeansProvider;
		this.fireBeansChanged = fireBeansChanged;
	}

	void save() {
		create();
		update();
	}

	private void create() {
		final Set<IBeanProxy<BEAN_TYPE>> beansToCreate = beansStateTracker.getBeansToCreate();
		if (!EmptyCheck.isEmpty(beansToCreate)) {
			if (creatorService == null) {
				throw new IllegalStateException("No creator service set. Could not create transient beans");
			}

			final BeanListExecutionHelper<BEAN_TYPE> executionHelper = new BeanListExecutionHelper<BEAN_TYPE>(
				CREATION_FAILED.get(),
				listModel,
				beansToCreate,
				beanExecutionPolicy,
				exceptionConverter,
				true,
				fireBeansChanged);

			for (final List<IBeanProxy<BEAN_TYPE>> preparedBeans : executionHelper.prepareExecutions(true)) {
				if (preparedBeans.size() > 0) {
					final IExecutionTask executionTask = preparedBeans.get(0).getExecutionTask();
					if (executionTask != null) {
						executionTask.setDescription(CREATE.get());

						final List<IBeanData> beansData = new LinkedList<IBeanData>();
						for (final IBeanProxy<BEAN_TYPE> bean : preparedBeans) {
							beansData.add(createBeanData(bean));
						}
						final IResultCallback<List<IBeanDto>> helperCallback = executionHelper.createResultCallback(preparedBeans);
						creatorService.create(helperCallback, parentBeansProvider.get(), beansData, executionTask);
					}
				}
			}
		}
	}

	private IBeanData createBeanData(final IBeanProxy<BEAN_TYPE> bean) {
		final IBeanDataBuilder builder = CapCommonToolkit.beanDataBuilder();
		for (final String propertyName : propertyNames) {
			if (propertyName != IBean.ID_PROPERTY && !IBeanProxy.ALL_META_ATTRIBUTES.contains(propertyName)) {
				builder.setProperty(propertyName, bean.getValue(propertyName));
			}
		}
		return builder.build();
	}

	private void update() {
		final Set<IBeanProxy<BEAN_TYPE>> modifiedBeans = beansStateTracker.getMasterBeansToUpdate();
		if (!EmptyCheck.isEmpty(modifiedBeans)) {
			if (updaterService == null) {
				throw new IllegalStateException("No updater service set. Can not update modified beans");
			}

			final BeanListExecutionHelper<BEAN_TYPE> executionHelper = new BeanListExecutionHelper<BEAN_TYPE>(
				SAVE_FAILED.get(),
				listModel,
				modifiedBeans,
				beanExecutionPolicy,
				exceptionConverter,
				false,
				fireBeansChanged);

			for (final List<IBeanProxy<BEAN_TYPE>> preparedBeans : executionHelper.prepareExecutions(false)) {
				if (preparedBeans.size() > 0) {
					final IExecutionTask executionTask = preparedBeans.get(0).getExecutionTask();
					if (executionTask != null) {
						executionTask.setDescription(SAVE.get());
						final List<IBeanModification> modifications = new LinkedList<IBeanModification>();
						for (final IBeanProxy<?> bean : preparedBeans) {
							modifications.addAll(bean.getModifications());
						}
						final IResultCallback<List<IBeanDto>> helperCallback = executionHelper.createResultCallback(preparedBeans);
						updaterService.update(helperCallback, modifications, executionTask);
					}
				}
			}
		}
	}

}
