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

package org.jowidgets.cap.service.impl.dummy.datastore;

import java.util.HashMap;
import java.util.Map;

import org.jowidgets.cap.common.api.bean.IBean;
import org.jowidgets.util.Assert;

@SuppressWarnings({"rawtypes", "unchecked"})
final class EntityDataStoreImpl implements IEntityDataStore {

	private final Map dataMap;

	EntityDataStoreImpl() {
		super();
		this.dataMap = new HashMap();
	}

	@Override
	public void putEntityData(final Object entityTypeId, final IEntityData<? extends IBean> data) {
		Assert.paramNotNull(entityTypeId, "entityTypeId");
		Assert.paramNotNull(data, "data");
		this.dataMap.put(entityTypeId, data);
	}

	@Override
	public IEntityData<? extends IBean> getEntityData(final Object entityTypeId) {
		Assert.paramNotNull(entityTypeId, "entityTypeId");
		return (IEntityData<? extends IBean>) dataMap.get(entityTypeId);
	}
}
