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

package org.jowidgets.cap.ui.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.jowidgets.api.convert.IObjectLabelConverter;
import org.jowidgets.api.convert.IStringObjectConverter;
import org.jowidgets.api.widgets.IInputControl;
import org.jowidgets.cap.common.api.CapCommonToolkit;
import org.jowidgets.cap.common.api.bean.Cardinality;
import org.jowidgets.cap.common.api.bean.IProperty;
import org.jowidgets.cap.common.api.bean.IPropertyValidatorBuilder;
import org.jowidgets.cap.common.api.bean.IValueRange;
import org.jowidgets.cap.common.api.lookup.ILookUpValueRange;
import org.jowidgets.cap.ui.api.CapUiToolkit;
import org.jowidgets.cap.ui.api.attribute.IAttribute;
import org.jowidgets.cap.ui.api.attribute.IAttributeBluePrint;
import org.jowidgets.cap.ui.api.attribute.IAttributeBuilder;
import org.jowidgets.cap.ui.api.attribute.IAttributeGroup;
import org.jowidgets.cap.ui.api.attribute.IControlPanelProvider;
import org.jowidgets.cap.ui.api.attribute.IControlPanelProviderBluePrint;
import org.jowidgets.cap.ui.api.attribute.IControlPanelProviderBuilder;
import org.jowidgets.cap.ui.api.control.DisplayFormat;
import org.jowidgets.cap.ui.api.control.IDisplayFormat;
import org.jowidgets.cap.ui.api.control.IInputControlProvider;
import org.jowidgets.cap.ui.api.control.IInputControlSupport;
import org.jowidgets.common.types.AlignmentHorizontal;
import org.jowidgets.common.widgets.factory.ICustomWidgetCreator;
import org.jowidgets.i18n.api.IMessage;
import org.jowidgets.i18n.tools.StaticMessage;
import org.jowidgets.util.Assert;
import org.jowidgets.validation.IValidator;

final class AttributeBuilderImpl<ELEMENT_VALUE_TYPE> implements IAttributeBuilder<ELEMENT_VALUE_TYPE> {

	private final IPropertyValidatorBuilder propertyValidatorBuilder;

	private String propertyName;
	private IValueRange valueRange;
	private Object defaultValue;
	private IMessage label;
	private IMessage labelLong;
	private IMessage description;
	private DisplayFormat labelDisplayFormat;
	private boolean visible;
	private boolean mandatory;
	private boolean editable;
	private boolean batchEditable;
	private boolean readonly;
	private AlignmentHorizontal tableAlignment;
	private int tableColumnWidth;
	private IAttributeGroup attributeGroup;
	private boolean sortable;
	private boolean filterable;
	private Boolean searchable;
	private Class<?> valueType;
	private Class<? extends ELEMENT_VALUE_TYPE> elementValueType;
	private IValidator<Object> validator;
	private boolean validatorAdded;
	private Cardinality cardinality;
	private IDisplayFormat displayFormat;

	@SuppressWarnings("rawtypes")
	private List controlPanels;

	AttributeBuilderImpl(final Class<? extends ELEMENT_VALUE_TYPE> elementValueType) {
		this();
		Assert.paramNotNull(elementValueType, "elementValueType");
		if (Collection.class.isAssignableFrom(elementValueType)) {
			throw new IllegalArgumentException("The parameter '" + elementValueType + "' must not be a 'Collection'");
		}
		this.elementValueType = elementValueType;
		this.valueType = elementValueType;
	}

	AttributeBuilderImpl(final Class<?> valueType, final Class<? extends ELEMENT_VALUE_TYPE> elementValueType) {
		this();
		Assert.paramNotNull(valueType, "valueType");
		Assert.paramNotNull(elementValueType, "elementValueType");
		this.valueType = elementValueType;
		this.elementValueType = elementValueType;
	}

