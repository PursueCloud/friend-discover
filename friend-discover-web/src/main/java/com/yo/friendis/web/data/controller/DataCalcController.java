package com.yo.friendis.web.data.controller;


import com.yo.friendis.common.common.bean.OperaterResult;
import com.yo.friendis.common.common.controller.BaseController;
import com.yo.friendis.core.hadoop.thread.CalDistance;
import com.yo.friendis.core.hadoop.thread.RunClusterStep1;
import com.yo.friendis.core.hadoop.thread.RunClusterStep2;
import com.yo.friendis.core.hadoop.util.DrawPic;
import com.yo.friendis.core.hadoop.util.HadoopUtils;
import com.yo.friendis.core.util.CommonUtils;
import com.yo.friendis.web.data.util.DataCalcUtils;
import com.yo.friendis.web.friend.model.CustomUser;
import com.yo.friendis.web.friend.model.CustomUserGroup;
import com.yo.friendis.web.friend.service.CustomUserGroupService;
import com.yo.friendis.web.friend.service.CustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yo on 2017/3/27.
 */
@Controller
@RequestMapping("dataCalc")
public class DataCalcController extends BaseController {
    @Autowired
    private CustomUserService custUserService;
    @Autowired
    private CustomUserGroupService custUGService;

    @RequestMapping("preCalc")//包括距离计算和最佳dc计算
    public ModelAndView preCalc() {
        return new ModelAndView("dataCalc/preCalc");
    }

