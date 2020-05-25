
package com.np.database.sql.visitor;

public enum  VisitorFeature {
    OutputUCase,
    OutputPrettyFormat,
    OutputParameterized,
    OutputDesensitize,
    OutputUseInsertValueClauseOriginalString,
    OutputSkipSelectListCacheString,
    OutputSkipInsertColumnsString,
    OutputParameterizedQuesUnMergeInList,
    OutputParameterizedQuesUnMergeOr,

    /**
     * @deprecated
     */
    OutputKeepParenthesisWhenNotExpr
    ;

    private VisitorFeature(){
        mask = (1 << ordinal());
    }

    public final int mask;


    public static boolean isEnabled(int features, VisitorFeature feature) {
        return (features & feature.mask) != 0;
    }

    public static int config(int features, VisitorFeature feature, boolean state) {
        if (state) {
            features |= feature.mask;
        } else {
            features &= ~feature.mask;
        }

        return features;
    }

    public static int of(VisitorFeature... features) {
        if (features == null) {
            return 0;
        }

        int value = 0;

        for (VisitorFeature feature: features) {
            value |= feature.mask;
        }

        return value;
    }
}
