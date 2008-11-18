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
package org.ant4eclipse.jdt.model.userlibrary;

import java.util.Vector;

import org.ant4eclipse.core.Assert;


/**
 * Description of an user library.
 * 
 * @author Daniel Kasmeroglu (daniel.kasmeroglu@kasisoft.net)
 */
public class UserLibrary {

  /**
   * The name of the user library
   */
	private String _name;

  /**
   * Represents this a system library (i.e. a library added to the boot classpath)
   */
	private boolean _systemlibrary;

  /**
   * The content of this library.
   * @see Archive
   */
	private Vector _archives;

	/**
	 * Creates a new user library entry with a specific name.
	 * 
	 * @param name
	 *            The name of the user library.
	 * @param syslib
	 *            true <=> This library affects the boot class path.
	 */
	public UserLibrary(String name, boolean syslib) {
		Assert.notNull(name);

		_name = name;
		_systemlibrary = syslib;
		_archives = new Vector();
	}

	/**
	 * Returns the name of this user library.
	 * 
	 * @return The name of this user library.
	 */
	public String getName() {
		return (_name);
	}

	/**
	 * Returns true if this library affects the boot class path.
	 * 
	 * @return true <=> This library affects the boot class path.
	 */
	public boolean isSystemLibrary() {
		return (_systemlibrary);
	}

	/**
	 * Adds an archive to this library entry.
	 * 
	 * @param arc
	 *            The archive that will be added.
	 */
	public void addArchive(Archive arc) {
		Assert.notNull(arc);

		_archives.add(arc);
	}

	/**
	 * Returns a list of archives that are registered with this library entry.
	 * 
	 * @return A list of archives that are registered with this library entry.
	 */
	public Archive[] getArchives() {
		Archive[] result = new Archive[_archives.size()];
		_archives.toArray(result);
		return (result);
	}

} /* ENDCLASS */
