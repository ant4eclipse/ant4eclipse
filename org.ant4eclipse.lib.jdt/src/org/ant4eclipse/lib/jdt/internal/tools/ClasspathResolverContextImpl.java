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
package org.ant4eclipse.lib.jdt.internal.tools;

import org.ant4eclipse.lib.jdt.tools.ResolvedClasspathEntry;
import org.ant4eclipse.lib.jdt.tools.container.ClasspathResolverContext;
import org.ant4eclipse.lib.jdt.tools.container.JdtClasspathContainerArgument;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.model.resource.Workspace;

import java.util.EmptyStackException;
import java.util.List;

/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ClasspathResolverContextImpl implements ClasspathResolverContext {

  private ClasspathEntryResolverExecutor _executor;

  private ResolverJob                    _resolverJob;

  private ResolvedClasspathImpl          _resolvedClasspath;

  public ClasspathResolverContextImpl( ClasspathEntryResolverExecutor executor, ResolverJob resolverJob,
      ResolvedClasspathImpl resolvedClasspath ) {
    _executor = executor;
    _resolverJob = resolverJob;
    _resolvedClasspath = resolvedClasspath;
  }

  public ClasspathResolverContextImpl( ClasspathEntryResolverExecutor executor, ResolverJob resolverJob ) {
    this( executor, resolverJob, null );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EclipseProject getCurrentProject() {
    try {
      return _executor.getCurrentProject();
    } catch( EmptyStackException e ) {
      return null;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasCurrentProject() {
    return _executor.hasCurrentProject();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void resolveProjectClasspath( EclipseProject project ) {
    _executor.resolveReferencedProject( project );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final Workspace getWorkspace() {
    return _resolverJob.getWorkspace();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean isCurrentProjectRoot() {
    return _resolverJob.getRootProject().equals( getCurrentProject() );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean isRuntime() {
    return _resolverJob.isRuntimeClasspath();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final List<JdtClasspathContainerArgument> getJdtClasspathContainerArguments() {
    return _resolverJob.getJdtClasspathContainerArguments();
  }

  /**
   * {@inheritDoc}
   */
  // Assure.nonEmpty( "key", key );
  @Override
  public JdtClasspathContainerArgument getJdtClasspathContainerArgument( String key ) {
    List<JdtClasspathContainerArgument> arguments = _resolverJob.getJdtClasspathContainerArguments();
    for( JdtClasspathContainerArgument jdtClasspathContainerArgument : arguments ) {
      if( key.equalsIgnoreCase( jdtClasspathContainerArgument.getKey() ) ) {
        return jdtClasspathContainerArgument;
      }
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean isWorkspaceRelative() {
    return _resolverJob.isRelative();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setBootClasspathEntry( ResolvedClasspathEntry resolvedClasspathEntry ) {
    if( _resolvedClasspath != null ) {
      _resolvedClasspath.addBootClasspathEntry( resolvedClasspathEntry );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addClasspathEntry( ResolvedClasspathEntry resolvedClasspathEntry ) {
    if( _resolvedClasspath != null ) {
      _resolvedClasspath.addClasspathEntry( resolvedClasspathEntry );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addReferencedProjects( EclipseProject eclipseProject ) {
    _executor.addReferencedProject( eclipseProject );
  }
  
} /* ENDCLASS */
