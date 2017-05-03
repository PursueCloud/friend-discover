package com.yo.friendis.web.data.controller;


import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.yo.friendis.common.common.bean.OperaterResult;
import com.yo.friendis.common.common.controller.BaseController;
import com.yo.friendis.common.common.util.DateUtils;
import com.yo.friendis.common.common.util.FileUtils;
import com.yo.friendis.common.easyui.bean.DataGrid;
import com.yo.friendis.common.easyui.bean.Page;
import com.yo.friendis.core.hadoop.model.HadoopCfgConst;
import com.yo.friendis.core.hadoop.service.HadoopCfgConstService;
import com.yo.friendis.core.hadoop.util.HadoopUtils;
import com.yo.friendis.core.util.CommonUtils;
import com.yo.friendis.web.friend.model.CustomUser;
import com.yo.friendis.web.friend.service.CustomUserGroupService;
import com.yo.friendis.web.friend.service.CustomUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * Created by Yo on 2017/3/27.
 */
@Controller
@RequestMapping("dataKeep")
public class DataKeepController extends BaseController {
    @Autowired
    private CustomUserService custUserService;
    @Autowired
    private CustomUserGroupService custUGService;
    @Autowired
    private HadoopCfgConstService haCfgConstService;

    @RequestMapping("initTable")
    public ModelAndView initTablePage() {
        return new ModelAndView("dataKeep/initTable");
    }

    @RequestMapping("keepData")
    public ModelAndView keepDataPage() {
        return new ModelAndView("dataKeep/keepData");
    }


    /**
     * 根据表名查询该表是否已存在
     */
    @RequestMapping("existsTable")
    @ResponseBody
    public Object existsTable(@RequestParam(required = false) String tableCode){
        boolean success = false;
        String msg = "";
        try {
            if( "0".equals(tableCode) ) {
                success = custUserService.selectExistsCurrTable();
            } else if( "1".equals(tableCode) ) {
                success = haCfgConstService.selectExistsCurrTable();
            }
        } catch(Exception e) {
            logger.error(e.getMessage(), e);
            msg = "系统出错！";
        }
        return new OperaterResult<>(success, msg);
    }

    /**
     * 根据表名创建表
     */
    @RequestMapping("createTable")
    @ResponseBody
    public Object createTable(@RequestParam(required = false) String tableCode){
        boolean success = false;
        String msg = "";
        String tableName = "";
        try {
            if( "0".equals(tableCode) ) {
                custUserService.createTable();
                custUGService.createTable();
                success = true;
                tableName = "“用户属性表”";
            } else if( "1".equals(tableCode) ) {
                haCfgConstService.createTable();
                haCfgConstService.insertInitialData();
                success = true;
                tableName = "“Hadoop集群配置表”";
            } else {
                msg = "表不存在";
            }
        } catch(Exception e) {
            logger.error(e.getMessage(), e);
            msg = "很抱歉，系统出错，初始化表" + tableName + "失败";
        }
        return new OperaterResult<>(success, msg);
    }

