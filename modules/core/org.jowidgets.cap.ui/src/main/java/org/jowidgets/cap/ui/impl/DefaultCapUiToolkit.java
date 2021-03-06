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
import java.util.Collections;

import org.jowidgets.cap.ui.api.ICapUiToolkit;
import org.jowidgets.cap.ui.api.attribute.IAttribute;
import org.jowidgets.cap.ui.api.attribute.IAttributeToolkit;
import org.jowidgets.cap.ui.api.bean.BeanMessageType;
import org.jowidgets.cap.ui.api.bean.IBeanExceptionConverter;
import org.jowidgets.cap.ui.api.bean.IBeanKeyFactory;
import org.jowidgets.cap.ui.api.bean.IBeanMessageBuilder;
import org.jowidgets.cap.ui.api.bean.IBeanMessageFixBuilder;
import org.jowidgets.cap.ui.api.bean.IBeanProxyContext;
import org.jowidgets.cap.ui.api.bean.IBeanProxyFactory;
import org.jowidgets.cap.ui.api.bean.IBeanProxyLabelRenderer;
import org.jowidgets.cap.ui.api.bean.IBeansStateTracker;
import org.jowidgets.cap.ui.api.clipboard.IBeanSelectionClipboardBuilder;
import org.jowidgets.cap.ui.api.clipboard.IBeanSelectionStringRenderer;
import org.jowidgets.cap.ui.api.clipboard.IBeanSelectionTransferableFactoryBuilder;
import org.jowidgets.cap.ui.api.command.ICapActionFactory;
import org.jowidgets.cap.ui.api.control.IDisplayFormatFactory;
import org.jowidgets.cap.ui.api.control.IInputControlProviderBuilder;
import org.jowidgets.cap.ui.api.control.IInputControlSupportBuilder;
import org.jowidgets.cap.ui.api.control.IInputControlSupportRegistry;
import org.jowidgets.cap.ui.api.converter.ICapConverterFactory;
import org.jowidgets.cap.ui.api.decorator.IUiServiceDecoratorProviderFactory;
import org.jowidgets.cap.ui.api.execution.IExecutionTaskFactory;
import org.jowidgets.cap.ui.api.filter.IFilterToolkit;
import org.jowidgets.cap.ui.api.form.IBeanFormToolkit;
import org.jowidgets.cap.ui.api.lookup.ILookUpCache;
import org.jowidgets.cap.ui.api.model.IDataModelContextBuilder;
import org.jowidgets.cap.ui.api.model.ILabelModelBuilder;
import org.jowidgets.cap.ui.api.model.ISingleBeanModelBuilder;
import org.jowidgets.cap.ui.api.sort.ISortModelConfigBuilder;
import org.jowidgets.cap.ui.api.tabfolder.IBeanTabFolderModelBuilder;
import org.jowidgets.cap.ui.api.table.IBeanTableConfigBuilder;
import org.jowidgets.cap.ui.api.table.IBeanTableMenuFactory;
import org.jowidgets.cap.ui.api.table.IBeanTableMenuInterceptor;
import org.jowidgets.cap.ui.api.table.IBeanTableModelBuilder;
import org.jowidgets.cap.ui.api.table.IBeanTableModelConfigBuilder;
import org.jowidgets.cap.ui.api.table.IBeanTableSettingsBuilder;
import org.jowidgets.cap.ui.api.tree.IBeanRelationTreeModelBuilder;
import org.jowidgets.cap.ui.api.types.IEntityTypeId;
import org.jowidgets.cap.ui.api.widgets.ICapApiBluePrintFactory;
import org.jowidgets.cap.ui.api.workbench.ICapWorkbenchToolkit;
import org.jowidgets.cap.ui.impl.workbench.CapWorkbenchToolkitImpl;

public final class DefaultCapUiToolkit implements ICapUiToolkit {

	private ICapApiBluePrintFactory bluePrintFactory;
	private ICapActionFactory actionFactory;
	private ICapConverterFactory converterFactory;
	private IExecutionTaskFactory executionTaskFactory;
	private IBeanKeyFactory beanKeyFactory;
	private IAttributeToolkit attributeToolkit;
	private IFilterToolkit filterToolkit;
	private ICapWorkbenchToolkit workbenchToolkit;
	private IDisplayFormatFactory displayFormatFactory;
	private IInputControlSupportRegistry inputControlRegistry;
	private ILookUpCache lookUpCache;
	private IUiServiceDecoratorProviderFactory uiServiceDecoratorProviderFactory;
	private IBeanExceptionConverter defaultExceptionConverter;

