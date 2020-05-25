
package com.np.database.sql.visitor.functions;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.expr.SQLMethodInvokeExpr;
import com.np.database.sql.parser.ParserException;
import com.np.database.sql.util.NpHexBin;
import com.np.database.sql.visitor.SQLEvalVisitor;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.expr.SQLMethodInvokeExpr;
import com.np.database.sql.parser.ParserException;
import com.np.database.sql.util.NpHexBin;

import static com.np.database.sql.visitor.SQLEvalVisitor.EVAL_VALUE;

public class Hex implements Function {

    public final static Hex instance = new Hex();

    public Object eval(SQLEvalVisitor visitor, SQLMethodInvokeExpr x) {
        if (x.getParameters().size() != 1) {
            throw new ParserException("argument's != 1, " + x.getParameters().size());
        }

        SQLExpr param0 = x.getParameters().get(0);
        param0.accept(visitor);

        Object param0Value = param0.getAttributes().get(EVAL_VALUE);
        if (param0Value == null) {
            return SQLEvalVisitor.EVAL_ERROR;
        }

        if (param0Value instanceof String) {
            byte[] bytes = ((String) param0Value).getBytes();
            String result = NpHexBin.encode(bytes);
            return result;
        }

        if (param0Value instanceof Number) {
            long value = ((Number) param0Value).longValue();
            String result = Long.toHexString(value).toUpperCase();
            return result;
        }

        return SQLEvalVisitor.EVAL_ERROR;
    }
}
