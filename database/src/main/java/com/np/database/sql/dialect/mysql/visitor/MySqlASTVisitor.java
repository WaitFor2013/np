
package com.np.database.sql.dialect.mysql.visitor;

import com.np.database.sql.dialect.mysql.ast.MySqlForceIndexHint;
import com.np.database.sql.dialect.mysql.ast.MySqlIgnoreIndexHint;
import com.np.database.sql.dialect.mysql.ast.MySqlKey;
import com.np.database.sql.dialect.mysql.ast.MySqlPrimaryKey;
import com.np.database.sql.dialect.mysql.ast.MySqlUnique;
import com.np.database.sql.dialect.mysql.ast.MySqlUseIndexHint;
import com.np.database.sql.dialect.mysql.ast.MysqlForeignKey;
import com.np.database.sql.dialect.mysql.ast.clause.MySqlCaseStatement;
import com.np.database.sql.dialect.mysql.ast.clause.MySqlCaseStatement.MySqlWhenStatement;
import com.np.database.sql.dialect.mysql.ast.clause.MySqlCursorDeclareStatement;
import com.np.database.sql.dialect.mysql.ast.clause.MySqlDeclareConditionStatement;
import com.np.database.sql.dialect.mysql.ast.clause.MySqlDeclareHandlerStatement;
import com.np.database.sql.dialect.mysql.ast.clause.MySqlDeclareStatement;
import com.np.database.sql.dialect.mysql.ast.clause.MySqlIterateStatement;
import com.np.database.sql.dialect.mysql.ast.clause.MySqlLeaveStatement;
import com.np.database.sql.dialect.mysql.ast.clause.MySqlRepeatStatement;
import com.np.database.sql.dialect.mysql.ast.clause.MySqlSelectIntoStatement;
import com.np.database.sql.dialect.mysql.ast.expr.MySqlCharExpr;
import com.np.database.sql.dialect.mysql.ast.expr.MySqlExtractExpr;
import com.np.database.sql.dialect.mysql.ast.expr.MySqlMatchAgainstExpr;
import com.np.database.sql.dialect.mysql.ast.expr.MySqlOrderingExpr;
import com.np.database.sql.dialect.mysql.ast.expr.MySqlOutFileExpr;
import com.np.database.sql.dialect.mysql.ast.expr.MySqlUserName;
import com.np.database.sql.dialect.mysql.ast.statement.CobarShowStatus;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlAlterEventStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlAlterLogFileGroupStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlAlterServerStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlAlterTableAlterColumn;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlAlterTableChangeColumn;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlAlterTableDiscardTablespace;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlAlterTableImportTablespace;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlAlterTableModifyColumn;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlAlterTableOption;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlAlterTablespaceStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlAlterUserStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlAnalyzeStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlBinlogStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlChecksumTableStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlCreateAddLogFileGroupStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlCreateEventStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlCreateServerStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlCreateTableSpaceStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlCreateUserStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlDeleteStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlEventSchedule;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlExecuteStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlExplainStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlFlushStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlHelpStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlHintStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlKillStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlLoadDataInFileStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlLoadXmlStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlLockTableStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlOptimizeStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlPartitionByKey;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlPrepareStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlRenameTableStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlResetStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlSetTransactionStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlShowAuthorsStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlShowBinLogEventsStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlShowBinaryLogsStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlShowCharacterSetStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlShowCollationStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlShowColumnsStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlShowContributorsStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlShowCreateDatabaseStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlShowCreateEventStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlShowCreateFunctionStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlShowCreateProcedureStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlShowCreateTableStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlShowCreateTriggerStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlShowCreateViewStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlShowDatabasePartitionStatusStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlShowDatabasesStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlShowEngineStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlShowEnginesStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlShowErrorsStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlShowEventsStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlShowFunctionCodeStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlShowFunctionStatusStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlShowGrantsStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlShowIndexesStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlShowKeysStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlShowMasterLogsStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlShowMasterStatusStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlShowOpenTablesStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlShowPluginsStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlShowPrivilegesStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlShowProcedureCodeStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlShowProcedureStatusStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlShowProcessListStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlShowProfileStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlShowProfilesStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlShowRelayLogEventsStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlShowSlaveHostsStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlShowSlaveStatusStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlShowStatusStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlShowTableStatusStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlShowTriggersStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlShowVariantsStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlShowWarningsStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlSubPartitionByKey;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlSubPartitionByList;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlTableIndex;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlUnlockTablesStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlUpdateStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlUpdateTableSource;
import com.np.database.sql.dialect.mysql.ast.statement.MysqlDeallocatePrepareStatement;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.dialect.mysql.ast.*;
import com.np.database.sql.dialect.mysql.ast.clause.*;
import com.np.database.sql.dialect.mysql.ast.expr.*;
import com.np.database.sql.dialect.mysql.ast.statement.*;
import com.np.database.sql.visitor.SQLASTVisitor;

