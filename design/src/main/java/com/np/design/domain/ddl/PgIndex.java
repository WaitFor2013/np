package com.np.design.domain.ddl;


import lombok.*;

import javax.persistence.Column;
import javax.persistence.Table;

@Getter
@Setter
@Table(name = "pg_catalog.pg_indexes")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PgIndex {

    @Column(name = "schemaname")
    private String schemaname;

    @Column(name = "tablename")
    private String tablename;

    @Column(name = "indexname")
    private String indexname;

    @Column(name = "tablespace")
    private String tablespace;

    @Column(name = "indexdef")
    private String indexdef;

}
