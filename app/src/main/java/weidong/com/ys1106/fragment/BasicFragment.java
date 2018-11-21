package weidong.com.ys1106.fragment;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;



import weidong.com.ys1106.Utils.CommonRequest;
import weidong.com.ys1106.Utils.HttpPostTask;
import weidong.com.ys1106.Utils.ResponseHandle;

public class BasicFragment extends Fragment {
    protected void sendHttpPostRequest(String url, CommonRequest request, ResponseHandle rHandle){
        new HttpPostTask(request,mHandle,rHandle).execute(url);
    }

    private Handler mHandle = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            return false;
        }
    });
}
