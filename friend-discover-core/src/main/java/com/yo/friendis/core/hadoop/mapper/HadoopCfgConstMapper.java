package com.yo.friendis.core.hadoop.mapper;

import com.yo.friendis.core.hadoop.model.HadoopCfgConst;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * Created by Yo on 2017/4/5.
 */
public interface HadoopCfgConstMapper extends Mapper<HadoopCfgConst> {
    public List<HadoopCfgConst> selectByIds(@Param("ids") List<Integer> ids);

    public int deleteAll();

    public int deleteByIds(@Param("ids") List<Integer> ids);

    public void dropTable();

    public void createTable();
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
    public List<HadoopCfgConst> getDataListByConditions(Map<String, Object> paramsMap);
}
