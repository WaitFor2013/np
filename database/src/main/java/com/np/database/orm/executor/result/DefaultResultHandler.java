package com.np.database.orm.executor.result;


import com.np.database.reflection.factory.ObjectFactory;

import java.util.List;

/**
 * @author Clinton Begin
 */
public class DefaultResultHandler implements ResultHandler<Object> {

  private final List<Object> list;

  @SuppressWarnings("unchecked")
  public DefaultResultHandler(ObjectFactory objectFactory) {
    list = objectFactory.create(List.class);
  }

  @Override
  public void handleResult(ResultContext<?> context) {
    list.add(context.getResultObject());
  }

  public List<Object> getResultList() {
    return list;
  }

}
