package weidong.com.ys1106.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class CommonRequest {

    /*请求码，类似于接口号
    * 1 -- 登录
    * 2 -- 注册
    * 3 -- 所有视频的信息
    * 32 -- 某一类的视频
    * 4 -- 用户基本信息
    * 5 -- 用户关注的类型
    * 6 -- 修改密码
    * 7 -- 修改基本信息
    * 80 -- 关注某一类
    * 81 -- 取消关注某一类
    * */
    private String requestCode;

    /*用户更新数据请求码
    * 1 —— 用户名
    * //2 —— 密码 （修改密码与其不是一样的业务操作，删除）
    * 3 —— 性别
    * 4 —— 年龄
    * 5 —— qq
    * 6 —— 手机
    * 7 —— 邮箱
    * 8 -- 姓名
    * */
    private String requestUpCode;

    /*请求参数*/
    private HashMap<String,String> requestParam;

    public CommonRequest(){
        requestCode = "";
        requestUpCode = "";
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
            object.put("UpdateCode",requestUpCode);
            object.put("params",param);//参数列表的name以及内容
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    public void setRequestUpCode(String requestUpCode) {
        this.requestUpCode = requestUpCode;
    }
}

