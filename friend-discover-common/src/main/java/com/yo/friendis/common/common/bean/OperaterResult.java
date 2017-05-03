package com.yo.friendis.common.common.bean;

/**
 * @author yhl 通用响应对象, 封装数据如下
 *
 *         {"meta":{success:true,"code":"业务编码，根据各个业务规定编码","message":"操作提示信息"},"data":{"id": "怎么也得有个独一无二的标识吧" ,"name":"谁都需要有个称号" ,"description":
 *         "这只是个描述而已" }}
 *
 * @param <T>
 */
public class OperaterResult<T> {
	private Meta meta;
	private T data;

	public OperaterResult() {

	}

	public OperaterResult(boolean success) {
		this(success, "");
	}

	@SuppressWarnings("unchecked")
	public OperaterResult(boolean success, String message) {
		this.meta = new Meta(success, message);
		this.data = (T) "";
	}

	public OperaterResult(boolean success, String message, T data) {
		this(success, message);
		this.data = data;
	}

	public Meta getMeta() {
		return meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
