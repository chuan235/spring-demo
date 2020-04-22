package com.gc.expexcel.bean;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * 教室空气质量对象
 */
@Repository
@Data
@NoArgsConstructor
public class AirQuality implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer airId;
    private String className;
    private String temperature;
    private String humidity;
    private String pm;
    private String co2;
    private String tvoc;
    private Long dataTime;


    @Override
    public String toString() {
        return "AirQuality{" +
                "airId=" + airId +
                ", className='" + className + '\'' +
                ", temperature='" + temperature + '\'' +
                ", humidity='" + humidity + '\'' +
                ", pm='" + pm + '\'' +
                ", co2='" + co2 + '\'' +
                ", tvoc='" + tvoc + '\'' +
                ", dataTime=" + dataTime +
                '}';
    }
}
