package com.np.design.domain.ddl;


import lombok.*;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@Table(name = "pg_catalog.pg_tables")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PgTable {

    @Column(name = "schemaname")
    private String schemaname;

    @Column(name = "tablename")
    private String tablename;

    @Column(name = "tableowner")
    private String tableowner;

    @Column(name = "tablespace")
    private String tablespace;

    @Column(name = "hasindexes")
    private Boolean hasindexes;

    @Column(name = "hasrules")
    private Boolean hasrules;

    @Column(name = "hastriggers")
    private Boolean hastriggers;

    @Column(name = "rowsecurity")
    private Boolean rowsecurity;

    public static Boolean contains(List<PgTable> list, String tablename) {
        boolean result = false;
        if (null != list) {
            for (PgTable tmp : list) {
                //log.info("tableName {}", tmp.getTablename());
                if (tmp.getTablename().equals(tablename))
                    result = true;
            }
        }
        return result;
    }
}
