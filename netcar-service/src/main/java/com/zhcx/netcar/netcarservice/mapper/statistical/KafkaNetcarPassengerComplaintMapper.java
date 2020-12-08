package com.zhcx.netcar.netcarservice.mapper.statistical;

import com.zhcx.netcar.pojo.statistical.KafkaNetcarPassengerComplaint;

import java.util.List;

/**
* @ClassName: KafkaNetcarPassengerComplaintMapper
* @Description: 
* @author: Mybatis Generator
* @date 2020/09/28 17:20:53
*/
public interface KafkaNetcarPassengerComplaintMapper {
    int deleteByPrimaryKey(String orderid);

    int insert(KafkaNetcarPassengerComplaint record);

    int insertSelective(KafkaNetcarPassengerComplaint record);

    KafkaNetcarPassengerComplaint selectByPrimaryKey(String orderid);

    int updateByPrimaryKeySelective(KafkaNetcarPassengerComplaint record);

    int updateByPrimaryKey(KafkaNetcarPassengerComplaint record);

    List<KafkaNetcarPassengerComplaint> selectByDateOnComplaint(KafkaNetcarPassengerComplaint complaint);

    List<KafkaNetcarPassengerComplaint> getSUMorderDate(KafkaNetcarPassengerComplaint complaint);
}