    /**
     * 根据tableCode获取表对应的搜索项数据
     */
    @RequestMapping("getTableFieldsData")
    @ResponseBody
    public Object getTableFieldsData(@RequestParam(required = false) String tableCode){
        boolean success = false;
        String msg = "";
        List<Map<String, String>> searchFields = null;
        if( "0".equals(tableCode) ) {
            searchFields = CustomUser.getSearchFields();
            success = true;
        } else if( "1".equals(tableCode) ) {
            searchFields = HadoopCfgConst.getSearchFields();
            success = true;
        } else {
            searchFields = new ArrayList<Map<String, String>>();
            msg = "表不存在";
        }
        return new OperaterResult<>(success, msg,  searchFields);
    }
    /**
     * 根据条件查询数据
     */
    @RequestMapping("getDataByConditions")
    @ResponseBody
    public Object getDataByConditions(Page page, @RequestParam(required = false) String tableCode,
                                      @RequestParam(required = false) String beginCreateDateStr, @RequestParam(required = false) String endCreateDateStr,
                                      @RequestParam(required = false) String beginModifyDateStr, @RequestParam(required = false) String endModifyDateStr,
                                      @RequestParam(required = false) String beginLastAccessDateStr, @RequestParam(required = false) String endLastAccessDateStr,
                                      @RequestParam(required = false, value="searchConditionFields[]") List<String> searchConditionFields,//搜索条件（可含多个）
                                      @RequestParam(required = false, value="searchConditionConts[]") List<String> searchConditionConts,@RequestParam(required = false) Integer sex,
                                      @RequestParam(required = false) Integer minAge, @RequestParam(required = false) Integer maxAge, @RequestParam(required = false) Integer minUpVotes,
                                      @RequestParam(required = false) Integer maxUpVotes, @RequestParam(required = false) Integer minDownVotes, @RequestParam(required = false) Integer maxDownVotes,
                                      @RequestParam(required = false) Integer minReputation, @RequestParam(required = false) Integer maxReputation,
                                      @RequestParam(required = false) Integer minViews, @RequestParam(required = false) Integer maxViews){//搜索内容（对应搜索条件，可含多个）
        DataGrid datagrid = new DataGrid();
        try {
            List list = null;
            if( "0".equals(tableCode) ) {
                list = custUserService.getDataListByConditions(page, true, searchConditionFields, searchConditionConts, beginCreateDateStr, endCreateDateStr,
                        beginModifyDateStr, endModifyDateStr, beginLastAccessDateStr, endLastAccessDateStr, sex, minAge, maxAge, minUpVotes, maxUpVotes, minDownVotes, maxDownVotes,
                        minReputation, maxReputation, minViews, maxViews);
            } else if( "1".equals(tableCode) ) {
                list = haCfgConstService.getDataListByConditions(page, true, searchConditionFields, searchConditionConts, beginCreateDateStr, endCreateDateStr,
                        beginModifyDateStr, endModifyDateStr);
            } else {
            }
            if( list != null ) {
                PageInfo pageInfo = new PageInfo(list);
                datagrid = new DataGrid(list, pageInfo.getTotal());
            }
        } catch(Exception e) {
            logger.error(e.getMessage(), e);
        }
        return datagrid;
    }

    /**
     * 新增数据
     */
    @RequestMapping("add")
    @ResponseBody
    public Object add(@RequestParam(required = false) String tableCode, @RequestParam(required = false)MultipartFile iconFile,
                      CustomUser custUser, HadoopCfgConst haCfgConst){
        boolean success = false;
        String msg = "";
        try {
            if( "0".equals(tableCode) ) {//添加普通用户
                if( StringUtils.isBlank(custUser.getDisplayName()) || StringUtils.isBlank(custUser.getEmailHash()) ) {//非空判断
                    if( StringUtils.isBlank(custUser.getDisplayName()) ) {
                        return new OperaterResult<>(false, "昵称不能为空！");
                    } else {
                        return new OperaterResult<>(false, "邮箱hash不能为空！");
                    }
                }
                //emailHash去重处理
                List<CustomUser> existEmailHashEntryList = custUserService.selectAllByEqField(CustomUser.FIELD_EMAILHASH, custUser.getEmailHash());
                if( existEmailHashEntryList!=null && !existEmailHashEntryList.isEmpty() ) {//emailHash重复
                    return new OperaterResult<>(false, "该邮箱hash已被使用，请重新输入！");
                } else {
                    //displayName去重处理
                    List<CustomUser> existDisplayNameEntryList = custUserService.selectAllByEqField(CustomUser.FIELD_DISPLAYNAME, custUser.getDisplayName());
                    if( existDisplayNameEntryList!=null && !existDisplayNameEntryList.isEmpty() ) {//displayName重复
                        return new OperaterResult<>(false, "该昵称已被使用，请重新输入！");
                    } else {
                        if( iconFile!=null && !iconFile.isEmpty() ) {
                            String path = request.getSession().getServletContext().getRealPath("/upload/custom-user/portraits");
                            String suffix = iconFile.getOriginalFilename().substring(iconFile.getOriginalFilename().lastIndexOf("."));//文件后缀
                            String iconFileName = UUID.randomUUID() + suffix;//使用UUID生成唯一标识符
                            FileUtils.uploadToLoc(iconFile, path, iconFileName);
                            custUser.setIcon("upload/custom-user/portraits/" + iconFileName);
                        }
                        custUser.setUserId("0");
                        custUser.setCreateDate(DateUtils.getCurrentTimestamp());
                        success = custUserService.addSelective(custUser) > 0;
                    }
                }
            } else if( "1".equals(tableCode) ) {//添加hadoop配置
                if( StringUtils.isBlank(haCfgConst.getConstKey()) || StringUtils.isBlank(haCfgConst.getConstValue()) ) {//非空判断
                    if( StringUtils.isBlank(haCfgConst.getConstKey()) ) {
                        return new OperaterResult<>(false, "配置项名不能为空！");
                    } else {
                        return new OperaterResult<>(false, "配置项值不能为空！");
                    }
                }
                //constKey去重处理
                List<HadoopCfgConst> existCustomKeyEntryList = haCfgConstService.selectAllByEqField(HadoopCfgConst.FIELD_CONSTKEY, haCfgConst.getConstKey());
                if( existCustomKeyEntryList!=null && !existCustomKeyEntryList.isEmpty() ) {//emailHash重复
                    return new OperaterResult<>(false, "该配置项已存在，请重新输入！");
                } else {
                    haCfgConst.setConstId("0");
                    haCfgConst.setCreateDate(DateUtils.getCurrentTimestamp());
                    success = haCfgConstService.addSelective(haCfgConst) > 0;
                }
            } else {

            }
        } catch(Exception e) {
            logger.error(e.getMessage(), e);
            msg = "很抱歉，系统出错，添加失败！";
        }
        return new OperaterResult<>(success, msg);
    }

