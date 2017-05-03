/**
 * 
 */
package com.yo.friendis.core.hadoop.mapred.filter;

import com.yo.friendis.core.hadoop.mapred.filter.keytype.IntPairWritable;
import com.yo.friendis.core.hadoop.mapred.filter.mr.CalDistanceMapper;
import com.yo.friendis.core.hadoop.mapred.filter.mr.CalDistanceReducer;
import com.yo.friendis.core.hadoop.util.HadoopUtils;
import com.yo.friendis.core.util.CommonUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;

/**
 * 计算记录两两之间的距离
 * map输出<DoubleWritable,IntPairWritable>
 * reduce 输出<doubleWritable,IntPairWritable>
 *             距离，<样本id，样本id>
 * 
 * @author fansy
 * @date 2015-6-25
 */
public class CalDistanceJob extends Configured implements Tool {

	@Override
	public int run(String[] args) throws Exception {
		Configuration conf = HadoopUtils.getConf();
	    String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
	    if (otherArgs.length !=2) {
	      System.err.println("Usage: com.fz.filter.CalDistanceJob <in> <out>");
	      System.exit(2);
	    }
	    conf.set("INPUT", otherArgs[0]);
	    Job job =  Job.getInstance(conf,"calculate vectors  from  input  :"+
	    		otherArgs[0]+" to "+otherArgs[1]);
	    job.setJarByClass(CalDistanceJob.class);
	    job.setMapperClass(CalDistanceMapper.class);
	    job.setReducerClass(CalDistanceReducer.class);
	    job.setNumReduceTasks(1);
	    
	    job.setMapOutputKeyClass(DoubleWritable.class	);
	    job.setMapOutputValueClass(IntPairWritable.class);
	    
	    job.setOutputKeyClass(DoubleWritable.class);
	    job.setOutputValueClass(IntPairWritable.class);
	    
	    job.setOutputFormatClass(SequenceFileOutputFormat.class);
	    job.setInputFormatClass(SequenceFileInputFormat.class);
	    SequenceFileInputFormat.addInputPath(job, new Path(otherArgs[0]));
	    SequenceFileOutputFormat.setOutputPath(job,new Path(otherArgs[1]));
	    FileSystem.get(conf).delete(new Path(otherArgs[1]), true);
	    int ret =job.waitForCompletion(true) ? 0 : 1;
	    long records=job.getCounters().findCounter(FilterCounter.REDUCE_COUNTER)
	    	    .getValue();
	    CommonUtils.simpleLog("距离计算后的总记录数："+records);
	    HadoopUtils.INPUT_RECORDS=records;
	    return ret;
	}
	
	// 命令行测试
	
	public static void main(String[] args) throws Exception {
		args = new String[]{"hdfs://10.14.1.50:9000/filterExps", "hdfs://10.14.1.50:9000/calcDistance-output"};
		Configuration conf = new Configuration();
	    String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
	    if (otherArgs.length !=2) {
	      System.err.println("Usage: com.fz.filter.CalDistanceJob <in> <out>");
	      System.exit(2);
	    }
	    conf.set("INPUT", otherArgs[0]);
	    Job job =  Job.getInstance(conf,"calculate vectors  from  input  :"+
	    		otherArgs[0]+" to "+otherArgs[1]);
	    job.setJarByClass(CalDistanceJob.class);
	    job.setMapperClass(CalDistanceMapper.class);
	    job.setReducerClass(CalDistanceReducer.class);
	    job.setNumReduceTasks(1);

	    job.setMapOutputKeyClass(DoubleWritable.class	);
	    job.setMapOutputValueClass(IntPairWritable.class);

	    job.setOutputKeyClass(DoubleWritable.class);
	    job.setOutputValueClass(IntPairWritable.class);

	    job.setOutputFormatClass(SequenceFileOutputFormat.class);
	    job.setInputFormatClass(SequenceFileInputFormat.class);
	    SequenceFileInputFormat.addInputPath(job, new Path(otherArgs[0]));
	    SequenceFileOutputFormat.setOutputPath(job,new Path(otherArgs[1]));
	    FileSystem.get(conf).delete(new Path(otherArgs[1]), true);
	    int ret =job.waitForCompletion(true) ? 0 : 1;
		System.out.println("ret:"+ret);
		long records=job.getCounters().findCounter(FilterCounter.REDUCE_COUNTER)
				.getValue();
		CommonUtils.simpleLog("距离计算后的总记录数："+records);
		HadoopUtils.INPUT_RECORDS=records;
	}

}