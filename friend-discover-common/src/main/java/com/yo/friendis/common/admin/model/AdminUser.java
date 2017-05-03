package com.yo.friendis.common.admin.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.yo.friendis.common.common.util.StringPool;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

@Table(name = "admin_user")
public class AdminUser implements Serializable {
	private static final long serialVersionUID = -3781190399240349761L;
	//属性名常量
	public static String FIELD_USER_ID = "userId";
	public static String FIELD_ACCOUNT = "account";
	public static String FIELD_USER_NAME = "userName";
	public static String FIELD_PASSWORD = "password";
	public static String FIELD_STATUS = "status";
	public static String FIELD_CREATE_TIME = "createTime";
	public static String FIELD_MODIFY_TIME = "modifyTime";

	public interface WithoutPasswordView {
	};

	public interface WithPasswordView extends WithoutPasswordView {
	};

	/**
	 * 后台用户账户状态
	 */
	public enum UserStatus {
		DISABLE(0), ENABLE(1);

		public int value;
		private UserStatus(int value) {
			this.value = value;
		}
	}

	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY, generator="Mysql")//不主动生成主键值,让mysql的auto_increment自动填充
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private String userId;
	private String account;//账户名（用于查询
	private String userName;//用户名(用于显示
	private String password;
	private int status = UserStatus.ENABLE.value;//账户状态:0无效, 1有效
	@JsonFormat(pattern = StringPool.PATTERN_TIME, timezone = "GMT+8")
	private Timestamp createTime;
	@JsonFormat(pattern = StringPool.PATTERN_TIME, timezone = "GMT+8")
	private Timestamp modifyTime;


	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@JsonView(WithPasswordView.class)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@JsonView(WithoutPasswordView.class)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Timestamp modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Override
	public String toString() {
		return this.account;
	}
}
