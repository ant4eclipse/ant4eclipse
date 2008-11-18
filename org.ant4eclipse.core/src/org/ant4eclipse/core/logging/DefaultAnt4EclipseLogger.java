/**********************************************************************
 * Copyright (c) 2005-2008 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.core.logging;

import org.ant4eclipse.core.util.MessageCreator;

public class DefaultAnt4EclipseLogger implements Ant4EclipseLogger {

	public static final int LOG_LEVEL_ERROR = 0;

	public static final int LOG_LEVEL_WARN = 1;

	public static final int LOG_LEVEL_INFO = 2;

	public static final int LOG_LEVEL_DEBUG /* ANT category: verbose */= 3;

	public static final int LOG_LEVEL_TRACE /* ANT category: debug */= 4;

	private static String[] LEVEL_DESCIRPTION = new String[] { "ERROR", "WARN",
			"INFO", "DEBUG", "TRACE" };

	private final int _logLevel;

	/**
	 * @param logLevel
	 */
	public DefaultAnt4EclipseLogger(final int logLevel) {
		this._logLevel = logLevel;
	}

	public DefaultAnt4EclipseLogger() {
		this._logLevel = LOG_LEVEL_TRACE;
	}

	/**
	 * @see org.ant4eclipse.core.logging.Ant4EclipseLogger#isDebuggingEnabled()
	 */
	public boolean isDebuggingEnabled() {
		return this._logLevel >= LOG_LEVEL_DEBUG;
	}

	/**
	 * @see org.ant4eclipse.core.logging.Ant4EclipseLogger#isTraceingEnabled()
	 */
	public boolean isTraceingEnabled() {
		return this._logLevel >= LOG_LEVEL_TRACE;
	}

	/**
	 * @see org.ant4eclipse.core.logging.Ant4EclipseLogger#setContext(java.lang.Object)
	 */
	public void setContext(final Object context) {
		//
	}

	public void debug(final String msg, final Object obj) {
		if (isDebuggingEnabled()) {
			debug(MessageCreator.createMessage(msg, obj));
		}
	}

	public void debug(final String msg, final Object[] args) {
		if (isDebuggingEnabled()) {
			debug(MessageCreator.createMessage(msg, args));
		}
	}

	public void debug(final String msg) {
		if (isDebuggingEnabled()) {
			log(msg, LOG_LEVEL_DEBUG);
		}
	}

	public void error(final String msg, final Object obj) {
		error(MessageCreator.createMessage(msg, obj));
	}

	public void error(final String msg, final Object[] args) {
		error(MessageCreator.createMessage(msg, args));
	}

	public void error(final String msg) {
		log(msg, LOG_LEVEL_ERROR);
	}

	public void info(final String msg, final Object obj) {
		error(MessageCreator.createMessage(msg, obj));
	}

	public void info(final String msg, final Object[] args) {
		error(MessageCreator.createMessage(msg, args));
	}

	public void info(final String msg) {
		log(msg, LOG_LEVEL_INFO);
	}

	public void trace(final String msg, final Object[] args) {
		if (isTraceingEnabled()) {
			trace(MessageCreator.createMessage(msg, args));
		}
	}

	public void trace(final String msg, final Object obj) {
		if (isTraceingEnabled()) {
			trace(MessageCreator.createMessage(msg, obj));
		}
	}

	public void trace(final String msg) {
		if (isTraceingEnabled()) {
			log(msg, LOG_LEVEL_TRACE);
		}
	}

	public void warn(final String msg, final Object obj) {
		warn(MessageCreator.createMessage(msg, obj));
	}

	public void warn(final String msg, final Object[] args) {
		warn(MessageCreator.createMessage(msg, args));
	}

	public void warn(final String msg) {
		log(msg, LOG_LEVEL_WARN);
	}

	protected void log(final String msg, final int level) {
		System.out.println("[" + LEVEL_DESCIRPTION[level] + "] " + msg);
	}
}
