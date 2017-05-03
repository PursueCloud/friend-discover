/**
 * 
 */
package com.yo.friendis.core.hadoop.thread;

import com.yo.friendis.core.hadoop.mapred.filter.DeduplicateJob;
import com.yo.friendis.core.hadoop.util.HadoopUtils;
import org.apache.hadoop.util.ToolRunner;

/**
 * @author yo
 * @date 2017-3-23
 */
public class Deduplicate implements Runnable {

	private String input;
	private String output;
	
	public Deduplicate(String input,String output){
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
			ToolRunner.run(HadoopUtils.getConf(), new DeduplicateJob(), args );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
