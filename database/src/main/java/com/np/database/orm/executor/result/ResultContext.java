package com.np.database.orm.executor.result;

/**
 * @author Clinton Begin
 */
public interface ResultContext<T> {

  T getResultObject();

  int getResultCount();

  boolean isStopped();

  void stop();

}
