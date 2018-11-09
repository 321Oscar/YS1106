package weidong.com.ys1106.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.lang.ref.WeakReference;

import weidong.com.ys1106.Utils.CommonRequest;
import weidong.com.ys1106.Utils.HttpPostTask;
import weidong.com.ys1106.Utils.ResponseHandle;

public class BasicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void sendHttpPostRequst(String url, CommonRequest request, ResponseHandle rHandle){
        new HttpPostTask(request,mHandle,rHandle).execute(url);
    }

    private MyHandle mHandle = new MyHandle(this);

    static class MyHandle extends  Handler{
        WeakReference weakReference;
        public MyHandle(BasicActivity activity){
            weakReference = new WeakReference(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

}
