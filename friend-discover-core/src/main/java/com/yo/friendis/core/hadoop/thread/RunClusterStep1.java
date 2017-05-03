/**
 * 
 */
package com.yo.friendis.core.hadoop.thread;

import com.yo.friendis.core.hadoop.mapred.fastcluster.DeltaDistanceJob;
import com.yo.friendis.core.hadoop.mapred.fastcluster.LocalDensityJob;
import com.yo.friendis.core.hadoop.mapred.fastcluster.SortJob;
import com.yo.friendis.core.hadoop.util.HadoopUtils;
import com.yo.friendis.core.util.CommonUtils;
import org.apache.hadoop.util.ToolRunner;

/**
 * 1. 寻找每个向量的局部密度
 * 2. 寻找每个向量的最远距离
 * @author yo
 * @date 2017-3-27
 */
public class RunClusterStep1 implements Runnable {

	private String input;
	private String numReducerDensity;
	private String numReducerDistance;
	private String numReducerSort;
	private String dc;
	private String method;
	
	public RunClusterStep1(String input, String dc, String method, String numReducerDensity,
					   String numReducerDistance, String numReducerSort){
		this.input=input;
		this.dc=dc;
		this.method=method;
		this.numReducerDensity=numReducerDensity;
		this.numReducerDistance=numReducerDistance;
		this.numReducerSort=numReducerSort;
	}
	@Override
	public void run() {
		String [] args =new String[]{
				HadoopUtils.getHDFSPath(input),
				HadoopUtils.getHDFSPath(HadoopUtils.LOCALDENSITYOUTPUT),
				dc,
				method,
				numReducerDensity
		};
		try {
			int ret=
			ToolRunner.run(HadoopUtils.getConf(), new LocalDensityJob(),args );
			if(ret!=0){
				CommonUtils.simpleLog("LocalDensityJob任务运行失败！");
				return ;
			}
			Thread.sleep(3000);// 等待3秒时间
			args=new String[]{
					HadoopUtils.getHDFSPath(input),// 使用距离计算后的路径作为输入
					HadoopUtils.getHDFSPath(HadoopUtils.DELTADISTANCEOUTPUT),
					HadoopUtils.getHDFSPath(HadoopUtils.LOCALDENSITYOUTPUT),
					numReducerDistance
			};
			
			ret=ToolRunner.run(HadoopUtils.getConf(), new DeltaDistanceJob(), args);
			if(ret!=0){
				CommonUtils.simpleLog("DeltaDistanceJob任务运行失败！");
				return ;
			}
			Thread.sleep(3000);// 等待3秒时间
			
			args=new String[]{
					HadoopUtils.getHDFSPath(HadoopUtils.DELTADISTANCEOUTPUT),
					HadoopUtils.getHDFSPath(HadoopUtils.SORTOUTPUT),
					numReducerSort
			};
			ret=ToolRunner.run(HadoopUtils.getConf(), new SortJob(), args);
			if(ret!=0){
				CommonUtils.simpleLog("SortJob任务运行失败！");
				return ;
			}
			
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

	public String getDc() {
		return dc;
	}
	public void setDc(String dc) {
		this.dc = dc;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getNumReducerDensity() {
		return numReducerDensity;
	}
	public void setNumReducerDensity(String numReducerDensity) {
		this.numReducerDensity = numReducerDensity;
	}
	public String getNumReducerDistance() {
		return numReducerDistance;
	}
	public void setNumReducerDistance(String numReducerDistance) {
		this.numReducerDistance = numReducerDistance;
	}
	/**
	 * @return the numReducerSort
	 */
	public String getNumReducerSort() {
		return numReducerSort;
	}
	/**
	 * @param numReducerSort the numReducerSort to set
	 */
	public void setNumReducerSort(String numReducerSort) {
		this.numReducerSort = numReducerSort;
	}

}
