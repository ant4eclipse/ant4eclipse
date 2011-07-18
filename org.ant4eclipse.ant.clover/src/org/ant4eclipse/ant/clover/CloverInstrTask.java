package org.ant4eclipse.ant.clover;

import org.ant4eclipse.lib.jdt.internal.model.project.RawClasspathEntryImpl;
import org.ant4eclipse.lib.jdt.model.ClasspathEntry;
import org.ant4eclipse.lib.jdt.model.project.RawClasspathEntry;
import org.ant4eclipse.lib.pde.model.buildproperties.PluginBuildProperties.Library;

import com.cenqua.clover.CloverInstr;

/**
 * <p>
 * Instruments an eclipse project.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class CloverInstrTask extends AbstractCloverTask {

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
        String dest = src + POSTFIX_CLOVER_INSTR;

        String[] cliArgs = { "-jdk15", "-i", getDatabasePath(), "-d",
            getEclipseProject().getChild(dest).getAbsolutePath(), "-s",
            getEclipseProject().getChild(src).getAbsolutePath() };
        int result = CloverInstr.mainImpl(cliArgs);
        if (result != 0) {
          throw new RuntimeException();
          // problem during instrumentation
        }

        if (getEclipseProject().getChild(dest).exists()) {

          //
          ((RawClasspathEntryImpl) classpathEntry).setPath(dest);

          //
          for (Library library : getPluginBuildProperties().getLibraries()) {

            for (int i = 0; i < library.getSource().length; i++) {
              String sourceEntry = library.getSource()[i];

              if (sourceEntry.equals(src)) {
                library.getSource()[i] = dest;
              }
            }
          }
        }
      }
    }

    getJavaProjectRole().getInternalEclipseClasspathEntries().add(
        new RawClasspathEntryImpl(RawClasspathEntry.CPE_LIBRARY, getCloverPath()));
  }

}