public interface MySqlASTVisitor extends SQLASTVisitor {
    boolean visit(MySqlTableIndex x);

    void endVisit(MySqlTableIndex x);

    boolean visit(MySqlKey x);

    void endVisit(MySqlKey x);

    boolean visit(MySqlPrimaryKey x);

    void endVisit(MySqlPrimaryKey x);

    boolean visit(MySqlUnique x);

    void endVisit(MySqlUnique x);

    boolean visit(MysqlForeignKey x);

    void endVisit(MysqlForeignKey x);

    void endVisit(MySqlExtractExpr x);

    boolean visit(MySqlExtractExpr x);

    void endVisit(MySqlMatchAgainstExpr x);

    boolean visit(MySqlMatchAgainstExpr x);

    void endVisit(MySqlPrepareStatement x);

    boolean visit(MySqlPrepareStatement x);

    void endVisit(MySqlExecuteStatement x);

    boolean visit(MysqlDeallocatePrepareStatement x);

    void endVisit(MysqlDeallocatePrepareStatement x);

    boolean visit(MySqlExecuteStatement x);

    void endVisit(MySqlDeleteStatement x);

    boolean visit(MySqlDeleteStatement x);

    void endVisit(MySqlInsertStatement x);

    boolean visit(MySqlInsertStatement x);

    void endVisit(MySqlLoadDataInFileStatement x);

    boolean visit(MySqlLoadDataInFileStatement x);

    void endVisit(MySqlLoadXmlStatement x);

    boolean visit(MySqlLoadXmlStatement x);

    void endVisit(MySqlShowColumnsStatement x);

    boolean visit(MySqlShowColumnsStatement x);

    void endVisit(MySqlShowDatabasesStatement x);

    boolean visit(MySqlShowDatabasesStatement x);

    void endVisit(MySqlShowWarningsStatement x);

    boolean visit(MySqlShowWarningsStatement x);

    void endVisit(MySqlShowStatusStatement x);

    boolean visit(MySqlShowStatusStatement x);

    void endVisit(MySqlShowAuthorsStatement x);

    boolean visit(MySqlShowAuthorsStatement x);

    void endVisit(CobarShowStatus x);

    boolean visit(CobarShowStatus x);

    void endVisit(MySqlKillStatement x);

    boolean visit(MySqlKillStatement x);

    void endVisit(MySqlBinlogStatement x);

    boolean visit(MySqlBinlogStatement x);

    void endVisit(MySqlResetStatement x);

    boolean visit(MySqlResetStatement x);

    void endVisit(MySqlCreateUserStatement x);

    boolean visit(MySqlCreateUserStatement x);

    void endVisit(MySqlCreateUserStatement.UserSpecification x);

    boolean visit(MySqlCreateUserStatement.UserSpecification x);

    void endVisit(MySqlPartitionByKey x);

    boolean visit(MySqlPartitionByKey x);

    boolean visit(MySqlSelectQueryBlock x);

    void endVisit(MySqlSelectQueryBlock x);

    boolean visit(MySqlOutFileExpr x);

    void endVisit(MySqlOutFileExpr x);

    boolean visit(MySqlExplainStatement x);

    void endVisit(MySqlExplainStatement x);

    boolean visit(MySqlUpdateStatement x);

    void endVisit(MySqlUpdateStatement x);

    boolean visit(MySqlSetTransactionStatement x);

    void endVisit(MySqlSetTransactionStatement x);

    boolean visit(MySqlShowBinaryLogsStatement x);

    void endVisit(MySqlShowBinaryLogsStatement x);

    boolean visit(MySqlShowMasterLogsStatement x);

    void endVisit(MySqlShowMasterLogsStatement x);

    boolean visit(MySqlShowCharacterSetStatement x);

    void endVisit(MySqlShowCharacterSetStatement x);

    boolean visit(MySqlShowCollationStatement x);

    void endVisit(MySqlShowCollationStatement x);

    boolean visit(MySqlShowBinLogEventsStatement x);

    void endVisit(MySqlShowBinLogEventsStatement x);

    boolean visit(MySqlShowContributorsStatement x);

    void endVisit(MySqlShowContributorsStatement x);

    boolean visit(MySqlShowCreateDatabaseStatement x);

    void endVisit(MySqlShowCreateDatabaseStatement x);

