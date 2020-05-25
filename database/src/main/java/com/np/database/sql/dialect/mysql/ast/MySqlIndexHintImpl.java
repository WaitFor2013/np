
package com.np.database.sql.dialect.mysql.ast;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLName;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;

public abstract class MySqlIndexHintImpl extends MySqlObjectImpl implements MySqlIndexHint {

    private MySqlIndexHint.Option option;

    private List<SQLName>         indexList = new ArrayList<SQLName>();

    @Override
    public abstract void accept0(MySqlASTVisitor visitor);

    public MySqlIndexHint.Option getOption() {
        return option;
    }

    public void setOption(MySqlIndexHint.Option option) {
        this.option = option;
    }

    public List<SQLName> getIndexList() {
        return indexList;
    }

    public void setIndexList(List<SQLName> indexList) {
        this.indexList = indexList;
    }

    public abstract MySqlIndexHintImpl clone();

    public void cloneTo(MySqlIndexHintImpl x) {
        x.option = option;
        for (SQLName name : indexList) {
            SQLName name2 = name.clone();
            name2.setParent(x);
            x.indexList.add(name2);
        }
    }
}
