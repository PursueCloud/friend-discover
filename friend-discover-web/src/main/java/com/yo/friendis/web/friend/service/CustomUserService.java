package com.yo.friendis.web.friend.service;

import com.github.pagehelper.PageHelper;
import com.yo.friendis.common.common.service.AbstractService;
import com.yo.friendis.common.common.util.DateUtils;
import com.yo.friendis.common.common.util.PageUtil;
import com.yo.friendis.common.common.util.PropertyMgr;
import com.yo.friendis.common.easyui.bean.Page;
import com.yo.friendis.core.util.CommonUtils;
import com.yo.friendis.web.friend.mapper.CustomUserMapper;
import com.yo.friendis.web.friend.model.CustomUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yo on 2017/4/7.
 */
@Service("custUserService")
public class CustomUserService extends AbstractService<CustomUser> {
    @Autowired
    private CustomUserMapper custUserMapper;
    @Autowired
    private CustomUserGroupService custUGServoce;
    private static Integer MAX_TOTAL = 100 * 10000;
    private static final String[] colsArray = new String[]{"昵称", "个人简介", "头像", "邮箱hash", "性别", "年龄",	"常驻地",
            "个人主页", "赞同数", "反对数", "声望值", "浏览数", "创建时间", "修改时间", "最后访问时间"};//CustomUser属性中文名数组
    private static final String[] fieldArray = new String[]{"displayName", "aboutMe", "icon", "emailHash", "sex", "age","location",
            "websiteUrl", "upVotes", "downVotes", "reputation", "views", "createDate", "modifyDate", "lastAccessDate"};//CustomUser属性名数组
    public static final String[] allCols = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14"};
    public List<CustomUser> selectAll() {
        return custUserMapper.selectAll();
    }
    public List<CustomUser> selectByIds(List<Integer> ids) {
        if( ids!=null && !ids.isEmpty() ) {
            return custUserMapper.selectByIds(ids);
        }
        return new ArrayList<CustomUser>();
    }

    public int deleteAll() {
        return custUserMapper.deleteAll();
    }

    public int deleteByIds(List<Integer> ids) {
        return custUserMapper.deleteByIds(ids);
    }

    /**
     * 删除表
     * @return
     */
    public void dropTable() {
        custUserMapper.dropTable();
    }

    /**
     * 创建表（如果表已存在，则先删除再创建
     */
    @Transactional(rollbackFor = Exception.class)//绑定事务
    public void createTable() {
        if( custUGServoce.selectExistsCurrTable() ) {//如果user_group表已存在，则先删除（因为存在外键依赖
            custUGServoce.dropTable();
        }
        if( selectExistsCurrTable() ) {//如果当前表已存在，则先删除
            custUserMapper.dropTable();
        }
        custUserMapper.createTable();
    }

