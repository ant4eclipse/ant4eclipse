package net.sf.ant4eclipse.core.service;

/**
 * <p>
 * A {@link NoUniqueIdentifierException} will be thrown, when a service is registered with an id that already exists in
 * the service registry.
 * </p>
 */
public class NoUniqueIdentifierException extends RuntimeException {

  /** serialVersionUID */
  private static final long serialVersionUID = 5291467360326912400L;

  /**
   * <p>
   * Creates a new instance of type {@link NoUniqueIdentifierException}.
   * </p>
   * 
   * @param msg
   *          the message of this exception.
   */
  public NoUniqueIdentifierException(final String msg) {
    super(msg);
  }
}
