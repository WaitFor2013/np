package com.np.design.domain.db;

import com.np.database.orm.NpOrmContext;
import com.np.database.orm.SerialId;
import com.np.database.reflection.MetaObject;
import com.np.design.domain.misc.GridCellInit;
import com.np.design.domain.misc.GridField;
import com.np.design.domain.misc.PgType;
import com.np.design.exception.NpException;
import com.np.design.domain.misc.GridCellInit;
import com.np.design.domain.misc.GridField;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Table(name = "schema_view")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Slf4j
public class SchemaView {

    //ID
    @SerialId
    @Column(name = "id")
    @PgType(column = " SERIAL ")
    private Integer id;

    @Column(name = "view_module")
    @GridField(name = "模块", maxWidth = "160", dict = "module")
    @PgType(column = "VARCHAR(255) NOT NULL")
    private String viewModule;

    @Id
    @Column(name = "view_name")
    @GridField(name = "名称", maxWidth = "250")
    @PgType(column = "VARCHAR(255) NOT NULL")
    private String viewName;

    @Column(name = "view_comment")
    @GridField(name = "中文备注", maxWidth = "500")
    @PgType(column = "VARCHAR(255) NOT NULL")
    private String viewComment;

    //查询SQL
    @Column(name = "view_sql")
    @PgType(column = "TEXT NOT NULL")
    private String viewSql;

    @GridField(name = "发布", maxWidth = "70", editable = "false")
    @GridCellInit(boolString = "false")
    private Boolean isRelease;

    public static SchemaView newInstance(String[] header, Object[] data) {

        SchemaView schemaView = new SchemaView();
        MetaObject metaObject = NpOrmContext.newMetaObject(schemaView);
        Map<String, String> fieldPair = SchemaTable.getFieldPair(SchemaView.class);
        for (int i = 0; i < data.length; i++) {
            String field = fieldPair.get(header[i]);
            metaObject.setValue(field, data[i]);
        }

        return schemaView;
    }

    public static SchemaView copyFrom(SchemaView temp){
        SchemaView result = new SchemaView();
        result.viewModule = temp.viewModule;
        result.viewName = temp.viewName;
        result.viewComment = temp.viewComment;

        return result;
    }

    public void check() {

        if (null == viewModule) {
            throw new NpException("模块不允许为空");
        }

        if (null == viewName || viewName.trim().isEmpty()) {
            throw new NpException("视图名不允许为空");
        }

        if (null == viewComment || viewComment.trim().isEmpty()) {
            throw new NpException("中文备注不允许为空");
        }

    }

    public Object[] getRow() {
        MetaObject metaObject = NpOrmContext.newMetaObject(this);
        List<Object> result = new ArrayList<>();
        for (Field field : SchemaView.class.getDeclaredFields()) {
            if (null != field.getAnnotation(GridField.class)) {
                result.add(metaObject.getValue(field.getName()));
            }
        }
        return result.toArray();
    }
}
