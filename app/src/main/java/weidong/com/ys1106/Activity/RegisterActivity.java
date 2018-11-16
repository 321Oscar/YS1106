package weidong.com.ys1106.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import weidong.com.ys1106.MainActivity;
import weidong.com.ys1106.R;
import weidong.com.ys1106.Utils.AnalysisUtils;
import weidong.com.ys1106.Utils.CommonRequest;
import weidong.com.ys1106.Utils.CommonResponse;
import weidong.com.ys1106.Utils.Constant;
import weidong.com.ys1106.Utils.MD5Utils;
import weidong.com.ys1106.Utils.MyToast;
import weidong.com.ys1106.Utils.ResponseHandle;
import weidong.com.ys1106.Utils.UserInfo;

public class RegisterActivity extends BasicActivity {
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ImageView finish = findViewById(R.id.btn_finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button register = findViewById(R.id.btn_commit);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int res1 = getInfo();
                if (res1 == 1) {
                    register(userInfo);
                } else {
                    MyToast.MyToastShow(RegisterActivity.this, "填写有误！");
                }
            }
        });
    }

    //把界面上的信息绑定到Userinfo中
    private int getInfo() {
        userInfo = new UserInfo();
        EditText acc = findViewById(R.id.et_acc);
//        EditText name = findViewById(R.id.et_name);
        EditText pass = findViewById(R.id.et_pass);
//        EditText qq = findViewById(R.id.et_qq);
//        RadioGroup rg = findViewById(R.id.rg_sex);

        if (acc.getText().toString().isEmpty() || pass.getText().toString().isEmpty()) {
            return 0;
        } else {
            userInfo.setAccount(acc.getText().toString());
            //存入MD5加密之后的密码
            userInfo.setPass(MD5Utils.ToMD5(pass.getText().toString().trim()));
//            userInfo.setName(name.getText().toString());
//            userInfo.setQq(qq.getText().toString());
//            if (rg.getCheckedRadioButtonId() == R.id.rb_sex_1) {
//                userInfo.setSex("1");//男
//            } else if (rg.getCheckedRadioButtonId() == R.id.rb_sex_2) {
//                userInfo.setSex("0");
//            }
            return 1;
        }
    }

    private void register(UserInfo info) {
        //请求数据集
        CommonRequest request = new CommonRequest();
        //写入参数的顺序为，0账户名，1密码，2真实姓名，3性别，4年龄，5手机，6qq，7邮箱
        String[] params = new String[]{
                info.getAccount(),
                info.getPass()};
//                info.getName(),
//                info.getSex(),
//                info.getAge(),
//                info.getPh(),
//                info.getQq(),
//                info.getEmail()};
        for (int i = 0; i < 2; i++) {
            String code = Integer.toString(i);
            request.addRequestParam(code, params[i]);
        }
        request.setRequestCode("2");//标识注册

        final ProgressDialog wait1 = new ProgressDialog(RegisterActivity.this);
        wait1.setMessage("注册中...");

        sendHttpPostRequst(Constant.URL_Login, request, new ResponseHandle() {
            @Override
            public void success(CommonResponse response) {

                DelyWait(wait1);

                //成功之后跳转到首页
                save();
                Intent intent = new Intent(RegisterActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void failure(String failCode, String failMsg) {

                DelyWait(wait1);

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
                setfaildialog(Msg);
            }
        });

    }

    //基本信息存入本地
    private void save(){
        AnalysisUtils.saveLoginStatus(RegisterActivity.this,true,userInfo.getAccount());
        AnalysisUtils.addloginPass(RegisterActivity.this,userInfo.getPass(),userInfo.getAccount());
    }

    //progressDialog 延时
    private void DelyWait(final ProgressDialog wait){
        new Thread(new Runnable() {
            @Override
            public void run() {
                int progress = 0;
                while (progress < 5) {
                    try {
                        Thread.sleep(1000);
                        progress++;
                        wait.incrementProgressBy(progress);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    wait.dismiss();
                }
            }
        }).start();
    }

    private void setfaildialog(String Msg){
        AlertDialog.Builder builder= new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("登录失败").setMessage(Msg).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }
}
