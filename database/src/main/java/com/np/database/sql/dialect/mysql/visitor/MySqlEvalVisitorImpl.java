
package com.np.database.sql.dialect.mysql.visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.np.database.sql.ast.expr.SQLBetweenExpr;
import com.np.database.sql.ast.expr.SQLBinaryExpr;
import com.np.database.sql.ast.expr.SQLBinaryOpExpr;
import com.np.database.sql.ast.expr.SQLBooleanExpr;
import com.np.database.sql.ast.expr.SQLCaseExpr;
import com.np.database.sql.ast.expr.SQLCharExpr;
import com.np.database.sql.ast.expr.SQLHexExpr;
import com.np.database.sql.ast.expr.SQLIdentifierExpr;
import com.np.database.sql.ast.expr.SQLInListExpr;
import com.np.database.sql.ast.expr.SQLIntegerExpr;
import com.np.database.sql.ast.expr.SQLMethodInvokeExpr;
import com.np.database.sql.ast.expr.SQLNullExpr;
import com.np.database.sql.ast.expr.SQLNumberExpr;
import com.np.database.sql.ast.expr.SQLQueryExpr;
import com.np.database.sql.ast.expr.SQLUnaryExpr;
import com.np.database.sql.ast.expr.SQLVariantRefExpr;
import com.np.database.sql.visitor.SQLEvalVisitor;
import com.np.database.sql.visitor.SQLEvalVisitorUtils;
import com.np.database.sql.visitor.functions.Function;
import com.np.database.sql.ast.expr.*;
import com.np.database.sql.visitor.SQLEvalVisitor;
import com.np.database.sql.visitor.SQLEvalVisitorUtils;
import com.np.database.sql.visitor.functions.Function;

public class MySqlEvalVisitorImpl extends MySqlASTVisitorAdapter implements SQLEvalVisitor {

    private Map<String, Function> functions        = new HashMap<String, Function>();
    private List<Object>          parameters       = new ArrayList<Object>();

    private int                   variantIndex     = -1;

    private boolean               markVariantIndex = true;

    public MySqlEvalVisitorImpl(){
        this(new ArrayList<Object>(1));
    }

    public MySqlEvalVisitorImpl(List<Object> parameters){
        this.parameters = parameters;
    }

    public List<Object> getParameters() {
        return parameters;
    }

    public void setParameters(List<Object> parameters) {
        this.parameters = parameters;
    }

    public boolean visit(SQLCharExpr x) {
        return SQLEvalVisitorUtils.visit(this, x);
    }

    public int incrementAndGetVariantIndex() {
        return ++variantIndex;
    }

    public int getVariantIndex() {
        return variantIndex;
    }

    public boolean visit(SQLVariantRefExpr x) {
        return SQLEvalVisitorUtils.visit(this, x);
    }

    public boolean visit(SQLBinaryOpExpr x) {
        return SQLEvalVisitorUtils.visit(this, x);
    }

    public boolean visit(SQLUnaryExpr x) {
        return SQLEvalVisitorUtils.visit(this, x);
    }

    public boolean visit(SQLIntegerExpr x) {
        return SQLEvalVisitorUtils.visit(this, x);
    }

    public boolean visit(SQLNumberExpr x) {
        return SQLEvalVisitorUtils.visit(this, x);
    }
    
    public boolean visit(SQLHexExpr x) {
        return SQLEvalVisitorUtils.visit(this, x);
    }
    
    @Override
    public boolean visit(SQLBinaryExpr x) {
        return SQLEvalVisitorUtils.visit(this, x);
    }

    @Override
    public boolean visit(SQLCaseExpr x) {
        return SQLEvalVisitorUtils.visit(this, x);
    }

    @Override
    public boolean visit(SQLBetweenExpr x) {
        return SQLEvalVisitorUtils.visit(this, x);
    }

    @Override
    public boolean visit(SQLInListExpr x) {
        return SQLEvalVisitorUtils.visit(this, x);
    }

    @Override
    public boolean visit(SQLNullExpr x) {
        return SQLEvalVisitorUtils.visit(this, x);
    }

    @Override
    public boolean visit(SQLMethodInvokeExpr x) {
        return SQLEvalVisitorUtils.visit(this, x);
    }

    @Override
    public boolean visit(SQLQueryExpr x) {
        return SQLEvalVisitorUtils.visit(this, x);
    }

    public boolean isMarkVariantIndex() {
        return markVariantIndex;
    }

    public void setMarkVariantIndex(boolean markVariantIndex) {
        this.markVariantIndex = markVariantIndex;
    }

    @Override
    public Function getFunction(String funcName) {
        return functions.get(funcName);
    }

    @Override
    public void registerFunction(String funcName, Function function) {
        functions.put(funcName, function);
    }

    public boolean visit(SQLIdentifierExpr x) {
        return SQLEvalVisitorUtils.visit(this, x);
    }

    @Override
    public void unregisterFunction(String funcName) {
        functions.remove(funcName);
    }
    
    @Override
    public boolean visit(SQLBooleanExpr x) {
        x.getAttributes().put(EVAL_VALUE, x.getBooleanValue());
        return false;
    }
}
