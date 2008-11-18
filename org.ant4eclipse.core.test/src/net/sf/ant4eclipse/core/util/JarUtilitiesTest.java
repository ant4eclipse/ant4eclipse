package net.sf.ant4eclipse.core.util;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.jar.JarFile;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JarUtilitiesTest {

  private File _expansionDirectory;

  @Before
  public void init() {

    String tempDirString = System.getProperty("java.io.tmpdir");
    _expansionDirectory = new File(tempDirString);

    Utilities.delete(new File(_expansionDirectory, "test.jar"));
    Utilities.delete(new File(_expansionDirectory, "test.txt"));
    Utilities.delete(new File(_expansionDirectory, "test2.jar"));
    Utilities.delete(new File(_expansionDirectory, "META-INF/Manifest.mf"));
  }

  @After
  public void dispose() {
    Utilities.delete(new File(_expansionDirectory, "test.jar"));
    Utilities.delete(new File(_expansionDirectory, "test.txt"));
    Utilities.delete(new File(_expansionDirectory, "test2.jar"));
    Utilities.delete(new File(_expansionDirectory, "META-INF/Manifest.mf"));
  }

  @Test
  public void test() {

    URL url = getClass().getClassLoader().getResource("net/sf/ant4eclipse/core/util/test-jar.jar");
    File jarFile = new File(url.getFile());

    assertTrue(jarFile.exists() && jarFile.isFile());

    assertTrue(_expansionDirectory.exists() && _expansionDirectory.isDirectory());

    try {
      JarUtilities.expandJarFile(new JarFile(jarFile), _expansionDirectory);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    assertTrue(new File(_expansionDirectory, "test.jar").exists());
    assertTrue(new File(_expansionDirectory, "test.txt").exists());
    assertTrue(new File(_expansionDirectory, "test2.jar").exists());
    assertTrue(new File(_expansionDirectory, "META-INF/Manifest.mf").exists());
  }
}