	AttributeBuilderImpl(final IAttribute<ELEMENT_VALUE_TYPE> attribute) {
		this();
		Assert.paramNotNull(attribute, "attribute");
		this.propertyName = attribute.getPropertyName();
		this.valueRange = attribute.getValueRange();
		this.defaultValue = attribute.getDefaultValue();
		this.label = attribute.getLabel();
		this.labelLong = attribute.getLabelLong();
		this.labelDisplayFormat = attribute.getLabelDisplayFormat();
		this.description = attribute.getDescription();
		this.visible = attribute.isVisible();
		this.mandatory = attribute.isMandatory();
		this.editable = attribute.isEditable();
		this.batchEditable = attribute.isBatchEditable();
		this.readonly = attribute.isReadonly();
		this.tableAlignment = attribute.getTableAlignment();
		this.tableColumnWidth = attribute.getTableWidth();
		this.attributeGroup = attribute.getGroup();
		this.sortable = attribute.isSortable();
		this.filterable = attribute.isFilterable();
		this.searchable = attribute.isSearchable();
		this.valueType = attribute.getValueType();
		this.elementValueType = attribute.getElementValueType();
		this.validator = attribute.getValidator();
		this.cardinality = attribute.getCardinality();
		this.displayFormat = attribute.getDisplayFormat();
		this.controlPanels = attribute.getControlPanels();
	}

	@SuppressWarnings("unchecked")
	public AttributeBuilderImpl(final IProperty property) {
		this();
		Assert.paramNotNull(property, "property");
		this.valueType = property.getValueType();
		this.elementValueType = (Class<? extends ELEMENT_VALUE_TYPE>) property.getElementValueType();
		this.validator = property.getValidator();
		this.cardinality = property.getCardinality();
		this.propertyName = property.getName();
		this.valueRange = property.getValueRange();
		this.defaultValue = property.getDefaultValue();
		this.label = property.getLabelDefault();
		this.labelLong = property.getLabelLongDefault();
		this.description = property.getDescriptionDefault();
		this.visible = property.isVisibleDefault();
		this.mandatory = property.isMandatoryDefault();
		this.editable = property.isEditable();
		this.batchEditable = property.isBatchEditable();
		this.readonly = property.isReadonly();
		this.sortable = property.isSortable();
		this.filterable = property.isFilterable();
		this.searchable = property.isSearchable();
	}

	private AttributeBuilderImpl() {
		this.propertyValidatorBuilder = CapCommonToolkit.propertyValidatorBuilder();
		this.controlPanels = new LinkedList<IControlPanelProvider<? extends ELEMENT_VALUE_TYPE>>();
		this.valueRange = CapCommonToolkit.staticValueRangeFactory().create();
		this.labelDisplayFormat = DisplayFormat.SHORT;
		this.visible = true;
		this.mandatory = false;
		this.editable = true;
		this.batchEditable = true;
		this.readonly = false;
		this.tableAlignment = AlignmentHorizontal.LEFT;
		this.tableColumnWidth = 100;
		this.sortable = true;
		this.filterable = true;
		this.validatorAdded = false;
	}

	IAttributeBuilder<ELEMENT_VALUE_TYPE> setElementValueType(final Class<? extends ELEMENT_VALUE_TYPE> elementValueType) {
		this.elementValueType = elementValueType;
		return this;
	}

	@Override
	public IAttributeBuilder<ELEMENT_VALUE_TYPE> setPropertyName(final String propertyName) {
		Assert.paramNotEmpty(propertyName, "propertyName");
		this.propertyName = propertyName;
		return this;
	}

	@Override
	public IAttributeBluePrint<ELEMENT_VALUE_TYPE> setValueRange(final IValueRange valueRange) {
		Assert.paramNotNull(valueRange, "valueRange");
		this.valueRange = valueRange;
		return this;
	}

	@Override
	public IAttributeBluePrint<ELEMENT_VALUE_TYPE> setValueRange(
		final boolean open,
		final Collection<? extends ELEMENT_VALUE_TYPE> values) {
		Assert.paramNotNull(values, "values");
		return setValueRange(CapCommonToolkit.staticValueRangeFactory().create(values, open));
	}

	@Override
	public IAttributeBluePrint<ELEMENT_VALUE_TYPE> setValueRange(final boolean open, final ELEMENT_VALUE_TYPE... values) {
		Assert.paramNotNull(values, "values");
		return setValueRange(CapCommonToolkit.staticValueRangeFactory().create(open, values));
	}

	@Override
	public IAttributeBluePrint<ELEMENT_VALUE_TYPE> setLookUpValueRange(final Object lookUpId) {
		return setValueRange(CapCommonToolkit.lookUpToolkit().lookUpValueRange(lookUpId));
	}

