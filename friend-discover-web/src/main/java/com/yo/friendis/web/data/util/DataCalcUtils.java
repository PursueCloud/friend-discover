package com.yo.friendis.web.data.util;

import com.yo.friendis.core.hadoop.mapred.filter.keytype.DoubleArrIntWritable;
import com.yo.friendis.core.hadoop.util.HadoopUtils;
import com.yo.friendis.core.util.CommonUtils;
import com.yo.friendis.web.friend.model.CustomUser;
import com.yo.friendis.web.friend.model.CustomUserGroup;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.util.ReflectionUtils;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Yo on 2017/4/17.
 */
public class DataCalcUtils {
    public static final String FILTER = "/user/root/_filter";
    public static final int FILTER_PREPAREVECTORS_FILES = 4;// 由数据库到hdfs产生的文件数
    public static final String FILTER_PREPAREVECTORS = FILTER + "/"
            + "preparevectors";// 准备距离向量
    public static final String LOCALCENTERFILE="WEB-INF/classes/centervector.dat";// 本地中心点文件
    public static final String FIRSTCENTERPATH = "/user/root/_center/iter_0/clustered/part-m-00000";
    /**
     * List  解析入HDFS
     *
     * @param list
     * @param fileNums
     * @throws IOException
     */
    public static void db2hdfs(List<CustomUser> list,String url, int fileNums) throws IOException {
//		int everyFileNum=list.size()/HadoopUtils.FILTER_PREPAREVECTORS_FILES;
        if(fileNums<=0||fileNums>9){
            fileNums=FILTER_PREPAREVECTORS_FILES;
        }
        int everyFileNum=(int)Math.ceil((double)list.size()/fileNums);
        Path path=null;
        int start=0;
        int end=start+everyFileNum;
        for(int i=0;i<fileNums;i++){
            // 如果url为空，那么使用默认的即可，否则使用提供的路径
            path= new Path(url==null?FILTER_PREPAREVECTORS:url+"/part-r-0000"+i);
            if(end>list.size()){
                end=list.size();
            }
            try{
                db2hdfs(list.subList(start, end),path);
                start=end;
                end+=everyFileNum;
            }catch(IOException e){
                throw e;
            }
        }

        CommonUtils.simpleLog("db2HDFS 全部解析上传完成！");
    }
    private static boolean db2hdfs(List<CustomUser> list, Path path) throws IOException {
        boolean flag =false;
        int recordNum=0;
        SequenceFile.Writer writer = null;
        Configuration conf = HadoopUtils.getConf();
        try {
            SequenceFile.Writer.Option optPath = SequenceFile.Writer.file(path);
            SequenceFile.Writer.Option optKey = SequenceFile.Writer
                    .keyClass(IntWritable.class);
            SequenceFile.Writer.Option optVal = SequenceFile.Writer.valueClass(DoubleArrIntWritable.class);
            writer = SequenceFile.createWriter(conf, optPath, optKey, optVal);
            DoubleArrIntWritable dVal = new DoubleArrIntWritable();
            IntWritable dKey = new IntWritable();
            for (CustomUser user : list) {
                if(!checkUser(user)){
                    continue; // 不符合规则
                }
                dVal.setValue(getDoubleArr(user),-1);
                dKey.set(getIntVal(user));
                writer.append(dKey, dVal);// 用户id,<type,用户的有效向量 >// 后面执行分类的时候需要统一格式，所以这里需要反过来
                recordNum++;
            }
        } catch (IOException e) {
            CommonUtils.simpleLog("db2HDFS失败,+hdfs file:"+path.toString());
            e.printStackTrace();
            flag =false;
            throw e;
        } finally {
            IOUtils.closeStream(writer);
        }
        flag=true;
        CommonUtils.simpleLog("db2HDFS 完成,hdfs file:"+path.toString()+",records:"+recordNum);
        return flag;
    }
    /**
     * 每次写入聚类中心之前，需要把前一次的结果删掉，防止重复,不应该在这里删除，应该在执行分类的时候删除
     * 根据给定的users提取出来每个聚类中心，并把其写入hdfs
     * key,value--> <IntWritable ,DoubleArrIntWritable> --> <id,<type,用户有效向量>>
     * 同时把聚类中心写入本地文件
     * @param localfile
     * @param users
     * @param output
     * @throws IOException
     */
    public static void writecenter2hdfs(List<CustomUser> users, String localfile, String output) throws IOException {
        localfile=localfile==null?LOCALCENTERFILE:localfile;
        localfile= CommonUtils.getRootPathBasedPath(localfile);
        output=output==null?FIRSTCENTERPATH:output;


        // 写入hdfs
        SequenceFile.Writer writer = null;
        Configuration conf = HadoopUtils.getConf();
        try {
            SequenceFile.Writer.Option optPath = SequenceFile.Writer.file(HadoopUtils.getHDFSPath(output, "false"));
            SequenceFile.Writer.Option optVal = SequenceFile.Writer
                    .valueClass(DoubleArrIntWritable.class);
            SequenceFile.Writer.Option optKey = SequenceFile.Writer.keyClass(IntWritable.class);
            writer = SequenceFile.createWriter(conf, optPath, optKey, optVal);
            DoubleArrIntWritable dVal = new DoubleArrIntWritable();
            IntWritable dKey = new IntWritable();
            int k=1;
            for (CustomUser user : users) {

                dVal.setValue(getDoubleArr(user),k++);
//				dVal.setIdentifier(k++);
                dKey.set(getIntVal(user));//
                writer.append(dKey, dVal);// 用户id,<type,用户的有效向量>
            }
        } catch (IOException e) {
            CommonUtils.simpleLog("writecenter2hdfs 失败,+hdfs file:"+output.toString());
            e.printStackTrace();
            throw e;
        } finally {
            IOUtils.closeStream(writer);
        }
        CommonUtils.simpleLog("聚类中心向量已经写入HDFS："+output.toString());
// 写入本地文件
        FileWriter writer2 = null;
        BufferedWriter bw = null;
        try {
            writer2 = new FileWriter(localfile);
            bw = new BufferedWriter(writer2);
            for(CustomUser user:users){
                bw.write("id:"+user.getUserId()+"\tvector:["+
                        HadoopUtils.doubleArr2Str(getDoubleArr(user))+"]");
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                bw.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        CommonUtils.simpleLog("聚类中心向量已经写入HDFS："+localfile.toString());

    }
    /**
     * 解析input里面的clustered里面的分类数据到List中
     * @param input
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */

    public static List<CustomUserGroup> resolve(String input) throws FileNotFoundException, IOException{
        List<CustomUserGroup> list = new ArrayList<CustomUserGroup>();

        Path path = HadoopUtils.getHDFSPath(input, "false");
        FileSystem fs = HadoopUtils.getFs();

        RemoteIterator<LocatedFileStatus> files =fs.listFiles(path, true);

        while(files.hasNext()){
            LocatedFileStatus lfs = files.next();

            input = lfs.getPath().toString();
            if(input.contains("/clustered/part")&&!input.contains("iter_0")){// 不包含iter_0目录
                path= lfs.getPath();
                if(lfs.getLen()>0){
                    list.addAll(resolve(path));
                }
            }
        }
        CommonUtils.simpleLog("一共读取了"+list.size()+"条记录！");
        return list;
    }
    /**
     * 把分类的数据解析到list里面
     * @param path
     * @return
     */
    private static Collection<CustomUserGroup> resolve(Path path) {
        // TODO Auto-generated method stub
        List<CustomUserGroup> list = new ArrayList<CustomUserGroup>();
        Configuration conf = HadoopUtils.getConf();
        SequenceFile.Reader reader = null;
        int i=0;
        try {
            reader = new SequenceFile.Reader(conf, SequenceFile.Reader.file(path),
                    SequenceFile.Reader.bufferSize(4096), SequenceFile.Reader.start(0));
            IntWritable dkey =  (IntWritable) ReflectionUtils
                    .newInstance(reader.getKeyClass(), conf);
            DoubleArrIntWritable dvalue =  (DoubleArrIntWritable) ReflectionUtils
                    .newInstance(reader.getValueClass(), conf);

            while (reader.next(dkey, dvalue)) {// 循环读取文件
                // 使用这个进行克隆
                list.add(new CustomUserGroup(i++, dkey.get(), dvalue.getIdentifier()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeStream(reader);
        }
        CommonUtils.simpleLog("读取"+list.size()+"条记录，文件："+path.toString());
        return list;
    }

    /**
     * 检查用户是否符合规则
     * 规则 ：reputation>15,upVotes>0,downVotes>0,views>0的用户
     * @param user
     * @return
     */
    private static boolean checkUser(CustomUser user) {
        CustomUser ud = user;
        if(ud.getReputation()<=15) return false;
        if(ud.getUpVotes()<=0) return false;
        if(ud.getDownVotes()<=0) return false;
        if(ud.getViews()<=0) return false;
        return true;
    }

    /**
     * @param user
     * @return
     */
    private static int getIntVal(CustomUser user) {
        String id = user.getUserId();
        return Integer.parseInt(id);
    }

    /**
     * @param user
     *            reputations,upVotes,downVotes,views
     * @return
     */
    public static double[] getDoubleArr(CustomUser user) {
        double[] arr = new double[4];
        CustomUser tUser = user;
        arr[0] = tUser.getReputation();
        arr[1] = tUser.getUpVotes();
        arr[2] = tUser.getDownVotes();
        arr[3] = tUser.getViews();
        return arr;
    }
}
