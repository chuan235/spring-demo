package top.gmfcj.util;

import java.util.HashMap;

public class ResultMap extends HashMap{

    public static  String SUCCESS_CODE="200";
    public static String SERVER_ERROR_CODE="500";
    public static String DATA_KEY = "data";
    public static String MSG_KEY = "msg";

    private ResultMap(){

    }

    public ResultMap set(String key, Object object){
        super.put(key,object);
        return  this;
    }

    private  static ResultMap getResult(){
        return new ResultMap();
    }

    public static ResultMap success(){

        return ResultMap.getResult().set("code", ResultMap.SUCCESS_CODE).set(ResultMap.MSG_KEY,"操作成功");
    }

    public static ResultMap success(String msg){

        return ResultMap.getResult().set("code", ResultMap.SUCCESS_CODE).set(ResultMap.MSG_KEY,msg);
    }

    public static ResultMap success(String msg, Object object){

        return ResultMap.getResult().set("code", ResultMap.SUCCESS_CODE).set(ResultMap.MSG_KEY,msg).set(ResultMap.DATA_KEY,object);
    }

    public ResultMap data(Object obj){
        return this.set("data",obj);
    }

    public static ResultMap error(){
        return ResultMap.getResult().set(ResultMap.MSG_KEY,"操作失败").set("code", ResultMap.SERVER_ERROR_CODE);
    }

    public static ResultMap error(String msg){
        return ResultMap.getResult().set(ResultMap.MSG_KEY,msg).set("code", ResultMap.SERVER_ERROR_CODE);
    }

    public static ResultMap error(String msg, Object object){
        return ResultMap.getResult().set(ResultMap.MSG_KEY,msg).set(ResultMap.DATA_KEY,object).set("code", ResultMap.SERVER_ERROR_CODE);
    }

}
