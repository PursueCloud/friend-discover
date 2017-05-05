package com.yo.friendis.core.hadoop.controller;


import com.alibaba.fastjson.JSON;
import com.yo.friendis.common.common.bean.OperaterResult;
import com.yo.friendis.common.common.controller.BaseController;
import com.yo.friendis.common.common.util.DateUtils;
import com.yo.friendis.common.common.util.FileUtils;
import com.yo.friendis.common.common.util.PropertyMgr;
import com.yo.friendis.common.common.util.ShellUtil;
import com.yo.friendis.common.easyui.bean.DataGrid;
import com.yo.friendis.common.easyui.bean.Page;
import com.yo.friendis.core.hadoop.model.CurrentJobInfo;
import com.yo.friendis.core.hadoop.service.HadoopCfgConstService;
import com.yo.friendis.core.hadoop.util.HadoopUtils;
import com.yo.friendis.core.util.CommonUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Yo on 2017/3/27.
 */
@Controller
@RequestMapping("hadoop")
public class HadoopController extends BaseController{
    @Autowired
    private HadoopCfgConstService haCfgConstService;
    private static int index = 2;

    @RequestMapping("hdfsBrowser")
    public ModelAndView hdfsBrowser() {
        return new ModelAndView("hadoop/hdfsBrowser");
    }

    @RequestMapping("examples")
    public ModelAndView examples() {
        return new ModelAndView("hadoop/examples");
    }

    @RequestMapping("monitor")
    public ModelAndView monitor() {
        return new ModelAndView("hadoop/monitor");
    }

    @RequestMapping("hdfsShell")
    public ModelAndView hdfsShell() {
        return new ModelAndView("hadoop/hdfsShell");
    }

    /**
     * 获取所有监控数据（所有任务信息）
     */
    @RequestMapping("getAllMonitorData")
    @ResponseBody
    public Object getAllMonitorData(Page page, @RequestParam(required = false) String keyword) {
        List<CurrentJobInfo> currJobList =null;
        try{
            currJobList= HadoopUtils.getAllJobs();
            if( currJobList!=null && !currJobList.isEmpty() ) {
                if( StringUtils.isNotBlank(keyword) ) {
                    List<CurrentJobInfo> targetList = new ArrayList<CurrentJobInfo>();
                    for(CurrentJobInfo jobInfo : currJobList) {
                        if( StringUtils.isBlank(jobInfo.getJobName()) ) {
                            jobInfo.setJobName("");
                        }
                        if( jobInfo.getJobName().indexOf(keyword) != -1 ) {
                            targetList.add(jobInfo);
                        }
                    }
                    currJobList = null;
                    currJobList = targetList;
                    targetList = null;
                }
                if( page!=null && StringUtils.isNotBlank(page.getSort()) ) {
                    String sort = page.getSort();
                    String order = page.getOrder();
                    String getterMethodName = "get" + com.yo.friendis.common.common.util.StringUtils.firstCharToUpperCase(sort);
                    currJobList.sort((CurrentJobInfo job1, CurrentJobInfo job2) -> {
                                try {
                                    Object fieldValueObj1 = CurrentJobInfo.class.getMethod(getterMethodName, null).invoke(job1);
                                    if( fieldValueObj1 == null ) {
                                        fieldValueObj1 = "";
                                    }
                                    Object fieldValueObj2 = CurrentJobInfo.class.getMethod(getterMethodName, null).invoke(job2);
                                    if( fieldValueObj2 == null ) {
                                        fieldValueObj2 = "";
                                    }
                                    if( "asc".equals(order) ) {
                                        return fieldValueObj1.toString().compareTo(fieldValueObj2.toString());
                                    } else {
                                        return fieldValueObj2.toString().compareTo(fieldValueObj1.toString());
                                    }
                                } catch (Exception e) {
                                    logger.error(e.getMessage(), e);
                                    return -1;
                                }
                            }
                    );
                }
            }
        } catch(Exception e){
            logger.error(e.getMessage(), e);
        }

        return new DataGrid(currJobList, currJobList.size());
    }

