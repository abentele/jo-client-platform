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

import java.util.List;

import org.jowidgets.cap.common.api.bean.Cardinality;
import org.jowidgets.cap.common.api.bean.IValueRange;
import org.jowidgets.cap.common.api.filter.IOperator;
import org.jowidgets.cap.ui.api.control.DisplayFormat;
import org.jowidgets.cap.ui.api.control.IDisplayFormat;
import org.jowidgets.cap.ui.api.filter.IFilterPanelProvider;
import org.jowidgets.cap.ui.api.filter.IFilterType;
import org.jowidgets.common.types.AlignmentHorizontal;
import org.jowidgets.i18n.api.IMessage;
import org.jowidgets.util.event.IChangeObservable;
import org.jowidgets.validation.IValidator;

public interface IAttribute<ELEMENT_VALUE_TYPE> extends IChangeObservable {

	String getPropertyName();

	IValueRange getValueRange();

	Object getDefaultValue();

	boolean isSortable();

	boolean isFilterable();

	boolean isSearchable();

	boolean isMandatory();

	boolean isEditable();

	boolean isBatchEditable();

	boolean isReadonly();

	IAttributeGroup getGroup();

	IMessage getLabel();

	IMessage getLabelLong();

	IMessage getDescription();

	String getCurrentLabel();

	String getValueAsString(Object value);

	DisplayFormat getLabelDisplayFormat();

	Class<?> getValueType();

	Class<ELEMENT_VALUE_TYPE> getElementValueType();

	IValidator<Object> getValidator();

	Cardinality getCardinality();

	boolean isCollectionType();

	List<IControlPanelProvider<ELEMENT_VALUE_TYPE>> getControlPanels();

	List<IFilterType> getSupportedFilterTypes();

	IFilterPanelProvider<IOperator> getFilterPanelProvider(IFilterType filterType);

	IControlPanelProvider<ELEMENT_VALUE_TYPE> getCurrentFilterControlPanel(IFilterType filterType);

	IControlPanelProvider<ELEMENT_VALUE_TYPE> getCurrentIncludingFilterControlPanel(IFilterType filterType);

	IControlPanelProvider<ELEMENT_VALUE_TYPE> getCurrentIncludingFilterControlPanel();

	boolean isVisible();

	IDisplayFormat getDisplayFormat();

	AlignmentHorizontal getTableAlignment();

	IControlPanelProvider<ELEMENT_VALUE_TYPE> getCurrentControlPanel();

	int getTableWidth();

	void setVisible(boolean visible);

	void setLabelDisplayFormat(DisplayFormat displayFormat);

	void setDisplayFormat(IDisplayFormat displayFormat);

	void setTableAlignment(AlignmentHorizontal alignment);

	void setTableWidth(int width);

	IAttributeConfig getConfig();

	void setConfig(IAttributeConfig config);

}
