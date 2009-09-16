package org.ant4eclipse.platform.test;

import org.apache.tools.ant.BuildFileTest;

import java.util.regex.Pattern;

public class EnhancedBuildFileTest extends BuildFileTest {

  public void expectLogMatches(String target, String regExp) {
    executeTarget(target);
    String realLog = getLog();
    assertTrue("expecting log to match \"" + regExp + "\" log was \"" + realLog + "\"", Pattern
        .matches(regExp, realLog));
  }
}
