package com.np.design.domain;

public class ReplaceTest {

    public static void main(String [] args){
        String whereX = "a.tenant_id = ?  AND a.gmt_create = ?  AND a.flow_engine = ?  AND a.process_def_id = ?";
        String whereY = "a.tenant_id = ?  and a.gmt_create = ?  and a.flow_engine = ?  and a.process_def_id = ?";
        String sql ="select count(a.sys_id) as num, to_char(a.gmt_create, 'YY-MM-DD') as gmt_create from wk_process a where a.tenant_id = ?  and a.gmt_create = ?  and a.flow_engine = ?  and a.process_def_id = ? group by to_char(a.gmt_create, 'YY-MM-DD')";

        System.out.println(sql.replace(whereY,""));
    }
}