    /**
     * 单个任务监控
     * @throws IOException
     */
    @RequestMapping("getOneMonitorData")
    @ResponseBody
    public Object getOneMonitorData() {
        Map<String ,Object> jsonMap = new HashMap<String,Object>();
        List<CurrentJobInfo> currJobList =null;
        try{
            currJobList= HadoopUtils.getJobs();
            jsonMap.put("jobNums", HadoopUtils.JOBNUM);
            // 任务完成的标识是获取的任务个数必须等于jobNum，同时最后一个job完成
            // true 所有任务完成
            // false 任务正在运行
            // error 某一个任务运行失败，则不再监控

            if(currJobList.size() >= HadoopUtils.JOBNUM){// 如果返回的list有JOBNUM个，那么才可能完成任务
                if("success".equals(HadoopUtils.hasFinished(currJobList.get(currJobList.size()-1)))){
                    jsonMap.put("finished", "true");
                    // 运行完成，初始化时间点
                    HadoopUtils.setJobStartTime(System.currentTimeMillis());
                }else if("running".equals(HadoopUtils.hasFinished(currJobList.get(currJobList.size()-1)))){
                    jsonMap.put("finished", "false");
                }else{// fail 或者kill则设置为error
                    jsonMap.put("finished", "error");
                    HadoopUtils.setJobStartTime(System.currentTimeMillis());
                }
            }else if(currJobList.size()>0){
                if("fail".equals(HadoopUtils.hasFinished(currJobList.get(currJobList.size()-1)))||
                        "kill".equals(HadoopUtils.hasFinished(currJobList.get(currJobList.size()-1)))){
                    jsonMap.put("finished", "error");
                    HadoopUtils.setJobStartTime(System.currentTimeMillis());
                }else{
                    jsonMap.put("finished", "false");
                }
            }

            if(currJobList.size()==0){
                jsonMap.put("finished", "false");
//    			return ;
            }else{
                if(jsonMap.get("finished").equals("error")){
                    CurrentJobInfo cj =currJobList.get(currJobList.size()-1);
                    cj.setRunState("Error!");
                    jsonMap.put("rows", cj);
                }else{
                    jsonMap.put("rows", currJobList.get(currJobList.size()-1));
                }
            }
            jsonMap.put("currJob", currJobList.size());
        }catch(Exception e){
            jsonMap.put("finished", "error");
            HadoopUtils.setJobStartTime(System.currentTimeMillis());
            logger.error(e.getMessage(), e);
            return new OperaterResult<>(false);
        }
        System.out.println(new java.util.Date()+":"+JSON.toJSONString(jsonMap));

        return new OperaterResult<>(true, "", jsonMap);
    }

    /**
     * 上传文件到hdfs
     */
    @RequestMapping("uploadFile2HDFS")
    @ResponseBody
    public Object uploadFile2HDFS(List<CommonsMultipartFile> uploadFile, boolean isUploadDir, String uploadHdfsPath){
        boolean success = false;
        String msg = "";

        try {
            if( uploadFile!=null && !uploadFile.isEmpty() ) {
                FileSystem fs = FileSystem.get(HadoopUtils.getConf());//连接到hdfs
                for(CommonsMultipartFile file : uploadFile) {
                    String fullPathName = file.getFileItem().getName();
                    String parentDirName = fullPathName.substring(0, fullPathName.lastIndexOf("/"));
                    String[] dirArr = parentDirName.split("/");
                    //判断当前文件所在父目录是否存在，否则新建目录
                    for(int i=0; i<dirArr.length; i++) {
                        String currDir = dirArr[i];
                        String parentDir = "";
                        if(i > 0) {
                            for(int j=0; j<i; j++) {//j<j而不是j<=i，因为不包括当前文件路径
                                parentDir += "/" + dirArr[j];
                            }
                        }
                        Path currDirPath = new Path(uploadHdfsPath + parentDir + "/" + currDir);
                        boolean existsCurrDir = fs.exists(currDirPath);
                        if(!existsCurrDir) {
                            fs.mkdirs(currDirPath);
                        }
                    }
                    String parentPath = uploadHdfsPath + "/" + parentDirName;//父hdfs目录
                    String path = request.getSession().getServletContext().getRealPath("/upload/hadoop");
                    String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                    String afterUploadFileName = UUID.randomUUID() + suffix;
                    FileUtils.uploadToLoc(file, path, afterUploadFileName);
        //            String uploadFilePath = path + "/" + uploadFile.getOriginalFilename();
                    Map<String,Object> map = HadoopUtils.upload(path + "/" + afterUploadFileName,
                            HadoopUtils.getHDFSPath(parentPath + "/" + file.getOriginalFilename()));
                    success = (boolean)map.get("success");
                    msg = map.get("msg").toString();
                }
            }
        } catch(Exception e) {
            logger.error(e.getMessage(), e);
            msg = "很抱歉，系统出错，上传失败！";
        }
        return new OperaterResult<>(success, msg);
    }
    /**
     * 从hdfs中下载文件到本地
     */
    @RequestMapping("downloadFileFromHDFS")
    @ResponseBody
    public Object downloadFileFromHDFS(@RequestParam(required = false) String downloadHdfsFilePath
//            , MultipartFile downloadLocalDirFile
    ){
        boolean success = false;
        String msg = "";
        if( StringUtils.isNotBlank(downloadHdfsFilePath) ) {
            try {
                String path = request.getSession().getServletContext().getRealPath("tmp/hadoop/");
                String downloadFileName = downloadHdfsFilePath.substring(downloadHdfsFilePath.lastIndexOf("/") + 1);
                Map<String,Object> map = HadoopUtils.downLoad(downloadHdfsFilePath, path);
                FileUtils.downloadFile(path, downloadFileName, response);
            } catch(Exception e) {
                logger.error(e.getMessage(), e);
                msg = "很抱歉，系统出错，下载失败！";
                return new OperaterResult<>(false, msg);
            }
        } else {
            msg = "请选择要下载的文件";
        }
        return null;
//        return new OperaterResult<>(true);
    }

