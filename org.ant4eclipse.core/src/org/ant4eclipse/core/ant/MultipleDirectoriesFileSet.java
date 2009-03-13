package org.ant4eclipse.core.ant;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.FileResource;

public class MultipleDirectoriesFileSet extends DataType implements ResourceCollection {

  public boolean isFilesystemOnly() {
    return true;
  }

  public Iterator<FileResource> iterator() {

    List<FileResource> list = new LinkedList<FileResource>();
    list.add(new FileResource(new File("Hurz")));
    return list.iterator();
  }

  public int size() {
    return 1;
  }

}
