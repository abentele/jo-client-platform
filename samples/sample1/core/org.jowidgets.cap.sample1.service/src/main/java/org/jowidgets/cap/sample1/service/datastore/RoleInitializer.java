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

package org.jowidgets.cap.sample1.service.datastore;

import java.util.LinkedList;
import java.util.List;

import org.jowidgets.cap.common.api.bean.IBean;
import org.jowidgets.cap.sample1.common.entity.EntityIds;
import org.jowidgets.cap.service.api.CapServiceToolkit;
import org.jowidgets.cap.service.api.bean.IBeanPropertyMap;
import org.jowidgets.cap.service.impl.dummy.datastore.EntityDataFactory;
import org.jowidgets.cap.service.impl.dummy.datastore.EntityDataStore;
import org.jowidgets.cap.service.impl.dummy.datastore.IEntityData;
import org.jowidgets.cap.service.impl.dummy.datastore.IEntityFactory;

public final class RoleInitializer {

	public static final String NAME_PROPERTY = "name";
	public static final String DESCRIPTION_PROPERTY = "description";

	public static final String ADMIN_ROLE = "Admin";
	public static final String USER_ROLE = "User";
	public static final String DEVELOPER_ROLE = "Developer";
	public static final String GUEST_ROLE = "Guest";
	public static final String MANAGER_ROLE = "Manager";

	public static final List<String> ALL_PROPERTIES = new LinkedList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add(NAME_PROPERTY);
			add(DESCRIPTION_PROPERTY);
			add(IBean.ID_PROPERTY);
			add(IBean.VERSION_PROPERTY);
		}
	};

	private RoleInitializer() {}

	public static void initialize() {

		final IEntityData<IBeanPropertyMap> data = EntityDataFactory.create(new IEntityFactory<IBeanPropertyMap>() {

			@Override
			public IBeanPropertyMap createBean(final Long id) {
				final IBeanPropertyMap result = CapServiceToolkit.beanPropertyMap(EntityIds.ROLE);
				result.setId(id);
				return result;
			}

			@Override
			public Class<? extends IBeanPropertyMap> getBeanType() {
				return IBeanPropertyMap.class;
			}
		});

		EntityDataStore.putEntityData(EntityIds.ROLE, data);

		addRole(data, "Admin", "The adminstrators role");
		addRole(data, "User", "The user role");
		addRole(data, "Developer", "The developers role");
		addRole(data, "Guest", "The guest role");
		addRole(data, "Manager", "The managers role");

	}

	private static void addRole(final IEntityData<IBeanPropertyMap> data, final String name, final String description) {
		final IBeanPropertyMap bean = CapServiceToolkit.beanPropertyMap(EntityIds.ROLE);
		bean.setId(data.nextId());
		bean.setValue(NAME_PROPERTY, name);
		bean.setValue(DESCRIPTION_PROPERTY, description);
		data.add(bean);
	}

}
