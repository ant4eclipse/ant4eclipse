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
package org.ant4eclipse.lib.core.nls;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Can be used to mark a <tt>public static, non-final</tt> field to be set by {@link NLS#initialize(Class)}
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.FIELD )
public @interface NLSMessage {

  /**
   * A default value, that should be set if no other value for the annotated field can be found in an external
   * properties file
   * 
   * @return The default value used for the initialisation of a message. Not <code>null</code>.
   */
  String value() default "";

} /* ENDANNOTATION */
