package com.yo.friendis.web.friend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yo.friendis.common.common.util.StringPool;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yo on 2017/4/5.
 */
@Table(name = "custom_user")
public class CustomUser implements Serializable{
    private static final long serialVersionUID = 4191864383893504136L;
    public static final String FIELD_USERID = "userId";
    public static final String FIELD_DISPLAYNAME = "displayName";
    public static final String FIELD_ICON = "icon";
    public static final String FIELD_ABOUTME = "aboutMe";
    public static final String FIELD_SEX = "sex";
    public static final String FIELD_AGE = "age";
    public static final String FIELD_EMAILHASH = "emailHash";
    public static final String FIELD_LOCATION = "location";
    public static final String FIELD_UPVOTES = "upVotes";
    public static final String FIELD_DOWNVOTES = "downVotes";
    public static final String FIELD_REPUTATION = "reputation";
    public static final String FIELD_VIEWS = "views";
    public static final String FIELD_WEBSITEURL = "websiteUrl";
    public static final String FIELD_CREATEDATE = "createDate";
    public static final String FIELD_MODIFYDATE = "modifyDate";
    public static final String FIELD_LASTACCESSDATE = "lastAccessDate";
    @Id
    @ColumnType(jdbcType = JdbcType.INTEGER)
    private String userId;
    private String displayName;
    private String icon;
    private String aboutMe;
    private Integer sex;
    private Integer age;
    private String emailHash;
    private String location;
    private Integer upVotes;
    private Integer downVotes;
    private Integer reputation;
    private Integer views;
    private String websiteUrl;
    @JsonFormat(pattern = StringPool.PATTERN_TIME, timezone = "GMT+8")
    private Timestamp createDate;
    @JsonFormat(pattern = StringPool.PATTERN_TIME, timezone = "GMT+8")
    private Timestamp modifyDate;
    @JsonFormat(pattern = StringPool.PATTERN_TIME, timezone = "GMT+8")
    private Timestamp lastAccessDate;

    public CustomUser(){	}

    public CustomUser(String[] s) throws ParseException {
        if( s[1] != null ){
            this.reputation = Integer.parseInt(s[1]);
        }
        if( s[2] != null ) {
            this.createDate = Timestamp.valueOf(s[2].replaceAll("T", " "));
        }
        if( s[3] != null ) {
            this.displayName = s[3];
        }
        if( s[4] != null ) {
            this.emailHash = s[4];
        }
        if( s[5] != null ) {
            this.lastAccessDate = Timestamp.valueOf(s[5].replaceAll("T", " "));
        }
        if( s[6] != null ) {
            this.location = s[6];
        }
        if( s[7] != null ){
            this.age=Integer.parseInt(s[7]);
        }
        if( s[8] != null ) {
            this.aboutMe = s[8];
        }
        if( s[9] != null ){
            this.views=Integer.parseInt(s[9]);
        }
        if( s[10] != null ){
            this.upVotes=Integer.parseInt(s[10]);
        }
        if( s[11] != null ){
            this.downVotes=Integer.parseInt(s[11]);
        }

    }

    /**
     * 获取搜索属性名list
     * @return
     */
    public static List<Map<String, String>> getSearchFields() {
        List<Map<String, String>> searchFields = new ArrayList<Map<String, String>>();
        Map<String, String> fieldMap1 = new HashMap<String, String>();
        fieldMap1.put("fieldName", FIELD_DISPLAYNAME);
        fieldMap1.put("fieldText", "昵称");
        searchFields.add(fieldMap1);
        Map<String, String> fieldMap2 = new HashMap<String, String>();
        fieldMap2.put("fieldName", FIELD_ABOUTME);
        fieldMap2.put("fieldText", "个人简介");
        searchFields.add(fieldMap2);
        Map<String, String> fieldMap3 = new HashMap<String, String>();
        fieldMap3.put("fieldName", FIELD_ICON);
        fieldMap3.put("fieldText", "头像");
        searchFields.add(fieldMap3);
        Map<String, String> fieldMap6 = new HashMap<String, String>();
        fieldMap6.put("fieldName", FIELD_EMAILHASH);
        fieldMap6.put("fieldText", "邮箱hash");
        searchFields.add(fieldMap6);
        Map<String, String> fieldMap7 = new HashMap<String, String>();
        fieldMap7.put("fieldName", FIELD_LOCATION);
        fieldMap7.put("fieldText", "常驻地");
        searchFields.add(fieldMap7);
        Map<String, String> fieldMap12 = new HashMap<String, String>();
        fieldMap12.put("fieldName", FIELD_WEBSITEURL);
        fieldMap12.put("fieldText", "个人主页");
        searchFields.add(fieldMap12);
        //返回
        return searchFields;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmailHash() {
        return emailHash;
    }

    public void setEmailHash(String emailHash) {
        this.emailHash = emailHash;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getUpVotes() {
        return upVotes;
    }

    public void setUpVotes(Integer upVotes) {
        this.upVotes = upVotes;
    }

    public Integer getDownVotes() {
        return downVotes;
    }

    public void setDownVotes(Integer downVotes) {
        this.downVotes = downVotes;
    }

    public Integer getReputation() {
        return reputation;
    }

    public void setReputation(Integer reputation) {
        this.reputation = reputation;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
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

    public Timestamp getLastAccessDate() {
        return lastAccessDate;
    }

    public void setLastAccessDate(Timestamp lastAccessDate) {
        this.lastAccessDate = lastAccessDate;
    }
}
