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

package org.jowidgets.cap.ui.api.command;

import java.util.List;

import org.jowidgets.api.command.IEnabledChecker;
import org.jowidgets.api.widgets.content.IInputContentCreator;
import org.jowidgets.api.widgets.descriptor.IInputDialogDescriptor;
import org.jowidgets.cap.common.api.bean.IBeanDto;
import org.jowidgets.cap.common.api.execution.IExecutableChecker;
import org.jowidgets.cap.common.api.service.IExecutorService;
import org.jowidgets.cap.common.api.service.IParameterProviderService;
import org.jowidgets.cap.ui.api.bean.IBeanExceptionConverter;
import org.jowidgets.cap.ui.api.bean.IBeanProxy;
import org.jowidgets.cap.ui.api.execution.BeanExecutionPolicy;
import org.jowidgets.cap.ui.api.execution.BeanMessageStatePolicy;
import org.jowidgets.cap.ui.api.execution.BeanModificationStatePolicy;
import org.jowidgets.cap.ui.api.execution.BeanSelectionPolicy;
import org.jowidgets.cap.ui.api.execution.IExecutionInterceptor;
import org.jowidgets.cap.ui.api.execution.IExecutor;
import org.jowidgets.cap.ui.api.execution.IParameterProvider;
import org.jowidgets.service.api.IServiceId;
import org.jowidgets.util.IFilter;

public interface IExecutorActionBuilder<BEAN_TYPE, PARAM_TYPE> extends
		ICapActionBuilder<IExecutorActionBuilder<BEAN_TYPE, PARAM_TYPE>> {

	IExecutorActionBuilder<BEAN_TYPE, PARAM_TYPE> setDefaultParameter(PARAM_TYPE defaultParameter);

	IExecutorActionBuilder<BEAN_TYPE, PARAM_TYPE> addParameterProvider(IInputContentCreator<PARAM_TYPE> inputContentCreator);

	IExecutorActionBuilder<BEAN_TYPE, PARAM_TYPE> addParameterProvider(IInputDialogDescriptor<PARAM_TYPE> inputDialogDescriptor);

	IExecutorActionBuilder<BEAN_TYPE, PARAM_TYPE> addParameterProvider(IParameterProvider<BEAN_TYPE, PARAM_TYPE> parameterProvider);

	IExecutorActionBuilder<BEAN_TYPE, PARAM_TYPE> addParameterProvider(
		IParameterProviderService<PARAM_TYPE> parameterProviderService);

	IExecutorActionBuilder<BEAN_TYPE, PARAM_TYPE> addParameterProvider(
		IServiceId<IParameterProviderService<PARAM_TYPE>> parameterProviderService);

	IExecutorActionBuilder<BEAN_TYPE, PARAM_TYPE> addSelectionFilter(IFilter<IBeanProxy<BEAN_TYPE>> filter);

	IExecutorActionBuilder<BEAN_TYPE, PARAM_TYPE> setExecutor(IExecutor<BEAN_TYPE, PARAM_TYPE> executor);

	IExecutorActionBuilder<BEAN_TYPE, PARAM_TYPE> setExecutor(IExecutorService<PARAM_TYPE> excecuterService);

	IExecutorActionBuilder<BEAN_TYPE, PARAM_TYPE> setExecutor(IServiceId<IExecutorService<PARAM_TYPE>> excecuterServiceId);

	IExecutorActionBuilder<BEAN_TYPE, PARAM_TYPE> setExecutor(String excecuterServiceId);

	IExecutorActionBuilder<BEAN_TYPE, PARAM_TYPE> setExecutionPolicy(BeanExecutionPolicy policy);

	IExecutorActionBuilder<BEAN_TYPE, PARAM_TYPE> setSelectionPolicy(BeanSelectionPolicy policy);

	IExecutorActionBuilder<BEAN_TYPE, PARAM_TYPE> setModificationPolicy(BeanModificationStatePolicy policy);

	IExecutorActionBuilder<BEAN_TYPE, PARAM_TYPE> setMessageStatePolicy(BeanMessageStatePolicy policy);

	IExecutorActionBuilder<BEAN_TYPE, PARAM_TYPE> addEnabledChecker(IEnabledChecker enabledChecker);

	IExecutorActionBuilder<BEAN_TYPE, PARAM_TYPE> addExecutableChecker(IExecutableChecker<BEAN_TYPE> executableChecker);

	IExecutorActionBuilder<BEAN_TYPE, PARAM_TYPE> addExecutionInterceptor(IExecutionInterceptor<List<IBeanDto>> interceptor);

	IExecutorActionBuilder<BEAN_TYPE, PARAM_TYPE> setExceptionConverter(IBeanExceptionConverter exceptionConverter);

}
