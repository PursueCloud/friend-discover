package com.yo.friendis.common.common.bean;

import com.yo.friendis.common.common.bean.Meta;
import com.yo.friendis.common.common.bean.OperaterResult;

public class OperaterException extends RuntimeException{

	private static final long serialVersionUID = -8740998414784230616L;

	private boolean success;
	
	private String code;
	
	private String message;

	public OperaterException() {
		
	}

	public OperaterException(boolean success, String code, String message) {
		super(message);
		this.success = success;
		this.code = code;
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	

	/**
	 * 转换成OperaterResult
	 * 
	 * @return
	 */
	public OperaterResult<Object> parseToOperaterResult(){
		OperaterResult<Object> result = new OperaterResult<Object>();
		result.setMeta(new Meta(success, code, message));
		return result;
	}
	
	
}
