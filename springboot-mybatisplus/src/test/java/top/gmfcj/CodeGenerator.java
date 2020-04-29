package top.gmfcj;


import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import top.gmfcj.base.BaseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CodeGenerator {



    public static void main(String[] args) {
        AutoGenerator mpg = new AutoGenerator();

        GlobalConfig gc = new GlobalConfig();
        // D:\Aworkspace\branch\blog\springboot-mybatisplus
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("zggc");
        gc.setOpen(false);
        mpg.setGlobalConfig(gc);

        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://192.168.222.134:3306/test?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true");
        dsc.setSchemaName("public");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("password");
        mpg.setDataSource(dsc);

        PackageConfig pc = new PackageConfig();
        pc.setParent("top.gmfcj");
        mpg.setPackageInfo(pc);

        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

//        String templatePath = "/templates/mapper.xml.ftl";
        String templatePath = "/templates/mapper.xml.vm";

        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + "/src/main/resources/mapper/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        /*

        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        TemplateConfig templateConfig = new TemplateConfig();

        templateConfig.setEntity("templates/entity.java");
        templateConfig.setMapper("templates/mapper.java");
        templateConfig.setService("templates/service.java");
        templateConfig.setServiceImpl("templates/serviceImpl.java");
        templateConfig.setController("templates/controller.java");
        mpg.setTemplate(templateConfig);

        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setSuperEntityClass(BaseEntity.class);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        strategy.setSuperControllerClass("top.gmfcj.base.BaseController");
        strategy.setSuperServiceImplClass("top.gmfcj.base.AbstractService");
        strategy.setSuperEntityColumns("create_by", "update_by", "create_time", "update_time", "remark", "delFlag");
        strategy.setInclude("sys_user");
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new VelocityTemplateEngine());
        mpg.execute();
        */
    }

}
