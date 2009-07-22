package org.ant4eclipse.core.util;

public class StopWatch {

  private long    startTime   = 0;

  private long    stopTime    = 0;

  private boolean _inProgress = false;

  public void start() {
    this.startTime = System.currentTimeMillis();
    this._inProgress = true;
  }

  public void stop() {
    this.stopTime = System.currentTimeMillis();
    this._inProgress = false;
  }

  public long getElapsedTime() {
    long elapsed;
    if (this._inProgress) {
      elapsed = (System.currentTimeMillis() - this.startTime);
    } else {
      elapsed = (this.stopTime - this.startTime);
    }
    return elapsed;
  }
}
