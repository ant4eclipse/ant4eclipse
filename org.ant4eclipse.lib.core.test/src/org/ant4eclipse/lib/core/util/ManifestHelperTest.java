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
package org.ant4eclipse.lib.core.util;

import org.ant4eclipse.lib.core.util.ManifestHelper.ManifestHeaderElement;
import org.junit.Assert;
import org.junit.Test;

import java.util.jar.Manifest;

public class ManifestHelperTest {

  @Test
  public void testManifestHelper() {
    Manifest manifest = new Manifest();
    manifest.getMainAttributes().putValue(ManifestHelper.BUNDLE_SYMBOLICNAME, "org.bruni;singleton:=true");
    ManifestHeaderElement[] elements = ManifestHelper.getManifestHeaderElements(manifest,
        ManifestHelper.BUNDLE_SYMBOLICNAME);
    Assert.assertNotNull(elements);
    Assert.assertEquals(elements.length, 1);
    Assert.assertEquals(elements[0].getValues().length, 1);
    Assert.assertEquals(elements[0].getValues()[0], "org.bruni");
  }

} /* ENDCLASS */
