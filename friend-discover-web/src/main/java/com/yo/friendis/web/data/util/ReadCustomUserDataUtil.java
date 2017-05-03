package com.yo.friendis.web.data.util;

import com.yo.friendis.common.common.util.DateUtils;
import com.yo.friendis.web.friend.model.CustomUser;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReadCustomUserDataUtil {
	public static final String OFFICE_EXCEL_2003_POSTFIX = "xls";
    public static final String OFFICE_EXCEL_2010_POSTFIX = "xlsx";
    public static final String EMPTY = "";
    public static final String POINT = ".";
    public static final String NOT_EXCEL_FILE = " : Not the Excel file!";
    public static final String PROCESSING = "Processing...";
    public static Logger logger = LoggerFactory.getLogger(ReadCustomUserDataUtil.class);
    /**
     * 读取短信excel文件，可自定根据后缀名不同调用不同方法，xls为excel2003/2007,xlsx为2010
     * @param path 文件路径
     * @return
     * @throws IOException
     */
    public static List<CustomUser> readExcel(String path) throws IOException{
        if (path == null || EMPTY.equals(path)) {
            return null;
        } else {
            String postfix = getPostfix(path);
            if (!EMPTY.equals(postfix)) {
                if (OFFICE_EXCEL_2003_POSTFIX.equals(postfix)) {
                    return readXls(path);
                } else if (OFFICE_EXCEL_2010_POSTFIX.equals(postfix)) {
                    return readXlsx(path);
                }
            } else {
                System.out.println(path + NOT_EXCEL_FILE);
            }
        }
        return null;
    }
    /**
     * 判断导入的文件中的短信是否有短信内容的长度超过限制
     * @param path
     * @return
     * @throws IOException
     */
    public static boolean isContainedOverword(String path) throws IOException{
        if (path == null || EMPTY.equals(path)) {
            return false;
        } else {
            String postfix = getPostfix(path);
            if (!EMPTY.equals(postfix)) {
                if (OFFICE_EXCEL_2003_POSTFIX.equals(postfix)) {
                    return isContainedOverword_xls(path);
                } else if (OFFICE_EXCEL_2010_POSTFIX.equals(postfix)) {
                    return isContainedOverword_xlsx(path);
                }
            } else {
                System.out.println(path + NOT_EXCEL_FILE);
            }
        }
        return false;
    }
    /**
     * 判断导入的文件中的短信数量是否超过了最大导入数量限制
     * @param path
     * @return
     * @throws IOException
     */
    public static boolean isMoreThanMaxCnt(String path) throws IOException{
        if (path == null || EMPTY.equals(path)) {
            return false;
        } else {
            String postfix = getPostfix(path);
            if (!EMPTY.equals(postfix)) {
                if (OFFICE_EXCEL_2003_POSTFIX.equals(postfix)) {
                    return isMoreThanMaxCnt_xls(path);
                } else if (OFFICE_EXCEL_2010_POSTFIX.equals(postfix)) {
                    return isMoreThanMaxCnt_xlsx(path);
                }
            } else {
                System.out.println(path + NOT_EXCEL_FILE);
            }
        }
        return false;
    }
    /**
     * 判断导入的文件中的短信数量是否超过了最大导入数量限制
     * @param path
     * @return
     * @throws IOException
     */
    public static boolean isMoreThanMaxCnt_xlsx(String path) throws  IOException{
        logger.info(PROCESSING + path);
        InputStream is = new FileInputStream(path);
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        int availableCnt = 0;
        // 读取工作簿
        for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
            if (xssfSheet == null) {
                continue;
            }
            // 读取行
            //从第二行开始，因为第1行为标题
            for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                //读取列
                if (xssfRow != null) {
                    XSSFCell smsContentCell = xssfRow.getCell(1);
                    String smsContent = getValue(smsContentCell);
//                    int smsContLength = com.yo.friendis.web.util.StringUtils.calcLen4UTF8(smsContent);
//                    if( StringUtils.isNotBlank(smsContent) ) {
//                        if( smsContLength < 3000 ) {
//                            availableCnt++;
//                        }
//                    }
                }
//                if( availableCnt > Sms.MAX_MINING_IMPORT_SMS_CNT ) {
//                    xssfWorkbook.close();
//                    return true;
//                }
            }
        }
        xssfWorkbook.close();
        return false;
    }
    /**
     * 判断导入的文件中的短信数量是否超过了最大导入数量限制
     * @param path
     * @return
     * @throws IOException
     */
    public static boolean isMoreThanMaxCnt_xls(String path) throws IOException{
        logger.info(PROCESSING + path);
        InputStream is = new FileInputStream(path);
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
        int availableCnt = 0;
        // 读取工作簿
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            // 读取行
            //从第二行开始，因为第1行为标题
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                //读取列
                if (hssfRow != null) {
                    HSSFCell smsContentCell = hssfRow.getCell(1);
                    String smsContent = getValue(smsContentCell);
//                    int smsContLength = com.yo.friendis.util.StringUtils.calcLen4UTF8(smsContent);
//                    if( StringUtils.isNotBlank(smsContent) ) {
//                        if( smsContLength < 3000 ) {
//                            availableCnt++;
//                        }
//                    }
                }
//                if( availableCnt > Sms.MAX_MINING_IMPORT_SMS_CNT ) {
//                    hssfWorkbook.close();
//                    return true;
//                }
            }
        }
        hssfWorkbook.close();
        return false;
    }
    /**
     * 判断导入的文化中是否含有超过长度限制的关键字
     * @param path
     * @return
     * @throws IOException
     */
    public static boolean isContainedOverword_xlsx(String path) throws  IOException{
    	logger.info(PROCESSING + path);
        InputStream is = new FileInputStream(path);
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        // 读取工作簿
        for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
            if (xssfSheet == null) {
                continue;
            }
            // 读取行
            //从第二行开始，因为第1行为标题
            for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                //读取列
                if (xssfRow != null) {
                	XSSFCell smsContentCell = xssfRow.getCell(1);
                	String smsContent = getValue(smsContentCell);
//            		int smsContLength = com.yo.friendis.util.StringUtils.calcLen4UTF8(smsContent);
//                	if(StringUtils.isNotBlank(smsContent) && smsContLength > 3000) {
//                		xssfWorkbook.close();
//                		return true;
//                	}
                }
            }
        }
        xssfWorkbook.close();
        return false;
    }
    /**
     * 判断导入的文化中是否含有超过长度限制的关键字
     * @param path
     * @return
     * @throws IOException
     */
    public static boolean isContainedOverword_xls(String path) throws  IOException{
    	logger.info(PROCESSING + path);
        InputStream is = new FileInputStream(path);
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
        // 读取工作簿
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            // 读取行
            //从第二行开始，因为第1行为标题
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                //读取列
                if (hssfRow != null) {
                	HSSFCell smsContentCell = hssfRow.getCell(1);
                	String smsContent = getValue(smsContentCell);
//            		int smsContLength = com.yo.friendis.util.StringUtils.calcLen4UTF8(smsContent);
//                	if(StringUtils.isNotBlank(smsContent) && smsContLength > 3000) {
//                		hssfWorkbook.close();
//                		return true;
//                	}
                }
            }
        }
        hssfWorkbook.close();
        return false;
    }
    /**
     * 根据路径读取短信excel文件(2010)
     * @param path 文件路径
     * @throws IOException
     */
    public static List<CustomUser> readXlsx(String path) throws  IOException{
    	logger.info(PROCESSING + path);
        InputStream is = new FileInputStream(path);
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        List<CustomUser> list = new ArrayList<CustomUser>();
        // 读取工作簿
        for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
            if (xssfSheet == null) {
                continue;
            }
            // 读取行
            //从第二行开始，因为第1行为标题
            for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                //读取列
                if (xssfRow != null) {
                	//读取短信
                	XSSFCell manualBTypeCell = xssfRow.getCell(0);//手工大类(可选
                	XSSFCell smsContentCell = xssfRow.getCell(1);//短信内容（必填），如果没填，自动跳过该行
                	
                	String manualBType = getValue(manualBTypeCell);
                	String smsContent = getValue(smsContentCell);
                	String smsEtlContent = "";
                	//如果读取到的短信内容或业务类型为空，则跳过（先不用处理业务类型
                	if(StringUtils.isBlank(smsContent)) {
                		continue;
                	} else {
//                        int smsUTF8Len = com.yo.friendis.util.StringUtils.calcLen4UTF8(smsContent);
//                        if( smsUTF8Len > Sms.MAX_SMS_LEN ) {//如果短信内容长度超过最大限制，则截断超过长度的部分
//                            smsContent = com.yo.friendis.util.StringUtils.cutStringByLen(smsContent, Sms.MAX_SMS_LEN);
//                        }
//                		smsContent = smsContent.replace("<", "&lt;").replace(">", "&gt;");
//                        Map<String, String> etlResMap = EtlUtils.getETLContent(bsMap, stMap, spMap, smsContent);//获取清洗后的短信内容
//                        if(etlResMap != null) {
//                            smsEtlContent = etlResMap.get("etlCont");
//                        }
                	}
                	
//                	Sms sms = new Sms();
//            		sms.setBusinessType("0");
//                	sms.setSmsContent(smsContent);
//                	sms.setSmsEtlContent(smsEtlContent);
//                	sms.setBatchId(batchId);
//                	sms.setSmsSource("1");
//                	sms.setCreateTime(DateUtils.getCurrentTimestamp());
//                	if(StringUtils.isNotBlank(manualBType)) {
//                        sms.setManualBTypeId(getTypeId(ktService, typeMap, manualBType));
//                	}
//                	list.add(sms);
                }
            }
        }
        xssfWorkbook.close();
        
        return list;
    }
    /**
     * 根据路径读取短信excel文件(2003/2007)
     * @param path 文件路径
     * @throws IOException
     */
    public static List<CustomUser> readXls(String path) throws IOException {
    	logger.info(PROCESSING + path);
        InputStream is = new FileInputStream(path);
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
        List<CustomUser> list = new ArrayList<CustomUser>();
        // 读取工作簿
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            // 读取行
            //从第二行开始，因为第1行为标题
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                //读取列
                if (hssfRow != null) {
                	//读取短信
                	HSSFCell manualBTypeCell = hssfRow.getCell(0);//手工大类(可选
                	HSSFCell smsContentCell = hssfRow.getCell(1);//短信内容
                	
                	String manualBType = getValue(manualBTypeCell);
                	String smsContent = getValue(smsContentCell);
                	String smsEtlContent = "";
                	//如果读取到的短信内容或批次名称为空，则跳过
                	if(StringUtils.isBlank(smsContent)) {
                		continue;
                	} else {
//                        int smsUTF8Len = com.yo.friendis.util.StringUtils.calcLen4UTF8(smsContent);
//                        if( smsUTF8Len > Sms.MAX_SMS_LEN ) {//如果短信内容长度超过最大限制，则截断超过长度的部分
//                            smsContent = com.yo.friendis.util.StringUtils.cutStringByLen(smsContent, Sms.MAX_SMS_LEN);
//                        }
//                		smsContent = smsContent.replace("<", "&lt;").replace(">", "&gt;");
//                        Map<String, String> etlResMap = EtlUtils.getETLContent(bsMap, stMap, spMap, smsContent);//获取清洗后的短信内容
//                        if(etlResMap != null) {
//                            smsEtlContent = etlResMap.get("etlCont");
//                        }
                		
                	}
                	
//                	Sms sms = new Sms();
//            		sms.setBusinessType("0");
//                	sms.setSmsContent(smsContent);
//                	sms.setSmsEtlContent(smsEtlContent);
//                	sms.setBatchId(batchId);
//                	sms.setSmsSource("1");
//                	sms.setCreateTime(DateUtils.getCurrentTimestamp());
//                	if(StringUtils.isNotBlank(manualBType)) {
//                        sms.setManualBTypeId(getTypeId(ktService, typeMap, manualBType));
//                	}
//                	list.add(sms);
                }
            }
        }
        hssfWorkbook.close();
        
        return list;
    }
    /**
     * 获取excel2010中单元格的内容
     * @param xssfCell
     * @return String
     */
    @SuppressWarnings("static-access")
    private static String getValue(XSSFCell xssfCell) {
    	if(xssfCell == null || xssfCell.getCellType() == XSSFCell.CELL_TYPE_ERROR || xssfCell.getCellType() == XSSFCell.CELL_TYPE_BLANK) {
    		return "";
    	}
    	//如果单元格类型为公式，则将其类型设置为String
        if(xssfCell.getCellType() == XSSFCell.CELL_TYPE_FORMULA) {
        	xssfCell.setCellType(XSSFCell.CELL_TYPE_STRING);
        }
        if (xssfCell.getCellType() == xssfCell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(xssfCell.getBooleanCellValue());
        } else if (xssfCell.getCellType() == xssfCell.CELL_TYPE_NUMERIC) {
        	DecimalFormat df = new DecimalFormat("0");
            return df.format(xssfCell.getNumericCellValue());
        } else {
            return String.valueOf(xssfCell.getStringCellValue());
        }
    }
    /**
     * 获取excel2003/2007种单元格的内容
     * @param hssfCell
     * @return String
     */
    @SuppressWarnings("static-access")
    private static String getValue(HSSFCell hssfCell) {
    	if(hssfCell == null || hssfCell.getCellType() == HSSFCell.CELL_TYPE_ERROR || hssfCell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
    		return "";
    	}
    	//如果单元格类型为公式，则将其类型设置为String
        if(hssfCell.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
        	hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
        }
        if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(hssfCell.getBooleanCellValue());
        } else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
        	DecimalFormat df = new DecimalFormat("0");
            return df.format(hssfCell.getNumericCellValue());
        } else {
            return String.valueOf(hssfCell.getStringCellValue());
        }
    }
    
    private static Timestamp getDateValue(XSSFCell xssfCell) {
    	//先直接返回日期
    	if(xssfCell != null && xssfCell.getCellType() == xssfCell.CELL_TYPE_NUMERIC) {
    		return new Timestamp(xssfCell.getDateCellValue().getTime());
    	}
        return DateUtils.getCurrentTimestamp();
    }
    private static Timestamp getDateValue(HSSFCell hssfCell) {
    	if (hssfCell !=null && hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
    	//先直接返回日期
    		return new Timestamp(hssfCell.getDateCellValue().getTime());
    	}
    	return DateUtils.getCurrentTimestamp();
    }
    /**
     * 获取文件路径后缀，方便调用不同的方法解析不同版本的excel文件
     * @param path
     * @return String
     */
    public static String getPostfix(String path) {
        if (path == null || EMPTY.equals(path.trim())) {
            return EMPTY;
        }
        if (path.contains(POINT)) {
            return path.substring(path.lastIndexOf(POINT) + 1, path.length());
        }
        return EMPTY;
    }
}