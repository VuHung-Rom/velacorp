package com.velacorp.order_management.common;

import com.google.gson.Gson;

public class Utils {
  public static final Gson gson = new Gson();

  public static String objectToJson(Object o){
    return gson.toJson(o);
  }
  public class Constants{

    public static final String STATUS_ACTIVE = "1";
    public static final String STATUS_INACTIVE = "-1";
    public static final String ERROR_NOT_FOUND = "NOT_FOUND";
    public static final String ERROR_VALIDATION_ERROR =  "VALIDATION_ERROR";
    public static final String ERROR_CREATE_ORDER =  "CREATE_ORDER_ERROR";
    public static final String SUCCESS =  "SUCCESS";
    public static final String ERROR_DELETE_PRODUCT_ID_NOT_FOUND =  "PRODUCT_ID_NOT_FOUND";
    public static final String ERROR_PRODUCT_VALIDATE =  "PRODUCT_VALIDATE_ERROR";
    public static final String ERROR_UNKNOW =  "UNKNOW_ERROR";



  }
}
