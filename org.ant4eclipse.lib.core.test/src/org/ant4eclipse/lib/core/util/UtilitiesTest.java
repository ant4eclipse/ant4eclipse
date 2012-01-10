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

import org.ant4eclipse.lib.core.CoreExceptionCode;
import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.testframework.ConfigurableAnt4EclipseTestCase;
import org.ant4eclipse.testframework.JUnitUtilities;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.jar.JarFile;

public class UtilitiesTest extends ConfigurableAnt4EclipseTestCase {

  /**
   * This one is also used to test the {@link Utilities#delete(File)} function. It would be nicer if we could separate
   * theses tests but JUnit doesn't support dependencies. It's also used to test the unpack method.
   */
  @Test
  public void expandJarFile() throws IOException {

    File file = Utilities.exportResource( "/util/test-jar.jar" );
    File dir = verifyExpandedJar( file );

    // delete function test
    Assert.assertTrue( Utilities.delete( dir ) );
    Assert.assertFalse( dir.exists() );

  }

  /**
   * This function would be necessary under TestNG !
   * 
   * @return The destination directory keeping the content
   */
  private File verifyExpandedJar( File file ) throws IOException {
    File destdir1 = JUnitUtilities.createTempDir( false );
    JarFile jarfile = new JarFile( file );
    try {
      Utilities.expandJarFile( jarfile, destdir1 );
    } finally {
      jarfile.close();
    }
    Assert.assertTrue( new File( destdir1, "test.jar" ).isFile() );
    Assert.assertTrue( new File( destdir1, "test.txt" ).isFile() );
    Assert.assertTrue( new File( destdir1, "test2.jar" ).isFile() );
    Assert.assertTrue( new File( destdir1, "META-INF" ).isDirectory() );
    Assert.assertTrue( new File( destdir1, "META-INF/MANIFEST.MF" ).isFile() );

    File destdir2 = JUnitUtilities.createTempDir();
    Utilities.unpack( file, destdir2 );
    Assert.assertTrue( new File( destdir2, "test.jar" ).isFile() );
    Assert.assertTrue( new File( destdir2, "test.txt" ).isFile() );
    Assert.assertTrue( new File( destdir2, "test2.jar" ).isFile() );
    Assert.assertTrue( new File( destdir2, "META-INF" ).isDirectory() );
    Assert.assertTrue( new File( destdir2, "META-INF/MANIFEST.MF" ).isFile() );

    return destdir2;
  }

  @Test
  public void calcRelative() {

    String relative1 = Utilities.calcRelative( new File( "/schnerd" ), new File( "/temp/rep/schrepp/depp" ) );
    Assert.assertEquals( "../temp/rep/schrepp/depp".replace( '/', File.separatorChar ), relative1 );

    if( Utilities.isWindows() ) {

      String relative2 = Utilities.calcRelative( new File( "K:/schnerd" ), new File( "K:/temp/rep/schrepp/depp" ) );
      Assert.assertEquals( "../temp/rep/schrepp/depp".replace( '/', File.separatorChar ), relative2 );

      String relative3 = Utilities.calcRelative( new File( "K:/" ), new File( "K:/temp/rep/schrepp/depp" ) );
      Assert.assertEquals( "temp/rep/schrepp/depp".replace( '/', File.separatorChar ), relative3 );

      String relative4 = Utilities.calcRelative( new File( "J:/" ), new File( "K:/temp/rep/schrepp/depp" ) );
      Assert.assertEquals( null, relative4 );

    } else {

      String relative2 = Utilities.calcRelative( new File( "/schnerd" ), new File( "/temp/rep/schrepp/depp" ) );
      Assert.assertEquals( "../temp/rep/schrepp/depp".replace( '/', File.separatorChar ), relative2 );

      String relative3 = Utilities.calcRelative( new File( "/" ), new File( "/temp/rep/schrepp/depp" ) );
      Assert.assertEquals( "temp/rep/schrepp/depp".replace( '/', File.separatorChar ), relative3 );

    }

  }

