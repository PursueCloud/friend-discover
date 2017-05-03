package com.yo.friendis.web.data.controller;


import com.alibaba.fastjson.JSON;
import com.yo.friendis.common.common.bean.OperaterResult;
import com.yo.friendis.common.common.controller.BaseController;
import com.yo.friendis.common.common.util.FileUtils;
import com.yo.friendis.core.hadoop.thread.CalDistance;
import com.yo.friendis.core.hadoop.thread.Deduplicate;
import com.yo.friendis.core.hadoop.util.HadoopUtils;
import com.yo.friendis.core.util.CommonUtils;
import com.yo.friendis.web.data.util.DataCalcUtils;
import com.yo.friendis.web.friend.model.CustomUser;
import com.yo.friendis.web.friend.service.CustomUserGroupService;
import com.yo.friendis.web.friend.service.CustomUserService;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yo on 2017/3/27.
 */
@Controller
@RequestMapping("dataSolve")
public class DataSolveController extends BaseController {
    @Autowired
    private CustomUserService custUserService;
    @Autowired
    private CustomUserGroupService custUGService;

    @RequestMapping("fileSolve")
    public ModelAndView fileSolve() {
        return new ModelAndView("dataSolve/fileSolve");
    }

    @RequestMapping("dataSolve")
    public ModelAndView dataSolve() {
        return new ModelAndView("dataSolve/dataSolve");
    }

    /**
     * 去重任务提交
     */
    @RequestMapping("deduplicate")
    @ResponseBody
    public Object deduplicate(String input, String output){
        Map<String ,Object> map = new HashMap<String,Object>();
        boolean success = false;
        String msg = "";
        try{
            long jobStartTime = System.currentTimeMillis();
            HadoopUtils.setJobStartTime(jobStartTime);
            HadoopUtils.JOBNUM = 1;
            new Thread(new Deduplicate(input,output)).start();
            map.put("success", true);
            map.put("monitor", true);
        } catch (Exception e) {
            map.put("success", false);
            map.put("monitor", false);
            map.put("msg", e.getMessage());
            logger.error(e.getMessage(), e);
        }
        success = (boolean)map.get("success");
        msg = map.get("msg")!=null ? map.get("msg").toString() : "";
        return new OperaterResult<>(success, msg, map);
    }

    /**
     * 数据库数据解析到云平台,为序列文件，是聚类运行的输入文件
     *
     */
    @RequestMapping("filterExp")//过滤导出（本地或hdfs
    @ResponseBody
    public Object filterExp(@RequestParam(required = false)String expLoc, @RequestParam(required = false)String expPath,
                            @RequestParam(required = false)String generateFileNum){
        boolean success = false;
        String msg = "";
        try{
            List<CustomUser> list = custUserService.selectAll();
            if( list==null || list.isEmpty() ){
                success = false;
            }
            if( "0".equals(expLoc) ) {//导出到本地
                DataCalcUtils.db2hdfs(list, expPath, Integer.parseInt(generateFileNum));//先过滤导出到hdfs
                String path = request.getSession().getServletContext().getRealPath("tmp/hadoop/filterExp");
                String downloadFileName = expPath.substring(expPath.lastIndexOf("/") + 1);
                Map<String,Object> map = HadoopUtils.downLoad(expPath, path);//再将文件下载到服务器
                File fileFilterExp = new File(path);
                File[] allFiles = fileFilterExp.listFiles();
                for(File file : allFiles) {
                    if( file.isFile() ) {
                        FileUtils.downloadFile(path, file.getName(), response);//最后将hdfs输出目录中所有文件下载到本地
                    }
                }
            } else if( "1".equals(expLoc) ) {//导出到hdfs
                DataCalcUtils.db2hdfs(list, expPath, Integer.parseInt(generateFileNum));
                success = true;
            } else {

            }
        }catch(Exception e){
            success = false;
            msg = e.getMessage();
            logger.error(e.getMessage(), e);
        }
        return new OperaterResult<>(success, msg);
    }
}
