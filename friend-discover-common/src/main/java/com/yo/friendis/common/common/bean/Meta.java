package com.yo.friendis.common.common.bean;

public class Meta {
	private boolean success;
	private String code;
	private String message;

	
	
	public Meta() {
		
	}

	public Meta(boolean success, String code, String message) {
		this.success = success;
		this.code = code;
		this.message = message;
	}

	/**
	 * @param sucess
	 * @param message
	 *            code或者message
	 */
	public Meta(boolean sucess, String message) {
		this(sucess, message, message);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
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
}
