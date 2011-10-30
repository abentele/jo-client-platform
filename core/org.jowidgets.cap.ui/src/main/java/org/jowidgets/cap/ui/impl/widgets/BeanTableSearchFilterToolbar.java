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

package org.jowidgets.cap.ui.impl.widgets;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.jowidgets.api.convert.IStringObjectConverter;
import org.jowidgets.api.image.IconsSmall;
import org.jowidgets.api.threads.IUiThreadAccess;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.api.widgets.ITextControl;
import org.jowidgets.api.widgets.IToolBar;
import org.jowidgets.api.widgets.IToolBarButton;
import org.jowidgets.api.widgets.blueprint.IToolBarButtonBluePrint;
import org.jowidgets.cap.common.api.filter.ArithmeticOperator;
import org.jowidgets.cap.common.api.filter.BooleanOperator;
import org.jowidgets.cap.common.api.lookup.ILookUpEntry;
import org.jowidgets.cap.common.api.lookup.ILookUpProperty;
import org.jowidgets.cap.common.api.lookup.ILookUpValueRange;
import org.jowidgets.cap.ui.api.CapUiToolkit;
import org.jowidgets.cap.ui.api.attribute.IAttribute;
import org.jowidgets.cap.ui.api.control.IDisplayFormat;
import org.jowidgets.cap.ui.api.filter.IUiArithmeticFilter;
import org.jowidgets.cap.ui.api.filter.IUiBooleanFilterBuilder;
import org.jowidgets.cap.ui.api.filter.IUiFilter;
import org.jowidgets.cap.ui.api.filter.IUiFilterFactory;
import org.jowidgets.cap.ui.api.lookup.ILookUp;
import org.jowidgets.cap.ui.api.lookup.ILookUpAccess;
import org.jowidgets.cap.ui.api.table.IBeanTableModel;
import org.jowidgets.common.widgets.controller.IActionListener;
import org.jowidgets.common.widgets.controller.IInputListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.widgets.blueprint.BPF;
import org.jowidgets.util.EmptyCheck;
import org.jowidgets.util.NullCompatibleEquivalence;
import org.jowidgets.util.concurrent.DaemonThreadFactory;

final class BeanTableSearchFilterToolbar<BEAN_TYPE> {

	private static final int LISTENER_DELAY = 200;

	private final IBeanTableModel<BEAN_TYPE> model;
	private final List<IAttribute<Object>> attributes;
	private final IComposite toolbar;

	private final IInputListener inputListener;

	private ScheduledFuture<?> schedule;

