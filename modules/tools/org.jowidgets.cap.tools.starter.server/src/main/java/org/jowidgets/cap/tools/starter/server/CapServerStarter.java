/*
 * Copyright (c) 2011, H.Westphal
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

package org.jowidgets.cap.tools.starter.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.FilterMapping;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.jowidgets.cap.remoting.common.RemotingBrokerId;
import org.jowidgets.message.api.IExceptionCallback;
import org.jowidgets.message.api.MessageToolkit;
import org.jowidgets.security.impl.http.server.BasicAuthenticationFilter;
import org.jowidgets.security.impl.http.server.SecurityRemotingServlet;
import org.jowidgets.util.Assert;

public final class CapServerStarter {

	private static final int DEFAULT_PORT = 8080;

	private CapServerStarter() {}

	public static void startServer() throws Exception {
		startServer(RemotingBrokerId.DEFAULT_BROKER_ID, DEFAULT_PORT);
	}

	public static void startServer(final int port) throws Exception {
		startServer(RemotingBrokerId.DEFAULT_BROKER_ID, port);
	}

	public static void startServer(final String brokerId) throws Exception {
		startServer(brokerId, DEFAULT_PORT);
	}

	public static void startServer(final String brokerId, final int port) throws Exception {
		Assert.paramNotNull(brokerId, "brokerId");
		MessageToolkit.addExceptionCallback(brokerId, new IExceptionCallback() {
			@Override
			public void exception(final Throwable throwable) {
				//CHECKSTYLE:OFF
				throwable.printStackTrace();
				//CHECKSTYLE:ON
			}
		});
		final Server server = new Server(port);
		final ServletContextHandler root = new ServletContextHandler(ServletContextHandler.SESSIONS);
		root.addServlet(new ServletHolder(new SecurityRemotingServlet(brokerId)), "/");
		root.addFilter(new FilterHolder(new BasicAuthenticationFilter()), "/", FilterMapping.DEFAULT);
		server.setHandler(root);
		server.start();
		server.join();
	}
}
