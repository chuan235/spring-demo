#### **Lombok注解**

+ @**Data**: 注解在类上，提供类所有属性的get和set方法，此外还提供了equals、canEqual、hashCode、toString方法
+ @**Setter**: 注解在属性上，为属性提供set方法
+ @**Getter**: 注解在属性上，为属性提供get方法
+ @**Log4j**: 注解在类上，为类提供一个属性名为log的log4j日志对象
+ @**NoArgsConstructor**: 注解在类上，为类提供一个无参的构造方法
+ @**AllArgsConstructor**: 注解在类上，为类提供一个全参的构造方法
+ @**Cleanup**: 可以关闭流
+ @**Builder**: 被注解的类加个构造者模式
+ @**Synchronized**: 加个同步锁
+ @**SneakyThrows**: 等同于try/catch 捕获异常
+ @**NonNull**: 如果给参数加个这个注解 参数为null会抛出空指针异常
+ @**Value**: 注解和@Data类似，区别在于它会把所有成员变量默认定义为private final修饰，并且不会生成set方法

* 未指定Thymeleaf版本时，默认使用Thymeleaf2.1，使用HTML5模版会进行警告，改变Thymeleaf的版本或者修改配置
```xml
<properties>
    <thymeleaf.version>3.0.2.RELEASE</thymeleaf.version>
    <thymeleaf-layout-dialect.version>2.1.1</thymeleaf-layout-dialect.version>
</properties>
```
```properties
spring.thymeleaf.mode=HTML
```
* Class path contains multiple SLF4J bindings.---> 日志jar包冲突
* 分页时出现重复数据的解决方法：按时间排序的记录前一页最后一条数据的时间(查询添加条件)，动态排序记录Id


#### **1、Apache PIO**

> Apache PIO提供对Microsoft Office读写的API

+ HSSF(Horrible SpreadSheet Format),通过操作HSSF，来读取、写入、修改excel文件
+ HSSF下分为两类API，分别是用户模型和事件用户模型，即usermodel和eventusermodel

**2、POI EXCEL文档结构类**

+ HSSFWorkbook          excel文档对象
+ HSSFSheet             excel的sheet
+ HSSFRow               excel的行
+ HSSFCell              excel的单元格 
+ HSSFFont              excel字体
+ HSSFName              名称 
+ HSSFDataFormat        日期格式
+ HSSFHeader            sheet头
+ HSSFFooter            sheet尾
+ HSSFCellStyle         cell样式
+ HSSFDateUtil          日期
+ HSSFPrintSetup        打印
+ HSSFErrorConstants    错误信息表

**3、导入Excel常用的方法：**

```java
POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream("d:/test.xls"));    
HSSFWorkbook wb = new HSSFWorkbook(fs);  //得到Excel工作簿对象   
HSSFSheet sheet = wb.getSheetAt(0);   //得到Excel工作表对象   
HSSFRow row = sheet.getRow(i);  //得到Excel工作表的行   
HSSFCell cell = row.getCell((short) j);  //得到Excel工作表指定行的单元格 
cellStyle = cell.getCellStyle();  //得到单元格样式  
```

**4、导出Excel常用的方法：**

```java
HSSFWorkbook wb = new HSSFWorkbook();  //创建Excel工作簿对象   
HSSFSheet sheet = wb.createSheet("new sheet");  //创建Excel工作表对象     
HSSFRow row = sheet.createRow((short)0);  //创建Excel工作表的行   
cellStyle = wb.createCellStyle();  //创建单元格样式   
row.createCell((short)0).setCellStyle(cellStyle);  //创建Excel工作表指定行的单元格   
row.createCell((short)0).setCellValue(1);  //设置Excel工作表的值 
```

**5、Excel工具类：**

```java
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
```

```java
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
```

```java
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
```

```java
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
```










































