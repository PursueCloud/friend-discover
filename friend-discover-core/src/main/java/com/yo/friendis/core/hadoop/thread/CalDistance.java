/**
 * 
 */
package com.yo.friendis.core.hadoop.thread;

import com.yo.friendis.core.hadoop.mapred.filter.CalDistanceJob;
import com.yo.friendis.core.hadoop.util.HadoopUtils;
import org.apache.hadoop.util.ToolRunner;

/**
 * @author fansy
 * @date 2015-7-2
 */
public class CalDistance implements Runnable {

	private String input;
	private String output;
	
	public CalDistance(String input,String output){
		this.input=input;
		this.output=output;
	}
	
	@Override
	public void run() {
		String [] args ={
				HadoopUtils.getHDFSPath(input),
				HadoopUtils.getHDFSPath(output)
		};
		try {
			ToolRunner.run(HadoopUtils.getConf(), new CalDistanceJob(),args );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}
	
	

}
