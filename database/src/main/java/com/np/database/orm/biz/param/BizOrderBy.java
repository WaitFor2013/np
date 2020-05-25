package com.np.database.orm.biz.param;

import lombok.*;

@Getter
@Setter
public class BizOrderBy {

    String column;
    Direction direction;

    protected BizOrderBy(String column, Direction direction) {
        this.column = column;
        this.direction = direction;
    }

}
