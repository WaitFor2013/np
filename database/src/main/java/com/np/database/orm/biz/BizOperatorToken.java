package com.np.database.orm.biz;

/**
 * @Author ch
 */
public enum BizOperatorToken {

    //操作符
    //is equal to
    EQ("=", "等于"),

    //is not equal to
    NEQ("!=", "不等于"),

    //is less than
    LT("<", "小于"),

    //is less than or equal to
    LTEQ("<=", "小于等于"),

    //is greater than
    GT(">", "大于"),

    //is greater than or equal to
    GTEQ(">=", "大于等于"),

    //contains
    LIKE("like", "包含"),

    //contains(case insensitive)

    //does not contains
    NOT_LIKE("not like", "不包含"),

    //does not contains(case insensitive)

    //begin with

    //does not begin with

    //end with

    //does not end with

    //is null
    IS_NULL_OR_EMPTY("is null or empty", "为空"),

    //is not null
    IS_NOT_NULL_OR_EMPTY("is not null or empty", "为空"),

    //is empty

    //is not empty

    //is between
    IS_BETWEEN("is between", "在 之间"),

    //is not between
    IS_NOT_BETWEEN("is not between", "不在 之间"),

    //is in list
    IS_IN(" in", "在 之中"),

    //is not in list
    IS_NOT_IN(" not in", "不在 之中"),

    //@>
    ARRAY_CONTAINS(" @> ", "数组包含")

    //自定义？
    ;

    public final String tag;
    public final String name;

    BizOperatorToken(String tag, String name) {
        this.tag = tag;
        this.name = name;
    }

}
