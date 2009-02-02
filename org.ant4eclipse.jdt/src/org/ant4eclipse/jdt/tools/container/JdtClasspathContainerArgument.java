package org.ant4eclipse.jdt.tools.container;

/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class JdtClasspathContainerArgument {

  private String key;

  private String value;

  /**
   * @return the key
   */
  public final String getKey() {
    return this.key;
  }

  /**
   * @param key
   *          the key to set
   */
  public final void setKey(final String key) {
    this.key = key;
  }

  /**
   * @return the value
   */
  public final String getValue() {
    return this.value;
  }

  /**
   * @param value
   *          the value to set
   */
  public final void setValue(final String value) {
    this.value = value;
  }

  /**
   */
  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer();
    buffer.append("[JdtClasspathContainerArgument:");
    buffer.append(" key: ");
    buffer.append(this.key);
    buffer.append(" value: ");
    buffer.append(this.value);
    buffer.append("]");
    return buffer.toString();
  }
}