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

package org.jowidgets.cap.common.tools.bean;

import java.util.Collection;

import org.jowidgets.cap.common.api.CapCommonToolkit;
import org.jowidgets.cap.common.api.bean.IBeanDtoDescriptor;
import org.jowidgets.cap.common.api.bean.IBeanDtoDescriptorBuilder;
import org.jowidgets.cap.common.api.bean.IBeanFormInfoDescriptor;
import org.jowidgets.cap.common.api.bean.IBeanPropertyBluePrint;
import org.jowidgets.cap.common.api.bean.IProperty;
import org.jowidgets.cap.common.api.sort.ISort;
import org.jowidgets.cap.common.api.validation.IBeanValidator;
import org.jowidgets.i18n.api.IMessage;
import org.jowidgets.util.Assert;

public class BeanDtoDescriptorBuilder implements IBeanDtoDescriptorBuilder {

	private final IBeanDtoDescriptorBuilder builder;

	public BeanDtoDescriptorBuilder(final Object beanTypeId, final Class<?> beanType) {
		this(CapCommonToolkit.dtoDescriptorBuilder(beanTypeId, beanType));
	}

	public BeanDtoDescriptorBuilder(final Object beanTypeId) {
		this(CapCommonToolkit.dtoDescriptorBuilder(beanTypeId));
	}

	public BeanDtoDescriptorBuilder(final Class<?> beanType) {
		this(CapCommonToolkit.dtoDescriptorBuilder(beanType));
	}

	BeanDtoDescriptorBuilder(final IBeanDtoDescriptorBuilder original) {
		Assert.paramNotNull(original, "original");
		this.builder = original;
	}

	@Override
	public IBeanDtoDescriptorBuilder setLabelSingular(final IMessage label) {
		return this.builder.setLabelSingular(label);
	}

	@Override
	public IBeanDtoDescriptorBuilder setLabelSingular(final String label) {
		return this.builder.setLabelSingular(label);
	}

	@Override
	public IBeanDtoDescriptorBuilder setLabelPlural(final IMessage label) {
		return this.builder.setLabelPlural(label);
	}

	@Override
	public IBeanDtoDescriptorBuilder setLabelPlural(final String label) {
		return this.builder.setLabelPlural(label);
	}

	@Override
	public IBeanDtoDescriptorBuilder setDescription(final IMessage description) {
		return this.builder.setDescription(description);
	}

	@Override
	public IBeanDtoDescriptorBuilder setDescription(final String description) {
		return this.builder.setDescription(description);
	}

	@Override
	public IBeanDtoDescriptorBuilder setCreateFormInfo(final IBeanFormInfoDescriptor info) {
		return this.builder.setCreateFormInfo(info);
	}

	@Override
	public IBeanDtoDescriptorBuilder setCreateFormInfo(final IMessage infoText) {
		return this.builder.setCreateFormInfo(infoText);
	}

	@Override
	public IBeanDtoDescriptorBuilder setCreateFormInfo(final String infoText) {
		return this.builder.setCreateFormInfo(infoText);
	}

	@Override
	public IBeanDtoDescriptorBuilder setEditFormInfo(final IBeanFormInfoDescriptor info) {
		return this.builder.setEditFormInfo(info);
	}

	@Override
	public IBeanDtoDescriptorBuilder setEditFormInfo(final IMessage infoText) {
		return this.builder.setEditFormInfo(infoText);
	}

	@Override
	public IBeanDtoDescriptorBuilder setEditFormInfo(final String infoText) {
		return this.builder.setEditFormInfo(infoText);
	}

	@Override
	public IBeanDtoDescriptorBuilder setFormInfo(final IBeanFormInfoDescriptor info) {
		return this.builder.setFormInfo(info);
	}

	@Override
	public IBeanDtoDescriptorBuilder setFormInfo(final IMessage infoText) {
		return this.builder.setFormInfo(infoText);
	}

	@Override
	public IBeanDtoDescriptorBuilder setFormInfo(final String infoText) {
		return this.builder.setFormInfo(infoText);
	}

	@Override
	public IBeanDtoDescriptorBuilder setRenderingPattern(final IMessage pattern) {
		return this.builder.setRenderingPattern(pattern);
	}

	@Override
	public IBeanDtoDescriptorBuilder setRenderingPattern(final String pattern) {
		return this.builder.setRenderingPattern(pattern);
	}

	@Override
	public IBeanDtoDescriptorBuilder setIconDescriptor(final Object iconDescriptor) {
		return this.builder.setIconDescriptor(iconDescriptor);
	}

	@Override
	public IBeanDtoDescriptorBuilder setCreateIconDescriptor(final Object iconDescriptor) {
		return this.builder.setCreateIconDescriptor(iconDescriptor);
	}

	@Override
	public IBeanDtoDescriptorBuilder setDeleteIconDescriptor(final Object iconDescriptor) {
		return this.builder.setDeleteIconDescriptor(iconDescriptor);
	}

	@Override
	public IBeanDtoDescriptorBuilder setCreateLinkIconDescriptor(final Object iconDescriptor) {
		return this.builder.setCreateLinkIconDescriptor(iconDescriptor);
	}

	@Override
	public IBeanDtoDescriptorBuilder setDeleteLinkIconDescriptor(final Object iconDescriptor) {
		return this.builder.setDeleteLinkIconDescriptor(iconDescriptor);
	}

	@Override
	public IBeanDtoDescriptorBuilder setValidators(final Collection<? extends IBeanValidator<?>> validators) {
		return this.builder.setValidators(validators);
	}

	@Override
	public IBeanDtoDescriptorBuilder setProperties(final Collection<? extends IProperty> properties) {
		return this.builder.setProperties(properties);
	}

	@Override
	public IBeanDtoDescriptorBuilder addValidator(final IBeanValidator<?> validator) {
		return this.builder.addValidator(validator);
	}

	@Override
	public IBeanDtoDescriptorBuilder setDefaultSorting(final ISort... defaultSorting) {
		return this.builder.setDefaultSorting(defaultSorting);
	}

	@Override
	public IBeanDtoDescriptorBuilder setDefaultSorting(final Collection<ISort> defaultSorting) {
		return this.builder.setDefaultSorting(defaultSorting);
	}

	@Override
	public final IBeanPropertyBluePrint addProperty(final String propertyName) {
		return this.builder.addProperty(propertyName);
	}

	@Override
	public final IBeanDtoDescriptor build() {
		return builder.build();
	}

}