	@SuppressWarnings("rawtypes")
	private IBeanSelectionStringRenderer beanSelectionStringRenderer;

	@Override
	public ICapApiBluePrintFactory bluePrintFactory() {
		if (bluePrintFactory == null) {
			bluePrintFactory = new CapApiBluePrintFactory();
		}
		return bluePrintFactory;
	}

	@Override
	public ICapActionFactory actionFactory() {
		if (actionFactory == null) {
			actionFactory = new CapActionFactoryImpl();
		}
		return actionFactory;
	}

	@Override
	public ICapConverterFactory converterFactory() {
		if (converterFactory == null) {
			converterFactory = new CapConverterFactoryImpl();
		}
		return converterFactory;
	}

	@Override
	public IUiServiceDecoratorProviderFactory serviceDecoratorFactory() {
		if (uiServiceDecoratorProviderFactory == null) {
			uiServiceDecoratorProviderFactory = new UiServiceDecoratorProviderFactory();
		}
		return uiServiceDecoratorProviderFactory;
	}

	@Override
	public <BEAN_TYPE> IBeanTableMenuFactory<BEAN_TYPE> beanTableMenuFactory(
		final Collection<IBeanTableMenuInterceptor<BEAN_TYPE>> interceptors) {
		return new BeanTableMenuFactoryImpl<BEAN_TYPE>(interceptors);
	}

	@Override
	public <BEAN_TYPE> IBeanTableMenuFactory<BEAN_TYPE> beanTableMenuFactory() {
		final Collection<IBeanTableMenuInterceptor<BEAN_TYPE>> interceptors = Collections.emptyList();
		return beanTableMenuFactory(interceptors);
	}

	@Override
	public IDisplayFormatFactory displayFormatFactory() {
		if (displayFormatFactory == null) {
			displayFormatFactory = new DisplayFormatFactoryImpl();
		}
		return displayFormatFactory;
	}

	@Override
	public IInputControlSupportRegistry inputControlRegistry() {
		if (inputControlRegistry == null) {
			inputControlRegistry = new InputControlSupportRegistryImpl();
		}
		return inputControlRegistry;
	}

	@Override
	public <ELEMENT_VALUE_TYPE> IInputControlProviderBuilder<ELEMENT_VALUE_TYPE> inputControlProviderBuilder(
		final Class<ELEMENT_VALUE_TYPE> elementValueType) {
		return new InputControlProviderBuilderImpl<ELEMENT_VALUE_TYPE>(elementValueType);
	}

	@Override
	public <ELEMENT_VALUE_TYPE> IInputControlSupportBuilder<ELEMENT_VALUE_TYPE> inputControlSupportBuilder() {
		return new InputControlSupportBuilderImpl<ELEMENT_VALUE_TYPE>();
	}

	@Override
	public ICapWorkbenchToolkit workbenchToolkit() {
		if (workbenchToolkit == null) {
			workbenchToolkit = new CapWorkbenchToolkitImpl();
		}
		return workbenchToolkit;
	}

	@Override
	public IAttributeToolkit attributeToolkit() {
		if (attributeToolkit == null) {
			attributeToolkit = new AttributeToolkitImpl();
		}
		return attributeToolkit;
	}

	@Override
	public IFilterToolkit filterToolkit() {
		if (filterToolkit == null) {
			filterToolkit = new FilterToolkitImpl();
		}
		return filterToolkit;
	}

	@Override
	public <BEAN_TYPE> IBeansStateTracker<BEAN_TYPE> beansStateTracker() {
		return beansStateTracker(new LocalBeanProxyContext());
	}

	@Override
	public <BEAN_TYPE> IBeansStateTracker<BEAN_TYPE> beansStateTracker(final IBeanProxyContext context) {
		return new BeansStateTrackerImpl<BEAN_TYPE>(context);
	}

	@Override
	public <BEAN_TYPE> IBeanProxyFactory<BEAN_TYPE> beanProxyFactory(
		final Object beanTypeId,
		final Class<? extends BEAN_TYPE> beanType) {
		return new BeanProxyFactoryImpl<BEAN_TYPE>(beanTypeId, beanType);
	}

	@Override
	public IBeanProxyContext beanProxyContext() {
		return new BeanProxyContextImpl();
	}

	@Override
	public IBeanKeyFactory beanKeyFactory() {
		if (beanKeyFactory == null) {
			beanKeyFactory = new BeanKeyFactoryImpl();
		}
		return beanKeyFactory;
	}

	@Override
	public IExecutionTaskFactory executionTaskFactory() {
		if (executionTaskFactory == null) {
			executionTaskFactory = new ExecutionTaskFactory();
		}
		return executionTaskFactory;
	}

