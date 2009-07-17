package org.ant4eclipse.jdt.ecj;

import org.ant4eclipse.core.Ant4EclipseConfigurator;
import org.ant4eclipse.core.logging.A4ELogging;

import org.ant4eclipse.jdt.ant.CompilerArguments;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.compilers.DefaultCompilerAdapter;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.resources.FileResource;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * Implements a javac compiler adapter for the eclipse compiler for java (ecj). The usage of the ecj has several
 * advantages, e.g. support of access restrictions, multiple source folders.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class JDTCompilerAdapter extends DefaultCompilerAdapter {

  /** the refid key for the additional compiler arguments */
  private static final String COMPILER_ARGS_REFID_KEY = "compiler.args.refid";

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings( { "deprecation", "unchecked" })
  public boolean execute() {

    // configure ant4eclipse
    Ant4EclipseConfigurator.configureAnt4Eclipse(getProject());

    // Fetch the CompilerArguments
    String compilerArgsRefid = null;
    final String[] currentCompilerArgs = getJavac().getCurrentCompilerArgs();
    for (final String compilerArg : currentCompilerArgs) {
      if (compilerArg.startsWith(COMPILER_ARGS_REFID_KEY)) {
        final String[] args = compilerArg.split("=");
        compilerArgsRefid = args[1];
      }
    }

    CompilerArguments compilerArguments = (CompilerArguments) getProject().getReference(compilerArgsRefid);

    if (compilerArguments == null) {
      // TODO: NLS
      throw new RuntimeException();
    }

    // source path is not supported!
    if (getJavac().getSourcepath() != null) {
      // TODO: NLS
      throw new BuildException("getJavac().getSourcepath() != null");
    }

    // get the files to compile
    final List<SourceFile> sourceFiles = new LinkedList<SourceFile>();
    for (final File file : getJavac().getFileList()) {

      // get the source folder
      final File sourceFolder = getSourceFolder(file);

      // get the relative source file name
      final String sourceFileName = file.getAbsolutePath().substring(
          sourceFolder.getAbsolutePath().length() + File.separator.length());

      sourceFiles.add(new SourceFile(sourceFolder, sourceFileName, compilerArguments.getOutputFolder(sourceFolder)));
    }

    // create EcjAdapter
    final EcjAdapter ejcAdapter = EcjAdapter.Factory.create();

    // Create compiler options
    // TODO
    final CompilerOptions compilerOptions = new CompilerOptions();
    compilerOptions.complianceLevel = ClassFileConstants.JDK1_5;
    compilerOptions.sourceLevel = ClassFileConstants.JDK1_5;
    compilerOptions.targetJDK = ClassFileConstants.JDK1_5;

    // create CompileJobDescription
    final DefaultCompileJobDescription compileJobDescription = new DefaultCompileJobDescription();
    compileJobDescription.setSourceFiles(sourceFiles.toArray(new SourceFile[0]));
    compileJobDescription.setCompilerOptions(compilerOptions.getMap());
    compileJobDescription.setClassFileLoader(createClassFileLoader(compilerArguments));

    // compile
    final CompileJobResult compileJobResult = ejcAdapter.compile(compileJobDescription);

    // dump result
    compileJobResult.dumpProblems();
    if (!compileJobResult.succeeded()) {
      throw new BuildException("Compilation was not successful!");
    }

    // return
    return true;
  }

  /**
   * <p>
   * Returns the source folder for the given source file.
   * </p>
   * 
   * @param sourceFile
   *          the source file.
   * @return the source folder
   */
  private File getSourceFolder(final File sourceFile) {

    // get the absoult path
    final String absolutePath = sourceFile.getAbsolutePath();

    // get the list of all source directories
    final String[] srcDirs = getJavac().getSrcdir().list();

    // find the 'right' source directory
    for (final String srcDir : srcDirs) {
      if (absolutePath.startsWith(srcDir) && absolutePath.charAt(srcDir.length()) == File.separatorChar) {
        return new File(srcDir);
      }
    }

    // TODO: NLS
    throw new RuntimeException();
  }

  /**
   * <p>
   * Creates class file loader.
   * </p>
   * 
   * @param compilerArguments
   *          the compiler arguments.
   * @return the class file loader.
   */
  @SuppressWarnings("unchecked")
  private ClassFileLoader createClassFileLoader(final CompilerArguments compilerArguments) {

    // create class file loader list
    final List<ClassFileLoader> classFileLoaderList = new LinkedList<ClassFileLoader>();

    // add boot class loader
    classFileLoaderList.add(createBootClassLoader(compilerArguments));

    // add class loader for class path entries
    for (final Iterator iterator = getJavac().getClasspath().iterator(); iterator.hasNext();) {

      // get the file resource
      final FileResource fileResource = (FileResource) iterator.next();

      if (fileResource.getFile().exists()) {

        // TODO: LIBRARY AND PROJECT
        // create class file loader for file resource
        final ClassFileLoader myclassFileLoader = ClassFileLoaderFactory.createClasspathClassFileLoader(fileResource
            .getFile(), EcjAdapter.LIBRARY);

        // create and add FilteringClassFileLoader is necessary
        if (compilerArguments.hasAccessRestrictions(fileResource.getFile())) {
          classFileLoaderList.add(ClassFileLoaderFactory.createFilteringClassFileLoader(myclassFileLoader,
              compilerArguments.getAccessRestrictions(fileResource.getFile())));
        }
        // else add class file loader
        else {
          classFileLoaderList.add(myclassFileLoader);
        }
      }
    }

    // return the compound class file loader
    return ClassFileLoaderFactory.createCompoundClassFileLoader(classFileLoaderList.toArray(new ClassFileLoader[0]));
  }

  /**
   * <p>
   * Create a boot class loader.
   * </p>
   * 
   * @param compilerArguments
   *          the compiler arguments
   * @return the boot class loader
   */
  @SuppressWarnings("unchecked")
  private ClassFileLoader createBootClassLoader(final CompilerArguments compilerArguments) {

    // get the boot class path as specified in the javac task
    final Path bootclasspath = getJavac().getBootclasspath();

    // create ClassFileLoaders for each entry in the boot class path
    final List<ClassFileLoader> bootClassFileLoaders = new LinkedList<ClassFileLoader>();

    // iterate over the boot class path entries as specified in the ant path
    for (final Iterator<FileResource> iterator = bootclasspath.iterator(); iterator.hasNext();) {

      // get the file resource
      final FileResource fileResource = iterator.next();

      // create class file loader
      if (fileResource.getFile().exists()) {
        final ClassFileLoader classFileLoader = ClassFileLoaderFactory.createClasspathClassFileLoader(fileResource
            .getFile(), EcjAdapter.LIBRARY);
        bootClassFileLoaders.add(classFileLoader);
      }
    }

    // debug
    if (A4ELogging.isDebuggingEnabled()) {
      A4ELogging.debug("Boot class path access restrictions: '%s'", compilerArguments
          .getBootClassPathAccessRestrictions());
    }

    // create compound class file loader
    final ClassFileLoader classFileLoader = ClassFileLoaderFactory.createCompoundClassFileLoader(bootClassFileLoaders
        .toArray(new ClassFileLoader[0]));

    // create FilteringClassFileLoader is necessary
    if (compilerArguments.hasBootClassPathAccessRestrictions()) {
      return ClassFileLoaderFactory.createFilteringClassFileLoader(classFileLoader, compilerArguments
          .getBootClassPathAccessRestrictions());
    }
    // else return compound class file loader
    else {
      return classFileLoader;
    }
  }
}