    @RequestMapping("clusterCalc")
    public ModelAndView clusterCalc() {
        return new ModelAndView("dataCalc/clusterCalc");
    }
//========================前置计算
    /**
     * 计算向量之间的距离
     */
    @RequestMapping("calcDistance")
    @ResponseBody
    public Object calcDistance(String inputPath, String outputPath){
        boolean success = false;
        String msg = "";
        Map<String ,Object> map = new HashMap<String,Object>();
        try{
            HadoopUtils.setJobStartTime(System.currentTimeMillis());
            HadoopUtils.JOBNUM=1;
            new Thread(new CalDistance(inputPath, outputPath)).start();
            success = true;
            map.put("monitor", true);
        } catch (Exception e) {
            success = false;
            msg = e.getMessage();
            map.put("monitor", false);
            map.put("msg", e.getMessage());
            logger.error(e.getMessage(), e);
        }
        return new OperaterResult<>(success, msg, map);
    }
    /**
     * 遍历向量距离文件，寻找最佳阈值
     */
    @RequestMapping("findBestDC")
    @ResponseBody
    public Object findBestDC(String inputPath, String inputRecordNum, String thresholdPercent){
        boolean success = false;
        String msg = "";
        double dc = 0.0;//最佳DC
        int recordInt = Integer.parseInt(inputRecordNum);
        if(HadoopUtils.INPUT_RECORDS==0&&recordInt!=0){
            HadoopUtils.INPUT_RECORDS=recordInt;
        }
        try{
            if(HadoopUtils.INPUT_RECORDS==0){
                success = false;
                msg = "请先运行计算距离MR任务，或者设置任务运行后的记录数!";
            } else {
                dc = HadoopUtils.findInitDC(Double.parseDouble(thresholdPercent)/100, inputPath,
                        HadoopUtils.INPUT_RECORDS);
                success = true;
            }
        } catch(Exception e){
            logger.error(e.getMessage(), e);
            success = false;
            msg = e.getMessage();
        }
        return new OperaterResult<>(success, msg, dc);
    }
//========================聚类计算
    /**
     * 提交fast cluster第一步MR任务(Fast Cluster 算法调用--寻找中心向量
     */
    @RequestMapping("execCluster")//执行聚类
    @ResponseBody
    public Object execCluster(String inputPath, String distanceThresholdNum, String consistencyCalcAlgorithm, String partConsistencyMRReducerNum,
                                String minDistanceMRReducerNum, String sortMRReducerNum) {
        boolean success = false;
        String msg = "";
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            //提交一个Hadoop MR任务的基本流程
            // 1. 设置提交时间阈值,并设置这组job的个数
            //使用当前时间即可,当前时间往前10s，以防服务器和云平台时间相差
            HadoopUtils.setJobStartTime(System.currentTimeMillis());//
            HadoopUtils.JOBNUM=3;
            // 2. 使用Thread的方式启动一组MR任务
            new Thread(new RunClusterStep1(inputPath, distanceThresholdNum, consistencyCalcAlgorithm, partConsistencyMRReducerNum,
                    minDistanceMRReducerNum, sortMRReducerNum)).start();
            // 3. 启动成功后，直接返回到监控，同时监控定时向后台获取数据，并在前台展示；
            success = true;
            map.put("monitor", true);
        } catch(Exception e) {
            success = false;
            msg = e.getMessage();
            map.put("monitor", false);
            logger.error(e.getMessage(), e);
        }
        return new OperaterResult<>(success, msg, map);
    }
    /**
     * 提交fast cluster第二步MR任务(Fast Cluster 算法调用--执行分类
     *
     */
    @RequestMapping("execClassify")//执行分类
    @ResponseBody
    public Object execClassify(String inputPath, String outputPath, String clusterCenterNum, String distanceThresholdNum){
        boolean success = false;
        String msg = "";
        Map<String ,Object> map = new HashMap<String,Object>();
        try {
            //提交一个Hadoop MR任务的基本流程
            // 1. 设置提交时间阈值,并设置这组job的个数
            //使用当前时间即可,当前时间往前10s，以防服务器和云平台时间相差
            HadoopUtils.setJobStartTime(System.currentTimeMillis());//
            // 由于不知道循环多少次完成，所以这里设置为2，每次循环都递增1
            // 当所有循环完成的时候，就该值减去2即可停止监控部分的循环
            HadoopUtils.JOBNUM=2;
            // 2. 使用Thread的方式启动一组MR任务
            new Thread(new RunClusterStep2(inputPath, outputPath, distanceThresholdNum, clusterCenterNum)).start();
            // 3. 启动成功后，直接返回到监控，同时监控定时向后台获取数据，并在前台展示；

            success = true;
            map.put("monitor", true);
        } catch (Exception e) {
            success = false;
            msg = e.getMessage();
            map.put("monitor", false);
            logger.error(e.getMessage(), e);
        }
        return new OperaterResult<>(success, msg, map);
    }

    /**
     * 根据给定的阈值寻找聚类中心向量，并写入hdfs
     * 非MR任务，不需要监控，注意返回值
     */
    @RequestMapping("findClusterCenter")
    @ResponseBody
    public Object findClusterCenter(String inputPath, String outputPath, String localFilePath,
                              String partConsistencyThresholdNum, String minDistanceThresholdNum){
        boolean success = false;
        String msg = "";
        // localfile:method
        // 1. 读取SortJob的输出，获取前面k条记录中的大于局部密度和最小距离阈值的id；
        // 2. 根据id，找到每个id对应的记录；
        // 3. 把记录转为double[] ；
        // 4. 把向量写入hdfs
        // 5. 把向量写入本地文件中，方便后面的查看
        Map<Object,Object> firstK =null;
        List<Integer> ids= null;
        List<CustomUser> users=null;
        try{
            firstK=HadoopUtils.readSeq(inputPath==null?HadoopUtils.SORTOUTPUT+"/part-r-00000":inputPath,
                    100);// 这里默认使用	前100条记录
            ids=HadoopUtils.getCentIds(firstK, partConsistencyThresholdNum, minDistanceThresholdNum);
            // 2
            users = custUserService.selectByIds(ids);
            CommonUtils.simpleLog("聚类中心向量有"+users.size()+"个！");
            // 3,4,5
            DataCalcUtils.writecenter2hdfs(users, localFilePath, outputPath);
            success = true;
        }catch(Exception e){
            msg = e.getMessage();
            logger.error(e.getMessage(), e);
        }
        return new OperaterResult<>(success, msg);
    }
    /**
     * 云平台已经分类好的数据解析并存入数据库中
     */
    @RequestMapping("putClassifyData2DB")//分类数据入库
    @ResponseBody
    public Object putClassifyData2DB(String inputPath){
        boolean success = false;
        String msg = "";
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("monitor", "false");
        try{
            // 解析
            List<CustomUserGroup> list = DataCalcUtils.resolve(inputPath);
            // 清库，并将数据批量入库
            custUGService.deleteAll();
            custUGService.addList(list);
            success = true;
        }catch(Exception e){
            msg = e.getMessage();
            logger.error(e.getMessage(), e);
        }
        return new OperaterResult<>(success, msg, map);
    }

    /**
     * 获取决策图数据
     * @return
     */
//    @RequestMapping("getDecisChartData")//决策图
//    @ResponseBody
//    public Object getDecisChartData(){
//        boolean success = false;
//        String msg = "";
//
//        Map<String, Object> resDataMap = null;
//        try{
//            String url = HadoopUtils.getHDFSPath(HadoopUtils.SORTOUTPUT);//获取数据所在hdfs-url
//            resDataMap = getXYseriesDataMap(url);//获取数据
//            success = true;
//        }catch(Exception e){
//            msg = e.getMessage();
//            logger.error(e.getMessage(), e);
//        }
//        return new OperaterResult<>(success, msg, resDataMap);
//    }
    /**
     * 画决策图
     */
    @RequestMapping("drawDecisionChart")//决策图
    @ResponseBody
    public Object drawDecisionChart(){
        boolean success = false;
        String msg = "";
        String url = HadoopUtils.getHDFSPath(HadoopUtils.SORTOUTPUT);
        String decisFilePath = "tmp/decision_chart.png";
        String file = CommonUtils.getRootPathBasedPath(decisFilePath);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("path", decisFilePath);
        try{
            DrawPic.drawPic(url, file);
            success = true;
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            msg = e.getMessage();
        }
        return new OperaterResult<>(success, msg, map);
    }
}
