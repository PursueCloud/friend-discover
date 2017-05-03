/**
 * 
 */
package com.yo.friendis.core.hadoop.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yo.friendis.common.common.util.DateUtils;
import com.yo.friendis.common.common.util.StringPool;
import com.yo.friendis.core.util.CommonUtils;
import org.apache.hadoop.mapred.JobStatus;
import org.apache.hadoop.mapred.RunningJob;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DecimalFormat;

/**
 * MR任务监控简要信息类
 * @author yo
 * @date 2017-3-28
 */
public class CurrentJobInfo implements Serializable,Comparable<CurrentJobInfo>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String jobId;
	private String jobName;
	private float mapProgress;
	private float redProgress;
	private String runState;
	private long startTime;
	private long finishTime;
	private String spendTime;
	@JsonFormat(pattern = StringPool.PATTERN_TIME, timezone = "GMT+8")
	private Timestamp startTimeTs;
	@JsonFormat(pattern = StringPool.PATTERN_TIME, timezone = "GMT+8")
	private Timestamp finishTimeTs;
	
	public CurrentJobInfo(RunningJob runningJob, long startTime, long finishTime, int runStateInt) throws IOException{
		this.jobId=runningJob.getID().toString();
		this.jobName=runningJob.getJobName();
		this.mapProgress=runningJob.mapProgress();
		this.redProgress=runningJob.reduceProgress();
		this.startTime=startTime;
		this.finishTime=finishTime;
		this.startTimeTs = new Timestamp(startTime);
		this.finishTimeTs = new Timestamp(finishTime);
		this.runState=JobStatus.getJobRunState(runStateInt);
		if("PREP".equals(this.runState)||"RUNNING".equals(this.runState)){
			this.runState=CommonUtils.getDotState(this.runState);
		}
	}
	public CurrentJobInfo(){
		this.jobId="null";
		this.jobName="null";
		this.mapProgress=0;
		this.redProgress=0;
		this.runState="null";
		this.startTime=System.currentTimeMillis();
		startTimeTs = new Timestamp(this.startTime);
	}
	@Override
	// 升序
	public int compareTo(CurrentJobInfo o) {
		if(this.startTime==o.startTime){
			return 0;
		}
		return this.startTime>o.startTime?1:-1;
	}

	public String getSpendTime() {
		return DateUtils.getSpendTimeByStartAndEnd(this.startTime, this.finishTime);
	}

	public void setSpendTime(String spendTime) {
		this.spendTime = spendTime;
	}

	public Timestamp getFinishTimeTs() {
		return finishTimeTs;
	}

	public void setFinishTimeTs(Timestamp finishTimeTs) {
		this.finishTimeTs = finishTimeTs;
	}

	public long getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(long finishTime) {
		this.finishTime = finishTime;
	}

	public String getJobId() {
		return jobId;
	}


	public void setJobId(String jobId) {
		this.jobId = jobId;
	}


	public String getJobName() {
		return jobName;
	}


	public void setJobName(String jobName) {
		this.jobName = jobName;
	}


	public String getMapProgress() {
		return Math.round(mapProgress*100) + "%";//精确到小数点后3位
	}


	public void setMapProgress(float mapProgress) {
		this.mapProgress = mapProgress;
	}


	public String getRedProgress() {
		return Math.round(redProgress*100) + "%";//精确到小数点后3位
	}


	public void setRedProgress(float redProgress) {
		this.redProgress = redProgress;
	}

/**
 * 
 * @return
 */
	public String getRunState() {
		
		return runState;
	}


	public void setRunState(String runState) {
		this.runState = runState;
	}


	public long getStartTime() {
		return startTime;
	}


	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public Timestamp getStartTimeTs() {
		return startTimeTs;
	}

	public void setStartTimeTs(Timestamp startTimeTs) {
		this.startTimeTs = startTimeTs;
	}

	@Override
	public String toString(){
		return "jobID:"+this.jobId+",jobName:"+this.jobName+",map:"+this.mapProgress+",reduce:"+
	this.redProgress+",state:"+this.runState;
	}
}
