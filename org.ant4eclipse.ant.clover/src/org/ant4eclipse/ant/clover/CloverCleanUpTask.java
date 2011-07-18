package org.ant4eclipse.ant.clover;

import java.util.Iterator;

import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.jdt.internal.model.project.RawClasspathEntryImpl;
import org.ant4eclipse.lib.jdt.model.ClasspathEntry;
import org.ant4eclipse.lib.jdt.model.project.RawClasspathEntry;
import org.ant4eclipse.lib.pde.model.buildproperties.PluginBuildProperties.Library;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class CloverCleanUpTask extends AbstractCloverTask {

  /**
   * {@inheritDoc}
   */
  @Override
  protected void doExecute() {

    //
    for (ClasspathEntry classpathEntry : getJavaProjectRole().getInternalEclipseClasspathEntries()) {

      if (classpathEntry.getEntryKind() == RawClasspathEntry.CPE_SOURCE) {

        //
        String src = classpathEntry.getPath();

        if (src.endsWith(POSTFIX_CLOVER_INSTR)) {
          String newSrc = src.substring(0, src.length() - POSTFIX_CLOVER_INSTR.length());

          // delete
          if (getEclipseProject().getChild(src).exists()) {
            Utilities.delete(getEclipseProject().getChild(src));
          }

          // re-set
          ((RawClasspathEntryImpl) classpathEntry).setPath(newSrc);

          //
          for (Library library : getPluginBuildProperties().getLibraries()) {

            for (int i = 0; i < library.getSource().length; i++) {
              String sourceEntry = library.getSource()[i];

              if (sourceEntry.equals(src)) {
                library.getSource()[i] = newSrc;
              }
            }
          }
        }
      }
    }

    for (Iterator<ClasspathEntry> iterator = getJavaProjectRole().getInternalEclipseClasspathEntries().iterator(); iterator
        .hasNext();) {

      ClasspathEntry entry = iterator.next();

      if (entry.getPath().equals(getCloverPath())) {
        iterator.remove();
      }
    }
  }
}
