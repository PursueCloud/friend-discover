package com.yo.friendis.core.hadoop.util;

import com.yo.friendis.core.hadoop.mapred.fastcluster.keytype.DoubleArrStrWritable;
import com.yo.friendis.core.hadoop.mapred.fastcluster.keytype.DoublePairWritable;
import com.yo.friendis.core.hadoop.mapred.fastcluster.keytype.IntDoublePairWritable;
import com.yo.friendis.core.hadoop.mapred.filter.keytype.DoubleArrIntWritable;
import com.yo.friendis.core.hadoop.model.CurrentJobInfo;
import com.yo.friendis.core.util.CommonUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobStatus;
import org.apache.hadoop.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * Created by Yo on 2017/4/7.
 */
public class HadoopUtils {
    public static final double VERYSMALL = 0.00000000000000000000000000001;

    // pre filter
    // 最原始user.xml文件
    public static final String SOURCEFILE = "/user/root/_source/source_users.xml";
    // 过滤文件夹
    public static final String FILTER = "/user/root/_filter";
    public static final String FILTER_DEDUPLICATE = FILTER + "/"
            + "deduplicate";

    public static final String FILTER_GETATTRIBUTES = FILTER + "/"
            + "getattributes";// 属性提取
    public static final String FILTER_GETMAXMIN = FILTER + "/" + "getmaxmin";// 获得列最大最小值
    public static final String FILTER_NORMALIZATION = FILTER + "/"
            + "normalization";// 归一化
    public static final String FILTER_FINDINITDC = FILTER + "/" + "findinitdc";// 寻找dc阈值
    public static final String FILTER_CALDISTANCE = FILTER + "/"
            + "caldistance";// 计算向量之间的距离

    public static final String DEDUPLICATE_LOCAL = "WEB-INF/classes/deduplicate_users.xml";

    public static final String MAP_COUNTER = "MAP_COUNTER";
    public static final String REDUCE_COUNTER = "REDUCE_COUNTER";
    public static final String REDUCE_COUNTER2 = "REDUCE_COUNTER2";

    public static final String DOWNLOAD_EXTENSION = ".dat";// 下载文件的后缀名

    public static double DELTA_DC = 0.0;// DC阈值，
    public static long INPUT_RECORDS = 0L;// 文件全部记录数，任务FindInitDCJob任务后对此值进行赋值

    // 聚类分类
    public static long CLUSTERED=-1;
    public static long UNCLUSTERED=-1;
    public static Logger logger = LoggerFactory.getLogger(HadoopUtils.class);
    // fast cluster
    public static final String LOCALDENSITYOUTPUT = "/user/root/localdensity";
    public static final String DELTADISTANCEOUTPUT = "/user/root/deltadistance";
    public static final String DELTADISTANCEBIN = "/user/root/deltadistance.bin";// 局部密度最大向量id存储路径
    public static final String SORTOUTPUT = "/user/root/sort";
    public static final String FILTER_PREPAREVECTORS = FILTER + "/"
            + "preparevectors";// 准备距离向量
    public static final String FIRSTUNCLUSTEREDPATH = "/user/root/_center/iter_0/unclustered";
    public static final String CENTERPATH = "/user/root/_center";

    public static final String CENTERPATHPREFIX = "/user/root/_center/iter_";

    private static Configuration conf = null;
    private static FileSystem fs = null;

    public static boolean flag = true; // get configuration from db or file
    // ,true : db,false:file

    public static int JOBNUM = 1; // 一组job的个数
    // 第一个job的启动时间阈值，大于此时间才是要取得的的真正监控任务

    private static long jobStartTime = 0L;// 使用 System.currentTimeMillis() 获得
    private static JobClient jobClient = null;

