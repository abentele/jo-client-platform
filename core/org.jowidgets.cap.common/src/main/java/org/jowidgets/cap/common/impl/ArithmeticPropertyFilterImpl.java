/*
 * Copyright (c) 2011, Nikolaus Moll
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

package org.jowidgets.cap.common.impl;

import java.io.Serializable;

import org.jowidgets.cap.common.api.filter.ArithmeticOperator;
import org.jowidgets.cap.common.api.filter.IArithmeticPropertyFilter;

final class ArithmeticPropertyFilterImpl implements IArithmeticPropertyFilter, Serializable {

	private static final long serialVersionUID = 2437119345197536769L;
	private final String leftHandPropertyName;
	private final ArithmeticOperator operator;
	private final String[] rightHandPropertyNames;
	private final boolean inverted;

	ArithmeticPropertyFilterImpl(
		final String leftHandPropertyName,
		final ArithmeticOperator operator,
		final String[] rightHandPropertyNames,
		final boolean inverted) {
		this.leftHandPropertyName = leftHandPropertyName;
		this.operator = operator;
		this.rightHandPropertyNames = rightHandPropertyNames;
		this.inverted = inverted;
	}

	@Override
	public boolean isInverted() {
		return inverted;
	}

	@Override
	public String getLeftHandPropertyName() {
		return leftHandPropertyName;
	}

	@Override
	public ArithmeticOperator getOperator() {
		return operator;
	}

	@Override
	public String[] getRightHandPropertyNames() {
		return rightHandPropertyNames;
	}

}
