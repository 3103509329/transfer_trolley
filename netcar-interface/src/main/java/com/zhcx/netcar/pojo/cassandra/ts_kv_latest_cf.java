package com.zhcx.netcar.pojo.cassandra;

import lombok.Data;

/**
 * @ClassName：ts_kv_latest_cf
 * @Description:
 * @author：李亮
 * @date：2020/7/2117:55
 */
@Data
public class ts_kv_latest_cf {

    private String entity_type;

    private String entity_id;

    private String key;

    private String str_v;

    private Long ts;
}
