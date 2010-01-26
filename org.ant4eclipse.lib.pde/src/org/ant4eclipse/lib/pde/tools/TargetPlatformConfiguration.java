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
package org.ant4eclipse.lib.pde.tools;

import org.ant4eclipse.core.Assert;



import org.ant4eclipse.lib.jdt.internal.model.jre.JavaProfileReader;
import org.ant4eclipse.lib.pde.model.pluginproject.Constants;
import org.eclipse.core.runtime.internal.adaptor.EclipseEnvironmentInfo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * TargetPlatformConfiguration --
 */
public class TargetPlatformConfiguration {

  /** - */
  private boolean             _preferProjects = true;

  /** - */
  private Map<Object, Object> _configurationProperties;

  /** - */
  private boolean             _isLocked       = false;

  /**
   * 
   */
  public TargetPlatformConfiguration() {
    this._preferProjects = true;

    this._configurationProperties = new HashMap<Object, Object>();

    this._configurationProperties.putAll(JavaProfileReader.getInstance().readDefaultProfile().getProperties());

    this._configurationProperties.put(Constants.PROP_WS, EclipseEnvironmentInfo.getDefault().getWS());
    this._configurationProperties.put(Constants.PROP_OS, EclipseEnvironmentInfo.getDefault().getOS());
    this._configurationProperties.put(Constants.PROP_ARCH, EclipseEnvironmentInfo.getDefault().getOSArch());
    this._configurationProperties.put(Constants.PROP_NL, EclipseEnvironmentInfo.getDefault().getNL());
  }

  public void setPreferProjects(boolean preferProjects) {
    assertNotLocked();

    this._preferProjects = preferProjects;
  }

  public void setOperatingSystem(String value) {
    assertNotLocked();

    this._configurationProperties.put(Constants.PROP_OS, value);
  }

  public void setArchitecture(String value) {
    assertNotLocked();

    this._configurationProperties.put(Constants.PROP_ARCH, value);
  }

  public void setWindowingSystem(String value) {
    assertNotLocked();

    this._configurationProperties.put(Constants.PROP_WS, value);
  }

  public void setProfileName(String value) {
    assertNotLocked();

    this._configurationProperties.put("osgi.java.profile.name", value);
  }

  public void setLanguageSetting(String value) {
    assertNotLocked();

    this._configurationProperties.put(Constants.PROP_NL, value);
  }

  public void lock() {
    this._isLocked = true;
  }

  public boolean isPreferProjects() {
    return this._preferProjects;
  }

  public Properties getConfigurationProperties() {
    Properties result = new Properties();

    Set<Map.Entry<Object, Object>> entrySet = this._configurationProperties.entrySet();
    Iterator<Map.Entry<Object, Object>> iterator = entrySet.iterator();
    while (iterator.hasNext()) {
      Map.Entry<Object, Object> entry = iterator.next();
      result.put(entry.getKey(), entry.getValue());
    }

    return result;
  }

  public String getProfileName() {
    return (String) this._configurationProperties.get("osgi.java.profile.name");
  }

  public String getLanguageSetting() {
    return (String) this._configurationProperties.get(Constants.PROP_NL);
  }

  public String getOperatingSystem() {
    return (String) this._configurationProperties.get(Constants.PROP_OS);
  }

  public String getArchitecture() {
    return (String) this._configurationProperties.get(Constants.PROP_ARCH);
  }

  public String getWindowingSystem() {
    return (String) this._configurationProperties.get(Constants.PROP_WS);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int prime = 31;
    int result = 1;
    result = prime * result + ((this._configurationProperties == null) ? 0 : this._configurationProperties.hashCode());
    result = prime * result + (this._preferProjects ? 1231 : 1237);
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    TargetPlatformConfiguration other = (TargetPlatformConfiguration) obj;
    if (this._configurationProperties == null) {
      if (other._configurationProperties != null) {
        return false;
      }
    } else if (!this._configurationProperties.equals(other._configurationProperties)) {
      return false;
    }
    if (this._preferProjects != other._preferProjects) {
      return false;
    }
    return true;
  }

  private void assertNotLocked() {
    Assert.assertTrue(!this._isLocked, "TargetPlatformConfiguration is locked!");
  }

  // * org.osgi.framework.system.packages - the packages exported by the system bundle <br>
  // * org.osgi.framework.executionenvironment - the comma separated list of supported execution environments <br>
  // * osgi.resolverMode - the resolver mode. A value of "strict" will set the resolver mode to strict.<br>
  // properties.put(Constants.PROP_WS, EclipseEnvironmentInfo.getDefault().getWS());
  // properties.put(Constants.PROP_OS, EclipseEnvironmentInfo.getDefault().getOS());
  // properties.put(Constants.PROP_ARCH, EclipseEnvironmentInfo.getDefault().getOSArch());
  // properties.put(Constants.PROP_NL, EclipseEnvironmentInfo.getDefault().getNL());

