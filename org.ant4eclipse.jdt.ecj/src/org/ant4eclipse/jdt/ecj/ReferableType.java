package org.ant4eclipse.jdt.ecj;

import org.eclipse.jdt.internal.compiler.env.AccessRestriction;

/**
 * <p>
 * A {@link ReferableType} is a class file or a source file that can be referred from a project that should be build.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface ReferableType {

  /**
   * <p>
   * Returns the type of the bundle this {@link ReferableType} was loaded from. Possible values are
   * {@link EcjAdapter#LIBRARY} and {@link EcjAdapter#PROJECT}.
   * </p>
   * 
   * @return the type of the bundle this class file was loaded from.
   */
  byte getLibraryType();

  /**
   * <p>
   * Returns of location of the bundle this {@link ReferableType} was loaded from.
   * </p>
   * 
   * @return the location of the bundle this {@link ReferableType} was loaded from.
   */
  String getLibraryLocation();

  /**
   * <p>
   * Returns whether there exists an access restriction for this class file or not.
   * </p>
   * 
   * @return whether there exists an access restriction for this class file or not.
   */
  boolean hasAccessRestriction();

  /**
   * <p>
   * Returns the access restriction for this class file or <code>null</code> if no access restriction exists.
   * </p>
   * 
   * @return the access restriction for this class file or <code>null</code> if no access restriction exists.
   */
  AccessRestriction getAccessRestriction();
}