    /**
     * 修改数据
     */
    @RequestMapping("update")
    @ResponseBody
    public Object update(@RequestParam(required = false) String tableCode, @RequestParam(required = false)MultipartFile iconFile,
                         CustomUser custUser, HadoopCfgConst haCfgConst){
        boolean success = false;
        String msg = "";
        try {
            if( "0".equals(tableCode) ) {//修改普通用户
                if( StringUtils.isBlank(custUser.getDisplayName()) || StringUtils.isBlank(custUser.getEmailHash())
                        || StringUtils.isBlank(custUser.getUserId())) {//非空判断
                    if( StringUtils.isBlank(custUser.getDisplayName()) ) {
                        return new OperaterResult<>(false, "昵称不能为空！");
                    } else if( StringUtils.isBlank(custUser.getEmailHash()) ){
                        return new OperaterResult<>(false, "邮箱hash不能为空！");
                    } else {
                        return new OperaterResult<>(false, "用户Id不能为空！");
                    }
                }
                //emailHash去重处理
                List<CustomUser> existEmailHashEntryList = custUserService.selectAllByEqField(CustomUser.FIELD_EMAILHASH, custUser.getEmailHash());
                if( existEmailHashEntryList!=null && !existEmailHashEntryList.isEmpty()
                        && !existEmailHashEntryList.get(0).getUserId().equals(custUser.getUserId()) ) {//emailHash重复
                    return new OperaterResult<>(false, "该邮箱hash已被使用，请重新输入！");
                } else {
                    //displayName去重处理
                    List<CustomUser> existDisplayNameEntryList = custUserService.selectAllByEqField(CustomUser.FIELD_DISPLAYNAME, custUser.getDisplayName());
                    if( existDisplayNameEntryList!=null && !existDisplayNameEntryList.isEmpty()
                            && !custUser.getUserId().equals(existDisplayNameEntryList.get(0).getUserId()) ) {//displayName重复
                        return new OperaterResult<>(false, "该昵称已被使用，请重新输入！");
                    } else {
                        if( iconFile!=null && !iconFile.isEmpty() ) {
                            String path = request.getSession().getServletContext().getRealPath("/upload/custom-user/portraits");
                            String suffix = iconFile.getOriginalFilename().substring(iconFile.getOriginalFilename().lastIndexOf("."));//文件后缀
                            String iconFileName = UUID.randomUUID() + suffix;//使用UUID生成唯一标识符
                            FileUtils.uploadToLoc(iconFile, path, iconFileName);
                            custUser.setIcon("upload/custom-user/portraits/" + iconFileName);
                        }
                        custUser.setModifyDate(DateUtils.getCurrentTimestamp());
                        success = custUserService.updateByPrimaryKeySelective(custUser) > 0;
                    }
                }
            } else if( "1".equals(tableCode) ) {//修改hadoop配置
                if( StringUtils.isBlank(haCfgConst.getConstKey()) || StringUtils.isBlank(haCfgConst.getConstValue())
                        || StringUtils.isBlank(haCfgConst.getConstId())) {//非空判断
                    if( StringUtils.isBlank(haCfgConst.getConstKey()) ) {
                        return new OperaterResult<>(false, "配置项名不能为空！");
                    } else if( StringUtils.isBlank(haCfgConst.getConstValue()) ){
                        return new OperaterResult<>(false, "配置项值不能为空！");
                    } else {
                        return new OperaterResult<>(false, "配置项Id不能为空！");
                    }
                }
                //constKey去重处理
                List<HadoopCfgConst> existCustomKeyEntryList = haCfgConstService.selectAllByEqField(HadoopCfgConst.FIELD_CONSTKEY, haCfgConst.getConstKey());
                if( existCustomKeyEntryList!=null && !existCustomKeyEntryList.isEmpty()
                        && !haCfgConst.getConstId().equals(existCustomKeyEntryList.get(0).getConstId()) ) {//emailHash重复
                    return new OperaterResult<>(false, "该配置项已存在，请重新输入！");
                } else {
                    haCfgConst.setModifyDate(DateUtils.getCurrentTimestamp());
                    success = haCfgConstService.updateByPrimaryKeySelective(haCfgConst) > 0;
                }
            } else {

            }
        } catch(Exception e) {
            logger.error(e.getMessage(), e);
            msg = "很抱歉，系统出错，修改失败！";
        }
        return new OperaterResult<>(success, msg);
    }

