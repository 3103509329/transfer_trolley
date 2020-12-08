package com.zhcx.netcar.netcarservice.mapper.base;


import com.zhcx.netcar.pojo.base.NetcarRatedPassengerComplaint;
import com.zhcx.netcar.params.PassengerComplainParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface NetcarRatedPassengerComplaintMapper {
    int deleteByPrimaryKey(Long uuid);

    int insert(NetcarRatedPassengerComplaint record);

    int insertSelective(NetcarRatedPassengerComplaint record);

    NetcarRatedPassengerComplaint selectByPrimaryKey(Long uuid);

    int updateByPrimaryKeySelective(NetcarRatedPassengerComplaint record);

    int updateByPrimaryKey(NetcarRatedPassengerComplaint record);

    List<NetcarRatedPassengerComplaint> selectListByKeyword(PassengerComplainParam param);

    List<NetcarRatedPassengerComplaint> queryPassengerComplaintvehicleNo(PassengerComplainParam param);
}