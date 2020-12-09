package com.zhcx.authorization.utils;


import java.util.HashMap;
import java.util.Map;


/**
 * @program: basic-data-service
 * @ClassName:A
 * @description:
 * @author: ZhangKai
 * @create: 2018-12-23 16:34
 **/
public class TransferAxesUitl {

    private static final double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

    private static final double PI = 3.14159265358979324;


    /**
     * BD 09 TO WGS84 百度坐标系转WGS84
     * @param bdlat
     * @param bdlng
     * @return
     */
    public static Map<String,Double> bd09ToWGS84(double bdlat,double bdlng){
        Map<String,Double> bToG = bdo9ToGCJ02(bdlat,bdlng);
        return  gcj02ToWGS84(bToG.get("lat"),bToG.get("lng"));
    }


    /**
     * BD-09 TO GCJ-02 百度坐标转火星坐标
     *
     * @param bdlat
     * @param bdlng
     * @return
     */
    public static Map<String, Double> bdo9ToGCJ02(double bdlat, double bdlng) {
        Map<String, Double> point = new HashMap<>();
        double x = bdlng - 0.0065, y = bdlat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        double gcjLon = z * Math.cos(theta);
        double gcjLat = z * Math.sin(theta);
        point.put("lat", gcjLat);
        point.put("lng", gcjLon);
        return point;
    }


    /**
     * GCJ-02 to WGS-84 火星坐标转地球坐标WGS84
     *
     * @param gcjLat
     * @param gcjLng
     * @return
     */
    public static Map<String, Double> gcj02ToWGS84(double gcjLat, double gcjLng) {
        Map<String, Double> result = new HashMap<>();
        if (outOfChina(gcjLat, gcjLng)) {
            result.put("lat", gcjLat);
            result.put("lng", gcjLng);
            return result;
        }

        Map<String, Double> deltaRes = delta(gcjLat, gcjLng);
        result.put("lat", gcjLat - deltaRes.get("lat"));
        result.put("lng", gcjLng - deltaRes.get("lng"));
        return result;
    }


    /**
     * WGS84 TO GCJ02 WGS84地球坐标转火星坐标系
     *
     * @param lat
     * @param lng
     * @return
     */
    public static Map<String, Double> wgs84ToGCJ02(double lat, double lng) {
        Map<String, Double> result = new HashMap<>();
        if (outOfChina(lat, lng)) {
            result.put("lat", lat);
            result.put("lng", lng);
            return result;
        }
        Map<String, Double> deltaRes = delta(lat, lng);
        result.put("lat", lat + deltaRes.get("lat"));
        result.put("lng", lng + deltaRes.get("lng"));
        return result;
    }

    /**
     * 地球转百度坐标
     * @param wgsLat
     * @param wgsLng
     * @return
     */
    public static Map<String,Double> wgs84ToBD09(double wgsLat,double wgsLng){
        Map<String,Double> wToG = wgs84ToGCJ02(wgsLat,wgsLng);
        return gcj02ToBD09(wToG.get("lat"),wToG.get("lng"));
    }

    /**
     * 地球转百度坐标
     * @return
     */
    public static Map<String,Double> wgs84ToBD09(String wgsLatStr,String wgsLngStr){
        Double wgsLat = Double.parseDouble(wgsLatStr);
        Double wgsLng = Double.parseDouble(wgsLngStr);
        Map<String,Double> wToG = wgs84ToGCJ02(wgsLat,wgsLng);
        return gcj02ToBD09(wToG.get("lat"),wToG.get("lng"));
    }


    /**
     * 谷歌坐标转百度坐标
     * @param latStr
     * @param lngStr
     * @return
     */
    public static Map<String, Double> gcj02ToBD09(String latStr, String lngStr) {
        Double lat = Double.parseDouble(latStr);
        Double lng = Double.parseDouble(lngStr);
        return gcj02ToBD09(lat, lng);
    }


    public static Map<String, Double> gcj02ToBD09(double lat, double lng) {
        Map<String, Double> result = new HashMap<>();
        double x = lng;
        double y = lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);

        System.out.println(x + " " + y + " " + z);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
        double bdLon = z * Math.cos(theta) + 0.0065;
        double bdLat = z * Math.sin(theta) + 0.006;
        result.put("lat", bdLat);
        result.put("lng", bdLon);
        return result;
    }

    public static Map<String, Double> delta(double lat, double lon) {
        Map<String, Double> result = new HashMap<>();
        double a = 6378245.0; //  a: 卫星椭球坐标投影到平面地图坐标系的投影因子。
        double ee = 0.00669342162296594323; //  ee: 椭球的偏心率。
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * PI;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * PI);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * PI);
        result.put("lat", dLat);
        result.put("lng", dLon);
        return result;
    }


    public static Double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * PI) + 40.0 * Math.sin(y / 3.0 * PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * PI) + 320 * Math.sin(y * PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    public static Double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * PI) + 40.0 * Math.sin(x / 3.0 * PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * PI) + 300.0 * Math.sin(x / 30.0 * PI)) * 2.0 / 3.0;
        return ret;
    }


    /**
     * 是否在中国
     *
     * @param lat
     * @param lng
     * @return
     */
    public static boolean outOfChina(double lat, double lng) {
        if (lng < 72.004 || lng > 137.8347) {
            return true;
        }
        if (lat < 0.8293 || lat > 55.8271) {
            return true;
        }
        return false;
    }

        public static void main(String[] args) {
        // 谷歌坐标:29.0741924301,111.6889679517

        Map<String, Double> gcj = wgs84ToGCJ02(29.0741924301, 111.6889679517);
        System.out.println("goole to gcj 02：" + gcj.toString());
        Map<String, Double> bd = gcj02ToBD09(gcj.get("lat"), gcj.get("lng"));
        System.out.println("gcj to bd:" + bd.toString());


        Map<String, Double> bTog = bdo9ToGCJ02(bd.get("lat"), bd.get("lng"));
        System.out.println("bd to gcj:" + bTog.toString());
        System.out.println("gcj to wgs:" + gcj02ToWGS84(bTog.get("lat"), bTog.get("lng")));

    }

}
