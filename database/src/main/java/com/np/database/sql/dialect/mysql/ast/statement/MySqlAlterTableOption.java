
package com.np.database.sql.dialect.mysql.ast.statement;

import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.ast.expr.SQLIdentifierExpr;
import com.np.database.sql.ast.statement.SQLAlterTableItem;
import com.np.database.sql.dialect.mysql.ast.MySqlObjectImpl;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.ast.expr.SQLIdentifierExpr;
import com.np.database.sql.ast.statement.SQLAlterTableItem;
import com.np.database.sql.dialect.mysql.ast.MySqlObjectImpl;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;

public class MySqlAlterTableOption extends MySqlObjectImpl implements SQLAlterTableItem {

    private String name;
    private SQLObject value;

    public MySqlAlterTableOption(String name, String value){
        this(name, new SQLIdentifierExpr(value));
    }

    public MySqlAlterTableOption(String name, SQLObject value){
        this.name = name;
        this.setValue(value);
    }

    public MySqlAlterTableOption(){
    }

    @Override
    public void accept0(MySqlASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SQLObject getValue() {
        return value;
    }

    public void setValue(SQLObject value) {
        this.value = value;
    }

}
