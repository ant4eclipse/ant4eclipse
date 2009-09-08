package org.ant4eclipse.pde.tools;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.pde.model.buildproperties.PluginBuildProperties;
import org.ant4eclipse.pde.model.pluginproject.PluginProjectRole;
import org.ant4eclipse.platform.model.resource.EclipseProject;

/**
 * <p>
 * The {@link PluginProjectChecker} can be used to check if a plug-in project contains inconsistent or erroneous build
 * property entries.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class PluginProjectChecker {

  /** the ERRONEOUS_PROJECT_DEFINITION_MSG */
  private static String  ERRONEOUS_PROJECT_DEFINITION_MSG                = "Inconsistent or erroneous plug-in project definition for project '%s'. Reason:\n";

  /** the MISSING_MANIFEST_FILE_MSG */
  private static String  MISSING_MANIFEST_FILE_MSG                       = ERRONEOUS_PROJECT_DEFINITION_MSG
                                                                             + "- Project must contain a bundle manifest file,\n"
                                                                             + "- e.g. '%s" + File.separator
                                                                             + "META-INF" + File.separator
                                                                             + "MANIFEST.MF'.";

  /** the MISSING_BIN_INCLUDE_ENTRY_FOR_MANIFEST_FILE_MSG */
  private static String  MISSING_BIN_INCLUDE_ENTRY_FOR_MANIFEST_FILE_MSG = ERRONEOUS_PROJECT_DEFINITION_MSG
                                                                             + "- Inconsistent build properties file '%s'.\n"
                                                                             + "- The build properties file must contain a 'bin.includes' entry for the META-INF directory ('META-INF/'). To fix this issue, please add an entry for the META-INF directory to the bin.includes list (e.g. 'bin.includes = META-INF/').\n";

  /** the BIN_EXCLUDE_ENTRY_FOR_MANIFEST_FILE_MSG */
  private static String  BIN_EXCLUDE_ENTRY_FOR_MANIFEST_FILE_MSG         = ERRONEOUS_PROJECT_DEFINITION_MSG
                                                                             + "- Inconsistent build properties file '%s'.\n"
                                                                             + "- The build properties must not contain a 'bin.excludes' entry for the manifest file ('bin.excludes = META-INF/MANIFEST.MF'). To fix this issue, please remove the META-INF/MANIFEST.MF entry from the bin.excludes list.\n";

  /** the eclipse project to check */
  private EclipseProject _eclipseProject;

  /** the list of issues */
  private List<Issue>    _issues;

  /**
   * <p>
   * Creates a new instance of type {@link PluginProjectChecker}.
   * </p>
   * 
   * @param eclipseProject
   *          the eclipse project to check.
   */
  public PluginProjectChecker(EclipseProject eclipseProject) {
    Assert.notNull(eclipseProject);

    this._eclipseProject = eclipseProject;
    this._issues = new LinkedList<Issue>();
  }

  /**
   * <p>
   * Checks the given eclipse plug-in project and returns a list of issues.
   * </p>
   * 
   * @return a list of issues.
   */
  public List<Issue> checkPluginProject() {

    // retrieve the project name
    String projectName = _eclipseProject.getSpecifiedName();

    // does the project has the PluginProjectRole?
    check(_eclipseProject.hasRole(PluginProjectRole.class), "Project '%s' must have role 'PluginProjectRole'.",
        projectName);

    // get the build properties
    PluginProjectRole pluginProjectRole = (PluginProjectRole) _eclipseProject.getRole(PluginProjectRole.class);
    PluginBuildProperties buildProperties = pluginProjectRole.getBuildProperties();
    List<String> binaryIncludesList = Arrays.asList(buildProperties.getBinaryIncludes());
    List<String> binaryExcludesList = Arrays.asList(buildProperties.getBinaryExcludes());

    // does the project contain a manifest?
    check(_eclipseProject.hasChild("META-INF/MANIFEST.MF"), MISSING_MANIFEST_FILE_MSG, projectName, _eclipseProject
        .getFolder());

    // does the build property file contain an entry for the META-INF directory?
    check(binaryIncludesList.contains("META-INF"), MISSING_BIN_INCLUDE_ENTRY_FOR_MANIFEST_FILE_MSG, projectName,
        _eclipseProject.getChild("build.properties").getAbsolutePath());

    // doesn't the binary excludes list contain an entry for the META-INF/MANIFEST.MF directory?
    check(!binaryExcludesList.contains("META-INF/MANIFEST.MF"), BIN_EXCLUDE_ENTRY_FOR_MANIFEST_FILE_MSG, projectName,
        _eclipseProject.getChild("build.properties").getAbsolutePath());

    // return the issue list
    return _issues;
  }

  /**
   * <p>
   * Adds an issue to the issue list if the given condition is false.
   * <p>
   * 
   * @param condition
   *          the condition
   * @param msg
   *          the message
   * @param args
   *          the args
   */
  private void check(boolean condition, final String msg, final Object... args) {
    if (!condition) {
      _issues.add(new Issue(String.format(msg, args)));
    }
  }

  /**
   * <p>
   * Represents an issue.
   * </p>
   * 
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public static class Issue {

    /** the message */
    private String _message;

    /**
     * <p>
     * Creates a new instance of type {@link Issue}.
     * </p>
     * 
     * @param message
     *          the message.
     */
    public Issue(String message) {
      _message = message;
    }

    /**
     * <p>
     * The message of the issue.
     * </p>
     * 
     * @return the message.
     */
    public String getMessage() {
      return _message;
    }
  }
}