  @Test
  public void cleanup() {

    String cleaned1 = Utilities.cleanup( (String) null );
    Assert.assertEquals( null, cleaned1 );

    String cleaned2 = Utilities.cleanup( "" );
    Assert.assertEquals( null, cleaned2 );

    String cleaned3 = Utilities.cleanup( "   " );
    Assert.assertEquals( null, cleaned3 );

    String cleaned4 = Utilities.cleanup( "\t\t\t" );
    Assert.assertEquals( null, cleaned4 );

    String cleaned5 = Utilities.cleanup( "   BLA  " );
    Assert.assertEquals( "BLA", cleaned5 );

    String[] cleaned6 = Utilities.cleanup( new String[] { null, "", "   ", "\t\t\t", "   BLA  " } );
    Assert.assertNotNull( cleaned6 );
    Assert.assertEquals( 1, cleaned6.length );
    Assert.assertEquals( "BLA", cleaned6[0] );

  }

  @Test
  public void contains() {
    Assert.assertFalse( Utilities.contains( "test" ) );
    Assert.assertFalse( Utilities.contains( "test", null, null ) );
    Assert.assertFalse( Utilities.contains( "test", "", "" ) );
    Assert.assertTrue( Utilities.contains( "test", "", "test" ) );
  }

//  @Test
//  public void copyStream() throws IOException {
//
//    String text = "My Message";
//    byte[] input = text.getBytes();
//
//    ByteArrayInputStream instream1 = new ByteArrayInputStream( input );
//    ByteArrayOutputStream outstream1 = new ByteArrayOutputStream();
//    try {
//      Utilities.copy( instream1, outstream1, new byte[0] );
//    } catch( Ant4EclipseException ex ) {
//      Assert.assertEquals( CoreExceptionCode.PRECONDITION_VIOLATION, ex.getExceptionCode() );
//    } finally {
//      Utilities.close( (Closeable) instream1 );
//      Utilities.close( (Closeable) outstream1 );
//    }
//
//    ByteArrayInputStream instream2 = new ByteArrayInputStream( input );
//    ByteArrayOutputStream outstream2 = new ByteArrayOutputStream();
//
//    try {
//      Utilities.copy( instream2, outstream2, new byte[10] );
//    } finally {
//      Utilities.close( (Closeable) instream2 );
//      Utilities.close( (Closeable) outstream2 );
//    }
//
//    Assert.assertEquals( text, new String( outstream2.toByteArray() ) );
//
//  }

//  @Test
//  public void copyResource() throws IOException {
//    URL url = getClass().getClassLoader().getResource( "util/test-jar.jar" );
//    File tempfile = JUnitUtilities.createTempFile();
//    Utilities.copy( url, tempfile );
//    verifyExpandedJar( tempfile );
//  }

  @Test
  public void equals() {
    Assert.assertTrue( Utilities.equals( null, null ) );
    Assert.assertFalse( Utilities.equals( "A", null ) );
    Assert.assertFalse( Utilities.equals( null, "A" ) );
    Assert.assertTrue( Utilities.equals( "A", "A" ) );
    Assert.assertTrue( Utilities.equals( "A", new String( "A" ) ) );
  }

  @Test
  public void newInstance() {

    Object object1 = Utilities.newInstance( UtilitiesTest.class.getName() );
    Assert.assertNotNull( object1 );
    Assert.assertTrue( object1 instanceof UtilitiesTest );

    Object object2 = Utilities.newInstance( String.class.getName(), "Frosch" );
    Assert.assertNotNull( object2 );
    Assert.assertTrue( object2 instanceof String );
    Assert.assertEquals( "Frosch", object2 );

  }

  @Test
  public void createFile() {
    File destfile = Utilities.createTempFile( "Frösche", ".txt", "UTF-8" );
    File file = Utilities.exportResource( "/util/createfile.txt" );
    Assert.assertEquals( file.length(), destfile.length() );
    byte[] current = JUnitUtilities.loadFile( destfile );
    byte[] expected = JUnitUtilities.loadFile( file );
    Assert.assertArrayEquals( expected, current );
  }

