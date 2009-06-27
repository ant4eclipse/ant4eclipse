package org.ant4eclipse.jdt.ecj;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.ant4eclipse.core.Ant4EclipseConfigurator;
import org.ant4eclipse.jdt.ant.CompilerArguments;
import org.ant4eclipse.jdt.ecj.internal.tools.loader.FilteringClassFileLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.compilers.DefaultCompilerAdapter;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.resources.FileResource;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;

/**
 * <p>
 * Implements a javac compiler adapter for the eclipse compiler for java (ecj). The usage of the ecj has several
 * advantages over
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class JDTCompilerAdapter extends DefaultCompilerAdapter {

  /** the refid key for the additional compiler arguments */
  private static final String COMPILER_ARGS_REFID_KEY = "compiler.args.refid";

  /**
   * @see org.apache.tools.ant.taskdefs.compilers.CompilerAdapter#execute()
   */
  @SuppressWarnings( { "deprecation", "unchecked" })
  public boolean execute() {

    // CompilerArguments
    String compilerArgsRefid = null;
    final String[] currentCompilerArgs = getJavac().getCurrentCompilerArgs();
    for (final String compilerArg : currentCompilerArgs) {
      if (compilerArg.startsWith(COMPILER_ARGS_REFID_KEY)) {
        final String[] args = compilerArg.split("=");
        compilerArgsRefid = args[1];
      }
    }

    // configure ant4eclipse
    Ant4EclipseConfigurator.configureAnt4Eclipse(getProject());

    CompilerArguments compilerArguments = (CompilerArguments) getProject().getReference(compilerArgsRefid);

    if (compilerArguments == null) {
      // TODO
      throw new RuntimeException();
    }

    // source path is not supported!
    if (getJavac().getSourcepath() != null) {
      // TODO
      throw new BuildException("getJavac().getSourcepath() != null");
    }

    // Files to Compile
    final List<SourceFile> sourceFiles = new LinkedList<SourceFile>();
    for (final File file : getJavac().getFileList()) {

      final File sourceFolder = getSourceFolder(file);
      final String sourceFileName = file.getAbsolutePath().substring(
          sourceFolder.getAbsolutePath().length() + File.separator.length());

      // TODO
      final SourceFile sourceFile = new SourceFile(sourceFolder, sourceFileName, compilerArguments
          .getOutputFolder(sourceFolder));
      sourceFiles.add(sourceFile);
    }

    final EcjAdapter ejcAdapter = EcjAdapter.Factory.create();

    final DefaultCompileJobDescription compileJobDescription = new DefaultCompileJobDescription();
    compileJobDescription.setSourceFiles(sourceFiles.toArray(new SourceFile[0]));
    // TODO
    final CompilerOptions compilerOptions = new CompilerOptions();
    compilerOptions.complianceLevel = ClassFileConstants.JDK1_5;
    compilerOptions.sourceLevel = ClassFileConstants.JDK1_5;
    compilerOptions.targetJDK = ClassFileConstants.JDK1_5;
    compileJobDescription.setCompilerOptions(compilerOptions.getMap());
    compileJobDescription.setClassFileLoader(createClassFileLoader(compilerArguments));

    final CompileJobResult compileJobResult = ejcAdapter.compile(compileJobDescription);

    compileJobResult.dumpProblems();

    if (!compileJobResult.succeeded()) {
      throw new BuildException("Compilation was not successful!");
    }

    return true;
  }

  private File getSourceFolder(final File sourceFile) {
    final String absolutePath = sourceFile.getAbsolutePath();

    final String[] srcDirs = getJavac().getSrcdir().list();
    for (final String srcDir : srcDirs) {
      if (absolutePath.startsWith(srcDir) && absolutePath.charAt(srcDir.length()) == File.separatorChar) {
        return new File(srcDir);
      }
    }
    // TODO
    throw new RuntimeException();
  }

  @SuppressWarnings("unchecked")
  private ClassFileLoader createClassFileLoader(final CompilerArguments compilerArguments) {

    // create class file loader list
    final List<ClassFileLoader> classFileLoaderList = new LinkedList<ClassFileLoader>();

    // add boot class loader
    classFileLoaderList.add(createBootClassLoader(compilerArguments));

    // add class loader for class path entries
    final Path classpath = getJavac().getClasspath();
    for (final Iterator iterator = classpath.iterator(); iterator.hasNext();) {
      final FileResource fileResource = (FileResource) iterator.next();
      if (fileResource.getFile().exists()) {
        // TODO: LIBRARY AND PROJECT
        final ClassFileLoader myclassFileLoader = ClassFileLoaderFactory.createClasspathClassFileLoader(fileResource
            .getFile(), EcjAdapter.LIBRARY);

        if (compilerArguments.hasAccessRestrictions(fileResource.getFile())) {
          classFileLoaderList.add(new FilteringClassFileLoader(myclassFileLoader, compilerArguments
              .getAccessRestrictions(fileResource.getFile())));
        } else {
          classFileLoaderList.add(myclassFileLoader);
        }
      }
    }

    // return the compound class file loader
    return ClassFileLoaderFactory.createCompoundClassFileLoader(classFileLoaderList.toArray(new ClassFileLoader[0]));
  }

  /**
   * <p>
   * </p>
   * 
   * @param compilerArguments
   * @return
   */
  @SuppressWarnings("unchecked")
  private ClassFileLoader createBootClassLoader(final CompilerArguments compilerArguments) {
    final Path bootclasspath = getJavac().getBootclasspath();

    final List<ClassFileLoader> bootClassFileLoaders = new LinkedList<ClassFileLoader>();

    for (final Iterator<FileResource> iterator = bootclasspath.iterator(); iterator.hasNext();) {
      final FileResource fileResource = iterator.next();
      if (fileResource.getFile().exists()) {
        final ClassFileLoader classFileLoader = ClassFileLoaderFactory.createClasspathClassFileLoader(fileResource
            .getFile(), EcjAdapter.LIBRARY);
        bootClassFileLoaders.add(classFileLoader);
      }
    }

    // System.err.println(compilerArguments.getBootClassPathAccessRestrictions());

    final ClassFileLoader classFileLoader = ClassFileLoaderFactory.createCompoundClassFileLoader(bootClassFileLoaders
        .toArray(new ClassFileLoader[0]));
    if (compilerArguments.hasBootClassPathAccessRestrictions()) {
      return new FilteringClassFileLoader(classFileLoader, compilerArguments.getBootClassPathAccessRestrictions());
    } else {
      return classFileLoader;
    }
  }
}
