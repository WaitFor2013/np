package com.np.database.orm.mapping;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @author Clinton Begin
 */
@Slf4j
@Getter
@Setter
public class ResultMap {

  private String id;
  private Class<?> type;
  private List<ResultMapping> resultMappings;
  private Set<String> mappedColumns;
  private Set<String> mappedProperties;

  private ResultMap() {
  }

  public static class Builder {

    private ResultMap resultMap = new ResultMap();

    public Builder(Class<?> type, List<ResultMapping> resultMappings) {
      resultMap.id = "defaultResultMap";
      resultMap.type = type;
      resultMap.resultMappings = resultMappings;
    }

    public Class<?> type() {
      return resultMap.type;
    }

    public ResultMap build() {
      if (resultMap.id == null) {
        throw new IllegalArgumentException("ResultMaps must have an id");
      }

      resultMap.mappedColumns = new HashSet<>();
      resultMap.mappedProperties = new HashSet<>();
      for (ResultMapping resultMapping : resultMap.resultMappings) {

        final String column = resultMapping.getColumn();
        if (column != null) {
          resultMap.mappedColumns.add(column.toUpperCase(Locale.ENGLISH));
        } else if (resultMapping.isCompositeResult()) {
          for (ResultMapping compositeResultMapping : resultMapping.getComposites()) {
            final String compositeColumn = compositeResultMapping.getColumn();
            if (compositeColumn != null) {
              resultMap.mappedColumns.add(compositeColumn.toUpperCase(Locale.ENGLISH));
            }
          }
        }
        final String property = resultMapping.getProperty();
        if (property != null) {
          resultMap.mappedProperties.add(property);
        }

      }

      // lock down collections
      resultMap.resultMappings = Collections.unmodifiableList(resultMap.resultMappings);

      return resultMap;
    }

  }

  public String getId() {
    return id;
  }

  public Class<?> getType() {
    return type;
  }

  public List<ResultMapping> getResultMappings() {
    return resultMappings;
  }
}