  @Test
  public void filter() {

    List<Object> input = new ArrayList<Object>();

    List<Object> result = Utilities.filter( input, String.class );
    Assert.assertTrue( result.isEmpty() );

    input.add( "Bla" );
    result = Utilities.filter( input, String.class );
    Assert.assertEquals( 1, result.size() );
    Assert.assertEquals( "Bla", result.get( 0 ) );

    input.add( new ArrayList<String>() );
    input.add( new LinkedList<String>() );
    result = Utilities.filter( input, String.class );
    Assert.assertEquals( 1, result.size() );
    Assert.assertEquals( "Bla", result.get( 0 ) );

    result = Utilities.filter( input, List.class );
    Assert.assertEquals( 2, result.size() );
    Assert.assertTrue( result.get( 0 ) instanceof List<?> );
    Assert.assertTrue( result.get( 1 ) instanceof List<?> );

    result = Utilities.filter( input, LinkedList.class );
    Assert.assertEquals( 1, result.size() );
    Assert.assertTrue( result.get( 0 ) instanceof LinkedList<?> );

  }

  @Test
  public void exportResource() throws IOException {
    File exported = Utilities.exportResource( "/util/createfile.txt", ".dat" );
    Assert.assertTrue( exported.isFile() );
    URL url = getClass().getClassLoader().getResource( "util/createfile.txt" );
    ByteArrayOutputStream expectedout = new ByteArrayOutputStream();
    InputStream instream = null;
    try {
      instream = url.openStream();
      Utilities.copy( instream, expectedout, new byte[512] );
    } finally {
      Utilities.close( (Closeable) instream );
    }
    byte[] expected = expectedout.toByteArray();
    Assert.assertEquals( expected.length, exported.length() );
    byte[] current = JUnitUtilities.loadFile( exported );
    Assert.assertArrayEquals( expected, current );
  }

  @Test
  public void getAllChildren() throws IOException {

    File file = Utilities.exportResource( "/util/test-jar.jar" );
    File dir = verifyExpandedJar( file );

    List<File> children1 = Utilities.getAllChildren( dir );
    Assert.assertNotNull( children1 );
    Assert.assertEquals( 4, children1.size() );
    Collections.sort( children1 );
    for( File child : children1 ) {
      Assert.assertTrue( child.isFile() );
    }
    Assert.assertEquals( new File( dir, "META-INF/MANIFEST.MF" ), children1.get( 0 ) );
    Assert.assertEquals( new File( dir, "test.jar" ), children1.get( 1 ) );
    Assert.assertEquals( new File( dir, "test.txt" ), children1.get( 2 ) );
    Assert.assertEquals( new File( dir, "test2.jar" ), children1.get( 3 ) );

    File tempdir = Utilities.createTempDir();
    List<File> children2 = Utilities.getAllChildren( tempdir );
    Assert.assertNotNull( children2 );
    Assert.assertEquals( 0, children2.size() );

  }

  @Test
  public void getAndHasChild() throws IOException {
    File file = Utilities.exportResource( "/util/test-jar.jar" );
    File dir = verifyExpandedJar( file );
    Assert.assertTrue( Utilities.hasChild( dir, "test.jar" ) );
    File child1 = Utilities.getChild( dir, "test.jar" );
    Assert.assertNotNull( child1 );
    Assert.assertTrue( child1.isFile() );
    Assert.assertFalse( Utilities.hasChild( dir, "frodo.txt" ) );
    File child2 = Utilities.getChild( dir, "frodo.txt" );
    Assert.assertNull( child2 );
  }

  @Test
  public void hasText() {
    Assert.assertFalse( Utilities.hasText( null ) );
    Assert.assertFalse( Utilities.hasText( "" ) );
    Assert.assertFalse( Utilities.hasText( "   " ) );
    Assert.assertTrue( Utilities.hasText( "12345" ) );
    Assert.assertTrue( Utilities.hasText( "   12345   " ) );
  }

  @Test
  public void listToString() {
    Assert.assertEquals( "", Utilities.listToString( new Object[0], null ) );
    Assert.assertEquals( "A,B,C", Utilities.listToString( new Object[] { "A", "B", "C" }, null ) );
    Assert.assertEquals( "ABC", Utilities.listToString( new Object[] { "A", "B", "C" }, "" ) );
    Assert.assertEquals(
        "12#13.0#14.0#NaN",
        Utilities.listToString(
            new Object[] { Integer.valueOf( 12 ), Float.valueOf( 13 ), Double.valueOf( 14 ),
                Double.valueOf( Double.NaN ) }, "#" ) );
  }

