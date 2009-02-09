package org.ant4eclipse.build.tools;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MergeAntlibXml extends AbstractMergeTask {

  private Document _result           = null;

  private Node     _resultAntlibNode = null;

  @SuppressWarnings("unchecked")
  public void doExecute() throws Exception {

    // create result tree
    _result = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    _resultAntlibNode = _result.createElement("antlib");
    _result.appendChild(_resultAntlibNode);

    // 
    for (ResourceCollection resourceCollection : getResourceCollections()) {
      for (Iterator iterator = resourceCollection.iterator(); iterator.hasNext();) {
        Resource resource = (Resource) iterator.next();
        loadFile(resource.getInputStream());
      }
    }

    // iterate through source, and write to file with updated properties
    writeFile();
  }

  private void loadFile(InputStream inputStream) throws SAXException, IOException, ParserConfigurationException {
    // parse the document
    Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);

    // visit the nodes
    visit(document, 0);
  }

  private void visit(Node node, int level) {
    NodeList nl = node.getChildNodes();

    for (int i = 0, cnt = nl.getLength(); i < cnt; i++) {

      if (level == 1 && nl.item(i) instanceof org.w3c.dom.Element) {

        Node importedNode = _result.importNode(nl.item(i), true);

        _resultAntlibNode.appendChild(importedNode);
      }

      if (level == 0) {
        visit(nl.item(i), level + 1);
      }
    }
  }

  private void writeFile() throws TransformerFactoryConfigurationError, TransformerException {

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
