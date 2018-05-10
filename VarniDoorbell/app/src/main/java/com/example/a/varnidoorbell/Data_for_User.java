package com.example.a.varnidoorbell;

import android.util.Log;

import java.lang.Object;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by a on 18-Feb-2016.
 */
public class Data_for_User {
    String userKey, deviceKey, deviceName, datastreamName;
    long device_id;
    long product_id;
    long datastream_id;
    double x;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    String time;

    public String getDatastreamName() {
        return datastreamName;
    }

    public void setDatastreamName(String datastreamName) {
        this.datastreamName = datastreamName;
    }



    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
//        time = time_change(time);
    }


    public long getDatastream_id() {
        return datastream_id;
    }

    public void setDatastream_id(long datastream_id) {
        this.datastream_id = datastream_id;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(long product_id) {
        this.product_id = product_id;
    }

    public long getDevice_id() {
        return device_id;
    }

    public void setDevice_id(long device_id) {
        this.device_id = device_id;
    }

    public String getDeviceKey() {
        return deviceKey;
    }

    public void setDeviceKey(String deviceKey) {
        this.deviceKey = deviceKey;
    }

    public String getUserKey() {

        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

//    public String time_change(String str){
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        try {
//            Date date = sdf.parse(str);
//            Calendar calendar = Calendar.getInstance(Locale.CHINA);
//            calendar.setTime(date);
//
//            Calendar cal = Calendar.getInstance(Locale.)
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return str;
//
//    }
}
