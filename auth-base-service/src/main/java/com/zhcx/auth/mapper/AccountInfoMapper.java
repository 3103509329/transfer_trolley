package com.zhcx.auth.mapper;

import com.zhcx.auth.pojo.AccountInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

@Mapper
public interface AccountInfoMapper {
	
	AccountInfo verifyAccount(@Param(value = "account") String account);

	/**
	  * @Title: countByExample
	  * @Description: 查询条数
	  * @param @param example
	  * @param @return
	  * @return int
	  * @throws
	  * @author tangding
	  * @date 2018年11月22日 上午11:08:48
	 */
    int countByExample(AccountInfo example);

    /**
      * @Title: deleteByPrimaryKey
      * @Description: 根绝主键删除
      * @param @param uuid
      * @param @return
      * @return int
      * @throws
      * @author tangding
      * @date 2018年11月22日 上午11:08:59
     */
    int deleteByPrimaryKey(Long uuid);

    /**
      * @Title: insert
      * @Description: 新增保存
      * @param @param record
      * @param @return
      * @return int
      * @throws
      * @author tangding
      * @date 2018年11月22日 上午11:09:07
     */
    int insert(AccountInfo record);

    /**
      * @Title: selectByExample
      * @Description: 查询
      * @param @param example
      * @param @return
      * @return List<AccountInfo>
      * @throws
      * @author tangding
      * @date 2018年11月22日 上午11:09:16
     */
    List<AccountInfo> selectByExample(AccountInfo example, RowBounds roow);
    
    /**
      * @Title: selectByPrimaryKey
      * @Description: 根据主键查询
      * @param @param uuid
      * @param @return
      * @return AccountInfo
      * @throws
      * @author tangding
      * @date 2018年11月22日 上午11:09:37
     */
    AccountInfo selectByPrimaryKey(Long uuid);
    
    /**
      * @Title: updateByPrimaryKey
      * @Description: 根据主键修改
      * @param @param record
      * @param @return
      * @return int
      * @throws
      * @author tangding
      * @date 2018年11月22日 上午11:09:45
     */
    int updateByPrimaryKey(AccountInfo record);
    
    List<AccountInfo> selectByAccountName(AccountInfo record);
    
    AccountInfo selectLogineByParam(AccountInfo record);
    
    /**
     * @Title: deleteByPrimaryKey
     * @Description: 根据userId删除账号信息（物理删除）
     * @param @param uuid
     * @param @return
     * @return int
     * @throws
     * @author tangding
     * @date 2018年11月22日 上午11:08:59
    */
   int deleteAccountByUserId(Long userId);
}