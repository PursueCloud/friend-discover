package com.yo.friendis.common.admin.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yo.friendis.common.common.util.StringPool;
import org.apache.ibatis.type.JdbcType;

import tk.mybatis.mapper.annotation.ColumnType;

@Table(name = "admin_menu")
public class AdminMenu implements Serializable {
	private static final long serialVersionUID = 4656183980059253188L;
	//属性名常量
	public static final String FIELD_MENU_ID = "menuId";
	public static final String FIELD_MENU_NAME = "menuName";
	public static final String FIELD_P_MENU_ID = "pMenuId";
	public static final String FIELD_ICON = "icon";
	public static final String FIELD_URL = "url";
	public static final String FIELD_MENU_TYPE= "menuType";
	public static final String FIELD_MENU_ORDER = "menuOrder";
	public static final String FIELD_CREATE_TIME = "createTime";
	public static final String FIELD_MODIFY_TIME = "modifyTime";
	public static final String FIELD_DESCRIPTION = "description";
	public static final String ROOT_ID = "0";

	/**
	 * 菜单状态：CLOSED（0）关闭，OPEN（1）打开
	 */
	public enum MenuState {
		CLOSED(0), OPEN(1);

		public int value;
		private MenuState(int value) {
			this.value = value;
		}
	}
	/**
	 * 菜单类型：LEAF：页面，PARENT：目录
	 */
	public static enum MenuType{
		LEAF(0), PARENT(1);
		public int value;
		public String valStr;

		private MenuType(int value) {
			this.value = value;
			this.valStr = this.value + "";
		}
	}
	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY ,generator="Mysql")//不主动生成主键值,让mysql的auto_increment自动填充
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private String menuId;
	/**
	 * 菜单名称
	 */
	private String menuName;
	/**
	 * 父菜单id
	 */
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private String pMenuId = ROOT_ID;
	/**
	 * 菜单图标
	 */
	private String icon;
	/**
	 * 菜单请求路径(url)
	 */
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String url = "";
	/**
	 * 菜单类型，区分菜单和功能
	 */
	private int menuType = MenuType.LEAF.value;
	/**
	 * 菜单排序
	 */
	private int menuOrder = 0;
	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = StringPool.PATTERN_TIME, timezone = "GMT+8")
	private Timestamp createTime;
	/**
	 * 修改时间
	 */
	@JsonFormat(pattern = StringPool.PATTERN_TIME, timezone = "GMT+8")
	private Timestamp modifyTime;
	/**
	 * 菜单描述
	 */
	private String description;
	/**
	 * 菜单状态，区分本菜单当前状态，打开，选中等。
	 */
	@Transient
	private String state;
	/**
	 * 子菜单
	 */
	@Transient
	private List<AdminMenu> subMenu;


	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	public List<AdminMenu> getSubMenu() {
		return subMenu;
	}

	public void setSubMenu(List<AdminMenu> subMenu) {
		this.subMenu = subMenu;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getpMenuId() {
		return pMenuId;
	}

	public void setpMenuId(String pMenuId) {
		this.pMenuId = pMenuId;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getMenuType() {
		return menuType;
	}

	public void setMenuType(int menuType) {
		this.menuType = menuType;
	}

	public int getMenuOrder() {
		return menuOrder;
	}

	public void setMenuOrder(int menuOrder) {
		this.menuOrder = menuOrder;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
