package weidong.com.ys1106.Utils;

import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.HashMap;

public class GetData {

    /*
    * @param rescode 请求码
    * @param username
    * */
    public ArrayList<HashMap<String,String>> getData(String rescode,String username){

        final ArrayList<HashMap<String, String>>[] list = new ArrayList[1];
        final String[] res = new String[]{"00"};

        CommonRequest request = new CommonRequest();

        /*请求码
        * 1 —— 登录
        * 2 —— 注册
        * 3 —— 视频信息
        * 4 —— 查找用户基本信息
        * */
        request.setRequestCode(rescode);

        //使用用户的用户名查找
        request.addRequestParam("account", username);

        sendHttpPostRequst(Constant.URL_Login, request, new ResponseHandle() {
            @Override
            public void success(CommonResponse response) {
                if (response.getDataList().size() > 0) {
                    list[0] = response.getDataList();
                    System.out.println("Weidong:"+list[0].get(0).get("qq"));
                    res[0] = "11";
                } else {
                    res[0] = "10";//list没有数据
                }
            }

            @Override
            public void failure(String failCode, String failMsg) {
                res[0] = "00";//数据库访问失败
            }
        });
        System.out.println("Weidong2:"+list[0].get(0).get("qq"));
        return list[0];
    }

    protected void sendHttpPostRequst(String url, CommonRequest request, ResponseHandle rHandle){
        new HttpPostTask(request,mHandle,rHandle).execute(url);
    }

    private Handler mHandle = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            return false;
        }
    });
}
