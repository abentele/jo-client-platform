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

import org.jowidgets.api.command.IAction;
import org.jowidgets.api.command.IActionBuilder;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.cap.ui.api.command.ICapActionBuilder;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Accelerator;
import org.jowidgets.common.types.Modifier;
import org.jowidgets.common.types.VirtualKey;
import org.jowidgets.util.builder.AbstractSingleUseBuilder;

abstract class AbstractCapActionBuilderImpl<INSTANCE_TYPE extends ICapActionBuilder<?>> extends AbstractSingleUseBuilder<IAction> implements
		ICapActionBuilder<INSTANCE_TYPE> {

	private final IActionBuilder builder;

	private String text;
	private String toolTipText;
	private IImageConstant icon;

	AbstractCapActionBuilderImpl() {
		this.builder = Toolkit.getActionBuilderFactory().create();
	}

	@SuppressWarnings("unchecked")
	@Override
	public final INSTANCE_TYPE setText(final String text) {
		checkExhausted();
		this.text = text;
		builder.setText(text);
		return (INSTANCE_TYPE) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final INSTANCE_TYPE setToolTipText(final String toolTipText) {
		checkExhausted();
		this.toolTipText = toolTipText;
		builder.setToolTipText(toolTipText);
		return (INSTANCE_TYPE) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final INSTANCE_TYPE setIcon(final IImageConstant icon) {
		checkExhausted();
		this.icon = icon;
		builder.setIcon(icon);
		return (INSTANCE_TYPE) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final INSTANCE_TYPE setMnemonic(final Character mnemonic) {
		checkExhausted();
		builder.setMnemonic(mnemonic);
		return (INSTANCE_TYPE) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final INSTANCE_TYPE setMnemonic(final char mnemonic) {
		checkExhausted();
		builder.setMnemonic(mnemonic);
		return (INSTANCE_TYPE) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final INSTANCE_TYPE setAccelerator(final Accelerator accelerator) {
		checkExhausted();
		builder.setAccelerator(accelerator);
		return (INSTANCE_TYPE) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final INSTANCE_TYPE setAccelerator(final char key, final Modifier... modifier) {
		checkExhausted();
		builder.setAccelerator(key, modifier);
		return (INSTANCE_TYPE) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final INSTANCE_TYPE setAccelerator(final VirtualKey virtualKey, final Modifier... modifier) {
		checkExhausted();
		builder.setAccelerator(virtualKey, modifier);
		return (INSTANCE_TYPE) this;
	}

	protected final IActionBuilder getBuilder() {
		return builder;
	}

	protected final String getText() {
		return text;
	}

	protected String getToolTipText() {
		return toolTipText;
	}

	protected final IImageConstant getIcon() {
		return icon;
	}

}
