/**********************************************************************
 * Copyright (c) 2005-2008 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.platform.model.resource.internal;


import net.sf.ant4eclipse.core.logging.A4ELogging;

import java.util.Vector;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.File;


/**
 * Simple representation for chunk based files as internally used by the
 * Eclipse framework.
 * 
 * @author Daniel Kasmeroglu (daniel.kasmeroglu@kasisoft.net)
 */
public class ChunkyFile {

  
  private static final byte[] BEGIN_CHUNK = {
    64, -79, -117, -127, 35, -68, 0, 20, 26, 37, -106, -25, -93, -109, -66, 30
  };

  private static final byte[] END_CHUNK   = {
    -64, 88, -5, -13, 35, -68, 0, 20, 26, 81, -13, -116, 123, -69, 119, -58
  };

  
  private Vector<byte[]>    _chunkdata ;
  
  
  /**
   * Creates a chunked representation of the supplied file.
   * 
   * @param source
   *            The file that shall be loaded.
   *
   * @throws  IOException   Reading the file failed for some reason.
   */
  public ChunkyFile(File source) throws IOException {
    _chunkdata         = new Vector<byte[]>();
    byte[]      data  = new byte[(int) source.length()];
    InputStream input = null;
    try {
      input = new FileInputStream(source);
      input.read(data);
      loadChunks(data);
    } catch (IOException ex) {
      A4ELogging.error(ex.getMessage());
      throw (ex);
    } finally {
      if (input != null) {
        input.close();
      }
    }
  }
  
  
  /**
   * Returns the number of available chunks.
   * 
   * @return  The number of available chunks.
   */
  public int getChunkCount() {
    return (_chunkdata.size());
  }
  
  
  /**
   * Returns a chunk by it's index.
   * 
   * @param index
   *            The index of the chunk.
   *
   * @return  The chunk data. null if the index wasn't valid.
   */
  public byte[] getChunk(int index) {
    if ((index >= 0) && (index < _chunkdata.size())) {
      return _chunkdata.get(index);
    }
    return (null);
  }
  
  
  /**
   * Loads each chunk while running through the supplied file content.
   * 
   * @param content
   *            A bytewise representation of the file.
   */
  private void loadChunks(byte[] content) {
    int ptr = find(content, BEGIN_CHUNK, 0);
    while ((ptr != -1) && (ptr < content.length)) {
      ptr       = ptr + BEGIN_CHUNK.length;
      int end   = find(content, END_CHUNK, ptr);
      if (end != -1) {
        byte[] data = new byte[end - ptr];
        System.arraycopy(content, ptr, data, 0, data.length);
        _chunkdata.add(data);
        ptr = end + END_CHUNK.length; 
      }
      ptr = find(content, BEGIN_CHUNK, ptr);
    }
  }
  
  
  /**
   * Returns the index of a byte sequence.
   * 
   * @param content
   *            The memory where to search for the byte sequence.
   * @param sequence
   *            The sequence that shall be searched.
   * @param first
   *            Initial starting point.
   *
   * @return  The index of the byte sequence or -1 if the 
   *          sequence could not be found.
   */
  private int find(byte[] content, byte[] sequence, int first) {
    while ((first + sequence.length) <= content.length) {
      if (content[first] == sequence[0]) {
        int i = 0;
        int j = first;
        while ((i < sequence.length) && (content[j] == sequence[i])) {
          i++;
          j++;
        }
        if (i == sequence.length) {
          return (first);
        }
      }
      first++;
    }
    return (-1);
  }
  
  
} /* ENDCLASS */
