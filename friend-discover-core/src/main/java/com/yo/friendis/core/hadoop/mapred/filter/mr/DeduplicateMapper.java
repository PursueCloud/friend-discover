/**
 * 
 */
package com.yo.friendis.core.hadoop.mapred.filter.mr;

import com.yo.friendis.core.util.CommonUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * 输出emailHash 和原数据
 * @author fansy
 * @date 2015-6-23
 */
public class DeduplicateMapper extends Mapper<LongWritable, Text, Text, Text> {
	private Text emailHashKey = new Text();
	private String keyAttr="EmailHash";
	
	public void map(LongWritable key, Text value, Context cxt)throws InterruptedException,IOException{
		// 去掉非数据行
		if(!value.toString().trim().startsWith("<row")){
			return ;
		}
		String emailHash = CommonUtils.getAttrValInLine(value.toString(),keyAttr);
		emailHashKey.set(emailHash);
		cxt.write(emailHashKey, value);
	}
}
