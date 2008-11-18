package org.ant4eclipse.core.util;

import java.io.File;

import org.ant4eclipse.core.util.Utilities;

import junit.framework.TestCase;

public class UtilitiesTest extends TestCase {

  public void testCalcRelative() {

    String relative = Utilities.calcRelative(new File("/schnerd"), new File("/temp/rep/schrepp/depp"));
    System.out.println(relative);
  }

  public void test_newInstance() {
    Dummy dummy = Utilities.newInstance(Dummy.class.getName());
    assertNotNull(dummy);
  }

  public static class Dummy {
    public Dummy() {
      // needed
    }
  }
}