	@Override
	public <BEAN_TYPE> IBeanTableModelBuilder<BEAN_TYPE> beanTableModelBuilder(
		final Object entityId,
		final Object beanTypeId,
		final Class<BEAN_TYPE> beanType) {
		return new BeanTableModelBuilderImpl<BEAN_TYPE>(entityId, beanTypeId, beanType);
	}

	@Override
	public <BEAN_TYPE> IBeanTableModelBuilder<BEAN_TYPE> beanTableModelBuilder(final Class<BEAN_TYPE> beanType) {
		return new BeanTableModelBuilderImpl<BEAN_TYPE>(null, null, beanType);
	}

	@Override
	public <BEAN_TYPE> IBeanTableModelBuilder<BEAN_TYPE> beanTableModelBuilder(
		final Object entityId,
		final Class<BEAN_TYPE> beanType) {
		return new BeanTableModelBuilderImpl<BEAN_TYPE>(entityId, null, beanType);
	}

	@Override
	public <BEAN_TYPE> IBeanTableModelBuilder<BEAN_TYPE> beanTableModelBuilder(final Object entityId) {
		return new BeanTableModelBuilderImpl<BEAN_TYPE>(entityId, null, null);
	}

	@Override
	public <CHILD_BEAN_TYPE> IBeanRelationTreeModelBuilder<CHILD_BEAN_TYPE> beanRelationTreeModelBuilder(final Object entityId) {
		return new BeanRelationTreeModelBuilderImpl<CHILD_BEAN_TYPE>(entityId, null, null);
	}

	@Override
	public <CHILD_BEAN_TYPE> IBeanRelationTreeModelBuilder<CHILD_BEAN_TYPE> beanRelationTreeModelBuilder(
		final Class<CHILD_BEAN_TYPE> beanType) {
		return new BeanRelationTreeModelBuilderImpl<CHILD_BEAN_TYPE>(null, null, beanType);
	}

	@Override
	public <CHILD_BEAN_TYPE> IBeanRelationTreeModelBuilder<CHILD_BEAN_TYPE> beanRelationTreeModelBuilder(
		final Object entityId,
		final Class<CHILD_BEAN_TYPE> beanType) {
		return new BeanRelationTreeModelBuilderImpl<CHILD_BEAN_TYPE>(entityId, null, beanType);
	}

	@Override
	public <CHILD_BEAN_TYPE> IBeanRelationTreeModelBuilder<CHILD_BEAN_TYPE> beanRelationTreeModelBuilder(
		final Object entityId,
		final Object beanTypeId,
		final Class<CHILD_BEAN_TYPE> beanType) {
		return new BeanRelationTreeModelBuilderImpl<CHILD_BEAN_TYPE>(beanType, beanTypeId, beanType);
	}

	@Override
	public <BEAN_TYPE> IBeanTabFolderModelBuilder<BEAN_TYPE> beanTabFolderBuilder(final Object entityId) {
		return new BeanTabFolderModelBuilderImpl<BEAN_TYPE>(entityId, null, null);
	}

	@Override
	public <BEAN_TYPE> IBeanTabFolderModelBuilder<BEAN_TYPE> beanTabFolderModelBuilder(final Class<BEAN_TYPE> beanType) {
		return new BeanTabFolderModelBuilderImpl<BEAN_TYPE>(null, null, beanType);
	}

	@Override
	public <BEAN_TYPE> IBeanTabFolderModelBuilder<BEAN_TYPE> beanTabFolderModelBuilder(
		final Object entityId,
		final Class<BEAN_TYPE> beanType) {
		return new BeanTabFolderModelBuilderImpl<BEAN_TYPE>(entityId, null, beanType);
	}

	@Override
	public <BEAN_TYPE> IBeanTabFolderModelBuilder<BEAN_TYPE> beanTabFolderModelBuilder(
		final Object entityId,
		final Object beanTypeId,
		final Class<BEAN_TYPE> beanType) {
		return new BeanTabFolderModelBuilderImpl<BEAN_TYPE>(entityId, beanTypeId, beanType);
	}

	@Override
	public <BEAN_TYPE> ISingleBeanModelBuilder<BEAN_TYPE> singleBeanModelBuilder(
		final Object entityId,
		final Object beanTypeId,
		final Class<BEAN_TYPE> beanType) {
		return new SingleBeanModelBuilder<BEAN_TYPE>(beanType, beanTypeId, beanType);
	}

