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

import java.util.LinkedList;
import java.util.List;

import org.jowidgets.api.command.IAction;
import org.jowidgets.api.command.IActionBuilder;
import org.jowidgets.api.command.ICommand;
import org.jowidgets.api.command.IEnabledChecker;
import org.jowidgets.api.image.IconsSmall;
import org.jowidgets.cap.common.api.execution.IExecutableChecker;
import org.jowidgets.cap.common.api.service.IDeleterService;
import org.jowidgets.cap.ui.api.bean.BeanExceptionConverter;
import org.jowidgets.cap.ui.api.bean.IBeanExceptionConverter;
import org.jowidgets.cap.ui.api.command.IDeleterActionBuilder;
import org.jowidgets.cap.ui.api.execution.BeanMessageStatePolicy;
import org.jowidgets.cap.ui.api.execution.BeanModificationStatePolicy;
import org.jowidgets.cap.ui.api.execution.IExecutionInterceptor;
import org.jowidgets.cap.ui.api.model.DataModelChangeType;
import org.jowidgets.cap.ui.api.model.IBeanListModel;
import org.jowidgets.cap.ui.api.model.IDataModelContextProvider;
import org.jowidgets.cap.ui.api.plugin.IServiceActionDecoratorPlugin;
import org.jowidgets.cap.ui.tools.command.DataModelContextCommandWrapper;
import org.jowidgets.common.types.VirtualKey;
import org.jowidgets.i18n.api.MessageReplacer;
import org.jowidgets.plugin.api.IPluginProperties;
import org.jowidgets.plugin.api.PluginProperties;
import org.jowidgets.plugin.api.PluginProvider;
import org.jowidgets.service.api.IServiceId;
import org.jowidgets.service.api.ServiceProvider;
import org.jowidgets.service.tools.ServiceId;
import org.jowidgets.util.Assert;
import org.jowidgets.util.EmptyCheck;

