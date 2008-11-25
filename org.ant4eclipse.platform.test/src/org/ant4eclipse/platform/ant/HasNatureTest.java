package org.ant4eclipse.platform.ant;

import java.io.File;

import org.apache.tools.ant.BuildFileTest;

public class HasNatureTest extends BuildFileTest {

  public void setUp() {
    configureProject("src/org/ant4eclipse/platform/ant/hasNature.xml");

    File workspaceDir = new File("C:/TEMP/a4e");

    getProject().setProperty("workspaceDir", workspaceDir.getAbsolutePath());
  }

  public void testNonexistingNature() {
    expectLog("testNonexistingNature", "OK");
  }

  public void testExistingNature() {
    expectLog("testExistingNature", "OK");
  }

  // protected void setUp() throws Exception {
  // super.setUp();
  // getTestEnvironment().copyFromResourceDirToWorkspace("projects/simpleworkspace");
  // }

  //    
  //    
  // public void testHasNature_3() throws Exception {
  //
  // StringBuffer target = new StringBuffer();
  // target.append("<target name='test'>");
  // target.append(" <if>");
  // target.append("  <hasNature project='${basedir}/../simpleproject' nature='nonexistingnature' />");
  // target.append("  <then><echo message=\"FAIL\"/></then>");
  // target.append("  <else><echo message=\"OK\"/></else>");
  // target.append(" </if>");
  // target.append("</target>");
  //
  // getBuildFile().addTarget(target.toString());
  // getBuildFile().execute(this, "simpleproject");
  //
  // expectLog("OK");
  // }
  //    
  // public void testHasNature_4() throws Exception {
  //
  // StringBuffer target = new StringBuffer();
  // target.append("<target name='test'>");
  // target.append(" <if>");
  // target.append("  <hasNature project='${basedir}/../simpleproject' nature='org.eclipse.jdt.core.javanature' />");
  // target.append("  <then><echo message=\"OK\"/></then>");
  // target.append("  <else><echo message=\"FAIL\"/></else>");
  // target.append(" </if>");
  // target.append("</target>");
  //
  // getBuildFile().addTarget(target.toString());
  // getBuildFile().execute(this, "simpleproject");
  //
  // expectLog("OK");
  // }
  //
  //
  // public void testHasNature_5() throws Exception {
  //
  // StringBuffer target = new StringBuffer();
  // target.append("<target name='test'>");
  // target.append(" <if>");
  // target.append("  <hasNature project='${basedir}/../simpleproject' nature='java' />");
  // target.append("  <then><echo message=\"OK\"/></then>");
  // target.append("  <else><echo message=\"FAIL\"/></else>");
  // target.append(" </if>");
  // target.append("</target>");
  //
  // getBuildFile().addTarget(target.toString());
  // getBuildFile().execute(this, "simpleproject");
  //
  // expectLog("OK");
  // }
}