	@Override
	public IAttributeBluePrint<ELEMENT_VALUE_TYPE> setValueRange(final Collection<? extends ELEMENT_VALUE_TYPE> values) {
		return setValueRange(false, values);
	}

	@Override
	public IAttributeBluePrint<ELEMENT_VALUE_TYPE> setValueRange(final ELEMENT_VALUE_TYPE... values) {
		Assert.paramNotNull(values, "values");
		return setValueRange(Arrays.asList(values));
	}

	@Override
	public IAttributeBluePrint<ELEMENT_VALUE_TYPE> setDefaultValue(final Object value) {
		this.defaultValue = value;
		return this;
	}

	@Override
	public IAttributeBluePrint<ELEMENT_VALUE_TYPE> setCardinality(final Cardinality cardinality) {
		this.cardinality = cardinality;
		return this;
	}

	@Override
	public IAttributeBluePrint<ELEMENT_VALUE_TYPE> setLabel(final IMessage label) {
		Assert.paramNotNull(label, "label");
		this.label = label;
		return this;
	}

	@Override
	public IAttributeBuilder<ELEMENT_VALUE_TYPE> setLabel(final String label) {
		Assert.paramNotEmpty(label, "label");
		this.label = new StaticMessage(label);
		return this;
	}

	@Override
	public IAttributeBluePrint<ELEMENT_VALUE_TYPE> setLabelLong(final IMessage labelLong) {
		this.labelLong = labelLong;
		return this;
	}

	@Override
	public IAttributeBuilder<ELEMENT_VALUE_TYPE> setLabelLong(final String labelLong) {
		this.labelLong = new StaticMessage(labelLong);
		return this;
	}

	@Override
	public IAttributeBluePrint<ELEMENT_VALUE_TYPE> setDescription(final IMessage description) {
		this.description = description;
		return this;
	}

	@Override
	public IAttributeBuilder<ELEMENT_VALUE_TYPE> setDescription(final String description) {
		this.description = new StaticMessage(description);
		return this;
	}

	@Override
	public IAttributeBluePrint<ELEMENT_VALUE_TYPE> setLabelDisplayFormat(final DisplayFormat displayFormat) {
		this.labelDisplayFormat = displayFormat;
		return this;
	}

	@Override
	public IAttributeBuilder<ELEMENT_VALUE_TYPE> setVisible(final boolean visible) {
		this.visible = visible;
		return this;
	}

	@Override
	public IAttributeBuilder<ELEMENT_VALUE_TYPE> setMandatory(final boolean mandatory) {
		this.mandatory = mandatory;
		return this;
	}

	@Override
	public IAttributeBuilder<ELEMENT_VALUE_TYPE> setEditable(final boolean editable) {
		this.editable = editable;
		return this;
	}

	@Override
	public IAttributeBluePrint<ELEMENT_VALUE_TYPE> setBatchEditable(final boolean editable) {
		this.batchEditable = editable;
		return this;
	}

	@Override
	public IAttributeBuilder<ELEMENT_VALUE_TYPE> setReadonly(final boolean readonly) {
		this.readonly = readonly;
		return this;
	}

	@Override
	public IAttributeBuilder<ELEMENT_VALUE_TYPE> setTableAlignment(final AlignmentHorizontal tableAlignment) {
		Assert.paramNotNull(tableAlignment, "tableAlignment");
		this.tableAlignment = tableAlignment;
		return this;
	}

	@Override
	public IAttributeBuilder<ELEMENT_VALUE_TYPE> setTableColumnWidth(final int tableColumnWidth) {
		this.tableColumnWidth = tableColumnWidth;
		return this;
	}

	@Override
	public IAttributeBuilder<ELEMENT_VALUE_TYPE> setGroup(final IAttributeGroup group) {
		this.attributeGroup = group;
		return this;
	}

	@Override
	public IAttributeBuilder<ELEMENT_VALUE_TYPE> setSortable(final boolean sortable) {
		this.sortable = sortable;
		return this;
	}

	@Override
	public IAttributeBuilder<ELEMENT_VALUE_TYPE> setFilterable(final boolean filterable) {
		this.filterable = filterable;
		return this;
	}

	@Override
	public IAttributeBluePrint<ELEMENT_VALUE_TYPE> setSearchable(final boolean searchable) {
		this.searchable = Boolean.valueOf(searchable);
		return this;
	}

