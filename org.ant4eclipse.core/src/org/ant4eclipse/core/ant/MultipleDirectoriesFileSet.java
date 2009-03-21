package org.ant4eclipse.core.ant;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;

public class MultipleDirectoriesFileSet extends DataType implements ResourceCollection {

  public boolean isFilesystemOnly() {
    return true;
  }

  public Iterator<Resource> iterator() {

    DirectoryScanner directoryScanner = new DirectoryScanner();
    directoryScanner.setBasedir("d:/temp");

    directoryScanner.scan();

    String[] files = directoryScanner.getIncludedFiles();

    List<Resource> list = new LinkedList<Resource>();
    for (String name : files) {
      list.add(directoryScanner.getResource(name));
    }
    return list.iterator();
  }

  public int size() {
    return 1;
  }

}
