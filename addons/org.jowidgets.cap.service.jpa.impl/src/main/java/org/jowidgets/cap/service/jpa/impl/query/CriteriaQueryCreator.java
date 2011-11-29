/*
 * Copyright (c) 2011, H.Westphal, M.Grossmann
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

package org.jowidgets.cap.service.jpa.impl.query;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.persistence.metamodel.Attribute;

import org.jowidgets.cap.common.api.bean.IBean;
import org.jowidgets.cap.common.api.bean.IBeanKey;
import org.jowidgets.cap.common.api.filter.BooleanOperator;
import org.jowidgets.cap.common.api.filter.IArithmeticFilter;
import org.jowidgets.cap.common.api.filter.IBooleanFilter;
import org.jowidgets.cap.common.api.filter.ICustomFilter;
import org.jowidgets.cap.common.api.filter.IFilter;
import org.jowidgets.cap.common.api.sort.ISort;
import org.jowidgets.cap.common.api.sort.SortOrder;
import org.jowidgets.cap.service.jpa.api.query.ICustomFilterPredicateCreator;
import org.jowidgets.cap.service.jpa.api.query.IPredicateCreator;
import org.jowidgets.cap.service.jpa.api.query.IQueryCreator;
import org.jowidgets.cap.service.jpa.api.query.LookupHierarchy;
import org.jowidgets.cap.service.jpa.api.query.QueryPath;
import org.jowidgets.util.Assert;

final class CriteriaQueryCreator<PARAM_TYPE> implements IQueryCreator<PARAM_TYPE> {

	private final Class<? extends IBean> beanType;
	private final boolean caseInsensitive;
	private final List<IPredicateCreator<PARAM_TYPE>> predicateCreators;
	private final Map<String, ? extends ICustomFilterPredicateCreator<PARAM_TYPE>> customFilterPredicateCreators;

	CriteriaQueryCreator(
		final Class<? extends IBean> beanType,
		final boolean caseSensitive,
		final Collection<? extends IPredicateCreator<PARAM_TYPE>> predicateCreators,
		final Map<String, ? extends ICustomFilterPredicateCreator<PARAM_TYPE>> customFilterPredicateCreators) {

		Assert.paramNotNull(beanType, "beanType");
		Assert.paramNotNull(predicateCreators, "predicateCreators");
		Assert.paramNotNull(customFilterPredicateCreators, "customFilterPredicateCreators");

		this.beanType = beanType;
		this.caseInsensitive = !caseSensitive;
		this.predicateCreators = new LinkedList<IPredicateCreator<PARAM_TYPE>>(predicateCreators);
		this.customFilterPredicateCreators = new HashMap<String, ICustomFilterPredicateCreator<PARAM_TYPE>>(
			customFilterPredicateCreators);
	}

	@Override
	public Query createReadQuery(
		final EntityManager entityManager,
		final List<? extends IBeanKey> parentBeanKeys,
		final IFilter filter,
		final List<? extends ISort> sorting,
		final PARAM_TYPE parameter) {

		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<?> query = criteriaBuilder.createQuery(beanType);
		query.distinct(true);
		final Root<?> bean = fillQuery(query, criteriaBuilder, beanType, parentBeanKeys, filter, parameter);

		if (sorting != null) {
			final List<Order> order = new LinkedList<Order>();
			for (final ISort sort : sorting) {
				final Path<?> path = getPath(bean, sort.getPropertyName());
				// TODO HRW fix sorting of joined attributes
				if (sort.getSortOrder() == SortOrder.ASC) {
					order.add(criteriaBuilder.asc(path));
				}
				else {
					order.add(criteriaBuilder.desc(path));
				}
			}
			query.orderBy(order);
		}

		return entityManager.createQuery(query);
	}

	@Override
	public Query createCountQuery(
		final EntityManager entityManager,
		final List<? extends IBeanKey> parentBeanKeys,
		final IFilter filter,
		final PARAM_TYPE parameter) {

		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
		final Root<?> bean = fillQuery(query, criteriaBuilder, beanType, parentBeanKeys, filter, parameter);

		return entityManager.createQuery(query.select(criteriaBuilder.countDistinct(bean)));
	}

	@SuppressWarnings("unchecked")
	private Root<?> fillQuery(
		final CriteriaQuery<?> query,
		final CriteriaBuilder criteriaBuilder,
		final Class<?> persistenceClass,
		final List<? extends IBeanKey> parentBeanKeys,
		final IFilter filter,
		final PARAM_TYPE parameter) {

		final Root<?> bean = query.from(persistenceClass);
		final List<Predicate> predicates = new LinkedList<Predicate>();

		final List<Object> parentIds = new LinkedList<Object>();
		if (parentBeanKeys != null) {
			for (final IBeanKey parentBeanKey : parentBeanKeys) {
				parentIds.add(parentBeanKey.getId());
			}
		}

		for (final IPredicateCreator<PARAM_TYPE> predicateCreator : predicateCreators) {
			final Predicate predicate = predicateCreator.createPredicate(
					criteriaBuilder,
					bean,
					query,
					(List<IBeanKey>) parentBeanKeys,
					parentIds,
					parameter);
			if (predicate != null) {
				predicates.add(predicate);
			}
		}

		if (filter != null) {
			predicates.add(createFilterPredicate(criteriaBuilder, bean, query, filter, parameter));
		}

		query.where(predicates.toArray(new Predicate[0]));
		return bean;
	}

	private Predicate createFilterPredicate(
		final CriteriaBuilder criteriaBuilder,
		final Root<?> bean,
		final CriteriaQuery<?> query,
		final IFilter filter,
		final PARAM_TYPE parameter) {
		final Predicate predicate;

		if (filter instanceof IArithmeticFilter) {
			predicate = createFilterPredicate(criteriaBuilder, bean, query, (IArithmeticFilter) filter);
		}
		else if (filter instanceof IBooleanFilter) {
			predicate = createFilterPredicate(criteriaBuilder, bean, query, (IBooleanFilter) filter, parameter);
		}
		else if (filter instanceof ICustomFilter) {
			final ICustomFilter customFilter = (ICustomFilter) filter;
			final ICustomFilterPredicateCreator<PARAM_TYPE> customFilterPredicateCreator = customFilterPredicateCreators.get(customFilter.getFilterType());
			if (customFilterPredicateCreator != null) {
				predicate = customFilterPredicateCreator.createPredicate(
						criteriaBuilder,
						getPath(bean, customFilter.getPropertyName()),
						query,
						customFilter.getValue(),
						parameter);
			}
			else {
				throw new IllegalArgumentException("unsupported custom filter type: " + customFilter.getFilterType());
			}
		}
		else {
			// TODO HRW support IArithmeticPropertyFilter
			throw new IllegalArgumentException("unsupported filter type: " + filter.getClass().getName());
		}
		if (filter.isInverted()) {
			return criteriaBuilder.not(predicate);
		}
		return predicate;
	}

	private Path<?> getPath(final Root<?> bean, final String propertyName) {
		try {
			return bean.get(propertyName);
		}
		catch (final IllegalArgumentException iae) {
			// check for QueryPath annotation
			final Class<?> beanClass = bean.getJavaType();
			try {
				final PropertyDescriptor descriptor = new PropertyDescriptor(propertyName, beanClass, "is"
					+ propertyName.substring(0, 1).toUpperCase(Locale.ENGLISH)
					+ propertyName.substring(1), null);
				final Method readMethod = descriptor.getReadMethod();
				final QueryPath queryPath = readMethod.getAnnotation(QueryPath.class);
				if (queryPath != null) {
					final String[] segments = queryPath.value().split("\\.");
					if (segments.length >= 1) {
						Path<?> path = bean;
						for (final String segment : segments) {
							path = path.get(segment);
						}
						return path;
					}
				}
			}
			catch (final IntrospectionException e) {
				throw iae;
			}
			throw iae;
		}
	}

	@SuppressWarnings("unchecked")
	private Predicate createFilterPredicate(
		final CriteriaBuilder criteriaBuilder,
		final Root<?> bean,
		final CriteriaQuery<?> query,
		final IArithmeticFilter filter) {

		final Path<?> path = getPath(bean, filter.getPropertyName());
		final boolean isCollection = ((Attribute<?, ?>) path.getModel()).isCollection();
		final boolean isJoined = path.getParentPath() instanceof Join;

		switch (filter.getOperator()) {
			case BETWEEN:
				return criteriaBuilder.between(
						(Expression<Comparable<Object>>) path,
						criteriaBuilder.literal((Comparable<Object>) filter.getParameters()[0]),
						criteriaBuilder.literal((Comparable<Object>) filter.getParameters()[1]));
			case GREATER:
				return criteriaBuilder.greaterThan(
						(Expression<Comparable<Object>>) path,
						criteriaBuilder.literal((Comparable<Object>) filter.getParameters()[0]));
			case GREATER_EQUAL:
				return criteriaBuilder.greaterThanOrEqualTo(
						(Expression<Comparable<Object>>) path,
						criteriaBuilder.literal((Comparable<Object>) filter.getParameters()[0]));
			case LESS:
				return criteriaBuilder.lessThan(
						(Expression<Comparable<Object>>) path,
						criteriaBuilder.literal((Comparable<Object>) filter.getParameters()[0]));
			case LESS_EQUAL:
				return criteriaBuilder.lessThanOrEqualTo(
						(Expression<Comparable<Object>>) path,
						criteriaBuilder.literal((Comparable<Object>) filter.getParameters()[0]));
				// CHECKSTYLE:OFF
			case EQUAL: {
				// CHECKSTYLE:ON
				Expression<?> expr = path;
				Object arg = filter.getParameters()[0];
				if (arg instanceof String && path.getJavaType() == String.class) {
					String s = (String) arg;
					if (caseInsensitive) {
						expr = criteriaBuilder.upper((Expression<String>) expr);
						s = s.toUpperCase();
						arg = s;
					}
					if (s.contains("*") || s.contains("%")) {
						// like queries for collection attributes cause duplicate results
						query.distinct(query.isDistinct() || isJoined || isCollection);
						return criteriaBuilder.like((Expression<String>) expr, s.replace('*', '%'));
						// TODO HRW add support for LIKE taxonomy queries
					}
				}
				final Predicate eqPredicate = criteriaBuilder.equal(expr, arg);
				final Predicate lookupPredicate = createLookupPredicate(
						criteriaBuilder,
						query,
						expr,
						arg,
						bean.getJavaType(),
						filter.getPropertyName());
				if (lookupPredicate != null) {
					return criteriaBuilder.or(eqPredicate, lookupPredicate);
				}
				return eqPredicate;
			}
			case EMPTY:
				if (isJoined && ((Attribute<?, ?>) path.getParentPath().getModel()).isCollection()) {
					return criteriaBuilder.isEmpty((Expression<Collection<?>>) path.getParentPath());
				}
				if (isCollection) {
					return criteriaBuilder.isEmpty((Expression<Collection<?>>) path);
				}
				if (path.getJavaType() == String.class) {
					return criteriaBuilder.or(path.isNull(), criteriaBuilder.equal(path, ""));
				}
				return path.isNull();
				// CHECKSTYLE:OFF
			case CONTAINS_ANY: {
				// CHECKSTYLE:ON
				Expression<?> expr = path;
				Collection<?> params = Arrays.asList(filter.getParameters());
				if (caseInsensitive && path.getJavaType() == String.class) {
					final Collection<String> newParams = new HashSet<String>();
					for (final Object p : params) {
						newParams.add(String.valueOf(p).toUpperCase());
					}
					params = newParams;
					expr = criteriaBuilder.upper((Expression<String>) path);
				}
				final Predicate lookupPredicate = createLookupPredicate(
						criteriaBuilder,
						query,
						expr,
						params,
						bean.getJavaType(),
						filter.getPropertyName());
				if (lookupPredicate != null) {
					return criteriaBuilder.or(expr.in(params), lookupPredicate);
				}
				return expr.in(params);
			}
				// CHECKSTYLE:OFF
			case CONTAINS_ALL: {
				// CHECKSTYLE:ON
				final Collection<?> params = Arrays.asList(filter.getParameters());
				final Collection<Object> newParams = new HashSet<Object>();
				final boolean toUpper = caseInsensitive && path.getJavaType() == String.class;
				for (final Object p : params) {
					if (p != null) {
						if (toUpper) {
							newParams.add(p.toString().toUpperCase());
						}
						else {
							newParams.add(p);
						}
					}
				}
				final Subquery<Long> subquery = query.subquery(Long.class);
				subquery.select(criteriaBuilder.count(criteriaBuilder.literal(1))).where(
						(toUpper ? criteriaBuilder.upper((Expression<String>) path) : path).in(newParams));
				return criteriaBuilder.ge(subquery, newParams.size());
				// TODO HRW add support for CONTAINS_ALL taxonomy queries
			}
			default:
				throw new IllegalArgumentException("unsupported operator: " + filter.getOperator());
		}
	}

	@SuppressWarnings("unchecked")
	private Predicate createLookupPredicate(
		final CriteriaBuilder criteriaBuilder,
		final CriteriaQuery<?> query,
		final Expression<?> expr,
		final Object arg,
		final Class<?> beanClass,
		final String propertyName) {

		final PropertyDescriptor descriptor;
		try {
			descriptor = new PropertyDescriptor(propertyName, beanClass, "is"
				+ propertyName.substring(0, 1).toUpperCase(Locale.ENGLISH)
				+ propertyName.substring(1), null);
		}
		catch (final IntrospectionException e) {
			return null;
		}
		final Method readMethod = descriptor.getReadMethod();
		final LookupHierarchy lookup = readMethod.getAnnotation(LookupHierarchy.class);
		if (lookup == null) {
			return null;
		}

		final Subquery<Object> subquery = query.subquery(Object.class);
		final Root<?> lookupBean = subquery.from(lookup.entityClass());
		Expression<?> valuePath = lookupBean.get(lookup.valueAttribute());
		Expression<?> ancestorValuePath = lookupBean.get(lookup.ancestorsAttribute()).get(lookup.valueAttribute());
		if (caseInsensitive && valuePath.getJavaType() == String.class) {
			valuePath = criteriaBuilder.upper((Expression<String>) valuePath);
			ancestorValuePath = criteriaBuilder.upper((Expression<String>) ancestorValuePath);
		}
		subquery.select((Expression<Object>) valuePath);
		if (arg instanceof Collection) {
			subquery.where(ancestorValuePath.in((Collection<?>) arg));
		}
		else {
			subquery.where(criteriaBuilder.equal(ancestorValuePath, arg));
		}
		return expr.in(subquery);
	}

	private Predicate createFilterPredicate(
		final CriteriaBuilder criteriaBuilder,
		final Root<?> bean,
		final CriteriaQuery<?> query,
		final IBooleanFilter filter,
		final PARAM_TYPE parameter) {
		final List<Predicate> predicates = new LinkedList<Predicate>();
		for (final IFilter subFilter : filter.getFilters()) {
			final Predicate predicate = createFilterPredicate(criteriaBuilder, bean, query, subFilter, parameter);
			predicates.add(predicate);
		}
		if (filter.getOperator() == BooleanOperator.AND) {
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		}
		return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
	}

}