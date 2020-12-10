//package com.car.carservice.mapper.cassandra;
//
//import com.zhcx.netcar.netcarservice.canssandra.Position;
//import com.zhcx.netcar.netcarservice.canssandra.PositionKey;
//import org.springframework.data.cassandra.repository.Query;
//import org.springframework.data.repository.CrudRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
///**
// * @author Lee
// * @email 570815140@qq.com
// * @date 2018/12/19 14:07
// **/
//@Repository
//public interface PositionRepository extends CrudRepository<Position, PositionKey> {
//
//    /**
//     * 历史定位
//     * @param partition
//     * @param entityType
//     * @param entityId
//     * @param keyword
//     * @param startTimestamp
//     * @param endTimestamp
//     * @return
//     */
//    @Query("select * from ts_kv_cf where partition= ?0 and entity_type= ?1 and entity_id= ?2 and key= ?3 and ts >= ?4 and ts<= ?5")
//    List<Position> selectListByCondition(Long partition, String entityType, String entityId, String keyword, Long startTimestamp, Long endTimestamp);
//
//    /**
//     * 实时定位查询 ts_kv_latest_cf
//     * @param entityType
//     * @param entityId
//     * @param keyword
//     * @return
//     */
//    @Query("select * from ts_kv_latest_cf where entity_type= ?0 and entity_id= ?1 and key= ?2")
//    Position selectLatestPositionByEntityId(String entityType, String entityId, String keyword);
//
//    @Query("DELETE " +
//            "FROM" +
//            "ts_kv_cf " +
//            "WHERE " +
//            "entity_type = ?0 " +
//            "AND entity_id = ?1 " +
//            "AND key = 'FULL' " +
//            "AND partition = ?2 " +
//            "AND ts = ?3 ")
//    void deleteCF(String entity_type, String entity_id, Long partition, Long ts);
//
//
//    @Query("INSERT INTO ts_kv_cf ( entity_type, entity_id, key, partition, ts, str_v )" +
//            "VALUES" +
//            "( ?0, ?1, ?2, ?3, ?4, ?5 );")
//    void insertCF(String entity_type, String entity_id, String key, Long partition, Long ts, String str_v);
//
//    @Query("INSERT INTO ts_kv_latest_cf ( entity_type, entity_id, key, str_v, ts ) " +
//            "VALUES ( ?0, ?1, ?2, ?3, ?4 );")
//    void insertLatestCF(String entity_type, String entity_id, String key, String str_v, Long ts);
//
//
//    @Query("DELETE " +
//            "FROM " +
//            "ts_kv_latest_cf " +
//            "WHERE " +
//            "entity_type = ?0 " +
//            "AND entity_id = ?1 " +
//            "AND KEY = 'FULL' "
////            "AND PARTITION = ?2 " +
////            "AND ts = ?3;"
//    )
//    void deleteLatestCF(String entity_type, String entity_id, Long partition, Long ts);
//}
