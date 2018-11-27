package weidong.com.ys1106;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    private EditText Account;
    private EditText pass;

    private ProgressDialog loginwait = null;

    private long lastBack = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        findView();

        setClick();

        //判断是否已经登录
        if (AnalysisUtils.readLoginStatus(MainActivity.this)) {
            initWaitDialog("自动登录中...");
            Account.setText(AnalysisUtils.readloginUserName(MainActivity.this));
            pass.setText(AnalysisUtils.readloginpass(MainActivity.this, AnalysisUtils.readloginUserName(MainActivity.this)));
            login(Account.getText().toString(), pass.getText().toString());
        }

    }

    private void findView() {
        //绑定控件
        Account = findViewById(R.id.login_et_name);
        pass = findViewById(R.id.login_et_pass);
        mTvRegister = findViewById(R.id.register);
        mBtnLogin = findViewById(R.id.login);
    }

    private void setClick() {
        //注册界面
        mTvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        //登录事件
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //非空判断
                if (Account.getText().toString().isEmpty() ||
                        pass.getText().toString().isEmpty()) {
                    MyToast.MyToastShow(MainActivity.this, "用户名和密码不能为空！");
                } else {
                    //登录等待Dialog
                    initWaitDialog("登录中...");

                    login(Account.getText().toString(), pass.getText().toString().trim());
                }

            }
        });
    }

    private void initWaitDialog(String Msg) {
        loginwait = new ProgressDialog(MainActivity.this);
        loginwait.setMessage(Msg);
        loginwait.show();
    }

    //实现登录 用MySQL+servlet
    private void login(String account, String pass) {
        //请求数据集
        CommonRequest request = new CommonRequest();

        //标识 登录
        request.setRequestCode("1");

        //添加登录参数
        String Md5Psw = MD5Utils.ToMD5(pass);
        request.addRequestParam("account", account);
        request.addRequestParam("password", Md5Psw);

        //向服务端发送请求
        sendHttpPostRequst(Constant.URL_Login, request, new ResponseHandle() {
            //登录成功
            @Override
            public void success(CommonResponse response) {
                loginSuc();
            }

            //登录失败
            @Override
            public void failure(String failCode) {
                loginFail(failCode);
            }
        });

    }

    private void loginSuc() {
        dialogDely();

        //将用户信息保存
        AnalysisUtils.saveLoginStatus(MainActivity.this, true, Account.getText().toString());
        AnalysisUtils.addloginPass(MainActivity.this, pass.getText().toString(), Account.getText().toString());

        //成功之后跳转到首页
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);

        //结束登录界面
        finish();
    }

    private void loginFail(String msg) {
        dialogDely();
        //10,00,0 解析失败结果码
        String Msg = "";
        switch (msg) {
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
        setFailDialog(Msg);
    }

    private void dialogDely() {
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
    }


    private void setFailDialog(String Msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("登录失败").
                setMessage(Msg).
                setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
