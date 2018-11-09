package weidong.com.ys1106.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class CommonRequest {

    /*请求码，类似于接口号*/
    private String requestCode;

    /*请求参数*/
    private HashMap<String,String> requestParam;

    public CommonRequest(){
        requestCode = "";
        requestParam = new HashMap<>();
    }

//    设置请求代码
    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }

    /*
    * 为请求报文设置参数
    * @param paramkey 参数名
    * @param paramvalue 参数值
    * */
    public void addRequestParam(String paramkey,String paramvalue) {
        requestParam.put(paramkey,paramvalue);
    }

    /*
    * 将请求报文组装成Json格式的字符串，以便进行网络发送
    * @return 请求报文的Json字符串
    * */
    public String getJsonStr(){
        JSONObject object = new JSONObject();
        JSONObject param = new JSONObject(requestParam);//参数列表
        try {
            object.put("ActionType",requestCode);
            object.put("params",param);//参数列表的name以及内容
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }
}

