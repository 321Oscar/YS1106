package weidong.com.ys1106.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import weidong.com.ys1106.R;
import weidong.com.ys1106.Utils.AnalysisUtils;
import weidong.com.ys1106.Utils.CommonRequest;
import weidong.com.ys1106.Utils.CommonResponse;
import weidong.com.ys1106.Utils.Constant;
import weidong.com.ys1106.Utils.MD5Utils;
import weidong.com.ys1106.Utils.MyToast;
import weidong.com.ys1106.Utils.ResponseHandle;
import weidong.com.ys1106.Utils.UserInfo;
/*
* 注册界面
* */
public class RegisterActivity extends BasicActivity {
    private UserInfo userInfo;
    private ProgressDialog wait1;
    private EditText acc;
    private EditText pass;
    private TextView wrong_pass_show;

    private boolean WRONGPASS = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ImageView finish = findViewById(R.id.btn_finish);
        Button register = findViewById(R.id.btn_commit);

        acc = findViewById(R.id.et_acc);
        pass = findViewById(R.id.et_pass);
        wrong_pass_show = findViewById(R.id.wrong_psw);

        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 6) {
                    wrong_pass_show.setText("不少于6位");
                    wrong_pass_show.setVisibility(View.VISIBLE);
                    WRONGPASS = true;
                } else if (s.length() > 11) {
                    wrong_pass_show.setText("不多于11位");
                    wrong_pass_show.setVisibility(View.VISIBLE);
                    WRONGPASS = true;
                } else {
                    wrong_pass_show.setVisibility(View.INVISIBLE);
                    WRONGPASS = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int res1 = getInfo();
                if (res1 == 1) {
                    if (!WRONGPASS){
                        initWaitDialog("注册中...");
                        register(userInfo);
                    }else{
                        MyToast.MyToastShow(RegisterActivity.this, "密码格式错误！");
                    }
                } else {
                    MyToast.MyToastShow(RegisterActivity.this, "请填写用户名密码！");
                }
            }
        });
    }

    //把界面上的信息绑定到UserInfo中
    private int getInfo() {
        userInfo = new UserInfo();

        if (acc.getText().toString().isEmpty() || pass.getText().toString().isEmpty()) {
            return 0;
        } else {
            userInfo.setAccount(acc.getText().toString());
            userInfo.setPass(pass.getText().toString().trim());
            return 1;
        }
    }

    private void register(UserInfo info) {
        //请求数据集
        CommonRequest request = new CommonRequest();

        //写入参数的顺序为，0账户名，1密码，2真实姓名，3性别，4年龄，5手机，6qq，7邮箱
        String[] params = new String[]{
                info.getAccount(),
                MD5Utils.ToMD5(info.getPass())};
        for (int i = 0; i < 2; i++) {
            String code = Integer.toString(i);
            request.addRequestParam(code, params[i]);
        }

        //标识 注册
        request.setRequestCode("2");

        sendHttpPostRequst(Constant.URL_Login, request, new ResponseHandle() {
            @Override
            public void success(CommonResponse response) {
                regSuc();
            }

            @Override
            public void failure(String failCode) {
                DelyWait();
                String Msg = "";
                //10,01,0
                switch (failCode) {
                    case "10":
                        Msg = "账号已存在";
                        break;
                    case "01":
                        Msg = "注册失败";
                        break;
                    case "0":
                        Msg = "数据库出错";
                        break;
                }
                SetFailDialog(Msg);
            }
        });

    }

    private void regSuc() {
        DelyWait();
        save();
        //成功之后跳转到首页
        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void initWaitDialog(String Msg) {
        wait1 = new ProgressDialog(RegisterActivity.this);
        wait1.setMessage(Msg);
        wait1.show();
    }

    //基本信息存入本地
    private void save() {
        AnalysisUtils.saveLoginStatus(RegisterActivity.this, true, userInfo.getAccount());
        AnalysisUtils.addloginPass(RegisterActivity.this, userInfo.getPass(), userInfo.getAccount());
    }

    //progressDialog 延时
    private void DelyWait() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int progress = 0;
                while (progress < 5) {
                    try {
                        Thread.sleep(1000);
                        progress++;
                        wait1.incrementProgressBy(progress);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    wait1.dismiss();
                }
            }
        }).start();
    }

    private void SetFailDialog(String Msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("注册失败").setMessage(Msg).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }
}
