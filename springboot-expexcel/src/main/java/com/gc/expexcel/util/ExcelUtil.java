package com.gc.expexcel.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static org.apache.poi.ss.usermodel.CellType.*;

/**
 * Excel导入导出
 */
@Slf4j
public class ExcelUtil {


    /**
     * 设置表头
     * @param workBook
     * @param sheet 工作表
     * @param str   表头数据
     */
    public static void setTitle(HSSFWorkbook workBook, HSSFSheet sheet, String[] str){
        try {
            HSSFRow row = sheet.createRow(0);
            // 设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
            for (int i = 0,len = str.length; i <= len; i++) {
                sheet.setColumnWidth(i, 15 * 256);
            }
            // 设置为居中加粗,格式化时间格式
            HSSFCellStyle style = workBook.createCellStyle();
            HSSFFont font = workBook.createFont();
            font.setBold(true);
            style.setFont(font);
            style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
            // 创建表头名称
            HSSFCell cell;
            for (int j = 0,len = str.length; j < len; j++) {
                cell = row.createCell(j);
                cell.setCellValue(str[j]);
                cell.setCellStyle(style);
            }
        } catch (Exception e) {
            log.info("导出时设置表头失败！");
            e.printStackTrace();
        }
    }

    /**
     * 数据填充
     * @param sheet 工作表
     * @param data 数据
     */
    public static void setData(HSSFSheet sheet, List<String> data){
        try{
            int rowNum = 1;
            for (int i = 0,s = data.size(); i < s; i++) {
                HSSFRow row = sheet.createRow(rowNum++);
                for (String val:data){
                    row.createCell(i).setCellValue(val);
                }
            }
            log.info("表格赋值成功！");
        }catch (Exception e){
            log.info("表格赋值失败！");
            e.printStackTrace();
        }
    }

    /**
     * 根据浏览器下载
     * @param response 响应
     * @param workbook 工作表
     * @param fileName 下载文件的名称
     */
    public static void setBrowser(HttpServletResponse response, HSSFWorkbook workbook, String fileName){
        try {
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            OutputStream os = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/vnd.ms-excel;charset=gb2312");
            // 将excel写入到输出流中
            workbook.write(os);
            os.flush();
            os.close();
            log.info("设置浏览器下载成功！");
        } catch (Exception e) {
            log.info("设置浏览器下载失败！");
            e.printStackTrace();
        }
    }

    /**
     * 导入数据
     * @param fileName
     * @return
     */
    public static List<Object[]> importExcel(String fileName){
        log.info("导入解析开始，fileName:{}",fileName);
        try {
            List<Object[]> list = new ArrayList<>();
            InputStream inputStream = new FileInputStream(fileName);
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            // 获取sheet的行数
            int rows = sheet.getPhysicalNumberOfRows();
            for (int i = 0; i < rows; i++) {
                // 过滤表头行
                if (i == 0) {
                    continue;
                }
                // 获取当前行的数据
                Row row = sheet.getRow(i);
                Object[] objects = new Object[row.getPhysicalNumberOfCells()];
                int index = 0;
                for (Cell cell : row) {
                    if (cell.getCellType().equals(NUMERIC)) {
                        objects[index] = (int) cell.getNumericCellValue();
                    }
                    if (cell.getCellType().equals(STRING)) {
                        objects[index] = cell.getStringCellValue();
                    }
                    if (cell.getCellType().equals(BOOLEAN)) {
                        objects[index] = cell.getBooleanCellValue();
                    }
                    if (cell.getCellType().equals(ERROR)) {
                        objects[index] = cell.getErrorCellValue();
                    }
                    index++;
                }
                list.add(objects);
            }
            log.info("导入文件解析成功！");
            return list;
        }catch (Exception e){
            log.info("导入文件解析失败！");
            e.printStackTrace();
        }
        return null;
    }


    public static boolean isExcel2003(String filePath) {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    public static boolean isExcel2007(String filePath) {
        return filePath.matches("^.+\\.(?i)(xlsx)$");

    }

}

