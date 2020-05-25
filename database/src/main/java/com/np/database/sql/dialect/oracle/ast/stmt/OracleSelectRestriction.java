
package com.np.database.sql.dialect.oracle.ast.stmt;

import com.np.database.sql.dialect.oracle.ast.OracleSQLObjectImpl;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;

public abstract class OracleSelectRestriction extends OracleSQLObjectImpl {

    public OracleSelectRestriction(){

    }

    public static class CheckOption extends OracleSelectRestriction {

        private OracleConstraint constraint;

        public CheckOption(){

        }

        public OracleConstraint getConstraint() {
            return this.constraint;
        }

        public void setConstraint(OracleConstraint constraint) {
            this.constraint = constraint;
        }

        public void accept0(OracleASTVisitor visitor) {
            if (visitor.visit(this)) {
                acceptChild(visitor, this.constraint);
            }

            visitor.endVisit(this);
        }

        public CheckOption clone() {
            CheckOption x = new CheckOption();
            if (constraint != null) {
                x.setConstraint(constraint.clone());
            }
            return x;
        }
    }

    public static class ReadOnly extends OracleSelectRestriction {

        public ReadOnly(){

        }

        public void accept0(OracleASTVisitor visitor) {
            visitor.visit(this);

            visitor.endVisit(this);
        }

        public ReadOnly clone() {
            ReadOnly x = new ReadOnly();
            return x;
        }
    }


}
