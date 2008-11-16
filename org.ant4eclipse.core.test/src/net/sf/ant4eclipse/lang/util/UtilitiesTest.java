package net.sf.ant4eclipse.lang.util;

import java.io.File;

import net.sf.ant4eclipse.core.util.Utilities;

import junit.framework.TestCase;

public class UtilitiesTest extends TestCase {

  public void testCalcRelative() {
    
    String relative = Utilities.calcRelative(new File("/schnerd"), new File("/temp/rep/schrepp/depp"));
    System.out.println(relative);
  }
}
