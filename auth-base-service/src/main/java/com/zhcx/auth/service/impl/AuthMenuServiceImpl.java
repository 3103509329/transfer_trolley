package com.zhcx.auth.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.zhcx.auth.facade.AuthMenuRelationService;
import com.zhcx.auth.facade.AuthMenuService;
import com.zhcx.auth.mapper.AuthMenuMapper;
import com.zhcx.auth.pojo.AuthMenu;
import com.zhcx.auth.pojo.AuthMenuRelation;
import com.zhcx.auth.utils.UUIDUtils;


/**
 * @author 唐定
 * @ClassName: AuthMenuServiceImpl
 * @Description:
 * @date 2018年11月23日 下午5:12:06
 */
@Service("authMenuService")
public class AuthMenuServiceImpl implements AuthMenuService {
    Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private AuthMenuMapper authMenuMapper;
    @Autowired
    private AuthMenuRelationService authMenuRelationService;
    public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource
    private UUIDUtils uuidUtils;

    @Override
    public int save(AuthMenu record) {
        record.setUuid(uuidUtils.getLongUUID("seq:car-hailing:auth:menu"));
        String ctime = sdf.format(new Date());
        record.setTimeCreated(ctime);
        return authMenuMapper.save(record);
    }

    @Override
    public int modifyByUuid(AuthMenu record) {
        String ctime = sdf.format(new Date());
        record.setTimeModified(ctime);
        return authMenuMapper.update(record);
    }

    @Override
    public List<AuthMenu> queryList(AuthMenu AuthMenuParam) {
        List<AuthMenu> AuthMenuResps = authMenuMapper.selectList(AuthMenuParam);
        List resultList = formatTree(AuthMenuResps);
        return resultList;
    }


    /**
     * 遍历所有一级节点,并找出所有一级节点下的所有子节点
     *
     * @param permissions
     * @return
     */
    public List<AuthMenu> formatTree(List<AuthMenu> permissions) {
        List<AuthMenu> menuList = new ArrayList<AuthMenu>();
        for (AuthMenu ps : permissions) {
            if (ps.getParentId().equals("-2")) {
				AuthMenu grid = getXTreeGrid(ps, permissions);
                menuList.add(grid);
            }
        }
        return menuList;
    }

    public static AuthMenu getXTreeGrid(AuthMenu ps, List<AuthMenu> permissions) {
    	List<AuthMenu> data = getChild(ps, permissions);
        ps.setMenuList(data);
        return ps;
    }

    /**
     *   递归查找子菜单
     *      
     */
    public static List<AuthMenu> getChild(AuthMenu id, List<AuthMenu> rootMenu) {
        List<AuthMenu> childList = new ArrayList<>();
        for (AuthMenu root : rootMenu) {
            // 遍历所有节点，将父菜单编码与传过来的编码进行比较、若相同则继续查看该节点下是否还有子节点
            if (!StringUtil.isEmpty(root.getParentId())) {
                if (root.getParentId().equals(id.getUuid().toString())) {
                    AuthMenu grid = getXTreeGrid(root, rootMenu);
                    childList.add(grid);
                }
            }
        }
        return childList;
    }
/*	@Override
	public List<AuthMenu> queryListAndOperation(AuthMenu AuthMenuParam) {
		List<AuthMenu> AuthMenuResps = authMenuMapper.selectListAndOperation(AuthMenuParam);
		return AuthMenuResps;
	}*/

    @Override
    public AuthMenu queryFirst(AuthMenu AuthMenuParam) {
        List<AuthMenu> AuthMenuResps = this.queryList(AuthMenuParam);
        AuthMenu AuthMenuResp = (null != AuthMenuResps && !AuthMenuResps.isEmpty()) ? AuthMenuResps.get(0) : new AuthMenu();
        return AuthMenuResp;
    }

    @Override
    public AuthMenu queryByUuid(Long uuid) {
        AuthMenu AuthMenuParam = new AuthMenu();
        AuthMenuParam.setUuid(uuid);
        return this.queryFirst(AuthMenuParam);
    }

/*	@Override
	public int queryCountByParam(AuthMenu AuthMenuParam) {
		return authMenuMapper.queryCountByParam(AuthMenuParam);

	}*/

    @Override
    public PageInfo<AuthMenu> queryPageByParam(AuthMenu AuthMenuParam) {
        PageHelper.startPage(Integer.parseInt(AuthMenuParam.getPageNo()), Integer.parseInt(AuthMenuParam.getPageSize()));
        List<AuthMenu> AuthMenuResps = authMenuMapper.selectPageByParam(AuthMenuParam);
        PageInfo<AuthMenu> authMenuPageInfo = new PageInfo<>(AuthMenuResps);
        return authMenuPageInfo;
    }

    @Override
    public List<AuthMenu> queryMenusByRegisterId(Long[] registerIds) {
        AuthMenuRelation authMenuRelation = new AuthMenuRelation();
        authMenuRelation.setRegisterIds(registerIds);
        List<AuthMenuRelation> mrs = authMenuRelationService.queryList(authMenuRelation);
        String menuStrs = "";
        for (AuthMenuRelation amr : mrs) {
            menuStrs = menuStrs + amr.getMenuIds() + ",";
        }
        //剥离出功能点ID，得到菜单ID
        menuStrs = getMenuIds(menuStrs);
        if ("".equals(menuStrs)) {
            return null;
        }
        //去重
        menuStrs = removeSameString(menuStrs);
        //转换成Long[]
        Long[] menus = replaceArrys(menuStrs);
        AuthMenu param = new AuthMenu();
        param.setQueryMenuIds(menus);
        List<AuthMenu> authMenus = authMenuMapper.selectList(param);
        System.out.print(JSON.toJSON(authMenus));
        return authMenus;
    }

    public static String removeSameString(String str) {
        Set<String> mlinkedset = new LinkedHashSet<String>();
        String[] strarray = str.split(",");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < strarray.length; i++) {
            if (!mlinkedset.contains(strarray[i])) {
                mlinkedset.add(strarray[i]);
                sb.append(strarray[i] + ",");
            }
        }
        return sb.toString().substring(0, sb.toString().length() - 1);
    }

    public String getMenuIds(String str) {
        String[] strs = str.split(",");
        List<String> ls = Arrays.asList(strs);
        StringBuffer buff = new StringBuffer();
        for (String uuid : ls) {
            if (StringUtils.isNotBlank(uuid)) {
                if (uuid.indexOf("#") < 0) {//得到菜单
                    buff.append(uuid + ",");
                }
            }

        }
        return buff.length() == 0 ? buff.toString() : buff.toString().substring(0, buff.toString().length() - 1);
    }

    public Long[] replaceArrys(String str) {
        String[] strs = str.split(",");
        Long[] menus = new Long[strs.length];
        for (int i = 0; i < strs.length; i++) {
            menus[i] = Long.valueOf(strs[i]);
        }
        return menus;
    }

    public static void main(String[] args) {
        String a = removeSameString("158,159,160,161,162,163,1440000,1380000,1390000,1400000,1410000,1420000,1450000,1460000,1470000,163,1440000,1450000,1460000,1470000,1500000,1510000,1530000,1540000,1550000");
//        System.out.println(a);
    }

    @Override
    public int delete(AuthMenu record) {
        return authMenuMapper.delete(record);
    }

}
