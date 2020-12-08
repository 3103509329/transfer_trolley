package com.zhcx.netcar.pojo.cassandra;

import lombok.Data;

/**
 * @ClassName：ts_kv_cf
 * @Description:
 * @author：李亮
 * @date：2020/7/2117:53
 */
@Data
public class ts_kv_cf {

    private String entity_type;

    private String entity_id;

    private String key;

    private Long portition;

    private String str_v;

    private Long ts;

}
