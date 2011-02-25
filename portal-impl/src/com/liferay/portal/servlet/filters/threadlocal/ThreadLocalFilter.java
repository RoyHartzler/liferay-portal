/**
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.servlet.filters.threadlocal;

import com.liferay.portal.kernel.servlet.TryFinallyFilter;
import com.liferay.portal.kernel.util.CentralizedThreadLocal;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.InitialThreadLocal;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.servlet.filters.BasePortalFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Brian Wing Shun Chan
 */
public class ThreadLocalFilter
	extends BasePortalFilter implements TryFinallyFilter {

	public static final boolean ENABLED = GetterUtil.getBoolean(
		PropsUtil.get(ThreadLocalFilter.class.getName()));

	public static boolean isInUse() {
		if (_useCountThreadLocal.get() > 0) {
			return true;
		}
		else {
			return false;
		}
	}

	public void doFilterFinally(
		HttpServletRequest request, HttpServletResponse response,
		Object ojbect) {

		_useCountThreadLocal.set(_useCountThreadLocal.get() - 1);

		CentralizedThreadLocal.clearPeriodicalThreadLocals();
	}

	public Object doFilterTry(
		HttpServletRequest request, HttpServletResponse response) {

		_useCountThreadLocal.set(_useCountThreadLocal.get() + 1);

		return null;
	}

	private static ThreadLocal<Long> _useCountThreadLocal =
		new InitialThreadLocal<Long>(
			ThreadLocalFilter.class + "_useCountThreadLocal", 0L);

}