	@Override
	public <BEAN_TYPE> ISingleBeanModelBuilder<BEAN_TYPE> singleBeanModelBuilder(final Class<BEAN_TYPE> beanType) {
		return new SingleBeanModelBuilder<BEAN_TYPE>(null, null, beanType);
	}

	@Override
	public <BEAN_TYPE> ISingleBeanModelBuilder<BEAN_TYPE> singleBeanModelBuilder(
		final Object entityId,
		final Class<BEAN_TYPE> beanType) {
		return new SingleBeanModelBuilder<BEAN_TYPE>(entityId, null, beanType);
	}

	@Override
	public <BEAN_TYPE> ISingleBeanModelBuilder<BEAN_TYPE> singleBeanModelBuilder(final Object entityId) {
		return new SingleBeanModelBuilder<BEAN_TYPE>(entityId, null, null);
	}

	@Override
	public IBeanTableModelConfigBuilder beanTableModelConfigBuilder() {
		return new BeanTableModelConfigBuilderImpl();
	}

	@Override
	public IBeanTableSettingsBuilder beanTableSettingsBuilder() {
		return new BeanTableSettingsBuilderImpl();
	}

	@Override
	public IBeanTableConfigBuilder beanTableConfigBuilder() {
		return new BeanTableConfigBuilderImpl();
	}

	@Override
	public IBeanMessageBuilder beanMessageBuilder(final BeanMessageType type) {
		return new BeanMessageBuilderImpl(type);
	}

	@Override
	public IBeanMessageFixBuilder beanMessageFixBuilder() {
		return new BeanMessageFixBuilderImpl();
	}

	@Override
	public ISortModelConfigBuilder sortModelConfigBuilder() {
		return new SortModelConfigBuilder();
	}

	@Override
	public IBeanFormToolkit beanFormToolkit() {
		return new BeanFormToolkitImpl();
	}

	@Override
	public <BEAN_TYPE> IBeanProxyLabelRenderer<BEAN_TYPE> beanProxyLabelPatternRenderer(
		final String labelPattern,
		final Collection<? extends IAttribute<?>> attributes) {
		return new BeanProxyLabelPatternRenderer<BEAN_TYPE>(labelPattern, attributes);
	}

	@Override
	public <BEAN_TYPE> IEntityTypeId<BEAN_TYPE> entityTypeId(final Object entityId) {
		return new EntityTypeIdImpl<BEAN_TYPE>(entityId, null, null);
	}

	@Override
	public <BEAN_TYPE> IEntityTypeId<BEAN_TYPE> entityTypeId(final Class<BEAN_TYPE> beanType) {
		return new EntityTypeIdImpl<BEAN_TYPE>(null, null, beanType);
	}

	@Override
	public <BEAN_TYPE> IEntityTypeId<BEAN_TYPE> entityTypeId(final Object entityId, final Class<BEAN_TYPE> beanType) {
		return new EntityTypeIdImpl<BEAN_TYPE>(entityId, null, beanType);
	}

	@Override
	public <BEAN_TYPE> IEntityTypeId<BEAN_TYPE> entityTypeId(
		final Object entityId,
		final Object beanTypeId,
		final Class<BEAN_TYPE> beanType) {
		return new EntityTypeIdImpl<BEAN_TYPE>(entityId, beanTypeId, beanType);
	}

	@Override
	public ILookUpCache lookUpCache() {
		if (lookUpCache == null) {
			lookUpCache = new LookUpCacheImpl();
		}
		return lookUpCache;
	}

	@Override
	public IBeanExceptionConverter defaultExceptionConverter() {
		if (defaultExceptionConverter == null) {
			defaultExceptionConverter = new BeanExceptionConverterImpl();
		}
		return defaultExceptionConverter;
	}

	@Override
	public <BEAN_TYPE> IBeanSelectionTransferableFactoryBuilder<BEAN_TYPE> beanSelectionTransferableFactoryBuilder() {
		return new BeanSelectionTransferableBuilderImpl<BEAN_TYPE>();
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public <BEAN_TYPE> IBeanSelectionStringRenderer<BEAN_TYPE> beanSelectionStringRenderer() {
		if (beanSelectionStringRenderer == null) {
			beanSelectionStringRenderer = new BeanSelectionStringRendererImpl();
		}
		return beanSelectionStringRenderer;
	}

	@Override
	public IBeanSelectionClipboardBuilder beanSelectionClipboardBuilder() {
		return new BeanSelectionClipboardBuilderImpl();
	}

	@Override
	public ILabelModelBuilder labelModelBuilder() {
		return new LabelModelBuilder();
	}

	@Override
	public IDataModelContextBuilder dataModelContextBuilder() {
		return new DataModelContextBuilderImpl();
	}

}
