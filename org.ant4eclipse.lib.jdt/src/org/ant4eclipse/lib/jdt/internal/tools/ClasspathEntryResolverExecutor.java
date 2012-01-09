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

import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.jdt.JdtExceptionCode;
import org.ant4eclipse.lib.jdt.internal.tools.classpathentry.ClasspathEntryResolver;
import org.ant4eclipse.lib.jdt.model.ClasspathEntry;
import org.ant4eclipse.lib.jdt.model.project.JavaProjectRole;
import org.ant4eclipse.lib.jdt.model.project.RawClasspathEntry;
import org.ant4eclipse.lib.jdt.tools.container.ClasspathResolverContext;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

/**
 * <p>
 * Helper class that resolves class path entries. The {@link ClasspathEntryResolverExecutor} holds two lists of
 * referenced projects:
 * <ul>
 * <li>The resolved projects list contains all projects that are referenced and transitively resolved, e.g. jdt projects
 * that are referenced through a 'src' class path entry</li>
 * <li>The referenced projects list contains all projects that are only referenced (and transitively resolved), e.g.
 * plug-in projects that are referenced through the 'org.eclipse.pde.core.requiredPlugins' container</li>
 * </ul>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ClasspathEntryResolverExecutor {

  /** stack of 'current projects' */
  private Stack<EclipseProject>    _currentProject;

  /** list with all projects that are (transitively) resolved */
  private List<EclipseProject>     _resolvedProjects;

  /** list with all projects that references. These projects are not transitively resolved */
  private List<EclipseProject>     _referencedProjects;

  /** array that contains all resolvers for raw class path entries * */
  private ClasspathEntryResolver[] _entryResolvers;

  /** the resolver context */
  private ClasspathResolverContext _resolverContext;

  /** indicates if an exception is thrown in the case that a container could not be resolved */
  private boolean                  _failOnNonHandledEntry;

  /**
   * <p>
   * Creates a new instance of type {@link ClasspathEntryResolverExecutor}.
   * </p>
   * 
   * @param failOnNonHandledEntry
   */
  public ClasspathEntryResolverExecutor( boolean failOnNonHandledEntry ) {
    // initialize the executor attributes
    _resolvedProjects = new ArrayList<EclipseProject>();
    _referencedProjects = new ArrayList<EclipseProject>();
    _currentProject = new Stack<EclipseProject>();
    _failOnNonHandledEntry = failOnNonHandledEntry;
  }

  /**
   * <p>
   * Returns the current project or <code>null</code> if no current project is set.
   * </p>
   * 
   * @return the current project or <code>null</code> if no current project is set.
   */
  public final EclipseProject getCurrentProject() {
    try {
      return _currentProject.peek();
    } catch( EmptyStackException e ) {
      return null;
    }
  }

  /**
   * <p>
   * Returns <code>true</code> if a current project is set.
   * </p>
   * 
   * @return <code>true</code> if a current project is set.
   */
  public final boolean hasCurrentProject() {
    return !_currentProject.empty();
  }

  /**
   * <p>
   * Returns all referenced projects. Both lists (resolved and referenced) will be merged.
   * </p>
   * 
   * @return all referenced projects.
   */
  public List<EclipseProject> getReferencedProjects() {

    List<EclipseProject> result = new ArrayList<EclipseProject>();

    result.addAll( _resolvedProjects );

    for( EclipseProject eclipseProject : _referencedProjects ) {
      if( !result.contains( eclipseProject ) ) {
        result.add( eclipseProject );
      }
    }

    return result;
  }

  /**
   * <p>
   * Main entry for resolving a eclipse project.
   * </p>
   * 
   * @param rootProject
   *          the root project
   * @param classpathEntryResolvers
   *          the list of all entry resolvers
   * @param classpathResolverContext
   *          the resolver context
   */
  public final void resolve( EclipseProject rootProject, ClasspathEntryResolver[] classpathEntryResolvers,
      ClasspathResolverContext classpathResolverContext ) {

    // Initialize the ProjectClasspathResolver instance
    _resolvedProjects.clear();
    _currentProject.clear();
    _referencedProjects.clear();

    // set the entry resolvers
    _entryResolvers = classpathEntryResolvers;

    // set the resolver context
    _resolverContext = classpathResolverContext;

    // resolve the class path
    resolveReferencedProject( rootProject );

  }

  /**
   * <p>
   * Adds a referenced project. The project will not (transitively) resolved.
   * </p>
   * 
   * @param project
   *          the project to add.
   */
  // Assure.notNull( "project", project );
  public final void addReferencedProject( EclipseProject project ) {

    // adds the referenced project
    if( !_referencedProjects.contains( project ) ) {
      _referencedProjects.add( project );
    }
  }

  /**
   * <p>
   * Adds a referenced project. The project will (transitively) resolved.
   * </p>
   * 
   * @param project
   *          the project to add.
   */
  // Assure.notNull( "project", project );
  public final void resolveReferencedProject( EclipseProject project ) {

    // detect circular dependencies
    if( _currentProject.contains( project ) ) {
      // TODO it should be configurable if the task fails on circular
      // dependencies
      // TODO detect which projects reference each other
      A4ELogging.warn( "Circular dependency detected! Project: '%s'", project.getFolderName() );
      return;
    }

    // return if project already has been resolved
    if( _resolvedProjects.contains( project ) ) {
      return;
    }

    // add project to the list of all resolved projects
    _resolvedProjects.add( project );

    // push the project to the stack
    _currentProject.push( project );

    // assert raw class path entries
    if( ! project.getRole( JavaProjectRole.class ).hasRawClasspathEntries() ) {
      throw new RuntimeException( String.format(
          "The JDT project '%s' (%s) doesn't contain any class path entries.", 
          project.getFolderName(), project.getFolder().getAbsolutePath()) );
    }
    
    // resolve the class path entries for this project
    resolveClasspathEntries( project.getRole( JavaProjectRole.class ).getRawClasspathEntries() );

    // pop the project from the stack
    _currentProject.pop();
  }

  /**
   * <p>
   * Resolves the class path entries.
   * </p>
   * 
   * @param classpathEntries
   *          the class path entries to resolve
   */
  private void resolveClasspathEntries( List<RawClasspathEntry> classpathEntries ) {
    for( RawClasspathEntry classpathEntry : classpathEntries ) {
      try {
        resolveClasspathEntry( classpathEntry );
      } catch( Ant4EclipseException e ) {
        throw e;
      } catch( Exception e ) {
        throw new Ant4EclipseException( e, JdtExceptionCode.EXCEPTION_WHILE_RESOLVING_CLASSPATH_ENTRY, classpathEntry, (hasCurrentProject() ? getCurrentProject().getSpecifiedName() : "<unkown>"), e.getMessage() );
      }
    }
  }

  /**
   * <p>
   * Resolves a eclipse class path entry.
   * </p>
   * 
   * @param entry
   *          the class path entry to resolve.
   */
  // Assure.notNull( "entry", entry );
  private final void resolveClasspathEntry( ClasspathEntry entry ) {

    // initialize handled
    boolean handled = false;

    // iterate over all the entry resolvers and try to resolve the entry
    for( ClasspathEntryResolver entryResolver : _entryResolvers ) {
      if( entryResolver.canResolve( entry ) ) {
        handled = true;
        entryResolver.resolve( entry, _resolverContext );
        break;
      }
    }

    // if the entry is not handled, we have to throw an exception here
    if( !handled && _failOnNonHandledEntry ) {
      throw new RuntimeException( String.format("Unsupported Entrykind: %s", entry ) );
    }
  }
  
} /* ENDCLASS */
