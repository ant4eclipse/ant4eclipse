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

import org.ant4eclipse.ant.platform.core.task.AbstractAnt4EclipseFileSet;
import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.jdt.model.project.JavaProjectRole;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.FileResource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * A {@link ResourceCollection} that includes all contents of source and/or output folders of a jdt project
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 * 
 */
public class JdtProjectFileSet extends AbstractAnt4EclipseFileSet {

  private boolean       _includeSourceFolders = false;

  private boolean       _includeOutputFolders = true;

  /**
   * List of all patterns that defines resources to be includes
   */
  private List<Pattern> _includePatterns      = new ArrayList<Pattern>();

  /**
   * List of all patterns that defines resources to be excludes
   */
  private List<Pattern> _excludePatterns      = new ArrayList<Pattern>();

  public JdtProjectFileSet( Project project ) {
    super( project );
  }

  /**
   * Set to true if contents of source folders should be included in the fileset
   * 
   * @param includeSourceFolders
   */
  public void setIncludeSourceFolders( boolean includeSourceFolders ) {
    if( isReference() ) {
      throw tooManyAttributes();
    }
    _includeSourceFolders = includeSourceFolders;
  }

  public boolean isIncludeSourceFolders() {
    return _includeSourceFolders;
  }

  /**
   * Set to true if contents of output folders should be included in the fileset.
   * 
   * @param includeSourceFolders
   */
  public void setIncludeOutputFolders( boolean includeOutputFolders ) {
    if( isReference() ) {
      throw tooManyAttributes();
    }
    _includeOutputFolders = includeOutputFolders;
  }

  public boolean isIncludeOutputFolders() {
    return _includeOutputFolders;
  }

  /**
   * Adds <code>includes</code> to the current list of include patterns. Patterns may be separated by a comma or a
   * space.
   * 
   * @param includes
   *          the string containing the include patterns
   */
  public void setIncludes( String includes ) {
    if( isReference() ) {
      throw tooManyAttributes();
    }
    if( Utilities.hasText( includes ) ) {
      StringTokenizer stringTokenizer = new StringTokenizer( includes, " ,", false );
      while( stringTokenizer.hasMoreElements() ) {
        String include = stringTokenizer.nextToken();
        createInclude().setName( include );
      }
    }
  }

  /**
   * Adds a new include pattern to the list of includes
   * 
   * @return
   */
  public Pattern createInclude() {
    if( isReference() ) {
      throw tooManyAttributes();
    }
    Pattern pattern = new Pattern();
    _includePatterns.add( pattern );
    return pattern;
  }

  /**
   * Adds a new exclude pattern to the list of excludes
   * 
   * @return
   */
  public Pattern createExclude() {
    if( isReference() ) {
      throw tooManyAttributes();
    }
    Pattern pattern = new Pattern();
    _excludePatterns.add( pattern );
    return pattern;
  }

  /**
   * Adds <code>excludes</code> to the current list of exclude patterns. Patterns may be separated by a comma or a
   * space.
   * 
   * @param excludes
   *          the string containing the exclude patterns
   */
  public void setExcludes( String excludes ) {
    if( isReference() ) {
      throw tooManyAttributes();
    }
    if( Utilities.hasText( excludes ) ) {
      StringTokenizer stringTokenizer = new StringTokenizer( excludes, " ,", false );
      while( stringTokenizer.hasMoreElements() ) {
        String exclude = stringTokenizer.nextToken();
        createExclude().setName( exclude );
      }
    }
  }

  /**
   * Returns all exclude patterns or <tt>null</tt> if no (valid) exclude pattern has been specified
   * 
   * @return
   */
  public String[] getAllExcludes() {
    if( _excludePatterns.isEmpty() ) {
      return null;
    }

    List<String> allExcludes = new ArrayList<String>();
    for( Pattern pattern : _excludePatterns ) {
      if( pattern.isValid() ) {
        allExcludes.add( pattern.getName() );
      }
    }

    return allExcludes.toArray( new String[allExcludes.size()] );
  }

  /**
   * Returns all includes patterns or <tt>null</tt> if no (valid) include pattern has been specified
   * 
   * @return
   */
  public String[] getAllIncludes() {
    if( _includePatterns.isEmpty() ) {
      return null;
    }
    List<String> allIncludes = new ArrayList<String>();
    for( Pattern pattern : _includePatterns ) {
      if( pattern.isValid() ) {
        allIncludes.add( pattern.getName() );
      }
    }
    return allIncludes.toArray( new String[allIncludes.size()] );
  }

  @Override
  protected void doComputeFileSet( List<Resource> resourceList ) {

    if( !(isIncludeSourceFolders() || isIncludeOutputFolders()) ) {
      A4ELogging.warn( "Neither output nor source folders are included in the fileset. FileSet will be empty" );
      return;
    }

    JavaProjectRole javaProjectRole = getEclipseProject().getRole( JavaProjectRole.class );

    // include output folder contents
    if( isIncludeOutputFolders() ) {
      A4ELogging.trace( "Adding output folders to file set" );
      final String[] allOutputFolders = javaProjectRole.getAllOutputFolders();
      for( String outputFolder : allOutputFolders ) {
        addFolderContent( resourceList, outputFolder );
      }
    }

    // include source folder contents
    if( isIncludeSourceFolders() ) {
      A4ELogging.trace( "Adding source folders to file set" );
      final String[] sourceFolders = javaProjectRole.getSourceFolders();
      for( String sourceFolder : sourceFolders ) {
        addFolderContent( resourceList, sourceFolder );
      }
    }

  }

  /**
   * adds the content of the specified project folder to the resourceList
   * 
   * @param resourceList
   * @param folder
   */
  protected void addFolderContent( List<Resource> resourceList, String folder ) {
    A4ELogging.trace( "adding folder '%s' to resourceList", folder );

    if( !getEclipseProject().hasChild( folder ) ) {
      A4ELogging.warn( "Folder '%s' does not exists in project '%s' - ignored", folder, getEclipseProject()
          .getSpecifiedName() );
      return;
    }

    // get the project child with the given name
    File directory = getEclipseProject().getChild( folder );
    if( !directory.isDirectory() ) {
      A4ELogging.warn( "Folder '%s' in project '%s' is not a directory - ignored", directory, getEclipseProject()
          .getSpecifiedName() );
      return;
    }

    DirectoryScanner directoryScanner = new DirectoryScanner();
    directoryScanner.setBasedir( directory );
    directoryScanner.setCaseSensitive( isCaseSensitive() );
    directoryScanner.setIncludes( getAllIncludes() );
    directoryScanner.setExcludes( getAllExcludes() );
    if( getDefaultexcludes() ) {
      directoryScanner.addDefaultExcludes();
    }

    // do the job
    directoryScanner.scan();

    // get the included files and add it to the resource list
    String[] files = directoryScanner.getIncludedFiles();

    // add files to result resourceList
    for( String fileName : files ) {
      resourceList.add( new FileResource( directory, fileName ) );
    }
  }

  /**
   * Represent a pattern for pattern matching, like <code>*.java</code>
   * 
   * @author nils
   * 
   */
  public class Pattern {
    private String _name;

    public String getName() {
      return _name;
    }

    /**
     * The name of the pattern
     * 
     * @param pattern
     */
    public void setName( String pattern ) {
      Assure.notNull( "pattern", pattern );
      _name = pattern;
    }

    /**
     * returns true if a non-empty name has been set
     * 
     * @return
     */
    public boolean isValid() {
      return Utilities.hasText( _name );
    }

  }
  
} /* ENDCLASS */
