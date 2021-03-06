package weidong.com.ys1106.fragment;


import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
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

    private TextView mChangeAccount;
    private TextView mChangesex;
    private TextView mChangeAge;
    private TextView mChangeName;
    private TextView mChangeqq;
    private TextView mChangeEmail;
    private TextView mChangePh;

    final int CODE = 0x717;

    public MyOwnFragment() {

    }

    public static MyOwnFragment NewInstance() {
        MyOwnFragment fragment = new MyOwnFragment();
        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE && resultCode == 0x711) {//修改成功之后要重新刷新数据
            initData();
        } else if (requestCode == CODE && resultCode == 0x710) {
            MyToast.MyToastShow(getActivity(), "未更改");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myinfo, container, false);

        initData();
        initView(view);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //更换用户名
            case R.id.change_account:
                updateUserInfo("account", mChangeAccount);
                break;
            //更改性别
            case R.id.change_sex:
                changeSex(mChangesex);
                break;
            //更改用户年龄
            case R.id.change_age:
                updateUserInfo("age", mChangeAge);
                break;
//          更改用户姓名
            case R.id.change_name:
                updateUserInfo("name", mChangeName);
                break;
//          更改qq
            case R.id.change_qq:
                updateUserInfo("qq", mChangeqq);
                break;
//          更改邮箱
            case R.id.change_email:
                updateUserInfo("email", mChangeEmail);
                break;
//          更改手机
            case R.id.change_ph:
                updateUserInfo("ph", mChangePh);
                break;
        }
    }

    /*
     * 从数据库中获取数据
     *  */
    public void initData() {
        CommonRequest request = new CommonRequest();

        //请求码：4 —— 查找用户基本信息
        request.setRequestCode("4");

        //使用用户的用户名查找
        String acccount = AnalysisUtils.readloginUserName(getActivity());
        request.addRequestParam("account", acccount);

        sendHttpPostRequest(Constant.URL_Login, request, new ResponseHandle() {
            @Override
            public void success(CommonResponse response) {
                if (response.getDataList().size() > 0) {
                    UserInfo info = new UserInfo();
                    info.setName(response.getDataList().get(0).get("name"));
                    info.setSex(response.getDataList().get(0).get("sex"));
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
            public void failure(String failCode) {
                MyToast.MyToastShow(getActivity(), "数据库连接失败");
            }
        });
    }

    /*
     * 将数据显示在textview里
     * */
    private void initTextViewData(UserInfo info) {
        mChangeAccount.setText(AnalysisUtils.readloginUserName(getActivity()));
        mChangesex.setText(info.getSex());
        if (info.getAge().equals("null")) {
            mChangeAge.setText("未填");
        } else {
            mChangeAge.setText(info.getAge());
        }
        if (info.getName().equals("null")) {
            mChangeName.setText("未填");
        } else {
            mChangeName.setText(info.getName());
        }
        if (info.getQq().equals("null")) {
            mChangeqq.setText("未填");
        } else {
            mChangeqq.setText(info.getQq());
        }
        if (info.getEmail().equals("null")) {
            mChangeEmail.setText("未填");
        } else {
            mChangeEmail.setText(info.getEmail());
        }
        if (info.getPh().equals("null")) {
            mChangePh.setText("未填");
        } else {
            mChangePh.setText(info.getPh());
        }
    }

    /*
     * 初始化控件
     * 绑定控件点击事件
     * */
    public void initView(View view) {

        //个人信息修改 用户名，年龄，性别，姓名，qq，邮箱，手机
        TableRow mAccount = view.findViewById(R.id.change_account);
        TableRow mSex = view.findViewById(R.id.change_sex);
        TableRow mAge = view.findViewById(R.id.change_age);
        TableRow mName = view.findViewById(R.id.change_name);
        TableRow mQQ = view.findViewById(R.id.change_qq);
        TableRow mEmail = view.findViewById(R.id.change_email);
        TableRow mPh = view.findViewById(R.id.change_ph);

        mAccount.setOnClickListener(this);
        mSex.setOnClickListener(this);
        mAge.setOnClickListener(this);
        mName.setOnClickListener(this);
        mQQ.setOnClickListener(this);
        mPh.setOnClickListener(this);
        mEmail.setOnClickListener(this);

        mChangeAccount = view.findViewById(R.id.frag_myOwn_tv_account);
        mChangeAge = view.findViewById(R.id.frag_myOwn_tv_age);
        mChangesex = view.findViewById(R.id.frag_myOwn_tv_sex);
        mChangeName = view.findViewById(R.id.frag_myOwn_tv_name);
        mChangeqq = view.findViewById(R.id.frag_myOwn_tv_qq);
        mChangeEmail = view.findViewById(R.id.frag_myOwn_tv_email);
        mChangePh = view.findViewById(R.id.frag_myOwn_tv_ph);

    }

    /*
     * 更改用户基本信息 跳转到新的Activity中更改相应的信息
     * @param type 更改的类型
     * @param content 更改的textView
     * */
    private void updateUserInfo(String type, TextView content) {
        //更改信息
        Intent intent = new Intent(getActivity(), ChangeInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putString("oldInfo", content.getText().toString());
        intent.putExtras(bundle);
        startActivityForResult(intent, CODE);
    }

    /*
     * 弹出选择性别
     * @param sex 用户的性别显示textView
     * */
    private void changeSex(final TextView sex) {
        //性别码 0 -- 男 1 -- 女 仅在dialog中使用
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
                if (which == 0) {//改为男性
                    //数据库里改变性别
                    ChangeSexInSql(which);
                } else if (which == 1) {//改为女性
                    ChangeSexInSql(which);
                }
                dialog.dismiss();
            }
        }).show();
    }

    /*
     * @param sex 在dialog中的选择，0为男性，1为女性
     * */
    private void ChangeSexInSql(final int sex) {
        String Sex;
        if (sex == 0) { //dialog中显示为男
            Sex = "1"; //数据库中 1 表示男
        } else {
            Sex = "0";
        }

        CommonRequest request = new CommonRequest();

        //设置用户更新信息码
        request.setRequestCode("7");

        //设置用户更新信息类型码
        request.setRequestUpCode("3");

        //添加用户更改的参数 account --用户名, param -- 新的信息
        request.addRequestParam("account", AnalysisUtils.readloginUserName(getActivity()));
        request.addRequestParam("param", Sex);

        sendHttpPostRequest(Constant.URL_Login, request, new ResponseHandle() {
            @Override
            public void success(CommonResponse response) {
                SexSuccess(sex);
            }

            @Override
            public void failure(String failCode) {
                MyToast.MyToastShow(getActivity(), "更改失败！");
            }
        });
    }

    /*修改成功 将text View中的显示也改变*/
    private void SexSuccess(int intsex) {
        ChangeSexInView(intsex, mChangesex);
    }

    /*
     * 改变显示性别
     * @param sex 属性
     * @param textView 目标textView
     * */
    private void ChangeSexInView(int sex, TextView textView) {
        if (sex == 0) {
            textView.setText("男");
        } else if (sex == 1) {
            textView.setText("女");
        }
    }


}
