package com.gc.expexcel.mapper;

import com.gc.expexcel.bean.AirQuality;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AirMapper {

    @Insert("insert into airdata(class_name,temperature,humidity,pm,co2,tvoc,data_time) values(#{className},#{temperature},#{humidity},#{pm},#{co2},#{tvoc},#{dataTime})")
    boolean addAirData(AirQuality airQuality);

    @Select("select * from airdata where class_name = #{name}")
    List<AirQuality> getDataByClassName(@Param("name") String className);

    @Select("select * from airdata")
    List<AirQuality> getAllData();

    @Select("select class_name,pm,data_time from airdata where class_name = #{className} ORDER BY data_time DESC LIMIT 10")
    List<AirQuality> getDataByPm(String className);
    @Select("select class_name,co2,data_time from airdata where class_name = #{className} ORDER BY data_time DESC LIMIT 10")
    List<AirQuality> getDataByCO2(String className);
    @Select("select class_name,humidity,data_time from airdata where class_name = #{className} ORDER BY data_time DESC LIMIT 10")
    List<AirQuality> getDataByHumidity(String className);
    @Select("select class_name,tvoc,data_time from airdata where class_name = #{className} ORDER BY data_time DESC LIMIT 10")
    List<AirQuality> getDataByTVOC(String className);
    @Select("select class_name,temperature,data_time from airdata where class_name = #{className} ORDER BY data_time DESC LIMIT 10")
    List<AirQuality> getDataByTemperature(String className);
}
