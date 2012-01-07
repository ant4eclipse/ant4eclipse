package org.ant4eclipse.ant.platform;

import java.util.List;

import org.ant4eclipse.ant.platform.core.MacroExecutionComponent;
import org.ant4eclipse.ant.platform.core.MacroExecutionValues;
import org.ant4eclipse.ant.platform.core.ScopedMacroDefinition;
import org.ant4eclipse.ant.platform.core.delegate.MacroExecutionDelegate;
import org.ant4eclipse.ant.platform.core.delegate.MacroExecutionValuesProvider;
import org.ant4eclipse.ant.platform.core.task.AbstractTeamProjectSetBasedTask;
import org.ant4eclipse.lib.core.util.StringMap;
import org.ant4eclipse.lib.platform.model.team.projectset.TeamProjectDescription;
import org.ant4eclipse.lib.platform.model.team.projectset.TeamProjectSet;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DynamicElement;
import org.apache.tools.ant.taskdefs.MacroDef;
import org.apache.tools.ant.taskdefs.MacroDef.NestedSequential;

/**
 * Executes a Team-Project-Set (psf) File.
 * 
 * @author nils
 * 
 */
public class ExecuteTeamProjectSetTask extends AbstractTeamProjectSetBasedTask implements
    MacroExecutionComponent<String>, DynamicElement {

  /**
   * The forEachProject-Scope name
   */
  public static final String                   SCOPE_FOR_EACH_PROJECT = "forEachProject";

  /** the macro execution delegate */
  private final MacroExecutionDelegate<String> _macroExecutionDelegate;

  public ExecuteTeamProjectSetTask() {
    this._macroExecutionDelegate = new MacroExecutionDelegate<String>( this, "executeTeamProjectSet" );

  }

  // ~~~ MacroExecutionComponent implementation

  /*
   * (non-Javadoc)
   * 
   * @see org.ant4eclipse.ant.platform.core.MacroExecutionComponent#createScopedMacroDefinition(java.lang.Object)
   */
  @Override
  public NestedSequential createScopedMacroDefinition( String scope ) {
    return this._macroExecutionDelegate.createScopedMacroDefinition( scope );
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.ant4eclipse.ant.platform.core.MacroExecutionComponent#executeMacroInstance(org.apache.tools.ant.taskdefs.MacroDef
   * , org.ant4eclipse.ant.platform.core.delegate.MacroExecutionValuesProvider)
   */
  @Override
  public void executeMacroInstance( MacroDef macroDef, MacroExecutionValuesProvider provider ) {
    this._macroExecutionDelegate.executeMacroInstance( macroDef, provider );

  }

  /*
   * (non-Javadoc)
   * 
   * @see org.ant4eclipse.ant.platform.core.MacroExecutionComponent#getPrefix()
   */
  @Override
  public String getPrefix() {
    return this._macroExecutionDelegate.getPrefix();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.ant4eclipse.ant.platform.core.MacroExecutionComponent#getScopedMacroDefinitions()
   */
  @Override
  public List<ScopedMacroDefinition<String>> getScopedMacroDefinitions() {
    return this._macroExecutionDelegate.getScopedMacroDefinitions();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.ant4eclipse.ant.platform.core.MacroExecutionComponent#setPrefix(java.lang.String)
   */
  @Override
  public void setPrefix( String prefix ) {
    this._macroExecutionDelegate.setPrefix( prefix );

  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.tools.ant.DynamicElement#createDynamicElement(java.lang.String)
   */
  @Override
  public final Object createDynamicElement( String name ) throws BuildException {

    // handle SCOPE_FOR_EACH_PROJECT
    if( SCOPE_FOR_EACH_PROJECT.equalsIgnoreCase( name ) ) {
      return createScopedMacroDefinition( SCOPE_FOR_EACH_PROJECT );
    }

    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.ant4eclipse.ant.core.AbstractAnt4EclipseTask#doExecute()
   */
  @Override
  protected void doExecute() {
    // check require fields
    requireProjectSetSet();

    // execute scoped macro definitions
    for( ScopedMacroDefinition<String> scopedMacroDefinition : getScopedMacroDefinitions() ) {

      MacroDef macroDef = scopedMacroDefinition.getMacroDef();

      // execute SCOPE_FOR_EACH_PROJECT
      if( SCOPE_FOR_EACH_PROJECT.equals( scopedMacroDefinition.getScope() ) ) {
        executeForEachProjectScopedMacroDef( macroDef );
      } else {
        throw new RuntimeException( "Unknown Scope '" + scopedMacroDefinition.getScope() + "'" );
      }
    }

  }

  /**
   * Executes the 'forEachProject'-Scope.
   * 
   * <p>
   * This iterates over all projects in the Team-Project-Set file and invokes the macroDefinition with scoped properties
   * that describe a single entry. The actual properties are vendor/scm specific
   * 
   * @param macroDef
   */
  private void executeForEachProjectScopedMacroDef( MacroDef macroDef ) {
    // Get the TeamProjectSet
    TeamProjectSet teamProjectSet = getTeamProjectSet();

    // Pull out the project descriptions
    final TeamProjectDescription[] teamProjectDescriptions = teamProjectSet.getTeamProjectDescriptions();

    // execute the macroDef for each project description
    for( final TeamProjectDescription teamProjectDescription : teamProjectDescriptions ) {
      executeMacroInstance( macroDef, new MacroExecutionValuesProvider() {

        @Override
        public MacroExecutionValues provideMacroExecutionValues( MacroExecutionValues values ) {
          // Get Properties from teamProjectDescription...
          final StringMap teamProjectDescriptionProperties = teamProjectDescription.getAsProperties();

          // ...add teamProjectDescription-properties as scoped properties to macro invocation
          values.getProperties().putAll( teamProjectDescriptionProperties );

          return values;
        }
      } );
    }
  }
  
} /* ENDCLASS */