final class DeleterActionBuilder<BEAN_TYPE> extends AbstractCapActionBuilderImpl<IDeleterActionBuilder<BEAN_TYPE>> implements
		IDeleterActionBuilder<BEAN_TYPE> {

	private final IBeanListModel<BEAN_TYPE> model;
	private final List<IEnabledChecker> enabledCheckers;
	private final List<IExecutableChecker<BEAN_TYPE>> executableCheckers;
	private final List<IExecutionInterceptor<Void>> executionInterceptors;

	private String entityLabelSingular;
	private String entityLabelPlural;
	private IDeleterService deleterService;
	private boolean multiSelection;
	private boolean autoSelection;
	private boolean deletionConfirmDialog;

	private BeanModificationStatePolicy beanModificationStatePolicy;
	private BeanMessageStatePolicy beanMessageStatePolicy;
	private IBeanExceptionConverter exceptionConverter;

	DeleterActionBuilder(final IBeanListModel<BEAN_TYPE> model) {
		checkExhausted();
		Assert.paramNotNull(model, "model");
		this.model = model;
		this.enabledCheckers = new LinkedList<IEnabledChecker>();
		this.executableCheckers = new LinkedList<IExecutableChecker<BEAN_TYPE>>();
		this.executionInterceptors = new LinkedList<IExecutionInterceptor<Void>>();
		this.exceptionConverter = BeanExceptionConverter.get();

		this.multiSelection = true;
		this.autoSelection = true;
		this.deletionConfirmDialog = true;
		this.beanModificationStatePolicy = BeanModificationStatePolicy.ANY_MODIFICATION;
		this.beanMessageStatePolicy = BeanMessageStatePolicy.NO_ERROR_MANDATORY;

		setAccelerator(VirtualKey.DELETE);
		setIcon(IconsSmall.DELETE);
	}

	@Override
	public IDeleterActionBuilder<BEAN_TYPE> setEntityLabelSingular(final String label) {
		checkExhausted();
		this.entityLabelSingular = label;
		return this;
	}

	@Override
	public IDeleterActionBuilder<BEAN_TYPE> setEntityLabelPlural(final String label) {
		checkExhausted();
		this.entityLabelPlural = label;
		return this;
	}

	@Override
	public DeleterActionBuilder<BEAN_TYPE> setDeleterService(final IDeleterService deleterService) {
		checkExhausted();
		Assert.paramNotNull(deleterService, "creatorService");
		this.deleterService = deleterService;
		return this;
	}

	@Override
	public DeleterActionBuilder<BEAN_TYPE> setDeleterService(final IServiceId<IDeleterService> deleterServiceId) {
		checkExhausted();
		Assert.paramNotNull(deleterServiceId, "deleterServiceId");
		final IDeleterService service = ServiceProvider.getService(deleterServiceId);
		if (service == null) {
			throw new IllegalArgumentException("No deleter service found for the id '" + deleterServiceId + "'.");
		}
		return setDeleterService(service);
	}

	@Override
	public DeleterActionBuilder<BEAN_TYPE> setDeleterService(final String deleterServiceId) {
		checkExhausted();
		Assert.paramNotNull(deleterServiceId, "deleterServiceId");
		return setDeleterService(new ServiceId<IDeleterService>(deleterServiceId, IDeleterService.class));
	}

	@Override
	public DeleterActionBuilder<BEAN_TYPE> addEnabledChecker(final IEnabledChecker enabledChecker) {
		checkExhausted();
		Assert.paramNotNull(enabledChecker, "enabledChecker");
		enabledCheckers.add(enabledChecker);
		return this;
	}

	@Override
	public IDeleterActionBuilder<BEAN_TYPE> setMultiSelectionPolicy(final boolean multiSelection) {
		checkExhausted();
		this.multiSelection = multiSelection;
		return this;
	}

	@Override
	public IDeleterActionBuilder<BEAN_TYPE> setMessageStatePolicy(final BeanMessageStatePolicy policy) {
		checkExhausted();
		Assert.paramNotNull(policy, "policy");
		this.beanMessageStatePolicy = policy;
		return this;
	}

	@Override
	public IDeleterActionBuilder<BEAN_TYPE> setModificationStatePolicy(final BeanModificationStatePolicy policy) {
		checkExhausted();
		Assert.paramNotNull(policy, "policy");
		this.beanModificationStatePolicy = policy;
		return this;
	}

	@Override
	public IDeleterActionBuilder<BEAN_TYPE> addExecutableChecker(final IExecutableChecker<BEAN_TYPE> executableChecker) {
		checkExhausted();
		Assert.paramNotNull(executableChecker, "executableChecker");
		executableCheckers.add(executableChecker);
		return this;
	}

	@Override
	public IDeleterActionBuilder<BEAN_TYPE> addExecutionInterceptor(final IExecutionInterceptor<Void> interceptor) {
		checkExhausted();
		Assert.paramNotNull(interceptor, "interceptor");
		executionInterceptors.add(interceptor);
		return this;
	}

	@Override
	public DeleterActionBuilder<BEAN_TYPE> setExceptionConverter(final IBeanExceptionConverter exceptionConverter) {
		checkExhausted();
		Assert.paramNotNull(exceptionConverter, "exceptionConverter");
		this.exceptionConverter = exceptionConverter;
		return this;
	}

	@Override
	public IDeleterActionBuilder<BEAN_TYPE> setAutoSelection(final boolean autoSelection) {
		checkExhausted();
		this.autoSelection = autoSelection;
		return this;
	}

	@Override
	public IDeleterActionBuilder<BEAN_TYPE> setDeletionConfirmDialog(final boolean deletionConfirmDialog) {
		checkExhausted();
		this.deletionConfirmDialog = deletionConfirmDialog;
		return this;
	}

	private void setDefaultTextIfNecessary() {
		if (EmptyCheck.isEmpty(getText())) {
			if (!EmptyCheck.isEmpty(entityLabelSingular)) {
				final String message = Messages.getString("DeleterActionBuilder.delete_single_var");
				setText(MessageReplacer.replace(message, entityLabelSingular));
			}
			else if (!EmptyCheck.isEmpty(entityLabelPlural) && multiSelection) {
				final String message = Messages.getString("DeleterActionBuilder.delete_multi_var");
				setText(MessageReplacer.replace(message, entityLabelPlural));
			}
			else if (!multiSelection) {
				setText(Messages.getString("DeleterActionBuilder.delete_single"));
			}
			else {
				setText(Messages.getString("DeleterActionBuilder.delete_multi"));
			}
		}
	}

	private void setDefaultToolTipTextIfNecessary() {
		if (EmptyCheck.isEmpty(getToolTipText())) {
			if (!multiSelection) {
				setToolTipText(Messages.getString("DeleterActionBuilder.delete_single_tooltip"));
			}
			else {
				setToolTipText(Messages.getString("DeleterActionBuilder.delete_multi_tooltip"));
			}
		}
	}

	@Override
	public IAction doBuild() {
		return decorateActionWithPlugins(buildAction());
	}

	private IAction decorateActionWithPlugins(final IAction action) {
		IAction result = action;
		final IPluginProperties properties = PluginProperties.create(
				IServiceActionDecoratorPlugin.SERVICE_TYPE_PROPERTY_KEY,
				IDeleterService.class);
		final List<IServiceActionDecoratorPlugin> plugins = PluginProvider.getPlugins(
				IServiceActionDecoratorPlugin.ID,
				properties);
		for (final IServiceActionDecoratorPlugin plugin : plugins) {
			result = plugin.decorate(result, deleterService);
			if (result == null) {
				return null;
			}
		}
		return result;
	}

	private IAction buildAction() {
		setDefaultTextIfNecessary();
		setDefaultToolTipTextIfNecessary();

		final BeanDeleterCommand<BEAN_TYPE> command = new BeanDeleterCommand<BEAN_TYPE>(
			model,
			enabledCheckers,
			executableCheckers,
			deleterService,
			executionInterceptors,
			multiSelection,
			beanModificationStatePolicy,
			beanMessageStatePolicy,
			exceptionConverter,
			autoSelection,
			deletionConfirmDialog);

		final IActionBuilder builder = getBuilder();
		if (model instanceof IDataModelContextProvider) {
			builder.setCommand(new DataModelContextCommandWrapper(
				(IDataModelContextProvider) model,
				DataModelChangeType.SELECTION_CHANGE,
				command));
		}
		else {
			builder.setCommand((ICommand) command);
		}
		return builder.build();
	}
}
