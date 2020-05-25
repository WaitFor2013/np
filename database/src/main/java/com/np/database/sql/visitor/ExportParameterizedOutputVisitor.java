
package com.np.database.sql.visitor;

import java.util.ArrayList;
import java.util.List;

public class ExportParameterizedOutputVisitor extends SQLASTOutputVisitor implements ExportParameterVisitor {

    /**
     * true= if require parameterized sql output
     */
    private final boolean requireParameterizedOutput;

    public ExportParameterizedOutputVisitor(final List<Object> parameters,final Appendable appender,final boolean wantParameterizedOutput){
        super(appender, true);
        this.parameters = parameters;
        this.requireParameterizedOutput = wantParameterizedOutput;
    }

    public ExportParameterizedOutputVisitor() {
        this(new ArrayList<Object>());
    }

    public ExportParameterizedOutputVisitor(final List<Object> parameters){
        this(parameters,new StringBuilder(),false);
    }

    public ExportParameterizedOutputVisitor(final Appendable appender) {
        this(new ArrayList<Object>(), appender, true);
    }

    
    public List<Object> getParameters() {
        return parameters;
    }
}
