package com.np.database.orm.biz.param;


import com.np.database.orm.biz.BizOperatorToken;
import lombok.*;

//simple query param
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BizProperty {

    private String column;

    private BizOperatorToken bizOperatorToken;

    private Object[] values;

    private String[] hasColumns;

    //used for update
    private Boolean isIndex;
}