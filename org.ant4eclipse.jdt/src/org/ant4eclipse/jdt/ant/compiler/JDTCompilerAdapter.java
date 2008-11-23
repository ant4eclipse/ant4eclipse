package org.ant4eclipse.jdt.ant.compiler;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.ant4eclipse.ant.Ant4EclipseConfiguration;
import org.ant4eclipse.jdt.tools.ejc.CompileJobResult;
import org.ant4eclipse.jdt.tools.ejc.EjcAdapter;
import org.ant4eclipse.jdt.tools.ejc.CompileJobDescription.SourceFile;
import org.ant4eclipse.jdt.tools.ejc.loader.ClassFileLoader;
import org.ant4eclipse.jdt.tools.ejc.loader.ClassFileLoaderFactory;
import org.ant4eclipse.jdt.tools.internal.ejc.loader.FilteringClassFileLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.compilers.DefaultCompilerAdapter;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.resources.FileResource;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;

public class JDTCompilerAdapter extends DefaultCompilerAdapter {

  public boolean execute() {

    Ant4EclipseConfiguration.configureAnt4Eclipse(getProject());

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
      System.err.println("'" + sourceFolder + "'");
      System.err.println("'" + sourceFileName + "'");
      final SourceFile sourceFile = new SourceFile(sourceFolder, sourceFileName, new File(
          "U:\\environments\\ant4eclipse-environment\\workspace\\ejc-test-dummy-project\\bin"));
      sourceFiles.add(sourceFile);
    }

    final EjcAdapter ejcAdapter = EjcAdapter.Factory.create();

    final AntCompileJobDescription compileJobDescription = new AntCompileJobDescription();
    compileJobDescription.setSourceFiles(sourceFiles.toArray(new SourceFile[0]));
    // TODO
    compileJobDescription.setCompilerOptions(new CompilerOptions().getMap());

    compileJobDescription.setClassFileLoader(createClassFileLoader());

    final CompileJobResult compileJobResult = ejcAdapter.compile(compileJobDescription);

    compileJobResult.dumpProblems();

    if (!compileJobResult.succeeded()) {
      throw new BuildException();
    }

