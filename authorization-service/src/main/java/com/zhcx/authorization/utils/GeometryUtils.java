package com.zhcx.authorization.utils;

import com.alibaba.fastjson.JSONArray;
import com.esri.core.geometry.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.sf.geographiclib.Geodesic;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 *
 * @author
 * @version 1.0
 */
public class GeometryUtils {

    private static double EARTH_RADIUS = 6378137;

    /**
     *
     * @param inlat  开始纬度
     * @param inlon 开始经度
     * @param outlat 结束纬度
     * @param outlon  结束经度
     * @param regionPoints  围栏字符串
     * @return
     */
    public static boolean polygonContainPoint(Double inlat,Double inlon,Double outlat,Double outlon,String regionPoints, String type){
        Gson gson = new Gson();
        List<Double[]> list = gson.fromJson(regionPoints,new TypeToken<List<Double[]>>(){}.getType());
        Polygon polygon = new Polygon();
        int pos = 0;

        if(StringUtils.equals(type, Constants.GEO_BD09)){
            //百度坐标转GPS坐标
            for(Double[] point : list){
                Map<String, Double> map = TransferAxesUitl.bd09ToWGS84(point[1], point[0]);
                point[0] = map.get("lng");
                point[1] = map.get("lat");
            }
        }
        if(StringUtils.equals(type, Constants.GEO_GCJ02)){
            //火星（谷歌）坐标转GPS坐标
            for(Double[] point : list){
                Map<String, Double> map = TransferAxesUitl.gcj02ToWGS84(point[1], point[0]);
                point[0] = map.get("lng");
                point[1] = map.get("lat");
            }
        }

        //构建围栏
        for(Double[] point : list){
            if(pos == 0){
                polygon.startPath(point[0],point[1]);
            }else{
                polygon.lineTo(new Point(point[0],point[1]));
            }
            pos++;
        }

        //闭合
        Double[] point = list.get(0);
        polygon.lineTo(new Point(point[0],point[1]));
        //起止点
        Point inPoint = new Point(inlon,inlat);
        Point outPoint = new Point(outlon,outlat);

        //起止点需要有一个在允许的经营区域内
        if(OperatorWithin.local().execute(inPoint,polygon, SpatialReference.create(4326),null) ||
                OperatorWithin.local().execute(outPoint,polygon, SpatialReference.create(4326),null)){
            return true;
        }
        return false;
    }
    
    /**
    *
    * @param inlat  开始纬度
    * @param inlon 开始经度
    * @param outlat 结束纬度
    * @param outlon  结束经度
    * @param regionPoints  围栏字符串
    * @return
    */
   public static boolean polygonContainPoint(Double inlat,Double inlon,String regionPoints, String type){
       Gson gson = new Gson();
       List<Double[]> list = gson.fromJson(regionPoints,new TypeToken<List<Double[]>>(){}.getType());
       Polygon polygon = new Polygon();
       int pos = 0;

       if(StringUtils.equals(type, Constants.GEO_BD09)){
           //百度坐标转GPS坐标
           for(Double[] point : list){
               Map<String, Double> map = TransferAxesUitl.bd09ToWGS84(point[1], point[0]);
               point[0] = map.get("lng");
               point[1] = map.get("lat");
           }
       }
       if(StringUtils.equals(type, Constants.GEO_GCJ02)){
           //火星（谷歌）坐标转GPS坐标
           for(Double[] point : list){
               Map<String, Double> map = TransferAxesUitl.gcj02ToWGS84(point[1], point[0]);
               point[0] = map.get("lng");
               point[1] = map.get("lat");
           }
       }

       //构建围栏
       for(Double[] point : list){
           if(pos == 0){
               polygon.startPath(point[0],point[1]);
           }else{
               polygon.lineTo(new Point(point[0],point[1]));
           }
           pos++;
       }

       //闭合
       Double[] point = list.get(0);
       polygon.lineTo(new Point(point[0],point[1]));
       //起止点
       Point inPoint = new Point(inlon,inlat);

       //起止点需要有一个在允许的经营区域内
       if(OperatorWithin.local().execute(inPoint,polygon, SpatialReference.create(4326),null)){
           return true;
       }
       return false;
   }