    public static Configuration getConf() {

        if (conf == null) {
            conf = new Configuration();
            // get configuration from db or file
            conf.setBoolean("mapreduce.app-submission.cross-platform", "true"
                    .equals(CommonUtils.getKey(
                            "mapreduce.app-submission.cross-platform", flag)));// 配置使用跨平台提交任务
            conf.set("fs.defaultFS", CommonUtils.getKey("fs.defaultFS", flag));// 指定namenode
            conf.set("mapreduce.framework.name",
                    CommonUtils.getKey("mapreduce.framework.name", flag)); // 指定使用yarn框架
            conf.set("yarn.resourcemanager.address",
                    CommonUtils.getKey("yarn.resourcemanager.address", flag)); // 指定resourcemanager
            conf.set("yarn.resourcemanager.scheduler.address", CommonUtils.getKey(
                    "yarn.resourcemanager.scheduler.address", flag));// 指定资源分配器
            conf.set("mapreduce.jobhistory.address",
                    CommonUtils.getKey("mapreduce.jobhistory.address", flag));
            conf.set("mapreduce.map.memory.mb","1536");
            conf.set("mapreduce.reduce.memory.mb","1536");
        }

        return conf;
    }

    public static FileSystem getFs() {
        if (fs == null) {
            try {
                fs = FileSystem.get(getConf());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fs;
    }

    /**
     * 获取hdfs文件目录及其子文件夹信息
     *
     * @param input
     * @param recursive
     * @return
     * @throws IOException
     */
    public static String getHdfsFiles(String input, boolean recursive)
            throws IOException {
        RemoteIterator<LocatedFileStatus> files = getFs().listFiles(
                new Path(input), recursive);
        StringBuffer buff = new StringBuffer();
        while (files.hasNext()) {
            buff.append(files.next().getPath().toString()).append("<br>");
        }

        return buff.toString();
    }

    /**
     * 根据时间来判断，然后获得Job的状态，以此来进行监控 Job的启动时间和使用system.currentTimeMillis获得的时间是一致的，
     * 不存在时区不同的问题；
     *
     * @return
     * @throws IOException
     */
    public static List<CurrentJobInfo> getJobs() throws IOException {
        JobStatus[] jss = getJobClient().getAllJobs();
        List<CurrentJobInfo> jsList = new ArrayList<CurrentJobInfo>();
        jsList.clear();
        for (JobStatus js : jss) {
            if (js.getStartTime() > jobStartTime) {
                jsList.add(new CurrentJobInfo(getJobClient().getJob(
                        js.getJobID()), js.getStartTime(), js.getFinishTime(), js.getRunState()));
            }
        }
        Collections.sort(jsList);
        return jsList;
    }

    /**
     * 获取所有任务信息
     * @return
     * @throws IOException
     */
    public static List<CurrentJobInfo> getAllJobs() throws IOException {
        JobStatus[] jss = getJobClient().getAllJobs();
        List<CurrentJobInfo> jsList = new ArrayList<CurrentJobInfo>();
        jsList.clear();
        for (JobStatus js : jss) {
            jsList.add(new CurrentJobInfo(getJobClient().getJob(
                    js.getJobID()), js.getStartTime(), js.getFinishTime(), js.getRunState()));
        }
        Collections.sort(jsList);
        return jsList;
    }
    public static void printJobStatus(JobStatus js) {
        System.out.println(new java.util.Date() + ":jobId:"
                + js.getJobID().toString() + ",map:" + js.getMapProgress()
                + ",reduce:" + js.getReduceProgress() + ",finish:"
                + js.getRunState());
    }

    /**
     * @return the jobClient
     */
    public static JobClient getJobClient() {
        if (jobClient == null) {
            try {
                jobClient = new JobClient(getConf());
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return jobClient;
    }

    /**
     * @param jobClient
     *            the jobClient to set
     */
    public static void setJobClient(JobClient jobClient) {
        jobClient = jobClient;
    }

    public static long getJobStartTime() {
        return jobStartTime;
    }

    public static void setJobStartTime(long jobStartTime) {
        HadoopUtils.jobStartTime = jobStartTime;
    }

    /**
     * 判断一组MR任务是否完成
     *
     * @param currentJobInfo
     * @return
     */
    public static String hasFinished(CurrentJobInfo currentJobInfo) {

        if (currentJobInfo != null) {
            if ("SUCCEEDED".equals(currentJobInfo.getRunState())) {
                return "success";
            }
            if ("FAILED".equals(currentJobInfo.getRunState())) {
                return "fail";
            }
            if ("KILLED".equals(currentJobInfo.getRunState())) {
                return "kill";
            }
        }

        return "running";
    }

    /**
     * 返回HDFS路径
     *
     * @param url
     * @return fs.defaultFs+url
     */
    public static String getHDFSPath(String url) {

        return CommonUtils.getKey("fs.defaultFS", flag) + url;
    }
    /**
     * 获得Path路径
     * 如果url包含hdfs:// ，那么flag使用true，否则flag使用false
     * @param url
     * @param flag
     * @return
     */
    public static Path getHDFSPath(String url,String flag) {
        if("true".equals(flag)){
            return new Path(url);
        }
        return new Path(getHDFSPath(url));
    }
    /**
     * use the oath distance
     *
     * @param inputI
     * @param ds
     * @return
     */
    public static double getDistance(double[] inputI, double[] ds) {
        double error = 0.0;
        for (int i = 0; i < inputI.length; i++) {
            error += (inputI[i] - ds[i]) * (inputI[i] - ds[i]);
        }
        return Math.sqrt(error);
    }

    /**
     * 上传本地文件到HFDS
     *
     * @param localPath
     * @param hdfsPath
     * @return
     */
    public static Map<String, Object> upload(String localPath, String hdfsPath) {
        Map<String, Object> ret = new HashMap<String, Object>();
        FileSystem fs = getFs();
        Path src = new Path(localPath);
        Path dst = new Path(hdfsPath);
        try {
            fs.copyFromLocalFile(src, dst);
            CommonUtils.simpleLog(localPath+"上传至"+hdfsPath+"成功");
            ret.put("success", true);
            ret.put("msg", localPath+"上传至"+hdfsPath+"成功");
        } catch (Exception e) {
            ret.put("success", false);
            ret.put("msg", e.getMessage());
            logger.error(e.getMessage(), e);
        }
        return ret;
    }

    /**
     * 删除HFDS文件或者文件夹
     *
     * @param hdfsFolder
     * @return
     */
    public static boolean delete(String hdfsFolder) {
        return false;
    }

    /**
     * 下载文件
     * @param hdfsPath
     * @param localPath
     *            ,本地文件夹
     * @return
     */
    public static Map<String, Object> downLoad(String hdfsPath, String localPath) {
        Map<String, Object> ret = new HashMap<String, Object>();
        FileSystem fs = getFs();
        Path src = new Path(hdfsPath);
        Path dst = new Path(localPath);
        try {
            RemoteIterator<LocatedFileStatus> fss = fs.listFiles(src, true);
            int i = 0;
            while (fss.hasNext()) {
                LocatedFileStatus file = fss.next();
                if (file.isFile()) {
                    // 使用这个才能下载成功
                    fs.copyToLocalFile(false, file.getPath(), new Path(dst,
                            file.getPath().getName()), true);
                }
            }
            ret.put("success", true);
        } catch (Exception e) {
            ret.put("success", false);
            ret.put("msg", e.getMessage());
            logger.error(e.getMessage(), e);
        }
        return ret;
    }

    /**
     * 移动文件
     *
     * @param hdfsPath
     * @param desHdfsPath
     * @return
     */
    public static boolean mv(String hdfsPath, String desHdfsPath) {

        return false;
    }

    /**
     * 更新最小值
     *
     * @param key
     * @param min_1
     * @param column
     */
    public static void updateMin(DoubleArrStrWritable key,
                                 DoubleArrStrWritable min_1, int column) {
        for (int i = 0; i < column; i++) {
            if (key.getDoubleArr()[i] < min_1.getDoubleArr()[i]) {
                min_1.getDoubleArr()[i] = key.getDoubleArr()[i];// 直接赋值
            }
        }
    }

    /**
     * 更新最大值
     *
     * @param key
     * @param max_1
     * @param column
     */
    public static void updateMax(DoubleArrStrWritable key,
                                 DoubleArrStrWritable max_1, int column) {
        for (int i = 0; i < column; i++) {
            if (key.getDoubleArr()[i] > max_1.getDoubleArr()[i]) {
                max_1.getDoubleArr()[i] = key.getDoubleArr()[i];// 直接赋值
            }
        }
    }

    /**
     * 根据给定的阈值百分比返回阈值
     *
     * @param percent
     *            一般为1~2%
     * @return
     */
    public static double findInitDC(double percent, String path,long iNPUT_RECORDS2) {
        Path input = null;
        if (path == null) {
            input = new Path(getHDFSPath(FILTER_CALDISTANCE
                    + "/part-r-00000"));
        } else {
            input = new Path(getHDFSPath(path + "/part-r-00000"));
        }
        Configuration conf = getConf();
        SequenceFile.Reader reader = null;
        long counter = 0;
        long percent_ = (long) (percent * iNPUT_RECORDS2);
        try {
            reader = new SequenceFile.Reader(conf, SequenceFile.Reader.file(input),
                    SequenceFile.Reader.bufferSize(4096), SequenceFile.Reader.start(0));
            DoubleWritable dkey = (DoubleWritable) ReflectionUtils.newInstance(
                    reader.getKeyClass(), conf);
            Writable dvalue = (Writable) ReflectionUtils.newInstance(
                    reader.getValueClass(), conf);
            while (reader.next(dkey, dvalue)) {// 循环读取文件
                counter++;
                if(counter%1000==0){
                    CommonUtils.simpleLog("读取了"+counter+"条记录。。。");
                }
                if (counter >= percent_) {
                    DELTA_DC = dkey.get();// 赋予最佳DC阈值
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeStream(reader);
        }
        return DELTA_DC;
    }

    /**
     * @param value
     * @return
     */
    public static double[] getInputI(Text value, String splitter) {
        return getInputI(value.toString(), splitter);
    }

    public static double[] getInputI(String value, String splitter) {
        String[] inputStrArr = value.split(splitter);
        double[] inputI = new double[inputStrArr.length];

        for (int i = 0; i < inputI.length; i++) {
            inputI[i] = Double.parseDouble(inputStrArr[i]);
        }
        return inputI;
    }

    /**
     * get the cluster center by the given k return the dc for next
     * ClusterDataJob
     *
     * @param input
     * @param output
     * @param k
     * @throws IOException
     */
    public static double[] getCenterVector(String input, String output, int k)
            throws IOException {
        double[] r = new double[k];
        String[] queue = new String[k];

        // initialize the r array
        for (int i = 0; i < k; i++) {
            r[i] = -Double.MAX_VALUE;
        }
        Path path = new Path(input);
        Configuration conf = getConf();
        InputStream in = null;
        try {
            FileSystem fs = getFs();
            in = fs.open(path);
            BufferedReader read = new BufferedReader(new InputStreamReader(in));
            String line = null;
            int index = -1;
            while ((line = read.readLine()) != null) {
                // [5.5,4.2,1.4,0.2] 5,0.3464101615137755
                String[] lines = line.split("\t");
                String[] sd = lines[1].split(",");
                index = findSmallest(r,
                        Double.parseDouble(sd[0]) * Double.parseDouble(sd[1]));
                if (index != -1) {
                    r[index] = Double.parseDouble(sd[0])
                            * Double.parseDouble(sd[1]);
                    queue[index] = lines[0];
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // print
        double dc = Double.MAX_VALUE;
        double dc_max = -Double.MAX_VALUE;
        double distance = 0.0;
        for (int i = 0; i < queue.length; i++) {
            System.out.print("vector:" + queue[i]);
            for (int j = i + 1; j < queue.length; j++) {
                distance = getDistance(
                        getInputI(queue[i].substring(1, queue[i].length() - 1),
                                ","),
                        getInputI(queue[j].substring(1, queue[j].length() - 1),
                                ","));
                if (distance < dc) {
                    dc = distance;
                }
                if (distance > dc_max) {
                    dc_max = distance;
                }
            }
            System.out.print("\tr:" + r[i] + "\n");
        }
        // write to hdfs

        path = new Path(output);
        DoubleArrStrWritable key = null;
        IntWritable value = new IntWritable();
        SequenceFile.Writer writer = null;
        try {
            writer = SequenceFile.createWriter(conf, SequenceFile.Writer.file(path),
                    SequenceFile.Writer.keyClass(DoubleArrStrWritable.class),
                    SequenceFile.Writer.valueClass(value.getClass()));
            for (int i = 0; i < queue.length; i++) {
                key = new DoubleArrStrWritable(getInputI(
                        queue[i].substring(1, queue[i].length() - 1), ","));
                value.set(i + 1);
                writer.append(key, value);
            }
        } finally {
            IOUtils.closeStream(writer);
        }
        return new double[] { dc / 5, dc_max / 3 };
    }

    /**
     * find whether the d can replace one of the r array if can return the index
     * else return -1
     *
     * @param r
     * @param d
     */
    private static int findSmallest(double[] r, double d) {
        double small = Double.MAX_VALUE;
        int index = 0;
        for (int i = 0; i < r.length; i++) {
            if (r[i] < small) {
                small = r[i];
                index = i;
            }
        }
        if (r[index] < d) {
            return index;
        }
        return -1;
    }

    /**
     * find whether the d can replace one of the r array if can return the index
     * else return -1
     *
     * @param r
     * @param d
     */
    public static int findLargest(double[] r, double d) {
        double max = -Double.MAX_VALUE;
        int index = 0;
        for (int i = 0; i < r.length; i++) {
            if (r[i] > max) {
                max = r[i];
                index = i;
            }
        }
        if (r[index] > d) {
            return index;
        }
        return -1;
    }

    public static void readSeq(String url, String localPath) {
        Path path = new Path(url);
        Configuration conf = getConf();
        SequenceFile.Reader reader = null;
        FileWriter writer = null;
        BufferedWriter bw = null;
        try {
            writer = new FileWriter(localPath);
            bw = new BufferedWriter(writer);
            reader = new SequenceFile.Reader(conf, SequenceFile.Reader.file(path),
                    SequenceFile.Reader.bufferSize(4096), SequenceFile.Reader.start(0));
            DoubleArrStrWritable dkey = (DoubleArrStrWritable) ReflectionUtils
                    .newInstance(reader.getKeyClass(), conf);
            DoublePairWritable dvalue = (DoublePairWritable) ReflectionUtils
                    .newInstance(reader.getValueClass(), conf);

            while (reader.next(dkey, dvalue)) {// 循环读取文件
                bw.write(dvalue.getFirst() + "," + dvalue.getSecond());
                bw.newLine();
            }
            System.out.println(new java.util.Date() + "ds file:" + localPath);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeStream(reader);
            try {
                bw.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取HDFS文件并写入本地
     *
     * @param url
     * @param localPath
     */
    public static void readHDFSFile(String url, String localPath) {
        Path path = new Path(url);
        // Configuration conf = HadoopUtils.getConf();
        FileWriter writer = null;
        BufferedWriter bw = null;
        InputStream in = null;
        try {
            writer = new FileWriter(localPath);
            bw = new BufferedWriter(writer);
            // FileSystem fs = FileSystem.get(URI.create(url), conf);
            FileSystem fs = getFs();
            in = fs.open(path);
            BufferedReader read = new BufferedReader(new InputStreamReader(in));
            String line = null;

            while ((line = read.readLine()) != null) {
                // System.out.println("result:"+line.trim());
                // [5.5,4.2,1.4,0.2] 5,0.3464101615137755
                String[] lines = line.split("\t");
                bw.write(lines[1]);
                bw.newLine();
            }
            System.out.println(new java.util.Date() + "ds file:" + localPath);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                bw.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * read center to local file 读取中心点文件到本地
     *
     * @param iter_i
     * @param localPath
     */
    public static void readCenterToLocal(int iter_i, String localPath) {

        FileSystem fs = null;
        FileWriter writer = null;
        BufferedWriter bw = null;
        try {
            // fs = FileSystem.get(getConf());
            fs = getFs();
            // read all before center files
            String parentFolder = null;
            Path path = null;
            writer = new FileWriter(localPath);

            bw = new BufferedWriter(writer);

            SequenceFile.Reader reader = null;
            int start = iter_i == 0 ? 0 : 1;
            for (int i = start; i <= iter_i; i++) {
                parentFolder = getHDFSPath(CENTERPATH + "/iter_"
                        + i + "/clustered");
                if (!fs.exists(new Path(parentFolder))) {
                    continue;
                }
                RemoteIterator<LocatedFileStatus> files = fs.listFiles(
                        new Path(parentFolder), false);
                while (files.hasNext()) {
                    path = files.next().getPath();
                    if (!path.toString().contains("part")) {
                        continue; // return
                    }
                    reader = new SequenceFile.Reader(conf, SequenceFile.Reader.file(path),
                            SequenceFile.Reader.bufferSize(4096), SequenceFile.Reader.start(0));
                    DoubleArrStrWritable dkey = (DoubleArrStrWritable) ReflectionUtils
                            .newInstance(reader.getKeyClass(), conf);
                    IntWritable dvalue = (IntWritable) ReflectionUtils
                            .newInstance(reader.getValueClass(), conf);
                    while (reader.next(dkey, dvalue)) {// read file literally
                        bw.write(doubleArr2Str(dkey.getDoubleArr()) + ","
                                + dvalue.get());
                        bw.newLine();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bw.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * double数组转为字符串
     *
     * @param d
     * @return
     */
    public static String doubleArr2Str(double[] d) {
        StringBuffer buff = new StringBuffer();

        for (int i = 0; i < d.length; i++) {
            buff.append(d[i]).append(",");
        }
        return buff.substring(0, buff.length() - 1);
    }

    /**
     * int 数组转为字符串
     *
     * @param d
     * @return
     */
    public static String intArr2Str(int[] d) {
        StringBuffer buff = new StringBuffer();

        for (int i = 0; i < d.length; i++) {
            buff.append(d[i]).append(",");
        }
        return buff.substring(0, buff.length() - 1);
    }

    /**
     * 如果jsonMap.get("rows")的List的size不够JOBNUM，那么就需要添加
     *
     * @param jsonMap
     */
    public static void checkJsonMap(Map<String, Object> jsonMap) {
        @SuppressWarnings("unchecked")
        List<CurrentJobInfo> list = (List<CurrentJobInfo>) jsonMap.get("rows");
        if (list.size() == JOBNUM) {
            return;
        }
        for (int i = list.size(); i < HadoopUtils.JOBNUM; i++) {
            list.add(new CurrentJobInfo());
        }

    }


    /**
     * 读取给定序列文件的前面k条记录
     * @param input
     * @param k
     * @return
     */
    public static Map<Object, Object> readSeq(String input, int k) {
        Map<Object,Object> map= new HashMap<Object,Object>();
        Path path = getHDFSPath(input,"false");
        Configuration conf = getConf();
        SequenceFile.Reader reader = null;
        try {
            reader = new SequenceFile.Reader(conf, SequenceFile.Reader.file(path),
                    SequenceFile.Reader.bufferSize(4096), SequenceFile.Reader.start(0));
            Writable dkey =  (Writable) ReflectionUtils
                    .newInstance(reader.getKeyClass(), conf);
            Writable dvalue =  (Writable) ReflectionUtils
                    .newInstance(reader.getValueClass(), conf);

            while (reader.next(dkey, dvalue)&&k>0) {// 循环读取文件
                // 使用这个进行克隆
                map.put(WritableUtils.clone(dkey, conf),WritableUtils.clone( dvalue,conf));
                k--;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeStream(reader);
        }
        return map;
    }

    /**
     * 获取firstK中的id
     * {key:mul,value:third:id}<first:density_i,second:min_distance_j,third:i>
     * @param firstK
     * @param numReducerDistance
     * @param numReducerDensity
     * @return
     */
    public static List<Integer> getCentIds(Map<Object, Object> firstK, String numReducerDensity, String numReducerDistance) {
        List<Integer> ids = new ArrayList<Integer>();
        IntDoublePairWritable v=null;

        for(Object i: firstK.values()){
            v=(IntDoublePairWritable) i;
            if(v.getFirst()>Double.parseDouble(numReducerDensity)&&
                    v.getSecond()>Double.parseDouble(numReducerDistance)){
                ids.add(v.getThird());
            }
        }
        return ids;
    }

    /**
     *
     * @param input
     * @param output
     */
    public static void copy(String input, String output) {
        FileSystem fs =getFs();
        Configuration conf = getConf();
        Path in= new Path(input);
        Path out= new Path(output);

        try{
            if(fs.exists(out)){//如果存在则删除
                fs.delete(out, true);
            }
//			fs.create(out);// 新建
            fs.mkdirs(out);
            FileStatus[] files=fs.listStatus(in);
            Path[] srcs= new Path[files.length];
            for(int i=0;i<files.length;i++){
                srcs[i]=files[i].getPath();
            }
            boolean flag =FileUtil.copy(fs,srcs,fs,out,false,true,conf);
            CommonUtils.simpleLog("数据从"+input.toString()+"传输到"+out.toString()+
                    (flag?"成功":"失败")+"!");
        }catch(Exception e){
            e.printStackTrace();
            CommonUtils.simpleLog("数据从"+input.toString()+"传输到"+out.toString()+
                    "失败"+"!");
        }
    }
    /**
     * 获得一个文件夹的信息
     * 用于比较前后两次文件夹的内容是否一致
     * @param folder
     * @param flag true则floder字符串包含fs信息，否则不包含
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static String getFolderInfo(String folder,String flag) throws FileNotFoundException, IOException{
        StringBuffer buff = new StringBuffer();
        FileSystem fs = getFs();
        Path path = getHDFSPath(folder, flag);

        FileStatus[] files = fs.listStatus(path);

        buff.append("contain files:"+files.length+"\t[");
        String filename="";
        for(FileStatus file:files){
            path = file.getPath();
            filename=path.toString();
            buff.append(filename.substring(filename.length()-1))
                    .append(":").append(file.getLen()).append(",");
        }
        filename=buff.substring(0, buff.length()-1);


        return filename+"]";
    }
    /**
     * 获得一个文件夹下面的文件个数
     * @param folder
     * @param flag true则floder字符串包含fs信息，否则不包含
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static  int getFolderFilesNum(String folder,String flag) throws FileNotFoundException, IOException{
        FileSystem fs = getFs();
        Path path = getHDFSPath(folder, flag);

        FileStatus[] files = fs.listStatus(path);
        return files.length;
    }

    /**
     *  提取Map中的center中心向量
     * @param vectorsMap
     * @return
     */
    public static double[][] getCenterVector(Map<Object, Object> vectorsMap) {
        double[][] centers = new double[vectorsMap.size()][];
        DoubleArrIntWritable value = null;
        int i=0;
        for(Object v:vectorsMap.values()){
            value =(DoubleArrIntWritable) v;
            centers[i++]=value.getDoubleArr();
        }

        return centers;
    }


    /**
     * 删除_center/iter_i (i>0)的所有文件夹
     * @param output
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static void clearCenter(String output) throws FileNotFoundException, IOException {

        FileStatus[] fss = getFs().listStatus(getHDFSPath(output, "false"));

        for(FileStatus f:fss){
            if(f.toString().contains("iter_0")){
                continue;
            }
            getFs().delete(f.getPath(), true);
            CommonUtils.simpleLog("删除文件"+f.getPath().toString()+"!");
        }
    }
}
