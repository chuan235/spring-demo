package com.gc.expexcel.service.impl;

import com.gc.expexcel.bean.AirQuality;
import com.gc.expexcel.mapper.AirMapper;
import com.gc.expexcel.service.AirService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirServiceImpl implements AirService {

    @Autowired
    private AirMapper airMapper;

    @Override
    public List<AirQuality> getAllData() {
        List<AirQuality> allData = airMapper.getAllData();
        if(allData!=null && allData.size()>0){
            return allData;
        }else{
            return null;
        }
    }

    @Override
    public List<AirQuality> getDataByClassName(String className){
        List<AirQuality> list = airMapper.getDataByClassName(className);
        if(list!=null && list.size()>0){
            return list;
        }else{
            return null;
        }
    }

    @Override
    public boolean addAirData(AirQuality airQuality){
        return airMapper.addAirData(airQuality);
    }

    @Override
    public List<AirQuality> getDataByCO2(String className) {
        return airMapper.getDataByCO2(className);
    }

    @Override
    public List<AirQuality> getDataByTVOC(String className) {
        return airMapper.getDataByTVOC(className);
    }

    @Override
    public List<AirQuality> getDataByTemperature(String className) {
        return airMapper.getDataByTemperature(className);
    }

    @Override
    public List<AirQuality> getDataByHumidity(String className) {
        return airMapper.getDataByHumidity(className);
    }

    @Override
    public List<AirQuality> getDataByPm(String className) {
        return airMapper.getDataByPm(className);
    }

    public AirMapper getAirMapper() {
        return airMapper;
    }

    public void setAirMapper(AirMapper airMapper) {
        this.airMapper = airMapper;
    }
}
