/*
 * Copyright (c) 2012, grossmann
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

package org.jowidgets.cap.security.ui.impl;

import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.api.widgets.blueprint.IScrollCompositeBluePrint;
import org.jowidgets.cap.security.ui.api.ISecureControlCreator;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.widgets.factory.ICustomWidgetFactory;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.layout.MigLayoutFactory;
import org.jowidgets.tools.widgets.blueprint.BPF;

final class DefaultSecureControlCreatorImpl implements ISecureControlCreator<IControl> {

	private final boolean border;

	DefaultSecureControlCreatorImpl() {
		this(false);
	}

	DefaultSecureControlCreatorImpl(final boolean border) {
		this.border = border;
	}

	@Override
	public IControl create(final ICustomWidgetFactory widgetFactory, final String label, final IImageConstant icon) {
		final IScrollCompositeBluePrint compositeBp = BPF.scrollComposite();
		if (border) {
			compositeBp.setBorder();
		}
		final IComposite composite = widgetFactory.create(compositeBp);
		composite.setLayout(MigLayoutFactory.growingCellLayout());

		final IComposite labelComposite = composite.add(BPF.composite(), "alignx c, aligny c");
		labelComposite.setLayout(new MigLayoutDescriptor("0[grow, 0::]0", "0[grow, 0::][grow, 0::]0"));
		labelComposite.add(BPF.icon(icon), "alignx c, wrap");
		labelComposite.add(BPF.textLabel(label), "alignx c");
		return composite;
	}

}
