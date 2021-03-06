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

package org.jowidgets.cap.service.spring;

import java.util.List;

import org.jowidgets.cap.service.api.annotation.Executor;
import org.jowidgets.cap.service.api.annotation.ExecutorBean;
import org.jowidgets.cap.service.api.annotation.Parameter;

@ExecutorBean(User.class)
public class SomeExecutors {

	@Executor(id = "doNothing")
	public void doNothing(final User user) {}

	@Executor(id = "doNothingList")
	public void doNothingList(final List<User> users) {}

	@Executor(id = "changeName")
	public void changeName(final User user, final String newName) {
		user.setName(newName);
	}

	@Executor(id = "changeNameList")
	public void changeNameList(final List<User> users, final String newName) {
		for (final User user : users) {
			user.setName(newName);
		}
	}

	@Executor(id = "changeFirstAndLastName")
	public void changeName(final User user, final String firstName, final String lastName) {
		user.setName(firstName + " " + lastName);
	}

	@Executor(id = "createUser")
	public User createUser(final String name) {
		final User user = new User(1);
		user.setName(name);
		return user;
	}

	@Executor(id = "changeFirstAndLastNameWithComplexParameter")
	public void changeNameWithComplexParameter(
		@Parameter("lastName.toUpperCase()") final String lastName,
		@Parameter("['firstName']") final String firstName,
		final User user,
		@Parameter("['age']") final int age) {
		user.setName(firstName + " " + lastName);
	}

}
