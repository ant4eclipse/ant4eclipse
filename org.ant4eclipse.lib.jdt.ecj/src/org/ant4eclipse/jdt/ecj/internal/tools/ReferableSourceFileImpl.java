package org.ant4eclipse.jdt.ecj.internal.tools;

import org.ant4eclipse.jdt.ecj.ReferableSourceFile;

import org.ant4eclipse.lib.core.Assure;
import org.eclipse.jdt.internal.compiler.env.AccessRestriction;

import java.io.File;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ReferableSourceFileImpl extends SourceFileImpl implements ReferableSourceFile {

  /** - */
  private DefaultReferableType _referableType = new DefaultReferableType();

  /**
   * <p>
   * Creates a new instance of type {@link ReferableSourceFileImpl}.
   * </p>
   * 
   * @param sourceFolder
   * @param sourceFileName
   */
  public ReferableSourceFileImpl(File sourceFolder, String sourceFileName, String libraryLocation, byte libraryType) {
    super(sourceFolder, sourceFileName);
    Assure.paramNotNull("libraryLocation", libraryLocation);
    this._referableType.setLibraryLocation(libraryLocation);
    this._referableType.setLibraryType(libraryType);
  }

  /**
   * {@inheritDoc}
   */
  public final AccessRestriction getAccessRestriction() {
    return this._referableType.getAccessRestriction();
  }

  /**
   * {@inheritDoc}
   */
  public String getLibraryLocation() {
    return this._referableType.getLibraryLocation();
  }

  /**
   * {@inheritDoc}
   */
  public byte getLibraryType() {
    return this._referableType.getLibraryType();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean hasAccessRestriction() {
    return this._referableType.hasAccessRestriction();
  }

  /**
   * {@inheritDoc}
   */
  public final void setAccessRestriction(AccessRestriction accessRestriction) {
    this._referableType.setAccessRestriction(accessRestriction);
  }
}
