package com.yo.friendis.web.friend.service;

import com.yo.friendis.common.common.service.AbstractService;
import com.yo.friendis.common.common.util.PropertyMgr;
import com.yo.friendis.core.util.CommonUtils;
import com.yo.friendis.web.friend.mapper.CustomUserGroupMapper;
import com.yo.friendis.web.friend.model.CustomUserGroup;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yo on 2017/4/7.
 */
@Service("custUGService")
public class CustomUserGroupService extends AbstractService<CustomUserGroup> {
    @Autowired
    private CustomUserGroupMapper custUGMapper;

    /**
     * 删除表
     * @return
     */
    public void dropTable() {
        custUGMapper.dropTable();
    }
    /**
     * 创建表（如果表已存在，则先删除再创建
     */
    @Transactional(rollbackFor = Exception.class)//绑定事务
    public void createTable() {
        if( selectExistsCurrTable() ) {//如果表已存在，则先删除
            custUGMapper.dropTable();
        }
        custUGMapper.createTable();
    }
    /**
     * 清空所有数据
     * @return
     */
    public int deleteAll() {
        return custUGMapper.deleteAll();
    }

    /**
     * 批量插入数据
     * @param list
     * @return
     */
    public int addList(List<CustomUserGroup> list) {
        if( list!=null && list.size()>0 ) {
//            for(CustomUserGroup obj : list) {
//                obj.setUserGroupId("0");
//                custUGMapper.insertSelective(obj);
//            }
            return custUGMapper.addList(list);
        }
        return 0;
    }

    /**
     * 根据groupType计算CustomUserGroup记录数
     * @param groupType
     * @return
     */
    public int selectCountByGroupType(int groupType) {
        return custUGMapper.selectCountByGroupType(groupType);
    }
    /**
     * 获取分类数据占比
     * @param k
     * @return
     */
    public List<String> getPercent(int k) {
        double[] percents= new double[k];
        double sum=0;
        for(int i=0;i<k;i++){
            percents[i] = custUGMapper.selectCountByGroupType(i+1);
            sum += percents[i];
        }
        List<String> list = new ArrayList<String>();
        for(int i=0;i<k;i++){
            list.add(CommonUtils.object2Percent(percents[i]/sum, 2));// 保留两位小数
        }
        return list;
    }


    /**
     * 查询数据库中当前表是否存在
     * @return
     */
    public boolean selectExistsCurrTable() {
        String dbName = PropertyMgr.getPropertyByKey("db-config.properties", "db.dbName");
        if(StringUtils.isBlank(dbName) ) {
            logger.error("类：" + this.getClass().getSimpleName() + ",方法：selectExistsCurrTable，参数dbName不能为空");
            return false;
        }
        return custUGMapper.selectCountCurrTable(dbName) > 0;
    }
}