  @Test
  public void readTextContent() throws IOException {

    InputStream instream = getClass().getClassLoader().getResource( "util/createfile.txt" ).openStream();
    try {
      // wrong encoding !
      StringBuffer buffer1 = Utilities.readTextContent( instream, "ISO-8859-1", false );
      Assert.assertNotNull( buffer1 );
      Assert.assertEquals( new String( "Frösche".getBytes( "UTF-8" ), "ISO-8859-1" ), buffer1.toString() );
    } finally {
      Utilities.close( (Closeable) instream );
    }

    instream = getClass().getClassLoader().getResource( "util/createfile.txt" ).openStream();
    try {
      // correct encoding !
      StringBuffer buffer2 = Utilities.readTextContent( instream, "UTF-8", false );
      Assert.assertNotNull( buffer2 );
      Assert.assertEquals( "Frösche", buffer2.toString() );
    } finally {
      Utilities.close( (Closeable) instream );
    }

    // multiple lines, without line separators
    instream = getClass().getClassLoader().getResource( "util/multilined.txt" ).openStream();
    try {
      StringBuffer buffer2 = Utilities.readTextContent( instream, "UTF-8", false );
      Assert.assertNotNull( buffer2 );
      Assert.assertEquals( "FröscheWürfelFlanch", buffer2.toString() );
    } finally {
      Utilities.close( (Closeable) instream );
    }

    // multiple lines, with line separators
    instream = getClass().getClassLoader().getResource( "util/multilined.txt" ).openStream();
    try {
      StringBuffer buffer2 = Utilities.readTextContent( instream, "UTF-8", true );
      Assert.assertNotNull( buffer2 );
      Assert.assertEquals( "Frösche" + Utilities.NL + "Würfel" + Utilities.NL + "Flanch" + Utilities.NL,
          buffer2.toString() );
    } finally {
      Utilities.close( (Closeable) instream );
    }

  }

  @Test
  public void removeTrailingPathSeparator() {
    Assert.assertEquals( null, Utilities.removeTrailingPathSeparator( null ) );
    Assert.assertEquals( "", Utilities.removeTrailingPathSeparator( "" ) );
    Assert.assertEquals( "/", Utilities.removeTrailingPathSeparator( "/" ) );
    Assert.assertEquals( "\\", Utilities.removeTrailingPathSeparator( "\\" ) );
    Assert.assertEquals( "/", Utilities.removeTrailingPathSeparator( "//" ) );
    Assert.assertEquals( "\\", Utilities.removeTrailingPathSeparator( "\\\\" ) );
    Assert.assertEquals( "/mypath", Utilities.removeTrailingPathSeparator( "/mypath" ) );
    Assert.assertEquals( "/mypath", Utilities.removeTrailingPathSeparator( "/mypath/" ) );
    Assert.assertEquals( "/mypath", Utilities.removeTrailingPathSeparator( "/mypath\\" ) );
  }

  @Test
  public void replace() {

    // character based replacements
    Assert.assertEquals( "My blanput Strblang", Utilities.replace( "My input String", 'i', "bla" ) );
    Assert.assertEquals( "My nput Strng", Utilities.replace( "My input String", 'i', "" ) );
    Assert.assertEquals( "My nput Strng", Utilities.replace( "iMy input Stringi", 'i', "" ) );
    Assert.assertEquals( "My iinput Striing", Utilities.replace( "My input String", 'i', "ii" ) );

    // string based replacements
    Assert.assertEquals( "My blanput Strblang", Utilities.replace( "My input String", "i", "bla" ) );
    Assert.assertEquals( "My nput Strng", Utilities.replace( "My input String", "i", "" ) );
    Assert.assertEquals( "My nput Strng", Utilities.replace( "iMy input Stringi", "i", "" ) );
    Assert.assertEquals( "My iinput Striing", Utilities.replace( "My input String", "i", "ii" ) );

  }

