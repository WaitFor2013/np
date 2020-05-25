
package com.np.database.sql.dialect.mysql.ast.expr;

import java.util.Collections;
import java.util.List;

import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.util.FnvHash;

public class MySqlUserName extends MySqlExprImpl implements SQLName, Cloneable {

    private String userName;
    private String host;
    private String identifiedBy;

    private long   userNameHashCod64;
    private long   hashCode64;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;

        this.hashCode64 = 0;
        this.userNameHashCod64 = 0;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;

        this.hashCode64 = 0;
        this.userNameHashCod64 = 0;
    }

    @Override
    public void accept0(MySqlASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }

    public String getSimpleName() {
        return userName + '@' + host;
    }

    public String getIdentifiedBy() {
        return identifiedBy;
    }

    public void setIdentifiedBy(String identifiedBy) {
        this.identifiedBy = identifiedBy;
    }

    public String toString() {
        return getSimpleName();
    }

    public MySqlUserName clone() {
        MySqlUserName x = new MySqlUserName();

        x.userName     = userName;
        x.host         = host;
        x.identifiedBy = identifiedBy;

        return x;
    }

    @Override
    public List<SQLObject> getChildren() {
        return Collections.emptyList();
    }

    public long nameHashCode64() {
        if (userNameHashCod64 == 0
                && userName != null) {
            userNameHashCod64 = FnvHash.hashCode64(userName);
        }
        return userNameHashCod64;
    }

    @Override
    public long hashCode64() {
        if (hashCode64 == 0) {
            if (host != null) {
                long hash = FnvHash.hashCode64(host);
                hash ^= '@';
                hash *= 0x100000001b3L;
                hash = FnvHash.hashCode64(hash, userName);

                hashCode64 = hash;
            } else {
                hashCode64 = nameHashCode64();
            }
        }

        return hashCode64;
    }
}
