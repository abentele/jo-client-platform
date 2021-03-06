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

package org.jowidgets.cap.common.impl;

import java.io.Serializable;
import java.util.Map;

import org.jowidgets.cap.common.api.bean.IBean;
import org.jowidgets.cap.common.api.bean.IBeanDto;
import org.jowidgets.util.Assert;

class BeanDtoImpl implements IBeanDto, Serializable {

	private static final long serialVersionUID = -7085159195317664441L;

	private final Object id;
	private final String beanTypeId;
	private final Map<String, Object> beanData;

	BeanDtoImpl(final Object id, final long version, final Object beanTypeId, final Map<String, Object> beanData) {
		Assert.paramNotNull(beanTypeId, "beanTypeId");
		Assert.paramNotNull(beanData, "beanData");
		this.id = id;
		if (beanTypeId instanceof Class) {
			this.beanTypeId = ((Class<?>) beanTypeId).getName();
		}
		else {
			this.beanTypeId = beanTypeId.toString();
		}

		this.beanData = beanData;

		beanData.put(IBean.ID_PROPERTY, id);
		beanData.put(IBean.VERSION_PROPERTY, version);
	}

	@Override
	public Object getValue(final String propertyName) {
		Assert.paramNotEmpty(propertyName, "propertyName");
		return beanData.get(propertyName);
	}

	@Override
	public Object getId() {
		return getValue(IBean.ID_PROPERTY);
	}

	@Override
	public long getVersion() {
		final Object result = getValue(IBean.VERSION_PROPERTY);
		if (result instanceof Long) {
			return ((Long) getValue(IBean.VERSION_PROPERTY)).longValue();
		}
		else {
			return 0;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((beanTypeId == null) ? 0 : beanTypeId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final BeanDtoImpl other = (BeanDtoImpl) obj;
		if (beanTypeId == null) {
			if (other.beanTypeId != null) {
				return false;
			}
		}
		else if (!beanTypeId.equals(other.beanTypeId)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		}
		else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "BeanDtoImpl [id=" + id + ", beanTypeId=" + beanTypeId + ", beanData=" + beanData + "]";
	}

}
