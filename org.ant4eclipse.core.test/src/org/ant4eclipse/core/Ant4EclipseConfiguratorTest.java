package org.ant4eclipse.core;

import org.ant4eclipse.core.service.ServiceRegistry;

import org.apache.tools.ant.Project;
import org.junit.Test;

import java.util.Properties;

public class Ant4EclipseConfiguratorTest {

  @Test
  public void testAnt4EclipseConfigurator() {

    Ant4EclipseConfigurator.configureAnt4Eclipse();
    Ant4EclipseConfigurator.configureAnt4Eclipse();

    ServiceRegistry.reset();
  }

  @Test
  public void testAnt4EclipseConfigurator_2() {
    Project project = new Project();
    Ant4EclipseConfigurator.configureAnt4Eclipse(project);
    Ant4EclipseConfigurator.configureAnt4Eclipse(project);

    ServiceRegistry.reset();
  }

  @Test
  public void testAnt4EclipseConfigurator_3() {
    Ant4EclipseConfigurator.configureAnt4Eclipse(new Properties());
    Ant4EclipseConfigurator.configureAnt4Eclipse(new Properties());

    ServiceRegistry.reset();
  }

  @Test
  public void testAnt4EclipseConfigurator_4() {
    Ant4EclipseConfigurator.configureAnt4Eclipse((Properties) null);
    Ant4EclipseConfigurator.configureAnt4Eclipse((Properties) null);

    ServiceRegistry.reset();
  }
}
