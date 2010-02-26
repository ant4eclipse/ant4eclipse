package org.ant4eclipse.lib.jdt.internal.model.userlibrary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.ant4eclipse.lib.jdt.model.userlibrary.UserLibraries;
import org.ant4eclipse.lib.jdt.model.userlibrary.UserLibrary;
import org.ant4eclipse.testframework.AbstractTestDirectoryBasedTest;
import org.junit.Test;

public class UserLibrariesFileParserImplTest extends AbstractTestDirectoryBasedTest {

  @Test
  public void parseFile() throws IOException {

    final File testFile = getTestDirectory().createFile("myUserLibraries.xml",
        getClass().getResourceAsStream("myUserLibraries.xml"));

    UserLibrariesFileParserImpl fileParserImpl = new UserLibrariesFileParserImpl();
    final UserLibraries userLibraries = fileParserImpl.parseUserLibrariesFile(testFile);
    assertNotNull(userLibraries);
    assertEquals(1, userLibraries.getAvailableLibraries().length);
    assertTrue(userLibraries.hasLibrary("J2EE Library"));
    final UserLibrary library = userLibraries.getLibrary("J2EE Library");
    assertNotNull(library);
    assertEquals(4, library.getArchives().length);

  }
}
