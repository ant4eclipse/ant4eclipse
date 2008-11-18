package org.ant4eclipse.core.service;

/**
 * <p>
 * A {@link ServiceNotAvailableException} will be thrown, when a service is requested from the service registry that
 * does not exist.
 * </p>
 */
public class ServiceNotAvailableException extends RuntimeException {

  /** serialVersionUID */
  private static final long serialVersionUID = -5200525359443355796L;

  /**
   * <p>
   * Creates a new instance of type {@link ServiceNotAvailableException}.
   * </p>
   * 
   * @param msg
   *          the message.
   */
  public ServiceNotAvailableException(final String msg) {
    super(msg);
  }
}