    /**
     * 获取HDFS文件List
     */
    @RequestMapping("execMapReduceExample")
    @ResponseBody
    public Object execMapReduceExample(@RequestParam(required = false) String exampleId, @RequestParam(required = false) String param1,
                                       @RequestParam(required = false) String param2){
        boolean success = false;
        String result = "";
        String msg = "";
        try {
            String hadoopConfigFileName = "hadoop-config.properties";
            String ip = PropertyMgr.getPropertyByKey(hadoopConfigFileName, "ip");
            String username = PropertyMgr.getPropertyByKey(hadoopConfigFileName, "username");
            String passwd = PropertyMgr.getPropertyByKey(hadoopConfigFileName, "passwd");
            if( StringUtils.isNotBlank(ip) && StringUtils.isNotBlank(username) && StringUtils.isNotBlank(passwd) ) {
                String cmd = "sh /home/test/shell/execHadoopShell.sh \"hadoop jar /home/soft/hadoop/hadoop-2.7.3/share/hadoop/mapreduce/hadoop-mapreduce-examples-2.7.3.jar\" ";
                switch(exampleId) {
                    case "0":
                        cmd += "wordcount " + param1 + " " + param2;
                        break;
                    case "1":
                        cmd += "pi " + param1 + " " + param2;
                        break;
                    case "2":
                        cmd += "maxtemperature " + param1 + " " + param2;
                        break;
                    default:
                        cmd = "";
                        break;
                }
                Map<String, String> resMap = null;
                if( !"".equals(cmd) ) {
                    resMap = ShellUtil.execRemoteCmd(ip, username, passwd, cmd);
                    if( resMap != null ) {
                        result = resMap.get("result")!=null ? resMap.get("result") : "";
                        success = result.indexOf("completed successfully") != -1;//判断MR任务是否成功执行
                        if( !success ) {
                            msg = resMap.get("error");
                        }
                    }
                } else {
                    msg = "未选择mapreduce示例！";
                }
            } else {
                msg = "连接到HDFS主机：“" + ip + "”失败，请检查hadoop配置!";
            }
        } catch(Exception e) {
            logger.error(e.getMessage(), e);
            msg = e.getMessage();
        }
        result = formatMRResult(result);
        result = com.yo.friendis.common.common.util.StringUtils.solveSpecialCharInJson(result);
        return new OperaterResult<>(success, msg, result);
    }

//    public static void main(String[] args) {
//        String od2 = "17/04/15 04:24:36 INFO client.RMProxy: Connecting to ResourceManager at /10.14.1.50:803217/04/15 04:24:42 INFO input.FileInputFormat: Total input paths to process : 117/04/15 04:24:43 INFO mapreduce.JobSubmitter: number of splits:117/04/15 04:24:43 INFO mapreduce.JobSubmitter: Submitting tokens for job: job_1492150227840_003717/04/15 04:24:44 INFO impl.YarnClientImpl: Submitted application application_1492150227840_003717/04/15 04:24:44 INFO mapreduce.Job: The url to track the job: http://master:8088/proxy/application_1492150227840_0037/17/04/15 04:24:44 INFO mapreduce.Job: Running job: job_1492150227840_003717/04/15 04:25:00 INFO mapreduce.Job: Job job_1492150227840_0037 running in uber mode : false17/04/15 04:25:00 INFO mapreduce.Job:  map 0% reduce 0%17/04/15 04:25:10 INFO mapreduce.Job:  map 100% reduce 0%17/04/15 04:25:27 INFO mapreduce.Job:  map 100% reduce 25%17/04/15 04:25:30 INFO mapreduce.Job:  map 100% reduce 50%17/04/15 04:25:32 INFO mapreduce.Job:  map 100% reduce 100%17/04/15 04:25:33 INFO mapreduce.Job: Job job_1492150227840_0037 completed successfully17/04/15 04:25:33 INFO mapreduce.Job: Counters: 50\tFile System Counters\t\tFILE: Number of bytes read=57\t\tFILE: Number of bytes written=594936\t\tFILE: Number of read operations=0\t\tFILE: Number of large read operations=0\t\tFILE: Number of write operations=0\t\tHDFS: Number of bytes read=125\t\tHDFS: Number of bytes written=21\t\tHDFS: Number of read operations=15\t\tHDFS: Number of large read operations=0\t\tHDFS: Number of write operations=8\tJob Counters \t\tKilled reduce tasks=1\t\tLaunched map tasks=1\t\tLaunched reduce tasks=4\t\tData-local map tasks=1\t\tTotal time spent by all maps in occupied slots (ms)=8125\t\tTotal time spent by all reduces in occupied slots (ms)=62519\t\tTotal time spent by all map tasks (ms)=8125\t\tTotal time spent by all reduce tasks (ms)=62519\t\tTotal vcore-milliseconds taken by all map tasks=8125\t\tTotal vcore-milliseconds taken by all reduce tasks=62519\t\tTotal megabyte-milliseconds taken by all map tasks=4160000\t\tTotal megabyte-milliseconds taken by all reduce tasks=64019456\tMap-Reduce Framework\t\tMap input records=1\t\tMap output records=4\t\tMap output bytes=36\t\tMap output materialized bytes=57\t\tInput split bytes=106\t\tCombine input records=4\t\tCombine output records=3\t\tReduce input groups=3\t\tReduce shuffle bytes=57\t\tReduce input records=3\t\tReduce output records=3\t\tSpilled Records=6\t\tShuffled Maps =4\t\tFailed Shuffles=0\t\tMerged Map outputs=4\t\tGC time elapsed (ms)=445\t\tCPU time spent (ms)=3970\t\tPhysical memory (bytes) snapshot=609132544\t\tVirtual memory (bytes) snapshot=13394571264\t\tTotal committed heap usage (bytes)=257232896\tShuffle Errors\t\tBAD_ID=0\t\tCONNECTION=0\t\tIO_ERROR=0\t\tWRONG_LENGTH=0\t\tWRONG_MAP=0\t\tWRONG_REDUCE=0\tFile Input Format Counters \t\tBytes Read=19\tFile Output Format Counters \t\tBytes Written=21";
//        String orginalRes = "17/04/15 04:24:36 INFO client.RMProxy: Connecting to ResourceManager at /10.14.1.50:803217/04/15 04:24:42 INFO ";
//        String res = formatMRResult(od2);
//        System.out.println(res);
//    }
    private static String formatMRResult(String orginalRes) {
        StringBuilder resBuilder = new StringBuilder();
        StringBuilder builder = new StringBuilder(orginalRes);
        String regex = "(\\d\\d){1}/(((11|12)|(0[1-9]))){1}/(((1|2)[0-9]|(30|31))|(0[1-9])){1}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(builder.toString());
        while( matcher.find() ) {
            String group = matcher.group();
            int start = builder.indexOf(group);
            if( start != -1 ) {
                int end = start + 8;
                resBuilder.append(builder.substring(0, start)).append("\n").append(group);
                builder.replace(0, end, "");
            }
        }
        resBuilder.append(builder);
        resBuilder.replace(0, 1, "");
        return resBuilder.toString();
    }
    /**
     * 新建文件夹
     */
    @RequestMapping("mkdir")
    @ResponseBody
    public Object mkdir(@RequestParam(required = false) String rootPath, @RequestParam(required = false) String dirName) {
        boolean success = false;
        String msg = "";
        if( StringUtils.isNotBlank(dirName) ) {
            FileSystem fs = null;
            try {
                fs = FileSystem.get(new URI(PropertyMgr.getPropertyByKey("hadoop-config.properties", "fs.defaultFS")), new Configuration());
                if( "/".equals(rootPath) ) {
                    rootPath = "";
                }
                fs.mkdirs(new Path(rootPath + "/" + dirName));
                success = true;
                msg = "新增文件夹：" + dirName + "成！";
            } catch(Exception e) {
                msg = e.getMessage();
                logger.error(e.getMessage(), e);
            }
        } else {
            msg = "请输入文件名";
        }
        return new OperaterResult<>(success, msg);
    }
    /**
     * 删除文件（文件夹）
     */
    @RequestMapping("deleteHdfsFiles")
    @ResponseBody
    public Object deleteHdfsFiles(@RequestParam(required = false, value = "deleteFilePaths[]") List<String> deleteFilePaths) {
        boolean success = false;
        String msg = "";
        if( deleteFilePaths!=null && !deleteFilePaths.isEmpty() ) {
            FileSystem fs = null;
            try {
                fs = FileSystem.get(new URI(PropertyMgr.getPropertyByKey("hadoop-config.properties", "fs.defaultFS")), new Configuration());
                for(String deleteFilePath : deleteFilePaths ) {
                    fs.delete(new Path(deleteFilePath), true);
                }
                success = true;
                msg = "删除成功！";
            } catch(Exception e) {
                msg = e.getMessage();
                logger.error(e.getMessage(), e);
            }
        } else {
            msg = "请选择要删除的文件";
        }
        return new OperaterResult<>(success, msg);
    }
    /**
     * 重命名
     */
    @RequestMapping("rename")
    @ResponseBody
    public Object rename(@RequestParam(required = false) String orginalFilePath, @RequestParam(required = false) String newName) {
        boolean success = false;
        String msg = "";
        if( StringUtils.isNotBlank(orginalFilePath) && StringUtils.isNotBlank(newName) ) {
            FileSystem fs = null;
            try {
                fs = FileSystem.get(new URI(PropertyMgr.getPropertyByKey("hadoop-config.properties", "fs.defaultFS")), new Configuration());
                if( !orginalFilePath.substring(0, 1).equals("/") ) {
                    orginalFilePath = "/" + orginalFilePath;
                }
                int lastXieGangIndex = orginalFilePath.lastIndexOf("/");
                String rootPath = "";
                if( lastXieGangIndex != -1 ) {
                    rootPath = orginalFilePath.substring(0, lastXieGangIndex);
                }
                fs.rename(new Path(orginalFilePath), new Path(rootPath + "/" + newName));
                success = true;
                msg = "重名名成功！";
            } catch(Exception e) {
                success = false;
                msg = e.getMessage();
                logger.error(e.getMessage(), e);
            }
        } else {
            msg = "请输入文件名";
        }
        return new OperaterResult<>(success, msg);
    }
    /**
     * 复制到
     */
    @RequestMapping("copyTo")
    @ResponseBody
    public Object copyTo(@RequestParam(required = false, value = "srcPaths[]") List<String> srcPaths, String dstPath) {
        boolean success = false;
        String msg = "";
        FileSystem fs = null;
        try {
            fs = FileSystem.get(new URI(PropertyMgr.getPropertyByKey("hadoop-config.properties", "fs.defaultFS")), new Configuration());
            String localTmpPath = request.getSession().getServletContext().getRealPath("tmp/hadoop/");
            for(String srcPath : srcPaths) {
                fs.copyToLocalFile(false, new Path(srcPath), new Path(localTmpPath));//先复制到本地文件夹(不删除源文件，即为复制）
                fs.copyFromLocalFile(true, new Path(localTmpPath + "/" + new Path(srcPath).getName()), new Path(dstPath));
            }
            success = true;
            msg = "复制成功！";
        } catch(Exception e) {
            msg = e.getMessage();
            logger.error(e.getMessage(), e);
        }
        return new OperaterResult<>(success, msg);
    }
    /**
     * 移动到
     */
    @RequestMapping("moveTo")
    @ResponseBody
    public Object moveTo(@RequestParam(required = false, value = "srcPaths[]") List<String> srcPaths, String dstPath) {
        boolean success = false;
        String msg = "";
        FileSystem fs = null;
        try {
            fs = FileSystem.get(new URI(PropertyMgr.getPropertyByKey("hadoop-config.properties", "fs.defaultFS")), new Configuration());
            String localTmpPath = request.getSession().getServletContext().getRealPath("tmp/hadoop/");
            for(String srcPath : srcPaths) {
                fs.moveToLocalFile(new Path(srcPath), new Path(localTmpPath));//先复制到本地文件夹(删除源文件，即为移动）//该方法原理为：调用copyToLocalFile(true, new Path(srcPath), new Path(localTmpPath))，故无论使用copy还是move类方法均可
                fs.moveFromLocalFile(new Path(localTmpPath + "/" + new Path(srcPath).getName()), new Path(dstPath));//该方法原理为：调用copyFromLocalFile(true, new Path(srcPath), new Path(localTmpPath))，故无论使用copy还是move类方法均可
            }
            success = true;
            msg = "移动成功！";
        } catch(Exception e) {
            msg = e.getMessage();
            logger.error(e.getMessage(), e);
        }
        return new OperaterResult<>(success, msg);
    }
    /**
     * 获取HDFS目录List
     */
    @RequestMapping("getHdfsDirs")
    @ResponseBody
    public Object getHdfsDirs(boolean isRoot, String expandNodeFilePath, String id){
        FileSystem fs = null;
        try {
            fs = FileSystem.get(new URI(PropertyMgr.getPropertyByKey("hadoop-config.properties", "fs.defaultFS")), new Configuration());
            if( isRoot ) {
                List<Map<String,Object>> dirsList = new ArrayList<Map<String,Object>>();
                Map<String,Object> rootDirMap = new HashMap<String,Object>();
                rootDirMap.put("fileId", "1");//已文件路径作为id
                rootDirMap.put("fileName", "全部文件");
                rootDirMap.put("fileSize", "");
                rootDirMap.put("parentId", "0");
                rootDirMap.put("state", "open");
                dirsList.add(rootDirMap);//添加根节点（全部文件)
                FileStatus[] files = fs.listStatus(new Path("/"));
                if( index > 1 ) {
                    index = 2;
                }
                rootDirMap.put("children", findDirChildsRecursive(fs, new Path("/"), 1, false));
                rootDirMap.put("lastIndex", index);//将最后的index值设置进root节点
                return new DataGrid(dirsList, dirsList.size());
            } else {
                List<Map<String, Object>> childDirsMapList = findDirChildsRecursive(fs, new Path(expandNodeFilePath), Integer.parseInt(id), false);
                return childDirsMapList;
            }
        } catch( Exception e ) {
            logger.error(e.getMessage(), e);
            return new OperaterResult<>(false, "读取hdfs目录出错！");
        }
    }
    /**
     * 获取HDFS文件List
     */
    @RequestMapping("getHdfsFiles")
    @ResponseBody
    public Object getHdfsFiles(Page page, boolean isRoot, String expandNodeFilePath, String id, String keyword){
        FileSystem fs = null;
        try {
            fs = FileSystem.get(new URI(PropertyMgr.getPropertyByKey("hadoop-config.properties", "fs.defaultFS")), new Configuration());
            if( isRoot ) {
                List<Map<String,Object>> dirsList = new ArrayList<Map<String,Object>>();
                if( StringUtils.isBlank(keyword) ) {
                    FileStatus[] files = fs.listStatus(new Path("/"));
                    Map<String,Object> rootDirMap = new HashMap<String,Object>();
                    rootDirMap.put("fileId", "1");
                    rootDirMap.put("fileName", "全部文件");
                    rootDirMap.put("fileSize", "");
                    rootDirMap.put("parentId", "0");
                    rootDirMap.put("state", "open");
                    dirsList.add(rootDirMap);//添加根节点（全部文件)
                    if( index > 1 ) {
                        index = 2;
                    }
                    List<Map<String, Object>> children = findFileChildsRecursive(fs, new Path("/"), 1, page, false);
                    rootDirMap.put("children", children);
                    rootDirMap.put("lastIndex", index);//将最后的index值设置进root节点
                } else {
                    List<Map<String, Object>> filesList = new ArrayList<Map<String, Object>>();
                    findFileChildsRecursive(filesList, fs, new Path("/"), 1, false);
                    for(Map<String, Object> fileMap : filesList) {
                        String fileName = fileMap.get("fileName").toString().substring(fileMap.get("fileName").toString().lastIndexOf("/")+1);
                        if( fileName.indexOf(keyword) != -1 ) {
//                            fileMap.put("fileName", fileName);
                            dirsList.add(fileMap);//添加
                        }
                    }
                    //排序
                    if( page!=null && StringUtils.isNotBlank(page.getSort()) && !"undefined".equals(page.getSort()) ) {
                        String sort = page.getSort();
                        String order = page.getOrder();
                        if( "asc".equals(order) ) {
                            dirsList.sort((Map<String, Object> m1, Map<String, Object> m2)->m1.get(sort).toString().compareTo(m2.get(sort).toString()));
                        } else {
                            dirsList.sort((Map<String, Object> m1, Map<String, Object> m2)->m2.get(sort).toString().compareTo(m1.get(sort).toString()));
                        }
                    }
                }
                return new DataGrid(dirsList, dirsList.size());
            } else {
                List<Map<String, Object>> childDirsMapList = findFileChildsRecursive(fs, new Path(expandNodeFilePath), Integer.parseInt(id), page, false);
                return childDirsMapList;
            }
        } catch( Exception e ) {
            logger.error(e.getMessage(), e);
            return new OperaterResult<>(false, "读取hdfs目录出错！");
        }
    }

