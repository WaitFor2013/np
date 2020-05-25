
package com.np.database.sql.dialect.postgresql.visitor;

import com.np.database.sql.dialect.postgresql.ast.expr.PGBoxExpr;
import com.np.database.sql.dialect.postgresql.ast.expr.PGCidrExpr;
import com.np.database.sql.dialect.postgresql.ast.expr.PGCircleExpr;
import com.np.database.sql.dialect.postgresql.ast.expr.PGExtractExpr;
import com.np.database.sql.dialect.postgresql.ast.expr.PGInetExpr;
import com.np.database.sql.dialect.postgresql.ast.expr.PGLineSegmentsExpr;
import com.np.database.sql.dialect.postgresql.ast.expr.PGMacAddrExpr;
import com.np.database.sql.dialect.postgresql.ast.expr.PGPointExpr;
import com.np.database.sql.dialect.postgresql.ast.expr.PGPolygonExpr;
import com.np.database.sql.dialect.postgresql.ast.expr.PGTypeCastExpr;
import com.np.database.sql.dialect.postgresql.ast.stmt.PGConnectToStatement;
import com.np.database.sql.dialect.postgresql.ast.stmt.PGDeleteStatement;
import com.np.database.sql.dialect.postgresql.ast.stmt.PGFunctionTableSource;
import com.np.database.sql.dialect.postgresql.ast.stmt.PGInsertStatement;
import com.np.database.sql.dialect.postgresql.ast.stmt.PGSelectQueryBlock;
import com.np.database.sql.dialect.postgresql.ast.stmt.PGSelectStatement;
import com.np.database.sql.dialect.postgresql.ast.stmt.PGShowStatement;
import com.np.database.sql.dialect.postgresql.ast.stmt.PGStartTransactionStatement;
import com.np.database.sql.dialect.postgresql.ast.stmt.PGUpdateStatement;
import com.np.database.sql.dialect.postgresql.ast.stmt.PGValuesQuery;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;

public interface PGASTVisitor extends SQLASTVisitor {

    void endVisit(PGSelectQueryBlock x);

    boolean visit(PGSelectQueryBlock x);

    void endVisit(PGSelectQueryBlock.WindowClause x);

    boolean visit(PGSelectQueryBlock.WindowClause x);

    void endVisit(PGSelectQueryBlock.FetchClause x);

    boolean visit(PGSelectQueryBlock.FetchClause x);

    void endVisit(PGSelectQueryBlock.ForClause x);

    boolean visit(PGSelectQueryBlock.ForClause x);

    void endVisit(PGDeleteStatement x);

    boolean visit(PGDeleteStatement x);

    void endVisit(PGInsertStatement x);

    boolean visit(PGInsertStatement x);

    void endVisit(PGSelectStatement x);

    boolean visit(PGSelectStatement x);

    void endVisit(PGUpdateStatement x);

    boolean visit(PGUpdateStatement x);

    void endVisit(PGFunctionTableSource x);

    boolean visit(PGFunctionTableSource x);
    
    void endVisit(PGTypeCastExpr x);
    
    boolean visit(PGTypeCastExpr x);
    
    void endVisit(PGValuesQuery x);
    
    boolean visit(PGValuesQuery x);
    
    void endVisit(PGExtractExpr x);
    
    boolean visit(PGExtractExpr x);
    
    void endVisit(PGBoxExpr x);
    
    boolean visit(PGBoxExpr x);
    
    void endVisit(PGPointExpr x);
    
    boolean visit(PGPointExpr x);
    
    void endVisit(PGMacAddrExpr x);
    
    boolean visit(PGMacAddrExpr x);
    
    void endVisit(PGInetExpr x);
    
    boolean visit(PGInetExpr x);
    
    void endVisit(PGCidrExpr x);
    
    boolean visit(PGCidrExpr x);
    
    void endVisit(PGPolygonExpr x);
    
    boolean visit(PGPolygonExpr x);
    
    void endVisit(PGCircleExpr x);
    
    boolean visit(PGCircleExpr x);
    
    void endVisit(PGLineSegmentsExpr x);
    
    boolean visit(PGLineSegmentsExpr x);

    void endVisit(PGShowStatement x);
    
    boolean visit(PGShowStatement x);

    void endVisit(PGStartTransactionStatement x);
    boolean visit(PGStartTransactionStatement x);

    void endVisit(PGConnectToStatement x);
    boolean visit(PGConnectToStatement x);

}
