
package com.np.database.sql.parser;

public enum SQLParserFeature {
    KeepInsertValueClauseOriginalString,
    KeepSelectListOriginalString, // for improved sql parameterized performance
    UseInsertColumnsCache,
    EnableSQLBinaryOpExprGroup,
    OptimizedForParameterized,
    OptimizedForForParameterizedSkipValue,
    KeepComments,
    SkipComments,
    StrictForWall,
    EnableMultiUnion,
    IgnoreNameQuotes,
    EnableCurrentUserExpr,

    PipesAsConcat, // for mysql
    ;

    private SQLParserFeature(){
        mask = (1 << ordinal());
    }

    public final int mask;


    public static boolean isEnabled(int features, SQLParserFeature feature) {
        return (features & feature.mask) != 0;
    }

    public static int config(int features, SQLParserFeature feature, boolean state) {
        if (state) {
            features |= feature.mask;
        } else {
            features &= ~feature.mask;
        }

        return features;
    }

    public static int of(SQLParserFeature... features) {
        if (features == null) {
            return 0;
        }

        int value = 0;

        for (SQLParserFeature feature: features) {
            value |= feature.mask;
        }

        return value;
    }
}
