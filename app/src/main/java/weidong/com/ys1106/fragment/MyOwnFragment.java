package weidong.com.ys1106.fragment;


import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;

import weidong.com.ys1106.Activity.ChangeInfoActivity;
import weidong.com.ys1106.Activity.ChangePassActivity;
import weidong.com.ys1106.MainActivity;
import weidong.com.ys1106.R;
import weidong.com.ys1106.Utils.AnalysisUtils;
import weidong.com.ys1106.Utils.CommonRequest;
import weidong.com.ys1106.Utils.CommonResponse;
import weidong.com.ys1106.Utils.Constant;
import weidong.com.ys1106.Utils.MyToast;
import weidong.com.ys1106.Utils.ResponseHandle;
import weidong.com.ys1106.Utils.UserInfo;

public class MyOwnFragment extends BasicFragment implements View.OnClickListener {

    private View view;
    private TextView mChangeAccount;
    private TextView mChangesex;
    private TextView mChangeAge;
    private TextView mChangeName;
    private TextView mChangeqq;
    private TextView mChangeEmail;
    private TextView mChangePh;
    private TextView mChangeUser;
    private TextView mChangePass;
    private TextView mExit;
    private TextView mDeleteUser;

    private TableRow mAccount;
    private TableRow mAge;
    private TableRow mSex;
    private TableRow mName;
    private TableRow mQQ;
    private TableRow mEmail;
    private TableRow mPh;

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
        initView(view);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //更换用户名
            case R.id.change_account:
                updateUserInfo("account",mChangeAccount);
                break;
            //更改性别
            case R.id.change_sex:
                changeSex(mChangesex);
                break;
            //更改用户年龄
            case R.id.change_age:
                MyToast.MyToastShow(getActivity(), "有用");
                break;
//          更改用户姓名
            case R.id.change_name:
                break;
//          更改qq
            case R.id.change_qq:
                break;
//          更改邮箱
            case R.id.change_email:
                break;
//          更改手机
            case R.id.change_ph:
                break;
            //注销账户
            case R.id.deleteaccount:
                DeleteUser();
                break;
            //退出程序
            case R.id.exit:
                ExitThis();
                break;
            //更换用户
            case R.id.btn_changeUser:
                ChangeUser();
                break;
            //更改密码
            case R.id.btn_changepass:
                ChangePass();
                break;
        }
    }

    /*
     *获取数据
     *  */
    public void initData() {
        CommonRequest request = new CommonRequest();
        //请求码：4 —— 查找用户基本信息
        request.setRequestCode("4");
        //使用用户的用户名查找
        String acccount = AnalysisUtils.readloginUserName(getActivity());
        System.out.println("account:" + acccount);

        request.addRequestParam("account", acccount);

        sendHttpPostRequst(Constant.URL_Login, request, new ResponseHandle() {
            @Override
            public void success(CommonResponse response) {
                if (response.getDataList().size() > 0) {
                    UserInfo info = new UserInfo();
                    info.setName(response.getDataList().get(0).get("name"));
                    info.setSex(response.getDataList().get(0).get("sex"));
                    System.out.println("-----初始性别："+response.getDataList().get(0).get("sex"));
                    info.setAge(response.getDataList().get(0).get("age"));
                    info.setQq(response.getDataList().get(0).get("qq"));
                    info.setPh(response.getDataList().get(0).get("phone"));
                    info.setEmail(response.getDataList().get(0).get("email"));
                    initTextViewData(info);
                } else {
                    MyToast.MyToastShow(getActivity(), "列表无数据");
                }
            }

            @Override
            public void failure(String failCode, String failMsg) {
                MyToast.MyToastShow(getActivity(), "数据库连接失败");
            }
        });
    }

    private void initTextViewData(UserInfo info){
        mChangeAccount.setText(AnalysisUtils.readloginUserName(getActivity()));
        mChangesex.setText(info.getSex());
        if(info.getAge().equals("null")){
            mChangeAge.setText("未填");
        }else{
            mChangeAge.setText(info.getAge());
        }
        if(info.getName().equals("null")){
            mChangeName.setText("未填");
        }else{
            mChangeName.setText(info.getName());
        }
        if(info.getQq().equals("null")){
            mChangeqq.setText("未填");
        }else{
            mChangeqq.setText(info.getQq());
        }
        if(info.getEmail().equals("null")){
            mChangeEmail.setText("未填");
        }else{
            mChangeEmail.setText(info.getEmail());
        }
        if(info.getPh().equals("null")){
            mChangePh.setText("未填");
        }else{
            mChangePh.setText(info.getPh());
        }
    }

    /*
     * 初始化控件
     * 绑定控件点击事件
     * */
    public void initView(View view) {
        //设置 改密码，退出，换账户，注销
        mChangePass = view.findViewById(R.id.btn_changepass);
        mChangeUser = view.findViewById(R.id.btn_changeUser);
        mExit = view.findViewById(R.id.exit);
        mDeleteUser = view.findViewById(R.id.deleteaccount);

        mDeleteUser.setOnClickListener(this);
        mChangePass.setOnClickListener(this);
        mChangeUser.setOnClickListener(this);
        mExit.setOnClickListener(this);

        //个人信息修改 用户名，年龄，性别，姓名，qq，邮箱，手机
        mAccount = view.findViewById(R.id.change_account);
        mSex = view.findViewById(R.id.change_sex);
        mAge = view.findViewById(R.id.change_age);
        mName = view.findViewById(R.id.change_name);
        mQQ = view.findViewById(R.id.change_qq);
        mEmail = view.findViewById(R.id.change_email);
        mPh = view.findViewById(R.id.change_ph);

        mAccount.setOnClickListener(this);
        mSex.setOnClickListener(this);
        mAge.setOnClickListener(this);
        mName.setOnClickListener(this);
        mQQ.setOnClickListener(this);
        mPh.setOnClickListener(this);
        mEmail.setOnClickListener(this);

        mChangeAccount = view.findViewById(R.id.frag_myOwn_tv_account);
        mChangeUser = view.findViewById(R.id.frag_myOwn_tv_account);
        mChangeAge = view.findViewById(R.id.frag_myOwn_tv_age);
        mChangesex = view.findViewById(R.id.frag_myOwn_tv_sex);
        mChangeName = view.findViewById(R.id.frag_myOwn_tv_name);
        mChangeqq = view.findViewById(R.id.frag_myOwn_tv_qq);
        mChangeEmail = view.findViewById(R.id.frag_myOwn_tv_email);
        mChangePh = view.findViewById(R.id.frag_myOwn_tv_ph);

    }

    //修改密码
    private void ChangePass() {
        Intent intent = new Intent(getActivity(), ChangePassActivity.class);
        startActivity(intent);
    }

    //切换账号
    private void ChangeUser() {
        //清除当前账号信息
        AnalysisUtils.cleanlogin(getActivity());
        //打开新的登录界面
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    //退出程序
    private void ExitThis() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("退出").
                setMessage("退出程序吗？").
                setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).
                setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                    }
                }).show();
    }

    /* 注销账号
     * 数据库中的用户存在标识改为0
     * 先确定密码，密码正确以后再执行注销
     * */
    private void DeleteUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_confirm_pass, null);
        final EditText pass = view.findViewById(R.id.et_confirmpass);
        Button sure = view.findViewById(R.id.btn_cofmpas_sure);

        //确认密码
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!pass.getText().toString().isEmpty()) {
                    if (pass.getText().toString().equals(AnalysisUtils.readloginpass(getActivity()))) {
                        //注销
                        MyToast.MyToastShow(getActivity(), "密码正确！");
                    } else {
                        MyToast.MyToastShow(getActivity(), "密码不正确！");
                    }
                } else {
                    MyToast.MyToastShow(getActivity(), "密码不能为空！");
                }
            }
        });
        builder.setTitle("账号注销").setView(view).show();
    }

    /*
    * 更改用户基本信息
    * @param type 更改的类型
    * */
    private void updateUserInfo(String type,TextView content){
        //更改用户名
        Intent intent = new Intent(getActivity(),ChangeInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("type",type);
        bundle.putString("oldInfo",content.getText().toString());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /*
     * 弹出选择性别
     * @param sex 用户的性别显示textView
     * */
    private void changeSex(final TextView sex) {
        int getsex = 0;
        if (sex.getText().toString().equals("男")) {
            getsex = 0;
        } else if (sex.getText().toString().equals("女")) {
            getsex = 1;
        }
        String[] sexs = new String[]{"男", "女"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("性别").setSingleChoiceItems(sexs, getsex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    //数据库里改变性别
                    changesex(which, sex);
                } else if (which == 1) {
                    changesex(which, sex);
                }
                dialog.dismiss();
            }
        }).show();
    }

    /*
     * 改变显示性别
     * @param sex 属性
     * @param textView 目标textView
     * */
    private void changesex(int sex, TextView textView) {
        if (sex == 0) {
            textView.setText("男");
        } else if (sex == 1) {
            textView.setText("女");
        }
    }


}
