package weidong.com.ys1106.Utils;

public interface ResponseHandle {
    /*
    * 交易成功的处理
    * @param response 格式化报文
    * */
    void success(CommonResponse response);



    /*
    * 报文通信正常，但交易内容失败的处理
    * @param failCode 返回的交易状态码
    * */
    void failure(String failCode);
}