    public static boolean circleContainPoint(Double onLat, Double onLng, Double offLat, Double offLng, String center, Double radius, String type){
        JSONArray centerXY = JSONArray.parseArray(center);
        Object lngObj = centerXY.get(0);
        Object latObj = centerXY.get(1);
        Double centerLat = Double.parseDouble(latObj.toString());
        Double centerLng = Double.parseDouble(lngObj.toString());
        //百度坐标转GPS坐标
        if(StringUtils.equals(type, Constants.GEO_BD09)){
            Map<String, Double> map = TransferAxesUitl.bd09ToWGS84(centerLat, centerLng);
            centerLat = map.get("lat");
            centerLng = map.get("lng");
        }
        //火星（谷歌）坐标转GPS坐标
        if(StringUtils.equals(type, Constants.GEO_GCJ02)){
            Map<String, Double> map = TransferAxesUitl.gcj02ToWGS84(centerLat, centerLng);
            centerLat = map.get("lat");
            centerLng = map.get("lng");
        }
        Double distance1 = getDistance(onLat, onLng, centerLat, centerLng);
        Double distance2 = getDistance(offLat, offLng, centerLat, centerLng);
        if(distance1 <= radius || distance2 <= radius){
            return true;
        }
        return false;
    }
    
    public static boolean circleContainPoint(Double onLat, Double onLng, String center, Double radius, String type){
        JSONArray centerXY = JSONArray.parseArray(center);
        Object lngObj = centerXY.get(0);
        Object latObj = centerXY.get(1);
        Double centerLat = Double.parseDouble(latObj.toString());
        Double centerLng = Double.parseDouble(lngObj.toString());
        //百度坐标转GPS坐标
        if(StringUtils.equals(type, Constants.GEO_BD09)){
            Map<String, Double> map = TransferAxesUitl.bd09ToWGS84(centerLat, centerLng);
            centerLat = map.get("lat");
            centerLng = map.get("lng");
        }
        //火星（谷歌）坐标转GPS坐标
        if(StringUtils.equals(type, Constants.GEO_GCJ02)){
            Map<String, Double> map = TransferAxesUitl.gcj02ToWGS84(centerLat, centerLng);
            centerLat = map.get("lat");
            centerLng = map.get("lng");
        }
        Double distance1 = getDistance(onLat, onLng, centerLat, centerLng);
        if(distance1 <= radius){
            return true;
        }
        return false;
    }

    /**
     * 计算两个经纬度之间的距离  结果单位：米
     * @param onLatStr
     * @param onLngStr
     * @param offLatStr
     * @param offLngStr
     * @return
     */

    public static double  getDistance(String onLatStr, String onLngStr, String offLatStr, String offLngStr) {
        Double onLat = Double.parseDouble(onLatStr);
        Double onLng = Double.parseDouble(onLngStr);
        Double offLat = Double.parseDouble(offLatStr);
        Double offLng = Double.parseDouble(offLngStr);

        return getDistance(onLat, onLng, offLat, offLng);
    }

    public static Double getDistance(Double onLat, Double onLng, Double offLat, Double offLng) {
        return Geodesic.WGS84.Inverse(onLat, onLng, offLat, offLng).s12;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }


    public static void main(String[] args){
        String cordinate = "[[111.667145,29.025496],[111.667145,29.052717],[111.723702,29.025496],[111.723702,29.052717],[111.667145,29.025496]]";
        JSONArray jsonArray = JSONArray.parseArray(cordinate);
        jsonArray.add(jsonArray.get(0).toString());
        System.out.println(jsonArray);
        Double onY = 29.030;
        Double onX = 111.668;
        Double offY = 29.031;
        Double offX = 111.669;
        Boolean result = polygonContainPoint(onY, onX, offY, offX, cordinate, Constants.GEO_WGS84);
        System.out.println(result);
//        System.out.println(getDistance("35.00", "111.668", "35.00", "111.669"));
        Double distance1 = getDistance(onY, onX, 29.025496, 111.667145);
        Double distance2 = getDistance(offY, offX, 29.025496, 111.667145);
        Boolean containPoint = circleContainPoint(onY, onX, offY, offX, "[111.667145,29.025496]", 506.11, Constants.GEO_WGS84);
        System.out.println(distance1);
        System.out.println(distance2);
        System.out.println(containPoint);
    }
}