	@Override
	public IAttributeBluePrint<ELEMENT_VALUE_TYPE> addValidator(final IValidator<? extends Object> validator) {
		Assert.paramNotNull(validator, "validator");
		this.propertyValidatorBuilder.addValidator(validator);
		return this;
	}

	@Override
	public IAttributeBluePrint<ELEMENT_VALUE_TYPE> addElementTypeValidator(final IValidator<ELEMENT_VALUE_TYPE> validator) {
		Assert.paramNotNull(validator, "validator");
		this.propertyValidatorBuilder.addElementTypeValidator(validator);
		return this;
	}

	@Override
	public IAttributeBluePrint<ELEMENT_VALUE_TYPE> addBeanValidator(final Class<?> beanType) {
		Assert.paramNotNull(beanType, "beanType");
		this.propertyValidatorBuilder.addBeanValidator(beanType, propertyName);
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public IAttributeBuilder<ELEMENT_VALUE_TYPE> addControlPanel(
		final IControlPanelProvider<? extends ELEMENT_VALUE_TYPE> controlPanel) {
		Assert.paramNotNull(controlPanel, "controlPanel");
		this.controlPanels.add(controlPanel);
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public IControlPanelProviderBluePrint<ELEMENT_VALUE_TYPE> addControlPanel(final IDisplayFormat displayFormat) {
		Assert.paramNotNull(displayFormat, "displayFormat");
		final ControlPanelProviderBluePrintImpl<ELEMENT_VALUE_TYPE> result = new ControlPanelProviderBluePrintImpl<ELEMENT_VALUE_TYPE>(
			displayFormat);
		controlPanels.add(result);
		return result;
	}

	@Override
	public IControlPanelProviderBluePrint<ELEMENT_VALUE_TYPE> setControlPanel() {
		this.controlPanels.clear();
		return addControlPanel(new DisplayFormatImpl(
			ControlPanelProviderBuilderImpl.DEFAULT_DISPLAY_FORMAT_ID,
			ControlPanelProviderBuilderImpl.DEFAULT_DISPLAY_NAME,
			null));
	}

	@SuppressWarnings("unchecked")
	@Override
	public IAttributeBluePrint<ELEMENT_VALUE_TYPE> setControlPanels(
		final Collection<? extends IControlPanelProvider<? extends ELEMENT_VALUE_TYPE>> controlPanels) {
		this.controlPanels.clear();
		if (controlPanels != null) {
			this.controlPanels.addAll(controlPanels);
		}
		this.displayFormat = null;
		return this;
	}

	@Override
	public IAttributeBluePrint<ELEMENT_VALUE_TYPE> setDisplayFormat(final IDisplayFormat displayFormat) {
		this.displayFormat = displayFormat;
		return this;
	}

	boolean isEditable() {
		return editable;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private List getControlPanels() {
		if (controlPanels.isEmpty()) {
			final IInputControlSupport<ELEMENT_VALUE_TYPE> controlSupport;

			if (valueRange instanceof ILookUpValueRange) {
				controlSupport = CapUiToolkit.inputControlRegistry().getControls((ILookUpValueRange) valueRange);
			}
			else {
				controlSupport = CapUiToolkit.inputControlRegistry().getControls(elementValueType);
			}

			if (controlSupport != null) {
				for (final IInputControlProvider<ELEMENT_VALUE_TYPE> controlProvider : controlSupport.getControls()) {
					final IObjectLabelConverter<ELEMENT_VALUE_TYPE> objectLabelConverter = controlProvider.getObjectLabelConverter(valueRange);
					final IStringObjectConverter<ELEMENT_VALUE_TYPE> stringObjectConverter = controlProvider.getStringObjectConverter(valueRange);
					final ICustomWidgetCreator<IInputControl<ELEMENT_VALUE_TYPE>> elementControlCreator = controlProvider.getControlCreator(
							objectLabelConverter,
							stringObjectConverter,
							valueRange);
					final ICustomWidgetCreator<IInputControl<? extends Collection<ELEMENT_VALUE_TYPE>>> collectionControlCreator;
					collectionControlCreator = controlProvider.getCollectionControlCreator(
							elementControlCreator,
							objectLabelConverter,
							stringObjectConverter,
							valueRange);

					final IControlPanelProviderBuilder<ELEMENT_VALUE_TYPE> builder = createControlPanelProviderBuilder();
					builder.setDisplayFormat(controlProvider.getDisplayFormat());
					builder.setObjectLabelConverter(objectLabelConverter);
					builder.setStringObjectConverter(stringObjectConverter);
					builder.setControlCreator(elementControlCreator);
					builder.setFilterCollectionControlCreator(collectionControlCreator);
					if (Collection.class.isAssignableFrom(valueType)
						&& Cardinality.LESS_OR_EQUAL_ONE == cardinality
						&& collectionControlCreator != null
						&& elementControlCreator != null) {
						builder.setCollectionControlCreator(new CombinedCollectionControlCreator<ELEMENT_VALUE_TYPE>(
							elementControlCreator,
							collectionControlCreator));
					}
					else {
						builder.setCollectionControlCreator(collectionControlCreator);
					}

					controlPanels.add(builder.build());
				}

				if (displayFormat == null) {
					displayFormat = controlSupport.getDefaultDisplayFormat();
				}
			}
			else {
				controlPanels.add(createControlPanelProviderBuilder().build());
			}
		}
		return controlPanels;
	}

	private IControlPanelProviderBuilder<ELEMENT_VALUE_TYPE> createControlPanelProviderBuilder() {
		if (Collection.class.isAssignableFrom(valueType)) {
			return CapUiToolkit.attributeToolkit().createControlPanelProviderBuilder(
					propertyName,
					valueType,
					elementValueType,
					valueRange,
					getCardinality());
		}
		else {
			return CapUiToolkit.attributeToolkit().createControlPanelProviderBuilder(
					propertyName,
					elementValueType,
					valueRange,
					getCardinality());
		}
	}

	private IDisplayFormat getDisplayFormat(final List<IControlPanelProvider<? extends ELEMENT_VALUE_TYPE>> controlPanels) {
		if (displayFormat == null) {
			return controlPanels.get(0).getDisplayFormat();
		}
		else {
			return displayFormat;
		}
	}

	private Cardinality getCardinality() {
		if (cardinality != null) {
			return cardinality;
		}
		else if (valueType != null && Collection.class.isAssignableFrom(valueType)) {
			return Cardinality.GREATER_OR_EQUAL_ZERO;
		}
		else {
			return Cardinality.LESS_OR_EQUAL_ONE;
		}
	}

	private boolean getSearchable() {
		if (searchable != null) {
			return searchable.booleanValue();
		}
		else {
			return filterable;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public IAttribute<ELEMENT_VALUE_TYPE> build() {
		final List<IControlPanelProvider<? extends ELEMENT_VALUE_TYPE>> panels = new LinkedList<IControlPanelProvider<? extends ELEMENT_VALUE_TYPE>>();

		final Cardinality currentCardinality = getCardinality();

		for (final Object object : getControlPanels()) {
			if (object instanceof IControlPanelProvider) {
				panels.add((IControlPanelProvider<? extends ELEMENT_VALUE_TYPE>) object);
			}
			else if (object instanceof ControlPanelProviderBluePrintImpl) {
				final ControlPanelProviderBluePrintImpl<?> bluePrint = (ControlPanelProviderBluePrintImpl<?>) object;
				panels.add((IControlPanelProvider<? extends ELEMENT_VALUE_TYPE>) bluePrint.create(
						propertyName,
						valueType,
						elementValueType,
						valueRange,
						currentCardinality));
			}
			else {
				throw new IllegalStateException("Control panel type '"
					+ object.getClass().getName()
					+ "' is not supported. This seems to be a bug.");
			}
		}

		if (validator != null && !validatorAdded) {
			propertyValidatorBuilder.addValidator(validator);
			validatorAdded = true;
		}

		return new AttributeImpl<ELEMENT_VALUE_TYPE>(
			propertyName,
			valueRange,
			defaultValue,
			label,
			labelLong,
			labelDisplayFormat,
			description,
			visible,
			mandatory,
			editable,
			batchEditable,
			readonly,
			tableAlignment,
			tableColumnWidth,
			attributeGroup,
			sortable,
			filterable,
			getSearchable(),
			valueType,
			elementValueType,
			propertyValidatorBuilder.build(valueType, true),
			currentCardinality,
			panels,
			getDisplayFormat(panels));
	}
}
