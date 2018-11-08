package weidong.com.ys1106.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import weidong.com.ys1106.R;
import weidong.com.ys1106.Utils.UserInfo;

public class MyOwnFragment extends Fragment {


    private View view;

    public MyOwnFragment() {

    }

    public static MyOwnFragment NewInstance(){
        MyOwnFragment fragment = new MyOwnFragment();
        return  fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_myinfo,container,false);
        //get  user tag
        String tag = "user1";
        initData(tag);
        return view;
    }

    /*
    * @param tag 用户标识*/
    public void initData(String tag){
        //账号，姓名，年龄，性别，QQ，手机，邮箱
        String[] userinfos = new String[]{"userAccount1","userName1","qq1","age","sex","ph","email"};
        UserInfo  info = new UserInfo();
        info.setAccount(userinfos[0]);
        info.setName(userinfos[1]);
        info.setQq(userinfos[2]);

        TextView name = view.findViewById(R.id.frag_myOwn_tv_name);
        TextView account = view.findViewById(R.id.frag_myOwn_tv_account);
        TextView age = view.findViewById(R.id.frag_myOwn_tv_age);
        TextView sex = view.findViewById(R.id.frag_myOwn_tv_sex);
        TextView qq = view.findViewById(R.id.frag_myOwn_tv_qq);
        TextView ph = view.findViewById(R.id.frag_myOwn_tv_ph);
        TextView email = view.findViewById(R.id.frag_myOwn_tv_email);

        name.setText(info.getName());
        account.setText(info.getAccount());
        qq.setText(info.getQq());
    }
}