    boolean visit(MySqlShowCreateEventStatement x);

    void endVisit(MySqlShowCreateEventStatement x);

    boolean visit(MySqlShowCreateFunctionStatement x);

    void endVisit(MySqlShowCreateFunctionStatement x);

    boolean visit(MySqlShowCreateProcedureStatement x);

    void endVisit(MySqlShowCreateProcedureStatement x);

    boolean visit(MySqlShowCreateTableStatement x);

    void endVisit(MySqlShowCreateTableStatement x);

    boolean visit(MySqlShowCreateTriggerStatement x);

    void endVisit(MySqlShowCreateTriggerStatement x);

    boolean visit(MySqlShowCreateViewStatement x);

    void endVisit(MySqlShowCreateViewStatement x);

    boolean visit(MySqlShowEngineStatement x);

    void endVisit(MySqlShowEngineStatement x);

    boolean visit(MySqlShowEnginesStatement x);

    void endVisit(MySqlShowEnginesStatement x);

    boolean visit(MySqlShowErrorsStatement x);

    void endVisit(MySqlShowErrorsStatement x);

    boolean visit(MySqlShowEventsStatement x);

    void endVisit(MySqlShowEventsStatement x);

    boolean visit(MySqlShowFunctionCodeStatement x);

    void endVisit(MySqlShowFunctionCodeStatement x);

    boolean visit(MySqlShowFunctionStatusStatement x);

    void endVisit(MySqlShowFunctionStatusStatement x);

    boolean visit(MySqlShowGrantsStatement x);

    void endVisit(MySqlShowGrantsStatement x);

    boolean visit(MySqlUserName x);

    void endVisit(MySqlUserName x);

    boolean visit(MySqlShowIndexesStatement x);

    void endVisit(MySqlShowIndexesStatement x);

    boolean visit(MySqlShowKeysStatement x);

    void endVisit(MySqlShowKeysStatement x);

    boolean visit(MySqlShowMasterStatusStatement x);

    void endVisit(MySqlShowMasterStatusStatement x);

    boolean visit(MySqlShowOpenTablesStatement x);

    void endVisit(MySqlShowOpenTablesStatement x);

    boolean visit(MySqlShowPluginsStatement x);

    void endVisit(MySqlShowPluginsStatement x);

    boolean visit(MySqlShowPrivilegesStatement x);

    void endVisit(MySqlShowPrivilegesStatement x);

    boolean visit(MySqlShowProcedureCodeStatement x);

    void endVisit(MySqlShowProcedureCodeStatement x);

    boolean visit(MySqlShowProcedureStatusStatement x);

    void endVisit(MySqlShowProcedureStatusStatement x);

    boolean visit(MySqlShowProcessListStatement x);

    void endVisit(MySqlShowProcessListStatement x);

    boolean visit(MySqlShowProfileStatement x);

    void endVisit(MySqlShowProfileStatement x);

    boolean visit(MySqlShowProfilesStatement x);

    void endVisit(MySqlShowProfilesStatement x);

    boolean visit(MySqlShowRelayLogEventsStatement x);

    void endVisit(MySqlShowRelayLogEventsStatement x);

    boolean visit(MySqlShowSlaveHostsStatement x);

    void endVisit(MySqlShowSlaveHostsStatement x);

    boolean visit(MySqlShowSlaveStatusStatement x);

    void endVisit(MySqlShowSlaveStatusStatement x);

    boolean visit(MySqlShowTableStatusStatement x);

    void endVisit(MySqlShowTableStatusStatement x);

    boolean visit(MySqlShowTriggersStatement x);

    void endVisit(MySqlShowTriggersStatement x);

    boolean visit(MySqlShowVariantsStatement x);

    void endVisit(MySqlShowVariantsStatement x);

    boolean visit(MySqlRenameTableStatement.Item x);

    void endVisit(MySqlRenameTableStatement.Item x);

    boolean visit(MySqlRenameTableStatement x);

    void endVisit(MySqlRenameTableStatement x);

    boolean visit(MySqlUseIndexHint x);

    void endVisit(MySqlUseIndexHint x);

    boolean visit(MySqlIgnoreIndexHint x);

    void endVisit(MySqlIgnoreIndexHint x);

    boolean visit(MySqlLockTableStatement x);

    void endVisit(MySqlLockTableStatement x);

    boolean visit(MySqlLockTableStatement.Item x);

    void endVisit(MySqlLockTableStatement.Item x);

    boolean visit(MySqlUnlockTablesStatement x);

    void endVisit(MySqlUnlockTablesStatement x);

    boolean visit(MySqlForceIndexHint x);

    void endVisit(MySqlForceIndexHint x);

