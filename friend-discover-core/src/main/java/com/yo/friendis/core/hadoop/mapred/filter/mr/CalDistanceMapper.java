/**
 * 
 */
package com.yo.friendis.core.hadoop.mapred.filter.mr;

import com.yo.friendis.core.hadoop.mapred.filter.FilterCounter;
import com.yo.friendis.core.hadoop.mapred.filter.keytype.DoubleArrIntWritable;
import com.yo.friendis.core.hadoop.mapred.filter.keytype.IntPairWritable;
import com.yo.friendis.core.hadoop.util.HadoopUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile.Reader;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author fansy
 * @date 2015-6-25
 */
public class CalDistanceMapper extends Mapper<IntWritable,DoubleArrIntWritable, DoubleWritable, IntPairWritable> {
	private Logger log = LoggerFactory.getLogger(CalDistanceMapper.class);
	private Path input;
	private DoubleWritable newKey= new DoubleWritable();
	private IntPairWritable newValue= new IntPairWritable();
	
	@Override 
	public void setup(Context cxt){
		input=new Path(cxt.getConfiguration().get("INPUT"));// 
	}
	
	@Override
	public void map(IntWritable key,DoubleArrIntWritable  value,Context cxt)throws InterruptedException,IOException{
		cxt.getCounter(FilterCounter.MAP_COUNTER).increment(1L);
		if(cxt.getCounter(FilterCounter.MAP_COUNTER).getValue()%3000==0){
			log.info("Map处理了{}条记录...",cxt.getCounter(FilterCounter.MAP_COUNTER).getValue());
			log.info("Map生成了{}条记录...",cxt.getCounter(FilterCounter.MAP_OUT_COUNTER).getValue());
		}
		Configuration conf = cxt.getConfiguration();
		Reader reader = null;
		FileStatus[] fss=input.getFileSystem(conf).listStatus(input);
		for(FileStatus f:fss){
			if(!f.toString().contains("part")){
				continue; // 排除其他文件
			}
			try {
				reader = new Reader(conf, Reader.file(f.getPath()),
						Reader.bufferSize(4096), Reader.start(0));
				IntWritable dKey = (IntWritable) ReflectionUtils.newInstance(
						reader.getKeyClass(), conf);
				DoubleArrIntWritable dVal = (DoubleArrIntWritable) ReflectionUtils.newInstance(
						reader.getValueClass(), conf);
	
				while (reader.next(dKey, dVal)) {// 循环读取文件
					// 当前IntWritable需要小于给定的dKey
					if(key.get()<dKey.get()){
						cxt.getCounter(FilterCounter.MAP_OUT_COUNTER).increment(1L);
						double dis= HadoopUtils.getDistance(value.getDoubleArr(), dVal.getDoubleArr());
						newKey.set(dis);
						newValue.setValue(key.get(), dKey.get());
						cxt.write(newKey, newValue);
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				IOUtils.closeStream(reader);
			}
		}
	}
	
}
