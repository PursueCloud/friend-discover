package com.yo.friendis.core.hadoop.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yo.friendis.common.common.util.DateUtils;
import com.yo.friendis.common.common.util.StringPool;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yo on 2017/4/5.
 */
@Table(name = "hadoop_cfg_const")
public class HadoopCfgConst implements Serializable {
    private static final long serialVersionUID = -9006318982295215739L;
    public static String FIELD_CONSTID = "constId";
    public static String FIELD_CONSTKEY = "constKey";
    public static String FIELD_CONSTVALUE = "constValue";
    public static String FIELD_DESCRIPTION = "description";
    public static String FIELD_CREATEDATE = "createDate";
    public static String FIELD_MODIFYDATE = "modifyDate";

    @Id
    @ColumnType(jdbcType = JdbcType.INTEGER)
    private String constId;
    private String constKey;
    private String constValue;
    @JsonFormat(pattern = StringPool.PATTERN_TIME, timezone = "GMT+8")
    private Timestamp createDate;
    @JsonFormat(pattern = StringPool.PATTERN_TIME, timezone = "GMT+8")
    private Timestamp modifyDate;
    private String description;

    public HadoopCfgConst() {}

    public HadoopCfgConst(String constKey, String constValue, String description) {
        this.constId = "0";//设置id为0，让auto_increment生效
        this.constKey = constKey;
        this.constValue = constValue;
        this.description = description;
        this.createDate = DateUtils.getCurrentTimestamp();
    }

    /**
     * 获取搜索属性名list
     * @return
     */
    public static List<Map<String, String>> getSearchFields() {
        List<Map<String, String>> searchFields = new ArrayList<Map<String, String>>();
        Map<String, String> fieldMap1 = new HashMap<String, String>();
        fieldMap1.put("fieldName", FIELD_CONSTKEY);
        fieldMap1.put("fieldText", "配置项名");
        searchFields.add(fieldMap1);
        Map<String, String> fieldMap2 = new HashMap<String, String>();
        fieldMap2.put("fieldName", FIELD_CONSTVALUE);
        fieldMap2.put("fieldText", "配置项值");
        searchFields.add(fieldMap2);
        Map<String, String> fieldMap3 = new HashMap<String, String>();
        fieldMap3.put("fieldName", FIELD_DESCRIPTION);
        fieldMap3.put("fieldText", "备注说明");
        searchFields.add(fieldMap3);
        //返回
        return searchFields;
    }

    public String getConstId() {
        return constId;
    }

    public void setConstId(String constId) {
        this.constId = constId;
    }

    public String getConstKey() {
        return constKey;
    }

    public void setConstKey(String constKey) {
        this.constKey = constKey;
    }

    public String getConstValue() {
        return constValue;
    }

    public void setConstValue(String constValue) {
        this.constValue = constValue;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Timestamp getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
