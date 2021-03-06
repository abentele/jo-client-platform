/*
 * Copyright (c) 2013, grossmann
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

package org.jowidgets.cap.ui.api.table;

import org.jowidgets.api.model.table.ITableCellBluePrint;
import org.jowidgets.cap.ui.api.attribute.IAttribute;
import org.jowidgets.cap.ui.api.bean.IBeanProxy;
import org.jowidgets.common.model.ITableCell;

public interface IBeanTableCellRenderer<BEAN_TYPE> {

	/**
	 * Renders the bean tables cell. The rendering will be done, each time the bean changes.
	 * At the moment the following changes are be supported:
	 * - PropertyChanges
	 * - ModificationStateChanges
	 * - ProcessStateChanges
	 * 
	 * @param bluePrint The blue print, holds the values defined by previous renderers and can be modified
	 * @param original The original cell, holds the values defined by previous renderers
	 * @param bean The bean to render.
	 * @param attribute The attribute that is assigned to the cell
	 * @param rowIndex The row index of the bean
	 * @param columnIndex The (logical) column index of the cell
	 * @param addedBean If true, the bean was added programmatically, if false, the bean was loaded by the reader service
	 */
	void render(
		ITableCellBluePrint bluePrint,
		ITableCell original,
		IBeanProxy<BEAN_TYPE> bean,
		IAttribute<Object> attribute,
		int rowIndex,
		int columnIndex,
		boolean addedBean);

}