    /**
     * 删除(可删除多个
     */
    @RequestMapping("delete")
    @ResponseBody
    public Object delete(@RequestParam(required = false) String tableCode, @RequestParam(required = false, value = "custUserIds[]") List<String> custUserIds,
                         @RequestParam(required = false, value = "haConstIds[]") List<String> haConstIds){
        boolean success = false;
        String msg = "";
        try {
            if( "0".equals(tableCode) ) {//删除普通用户
                List<Integer> userIds = new ArrayList<Integer>();
                for(String userIdStr : custUserIds) {
                    if( StringUtils.isNotBlank(userIdStr) ) {
                        userIds.add(Integer.parseInt(userIdStr));
                    }
                }
                success = custUserService.deleteByIds(userIds) > 0;
            } else if( "1".equals(tableCode) ) {//删除hadoop配置
                List<Integer> constIds = new ArrayList<Integer>();
                for(String constIdStr : haConstIds) {
                    if( StringUtils.isNotBlank(constIdStr) ) {
                        constIds.add(Integer.parseInt(constIdStr));
                    }
                }
                success = haCfgConstService.deleteByIds(constIds) > 0;
            } else {

            }
        } catch(Exception e) {
            logger.error(e.getMessage(), e);
            msg = "很抱歉，系统出错，删除失败！";
        }
        return new OperaterResult<>(success, msg);
    }

    /**
     * 导入
     */
    @RequestMapping("import")
    @ResponseBody
    public Object importData(@RequestParam(required = false) String tableCode, @RequestParam(required = false) MultipartFile file){
        boolean success = false;
        String msg = "";

        try {
            if( "0".equals(tableCode) ) {//导入普通用户数据
                if( file!=null && !file.isEmpty() ) {
                    String path = request.getSession().getServletContext().getRealPath("/tmp");
                    String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                    String fileName = UUID.randomUUID() + suffix;
                    FileUtils.uploadToLoc(file, path, fileName);
                    Map<String,Object> map = custUserService.addData(path + "/" + fileName);
                    success = (boolean)map.get("success");
                    msg = map.get("msg")!=null ? map.get("msg").toString() : "";
                }
            } else if( "1".equals(tableCode) ) {//导入hadoop配置数据

            } else {

            }
        } catch(Exception e) {
            logger.error(e.getMessage(), e);
            msg = "很抱歉，系统出错，删除失败！";
        }
        return new OperaterResult<>(success, msg);
    }

