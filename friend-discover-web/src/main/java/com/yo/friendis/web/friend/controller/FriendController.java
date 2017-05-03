package com.yo.friendis.web.friend.controller;


import com.github.pagehelper.PageInfo;
import com.yo.friendis.common.common.controller.BaseController;
import com.yo.friendis.common.easyui.bean.DataGrid;
import com.yo.friendis.common.easyui.bean.Page;
import com.yo.friendis.core.hadoop.service.HadoopCfgConstService;
import com.yo.friendis.web.friend.model.CustomUser;
import com.yo.friendis.web.friend.service.CustomUserGroupService;
import com.yo.friendis.web.friend.service.CustomUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yo on 2017/3/27.
 */
@Controller
@RequestMapping("friend")
public class FriendController extends BaseController{
    @Autowired
    private CustomUserService custUserService;
    @Autowired
    private HadoopCfgConstService haCfgConstService;
    @Autowired
    private CustomUserGroupService custUserGroupService;

    /**
     * 根据用户Id获取推荐用户
     */
    @RequestMapping("getRecUserData")//分类数据入库
    @ResponseBody
    public Object getRecUserData(Page page, @RequestParam(required=false) String orginalUserId,
                                 @RequestParam(required = false, value="searchConditionFields[]") List<String> searchConditionFields,//搜索条件（可含多个）
                                 @RequestParam(required = false, value="searchConditionConts[]") List<String> searchConditionConts){
        List<CustomUser> list = new ArrayList<CustomUser>();
        if( StringUtils.isNotBlank(orginalUserId) ) {
            list = custUserService.selectRecUserDataByUserId(page, orginalUserId, searchConditionFields, searchConditionConts);
        }
        PageInfo pageInfo = new PageInfo(list);
        return new DataGrid(list, pageInfo.getTotal());

    }
}
