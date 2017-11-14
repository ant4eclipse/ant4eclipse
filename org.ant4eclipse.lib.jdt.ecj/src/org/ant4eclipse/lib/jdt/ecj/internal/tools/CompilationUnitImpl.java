/**********************************************************************
 * Copyright (c) 2005-2009 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.lib.jdt.ecj.internal.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.jdt.ecj.EcjExceptionCodes;
import org.ant4eclipse.lib.jdt.ecj.SourceFile;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;

/**
 * <p>Adapter class for providing java source files to the eclipse java compiler.</p>
 *
 * @author  Nils Hartmann (nils@nilshartmann.net)
 * @author  Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class CompilationUnitImpl implements ICompilationUnit {

	/**
	 * the constant JAVA_FILE_POSTFIX
	 */
	private static final String JAVA_FILE_POSTFIX = ".java";

	/**
	 * -
	 */
	private SourceFile _sourceFile;

	/**
	 * the file name, relative to the source folder
	 */
	private char[] _fileName;

	/**
	 * the name of the top level public type, e.g. {Hashtable}
	 */
	private char[] _mainTypeName;

	/**
	 * the name of the package , e.g. {java, lang}
	 */
	private char[][] _packageName;

	/**
	 * <p>Creates a new instance of type {@link CompilationUnitImpl}.</p>
	 *
	 * @param  sourceFile  the source file
	 */
	public CompilationUnitImpl(SourceFile sourceFile) {
		Assure.notNull("sourceFile", sourceFile);

		this._sourceFile = sourceFile;

		this._fileName = this._sourceFile.getSourceFileName().toCharArray();

		// compute qualified name
		String qualifiedTypeName = getQualifiedTypeName(this._sourceFile.getSourceFileName());

		// compute package and main type name
		int v = qualifiedTypeName.lastIndexOf('.');
		this._mainTypeName = qualifiedTypeName.substring(v + 1).toCharArray();
		if ((v > 0) && (v < qualifiedTypeName.length())) {
			String packageName = qualifiedTypeName.substring(0, v);
			StringTokenizer packages = new StringTokenizer(packageName, ".");
			this._packageName = new char[packages.countTokens()][];
			for (int i = 0; i < this._packageName.length; i++) {
				this._packageName[i] = packages.nextToken().toCharArray();
			}
		} else {
			this._packageName = new char[0][];
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public final char[] getMainTypeName() {
		return this._mainTypeName;

	}

	/**
	 * {@inheritDoc}
	 */
	public final char[][] getPackageName() {
		return this._packageName;
	}

	/**
	 * {@inheritDoc}
	 */
	public final char[] getFileName() {
		return this._fileName;
	}

	/**
	 * {@inheritDoc}
	 */
	public final char[] getContents() {
		String filename = new String(this._fileName);
		File sourceFile = new File(this._sourceFile.getSourceFolder(), filename);

		StringBuilder result = new StringBuilder();
		BufferedReader in = null;

		try {
			in =
				new BufferedReader(
					new InputStreamReader(
						new FileInputStream(sourceFile),
						this._sourceFile.getEncoding()
					)
				);

			String str;
			while ((str = in.readLine()) != null) {
				result.append(str);
				result.append("\n");
			}
		} catch (UnsupportedEncodingException e) {
			throw new Ant4EclipseException(
				e,
				EcjExceptionCodes.UNABLE_TO_READ_COMPILATION_CONTENT_EXCEPTION,
				filename,
				this._sourceFile.getSourceFolder(),
				this._sourceFile.getEncoding()
			);
		} catch (IOException e) {
			throw new Ant4EclipseException(
				e,
				EcjExceptionCodes.UNABLE_TO_READ_COMPILATION_CONTENT_EXCEPTION,
				filename,
				this._sourceFile.getSourceFolder(),
				this._sourceFile.getEncoding()
			);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception ex) {
					// ignore
				}
			}
		}
		return result.toString().toCharArray();
	}

	/**
	 * <p>
	 * </p>
	 *
	 * @return
	 */
	public SourceFile getSourceFile() {
		return this._sourceFile;
	}

	/**
	 * <p>Returns the qualified type name for the given type name.</p>
	 *
	 * @param   fileName  the file name to resolve
	 *
	 * @return  the qualified type name for the given type name.
	 */
	private String getQualifiedTypeName(String fileName) {
		if (fileName.toLowerCase().endsWith(JAVA_FILE_POSTFIX)) {
			return fileName.substring(0, fileName.length() - 5).replace(File.separatorChar, '.');
		} else {
			return fileName.replace(File.separatorChar, '.');
		}
	}

	public boolean ignoreOptionalProblems() {
		return false;
	}
}