	BeanTableSearchFilterToolbar(final IComposite composite, final BeanTableImpl<BEAN_TYPE> table) {
		this.model = table.getModel();
		this.attributes = model.getAttributes();

		this.toolbar = composite.add(0, BPF.composite(), "growx, w 0::, wrap");
		toolbar.setLayout(new MigLayoutDescriptor("0[][][grow]0", "0[grow]1"));

		final IToolBar toolBar1 = toolbar.add(BPF.toolBar(), "");
		final IToolBarButtonBluePrint closeButtonBp = BPF.toolBarButton().setIcon(IconsSmall.DELETE);
		closeButtonBp.setToolTipText(Messages.getString("BeanTableSearchFilterToolbar.close_toolbar"));
		final IToolBarButton closeButton = toolBar1.addItem(closeButtonBp);

		toolBar1.pack();

		toolbar.add(BPF.textLabel().setText(Messages.getString("BeanTableSearchFilterToolbar.search_filter")), "");
		final ITextControl textField = toolbar.add(BPF.textField(), "growx, w 100::");

		closeButton.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				table.setSearchFilterToolbarVisible(false);
			}
		});

		this.inputListener = new IInputListener() {
			@Override
			public void inputChanged() {
				doSearchScheduled(textField.getText());
			}
		};

		textField.addInputListener(inputListener);

		toolbar.layout();
	}

	private void doSearchScheduled(final String text) {
		if (schedule != null) {
			schedule.cancel(false);
		}
		final IUiThreadAccess uiThreadAccess = Toolkit.getUiThreadAccess();
		final Runnable runnable = new Runnable() {
			@Override
			public void run() {
				uiThreadAccess.invokeLater(new Runnable() {
					@Override
					public void run() {
						schedule = null;
						doSearch(text);
					}
				});
			}
		};
		schedule = Executors.newSingleThreadScheduledExecutor(new DaemonThreadFactory()).schedule(
				runnable,
				LISTENER_DELAY,
				TimeUnit.MILLISECONDS);

	}

	private void doSearch(final String text) {
		final IUiFilter filter = createFilter(text);
		model.setFilter(IBeanTableModel.UI_SEARCH_FILTER_ID, filter);
		model.load();
	}

	private IUiFilter createFilter(final String text) {
		if (EmptyCheck.isEmpty(text)) {
			return null;
		}

		final StringTokenizer tokenizer = new StringTokenizer(text);

		final IUiFilterFactory factory = CapUiToolkit.filterToolkit().filterFactory();
		final IUiBooleanFilterBuilder andFilterBuilder = factory.booleanFilterBuilder().setOperator(BooleanOperator.AND);

		while (tokenizer.hasMoreElements()) {
			final String token = tokenizer.nextToken();
			final IUiBooleanFilterBuilder orFilterBuilder = factory.booleanFilterBuilder().setOperator(BooleanOperator.OR);
			boolean predicateCreated = false;
			for (final IAttribute<Object> attribute : attributes) {
				final IUiArithmeticFilter<?> arithmeticFilter;
				if (attribute.getValueRange() instanceof ILookUpValueRange) {
					arithmeticFilter = createLookUpFilter(attribute, token);
				}
				else if (String.class.isAssignableFrom(attribute.getElementValueType())) {
					arithmeticFilter = createStringTypeFilter(attribute, token);
				}
				else {
					arithmeticFilter = createManifoldTypeFilter(attribute, token);
				}
				if (arithmeticFilter != null) {
					orFilterBuilder.addFilter(arithmeticFilter);
					predicateCreated = true;
				}
			}
			if (predicateCreated) {
				andFilterBuilder.addFilter(orFilterBuilder.build());
			}
		}
		return andFilterBuilder.build();
	}

	private IUiArithmeticFilter<?> createManifoldTypeFilter(final IAttribute<Object> attribute, final String string) {
		final IStringObjectConverter<Object> converter = attribute.getCurrentControlPanel().getStringObjectConverter();
		if (converter != null) {
			final Object value = converter.convertToObject(string);
			if (value != null) {
				return createAritmeticFilter(attribute, value);
			}
		}
		return null;
	}

	private IUiArithmeticFilter<?> createStringTypeFilter(final IAttribute<Object> attribute, final String string) {
		return createAritmeticFilter(attribute, createMaskedString(string));
	}

	private IUiArithmeticFilter<?> createLookUpFilter(final IAttribute<Object> attribute, final String string) {
		final ILookUpValueRange valueRange = (ILookUpValueRange) attribute.getValueRange();

		final ILookUpAccess lookUpAccess = CapUiToolkit.lookUpCache().getAccess(valueRange.getLookUpId());
		final ILookUp currentLookUp = lookUpAccess.getCurrentLookUp();
		if (currentLookUp == null) {
			return createManifoldTypeFilter(attribute, string);
		}
		final ILookUpProperty lookUpProperty = getLookUpProperty(attribute, valueRange);
		if (lookUpProperty == null || !String.class.isAssignableFrom(lookUpProperty.getValueType())) {
			return createManifoldTypeFilter(attribute, string);
		}

		final Object[] entries = getMatchingEntries(currentLookUp, lookUpProperty, string);
		if (entries.length != 0) {
			final IUiFilterFactory factory = CapUiToolkit.filterToolkit().filterFactory();
			return factory.arithmeticFilter(attribute.getPropertyName(), ArithmeticOperator.CONTAINS_ANY, entries);
		}
		return createManifoldTypeFilter(attribute, string);
	}

	private Object[] getMatchingEntries(final ILookUp lookUp, final ILookUpProperty lookUpProperty, final String string) {
		final List<Object> result = new LinkedList<Object>();
		final String pattern = createRegex(createMaskedString(string));
		for (final ILookUpEntry lookUpEntry : lookUp.getEntries()) {
			if (matches(lookUpEntry.getValue(lookUpProperty.getName()), pattern)) {
				result.add(lookUpEntry.getKey());
			}
		}
		return result.toArray();
	}

	private boolean matches(final Object source, final String pattern) {
		if (source != null && source instanceof String) {
			return ((String) source).toLowerCase().matches(pattern);
		}
		else if (source instanceof String) {
			return pattern == null;
		}
		return false;
	}

	private ILookUpProperty getLookUpProperty(final IAttribute<Object> attribute, final ILookUpValueRange valueRange) {
		final IDisplayFormat displayFormat = attribute.getDisplayFormat();
		for (final ILookUpProperty lookUpProperty : valueRange.getValueProperties()) {
			if (NullCompatibleEquivalence.equals(displayFormat.getId(), lookUpProperty.getDisplayFormatId())) {
				return lookUpProperty;
			}
		}
		return null;
	}

	private IUiArithmeticFilter<?> createAritmeticFilter(final IAttribute<Object> attribute, final Object value) {
		final IUiFilterFactory factory = CapUiToolkit.filterToolkit().filterFactory();
		return factory.arithmeticFilter(attribute.getPropertyName(), ArithmeticOperator.EQUAL, value);
	}

	private String createMaskedString(final String string) {
		return string + "*";
	}

	private String createRegex(final String search) {
		final StringBuilder regex = new StringBuilder(search.length());
		for (final char c : search.toLowerCase().toCharArray()) {
			switch (c) {
				case '\\':
					regex.append("\\\\");
					break;

				case '[':
				case ']':
				case '(':
				case ')':
				case '.':
				case '+':
				case '^':
				case '$':
					regex.append('\\');
					regex.append(c);
					break;

				// wild cards
				case '%':
				case '*':
					regex.append(".*");
					break;

				case '_':
					regex.append('.');
					break;

				default:
					regex.append(c);
			}
		}
		return regex.toString();
	}

	IControl getToolbar() {
		return toolbar;
	}

}
