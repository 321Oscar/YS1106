package weidong.com.ys1106.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/*
* 处理服务器返回的数据
* */
public class CommonResponse {

    //交易状态代码
    private String resCode="";

    //交易失败说明
    private String resMsg="";

    //简单信息
    private HashMap<String,String> propertyMap;

    //列表类信息
    private ArrayList<HashMap<String,String>> mapList;

    //报文返回构造函数
    public CommonResponse(String responseString){
        propertyMap = new HashMap<>();
        mapList = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(responseString);

            resCode = root.getString("resCode");
            resMsg = root.getString("resMsg");

            JSONObject property = root.optJSONObject("property");
            if(property!=null){
                parseProperty(property,propertyMap);
            }

            JSONArray list = root.getJSONArray("list");
            if(list != null){
                parseList(list);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
    * 简单信息部分的解析到@link CommonResponse#propertyMap
    *
    * @param property 信息部分
    * @param targetMap 解析后保存目标
    * */
    private void parseProperty(JSONObject property,HashMap<String,String> targetMap){
        Iterator it = property.keys();
        while (it.hasNext()){
            String key = it.next().toString();
            Object value = property.opt(key);
            targetMap.put(key,value.toString());
        }
    }

    /*
     * 简单信息部分的解析到@link CommonResponse#mapList
     *
     * @param property 信息部分
     * */
    private void parseList(JSONArray list){
        int i = 0;
        while(i<list.length()){
            HashMap<String,String> map = new HashMap<>();
            try {
                parseProperty(list.getJSONObject(i++),map);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mapList.add(map);
        }
    }

    public String getResCode(){
        return resCode;
    }

    public String getResMsg(){
        return resMsg;
    }

    public HashMap<String,String> getPropertyMap(){
        return propertyMap;
    }

    public ArrayList<HashMap<String,String>> getDataList(){
        return mapList;
    }
}
