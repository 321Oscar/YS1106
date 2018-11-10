package weidong.com.ys1106.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import weidong.com.ys1106.R;
import weidong.com.ys1106.Utils.CommonRequest;
import weidong.com.ys1106.Utils.CommonResponse;
import weidong.com.ys1106.Utils.Constant;
import weidong.com.ys1106.Utils.GetData;
import weidong.com.ys1106.Utils.MyToast;
import weidong.com.ys1106.Utils.ResponseHandle;
import weidong.com.ys1106.Utils.UserInfo;
import weidong.com.ys1106.Utils.VideoInfo;

public class MyOwnFragment extends BasicFragment {

    private View view;
    private UserInfo userInfos = new UserInfo();
    private SharedPreferences sharedPreferences;

    public MyOwnFragment() {

    }

    public static MyOwnFragment NewInstance() {
        MyOwnFragment fragment = new MyOwnFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_myinfo, container, false);

        initData();

        return view;
    }

    /*
     *获取数据
      *  */
    public void initData() {
        final TextView name = view.findViewById(R.id.frag_myOwn_tv_name);
        final TextView account = view.findViewById(R.id.frag_myOwn_tv_account);
        final TextView age = view.findViewById(R.id.frag_myOwn_tv_age);
        final TextView sex = view.findViewById(R.id.frag_myOwn_tv_sex);
        final TextView qq = view.findViewById(R.id.frag_myOwn_tv_qq);
        final TextView ph = view.findViewById(R.id.frag_myOwn_tv_ph);
        final TextView email = view.findViewById(R.id.frag_myOwn_tv_email);

        CommonRequest request = new CommonRequest();
        //请求码：4 —— 查找用户基本信息
        request.setRequestCode("4");
        //使用用户的用户名查找
        sharedPreferences = getActivity().getSharedPreferences("userinfo",Context.MODE_PRIVATE);
        String acccount = sharedPreferences.getString("account","");
        System.out.println("account:"+acccount);

        request.addRequestParam("account",acccount);

        sendHttpPostRequst(Constant.URL_Login, request, new ResponseHandle() {
            @Override
            public void success(CommonResponse response) {
                if (response.getDataList().size() > 0){
                    name.setText(response.getDataList().get(0).get("name"));
                    account.setText(sharedPreferences.getString("account",""));
                    age.setText(response.getDataList().get(0).get("age"));
                    if("1".equals(response.getDataList().get(0).get("sex"))){
                        sex.setText("男");
                    }else if("0".equals(response.getDataList().get(0).get("sex"))){
                        sex.setText("女");
                    }
                    qq.setText(response.getDataList().get(0).get("qq"));
                    ph.setText(response.getDataList().get(0).get("phone"));
                    email.setText(response.getDataList().get(0).get("email"));
                }else {
                    MyToast.MyToastShow(getActivity(),"列表无数据");
                }
            }

            @Override
            public void failure(String failCode, String failMsg) {
                MyToast.MyToastShow(getActivity(),"数据库连接失败");
            }
        });
    }
}
