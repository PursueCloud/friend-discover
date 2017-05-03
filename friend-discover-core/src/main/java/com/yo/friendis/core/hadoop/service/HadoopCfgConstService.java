package com.yo.friendis.core.hadoop.service;

import com.github.pagehelper.PageHelper;
import com.yo.friendis.common.common.service.AbstractService;
import com.yo.friendis.common.common.util.DateUtils;
import com.yo.friendis.common.common.util.PageUtil;
import com.yo.friendis.common.common.util.PropertyMgr;
import com.yo.friendis.common.easyui.bean.Page;
import com.yo.friendis.core.hadoop.mapper.HadoopCfgConstMapper;
import com.yo.friendis.core.hadoop.model.HadoopCfgConst;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.Intercept;
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
@Service("haCfgConstService")
public class HadoopCfgConstService extends AbstractService<HadoopCfgConst> {
    @Autowired
    private HadoopCfgConstMapper haCfgConstMapper;
    private static Integer MAX_TOTAL = 100 * 10000;
    private static final String[] colsArray = new String[]{"配置项名", "配置项值", "备注说明", "创建时间", "修改时间"};//HadoopCfgConst属性中文名数组
    private static final String[] fieldArray = new String[]{"constKey", "constValue", "description", "createDate", "modifyDate"};//HadoopCfgConst属性名数组

    public List<HadoopCfgConst> selectByIds(List<Integer> ids) {
        return haCfgConstMapper.selectByIds(ids);
    }

    public int deleteAll() {
        return haCfgConstMapper.deleteAll();
    }

    public int deleteByIds(List<Integer> ids) {
        return haCfgConstMapper.deleteByIds(ids);
    }
    /**
     * 删除表
     * @return
     */
    public void dropTable() {
        haCfgConstMapper.dropTable();
    }

    /**
     * 创建表（如果表已存在，则先删除再创建
     */
    @Transactional(rollbackFor = Exception.class)//绑定事务
    public void createTable() {
        if( selectExistsCurrTable() ) {//如果表已存在，则先删除
            haCfgConstMapper.dropTable();
        }
        haCfgConstMapper.createTable();
    }
    /**
     * 插入初始数据
     * @return
     */
    @Transactional(rollbackFor = Exception.class)//绑定事务
    public int insertInitialData() {
        int res = 0;

        //插入数据
        HadoopCfgConst const1 = new HadoopCfgConst("mapreduce.app-submission.cross-platform", "true", "是否跨平台提交任务");
        res += haCfgConstMapper.insertSelective(const1);
        HadoopCfgConst const2 = new HadoopCfgConst("fs.defaultFS", "hdfs://master:9000", "namenode主机及端口");
        res += haCfgConstMapper.insertSelective(const2);
        HadoopCfgConst const3 = new HadoopCfgConst("mapreduce.framework.name", "yarn", "mapreduce 使用配置");
        res += haCfgConstMapper.insertSelective(const3);
        HadoopCfgConst const4 = new HadoopCfgConst("yarn.resourcemanager.address", "master:8032", "ResourceManager主机及端口");
        res += haCfgConstMapper.insertSelective(const4);
        HadoopCfgConst const5 = new HadoopCfgConst("yarn.resourcemanager.scheduler.address", "master:8030", "Scheduler主机及端口");
        res += haCfgConstMapper.insertSelective(const5);
        HadoopCfgConst const6 = new HadoopCfgConst("mapreduce.jobhistory.address", "master:10020", "JobHistory主机及端口");
        res += haCfgConstMapper.insertSelective(const6);

        return res;
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
        return haCfgConstMapper.selectCountCurrTable(dbName) > 0;
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
        return new StringBuilder().append("%").append(value).append("%").toString();//如果搜索条件类型为String，则在对应的搜索内容里前后添加%，以便数据库使用like搜索
    }
    /**
     * 根据条件获取数据，返回list
     * @param searchConditionFields
     * @param searchConditionConts
     * @return
     */
    public List<HadoopCfgConst> getDataListByConditions(Page page, boolean needPagination, List<String> searchConditionFields, List<String> searchConditionConts,
                                                        String beginCreateDateStr, String endCreateDateStr,String beginModifyDateStr, String endModifyDateStr) throws NoSuchFieldException {
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
        if( page != null ) {//分页
            if( needPagination ) {
                PageHelper.startPage(page.getPageNum(), page.getPageSize());
            }
            if( StringUtils.isNotBlank(page.getSort()) ) {
                boolean existsField = false;
                Field[] fields = HadoopCfgConst.class.getDeclaredFields();
                if( fields!=null && fields.length>0 ) {
                    for(Field field : fields) {
                        if(field.getName().equals(page.getSort()) ) {
                            existsField = true;
                            break;
                        }
                    }
                }
                if( existsField ) {
                    PageHelper.orderBy(PageUtil.getOrderCause(page.getSort() + "," + HadoopCfgConst.FIELD_CONSTID,
                            page.getOrder() + ",desc", HadoopCfgConst.class));
                }
            }
        }
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
        return haCfgConstMapper.getDataListByConditions(paramsMap);
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
    public XSSFWorkbook createDynamicXLSXExcelByData(List<HadoopCfgConst> data, List<String> cols) {
        XSSFWorkbook wb = new XSSFWorkbook();

        XSSFSheet sheet = wb.createSheet("导出的Hadoop配置数据");
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
            HadoopCfgConst haCfgConst = data.get(t);
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
                    Method method = haCfgConst.getClass().getMethod(getFieldMethodName);
                    if(method == null) {
                        continue;
                    }
                    Object value = method.invoke(haCfgConst);
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
                    logger.error("can not find method : " + getFieldMethodName + " in class " + haCfgConst.getClass().getName());
                }

                //温馨提示：cellValue的值不能为null，否则会保NullPointerException
                cell.setCellValue(colValue);
                // 创建单元格样式
                XSSFCellStyle cellStyle = wb.createCellStyle();
                cellStyle.setFont(font);
                // 创建单元格样式
                // 指定单元格居中对齐
                if("description".equals(field)) {
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
}
