/**
 * 
 */
package com.yo.friendis.core.hadoop.mapred.fastcluster.mr;

import com.yo.friendis.core.util.CommonUtils;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;


/**
 * filter the same point vector 
 * @author fansy
 * @date 2015-6-1
 */
public class LocalDensityReducer extends
		Reducer<IntWritable, DoubleWritable, IntWritable, DoubleWritable> {
	private DoubleWritable sumAll = new DoubleWritable();
	
	@Override
	public void reduce(IntWritable key, Iterable<DoubleWritable> values,Context cxt)
	throws IOException,InterruptedException{
		double sum =0;
		for(DoubleWritable v:values){
			sum+=v.get();
		}
		sumAll.set(sum);// 
		cxt.write(key, sumAll);
		CommonUtils.simpleLog("vectorI:"+key.get()+",density:"+sumAll);
	}
}
