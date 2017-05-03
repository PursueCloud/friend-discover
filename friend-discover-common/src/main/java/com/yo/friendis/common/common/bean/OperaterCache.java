package com.yo.friendis.common.common.bean;

import java.io.Serializable;
import java.util.Date;

public class OperaterCache<T> implements Serializable {
	public OperaterCache() {

	}

	public static class Type {
		public final static String PROCESS = "process";
		public final static String ERROR = "error";
		public final static String COMPLETE = "complete";
	}

	public static class Error implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -5247801212019734562L;
		private String message;
		private Date updateTime;

		public Error(String message) {
			this.message = message;
			this.updateTime = new Date();
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public Date getUpdateTime() {
			return updateTime;
		}

		public void setUpdateTime(Date updateTime) {
			this.updateTime = updateTime;
		}

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -2617471647543906904L;
	private String type;
	private T data;

	public OperaterCache(String type, T data) {
		this.type = type;
		this.data = data;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