  // 'osgi.nl' -> 'de_DE'
  // 'osgi.ws' -> 'win32'
  // 'osgi.os' -> 'win32'
  // 'osgi.arch' -> 'x86'
  // 'osgi.java.profile.name' -> 'JavaSE-1.6'
  // 'org.osgi.framework.executionenvironment' ->
  // 'OSGi/Minimum-1.0,OSGi/Minimum-1.1,JRE-1.1,J2SE-1.2,J2SE-1.3,J2SE-1.4,J2SE-1.5,JavaSE-1.6'
  // 'org.osgi.framework.bootdelegation' -> 'javax.*,org.ietf.jgss,org.omg.*,org.w3c.*,org.xml.*,sun.*,com.sun.*'
  // 'org.osgi.framework.system.packages' ->
  // 'javax.accessibility,javax.activation,javax.activity,javax.annotation,javax.annotation.processing,javax.crypto,javax.crypto.interfaces,javax.crypto.spec,javax.imageio,javax.imageio.event,javax.imageio.metadata,javax.imageio.plugins.bmp,javax.imageio.plugins.jpeg,javax.imageio.spi,javax.imageio.stream,javax.jws,javax.jws.soap,javax.lang.model,javax.lang.model.element,javax.lang.model.type,javax.lang.model.util,javax.management,javax.management.loading,javax.management.modelmbean,javax.management.monitor,javax.management.openmbean,javax.management.relation,javax.management.remote,javax.management.remote.rmi,javax.management.timer,javax.naming,javax.naming.directory,javax.naming.event,javax.naming.ldap,javax.naming.spi,javax.net,javax.net.ssl,javax.print,javax.print.attribute,javax.print.attribute.standard,javax.print.event,javax.rmi,javax.rmi.CORBA,javax.rmi.ssl,javax.script,javax.security.auth,javax.security.auth.callback,javax.security.auth.kerberos,javax.security.auth.login,javax.security.auth.spi,javax.security.auth.x500,javax.security.cert,javax.security.sasl,javax.sound.midi,javax.sound.midi.spi,javax.sound.sampled,javax.sound.sampled.spi,javax.sql,javax.sql.rowset,javax.sql.rowset.serial,javax.sql.rowset.spi,javax.swing,javax.swing.border,javax.swing.colorchooser,javax.swing.event,javax.swing.filechooser,javax.swing.plaf,javax.swing.plaf.basic,javax.swing.plaf.metal,javax.swing.plaf.multi,javax.swing.plaf.synth,javax.swing.table,javax.swing.text,javax.swing.text.html,javax.swing.text.html.parser,javax.swing.text.rtf,javax.swing.tree,javax.swing.undo,javax.tools,javax.transaction,javax.transaction.xa,javax.xml,javax.xml.bind,javax.xml.bind.annotation,javax.xml.bind.annotation.adapters,javax.xml.bind.attachment,javax.xml.bind.helpers,javax.xml.bind.util,javax.xml.crypto,javax.xml.crypto.dom,javax.xml.crypto.dsig,javax.xml.crypto.dsig.dom,javax.xml.crypto.dsig.keyinfo,javax.xml.crypto.dsig.spec,javax.xml.datatype,javax.xml.namespace,javax.xml.parsers,javax.xml.soap,javax.xml.stream,javax.xml.stream.events,javax.xml.stream.util,javax.xml.transform,javax.xml.transform.dom,javax.xml.transform.sax,javax.xml.transform.stax,javax.xml.transform.stream,javax.xml.validation,javax.xml.ws,javax.xml.ws.handler,javax.xml.ws.handler.soap,javax.xml.ws.http,javax.xml.ws.soap,javax.xml.ws.spi,javax.xml.xpath,org.ietf.jgss,org.omg.CORBA,org.omg.CORBA_2_3,org.omg.CORBA_2_3.portable,org.omg.CORBA.DynAnyPackage,org.omg.CORBA.ORBPackage,org.omg.CORBA.portable,org.omg.CORBA.TypeCodePackage,org.omg.CosNaming,org.omg.CosNaming.NamingContextExtPackage,org.omg.CosNaming.NamingContextPackage,org.omg.Dynamic,org.omg.DynamicAny,org.omg.DynamicAny.DynAnyFactoryPackage,org.omg.DynamicAny.DynAnyPackage,org.omg.IOP,org.omg.IOP.CodecFactoryPackage,org.omg.IOP.CodecPackage,org.omg.Messaging,org.omg.PortableInterceptor,org.omg.PortableInterceptor.ORBInitInfoPackage,org.omg.PortableServer,org.omg.PortableServer.CurrentPackage,org.omg.PortableServer.POAManagerPackage,org.omg.PortableServer.POAPackage,org.omg.PortableServer.portable,org.omg.PortableServer.ServantLocatorPackage,org.omg.SendingContext,org.omg.stub.java.rmi,org.w3c.dom,org.w3c.dom.bootstrap,org.w3c.dom.css,org.w3c.dom.events,org.w3c.dom.html,org.w3c.dom.ls,org.w3c.dom.ranges,org.w3c.dom.stylesheets,org.w3c.dom.traversal,org.w3c.dom.views
  // ,org.xml.sax,org.xml.sax.ext,org.xml.sax.helpers'
}
