package com.gc.expexcel.service;


import com.gc.expexcel.bean.AirQuality;

import java.util.List;

public interface AirService {

    /**
     * 根据教室名称获取教室空气数据
     * @param className
     * @return
     */
    List<AirQuality> getDataByClassName(String className);

    /**
     * 增加教室空气数据
     * @param airQuality
     * @return
     */
    boolean addAirData(AirQuality airQuality);

    /**
     * 查询所有的数据
     * @return
     */
    List<AirQuality> getAllData();

    /**
     * 查询教室最新的CO2数据
     * @return
     */
    List<AirQuality> getDataByCO2(String className);

    /**
     * 查询教室最新的TVOC数据
     * @return
     */
    List<AirQuality> getDataByTVOC(String className);
    /**
     * 查询教室最新的温度数据
     * @return
     */
    List<AirQuality> getDataByTemperature(String className);
    /**
     * 查询教室最新的湿度数据
     * @return
     */
    List<AirQuality> getDataByHumidity(String className);
    /**
     * 查询教室最新的PM2.5数据
     * @return
     */
    List<AirQuality> getDataByPm(String className);
}
