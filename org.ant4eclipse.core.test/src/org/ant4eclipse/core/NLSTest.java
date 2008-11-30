package org.ant4eclipse.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class NLSTest {

  @Test
  public void testNLS() {
    NLS.initialize(TestNLSClass.class);

    assertNotNull(TestNLSClass.PARAMETER_MUST_BE_SET_ON_TYPE);
    assertEquals("Test Message", TestNLSClass.PARAMETER_MUST_BE_SET_ON_TYPE.toString());
  }

  public static class TestNLSClass {

    private String             _message;

    @NLSMessage("Test Message")
    public static TestNLSClass PARAMETER_MUST_BE_SET_ON_TYPE;

    @Override
    public String toString() {
      return _message;
    }

    private TestNLSClass(String message) {
      _message = message;
    }
  }
}
