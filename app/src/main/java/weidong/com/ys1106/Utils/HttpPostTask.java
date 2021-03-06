package weidong.com.ys1106.Utils;

import android.os.AsyncTask;
import android.os.Handler;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpPostTask extends AsyncTask<String, String, String> {

    /*BasicActivity 中的基础问题的处理mHandle*/
    private Handler mHandle;

    /*返回信息处理回调接口*/
    private ResponseHandle rHandle;

    /*请求类对象*/
    private CommonRequest request;

    public HttpPostTask(CommonRequest request, Handler handler, ResponseHandle rHandle) {
        this.request = request;
        this.mHandle = handler;
        this.rHandle = rHandle;
    }

    @Override
    protected String doInBackground(String... strings) {
        StringBuilder resultBuf = new StringBuilder();
        try {
            URL url = new URL(strings[0]);

            //step1:使用URL打开一个HttpConnection连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            //step2：设置HTTP Connection连接相关属性
            connection.setRequestProperty("Content-type", "application/json;charset=utf-8");
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            connection.setDoOutput(true);
            connection.setDoInput(true);

            //如果是POST方法，需要在获取输入流之前向连接写入POST参数
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
//            out.writeBytes(request.getJsonStr());
            //上面的方法 中文传输出现乱码 改为下面的方法
            out.write(request.getJsonStr().getBytes());
            out.flush();

            //step3：打开连接输入流读取返回报文 （在此步骤才真正开始网络请求）
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                //通过连接的输入流 获取 下发报文，然后就是Java的流处理
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    resultBuf.append(line);
                }
                return resultBuf.toString();
            } else {
                //异常情况，如404/500
                mHandle.obtainMessage(Constant.HANDLER_HTTP_RECEIVE_FIAL,
                        "["+responseCode+"]"+connection.getResponseMessage()).sendToTarget();
            }
        } catch (IOException e) {
            //网络请求过程中发生IO异常
            e.printStackTrace();
        }
        return resultBuf.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        if(rHandle != null){
            if(!"".equals(s)){
                CommonResponse response = new CommonResponse(s);
                //这里的业务完成的判断Code也是与服务器约定好的
                if("11".equals(response.getResCode())){//正确
                    rHandle.success(response);
                }else {
                    rHandle.failure(response.getResCode());
                }
            }
        }
        super.onPostExecute(s);
    }
}
