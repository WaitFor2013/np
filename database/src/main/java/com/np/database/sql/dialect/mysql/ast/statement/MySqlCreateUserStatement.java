
package com.np.database.sql.dialect.mysql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.statement.SQLCreateStatement;
import com.np.database.sql.dialect.mysql.ast.MySqlObjectImpl;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.statement.SQLCreateStatement;
import com.np.database.sql.dialect.mysql.ast.MySqlObjectImpl;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;

public class MySqlCreateUserStatement extends MySqlStatementImpl implements SQLCreateStatement {

    private List<UserSpecification> users = new ArrayList<UserSpecification>(2);

    public List<UserSpecification> getUsers() {
        return users;
    }

    public void addUser(UserSpecification user) {
        if (user != null) {
            user.setParent(this);
        }
        this.users.add(user);
    }

    @Override
    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, users);
        }
        visitor.endVisit(this);
    }

    public static class UserSpecification extends MySqlObjectImpl {

        private SQLExpr user;
        private boolean passwordHash = false;
        private SQLExpr password;
        private SQLExpr authPlugin;

        public SQLExpr getUser() {
            return user;
        }

        public void setUser(SQLExpr user) {
            this.user = user;
        }

        public boolean isPasswordHash() {
            return passwordHash;
        }

        public void setPasswordHash(boolean passwordHash) {
            this.passwordHash = passwordHash;
        }

        public SQLExpr getPassword() {
            return password;
        }

        public void setPassword(SQLExpr password) {
            this.password = password;
        }

        public SQLExpr getAuthPlugin() {
            return authPlugin;
        }

        public void setAuthPlugin(SQLExpr authPlugin) {
            this.authPlugin = authPlugin;
        }

        @Override
        public void accept0(MySqlASTVisitor visitor) {
            if (visitor.visit(this)) {
                acceptChild(visitor, user);
                acceptChild(visitor, password);
                acceptChild(visitor, authPlugin);
            }
            visitor.endVisit(this);
        }

    }
}
