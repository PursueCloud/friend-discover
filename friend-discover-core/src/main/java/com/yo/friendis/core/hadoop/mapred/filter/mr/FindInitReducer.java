/**
 * 
 */
package com.yo.friendis.core.hadoop.mapred.filter.mr;

import com.yo.friendis.core.hadoop.mapred.filter.FilterCounter;
import com.yo.friendis.core.hadoop.util.HadoopUtils;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * @author fansy
 * @date 2015-6-23
 */
public class FindInitReducer extends Reducer<DoubleWritable, NullWritable, DoubleWritable, NullWritable> {

	private long records = 0L;
	
	@Override
	public void reduce(DoubleWritable key, Iterable<NullWritable> values, Context cxt)throws InterruptedException,IOException{
		records++;
		cxt.getCounter(FilterCounter.REDUCE_COUNTER).increment(1);
		cxt.write(key, NullWritable.get());
	}
	
	@Override
	public void cleanup(Context cxt){
		System.out.println("reducer counters:"+records);
		cxt.getConfiguration().setLong(HadoopUtils.REDUCE_COUNTER2, records);
		cxt.getConfiguration().setLong(HadoopUtils.REDUCE_COUNTER, 
				cxt.getCounter(FilterCounter.REDUCE_COUNTER).getValue());
	}
}
