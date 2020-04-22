package com.gc.expexcel.controller;

import com.gc.expexcel.bean.AirQuality;
import com.gc.expexcel.service.AirService;
import com.gc.expexcel.util.ExcelUtil;
import com.gc.expexcel.util.OtherUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@Controller
@Slf4j
public class ExcelController {

    @Autowired
    @Setter
    private AirService airService;

    /**
     * 将数据导出到Excel
     */
    @RequestMapping("/excel/download")
    public void downloadExcel(HttpServletResponse response){
        String[] dataHead = {"序号","教室名称","温度(摄氏度)","湿度(%RH)","PM2.5浓度(ug/m3)","CO2浓度(ppm)","TVOC浓度(ppb)","测量时间"};

        List<AirQuality> data = airService.getAllData();
        if(data == null || data.size()==0){
            log.debug("未获取到数据");
            return;
        }
        try {
            // 例化HSSFWorkBook
            HSSFWorkbook workBook = new HSSFWorkbook();
            // 创建一个Excel表单，参数为sheet的名字
            HSSFSheet sheet = workBook.createSheet("空气质量表");
            // 创建表头
            ExcelUtil.setTitle(workBook,sheet,dataHead);
            // 遍历填充数据
            int rowIndex = 1;
            for(AirQuality airQuality:data){
                HSSFRow row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(airQuality.getAirId());
                row.createCell(1).setCellValue(airQuality.getClassName());
                row.createCell(2).setCellValue(airQuality.getTemperature());
                row.createCell(3).setCellValue(airQuality.getHumidity());
                row.createCell(4).setCellValue(airQuality.getPm());
                row.createCell(5).setCellValue(airQuality.getCo2());
                row.createCell(6).setCellValue(airQuality.getTvoc());
                row.createCell(7).setCellValue(OtherUtil.formatDateToString(new Date(airQuality.getDataTime()),"YYYY-MM-DD HH:mm"));
            }
            // 使用浏览器下载
            ExcelUtil.setBrowser(response,workBook,"downData.xlsx");
        } catch (Exception e) {
            log.error("创建文件出错....请重试！！");
        }
    }


}
