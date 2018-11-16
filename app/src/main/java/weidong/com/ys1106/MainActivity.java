package weidong.com.ys1106;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import weidong.com.ys1106.Activity.BasicActivity;
import weidong.com.ys1106.Activity.RegisterActivity;
import weidong.com.ys1106.Activity.HomeActivity;
import weidong.com.ys1106.Utils.AnalysisUtils;
import weidong.com.ys1106.Utils.CommonRequest;
import weidong.com.ys1106.Utils.CommonResponse;
import weidong.com.ys1106.Utils.Constant;
import weidong.com.ys1106.Utils.MD5Utils;
import weidong.com.ys1106.Utils.MyToast;
import weidong.com.ys1106.Utils.ResponseHandle;
import weidong.com.ys1106.Utils.UserInfo;

public class MainActivity extends BasicActivity {

    private Button mBtnLogin;
    private TextView mTvRegister;

    private CheckBox cb_rem;
    private CheckBox cb_auto;
    private SharedPreferences sp;
    private ProgressDialog loginwait = null;

    private long lastBack = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final EditText Account = findViewById(R.id.login_et_name);
        final EditText pass = findViewById(R.id.login_et_pass);

        //判断是否已经登录
        if (AnalysisUtils.readLoginStatus(MainActivity.this)) {
            UserInfo info = new UserInfo();
            info.setAccount(AnalysisUtils.readloginUserName(MainActivity.this));
            MyToast.SysInfo("----password-----" + AnalysisUtils.readloginpass(MainActivity.this, AnalysisUtils.readloginUserName(MainActivity.this)));
            info.setPass(AnalysisUtils.readloginpass(MainActivity.this, AnalysisUtils.readloginUserName(MainActivity.this)));
            Account.setText(info.getAccount());
            pass.setText(info.getPass());
            login(info);
        }
//        //自动登录
//        cb_rem = findViewById(R.id.login_cb_rem);
//        cb_auto = findViewById(R.id.login_cb_auto);
//        sp = getSharedPreferences("userinfo", MODE_PRIVATE);
//
//
//        //是否记住密码和自动登录
//        if (sp.getString("rem", "").equals("1")) {
//            cb_rem.setChecked(true);
//            Account.setText(AnalysisUtils.readloginUserName(MainActivity.this));
//            pass.setText(AnalysisUtils.readloginpass(MainActivity.this));
//            if ((AnalysisUtils.readloginauto(MainActivity.this).equals("1"))) {
//                cb_auto.setChecked(true);
//                UserInfo autoinfo = new UserInfo();
//                autoinfo.setAccount(AnalysisUtils.readloginUserName(MainActivity.this));
//                autoinfo.setPass(AnalysisUtils.readloginpass(MainActivity.this));
//                login(autoinfo);
//            }
//        }
//
//        //自动登录选中则自动选中记住密码
//        cb_auto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    cb_rem.setChecked(true);
//                }
//            }
//        });
//
//        //若没有选中记住密码，则自动登录也设为未选中
//        cb_rem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(!isChecked){
//                    cb_auto.setChecked(false);
//                }
//            }
//        });

        //跳转到注册界面
        mTvRegister = findViewById(R.id.register);
        mTvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        //登录事件
        mBtnLogin = findViewById(R.id.login);
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Account.getText().toString().isEmpty() || pass.getText().toString().isEmpty()) {
                    MyToast.MyToastShow(MainActivity.this, "用户名和密码不能为空！");
                } else {
                    UserInfo info = new UserInfo();
                    info.setAccount(Account.getText().toString());
                    info.setPass(pass.getText().toString().trim());
                    login(info);
                }

            }
        });
    }

    //实现登录 用MySQL+servlet
    private void login(final UserInfo info) {
        //请求数据集
        CommonRequest request = new CommonRequest();

        //标识 登录
        request.setRequestCode("1");

        //添加登录参数
        String Md5Psw = MD5Utils.ToMD5(info.getPass());
        request.addRequestParam("account", info.getAccount());
        request.addRequestParam("password", Md5Psw);


        //登录等待Dialog
        loginwait = new ProgressDialog(MainActivity.this);
        loginwait.setMessage("登录中...");
        loginwait.show();

        //向服务端发送请求
        sendHttpPostRequst(Constant.URL_Login, request, new ResponseHandle() {
            //登录成功
            @Override
            public void success(CommonResponse response) {
                //延时效果
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int progress = 0;
                        while (progress < 5) {
                            try {
                                Thread.sleep(1000);
                                progress++;
                                loginwait.incrementProgressBy(progress);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            loginwait.dismiss();
                        }
                    }
                }).start();

                //将用户信息保存
                AnalysisUtils.saveLoginStatus(MainActivity.this, true, info.getAccount());
                AnalysisUtils.addloginPass(MainActivity.this, info.getPass(), info.getAccount());

                MyToast.SysInfo("----passwordsave----" + AnalysisUtils.readloginpass(MainActivity.this, info.getAccount()));

//                //如果用户选择记住密码，则在存储用户的选择
//                if (cb_rem.isChecked()) {
//                    editor.putString("rem", "1");
//                    if (cb_auto.isChecked()) {
//                        editor.putString("auto_login", "1");
//                    } else {
//                        editor.putString("auto_login", "0");
//                    }
//                } else {
//                    editor.putString("rem", "0");
//                }
//                editor.apply();

                //成功之后跳转到首页
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);

                //结束登录界面
                finish();
            }

            //登录失败
            @Override
            public void failure(String failCode, String failMsg) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int progress = 0;
                        while (progress < 5) {
                            try {
                                Thread.sleep(1000);
                                progress++;
                                loginwait.incrementProgressBy(progress);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            loginwait.dismiss();
                        }
                    }
                }).start();

                String Msg = "";
                //10,00,0 解析失败结果码
                switch (failCode) {
                    case "10":
                        Msg = "密码错误";
                        break;
                    case "00":
                        Msg = "账号不存在";
                        break;
                    case "0":
                        Msg = "数据库错误";
                        break;
                }
                //显示登录失败原因
                setfaildialog(Msg);

//                Toast.makeText(MainActivity.this, Msg, Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void setfaildialog(String Msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("登录失败").setMessage(Msg).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }

    @Override
    public void onBackPressed() {
        if (lastBack == 0 || System.currentTimeMillis() - lastBack > 2000) {
            Toast.makeText(MainActivity.this, "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            lastBack = System.currentTimeMillis();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (loginwait != null) {
            loginwait.dismiss();
        }
        super.onDestroy();
    }
}
