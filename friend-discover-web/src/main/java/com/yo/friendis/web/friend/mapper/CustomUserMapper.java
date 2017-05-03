package com.yo.friendis.web.friend.mapper;

import com.yo.friendis.web.friend.model.CustomUser;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * Created by Yo on 2017/4/5.
 */
public interface CustomUserMapper extends Mapper<CustomUser> {

    public void dropTable();

    public void createTable();

    public List<CustomUser> selectAll();

    public List<CustomUser> selectByIds(@Param("ids") List<Integer> ids);

    public int deleteAll();

    public int addList(@Param("userList") List<CustomUser> userList);

    public int deleteByIds(@Param("ids") List<Integer> ids);

    /**
     * 查询当前表的数量（如果表已存在，则返回1，否则返回0
     * @param dbName
     * @return
     */
    public int selectCountCurrTable(@Param("dbName")String dbName);

    /**
     * 根据条件获取数据，返回list
     * @param paramsMap (Map类型
     * @return
     */
    public List<CustomUser> getDataListByConditions(Map<String, Object> paramsMap);

    /**
     * 根据条件查询推荐用户数据集合
     * @param paramsMap
     * @return
     */
    public List<CustomUser> selectRecUserDataByUserId(Map<String, Object> paramsMap);

    public List<Map<String, Object>> selectUserAgeCnt();
}
