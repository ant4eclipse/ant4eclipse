package org.ant4eclipse.lib.jdt.internal.model.userlibrary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.ant4eclipse.lib.jdt.model.userlibrary.Archive;
import org.ant4eclipse.lib.jdt.model.userlibrary.UserLibraries;
import org.ant4eclipse.lib.jdt.model.userlibrary.UserLibrary;
import org.ant4eclipse.testframework.AbstractTestDirectoryBasedTest;
import org.junit.Test;

public class UserLibrariesFileParserImplTest extends AbstractTestDirectoryBasedTest {

  @Test
  public void parseFile() throws IOException {

    File testFile = getTestDirectory().createFile( "myUserLibraries.xml",
        getClass().getResourceAsStream( "myUserLibraries.xml" ) );

    UserLibrariesFileParserImpl fileParserImpl = new UserLibrariesFileParserImpl();
    UserLibraries userLibraries = fileParserImpl.parseUserLibrariesFile( testFile, null );
    assertNotNull( userLibraries );
    assertEquals( 1, userLibraries.getAvailableLibraries().size() );
    assertTrue( userLibraries.hasLibrary( "J2EE Library" ) );
    UserLibrary library = userLibraries.getLibrary( "J2EE Library" );
    assertNotNull( library );
    assertNotNull( library.getArchives() );
    assertEquals( 4, library.getArchives().size() );
    File expectedPath = new File( "src/org/ant4eclipse/lib/jdt/internal/model/userlibrary/library1.jar" );
    Archive firstLibrary = library.getArchives().get( 0 );
    assertNotNull( firstLibrary );
    assertEquals( expectedPath, firstLibrary.getPath() );
  }

} /* ENDCLASS */