    /**
     * 获取转换后的值
     * @param field
     * @param value
     * @return
     */
    private Object getTransferedValue(String field, String value) {
        if( StringUtils.isBlank(value) ) {
            return null;
        }
        Object res = new StringBuilder().append("%").append(value).append("%").toString();//如果搜索条件类型为String，则在对应的搜索内容里前后添加%，以便数据库使用like搜索
        if( "sex".equals(field) || "age".equals(field) || "upVotes".equals(field)//如果搜索条件类型为int，则将对应的搜索内容转换为int
                || "downVotes".equals(field) || "reputation".equals(field) || "views".equals(field)) {
            res = Integer.parseInt(value);
        }
        return res;
    }
    /**
     * 根据条件获取数据，返回list
     * @param searchConditionFields
     * @param searchConditionConts
     * @return
     */
    public List<CustomUser> getDataListByConditions(Page page, boolean needPagination, List<String> searchConditionFields, List<String> searchConditionConts,
                                                    String beginCreateDateStr, String endCreateDateStr,String beginModifyDateStr, String endModifyDateStr,
                                                    String beginLastAccessDateStr, String endLastAccessDateStr, Integer sex, Integer minAge, Integer maxAge, Integer minUpVotes, Integer maxUpVotes,
                                                    Integer minDownVotes, Integer maxDownVotes, Integer minReputation, Integer maxReputation, Integer minViews, Integer maxViews) throws NoSuchFieldException {
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        //构造map参数
        if( searchConditionFields!=null && !searchConditionFields.isEmpty()
                && searchConditionConts!=null && !searchConditionConts.isEmpty() ) {
            for(int i=0; i<searchConditionFields.size(); i++) {
                if( i < searchConditionConts.size() ) {
                    String field = searchConditionFields.get(i);
                    String value = searchConditionConts.get(i);
                    paramsMap.put(searchConditionFields.get(i), getTransferedValue(field, value));
                } else {//如果对应的搜索内容为空，则设置搜索内容为""
                    paramsMap.put(searchConditionFields.get(i), "");
                }
            }
        }
        //日期参数
        if (StringUtils.isNotBlank(beginCreateDateStr)) {
            Timestamp beginCreateDate = Timestamp.valueOf(beginCreateDateStr + " 00:00:00");
            paramsMap.put("beginCreateDate", beginCreateDate);
        }
        if (StringUtils.isNotBlank(endCreateDateStr)) {
            Timestamp endCreateDate = Timestamp.valueOf(endCreateDateStr + " 23:59:59");
            paramsMap.put("endCreateDate", endCreateDate);
        }
        if (StringUtils.isNotBlank(beginModifyDateStr)) {
            Timestamp beginModifyDate = Timestamp.valueOf(beginModifyDateStr + " 00:00:00");
            paramsMap.put("beginModifyDate", beginModifyDate);
        }
        if (StringUtils.isNotBlank(endModifyDateStr)) {
            Timestamp endModifyDate = Timestamp.valueOf(endModifyDateStr + " 23:59:59");
            paramsMap.put("endModifyDate", endModifyDate);
        }
        if (StringUtils.isNotBlank(beginLastAccessDateStr)) {
            Timestamp beginLastAccessDate = Timestamp.valueOf(beginLastAccessDateStr + " 00:00:00");
            paramsMap.put("beginLastAccessDate", beginLastAccessDate);
        }
        if (StringUtils.isNotBlank(endLastAccessDateStr)) {
            Timestamp endLastAccessDate = Timestamp.valueOf(endLastAccessDateStr + " 23:59:59");
            paramsMap.put("endLastAccessDate", endLastAccessDate);
        }
        //数字参数
        paramsMap.put("sex", sex);
        paramsMap.put("minAge", minAge);
        paramsMap.put("maxAge", maxAge);
        paramsMap.put("minUpVotes", minUpVotes);
        paramsMap.put("maxUpVotes", maxUpVotes);
        paramsMap.put("minDownVotes", minDownVotes);
        paramsMap.put("maxDownVotes", maxDownVotes);
        paramsMap.put("minReputation", minReputation);
        paramsMap.put("maxReputation", maxReputation);
        paramsMap.put("minViews", minViews);
        paramsMap.put("maxViews", maxViews);
        if( page != null ) {//分页
            if( needPagination ) {
                PageHelper.startPage(page.getPageNum(), page.getPageSize());
            }
            if( StringUtils.isNotBlank(page.getSort()) ) {
                boolean existsField = false;
                Field[] fields = CustomUser.class.getDeclaredFields();
                if( fields!=null && fields.length>0 ) {
                    for(Field field : fields) {
                        if( field.getName().equals(page.getSort()) ) {
                            existsField = true;
                            break;
                        }
                    }
                }
                if( existsField ) {
                    PageHelper.orderBy(PageUtil.getOrderCause(page.getSort() + "," + CustomUser.FIELD_USERID,
                            page.getOrder() + ",desc", CustomUser.class));
                }
            }
        }
        return custUserMapper.getDataListByConditions(paramsMap);
    }
    /**
     * 批量插入数据
     * @param list
     * @return
     */
    public int addList(List<CustomUser> list) {
        int res = 0;
        if( list!=null && list.size()>0 ) {
//            for(CustomUser obj : list) {
//                obj.setUserId("0");
//            }
            res += custUserMapper.addList(list);
        }
        return res;
    }
    public Map<String,Object> addData(String xmlDataFilePath) {
        Map<String,Object> resMap = new HashMap<String,Object>();
        try{
            //清空所有普通用户数据
//            custUserMapper.deleteAll();
            // ---解析不使用xml解析，直接使用定制解析即可
            List<String[]> stringArrList = CommonUtils.parseDat2StrArr(xmlDataFilePath);
            List<CustomUser> uds = new ArrayList<CustomUser>();
            for(String[] s : stringArrList){
                uds.add(new CustomUser(s));
            }
            int ret = addList(uds);
            logger.info("用户表批量插入了{}条记录!",ret);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            resMap.put("success", false);
            resMap.put("msg", e.getMessage());
            return resMap;
        }
        resMap.put("success", true);
        resMap.put("msg", "解析入库成功");
        return resMap;
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
        return custUserMapper.selectCountCurrTable(dbName) > 0;
    }
    /**
     * 根据用户的选择列动态获取表头名数组
     * @param cols
     * @return
     */
    public String[] getDynamicTitleArrays(List<String> cols) {
        String titles[] = null;
        if(cols != null && !cols.isEmpty()) {
            //根据用户的选择创建列名
            List<String> colsNames = new ArrayList<String>();
            for(String col : cols) {
                int index = Integer.parseInt(col);
                colsNames.add(colsArray[index]);
            }
            Object objTitles[] = colsNames.toArray();
            if(objTitles != null && objTitles.length > 0) {
                titles = new String[objTitles.length];
                int i = 0;
                for(Object obj : objTitles) {
                    String title = obj.toString();
                    titles[i] = title;
                    i++;
                }
            }
        }
        return titles;
    }

