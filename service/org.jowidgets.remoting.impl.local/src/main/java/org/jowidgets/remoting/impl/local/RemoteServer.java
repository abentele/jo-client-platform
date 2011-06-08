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

package org.jowidgets.remoting.impl.local;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jowidgets.remoting.common.api.ICancelService;
import org.jowidgets.remoting.common.api.IInvocationCallbackService;
import org.jowidgets.remoting.common.api.IMethod;
import org.jowidgets.remoting.common.api.IRemoteMethod;
import org.jowidgets.remoting.common.api.IUserQuestionResultService;
import org.jowidgets.remoting.server.api.IRemoteServer;
import org.jowidgets.remoting.server.api.IRemoteServerRegistry;
import org.jowidgets.util.Assert;

final class RemoteServer implements IRemoteServer, IRemoteServerRegistry {

	private static final RemoteServer INSTANCE = new RemoteServer();

	private final Map<String, IRemoteMethod> methods;
	private ICancelService cancelService;
	private IUserQuestionResultService userQuestionResultService;
	private final Object serverId;

	private RemoteServer() {
		this.methods = new HashMap<String, IRemoteMethod>();
		this.serverId = UUID.randomUUID();
	}

	@Override
	public void register(final String methodName, final IMethod method) {
		Assert.paramNotNull(methodName, "methodName");
		Assert.paramNotNull(method, "method");
		final IRemoteMethod remoteMethod = new IRemoteMethod() {

			@Override
			public void invoke(final Object clientId, final Object invocationId, final Object parameter) {
				method.invoke(clientId, invocationId, parameter);
			}

			@Override
			public Object getServerId() {
				return serverId;
			}
		};
		methods.put(methodName, remoteMethod);
	}

	@Override
	public void register(final ICancelService cancelService) {
		Assert.paramNotNull(cancelService, "cancelService");
		this.cancelService = cancelService;
	}

	@Override
	public void register(final IUserQuestionResultService resultService) {
		Assert.paramNotNull(resultService, "resultService");
		this.userQuestionResultService = resultService;
	}

	@Override
	public IInvocationCallbackService getInvocationCallback(final Object clientId) {
		return RemoteClient.getInstance().getCallbackService();
	}

	IRemoteMethod getMethod(final String methodName) {
		Assert.paramNotNull(methodName, "methodName");
		return methods.get(methodName);
	}

	ICancelService getCancelService() {
		return cancelService;
	}

	IUserQuestionResultService getUserQuestionResultService() {
		return userQuestionResultService;
	}

	public static RemoteServer getInstance() {
		return INSTANCE;
	}

}
