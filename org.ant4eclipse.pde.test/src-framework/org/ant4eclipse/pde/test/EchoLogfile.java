package org.ant4eclipse.pde.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Assert;

public class EchoLogfile extends Assert {

  private final String         _echoLogfileName;

  private final BufferedReader _reader;

  public EchoLogfile(String echoLogfileName) throws FileNotFoundException {
    assertNotNull(echoLogfileName != null);
    this._echoLogfileName = echoLogfileName;

    File echoLogfile = new File(echoLogfileName);
    assertTrue(echoLogfile.exists());

    this._reader = new BufferedReader(new FileReader(echoLogfile));
  }

  /**
   * Asserts that the next line in the EchoLogfile is equal to the specified expectedLine.
   * 
   * <p>
   * Note that the line read from the file is <code>trim()</code>ed before comparsion!
   * 
   * @param expectedLine
   * @throws IOException
   */
  public void assertLine(String expectedLine) throws IOException {
    String line = this._reader.readLine();

    if (line != null) {
      line = line.trim();
    }

    assertEquals(expectedLine, line);
  }

  /**
   * Returns the path of this echo log file
   * 
   * @return
   */
  public String getEchoLogfileName() {
    return this._echoLogfileName;
  }

  /**
   * Disposes this instance.
   * 
   * <p>
   * This method closes the reader. It <b>never<b> throws an Exception
   */
  public void dispose() {
    try {
      this._reader.close();
    } catch (Exception ex) {
      System.err.println("WARN! Could not close echo logfile reader !");
    }
  }

}
