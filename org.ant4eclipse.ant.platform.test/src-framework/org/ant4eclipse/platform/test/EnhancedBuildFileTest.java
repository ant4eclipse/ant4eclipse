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