    /**
     * 导出
     */
    @RequestMapping("export")
    public Object exportData(Page page, @RequestParam(required = false) String tableCode,
                             @RequestParam(required = false, value = "ids[]") List<String> ids,
                             @RequestParam(required = false, value = "cols[]") List<String> cols,
                             @RequestParam(required = false) String beginCreateDateStr, @RequestParam(required = false) String endCreateDateStr,
                             @RequestParam(required = false) String beginModifyDateStr, @RequestParam(required = false) String endModifyDateStr,
                             @RequestParam(required = false) String beginLastAccessDateStr, @RequestParam(required = false) String endLastAccessDateStr,
                             @RequestParam(required = false, value="searchConditionFields[]") List<String> searchConditionFields,//搜索条件（可含多个）
                             @RequestParam(required = false, value="searchConditionConts[]") List<String> searchConditionConts,@RequestParam(required = false) Integer sex,
                             @RequestParam(required = false) Integer minAge, @RequestParam(required = false) Integer maxAge, @RequestParam(required = false) Integer minUpVotes,
                             @RequestParam(required = false) Integer maxUpVotes, @RequestParam(required = false) Integer minDownVotes, @RequestParam(required = false) Integer maxDownVotes,
                             @RequestParam(required = false) Integer minReputation, @RequestParam(required = false) Integer maxReputation,
                             @RequestParam(required = false) Integer minViews, @RequestParam(required = false) Integer maxViews){//搜索内容（对应搜索条件，可含多个）

        try {
            List list = null;
            XSSFWorkbook wb = null;
            String expFileName = "";
            if( "0".equals(tableCode) ) {
                if( ids!=null && !ids.isEmpty() ) {//导出选中数据
                    List<Integer> intIds = new ArrayList<Integer>();
                    for(String idStr : ids) {
                        intIds.add(Integer.parseInt(idStr));
                    }
                    list = custUserService.selectByIds(intIds);
                } else {//根据条件导出
                    list = custUserService.getDataListByConditions(page, false, searchConditionFields, searchConditionConts, beginCreateDateStr, endCreateDateStr,
                            beginModifyDateStr, endModifyDateStr, beginLastAccessDateStr, endLastAccessDateStr, sex, minAge, maxAge, minUpVotes, maxUpVotes, minDownVotes, maxDownVotes,
                            minReputation, maxReputation, minViews, maxViews);
                }
                wb = custUserService.createDynamicXLSXExcelByData(list, cols);
                expFileName = "导出的用户数据.xlsx";
            } else if( "1".equals(tableCode) ) {
                if( ids!=null && !ids.isEmpty() ) {//导出选中数据
                    List<Integer> intIds = new ArrayList<Integer>();
                    for(String idStr : ids) {
                        intIds.add(Integer.parseInt(idStr));
                    }
                    list = haCfgConstService.selectByIds(intIds);
                } else {//根据条件导出
                    list = haCfgConstService.getDataListByConditions(page, false, searchConditionFields, searchConditionConts, beginCreateDateStr, endCreateDateStr,
                            beginModifyDateStr, endModifyDateStr);
                }
                wb = haCfgConstService.createDynamicXLSXExcelByData(list, cols);
                expFileName = "导出的Hadoop配置数据.xlsx";
            } else {

            }
            if( wb != null ) {
                FileUtils.downLoadExcel(wb, expFileName, response);
        }
        } catch(Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
//        return new OperaterResult<>(true);
    }

    /**
     * 解析入库
     */
//    @RequestMapping("resolve2db")
//    @ResponseBody
//    public Object resolve2db(String input){
//        Map<String,Object> map = custUserService.addData(CommonUtils.getRootPathBasedPath(input));
//        boolean success = (boolean)map.get("flag");
//        String msg = map.get("msg").toString();
//        CommonUtils.write2PrintWriter(JSON.toJSONString(map));
//        return new OperaterResult<>(success, msg);
//    }
}
