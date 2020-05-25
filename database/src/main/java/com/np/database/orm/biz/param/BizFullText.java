package com.np.database.orm.biz.param;

import com.np.database.exception.NpDbException;
import com.np.database.sql.util.StringUtils;
import com.np.database.sql.util.StringUtils;
import lombok.Getter;
import lombok.Setter;

//full text query based on like
@Getter
@Setter
public class BizFullText {

    private String[] properties;

    private Object value;

    protected BizFullText(String[] columns, String value) {
        if (null == columns || StringUtils.isEmpty(value)/*issue: can not be empty*/) {
            throw new NpDbException("properties and value can't be null or empty");
        }
        this.properties = columns;
        this.value = value;
    }

}
