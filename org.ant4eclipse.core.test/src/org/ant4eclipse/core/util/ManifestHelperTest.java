package org.ant4eclipse.core.util;

import org.ant4eclipse.core.util.ManifestHelper.ManifestHeaderElement;

import java.util.jar.Manifest;

import junit.framework.TestCase;

public class ManifestHelperTest extends TestCase {

  public void testManifestHelper() {
    Manifest manifest = new Manifest();
    manifest.getMainAttributes().putValue(ManifestHelper.BUNDLE_SYMBOLICNAME, "org.bruni;singleton:=true");

    ManifestHeaderElement[] elements = ManifestHelper.getManifestHeaderElements(manifest,
        ManifestHelper.BUNDLE_SYMBOLICNAME);

    assertNotNull(elements);
    assertEquals(elements.length, 1);
    assertEquals(elements[0].getValues().length, 1);
    assertEquals(elements[0].getValues()[0], "org.bruni");
  }
}
