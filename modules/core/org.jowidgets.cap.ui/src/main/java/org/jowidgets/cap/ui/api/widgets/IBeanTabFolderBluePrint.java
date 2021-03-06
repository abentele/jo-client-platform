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

package org.jowidgets.cap.ui.api.widgets;

import org.jowidgets.api.widgets.blueprint.builder.ITabFolderSetupBuilder;
import org.jowidgets.api.widgets.descriptor.setup.ITabFolderSetup;
import org.jowidgets.cap.ui.api.tabfolder.IBeanTabFolderModel;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.descriptor.setup.mandatory.Mandatory;

public interface IBeanTabFolderBluePrint<BEAN_TYPE> extends
		ITabFolderSetup,
		ITabFolderSetupBuilder<IBeanTabFolderBluePrint<BEAN_TYPE>>,
		IBeanTabFolderSetupConvenience<BEAN_TYPE, IBeanTabFolderBluePrint<BEAN_TYPE>>,
		IWidgetDescriptor<IBeanTabFolder<BEAN_TYPE>> {

	IBeanTabFolderBluePrint<BEAN_TYPE> setModel(IBeanTabFolderModel<BEAN_TYPE> model);

	IBeanTabFolderBluePrint<BEAN_TYPE> setTabFactory(IBeanTabFactory<BEAN_TYPE> tabFactory);

	@Mandatory
	IBeanTabFolderModel<BEAN_TYPE> getModel();

	IBeanTabFactory<BEAN_TYPE> getTabFactory();

}
