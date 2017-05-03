package com.yo.friendis.web.friend.model;

import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by Yo on 2017/4/5.
 */
@Table(name = "custom_user_group")
public class CustomUserGroup implements Serializable {
    private static final long serialVersionUID = -479907194324867252L;
    @Id
    @ColumnType(jdbcType = JdbcType.INTEGER)
    private String userGroupId;
    @ColumnType(jdbcType = JdbcType.INTEGER)
    private String customUserId;
    private String groupType;

    public CustomUserGroup() {

    }
    public CustomUserGroup(Integer userId,Integer group){
        this.customUserId = userId + "";
        this.groupType = group + "";
    }
    public CustomUserGroup(Integer id,Integer userId,Integer group){
        this.userGroupId = id + "";
        this.customUserId = userId + "";
        this.groupType = group + "";
    }
    public String getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(String userGroupId) {
        this.userGroupId = userGroupId;
    }

    public String getCustomUserId() {
        return customUserId;
    }

    public void setCustomUserId(String customUserId) {
        this.customUserId = customUserId;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }
}