    // CompilerArguments
    final String[] currentCompilerArgs = getJavac().getCurrentCompilerArgs();
    for (final String string : currentCompilerArgs) {
      System.err.println(" -> " + string);
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

  private ClassFileLoader createClassFileLoader() {

    final List<ClassFileLoader> classFileLoaderList = new LinkedList<ClassFileLoader>();

    final Path bootclasspath = getJavac().getBootclasspath();
    System.err.println(Arrays.asList(bootclasspath.list()));

    final List<ClassFileLoader> bootClassFileLoader = new LinkedList<ClassFileLoader>();

    for (final Iterator<FileResource> iterator = bootclasspath.iterator(); iterator.hasNext();) {
      final FileResource fileResource = iterator.next();
      if (fileResource.getFile().exists()) {
        final ClassFileLoader classFileLoader = ClassFileLoaderFactory.createClasspathClassFileLoader(fileResource
            .getFile(), EjcAdapter.LIBRARY);
        bootClassFileLoader.add(classFileLoader);
      }
    }

    final ClassFileLoader classFileLoader = ClassFileLoaderFactory.createCompoundClassFileLoader(bootClassFileLoader
        .toArray(new ClassFileLoader[0]));

    final String pattern = "+java/**/*;+javax/accessibility/*;+javax/activity/*;+javax/crypto/*;+javax/crypto/interfaces/*;+javax/crypto/spec/*;+javax/imageio/*;+javax/imageio/event/*;+javax/imageio/metadata/*;+javax/imageio/plugins/bmp/*;+javax/imageio/plugins/jpeg/*;+javax/imageio/spi/*;+javax/imageio/stream/*;+javax/management/*;+javax/management/loading/*;+javax/management/modelmbean/*;+javax/management/monitor/*;+javax/management/openmbean/*;+javax/management/relation/*;+javax/management/remote/*;+javax/management/remote/rmi/*;+javax/management/timer/*;+javax/naming/*;+javax/naming/directory/*;+javax/naming/event/*;+javax/naming/ldap/*;+javax/naming/spi/*;+javax/net/*;+javax/net/ssl/*;+javax/print/*;+javax/print/attribute/*;+javax/print/attribute/standard/*;+javax/print/event/*;+javax/rmi/*;+javax/rmi/CORBA/*;+javax/rmi/ssl/*;+javax/security/auth/*;+javax/security/auth/callback/*;+javax/security/auth/kerberos/*;+javax/security/auth/login/*;+javax/security/auth/spi/*;+javax/security/auth/x500/*;+javax/security/cert/*;+javax/security/sasl/*;+javax/sound/midi/*;+javax/sound/midi/spi/*;+javax/sound/sampled/*;+javax/sound/sampled/spi/*;+javax/sql/*;+javax/sql/rowset/*;+javax/sql/rowset/serial/*;+javax/sql/rowset/spi/*;+javax/swing/*;+javax/swing/border/*;+javax/swing/colorchooser/*;+javax/swing/event/*;+javax/swing/filechooser/*;+javax/swing/plaf/*;+javax/swing/plaf/basic/*;+javax/swing/plaf/metal/*;+javax/swing/plaf/multi/*;+javax/swing/plaf/synth/*;+javax/swing/table/*;+javax/swing/text/*;+javax/swing/text/html/*;+javax/swing/text/html/parser/*;+javax/swing/text/rtf/*;+javax/swing/tree/*;+javax/swing/undo/*;+javax/transaction/*;+javax/transaction/xa/*;+javax/xml/*;+javax/xml/datatype/*;+javax/xml/namespace/*;+javax/xml/parsers/*;+javax/xml/transform/*;+javax/xml/transform/dom/*;+javax/xml/transform/sax/*;+javax/xml/transform/stream/*;+javax/xml/validation/*;+javax/xml/xpath/*;+org/ietf/jgss/*;+org/omg/CORBA/*;+org/omg/CORBA_2_3/*;+org/omg/CORBA_2_3/portable/*;+org/omg/CORBA/DynAnyPackage/*;+org/omg/CORBA/ORBPackage/*;+org/omg/CORBA/portable/*;+org/omg/CORBA/TypeCodePackage/*;+org/omg/CosNaming/*;+org/omg/CosNaming/NamingContextExtPackage/*;+org/omg/CosNaming/NamingContextPackage/*;+org/omg/Dynamic/*;+org/omg/DynamicAny/*;+org/omg/DynamicAny/DynAnyFactoryPackage/*;+org/omg/DynamicAny/DynAnyPackage/*;+org/omg/IOP/*;+org/omg/IOP/CodecFactoryPackage/*;+org/omg/IOP/CodecPackage/*;+org/omg/Messaging/*;+org/omg/PortableInterceptor/*;+org/omg/PortableInterceptor/ORBInitInfoPackage/*;+org/omg/PortableServer/*;+org/omg/PortableServer/CurrentPackage/*;+org/omg/PortableServer/POAManagerPackage/*;+org/omg/PortableServer/POAPackage/*;+org/omg/PortableServer/portable/*;+org/omg/PortableServer/ServantLocatorPackage/*;+org/omg/SendingContext/*;+org/omg/stub/java/rmi/*;+org/w3c/dom/*;+org/w3c/dom/bootstrap/*;+org/w3c/dom/css/*;+org/w3c/dom/events/*;+org/w3c/dom/html/*;+org/w3c/dom/ls/*;+org/w3c/dom/ranges/*;+org/w3c/dom/stylesheets/*;+org/w3c/dom/traversal/*;+org/w3c/dom/views/*;+org/xml/sax/*;+org/xml/sax/ext/*;+org/xml/sax/helpers/*;-**/*";

    final ClassFileLoader filteringClassFileLoader = new FilteringClassFileLoader(classFileLoader, pattern);

    // TODO: LIBRARY AND PROJECT

    classFileLoaderList.add(filteringClassFileLoader);

    final Path classpath = getJavac().getClasspath();
    System.err.println(Arrays.asList(classpath.list()));
    for (final Iterator iterator = classpath.iterator(); iterator.hasNext();) {
      final FileResource fileResource = (FileResource) iterator.next();

      if (fileResource.getFile().exists()) {
        // TODO: LIBRARY AND PROJECT
        final ClassFileLoader myclassFileLoader = ClassFileLoaderFactory.createClasspathClassFileLoader(fileResource
            .getFile(), EjcAdapter.LIBRARY);
        System.err.println("Adding " + fileResource.getFile());
        classFileLoaderList.add(myclassFileLoader);
      }
    }

    return ClassFileLoaderFactory.createCompoundClassFileLoader(classFileLoaderList.toArray(new ClassFileLoader[0]));
  }
}
