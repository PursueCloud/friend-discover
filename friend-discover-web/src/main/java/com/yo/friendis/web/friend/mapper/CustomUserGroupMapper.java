package com.yo.friendis.web.friend.mapper;

import com.yo.friendis.web.friend.model.CustomUserGroup;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * Created by Yo on 2017/4/5.
 */
public interface CustomUserGroupMapper extends Mapper<CustomUserGroup> {

    public void dropTable();

    public void createTable();

    public int deleteAll();

    public int selectCountByGroupType(@Param("groupType") int groupType);
    /**
     * 查询当前表的数量（如果表已存在，则返回1，否则返回0
     * @param dbName
     * @return
     */
    public int selectCountCurrTable(@Param("dbName")String dbName);

    public int addList(@Param("userGroupList") List<CustomUserGroup> userGroupList);
}
