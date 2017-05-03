package com.yo.friendis.common.common.model;

import javax.persistence.Table;

@Table(name = "YO_CONFIG")
public class Config {
	public final static String FIELD_KEY = "key";
	public final static String FIELD_USER_ID = "userId";
	private String key, value;
	private String userId;

	public Config() {

	}

	public Config(String key, String userId) {
		this.key = key;
		this.userId = userId;
	}

	public Config(String key, String value, String userId) {
		this.key = key;
		this.value = value;
		this.userId = userId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "ConfigModel [key=" + key + ", value=" + value + ", userId=" + userId + "]";
	}

}
