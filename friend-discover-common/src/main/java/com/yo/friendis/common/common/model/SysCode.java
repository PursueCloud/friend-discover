package com.yo.friendis.common.common.model;

import javax.persistence.Table;

@Table(name = "YO_CODE")
public class SysCode {
	private String codeType;
	private String name;
	private String value;

	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
