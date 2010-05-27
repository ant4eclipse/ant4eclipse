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
package org.ant4eclipse.ant.jdt;

import org.ant4eclipse.ant.jdt.ecj.EcjCompilerAdapter;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.AntTypeDefinition;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ComponentHelper;
import org.apache.tools.ant.taskdefs.Javac;
import org.apache.tools.ant.types.Path;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * <p>
 * This task is an extension to the Javac compiler task which makes use of the {@link EcjCompilerAdapter}. The most
 * important advantage is that the compiler implementation is provided using the classloader of the a4e package which
 * means that the package itself is not required to be provided together with the ant distribution.
 * </p>
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class JdtCompilerTask extends Javac {

  private static final String MSG_SKIPPING_ENHANCEMENT         = "Skipping resource '%s' for the enhancement as it's type couldn't be determined !";

  private static final String MSG_FAILED_TO_ACCESS_CLASSLOADER = "The typedefition for '%s' doesn't provide a usable ClassLoader (%s) !";

  private static final String MSG_INVALID_ATTRIBUTE            = "The attribute 'compiler' for the task '%s' is not allowed to be used.";

  private static final String MSG_ENHANCEMENT_FAILED           = "The task '%s' tried to enhance but failed for the following reason: '%s'. A minimum Ant version 1.8 is required !";

  private boolean             enhance;

  public JdtCompilerTask() {
    super();
    this.enhance = false;
  }

  /**
   * If set to <code>true</code> and under the precondition that ant 1.8 is used the compiler adapter can be used in an
   * environment where the antlib loading mechanism isn't used.
   * 
   * @param newenhance
   *          <code>true</code> <=> Use the ant 1.8 mechanism to provide a classpath for the compiler adapter
   *          instantiation.
   */
  public void setEnhance(boolean newenhance) {
    this.enhance = newenhance;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setCompiler(String compiler) {
    A4ELogging.error(MSG_INVALID_ATTRIBUTE, getTaskName());
  }

  /**
   * Enhances the classpath used for the compiler lookup. This is only possible since ant 1.8 so the enhancement won't
   * have any effect on earlier versions.
   */
  private void enhanceCompilerClasspath() {

    try {

      // get the Path instance first
      Method method = getClass().getMethod("createCompilerClasspath");
      Path path = (Path) method.invoke(this);

      // now we need to access our classpath which has been provided using the taskdef element
      ComponentHelper helper = ComponentHelper.getComponentHelper(getProject());
      AntTypeDefinition typedef = helper.getDefinition(getTaskName());
      if (typedef.getClassLoader() instanceof AntClassLoader) {

        AntClassLoader cl = (AntClassLoader) typedef.getClassLoader();

        // get the classpath and split it
        String classpath = cl.getClasspath();
        String pathsep = getProject().getProperty("path.separator");
        String[] parts = classpath.split(Pattern.quote(pathsep));
        for (String p : parts) {

          // add the classpath entry for the classpath used to lookup the compiler
          File file = new File(p);
          if (file.isDirectory()) {
            path.createPathElement().setPath(file.getAbsolutePath());
          } else if (file.isFile()) {
            path.createPathElement().setLocation(file);
          } else {
            A4ELogging.warn(MSG_SKIPPING_ENHANCEMENT, file.getAbsolutePath());
          }

        }

      } else {
        A4ELogging
            .error(MSG_FAILED_TO_ACCESS_CLASSLOADER, getTaskName(), typedef.getClassLoader().getClass().getName());
      }

    } catch (NoSuchMethodException ex) {
      A4ELogging.error(MSG_ENHANCEMENT_FAILED, getTaskName(), ex.getMessage());
    } catch (IllegalArgumentException ex) {
      A4ELogging.error(MSG_ENHANCEMENT_FAILED, getTaskName(), ex.getMessage());
    } catch (IllegalAccessException ex) {
      A4ELogging.error(MSG_ENHANCEMENT_FAILED, getTaskName(), ex.getMessage());
    } catch (InvocationTargetException ex) {
      A4ELogging.error(MSG_ENHANCEMENT_FAILED, getTaskName(), ex.getMessage());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void execute() throws BuildException {
    if (this.enhance) {
      enhanceCompilerClasspath();
    }
    super.setCompiler(EcjCompilerAdapter.class.getName());
    super.execute();
  }
} /* ENDCLASS */
