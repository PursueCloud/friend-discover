package com.yo.friendis.web.data.controller;


import com.alibaba.fastjson.JSON;
import com.yo.friendis.common.common.bean.OperaterResult;
import com.yo.friendis.common.common.controller.BaseController;
import com.yo.friendis.common.common.util.FileUtils;
import com.yo.friendis.core.hadoop.util.HadoopUtils;
import com.yo.friendis.core.util.CommonUtils;
import com.yo.friendis.web.data.util.DataCalcUtils;
import com.yo.friendis.web.friend.service.CustomUserGroupService;
import com.yo.friendis.web.friend.service.CustomUserService;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DataOutputBuffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.util.*;

/**
 * Created by Yo on 2017/3/27.
 */
@Controller
@RequestMapping("dataShow")
public class DataShowController extends BaseController {
    @Autowired
    private CustomUserService custUserService;
    @Autowired
    private CustomUserGroupService custUGService;

    @RequestMapping("showClusterCenterCensus")//包括距离计算和最佳dc计算
    public ModelAndView showClusterCenterCensus() {
        return new ModelAndView("dataShow/showClusterCenterCensus");
    }

    @RequestMapping("showUserDataCensus")
    public ModelAndView showUserDataCensus() {
        return new ModelAndView("dataShow/showUserDataCensus");
    }

    /**
     * 解析本地聚类中心数据，并获得数据库中分类数据占比情况
     * 返回前台显示
     */
    @RequestMapping("showClusterCenterFileCont")
    @ResponseBody
    public Object showClusterCenterFileCont(@RequestParam(required = false) String showType,
                                            @RequestParam(required = false) String dataLoc,
                                            @RequestParam(required = false) MultipartFile clusterDataFile,
                                            @RequestParam(required = false) String inputPath){
        boolean success = false;
        String msg = "";
        Map<String,Object> map = new HashMap<String,Object>();
        List<String> centerVec=null;
        List<String> percentVec=null;
        try{
            if( "0".equals(showType) ) {//查看内容
                // 整合数据
                StringBuilder clusterCenterFileCont = new StringBuilder();
                if( "0".equals(dataLoc) ) {
                    String input = null;
                    if( clusterDataFile!=null && !clusterDataFile.isEmpty() ) {//读取本地文件
                        String path = request.getSession().getServletContext().getRealPath("/tmp");
                        String suffix = clusterDataFile.getOriginalFilename().substring(clusterDataFile.getOriginalFilename().lastIndexOf("."));
                        String fileName = UUID.randomUUID() + suffix;
                        FileUtils.uploadToLoc(clusterDataFile, path, fileName);
                        input = path + "/" + fileName;
                        centerVec= CommonUtils.getLines(input, true);
                    } else {
                        input = DataCalcUtils.LOCALCENTERFILE;
                        centerVec= CommonUtils.getLines(input, false);
                    }
                } else if( "1".equals(dataLoc) ) {
                    centerVec = new ArrayList<String>();
                    FileSystem fs = FileSystem.get(HadoopUtils.getConf());//读取hdfs文件
                    String line = "";
                    BufferedReader reader = new BufferedReader(new InputStreamReader(fs.open(new Path(inputPath))));
                    while( (line=reader.readLine()) != null ) {
                        centerVec.add(line);
                    }
                } else {

                }
                if( centerVec!=null && !centerVec.isEmpty() ) {
                    percentVec=  custUGService.getPercent(centerVec.size());
                    clusterCenterFileCont.append("<br>");
                    for(int i=0;i<centerVec.size();i++){
                        clusterCenterFileCont.append("聚类中心："+centerVec.get(i)+"\t,占比："+percentVec.get(i)+"<br>");
                    }
                    map.put("clusterCenterFileCont", clusterCenterFileCont.toString());
                    success = true;
                }
            } else {//图表显示

            }
        }catch(Exception e){
            msg = "找不到该文件！";//"解析聚类中心出错！"
            logger.error(e.getMessage(), e);
        }
        return new OperaterResult<>(success, msg, map);
    }
    /**
     * 获取用户年龄占比情况
     */
    @RequestMapping("getUserAgeCensusData")
    @ResponseBody
    public Object getUserAgeCensusData() {
        boolean success = false;
        String msg = "";
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<String> ageScopeList = new ArrayList<String>();
            ageScopeList.add("无");
            ageScopeList.add("0~12岁");
            ageScopeList.add("13~18岁");
            ageScopeList.add("19~23岁");
            ageScopeList.add("24~40岁");
            ageScopeList.add("41~60岁");
            ageScopeList.add("61~80岁");
            ageScopeList.add(">80岁");
            List<Map<String, Object>> ageDataList = new ArrayList<Map<String, Object>>();
            int none = 0;//无
            int less12 = 0;//0~12
            int thirteen218 = 0;//13~18
            int nineteen223 = 0;//19~23
            int twentyFour240 = 0;//24~40
            int fortyOne260 = 0;//41~60
            int sixthyOne280 = 0;//61~80
            int bigger80 = 0;//>80

            List<Map<String, Object>> ageCensusInfoList = custUserService.selectUserAgeCnt();
            if (ageCensusInfoList != null && !ageCensusInfoList.isEmpty()) {
                for (Map<String, Object> ageCensusMap : ageCensusInfoList) {
                    String currAgeStr = ageCensusMap.get("age").toString();
                    int currAgeCnt = Integer.parseInt(ageCensusMap.get("cnt").toString());
                    if ("无".equals(currAgeStr)) {
                        none = currAgeCnt;
                    } else {
                        int currAge = Integer.parseInt(currAgeStr);
                        if (currAge <= 12) {
                            less12 += currAgeCnt;
                        } else if (currAge >= 13 && currAge <= 18) {
                            thirteen218 += currAgeCnt;
                        } else if (currAge >= 19 && currAge <= 23) {
                            nineteen223 += currAgeCnt;
                        } else if (currAge >= 24 && currAge <= 40) {
                            twentyFour240 += currAgeCnt;
                        } else if (currAge >= 41 && currAge <= 60) {
                            fortyOne260 += currAgeCnt;
                        } else if (currAge >= 61 && currAge <= 80) {
                            sixthyOne280 += currAgeCnt;
                        } else if (currAge > 80) {
                            bigger80 += currAgeCnt;
                        }
                    }
                }
            }
            int index = 0;
            for (String ageScope : ageScopeList) {
                Map<String, Object> ageCensusInfoMap = new HashMap<String, Object>();
                int ageCnt = 0;
                if (index == 0) {
                    ageCnt = none;
                } else if (index == 1) {
                    ageCnt = less12;
                } else if (index == 2) {
                    ageCnt = thirteen218;
                } else if (index == 3) {
                    ageCnt = nineteen223;
                } else if (index == 4) {
                    ageCnt = twentyFour240;
                } else if (index == 5) {
                    ageCnt = fortyOne260;
                } else if (index == 6) {
                    ageCnt = sixthyOne280;
                } else if (index == 7) {
                    ageCnt = bigger80;
                }
                ageCensusInfoMap.put("name", ageScope);
                ageCensusInfoMap.put("value", ageCnt);
                ageDataList.add(ageCensusInfoMap);
                index++;
            }
            map.put("name", ageScopeList);
            map.put("data", ageDataList);
            success = true;
        } catch (Exception e){
            logger.error(e.getMessage(), e);
        }
        return new OperaterResult<>(success, msg, map);
    }
}
