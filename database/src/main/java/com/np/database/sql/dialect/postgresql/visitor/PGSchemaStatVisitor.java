
package com.np.database.sql.dialect.postgresql.visitor;

import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.statement.SQLSelectQueryBlock;
import com.np.database.sql.ast.statement.SQLSelectStatement;
import com.np.database.sql.ast.statement.SQLTableSource;
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
import com.np.database.sql.dialect.postgresql.ast.stmt.PGSelectQueryBlock.FetchClause;
import com.np.database.sql.dialect.postgresql.ast.stmt.PGSelectQueryBlock.ForClause;
import com.np.database.sql.dialect.postgresql.ast.stmt.PGSelectQueryBlock.WindowClause;
import com.np.database.sql.dialect.postgresql.ast.stmt.PGSelectStatement;
import com.np.database.sql.dialect.postgresql.ast.stmt.PGShowStatement;
import com.np.database.sql.dialect.postgresql.ast.stmt.PGStartTransactionStatement;
import com.np.database.sql.dialect.postgresql.ast.stmt.PGUpdateStatement;
import com.np.database.sql.dialect.postgresql.ast.stmt.PGValuesQuery;
import com.np.database.sql.visitor.SchemaStatVisitor;
import com.np.database.sql.TableStat;
import com.np.database.sql.TableStat.Mode;
import com.np.database.sql.util.JdbcConstants;
import com.np.database.sql.util.JdbcUtils;
import com.np.database.sql.util.PGUtils;
import com.np.database.sql.TableStat;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.statement.SQLSelectQueryBlock;
import com.np.database.sql.ast.statement.SQLSelectStatement;
import com.np.database.sql.ast.statement.SQLTableSource;
import com.np.database.sql.util.JdbcConstants;
import com.np.database.sql.util.JdbcUtils;
import com.np.database.sql.util.PGUtils;
import com.np.database.sql.visitor.SchemaStatVisitor;

public class PGSchemaStatVisitor extends SchemaStatVisitor implements PGASTVisitor {
    public PGSchemaStatVisitor() {
        super(JdbcConstants.POSTGRESQL);
    }

    @Override
    public String getDbType() {
        return JdbcUtils.POSTGRESQL;
    }

    @Override
    public void endVisit(WindowClause x) {

    }

    @Override
    public boolean visit(WindowClause x) {
        return true;
    }

    @Override
    public void endVisit(FetchClause x) {

    }

    @Override
    public boolean visit(FetchClause x) {
        return true;
    }

    @Override
    public void endVisit(ForClause x) {

    }

    @Override
    public boolean visit(ForClause x) {

        return true;
    }

    @Override
    public void endVisit(PGDeleteStatement x) {

    }

    @Override
    public boolean visit(PGDeleteStatement x) {
        if (repository != null
                && x.getParent() == null) {
            repository.resolve(x);
        }

        if (x.getWith() != null) {
            x.getWith().accept(this);
        }

        SQLTableSource using = x.getUsing();
        if (using != null) {
            using.accept(this);
        }

        x.putAttribute("_original_use_mode", getMode());
        setMode(x, TableStat.Mode.Delete);

        TableStat stat = getTableStat(x.getTableName());
        stat.incrementDeleteCount();

        accept(x.getWhere());

        return false;
    }

    @Override
    public void endVisit(PGInsertStatement x) {

    }

    @Override
    public boolean visit(PGInsertStatement x) {
        if (repository != null
                && x.getParent() == null) {
            repository.resolve(x);
        }

        if (x.getWith() != null) {
            x.getWith().accept(this);
        }

        x.putAttribute("_original_use_mode", getMode());
        setMode(x, TableStat.Mode.Insert);


        SQLName tableName = x.getTableName();
        {
            TableStat stat = getTableStat(tableName);
            stat.incrementInsertCount();
        }

        accept(x.getColumns());
        accept(x.getQuery());

        return false;
    }

    @Override
    public void endVisit(PGSelectStatement x) {

    }

    @Override
    public boolean visit(PGSelectStatement x) {
        return visit((SQLSelectStatement) x);
    }

    @Override
    public void endVisit(PGUpdateStatement x) {

    }

    public boolean isPseudoColumn(long hash) {
        return PGUtils.isPseudoColumn(hash);
    }

    @Override
    public boolean visit(PGUpdateStatement x) {
        if (repository != null
                && x.getParent() == null) {
            repository.resolve(x);
        }

        if (x.getWith() != null) {
            x.getWith().accept(this);
        }

        TableStat stat = getTableStat(x.getTableName());
        stat.incrementUpdateCount();

        accept(x.getFrom());

        accept(x.getItems());
        accept(x.getWhere());

        return false;
    }

    @Override
    public void endVisit(PGSelectQueryBlock x) {
        super.endVisit((SQLSelectQueryBlock) x);
    }

    @Override
    public boolean visit(PGSelectQueryBlock x) {
        return this.visit((SQLSelectQueryBlock) x);
    }

    @Override
    public void endVisit(PGFunctionTableSource x) {

    }

    @Override
    public boolean visit(PGFunctionTableSource x) {
        return true;
    }
    
    @Override
    public boolean visit(PGTypeCastExpr x) {
        x.getExpr().accept(this);
        return false;
    }
    
    @Override
    public void endVisit(PGTypeCastExpr x) {
        
    }

    @Override
    public void endVisit(PGValuesQuery x) {
        
    }

    @Override
    public boolean visit(PGValuesQuery x) {
        return true;
    }
    
    @Override
    public void endVisit(PGExtractExpr x) {
        
    }
    
    @Override
    public boolean visit(PGExtractExpr x) {
        return true;
    }
    
    @Override
    public void endVisit(PGBoxExpr x) {
        
    }
    
    @Override
    public boolean visit(PGBoxExpr x) {
        return true;
    }
    
    @Override
    public void endVisit(PGPointExpr x) {
        
    }
    
    @Override
    public boolean visit(PGMacAddrExpr x) {
        return true;
    }
    
    @Override
    public void endVisit(PGMacAddrExpr x) {
        
    }
    
    @Override
    public boolean visit(PGInetExpr x) {
        return true;
    }
    
    @Override
    public void endVisit(PGInetExpr x) {
        
    }
    
    @Override
    public boolean visit(PGCidrExpr x) {
        return true;
    }
    
    @Override
    public void endVisit(PGCidrExpr x) {
        
    }
    
    @Override
    public boolean visit(PGPolygonExpr x) {
        return true;
    }
    
    @Override
    public void endVisit(PGPolygonExpr x) {
        
    }
    
    @Override
    public boolean visit(PGCircleExpr x) {
        return true;
    }
    
    @Override
    public void endVisit(PGCircleExpr x) {
        
    }
    
    @Override
    public boolean visit(PGLineSegmentsExpr x) {
        return true;
    }

    @Override
    public void endVisit(PGLineSegmentsExpr x) {
        
    }
    
    @Override
    public boolean visit(PGPointExpr x) {
        return true;
    }
    
    @Override
    public void endVisit(PGShowStatement x) {
        
    }
    
    @Override
    public boolean visit(PGShowStatement x) {
        return false;
    }

    @Override
    public void endVisit(PGStartTransactionStatement x) {
        
    }

    @Override
    public boolean visit(PGStartTransactionStatement x) {
        return false;
    }

    @Override
    public void endVisit(PGConnectToStatement x) {

    }

    @Override
    public boolean visit(PGConnectToStatement x) {
        return false;
    }

}
