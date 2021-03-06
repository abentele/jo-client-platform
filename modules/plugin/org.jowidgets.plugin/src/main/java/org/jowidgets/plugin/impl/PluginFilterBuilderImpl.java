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

package org.jowidgets.plugin.impl;

import java.util.LinkedList;
import java.util.List;

import org.jowidgets.plugin.api.IPluginFilter;
import org.jowidgets.plugin.api.IPluginFilterBuilder;
import org.jowidgets.plugin.api.IPluginProperties;
import org.jowidgets.util.ITypedKey;
import org.jowidgets.util.NullCompatibleEquivalence;
import org.jowidgets.util.Tuple;

final class PluginFilterBuilderImpl implements IPluginFilterBuilder {

	private final List<Tuple<ITypedKey<Object>, Object>> conditions;

	private final boolean and;

	PluginFilterBuilderImpl(final boolean and) {
		this.and = and;
		this.conditions = new LinkedList<Tuple<ITypedKey<Object>, Object>>();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <VALUE_TYPE> IPluginFilterBuilder addCondition(final ITypedKey<VALUE_TYPE> key, final VALUE_TYPE value) {
		conditions.add(new Tuple<ITypedKey<Object>, Object>((ITypedKey<Object>) key, value));
		return this;
	}

	@Override
	public IPluginFilter build() {
		return new IPluginFilter() {
			@Override
			public boolean accept(final IPluginProperties properties) {
				if (and) {
					for (final Tuple<ITypedKey<Object>, Object> condition : conditions) {
						if (!NullCompatibleEquivalence.equals(properties.getValue(condition.getFirst()), condition.getSecond())) {
							return false;
						}
					}
					return true;
				}
				else {
					for (final Tuple<ITypedKey<Object>, Object> condition : conditions) {
						if (NullCompatibleEquivalence.equals(properties.getValue(condition.getFirst()), condition.getSecond())) {
							return true;
						}
					}
					return false;
				}
			}
		};

	}
}
