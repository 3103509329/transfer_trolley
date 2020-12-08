package com.zhcx.auth.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhcx.auth.facade.AuthMenuRelationService;
import com.zhcx.auth.mapper.AuthMenuRelationMapper;
import com.zhcx.auth.pojo.AuthMenuRelation;
import com.zhcx.auth.utils.UUIDUtils;


/**
  * @ClassName: AuthMenuRelationServiceImpl
  * @Description: 
  * @author tangding
  * @date 2018年11月25日 下午5:12:06
  */
@Service("authMenuRelationService")
public class AuthMenuRelationServiceImpl implements AuthMenuRelationService {
	Logger log = LoggerFactory.getLogger(this.getClass());

	@Resource
	private AuthMenuRelationMapper authMenuRelationMapper;
	
	public SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Resource
	private UUIDUtils uuidUtils;

	@Override
	public int save(AuthMenuRelation record){
		record.setUuid(uuidUtils.getLongUUID());
		String ctime = sdf.format(new Date());
		record.setTimeCreated(ctime);
		return authMenuRelationMapper.insert(record);
	}

	@Override
	public int modifyByUuid(AuthMenuRelation record){
		String ctime = sdf.format(new Date());
		record.setTimeModified(ctime);
		return authMenuRelationMapper.update(record);
	}

	@Override
	public List<AuthMenuRelation> queryList(AuthMenuRelation AuthMenuRelationParam) {
		List<AuthMenuRelation> AuthMenuRelationResps = authMenuRelationMapper.selectList(AuthMenuRelationParam);
		return AuthMenuRelationResps;
	}

	@Override
	public AuthMenuRelation queryFirst(AuthMenuRelation AuthMenuRelationParam) {
		List<AuthMenuRelation> AuthMenuRelationResps = this.queryList(AuthMenuRelationParam);
		AuthMenuRelation AuthMenuRelationResp = (null != AuthMenuRelationResps && !AuthMenuRelationResps.isEmpty()) ? AuthMenuRelationResps.get(0) : new AuthMenuRelation();
		return AuthMenuRelationResp;
	}

	@Override
	public AuthMenuRelation queryByUuid(Long uuid) {
		AuthMenuRelation AuthMenuRelationParam = new AuthMenuRelation();
		AuthMenuRelationParam.setUuid(uuid);
		return this.queryFirst(AuthMenuRelationParam);
	}

/*	@Override
	public int queryCountByParam(AuthMenuRelation AuthMenuRelationParam) {
		return authMenuRelationMapper.queryCountByParam(AuthMenuRelationParam);

	}*/

	@Override
	public PageInfo<AuthMenuRelation> queryPageByParam(AuthMenuRelation AuthMenuRelationParam) {
		PageHelper.startPage(Integer.parseInt(AuthMenuRelationParam.getPageNo()), Integer.parseInt(AuthMenuRelationParam.getPageSize()));
		List<AuthMenuRelation> AuthMenuRelationResps = authMenuRelationMapper.selectPageByParam(AuthMenuRelationParam);
		PageInfo<AuthMenuRelation> authMenuRelationPageInfo = new PageInfo<>(AuthMenuRelationResps);
		return authMenuRelationPageInfo;
	}
}