  @Test
  public void stripSuffix() {
    Assert.assertEquals( "", Utilities.stripSuffix( "" ) );
    Assert.assertEquals( "", Utilities.stripSuffix( ".txt" ) );
    Assert.assertEquals( "txt", Utilities.stripSuffix( "txt" ) );
    Assert.assertEquals( "bla", Utilities.stripSuffix( "bla.txt" ) );
    Assert.assertEquals( "bla.txt", Utilities.stripSuffix( "bla.txt.txt" ) );
  }

  @Test
  public void toStringTest() {
    Properties properties = new Properties();
    properties.setProperty( "A", "A-Value" );
    properties.setProperty( "B", "B-Value" );
    Assert.assertEquals( "'A' -> 'A-Value'" + Utilities.NL + "'B' -> 'B-Value'" + Utilities.NL,
        Utilities.toString( properties ) );
    Assert.assertEquals( "my-title" + Utilities.NL + "'A' -> 'A-Value'" + Utilities.NL + "'B' -> 'B-Value'"
        + Utilities.NL, Utilities.toString( "my-title", properties ) );
  }

  @Test
  public void toURL() {

    URL expectedurl = getClass().getClassLoader().getResource( "util/test-jar.jar" );

    // the test setup is required to provide the resource as a separate file and not contained
    // within a jar (toURL is only supposed to handle normal file objects)
    Assert.assertFalse( expectedurl.toExternalForm().startsWith( "jar:" ) );

    String path = expectedurl.getPath();

    if( !Utilities.isWindows() ) {
      /**
       * @todo [29-Dec-2009:KASI] This is somewhat odd. The 'getPath' is supposed to return the path itself. For some
       *       reason the unix version returns the protocol, too.
       */
      int idx = path.indexOf( '/' );
      path = path.substring( idx );
    }

    File file = new File( path );

    URL url1 = Utilities.toURL( file );
    Assert.assertEquals( expectedurl, url1 );

  }

  @Test
  public void writeFile() throws IOException {

    // write the file using binary data
    File tempfile1 = JUnitUtilities.createTempFile();
    Utilities.writeFile( tempfile1, "Frösche".getBytes( "UTF-8" ) );

    // write the file using character data (the String)
    File tempfile2 = JUnitUtilities.createTempFile();
    Utilities.writeFile( tempfile2, "Frösche", "UTF-8" );

    InputStream instream1 = new FileInputStream( tempfile1 );
    try {
      StringBuffer buffer1 = Utilities.readTextContent( instream1, "UTF-8", false );
      Assert.assertNotNull( buffer1 );
      Assert.assertEquals( "Frösche", buffer1.toString() );
    } finally {
      Utilities.close( (Closeable) instream1 );
    }

    InputStream instream2 = new FileInputStream( tempfile1 );
    try {
      StringBuffer buffer2 = Utilities.readTextContent( instream2, "UTF-8", false );
      Assert.assertNotNull( buffer2 );
      Assert.assertEquals( "Frösche", buffer2.toString() );
    } finally {
      Utilities.close( (Closeable) instream2 );
    }

  }

  @Test
  public void replaceTokens() {

    Map<String,String> replacements = new Hashtable<String,String>();
    replacements.put( "dev1", "Gerd Wütherich" );
    replacements.put( "devel2", "Nils Hartmann" );
    replacements.put( "d3", "Daniel Kasmeroglu" );

    String template1 = "${notclosed ${devel2} was here. ${unknown} sees ${d3}. Hello ${dev1}";
    String result1 = Utilities.replaceTokens( template1, replacements );
    Assert.assertEquals( "${notclosed Nils Hartmann was here. ${unknown} sees Daniel Kasmeroglu. Hello Gerd Wütherich",
        result1 );

    String result2 = Utilities.replaceTokens( template1, replacements, "$", "$" );
    Assert.assertEquals( "${notclosed ${devel2} was here. ${unknown} sees ${d3}. Hello ${dev1}", result2 );

    String template2 = template1.replace( '$', '@' );
    String result3 = Utilities.replaceTokens( template2, replacements, "@{", "}" );
    Assert.assertEquals( "@{notclosed Nils Hartmann was here. @{unknown} sees Daniel Kasmeroglu. Hello Gerd Wütherich",
        result3 );

    String result4 = Utilities.replaceTokens( "", replacements, "@{", "}" );
    Assert.assertEquals( "", result4 );

  }

} /* ENDCLASS */
