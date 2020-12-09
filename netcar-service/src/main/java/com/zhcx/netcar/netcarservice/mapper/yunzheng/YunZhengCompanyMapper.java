package com.zhcx.netcar.netcarservice.mapper.yunzheng;

import com.zhcx.netcar.pojo.yuzheng.YunZhengCompany;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface YunZhengCompanyMapper {
    int deleteByPrimaryKey(Long uuid);

    int insert(YunZhengCompany record);

    int insertSelective(YunZhengCompany record);

    YunZhengCompany selectByPrimaryKey(Long uuid);

    int updateByPrimaryKeySelective(YunZhengCompany record);

    int updateByPrimaryKey(YunZhengCompany record);

    int updateByBusiregnumber(YunZhengCompany yzCompany);

    List<YunZhengCompany> selectByCompanyName(@Param("keyword") String keyword, @Param("uuid") Long uuid);

    @Select("select count(1) from yunzheng_base_info_company where busiRegNumber = #{busiregnumber}")
    int selectByBusiregnumber(String busiregnumber);

    @Select("select uuid , clitname from yunzheng_base_info_company where uuid = #{corpId}")
    YunZhengCompany selectByCorpId(@Param("corpId") Long corpId);

    Long selectCountByCompanyId(@Param("companyId") String companyId);

    @Select(" select busiRegNumber from yunzheng_base_info_company ")
    List<String> getCompanyBusiRegNumberList();

    @Select("select t1.company_id as companyId, t1.company_name as clitname, t1.identifier as busiRegNumber from t_base_info_company as t1 where t1.corp_type = 4 and t1.company_id != ''")
    List<YunZhengCompany> selectAll();
}