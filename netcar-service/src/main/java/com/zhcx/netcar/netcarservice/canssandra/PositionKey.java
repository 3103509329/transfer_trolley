package com.zhcx.netcar.netcarservice.canssandra;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2018/12/19 13:56
 * PrimaryKeyClass必须实现Serializable并且应该提供equals()和的实现hashCode()。
 **/
@PrimaryKeyClass
public class PositionKey implements Serializable {

    private static final long serialVersionUID = 8009556574564938615L;
    /**
     * 分区键
     */
    @PrimaryKeyColumn(name = "entity_type", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String entityType;

    /**
     * 实体ID
     */
    @PrimaryKeyColumn(name = "entity_id", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    private String entityId;

    /**
     * 关键字 ： 字段名
     */
    @PrimaryKeyColumn(name = "key", ordinal = 2, type = PrimaryKeyType.PARTITIONED)
    private String keyword;

    /**
     * 分区字段 按月分区
     */
    @PrimaryKeyColumn(name = "partition", ordinal = 3, type = PrimaryKeyType.PARTITIONED)
    private Long partition;

    /**
     * 集群键
     * 记录时间
     */
//    @PrimaryKeyColumn(name = "ts", ordinal = 4, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    @PrimaryKeyColumn(name = "ts", ordinal = 4, type = PrimaryKeyType.PARTITIONED)
    private Long ts;

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Long getPartition() {
        return partition;
    }

    public void setPartition(Long partition) {
        this.partition = partition;
    }

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PositionKey that = (PositionKey) o;
        return Objects.equals(entityType, that.entityType) &&
                Objects.equals(entityId, that.entityId) &&
                Objects.equals(keyword, that.keyword) &&
                Objects.equals(partition, that.partition) &&
                Objects.equals(ts, that.ts);
    }

    @Override
    public int hashCode() {

        return Objects.hash(entityType, entityId, keyword, partition, ts);
    }
}