    /**
     * 设置导出的漏拦短信列表的列头信息
     * @param wb
     * @param sheet
     */
    public static void setExcelTableHeader(XSSFWorkbook wb, XSSFSheet sheet, String[] titles) {
        //创建列标头LIST
        List<String> titleList = new ArrayList<String>();
        for(String t : titles) {
            titleList.add(t);
        }
        // 计算该报表的列数
        int number = titleList.size();

        // 给工作表列定义列宽(实际应用自己更改列数)
        for (int i = 0; i < number; i++) {
            int width = 4000;
            sheet.setColumnWidth(i, width);
        }

        // 创建单元格样式
        XSSFCellStyle cellStyle = wb.createCellStyle();
        // 指定单元格居中对齐
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        // 指定单元格垂直居中对齐
        cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        // 指定当单元格内容显示不下时自动换行
        cellStyle.setWrapText(true);

        // 设置单元格字体
        XSSFFont font = wb.createFont();
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        font.setFontName("宋体");
        font.setFontHeight((short) 200);
        cellStyle.setFont(font);

        // 创建报表头部
//		createTitleRow(wb, sheet, "导出的关键字组记录表", number-1);

        XSSFRow row3 = sheet.createRow(0);
        // 设置行高
        row3.setHeight((short) 700);

        XSSFCell row3Cell = null;
        // 创建不同的LIST的列标题 ,并设置不同颜色
        for (int i = 0; i < number; i++) {
            XSSFCellStyle cellStyleT = wb.createCellStyle();
            // 指定单元格居中对齐
            cellStyleT.setAlignment(XSSFCellStyle.ALIGN_CENTER);
            // 指定单元格垂直居中对齐
            cellStyleT.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
//			if(i==0 || i == 4) {
//				cellStyle.setWrapText(true);
//			}
            cellStyle.setBorderRight(CellStyle.BORDER_THIN);
            // 指定当单元格内容显示不下时自动换行
//			cellStyleT.setWrapText(true);
            cellStyleT.setFont(font);

            row3Cell = row3.createCell(i);
            cellStyle = cellStyleT;
            cellStyle.setTopBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
            cellStyle.setBorderTop(CellStyle.BORDER_THIN);
            cellStyle.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
            cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
            cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
            if(i == 8) {
                cellStyle.setWrapText(true);
            }
            row3Cell.setCellStyle(cellStyle);
            row3Cell.setCellValue(new XSSFRichTextString(titleList.get(i).toString()));
        }
    }
    public XSSFWorkbook createDynamicXLSXExcelByData(List<CustomUser> data, List<String> cols) {
        XSSFWorkbook wb = new XSSFWorkbook();

        XSSFSheet sheet = wb.createSheet("导出的用户数据");
        if (data.isEmpty() || data.size() > MAX_TOTAL) {// 一百万条数据
            return wb;
        }

        String[] titles = this.getDynamicTitleArrays(cols);
        //创建表格头
        setExcelTableHeader(wb,  sheet, titles);

        // 设置单元格字体
        XSSFFont font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeight((short) 180);

        int rowSum = 1;//已创建的行数
        int maxColNum = cols.size();
        for(int t=0; t<data.size(); t++) {
            CustomUser custUser = data.get(t);
            //创建数据行
            Row row = sheet.createRow(rowSum);
            for (int j = 0; j < maxColNum; j++) {
                Cell cell = row.createCell(j);
                String colValue = " ";
                int colIndex = Integer.parseInt(cols.get(j));
                String field = fieldArray[colIndex];
                if(org.apache.commons.lang.StringUtils.isBlank(field)) {//如果当前获取到的属性名为空，则跳过当前循环
                    continue;
                }
                //通过放射获取对应属性的值
                String firstUpperMethodName = com.yo.friendis.common.common.util.StringUtils.firstCharToUpperCase(field);
                String getFieldMethodName = "get" + firstUpperMethodName;
                try {
                    Method method = custUser.getClass().getMethod(getFieldMethodName);
                    if(method == null) {
                        continue;
                    }
                    Object value = method.invoke(custUser);
                    if(value != null) {
                        if(value instanceof String) {
                            colValue = value.toString();
                        } else if(value instanceof Timestamp) {
                            Timestamp time = (Timestamp)value;
                            colValue = DateUtils.formatTime(time, "yyyy/MM/dd H:mm:ss");
                        } else if(value instanceof Integer) {
                            colValue = value.toString();
                        }
                    } else {
                        colValue = "";
                    }
                } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    logger.error("can not find method : " + getFieldMethodName + " in class " + custUser.getClass().getName());
                }
                //导出数据格式化
                if("sex".equals(field)) {
                    if("0".equals(colValue)) {
                        colValue = "男";
                    } else {
                        colValue = "女";
                    }
                }

                //温馨提示：cellValue的值不能为null，否则会保NullPointerException
                cell.setCellValue(colValue);
                // 创建单元格样式
                XSSFCellStyle cellStyle = wb.createCellStyle();
                cellStyle.setFont(font);
                // 创建单元格样式
                // 指定单元格居中对齐
                if("aboutMe".equals(field) || "displayName".equals(field)) {
                    cellStyle.setAlignment(XSSFCellStyle.ALIGN_LEFT);
                    cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_TOP);
                    cellStyle.setWrapText(true);
                } else {
                    cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
                    cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
                }
                // 指定单元格垂直居中对齐
                cell.setCellStyle(cellStyle);

            }
            rowSum ++;
            //设置行高
            row.setHeight((short) 300);
        }

        return wb;
    }

    /**
     * 根据
     * @param page
     * @param orginalUserId
     * @param searchConditionFields
     * @param searchConditionConts
     * @return
     */
    public List<CustomUser> selectRecUserDataByUserId(Page page, String orginalUserId, List<String> searchConditionFields, List<String> searchConditionConts) {
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("orginalUserId", orginalUserId);
        //构造map参数
        if( searchConditionFields!=null && !searchConditionFields.isEmpty()
                && searchConditionConts!=null && !searchConditionConts.isEmpty() ) {
            for(int i=0; i<searchConditionFields.size(); i++) {
                if( i < searchConditionConts.size() ) {
                    String field = searchConditionFields.get(i);
                    String value = searchConditionConts.get(i);
                    paramsMap.put(searchConditionFields.get(i), getTransferedValue(field, value));
                } else {//如果对应的搜索内容为空，则设置搜索内容为""
                    paramsMap.put(searchConditionFields.get(i), "");
                }
            }
        }
        if( page != null ) {//分页
            PageHelper.startPage(page.getPageNum(), page.getPageSize());
            if( StringUtils.isNotBlank(page.getSort()) ) {
                boolean existsField = false;
                Field[] fields = CustomUser.class.getDeclaredFields();
                if( fields!=null && fields.length>0 ) {
                    for(Field field : fields) {
                        if( field.getName().equals(page.getSort()) ) {
                            existsField = true;
                            break;
                        }
                    }
                }
                if( existsField ) {
                    PageHelper.orderBy(PageUtil.getOrderCause(page.getSort() + "," + CustomUser.FIELD_USERID,
                            page.getOrder() + ",desc", CustomUser.class));
                }
            }
        }
        return custUserMapper.selectRecUserDataByUserId(paramsMap);
    }

    /**
     * 查询年龄统计信息map（两列：age（年龄和cnt（该年龄用户数），当age为空时，age的值为'无'
     * @return
     */
    public List<Map<String, Object>> selectUserAgeCnt(){
        return custUserMapper.selectUserAgeCnt();
    }
}
