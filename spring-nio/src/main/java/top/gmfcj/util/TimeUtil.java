package top.gmfcj.util;

import java.util.Date;

public class TimeUtil {

    /**
     * 返回至当前时刻毫米级的时间，随着系统的时间改变而该变
     * @return
     */
    public static long currentElapsedTime() {
        return System.nanoTime() / 1000000;
    }

    /**
     * 返回系统当前的毫秒数
     * @return
     */
    public static long currentWallTime() {
        return System.currentTimeMillis();
    }

    /**
     * 将elapsedTime转为Date
     * @param elapsedTime
     * @return
     */
    public static Date elapsedTimeToDate(long elapsedTime) {
        long wallTime = currentWallTime() + elapsedTime - currentElapsedTime();
        return new Date(wallTime);
    }

    public static void main(String[] args) {
        System.out.println("nanoTime="+System.nanoTime());
        System.out.println("currentElapsedTime="+currentElapsedTime());
        System.out.println("currentWallTime="+currentWallTime());
    }
}