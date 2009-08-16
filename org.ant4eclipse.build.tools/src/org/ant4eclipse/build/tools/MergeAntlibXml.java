package org.ant4eclipse.build.tools;

import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.InputStream;
import java.util.Iterator;

/**
 * <p>
 * Merges a given set of antlib.xml files in one target file.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class MergeAntlibXml extends AbstractMergeTask {

  /** the result document */
  private Document _result           = null;

  /** the node that contains the result typedefs */
  private Node     _resultAntlibNode = null;

  /**
   * @see org.ant4eclipse.build.tools.AbstractMergeTask#doExecute()
   */
  @SuppressWarnings("unchecked")
  public void doExecute() throws Exception {

    // create result tree
    _result = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    _resultAntlibNode = _result.createElement("antlib");
    _result.appendChild(_resultAntlibNode);

    // process the specified files
    for (ResourceCollection resourceCollection : getResourceCollections()) {
      for (Iterator iterator = resourceCollection.iterator(); iterator.hasNext();) {
        Resource resource = (Resource) iterator.next();
        loadFile(resource.getInputStream());
      }
    }

    // iterate through source, and write to file with updated properties
    writeFile();
  }

  /**
   * <p>
   * Adds the typedefs defined in the given input stream to the result document.
   * </p>
   * 
   * @param inputStream
   * 
   * @throws Exception
   */
  private void loadFile(InputStream inputStream) throws Exception {
    // parse the document
    Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);

    // visit the nodes
    visit(document.getDocumentElement());
  }

  /**
   * @param node
   * @param level
   */
  private void visit(Element node) {
    NodeList nl = node.getChildNodes();
    for (int i = 0, cnt = nl.getLength(); i < cnt; i++) {
      if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
        Node importedNode = _result.importNode(nl.item(i), true);
        _resultAntlibNode.appendChild(importedNode);
      }
    }
  }

  /**
   * <p>
   * Writes the result document to disc.
   * </p>
   * 
   * @throws Exception
   */
  private void writeFile() throws Exception {

    log("Writing file '" + getDestinationFile() + "'");

    // Prepare the DOM document for writing
    Source source = new DOMSource(_result);

    // Prepare the output file
    Result result = new StreamResult(getDestinationFile());

    // Write the DOM document to the file
    Transformer xformer = TransformerFactory.newInstance().newTransformer();
    xformer.setOutputProperty(OutputKeys.INDENT, "yes");
    xformer.transform(source, result);
  }
}
