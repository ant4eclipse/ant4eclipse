package org.ant4eclipse.platform.test;

import org.apache.tools.ant.BuildFileTest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EnhancedBuildFileTest extends BuildFileTest {

  public void expectLogMatches(String target, String regExp) {

    executeTarget(target);

    String realLog = getLog();

    Pattern patter = Pattern.compile(regExp);
    Matcher matcher = patter.matcher(target);

    assertTrue("expecting log to match \"" + regExp + "\" log was \"" + realLog + "\"", matcher.matches());
  }
}
