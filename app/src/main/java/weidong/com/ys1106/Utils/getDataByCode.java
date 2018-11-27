package weidong.com.ys1106.Utils;

import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.HashMap;

public class getDataByCode {
    private ArrayList<VideoInfo> videoInfoArrayList = new ArrayList<>();
    private ArrayList<UserInfo> userInfoArrayList = new ArrayList<>();
    private ArrayList<YangShInfo> yangShInfoArrayList = new ArrayList<>();

    /*请求码
     * 1 —— 登录
     * 2 —— 注册
     * 3 —— 视频信息
     * 4 —— 查找用户基本信息
     * */
    private String requestCode;

    public String getResultCode() {
        return resultCode;
    }

    private void setResultCode(String code){
        resultCode = code;
    }

    //返回结果码
    private String resultCode;

    /*
    * @param requestCode 请求码
    * @param username
    * */
    public void getDataFrom(final String requestCode, String[] params){

        CommonRequest request = new CommonRequest();
        //设置请求码
        request.setRequestCode(requestCode);

        //根据请求码 分别加入参数
        switch (requestCode){
            case "1"://登录 用户名+密码
                request.addRequestParam("account", params[0]);
                request.addRequestParam("password", params[1]);
                break;
        }

        //发送请求
        sendHttpPostRequst(Constant.URL_Login, request, new ResponseHandle() {
            @Override
            public void success(CommonResponse response) {
                //根据请求码 判断哪些是数据集合报文 哪些是只有数字报文
                switch (requestCode){
                    //登录 只有结果码 无需解析数据集合
                    case "1":
                        setResultCode(response.getResCode());
                        break;
                        //视频信息 解析数据集合
                    case "3":
                        if (response.getDataList().size() > 0) {
                            //数据赋值
                            setDataList(response.getDataList());
                        }
                        break;
                }

            }

            @Override
            public void failure(String failCode) {
                //数据库访问失败
                setResultCode(failCode);
            }
        });

    }

    public void setDataList(ArrayList<HashMap<String, String>> list) {
        //根据请求码的类型 解析报文
        // 3 32 视频信息
        // 4 用户信息
        // 5 关注信息
        switch (requestCode){
            case "3"://视频信息
            case "32":
                for (int i = 0; i < list.size(); i++) {
                    VideoInfo videoInfo = new VideoInfo();
                    videoInfo.setVideoType(list.get(i).get("type"));//类型名称
                    videoInfo.setVideoDes(list.get(i).get("des"));//视频描述
                    videoInfo.setVideoTitle(list.get(i).get("title"));//视频标题
                    videoInfo.setImgUrl(list.get(i).get("imgurl"));//图片路径
                    videoInfo.setVideoURL(list.get(i).get("url"));//视频路径
                    videoInfoArrayList.add(videoInfo);
                }
                break;
            case "4":
                break;
            case "5":
                break;
        }

    }

    public ArrayList<VideoInfo> getVideoInfoArrayList() {
        return videoInfoArrayList;
    }

    public ArrayList<UserInfo> getUserInfoArrayList() {
        return userInfoArrayList;
    }

    public ArrayList<YangShInfo> getYangShInfoArrayList() {
        return yangShInfoArrayList;
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
