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

package org.jowidgets.cap.ui.api.attribute;

import java.util.Collection;

import org.jowidgets.cap.common.api.bean.Cardinality;
import org.jowidgets.cap.common.api.bean.IValueRange;
import org.jowidgets.cap.ui.api.control.DisplayFormat;
import org.jowidgets.cap.ui.api.control.IDisplayFormat;
import org.jowidgets.common.types.AlignmentHorizontal;
import org.jowidgets.i18n.api.IMessage;
import org.jowidgets.validation.IValidator;

public interface IAttributeBluePrint<ELEMENT_VALUE_TYPE> {

	IAttributeBluePrint<ELEMENT_VALUE_TYPE> setLabel(IMessage label);

	IAttributeBluePrint<ELEMENT_VALUE_TYPE> setLabelLong(IMessage labelLong);

	IAttributeBluePrint<ELEMENT_VALUE_TYPE> setDescription(IMessage description);

	IAttributeBluePrint<ELEMENT_VALUE_TYPE> setLabel(String label);

	IAttributeBluePrint<ELEMENT_VALUE_TYPE> setLabelLong(String labelLong);

	IAttributeBluePrint<ELEMENT_VALUE_TYPE> setDescription(String description);

	IAttributeBluePrint<ELEMENT_VALUE_TYPE> setValueRange(IValueRange valueRange);

	IAttributeBluePrint<ELEMENT_VALUE_TYPE> setValueRange(ELEMENT_VALUE_TYPE... values);

	IAttributeBluePrint<ELEMENT_VALUE_TYPE> setValueRange(Collection<? extends ELEMENT_VALUE_TYPE> values);

	IAttributeBluePrint<ELEMENT_VALUE_TYPE> setValueRange(boolean open, Collection<? extends ELEMENT_VALUE_TYPE> values);

	IAttributeBluePrint<ELEMENT_VALUE_TYPE> setValueRange(boolean open, ELEMENT_VALUE_TYPE... values);

	IAttributeBluePrint<ELEMENT_VALUE_TYPE> setLookUpValueRange(Object lookUpId);

	IAttributeBluePrint<ELEMENT_VALUE_TYPE> setDefaultValue(Object value);

	IAttributeBluePrint<ELEMENT_VALUE_TYPE> setCardinality(Cardinality cardinality);

	IAttributeBluePrint<ELEMENT_VALUE_TYPE> setVisible(boolean visible);

	IAttributeBluePrint<ELEMENT_VALUE_TYPE> setEditable(boolean editable);

	IAttributeBluePrint<ELEMENT_VALUE_TYPE> setBatchEditable(boolean editable);

	IAttributeBluePrint<ELEMENT_VALUE_TYPE> setReadonly(boolean readonly);

	IAttributeBluePrint<ELEMENT_VALUE_TYPE> setMandatory(boolean mandatory);

	IAttributeBluePrint<ELEMENT_VALUE_TYPE> setTableAlignment(AlignmentHorizontal alignment);

	IAttributeBluePrint<ELEMENT_VALUE_TYPE> setTableColumnWidth(int width);

	IAttributeBluePrint<ELEMENT_VALUE_TYPE> setGroup(IAttributeGroup group);

	IAttributeBluePrint<ELEMENT_VALUE_TYPE> setSortable(boolean sortable);

	IAttributeBluePrint<ELEMENT_VALUE_TYPE> setFilterable(boolean filterable);

	IAttributeBluePrint<ELEMENT_VALUE_TYPE> setSearchable(boolean searchable);

	IAttributeBluePrint<ELEMENT_VALUE_TYPE> setLabelDisplayFormat(DisplayFormat displayFormat);

	IAttributeBluePrint<ELEMENT_VALUE_TYPE> addValidator(IValidator<? extends Object> validator);

	IAttributeBluePrint<ELEMENT_VALUE_TYPE> addElementTypeValidator(IValidator<ELEMENT_VALUE_TYPE> validator);

	/**
	 * Adds a bean property validator using bean validation (jsr 303)
	 * 
	 * @param beanType The type of the bean
	 * 
	 * @return This builder
	 */
	IAttributeBluePrint<ELEMENT_VALUE_TYPE> addBeanValidator(Class<?> beanType);

	/**
	 * Sets the display format.
	 * 
	 * If the display format is not set, the first added control panels id will be used.
	 * 
	 * @param displayFormat The display format to set. A control panel with the display format must exist
	 * 
	 * @return This builder
	 */
	IAttributeBluePrint<ELEMENT_VALUE_TYPE> setDisplayFormat(IDisplayFormat displayFormat);

	IAttributeBluePrint<ELEMENT_VALUE_TYPE> addControlPanel(IControlPanelProvider<? extends ELEMENT_VALUE_TYPE> controlPanel);

	IControlPanelProviderBluePrint<ELEMENT_VALUE_TYPE> addControlPanel(IDisplayFormat displayFormat);

	IControlPanelProviderBluePrint<ELEMENT_VALUE_TYPE> setControlPanel();

	IAttributeBluePrint<ELEMENT_VALUE_TYPE> setControlPanels(
		Collection<? extends IControlPanelProvider<? extends ELEMENT_VALUE_TYPE>> controlPanels);

}
