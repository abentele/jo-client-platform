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

package org.jowidgets.cap.service.jpa.api.query;

import org.jowidgets.cap.common.api.filter.IFilter;

public interface ICriteriaQueryCreatorBuilder<PARAMETER_TYPE> {

	ICriteriaQueryCreatorBuilder<PARAMETER_TYPE> setParentPropertyName(String parentPropertyName);

	ICriteriaQueryCreatorBuilder<PARAMETER_TYPE> setParentPropertyPath(String... parentPropertyPath);

	ICriteriaQueryCreatorBuilder<PARAMETER_TYPE> setParentPropertyName(boolean linked, String parentPropertyName);

	/**
	 * Sets a predicate creator that evaluates if the object is linked (or not) with respect to the parent bean keys.
	 * 
	 * @param linked if true, the linked beans will be return, if false, the unlinked beans will be returned
	 * @param parentPropertyPath The path to the parent property (the parent beans id can be omitted)
	 * @return This builder
	 */
	ICriteriaQueryCreatorBuilder<PARAMETER_TYPE> setParentPropertyPath(boolean linked, String... parentPropertyPath);

	/**
	 * Adds a predicate creator that evaluates if the object is linked (or not) with respect to the parent bean keys.
	 * 
	 * @param linked if true, the linked beans will be returned (all paths will be disjuncted),
	 *            if false, the unlinked beans will be returned (al path will be conjuncted)
	 * @param parentPropertyPath The path to the parent property (the parent beans id can be omitted)
	 * @return This builder
	 */
	ICriteriaQueryCreatorBuilder<PARAMETER_TYPE> addParentPropertyPath(boolean linked, String... parentPropertyPath);

	ICriteriaQueryCreatorBuilder<PARAMETER_TYPE> setCaseSensitve(boolean caseSensitve);

	ICriteriaQueryCreatorBuilder<PARAMETER_TYPE> addPredicateCreator(IPredicateCreator<PARAMETER_TYPE> predicateCreator);

	ICriteriaQueryCreatorBuilder<PARAMETER_TYPE> addFilter(IFilter filter);

	ICriteriaQueryCreatorBuilder<PARAMETER_TYPE> addPropertyFilterPredicateCreator(
		String propertyName,
		IPropertyFilterPredicateCreator<PARAMETER_TYPE> predicateCreators);

	ICriteriaQueryCreatorBuilder<PARAMETER_TYPE> addCustomFilterPredicateCreator(
		String filterType,
		ICustomFilterPredicateCreator<PARAMETER_TYPE> predicateCreator);

	IQueryCreator<PARAMETER_TYPE> build();
}
