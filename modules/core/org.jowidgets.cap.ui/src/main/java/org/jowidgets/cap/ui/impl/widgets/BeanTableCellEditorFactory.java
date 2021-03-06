/*
 * Copyright (c) 2014, grossmann
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

package org.jowidgets.cap.ui.impl.widgets;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import org.jowidgets.api.controller.IDisposeListener;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IComboBox;
import org.jowidgets.api.widgets.IInputControl;
import org.jowidgets.cap.common.api.CapCommonToolkit;
import org.jowidgets.cap.common.api.validation.IBeanValidationResult;
import org.jowidgets.cap.common.api.validation.IBeanValidationResultListBuilder;
import org.jowidgets.cap.ui.api.attribute.IAttribute;
import org.jowidgets.cap.ui.api.attribute.IControlPanelProvider;
import org.jowidgets.cap.ui.api.bean.IBeanProxy;
import org.jowidgets.cap.ui.api.bean.IExternalBeanValidator;
import org.jowidgets.cap.ui.api.bean.IExternalBeanValidatorListener;
import org.jowidgets.cap.ui.api.color.CapColors;
import org.jowidgets.cap.ui.api.table.IBeanTableModel;
import org.jowidgets.cap.ui.api.widgets.IBeanTable;
import org.jowidgets.common.model.ITableCell;
import org.jowidgets.common.types.VirtualKey;
import org.jowidgets.common.widgets.ISelectable;
import org.jowidgets.common.widgets.controller.IInputListener;
import org.jowidgets.common.widgets.controller.IKeyEvent;
import org.jowidgets.common.widgets.controller.IKeyListener;
import org.jowidgets.common.widgets.editor.EditActivation;
import org.jowidgets.common.widgets.editor.ITableCellEditor;
import org.jowidgets.common.widgets.factory.ICustomWidgetCreator;
import org.jowidgets.common.widgets.factory.ICustomWidgetFactory;
import org.jowidgets.tools.controller.KeyObservable;
import org.jowidgets.tools.editor.AbstractTableCellEditor;
import org.jowidgets.tools.editor.AbstractTableCellEditorFactory;
import org.jowidgets.util.Assert;
import org.jowidgets.util.event.IChangeListener;
import org.jowidgets.validation.IValidationConditionListener;
import org.jowidgets.validation.IValidationResult;

final class BeanTableCellEditorFactory extends AbstractTableCellEditorFactory<ITableCellEditor> {

	private final IBeanTable<?> table;
	private final IBeanTableModel<?> model;

	BeanTableCellEditorFactory(final IBeanTable<?> table) {
		Assert.paramNotNull(table, "table");
		this.table = table;
		this.model = table.getModel();
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public ITableCellEditor create(
		final ITableCell cell,
		final int row,
		final int column,
		final ICustomWidgetFactory widgetFactory) {

		final IAttribute<Object> attribute = model.getAttribute(column);
		if (attribute.isEditable()) {
			final IControlPanelProvider<Object> controlPanel = attribute.getCurrentControlPanel();
			if (controlPanel != null) {
				if (attribute.isCollectionType()) {
					final ICustomWidgetCreator<IInputControl<? extends Collection<Object>>> controlCreator = controlPanel.getCollectionControlCreator();
					if (controlCreator != null) {
						final IInputControl editor = controlCreator.create(widgetFactory);
						return new AttributeTableCellEditor(attribute, editor);
					}
				}
				else {
					final ICustomWidgetCreator<IInputControl<Object>> controlCreator = controlPanel.getControlCreator();
					if (controlCreator != null) {
						final IInputControl<Object> editor = controlCreator.create(widgetFactory);
						return new AttributeTableCellEditor(attribute, editor);
					}
				}
			}
		}

		return null;
	}

	@Override
	public EditActivation getActivation(
		final ITableCell cell,
		final int row,
		final int column,
		final boolean editMode,
		final long editModeStopped) {
		if (editMode || (System.currentTimeMillis() - editModeStopped < 100)) {
			return EditActivation.SINGLE_CLICK;
		}
		else {
			return super.getActivation(cell, row, column, editMode, editModeStopped);
		}
	}

	private final class AttributeTableCellEditor extends AbstractTableCellEditor implements IExternalBeanValidator {

		private final IInputControl<Object> editor;
		private final IAttribute<Object> attribute;
		private final Set<IExternalBeanValidatorListener> externalValidatorListeners;
		private final KeyObservable keyObservable;

		private Object lastValue;
		private IBeanProxy<?> lastBean;
		private IInputListener inputListener;
		private PropertyChangeListener propertyChangeListener;
		private IChangeListener attributeListener;

		private AttributeTableCellEditor(final IAttribute<Object> attribute, final IInputControl<Object> editor) {
			super(editor);
			this.attribute = attribute;
			this.editor = editor;
			this.externalValidatorListeners = new LinkedHashSet<IExternalBeanValidatorListener>();
			this.keyObservable = new KeyObservable();

			if (attribute.isMandatory()) {
				editor.setBackgroundColor(CapColors.MANDATORY_BACKGROUND);
			}

			editor.addValidationConditionListener(new IValidationConditionListener() {
				@Override
				public void validationConditionsChanged() {
					fireValidationConditionsChanged();
				}
			});

			editor.addKeyListener(new IKeyListener() {

				@Override
				public void keyReleased(final IKeyEvent event) {
					keyObservable.fireKeyReleased(event);
				}

				@Override
				public void keyPressed(final IKeyEvent event) {
					//avoid stop editing when combobox should be closed
					if (editor instanceof IComboBox<?>) {
						if (((IComboBox<?>) editor).isPopupVisible()) {
							if (VirtualKey.ESC == event.getVirtualKey() || VirtualKey.ENTER == event.getVirtualKey()) {
								return;
							}
						}
					}
					keyObservable.fireKeyPressed(event);
				}
			});

		}

		@Override
		public void addKeyListener(final IKeyListener listener) {
			keyObservable.addKeyListener(listener);
		}

		@Override
		public void removeKeyListener(final IKeyListener listener) {
			keyObservable.removeKeyListener(listener);
		}

		@Override
		public void startEditing(final ITableCell cell, final int row, final int column) {
			final IBeanProxy<?> bean = model.getBean(row);
			editor.setValue(bean.getValue(attribute.getPropertyName()));

			lastValue = editor.getValue();
			lastBean = bean;

			if (lastValue != null && (editor instanceof ISelectable)) {
				if (editor.hasFocus()) {
					((ISelectable) editor).select();
				}
				else {
					Toolkit.getUiThreadAccess().invokeLater(new Runnable() {
						@Override
						public void run() {
							if (!editor.isDisposed()) {
								((ISelectable) editor).select();
							}
						}
					});
				}
			}

			removeListeners(row);
			bean.registerExternalValidator(AttributeTableCellEditor.this);

			inputListener = new IInputListener() {
				@Override
				public void inputChanged() {
					if (propertyChangeListener != null) {
						bean.removePropertyChangeListener(propertyChangeListener);
					}
					bean.setValue(attribute.getPropertyName(), editor.getValue());
					if (propertyChangeListener != null) {
						bean.addPropertyChangeListener(propertyChangeListener);
					}
				}
			};
			editor.addInputListener(inputListener);

			propertyChangeListener = new PropertyChangeListener() {
				@Override
				public void propertyChange(final PropertyChangeEvent evt) {
					if (attribute.getPropertyName().equals(evt.getPropertyName())) {
						if (inputListener != null) {
							editor.removeInputListener(inputListener);
						}
						editor.setValue(bean.getValue(attribute.getPropertyName()));
						lastValue = editor.getValue();
						if (inputListener != null) {
							editor.addInputListener(inputListener);
						}
					}
				}
			};
			bean.addPropertyChangeListener(propertyChangeListener);

			attributeListener = new IChangeListener() {
				@Override
				public void changed() {
					table.stopEditing();
				}
			};
			attribute.addChangeListener(attributeListener);

			editor.addDisposeListener(new IDisposeListener() {
				@Override
				public void onDispose() {
					bean.unregisterExternalValidator(AttributeTableCellEditor.this);

					if (propertyChangeListener != null) {
						bean.removePropertyChangeListener(propertyChangeListener);
						propertyChangeListener = null;
					}

					if (attributeListener != null) {
						attribute.removeChangeListener(attributeListener);
						attributeListener = null;
					}
				}
			});

		}

		@Override
		public Collection<IBeanValidationResult> validate(final Collection<IBeanValidationResult> parentResult) {
			final IBeanValidationResultListBuilder builder = CapCommonToolkit.beanValidationResultListBuilder();

			final IValidationResult editorResult = editor.validate();
			if (!editorResult.isValid() || (!editorResult.isOk() && !hasErrors(parentResult))) {
				builder.addResult(editorResult.withContext(attribute.getCurrentLabel()), attribute.getPropertyName());
			}
			else {
				builder.addResult(parentResult);
			}

			return builder.build();
		}

		private boolean hasErrors(final Collection<IBeanValidationResult> parentResults) {
			for (final IBeanValidationResult parentResult : parentResults) {
				if (!parentResult.getValidationResult().isValid()) {
					return true;
				}
			}
			return false;
		}

		@Override
		public Collection<String> getObservedProperties() {
			return Collections.singleton(attribute.getPropertyName());
		}

		@Override
		public void addExternalValidatorListener(final IExternalBeanValidatorListener listener) {
			externalValidatorListeners.add(listener);
		}

		@Override
		public void removeExternalValidatorListener(final IExternalBeanValidatorListener listener) {
			externalValidatorListeners.remove(listener);
		}

		private void fireValidationConditionsChanged() {
			final Collection<String> properties = getObservedProperties();
			for (final IExternalBeanValidatorListener listener : new LinkedList<IExternalBeanValidatorListener>(
				externalValidatorListeners)) {
				listener.validationConditionsChanged(this, properties);
			}
		}

		@Override
		public void stopEditing(final ITableCell cell, final int row, final int column) {
			setValueToBean(row, editor.getValue());
			removeListeners(row);

		}

		@Override
		public void cancelEditing(final ITableCell cell, final int row, final int column) {
			setValueToBean(row, lastValue);
			removeListeners(row);
		}

		private void setValueToBean(final int row, final Object value) {
			final IBeanProxy<?> bean = model.getBean(row);
			if (bean != null && bean == lastBean && !bean.isDisposed()) {
				bean.setValue(attribute.getPropertyName(), value);
			}
			lastBean = null;
			lastValue = null;
		}

		private void removeListeners(final int row) {
			final IBeanProxy<?> bean = model.getBean(row);
			if (bean != null && !bean.isDisposed()) {
				bean.unregisterExternalValidator(AttributeTableCellEditor.this);
			}
			if (inputListener != null) {
				editor.removeInputListener(inputListener);
			}

		}

	}

}