    boolean visit(MySqlAlterTableChangeColumn x);

    void endVisit(MySqlAlterTableChangeColumn x);

    boolean visit(MySqlAlterTableOption x);

    void endVisit(MySqlAlterTableOption x);

    boolean visit(MySqlCreateTableStatement x);

    void endVisit(MySqlCreateTableStatement x);

    boolean visit(MySqlHelpStatement x);

    void endVisit(MySqlHelpStatement x);

    boolean visit(MySqlCharExpr x);

    void endVisit(MySqlCharExpr x);

    boolean visit(MySqlAlterTableModifyColumn x);

    void endVisit(MySqlAlterTableModifyColumn x);

    boolean visit(MySqlAlterTableDiscardTablespace x);

    void endVisit(MySqlAlterTableDiscardTablespace x);

    boolean visit(MySqlAlterTableImportTablespace x);

    void endVisit(MySqlAlterTableImportTablespace x);

    boolean visit(MySqlCreateTableStatement.TableSpaceOption x);

    void endVisit(MySqlCreateTableStatement.TableSpaceOption x);

    boolean visit(MySqlAnalyzeStatement x);

    void endVisit(MySqlAnalyzeStatement x);

    boolean visit(MySqlAlterUserStatement x);

    void endVisit(MySqlAlterUserStatement x);

    boolean visit(MySqlOptimizeStatement x);

    void endVisit(MySqlOptimizeStatement x);

    boolean visit(MySqlHintStatement x);

    void endVisit(MySqlHintStatement x);

    boolean visit(MySqlOrderingExpr x);

    void endVisit(MySqlOrderingExpr x);

    boolean visit(MySqlCaseStatement x);

    void endVisit(MySqlCaseStatement x);

    boolean visit(MySqlDeclareStatement x);

    void endVisit(MySqlDeclareStatement x);

    boolean visit(MySqlSelectIntoStatement x);

    void endVisit(MySqlSelectIntoStatement x);

    boolean visit(MySqlCaseStatement.MySqlWhenStatement x);

    void endVisit(MySqlCaseStatement.MySqlWhenStatement x);

    boolean visit(MySqlLeaveStatement x);

    void endVisit(MySqlLeaveStatement x);

    boolean visit(MySqlIterateStatement x);

    void endVisit(MySqlIterateStatement x);

    boolean visit(MySqlRepeatStatement x);

    void endVisit(MySqlRepeatStatement x);

    boolean visit(MySqlCursorDeclareStatement x);

    void endVisit(MySqlCursorDeclareStatement x);

    boolean visit(MySqlUpdateTableSource x);

    void endVisit(MySqlUpdateTableSource x);

    boolean visit(MySqlAlterTableAlterColumn x);

    void endVisit(MySqlAlterTableAlterColumn x);

    boolean visit(MySqlSubPartitionByKey x);

    void endVisit(MySqlSubPartitionByKey x);

    boolean visit(MySqlSubPartitionByList x);

    void endVisit(MySqlSubPartitionByList x);

    boolean visit(MySqlDeclareHandlerStatement x);

    void endVisit(MySqlDeclareHandlerStatement x);

    boolean visit(MySqlDeclareConditionStatement x);

    void endVisit(MySqlDeclareConditionStatement x);

    boolean visit(MySqlFlushStatement x);

    void endVisit(MySqlFlushStatement x);

    boolean visit(MySqlEventSchedule x);
    void endVisit(MySqlEventSchedule x);

    boolean visit(MySqlCreateEventStatement x);
    void endVisit(MySqlCreateEventStatement x);

    boolean visit(MySqlCreateAddLogFileGroupStatement x);
    void endVisit(MySqlCreateAddLogFileGroupStatement x);

    boolean visit(MySqlCreateServerStatement x);
    void endVisit(MySqlCreateServerStatement x);

    boolean visit(MySqlCreateTableSpaceStatement x);
    void endVisit(MySqlCreateTableSpaceStatement x);

    boolean visit(MySqlAlterEventStatement x);
    void endVisit(MySqlAlterEventStatement x);

    boolean visit(MySqlAlterLogFileGroupStatement x);
    void endVisit(MySqlAlterLogFileGroupStatement x);

    boolean visit(MySqlAlterServerStatement x);
    void endVisit(MySqlAlterServerStatement x);

    boolean visit(MySqlAlterTablespaceStatement x);
    void endVisit(MySqlAlterTablespaceStatement x);

    boolean visit(MySqlShowDatabasePartitionStatusStatement x);
    void endVisit(MySqlShowDatabasePartitionStatusStatement x);

    boolean visit(MySqlChecksumTableStatement x);
    void endVisit(MySqlChecksumTableStatement x);

} //