    private List<Map<String, Object>> findDirChildsRecursive(FileSystem fs, Path path, int rootId, boolean recursive) throws IOException {
        List<Map<String,Object>> rootChildren = new ArrayList<Map<String,Object>>();
        FileStatus[] childs = fs.listStatus(path);

        if( childs!=null && childs.length>0 ) {
            for(FileStatus file : childs) {
                if( file.isDirectory() ) {//仅添加目录
                    int currNodeId = index++;
                    String rootPath = file.getPath().toString().substring(0, file.getPath().toString().lastIndexOf("/")+1).replace("hdfs://master:9000", "");
                    String fileName = file.getPath().toString().substring(file.getPath().toString().lastIndexOf("/")+1);
                    Map<String,Object> dirMap = new HashMap<String,Object>();
                    dirMap.put("fileId", currNodeId);//已文件路径作为id
                    dirMap.put("fileName", fileName);
                    dirMap.put("rootPath", rootPath);
                    dirMap.put("modifyTime", DateUtils.formatTime(new Date(file.getModificationTime())));
                    dirMap.put("fileSize", FileUtils.getFormatFileSize(file.getLen()));
                    dirMap.put("parentId", rootId);
                    dirMap.put("isFolder", file.isDirectory());
                    FileStatus[] childChilds = fs.listStatus(file.getPath());
                    List<Map<String,Object>> children = null;
                    if( childChilds!=null && childChilds.length>0 ) {//存在子文件夹
                        children = findDirChildsRecursive(fs, file.getPath(), currNodeId, recursive);
                    } else {
                        if( file.isDirectory() ) {
                            dirMap.put("isEmptyFolder", true);
                        }
                    }
                    if( children!=null && !children.isEmpty() ) {
                        dirMap.put("state", "closed");
                        if( recursive ) {
                            dirMap.put("children", children);
                        }
                    } else {
                        dirMap.put("iconCls", "icon-tree-folder");
                    }
                    rootChildren.add(dirMap);
                }
            }
        }
        return rootChildren;
    }
    private List<Map<String, Object>> findFileChildsRecursive(FileSystem fs, Path path, int rootId, boolean recursive) throws IOException {
        List<Map<String,Object>> rootChildren = new ArrayList<Map<String,Object>>();
        FileStatus[] childs = fs.listStatus(path);

        if( childs!=null && childs.length>0 ) {
            for(FileStatus file : childs) {
                int currNodeId = index++;
                String rootPath = file.getPath().toString().substring(0, file.getPath().toString().lastIndexOf("/")+1).replace("hdfs://master:9000", "");
                String fileName = file.getPath().toString().substring(file.getPath().toString().lastIndexOf("/")+1);
                Map<String,Object> dirMap = new HashMap<String,Object>();
                dirMap.put("fileId", currNodeId);//已文件路径作为id
                dirMap.put("fileName", fileName);
                dirMap.put("rootPath", rootPath);
                dirMap.put("modifyTime", DateUtils.formatTime(new Date(file.getModificationTime())));
                dirMap.put("fileSize", FileUtils.getFormatFileSize(file.getLen()));
                dirMap.put("parentId", rootId);
                dirMap.put("isFolder", file.isDirectory());
                if( file.isDirectory() ) {//当当前file为目录时才寻找子文件和目录
                    FileStatus[] childChilds = fs.listStatus(file.getPath());
                    List<Map<String,Object>> children = null;
                    if( childChilds!=null && childChilds.length>0 ) {//存在子文件或文件夹
                        children = findFileChildsRecursive(fs, file.getPath(), currNodeId, recursive);
                    } else {
                        dirMap.put("isEmptyFolder", true);
                    }
                    if( children!=null && !children.isEmpty() ) {
                        dirMap.put("state", "closed");
                        if( recursive ) {
                            dirMap.put("children", children);
                        }
                    } else {
                        if( file.isDirectory() ) {
                            dirMap.put("iconCls", "icon-tree-folder");
                        }
                    }
                }
                rootChildren.add(dirMap);
            }
        }
        return rootChildren;
    }
    private List<Map<String, Object>> findFileChildsRecursive(FileSystem fs, Path path, int rootId, Page page, boolean recursive) throws IOException {
        List<Map<String,Object>> rootChildren = new ArrayList<Map<String,Object>>();
        FileStatus[] childs = fs.listStatus(path);

        if( childs!=null && childs.length>0 ) {
            for(FileStatus file : childs) {
                int currNodeId = index++;
                String rootPath = file.getPath().toString().substring(0, file.getPath().toString().lastIndexOf("/")+1).replace("hdfs://master:9000", "");
                String fileName = file.getPath().toString().substring(file.getPath().toString().lastIndexOf("/")+1);
                Map<String,Object> dirMap = new HashMap<String,Object>();
                dirMap.put("fileId", currNodeId);//已文件路径作为id
                dirMap.put("fileName", fileName);
                dirMap.put("rootPath", rootPath);
                dirMap.put("modifyTime", DateUtils.formatTime(new Date(file.getModificationTime())));
                dirMap.put("fileSize", FileUtils.getFormatFileSize(file.getLen()));
                dirMap.put("parentId", rootId);
                dirMap.put("isFolder", file.isDirectory());
                if( file.isDirectory() ) {//当当前file为目录时才寻找子文件和目录
                    FileStatus[] childChilds = fs.listStatus(file.getPath());
                    List<Map<String,Object>> children = null;
                    if( childChilds!=null && childChilds.length>0 ) {//存在子文件或文件夹
                        children = findFileChildsRecursive(fs, file.getPath(), currNodeId, page, recursive);
                    } else {
                        dirMap.put("isEmptyFolder", true);
                    }
                    if( children!=null && !children.isEmpty() ) {
                        dirMap.put("state", "closed");
                        if( recursive ) {
                            dirMap.put("children", children);
                        }
                    } else {
                        if( file.isDirectory() ) {
                            dirMap.put("iconCls", "icon-tree-folder");
                        }
                    }
                }
                rootChildren.add(dirMap);
            }
        }
        //递归排序
        if( page!=null && StringUtils.isNotBlank(page.getSort()) && !"undefined".equals(page.getSort()) ) {
            String sort = page.getSort();
            String order = page.getOrder();
            if( "asc".equals(order) ) {
                rootChildren.sort((Map<String, Object> m1, Map<String, Object> m2)->m1.get(sort).toString().compareTo(m2.get(sort).toString()));
            } else {
                rootChildren.sort((Map<String, Object> m1, Map<String, Object> m2)->m2.get(sort).toString().compareTo(m1.get(sort).toString()));
            }
        }
        return rootChildren;
    }
    private List<Map<String, Object>> findFileChildsRecursive(List<Map<String,Object>> files, FileSystem fs, Path path, int rootId, boolean recursive) throws IOException {
        List<Map<String,Object>> rootChildren = new ArrayList<Map<String,Object>>();
        FileStatus[] childs = fs.listStatus(path);

        if( childs!=null && childs.length>0 ) {
            for(FileStatus file : childs) {
                int currNodeId = index++;
                String rootPath = file.getPath().toString().substring(0, file.getPath().toString().lastIndexOf("/")+1).replace("hdfs://master:9000", "");
                String fileName = file.getPath().toString().substring(file.getPath().toString().lastIndexOf("/")+1);
                Map<String,Object> dirMap = new HashMap<String,Object>();
                dirMap.put("fileId", currNodeId);//已文件路径作为id
                dirMap.put("fileName", fileName);
                dirMap.put("rootPath", rootPath);
                dirMap.put("modifyTime", DateUtils.formatTime(new Date(file.getModificationTime())));
                dirMap.put("fileSize", FileUtils.getFormatFileSize(file.getLen()));
                dirMap.put("parentId", rootId);
                dirMap.put("isFolder", file.isDirectory());
                files.add(dirMap);
                if( file.isDirectory() ) {//当当前file为目录时才寻找子文件和目录
                    FileStatus[] childChilds = fs.listStatus(file.getPath());
                    List<Map<String,Object>> children = null;
                    if( childChilds!=null && childChilds.length>0 ) {//存在子文件或文件夹
                        children = findFileChildsRecursive(files, fs, file.getPath(), currNodeId, recursive);
                    } else {
                        dirMap.put("isEmptyFolder", true);
                    }
                    if( children!=null && !children.isEmpty() ) {
                        dirMap.put("state", "closed");
                        if( recursive ) {
                            dirMap.put("children", children);
                        }
                        files.addAll(children);
                    } else {
                        if( file.isDirectory() ) {
                            dirMap.put("iconCls", "icon-tree-folder");
                        }
                    }
                }
            }
        }
        return rootChildren;
    }
}
