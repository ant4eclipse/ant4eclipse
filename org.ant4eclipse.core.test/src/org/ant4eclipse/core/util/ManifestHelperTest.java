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
